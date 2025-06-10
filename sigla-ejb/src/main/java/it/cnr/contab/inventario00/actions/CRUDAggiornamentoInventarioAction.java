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

import it.cnr.contab.inventario00.bp.CRUDAggiornamentoInventarioBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.util.action.RicercaLiberaBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;
/**
 * Insert the type's description here.
 * @author: RPucciarelli
 */
public class CRUDAggiornamentoInventarioAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDAggiornamentoInventarioAction() {
	super();
}

/**
  * Gestisce il risultato di una ricerca sull' Ubicazione di Destinazione:
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param bulk il <code>Aggiornamento_inventarioBulk</code> il bulk che contiene le informazioni relative all'operazione di aggiornamento
  * @param uo_dest la <code>Ubicazione_beneBulk</code> Ubicazione di destinazione selezionata dall'utente.
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackSearchFindUbicazioneDestinazione(
	ActionContext context, 
	it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk bulk,
	it.cnr.contab.inventario00.tabrif.bulk.Ubicazione_beneBulk ubicazione_dest) {

	try {
		fillModel(context);
		if (ubicazione_dest != null){
			it.cnr.jada.UserContext userContext = context.getUserContext();
			CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
			
			it.cnr.contab.inventario00.ejb.IdInventarioComponentSession h = (it.cnr.contab.inventario00.ejb.IdInventarioComponentSession)
														it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																	"CNRINVENTARIO00_EJB_IdInventarioComponentSession",
																	it.cnr.contab.inventario00.ejb.IdInventarioComponentSession.class);
			bulk.setUbicazione_destinazione(ubicazione_dest);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}	
}
/**
  * Gestisce l'aggiunta di un nuovo CDR Utilizzatore. Prima di permettere l'aggiunta
  *	di un Utilizzatore, va a fare il controllo di validità su quello attuale,
  *	e sulle linee di attività ad esso associate.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public Forward doAddToCRUDMain_VUtilizzatori(ActionContext context) {

	try {
		fillModel(context);
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
		it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk agg = (it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)bp.getModel();	
		
		it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk v_utitliz = (it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk)bp.getVutilizzatori().getModel();
		
		if (agg.getV_utilizzatoriColl().size()>0 && v_utitliz != null){
			if (v_utitliz.getCdr()==null || v_utitliz.getCdr().getCd_centro_responsabilita()==null)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un CDR Utilizzatore"));

			if (v_utitliz.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal(0))==0)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una percentuale di utilizzo per il CDR Utilizzatore"));

			if (v_utitliz.getBuono_cs_utilizzatoriColl().size()>0){
				bp.validate_Percentuali_LA(context, v_utitliz);
			}
			else{
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare almeno un GAE con la relativa Percentuale di Utilizzo"));
			}
		}

		int cont = 1;
		for (java.util.Iterator i = agg.getV_utilizzatoriColl().iterator(); i.hasNext();){
			it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk v_utilizzatore = (it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk)i.next();
			
			if (v_utilizzatore.getCdr()==null || v_utilizzatore.getCdr().getCd_centro_responsabilita()==null)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare un CDR per la riga " + cont));

			if (v_utilizzatore.getPercentuale_utilizzo_cdr().compareTo(new java.math.BigDecimal(0))==0)
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare una percentuale di utilizzo per il CDR Utilizzatore alla riga " + cont));

			if (v_utilizzatore.getBuono_cs_utilizzatoriColl().size()==0){
				return handleException(context, new it.cnr.jada.bulk.ValidationException("Attenzione: specificare almeno un GAE " + 
					"con la relativa Percentuale di Utilizzo per ogni Utilizzatore.\n " + 
					"Specificare un GAE per la riga " + cont));
			}
			cont++;
		}
		
		getController(context,"main.VUtilizzatori").add(context);
		return context.findDefaultForward();
			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
public Forward doTab(ActionContext context,String tabName,String pageName) {
	
	CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
	it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk agg=(it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)bp.getModel();
	try	{
		fillModel( context );
		
		//if ("tabAggiornamentoInventarioTestata".equalsIgnoreCase(bp.getTab(tabName))) {
		//	if (buonoT.getUbicazione_destinazione() == null ){
	      //  	throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare Ubicazione Destinazione.");
			//}
	//	}

		return super.doTab( context, tabName, pageName );
	} catch(Throwable e) {
		return handleException(context,e);
	}
}


public Forward doAddToCRUDMain_dettaglioCRUDController(ActionContext context) {

	try {
		fillModel(context);
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
		((it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)bp.getModel()).setDettagli(new SimpleBulkList());
		bp.getDettagliCRUDController().validate(context);
		
		it.cnr.jada.util.RemoteIterator ri = ((it.cnr.contab.inventario00.ejb.Aggiornamento_inventarioComponentSession)bp.createComponentSession()).cercaBeniAggiornabili(context.getUserContext(),(it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk)bp.getModel(),null);
		ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
		
		int count = ri.countElements();

		
		if (count == 0) {
			bp.setMessage("Nessun Bene aggiornabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, ri);		
			context.closeBusinessProcess();
		} else {
			
			RicercaLiberaBP rlbp = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
			//rlbp.setCanPerformSearchWithoutClauses(true);
			
			it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession inventario_component = (it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession)bp.createComponentSession("CNRINVENTARIO00_EJB_Inventario_beniComponentSession",it.cnr.contab.inventario00.ejb.Inventario_beniComponentSession.class);
			it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk model_for_search = (it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk)inventario_component.inizializzaBulkPerInserimento(context.getUserContext(),new it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk());
			rlbp.setPrototype(model_for_search);
			//rlbp.setMultiSelection(true);
			rlbp.setShowSearchResult(false);
			context.addHookForward("searchResult",this,"doBringBackAddBeni");
			context.addHookForward("filter",this,"doBringBackAddBeni");
			return context.addBusinessProcess(rlbp);
		}		
		return context.findDefaultForward();			
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

public Forward doBringBackAddBeni(ActionContext context) {

	try {
		HookForward fwd = (HookForward)context.getCaller();
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
		it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk buonoS =(it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk)bp.getModel();
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = (it.cnr.jada.persistency.sql.CompoundFindClause)fwd.getParameter("filter");
		bp.setClauses(clauses);
		it.cnr.jada.util.RemoteIterator iterator = bp.getListaBeniDaAggiornare(context.getUserContext(),buonoS.getBuono_carico_scarico_dettColl(), clauses);
		iterator = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,iterator);
		int count = iterator.countElements();
		if (count == 0) {
			bp.setMessage("Nessun Bene associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, iterator);
		} else{		
			SelezionatoreListaBP slbp = select(context,iterator,it.cnr.jada.bulk.BulkInfo.getBulkInfo(it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk.class),null,"doSelezionaBeni",null,bp);
			slbp.setMultiSelection(true);
			return slbp;
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}

public Forward doSelezionaBeni(ActionContext context) {
	try {
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)context.getBusinessProcess();
		bp.getDettagliCRUDController().reset(context);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
	 * @param context	L'ActionContext della richiesta
	 * 	 * @return Il Forward alla pagina di risposta
	 */
public Forward doSalva(ActionContext context) {
	try {
		fillModel(context);
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)context.getBusinessProcess();
		bp.validate(context);
		bp.aggiornamento_beni(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return super.handleException(context,e);
	}
}
/**
 * Gestisce il comando azzeramento di un searchtool
 * Quando si azzera il searchTool per la ricerca del CdR Utilizzatore,
 * azzera la Collection delle Linee di Attivita collegate
 */ 
public Forward doBlankSearchFind_cdr(ActionContext context, 
	it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk cdr_utilizzatore) 
	throws java.rmi.RemoteException {
		
	try {
		CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)getBusinessProcess(context);
				
		cdr_utilizzatore.getBuono_cs_utilizzatoriColl().clear();
		cdr_utilizzatore.setBene(null);
		cdr_utilizzatore.setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk());
		cdr_utilizzatore.setDettaglio(null);
		cdr_utilizzatore.setPercentuale_utilizzo_cdr(null);		

		bp.getUtilizzatori().reset(context);
		return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
 
}
