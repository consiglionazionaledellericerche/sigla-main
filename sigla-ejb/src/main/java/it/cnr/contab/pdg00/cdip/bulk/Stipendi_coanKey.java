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

public class Stipendi_coanKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UO VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_uo;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// MESE DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer mese;

public Stipendi_coanKey() {
	super();
}
public Stipendi_coanKey(java.lang.String cd_cds,java.lang.String cd_uo,java.lang.Integer esercizio,java.lang.Integer mese) {
	super();
	this.cd_cds = cd_cds;
	this.cd_uo = cd_uo;
	this.esercizio = esercizio;
	this.mese = mese;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Stipendi_coanKey)) return false;
	Stipendi_coanKey k = (Stipendi_coanKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_uo(),k.getCd_uo())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getMese(),k.getMese())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_uo
 */
public java.lang.String getCd_uo() {
	return cd_uo;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_uo())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getMese());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_uo
 */
public void setCd_uo(java.lang.String cd_uo) {
	this.cd_uo = cd_uo;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
}
