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
package net.sourceforge.subsonic.domain;

import java.io.*;

/**
 * Represents a top level directory in which music or other media is stored.
 *
 * @author Sindre Mehus
 * @version $Revision: 1.1 $ $Date: 2005/11/27 14:32:05 $
 */
public class MusicFolder {

    private Integer id;
    private File path;
    private String name;
    private boolean isEnabled;

    /**
     * Creates a new music folder.
     * @param id The system-generated ID.
     * @param path The path of the music folder.
     * @param name The user-defined name.
     * @param enabled Whether the folder is enabled.
     */
    public MusicFolder(Integer id, File path, String name, boolean enabled) {
        this.id = id;
        this.path = path;
        this.name = name;
        isEnabled = enabled;
    }

    /**
     * Creates a new music folder.
     * @param path The path of the music folder.
     * @param name The user-defined name.
     * @param enabled Whether the folder is enabled.
     */
    public MusicFolder(File path, String name, boolean enabled) {
        this(null, path, name, enabled);
    }

    /**
     * Returns the system-generated ID.
     * @return The system-generated ID.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns the path of the music folder.
     * @return The path of the music folder.
     */
    public File getPath() {
        return path;
    }

    /**
     * Sets the path of the music folder.
     * @param path The path of the music folder.
     */
    public void setPath(File path) {
        this.path = path;
    }

    /**
     * Returns the user-defined name.
     * @return The user-defined name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the user-defined name.
     * @param name The user-defined name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns whether the folder is enabled.
     * @return Whether the folder is enabled.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * Sets whether the folder is enabled.
     * @param enabled Whether the folder is enabled.
     */
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

}