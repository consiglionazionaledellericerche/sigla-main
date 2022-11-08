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

import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.http.HttpStatus;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Servlet implementation class GenericDownload
 */
public class GenericDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private transient final static Logger LOGGER = LoggerFactory.getLogger(GenericDownloadServlet.class);
    private static final String AUTHORIZATION = "Authorization";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenericDownloadServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
        final String nodeRef = request.getParameter("nodeRef");
        if (nodeRef != null) {
            final String header = request.getHeader(AUTHORIZATION);
            UsernamePasswordCredentials customCredentials = Optional.ofNullable(getCredentials(header))
                    .orElseThrow(() -> new ServletException("AUTHORIZATION not found!"));
            StorageObject storageObject = storeService.getStorageObjectBykey(nodeRef, customCredentials);
            /**
             * Controllo se sto richiamando un curriculum più vecchio di 3 anni
             */
            LOGGER.info("Try to GET content of node {} with AUTHORIZATION {}", nodeRef, header);
            InputStream is = storeService.getResource(storageObject);
            response.setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
            response.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
            OutputStream os = response.getOutputStream();
            response.setDateHeader("Expires", 0);
            byte[] buffer = new byte[response.getBufferSize()];
            int buflength;
            while ((buflength = is.read(buffer)) > 0) {
                os.write(buffer, 0, buflength);
            }
            is.close();
            os.flush();
        } else {
            HttpActionContext actionContext = new HttpActionContext(this, request, response);
            try {
                final BusinessProcess businessProcess = Optional.ofNullable(BusinessProcess.getBusinessProcess(request)).orElseThrow(() -> {
                    response.setStatus(HttpStatus.SC_UNAUTHORIZED);
                    return new ServletException("AUTHORIZATION not found!");
                });
                if (request.getParameter("methodName") != null)
                    Introspector.invoke(businessProcess, request.getParameter("methodName"), actionContext);
            } catch (NoSuchMethodException e) {
                throw new ServletException(e);
            } catch (IllegalAccessException e) {
                throw new ServletException(e);
            } catch (InvocationTargetException e) {
                throw new ServletException(e.getTargetException().getMessage(), e);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private UsernamePasswordCredentials getCredentials(String authorization) {

        if (authorization != null) {
            String[] values = authorization.split(" ");

            if (values.length == 2 && values[0].equals("Basic")) {

                String base64 = values[1];
                String decoded = new String(
                        DatatypeConverter.parseBase64Binary(base64));

                int sep = decoded.indexOf(':');
                LOGGER.debug("decoded value: " + decoded);

                String username = decoded.substring(0, sep);
                String password = decoded.substring(sep + 1);

                LOGGER.info("username: " + username);
                LOGGER.info("password: " + password);

                return new UsernamePasswordCredentials(username,
                        password);


            } else {
                LOGGER.info("Problemi con Authorization Header: " + authorization);
            }
        } else {
            LOGGER.info("authorization is null");
        }

        return null;
    }

}
