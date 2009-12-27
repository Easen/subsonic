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
package net.sourceforge.subsonic.backend.dao.schema;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Used for creating and evolving the database schema.
 * This class implementes the database schema for Subsonic Backend version 1.0.
 *
 * @author Sindre Mehus
 */
public class Schema10 extends Schema {
    private static final Logger LOG = Logger.getLogger(Schema10.class);

    public void execute(JdbcTemplate template) {

        /*
        Example row 1:

        id: 123
        principal: sindre@activeobjects.no
        redirect_from: sindre
        redirect_to: http://23.45.123.56:8080/subsonic
        trial: false
        trial_expires: null

        Example row 2:

        id: 124
        principal: 29384728372
        redirect_from: joe
        redirect_to: http://232.21.18.14/subsonic
        trial: true
        trial_expires: 2010-01-13 05:34:17
         */

        if (!tableExists(template, "redirection")) {
            LOG.info("Database table 'redirection' not found.  Creating it.");
            template.execute("create cached table redirection (" +
                             "id identity," +
                             "principal varchar not null," +
                             "redirect_from varchar not null," +
                             "redirect_to varchar not null," +
                             "trial boolean not null," +
                             "trial_expires datetime," +
                             "last_updated datetime," +
                             "last_read datetime," +
                             "unique(redirect_from))");
            template.execute("create index idx_redirection on redirection(redirect_from)");
            LOG.info("Database table 'redirection' was created successfully.");
        }
    }
}
