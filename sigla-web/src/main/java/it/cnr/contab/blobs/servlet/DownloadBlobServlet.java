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

package it.cnr.contab.blobs.servlet;


import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.blobs.bulk.Bframe_blobKey;
import it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk;
import it.cnr.jada.blobs.bulk.Bframe_blob_tipoKey;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.ejb.EJBCommonServices;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;

public class DownloadBlobServlet extends HttpServlet {
    public DownloadBlobServlet() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpActionContext context = new HttpActionContext(this, request, response);
        try {
            Connection conn = EJBCommonServices.getConnection(context);
            try {
                InputStream is;
                int bufferSize;
                String fullpath = request.getPathInfo();
                HomeCache homeCache = new HomeCache(conn);
                String tipo = fullpath.substring(1, fullpath.indexOf('/', 1));

                String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
                String path = fullpath.substring(fullpath.indexOf('/', 1) + 1, fullpath.lastIndexOf('/') + 1);
                BulkHome home = (BulkHome) homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blobBulk.class);
                Bframe_blob_tipoBulk blob_tipo = (Bframe_blob_tipoBulk) homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findByPrimaryKey(new Bframe_blob_tipoKey(tipo));
                if (blob_tipo == null) {
                    response.sendError(404, fullpath);
                    return;
                }
                Bframe_blobBulk blob_bulk = (Bframe_blobBulk) home.findByPrimaryKey(new Bframe_blobKey(tipo, filename, path));
                if (blob_bulk == null) {
                    response.sendError(404, fullpath);
                    return;
                }
                if (blob_bulk.getStato() != null && !"S".equals(blob_bulk.getStato())) {
                    response.sendError(404, fullpath);
                    return;
                }
                if (blob_tipo.getFl_binario().booleanValue()) {
                    Blob blob = home.getSQLBlob(blob_bulk, "BDATA");
                    bufferSize = Long.valueOf(blob.length()).intValue();
                    response.setBufferSize(bufferSize);
                    String contentType = getServletContext().getMimeType(filename);
                    if (contentType != null)
                        response.setContentType(contentType);
                    else
                        response.setContentType("www/unknown");
                    long length = blob.length();
                    if (length > 0L && length < 0x7fffffffL)
                        response.setContentLength((int) length);
                    is = blob.getBinaryStream();
                } else {
                    Clob clob = home.getSQLClob(blob_bulk, "CDATA");
                    bufferSize = Long.valueOf(clob.length()).intValue();
                    response.setBufferSize(bufferSize);
                    String contentType = getServletContext().getMimeType(filename);
                    if (contentType != null)
                        response.setContentType(contentType);
                    else
                        response.setContentType("www/unknown");
                    long length = clob.length();
                    //if(length > 0L && length < 0x7fffffffL)
                    response.setContentLength((int) length);
                    is = clob.getAsciiStream();
                }
                response.setDateHeader("Last-Modified", blob_bulk.getDuva().getTime());

                OutputStream os = response.getOutputStream();
                try {
                    byte buffer[] = new byte[bufferSize];
                    int size;
                    while ((size = is.read(buffer)) > 0)
                        os.write(buffer, 0, size);
                } catch (SocketException ex) {
                } finally {
                    is.close();
                }
            } finally {
                conn.commit();
                conn.close();
                conn = null;
            }
        } catch (Throwable _ex) {
            response.sendError(500);
        }
    }
}
