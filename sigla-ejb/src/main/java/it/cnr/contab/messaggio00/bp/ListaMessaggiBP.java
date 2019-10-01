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

package it.cnr.contab.messaggio00.bp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.ejb.CRUDMessaggioComponentSession;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

/**
 * Insert the type's description here. Creation date: (10/09/2002 16:36:21)
 * 
 * @author: CNRADM
 */
public class ListaMessaggiBP extends
		it.cnr.jada.util.action.SelezionatoreListaBP {
	private static final long serialVersionUID = 1L;
	private java.lang.String server_url;

	/**
	 * ListaMessaggiBP constructor comment.
	 */
	public ListaMessaggiBP() {
		super();
		table.setMultiSelection(true);
	}

	/**
	 * ListaMessaggiBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public ListaMessaggiBP(String function) {
		super(function);
		table.setMultiSelection(true);
	}

	/**
	 * ListaMessaggiBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public ListaMessaggiBP(String function, String server_url) {
		this(function);
		this.server_url = server_url;
	}

	public GestioneLoginComponentSession createComponentSession()
			throws BusinessProcessException {
		return (GestioneLoginComponentSession) createComponentSession(
				"CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession",
				GestioneLoginComponentSession.class);
	}

	public it.cnr.jada.util.jsp.Button[] createNavigatorToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[6];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()),
				"Navigator.previousFrame");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Navigator.previous");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Navigator.next");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Navigator.nextFrame");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.remove");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config
				.getHandler().getProperties(getClass()), "Toolbar.removeAll");
		return toolbar;
	}

	/**
	 * Insert the method's description here. Creation date: (11/09/2002
	 * 17:18:46)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getServer_url() {
		return server_url;
	}

	protected void init(it.cnr.jada.action.Config config,
			it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		setBulkInfo(it.cnr.jada.bulk.BulkInfo
				.getBulkInfo(it.cnr.contab.messaggio00.bulk.MessaggioBulk.class));
		setColumns(getBulkInfo().getColumnFieldPropertyDictionary("view"));
		refresh(context);
		super.init(config, context);
	}

	public boolean isInputReadonly() {
		return true;
	}

	public void refresh(ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			setIterator(context,
					it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
							context,
							createComponentSession().listaMessaggi(
									context.getUserContext(), server_url)));
			reset(context);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public void reset(ActionContext context) throws BusinessProcessException {
		super.reset(context);
		setModel(context, null);
	}

	public it.cnr.jada.util.action.Selection saveSelection(
			it.cnr.jada.action.ActionContext context)
			throws BusinessProcessException {
		setModel(context, null);
		return super.saveSelection(context);
	}

	/**
	 * Insert the method's description here. Creation date: (11/09/2002
	 * 17:18:46)
	 * 
	 * @param newServer_url
	 *            java.lang.String
	 */
	public void setServer_url(java.lang.String newServer_url) {
		server_url = newServer_url;
	}

	/*
	 * Nel momento in cui l'utente naviga sul record viene memorizzato nella
	 * tabella Messaggio_Visionato
	 */
	public int setFocus(ActionContext actioncontext)
			throws BusinessProcessException {
		int appoFocus = super.setFocus(actioncontext);
		MessaggioBulk messaggio = ((MessaggioBulk) getModel());
		if (!messaggio.getVisionato().booleanValue()) {
			try {
				CRUDMessaggioComponentSession messaggioComponentSession = (CRUDMessaggioComponentSession) createComponentSession(
						"CNRMESSAGGIO00_EJB_CRUDMessaggioComponentSession",
						CRUDMessaggioComponentSession.class);
				messaggioComponentSession.setMessaggioVisionato(
						actioncontext.getUserContext(), messaggio);
				messaggio.setVisionato(Boolean.TRUE);
				this.getIterator().refresh();
			} catch (Throwable e) {
				throw handleException(e);
			}
		}
		return appoFocus;
	}

	/*
	 * Il campo, anche se non di tabella, viene reso ordinabile in quanto il
	 * metodo doSort() dentro il BP gestisce la richiesta
	 */
	public boolean isOrderableBy(String s) {
		if (s.equals("iconaOpenClose"))
			return true;
		return super.isOrderableBy(s);
	}
}
