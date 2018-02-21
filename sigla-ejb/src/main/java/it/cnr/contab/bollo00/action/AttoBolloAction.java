package it.cnr.contab.bollo00.action;

import java.util.Optional;

import it.cnr.contab.bollo00.bp.CRUDAttoBolloBP;
import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

public class AttoBolloAction extends it.cnr.jada.util.action.CRUDAction{
	private static final long serialVersionUID = 1L;

	public AttoBolloAction() {
		super();
	}

	public Forward doOnNumFogliChange(ActionContext context) {
		try{
			fillModel(context);
			Atto_bolloBulk model = (Atto_bolloBulk)getBusinessProcess(context).getModel();
			if (!Optional.ofNullable(model.getNumDettagli()).filter(el->el>0).isPresent()) {
				model.setNumRighe(0);
				model.setNumDettagli(0);
			}
			((CRUDAttoBolloBP)getBusinessProcess(context)).validateFogliRighe(context);
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doOnNumRigheChange(ActionContext context) {
		try{
			fillModel(context);
			Atto_bolloBulk model = (Atto_bolloBulk)getBusinessProcess(context).getModel();
			if (!Optional.ofNullable(model.getNumRighe()).filter(el->el>0).isPresent()) {
				model.setNumRighe(0);
				model.setNumDettagli(0);
			}
			((CRUDAttoBolloBP)getBusinessProcess(context)).validateFogliRighe(context);
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doOnFlContrattoRegistratoChange(ActionContext context) {
		try{
			fillModel(context);
			Atto_bolloBulk model = (Atto_bolloBulk)getBusinessProcess(context).getModel();
			if (!model.isFlContrattoRegistrato())
				model.setContratto(null);
		} catch (it.cnr.jada.bulk.FillException e){
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doToggleAllegati(ActionContext context) {
    	CRUDAttoBolloBP bp = Optional.ofNullable(getBusinessProcess(context))
                .filter(CRUDAttoBolloBP.class::isInstance)
                .map(CRUDAttoBolloBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business Process non valido"));
        bp.setAllegatiCollapse(!bp.isAllegatiCollapse());
        return context.findDefaultForward();
    }
}