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

import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.SessionTraceBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.ejb.HttpEJBCleaner;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Optional;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpireSessionServlet extends HttpServlet implements Serializable,HttpSessionListener {
	@SuppressWarnings("unchecked")
	public  final static Hashtable<String,HttpSession> sessionObjects = new Hashtable();
	private transient final static Logger log = LoggerFactory.getLogger(ExpireSessionServlet.class);
	
	public ExpireSessionServlet() {
		super();
	}
	@Override
	public void init() throws ServletException {
		super.init();
		new Thread(new ExpireThread()).start();	
	}

	class ExpireThread implements Runnable {
		public void run()
		{
			while (true){
			  try{
				Thread.sleep(1000*60);
				expireSession();
			  }
			  catch(Throwable e){
			  }
			}				
		}
		ExpireThread()
		{
		}
	}
    private void expireSession(){
    	for (Enumeration sessions = sessionObjects.elements();sessions.hasMoreElements();){
            long timeNow;
            timeNow = System.currentTimeMillis();
    		HttpSession session = (HttpSession)sessions.nextElement();
    		int maxInactiveInterval;
    		maxInactiveInterval = session.getMaxInactiveInterval();
            if(maxInactiveInterval < 0)
                continue;
            int timeIdle = (int)((timeNow - session.getLastAccessedTime()) / 1000L);
            if(timeIdle < maxInactiveInterval)
                continue;
    		try {
    			SessionTraceBulk sessionTrace = (SessionTraceBulk)createCRUDComponentSession().inizializzaBulkPerModifica(new CNRUserContext("SESSIONTRACE",session.getId(),null,null,null,null), new SessionTraceBulk(session.getId()));
    			sessionTrace.setToBeDeleted();
    			createCRUDComponentSession().eliminaConBulk(new CNRUserContext("SESSIONTRACE",session.getId(),null,null,null,null), sessionTrace);
    		} catch (Exception e) {
    		}
    		HttpEJBCleaner httpejbcleaner = (HttpEJBCleaner)session.getAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner");
    		if (httpejbcleaner != null)
    			httpejbcleaner.remove();
            sessionObjects.remove(session.getId());
            session.invalidate();
    	}
    }
	public void sessionCreated(HttpSessionEvent se) {
		try {
			SessionTraceBulk sessionTrace = new SessionTraceBulk();
			sessionTrace.setId_sessione(se.getSession().getId());
			sessionTrace.setServer_url(" ");
			sessionTrace.setToBeCreated();
			createCRUDComponentSession().creaConBulk(new CNRUserContext("SESSIONTRACE",se.getSession().getId(),null,null,null,null), sessionTrace);
		} catch (Exception e) {
		}
		sessionObjects.put(se.getSession().getId(),se.getSession());
	}
	public void sessionDestroyed(HttpSessionEvent se) {
		UserContext userContext = (UserContext) se.getSession().getAttribute("UserContext");
		try {
			Optional.ofNullable(userContext)
					.ifPresent(userContext1 -> {
						try {
							StringBuffer infoUser = new StringBuffer();
							infoUser.append("LogOut User:"+userContext1.getUser());
							log.warn(infoUser.toString());
							createGestioneLoginComponentSession().unregisterUser(userContext1);
						} catch (ComponentException|RemoteException e) {
							log.error("", e);
						}
					});
			SessionTraceBulk sessionTrace = (SessionTraceBulk)createCRUDComponentSession().inizializzaBulkPerModifica(new CNRUserContext("SESSIONTRACE",se.getSession().getId(),null,null,null,null), new SessionTraceBulk(se.getSession().getId()));
			sessionTrace.setToBeDeleted();
			createCRUDComponentSession().eliminaConBulk(new CNRUserContext("SESSIONTRACE",se.getSession().getId(),null,null,null,null), sessionTrace);
		} catch (CRUDException e) {
		} catch (Exception e) {
			log.error("Delete SESSIONTRACE failed", e);
		}
		HttpEJBCleaner httpejbcleaner = (HttpEJBCleaner)se.getSession().getAttribute("it.cnr.jada.util.ejb.HttpEJBCleaner");
		if (httpejbcleaner != null)
			httpejbcleaner.remove();
		sessionObjects.remove(se.getSession().getId());
	}
	public CRUDComponentSession createCRUDComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}

	public GestioneLoginComponentSession createGestioneLoginComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession");
	}
}
