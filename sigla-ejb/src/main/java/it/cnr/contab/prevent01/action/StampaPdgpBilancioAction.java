/*
 * Created on Apr 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.action;

import it.cnr.contab.prevent01.bp.StampaPdgpBilancioBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.OptionBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaPdgpBilancioAction extends it.cnr.contab.reports.action.ParametricPrintAction {

	/**
	 * 
	 */
	public StampaPdgpBilancioAction() {
		super();
	}

	public Forward doOnTipoChange(ActionContext context) {
		try{
			StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
			fillModel(context);
			try {
				bp.loadModelBulkOptions(context);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doOnTipoAggregazioneChange(ActionContext context) {
		try{
			StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
			fillModel(context);
			try {
				bp.loadModelBulkOptions(context);
			} catch (BusinessProcessException e) {
				return handleException(context, e);
			}
			return context.findDefaultForward();
		}catch(it.cnr.jada.bulk.FillException ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doAggiornaPrevisioneAC(ActionContext context) 
	{
		try {
			fillModel(context);
			return openConfirm(context,"Attenzione! Confermi l'aggiornamento della previsione anno corrente?",OptionBP.CONFIRM_YES_NO,"doConfirmAggiornaPrevisioneAC");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doAggiornaResiduoAC(ActionContext context) 
	{
		try {
			fillModel(context);
			return openConfirm(context,"Attenzione! Confermi l'aggiornamento dei dati residui anno corrente?",OptionBP.CONFIRM_YES_NO,"doConfirmAggiornaResiduoAC");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doAggiornaResiduoAP(ActionContext context) 
	{
		try {
			fillModel(context);
			return openConfirm(context,"Attenzione! Confermi l'aggiornamento dei residui assestati anno precedente?",OptionBP.CONFIRM_YES_NO,"doConfirmAggiornaResiduoAP");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doAggiornaCassaAC(ActionContext context) 
	{
		try {
			fillModel(context);
			return openConfirm(context,"Attenzione! Confermi l'aggiornamento della cassa anno corrente?",OptionBP.CONFIRM_YES_NO,"doConfirmAggiornaCassaAC");
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}	

	public Forward doConfirmAggiornaPrevisioneAC(ActionContext context,int option) {
		try
		{
			if (option == OptionBP.YES_BUTTON) 
			{
				StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
				bp.stampaBilancioCallAggiornaDati(context, true, false, false, false);		
				bp.setMessage("Operazione effettuata!");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmAggiornaResiduoAC(ActionContext context,int option) {
		try
		{
			if (option == OptionBP.YES_BUTTON) 
			{
				StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
				bp.stampaBilancioCallAggiornaDati(context, false, true, false, false);
				bp.setMessage("Operazione effettuata!");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmAggiornaResiduoAP(ActionContext context,int option) {
		try
		{
			if (option == OptionBP.YES_BUTTON) 
			{
				StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
				bp.stampaBilancioCallAggiornaDati(context, false, false, true, false);
				bp.setMessage("Operazione effettuata!");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}

	public Forward doConfirmAggiornaCassaAC(ActionContext context,int option) {
		try
		{
			if (option == OptionBP.YES_BUTTON) 
			{
				StampaPdgpBilancioBP  bp = (StampaPdgpBilancioBP)context.getBusinessProcess();
				bp.stampaBilancioCallAggiornaDati(context, false, false, false, true);
				bp.setMessage("Operazione effettuata!");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
}
