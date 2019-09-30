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

package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.contab.config00.pdcfin.bulk.*;

public class Filtro_ricerca_speseVBulk extends it.cnr.jada.bulk.OggettoBulk {

	private Fondo_economaleBulk fondo_economale;

	// PG_FONDO_SPESA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_fondo_spesa;

	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// DENOMINAZIONE_FORNITORE VARCHAR(200)
	private java.lang.String denominazione_fornitore;

	// DT_SPESA TIMESTAMP
	private java.sql.Timestamp dt_spesa;

	// FL_DOCUMENTATA CHAR(1)
	private java.lang.Boolean fl_documentata;

	// FL_REINTEGRATA CHAR(1)
	private java.lang.Boolean fl_reintegrata;

	// IM_AMMONTARE_SPESA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_ammontare_spesa;

	// PARTITA_IVA VARCHAR(20)
	private java.lang.String partita_iva;

	private boolean multiSelection = false;
/**
 * Filtro_ricerca_obbligazioniVBulk constructor comment.
 */
public Filtro_ricerca_speseVBulk() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.lang.String
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
public java.sql.Timestamp getCurrentDate() {

	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	} catch (javax.ejb.EJBException e) {
		throw new it.cnr.jada.DetailedRuntimeException(e);
	}	
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.lang.String
 */
public java.lang.String getDenominazione_fornitore() {
	return denominazione_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDt_spesa() {
	return dt_spesa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_documentata() {
	return fl_documentata;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.lang.Boolean
 */
public java.lang.Boolean getFl_reintegrata() {
	return fl_reintegrata;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public Fondo_economaleBulk getFondo_economale() {
	return fondo_economale;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getIm_ammontare_spesa() {
	return im_ammontare_spesa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @return java.lang.String
 */
public java.lang.String getPartita_iva() {
	return partita_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.51)
 * @return java.lang.Long
 */
public java.lang.Long getPg_fondo_spesa() {
	return pg_fondo_spesa;
}
public Filtro_ricerca_speseVBulk initializeForSearch(
									it.cnr.jada.util.action.BulkBP bp,
									it.cnr.jada.action.ActionContext context) {

//	setDt_spesa(getCurrentDate());
//	setIm_ammontare_spesa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	setFl_documentata(Boolean.FALSE);
	setFl_reintegrata(Boolean.FALSE);
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (5/20/2002 4:36:49 PM)
 * @return boolean
 */
public boolean isMultiSelection() {
	return multiSelection;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newCodice_fiscale java.lang.String
 */
public void setCodice_fiscale(java.lang.String newCodice_fiscale) {
	codice_fiscale = newCodice_fiscale;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newDenominazione_fornitore java.lang.String
 */
public void setDenominazione_fornitore(java.lang.String newDenominazione_fornitore) {
	denominazione_fornitore = newDenominazione_fornitore;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newDt_spesa java.sql.Timestamp
 */
public void setDt_spesa(java.sql.Timestamp newDt_spesa) {
	dt_spesa = newDt_spesa;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newFl_documentata java.lang.Boolean
 */
public void setFl_documentata(java.lang.Boolean newFl_documentata) {
	fl_documentata = newFl_documentata;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newFl_reintegrata java.lang.Boolean
 */
public void setFl_reintegrata(java.lang.Boolean newFl_reintegrata) {
	fl_reintegrata = newFl_reintegrata;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newFondo_economale it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setFondo_economale(Fondo_economaleBulk newFondo_economale) {
	fondo_economale = newFondo_economale;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newIm_ammontare_spesa java.math.BigDecimal
 */
public void setIm_ammontare_spesa(java.math.BigDecimal newIm_ammontare_spesa) {
	im_ammontare_spesa = newIm_ammontare_spesa;
}
/**
 * Insert the method's description here.
 * Creation date: (5/20/2002 4:36:49 PM)
 * @param newMultiSelection boolean
 */
public void setMultiSelection(boolean newMultiSelection) {
	multiSelection = newMultiSelection;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.50)
 * @param newPartita_iva java.lang.String
 */
public void setPartita_iva(java.lang.String newPartita_iva) {
	partita_iva = newPartita_iva;
}
/**
 * Insert the method's description here.
 * Creation date: (20/02/2002 11.03.51)
 * @param newPg_fondo_spesa java.lang.Long
 */
public void setPg_fondo_spesa(java.lang.Long newPg_fondo_spesa) {
	pg_fondo_spesa = newPg_fondo_spesa;
}
}
