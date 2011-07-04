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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import net.sourceforge.subsonic.domain.MusicFile;
import net.sourceforge.subsonic.domain.Share;
import net.sourceforge.subsonic.service.MusicFileService;
import net.sourceforge.subsonic.service.SettingsService;
import net.sourceforge.subsonic.service.ShareService;

/**
 * Controller for sharing music on Twitter, Facebook etc.
 *
 * @author Sindre Mehus
 */
public class ShareManagementController extends MultiActionController {

    private MusicFileService musicFileService;
    private SettingsService settingsService;
    private ShareService shareService;

    public ModelAndView createShare(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dir = request.getParameter("dir");

        List<MusicFile> files = getMusicFiles(request);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("urlRedirectionEnabled", settingsService.isUrlRedirectionEnabled());
        map.put("dir", musicFileService.getMusicFile(dir));
        Share share = shareService.createShare(request, files);
        map.put("playUrl", shareService.getShareUrl(share));

        return new ModelAndView("createShare", "model", map);
    }

    private List<MusicFile> getMusicFiles(HttpServletRequest request) throws IOException {
        MusicFile dir = musicFileService.getMusicFile(request.getParameter("dir"));
        int[] songIndexes = ServletRequestUtils.getIntParameters(request, "i");
        if (songIndexes.length == 0) {
            return Arrays.asList(dir);
        }

        List<MusicFile> children = dir.getChildren(true, true, true);
        List<MusicFile> result = new ArrayList<MusicFile>();
        for (int songIndex : songIndexes) {
            result.add(children.get(songIndex));
        }

        return result;
    }

    public void setMusicFileService(MusicFileService musicFileService) {
        this.musicFileService = musicFileService;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void setShareService(ShareService shareService) {
        this.shareService = shareService;
    }
}