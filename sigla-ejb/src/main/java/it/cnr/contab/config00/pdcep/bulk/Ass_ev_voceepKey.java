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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_voceepKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_VOCE_EP VARCHAR(45) NOT NULL (PK)
	private java.lang.String cd_voce_ep;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

public Ass_ev_voceepKey() {
	super();
}
public Ass_ev_voceepKey(java.lang.String cd_elemento_voce,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super();
	this.cd_elemento_voce = cd_elemento_voce;
	this.cd_voce_ep = cd_voce_ep;
	this.esercizio = esercizio;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_ev_voceepKey)) return false;
	Ass_ev_voceepKey k = (Ass_ev_voceepKey)o;
	if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
	if(!compareKey(getCd_voce_ep(),k.getCd_voce_ep())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_voce_ep
 */
public java.lang.String getCd_voce_ep() {
	return cd_voce_ep;
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
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_elemento_voce())+
		calculateKeyHashCode(getCd_voce_ep())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione());
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_voce_ep
 */
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.cd_voce_ep = cd_voce_ep;
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
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
}
