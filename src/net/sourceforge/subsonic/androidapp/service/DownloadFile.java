/*
 This file is part of Subsonic.

 Subsonic is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Subsonic is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Subsonic.  If not, see <http://www.gnu.org/licenses/>.

 Copyright 2009 (C) Sindre Mehus
 */
package net.sourceforge.subsonic.androidapp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import net.sourceforge.subsonic.androidapp.domain.MusicDirectory;
import net.sourceforge.subsonic.androidapp.util.CancellableTask;
import net.sourceforge.subsonic.androidapp.util.FileUtil;
import net.sourceforge.subsonic.androidapp.util.Util;

/**
 * @author Sindre Mehus
 * @version $Id$
 */
public class DownloadFile {

    private static final String TAG = DownloadFile.class.getSimpleName();
    private final Context context;
    private final Handler handler;
    private final MusicDirectory.Entry song;
    private final File partialFile;
    private final File completeFile;
    private final File saveFile;

    private final MediaStoreService mediaStoreService;
    private CancellableTask downloadTask;
    private boolean save;

    public DownloadFile(Context context, Handler handler, MusicDirectory.Entry song, boolean save) {
        this.context = context;
        this.handler = handler;
        this.song = song;
        this.save = save;
        saveFile = FileUtil.getSongFile(song, true);
        partialFile = new File(saveFile.getPath() + ".partial");
        completeFile = new File(saveFile.getPath() + ".complete");
        mediaStoreService = new MediaStoreService(context);
    }

    public MusicDirectory.Entry getSong() {
        return song;
    }

    public synchronized void download() {
        downloadTask = new DownloadTask();
        downloadTask.start();
    }

    public synchronized void cancelDownload() {
        if (downloadTask != null) {
            downloadTask.cancel();
        }
    }

    public boolean isSaved() {
        return saveFile.exists();
    }

    public File getCompleteFile() {
        if (saveFile.exists()) {
            return saveFile;
        }

        if (completeFile.exists()) {
            return completeFile;
        }

        return saveFile;
    }

    public File getPartialFile() {
        return partialFile;
    }

    public boolean isComplete() {
        return saveFile.exists() || completeFile.exists();
    }

    public boolean isDone() {
        return saveFile.exists() || (completeFile.exists() && !save);
    }

    public void delete() {
        cancelDownload();
        Util.delete(partialFile);
        Util.delete(completeFile);
        Util.delete(saveFile);
        mediaStoreService.deleteFromMediaStore(this);
    }

    public boolean cleanup() {
        boolean ok = true;
        if (partialFile.exists()) {
            ok = Util.delete(partialFile);
        }
        if (saveFile.exists() && completeFile.exists()) {
            ok &= Util.delete(completeFile);
        }
        return ok;
    }

    private class DownloadTask extends CancellableTask {

        @Override
        public void execute() {

            Log.i(TAG, "Starting to download " + song);
            InputStream in = null;
            FileOutputStream out = null;
            try {

                if (saveFile.exists()) {
                    Log.i(TAG, saveFile + " already exists. Skipping.");
                    return;
                }
                if (completeFile.exists()) {
                    if (save) {
                        Util.atomicCopy(completeFile, saveFile);
                    } else {
                        Log.i(TAG, completeFile + " already exists. Skipping.");
                    }
                    return;
                }

                MusicService musicService = MusicServiceFactory.getMusicService(context);
                in = musicService.getDownloadInputStream(context, song);
                out = new FileOutputStream(partialFile);
                long n = copy(in, out);
                Log.i(TAG, "Downloaded " + n + " bytes to " + partialFile);
                out.flush();
                out.close();

                Util.atomicCopy(partialFile, completeFile);
                if (save) {
                    Util.atomicCopy(partialFile, saveFile);
                    mediaStoreService.saveInMediaStore(DownloadFile.this);
                }
                if (isCancelled()) {
                    throw new Exception("Download of " + song + " was cancelled");
                }

            } catch (Exception x) {
                Util.close(out);
                Util.delete(partialFile);
                Util.delete(completeFile);
                Util.delete(saveFile);
                if (!isCancelled()) {
                    String msg = "Error downloading \"" + song.getTitle() + "\"";
                    Util.showErrorNotification(context, handler, msg, x);
                    Log.e(TAG, msg, x);
                }

            } finally {
                Util.close(in);
                Util.close(out);
            }
        }

        private long copy(InputStream in, OutputStream out) throws IOException, InterruptedException {
            byte[] buffer = new byte[1024 * 16];
            long count = 0;
            int n;
            long lastLog = System.currentTimeMillis();

            while (!isCancelled() && (n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
                count += n;

                long now = System.currentTimeMillis();
                if (now - lastLog > 2000L) {  // Only every so often.
                    Log.i(TAG, "Downloaded " + Util.formatBytes(count) + " of " + song);
                    lastLog = now;
                }
            }
            return count;
        }
    }
}
