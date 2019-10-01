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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.bp.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.pdg00.bp.*;
import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class ContabilizzazioneFlussoStipendialeMensileAction extends it.cnr.jada.util.action.SelezionatoreListaAction {
public ContabilizzazioneFlussoStipendialeMensileAction() {
	super();
}
public Forward doApriCompenso(ActionContext context) throws BusinessProcessException {
	ContabilizzazioneFlussoStipendialeMensileBP bp = (ContabilizzazioneFlussoStipendialeMensileBP)context.getBusinessProcess();

	Stipendi_cofiBulk stipendi_cofi = (Stipendi_cofiBulk)bp.getFocusedElement();
	if (stipendi_cofi == null) {
		bp.setMessage("E' necessario selezionare una mensilità.");
		return context.findDefaultForward();
	}

	if (stipendi_cofi.getCd_cds_comp() == null ||
		stipendi_cofi.getCd_uo_comp() == null ||
		stipendi_cofi.getEsercizio_comp() == null ||
		stipendi_cofi.getPg_comp() == null) {
		bp.setMessage("E' necessario selezionare una mensilità già liquidata.");
		return context.findDefaultForward();
	}
		
	CompensoBulk compenso = new CompensoBulk(
		stipendi_cofi.getCd_cds_comp(),
		stipendi_cofi.getCd_uo_comp(),
		stipendi_cofi.getEsercizio_comp(),
		stipendi_cofi.getPg_comp());
	
	CRUDCompensoBP compBP = CRUDCompensoBP.getBusinessProcessFor(context, compenso, "VRSWTh");
	
	compBP.edit(context,compenso);

	return context.addBusinessProcess(compBP);
}
public Forward doApriDocumentoGenerico(ActionContext context) throws BusinessProcessException {
	ContabilizzazioneFlussoStipendialeMensileBP bp = (ContabilizzazioneFlussoStipendialeMensileBP)context.getBusinessProcess();

	Stipendi_cofiBulk stipendi_cofi = (Stipendi_cofiBulk)bp.getFocusedElement();
	if (stipendi_cofi == null) {
		bp.setMessage("E' necessario selezionare una mensilità.");
		return context.findDefaultForward();
	}

	if (stipendi_cofi.getCd_cds_doc_gen() == null ||
		stipendi_cofi.getCd_uo_doc_gen() == null ||
		stipendi_cofi.getEsercizio_doc_gen() == null ||
		stipendi_cofi.getCd_tipo_doc_gen() == null ||
		stipendi_cofi.getPg_doc_gen() == null) {
		bp.setMessage("E' necessario selezionare una mensilità già liquidata.");
		return context.findDefaultForward();
	}
		
	CRUDDocumentoGenericoPassivoBP newbp = (CRUDDocumentoGenericoPassivoBP)context.createBusinessProcess("CRUDGenericoPassivoBP",new Object[] { "VRSWTh" });
	
	newbp.edit(context,new Documento_generico_passivoBulk(
		stipendi_cofi.getCd_cds_doc_gen(),
		stipendi_cofi.getCd_tipo_doc_gen(),
		stipendi_cofi.getCd_uo_doc_gen(),
		stipendi_cofi.getEsercizio_doc_gen(),
		stipendi_cofi.getPg_doc_gen()));

	return context.addBusinessProcess(newbp);
}
public Forward doApriMandato(ActionContext context) throws BusinessProcessException {
	ContabilizzazioneFlussoStipendialeMensileBP bp = (ContabilizzazioneFlussoStipendialeMensileBP)context.getBusinessProcess();

	Stipendi_cofiBulk stipendi_cofi = (Stipendi_cofiBulk)bp.getFocusedElement();
	if (stipendi_cofi == null) {
		bp.setMessage("E' necessario selezionare una mensilità.");
		return context.findDefaultForward();
	}

	if (stipendi_cofi.getCd_cds_mandato() == null ||
		stipendi_cofi.getCd_uo_doc_gen() == null ||
		stipendi_cofi.getEsercizio_mandato() == null ||
		stipendi_cofi.getPg_mandato() == null) {
		bp.setMessage("E' necessario selezionare una mensilità già liquidata.");
		return context.findDefaultForward();
	}
		
	CRUDMandatoBP newbp = (CRUDMandatoBP)context.createBusinessProcess("CRUDMandatoBP",new Object[] { "VRSWTh" });
	
	newbp.edit(context,new MandatoIBulk(
		stipendi_cofi.getCd_cds_mandato(),
		stipendi_cofi.getEsercizio_mandato(),
		stipendi_cofi.getPg_mandato()));

	return context.addBusinessProcess(newbp);
}
public Forward doBringBack(ActionContext context) throws BusinessProcessException {
	// Reimplementato per impedire che la selezione di un elemento provochi
	// la chiusura del bp (come da default in SelezionatoreListaAction
	return context.findDefaultForward();
}
/**
 * Avvia la la contabilizzazione dei flussi stipendiali mensili
 * sul mese selezionato.
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 * @throws BusinessProcessException	
 */
public Forward doContabilizzaFlussoStipendialeMensile(ActionContext context) throws BusinessProcessException {
	ContabilizzazioneFlussoStipendialeMensileBP bp = (ContabilizzazioneFlussoStipendialeMensileBP)context.getBusinessProcess();
	bp.saveSelection(context);
	Stipendi_cofiBulk stipendi_cofi = (Stipendi_cofiBulk)bp.getFocusedElement();
	if (stipendi_cofi != null)
		try {
			bp.createComponentSession().contabilizzaFlussoStipendialeMensile(context.getUserContext(),stipendi_cofi.getMese().intValue());
			bp.refresh(context);
		} catch(Throwable e) {
			return handleException(context,e);
		}
	else 
		setErrorMessage(context,"Per poter effettuare la contabilizzazione è necessario selezionare un mese.");
	return context.findDefaultForward();
}
}
