package it.cnr.contab.compensi00.actions;

import java.util.Date;
import java.util.GregorianCalendar;

import it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk;
import it.cnr.contab.compensi00.docs.bulk.*;
import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.BulkCollections;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.OptionBP;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2002 16.13.08)
 * @author: Roberto Fantino
 */
public class CRUDMinicarrieraAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDCompensoAction constructor comment.
 */
public CRUDMinicarrieraAction() {
	super();
}
/**
 * Creo un nuovo compenso da associare alle rate selezionate e lo apro in modalità 
 * inserimento. Viene validata la selezione con il metodo 'validaSelezionePerAssociazioneCompenso'
 */

private CRUDCompensoBP creaCompensoBP(ActionContext context, boolean setSafePoint) 
	throws BusinessProcessException {

	CRUDCompensoBP compensoBP = (CRUDCompensoBP)context.getUserInfo().createBusinessProcess(
															context,
															"CRUDCompensoBP",
															new Object[] { "MRSWTh" }
														);
	if (setSafePoint)
		compensoBP.setSavePoint(context, CRUDMinicarrieraBP.SAVE_POINT_NAME);
		
	return compensoBP;
}
/**
 * Inoltro la richiesta alla stored procedure per la generazione delle rate
 * Il modello deve essere prima validato dal metodo 'validate'
 */

public Forward doAzzeraAliquotaMedia(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		carriera.resetTassazioneSeparataData();
		bp.setDirty(true);
		setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Dati Irpef azzerati con successo.");

		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
private void doAzzeraTipoRapporto(ActionContext context, MinicarrieraBulk carriera) {

	if (carriera != null){
		carriera.setTipiRapporto(null);
		carriera.setTipo_rapporto(null);
		doAzzeraTipoTrattamento(context , carriera);
	}
}
private void doAzzeraTipoTrattamento(ActionContext context, MinicarrieraBulk carriera) {

	if (carriera!=null){
		carriera.setTipiTrattamento(null);
		carriera.setTipo_trattamento(null);
		carriera.setTipoPrestazioneCompenso(null);
		carriera.resetTassazioneSeparataData();
		
		//carriera.setFl_tassazione_separata(Boolean.FALSE);
		//carriera.setTi_istituz_commerc(carriera.TIPO_COMPENSO_ISTITUZIONALE);
	}
}
/**
 * Prepara la minicarriera per la ricerca di un nuovo percipiente
 */
public Forward doBlankSearchFind_percipiente(ActionContext context, MinicarrieraBulk carriera) {

	if (carriera != null){
		V_terzo_per_compensoBulk percipiente = new V_terzo_per_compensoBulk();
		percipiente.setTi_dipendente_altro(carriera.getTi_anagrafico());
		carriera.setPercipiente(percipiente);
		carriera.setNome(null);
		carriera.setCognome(null);
		carriera.setRagione_sociale(null);
		carriera.setPartita_iva(null);
		carriera.setCodice_fiscale(null);
		carriera.setTermini(null);
		carriera.setTermini_pagamento(null);
		carriera.setModalita(null);
		carriera.setModalita_pagamento(null);
		carriera.setBanca(null);
		carriera.setTipiRapporto(null);
		carriera.setTipo_rapporto(null);
		carriera.setTipiTrattamento(null);
		carriera.setTipo_trattamento(null);
		carriera.setTipoPrestazioneCompenso(null);
		carriera.setIncarichi_repertorio(null);
		//carriera.setTi_istituz_commerc(carriera.TIPO_COMPENSO_ISTITUZIONALE);
		//carriera.setFl_tassazione_separata(Boolean.FALSE);
		carriera.resetTassazioneSeparataData();
	}
	return context.findDefaultForward();

}
/**
 * Al ritorno della creazione di un compenso, associo alle rate selezionate questo documento
 */

public Forward doBringBackCompenso(ActionContext context) {

	HookForward caller = (HookForward)context.getCaller();
	CompensoBulk compenso = (CompensoBulk)caller.getParameter("bringback");
	
	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	if(compenso == null) {
		try {
			creaCompensoBP(context, false).rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return context.findDefaultForward();
	}

	try {
		java.util.List rateSelezionate = null;
		rateSelezionate = bp.getRateCRUDController().getSelectedModels(context);
		bp.getRateCRUDController().getSelection().clearSelection();

		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		carriera = ((MinicarrieraComponentSession)bp.createComponentSession()).associaCompenso(
									context.getUserContext(),
									carriera,
									rateSelezionate,
									compenso);
		bp.setModel(context, carriera);
		bp.setDirty(true);
			
		bp.setMessage("Associazione terminata con successo.");
		
		return context.findDefaultForward();
		
	} catch (Throwable t) {
		try {
			creaCompensoBP(context, false).rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return handleException(context, t);
	}
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca del percipiente
 */
 
public Forward doBringBackSearchFind_percipiente(ActionContext context, MinicarrieraBulk carriera, V_terzo_per_compensoBulk vTerzo) {

	try {

		if(vTerzo != null) {
			doBlankSearchFind_percipiente(context, carriera);
			CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP) getBusinessProcess(context);
			bp.completaPercipiente(context, carriera, vTerzo);
		} 
		return context.findDefaultForward();

	} catch(BusinessProcessException e) {
		return handleException(context,e);
	}
}
/**
 * Creo un nuovo compenso da associare alle rate selezionate e lo apro in modalità 
 * inserimento. Viene validata la selezione con il metodo 'validaSelezionePerAssociazioneCompenso'
 */

public Forward doBringBackVisualizzaCompenso(ActionContext context) {

	try	{
		creaCompensoBP(context, false).rollbackToSavePoint(
													context, 
													CRUDMinicarrieraBP.SAVE_POINT_NAME);
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Inoltro la richiesta alla stored procedure per la generazione delle rate
 * Il modello deve essere prima validato dal metodo 'validate'
 */

public Forward doCalcolaAliquotaMedia(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		//carriera.validate();

		if (carriera.getFl_tassazione_separata() == null || !carriera.getFl_tassazione_separata().booleanValue())
			throw new it.cnr.jada.comp.ApplicationException("Per calcolare l'aliquota media è necessario selezionare \"tassazione separata\"!");
		if (carriera.isAssociataACompensoConTassazioneSeparata())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile calcolare l'aliquota media perchè almeno una rata è collegata a compenso!");

		carriera.validaDate();
		carriera.validaPercipiente(true);
		carriera.validaImponibiliIrpef();
		
		bp.calcolaAliquotaMedia(context);
		bp.setDirty(true);
		setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Aliquota media calcolata con successo.");

		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la richiesta di cambiamento della data di registrazione della 
 * minicarriera. Viene verificata la validità del percipiente e suoi attributi
 */

public Forward doConfermaAzzeramentoTipoTrattamentoData(
	ActionContext context,
	OptionBP optionBP) {
		
	try{
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if (optionBP.getOption() == OptionBP.YES_BUTTON)
			carriera.resetTassazioneSeparataData();

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
public Forward doConfermaModificaDataInizioValidita(ActionContext context, OptionBP optionBP){

	try{
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		java.sql.Timestamp oldDataIni = (java.sql.Timestamp)optionBP.getAttribute("oldDataIni");
		int errorCodeTerzo = ((Integer)optionBP.getAttribute("errorCodeTerzo")).intValue();
		
		if (optionBP.getOption() == OptionBP.YES_BUTTON){
			switch (errorCodeTerzo) {
				case 5: {
					doAzzeraTipoRapporto(context, carriera);
					bp.findTipiRapporto(context);
					break;
				}
			}
		} else
			carriera.setDt_inizio_minicarriera(oldDataIni);

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
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		java.sql.Timestamp oldDataReg= (java.sql.Timestamp)optionBP.getAttribute("dataReg");
		int errorCodeTerzo = ((Integer)optionBP.getAttribute("errorCodeTerzo")).intValue();
		
		if (optionBP.getOption() == OptionBP.YES_BUTTON) {
			switch (errorCodeTerzo) {
				case 2: {
					doBlankSearchFind_percipiente(context, carriera);
					break;
				}case 7: {
					doAzzeraTipoTrattamento(context, carriera);
					bp.findTipiTrattamento(context);
					break;
				}
			}
		} else
			carriera.setDt_registrazione(oldDataReg);

		return context.findDefaultForward();

	} catch(Throwable ex){
		return handleException(context, ex);
	}
}
/**
 * Creo un nuovo compenso da associare alle rate selezionate e lo apro in modalità 
 * inserimento. Viene validata la selezione con il metodo 'validaSelezionePerAssociazioneCompenso'
 */

public Forward doCreaCompenso(ActionContext context) {

	try	{
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if (carriera.getModalita_pagamento() == null)
			throw new it.cnr.jada.comp.ApplicationException("Specificare la modalità di pagamento prima di creare il compenso!");
		if (carriera.getTipo_rapporto() == null)
			throw new it.cnr.jada.comp.ApplicationException("Specificare il tipo di rapporto prima di creare il compenso!");
		if (carriera.getTipo_trattamento() == null)
			throw new it.cnr.jada.comp.ApplicationException("Specificare il tipo di trattamento prima di creare il compenso!");
		if (carriera.getTipoPrestazioneCompenso() == null && carriera.isVisualizzaPrestazione())
			throw new it.cnr.jada.comp.ApplicationException("Specificare il tipo di prestazione prima di creare il compenso!");
		
		try {
			carriera.validaCorpo();
		} catch (it.cnr.jada.bulk.ValidationException e) {
			throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		}
		/*
		if (bp.isGestitePrestazioni(context.getUserContext()))
			carriera.impostaVisualizzaPrestazione();
		else
			carriera.setVisualizzaPrestazione(false);
		*/
		carriera.impostaVisualizzaPrestazione();
		
		if (bp.isGestitiIncarichi(context.getUserContext()))
			carriera.impostaVisualizzaIncarico();
		else
			carriera.setVisualizzaIncarico(false);
		
		if(carriera.getPg_minicarriera()!=null && !(carriera.getPg_minicarriera().compareTo(new Long(0)) <0))
		{
			if(carriera.isVisualizzaPrestazione() && (carriera.getTipoPrestazioneCompenso() == null || carriera.getTipoPrestazioneCompenso().getCrudStatus()== OggettoBulk.UNDEFINED))
			{
				setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Inserire il tipo di prestazione.");
				return context.findDefaultForward();			
			}
			if(carriera.isVisualizzaIncarico() && (carriera.getIncarichi_repertorio() == null || carriera.getIncarichi_repertorio().getCrudStatus()== OggettoBulk.UNDEFINED))
			{
				setMessage(context, it.cnr.jada.util.action.FormBP.WARNING_MESSAGE, "Inserire il Contratto.");
				return context.findDefaultForward();			
			}	
		}
		

		java.util.List rateSelezionate = bp.getRateCRUDController().getSelectedModels(context);
		
		validaSelezionePerAssociazioneCompenso(context, carriera, rateSelezionate);
		
		context.addHookForward("bringback",this,"doBringBackCompenso");
//		context.addHookForward("close",this,"doBringBackCompenso");

		CRUDCompensoBP compensoBP = creaCompensoBP(context, true);
		try {
			compensoBP.reset(context);
			CompensoBulk compenso = (CompensoBulk)compensoBP.getModel();
			
			// Lancia procedura di Paolo per importi compenso
			compenso.setIm_lordo_percipiente(carriera.calcolaTotaleRate(rateSelezionate));
			//compenso.setQuota_esente(new java.math.BigDecimal(0));

			it.cnr.contab.compensi00.ejb.CompensoComponentSession component = (it.cnr.contab.compensi00.ejb.CompensoComponentSession)bp.createComponentSession("CNRCOMPENSI00_EJB_CompensoComponentSession",it.cnr.contab.compensi00.ejb.CompensoComponentSession.class );
			compenso = component.inizializzaCompensoPerMinicarriera(
											context.getUserContext(), 
											compenso, 
											carriera, 
											rateSelezionate);
			compensoBP.setModel(context, compenso);
		} catch (Throwable t) {
			compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
			throw t;
		}
			
		return context.addBusinessProcess(compensoBP);
	} catch(Throwable e) {
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
 * Gestisco il cambiamento dell'aspetto anticipio/posticipo.
 * Se il valore nuovo impostato è TIPO_NESSUNO pongo i mesi al valore di default
 */

public Forward doOnAnticipoPosticipoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if (carriera.TIPO_NESSUNO.equalsIgnoreCase(carriera.getTi_anticipo_posticipo())) {
			carriera.setMesi_anticipo_posticipo(new Integer(0));
		}
		
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce l'eccezione CheckDisponibilitaCassaFailed generata dall'obbligazione
 * mantenendo traccia della scelta di conferma o annullamento dell'operazione
 * da parte dell'utente
 */

public Forward doOnCheckDisponibilitaCassaFailed(
	ActionContext context,
	int option) {

	if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON) {
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		try {
			boolean modified = fillModel(context);
			OptionRequestParameter userConfirmation = new OptionRequestParameter();
			userConfirmation.setCheckDisponibilitaDiCassaRequired(Boolean.FALSE);
			bp.setUserConfirm(userConfirmation);
			if (bp.isBringBack())
				doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
			else
				doSalva(context);
		} catch(Throwable e) {
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
public Forward doOnCheckDisponibilitaContrattoFailed( ActionContext context, int option) {
    if (option == it.cnr.jada.util.action.OptionBP.OK_BUTTON){
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
        try 
        {
            boolean modified = fillModel(context);
            OptionRequestParameter userConfirmation = new OptionRequestParameter();
            userConfirmation.setCheckDisponibilitaContrattoRequired(Boolean.FALSE);
            bp.setUserConfirm(userConfirmation);
            if (bp.isBringBack())
                doConfermaRiporta(context, it.cnr.jada.util.action.OptionBP.OK_BUTTON);
            else
                doSalva(context);
        } 
        catch(Throwable e){
            return handleException(context,e);
        }
    }
    return context.findDefaultForward();
}

/**
 * Gestisce la richiesta di cessazione della minicarriera eseguendo anche dei
 * controlli sulla data di cessazione impostata
 */


public Forward doOnDtCessataChange(ActionContext context) {

	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	try {
		fillModel(context);
		if (!bp.isSearching() && carriera.getDt_cessazione() != null) {

			if (carriera.isAttiva()) {
				if (carriera.getDt_inizio_minicarriera() == null)
				  throw new it.cnr.jada.comp.ApplicationException("Inserire la data inizio validità!");
				if ((carriera.getDt_cessazione().before(carriera.getDt_inizio_minicarriera()) &&
					!(carriera.getDt_cessazione().equals(carriera.getDt_inizio_minicarriera()))))
					throw new it.cnr.jada.comp.ApplicationException("La data di cessazione deve essere maggiore della data inizio validità!");
			} else if(carriera.isSospesa()) {
				if (carriera.getDt_sospensione() == null)
				  throw new it.cnr.jada.comp.ApplicationException("Inserire la data di sospensione!");				
				if ((carriera.getDt_cessazione().before(carriera.getDt_sospensione()) &&
					!(carriera.getDt_cessazione().equals(carriera.getDt_sospensione()))))
					throw new it.cnr.jada.comp.ApplicationException("La data di cessazione deve essere maggiore della data sospensione!");
			}
			
			MinicarrieraBulk carrCessata = ((MinicarrieraComponentSession)bp.createComponentSession()).cessa(
																context.getUserContext(), 
																carriera);
			bp.commitUserTransaction();
			
			bp.edit(context, carrCessata);
		}			
		return context.findDefaultForward();

	}catch (Throwable ex) {
		carriera.setDt_cessazione(null);
		try {
			bp.rollbackUserTransaction();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento delle date di fine validità della 
 * minicarriera. Viene verificata la validità del percipiente e suoi attributi
 */
 
public Forward doOnDtFineValiditaChange(ActionContext context) {

	try {
		//fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		java.sql.Timestamp oldDataFine = ((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera();
		fillModel(context);
		if (oldDataFine == null)
			return context.findDefaultForward();
		if(((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera() == null)
			  ((MinicarrieraBulk)bp.getModel()).setDt_fine_minicarriera(oldDataFine);
		GregorianCalendar data_a = (GregorianCalendar) GregorianCalendar.getInstance();
		GregorianCalendar old_data_a = (GregorianCalendar) GregorianCalendar.getInstance();
		data_a.setTime(((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera());
		old_data_a.setTime(oldDataFine);
		
		int annoCompetenzaA = data_a.get(java.util.GregorianCalendar.YEAR); 
		int annoOldCompetenzaA = old_data_a.get(java.util.GregorianCalendar.YEAR); 
		
		if (bp.isTerzoCervellone(context.getUserContext(),(MinicarrieraBulk)bp.getModel()) &&
				annoCompetenzaA != annoOldCompetenzaA)
		{
			((MinicarrieraBulk)bp.getModel()).setDt_fine_minicarriera(oldDataFine);
			throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
		}
		if (!((MinicarrieraBulk)bp.getModel()).getFl_tassazione_separata().booleanValue())
		((MinicarrieraBulk)bp.getModel()).setNumero_rate(new Integer(it.cnr.jada.util.DateUtils.monthsBetweenDates(new Date(((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera().getTime()),new Date(((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera().getTime()))));
		
		if (!bp.isSearching()) {
			MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
			carriera.validaDate();
			try {
				if (carriera.getPercipiente() != null && 
					carriera.getPercipiente().getCrudStatus() == OggettoBulk.NORMAL)
					carriera.validaPercipiente(true);
			} catch (it.cnr.jada.bulk.ValidationException e) {
				bp.setMessage(
					it.cnr.jada.util.action.OptionBP.WARNING_MESSAGE,
					e.getMessage());
			}
		}

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento delle date di inizio validità della 
 * minicarriera. Viene verificata la validità del percipiente e suoi attributi
 */

public Forward doOnDtInizioValiditaChange(ActionContext context) {

	try {
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		java.sql.Timestamp oldDataInizio = ((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera();
		fillModel(context);
		if (oldDataInizio == null)
			return context.findDefaultForward();
		if(((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera() == null)
		  ((MinicarrieraBulk)bp.getModel()).setDt_inizio_minicarriera(oldDataInizio);
        
		GregorianCalendar data_da = (GregorianCalendar) GregorianCalendar.getInstance();
		GregorianCalendar old_data_da = (GregorianCalendar) GregorianCalendar.getInstance();
		data_da.setTime(((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera());
		old_data_da.setTime(oldDataInizio);
		
		int annoCompetenzaDa = data_da.get(java.util.GregorianCalendar.YEAR); 
		int annoOldCompetenzaDa = old_data_da.get(java.util.GregorianCalendar.YEAR); 
		
		if (bp.isTerzoCervellone(context.getUserContext(),(MinicarrieraBulk)bp.getModel()) &&
				annoCompetenzaDa != annoOldCompetenzaDa)
		{
			((MinicarrieraBulk)bp.getModel()).setDt_inizio_minicarriera(oldDataInizio);
			throw new it.cnr.jada.comp.ApplicationException("Operazione non consentita. Non è possibile cambiare l'anno poichè il Terzo scelto potrebbe essere soggetto ad Agevolazioni per 'Rientro dei Cervelli'.");
		}
        
		int errorCodeTerzo = bp.validaPercipiente(context, true);
		if (errorCodeTerzo==5){
			String msg = null;
			switch (errorCodeTerzo) {
				case 5: {
					msg = "Il tipo rapporto selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
					break;
				}
			}
				
			OptionBP option = openConfirm(context, msg , OptionBP.CONFIRM_YES_NO,"doConfermaModificaDataInizioValidita");
			option.addAttribute("oldDataIni", oldDataInizio);
			option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
			return option;
		}
		if (!((MinicarrieraBulk)bp.getModel()).getFl_tassazione_separata().booleanValue())
		((MinicarrieraBulk)bp.getModel()).setNumero_rate(new Integer(it.cnr.jada.util.DateUtils.monthsBetweenDates(new Date(((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera().getTime()),new Date(((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera().getTime()))));		
		try {
			bp.findTipiRapporto(context);
		} catch (BusinessProcessException e) {
			doAzzeraTipoTrattamento(context, (MinicarrieraBulk)bp.getModel());
			throw e;
		}
		bp.ripristinaSelezioneTipoRapporto();

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento della data di registrazione della 
 * minicarriera. Viene verificata la validità del percipiente e suoi attributi
 */

public Forward doOnDtRegistrazioneChange(ActionContext context) {
	try {
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		java.sql.Timestamp oldDataReg = ((MinicarrieraBulk)bp.getModel()).getDt_registrazione();
		fillModel(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if (!bp.isSearching())
			try{
				int annoSolare = carriera.getAnno(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
				if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() &&
					carriera.getDt_registrazione() != null &&
					carriera.getAnno(carriera.getDt_registrazione()) != carriera.getEsercizio().intValue())
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: la data di registrazione deve appartenere all'esercizio del documento");
			} catch(it.cnr.jada.comp.ApplicationException e) {
				carriera.setDt_registrazione(oldDataReg);
				throw e;
			}
			
		if (!bp.isSearching() &&
			carriera.getPercipiente() != null && 
			carriera.getPercipiente().getCrudStatus() == OggettoBulk.NORMAL) {
			
			int errorCodeTerzo = bp.validaPercipiente(context, true);
			if (errorCodeTerzo==2 || errorCodeTerzo==7) {
				String msg = null;
				switch (errorCodeTerzo) {
					case 2: {
						msg = "Il percipiente selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
						break;
					}case 7: {
						msg = "Il tipo trattamento selezionato non è più valido! I dati verranno persi. Vuoi continuare?";
						break;
					}
				}
					
				OptionBP option = openConfirm(context, msg , OptionBP.CONFIRM_YES_NO,"doConfermaModificaDataRegistrazione");
				option.addAttribute("dataReg", oldDataReg);
				option.addAttribute("errorCodeTerzo", new Integer(errorCodeTerzo));
				return option;
			}
		}

		bp.findTipiTrattamento(context);
		bp.ripristinaSelezioneTipoTrattamento();
		
		carriera = (MinicarrieraBulk)bp.getModel();
		if (carriera.isNonAssociataACompenso() &&
			carriera.getFl_tassazione_separata() != null &&
			carriera.getFl_tassazione_separata().booleanValue())
			return openConfirm(context, "Vuoi cancellare i dati relativi all'aliquota irpef media?" , OptionBP.CONFIRM_YES_NO,"doConfermaAzzeramentoTipoTrattamentoData");
			
		return context.findDefaultForward();

	} catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di rinnovo della minicarriera eseguendo anche dei
 * controlli sulla data di rinnovo impostata
 */
 
public Forward doOnDtRinnovataChange(ActionContext context) {

	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	try {
		fillModel(context);
		if (!bp.isSearching()) {

			if (carriera.isAttiva() && carriera.getDt_rinnovo() != null && carriera.getDt_inizio_minicarriera() != null && carriera.getDt_inizio_minicarriera() != null && 
			    carriera.getDt_rinnovo().before(carriera.getDt_inizio_minicarriera()) &&
				!carriera.getDt_rinnovo().equals(carriera.getDt_inizio_minicarriera()))
					throw new it.cnr.jada.comp.ApplicationException("La data di rinnovo deve essere maggiore della data di inizio validità!");

			MinicarrieraBulk carrRinnovata = ((MinicarrieraComponentSession)bp.createComponentSession()).rinnova(
																context.getUserContext(), 
																carriera);
			
			//Patch per pacchetto di copia di Paolo. Quando torna dalle ferie avvertire
			//e togliere la riga seguente!!
			//Nel caso di rinnovo (copia) di una minicarriera associata a compensi lo stato rimane tale e non posso generare rate!
			carrRinnovata.setStato_ass_compenso(carrRinnovata.STATO_NON_ASS_COMPENSO);
			carrRinnovata.setStato(carrRinnovata.STATO_ATTIVA);
			carrRinnovata.setDt_rinnovo(null);
			carrRinnovata.setDt_ripristino(null);
			carrRinnovata.setDt_sospensione(null);
			carrRinnovata.setDt_cessazione(null);
			carrRinnovata.setMinicarriera_origine(carriera);
			//
			
			bp.setStatus(bp.INSERT);
			bp.setModel(context, carrRinnovata);
		}
		return context.findDefaultForward();

	} catch (Throwable ex) {
		carriera.setDt_rinnovo(null);
		try {
			bp.rollbackUserTransaction();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di ripristino della minicarriera eseguendo anche dei
 * controlli sulla data di ripristino impostata
 */
 
public Forward doOnDtRipristinataChange(ActionContext context) {

	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

	try {
		fillModel(context);
		if (!bp.isSearching()) {

			if (carriera.isAttiva()) {
				if ((carriera.getDt_ripristino().before(carriera.getDt_inizio_minicarriera()) &&
					!(carriera.getDt_ripristino().equals(carriera.getDt_inizio_minicarriera()))))
					throw new it.cnr.jada.comp.ApplicationException("La data di ripristino deve essere maggiore della data inizio validità!");
			} else if(carriera.isSospesa()) {
				if ((carriera.getDt_ripristino().before(carriera.getDt_sospensione()) &&
					!(carriera.getDt_ripristino().equals(carriera.getDt_sospensione()))))
					throw new it.cnr.jada.comp.ApplicationException("La data di ripristino deve essere maggiore della data sospensione!");
			}

			MinicarrieraBulk carrRipristinata = ((MinicarrieraComponentSession)bp.createComponentSession()).ripristina(
																context.getUserContext(), 
																carriera);
			//Patch per pacchetto di copia di Paolo. Quando torna dalle ferie avvertire
			//e togliere la riga seguente!!
			//Nel caso di rinnovo (copia) di una minicarriera associata a compensi lo stato rimane tale e non posso generare rate!
			carrRipristinata.setStato_ass_compenso(carrRipristinata.STATO_NON_ASS_COMPENSO);
			carrRipristinata.setStato(carrRipristinata.STATO_ATTIVA);
			carrRipristinata.setDt_rinnovo(null);
			carrRipristinata.setDt_ripristino(null);
			carrRipristinata.setDt_sospensione(null);
			carrRipristinata.setDt_cessazione(null);
			carrRipristinata.setMinicarriera_origine(carriera);
			//
			
			bp.setStatus(bp.INSERT);
			bp.setModel(context, carrRipristinata);
		}
			
		return context.findDefaultForward();

	} catch (Throwable ex) {
		carriera.setDt_ripristino(null);
		try {
			bp.rollbackUserTransaction();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di sospensione della minicarriera eseguendo anche dei
 * controlli sulla data di sospensione impostata
 */
 
public Forward doOnDtSospesaChange(ActionContext context) {

	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	
	try {
		fillModel(context);
		if (!bp.isSearching() && carriera.getDt_sospensione() != null) {
			if (carriera.getDt_inizio_minicarriera() == null)
			  throw new it.cnr.jada.comp.ApplicationException("Inserire la data di inizio validità!");				
			if (carriera.isAttiva() && carriera.getDt_sospensione().before(carriera.getDt_inizio_minicarriera()) &&
				!carriera.getDt_sospensione().equals(carriera.getDt_inizio_minicarriera()))
					throw new it.cnr.jada.comp.ApplicationException("La data di sospensione deve essere maggiore della data di inizio validità!");
			
			MinicarrieraBulk carrSospesa = ((MinicarrieraComponentSession)bp.createComponentSession()).sospendi(
																context.getUserContext(), 
																carriera);
			bp.commitUserTransaction();
			
			bp.edit(context, carrSospesa);
		}
		return context.findDefaultForward();

	}catch (Throwable ex) {
		carriera.setDt_sospensione(null);
		try {
			bp.rollbackUserTransaction();
		} catch (BusinessProcessException e) {
			return handleException(context, e);
		}
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento del tipo rapporto
 * Vengono ricercati i nuovi tipi di trattamento validi
 */

public Forward doOnFlTassazioneSeparataChange(ActionContext context) {
	
	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		if  (!bp.isSearching()){
			MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
			if (!carriera.getFl_tassazione_separata().booleanValue())
				carriera.setNumero_rate(new Integer(it.cnr.jada.util.DateUtils.monthsBetweenDates(new Date(((MinicarrieraBulk)bp.getModel()).getDt_inizio_minicarriera().getTime()),new Date(((MinicarrieraBulk)bp.getModel()).getDt_fine_minicarriera().getTime()))));
			else
				carriera.setNumero_rate(new Integer(1));
		}
	return doOnTipoRapportoChange(context);
	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce il cambiamento del tipo sezionale ricaricandoli
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
 
public Forward doOnIstituzionaleCommercialeChange(ActionContext context) {
	
	return doOnTipoRapportoChange(context);
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
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();

		if (!bp.isSearching())
			return doBlankSearchFind_percipiente(context, carriera);
		return context.findDefaultForward();

	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
 * Gestisce la richiesta di cambiamento del tipo rapporto
 * Vengono ricercati i nuovi tipi di trattamento validi
 */

public Forward doOnTipoRapportoChange(ActionContext context) {

	try {
		fillModel(context);
		PostTipoRapportoChange(context);
		
		return context.findDefaultForward();
	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
/**
 * Gestisce la richiesta di cambiamento del tipo rapporto
 * Vengono ricercati i nuovi tipi di trattamento validi
 */

public Forward doOnTipoTrattamentoChange(ActionContext context) {

	try {
		fillModel(context);
		
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)context.getBusinessProcess();
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		if(!carriera.getTipo_trattamento().getFl_visibile_a_tutti()&& !UtenteBulk.isAbilitatoAllTrattamenti(context.getUserContext()))
		{
			doAzzeraTipoTrattamento(context, carriera);
			bp.findTipiTrattamento(context);
			throw new it.cnr.jada.comp.ApplicationException(
		    "Utente non abilitato all'utilizzo del trattamento selezionato!");
		}	
		
		bp.findTipiPrestazioneCompenso(context);
		//carriera.setTipoPrestazioneCompenso(null);
		
		PostTipoTrattamentoChange(context);
		
		return context.findDefaultForward();
	} catch (Throwable ex) {
		return handleException(context, ex);
	}
}
public Forward doOnTipoPrestazioneCompensoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)context.getBusinessProcess();
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		if(carriera.getTipoPrestazioneCompenso()== null 
		   || 
		   (carriera.getTipoPrestazioneCompenso()!= null && !carriera.getTipoPrestazioneCompenso().getFl_incarico()))
		{
		carriera.setIncarichi_repertorio(null);
		carriera.setVisualizzaIncarico(false);
		}
		else
			carriera.setVisualizzaIncarico(true);
		
		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
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
					it.cnr.jada.util.action.OptionBP.WARNING_MESSAGE,
					e.getMessage());
			}
		}

		return super.doTab( context, tabName, pageName );

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Creo un nuovo compenso da associare alle rate selezionate e lo apro in modalità 
 * inserimento. Viene validata la selezione con il metodo 'validaSelezionePerAssociazioneCompenso'
 */

public Forward doVisualizzaCompenso(ActionContext context) {

	try	{
		fillModel(context);
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		
		Minicarriera_rataBulk rataSelezionata = (Minicarriera_rataBulk)bp.getRateCRUDController().getModel();
		if (rataSelezionata == null || !rataSelezionata.isAssociataACompenso())
			throw new it.cnr.jada.comp.ApplicationException("Selezionare una rata collegata a compenso per continuare!");

		context.addHookForward("bringback",this,"doBringBackVisualizzaCompenso");
//		context.addHookForward("close",this,"doBringBackVisualizzaCompenso");

		CRUDCompensoBP compensoBP = (CRUDCompensoBP)context.createBusinessProcess(
											"CRUDCompensoBP",
											new Object[] { "VRSWTh" });
		compensoBP.setSavePoint(context, bp.SAVE_POINT_NAME);
		try {
			compensoBP.edit(context, rataSelezionata.getCompenso());
			((CompensoBulk)compensoBP.getModel()).setMinicarriera(carriera);
		} catch (Throwable t) {
			compensoBP.rollbackToSavePoint(context, bp.SAVE_POINT_NAME);
			throw t;
		}
			
		return context.addBusinessProcess(compensoBP);
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
 * Valida la selezione delle rate della minicarriera in richiesta di associazione
 * a compenso
 */

private void validaSelezionePerAssociazioneCompenso(
	ActionContext context,
	MinicarrieraBulk carriera,
	java.util.List rateSelezionate)
	throws it.cnr.jada.comp.ApplicationException {

	if (rateSelezionate == null || rateSelezionate.isEmpty())
		throw new it.cnr.jada.comp.ApplicationException("Selezionare le rate da associare al compenso!");

	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);

	Minicarriera_rataBulk rataPrec = null;
	try {
		for (java.util.Iterator i = rateSelezionate.iterator(); i.hasNext();) {
			Minicarriera_rataBulk rata = (Minicarriera_rataBulk)i.next();
			bp.getRateCRUDController().validate(context, rata);
			if (rata.isAssociataACompenso())
				throw new it.cnr.jada.comp.ApplicationException("La rata \"" + rata.getPg_rata().longValue() + "\" è già stata associata a compenso! Operazione annullata.");
			if (rataPrec != null) {
				if (!carriera.incrementaData(rataPrec.getDt_fine_rata()).equals(carriera.getDataOdierna(rata.getDt_inizio_rata())))
					throw new it.cnr.jada.comp.ApplicationException("La data di fine validità della rata \"" + rataPrec.getPg_rata().longValue() + "\" deve essere contigua\nalla data inizio validità della rata \"" + rata.getPg_rata().longValue() + "\"\nper essere associata allo stesso compenso!");
				if (carriera.getFl_tassazione_separata() != null && carriera.getFl_tassazione_separata().booleanValue()) {
					//Controllo che, nel caso di tassazione separata, tutte
					//le rate da associare a compenso selezionate siano tutte
					//nell'anno precedente all'esercizio della minic
					if (carriera.getAnno(rataPrec.getDt_fine_rata()) < carriera.getEsercizio().intValue() &&
						carriera.getAnno(rata.getDt_fine_rata()) >= carriera.getEsercizio().intValue())
						throw new it.cnr.jada.comp.ApplicationException("Selezione multipla non corretta!\nIn tassazione separata le rate associabili allo stesso compenso devono appartenere tutte\nallo stesso anno (rispetto alla \"data fine\" della rata stessa e all'esercizio della minicarriera).");
				}
			}
			if (BulkCollections.indexOfByPrimaryKey(bp.getRateCRUDController().getDetails(),rata) == bp.getRateCRUDController().countDetails()-1 &&
				!rata.getDt_fine_rata().equals(carriera.getDt_fine_minicarriera()))
				throw new it.cnr.jada.comp.ApplicationException("La data \"fine validità\" dell'ultima rata NON corrisponde alla data fine validità della minicarriera! Operazione annullata.");
			rataPrec = rata;
		}
	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	}
}
public Forward doBringBackSearchIncarichi_repertorio(ActionContext context, MinicarrieraBulk carriera, Incarichi_repertorioBulk incarico) throws BusinessProcessException {
	if(incarico != null)
	try 
	{
			fillModel(context);
			CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);

			if(carriera.getTipo_rapporto()==null )
			{
				carriera.impostaTipo_rapporto(incarico.getTipo_rapporto());
				PostTipoRapportoChange(context);
			}
			if(carriera.getTipo_trattamento()==null)
				bp.findTipiTrattamento(context);

			carriera.setIncarichi_repertorio(incarico);
			
			//bp.completaIncarico(context, carriera,incarico);
			PostTipoTrattamentoChange(context);
			bp.setDirty(true);
			
	}catch (Throwable ex) {
			return handleException(context, ex);
	}
	return context.findDefaultForward();
}
public void PostTipoRapportoChange(ActionContext context) {

	try {
		CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
		bp.findTipiTrattamento(context);

		MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
		carriera.resetTassazioneSeparataData();
		/*
		if (bp.isGestitePrestazioni(context.getUserContext())) 
			carriera.impostaVisualizzaPrestazione();
		else
			carriera.setVisualizzaPrestazione(false);
		carriera.setTipoPrestazioneCompenso(null);
		*/
		bp.findTipiPrestazioneCompenso(context);
		carriera.impostaVisualizzaPrestazione();
		
		if (bp.isGestitiIncarichi(context.getUserContext())) 
			carriera.impostaVisualizzaIncarico();
		else
			carriera.setVisualizzaIncarico(false);
		carriera.setIncarichi_repertorio(null);

	}catch (Throwable ex) {
		handleException(context, ex);
	}
}
public void PostTipoTrattamentoChange(ActionContext context) {
	
	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)getBusinessProcess(context);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	carriera.resetTassazioneSeparataData();

	try {
		/*
		if (bp.isGestitePrestazioni(context.getUserContext())) 
			carriera.impostaVisualizzaPrestazione();
		else
			carriera.setVisualizzaPrestazione(false);
		*/
		carriera.impostaVisualizzaPrestazione();
		if (bp.isGestitiIncarichi(context.getUserContext())) 
			carriera.impostaVisualizzaIncarico();
		else
			carriera.setVisualizzaIncarico(false);
		
	}catch (Throwable ex) {
		handleException(context, ex);
	}
	
}
}
