package it.cnr.contab.bollo00.action;

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class TipoAttoBolloAction extends it.cnr.jada.util.action.CRUDAction{
	private static final long serialVersionUID = 1L;

	public TipoAttoBolloAction() {
		super();
	}

	public Forward doOnTipoCalcoloChange(ActionContext context) {
		try{
			fillModel(context);
	
			Tipo_atto_bolloBulk tipoAtto = (Tipo_atto_bolloBulk)getBusinessProcess(context).getModel();
			tipoAtto.setLimiteCalcolo(null);
			tipoAtto.setRigheFoglio(null);
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
}