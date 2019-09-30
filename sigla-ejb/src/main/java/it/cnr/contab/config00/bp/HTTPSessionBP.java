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

package it.cnr.contab.config00.bp;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;

import it.cnr.contab.utenze00.bulk.SessionTraceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

public class HTTPSessionBP extends ConsultazioniBP {

	public HTTPSessionBP() {
		super();
	}
	
	public HTTPSessionBP(String s) {
		super(s);
	}
	@Override
	public Vector addButtonsToToolbar(Vector listButton) {
		Button terminaSessioni = new Button();
		terminaSessioni.setImg("img/delete24.gif");
		terminaSessioni.setDisabledImg("img/delete24.gif");
		terminaSessioni.setLabel("<u>T</u>ermina sessioni selezionate");
		terminaSessioni.setHref("javascript:submitForm('doEliminaSessioni')");
		terminaSessioni.setStyle("width:100px");
		terminaSessioni.setTitle("Termina sessioni selezionate");
		terminaSessioni.setAccessKey("T");
		terminaSessioni.setSeparator(true);

		Button aggiorna = new Button();
		aggiorna.setImg("img/find24.gif");
		aggiorna.setDisabledImg("img/find24.gif");
		aggiorna.setLabel("<u>A</u>ggiorna");
		aggiorna.setHref("javascript:submitForm('doAggiorna')");
		aggiorna.setStyle("width:100px");
		aggiorna.setTitle("Aggiorna");
		aggiorna.setAccessKey("A");
		aggiorna.setSeparator(true);
		
		listButton.add(aggiorna);
		listButton.add(terminaSessioni);
		return super.addButtonsToToolbar(listButton);
	}

	@SuppressWarnings("unchecked")
	public void disconnettiSessioni(ActionContext actioncontext) throws BusinessProcessException{		
		BulkList httpSessions = new BulkList(getSelectedElements(actioncontext));
		for (Iterator<SessionTraceBulk> iterator = httpSessions.iterator(); iterator.hasNext();) {
			SessionTraceBulk httpSession = iterator.next();
			if (!httpSession.getId_sessione().equalsIgnoreCase(actioncontext.getUserContext().getSessionId())){
				try {
					URL url = new URL(httpSession.getServer_url());
					url.openConnection().getContent();
				}catch(Exception e){
					httpSession.setToBeDeleted();
					try {
						createComponentSession().eliminaConBulk(actioncontext.getUserContext(), httpSession);
					} catch (ComponentException e1) {
						handleException(e1);
					} catch (RemoteException e1) {
						handleException(e1);
					} catch (EJBException e1) {
						handleException(e1);
					}
				}			
			}
		}
	}
}
