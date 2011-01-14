package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class RiepilogoIvaEsigibilitaDifferitaDefinitivaBP extends RiepilogoIvaEsigibilitaDifferitaBP {

	private int status = INSERT;
public RiepilogoIvaEsigibilitaDifferitaDefinitivaBP() {
	this("");
}
public RiepilogoIvaEsigibilitaDifferitaDefinitivaBP(String function) {
	super(function+"Tr");
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Riepilogo_iva_esigibilita_differitaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Riepilogo_iva_esigibilita_differita_definitivaVBulk bulk = new Riepilogo_iva_esigibilita_differita_definitivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Riepilogo_iva_esigibilita_differita_definitivaVBulk)bulk.initializeForSearch(this,context);
		
		return (Riepilogo_iva_esigibilita_differita_definitivaVBulk)aggiornaRegistriStampati(context, bulk);

	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
