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

package it.cnr.contab.prevent00.action;

import it.cnr.contab.prevent00.bp.CRUDPdGAggregatoBP;
import it.cnr.contab.prevent00.bp.CRUDTotaliPdGAggregatoBP;
import it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk;
import it.cnr.contab.prevent00.ejb.PdgAggregatoComponentSession;

/**
 * Adatta e implementa la {@link it.cnr.jada.util.action.BulkAction } affinchè 
 * 		la JSP non disegni il CRUD.
 * 
 * @author: Bisquadro Vincenzo
 */

public class CRUDPdGAggregatoAction extends it.cnr.jada.util.action.BulkAction {
/**
 * Costruttore standard di CRUDPdGAggregatoAction.
 */
public CRUDPdGAggregatoAction() {
	super();
}
private it.cnr.jada.action.Forward doApriDettagli(it.cnr.jada.action.ActionContext context,String bpname) {
		try {
			fillModel(context);
			CRUDPdGAggregatoBP bp = (CRUDPdGAggregatoBP)context.getBusinessProcess();
			Pdg_aggregatoBulk pdg = bp.caricaPdg_aggregato(context);
			it.cnr.jada.action.BusinessProcess newbp = context. createBusinessProcess(
				bpname,
				new Object[] { 
					bp.createComponentSession().isPdGAggregatoModificabile(context.getUserContext(),pdg) ? "M" : "V",
					pdg.getCdr() });
			return context.addBusinessProcess(newbp);
		} catch(Throwable e) {
			return handleException(context,e);
		}
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di ricerca del searchtool "cdr"
 *
 * @param context	L'ActionContext della richiesta
 * @param pdg	L'OggettoBulk padre del searchtool
 * @param cdr	L'OggettoBulk selezionato dall'utente
 * @return Il Forward alla pagina di risposta
 */
public it.cnr.jada.action.Forward doBringBackSearchCdr(it.cnr.jada.action.ActionContext context,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk pdg, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	try {
		CRUDPdGAggregatoBP bp = (CRUDPdGAggregatoBP)context.getBusinessProcess();
		bp.getPdg_aggregato().setCdr(cdr);
		bp.caricaPdg_aggregato(context);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Presiede alla commutazione (da A a B) dello stato del PdG aggregato nel caso 
 * 		che questo venga approvato.
 *
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @return Forward
 *
 * @exception handleException
 */
public it.cnr.jada.action.Forward doCambiaStato(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
		CRUDPdGAggregatoBP bp = (CRUDPdGAggregatoBP)context.getBusinessProcess();
		try {
			fillModel(context);
			bp.setModel(context,((PdgAggregatoComponentSession)bp.createComponentSession()).modificaStatoPdg_aggregato(context.getUserContext(),(Pdg_aggregatoBulk)bp.getModel())); 
			return context.findDefaultForward();
		} catch(it.cnr.jada.comp.ApplicationException e) {
			bp.caricaPdg_aggregato(context);
			return handleException(context,e);
		} catch(Throwable e) {
			return handleException(context,e);
		}
}
/**
 * A seconda dello stato del PdG viene istanziato un nuovo Business Process che 
 * 		che consentirà la "M"odifica/"V"isualizzazione delle Entrate (risp. Stato A/B).
 * 
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @return BusinessProcess
 *
 * @exception handleException
 */
public it.cnr.jada.action.Forward doContrattazioneEntrate(it.cnr.jada.action.ActionContext context) {
	return doApriDettagli(context,"CRUDEtrDetPdGAggregatoBP");
}
/**
 * A seconda dello stato del PdG viene istanziato un nuovo Business Process che 
 * 		che consentirà la "M"odifica/"V"isualizzazione delle Spese (risp. Stato A/B).
 * 
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @return BusinessProcess
 *
 * @exception handleException
 */
 
public it.cnr.jada.action.Forward doContrattazioneSpese(it.cnr.jada.action.ActionContext context) {
	return doApriDettagli(context,"CRUDSpeDetPdGAggregatoBP");
}
/**
 * Fornisce il totale degli aggregati per le operazioni di stampa.
 * 
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @return BusinessProcess
 *
 * @exception handleException
 */
public it.cnr.jada.action.Forward doTotaliPianoAggregati(it.cnr.jada.action.ActionContext context) {
	try {
		fillModel(context);
		CRUDPdGAggregatoBP bp = (CRUDPdGAggregatoBP)context.getBusinessProcess();
		Pdg_aggregatoBulk pdg = bp.caricaPdg_aggregato(context);
		
		CRUDTotaliPdGAggregatoBP newbp = (CRUDTotaliPdGAggregatoBP)context.createBusinessProcess("CRUDTotaliPdGAggregatoBP");
		newbp.setModel(context,pdg);
		return context.addBusinessProcess(newbp);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
