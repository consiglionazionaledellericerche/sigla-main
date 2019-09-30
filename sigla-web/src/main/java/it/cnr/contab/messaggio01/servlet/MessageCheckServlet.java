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

package it.cnr.contab.messaggio01.servlet;

import it.cnr.jada.persistency.sql.LoggableStatement;

import javax.servlet.http.*;

/**
 * Insert the type's description here.
 * Creation date: (10/09/2002 12:07:15)
 * @author: CNRADM
 */
public class MessageCheckServlet extends javax.servlet.http.HttpServlet {
	private int refresh;
/**
 * MessageCheckServlet constructor comment.
 */
public MessageCheckServlet() {
	super();
}
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
	try {
		String user = request.getParameter("user");
		String server_url = request.getParameter("server_url");
		if (user != null && server_url != null) {
			if (refresh > 0)
				response.setHeader("Refresh",String.valueOf(refresh));
			java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();
			try {
				String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
				String sql = "SELECT MIN(PRIORITA) FROM "+schema+"MESSAGGIO WHERE ( CD_UTENTE IS NULL OR CD_UTENTE = ? ) AND ( SERVER_URL IS NULL OR SERVER_URL = ? ) AND ( DT_INIZIO_VALIDITA IS NULL OR DT_INIZIO_VALIDITA < SYSDATE ) AND ( DT_FINE_VALIDITA IS NULL OR SYSDATE < DT_FINE_VALIDITA+1 ) AND ( PG_MESSAGGIO > ( SELECT PG_MESSAGGIO FROM "+schema+"MESSAGGIO_NOTIFICATO WHERE MESSAGGIO_NOTIFICATO.CD_UTENTE = ? ) OR NOT EXISTS ( SELECT 1 FROM "+schema+"MESSAGGIO_NOTIFICATO WHERE MESSAGGIO_NOTIFICATO.CD_UTENTE = ? ) ) AND NOT EXISTS ( SELECT 1 FROM "+schema+"MESSAGGIO_LETTO WHERE MESSAGGIO_LETTO.PG_MESSAGGIO = MESSAGGIO.PG_MESSAGGIO AND MESSAGGIO_LETTO.CD_UTENTE = ? )";
				LoggableStatement stm = new LoggableStatement(conn,sql,true,this.getClass());
				try {
					stm.setString(1,user);
					stm.setString(2,server_url);
					stm.setString(3,user);
					stm.setString(4,user);
					stm.setString(5,user);
					java.sql.ResultSet rs = stm.executeQuery();
					try {
						if (rs.next()) {
							int priorita = rs.getInt(1);
							if (!rs.wasNull())
								request.setAttribute("it.cnr.contab.messaggio00.servlet.MessageCheckServlet.priorita",new Integer(priorita));
						}
					} finally {
						try{rs.close();}catch( java.sql.SQLException e ){};
					}
				} finally {
					try{stm.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				conn.close();
			}
		}
	} catch(Throwable e) {
	}
	request.getRequestDispatcher(request.getServletPath()+".jsp").forward(request,response);
}
public void init(javax.servlet.ServletConfig config) throws javax.servlet.ServletException {
	super.init(config);
	try {
		it.cnr.jada.util.Config.setServletConfig(getServletConfig());
		refresh = Integer.parseInt(it.cnr.jada.util.Config.getHandler().getProperty("Message_check.refresh"));
	} catch(NumberFormatException e) {
		refresh = 60;
	}
}
}
