package net.sourceforge.subsonic.domain;

import java.util.*;

/**
 * Represent a user.
 *
 * @author Sindre Mehus
 * @version $Revision: 1.3 $ $Date: 2006/01/12 15:22:51 $
 */
public class User {

    public static final String USERNAME_ADMIN = "admin";

    private String username;
    private String password;
    private long bytesStreamed;
    private long bytesDownloaded;
    private long bytesUploaded;
    private Locale locale;
    private String themeId;

    private boolean isAdminRole;
    private boolean isDownloadRole;
    private boolean isUploadRole;
    private boolean isPlaylistRole;
    private boolean isCoverArtRole;
    private boolean isCommentRole;

    public User(String username, String password, long bytesStreamed, long bytesDownloaded, long bytesUploaded,
                Locale locale, String theme) {
        this.username = username;
        this.password = password;
        this.bytesStreamed = bytesStreamed;
        this.bytesDownloaded = bytesDownloaded;
        this.bytesUploaded = bytesUploaded;
        this.locale = locale;
        this.themeId = theme;
    }

    public User(String username, String password) {
        this(username,  password, 0, 0, 0, null, null);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBytesStreamed() {
        return bytesStreamed;
    }

    public void setBytesStreamed(long bytesStreamed) {
        this.bytesStreamed = bytesStreamed;
    }

    public long getBytesDownloaded() {
        return bytesDownloaded;
    }

    public void setBytesDownloaded(long bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
    }

    public long getBytesUploaded() {
        return bytesUploaded;
    }

    public void setBytesUploaded(long bytesUploaded) {
        this.bytesUploaded = bytesUploaded;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    public boolean isAdminRole() {
        return isAdminRole;
    }

    public void setAdminRole(boolean isAdminRole) {
        this.isAdminRole = isAdminRole;
    }

    public boolean isCommentRole() {
        return isCommentRole;
    }

    public void setCommentRole(boolean isCommentRole) {
        this.isCommentRole = isCommentRole;
    }

    public boolean isDownloadRole() {
        return isDownloadRole;
    }

    public void setDownloadRole(boolean isDownloadRole) {
        this.isDownloadRole = isDownloadRole;
    }

    public boolean isUploadRole() {
        return isUploadRole;
    }

    public void setUploadRole(boolean isUploadRole) {
        this.isUploadRole = isUploadRole;
    }

    public boolean isPlaylistRole() {
        return isPlaylistRole;
    }

    public void setPlaylistRole(boolean isPlaylistRole) {
        this.isPlaylistRole = isPlaylistRole;
    }

    public boolean isCoverArtRole() {
        return isCoverArtRole;
    }

    public void setCoverArtRole(boolean isCoverArtRole) {
        this.isCoverArtRole = isCoverArtRole;
    }

    public String toString() {
        StringBuffer result = new StringBuffer(username);

        if (isAdminRole) {
            result.append(" [admin]");
        }
        if (isDownloadRole) {
            result.append(" [download]");
        }
        if (isUploadRole) {
            result.append(" [upload]");
        }
        if (isPlaylistRole) {
            result.append(" [playlist]");
        }
        if (isCoverArtRole) {
            result.append(" [coverart]");
        }
        if (isCommentRole) {
            result.append(" [comment]");
        }

        return result.toString();
    }
}
