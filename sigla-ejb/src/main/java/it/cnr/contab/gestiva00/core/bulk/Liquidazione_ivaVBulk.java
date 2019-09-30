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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (10/16/2001 11:10:31 AM)
 * @author: Ardire Alfonso
 */
public abstract class Liquidazione_ivaVBulk 
	extends Stampa_registri_ivaVBulk
	implements IPrintable {

    public static java.util.Dictionary MESI;
    public static java.util.Dictionary MESI_INT;
    public static java.util.Dictionary INT_MESI;

    static {
	    initializeHashes();
    }
    
    private Liquidazione_ivaBulk liquidazione_iva=null;
	private java.math.BigDecimal totale_vendite;
	private java.math.BigDecimal totale_acquisti;	
	private java.math.BigDecimal variazioni_imposta_deb;
	private java.math.BigDecimal variazioni_imposta_cre;
	private java.math.BigDecimal imp_der_per_prec_cre;
	private java.math.BigDecimal imp_der_per_prec_deb;	
	private java.math.BigDecimal iva_dovuta_deb;
	private java.math.BigDecimal iva_dovuta_cre;	
	private java.math.BigDecimal imp_da_vers_deb;
	private java.math.BigDecimal imp_da_vers_cre;
	private java.math.BigDecimal variazioni_imposta_esterna_deb;
	private java.math.BigDecimal variazioni_imposta_esterna_cre;
	
	private java.math.BigDecimal id_report = null;
	private Integer pageNumber = null;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Liquidazione_ivaVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2002 11.31.22)
 * @author: Alfonso Ardire
 * @return it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk
 */
public void aggiornaTotali() {

	Liquidazione_ivaBulk liquidazione = getLiquidazione_iva();

	if (liquidazione==null) return;

	if (getLiquidazione_iva().getIva_deb_cred()!=null && getLiquidazione_iva().getIva_deb_cred().compareTo(new java.math.BigDecimal(0))<0)
		setIva_dovuta_deb(getLiquidazione_iva().getIva_deb_cred().abs());
	else
		setIva_dovuta_cre(getLiquidazione_iva().getIva_deb_cred());

	if (getLiquidazione_iva().getVar_imp_per_prec() != null && getLiquidazione_iva().getVar_imp_per_prec().compareTo(new java.math.BigDecimal(0))<0)
		setVariazioni_imposta_deb(getLiquidazione_iva().getVar_imp_per_prec().abs());
	else
		setVariazioni_imposta_cre(getLiquidazione_iva().getVar_imp_per_prec());
	
	if (getLiquidazione_iva().getIva_liq_esterna() != null && getLiquidazione_iva().getIva_liq_esterna().compareTo(new java.math.BigDecimal(0))<0)
		setVariazioni_imposta_esterna_deb(getLiquidazione_iva().getIva_liq_esterna().abs());
	else
		setVariazioni_imposta_esterna_cre(getLiquidazione_iva().getIva_liq_esterna());

	if (getLiquidazione_iva().getIva_da_versare() !=null && getLiquidazione_iva().getIva_da_versare().compareTo(new java.math.BigDecimal(0))<0)
		setImp_da_vers_deb(getLiquidazione_iva().getIva_da_versare().abs());
	else
		setImp_da_vers_cre(getLiquidazione_iva().getIva_da_versare());

	setTotale_vendite(getLiquidazione_iva().getIva_debito());
	setTotale_acquisti(getLiquidazione_iva().getIva_credito());
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImp_da_vers_cre() {
	return imp_da_vers_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImp_da_vers_deb() {
	return imp_da_vers_deb;
}
/**
 * Insert the method's description here.
 * Creation date: (22/07/2002 14.31.56)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImp_der_per_prec_cre() {
	return imp_der_per_prec_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (22/07/2002 14.31.56)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImp_der_per_prec_deb() {
	return imp_der_per_prec_deb;
}
public java.util.Dictionary getInt_mesi() {

	return INT_MESI;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIva_dovuta_cre() {
	return iva_dovuta_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIva_dovuta_deb() {
	return iva_dovuta_deb;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2002 11.31.22)
 * @author: Alfonso Ardire
 * @return it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk
 */
public Liquidazione_ivaBulk getLiquidazione_iva() {
	return liquidazione_iva;
}
public java.util.Dictionary getMesi() {

	return MESI;
}
public java.util.Dictionary getMesi_int() {

	return MESI_INT;
}
public java.lang.Integer getPageNumber() {
	return pageNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public java.lang.String getReportName() {
	return "/gestiva/gestiva/liquidazione_iva.jasper";
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public abstract java.util.Vector getReportParameters();
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_documento_stampato() {
	return "*";
}
/**
 * Insert the method's description here.
 * Creation date: (12/10/2002 2:31:34 PM)
 * @author: Alfonso Ardire
 * @param newTipo_report java.lang.String
 */
public java.lang.String getTipo_report_stampato() {
	return "LIQUIDAZIONE_IVA";
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.30.48)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotale_acquisti() {
	return totale_acquisti;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.30.48)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getTotale_vendite() {
	return totale_vendite;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazioni_imposta_cre() {
	return variazioni_imposta_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getVariazioni_imposta_deb() {
	return variazioni_imposta_deb;
}
protected static void initializeHashes() {

	MESI = new OrderedHashtable();
	MESI.put(DICEMBRE, DICEMBRE);
	MESI.put(GENNAIO, GENNAIO);
	MESI.put(FEBBRAIO, FEBBRAIO);
	MESI.put(MARZO, MARZO);
	MESI.put(APRILE, APRILE);
	MESI.put(MAGGIO, MAGGIO);
	MESI.put(GIUGNO, GIUGNO);
	MESI.put(LUGLIO, LUGLIO);
	MESI.put(AGOSTO, AGOSTO);
	MESI.put(SETTEMBRE, SETTEMBRE);
	MESI.put(OTTOBRE, OTTOBRE);
	MESI.put(NOVEMBRE, NOVEMBRE);
	

	MESI_INT = new OrderedHashtable();
	MESI_INT.put(DICEMBRE, new Integer(-1));
	MESI_INT.put(GENNAIO, new Integer(1));
	MESI_INT.put(FEBBRAIO, new Integer(2));
	MESI_INT.put(MARZO, new Integer(3));
	MESI_INT.put(APRILE, new Integer(4));
	MESI_INT.put(MAGGIO, new Integer(5));
	MESI_INT.put(GIUGNO, new Integer(6));
	MESI_INT.put(LUGLIO, new Integer(7));
	MESI_INT.put(AGOSTO, new Integer(8));
	MESI_INT.put(SETTEMBRE, new Integer(9));
	MESI_INT.put(OTTOBRE, new Integer(10));
	MESI_INT.put(NOVEMBRE, new Integer(11));
	
	
	INT_MESI = new OrderedHashtable();
	INT_MESI.put(new Integer(-1), DICEMBRE);
	INT_MESI.put(new Integer(1), GENNAIO);
	INT_MESI.put(new Integer(2), FEBBRAIO);
	INT_MESI.put(new Integer(3), MARZO);
	INT_MESI.put(new Integer(4), APRILE);
	INT_MESI.put(new Integer(5), MAGGIO);
	INT_MESI.put(new Integer(6), GIUGNO);
	INT_MESI.put(new Integer(7), LUGLIO);
	INT_MESI.put(new Integer(8), AGOSTO);
	INT_MESI.put(new Integer(9), SETTEMBRE);
	INT_MESI.put(new Integer(10), OTTOBRE);
	INT_MESI.put(new Integer(11), NOVEMBRE);
	
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public boolean isDBManagementRequired() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public boolean isRistampabile() {
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2002 11.31.22)
 * @author: Alfonso Ardire
 * @return it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk
 */
public void resetLiquidazioneIva() {
    setLiquidazione_iva(new Liquidazione_ivaBulk());
    java.math.BigDecimal zero= new java.math.BigDecimal(0);

    setTotale_vendite(zero);
    getLiquidazione_iva().setIva_vendite(zero);
    getLiquidazione_iva().setIva_vendite_diff(zero);
    getLiquidazione_iva().setIva_vend_diff_esig(zero);
    getLiquidazione_iva().setIva_autofatt(zero);
    getLiquidazione_iva().setIva_intraue(zero);

    setTotale_acquisti(zero);
	getLiquidazione_iva().setIva_acquisti(zero);
	getLiquidazione_iva().setIva_acq_non_detr(zero);
	getLiquidazione_iva().setIva_acquisti_diff(zero);
	getLiquidazione_iva().setIva_acq_diff_esig(zero);
	
	getLiquidazione_iva().setVar_imp_per_prec(zero);
	getLiquidazione_iva().setIva_non_vers_per_prec(zero);
    getLiquidazione_iva().setIva_deb_cred_per_prec(zero);
    getLiquidazione_iva().setInt_deb_liq_trim(zero);
    getLiquidazione_iva().setCred_iva_spec_detr(zero);
    getLiquidazione_iva().setAcconto_iva_vers(zero);
    getLiquidazione_iva().setDt_versamento(null);
    getLiquidazione_iva().setIva_versata(zero);
    getLiquidazione_iva().setCred_iva_infrann_comp(zero);
    getLiquidazione_iva().setCred_iva_infrann_rimb(zero);    
   
    getLiquidazione_iva().setAbi(null);
    getLiquidazione_iva().setCab(null);
    getLiquidazione_iva().setAnnotazioni(null);

    setVariazioni_imposta_cre(zero);
	setVariazioni_imposta_deb(zero);
	setImp_da_vers_cre(zero);
	setImp_da_vers_deb(zero);
	setImp_der_per_prec_cre(zero);
	setImp_der_per_prec_deb(zero);
	setIva_dovuta_cre(zero);
	setIva_dovuta_deb(zero);
	setVariazioni_imposta_esterna_cre(zero);
	setVariazioni_imposta_esterna_deb(zero);
		
    getLiquidazione_iva().setStato(PROVVISORIO);
    getLiquidazione_iva().setCd_cds(getCd_cds());
    getLiquidazione_iva().setCd_unita_organizzativa(getCd_unita_organizzativa());
    getLiquidazione_iva().setDt_inizio(getData_da());
    getLiquidazione_iva().setDt_fine(getData_a());
    getLiquidazione_iva().setEsercizio(getEsercizio());
}
/**
 * Insert the method's description here.
 * Creation date: (12/6/2002 4:17:20 PM)
 */
public void setId_report(java.math.BigDecimal new_id_report) {

	id_report = new_id_report;	
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newImp_da_vers_cre java.math.BigDecimal
 */
public void setImp_da_vers_cre(java.math.BigDecimal newImp_da_vers_cre) {
	imp_da_vers_cre = newImp_da_vers_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newImp_da_vers_deb java.math.BigDecimal
 */
public void setImp_da_vers_deb(java.math.BigDecimal newImp_da_vers_deb) {
	imp_da_vers_deb = newImp_da_vers_deb;
}
/**
 * Insert the method's description here.
 * Creation date: (22/07/2002 14.31.56)
 * @author: Alfonso Ardire
 * @param newImp_der_per_prec_cre java.math.BigDecimal
 */
public void setImp_der_per_prec_cre(java.math.BigDecimal newImp_der_per_prec_cre) {
	imp_der_per_prec_cre = newImp_der_per_prec_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (22/07/2002 14.31.56)
 * @author: Alfonso Ardire
 * @param newImp_der_per_prec_deb java.math.BigDecimal
 */
public void setImp_der_per_prec_deb(java.math.BigDecimal newImp_der_per_prec_deb) {
	imp_der_per_prec_deb = newImp_der_per_prec_deb;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newIva_dovuta_cre java.math.BigDecimal
 */
public void setIva_dovuta_cre(java.math.BigDecimal newIva_dovuta_cre) {
	iva_dovuta_cre = newIva_dovuta_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newIva_dovuta_deb java.math.BigDecimal
 */
public void setIva_dovuta_deb(java.math.BigDecimal newIva_dovuta_deb) {
	iva_dovuta_deb = newIva_dovuta_deb;
}
/**
 * Insert the method's description here.
 * Creation date: (04/07/2002 11.31.22)
 * @author: Alfonso Ardire
 * @param newLiquidazione_iva it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk
 */
public void setLiquidazione_iva(Liquidazione_ivaBulk newLiquidazione_iva) {
	liquidazione_iva = newLiquidazione_iva;
}
public void setPageNumber(java.lang.Integer newPageNumber) {

	pageNumber = newPageNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.30.48)
 * @author: Alfonso Ardire
 * @param newTotale_acquisti java.math.BigDecimal
 */
public void setTotale_acquisti(java.math.BigDecimal newTotale_acquisti) {
	totale_acquisti = newTotale_acquisti;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.30.48)
 * @author: Alfonso Ardire
 * @param newTotale_vendite java.math.BigDecimal
 */
public void setTotale_vendite(java.math.BigDecimal newTotale_vendite) {
	totale_vendite = newTotale_vendite;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newVariazioni_imposta_cre java.math.BigDecimal
 */
public void setVariazioni_imposta_cre(java.math.BigDecimal newVariazioni_imposta_cre) {
	variazioni_imposta_cre = newVariazioni_imposta_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.46.14)
 * @author: Alfonso Ardire
 * @param newVariazioni_imposta_deb java.math.BigDecimal
 */
public void setVariazioni_imposta_deb(java.math.BigDecimal newVariazioni_imposta_deb) {
	variazioni_imposta_deb = newVariazioni_imposta_deb;
}
public java.math.BigDecimal getVariazioni_imposta_esterna_deb() {
	return variazioni_imposta_esterna_deb;
}
public void setVariazioni_imposta_esterna_deb(
		java.math.BigDecimal variazioni_imposta_esterna_deb) {
	this.variazioni_imposta_esterna_deb = variazioni_imposta_esterna_deb;
}
public java.math.BigDecimal getVariazioni_imposta_esterna_cre() {
	return variazioni_imposta_esterna_cre;
}
public void setVariazioni_imposta_esterna_cre(
		java.math.BigDecimal variazioni_imposta_esterna_cre) {
	this.variazioni_imposta_esterna_cre = variazioni_imposta_esterna_cre;
}
/**
 * Insert the method's description here.
 * Creation date: (08/07/2002 14.30.48)
 * @author: Alfonso Ardire
 */
public void validate() throws ValidationException {

    java.math.BigDecimal zero= new java.math.BigDecimal(0);
    //if ((getImp_da_vers_cre()!=null || getImp_da_vers_cre().compareTo(zero)) &&
    //(getImp_da_vers_deb()!=null || getImp_da_vers_deb().compareTo(zero)))
    //if ((getIva_dovuta_cre() != null && getIva_dovuta_cre().compareTo(zero)!=0) 
	    //|| (getIva_dovuta_deb() != null && getIva_dovuta_deb().compareTo(zero)!=0))
        //throw new it.cnr.jada.bulk.ValidationException("Impostare un solo valore (debito/credito) per il campo 'Iva dovuto o a credito nel periodo'");
    if ((getVariazioni_imposta_cre() != null && getVariazioni_imposta_cre().compareTo(zero)!=0) 
	    && (getVariazioni_imposta_deb() != null && getVariazioni_imposta_deb().compareTo(zero)!=0))
        throw new ValidationException("Impostare un solo valore (debito/credito) per il campo 'Variazioni comprensive degli interessi...'");

    if ((getVariazioni_imposta_cre() != null && getVariazioni_imposta_cre().compareTo(zero)!=0))
		getLiquidazione_iva().setVar_imp_per_prec(getVariazioni_imposta_cre());
	else 
	if ((getVariazioni_imposta_deb() != null && getVariazioni_imposta_deb().compareTo(zero)!=0))
		getLiquidazione_iva().setVar_imp_per_prec(getVariazioni_imposta_deb().negate());
      
    if ((getVariazioni_imposta_esterna_cre() != null && getVariazioni_imposta_esterna_cre().compareTo(zero)!=0) 
    	  && (getVariazioni_imposta_esterna_deb() != null && getVariazioni_imposta_esterna_deb().compareTo(zero)!=0))
            throw new ValidationException("Impostare un solo valore (debito/credito) per il campo 'Variazioni risultante da liquidazioni esterne.'");

    if ((getVariazioni_imposta_esterna_cre() != null && getVariazioni_imposta_esterna_cre().compareTo(zero)!=0))
		getLiquidazione_iva().setIva_liq_esterna(getVariazioni_imposta_esterna_cre());
	else 
		if ((getVariazioni_imposta_esterna_deb() != null && getVariazioni_imposta_esterna_deb().compareTo(zero)!=0))
			getLiquidazione_iva().setIva_liq_esterna(getVariazioni_imposta_esterna_deb().negate());
    super.validate();

    if (getLiquidazione_iva() != null)
    	getLiquidazione_iva().validate();
}
}
