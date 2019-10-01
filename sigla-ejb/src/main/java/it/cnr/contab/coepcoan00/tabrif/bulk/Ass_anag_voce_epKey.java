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

package it.cnr.contab.coepcoan00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_anag_voce_epKey extends OggettoBulk implements KeyedPersistent {
	// ENTE_ALTRO CHAR(1) NOT NULL (PK)
	private java.lang.String ente_altro;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TI_ENTITA VARCHAR(5) NOT NULL (PK)
	private java.lang.String ti_entita;

	// TI_TERZO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_terzo;

	// CD_CLASSIFIC_ANAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_classific_anag;

	// ITALIANO_ESTERO CHAR(1) NOT NULL (PK)
	private java.lang.String italiano_estero;

public Ass_anag_voce_epKey() {
	super();
}
public Ass_anag_voce_epKey(java.lang.String cd_classific_anag,java.lang.String ente_altro,java.lang.Integer esercizio,java.lang.String italiano_estero,java.lang.String ti_entita,java.lang.String ti_terzo) {
	this.cd_classific_anag = cd_classific_anag;
	this.ente_altro = ente_altro;
	this.esercizio = esercizio;
	this.italiano_estero = italiano_estero;
	this.ti_entita = ti_entita;
	this.ti_terzo = ti_terzo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_anag_voce_epKey)) return false;
	Ass_anag_voce_epKey k = (Ass_anag_voce_epKey)o;
	if(!compareKey(getCd_classific_anag(),k.getCd_classific_anag())) return false;
	if(!compareKey(getEnte_altro(),k.getEnte_altro())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getItaliano_estero(),k.getItaliano_estero())) return false;
	if(!compareKey(getTi_entita(),k.getTi_entita())) return false;
	if(!compareKey(getTi_terzo(),k.getTi_terzo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_classific_anag
 */
public java.lang.String getCd_classific_anag() {
	return cd_classific_anag;
}
/* 
 * Getter dell'attributo ente_altro
 */
public java.lang.String getEnte_altro() {
	return ente_altro;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo italiano_estero
 */
public java.lang.String getItaliano_estero() {
	return italiano_estero;
}
/* 
 * Getter dell'attributo ti_entita
 */
public java.lang.String getTi_entita() {
	return ti_entita;
}
/* 
 * Getter dell'attributo ti_terzo
 */
public java.lang.String getTi_terzo() {
	return ti_terzo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_classific_anag())+
		calculateKeyHashCode(getEnte_altro())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getItaliano_estero())+
		calculateKeyHashCode(getTi_entita())+
		calculateKeyHashCode(getTi_terzo());
}
/* 
 * Setter dell'attributo cd_classific_anag
 */
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.cd_classific_anag = cd_classific_anag;
}
/* 
 * Setter dell'attributo ente_altro
 */
public void setEnte_altro(java.lang.String ente_altro) {
	this.ente_altro = ente_altro;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo italiano_estero
 */
public void setItaliano_estero(java.lang.String italiano_estero) {
	this.italiano_estero = italiano_estero;
}
/* 
 * Setter dell'attributo ti_entita
 */
public void setTi_entita(java.lang.String ti_entita) {
	this.ti_entita = ti_entita;
}
/* 
 * Setter dell'attributo ti_terzo
 */
public void setTi_terzo(java.lang.String ti_terzo) {
	this.ti_terzo = ti_terzo;
}
}
