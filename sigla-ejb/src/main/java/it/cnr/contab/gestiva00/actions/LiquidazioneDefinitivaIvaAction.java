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

package it.cnr.contab.gestiva00.actions;

import java.math.BigDecimal;
import java.sql.Timestamp;
import it.cnr.contab.gestiva00.bp.LiquidazioneDefinitivaIvaBP;
import it.cnr.contab.gestiva00.bp.LiquidazioneIvaBP;
import it.cnr.contab.gestiva00.core.bulk.IPrintable;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_provvisoria_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.MTUWrapper;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.ConsultazioniBP;

/**
 * Liquidazione iva
 */

public class LiquidazioneDefinitivaIvaAction  extends StampaAction{
public LiquidazioneDefinitivaIvaAction() {
	super();
}
/**
 * gestisce la creazione dei registri iva
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
protected Forward basicDoCerca(
		ActionContext context)
		throws Throwable {
		LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
		Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)bp.getModel();
		if (!bp.isUoEnte() && bulk.getTipoSezionaleFlag().equals(bulk.SEZIONALI_COMMERCIALI))
		{
			bp.saveRipartizioneFinanziaria(context);

			String message=null;
			MTUWrapper wrapper = makeLiquidazioneProvvisoria(context);
			Liquidazione_ivaBulk newLiquidazione = (Liquidazione_ivaBulk)((Liquidazione_provvisoria_ivaVBulk)wrapper.getBulk()).getLiquidazione_iva();
			Liquidazione_ivaBulk oldLiquidazione = bulk.getLastLiquidazioneProvvisoria();
			
			if (oldLiquidazione==null || newLiquidazione.getIva_da_versare().compareTo(oldLiquidazione.getIva_da_versare())!=0) {
				if (newLiquidazione.getIva_da_versare().compareTo(BigDecimal.ZERO)<0 &&
				    bulk.getTotaleRipartizioneFinanziaria().compareTo(newLiquidazione.getIva_da_versare().abs())!=0) {
					bp.commitUserTransaction();
					bp.inizializzaMese(context);
					if (!bp.isStanziamentoAccentrato())
						message = "Attenzione! L'importo da versare è stato aggiornato e non corrisponde al totale ripartito per esercizio! Saranno create variazioni temporanee da completare successivamente! Si desidera continuare?";
				}
			} else {
				bp.rollbackUserTransaction();
				if (bulk.getTotaleRipartizioneFinanziaria().compareTo(bulk.getDebitoLastLiquidazioneProvvisoria())!=0 &&
					!bp.isStanziamentoAccentrato())
					message = "Attenzione! L'importo da versare non corrisponde al totale ripartito per esercizio! Saranno create variazioni temporanee da completare successivamente! Si desidera continuare?";
			}

			 if (message!=null)
				 return openConfirm(context,message,it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaCerca");
		}  
		return basicDoCercaConfermato(context);
}
public Forward doConfermaCerca(ActionContext context,int option) {

	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			return basicDoCercaConfermato(context);
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
protected Forward basicDoCercaConfermato(
	ActionContext context)
	throws Throwable {

	LiquidazioneIvaBP bp= (LiquidazioneIvaBP) context.getBusinessProcess();
	if (bp.isLiquidato())
		throw new it.cnr.jada.comp.ApplicationException("Premere su 'reset dati' prima di proseguire");
	Stampa_registri_ivaVBulk bulk = (Stampa_registri_ivaVBulk)bp.getModel();

	if (bp.isBulkPrintable())
		((IPrintable)bulk).setId_report(null);
	bulk.setRistampa(false);
       	
	MTUWrapper wrapper = manageStampa(context, bulk);
	bp.setLiquidato(true);
	
	Liquidazione_definitiva_ivaVBulk stampaBulk= (Liquidazione_definitiva_ivaVBulk) wrapper.getBulk();
	stampaBulk = ((LiquidazioneDefinitivaIvaBP)bp).aggiornaProspetti(context,stampaBulk);
	stampaBulk.aggiornaTotali();
	bp.setModel(context, stampaBulk);

	String message = getMessageFrom(wrapper);
	bp.commitUserTransaction();
	if (message != null)
		bp.setMessage(message);
	else { 
		if (bp.isBulkPrintable())
			return doStampa(context, (IPrintable)bp.getModel(), wrapper);
		bp.setMessage("Operazione effettuata in modo corretto");
	}

	return context.findDefaultForward();
}
protected Forward basicDoRistampa(ActionContext context) 
	throws Throwable {

	LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
	Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)bp.getModel().clone();

	if (bp.isBulkReprintable()) {
		
		Liquidazione_ivaBulk liqDef = (Liquidazione_ivaBulk)bp.getDettaglio_prospetti().getModel();
		if (liqDef == null)
			throw new it.cnr.jada.comp.ApplicationException("Selezionare la liquidazione definitiva da ristampare!");

		bulk.completeFrom(liqDef);
        bulk.validate();
		bulk.setRistampa(true);
		((IPrintable)bulk).setId_report(new java.math.BigDecimal(liqDef.getReport_id().doubleValue()));

		return doStampa(context, (IPrintable)bulk, (MTUWrapper)null);
	}
	
	bp.rollbackUserTransaction();
	bp.setMessage(
				it.cnr.jada.util.action.OptionBP.ERROR_MESSAGE, 
				"Questo tipo di registro non è ristampabile");
	return context.findDefaultForward();
}
public Forward doOnTipoChange(ActionContext context) {

    try {
        fillModel(context);
        LiquidazioneDefinitivaIvaBP bp = (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
        Liquidazione_definitiva_ivaVBulk liquidazione = (Liquidazione_definitiva_ivaVBulk) bp.getModel();
		//liquidazione.setTipi_sezionali(
			//bp.createComponentSession().selectTipi_sezionaliByClause(
												//context.getUserContext(),
												//liquidazione,
												//new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),
												//null));

		bp.aggiornaProspetti(context, liquidazione);

		aggiornaRegistriStampati(context);
		if (liquidazione.getTipoSezionaleFlag().equals(liquidazione.SEZIONALI_COMMERCIALI))
		{
			liquidazione.setLiquidazione_commerciale(true);
		   	liquidazione.setTipoImpegnoFlag(liquidazione.IMPEGNI_COMPETENZA);
		}
		else
		{
			liquidazione.setLiquidazione_commerciale(false);
			liquidazione.setTipoImpegnoFlag(null);
		}

		bp.inizializzaMese(context);
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
public Forward doReset(ActionContext context) {

    try {
        LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();

        bp.resetForSearch(context);

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
/**
 * Gestisce la selezione dopo una richiesta di ricerca.
 *
 * L'implementazione di default utilizza il metodo astratto <code>read</code>
 * di <code>CRUDBusinessProcess</code>.
 * Se la ricerca fornisce più di un risultato viene creato un
 * nuovo <code>SelezionatoreListaBP</code> per la selezione di un elemento.
 * Al business process viene anche chiesto l'elenco delle colonne da
 * visualizzare.
 */

protected Forward doStampa(
	ActionContext context,
	IPrintable stampaBulk,
	it.cnr.jada.bulk.MTUWrapper wrapper)
	throws BusinessProcessException {

	String message = "L'operazione è stata eseguita correttamente. Se si desidera, eseguire la stampa del report.";

	String s = getMessageFrom(wrapper);
	if (s != null)
		message = s + "! Se si desidera, eseguire la stampa del report.";

	return doStampa(context, stampaBulk, message);
}
public it.cnr.jada.action.Forward doSaveRipartizioneFinanziaria(ActionContext context) {
    LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
    try {
        bp.fillModel(context);
		bp.saveRipartizioneFinanziaria(context);
    } catch (Exception e) {
		return handleException(context, e);
    }
	return context.findDefaultForward();
}
public it.cnr.jada.action.Forward doAggiornaIvaDaVersare(ActionContext context) {
    try {
        fillModel(context);
        return openConfirm(context,"Desideri effettuare l'aggiornamento dell'iva da versare?",it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaAggiornaIvaDaVersare");
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
public Forward doConfermaAggiornaIvaDaVersare(ActionContext context,int option) {
	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
			Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)bp.getModel();

			MTUWrapper wrapper = makeLiquidazioneProvvisoria(context);

			Liquidazione_ivaBulk newLiquidazione = (Liquidazione_ivaBulk)((Liquidazione_provvisoria_ivaVBulk)wrapper.getBulk()).getLiquidazione_iva();
			Liquidazione_ivaBulk oldLiquidazione = bulk.getLastLiquidazioneProvvisoria();

			if (oldLiquidazione!=null && newLiquidazione.equalsByImporti(oldLiquidazione)) {
				bp.rollbackUserTransaction();
				bp.setMessage("Aggiornamento effettuato. Nessun cambiamento rilevato!");
			} else {
				bp.commitUserTransaction();
				bp.inizializzaMese(context);
				bp.setMessage("Aggiornamento Iva da Versare effettuata in modo corretto");
			}

			return context.findDefaultForward();
		}
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}

private MTUWrapper makeLiquidazioneProvvisoria(ActionContext context) throws Throwable{
	LiquidazioneDefinitivaIvaBP bp= (LiquidazioneDefinitivaIvaBP) context.getBusinessProcess();
	
	if (bp.isLiquidato())
		throw new it.cnr.jada.comp.ApplicationException("Premere su 'reset dati' prima di proseguire");
	
	Stampa_registri_ivaVBulk model = (Stampa_registri_ivaVBulk)bp.getModel();
	
	Liquidazione_provvisoria_ivaVBulk modelProvv = new Liquidazione_provvisoria_ivaVBulk();
	modelProvv.initializeForSearch(bp, context);
	modelProvv.setData_da(model.getData_da());
	modelProvv.setData_a(model.getData_a());
	modelProvv.setMese(model.getMese());
	modelProvv.setUser(model.getUser());
	modelProvv.setRistampa(false);
		   	
	return manageStampa(context, modelProvv);
}
public Forward doConsultaDettagliFatturaComSplit(ActionContext context) {
	try {
		fillModel(context);
		LiquidazioneDefinitivaIvaBP bp = (LiquidazioneDefinitivaIvaBP)context.getBusinessProcess();

		Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)bp.getModel();

		if (bulk==null) {
			bp.setMessage("Nessun dettaglio selezionato");
			return context.findDefaultForward();
		}
		if(bulk.getData_a()==null){
			bp.setMessage("Selezionare il mese");
			return context.findDefaultForward();	
		}
		java.util.Calendar cal = java.util.GregorianCalendar.getInstance();
		cal.setTime(new java.util.Date(bulk.getData_a().getTime()));
		Integer meseNum = new Integer(cal.get(java.util.Calendar.MONTH)+1);
		Integer esercizioNum = new Integer(cal.get(java.util.Calendar.YEAR));
		CompoundFindClause clause = new CompoundFindClause();
		clause.addClause("AND","esercizio",SQLBuilder.EQUALS,esercizioNum);
		clause.addClause("AND","cdCds",SQLBuilder.EQUALS,bulk.getCd_cds());
		clause.addClause("AND","cdUnitaOrganizzativa",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa()); 
		clause.addClause("AND","mese",SQLBuilder.EQUALS,meseNum);
		ConsultazioniBP ricercaLiberaBP = (ConsultazioniBP)context.createBusinessProcess("ConsFatturaGaeSplitBP");
		
		ricercaLiberaBP.addToBaseclause(clause);
		ricercaLiberaBP.openIterator(context);
		
		context.addHookForward("close",this,"doDefault");
		return context.addBusinessProcess(ricercaLiberaBP);
	}catch(Throwable ex){
		return handleException(context, ex);
	}
}

}