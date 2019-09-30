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

package it.cnr.contab.preventvar00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancio_detKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// PG_VARIAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_variazione;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

	// CD_VOCE VARCHAR(50) NOT NULL (PK)
	private java.lang.String cd_voce;

public Var_bilancio_detKey() {
	super();
}
public Var_bilancio_detKey(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	this.cd_cds = cd_cds;
	this.cd_voce = cd_voce;
	this.esercizio = esercizio;
	this.pg_variazione = pg_variazione;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Var_bilancio_detKey)) return false;
	Var_bilancio_detKey k = (Var_bilancio_detKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_voce(),k.getCd_voce())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_variazione(),k.getPg_variazione())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
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
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_variazione
 */
public java.lang.Long getPg_variazione() {
	return pg_variazione;
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
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_voce())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_variazione())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
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
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_variazione
 */
public void setPg_variazione(java.lang.Long pg_variazione) {
	this.pg_variazione = pg_variazione;
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
