package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Quadri_770Bulk;

/**
 * Creation date: (24/09/2004)
 * @author: Aurelio D'Amico
 * @version: 1.0
 */
public class Estrazione770Bulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ID_REPORT
	private java.math.BigDecimal id_report;
	
	// QUADRI_770
	private Quadri_770Bulk quadri_770;
	
/**
 * EstrazioneCUDVBulk constructor comment.
 */
public Estrazione770Bulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newId_report java.math.BigDecimal
 */
public void setId_report(java.math.BigDecimal newId_report) {
	id_report = newId_report;
}
public Quadri_770Bulk getQuadri_770() {
	return quadri_770;
}
public void setQuadri_770(Quadri_770Bulk quadri_770) {
	this.quadri_770 = quadri_770;
}
public boolean isROQuadri_770(){
	return quadri_770 == null || quadri_770.getCrudStatus() == NORMAL;
}
public boolean isFileOrdinario() {
	if(getQuadri_770() != null && getQuadri_770().getCd_quadro()!= null)
	{
		if(getQuadri_770().getTi_modello() != null && getQuadri_770().getTi_modello().compareTo("O")== 0)
			return Boolean.TRUE;
	}
	return Boolean.FALSE;
}
public boolean isFileSemplificato() {
	if(getQuadri_770() != null && getQuadri_770().getCd_quadro()!= null)
	{
		if(getQuadri_770().getTi_modello() != null && getQuadri_770().getTi_modello().compareTo("S")== 0)
			return Boolean.TRUE;
	}
	return Boolean.FALSE;
}
}
