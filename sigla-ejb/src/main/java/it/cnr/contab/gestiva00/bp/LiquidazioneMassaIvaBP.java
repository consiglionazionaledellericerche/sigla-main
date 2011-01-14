package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class LiquidazioneMassaIvaBP extends LiquidazioneIvaBP {

	private int status = SEARCH;
public LiquidazioneMassaIvaBP() {
	this("");
}
public LiquidazioneMassaIvaBP(String function) {
	super(function+"Tr");
}
public Liquidazione_massa_ivaVBulk aggiornaProspetti(ActionContext context,Liquidazione_massa_ivaVBulk bulk) throws BusinessProcessException {

	return bulk;
}
public Stampa_registri_ivaVBulk aggiornaRegistriStampati(
	ActionContext context,
	Stampa_registri_ivaVBulk model)
	throws Throwable {
	
	return model;
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Liquidazione_massa_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Liquidazione_massa_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Liquidazione_massa_ivaVBulk bulk = new Liquidazione_massa_ivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Liquidazione_massa_ivaVBulk)bulk.initializeForSearch(this,context);
		
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	//resetTabs();
	resetForSearch(context);
}
public boolean isBulkPrintable() {
	
	return false;
}
public boolean isPrintButtonEnabled() {

	return false;
}
public boolean isPrintButtonHidden() {

	return getPrintbp() == null;
}
public boolean isStartSearchButtonEnabled() {

	return true;
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}
