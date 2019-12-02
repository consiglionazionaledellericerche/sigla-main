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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SospesoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TI_SOSPESO_RISCONTRO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_sospeso_riscontro;

	// TI_ENTRATA_SPESA CHAR(1) NOT NULL (PK)
	private java.lang.String ti_entrata_spesa;

	// CD_SOSPESO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_sospeso;

public SospesoKey() {
	super();
}
public SospesoKey(java.lang.String cd_cds,java.lang.String cd_sospeso,java.lang.Integer esercizio,java.lang.String ti_entrata_spesa,java.lang.String ti_sospeso_riscontro) {
	super();
	this.cd_cds = cd_cds;
	this.cd_sospeso = cd_sospeso;
	this.esercizio = esercizio;
	this.ti_entrata_spesa = ti_entrata_spesa;
	this.ti_sospeso_riscontro = ti_sospeso_riscontro;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof SospesoKey)) return false;
	SospesoKey k = (SospesoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_sospeso(),k.getCd_sospeso())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTi_entrata_spesa(),k.getTi_entrata_spesa())) return false;
	if(!compareKey(getTi_sospeso_riscontro(),k.getTi_sospeso_riscontro())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_sospeso
 */
public java.lang.String getCd_sospeso() {
	return cd_sospeso;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo ti_entrata_spesa
 */
public java.lang.String getTi_entrata_spesa() {
	return ti_entrata_spesa;
}
/* 
 * Getter dell'attributo ti_sospeso_riscontro
 */
public java.lang.String getTi_sospeso_riscontro() {
	return ti_sospeso_riscontro;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_sospeso())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getTi_entrata_spesa())+
		calculateKeyHashCode(getTi_sospeso_riscontro());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_sospeso
 */
public void setCd_sospeso(java.lang.String cd_sospeso) {
	this.cd_sospeso = cd_sospeso;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo ti_entrata_spesa
 */
public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
	this.ti_entrata_spesa = ti_entrata_spesa;
}
/* 
 * Setter dell'attributo ti_sospeso_riscontro
 */
public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
	this.ti_sospeso_riscontro = ti_sospeso_riscontro;
}
}
