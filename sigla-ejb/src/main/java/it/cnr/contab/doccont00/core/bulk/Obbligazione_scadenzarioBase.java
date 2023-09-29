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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Obbligazione_scadenzarioBase extends Obbligazione_scadenzarioKey implements Keyed {
	// DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;

	// DT_SCADENZA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_scadenza;

	// IM_ASSOCIATO_DOC_AMM DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato_doc_amm;

	// IM_ASSOCIATO_DOC_CONTABILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_associato_doc_contabile;

	// IM_SCADENZA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_scadenza;

	// FL_ASSOCIATA_ORDINE CHAR(1) NOT NULL
	private java.lang.Boolean flAssociataOrdine;

	private java.lang.Boolean flScollegaDocumenti;

public Obbligazione_scadenzarioBase() {
	super();
}
public Obbligazione_scadenzarioBase(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_originale,java.lang.Long pg_obbligazione,java.lang.Long pg_obbligazione_scadenzario) {
	super(cd_cds,esercizio,esercizio_originale,pg_obbligazione,pg_obbligazione_scadenzario);
}
/* 
 * Getter dell'attributo ds_scadenza
 */
public java.lang.String getDs_scadenza() {
	return ds_scadenza;
}
/* 
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo im_associato_doc_amm
 */
public java.math.BigDecimal getIm_associato_doc_amm() {
	return im_associato_doc_amm;
}
/* 
 * Getter dell'attributo im_associato_doc_contabile
 */
public java.math.BigDecimal getIm_associato_doc_contabile() {
	return im_associato_doc_contabile;
}
/* 
 * Getter dell'attributo im_scadenza
 */
public java.math.BigDecimal getIm_scadenza() {
	return im_scadenza;
}
/* 
 * Setter dell'attributo ds_scadenza
 */
public void setDs_scadenza(java.lang.String ds_scadenza) {
	this.ds_scadenza = ds_scadenza;
}
/* 
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo im_associato_doc_amm
 */
public void setIm_associato_doc_amm(java.math.BigDecimal im_associato_doc_amm) {
	this.im_associato_doc_amm = im_associato_doc_amm;
}
/* 
 * Setter dell'attributo im_associato_doc_contabile
 */
public void setIm_associato_doc_contabile(java.math.BigDecimal im_associato_doc_contabile) {
	this.im_associato_doc_contabile = im_associato_doc_contabile;
}
/* 
 * Setter dell'attributo im_scadenza
 */
public void setIm_scadenza(java.math.BigDecimal im_scadenza) {
	this.im_scadenza = im_scadenza;
}
public java.lang.Boolean getFlAssociataOrdine() {
	return flAssociataOrdine;
}
public void setFlAssociataOrdine(java.lang.Boolean flAssociataOrdine) {
	this.flAssociataOrdine = flAssociataOrdine;
}

	public Boolean getFlScollegaDocumenti() {
		return flScollegaDocumenti;
	}

	public void setFlScollegaDocumenti(Boolean flScollegaDocumenti) {
		this.flScollegaDocumenti = flScollegaDocumenti;
	}
}
