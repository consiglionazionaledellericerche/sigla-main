package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.CRUDDistintaCassiere1210BP;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDAction;

import java.rmi.RemoteException;

public class CRUDDistintaCassiere1210Action extends CRUDAction {

	private static final long serialVersionUID = 1L;

	public Forward doRimuoviDocumento(ActionContext context) throws RemoteException {
		try{
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			bp.rimuoviDocumento(context);
			bp.setDirty(true);
			return context.findDefaultForward();
		} catch (Exception e ){
			return handleException( context, e );
		}	
	}
	@Override
	public Forward doElimina(ActionContext actioncontext)
			throws RemoteException {
		CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)actioncontext.getBusinessProcess();	
		super.doElimina(actioncontext);
		bp.setDirty(true);
		return actioncontext.findDefaultForward();		
	}
	@Override
	public Forward doConfermaNuovo(ActionContext actioncontext, int i) {
		CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)actioncontext.getBusinessProcess();	
		Forward forward = super.doConfermaNuovo(actioncontext, i);
		bp.setDirty(true);
		return forward;
	}
	public Forward doAssociaDocumento(ActionContext context) throws RemoteException {
		try{
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			bp.associaDocumento(context);
			bp.setDirty(true);
			return context.findDefaultForward();
		} catch (Exception e ){
			return handleException( context, e );
		}	
	}
	
	public Forward doInvia(ActionContext context) throws RemoteException {
		try{
			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(context, new FirmaOTPBulk());
			context.addHookForward("firmaOTP",this,"doBackInvia");			
			return context.addBusinessProcess(firmaOTPBP);
		} catch (Exception e ){
			return handleException( context, e );
		}	
	}	
	
	public Forward doBackInvia(ActionContext context) {
		try{
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			HookForward caller = (HookForward)context.getCaller();
			FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
			bp.invia(context, firmaOTPBulk);			
			return context.findDefaultForward();
		} catch (Exception e ){
			return handleException( context, e );
		}
	}		
	@Override
	public Forward doSalva(ActionContext context) throws RemoteException {
		try {
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			if (bp.getDistintaCassiere1210LettereCollegate().countDetails() == 0)
				return super.doSalva(context);	
			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
			firmaOTPBP.setModel(context, new FirmaOTPBulk());
			context.addHookForward("firmaOTP",this,"doBackSign");			
			return context.addBusinessProcess(firmaOTPBP);
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doBackSign(ActionContext context) {
		try{
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			HookForward caller = (HookForward)context.getCaller();
			FirmaOTPBulk firmaOTPBulk = (FirmaOTPBulk) caller.getParameter("firmaOTP");
			bp.signDocuments(context, firmaOTPBulk);			
			return super.doSalva(context);
		} catch (Exception e ){
			return handleException( context, e );
		}
	}	
}