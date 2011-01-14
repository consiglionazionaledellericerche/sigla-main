package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public abstract class QuadriLiqAnnualeBP extends LiquidazioneIvaAnnualeBP {

	private int status = INSERT;

	private final SimpleDetailCRUDController elencoSecondario = new SimpleDetailCRUDController(
		"Elenco secondario", Vp_liquid_iva_annualeBulk.class,"elencoSecondario",this);
public QuadriLiqAnnualeBP() {
	this("");
}
public QuadriLiqAnnualeBP(String function) {
	super(function+"Tr");
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 15.39.48)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getElencoSecondario() {
	return elencoSecondario;
}
}
