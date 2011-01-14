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
	
	private boolean editingTi_cert;
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
	return "Stampa Certificazione";
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

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
	} else {
		setStampaRit_acconto(true);
		stampa.setStampaRit_acconto(true);
		stampa.setTi_cert(stampa.TI_ACCONTO);
		setEditingTi_cert(true);
	}
	setModel(context,stampa);
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
}
