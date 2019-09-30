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

package it.cnr.contab.config00.bp;

import it.cnr.contab.config00.ejb.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.reports.bp.*;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 11.35.30)
 * @author: Simonetta Costa
 */
public class StampaPdcFinSpeseBP extends it.cnr.jada.util.action.BulkBP {
/**
 * StampaPdcFinSpeseBP constructor comment.
 */
public StampaPdcFinSpeseBP() {
	super();
}
/**
 * StampaPdcFinSpeseBP constructor comment.
 * @param function java.lang.String
 */
public StampaPdcFinSpeseBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (03/04/2002 18.04.30)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
	return toolbar;
}
/**
 * Effettua una operazione di ricerca per un attributo di un modello.
 * @param actionContext contesto dell'azione in corso
 * @param clauses Albero di clausole da utilizzare per la ricerca
 * @param bulk prototipo del modello di cui si effettua la ricerca
 * @param context modello che fa da contesto alla ricerca (il modello del FormController padre del
 * 			controller che ha scatenato la ricerca)
 * @return un RemoteIterator sul risultato della ricerca o null se la ricerca non ha ottenuto nessun risultato
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
protected void init(Config config, ActionContext context) throws BusinessProcessException {

	try{
		super.init(config, context);
		V_stampa_pdc_fin_speseBulk obj = new V_stampa_pdc_fin_speseBulk();
		obj.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));

		PDCFinComponentSession sess = (PDCFinComponentSession)createComponentSession("CNRCONFIG00_EJB_PDCFinComponentSession", PDCFinComponentSession.class);
		obj.setTipologieCdsKeys(sess.loadTipologieCdsKeys(context.getUserContext()));

		setModel(context, obj);
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
protected void initializePrintBP(AbstractPrintBP bp) {

	OfflineReportPrintBP printbp = (OfflineReportPrintBP)bp;
	printbp.setReportName("/cnrconfigurazione/pdc/piano_dei_conti_finanziario_spese_cds_esercizio.jasper");
	V_stampa_pdc_fin_speseBulk obj = (V_stampa_pdc_fin_speseBulk)getModel();
	//printbp.setReportParameter(0, obj.getEsercizio().toString());
	//printbp.setReportParameter(1, obj.getTipoCds().toUpperCase());
	Print_spooler_paramBulk param;
	param = new Print_spooler_paramBulk();
	param.setNomeParam("esercizio");
	param.setValoreParam(obj.getEsercizio().toString());
	param.setParamType("java.lang.Integer");
	printbp.addToPrintSpoolerParam(param);
	
	param = new Print_spooler_paramBulk();
	param.setNomeParam("tipo_cds");
	param.setValoreParam(obj.getTipoCds().toUpperCase());
	param.setParamType("java.lang.String");
	printbp.addToPrintSpoolerParam(param);

}
public boolean isPrintButtonEnabled() {
	return true;
}
public boolean isPrintButtonHidden() {
	return false;
}
}
