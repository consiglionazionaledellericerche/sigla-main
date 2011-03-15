package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.bp.StampaCertificazioneBP;
import it.cnr.contab.compensi00.docs.bulk.StampaCertificazioneVBulk;
import it.cnr.contab.missioni00.bp.CRUDMissioneBP;
import it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk;
import it.cnr.jada.bulk.FillException;

/**
 * Insert the type's description here.
 * Creation date: (01/03/2006 9.50.38)
 * @author: Tilde D'urso
 */
public class StampaCertificazioneAction extends it.cnr.contab.reports.action.ParametricPrintAction {

public StampaCertificazioneAction() {
	super();
}
public it.cnr.jada.action.Forward doChangeTi_cert(it.cnr.jada.action.ActionContext context) {

	StampaCertificazioneBP bp = (StampaCertificazioneBP)context.getBusinessProcess();			
	StampaCertificazioneVBulk stampa = (StampaCertificazioneVBulk) bp.getController().getModel();
	try {
		fillModel(context);
	} catch (FillException e) {
		return handleException(context, e);
	}
	if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_PREVIDENZIALE)){		
		bp.setStampaRit_prev(true);
		stampa.setStampaRit_prev(true);
		bp.setStampaRit_acconto(false);
		stampa.setStampaRit_acconto(false);
		bp.setStampaTit_imposta(false);
		stampa.setStampaTit_imposta(false);
		bp.setStampaTit_imposta_cc(false);
		stampa.setStampaTit_imposta_cc(false);
		bp.setStampaRit_contrib(false);
		stampa.setStampaRit_contrib(false);
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
	} else if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_ACCONTO)){
		bp.setStampaRit_prev(false);
		stampa.setStampaRit_prev(false);
		bp.setStampaRit_acconto(true);
		stampa.setStampaRit_acconto(true);
		bp.setStampaTit_imposta(false);
		stampa.setStampaTit_imposta(false);
		bp.setStampaTit_imposta_cc(false);
		stampa.setStampaTit_imposta_cc(false);
		bp.setStampaRit_contrib(false);
		stampa.setStampaRit_contrib(false);
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
	} else if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_IMPOSTA)){
		bp.setStampaRit_prev(false);
		stampa.setStampaRit_prev(false);
		bp.setStampaRit_acconto(false);
		stampa.setStampaRit_acconto(false);
		bp.setStampaTit_imposta(true);
		stampa.setStampaTit_imposta(true);
		bp.setStampaTit_imposta_cc(false);
		stampa.setStampaTit_imposta_cc(false);
		bp.setStampaRit_contrib(false);
		stampa.setStampaRit_contrib(false);
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
	} else if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_IMPOSTA_CC)){
		bp.setStampaRit_prev(false);
		stampa.setStampaRit_prev(false);
		bp.setStampaRit_acconto(false);
		stampa.setStampaRit_acconto(false);
		bp.setStampaTit_imposta(false);
		stampa.setStampaTit_imposta(false);
		bp.setStampaTit_imposta_cc(true);
		stampa.setStampaTit_imposta_cc(true);
		bp.setStampaRit_contrib(false);
		stampa.setStampaRit_contrib(false);
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
	} else if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_IMPOSTA_PC)){
		bp.setStampaRit_prev(false);
		stampa.setStampaRit_prev(false);
		bp.setStampaRit_acconto(false);
		stampa.setStampaRit_acconto(false);
		bp.setStampaTit_imposta(false);
		stampa.setStampaTit_imposta(false);
		bp.setStampaTit_imposta_cc(false);
		stampa.setStampaTit_imposta_cc(false);
		bp.setStampaRit_contrib(false);
		stampa.setStampaRit_contrib(false);	
		bp.setStampaTit_imposta_pc(true);
		stampa.setStampaTit_imposta_pc(true);
	} else {
		bp.setStampaRit_prev(false);
		stampa.setStampaRit_prev(false);
		bp.setStampaRit_acconto(false);
		stampa.setStampaRit_acconto(false);
		bp.setStampaTit_imposta(false);
		stampa.setStampaTit_imposta(false);
		bp.setStampaTit_imposta_cc(false);
		stampa.setStampaTit_imposta_cc(false);
		bp.setStampaRit_contrib(true);
		stampa.setStampaRit_contrib(true);
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
	}
	return context.findDefaultForward();
}
}
