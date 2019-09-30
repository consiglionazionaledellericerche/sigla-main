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

package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_documento_ammBase extends Tipo_documento_ammKey implements Keyed {
	// DS_TIPO_DOCUMENTO_AMM VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_documento_amm;

/* 
 * Getter dell'attributo ds_tipo_documento_amm
 */
public java.lang.String getDs_tipo_documento_amm() {
	return ds_tipo_documento_amm;
}

/* 
 * Setter dell'attributo ds_tipo_documento_amm
 */
public void setDs_tipo_documento_amm(java.lang.String ds_tipo_documento_amm) {
	this.ds_tipo_documento_amm = ds_tipo_documento_amm;
}
	// FL_DOC_GENERICO CHAR(1) NOT NULL
	private java.lang.Boolean fl_doc_generico;

/* 
 * Getter dell'attributo fl_doc_generico
 */
public java.lang.Boolean getFl_doc_generico() {
	return fl_doc_generico;
}

/* 
 * Setter dell'attributo fl_doc_generico
 */
public void setFl_doc_generico(java.lang.Boolean fl_doc_generico) {
	this.fl_doc_generico = fl_doc_generico;
}
	// FL_MANREV_UTENTE CHAR(1) NOT NULL
	private java.lang.Boolean fl_manrev_utente;

/* 
 * Getter dell'attributo fl_manrev_utente
 */
public java.lang.Boolean getFl_manrev_utente() {
	return fl_manrev_utente;
}

/* 
 * Setter dell'attributo fl_manrev_utente
 */
public void setFl_manrev_utente(java.lang.Boolean fl_manrev_utente) {
	this.fl_manrev_utente = fl_manrev_utente;
}
	// FL_SOLO_PARTITA_GIRO CHAR(1) NOT NULL
	private java.lang.Boolean fl_solo_partita_giro;

/* 
 * Getter dell'attributo fl_solo_partita_giro
 */
public java.lang.Boolean getFl_solo_partita_giro() {
	return fl_solo_partita_giro;
}

/* 
 * Setter dell'attributo fl_solo_partita_giro
 */
public void setFl_solo_partita_giro(java.lang.Boolean fl_solo_partita_giro) {
	this.fl_solo_partita_giro = fl_solo_partita_giro;
}
	// FL_UTILIZZO_DOC_GENERICO CHAR(1) NOT NULL
	private java.lang.Boolean fl_utilizzo_doc_generico;

/* 
 * Getter dell'attributo fl_utilizzo_doc_generico
 */
public java.lang.Boolean getFl_utilizzo_doc_generico() {
	return fl_utilizzo_doc_generico;
}

/* 
 * Setter dell'attributo fl_utilizzo_doc_generico
 */
public void setFl_utilizzo_doc_generico(java.lang.Boolean fl_utilizzo_doc_generico) {
	this.fl_utilizzo_doc_generico = fl_utilizzo_doc_generico;
}
	// TI_ENTRATA_SPESA CHAR(1) NOT NULL
	private java.lang.String ti_entrata_spesa;

/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}

/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}

public Tipo_documento_ammBase() {
	super();
}

public Tipo_documento_ammBase(java.lang.String cd_tipo_documento_amm) {
	super(cd_tipo_documento_amm);
}
}
