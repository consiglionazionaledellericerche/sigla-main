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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bp.CRUDWorkpackageBP;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneBP;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneModificaBP;
import it.cnr.contab.doccont00.bp.CRUDObbligazioneResBP;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.bp.MandatoAutomaticoWizardBP;
import it.cnr.contab.doccont00.bp.ProspettoSpeseCdrBP;
import it.cnr.contab.doccont00.bp.SelezionatoreAssestatoDocContBP;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * (Obbligazione)
 */
public class CRUDObbligazioneAction extends CRUDAbstractObbligazioneAction {
public CRUDObbligazioneAction() {
	super();
}
/**
 * Gestisce il comando di aggiunta di una nuova scadenza
	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */ 
public Forward doAddToCRUDMain_Scadenzario(ActionContext context) 
{
	try 
	{
		((CRUDObbligazioneBP)getBusinessProcess( context )).addScadenza( context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doBlankSearchFind_creditore(ActionContext context,ObbligazioneBulk obbligazione) 
{
	try 
	{
		obbligazione.setCreditore(new TerzoBulk());
		obbligazione.getCreditore().setAnagrafico( new AnagraficoBulk());
		obbligazione.setIncarico_repertorio(new Incarichi_repertorioBulk());
		obbligazione.setContratto(new ContrattoBulk());
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento dei capitoli di spesa
 	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBlankSearchFind_elemento_voce(ActionContext context, ObbligazioneBulk obbligazione) 
{
	try 
	{
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		bp.annullaImputazioneFinanziariaCapitoli( context );
		obbligazione.setElemento_voce( new Elemento_voceBulk() );
		obbligazione.setElemento_voce_next( new Elemento_voceBulk() );
		/*
		// reset dei campi della disponibilità di cassa dei due esercizi successivi a quello di scrivania
		obbligazione.setIm_disp_cassa_cds1( null );
		obbligazione.setIm_disp_cassa_cds2( null );
		*/
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce la validazione di nuovo terzo creato
 	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code> 
	 * @param terzo Oggetto di tipo <code>TerzoBulk</code> che rappresenta il nuovo terzo creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_creditore(ActionContext context, ObbligazioneBulk obbligazione, TerzoBulk terzo) 
{
	try 
	{
		if (terzo != null )
		{
			obbligazione.validateTerzo( terzo);
			obbligazione.setCreditore( terzo );
		}	
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento delle nuove linee di attività
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_linea_attivita(ActionContext context) 
{
	try 
	{
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP) context.getBusinessProcess();
	    it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt = (it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk) bp.getNuoveLineeDiAttivita().getModel();
		HookForward caller = (HookForward)context.getCaller();	    
	    it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt = (it.cnr.contab.config00.latt.bulk.WorkpackageBulk) caller.getParameter("bringback");;

		((CRUDObbligazioneBP)getBusinessProcess( context )).validaNuovaLineaAttivita( context, nuovaLatt, latt);
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento delle nuove linee di attività
 	 * @param context <code>ActionContext</code> in uso.
	 * @param nuovaLatt Oggetto di tipo <code>Linea_attivitaBulk</code> (istanza doc contabili)
	 * @param latt Oggetto di tipo <code>Linea_attivitaBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDCrea_linea_attivita(ActionContext context, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) 
{
	try 
	{
		((CRUDObbligazioneBP)getBusinessProcess( context )).validaNuovaLineaAttivita( context, nuovaLatt, latt);
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento dei capitoli di spesa
 	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code>
	 * @param capitolo Oggetto di tipo <code>Elemento_voceBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackSearchFind_elemento_voce(ActionContext context, ObbligazioneBulk obbligazione, Elemento_voceBulk capitolo) 
{
	try 
	{
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);	
		if ( capitolo != null ){
			if(obbligazione!=null && !obbligazione.isFromDocAmm() && obbligazione.getCd_elemento_voce()!=null &&
					obbligazione.getCd_iniziale_elemento_voce()!=null &&
					obbligazione.getCd_iniziale_elemento_voce().compareTo(obbligazione.getCd_elemento_voce())!=0){
				Elemento_voceBulk  elemento_ini =((Elemento_voceBulk)(((FatturaAttivaSingolaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",FatturaAttivaSingolaComponentSession.class)).completaOggetto(context.getUserContext(),new Elemento_voceBulk(obbligazione.getCd_iniziale_elemento_voce(),capitolo.getEsercizio(),capitolo.getTi_appartenenza(),capitolo.getTi_gestione()))));
				java.util.List obb_sca =obbligazione.getObbligazione_scadenzarioColl();
				BigDecimal ass=new BigDecimal(0);
				for(Iterator i=obb_sca.iterator();i.hasNext();){
					Obbligazione_scadenzarioBulk scad=(Obbligazione_scadenzarioBulk)i.next();
					ass=ass.add(scad.getIm_associato_doc_amm());
				}
				if (capitolo.getFl_inv_beni_patr()!= null && capitolo.getFl_inv_beni_patr().booleanValue() && !elemento_ini.getFl_inv_beni_patr().booleanValue()||
					!capitolo.getFl_inv_beni_patr().booleanValue() && elemento_ini.getFl_inv_beni_patr().booleanValue()||
					(capitolo.getFl_inv_beni_patr().booleanValue() && elemento_ini.getFl_inv_beni_patr().booleanValue() && capitolo.getCd_elemento_voce().compareTo(elemento_ini.getCd_elemento_voce())!=0)&&
				    (ass.compareTo(new BigDecimal(0))!=0)){
					bp.setMessage("Attenzione sarà necessario aggiornare manualmente la parte relativa all'inventario!");
				}
			}
			obbligazione.setElemento_voce( capitolo );
			obbligazione.setElemento_voce_next( new Elemento_voceBulk() );
			// SETTO IL FLAG CHE SERVE PER CAPIRE SE OCCORRE RICHIEDERE L'INSERIMENTO DELLA VOCE NUOVA DA UTILIZZARE PER IL RIBALTAMENTO
			// LA VOCE VIENE RICHIESTA SOLO SE NON PRESENTE L'ASSOCIAZIONE NELLA TABELLA ASS_EVOLD_EVNEWBULK
			obbligazione.setEnableVoceNext(!((ObbligazioneComponentSession)bp.createComponentSession()).existAssElementoVoceNew(context.getUserContext(),(ObbligazioneBulk)obbligazione));
		}
		if ( capitolo != null && bp.isEditable() )
			bp.caricaCentriDiResponsabilita(context);
		return context.findDefaultForward();
	} 
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Gestisce il caricamento delle nuove linee di attività
 	 * @param context <code>ActionContext</code> in uso.
	 * @param nuovaLatt Oggetto di tipo <code>Linea_attivitaBulk</code> (istanza doc contabili)
	 * @param latt Oggetto di tipo <code>Linea_attivitaBulk</code>
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackSearchFind_linea_attivita(ActionContext context, it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk nuovaLatt, it.cnr.contab.config00.latt.bulk.WorkpackageBulk latt) 
{
	try 
	{
		if (latt != null )
		{
			nuovaLatt.getObbligazione().validateNuovaLineaAttivita( nuovaLatt, latt );
			nuovaLatt.setLinea_att( latt );
		}	
		return context.findDefaultForward();
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}

/**
 * Gestisce il cambio del flag imputazione finanziaria automatica o manuale
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCambiaFl_calcolo_automatico(ActionContext context) 
{
	try {
		fillModel( context );		
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		bp.cambiaFl_calcolo_automatico(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la conferma dei dati di testata dell'obbligazione
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCaricaCentriDiResponsabilita(ActionContext context) 
{
	try {
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);		
		Collection capitoliIniziali = ((ObbligazioneBulk)bp.getModel()).getCapitoliDiSpesaCdsSelezionatiColl();
		fillModel( context );					
		if (bp.isDirty() && capitoliIniziali.size() > 0)
			return openConfirm(context,"Attenzione l'imputazione finanziaria corrente verrà persa. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmCaricaCentriDiResponsabilita");
		return doConfirmCaricaCentriDiResponsabilita(context,OptionBP.YES_BUTTON);			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce il caricamento delle linee di attivita
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCaricaLineeAttivita(ActionContext context) 
{
	try {
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		Collection cdrIniziali = ((ObbligazioneBulk)bp.getModel()).getCdrSelezionatiColl();
		fillModel( context );									
		if (bp.isDirty() && cdrIniziali.size() > 0 )
			return openConfirm(context,"Attenzione l'imputazione finanziaria corrente verrà persa. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmCaricaLineeAttivita");
		return doConfirmCaricaLineeAttivita(context,OptionBP.YES_BUTTON);			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la conferma delle linee di attivita
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doConfermaLineeAttivita(ActionContext context) 
{
	try {
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		fillModel( context );
		ObbligazioneBulk obbligazione = (ObbligazioneBulk)bp.getModel();		
		if (bp.isDirty() && obbligazione.hasDettagli() )
			return openConfirm(context,"Attenzione i dettagli delle scadenze saranno persi. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmConfermaLineeAttivita");		
		return doConfirmConfermaLineeAttivita(context,OptionBP.YES_BUTTON);			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Fine modalita modifica/inserimento scadenza
 * Intercetta la selezione del bottone di Conferma della scadenza
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doConfermaScadenza(ActionContext context) 
{
	try 
	{
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)bp.getScadenzario().getModel();
		//salvo l'importo prima di rileggere il nuovo valore
//		scadenza.setIm_iniziale_scadenza( scadenza.getIm_scadenza() );
		fillModel( context );
		bp.confermaScadenza(context);
		return context.findDefaultForward();
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
  	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di conferma
	 *
	 * @return <code>Forward</code>
 */
public Forward doConfirmCaricaCentriDiResponsabilita(ActionContext context,int option) {
	try
	{
		if (option == OptionBP.YES_BUTTON) 
		{
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
			bp.caricaCentriDiResponsabilita(context);		
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
   	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di conferma
	 *
	 * @return <code>Forward</code>
 */
public Forward doConfirmCaricaLineeAttivita(ActionContext context,int option) {
	try
	{
		if (option == OptionBP.YES_BUTTON) 
		{
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
			bp.caricaLineeAttivita(context);		
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
   	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di conferma
	 *
	 * @return <code>Forward</code>
 */
public Forward doConfirmConfermaLineeAttivita(ActionContext context,int option) {
	try
	{
		if (option == OptionBP.YES_BUTTON) 
		{
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
			bp.confermaLineeAttivita(context);		
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * Gestisce un HookForward di ritorno dalla conferma del caricamento Cdr
   	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di conferma
	 *
	 * @return <code>Forward</code>
 */
public Forward doConfirmTabImputazioneFin(ActionContext context,OptionBP option) 
{
	try
	{
		if ( option.getOption() == OptionBP.YES_BUTTON) 
		{
			super.doTab( context, (String)option.getAttribute( "tabName"), (String)option.getAttribute( "pageName") );
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * Gestisce la copia di una obbligazione
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doCopiaObbligazione(ActionContext context) 
{
	try {
		fillModel( context );
		BulkBP bulkbp = (BulkBP)context.getBusinessProcess();
        if(bulkbp.isDirty())
            return openContinuePrompt(context, "doConfirmCopiaObbligazione");
        else
            return doConfirmCopiaObbligazione(context, 4);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la copia di una obbligazione
  	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doConfirmCopiaObbligazione(ActionContext context, int option) 
{
	try {
		if (option == OptionBP.YES_BUTTON) {
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
			bp.copiaObbligazione( context );
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce una richiesta di creazione contestuale
 */
public Forward doCRUDCrea_linea_attivita(ActionContext context) 
{
	try 
	{
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP) context.getBusinessProcess();
		CRUDWorkpackageBP newBp = (CRUDWorkpackageBP) context.createBusinessProcess( "CRUDLinea_attivitaBP", new Object[] { "MRWS" } );
		newBp.reset( context );	
		context.addHookForward("bringback",this,"doBringBackCRUDCrea_linea_attivita");
//		HookForward hook = (HookForward)context.findForward("bringback");
//		hook.addParameter("field",field);
		return context.addBusinessProcess( newBp );
	
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la validazione di nuovo contratto creato
	 * @param context <code>ActionContext</code> in uso.
	 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code> 
	 * @param contratto Oggetto di tipo <code>ContrattoBulk</code> che rappresenta il nuovo contratto creato
	 *
	 * @return <code>Forward</code>
 */
public Forward doBringBackCRUDFind_contratto(ActionContext context, ObbligazioneBulk obbligazione, ContrattoBulk contratto)
{
	try 
	{
		if (contratto != null )
		{
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
			
			bp.validaContratto( context, contratto);
			obbligazione.setContratto( contratto );
		}	
		return doBringBackSearchFind_contratto(context, obbligazione, contratto);
	}
	catch(it.cnr.jada.action.MessageToUser e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	
	catch(Throwable e) {return handleException(context,e);}
}
/**
 * Inizio modalita modifica scadenza
 * Intercetta la selezione del bottone di Edit della scadenza
   	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doEditaScadenza(ActionContext context) 
{
	try 
	{
		fillModel( context );
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		bp.editaScadenza(context);
		return context.findDefaultForward();
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce un comando di cancellazione.
 */
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {
	try {
		fillModel(context);

		CRUDBP bp = getBusinessProcess(context);
		if (!bp.isEditing()) {
			bp.setMessage("Non è possibile cancellare in questo momento");
		} else {
			bp.delete(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
	 * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
	 * la disponibilità per il capitolo
	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di sfondamento
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>RemoteException</code>
	 *
	 */

public Forward doOnCheckDisponibilitaCassaFailed( ActionContext context, int option) 
{

	if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
	{
		try 
		{
			CRUDBP bp = getBusinessProcess(context);
			((ObbligazioneBulk)bp.getModel()).setCheckDisponibilitaCassaEseguito( true );
			if (bp.isBringBack())
				return doConfermaRiporta(context,option);
			else
				doSalva(context);
		} 
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	return context.findDefaultForward();
}
/**
	 * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
	 * la disponibilità per il contratto
	 * @param context <code>ActionContext</code> in uso.
	 * @param option Esito della risposta alla richiesta di sfondamento
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>RemoteException</code>
	 *
	 */

public Forward doOnCheckDisponibilitaContrattoFailed( ActionContext context, int option) 
{

	if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
	{
		try 
		{
			CRUDBP bp = getBusinessProcess(context);
			((ObbligazioneBulk)bp.getModel()).setCheckDisponibilitaContrattoEseguito( true );
			if (bp.isBringBack())
				return doConfermaRiporta(context,option);
			else
				doSalva(context);
		} 
		catch(Throwable e) 
		{
			return handleException(context,e);
		}
	}
	return context.findDefaultForward();
}
/**
 * Metodo utilizzato per gestire la conferma dell'inserimento/modifica di una obbligazione che ha sfondato
 * la disponibilità per l'incarico conferiro
 * @param context <code>ActionContext</code> in uso.
 * @param option Esito della risposta alla richiesta di sfondamento
 *
 * @return <code>Forward</code>
 *
 * @exception <code>RemoteException</code>
 *
 */

public Forward doOnCheckDisponibilitaIncaricoRepertorioFailed( ActionContext context, int option) 
{

if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
{
	try 
	{
		CRUDBP bp = getBusinessProcess(context);
		((ObbligazioneBulk)bp.getModel()).setCheckDisponibilitaIncaricoRepertorioEseguito( true );
		if (bp.isBringBack())
			return doConfermaRiporta(context,option);
		else
			doSalva(context);
	} 
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
return context.findDefaultForward();
}
//
//	Prima di riportare l'oggetto riordino le scadenze e riseleziono la
//	scadenza da riportare (selezionata prima dell'ordinamento).
//	Metodo implementato per evitare che la query che va a rileggere le scadenze, 
//	che	e' ordinata per data, disallinei la selezione della scadenza da riportare
//
public Forward doRiporta(ActionContext context) 
{
    CRUDObbligazioneBP bp = (CRUDObbligazioneBP) getBusinessProcess(context);
    ObbligazioneBulk obbligazione = (ObbligazioneBulk) bp.getModel();
    
   	if((obbligazione.getObbligazione_scadenzarioColl() == null ) ||
	   (obbligazione.getObbligazione_scadenzarioColl().isEmpty()))
        return doRiportaCondizionato(context);
        
    Obbligazione_scadenzarioBulk scadSelezionata = (Obbligazione_scadenzarioBulk)bp.getScadenzario().getModel();

	if(scadSelezionata == null)
        return doRiportaCondizionato(context);

	obbligazione.setObbligazione_scadenzarioColl(scadSelezionata.ordinaPerDataScadenza(obbligazione.getObbligazione_scadenzarioColl()));
	
/* simona 10.5.2002 l'indexOfPrimaryKey non funziona sulle scadenze nuove che non hanno la schiave valorizzata */
 //   int index = BulkCollections.indexOfByPrimaryKey(bp.getScadenzario().getDetails(),scadSelezionata);
 
 	int index = -1;
	java.util.ListIterator e = bp.getScadenzario().getDetails().listIterator();
    while (e.hasNext())
		if (scadSelezionata.getPg_obbligazione_scadenzario().longValue() == ((Obbligazione_scadenzarioBulk)e.next()).getPg_obbligazione_scadenzario().longValue())
		    index =  e.previousIndex();
	
	bp.getScadenzario().setModelIndex(context, index);

	try	{
		//utilizzata per compensi
		fillModel(context);
		bp.verificaObbligazionePerDocAmm( context );

	}catch ( BusinessProcessException be ){
		return handleException(context, be);
	}catch ( FillException fe){
		return handleException(context, fe);
	}
    
    return doRiportaCondizionato(context);
}

public Forward doRiportaCondizionato(ActionContext context) 
{
    CRUDObbligazioneBP bp = (CRUDObbligazioneBP) getBusinessProcess(context);

	try 
	{
	
		StringBuffer errControllo = new StringBuffer();
		fillModel(context);
		bp.validate(context);

		// in questo caso l'obbl. modifica è stata già effettuata
		if(((ObbligazioneBulk) bp.getModel()).isCheckDisponibilitaContrattoEseguito()&&
		   ((ObbligazioneBulk) bp.getModel()).isCheckDisponibilitaIncaricoRepertorioEseguito())
			return super.doRiporta(context);

		if (bp.modificaObbligazioneResProprie(context,errControllo)) {
			if (!errControllo.toString().equals("")) {
	
				String message = errControllo + "." +
					"Si vuole creare un movimento di modifica dell'impegno residuo?";
				openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doModificaConfermataRiporta");
				
				return context.findDefaultForward();
			}
		}
		else {
			// altrimenti cancello l'eventuale modifica precedente
			if (((ObbligazioneBulk) bp.getModel()).isObbligazioneResiduo()) {
				ObbligazioneResBulk	obbligazione = (ObbligazioneResBulk) bp.getModel();
				if (obbligazione.getObbligazione_modifica()!=null && obbligazione.getObbligazione_modifica().getPg_modifica()!=null) {
					((CRUDObbligazioneResBP)bp).cancellaObbligazioneModTemporanea(context,obbligazione.getObbligazione_modifica());
					obbligazione.setObbligazione_modifica(new Obbligazione_modificaBulk());
				}
			}
		}
	
	} catch(Exception e1) {
		return handleException(context,e1);
	}
	return super.doRiporta(context);
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
   	 * @param context <code>ActionContext</code> in uso.
	 * @param tabName Nome del tab in uso.
	 * @param pageName Nome della pagina in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doTab(ActionContext context,String tabName,String pageName) 
{
	try
	{
		ObbligazioneBulk obbligazione;
		fillModel( context );
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		// validiamo anche se non è editabile, come nel caso dei residui propri 
		bp.getModel().validate();
		if ( bp.isEditable() )
		{
			
			if ( bp.getTab( tabName ).equalsIgnoreCase("tabObbligazione") )
			{
				bp.getModel().validate();
				if ( ((ObbligazioneBulk) bp.getModel()).getElemento_voce().getCrudStatus() == bp.getModel().UNDEFINED )
					doSearch( context, "main.find_elemento_voce" );
				if ( bp.getMessage() != null )
				{
					//bp.setMessage("La ricerca della Voce del Piano non ha fornito alcun risultato.");
					return context.findDefaultForward();
				}	
				bp.verificaTestataObbligazione( context );
			}
			else if ( bp.getTab( tabName ).equalsIgnoreCase("tabImputazioneFin") )
			{
				bp.getModel().validate(); 
				if (((ObbligazioneBulk) bp.getModel()).getInternalStatus() == ObbligazioneBulk.INT_STATO_CDR_CONFERMATI )
				{
					OptionBP option = openConfirm(context,"Le linee di attività non sono state confermate. Si intende proseguire?",OptionBP.CONFIRM_YES_NO,"doConfirmTabImputazioneFin");
					option.addAttribute("tabName",tabName);
					option.addAttribute("pageName",pageName);					
					return option;
				}	
			}	
		}	
		Forward frw = super.doTab( context, tabName, pageName );
		if (bp instanceof CRUDObbligazioneResBP) ((CRUDObbligazioneResBP)bp).setStatusAndEditableMap();
		return frw;		
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
}
/**
 * Fine modalita modifica/inserimento scadenza
 * Intercetta la selezione del bottone di Undo della scadenza
   	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */

public Forward doUndoScadenza(ActionContext context) 
{
	try 
	{
//		fillModel( context );
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		bp.undoScadenza(context);
		return context.findDefaultForward();
	}		
	catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Visualizza il prospetto spese per Cdr e linea attività
 	 * @param context <code>ActionContext</code> in uso.
	 *
	 * @return <code>Forward</code>
 */
public Forward doVisualizzaSpeseCdr(ActionContext context) 
{
	try {
		fillModel( context );
		context.addHookForward("close",this,"doDefault");		
		CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
		CdrBulk cdrSelezionato = (CdrBulk) bp.getCentriDiResponsabilita().getModel();
	
		ProspettoSpeseCdrBP prospettoBP = (ProspettoSpeseCdrBP) context.createBusinessProcess( "ProspettoSpeseCdrBP" );
		ProspettoSpeseCdrBulk prospetto = new ProspettoSpeseCdrBulk( cdrSelezionato );
		prospettoBP.setModel( context, prospetto );
		prospettoBP.refreshSpeseCdr( context );
		return context.addBusinessProcess(prospettoBP);		
		
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	/**
	 * Metodo utilizzato per gestire l'eccezione generata dallo sfondamento della disponibilità dei CdR da PdG
	 *
	 * @param context <code>ActionContext</code> in uso.
	 * @param ex Eccezione da gestire.
	 *
	 * @return <code>Forward</code>
	 *
	 * @exception <code>RemoteException</code>
	 *
	 */

public Forward handleException(ActionContext context, Throwable ex) 
{
	try {
		throw ex;
	}
	/*
	catch(it.cnr.contab.doccont00.comp.CheckDisponibilitaCassaFailed e) {
		String message = "L'importo dei dettagli inseriti supera la disponiblità di cassa relativa al capitolo e al CdS.\n"
						+ "Vuoi continuare?";
		try {
			return openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaSfondamentoPdG");
		} catch(BusinessProcessException be) {
			return handleException(context,be);
		}
	} */
	catch(it.cnr.contab.doccont00.comp.SfondamentoPdGException e) {
		String message = "La disponiblità prevista nel PdG per i CdR e' stata superata.\n"
						+ "Vuoi continuare?";
		try {
			return openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doConfermaSfondamentoPdG");			
		} catch(BusinessProcessException be) {
			return handleException(context,be);
		}
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
	}
	
	public Forward doBringBackSearchFind_contratto(ActionContext context, ObbligazioneBulk obbligazione, ContrattoBulk contratto) 
	{
		try 
		{
			if ( contratto != null ){
				if(obbligazione.getCreditore() == null ||(obbligazione.getCreditore() != null && obbligazione.getCreditore().getCd_terzo()==null))
				  obbligazione.setCreditore(contratto.getFigura_giuridica_esterna());
				if(obbligazione.getDs_obbligazione() == null)
				  obbligazione.setDs_obbligazione(contratto.getOggetto()); 
				obbligazione.setContratto(contratto);   
				obbligazione.setIncarico_repertorio(null);
			}
			return context.findDefaultForward();
		} 
		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doOnCheckDisponibilitaCdrGAEFailed( ActionContext context, int option) 
	{
		if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) 
		{
			try 
			{
				CRUDBP bp = getBusinessProcess(context);
				((ObbligazioneBulk)bp.getModel()).setCheckDisponibilitaCdrGAEEseguito( true );
				if (bp.isBringBack())
					return doConfermaRiporta(context,option);
				else
					doSalva(context);
			} 
			catch(Throwable e) 
			{
				return handleException(context,e);
			}
		}
		return context.findDefaultForward();
	}
	
	public OptionBP createOptionBP(ActionContext actioncontext,String s,int i,int j,String s1) {
		OptionBP optionBP = super.createOptionBP(actioncontext, s, i, j, s1);
		if (s1!= null && s1.equals("doOnCheckDisponibilitaCdrGAEFailed")){
			optionBP.setMessageStatus(OptionBP.ERROR_MESSAGE);
			optionBP.setNowrap(true);
		}
		return optionBP;
	}
	public Forward doConsultaInserisciVoce(ActionContext context) {
		try {
			fillModel(context);
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
			ObbligazioneBulk obbligazione = (ObbligazioneBulk)bp.getModel();

			bp.getModel().validate();

			if (obbligazione == null || obbligazione.getCd_elemento_voce()==null)
				throw new ValidationException("E' necessario selezionare una voce");

			if ( obbligazione.getElemento_voce().getCrudStatus() != OggettoBulk.NORMAL )
				doSearch( context, "main.find_elemento_voce" );
			if ( bp.getMessage() != null )
			{
				bp.setMessage("La ricerca della Voce del Piano non ha fornito alcun risultato.");
				return context.findDefaultForward();
			}	
			bp.verificaTestataObbligazione( context );

			SelezionatoreAssestatoDocContBP nbp = (SelezionatoreAssestatoDocContBP)context.createBusinessProcess("SelezionatoreAssestatoDocContBP",
											new Object[]{ 
											"M",
											getBusinessProcess(context).getModel(),
											obbligazione.getIm_obbligazione(),
											CostantiTi_gestione.TI_GESTIONE_SPESE});

			if (bp instanceof CRUDObbligazioneResBP) 
			{
				nbp.impostaModalitaMappa(context, nbp.MODALITA_CONSULTAZIONE);
			}
			else
			{
				nbp.caricaSelezioniEffettuate(context, obbligazione);
				nbp.impostaModalitaMappa(context, nbp.MODALITA_INSERIMENTO_IMPORTI);
				context.addHookForward("seleziona",this,"doRiportaSelezioneVoci");
			}

			return context.addBusinessProcess(nbp);
		} catch(Throwable e) {
			return handleException(context,e);
		} 
	}

	public Forward doRiportaSelezioneVoci(ActionContext context)  throws java.rmi.RemoteException {
		try {
			fillModel( context );					
			HookForward caller = (HookForward)context.getCaller();
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
			ObbligazioneBulk obbligazione = (ObbligazioneBulk)bp.getModel();
			Collection capitoliIniziali = ((ObbligazioneBulk)bp.getModel()).getCapitoliDiSpesaCdsSelezionatiColl();

			java.util.List vociList = (java.util.List)caller.getParameter("selectedElements");
			if (vociList!=null && !vociList.isEmpty()) {
				if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(context.getUserContext(), CNRUserContext.getEsercizio(context.getUserContext()))) {
					for ( Iterator s = vociList.iterator(); s.hasNext(); ) {
						V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
						if (Optional.ofNullable(voceSel.getProgetto_dt_inizio())
								.map(dt->dt.after(obbligazione.getDt_registrazione()))
								.orElse(Boolean.FALSE)) {
								bp.setMessage("Attenzione! GAE "+voceSel.getCd_linea_attivita()+" non selezionabile. "
												+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(voceSel.getProgetto_dt_inizio())
												+ ") del progetto "+voceSel.getCd_modulo()+" associato è successiva "
												+ "rispetto alla data di registrazione dell'impegno ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione())+").");
								return context.findDefaultForward();
						}
						
						LocalDate localDateFineProgetto = Optional.ofNullable(voceSel.getProgetto_dt_proroga())
								.orElse(Optional.ofNullable(voceSel.getProgetto_dt_fine())
										.orElse(DateUtils.firstDateOfTheYear(3000))).toLocalDateTime().toLocalDate();

						int ggProroga = Optional.ofNullable(obbligazione.getElemento_voce())
												.flatMap(el->{
													if (obbligazione.isCompetenza())
														return Optional.ofNullable(el.getGg_deroga_obbl_comp_prg_scad());
													else
														return Optional.ofNullable(el.getGg_deroga_obbl_res_prg_scad());
												})
												.filter(el->el.compareTo(0)>0)
												.orElse(0);

						localDateFineProgetto = localDateFineProgetto.plusDays(ggProroga);

						if (localDateFineProgetto.isBefore(obbligazione.getDt_registrazione().toLocalDateTime().toLocalDate()))
							throw new ApplicationMessageFormatException("Attenzione! GAE {0} non selezionabile. "
										+ "La data fine/proroga del progetto {1} {2} ({3}) è precedente rispetto alla data di registrazione dell''impegno ({4}).",
									voceSel.getCd_linea_attivita(),
									voceSel.getCd_modulo(),
									(ggProroga>0?", aumentata di " + ggProroga +" giorni,":""),
									localDateFineProgetto.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
									new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione()));
					}
				}

				bp.setVociSelezionate(vociList);
				if (bp.getVociSelezionate().get(0) instanceof V_assestatoBulk) {
					BigDecimal totaleSel = new BigDecimal( 0 ); 
					for ( Iterator s = bp.getVociSelezionate().iterator(); s.hasNext(); ) {
						V_assestatoBulk voceSel = (V_assestatoBulk) s.next();
						totaleSel = totaleSel.add( Utility.nvl(voceSel.getImp_da_assegnare()) );
					}
					if (totaleSel.compareTo(new BigDecimal(0))==1 &&
					    totaleSel.compareTo(obbligazione.getIm_obbligazione())!=0)
						if  (obbligazione.hasDettagli())
							return openConfirm(context,"Attenzione!! I dettagli delle scadenze saranno persi e l'importo dell'impegno verrà aggiornato al nuovo valore selezionato di " + new it.cnr.contab.util.EuroFormat().format(totaleSel) + ". Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRiportaSelezioneVoci");
						else
							return openConfirm(context,"Attenzione!! L'importo dell'impegno verrà aggiornato al nuovo valore selezionato di " + new it.cnr.contab.util.EuroFormat().format(totaleSel) + ". Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRiportaSelezioneVoci");
				}
				if (obbligazione.hasDettagli() )
					return openConfirm(context,"Attenzione i dettagli delle scadenze saranno persi. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRiportaSelezioneVoci");		
				else if (obbligazione.getCdrSelezionatiColl().size() > 0)
					return openConfirm(context,"Attenzione l'imputazione finanziaria corrente verrà persa. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRiportaSelezioneVoci");
				return doConfirmRiportaSelezioneVoci(context,OptionBP.YES_BUTTON);
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	
	public Forward doConfirmRiportaSelezioneVoci(ActionContext context,int option) {
		try
		{
			if (option == OptionBP.YES_BUTTON) 
			{
				CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
				bp.riportaSelezioneVoci(context, bp.getVociSelezionate());
				/*serve per impostare la mappa con la scadenza creata già evidenziata*/
				if (((ObbligazioneBulk)bp.getModel()).hasDettagli()){
					bp.getScadenzario().getSelection().setFocus(0);
					bp.getScadenzario().setSelection(context);
				}
				bp.setTab("tab","tabScadenzario");
				bp.setTab("tabScadenzario","tabScadenza");
			}
			return context.findDefaultForward();
		} catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Metodo utilizzato per emettere il mandato relativo al CdS selezionato.
	 	 * @param context <code>ActionContext</code> in uso.
		 *
		 * @return <code>Forward</code>
	 */
	public Forward doApriMandatoWizard(ActionContext context) 
	{
		try 
		{
			fillModel( context );
			BulkBP bulkbp = (BulkBP)context.getBusinessProcess();
            if(bulkbp.isDirty())
                return openContinuePrompt(context, "doConfirmApriMandatoWizard");
            else
                return doConfirmApriMandatoWizard(context,4);
		}		
		catch(Throwable e) {
			return handleException(context,e);
		}
	}
	/**
	 * Metodo utilizzato per emettere il mandato relativo al CdS selezionato.
	 	 * @param context <code>ActionContext</code> in uso.
		 *
		 * @return <code>Forward</code>
	 */
	public Forward doConfirmApriMandatoWizard(ActionContext context, int option) 
	{
		try 
		{
			if (option == OptionBP.YES_BUTTON) {
				CRUDBP bp = (CRUDBP)context.getBusinessProcess();
				if (bp.isDirty())
					if (bp.getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.UNDEFINED ||
						bp.getModel().getCrudStatus() == it.cnr.jada.bulk.OggettoBulk.TO_BE_CREATED)
						bp.reset( context );
					else	
						bp.edit( context, bp.getModel(), true );

				ObbligazioneBulk obbligazione = (ObbligazioneBulk)bp.getModel();
				MandatoAutomaticoWizardBP wizard = 
					(MandatoAutomaticoWizardBP) context.createBusinessProcess( 
						"MandatoAutomaticoWizardBP", 
						new Object[] {"M", 
									   obbligazione.getCd_terzo(),
									   obbligazione.isCompetenza()?MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_COMPETENZA:
										   						   MandatoAutomaticoWizardBulk.IMPEGNI_TIPO_RESIDUO,
									   MandatoAutomaticoWizardBulk.AUTOMATISMO_DA_IMPEGNI} );		
				context.addHookForward("close",this,"doRefreshObbligazione");
				return context.addBusinessProcess(wizard);			
			}
			return context.findDefaultForward();
		}		
		catch(Throwable e) {
			return handleException(context,e);
		}
	}
	public Forward doRefreshObbligazione(ActionContext context)
	{
		try 
		{
			if ( context.getBusinessProcess() instanceof CRUDObbligazioneBP)
			{
				CRUDBP bp = (CRUDBP) context.getBusinessProcess();
				if (bp.getModel().getCrudStatus() != it.cnr.jada.bulk.OggettoBulk.UNDEFINED)
					bp.edit( context, bp.getModel(), true );
				return context.findDefaultForward();
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doRaggruppaScadenze(ActionContext context)
	{
		try 
		{
			if ( context.getBusinessProcess() instanceof CRUDObbligazioneBP)
			{
				fillModel( context );
				CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
	            if  (bp.getScadenzario().getSelection().size()<2) {
					bp.setMessage("Attenzione! Per accorpare le scadenze occorre selezionarne almeno due.");
					return context.findDefaultForward();
	            }
				else
					return openConfirm(context,"Attenzione!! Le scadenze selezionate saranno accorpate. Vuoi continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRaggruppaScadenze");
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	public Forward doConfirmRaggruppaScadenze(ActionContext context, int option) 
	{
		try 
		{
			if (option == OptionBP.YES_BUTTON) {
				CRUDObbligazioneBP bp = (CRUDObbligazioneBP)context.getBusinessProcess();
				ObbligazioneBulk obbligazione = (ObbligazioneBulk)bp.getModel();
				obbligazione.raggruppaScadenze(new BulkList(bp.getScadenzario().getSelectedModels(context)));
				bp.setDirty(Boolean.TRUE);
				bp.getScadenzario().getSelection().clear();
				return context.findDefaultForward();
			}
			return context.findDefaultForward();
		} catch(Exception e) {
			return handleException(context,e);
		}
	}

	public Forward doSalva(ActionContext context) throws java.rmi.RemoteException {
		try 
		{
			CRUDObbligazioneBP bp= (CRUDObbligazioneBP) getBusinessProcess(context);

			StringBuffer errControllo = new StringBuffer();
			fillModel(context);
			bp.validate(context);

			// in questo caso l'obbl. modifica è stata già effettuata
			if(((ObbligazioneBulk) bp.getModel()).isCheckDisponibilitaContrattoEseguito() &&
			   ((ObbligazioneBulk) bp.getModel()).isCheckDisponibilitaIncaricoRepertorioEseguito())
				return super.doSalva(context);
			
			if (bp instanceof CRUDObbligazioneResBP) {
				if (bp.modificaObbligazioneResProprie(context,errControllo)) {
					if (!errControllo.toString().equals("")) {
	
						String message = errControllo + "." +
							"Si vuole creare un movimento di modifica dell'impegno residuo?";
						openConfirm(context, message, it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO, "doModificaConfermata");
						
						return context.findDefaultForward();
					}
				}
				else {
					// altrimenti cancello l'eventuale modifica precedente
					if (((ObbligazioneBulk) bp.getModel()).isObbligazioneResiduo()) {
						ObbligazioneResBulk	obbligazione = (ObbligazioneResBulk) bp.getModel();
						if (obbligazione.getObbligazione_modifica()!=null && obbligazione.getObbligazione_modifica().getPg_modifica()!=null) {
							((CRUDObbligazioneResBP)bp).cancellaObbligazioneModTemporanea(context,obbligazione.getObbligazione_modifica());
							obbligazione.setObbligazione_modifica(new Obbligazione_modificaBulk());
						}
					}
				}
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return super.doSalva(context);
	}

	public Forward doModificaConfermata(ActionContext context, int opt) throws RemoteException {
		try 
		{
			CRUDObbligazioneBP bp= (CRUDObbligazioneBP) getBusinessProcess(context);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) bp.getModel();
			boolean viewMode = bp.isViewing();
			//String status = viewMode ?"V":"M";
			String status = "M";
			CRUDObbligazioneModificaBP newbp = null;
			if (opt == OptionBP.YES_BUTTON) {
				// controlliamo prima che abbia l'accesso al BP
				// per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDObbligazioneModificaBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle modifiche\nagli impegni residui. Impossibile continuare.");

				newbp = (CRUDObbligazioneModificaBP) context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneModificaBP",new Object[] { status + "RSWTh",  obbligazione, CRUDObbligazioneModificaBP.TIPO_ACCESSO_MODIFICA });
				context.addHookForward("bringback",this,"doBringBackObbligazioniModificaWindow");
				//context.addHookForward("close",this,"doBringBackObbligazioniModificaWindow");
				//HookForward hook = (HookForward)context.findForward("bringback");
				return context.addBusinessProcess(newbp);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}
	public Forward doModificaConfermataRiporta(ActionContext context, int opt) throws RemoteException {
		try 
		{
			CRUDObbligazioneBP bp= (CRUDObbligazioneBP) getBusinessProcess(context);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) bp.getModel();
			boolean viewMode = bp.isViewing();
			//String status = viewMode ?"V":"M";
			String status = "M";
			CRUDObbligazioneModificaBP newbp = null;
			if (opt == OptionBP.YES_BUTTON) {
				// controlliamo prima che abbia l'accesso al BP
				// per dare un messaggio più preciso
				String mode = it.cnr.contab.utenze00.action.GestioneUtenteAction.getComponentSession().validaBPPerUtente(context.getUserContext(),((CNRUserInfo)context.getUserInfo()).getUtente(),((CNRUserInfo)context.getUserInfo()).getUtente().isUtenteComune() ? ((CNRUserInfo)context.getUserInfo()).getUnita_organizzativa().getCd_unita_organizzativa() : "*","CRUDObbligazioneModificaBP");
				if (mode == null) 
					throw new it.cnr.jada.action.MessageToUser("Accesso non consentito alla mappa di creazione delle modifiche\nagli impegni residui. Impossibile continuare.");

				newbp = (CRUDObbligazioneModificaBP) context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneModificaBP",new Object[] { status + "RSWTh",  obbligazione, CRUDObbligazioneModificaBP.TIPO_ACCESSO_MODIFICA });
				context.addHookForward("bringback",this,"doBringBackObbligazioniModificaRiportaWindow");
				//context.addHookForward("close",this,"doBringBackObbligazioniModificaWindow");
				//HookForward hook = (HookForward)context.findForward("bringback");
				return context.addBusinessProcess(newbp);
			}
		} catch(Exception e) {
			return handleException(context,e);
		}
		return context.findDefaultForward();
	}

	public Forward doBringBackObbligazioniModificaWindow(ActionContext context) {

		try {
			CRUDObbligazioneResBP bp= (CRUDObbligazioneResBP) getBusinessProcess(context);
			ObbligazioneResBulk obbligazione = (ObbligazioneResBulk) bp.getModel();
			HookForward caller = (HookForward)context.getCaller();
			Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)caller.getParameter("bringback");
			if (obbMod!=null) {
				// cancello l'eventuale modifica temporanea precedente inserita
				if (obbligazione.getObbligazione_modifica()!=null && obbligazione.getObbligazione_modifica().isTemporaneo())
					bp.cancellaObbligazioneModTemporanea(context,obbligazione.getObbligazione_modifica());
				obbligazione.setObbligazione_modifica(obbMod);
				//se provengo da BP che si occupa dell'aggiornamento dei saldi aggiorno
				if (IDefferedUpdateSaldiBP.class.isAssignableFrom( bp.getParent().getClass()))
					obbligazione.setSaldiDaAggiornare(false);
				else
					obbligazione.setSaldiDaAggiornare(true);
				return super.doSalva(context);
			}

		} catch(Throwable ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public Forward doBringBackObbligazioniModificaRiportaWindow(ActionContext context) {

		try {
			CRUDObbligazioneResBP bp= (CRUDObbligazioneResBP) getBusinessProcess(context);
			ObbligazioneResBulk obbligazione = (ObbligazioneResBulk) bp.getModel();
			HookForward caller = (HookForward)context.getCaller();
			Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)caller.getParameter("bringback");
			if (obbMod!=null) {
				// cancello l'eventuale modifica temporanea precedente inserita
				if (obbligazione.getObbligazione_modifica()!=null && obbligazione.getObbligazione_modifica().isTemporaneo())
					bp.cancellaObbligazioneModTemporanea(context,obbligazione.getObbligazione_modifica());
				obbligazione.setObbligazione_modifica(obbMod);
				//se provengo da BP che si occupa dell'aggiornamento dei saldi aggiorno
				if (IDefferedUpdateSaldiBP.class.isAssignableFrom( bp.getParent().getClass()))
					obbligazione.setSaldiDaAggiornare(false);
				else
					obbligazione.setSaldiDaAggiornare(true);
			    return super.doRiporta(context);
			}

		} catch(Throwable ex) {
			return handleException(context, ex);
		}
		return context.findDefaultForward();
	}
	public Forward doApriModificheImpegni(ActionContext context) {
		try 
		{
			CRUDObbligazioneBP bp= (CRUDObbligazioneBP) getBusinessProcess(context);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) bp.getModel();
			if (obbligazione.getFl_gara_in_corso()!=null && obbligazione.getFl_gara_in_corso().booleanValue() &&
					obbligazione.isObbligazioneResiduo()
					&& obbligazione.getStato_obbligazione()!=null && obbligazione.getStato_obbligazione().equals( obbligazione.STATO_OBB_PROVVISORIO ))
			throw new ApplicationException("Non e' possibile modificare un'impegno residuo con gara di appalto in corso di espletamento, si prega di riportarlo indietro dall'esercizio precedente e di renderlo definitivo.");
			CRUDObbligazioneModificaBP newbp = null;
			newbp = (CRUDObbligazioneModificaBP) context.getUserInfo().createBusinessProcess(context,"CRUDObbligazioneModificaBP",new Object[] { "V",  obbligazione, CRUDObbligazioneModificaBP.TIPO_ACCESSO_VISUALIZZAZIONE });
			context.addBusinessProcess(newbp);
			return doCerca(context);

		} catch(Exception e) {
			return handleException(context,e);
		}
	}
	/**
	 * Gestisce la validazione di nuovo repertorio incarico creato
		 * @param context <code>ActionContext</code> in uso.
		 * @param obbligazione Oggetto di tipo <code>ObbligazioneBulk</code> 
		 * @param incarico Oggetto di tipo <code>Incarichi_repertorioBulk</code> che rappresenta il nuovo incarico creato
		 *
		 * @return <code>Forward</code>
	 */
	public Forward doBringBackCRUDCrea_incarico_repertorio(ActionContext context, ObbligazioneBulk obbligazione, Incarichi_repertorioBulk incarico) 
	{
		try 
		{
			if (incarico != null )
			{
				CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
				
				bp.validaIncaricoRepertorio( context, incarico);
				obbligazione.setIncarico_repertorio( incarico );
			}	
			return doBringBackSearchFind_incarico_repertorio(context, obbligazione, incarico);
		}
		catch(it.cnr.jada.action.MessageToUser e) 
		{
			getBusinessProcess(context).setErrorMessage(e.getMessage());
			return context.findDefaultForward();
		}		
		
		catch(Throwable e) {return handleException(context,e);}
	}
	public Forward doBringBackSearchFind_incarico_repertorio(ActionContext context, ObbligazioneBulk obbligazione, Incarichi_repertorioBulk incarico) 
	{
		try 
		{
			if ( incarico != null ){
				incarico=(Incarichi_repertorioBulk)Utility.createIncarichiRepertorioComponentSession().inizializzaBulkPerModifica(context.getUserContext(), incarico);
				if(obbligazione.getCreditore() == null ||(obbligazione.getCreditore() != null && obbligazione.getCreditore().getCd_terzo()==null))
				  obbligazione.setCreditore(incarico.getTerzo());
				if(obbligazione.getDs_obbligazione() == null)
				  obbligazione.setDs_obbligazione(incarico.getIncarichi_procedura().getOggetto()); 
				obbligazione.setIncarico_repertorio(incarico);
				obbligazione.setContratto(null);
			}
			return context.findDefaultForward();
		} 
		catch(Throwable e) {return handleException(context,e);}
	}

	public Forward doOnFlGaraInCorsoChange(ActionContext context) {
		try{
			fillModel(context);
			CRUDObbligazioneBP bp = (CRUDObbligazioneBP)getBusinessProcess(context);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk) bp.getModel();
			
			if (obbligazione.getFl_gara_in_corso().booleanValue() && 
				(obbligazione.getCreditore() == null || obbligazione.getCreditore().getCd_terzo()==null))
				bp.caricaTerzoDiversi(context);
			return context.findDefaultForward();	
		}
		catch(Throwable e) {return handleException(context,e);}
	}

	public Forward doOnAnnoObbligazionePluriennaleChange(ActionContext actioncontext) {
		try {


			CRUDObbligazioneBP  bp = (CRUDObbligazioneBP ) getBusinessProcess(actioncontext);
			ObbligazioneBulk model=(ObbligazioneBulk)bp.getModel();
			Obbligazione_pluriennaleBulk riga = (Obbligazione_pluriennaleBulk) bp.getCrudObbligazione_pluriennale().getModel();

			Integer annoCorrente = model.getEsercizio();

			fillModel(actioncontext);

			if( riga.getAnno()==null)
				throw new ApplicationException("Bisogna impostare un anno");

			if ((( ObbligazioneOrdBulk)bp.getModel()).getObbligazioniPluriennali().stream().filter(p->p.getAnno().equals(riga.getAnno())).count()>1)
				throw new ApplicationException("L'anno impostato già esiste nella lista dei pluriennali");

			if(riga.getAnno().compareTo(annoCorrente) <= 0){
				throw new ApplicationException("L'anno di Obbligazione Pluriennale deve essere successivo all'anno corrente");
			}
			return actioncontext.findDefaultForward();
		} catch (Throwable e) {
			return handleException(actioncontext, e);
		}
	}
}

