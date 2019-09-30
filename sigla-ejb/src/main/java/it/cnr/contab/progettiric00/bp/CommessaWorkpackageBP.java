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

/*
 * Created on Oct 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.config00.ejb.Linea_attivitaComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaComponentSession;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaModuloComponentSession;
import it.cnr.contab.progettiric00.ejb.ProgettoRicercaPadreComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;


public class CommessaWorkpackageBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController crudWorkpackage_disponibili = new SimpleDetailCRUDController("workpackage_disponibili",WorkpackageBulk.class,"workpackage_disponibili",this);
	private final SimpleDetailCRUDController crudWorkpackage_collegati = new SimpleDetailCRUDController("workpackage_collegati",WorkpackageBulk.class,"workpackage_collegati",this);
	
public CommessaWorkpackageBP() throws BusinessProcessException {
	super();
	initWPTable();
}
public CommessaWorkpackageBP( String function ) throws BusinessProcessException {
	super( function );
	initWPTable();
}
public static ProgettoRicercaPadreComponentSession getProgettoRicercaPadreComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
	return (ProgettoRicercaPadreComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession",ProgettoRicercaPadreComponentSession.class);
}	
private void aggiornaGECO(UserContext userContext) {
	try {
		getProgettoRicercaPadreComponentSession().aggiornaGECO(userContext);
	} catch (Exception e) {
		String text = "Errore interno del Server Utente:"+CNRUserContext.getUser(userContext);
		SendMail.sendErrorMail(text,e.toString());
	}
}

@Override
protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
	aggiornaGECO(actioncontext.getUserContext());
	super.initialize(actioncontext);
}

protected void init(it.cnr.jada.action.Config config, ActionContext actioncontext)
		throws BusinessProcessException
{
	super.init(config, actioncontext);
}

public void cercaWorkpackages(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		ProgettoBulk commessa = (ProgettoBulk)getModel();
		commessa.resetWorkpackages();
		if (commessa.getPg_progetto() != null)
				setModel(context,((ProgettoRicercaComponentSession)createComponentSession()).cercaWorkpackages(context.getUserContext(),commessa));
	} catch(Exception e) {
		throw handleException(e);
	}
}
public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws BusinessProcessException{
	try
{
	return EJBCommonServices.openRemoteIterator(actioncontext, ((ProgettoRicercaModuloComponentSession)createComponentSession()).cercaModuliForWorkpackage(actioncontext.getUserContext(), compoundfindclause, oggettobulk));
}
catch(Exception exception)
{
	throw handleException(exception);
}
}

public void updateWorkpackages(it.cnr.jada.UserContext userContext, BulkList wp) throws it.cnr.jada.action.BusinessProcessException {

	try{
		Linea_attivitaComponentSession LAcomponent = (Linea_attivitaComponentSession)createComponentSession("CNRCONFIG00_EJB_Linea_attivitaComponentSession", Linea_attivitaComponentSession.class);
		for (java.util.Iterator i = wp.iterator();i.hasNext();) 
		{
			OggettoBulk obj = (OggettoBulk) i.next();
			if (obj.isToBeUpdated()) {
				LAcomponent.modificaConBulk(userContext, obj);
			}
		}
	} catch (it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}


public SimpleDetailCRUDController getCrudWorkpackage_disponibili() {
	return crudWorkpackage_disponibili;
}

public SimpleDetailCRUDController getCrudWorkpackage_collegati() {
	return crudWorkpackage_collegati;
}

private void initWPTable() {
	crudWorkpackage_disponibili.setPaged(true);
	crudWorkpackage_disponibili.setPageSize(10);
	crudWorkpackage_collegati.setPaged(true);
	crudWorkpackage_collegati.setPageSize(10);
}

protected Button[] createToolbar()
  {
	  Button abutton[] = new Button[4];
	  int i = 0;
	  //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.search");
	  //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.startSearch");
	  //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.freeSearch");
	  //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.new");
	  abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.save");
	  //abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.delete");
	  abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.bringBack");
	  abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.print");
	  abutton[i++] = new Button(Config.getHandler().getProperties(getClass()), "CRUDToolbar.undoBringBack");
	  return abutton;
  }

}
