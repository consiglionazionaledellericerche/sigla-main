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

package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_mandato_fondo_ecoBase extends OggettoBulk implements Persistent {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CODICE_FONDO VARCHAR(10)
	private java.lang.String cd_codice_fondo;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

	// IM_MANDATO DECIMAL(15,2)
	private java.math.BigDecimal im_mandato;

	// IM_PAGATO DECIMAL(15,2)
	private java.math.BigDecimal im_pagato;

	// PG_MANDATO DECIMAL(10,0)
	private java.lang.Long pg_mandato;

	// STATO CHAR(1)
	private java.lang.String stato;

	// TI_APERTURA_INCREMENTO VARCHAR(1)
	private java.lang.String ti_apertura_incremento;

public V_ass_mandato_fondo_ecoBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_codice_fondo
 */
public java.lang.String getCd_codice_fondo() {
	return cd_codice_fondo;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo im_mandato
 */
public java.math.BigDecimal getIm_mandato() {
	return im_mandato;
}
/* 
 * Getter dell'attributo im_pagato
 */
public java.math.BigDecimal getIm_pagato() {
	return im_pagato;
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Getter dell'attributo ti_apertura_incremento
 */
public java.lang.String getTi_apertura_incremento() {
	return ti_apertura_incremento;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_codice_fondo
 */
public void setCd_codice_fondo(java.lang.String cd_codice_fondo) {
	this.cd_codice_fondo = cd_codice_fondo;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo im_mandato
 */
public void setIm_mandato(java.math.BigDecimal im_mandato) {
	this.im_mandato = im_mandato;
}
/* 
 * Setter dell'attributo im_pagato
 */
public void setIm_pagato(java.math.BigDecimal im_pagato) {
	this.im_pagato = im_pagato;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
/* 
 * Setter dell'attributo ti_apertura_incremento
 */
public void setTi_apertura_incremento(java.lang.String ti_apertura_incremento) {
	this.ti_apertura_incremento = ti_apertura_incremento;
}
}
