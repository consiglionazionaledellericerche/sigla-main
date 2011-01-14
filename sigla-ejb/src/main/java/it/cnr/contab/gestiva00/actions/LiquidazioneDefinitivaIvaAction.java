package it.cnr.contab.gestiva00.actions;

import it.cnr.contab.docamm00.bp.CRUDFatturaPassivaBP;
import it.cnr.contab.gestiva00.ejb.*;
import java.util.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.contab.gestiva00.bp.*;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.MTUWrapper;
import it.cnr.jada.util.action.BulkBP;

import java.sql.Timestamp;

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
		LiquidazioneIvaBP bp= (LiquidazioneIvaBP) context.getBusinessProcess();
		Liquidazione_definitiva_ivaVBulk bulk = (Liquidazione_definitiva_ivaVBulk)bp.getModel();
		String message=null;
		if (bulk.getTipoSezionaleFlag().equals(bulk.SEZIONALI_COMMERCIALI))
		{
			 if (bulk.getTipoImpegnoFlag().equals(bulk.IMPEGNI_RESIDUO))
			 {
				 message = "Si è scelto di generare impegni a Residuo."
						+ "Si desidera continuare?";
			 }
			 else
			 {
				 message = "Si è scelto di generare impegni a Competenza."
						+ "Si desidera continuare?";
			 }
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
protected it.cnr.jada.action.Forward setDataDaA(
	ActionContext context,
	Stampa_registri_ivaVBulk stampaBulk) {

    try {
	    int esercizio = stampaBulk.getEsercizio().intValue();
	    int meseIndex = ((Integer)stampaBulk.getMesi_int().get(stampaBulk.getMese())).intValue();
		java.util.GregorianCalendar gc = getGregorianCalendar();
		gc.set(java.util.Calendar.DAY_OF_MONTH, 1);
		gc.set(java.util.Calendar.YEAR, esercizio);

		gc.set(java.util.Calendar.MONTH, ((meseIndex > 0) ? meseIndex-1 : meseIndex));
		stampaBulk.setData_da(new Timestamp(gc.getTime().getTime()));
		if (meseIndex < 0)
			gc.set(java.util.Calendar.YEAR, esercizio);
		gc.set(java.util.Calendar.MONTH, ((meseIndex > 0) ? meseIndex : meseIndex + 1));
		gc.add(java.util.Calendar.DAY_OF_MONTH, -1);
        stampaBulk.setData_a(new Timestamp(gc.getTime().getTime()));

    } catch (Exception e) {
		return handleException(context, e);
    }
    return context.findDefaultForward();

}
}
