package it.cnr.contab.anagraf00.bp;

/**
 * Insert the type's description here.
 * Creation date: (23/07/2001 10:33:18)
 * @author: CNRADM
 */
public class CRUDAnagraficaStrutturaCNRBP extends CRUDAnagraficaBP {
/**
 * CRUDAnagraficaStrutturaCNRBP constructor comment.
 * @param function java.lang.String
 * @exception it.cnr.jada.action.BusinessProcessException The exception description.
 */
public CRUDAnagraficaStrutturaCNRBP(String function) throws it.cnr.jada.action.BusinessProcessException {
	super(function);
}
public it.cnr.jada.bulk.OggettoBulk createNewBulk(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = (it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk)super.createNewBulk(context);
	anagrafico.setTi_entita_persona_struttura(anagrafico.ENTITA_STRUTTURA);
	anagrafico.setTi_entita(anagrafico.STRUT_CNR);
	return anagrafico;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	super.init(config,context);
	try {
		edit(context,
			((it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession)createComponentSession()
			).getAnagraficoEnte(context.getUserContext())
		);
	} catch(Throwable e) {
	}
}
public boolean isDeleteButtonEnabled() {
	return false;
}
public boolean isDeleteButtonHidden() {
	return true;
}
public boolean isNewButtonEnabled() {
	return false;
}
public boolean isNewButtonHidden() {
	return true;
}
}
