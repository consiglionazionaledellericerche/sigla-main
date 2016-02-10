package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.ElaboraFileBlackListBP;
import it.cnr.contab.docamm00.docs.bulk.VFatcomBlacklistBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

public class ElaboraFileBlackListAction extends it.cnr.jada.util.action.CRUDAction {
public ElaboraFileBlackListAction() {
	super();
}
public Forward doBringBack(ActionContext context) {
	return context.findDefaultForward();
}

public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
	it.cnr.contab.docamm00.bp.ElaboraFileBlackListBP bp= (it.cnr.contab.docamm00.bp.ElaboraFileBlackListBP) context.getBusinessProcess();
    try {
        bp.fillModel(context); 
        bp.setFile(null);
        return context.findDefaultForward();
	} catch (Exception e) {
		return handleException(context,e);
	}        
}
public Forward doElaboraFile(ActionContext context) throws ComponentException, PersistencyException, IntrospectionException {	

	try{
		fillModel(context);
		ElaboraFileBlackListBP bp = (ElaboraFileBlackListBP) context.getBusinessProcess();
		VFatcomBlacklistBulk dett = (VFatcomBlacklistBulk)bp.getModel();
		if (dett.getMese() == null )
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare il Mese"));
		try {
				bp.doElaboraFile(context,dett);
				
		} catch (Exception e) { 
			return handleException(context, e);
		}
		
		bp.setMessage("Elaborazione completata.");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}

}