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

package it.cnr.contab.gestiva00.bp;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.gestiva00.core.bulk.IPrintable;
import it.cnr.contab.gestiva00.core.bulk.Report_statoBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.gestiva00.ejb.StampaRegistriIvaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class StampaRegistriIvaBP extends BulkBP {
    private int status= INSERT;
	private Unita_organizzativaBulk uoSrivania;

	private final SimpleDetailCRUDController registri_stampati = new SimpleDetailCRUDController("Registri stampati", Report_statoBulk.class,"registri_stampati",this);
	
public StampaRegistriIvaBP() {
	super();
}
public StampaRegistriIvaBP(String function) {
	super(function);
}
public void aggiornaRegistriStampati(ActionContext context)
	throws Throwable {
	
	Stampa_registri_ivaVBulk model = (Stampa_registri_ivaVBulk)getModel();
	aggiornaRegistriStampati(context, model);
}
public Stampa_registri_ivaVBulk aggiornaRegistriStampati(
	ActionContext context,
	Stampa_registri_ivaVBulk model)
	throws Throwable {
	
	if (isBulkReprintable(model)) {
		getRegistri_stampati().reset(context);
		
		BulkList stampeEseguite = new BulkList();
//		if (model.getTipo_sezionale() != null)
			stampeEseguite = createComponentSession().findRegistriStampati(
																	context.getUserContext(),
																	model);
		model.setRegistri_stampati(stampeEseguite);
	}

	return model;
}
/**
 * Crea una componente di sessione
 * 
 *
 * @return 
 * @throws EJBException	Se si verifica qualche eccezione applicativa per cui non è possibile effettuare l'operazione
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public StampaRegistriIvaComponentSession createComponentSession()
	throws javax.ejb.EJBException,
			java.rmi.RemoteException,
			BusinessProcessException {
	return (StampaRegistriIvaComponentSession)createComponentSession("CNRGESTIVA00_EJB_StampaRegistriIvaComponentSession",StampaRegistriIvaComponentSession.class);
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;

	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.ristampa");

	return toolbar;
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(
    it.cnr.jada.action.ActionContext actionContext,
    it.cnr.jada.persistency.sql.CompoundFindClause clauses,
    it.cnr.jada.bulk.OggettoBulk bulk,
    it.cnr.jada.bulk.OggettoBulk context,
    java.lang.String property)
    throws it.cnr.jada.action.BusinessProcessException {
    try {
        return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext, createComponentSession().cerca(actionContext.getUserContext(), clauses, bulk, context, property));
    } catch (Exception e) {
        throw new it.cnr.jada.action.BusinessProcessException(e);
    }
}
/**
 * Insert the method's description here.
 * Creation date: (12/4/2002 3:47:50 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getRegistri_stampati() {
	return registri_stampati;
}
public int getStatus() {
	return status;
}
/**
 * Restituisce il valore della proprietà 'bringbackButtonEnabled'
 *
 * @return Il valore della proprietà 'bringbackButtonEnabled'
 */
public boolean isBringbackButtonEnabled() {
	return isSearching();
}
/**
 * Restituisce il valore della proprietà 'searchButtonHidden'
 *
 * @return Il valore della proprietà 'searchButtonHidden'
 */
public boolean isBulkPrintable() {
	
	return getModel() != null && isBulkPrintable((Stampa_registri_ivaVBulk)getModel());
}
/**
 * Restituisce il valore della proprietà 'searchButtonHidden'
 *
 * @return Il valore della proprietà 'searchButtonHidden'
 */
protected boolean isBulkPrintable(Stampa_registri_ivaVBulk stampaBulk) {
	
	return stampaBulk instanceof IPrintable;
}
/**
 * Restituisce il valore della proprietà 'searchButtonHidden'
 *
 * @return Il valore della proprietà 'searchButtonHidden'
 */
public boolean isBulkReprintable() {
	
	return isBulkReprintable((Stampa_registri_ivaVBulk)getModel());
}
/**
 * Restituisce il valore della proprietà 'searchButtonHidden'
 *
 * @return Il valore della proprietà 'searchButtonHidden'
 */
protected boolean isBulkReprintable(Stampa_registri_ivaVBulk stampaBulk) {
	
	return isBulkPrintable(stampaBulk) && ((IPrintable)stampaBulk).isRistampabile();
}
/**
 * Restituisce il valore della proprietà 'bringbackButtonEnabled'
 *
 * @return Il valore della proprietà 'bringbackButtonEnabled'
 */
public boolean isReprintButtonEnabled() {
	
	return isBulkReprintable();
}
/**
 * Restituisce il valore della proprietà 'bringbackButtonEnabled'
 *
 * @return Il valore della proprietà 'bringbackButtonEnabled'
 */
public boolean isReprintButtonHidden() {
	
	return !isBulkReprintable();
}
/**
 * Restituisce il valore della proprietà 'searchButtonHidden'
 *
 * @return Il valore della proprietà 'searchButtonHidden'
 */
public boolean isSearchButtonHidden() {
	return isSearching();
}
public boolean isSearching() {
	return status == SEARCH;
}
/**
 * Restituisce il valore della proprietà 'startSearchButtonHidden'
 *
 * @return Il valore della proprietà 'startSearchButtonHidden'
 */
public boolean isStartSearchButtonEnabled() {
	return isBulkPrintable();
}
/**
 * Restituisce il valore della proprietà 'startSearchButtonHidden'
 *
 * @return Il valore della proprietà 'startSearchButtonHidden'
 */
public boolean isStartSearchButtonHidden() {
	return !isSearching();
}
/**
 * nessuna conferma di chiusura senza salvataggio
 */
public void setDirty(boolean newDirty) {	
}
/**
 * Imposta il valore della proprietà 'status'
 *
 * @param newStatus	Il valore da assegnare a 'status'
 */
public void setStatus(int newStatus) {
	status = newStatus;
}
public Unita_organizzativaBulk getUoSrivania() {
	return uoSrivania;
}
private void setUoSrivania(Unita_organizzativaBulk uoSrivania) {
	this.uoSrivania = uoSrivania;
}
@Override
protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
	super.init(config, actioncontext);
	setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
}
public boolean isUoEnte(){
	return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
}
	public void doOnMeseChange(ActionContext context) throws BusinessProcessException {
		if (this.isBulkReprintable())
			this.getRegistri_stampati().reset(context);
		setDataDaA(context, (Stampa_registri_ivaVBulk) this.getModel());
	}

	protected void setDataDaA(ActionContext context, Stampa_registri_ivaVBulk stampaBulk) {
		int esercizio = stampaBulk.getEsercizio().intValue();
		int meseIndex = ((Integer)stampaBulk.getMesi_int().get(stampaBulk.getMese())).intValue();
		java.util.GregorianCalendar gc = (java.util.GregorianCalendar)java.util.GregorianCalendar.getInstance();
		gc.set(java.util.Calendar.HOUR, 0);
		gc.set(java.util.Calendar.MINUTE, 0);
		gc.set(java.util.Calendar.SECOND, 0);
		gc.set(java.util.Calendar.MILLISECOND, 0);
		gc.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		gc.set(java.util.Calendar.DAY_OF_MONTH, 1);
		gc.set(java.util.Calendar.YEAR, esercizio);

		gc.set(java.util.Calendar.MONTH, meseIndex-1);
		stampaBulk.setData_da(new Timestamp(gc.getTime().getTime()));
		gc.set(java.util.Calendar.MONTH, meseIndex);
		gc.add(java.util.Calendar.DAY_OF_MONTH, -1);
		stampaBulk.setData_a(new Timestamp(gc.getTime().getTime()));
	}
}
