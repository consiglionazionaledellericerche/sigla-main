package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.57.12)
 * @author: Roberto Peli
 */
public abstract class Quadri_liq_annualeVBulk extends Liquidazione_iva_annualeVBulk {

	private BulkList elencoSecondario = new BulkList();

	private java.math.BigDecimal im_tot_iva = null;
	private java.math.BigDecimal im_totale = null;
public int addToElencoSecondario(Vp_liquid_iva_annualeBulk aLiqAnnuale) {	

	getElencoSecondario().add(aLiqAnnuale);
	return getElenco().size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.09.07)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getElencoSecondario() {
	return elencoSecondario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_tot_iva() {
	return im_tot_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_totale() {
	return im_totale;
}
public Vp_liquid_iva_annualeBulk removeFromElencoSecondario( int indiceDiLinea ) {

	return (Vp_liquid_iva_annualeBulk)getElencoSecondario().remove(indiceDiLinea);
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 13.09.07)
 * @param newElencoSecondario it.cnr.jada.bulk.BulkList
 */
public void setElencoSecondario(it.cnr.jada.bulk.BulkList newElencoSecondario) {
	elencoSecondario = newElencoSecondario;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @param newIm_tot_iva java.math.BigDecimal
 */
public void setIm_tot_iva(java.math.BigDecimal newIm_tot_iva) {
	im_tot_iva = newIm_tot_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 11.22.46)
 * @param newIm_totale java.math.BigDecimal
 */
public void setIm_totale(java.math.BigDecimal newIm_totale) {
	im_totale = newIm_totale;
}
}
