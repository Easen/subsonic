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

import net.sourceforge.subsonic.Logger;
import net.sourceforge.subsonic.domain.MediaFile;
import net.sourceforge.subsonic.domain.Player;
import net.sourceforge.subsonic.service.MediaFileService;
import net.sourceforge.subsonic.service.PlayerService;
import net.sourceforge.subsonic.service.SettingsService;
import net.sourceforge.subsonic.util.StringUtil;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.MalformedURLException;

/**
 * Controller which produces the HLS (Http Live Streaming) playlist.
 *
 * @author Sindre Mehus
 */
public class HLSController implements Controller {

    private static final Logger LOG = Logger.getLogger(HLSController.class);
    private static final int SEGMENT_DURATION = 10;

    private SettingsService settingsService;
    private PlayerService playerService;
    private MediaFileService mediaFileService;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        int id = ServletRequestUtils.getIntParameter(request, "id");
        MediaFile mediaFile = mediaFileService.getMediaFile(id);
        if (mediaFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Media file not found: " + id);
            return null;
        }
        Integer duration = mediaFile.getDurationSeconds();
        if (duration == null || duration == 0) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown duration for media file: " + id);
            return null;
        }

        Player player = playerService.getPlayer(request, response);
        response.setContentType("application/vnd.apple.mpegurl");
        response.setCharacterEncoding(StringUtil.ENCODING_UTF8);

        PrintWriter writer = response.getWriter();
        writer.println("#EXTM3U");
        writer.println("#EXT-X-TARGETDURATION:" + SEGMENT_DURATION);

        for (int i = 0; i < duration / SEGMENT_DURATION; i++) {
            int offset = i * SEGMENT_DURATION;
            writer.println("#EXTINF:" + SEGMENT_DURATION + ",");
            writer.println(createStreamUrl(request, player, id, SEGMENT_DURATION, offset));
        }

        int remainder = duration % SEGMENT_DURATION;
        if (remainder > 0) {
            writer.println("#EXTINF:" + remainder + ",");
            int offset = duration - remainder;
            writer.println(createStreamUrl(request, player, id, remainder, offset));
        }
        writer.println("#EXT-X-ENDLIST");
        return null;
    }

    private String createStreamUrl(HttpServletRequest request, Player player, int id, int remainder, int offset) throws MalformedURLException {
        String url = request.getRequestURL().toString();
        url = url.replaceFirst("/rest/hls\\..*", "/stream?");
        url = url.replaceFirst("/hls\\..*", "/stream?");
        url += "id=" + id + "&hls=true&timeOffset=" + offset + "&player=" + player.getId() + "&duration=" + remainder;

        // Rewrite URLs in case we're behind a proxy.
        if (settingsService.isRewriteUrlEnabled()) {
            String referer = request.getHeader("referer");
            url = StringUtil.rewriteUrl(url, referer);
        }

        // Change protocol and port, if specified. (To make it work with players that don't support SSL.)
        int streamPort = settingsService.getStreamPort();
        if (streamPort != 0) {
            url = StringUtil.toHttpUrl(url, streamPort);
            LOG.info("Using non-SSL port " + streamPort + " in HLS playlist.");
        }

        return url;
    }

    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    public void setMediaFileService(MediaFileService mediaFileService) {
        this.mediaFileService = mediaFileService;
    }

    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }
}
