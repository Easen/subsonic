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
package net.sourceforge.subsonic.controller;

import net.sourceforge.subsonic.command.*;
import net.sourceforge.subsonic.domain.*;
import net.sourceforge.subsonic.service.*;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.mvc.*;
import org.springframework.web.servlet.view.*;

import javax.servlet.http.*;
import java.util.Date;

/**
 * Controller for saving the play queue as a playlist.
 *
 * @author Sindre Mehus
 */
public class SavePlaylistController extends SimpleFormController {

    private PlaylistService playlistService;
    private PlayerService playerService;
    private SecurityService securityService;

    public ModelAndView onSubmit(Object comm) throws Exception {
        SavePlaylistCommand command = (SavePlaylistCommand) comm;
        PlayQueue playQueue = command.getPlayQueue();

        Playlist playlist = new Playlist();
        playlist.setName(command.getName());
        playlist.setCreated(new Date());
        playlist.setPublic(false);
        playlist.setUsername(command.getUsername());

        playlistService.createPlaylist(playlist);
        playlistService.setSongsInPlaylist(playlist.getId(), playQueue.getFiles());

        playQueue.setName(command.getName());
        return new ModelAndView(new RedirectView(getSuccessView()));
    }

    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Player player = playerService.getPlayer(request, null);
        return new SavePlaylistCommand(player.getPlayQueue(), securityService.getCurrentUsername(request));
    }

    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
}
