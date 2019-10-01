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

public class Ass_obb_acr_pgiroKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_obbligazione;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_accertamento;

public Ass_obb_acr_pgiroKey() {
	super();
}
public Ass_obb_acr_pgiroKey(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento,java.lang.Integer esercizio_ori_obbligazione,java.lang.Long pg_obbligazione) {
	super();
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
	this.pg_accertamento = pg_accertamento;
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
	this.pg_obbligazione = pg_obbligazione;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_obb_acr_pgiroKey)) return false;
	Ass_obb_acr_pgiroKey k = (Ass_obb_acr_pgiroKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_ori_accertamento(),k.getEsercizio_ori_accertamento())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
	if(!compareKey(getEsercizio_ori_obbligazione(),k.getEsercizio_ori_obbligazione())) return false;
	if(!compareKey(getPg_obbligazione(),k.getPg_obbligazione())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_obb_acr_pgiroKey)) return false;
	Ass_obb_acr_pgiroKey k = (Ass_obb_acr_pgiroKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getEsercizio_ori_accertamento(),k.getEsercizio_ori_accertamento())) return false;
	if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
	if(!compareKey(getEsercizio_ori_obbligazione(),k.getEsercizio_ori_obbligazione())) return false;
	if(!compareKey(getPg_obbligazione(),k.getPg_obbligazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_ori_accertamento())+
		calculateKeyHashCode(getPg_accertamento())+
		calculateKeyHashCode(getEsercizio_ori_obbligazione())+
		calculateKeyHashCode(getPg_obbligazione());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getEsercizio_ori_accertamento())+
		calculateKeyHashCode(getPg_accertamento())+
		calculateKeyHashCode(getEsercizio_ori_obbligazione())+
		calculateKeyHashCode(getPg_obbligazione());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
}
