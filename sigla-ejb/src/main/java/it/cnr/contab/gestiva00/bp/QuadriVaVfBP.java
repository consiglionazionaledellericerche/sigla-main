package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class QuadriVaVfBP extends QuadriLiqAnnualeBP {

	private int status = INSERT;
public QuadriVaVfBP() {
	this("");
}
public QuadriVaVfBP(String function) {
	super(function+"Tr");
}
public Liquidazione_iva_annualeVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Quadri_va_vfVBulk bulk = new Quadri_va_vfVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return (Liquidazione_iva_annualeVBulk)bulk.initializeForSearch(this,context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk manage(it.cnr.jada.action.ActionContext context, it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	try {
		return createComponentSession().tabCodIvaAcquisti(context.getUserContext(), bulk);
	} catch (Throwable t) {
		throw handleException(t);
	}	
}
}
