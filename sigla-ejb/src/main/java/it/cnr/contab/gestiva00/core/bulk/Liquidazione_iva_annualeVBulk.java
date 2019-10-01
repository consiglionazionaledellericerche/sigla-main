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

import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (02/12/2003 12.34.35)
 * @author: Roberto Peli
 */
public abstract class Liquidazione_iva_annualeVBulk
	extends Stampa_registri_ivaVBulk
	implements IPrintable {

	public static final String TIPO_A_CLAUSE = "A";
	public static final String TIPO_B_CLAUSE = "B";
	public static final String TIPO_C_CLAUSE = "C";

	private java.math.BigDecimal importo = null;
	private BulkList elenco = new BulkList();

	private java.math.BigDecimal id_report = null;
	private Integer pageNumber = null;
	private boolean ristampabile = false;
public int addToElenco(Vp_liquid_iva_annualeBulk aLiqAnnuale) {	

	getElenco().add(aLiqAnnuale);
	return getElenco().size()-1;
}


/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.42.21)
 * @return BulkList
 */
public BulkList getElenco() {
	return elenco;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.40.12)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.39.11)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getImporto() {
	return importo;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.40.12)
 * @return java.lang.Integer
 */
public java.lang.Integer getPageNumber() {
	return pageNumber;
}
public java.lang.String getReportName() {

	return "/gestiva/gestiva/liquid_iva_ann_acquisti_vendite.jasper";
}
public java.lang.String getTipo_documento_stampato() {
	return null;
}
public java.lang.String getTipo_report_stampato() {
	return null;
}
public OggettoBulk initializeForSearch(
	it.cnr.jada.util.action.BulkBP bp,
	it.cnr.jada.action.ActionContext context) {

	Liquidazione_iva_annualeVBulk bulk = (Liquidazione_iva_annualeVBulk)super.initializeForSearch(bp, context);
	
	bulk.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	bulk.setPageNumber(new Integer(1));
	bulk.setRistampabile(false);
	
	return bulk;
}
public boolean isPageNumberRequired() {
	return false;
}
public boolean isRistampabile() {
	return ristampabile;
}
public Vp_liquid_iva_annualeBulk removeFromElenco( int indiceDiLinea ) {

	return (Vp_liquid_iva_annualeBulk)getElenco().remove(indiceDiLinea);
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.42.21)
 * @param newElenco BulkList
 */
public void setElenco(BulkList newElenco) {
	elenco = newElenco;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.40.12)
 * @param newId_report java.math.BigDecimal
 */
public void setId_report(java.math.BigDecimal newId_report) {
	id_report = newId_report;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.39.11)
 * @param newImporto java.math.BigDecimal
 */
public void setImporto(java.math.BigDecimal newImporto) {
	importo = newImporto;
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 12.40.12)
 * @param newPageNumber java.lang.Integer
 */
public void setPageNumber(java.lang.Integer newPageNumber) {
	pageNumber = newPageNumber;
}
/**
 * Insert the method's description here.
 * Creation date: (03/12/2003 12.11.41)
 * @param newRistampabile boolean
 */
public void setRistampabile(boolean newRistampabile) {
	ristampabile = newRistampabile;
}
/**
 * Insert the method's description here.
 * Creation date: (03/07/2002 14.44.23)
 * @author: Alfonso Ardire
 * @param newTipo_stampa java.lang.String
 */
public void validate() throws ValidationException {

	//NON fare il super.

	if (getPageNumber() == null)
		throw new ValidationException("Specificare il numero di pagina da cui iniziare la stampa!");
	if (getPageNumber().intValue() < 1)
		throw new ValidationException("Il numero di pagina da cui iniziare la stampa deve essere maggiore o uguale a 1!");
}
}
