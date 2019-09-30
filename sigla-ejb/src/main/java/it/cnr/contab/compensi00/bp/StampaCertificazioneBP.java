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

package it.cnr.contab.compensi00.bp;

import it.cnr.contab.compensi00.docs.bulk.StampaCertificazioneVBulk;
/**
 * Insert the type's description here.
 * Creation date: (27/01/2004 11.27.08)
 * @author: Gennaro Borriello
 */
public class StampaCertificazioneBP extends it.cnr.contab.reports.bp.ParametricPrintBP {

	private boolean stampaRit_prev;
	private boolean stampaRit_acconto;
	private boolean stampaTit_imposta;
	private boolean stampaTit_imposta_cc;	
	private boolean stampaRit_contrib;
	private boolean stampaTit_imposta_pc;
	private boolean stampaRit_acconto_ppt;
	
	private boolean editingTi_cert;
	
	private String reportNameComunicazione;

/**
 * StampaCertificazioneBP constructor comment.
 */
public StampaCertificazioneBP() {
	super();
}
/**
 * StampaCertificazioneBP constructor comment.
 * @param function java.lang.String
 */
public StampaCertificazioneBP(String function) {
	super(function);
}
public String getJSPTitle(){

	if (isStampaRit_prev())
		return "Stampa Certificazione a Ritenuta Previdenziale";
	else if (isStampaRit_acconto())
		return "Stampa Certificazione a Ritenuta d'Acconto";
	else if (isStampaTit_imposta())
		return "Stampa Certificazione a Titolo d'Imposta";
	else if (isStampaTit_imposta_cc())
		return "Stampa Certificazione a Titolo d'Imposta - Co.Co.Co.";
	else if (isStampaRit_contrib())
		return "Stampa Certificazione per Contributi corrisposti ad imprese";
	else if (isStampaTit_imposta_pc())
		return "Stampa Certificazione a Titolo d'Imposta - Premi per concorsi";
	else if (isStampaRit_acconto_ppt())
		return "Stampa Certificazione a Ritenuta d'Acconto su somme liquidate a seguito di pignoramenti presso terzi";
	return "Stampa Certificazione";
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */
public it.cnr.jada.util.jsp.Button[] createToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[3];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.printComunicazione");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.close");
	return toolbar;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	
	super.init(config,context);
		
	String type = config.getInitParameter("TiCertif");
	StampaCertificazioneVBulk stampa = (StampaCertificazioneVBulk)getModel();
	
	if (type != null && type.equals(stampa.TI_PREVIDENZIALE)){		
		setStampaRit_prev(true);
		stampa.setStampaRit_prev(true);
		stampa.setTi_cert(stampa.TI_PREVIDENZIALE);
		setEditingTi_cert(false);
	} else if (type != null && type.equals(stampa.TI_ACCONTO)){
		setStampaRit_acconto(true);
		stampa.setStampaRit_acconto(true);
		stampa.setTi_cert(stampa.TI_ACCONTO);
		setEditingTi_cert(false);
	} else if (type != null && type.equals(stampa.TI_IMPOSTA)){
		setStampaTit_imposta(true);
		stampa.setStampaTit_imposta(true);
		stampa.setTi_cert(stampa.TI_IMPOSTA);
		setEditingTi_cert(false);
	} else if (type != null && type.equals(stampa.TI_IMPOSTA_CC)){
		setStampaTit_imposta_cc(true);
		stampa.setStampaTit_imposta_cc(true);
		stampa.setTi_cert(stampa.TI_IMPOSTA_CC);
		setEditingTi_cert(false);
	} else if (type != null && type.equals(stampa.TI_IMPOSTA_PC)){
		setStampaTit_imposta_pc(true);
		stampa.setStampaTit_imposta_pc(true);
		stampa.setTi_cert(stampa.TI_IMPOSTA_PC);
		setEditingTi_cert(false);	
	} else if (type != null && type.equals(stampa.TI_ACCONTO_PPT)){
		setStampaRit_acconto_ppt(true);
		stampa.setStampaRit_acconto_ppt(true);
		stampa.setTi_cert(stampa.TI_ACCONTO_PPT);
		setEditingTi_cert(false);		
	} else {
		setStampaRit_acconto(true);
		stampa.setStampaRit_acconto(true);
		stampa.setTi_cert(stampa.TI_ACCONTO);
		setEditingTi_cert(true);
	}
	setModel(context,stampa);
	setReportNameComunicazione(new String("/docamm/docamm/certificazione_comunicaz_pignorato.jasper"));
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @return boolean
 */
public boolean isStampaRit_acconto() {
	return stampaRit_acconto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @return boolean
 */
public boolean isStampaRit_prev() {
	return stampaRit_prev;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @return boolean
 */
public boolean isStampaTit_imposta() {
	return stampaTit_imposta;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @return boolean
 */
public boolean isStampaRit_contrib() {
	return stampaRit_contrib;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @param newStampaRit_acconto boolean
 */
public void setStampaRit_acconto(boolean newStampaRit_acconto) {
	stampaRit_acconto = newStampaRit_acconto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @param newStampaRit_prev boolean
 */
public void setStampaRit_prev(boolean newStampaRit_prev) {
	stampaRit_prev = newStampaRit_prev;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.41.05)
 * @param newStampaTit_imposta boolean
 */
public void setStampaTit_imposta(boolean newStampaTit_imposta) {
	stampaTit_imposta = newStampaTit_imposta;
}
	/**
	 * @return
	 */
	public boolean isStampaTit_imposta_cc() {
		return stampaTit_imposta_cc;
	}

	/**
	 * @param b
	 */
	public void setStampaTit_imposta_cc(boolean b) {
		stampaTit_imposta_cc = b;
	}
	/**
	 * @param b
	 */
	public void setStampaRit_contrib(boolean b) {
		stampaRit_contrib = b;
	}
	public boolean isEditingTi_cert() {
		return editingTi_cert;
	}
	/**
	 * @param editingTi_cert The editingTi_cert to set.
	 */
	public void setEditingTi_cert(boolean editingTi_cert) {
		this.editingTi_cert = editingTi_cert;
	}
	/**
	 * @return
	 */
	public boolean isStampaTit_imposta_pc() {
		return stampaTit_imposta_pc;
	}

	/**
	 * @param b
	 */
	public void setStampaTit_imposta_pc(boolean b) {
		stampaTit_imposta_pc = b;
	}
	
	public boolean isStampaRit_acconto_ppt() {
		return stampaRit_acconto_ppt;
	}
	public void setStampaRit_acconto_ppt(boolean stampaRit_acconto_ppt) {
		this.stampaRit_acconto_ppt = stampaRit_acconto_ppt;
	}
	public String getReportNameComunicazione() {
		return reportNameComunicazione;
	}
	public void setReportNameComunicazione(String reportNameComunicazione) {
		this.reportNameComunicazione = reportNameComunicazione;
	}
	public boolean isPrintComButtonHidden() {
		if(isStampaRit_acconto_ppt())
			return false;
		else
			return true;
	}
}
