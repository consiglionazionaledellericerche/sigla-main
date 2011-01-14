package it.cnr.contab.compensi00.actions;


public class CRUDAssTipoCoriVEPAction extends it.cnr.jada.util.action.CRUDAction {

	public CRUDAssTipoCoriVEPAction() {
		super();
	}
	/*public Forward doBlankSearchFind_voce(ActionContext context,Ass_tipo_cori_voce_epBulk bulk)
	{
		try{
			fillModel(context);
			ContoBulk elem= new ContoBulk();
			elem.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
			bulk.setVoce_ep(elem);
			bulk.setVoce_ep_contr(elem);
			((CRUDBP)context.getBusinessProcess()).setDirty(true);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	}*/
}
