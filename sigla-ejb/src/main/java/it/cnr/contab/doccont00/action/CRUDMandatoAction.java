package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.anagraf00.bp.CRUDTerzoBP;
import it.cnr.contab.anagraf00.core.bulk.Termini_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.*;

import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
/**
 * Azione che gestisce le richieste relative alla Gestione Documenti Contabili
 * (Mandato)
 */
public class CRUDMandatoAction extends CRUDAbstractMandatoAction {
public CRUDMandatoAction() {
	super();
}
/**
 * Metodo utilizzato per gestire la conferma dei documenti passivi 
 * disponibili selezionati.
 */

public Forward doAggiungiDocPassivi(ActionContext context) 
{
	try 
	{
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		fillModel( context );
		bp.aggiungiDocPassivi(context);
		return context.findDefaultForward();
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il caricamento dei doc.amm.attivi collegati all'accertamento
 	 * @param context <code>ActionContext</code> in uso.
	 * @param mandato Oggetto di tipo <code>MandatoIBulk</code>
	 * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBlankSearchFind_accertamento(ActionContext context, MandatoIBulk mandato) 
{
	try 
	{
		mandato.setDocGenericiPerRegolarizzazione( null);
		mandato.setDocGenericiSelezionatiPerRegolarizzazione( null);
		mandato.setAccertamentoPerRegolarizzazione( new AccertamentoBulk());
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce l'annullamento del terzo 
 *
 */			   
public Forward doBlankSearchFind_doc_passivi(ActionContext context, OggettoBulk mandato) 
{
	try 
	{ 	
		((MandatoIBulk)mandato).setDocPassiviColl( new java.util.ArrayList() );
		((MandatoIBulk)mandato).getFind_doc_passivi().setTerzoAnag( new it.cnr.contab.anagraf00.core.bulk.V_all_anagrafico_terzoBulk());
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento dei doc.amm.attivi collegati all'accertamento
 	 * @param context <code>ActionContext</code> in uso.
	 * @param mandato Oggetto di tipo <code>MandatoIBulk</code>
	 * @param accertamento Oggetto di tipo <code>AccertamentoBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackSearchFind_accertamento(ActionContext context, MandatoIBulk mandato, AccertamentoBulk accertamento) 
{
	try 
	{
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		if ( accertamento != null )
		{
			mandato.setAccertamentoPerRegolarizzazione( accertamento );
			if ( mandato.getCd_unita_organizzativa() == null || mandato.getCd_uo_ente() == null ||
				 !mandato.getCd_unita_organizzativa().equals( mandato.getCd_uo_ente() ) )
				bp.caricaScadenzeAccertamentoPerRegolarizzazione(context);
			else
				bp.caricaDocAttiviPerRegolarizzazione(context);
		}	
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione di un tipo bollo
 *
 */
public Forward doCambiaTipoBollo(ActionContext context) 
{
	try 
	{
		fillModel( context );
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		MandatoBulk mandato = (MandatoBulk)bp.getModel();
		mandato.getMandato_terzo().setToBeUpdated();
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione dell'unità organizzativa
 *
 */
public Forward doCambiaTipoMandato(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la selezione dell'unità organizzativa
 *
 */
public Forward doCambiaUnitaOrganizzativa(ActionContext context) 
{
	try 
	{ 	
		fillModel( context );
		SimpleCRUDBP bp = (SimpleCRUDBP)getBusinessProcess(context);
		MandatoIBulk mandato = (MandatoIBulk)bp.getModel();
		mandato.setDocPassiviColl( new java.util.ArrayList() );
//		mandato.setSospesiColl( new ArrayList() );
		mandato.setCd_cds( mandato.getUnita_organizzativa().getCd_unita_padre());
		mandato.setAccertamentoPerRegolarizzazione( null );
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento dei documenti passivi
 *
 */
public Forward doCercaDocPassivi(ActionContext context) 
{
	try 
	{
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		fillModel( context );
		MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
		if ( mandato.getFind_doc_passivi().getCd_terzo() == null &&
           mandato.getFind_doc_passivi().getCd_precedente() == null && 			
			 mandato.getFind_doc_passivi().getCognome() == null &&
			 mandato.getFind_doc_passivi().getRagione_sociale() == null &&
			 mandato.getFind_doc_passivi().getNome()  == null &&
			 mandato.getFind_doc_passivi().getPartita_iva()  == null &&
			 mandato.getFind_doc_passivi().getCodice_fiscale()  == null  
			 )
			throw new it.cnr.jada.comp.ApplicationException ( "Attenzione! Deve essere specificato almeno un campo dell'anagrafica." );

		if ( mandato.getFind_doc_passivi().getTerzoAnag().getCrudStatus() == bp.getModel().UNDEFINED )
		{
			//doSearchFind_doc_passiviInAutomatico( context );
			it.cnr.jada.util.RemoteIterator ri = bp.find(context,null,mandato.getFind_doc_passivi().getTerzoAnag(),mandato,"find_doc_passivi.terzoAnag");
			if (ri == null || ri.countElements() == 0) 
			{
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				bp.setMessage("Il terzo non e' presente nell'anagrafico.");
				return context.findDefaultForward();
			} else if (ri.countElements() == 1) 
			{
				FormField field = getFormField(context,"main.find_doc_passivi");			
				doBringBackSearchResult(context,field,(OggettoBulk)ri.nextElement());
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			} else 
			{
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
				bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
				return context.findDefaultForward();
			}	
		}
		bp.cercaDocPassivi(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Metodo utilizzato per ricercare i documenti passivi in automatico.
 */
public Forward doSearchFind_doc_passiviInAutomatico(ActionContext context) 
{
	try 
	{
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		MandatoIBulk mandato = (MandatoIBulk) bp.getModel();
		it.cnr.jada.util.RemoteIterator ri = bp.find(context,null,mandato.getFind_doc_passivi().getTerzoAnag(),mandato,"find_doc_passivi.terzoAnag");
		if (ri == null || ri.countElements() == 0) {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("Il terzo non e' presente nell'anagrafico.");
			return context.findDefaultForward();
		} else if (ri.countElements() == 1) {
			FormField field = getFormField(context,"main.find_doc_passivi");			
			doBringBackSearchResult(context,field,(OggettoBulk)ri.nextElement());
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			return context.findDefaultForward();
		} else {
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
			bp.setMessage("Esite piu' di un terzo che soddisfa i criteri di ricerca.");
			return context.findDefaultForward();
		}
	} catch(Exception e) {
		return handleException(context,e);
	}
		
}
/**
 * Metodo utilizzato per visualizzare la disponibilità di cassa sul capitolo
 */

public Forward doVisualizzaDispCassaCapitolo(ActionContext context)
{
	try 
	{
		CRUDMandatoBP bp = (CRUDMandatoBP)getBusinessProcess(context);
		fillModel( context );
		context.addHookForward("close",this,"doDefault");				
		DispCassaCapitoloBP view = (DispCassaCapitoloBP) context.createBusinessProcess( "DispCassaCapitoloBP" );
		DispCassaCapitoloBulk bulk = new DispCassaCapitoloBulk();
		bulk.setMandato( (MandatoBulk) bp.getModel());
		view.setModel( context, bulk );
		view.refreshDispCassa( context );
		return context.addBusinessProcess(view);

	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doPrint(ActionContext actioncontext) {
	try 
	{
		CRUDAbstractMandatoBP bp = (CRUDAbstractMandatoBP)getBusinessProcess(actioncontext);
		fillModel( actioncontext );
		bp.esistonoPiuModalitaPagamento(actioncontext);
	}		
	catch(Throwable e) {
		return handleException(actioncontext,e);
	}
	return super.doPrint(actioncontext);
}
/**
 * Associa un codice SIOPE (Mandato_siopeBulk), ad una riga di mandato (Mandato_rigaBulk).
 *
 * @param context {@link ActionContext } in uso.
 *
 * @return Forward
 */

public Forward doAggiungiCodiceSiope(ActionContext context) {
	try {
		fillModel(context);
		CRUDMandatoBP bp = (CRUDMandatoBP)context.getBusinessProcess();
		bp.getCodiciSiopeCollegabili().remove(context);
		bp.getCodiciSiopeCollegati().reset(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}
/**
 * Rimuove i codici SIOPE selezionati.
 *
 * @param context {@link ActionContext } in uso.
 *
 * @return Forward
 */

public Forward doRimuoviCodiceSiope(ActionContext context) {
	try {
		fillModel(context);
		CRUDMandatoBP bp = (CRUDMandatoBP)context.getBusinessProcess();
		bp.getCodiciSiopeCollegati().remove(context);
		bp.getCodiciSiopeCollegabili().reset(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}
public Forward doSalva(ActionContext context) throws RemoteException {
	try {
		fillModel(context);
		CRUDMandatoBP bp = (CRUDMandatoBP)context.getBusinessProcess();
		MandatoBulk mandato = (MandatoBulk)bp.getModel();
		
		if (bp.isSiope_attiva() && mandato.isRequiredSiope() && !mandato.isSiopeTotalmenteAssociato()){
			if (bp.isSiopeBloccante(context)){
				bp.setMessage("Attenzione! Alcune o tutte le righe mandato non risultano associate completamente a codici SIOPE. Impossibile continuare.");
				return doSelezionaRigaSiopeDaCompletare(context);
			}
			return openConfirm(context,"Attenzione! Alcune o tutte le righe mandato non risultano associate completamente a codici SIOPE. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmSalvaCup");
		}
		
		return doConfirmSalvaCup(context,OptionBP.YES_BUTTON);			
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}
public Forward doConfirmSalva(ActionContext actioncontext,int option) {
	try
	{
		if (option == OptionBP.YES_BUTTON) 
		{
			return super.doSalva(actioncontext);
		}
		return doSelezionaRigaSiopeDaCompletare(actioncontext);
	} catch(Throwable e) {
		return handleException(actioncontext,e);
	}
}
public Forward doConfirmSalvaCup(ActionContext actioncontext,int option) {
	try
	{
		if (option == OptionBP.YES_BUTTON) 
		{
			CRUDMandatoBP bp = (CRUDMandatoBP)actioncontext.getBusinessProcess();
			MandatoBulk mandato = (MandatoBulk)bp.getModel();
			// mandato.isRequiredSiope() controlla che non sia un mandato di regolarizzazione 
			if (bp.isCup_attivo() && mandato.isRequiredSiope() ){
				boolean trovato =false;
				if (mandato instanceof MandatoIBulk){
					bp.getCupCollegati().validate(actioncontext);
					for (Iterator i=mandato.getMandato_rigaColl().iterator();i.hasNext()&&!trovato;){
						Mandato_rigaBulk riga = (Mandato_rigaBulk)i.next();
							if(riga.getMandatoCupColl().isEmpty()||riga.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO)!=0)
								trovato =true;
						}
					if(trovato)
					  return openConfirm(actioncontext,"Attenzione! Alcune o tutte le righe mandato non risultano associate completamente al CUP. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmSalva");
				}
			}
			if(!mandato.isAnnullato()){
				if (bp.isSiope_cup_attivo() && mandato.isRequiredSiope() ){
					boolean trovato =false;
					if (mandato instanceof MandatoIBulk){
						bp.getSiopeCupCollegati().validate(actioncontext);
						for (Iterator i=mandato.getMandato_rigaColl().iterator();i.hasNext()&&!trovato;){
							Mandato_rigaBulk riga = (Mandato_rigaBulk)i.next();
							for (Iterator j=riga.getMandato_siopeColl().iterator();j.hasNext()&&!trovato;){
								Mandato_siopeBulk rigaSiope = (Mandato_siopeBulk)j.next();
							
								if(rigaSiope.getMandatoSiopeCupColl().isEmpty()||rigaSiope.getTipoAssociazioneCup().compareTo(Mandato_rigaBulk.SIOPE_TOTALMENTE_ASSOCIATO)!=0)
									trovato =true;
							}
						if(trovato)
						  return openConfirm(actioncontext,"Attenzione! Alcune o tutte le righe siope non risultano associate completamente al CUP. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmSalva");
						}
					}
				}
			}
			return doConfirmSalva(actioncontext,OptionBP.YES_BUTTON);
		}
		return doConfirmSalva(actioncontext,OptionBP.NO_BUTTON);
	} catch(Throwable e) {
		return handleException(actioncontext,e);
	}
}
public Forward doTab(ActionContext actioncontext, String s, String s1) {
	try 
	{
		fillModel(actioncontext);
		Forward forward = super.doTab(actioncontext, s, s1);
		if (s.equals("tab") && s1.equals("tabDettaglioMandato")) {
			CRUDMandatoBP bp = (CRUDMandatoBP)actioncontext.getBusinessProcess();
	        CRUDController crudcontroller = getController(actioncontext, "main.DocumentiPassiviSelezionati");
			MandatoBulk mandato = (MandatoBulk)bp.getModel();
			if (mandato!=null && 
				crudcontroller != null &&
				crudcontroller.getElements().hasMoreElements() &&
				crudcontroller.getSelection().isEmpty() &&
				crudcontroller.getSelection().getFocus()==-1){
				crudcontroller.getSelection().setFocus(0);
				return doSelection(actioncontext, "main.DocumentiPassiviSelezionati");
			}
		}
		return forward;
	}
	catch(Exception exception)
	{
	    return handleException(actioncontext, exception);
	}
}
public Forward doSelezionaRigaSiopeDaCompletare(ActionContext actioncontext) {
	try 
	{
		fillModel(actioncontext);
		CRUDMandatoBP bp = (CRUDMandatoBP)actioncontext.getBusinessProcess();
		super.doTab(actioncontext, "tab", "tabDettaglioMandato");
		bp.selezionaRigaSiopeDaCompletare(actioncontext);
		return actioncontext.findDefaultForward();
	}
	catch(Exception exception)
	{
	    return handleException(actioncontext, exception);
	}
}
}
