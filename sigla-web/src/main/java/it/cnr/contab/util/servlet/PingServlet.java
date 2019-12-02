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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;

import it.cnr.contab.util00.comp.IPingMgr;
import it.cnr.contab.util00.ejb.*;
import it.cnr.jada.ejb.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Servlet di PING per il check di attivazione del server
 */

public class PingServlet extends HttpServlet {
public PingServlet() {
	super();
}

/**
 * Invocato da client per il PING di verifica attivazione del server applicativo.
 * Effettua le seguenti operazioni:
 *    1. Verifica la validità dell'UTENTE specificato sulla form
 *    2. Invoca il metodo ping sulla componente di ping
 *    3. Ritorna lo stato di attivazione del server
 *
 * @param request Http Request
 * @param response Http Response
 */
 
protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
	String tipo = request.getParameter("tipo");
	response.setHeader("Cache-Control","private"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	try {
		    Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
		    conn.setAutoCommit(false);
		    conn.commit();
		    conn.close();
		    PingComponentSession ping = (PingComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTIL00_EJB_PingComponentSession");
			String hostname="";
			try {
				InetAddress addr = InetAddress.getLocalHost();
				//byte[] ipAddr = addr.getAddress();
				hostname = addr.getHostName();
			} catch (UnknownHostException e) {
			}
			Integer tipoPing;
			if (tipo != null && tipo.equalsIgnoreCase("login"))
				tipoPing = new Integer(IPingMgr.TIPO_PING_LOGIN_ATTIVO.intValue());
			else
				tipoPing = new Integer(IPingMgr.TIPO_PING_SERVER_ATTIVO.intValue());
		    if (ping.ping(hostname, tipoPing)) {
			    response.getWriter().println("OK");
			    return;
		    }
		    response.getWriter().println("Errore interno dell'EJB server.");
		    response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
		    return;
	} catch(java.sql.SQLException e) {
		    if (e.getErrorCode() == 1017) {
			    response.getWriter().println("Richiesta non autorizzata: password errata");
				response.setStatus(response.SC_UNAUTHORIZED);
		    } else {
			    response.getWriter().println("Errore SQL: ");
			    response.getWriter().println(e);
			    response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
			}
	} catch(Throwable e) {
		    response.getWriter().println("Errore interno del server");
		    response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
		    return;
	}
}
}