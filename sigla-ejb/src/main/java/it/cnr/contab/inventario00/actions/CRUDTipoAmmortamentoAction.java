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

package it.cnr.contab.inventario00.actions;

/**
 * Action che gestisce le operazioni di CRUD (Create,Read,Update e Delete) 
 * per il BP CRUDTipoAmmortamentoBP: permette di gestire le richieste per la creazione
 * di un Tipo Ammortamento e della sua associazione a delle Categorie Gruppo Inventario.
 *
 * <p>Implementa i seguenti comandi:
 * <dl>
 * <dt> doSelezionaGruppi
 * <dt> doConfirmElimina
 * <dt> doChangeTipoAmmortamento
 * <dt> doAddToCRUDMain_gruppiController
 * <dt> doBringBackToRiassocia
 * <dt> doElimina
 * <dt> doConfirmRiassocia
 * <dt> doRiassocia
 * </dl>
 *
*/

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
 
public class CRUDTipoAmmortamentoAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDTipoAmmortamentoAction() {
	super();
}
/**
  *   Gestisce l'aggiunta di una nuova Categoria Gruppo Inventario.
  *	La ricerca di Gruppi disponibili per l'associazione al Tipo Ammortamento, cambia a seconda 
  *	dello stato del BusinessProcess:
  *		- in fase di creazione viene invocato il metodo Tipo_ammortamentoComponent.cercaGruppiAssociabili;
  *		- in modifica, invece, il metodo Tipo_ammortamentoComponent.cercaGruppiAssociabiliPerModifica.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_gruppiController(ActionContext context) {

	try {
		fillModel(context);
		it.cnr.jada.util.RemoteIterator ri = null;
		CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);
		
		if (bp.isInserting()){
			ri = ((it.cnr.contab.inventario00.ejb.Tipo_ammortamentoComponentSession)bp.createComponentSession()).cercaGruppiAssociabili(context.getUserContext(),(Tipo_ammortamentoBulk)bp.getModel());
		} else{
			ri = ((it.cnr.contab.inventario00.ejb.Tipo_ammortamentoComponentSession)bp.createComponentSession()).cercaGruppiAssociabiliPerModifica(context.getUserContext(),(Tipo_ammortamentoBulk)bp.getModel());
		}
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		int count = ri.countElements();
		if (count == 0) {
			bp.setMessage("Nessun Gruppo associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);
		} else {
			SelezionatoreListaBP slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(Categoria_gruppo_inventBulk.class),null,"doSelezionaGruppi",null,bp);
			slbp.setMultiSelection(true);
		}
		
		return context.findDefaultForward();	
		
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Gestisce una operazione di riassocia.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackToRiassocia(ActionContext context) {

	try {
		HookForward fwd = (HookForward)context.getCaller();
		java.util.List selectedModels = (java.util.List)fwd.getParameter("selectedElements");	
			
		if (selectedModels != null && !selectedModels.isEmpty()) {
			CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);
			Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bp.getModel();
			Tipo_ammortamentoBulk tipo_ammortamento_associato = (Tipo_ammortamentoBulk)selectedModels.get(0);
			tipo_ammortamento.setAmmortamento_associato(tipo_ammortamento_associato);
			tipo_ammortamento.setIsPerRiassocia(Boolean.TRUE);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *  E' stata generata la richiesta di cambiare il Tipo Ammortamento.
  *	Questo metodo è invocato ogni volta che l'utente seleziona o deseleziona uno dei check-box 
  *	relativi al tipo dell'ammortamento che sta creando.
  *	Il metodo provvede ad azzerare le percentuali precedentemente indicate per il tipo selezionato.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param tipo il <code>String</code> tipo selezionato dall'utente
  *
  * @return forward <code>Forward</code>
**/
public Forward doChangeTipoAmmortamento(ActionContext context, String tipo) {

	CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);
	Tipo_ammortamentoBulk ti_ammort = (Tipo_ammortamentoBulk)bp.getModel();	
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	java.math.BigDecimal zero = new java.math.BigDecimal(0);

	try{
		fillModel(context);
		

		if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ORDINARIO) && !ti_ammort.isOrdinario()){
			ti_ammort.setPerc_primo_anno_ord(zero);
			ti_ammort.setPerc_successivi_ord(zero);			
		}

		if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ACCELERATO) && !ti_ammort.isAnticipato()){
			ti_ammort.setPerc_primo_anno_ant(zero);
			ti_ammort.setPerc_successivi_ant(zero);
		}

		if (tipo.equalsIgnoreCase(Tipo_ammortamentoBulk.TIPO_ALTRO) && !ti_ammort.isAltro()){
			ti_ammort.setPerc_primo_anno_altro(zero);
			ti_ammort.setPerc_successivi_altro(zero);
		}		
		
	} catch (it.cnr.jada.bulk.FillException e){
		return handleException(context,e);
	}

	return context.findDefaultForward();
}
public Forward doConfirmElimina(ActionContext context, OptionBP optionBP) {

	try {
		CRUDTipoAmmortamentoBP bp_ammortamento = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);		
		if (optionBP.getOption() == OptionBP.YES_BUTTON) {
			return super.doElimina(context);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
public Forward doConfirmRiassocia(ActionContext context, OptionBP optionBP) {

	try {
		CRUDTipoAmmortamentoBP bp_ammortamento = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);		
		Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)optionBP.getAttribute("tipo_ammortamento");
		if (optionBP.getOption() == OptionBP.YES_BUTTON) {
			RicercaLiberaBP bp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			bp.setCanPerformSearchWithoutClauses(true);			
			bp.setSearchProvider(bp_ammortamento.getAmmortamentoSearchProvider(context));
			bp.setPrototype( new Tipo_ammortamentoBulk());
			bp.setMultiSelection(false);
			context.addHookForward("seleziona",this,"doBringBackToRiassocia");
			return context.addBusinessProcess(bp);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  *   Gestisce un comando di cancellazione di un Tipo Ammortamento.
  *  La cancellazione è solo logica, e le Categorie Gruppo Inventario associate a questo Tipo 
  *	Ammortamento risulteranno NON associate a nessun ammortamento e, quindi, disponibili per 
  *	ulteriori associazioni
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doElimina(ActionContext context)throws java.rmi.RemoteException {

	
	CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);	
	Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bp.getModel();
	
	try {		
		fillModel(context);
		OptionBP optionBP = openConfirm(context,"Attenzione: questa operazione cancellerà le associazioni originarie.\n " + 
										"Tutti i beni appartenenti ai gruppi associati a questo Tipo\n " + 
										"risulteranno privi di un Ammortamento. Si vuol continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmElimina");
		optionBP.addAttribute("tipo_ammortamento", tipo_ammortamento);
		return optionBP;
	} catch(Throwable e) {		
		return handleException(context,e);
	}
}

public Forward doRiassocia(ActionContext context) throws it.cnr.jada.comp.ApplicationException{

	
	CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)getBusinessProcess(context);	
	Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)bp.getModel();
	
	try {		
		fillModel(context);
		OptionBP optionBP = openConfirm(context,"Attenzione: questa operazione cancellerà le associazioni originarie. Si vuol continuare?",OptionBP.CONFIRM_YES_NO,"doConfirmRiassocia");
		optionBP.addAttribute("tipo_ammortamento", tipo_ammortamento);
		return optionBP;
	} catch(Throwable e) {		
		return handleException(context,e);
	}
}
public Forward doSelezionaGruppi(ActionContext context) {
	try {
		CRUDTipoAmmortamentoBP bp = (CRUDTipoAmmortamentoBP)context.getBusinessProcess();
		bp.getGruppiController().reset(context);
		bp.setDirty(true);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
