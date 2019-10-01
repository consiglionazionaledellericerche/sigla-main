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

package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_sing_contoKey extends OggettoBulk  implements KeyedPersistent {

	// ID_REPORT DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal id_report;

	// CD_VOCE VARCHAR(200)
	private java.lang.String cd_voce;

	// ESERCIZIO DECIMAL(20,3)
	private java.math.BigDecimal esercizio;

	// TI_APPARTENENZA VARCHAR(200)
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE VARCHAR(200)
	private java.lang.String ti_gestione;
public V_stm_paramin_sing_contoKey() {
	super();
}
/* 
 * Getter dell'attributo cd_voce
 */
public java.lang.String getCd_voce() {
	return cd_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.math.BigDecimal getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id_report
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Setter dell'attributo cd_voce
 */
public void setCd_voce(java.lang.String cd_voce) {
	this.cd_voce = cd_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.math.BigDecimal esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id_report
 */
public void setId_report(java.math.BigDecimal id_report) {
	this.id_report = id_report;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
}
