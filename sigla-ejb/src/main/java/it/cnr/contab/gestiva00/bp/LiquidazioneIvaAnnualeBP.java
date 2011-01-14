package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public abstract class LiquidazioneIvaAnnualeBP extends StampaRegistriIvaBP {

	private int status = INSERT;

	private final SimpleDetailCRUDController elenco = new SimpleDetailCRUDController(
		"Elenco", Vp_liquid_iva_annualeBulk.class,"elenco",this);

public LiquidazioneIvaAnnualeBP() {
	this("");
}
public LiquidazioneIvaAnnualeBP(String function) {
	super(function+"Tr");
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Liquidazione_iva_annualeVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public abstract Liquidazione_iva_annualeVBulk createNewBulk(ActionContext context) throws BusinessProcessException;
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 15.38.54)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getElenco() {
	return elenco;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetForSearch(context);
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public abstract Liquidazione_iva_annualeVBulk manage(
	ActionContext context,
	Liquidazione_iva_annualeVBulk bulk) 
	throws BusinessProcessException;
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		setDirty(false);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
}
