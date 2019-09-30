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

package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MessaggioBase extends MessaggioKey implements Keyed {
	// CD_UTENTE VARCHAR(20)
	private java.lang.String cd_utente;

	// CORPO VARCHAR(4000)
	private java.lang.String corpo;

	// DS_MESSAGGIO VARCHAR(200)
	private java.lang.String ds_messaggio;

	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

	// DT_INIZIO_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_inizio_validita;

	// PRIORITA DECIMAL(1,0) NOT NULL
	private java.lang.Integer priorita;

	// SERVER_URL VARCHAR(30)
	private java.lang.String server_url;

	// SOGGETTO VARCHAR(200) NOT NULL
	private java.lang.String soggetto;


public MessaggioBase() {
	super();
}
public MessaggioBase(java.lang.Long pg_messaggio) {
	super(pg_messaggio);
}
/* 
 * Getter dell'attributo cd_utente
 */
public java.lang.String getCd_utente() {
	return cd_utente;
}
/* 
 * Getter dell'attributo corpo
 */
public java.lang.String getCorpo() {
	return corpo;
}
/* 
 * Getter dell'attributo ds_messaggio
 */
public java.lang.String getDs_messaggio() {
	return ds_messaggio;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo dt_inizio_validita
 */
public java.sql.Timestamp getDt_inizio_validita() {
	return dt_inizio_validita;
}
/* 
 * Getter dell'attributo priorita
 */
public java.lang.Integer getPriorita() {
	return priorita;
}
/* 
 * Getter dell'attributo server_url
 */
public java.lang.String getServer_url() {
	return server_url;
}
/* 
 * Getter dell'attributo soggetto
 */
public java.lang.String getSoggetto() {
	return soggetto;
}

/* 
 * Setter dell'attributo cd_utente
 */
public void setCd_utente(java.lang.String cd_utente) {
	this.cd_utente = cd_utente;
}
/* 
 * Setter dell'attributo corpo
 */
public void setCorpo(java.lang.String corpo) {
	this.corpo = corpo;
}
/* 
 * Setter dell'attributo ds_messaggio
 */
public void setDs_messaggio(java.lang.String ds_messaggio) {
	this.ds_messaggio = ds_messaggio;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo dt_inizio_validita
 */
public void setDt_inizio_validita(java.sql.Timestamp dt_inizio_validita) {
	this.dt_inizio_validita = dt_inizio_validita;
}
/* 
 * Setter dell'attributo priorita
 */
public void setPriorita(java.lang.Integer priorita) {
	this.priorita = priorita;
}
/* 
 * Setter dell'attributo server_url
 */
public void setServer_url(java.lang.String server_url) {
	this.server_url = server_url;
}
/* 
 * Setter dell'attributo soggetto
 */
public void setSoggetto(java.lang.String soggetto) {
	this.soggetto = soggetto;
}
}
