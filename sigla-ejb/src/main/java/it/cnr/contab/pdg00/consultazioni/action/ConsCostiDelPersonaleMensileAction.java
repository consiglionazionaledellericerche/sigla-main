package it.cnr.contab.pdg00.consultazioni.action;


import java.rmi.RemoteException;

import javax.ejb.RemoveException;

import it.cnr.contab.pdg00.consultazioni.bp.ConsCostiDelPersonaleMensileBP;
import it.cnr.contab.pdg00.consultazioni.bp.ConsCostiDelPersonaleMensileDettBP;
import it.cnr.contab.pdg00.consultazioni.bulk.Param_cons_costi_personaleBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.BulkAction;

public class ConsCostiDelPersonaleMensileAction extends BulkAction{

	public Forward doCerca(ActionContext context) throws RemoteException, InstantiationException, RemoveException{
			try {
				
				ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP) context.getBusinessProcess();
				CompoundFindClause clauses = new CompoundFindClause();
				Param_cons_costi_personaleBulk parametri = (Param_cons_costi_personaleBulk)bp.getModel();
				
				bp.fillModel(context); 
				parametri.validaUo();
				parametri.validaMese();
				
										
				ConsCostiDelPersonaleMensileDettBP DettBP = (ConsCostiDelPersonaleMensileDettBP) context.createBusinessProcess("ConsCostiDelPersonaleMensileDettBP");
				context.addBusinessProcess(DettBP);
				DettBP.addToBaseclause(clauses);
//				DettBP.addToBaseclause(DettBP.getSelezione(context,parametri));
				DettBP.openIterator(context);
				
			    return context.findDefaultForward();
			
//				return context.addBusinessProcess(DettBP);			
				
			} catch (Exception e) {
					return handleException(context,e); 
			}
	}
		
	
	
	public Forward doFreeSearchFind_dipendente(ActionContext context) 
	{
		try 
		{
			ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP)context.getBusinessProcess();	
			bp.fillModel(context);
			
			Param_cons_costi_personaleBulk parametri = (Param_cons_costi_personaleBulk)bp.getModel();
			
			if( parametri.getMese() == null)
				throw new it.cnr.jada.comp.ApplicationException("Selezionare il mese");
			
			return freeSearch(context,bp.getFormField("find_dipendente"));
		} 
		catch (Throwable t) 
		{
			return handleException(context, t);
		}
	}
	
	
	public Forward doFreeSearchFind_modulo(ActionContext context) 
	{
		try 
		{
			ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP)context.getBusinessProcess();	
			bp.fillModel(context);
			
			Param_cons_costi_personaleBulk parametri = (Param_cons_costi_personaleBulk)bp.getModel();
			
			if( parametri.getMese() == null)
				throw new it.cnr.jada.comp.ApplicationException("Selezionare l'Unità Organizzativa");
			
			return freeSearch(context,bp.getFormField("find_modulo"));
		} 
		catch (Throwable t) 
		{
			return handleException(context, t);
		}
	}
	
	public Forward doFreeSearchFind_commessa(ActionContext context) 
	{
		try 
		{
			ConsCostiDelPersonaleMensileBP bp = (ConsCostiDelPersonaleMensileBP)context.getBusinessProcess();	
			bp.fillModel(context);
			
			Param_cons_costi_personaleBulk parametri = (Param_cons_costi_personaleBulk)bp.getModel();
			
			if( parametri.getMese() == null)
				throw new it.cnr.jada.comp.ApplicationException("Selezionare l'Unità Organizzativa");
			
			return freeSearch(context,bp.getFormField("find_commessa"));
		} 
		catch (Throwable t) 
		{
			return handleException(context, t);
		}
	}

	public Forward doBlankSearchFind_commessa(ActionContext context, Param_cons_costi_personaleBulk parametri) {
		parametri.setV_commessa(new ProgettoBulk());
		parametri.setV_modulo(new ProgettoBulk());
		return context.findDefaultForward();
	}
	public Forward doBringBackSearchFindCommessaForPrint(ActionContext context, Param_cons_costi_personaleBulk parametri, ProgettoBulk commessa) {
		if (commessa != null && commessa.getCd_progetto()!=null){
			parametri.setV_commessa(commessa);	
			parametri.setV_modulo(new ProgettoBulk());
		}		  
		return context.findDefaultForward();	
	}	
	
	
}
