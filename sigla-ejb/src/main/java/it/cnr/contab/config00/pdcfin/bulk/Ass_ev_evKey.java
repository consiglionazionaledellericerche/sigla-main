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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_evKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TI_APPARTENENZA_COLL CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza_coll;

	// TI_GESTIONE_COLL CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione_coll;

	// CD_NATURA VARCHAR(1) NOT NULL (PK)
	private java.lang.String cd_natura;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

	// CD_ELEMENTO_VOCE_COLL VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce_coll;

public Ass_ev_evKey() {
	super();
}
public Ass_ev_evKey(java.lang.String cd_cds,java.lang.String cd_elemento_voce,java.lang.String cd_elemento_voce_coll,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_appartenenza_coll,java.lang.String ti_gestione,java.lang.String ti_gestione_coll) {
	super();
	this.cd_cds = cd_cds;
	this.cd_elemento_voce = cd_elemento_voce;
	this.cd_elemento_voce_coll = cd_elemento_voce_coll;
	this.cd_natura = cd_natura;
	this.esercizio = esercizio;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_appartenenza_coll = ti_appartenenza_coll;
	this.ti_gestione = ti_gestione;
	this.ti_gestione_coll = ti_gestione_coll;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_ev_evKey)) return false;
	Ass_ev_evKey k = (Ass_ev_evKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
	if(!compareKey(getCd_elemento_voce_coll(),k.getCd_elemento_voce_coll())) return false;
	if(!compareKey(getCd_natura(),k.getCd_natura())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_appartenenza_coll(),k.getTi_appartenenza_coll())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	if(!compareKey(getTi_gestione_coll(),k.getTi_gestione_coll())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_elemento_voce_coll
 */
public java.lang.String getCd_elemento_voce_coll() {
	return cd_elemento_voce_coll;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
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
 * Getter dell'attributo ti_appartenenza_coll
 */
public java.lang.String getTi_appartenenza_coll() {
	return ti_appartenenza_coll;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Getter dell'attributo ti_gestione_coll
 */
public java.lang.String getTi_gestione_coll() {
	return ti_gestione_coll;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_elemento_voce())+
		calculateKeyHashCode(getCd_elemento_voce_coll())+
		calculateKeyHashCode(getCd_natura())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_appartenenza_coll())+
		calculateKeyHashCode(getTi_gestione())+
		calculateKeyHashCode(getTi_gestione_coll());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_elemento_voce_coll
 */
public void setCd_elemento_voce_coll(java.lang.String cd_elemento_voce_coll) {
	this.cd_elemento_voce_coll = cd_elemento_voce_coll;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
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
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_appartenenza_coll
 */
public void setTi_appartenenza_coll(java.lang.String ti_appartenenza_coll) {
	this.ti_appartenenza_coll = ti_appartenenza_coll;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
/* 
 * Setter dell'attributo ti_gestione_coll
 */
public void setTi_gestione_coll(java.lang.String ti_gestione_coll) {
	this.ti_gestione_coll = ti_gestione_coll;
}
}
