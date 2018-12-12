package it.cnr.contab.chiusura00.action;

import java.rmi.RemoteException;

import it.cnr.contab.chiusura00.bp.RiportoEsSuccessivoBP;
import it.cnr.contab.chiusura00.bp.RiportoSelezionatoreListaBP;
import it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.util.ObjectReplacer;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelectionListener;

/**
 * Action che gestisce il riporto avanti massivo di documenti contabili all'esercizio successivo;
 * utilizzata anche nel caso di riporto avanti con cambio di capitolo finanziario
 */

public class RiportoEsSuccessivoAction extends it.cnr.jada.util.action.BulkAction {
public RiportoEsSuccessivoAction() {
	super();
}
/**
 * L'utente ha annullato la selezione dei documenti contabili fatta in precedenza
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doAnnullaSelezione(ActionContext context)  throws BusinessProcessException
{
	return context.findDefaultForward();
}
/**
 * L'utente ha annullato la selezione della voce_f o dell'elemento voce fatta in precedenza.
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAnnullaVoce(ActionContext context)  
{
	try
	{	
		fillModel( context );
		((V_obb_acc_xxxBulk)((BulkBP )context.getBusinessProcess()).getModel()).setNuova_voce( new Voce_fBulk());
		((V_obb_acc_xxxBulk)((BulkBP)context.getBusinessProcess()).getModel()).setNuovo_ev( new Elemento_voceBulk());	
		return context.findDefaultForward();
	}
	catch ( Exception e )
	{
		return handleException( context, e );
	}	
		
}
/**
 * L'utente ha richiesto la ricerca di documenti contabili idonei as essere riportati aventi
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward  doCercaDocDaRiportare( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		fillModel(context);
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP) context.getBusinessProcess();
		V_obb_acc_xxxBulk model = (V_obb_acc_xxxBulk)bp.getModel();
//		model.validate();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaDocDaRiportare(context,model);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			if(bp.isCdsRibaltato(context)) 
				bp.setMessage("La ricerca non ha fornito alcun risultato.");
			else {
				return openConfirm(context,"La ricerca non ha fornito alcun risultato. Si vuole procedere con il ribaltamento delle disponibilità residue?",OptionBP.CONFIRM_YES_NO,"doConfermaRibaltamento");
			}
//			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{
			bp.setModel(context,model);
			RiportoSelezionatoreListaBP nbp = selectRiporto(context,
												ri,
												it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_obb_acc_xxxBulk.class),
												null,"doRiportaSelezione",null,(RiportoEsSuccessivoBP)bp);

			nbp.setMultiSelection( true );
			nbp.setSelectAllOnly(!bp.isRibaltato());
			context.addHookForward("annulla_seleziona",this,"doAnnullaSelezione");			
			context.addHookForward("close",this,"doDefault");			
			return context.findDefaultForward();
			
		}

	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * L'utente ha confermato di voler procedere con il riporto avanti dei documenti contabili
 * selezionati
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @param option	
 * @return Il Forward alla pagina di risposta
 */
public Forward doConfermaRiporto(ActionContext context, int option)
{
	try
	{
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP)context.getBusinessProcess();		
		if (option == OptionBP.YES_BUTTON) 
			bp.confermaRiporto(context);		
		else
			bp.rollbackUserTransaction();
			
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * L'utente ha confermato la selezione effettuata dei documenti contabili
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doRiportaSelezione(ActionContext context)  throws BusinessProcessException
{
	
	return openConfirm(context,"Attenzione tutti i documenti selezionati saranno riportati all'esercizio " +
								(CNRUserContext.getEsercizio( context.getUserContext()).intValue() + 1) +
								". Vuoi procedere?",OptionBP.CONFIRM_YES_NO,"doConfermaRiporto");
}

protected RiportoSelezionatoreListaBP selectRiporto(ActionContext actioncontext, RemoteIterator remoteiterator, BulkInfo bulkinfo, String s, String s1, ObjectReplacer objectreplacer, SelectionListener selectionlistener)
	throws BusinessProcessException
{
	try
	{
		RiportoSelezionatoreListaBP selezionatorelistabp = (RiportoSelezionatoreListaBP)actioncontext.createBusinessProcess("RiportoSelezionatoreListaBP");
		selezionatorelistabp.setObjectReplacer(objectreplacer);
		selezionatorelistabp.setSelectionListener(actioncontext, selectionlistener);
		selezionatorelistabp.setIterator(actioncontext, remoteiterator);
		selezionatorelistabp.setBulkInfo(bulkinfo);
		selezionatorelistabp.setColumns(bulkinfo.getColumnFieldPropertyDictionary(s));
		actioncontext.addHookForward("seleziona", this, s1);
		HookForward _tmp = (HookForward)actioncontext.findForward("seleziona");
		actioncontext.addBusinessProcess(selezionatorelistabp);
		return selezionatorelistabp;
	}
	catch(RemoteException remoteexception)
	{
		throw new BusinessProcessException(remoteexception);
	}
}

public Forward doConfermaRibaltamento(ActionContext context,int option) throws BusinessProcessException {
	try
	{
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP) context.getBusinessProcess();
		if (option == OptionBP.YES_BUTTON) 
		{
			bp.confermaRibaltamentoDispImproprie(context);
			bp.commitUserTransaction();
			bp.aggiornaParametriCds(context);
			bp.commitUserTransaction();
			bp.setMessage("E' stato effettuato il ribaltamento");
		}
		if (option == OptionBP.NO_BUTTON) 
			bp.setMessage("Non è stato effettuato il ribaltamento");
				
		return context.findDefaultForward();
	} 
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}

public it.cnr.jada.action.Forward doCercaResiduiForRiaccertamento( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		fillModel(context);
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP) context.getBusinessProcess();
		V_obb_acc_xxxBulk model = (V_obb_acc_xxxBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaResiduiForRiaccertamento(context);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{
			bp.setModel(context,model);
			RiportoSelezionatoreListaBP nbp = selectRiporto(context,
												ri,
												it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_obb_acc_xxxBulk.class),
												null,"doDefault",null,(RiportoEsSuccessivoBP)bp);
			nbp.setMultiSelection( false );
			nbp.setSelectAllOnly(false);
			context.addHookForward("close",this,"doDefault");			
			return context.findDefaultForward();
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public it.cnr.jada.action.Forward doCercaGaeSenzaProgettiForRibaltamento( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		fillModel(context);
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP) context.getBusinessProcess();
		V_obb_acc_xxxBulk model = (V_obb_acc_xxxBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaGaeSenzaProgettiForRibaltamento(context);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{
			bp.setModel(context,model);
			RiportoSelezionatoreListaBP nbp = selectRiporto(context,
												ri,
												it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_obb_acc_xxxBulk.class),
												null,"doDefault",null,(RiportoEsSuccessivoBP)bp);

			nbp.setMultiSelection( false );
			nbp.setSelectAllOnly(false);
			context.addHookForward("close",this,"doDefault");			
			return context.findDefaultForward();
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
}

public it.cnr.jada.action.Forward doCercaProgettiCollegatiGaeNonApprovatiForRibaltamento( it.cnr.jada.action.ActionContext context )
{
	try 
	{
		fillModel(context);
		RiportoEsSuccessivoBP bp = (RiportoEsSuccessivoBP) context.getBusinessProcess();
		V_obb_acc_xxxBulk model = (V_obb_acc_xxxBulk)bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(context);
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
			bp.setMessage("La ricerca non ha fornito alcun risultato.");
			return context.findDefaultForward();
		}
		else
		{
			bp.setModel(context,model);
			RiportoSelezionatoreListaBP nbp = selectRiporto(context,
												ri,
												it.cnr.jada.bulk.BulkInfo.getBulkInfo(V_obb_acc_xxxBulk.class),
												null,"doDefault",null,(RiportoEsSuccessivoBP)bp);

			nbp.setMultiSelection( false );
			nbp.setSelectAllOnly(false);
			context.addHookForward("close",this,"doDefault");			
			return context.findDefaultForward();
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
