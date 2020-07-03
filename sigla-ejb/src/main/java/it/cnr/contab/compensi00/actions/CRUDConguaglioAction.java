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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (08/07/2002 16.56.46)
 * @author: Roberto Fantino
 */
public class CRUDConguaglioAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDConguaglioAction constructor comment.
 */
public CRUDConguaglioAction() {
	super();
}
/**
  * Gestisce un comando di cancellazione del conguaglio
  */

private Forward basicDoEliminaConguaglio(ActionContext context) 
{
	try
	{
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		bp.delete(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
		ConguaglioComponentSession session = (ConguaglioComponentSession)bp.createComponentSession();
		if (session.isConguaglioAnnullato(context.getUserContext(), conguaglio))
		{
			bp.edit(context, conguaglio);
			bp.setMessage("Annullamento effettuato");
		}
		else
		{
			bp.reset(context);
			bp.setMessage("Cancellazione effettuata");
		}
		return context.findDefaultForward();
	}
	catch(Throwable ex)
	{
		return handleException(context, ex);
	}
}
/**
  * Abilito il conguaglio alla creazione del compenso
  */
public Forward doAbilitaConguaglio(ActionContext context) {

	try {

		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		bp.doAbilitaConguaglio(context);

		setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Calcolo eseguito in modo corretto.");

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
private void doAzzeraTipoRapporto(ActionContext context, ConguaglioBulk conguaglio) {

	if (conguaglio!=null){
		conguaglio.setTipiRapporto(null);
		conguaglio.setTipoRapporto(null);
		doAzzeraTipoTrattamento(context , conguaglio);
	}
}
private void doAzzeraTipoTrattamento(ActionContext context, ConguaglioBulk conguaglio) {

	if (conguaglio!=null){
		conguaglio.setTipiTrattamento(null);
		conguaglio.setTipoTrattamento(null);
	}
}
public Forward doBlankSearchFind_terzo(ActionContext context, ConguaglioBulk conguaglio) {

	if (conguaglio!=null){
		V_terzo_per_conguaglioBulk v_terzo = new V_terzo_per_conguaglioBulk();
		conguaglio.setV_terzo(v_terzo);
		conguaglio.setCd_terzo(null);
		conguaglio.setNome(null);
		conguaglio.setCognome(null);
		conguaglio.setRagione_sociale(null);
		conguaglio.setPartita_iva(null);
		conguaglio.setCodice_fiscale(null);
		conguaglio.setPg_comune(null);
		conguaglio.setCd_provincia(null);
		conguaglio.setCd_regione(null);

		conguaglio.setTermini(null);
		conguaglio.setTerminiPagamento(null);
		conguaglio.setModalita(null);
		conguaglio.setModalitaPagamento(null);
		conguaglio.setBanca(null);

		doAzzeraTipoRapporto(context, conguaglio);
	}
	return context.findDefaultForward();

}
/**
  * gestisco il rientro dalla creazione/modifica di un compenso
  */

public Forward doBringBackCompenso(ActionContext context) {

	try{
		HookForward caller = (HookForward)context.getCaller();
		CompensoBulk compenso = (CompensoBulk)caller.getParameter("bringback");
		
		if(compenso == null)
			return context.findDefaultForward();

		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
		conguaglio.setCompenso(compenso);

		return super.doSalva(context);

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
public Forward doBringBackSearchFind_terzo(ActionContext context, ConguaglioBulk conguaglio, V_terzo_per_compensoBulk vTerzo) {

	try {

		if(vTerzo != null) {
			doBlankSearchFind_terzo(context, conguaglio);
			CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
			bp.completaTerzo(context, conguaglio, vTerzo);
		} 
		return context.findDefaultForward();

	} catch(BusinessProcessException e) {
		return handleException(context,e);
	}
}
/**
  * Significato dei codici di errore:
  * --> 1 terzo non valido
  * --> 2 tipo rapporto non valido
  * --> 3 tipo trattamento non valido
  *
  *
**/
public Forward doConfermaModificaDataCompetenzaCoge(ActionContext context, OptionBP optionBP){

	try{
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();

		java.sql.Timestamp oldDataCompCoge = (java.sql.Timestamp)optionBP.getAttribute("oldDataCompCoge");
		int errorCodeTerzo = ((Integer)optionBP.getAttribute("errorCodeTerzo")).intValue();
		
		if (optionBP.getOption() == OptionBP.YES_BUTTON){
			switch (errorCodeTerzo) {
				case 6: {
					doAzzeraTipoRapporto(context, conguaglio);
					bp.findTipiRapporto(context);
					break;
				}
			}
			conguaglio.setStatoToAbilitaConguaglio();
		}else
			conguaglio.setDt_da_competenza_coge(oldDataCompCoge);

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
  * Significato dei codici di errore:
  * --> 1 terzo non valido
  * --> 2 tipo rapporto non valido
  * --> 3 tipo trattamento non valido
  *
  *
**/
public Forward doConfermaModificaDataRegistrazione(ActionContext context, OptionBP optionBP){

	try{
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();

		java.sql.Timestamp oldDataReg= (java.sql.Timestamp)optionBP.getAttribute("dataReg");
		int errorCodeTerzo = ((Integer)optionBP.getAttribute("errorCodeTerzo")).intValue();
		
		if (optionBP.getOption() == OptionBP.YES_BUTTON){
			switch (errorCodeTerzo) {
				case 2: {
					doBlankSearchFind_terzo(context, conguaglio);
					break;
				}case 8: {
					doAzzeraTipoTrattamento(context, conguaglio);
					bp.findTipiTrattamento(context);
					break;
				}
			}
			conguaglio.setStatoToAbilitaConguaglio();
		}else
			conguaglio.setDt_registrazione(oldDataReg);

		return context.findDefaultForward();

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
public Forward doCreaCompensoConguaglio(ActionContext context) {

	try {

		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();

		if ( conguaglio.isStatoAbilitaConguaglio()){
			setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, conguaglio.getMsgAbilitaConguaglio());
			return context.findDefaultForward();
		}
		
		bp.doValidaDatiEsterni(context);
		
		String message = bp.doVerificaIncoerenzaCarichiFam(context);
		
		bp.doCreaCompensoConguaglio(context);
		if(message!=null)
			setMessage(context,it.cnr.jada.util.action.FormBP.WARNING_MESSAGE,message);
		else
			setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Compenso creato in modo corretto");

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
  * Gestisce un comando di cancellazione del conguaglio
  */
 
public Forward doElimina(ActionContext context) throws java.rmi.RemoteException 
{
	try 
	{
		fillModel(context);
	} 
	catch(it.cnr.jada.bulk.FillException e) 
	{
		return handleException(context,e);
	}

	return basicDoEliminaConguaglio(context);
}
public Forward doOnDtACompetenzaCogeChange(ActionContext context) {

	CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
	ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
	java.sql.Timestamp oldDataCompCoge = conguaglio.getDt_a_competenza_coge();
	try{

		try {
			fillModel(context);
			if (bp.isSearching())
				return context.findDefaultForward();

			conguaglio.validaDate();
		} catch(it.cnr.jada.bulk.FillException e) {
			conguaglio.setDt_a_competenza_coge(oldDataCompCoge);
			throw e;
		} catch(it.cnr.jada.comp.ApplicationException e) {
			conguaglio.setDt_a_competenza_coge(oldDataCompCoge);
			throw e;
		}
		conguaglio.setStatoToAbilitaConguaglio();
		return context.findDefaultForward();

	} catch(Throwable e) {
		conguaglio.setDt_a_competenza_coge(oldDataCompCoge);
		return handleException(context, e);
	}
}
	public Forward doOnDtDaCompetenzaCogeChange(ActionContext context) {
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		java.sql.Timestamp oldDataCompCoge = ((ConguaglioBulk)bp.getModel()).getDt_da_competenza_coge();
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
	try {
		fillModel(context);

		if (bp.isSearching())
			return context.findDefaultForward();

		try{
			conguaglio.validaDate();
		} catch(it.cnr.jada.comp.ApplicationException e) {
			conguaglio.setDt_da_competenza_coge(oldDataCompCoge);
			throw e;
		}


		int errorCodeTerzo = bp.validaTerzo(context, false);
		if (errorCodeTerzo==6){
			String msg = null;
			switch (errorCodeTerzo) {
				case 6: {
					msg = "Il tipo rapporto selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
					break;
				}
			}
				
			OptionBP option = openConfirm(context, msg , OptionBP.CONFIRM_YES_NO,"doConfermaModificaDataCompetenzaCoge");
			option.addAttribute("oldDataCompCoge", oldDataCompCoge);
			option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
			return option;
		}
		bp.findTipiRapporto(context);
		bp.ripristinaSelezioneTipoRapporto();
		conguaglio.setStatoToAbilitaConguaglio();

		return context.findDefaultForward();

	}catch (Throwable ex) {
		conguaglio.setDt_da_competenza_coge(oldDataCompCoge);
		return handleException(context, ex);
	}
}
public Forward doOnDtRegistrazioneChange(ActionContext context) {

	try {
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		java.sql.Timestamp oldDataReg = ((ConguaglioBulk)bp.getModel()).getDt_registrazione();
		fillModel(context);

		if (bp.isSearching())
			return context.findDefaultForward();

		try{
			((ConguaglioBulk)bp.getModel()).validaDate();
		} catch(it.cnr.jada.comp.ApplicationException e) {
			((ConguaglioBulk)bp.getModel()).setDt_registrazione(oldDataReg);
			throw e;
		}

		int errorCodeTerzo = bp.validaTerzo(context, false);
		if (errorCodeTerzo==2 || errorCodeTerzo==8){
			String msg = null;
			switch (errorCodeTerzo) {
				case 2: {
					msg = "Il terzo selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
					break;
				}case 8: {
					msg = "Il tipo trattamento selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
					break;
				}
			}
				
			OptionBP option = openConfirm(context, msg , OptionBP.CONFIRM_YES_NO,"doConfermaModificaDataRegistrazione");
			option.addAttribute("dataReg", oldDataReg);
			option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
			return option;
		}

		bp.findTipiTrattamento(context);
		bp.ripristinaSelezioneTipoTrattamento();
		((ConguaglioBulk)bp.getModel()).setStatoToAbilitaConguaglio();

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
  * Alla selezione delle Flag nel Tab Testata devo verificare se e' gia' stato creato eseguito il "calcola". Se si
  *	devo forzare il ricalcolo
  */

public Forward doOnFlagChange(ActionContext context) 
{
	try 
	{
		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk aConguaglio = (ConguaglioBulk)bp.getModel();

		if(aConguaglio.isStatoNormale())
		{
			aConguaglio.setStatoToAbilitaConguaglio();
			setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Riabilitare il conguaglio.");			
		}

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
  * Alla selezione della Modalita di Pagamento ricerco le banche
  */
public Forward doOnModalitaPagamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		bp.findListaBanche(context);

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
  * Alla selezione del Tipo Rapporto ricerco i Tipi Trattamento eleggibili
  */

public Forward doOnTipoRapportoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		bp.findTipiTrattamento(context);

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
public Forward doOnTipoTrattamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();
		conguaglio.setStatoToAbilitaConguaglio();
		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce una richiesta di salvataggio.
 */
public Forward doSalva(ActionContext context){

	try
	{
		fillModel(context);
		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();

		conguaglio.validaTestata();
		ConguaglioComponentSession sess = (ConguaglioComponentSession)bp.createComponentSession();
		sess.validaTerzo(context.getUserContext(), conguaglio);

		if (conguaglio.isStatoAbilitaConguaglio()){
			setMessage(context, 0, conguaglio.getMsgAbilitaConguaglio());
			return context.findDefaultForward();
		}else if (conguaglio.isStatoCreaCompensoConguaglio()){
			setMessage(context, 0, conguaglio.getMsgCreaCompensoConguaglio());
			return context.findDefaultForward();
		}	
		
		return doSalvaCompenso(context, conguaglio);

	}catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Gestisce una richiesta di salvataggio.
 */
private Forward doSalvaCompenso(ActionContext context, ConguaglioBulk conguaglio) {

	try {

		CompensoBulk compenso = (CompensoBulk)conguaglio.getCompenso();
		boolean viewMode = getBusinessProcess(context).isViewing();
		if (compenso == null)
			throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Compenso inesistente.");
			
		String status = viewMode ?"V":"M";
		CRUDCompensoBP compensoBP = CRUDCompensoBP.getBusinessProcessFor(context, compenso, status + "RSWTh");
		compensoBP.edit(context,compenso);
		compensoBP.inizializzaCompensoPerConguaglio(context, conguaglio);

		context.addHookForward("bringback",this,"doBringBackCompenso");
		context.addHookForward("close",this,"doBringBackCompenso");
		HookForward hook = (HookForward)context.findForward("bringback");

		return context.addBusinessProcess(compensoBP);

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
  * Gestisce la selezione del bottone di ricerca delle Banche
  */

public Forward doSearchListaBanche(ActionContext context) {
	
	ConguaglioBulk conguaglio = (ConguaglioBulk)getBusinessProcess(context).getModel();
	String columnSet = conguaglio.getModalitaPagamento().getTiPagamentoColumnSet();
	return search(context, getFormField(context, "main.listaBanche"), columnSet);
}
/**
 *  Viene richiesto di visualizzare il compenso collegato al conguaglio
 *
**/
public Forward doVisualizzaCompenso(ActionContext context) {

	try {

		CRUDConguaglioBP bp = (CRUDConguaglioBP)getBusinessProcess(context);
		ConguaglioBulk conguaglio = (ConguaglioBulk)bp.getModel();

		fillModel(context);

		if (conguaglio != null && conguaglio.getPg_compenso()==null){
			setMessage(context, bp.WARNING_MESSAGE, "Non esiste un compenso collegato a questo conguaglio");
			return context.findDefaultForward();
		}
			
		CompensoBulk compenso = conguaglio.getCompenso();
		CRUDCompensoBP compBP = CRUDCompensoBP.getBusinessProcessFor(context, compenso, "VRSWTh");
		compBP.edit(context,compenso);

		return context.addBusinessProcess(compBP);
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}
