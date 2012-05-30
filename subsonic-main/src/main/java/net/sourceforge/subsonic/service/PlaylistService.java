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
package net.sourceforge.subsonic.service;

import net.sourceforge.subsonic.Logger;
import net.sourceforge.subsonic.dao.MediaFileDao;
import net.sourceforge.subsonic.dao.PlaylistDao;
import net.sourceforge.subsonic.domain.MediaFile;
import net.sourceforge.subsonic.domain.PlayQueue;
import net.sourceforge.subsonic.domain.Playlist;
import net.sourceforge.subsonic.util.StringUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.bcel.internal.classfile.ConstantString;

/**
 * Provides services for loading and saving playlists to and from persistent storage.
 *
 * @author Sindre Mehus
 * @see net.sourceforge.subsonic.domain.PlayQueue
 */
public class PlaylistService {

    private static final Logger LOG = Logger.getLogger(PlaylistService.class);
    private MediaFileService mediaFileService;
    private MediaFileDao mediaFileDao;
    private PlaylistDao playlistDao;

    public List<Playlist> getReadablePlaylistsForUser(String username) {
        return playlistDao.getReadablePlaylistsForUser(username);
    }

    public List<Playlist> getWritablePlaylistsForUser(String username) {
        return playlistDao.getWritablePlaylistsForUser(username);
    }

    public Playlist getPlaylist(int id) {
        return playlistDao.getPlaylist(id);
    }

    public List<String> getPlaylistUsers(int playlistId) {
        return  playlistDao.getPlaylistUsers(playlistId);
    }

    public List<MediaFile> getFilesInPlaylist(int id) {
        return mediaFileDao.getFilesInPlaylist(id);
    }

    public void setFilesInPlaylist(int id, List<MediaFile> files) {
        playlistDao.setFilesInPlaylist(id, files);
    }

    public void createPlaylist(Playlist playlist) {
        playlistDao.createPlaylist(playlist);
    }

    public void addPlaylistUser(int playlistId, String username) {
        playlistDao.addPlaylistUser(playlistId, username);
    }

    public void deletePlaylistUser(int playlistId, String username) {
        playlistDao.deletePlaylistUser(playlistId, username);
    }

    public boolean isReadAllowed(Playlist playlist, String username) {
        if (username == null) {
            return false;
        }
        if (username.equals(playlist.getUsername()) || playlist.isPublic()) {
            return true;
        }
        return playlistDao.getPlaylistUsers(playlist.getId()).contains(username);
    }

    public boolean isWriteAllowed(Playlist playlist, String username) {
        return username != null && username.equals(playlist.getUsername());
    }

    public void deletePlaylist(int id) {
        playlistDao.deletePlaylist(id);
    }

    public void updatePlaylist(Playlist playlist) {
        playlistDao.updatePlaylist(playlist);
    }

    public Playlist importPlaylist(String username, String playlistName, String format, InputStream inputStream) throws Exception {
        PlaylistFormat playlistFormat = PlaylistFormat.getPlaylistFormat(format);
        if (playlistFormat == null) {
            throw new Exception("Unsupported playlist format: " + format);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StringUtil.ENCODING_UTF8));
        List<MediaFile> files = playlistFormat.parse(reader, mediaFileService);
        if (files.isEmpty()) {
            throw new Exception("No songs in the playlist were found.");
        }

        Date now = new Date();
        Playlist playlist = new Playlist();
        playlist.setUsername(username);
        playlist.setCreated(now);
        playlist.setChanged(now);
        playlist.setPublic(true);
        playlist.setName(playlistName);

        createPlaylist(playlist);
        setFilesInPlaylist(playlist.getId(), files);

        return playlist;
    }

    public void setPlaylistDao(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    public void setMediaFileDao(MediaFileDao mediaFileDao) {
        this.mediaFileDao = mediaFileDao;
    }

    public void setMediaFileService(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    /**
     * Abstract superclass for playlist formats.
     */
    private abstract static class PlaylistFormat {
        public abstract List<MediaFile> parse(BufferedReader reader, MediaFileService mediaFileService) throws IOException;

        public abstract void savePlaylist(PlayQueue playQueue, PrintWriter writer) throws IOException;

        public static PlaylistFormat getPlaylistFormat(String format) {
            if (format == null) {
                return null;
            }
            if (format.equalsIgnoreCase("m3u") || format.equalsIgnoreCase("m3u8")) {
                return new M3UFormat();
            }
            if (format.equalsIgnoreCase("pls")) {
                return new PLSFormat();
            }
            if (format.equalsIgnoreCase("xspf")) {
                return new XSPFFormat();
            }
            return null;
        }

        protected MediaFile getMediaFile(MediaFileService mediaFileService, String path) {
            try {
                MediaFile file = mediaFileService.getMediaFile(path);
                if (file.exists()) {
                    return file;
                } else {
                    LOG.warn("File not found: " + path);
                }
            } catch (SecurityException x) {
                LOG.warn(x.getMessage(), x);
            }
            return null;
        }
    }

    /**
     * Implementation of M3U playlist format.
     */
    private static class M3UFormat extends PlaylistFormat {
        public List<MediaFile> parse(BufferedReader reader, MediaFileService mediaFileService) throws IOException {
            List<MediaFile> result = new ArrayList<MediaFile>();
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("#")) {
                    MediaFile file = getMediaFile(mediaFileService, line);
                    if (file != null) {
                        result.add(file);
                    }
                }
                line = reader.readLine();
            }
            return result;
        }

        public void savePlaylist(PlayQueue playQueue, PrintWriter writer) throws IOException {
            writer.println("#EXTM3U");
            for (MediaFile file : playQueue.getFiles()) {
                writer.println(file.getPath());
            }
            if (writer.checkError()) {
                throw new IOException("Error when writing playlist");
            }
        }
    }

    /**
     * Implementation of PLS playlist format.
     */
    private static class PLSFormat extends PlaylistFormat {
        public List<MediaFile> parse(BufferedReader reader, MediaFileService mediaFileService) throws IOException {
            List<MediaFile> result = new ArrayList<MediaFile>();

            Pattern pattern = Pattern.compile("^File\\d+=(.*)$");
            String line = reader.readLine();
            while (line != null) {

                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    MediaFile file = getMediaFile(mediaFileService, matcher.group(1));
                    if (file != null) {
                        result.add(file);
                    }
                }
                line = reader.readLine();
            }
            return result;
        }

        public void savePlaylist(PlayQueue playQueue, PrintWriter writer) throws IOException {
            writer.println("[playlist]");
            int counter = 0;

            for (MediaFile file : playQueue.getFiles()) {
                counter++;
                writer.println("File" + counter + '=' + file.getPath());
            }
            writer.println("NumberOfEntries=" + counter);
            writer.println("Version=2");

            if (writer.checkError()) {
                throw new IOException("Error when writing playlist.");
            }
        }
    }

    /**
     * Implementation of XSPF (http://www.xspf.org/) playlist format.
     */
    private static class XSPFFormat extends PlaylistFormat {
        public List<MediaFile> parse(BufferedReader reader, MediaFileService mediaFileService) throws IOException {
            List<MediaFile> result = new ArrayList<MediaFile>();

            SAXBuilder builder = new SAXBuilder();
            Document document;
            try {
                document = builder.build(reader);
            } catch (JDOMException x) {
                LOG.warn("Failed to parse XSPF playlist.", x);
                throw new IOException("Failed to parse XSPF playlist.");
            }

            Element root = document.getRootElement();
            Namespace ns = root.getNamespace();
            Element trackList = root.getChild("trackList", ns);
            List<?> tracks = trackList.getChildren("track", ns);

            for (Object obj : tracks) {
                Element track = (Element) obj;
                String location = track.getChildText("location", ns);
                if (location != null && location.startsWith("file://")) {
                    location = location.replaceFirst("file://", "");
                    MediaFile file = getMediaFile(mediaFileService, location);
                    if (file != null) {
                        result.add(file);
                    }
                }
            }
            return result;
        }

        public void savePlaylist(PlayQueue playQueue, PrintWriter writer) throws IOException {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<playlist version=\"1\" xmlns=\"http://xspf.org/ns/0/\">");
            writer.println("    <trackList>");

            for (MediaFile file : playQueue.getFiles()) {
                writer.println("        <track><location>file://" + StringEscapeUtils.escapeXml(file.getPath()) + "</location></track>");
            }
            writer.println("    </trackList>");
            writer.println("</playlist>");

            if (writer.checkError()) {
                throw new IOException("Error when writing playlist.");
            }
        }
    }
}
