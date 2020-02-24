/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.util.servlet;

import it.cnr.contab.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * @author Marco Spasiano
 * @version 1.0
 */
public class UtilServlet extends HttpServlet {
    private transient final static Logger logger = LoggerFactory.getLogger(UtilServlet.class);

    public void init()
            throws ServletException {
        Utility.loadPersistentInfos();
        String version = "01.001.000";
        InputStream is = getServletContext().getResourceAsStream(Utility.MANIFEST_PATH);
        if (is != null) {
            try {
                Manifest manifest = new Manifest(is);
                Attributes attributes = manifest.getMainAttributes();

                version = attributes.getValue("Implementation-Version");
            } catch (IOException e) {
                logger.warn("IOException", e);
            }
        }
        String APPLICATION_TITLE = Utility.APPLICATION_TITLE;
        String APPLICATION_VERSION = "Documenti contabili/amministrativi transact. " + version;
        String APPLICATION_TITLE_VERSION = APPLICATION_TITLE + " - " + APPLICATION_VERSION;

        getServletContext().setAttribute("VERSION", version);
        getServletContext().setAttribute("APPLICATION_TITLE", APPLICATION_TITLE);
        getServletContext().setAttribute("APPLICATION_VERSION", APPLICATION_VERSION);
        getServletContext().setAttribute("APPLICATION_TITLE_VERSION", APPLICATION_TITLE_VERSION);
    }

}
