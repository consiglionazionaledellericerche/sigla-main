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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_liquid_capitoli_coriBase extends OggettoBulk implements Persistent {
	// CD_CONTRIBUTO_RITENUTA VARCHAR(10) NOT NULL
	private java.lang.String cd_contributo_ritenuta;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// CD_GRUPPO_CR VARCHAR(10)
	private java.lang.String cd_gruppo_cr;

	// DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
	
	private java.lang.String ti_ente_percepiente;
	
	private java.lang.String ti_gestione;
	
	private java.lang.String ti_appartenenza;

public V_liquid_capitoli_coriBase() {
	super();
}
/* 
 * Getter dell'attributo cd_contributo_ritenuta
 */
public java.lang.String getCd_contributo_ritenuta() {
	return cd_contributo_ritenuta;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_gruppo_cr
 */
public java.lang.String getCd_gruppo_cr() {
	return cd_gruppo_cr;
}
/* 
 * Getter dell'attributo ds_elemento_voce
 */
public java.lang.String getDs_elemento_voce() {
	return ds_elemento_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_ente_percepiente
 */
public java.lang.String getTi_ente_percepiente() {
	return ti_ente_percepiente;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Setter dell'attributo cd_contributo_ritenuta
 */
public void setCd_contributo_ritenuta(java.lang.String cd_contributo_ritenuta) {
	this.cd_contributo_ritenuta = cd_contributo_ritenuta;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_gruppo_cr
 */
public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr) {
	this.cd_gruppo_cr = cd_gruppo_cr;
}
/* 
 * Setter dell'attributo ds_elemento_voce
 */
public void setDs_elemento_voce(java.lang.String ds_elemento_voce) {
	this.ds_elemento_voce = ds_elemento_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String newTi_appartenenza) {
	ti_appartenenza = newTi_appartenenza;
}
/**
 * Setter della proprietà ti_ente_percepiente
 *
 * @param newTi_ente_percepiente <code>String</code> il nuovo valore
 */
public void setTi_ente_percepiente(java.lang.String newTi_ente_percepiente) {
	ti_ente_percepiente = newTi_ente_percepiente;
}
/**
 * Setter della proprietà ti_gestione
 *
 * @param newTi_gestione <code>String</code> il nuovo valore
 */
public void setTi_gestione(java.lang.String newTi_gestione) {
	ti_gestione = newTi_gestione;
}
}
