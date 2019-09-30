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

package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.pdg00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

public class CostiStipendialiMensiliBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
/**
 * CostiStipendialiMensiliBP constructor comment.
 */
public CostiStipendialiMensiliBP() {
	super();
}
/**
 * CostiStipendialiMensiliBP constructor comment.
 * @param function java.lang.String
 */
public CostiStipendialiMensiliBP(String function) throws BusinessProcessException {
	super(function);
	try {
		table.setMultiSelection(true);
		setPageSize(12);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Crea il riferimento alla componente CNRPDG00_EJB_CostiDipendenteComponentSession
 *
 * @return Remote interface della componente
 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public CostiDipendenteComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
	return (CostiDipendenteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession",CostiDipendenteComponentSession.class);
}
public it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.ripartizione");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.generaScrittura");
//	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.annullaScrittura");
	return toolbar;
}
public String getFormTitle() {
	return "Costi stipendiali mensili";
}
public V_cdp_stato_mensilitaBulk getStato_mensilita() {
	return (V_cdp_stato_mensilitaBulk)getModel();
}
protected void init(Config config,ActionContext context) throws BusinessProcessException {
	super.init(config,context);
	try {
		setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_cdp_stato_mensilitaBulk.class));
		refresh(context);
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public boolean isAnnullaScritturaButtonEnabled() {
	//return 
		//getStato_mensilita() != null &&
		//getStato_mensilita().getFl_stato_scarico().booleanValue();
	// Ritorno sempre true perchè deve essere possibile fare una selezione
	// multipla
	return true;
}
public boolean isGeneraScritturaButtonEnabled() {
	//return 
		//getStato_mensilita() != null &&
		//!getStato_mensilita().getFl_stato_scarico().booleanValue();
	// Ritorno sempre true perchè deve essere possibile fare una selezione
	// multipla
	return true;
}
public boolean isRipartizioneButtonEnabled() {
	return 
		getStato_mensilita() != null &&
		!getStato_mensilita().getFl_stato_scarico().booleanValue();
}
public void refresh(ActionContext context) throws BusinessProcessException {
	try {
		setIterator(context,createComponentSession().listaCdp_analitica(context.getUserContext()));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
