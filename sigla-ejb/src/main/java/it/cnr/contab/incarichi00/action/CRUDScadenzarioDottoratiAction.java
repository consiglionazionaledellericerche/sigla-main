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

package it.cnr.contab.incarichi00.action;

import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.compensi00.bp.CRUDCompensoBP;
import it.cnr.contab.compensi00.bp.CRUDMinicarrieraBP;
import it.cnr.contab.compensi00.bp.MinicarrieraRataCRUDController;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk;
import it.cnr.contab.compensi00.docs.bulk.Minicarriera_rataBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.compensi00.ejb.MinicarrieraComponentSession;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.incarichi00.bp.CRUDScadenzarioDottoratiBP;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.incarichi00.bulk.ScadenzarioDottoratiBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.OptionBP;

import java.util.Date;
import java.util.GregorianCalendar;
/**

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.08)
 * @author: Roberto Fantino

 */

public class CRUDScadenzarioDottoratiAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDCompensoAction constructor comment.
 */
public CRUDScadenzarioDottoratiAction() {
	super();
}

/**
 * Prepara la minicarriera per la ricerca di un nuovo percipiente
 */
public Forward doBlankSearchFind_terzo(ActionContext context, ScadenzarioDottoratiBulk scadenzarioDottorati) {

	if (scadenzarioDottorati != null){
		TerzoBulk terzo = new TerzoBulk();
		terzo = scadenzarioDottorati.getTerzo();
		scadenzarioDottorati.setCdTerzo(terzo.getCd_terzo());
		context.getBusinessProcess().setResource("findTerzo",terzo.getCd_terzo().toString());
		scadenzarioDottorati.setNome(terzo.getNome_unita_organizzativa());
		scadenzarioDottorati.setCognome(null);
		scadenzarioDottorati.setRagioneSociale(null);
		scadenzarioDottorati.setPartitaIva(terzo.getPartita_iva_anagrafico());
		scadenzarioDottorati.setCodiceFiscale(terzo.getCodice_fiscale_anagrafico());
		scadenzarioDottorati.setCdTerminiPag(terzo.getTermini_pagamento().toString());
		scadenzarioDottorati.setCdModalitaPag(terzo.getModalita_pagamento().toString());
		scadenzarioDottorati.setPgBanca(null);
		scadenzarioDottorati.setCdTipoRapporto(null);
		scadenzarioDottorati.setTiPrestazione(null);
		}
	return context.findDefaultForward();

}

/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca del percipiente
 */
 
public Forward doBringBackSearchFindTerzo(ActionContext context, ScadenzarioDottoratiBulk scadenzarioDottorati, TerzoBulk vTerzo) {

	try {

		if(vTerzo != null) {
			scadenzarioDottorati.setTerzo(vTerzo);
			doBlankSearchFind_terzo(context, scadenzarioDottorati);
			CRUDScadenzarioDottoratiBP bp = (CRUDScadenzarioDottoratiBP) getBusinessProcess(context);
			if (scadenzarioDottorati != null){
				scadenzarioDottorati.setCdTerzo(vTerzo.getCd_terzo());
				scadenzarioDottorati.setNome(vTerzo.getDenominazionePcc());
				scadenzarioDottorati.setCognome(null);
				scadenzarioDottorati.setRagioneSociale(null);
				scadenzarioDottorati.setPartitaIva(vTerzo.getPartita_iva_anagrafico());
				scadenzarioDottorati.setCodiceFiscale(vTerzo.getCodice_fiscale_anagrafico());
				scadenzarioDottorati.setCdTerminiPag(null);
				scadenzarioDottorati.setCdModalitaPag(null);
				scadenzarioDottorati.setModalita(vTerzo.getModalita_pagamento());
				scadenzarioDottorati.setCdTipoRapporto(null);
				scadenzarioDottorati.setTiPrestazione(null);
			}
		} 
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Inoltro la richiesta alla stored procedure per la generazione delle rate
 * Il modello deve essere prima validato dal metodo 'validate'
 */

public Forward doGeneraRate(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		carriera.validate();
		if (!carriera.isNonAssociataACompenso())
			throw new it.cnr.jada.comp.ApplicationException("Almeno una rata ha già generato un compenso. Impossibile rigenerare le rate.");
		
		bp.generaRate(context);
		bp.setDirty(true);
		setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Creazione delle rate eseguita in maniera corretta.");

		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Gestisce la richiesta di cambiamento della modalità di pagamento e ricerca le
 * banche valide
 */

public Forward doOnModalitaPagamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		bp.findListaBanche(context);

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento del tipo di anagrafico della
 * minicarriera. Inoltre essa viene preparata per una nuova ricerca
 * del percipiente
 */
 
public Forward doOnTipoAnagraficoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDScadenzarioDottoratiBP bp = (CRUDScadenzarioDottoratiBP) getBusinessProcess(context);
		ScadenzarioDottoratiBulk carriera = (ScadenzarioDottoratiBulk) bp.getModel();

		if (!bp.isSearching())
			return doBlankSearchFind_terzo(context, carriera);
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la richiesta di cambiamento del tipo rapporto
 * Vengono ricercati i nuovi tipi di trattamento validi
 */
/*
public Forward doOnTipoRapportoChange(ActionContext context) {

	try {
		fillModel(context);
		PostTipoRapportoChange(context);
		
		return context.findDefaultForward();
	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}*/

/**
 * Gestisce la richiesta di cancellazione di una o piu' rate
 */
 
public Forward doRemoveFromCRUDMain_rateCRUDController(ActionContext context) {
	
	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)context.getBusinessProcess();
	MinicarrieraRataCRUDController rateController = (MinicarrieraRataCRUDController)bp.getRateCRUDController();
	it.cnr.jada.util.action.Selection selection = rateController.getSelection();
	try {
		if (selection.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare le rate che si desidera eliminare!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		return handleException(context, e);
	}

	try {
		rateController.remove(context);
	} catch (Throwable e) {
		return handleException(context, e);
	}
	
	return context.findDefaultForward();
}
/**
 * Gestisce la ricerca delle banche valide per la modalità di pagamento selezionata
 */

public Forward doSearchListaBanche(ActionContext context) {
	
	MinicarrieraBulk carriera = (MinicarrieraBulk)getBusinessProcess(context).getModel();
	String columnSet = carriera.getModalita_pagamento().getTiPagamentoColumnSet();
	return search(context, getFormField(context, "main.listaBanche"), columnSet);
}
/**
 * Gestisce la richiesta di cambiamento della pagina del viewer
 */
 /*
public Forward doTab(ActionContext context,String tabName,String pageName) {

	try	{
			
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if ((bp.isEditable()) && (!bp.isSearching())){
			try {
				if ("tabMinicarriera".equalsIgnoreCase(bp.getTab(tabName))) {
					carriera.validaTestata();
				}
				if ("tabMinicarrieraPercipiente".equalsIgnoreCase(bp.getTab(tabName))) {
					//carriera.validaPercipiente();
				}
				if ("tabMinicarrieraRate".equalsIgnoreCase(bp.getTab(tabName))) {
					bp.getRateCRUDController().validate(context);
				}
			} catch (it.cnr.jada.bulk.ValidationException e) {
				bp.setMessage(
					OptionBP.WARNING_MESSAGE,
					e.getMessage());
			}
		}

		return super.doTab( context, tabName, pageName );

	} catch(Throwable e) {
		return handleException(context,e);
	}
}*/


}
