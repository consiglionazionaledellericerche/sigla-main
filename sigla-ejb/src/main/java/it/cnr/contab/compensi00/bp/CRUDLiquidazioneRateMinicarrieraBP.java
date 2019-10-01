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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 11.39.09)
 * @author: Roberto Fantino
 */
public class CRUDLiquidazioneRateMinicarrieraBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final SimpleDetailCRUDController rateCRUDController = new SimpleDetailCRUDController("rateCRUDController",Liquidazione_rate_minicarrieraBulk.class,"rate",this, true){
		@Override
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context,	boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException, javax.servlet.ServletException
		{
			super.writeHTMLToolbar(context, reset, find, delete, false);
			writeExtraHTMLToolbar(context);
			super.closeButtonGROUPToolbar(context);
		}
	};
/**
 * LiquidazioneRateMinicarrieraBP constructor comment.
 */
public CRUDLiquidazioneRateMinicarrieraBP() {
	super();
}
/**
 * LiquidazioneRateMinicarrieraBP constructor comment.
 * @param function java.lang.String
 */
public CRUDLiquidazioneRateMinicarrieraBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2002 18.04.30)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.new");
	return toolbar;
}
public void doLiquidaRate(ActionContext context) throws BusinessProcessException {

	try{

		Liquidazione_rate_minicarrieraBulk bulk = (Liquidazione_rate_minicarrieraBulk)getModel();
		if (bulk.getCdElementoVoce()==null)
			throw new ApplicationException("E' necessario selezionare l'elemento voce.");
		if (bulk.getCdLineaAttivita()==null)
			throw new ApplicationException("E' necessario selezionare un GAE.");
			
		java.util.List rateDaLiquidare = getRateCRUDController().getSelectedModels(context);
		if (rateDaLiquidare == null || rateDaLiquidare.isEmpty())
			throw new ApplicationException("E' necessario selezionare le rate da liquidare.");

		LiquidazioneRateMinicarrieraComponentSession sess = (LiquidazioneRateMinicarrieraComponentSession)createComponentSession();
		sess.liquidaRate(context.getUserContext(), (Liquidazione_rate_minicarrieraBulk)getModel(), rateDaLiquidare);

		setDirty(false);
		setEditable(false);
		setStatus(VIEW);
		getRateCRUDController().getSelection().clear();
		getRateCRUDController().setModelIndex(context, -1);

	}catch(it.cnr.jada.bulk.ValidationException ex){
		throw handleException(ex);
	}catch(ApplicationException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}

	
}
public Voce_fBulk findVoceF(ActionContext context) throws BusinessProcessException{

	try{
			LiquidazioneRateMinicarrieraComponentSession sess = (LiquidazioneRateMinicarrieraComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCOMPENSI00_EJB_LiquidazioneRateMinicarrieraComponentSession", LiquidazioneRateMinicarrieraComponentSession.class);
			return sess.findVoceF(context.getUserContext(), (Liquidazione_rate_minicarrieraBulk)getModel());

	}catch(ComponentException ex){
		throw handleException(ex);
	}catch(javax.ejb.EJBException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (02/01/2003 14.58.50)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getRateCRUDController() {
	return rateCRUDController;
}
public void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
}
public boolean isNewButtonEnabled() {
	return super.isNewButtonEnabled() || isViewing();
}
public void reset(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.reset(context);
	setStatus(SEARCH);
}
/**
 * Insert the method's description here.
 * Creation date: (22/02/2002 18.28.25)
 */
public void resetTabs(ActionContext context) {
	setTab("tab","tabFiltroRate");
}
public void writeExtraHTMLToolbar(javax.servlet.jsp.PageContext context) throws javax.servlet.ServletException, java.io.IOException{

	if(isViewing())
		it.cnr.jada.util.jsp.JSPUtils.toolbarButton(context, "img/history16.gif",null,false,"Liquida rate",
				HttpActionContext.isFromBootstrap(context));
	else
		it.cnr.jada.util.jsp.JSPUtils.toolbarButton(context, "img/history16.gif","javascript:submitForm('doLiquidaRate')",false,"Liquida rate",
				HttpActionContext.isFromBootstrap(context));
}
}
