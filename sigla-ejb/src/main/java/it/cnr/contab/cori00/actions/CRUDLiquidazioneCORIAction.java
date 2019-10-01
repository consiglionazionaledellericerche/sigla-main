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

package it.cnr.contab.cori00.actions;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.cori00.docs.bulk.*;
import it.cnr.contab.cori00.bp.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

/**
 * Action che gestisce le operazioni di CRUD (Create,Read,Update e Delete) 
 * per il BP CRUDLiquidazioneCORIBP
 *
 * <p>Implementa i seguenti comandi:
 * <dl>
 * <dt> doCalcolaLiquidazione
 * <dt> doConfermaNuovo
 * <dt> doLiquidazione
 * </dl>
 *
*/

public class CRUDLiquidazioneCORIAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDLiquidazioneCORIAction() {
	super();
}
/**
  * Calcola Liquidazione
  *
  *  E' stata generata la richiesta di calcolare i le liquidazioni da versare.
  * Prima di tutto vengono controllate le date inserite come inizio e fine periodo per il
  *	calcolo della Liquidazione. Viene poi invocato il metodo,(Liquid_coriComponent.calcolaLiquidazione), 
  *	che farà riferimento alla Stored Procedure che effettuerà le operazioni di calcolo e la 
  *	popolazione delle tabelle interessate.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doCalcolaLiquidazione(ActionContext context) {	

	try{
		fillModel(context);
		CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)getBusinessProcess(context);	
		Liquid_coriBulk liquidazione = (Liquid_coriBulk)bp.getModel();
		Liquid_coriBulk newLiquidazione = new Liquid_coriBulk();
		
		if (liquidazione.getDt_da() == null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un data di Inizio Periodo"));

		if (liquidazione.getDt_a() == null)
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una data di Fine Periodo"));

		if (liquidazione.getDt_da().after(liquidazione.getDt_a()))
			return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: la data di Fine Periodo deve essere maggiore della data di Inizio Perido"));
		
		try{
			newLiquidazione = ((it.cnr.contab.cori00.ejb.Liquid_coriComponentSession)bp.createComponentSession()).calcolaLiquidazione(context.getUserContext(), liquidazione);
			bp.setIsCalcolato(true);
			bp.setModel(context, newLiquidazione);
		} catch (it.cnr.jada.comp.ComponentException e){
			return handleException(context, e);
		} catch (java.rmi.RemoteException e){
			return handleException(context, e);
		} catch (BusinessProcessException e){
			return handleException(context, e);
		}
		
	
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	}
}
/**
  * Gestisce un comando "nuovo".
  *
  * L'implementazione di default utilizza il metodo astratto <code>reset</code>
  * di <code>CRUDBusinessProcess</code>
  *
  * Il metodo è stato reimplementato per far sì che tutte le operazioni effettuate sulle tabelle
  *	fossero resettate. Vengono, inoltre, poste a FALSE le proprietà <code>isCalcolato</code> 
  *	e <code>isLiquidato</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param option <code>int</code> il valore che rappresenta la scelta dell'utente di continuare con le operazioni.
  *
  * @return forward <code>Forward</code>
**/
public Forward doConfermaNuovo(ActionContext context,int option) {
	try {
		CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)context.getBusinessProcess();
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON){
			bp.reset(context);
			bp.setIsCalcolato(false);
			bp.setIsLiquidato(false);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Permette di visualizzare i beni accessori del bene su cui si sta lavorando. 
  *	Apre un'altra finestra, (SelezionatoreBP), che ha come Iterator un Iteratore sui beni 
  *	accessori del bene che è Oggetto Model del BP.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doDettaglioGruppiLocaliFor(ActionContext context) {	

	CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)getBusinessProcess(context);

	Liquid_coriBulk liquid_cori = (Liquid_coriBulk)bp.getModel();
	Liquid_gruppoCoriCRUDController gruppi_controller = (Liquid_gruppoCoriCRUDController)bp.getGruppi();
	Liquid_gruppo_coriIBulk selected = (Liquid_gruppo_coriIBulk)gruppi_controller.getModel();
	try {
		if (selected == null)
			return handleException(context, new it.cnr.jada.comp.ApplicationException("Selezionare un Gruppo."));
		Unita_organizzativaBulk uoOrigineSelected = (Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findUOByCodice(context.getUserContext(),selected.getCd_uo_origine());
		
		if (selected != null 
				//&& selected.getCd_cds().compareTo(selected.getCd_cds_origine())!=0){
				&& (uoOrigineSelected.getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0)){	
			try{	
				it.cnr.jada.util.RemoteIterator ri = ((it.cnr.contab.cori00.ejb.Liquid_coriComponentSession)bp.createComponentSession()).cerca(context.getUserContext(),null, new Liquid_gruppo_coriBulk(), selected, "gruppiLocali");
				ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
				if (ri.countElements() == 0) {
					it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non sono definite liquidazioni locali per il gruppo selezionato");
				}
				SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
				nbp.setIterator(context,ri);
				nbp.disableSelection();
				it.cnr.jada.bulk.BulkInfo aBIF = it.cnr.jada.bulk.BulkInfo.getBulkInfo(Liquid_gruppo_coriIBulk.class);
				nbp.setBulkInfo(aBIF);		
				nbp.setColumns( aBIF.getColumnFieldPropertyDictionary("v_liquid_centro_uo"));
				HookForward hook = (HookForward)context.findForward("seleziona");		
				return context.addBusinessProcess(nbp);		
			}
			catch (Throwable e){		
				return handleException(context,e);
			}
		}
	} catch (it.cnr.jada.comp.ComponentException e){
		return handleException(context, e);
	} catch (java.rmi.RemoteException e){
		return handleException(context, e);
	}
	
	return context.findDefaultForward();

}
/**
  * Esegui Liquidazione
  *
  *  E' stata generata la richiesta di liquidare i CORI selezionati dall'utente.
  * Prima di tutto si controlla che l'utente abbia selezionato degli elemnti validi.
  *	Viene poi invocato il metodo,(Liquid_coriComponent.eseguiLiquidazione), 
  *	che farà riferimento alla Stored Procedure che effettuerà la liquidazione e la 
  *	popolazione delle tabelle interessate.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doLiquidazione(ActionContext context) {	

	try{
		fillModel(context);
		CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)getBusinessProcess(context);	
		Liquid_coriBulk liquidazione = (Liquid_coriBulk)bp.getModel();
		java.util.List selezionati = bp.getGruppi().getSelectedModels(context);
		if (selezionati.size()==0)
			throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare almeno un gruppo");

		Liquid_coriBulk newLiquid_cori = ((it.cnr.contab.cori00.ejb.Liquid_coriComponentSession)bp.createComponentSession()).eseguiLiquidazione(context.getUserContext(), liquidazione, selezionati);
		bp.setIsLiquidato(true);
		bp.setModel(context, newLiquid_cori);
		bp.getGruppi().reset(context);
		bp.commitUserTransaction();
		
		bp.edit(context, liquidazione);
		bp.setMessage("Liquidazione completata");
		return context.findDefaultForward();
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context, e);
	} catch (it.cnr.jada.bulk.ValidationException e){
		return handleException(context, e);
	} catch (BusinessProcessException e){
		return handleException(context, e);
	} catch (it.cnr.jada.comp.ComponentException e){
		return handleException(context, e);
	} catch (java.rmi.RemoteException e){
		return handleException(context, e);
	}
}
public Forward doF24EP(ActionContext context) {
	try {
		CRUDBP bp = getBusinessProcess(context);
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfermaF24");
		return doConfermaF24(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfermaF24(ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)getBusinessProcess(context);
			bp.Estrazione(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}		

public Forward doSelezionaF24(ActionContext context) {
	try {
	fillModel(context);
	return openConfirm(context,"Si vuole selezionare anche i gruppi negativi?",OptionBP.CONFIRM_YES_NO,"doConfirmSelezionaF24");
	} catch(Throwable e) {
		return handleException(context,e);
	}
}	
public Forward doConfirmSelezionaF24(ActionContext context,int option) {
	try
	{
		fillModel(context);
		CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)context.getBusinessProcess();
		if (option == OptionBP.YES_BUTTON) 
			bp.SelezionaF24(context,true);
		else
			bp.SelezionaF24(context,false);
	return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	
public Forward doSelezionaF24Prev(ActionContext context) {
try {
		fillModel(context);
		return openConfirm(context,"Si vuole selezionare anche i gruppi negativi?",OptionBP.CONFIRM_YES_NO,"doConfirmSelezionaF24Prev");
	} catch(Throwable e) {
		return handleException(context,e);
	}
}	
public Forward doConfirmSelezionaF24Prev(ActionContext context,int option) {
try
	{
		fillModel(context);
		CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)context.getBusinessProcess();
		if (option == OptionBP.YES_BUTTON) 
			bp.SelezionaF24Prev(context,true);
		else
			bp.SelezionaF24Prev(context,false);
	return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doF24EPTot(ActionContext context) {
	try {
		CRUDBP bp = getBusinessProcess(context);
		fillModel(context);
		if (bp.isDirty())
			return openContinuePrompt(context,"doConfermaF24Tot");
		return doConfermaF24Tot(context,OptionBP.YES_BUTTON);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfermaF24Tot(ActionContext context,int option) {
	try {
		if (option == OptionBP.YES_BUTTON) {
			CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)getBusinessProcess(context);
			bp.EstrazioneTot(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
