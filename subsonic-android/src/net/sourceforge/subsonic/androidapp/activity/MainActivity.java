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

package net.sourceforge.subsonic.androidapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import net.sourceforge.subsonic.androidapp.R;
import net.sourceforge.subsonic.androidapp.service.DownloadServiceImpl;
import net.sourceforge.subsonic.androidapp.service.MusicServiceFactory;
import net.sourceforge.subsonic.androidapp.util.BackgroundTask;
import net.sourceforge.subsonic.androidapp.util.Constants;
import net.sourceforge.subsonic.androidapp.util.Pair;
import net.sourceforge.subsonic.androidapp.util.Util;

import java.util.List;

public class MainActivity extends OptionsMenuActivity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        startService(new Intent(this, DownloadServiceImpl.class));
        setContentView(R.layout.main);

        View browseButton = findViewById(R.id.main_browse);
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setOffline(MainActivity.this, false);
                startActivity(new Intent(MainActivity.this, SelectArtistActivity.class));
            }
        });

        View browseOfflineButton = findViewById(R.id.main_browse_offline);
        browseOfflineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setOffline(MainActivity.this, true);
                startActivity(new Intent(MainActivity.this, SelectArtistActivity.class));
            }
        });

        View searchButton = findViewById(R.id.main_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setOffline(MainActivity.this, false);
                showSearchDialog();
            }
        });

        View loadPlaylistButton = findViewById(R.id.main_load_playlist);
        loadPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.setOffline(MainActivity.this, false);
                showPlaylistDialog();
            }
        });

        View nowPlayingButton = findViewById(R.id.main_now_playing);
        nowPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DownloadActivity.class));
            }
        });

        View settingsButton = findViewById(R.id.main_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        View helpButton = findViewById(R.id.main_help);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
            }
        });

        String version = Util.getVersion(this);
        if (version != null) {
            TextView versionText = (TextView) findViewById(R.id.main_version);
            versionText.setText("v " + version);
        }
    }

    private void showPlaylistDialog() {
        new BackgroundTask<List<Pair<String, String>>>(this) {
            @Override
            protected List<Pair<String, String>> doInBackground() throws Throwable {
                return MusicServiceFactory.getMusicService(MainActivity.this).getPlaylists(MainActivity.this, this);
            }

            @Override
            protected void cancel() {
                // Do nothing.
            }

            @Override
            protected void done(final List<Pair<String, String>> result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.main_select_playlist);
                builder.setCancelable(true);

                if (result.isEmpty()) {
                    builder.setMessage(R.string.main_no_playlists);
                } else {
                    final CharSequence[] items = new CharSequence[result.size()];
                    for (int i = 0; i < items.length; i++) {
                        items[i] = result.get(i).getSecond();
                    }
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int button) {
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, SelectAlbumActivity.class);
                            intent.putExtra(Constants.INTENT_EXTRA_NAME_PLAYLIST_ID, result.get(button).getFirst());
                            intent.putExtra(Constants.INTENT_EXTRA_NAME_PLAYLIST_NAME, result.get(button).getSecond());
                            startActivity(intent);
                        }
                    });
                }
                builder.show();
            }
        }.execute();
    }
}