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

import java.util.Enumeration;

import it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk;
import it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP;
import it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRperCdsBP;
import it.cnr.contab.reports.action.ParametricPrintAction;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.contab.reports.bp.ReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.PrintFieldProperty;
import it.cnr.jada.bulk.ValidationException;
/**
 * Insert the type's description here.
 * Creation date: (23/03/2004 15.54.51)
 * @author: Gennaro Borriello
 */
public class StampaRendFinanziarioCNRAction extends ParametricPrintAction {
/**
 * StampaRendFinanziarioCNRAction constructor comment.
 */
public StampaRendFinanziarioCNRAction() {
	super();
}
/**
 * Stampa Rendiconto Finanziario CNR per CDS - Entrate Articolo
 */
public Forward doStampaEntrateCDSPerArticolo(ActionContext context) {
	try {
		it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
		StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
		validaStampaConCds(stampa);
		stampa.setStampaArticolo(Boolean.TRUE);
		bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_entrate_cnr_dz.jasper");
		return doPrint(context);
	} catch (it.cnr.jada.bulk.ValidationException ve){
		handleException(context, ve);
	}

	return context.findDefaultForward();
}
/**
 * Stampa Rendiconto Finanziario CNR per CDS - Entrate Capitolo
 */
public Forward doStampaEntrateCDSPerCapitolo(ActionContext context) {
	try {
		it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
		StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
		validaStampaConCds(stampa);
		stampa.setStampaArticolo(Boolean.FALSE);
		bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_entrate_cnr_dz.jasper");
		return doPrint(context);
	} catch (it.cnr.jada.bulk.ValidationException ve){
		handleException(context, ve);
	}

	return context.findDefaultForward();
}
/**
 * Stampa Rendiconto Finanziario CNR - Entrate Articolo
 */
public Forward doStampaEntratePerArticolo(ActionContext context) {
	it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
	StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
	stampa.setStampaArticolo(Boolean.TRUE);
	bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_entrate_cnr_dz.jasper");
	return doPrint(context);
}
/**
 * Stampa Rendiconto Finanziario CNR - Entrate Capitolo
 */
public Forward doStampaEntratePerCapitolo(ActionContext context) {
	it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
	StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
	stampa.setStampaArticolo(Boolean.FALSE);
	bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_entrate_cnr_dz.jasper");
	return doPrint(context);
}
/**
 * Stampa Rendiconto Finanziario CNR selezionando un CDS - Spese Articolo
 */
public Forward doStampaSpeseCDSPerArticolo(ActionContext context) {
	try {
		it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
		StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
		validaStampaConCds(stampa);
		stampa.setStampaArticolo(Boolean.TRUE);
		bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_spese_cnr_dz.jasper");
		return doPrint(context);
	} catch (it.cnr.jada.bulk.ValidationException ve){
		handleException(context, ve);
	}

	return context.findDefaultForward();
}
/**
 * Stampa Rendiconto Finanziario CNR selezionando un CDS - Spese Capitolo
 */
public Forward doStampaSpeseCDSPerCapitolo(ActionContext context) {
	try {
		it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
		StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
		validaStampaConCds(stampa);
		stampa.setStampaArticolo(Boolean.FALSE);
		bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_spese_cnr_dz.jasper");
		return doPrint(context);
	} catch (it.cnr.jada.bulk.ValidationException ve){
		handleException(context, ve);
	}

	return context.findDefaultForward();
}
/**
 * Stampa Rendiconto Finanziario CNR - Spese Articolo
 */
public Forward doStampaSpesePerArticolo(ActionContext context) {
	it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
	StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
	stampa.setStampaArticolo(Boolean.TRUE);
	bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_spese_cnr_dz.jasper");
	return doPrint(context);
}
/**
 * Stampa Rendiconto Finanziario CNR - Spese Capitolo
 */
public Forward doStampaSpesePerCapitolo(ActionContext context) {
	it.cnr.contab.pdg00.bp.StampaRendFinanziarioCNRBP bp = (StampaRendFinanziarioCNRBP)context.getBusinessProcess();
	StampaRendFinCNRVBulk stampa = (StampaRendFinCNRVBulk)bp.getModel();
	stampa.setStampaArticolo(Boolean.FALSE);
	bp.setReportName("/cnrpreventivo/pdg/rendiconto_fin_spese_cnr_dz.jasper");
	return doPrint(context);
}
/**
 * Controlla, per le stampe che richiedono il cds, che l'utente abbia selezionato almeno un CdS
 */
private void validaStampaConCds(it.cnr.contab.pdg00.bulk.StampaRendFinCNRVBulk stampa) throws it.cnr.jada.bulk.ValidationException{
	if (stampa.getCds() == null || stampa.getCds().getCd_unita_organizzativa() == null)
		throw new it.cnr.jada.bulk.ValidationException("Attenzione: selezionare un CdS");
}
}
