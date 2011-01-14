package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.tabrif.bulk.Ass_tipo_cori_evBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDBP;


public class CRUDAssTipoCoriEvAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDAssTipoCoriEvAction() {
		super();
	}
	public Forward doBlankSearchFind_elemento_voce(ActionContext context,Ass_tipo_cori_evBulk bulk)
	{
		try{
			fillModel(context);
			Elemento_voceBulk elem= new Elemento_voceBulk();
			elem.setTi_gestione(bulk.getTi_gestione());
			elem.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
			elem.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			bulk.setElemento_voce(elem);
			((CRUDBP)context.getBusinessProcess()).setDirty(true);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	}
}
