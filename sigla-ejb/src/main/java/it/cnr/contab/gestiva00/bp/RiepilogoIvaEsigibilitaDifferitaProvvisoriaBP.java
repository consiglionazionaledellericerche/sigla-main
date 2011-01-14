package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP extends RiepilogoIvaEsigibilitaDifferitaBP {

	private int status = INSERT;
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 2:05:58 PM)
 */
public RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP() {

	this("");
}
public RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP(String function) {
	super(function+"Tr");
}
public Riepilogo_iva_esigibilita_differitaVBulk createNewBulk(
	ActionContext context)
	throws BusinessProcessException {
		
	try {
		Riepilogo_iva_esigibilita_differita_provvisoriaVBulk bulk = new Riepilogo_iva_esigibilita_differita_provvisoriaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return (Riepilogo_iva_esigibilita_differita_provvisoriaVBulk)bulk.initializeForSearch(this,context);
		
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
