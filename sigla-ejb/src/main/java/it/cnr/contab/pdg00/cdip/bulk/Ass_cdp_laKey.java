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

public class Ass_cdp_laKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ID_MATRICOLA VARCHAR(10) NOT NULL (PK)
	private java.lang.String id_matricola;

	// MESE DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer mese;

	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

public Ass_cdp_laKey() {
	super();
}
public Ass_cdp_laKey(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese) {
	super();
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_linea_attivita = cd_linea_attivita;
	this.esercizio = esercizio;
	this.id_matricola = id_matricola;
	this.mese = mese;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_cdp_laKey)) return false;
	Ass_cdp_laKey k = (Ass_cdp_laKey)o;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getId_matricola(),k.getId_matricola())) return false;
	if(!compareKey(getMese(),k.getMese())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_linea_attivita
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
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
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_linea_attivita())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getId_matricola())+
		calculateKeyHashCode(getMese());
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_linea_attivita
 */
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.cd_linea_attivita = cd_linea_attivita;
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
}
