package it.cnr.contab.config00.action;

import it.cnr.contab.config00.pdcfin.bulk.Ass_evold_evnewBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.SimpleCRUDBP;

public class CRUDConfigAssEvoldEvnewAction extends CRUDAction{
	private static final long serialVersionUID = 1L;

	public CRUDConfigAssEvoldEvnewAction() {
		super();
	}

	public Forward doOnTipoGestioneSearchChange(ActionContext context) {
		try{
			fillModel(context);
			SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
			Ass_evold_evnewBulk bulk = (Ass_evold_evnewBulk)bp.getModel();
			bulk.setElemento_voce_old(new Elemento_voceBulk());
			bulk.setElemento_voce_new(new Elemento_voceBulk());
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
}
