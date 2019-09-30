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

package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_linea_attivitaBase extends Tipo_linea_attivitaKey implements Keyed {
	// CD_CDR_CREATORE VARCHAR(30)
	private java.lang.String cd_cdr_creatore;

/* 
 * Getter dell'attributo cd_cdr_creatore
 */
public java.lang.String getCd_cdr_creatore() {
	return cd_cdr_creatore;
}

/* 
 * Setter dell'attributo cd_cdr_creatore
 */
public void setCd_cdr_creatore(java.lang.String cd_cdr_creatore) {
	this.cd_cdr_creatore = cd_cdr_creatore;
}
	// CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;

/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}

/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
	// CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;

/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}

/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
	// DS_TIPO_LINEA_ATTIVITA VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_linea_attivita;

/* 
 * Getter dell'attributo ds_tipo_linea_attivita
 */
public java.lang.String getDs_tipo_linea_attivita() {
	return ds_tipo_linea_attivita;
}

/* 
 * Setter dell'attributo ds_tipo_linea_attivita
 */
public void setDs_tipo_linea_attivita(java.lang.String ds_tipo_linea_attivita) {
	this.ds_tipo_linea_attivita = ds_tipo_linea_attivita;
}
	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}

/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
	// TI_TIPO_LA CHAR(1) NOT NULL
	private java.lang.String ti_tipo_la;

/* 
 * Getter dell'attributo ti_tipo_la
 */
public java.lang.String getTi_tipo_la() {
	return ti_tipo_la;
}

/* 
 * Setter dell'attributo ti_tipo_la
 */
public void setTi_tipo_la(java.lang.String ti_tipo_la) {
	this.ti_tipo_la = ti_tipo_la;
}

public Tipo_linea_attivitaBase() {
	super();
}

public Tipo_linea_attivitaBase(java.lang.String cd_tipo_linea_attivita) {
	super(cd_tipo_linea_attivita);
}
}
