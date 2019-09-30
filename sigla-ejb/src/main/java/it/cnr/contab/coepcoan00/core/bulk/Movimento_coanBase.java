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

package it.cnr.contab.coepcoan00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Movimento_coanBase extends Movimento_coanKey implements Keyed {
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;

	// CD_FUNZIONE VARCHAR(2) NOT NULL
	private java.lang.String cd_funzione;

	// CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;

	// CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;

	// CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;

	// DS_MOVIMENTO VARCHAR(100) NOT NULL
	private java.lang.String ds_movimento;

	// IM_MOVIMENTO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_movimento;

	// PG_NUMERO_DOCUMENTO DECIMAL(10,0)
	private java.lang.Long pg_numero_documento;

	// STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;

public Movimento_coanBase() {
	super();
}
public Movimento_coanBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.Long pg_movimento,java.lang.Long pg_scrittura,java.lang.String sezione) {
	super(cd_cds,cd_unita_organizzativa,cd_voce_ep,esercizio,pg_movimento,pg_scrittura,sezione);
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
/* 
 * Getter dell'attributo cd_linea_attivita
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo ds_movimento
 */
public java.lang.String getDs_movimento() {
	return ds_movimento;
}
/* 
 * Getter dell'attributo im_movimento
 */
public java.math.BigDecimal getIm_movimento() {
	return im_movimento;
}
/* 
 * Getter dell'attributo pg_numero_documento
 */
public java.lang.Long getPg_numero_documento() {
	return pg_numero_documento;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
/* 
 * Setter dell'attributo cd_linea_attivita
 */
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.cd_linea_attivita = cd_linea_attivita;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo ds_movimento
 */
public void setDs_movimento(java.lang.String ds_movimento) {
	this.ds_movimento = ds_movimento;
}
/* 
 * Setter dell'attributo im_movimento
 */
public void setIm_movimento(java.math.BigDecimal im_movimento) {
	this.im_movimento = im_movimento;
}
/* 
 * Setter dell'attributo pg_numero_documento
 */
public void setPg_numero_documento(java.lang.Long pg_numero_documento) {
	this.pg_numero_documento = pg_numero_documento;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
