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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Costo_del_dipendenteKey extends OggettoBulk implements KeyedPersistent {
	// TI_APPARTENENZA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_appartenenza;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ID_MATRICOLA VARCHAR(10) NOT NULL (PK)
	private java.lang.String id_matricola;

	// MESE DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer mese;

	// TI_PREV_CONS VARCHAR(10) NOT NULL (PK)
	private java.lang.String ti_prev_cons;

	// TI_GESTIONE CHAR(1) NOT NULL (PK)
	private java.lang.String ti_gestione;

public Costo_del_dipendenteKey() {
	super();
}
public Costo_del_dipendenteKey(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super();
	this.cd_elemento_voce = cd_elemento_voce;
	this.esercizio = esercizio;
	this.id_matricola = id_matricola;
	this.mese = mese;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
	this.ti_prev_cons = ti_prev_cons;
}
public Costo_del_dipendenteKey(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super();
	this.cd_elemento_voce = cd_elemento_voce;
	this.esercizio = esercizio;
	this.id_matricola = id_matricola;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
	this.ti_prev_cons = ti_prev_cons;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Costo_del_dipendenteKey)) return false;
	Costo_del_dipendenteKey k = (Costo_del_dipendenteKey)o;
	if(!compareKey(getCd_elemento_voce(),k.getCd_elemento_voce())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getId_matricola(),k.getId_matricola())) return false;
	if(!compareKey(getMese(),k.getMese())) return false;
	if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
	if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
	if(!compareKey(getTi_prev_cons(),k.getTi_prev_cons())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id_matricola
 */
public java.lang.String getId_matricola() {
	return id_matricola;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
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
 * Getter dell'attributo ti_prev_cons
 */
public java.lang.String getTi_prev_cons() {
	return ti_prev_cons;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_elemento_voce())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getId_matricola())+
		calculateKeyHashCode(getMese())+
		calculateKeyHashCode(getTi_appartenenza())+
		calculateKeyHashCode(getTi_gestione())+
		calculateKeyHashCode(getTi_prev_cons());
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id_matricola
 */
public void setId_matricola(java.lang.String id_matricola) {
	this.id_matricola = id_matricola;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
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
/* 
 * Setter dell'attributo ti_prev_cons
 */
public void setTi_prev_cons(java.lang.String ti_prev_cons) {
	this.ti_prev_cons = ti_prev_cons;
}
}
