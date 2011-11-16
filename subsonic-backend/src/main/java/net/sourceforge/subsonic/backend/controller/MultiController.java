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
package net.sourceforge.subsonic.backend.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.subsonic.backend.dao.PaymentDao;
import net.sourceforge.subsonic.backend.service.WhitelistGenerator;
import org.apache.log4j.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.dao.DataAccessException;
import net.sourceforge.subsonic.backend.dao.DaoHelper;
import net.sourceforge.subsonic.backend.Util;
import net.sourceforge.subsonic.backend.service.EmailSession;

/**
 * Multi-controller used for simple pages.
 *
 * @author Sindre Mehus
 */
public class MultiController extends MultiActionController {

    private static final Logger LOG = Logger.getLogger(RedirectionController.class);

    private static final String SUBSONIC_VERSION = "4.5";
    private static final String SUBSONIC_BETA_VERSION = "4.6.beta2";

    private static final Date LICENSE_DATE_THRESHOLD;

    private DaoHelper daoHelper;

    private PaymentDao paymentDao;
    private WhitelistGenerator whitelistGenerator;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.DAY_OF_MONTH, 19);
        LICENSE_DATE_THRESHOLD = calendar.getTime();
    }

    public ModelAndView version(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String localVersion = request.getParameter("v");
        LOG.info(request.getRemoteAddr() + " asked for latest version. Local version: " + localVersion);

        PrintWriter writer = response.getWriter();

        writer.println("SUBSONIC_VERSION_BEGIN" + SUBSONIC_VERSION + "SUBSONIC_VERSION_END");
        writer.println("SUBSONIC_FULL_VERSION_BEGIN" + SUBSONIC_VERSION + "SUBSONIC_FULL_VERSION_END");
        writer.println("SUBSONIC_BETA_VERSION_BEGIN" + SUBSONIC_BETA_VERSION + "SUBSONIC_BETA_VERSION_END");

        return null;
    }

    public ModelAndView validateLicense(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String email = request.getParameter("email");
        Long date = ServletRequestUtils.getLongParameter(request, "date");

        boolean valid = isLicenseValid(email, date);
        LOG.info(request.getRemoteAddr() + " asked to validate license for " + email + ". Result: " + valid);

        PrintWriter writer = response.getWriter();
        writer.println(valid);

        return null;
    }

    public ModelAndView sendMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String subject = request.getParameter("subject");
        String text = request.getParameter("text");

        EmailSession session = new EmailSession();
        session.sendMessage(from, Arrays.asList(to), null, null, null, subject, text);

        LOG.info("Sent email on behalf of " + request.getRemoteAddr() + " to " + to + " with subject '" + subject + "'");

        return null;
    }

    public ModelAndView db(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String password = ServletRequestUtils.getRequiredStringParameter(request, "p");
        if (!password.equals(Util.getPassword("backendpwd.txt"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("p", password);
        String query = request.getParameter("query");
        if (query != null) {
            map.put("query", query);

            try {
                List<?> result = daoHelper.getJdbcTemplate().query(query, new ColumnMapRowMapper());
                map.put("result", result);
            } catch (DataAccessException x) {
                map.put("error", ExceptionUtils.getRootCause(x).getMessage());
            }
        }

        return new ModelAndView("backend/db", "model", map);
    }

    public ModelAndView whitelist(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String password = ServletRequestUtils.getRequiredStringParameter(request, "p");
        if (!password.equals(Util.getPassword("backendpwd.txt"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        Date newerThan = MultiController.LICENSE_DATE_THRESHOLD;
        Integer days = ServletRequestUtils.getIntParameter(request, "days");
        if (days != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -days);
            newerThan = cal.getTime();
        }
        whitelistGenerator.generate(newerThan);
        return null;
    }

    private boolean isLicenseValid(String email, Long date) {
        if (email == null || date == null) {
            return false;
        }

        if (paymentDao.isBlacklisted(email)) {
            return false;
        }

        // Always accept licenses that are older than 2010-06-19.
        if (date < LICENSE_DATE_THRESHOLD.getTime()) {
            return true;
        }

        return paymentDao.getPaymentByEmail(email) != null || paymentDao.isWhitelisted(email);
    }

    public void setDaoHelper(DaoHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    public void setPaymentDao(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public void setWhitelistGenerator(WhitelistGenerator whitelistGenerator) {
        this.whitelistGenerator = whitelistGenerator;
    }
}
