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

import java.util.Enumeration;

import it.cnr.contab.compensi00.bp.StampaCertificazioneBP;
import it.cnr.contab.compensi00.docs.bulk.StampaCertificazioneVBulk;
import it.cnr.contab.missioni00.bp.CRUDMissioneBP;
import it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.reports.bp.ReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.PrintFieldProperty;
import it.cnr.jada.bulk.ValidationException;

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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
	} else if (stampa.getTi_cert().equals(StampaCertificazioneVBulk.TI_ACCONTO_PPT)){
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
		bp.setStampaTit_imposta_pc(false);
		stampa.setStampaTit_imposta_pc(false);
		bp.setStampaRit_acconto_ppt(true);
		stampa.setStampaRit_acconto_ppt(true);		
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
		bp.setStampaRit_acconto_ppt(false);
		stampa.setStampaRit_acconto_ppt(false);
	}
	return context.findDefaultForward();
}
public Forward doPrintComunicazione(ActionContext context) {
	ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
	StampaCertificazioneBP scbp = (StampaCertificazioneBP)context.getBusinessProcess();	
	try {
		fillModel(context);
		bp.completeSearchTools(context,bp.getController());
		bp.validate(context);
		bp.print(context,bp.getModel());
		OfflineReportPrintBP printbp = (OfflineReportPrintBP)context.createBusinessProcess("OfflineReportPrintBP");
		printbp.setReportName(scbp.getReportNameComunicazione());
		for (Enumeration e = bp.getBulkInfo().getPrintFieldProperties(scbp.getReportNameComunicazione());e.hasMoreElements();) {
			PrintFieldProperty printFieldProperty = (PrintFieldProperty)e.nextElement();
			Object value = printFieldProperty.getValueFrom(bp.getModel());
			Print_spooler_paramBulk param = new Print_spooler_paramBulk();
			if (scbp.getReportNameComunicazione().endsWith("jasper")){
				param.setNomeParam(printFieldProperty.getParamNameJR());
				param.setParamType(printFieldProperty.getParamTypeJR());
			}else{
				param.setNomeParam("prompt"+printFieldProperty.getParameterPosition());
			}
			switch(printFieldProperty.getParamType()) {
				case PrintFieldProperty.TYPE_DATE:
					param.setValoreParam(ReportPrintBP.DATE_FORMAT.format((java.sql.Timestamp)value));
					break;
				case PrintFieldProperty.TYPE_TIMESTAMP:
					param.setValoreParam(ReportPrintBP.TIMESTAMP_FORMAT.format((java.sql.Timestamp)value));
					break;
				case PrintFieldProperty.TYPE_STRING:
				default:
					if (value == null)
						param.setValoreParam("");
					else
						param.setValoreParam(value.toString());
					break;
			}								
			printbp.addToPrintSpoolerParam(param);
		}
		bp.setDirty(false);
		return context.addBusinessProcess(printbp);
	} catch(ValidationException e) {
		bp.setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
