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
package net.sourceforge.subsonic.dao;

import net.sourceforge.subsonic.Logger;
import net.sourceforge.subsonic.domain.Album;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Provides database services for artists.
 *
 * @author Sindre Mehus
 */
public class AlbumDao extends AbstractDao {

    private static final Logger LOG = Logger.getLogger(AlbumDao.class);
    private static final String COLUMNS = "id, name, artist, song_count, duration_seconds, cover_art_path, " +
            "play_count, last_played, comment, created, last_scanned, present";

    private final RowMapper rowMapper = new AlbumMapper();

    /**
     * Returns the album with the given artist and album name.
     *
     * @param artistName The artist name.
     * @param albumName  The album name.
     * @return The album or null.
     */
    public Album getAlbum(String artistName, String albumName) {
        return queryOne("select " + COLUMNS + " from album where artist=? and name=?", rowMapper, artistName, albumName);
    }

    public Album getAlbum(int id) {
        return queryOne("select " + COLUMNS + " from album where id=?", rowMapper, id);
    }

    public List<Album> getAlbumsForArtist(String artist) {
        return query("select " + COLUMNS + " from album where artist=? and present order by name", rowMapper, artist);
    }

    /**
     * Creates or updates an album.
     *
     * @param album The album to create/update.
     */
    public synchronized void createOrUpdateAlbum(Album album) {
        String sql = "update album set " +
                "song_count=?," +
                "duration_seconds=?," +
                "cover_art_path=?," +
                "play_count=?," +
                "last_played=?," +
                "comment=?," +
                "created=?," +
                "last_scanned=?," +
                "present=? " +
                "where artist=? and name=?";

        int n = update(sql, album.getSongCount(), album.getDurationSeconds(), album.getCoverArtPath(), album.getPlayCount(), album.getLastPlayed(),
                album.getComment(), album.getCreated(), album.getLastScanned(), album.isPresent(), album.getArtist(), album.getName());

        if (n == 0) {

            update("insert into album (" + COLUMNS + ") values (" + questionMarks(COLUMNS) + ")", null, album.getName(), album.getArtist(),
                    album.getSongCount(), album.getDurationSeconds(), album.getCoverArtPath(), album.getPlayCount(), album.getLastPlayed(),
                    album.getComment(), album.getCreated(), album.getLastScanned(), album.isPresent());
        }

        int id = queryForInt("select id from album where artist=? and name=?", null, album.getArtist(), album.getName());
        album.setId(id);
    }

    /**
     * Returns albums in alphabetical order.
     *
     * @param offset Number of albums to skip.
     * @param count  Maximum number of albums to return.
     * @return Albums in alphabetical order.
     */
    public List<Album> getAlphabetialAlbums(int offset, int count) {
        return query("select " + COLUMNS + " from album where present order by artist, name limit ? offset ?", rowMapper, count, offset);
    }

    /**
     * Returns the most frequently played albums.
     *
     * @param offset Number of albums to skip.
     * @param count  Maximum number of albums to return.
     * @return The most frequently played albums.
     */
    public List<Album> getMostFrequentlyPlayedAlbums(int offset, int count) {
        return query("select " + COLUMNS + " from album where play_count > 0 and present " +
                "order by play_count desc limit ? offset ?", rowMapper, count, offset);
    }

    /**
     * Returns the most recently played albums.
     *
     * @param offset Number of albums to skip.
     * @param count  Maximum number of albums to return.
     * @return The most recently played albums.
     */
    public List<Album> getMostRecentlyPlayedAlbums(int offset, int count) {
        return query("select " + COLUMNS + " from album where last_played is not null and present " +
                "order by last_played desc limit ? offset ?", rowMapper, count, offset);
    }

    /**
     * Returns the most recently added albums.
     *
     * @param offset Number of albums to skip.
     * @param count  Maximum number of albums to return.
     * @return The most recently added albums.
     */
    public List<Album> getNewestAlbums(int offset, int count) {
        return query("select " + COLUMNS + " from album where present order by created desc limit ? offset ?",
                rowMapper, count, offset);
    }

    /**
     * Returns the most recently starred albums.
     *
     * @param offset   Number of albums to skip.
     * @param count    Maximum number of albums to return.
     * @param username Returns albums starred by this user.
     * @return The most recently starred albums for this user.
     */
    public List<Album> getStarredAlbums(int offset, int count, String username) {
        return query("select " + prefix(COLUMNS, "album") + " from album, starred_album where album.id = starred_album.album_id and " +
                "album.present and starred_album.username=? order by starred_album.created desc limit ? offset ?",
                rowMapper, username, count, offset);
    }

    public void markNonPresent(Date lastScanned) {
        int minId = queryForInt("select id from album where true limit 1", 0);
        int maxId = queryForInt("select max(id) from album", 0);

        final int batchSize = 1000;
        for (int id = minId; id <= maxId; id += batchSize) {
            update("update album set present=false where id between ? and ? and last_scanned != ? and present", id, id + batchSize, lastScanned);
        }
    }

    public void starAlbum(int albumId, String username) {
        unstarAlbum(albumId, username);
        update("insert into starred_album(album_id, username, created) values (?,?,?)", albumId, username, new Date());
    }

    public void unstarAlbum(int albumId, String username) {
        update("delete from starred_album where album_id=? and username=?", albumId, username);
    }

    public Date getAlbumStarredDate(int albumId, String username) {
        return queryForDate("select created from starred_album where album_id=? and username=?", null, albumId, username);
    }

    private static class AlbumMapper implements ParameterizedRowMapper<Album> {
        public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Album(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getInt(5),
                    rs.getString(6),
                    rs.getInt(7),
                    rs.getTimestamp(8),
                    rs.getString(9),
                    rs.getTimestamp(10),
                    rs.getTimestamp(11),
                    rs.getBoolean(12));
        }
    }
}
