/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.CRUDDistintaCassiere1210BP;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.comp.ApplicationException;
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
			CRUDDistintaCassiere1210BP bp = (CRUDDistintaCassiere1210BP)context.getBusinessProcess();	
			if (bp.getDistintaCassiere1210LettereCollegate().countDetails() == 0)
				throw new ApplicationException("Associare almeno un documento alla distinta!");
			bp.generaDistinta(context);
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
//			if (bp.getDistintaCassiere1210LettereCollegate().countDetails() == 0) 
//				throw new ApplicationException("Associare almeno un documento alla distinta!");
//			else
//			BulkBP firmaOTPBP = (BulkBP) context.createBusinessProcess("FirmaOTPBP");
//			firmaOTPBP.setModel(context, new FirmaOTPBulk());
//			context.addHookForward("firmaOTP",this,"doBackSign");			
			//return context.addBusinessProcess(firmaOTPBP);
				return super.doSalva(context);
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