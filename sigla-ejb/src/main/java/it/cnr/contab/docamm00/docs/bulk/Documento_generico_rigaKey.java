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

package it.cnr.contab.docamm00.docs.bulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Documento_generico_rigaKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_amm;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_DOCUMENTO_GENERICO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_documento_generico;

	// PROGRESSIVO_RIGA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long progressivo_riga;

public Documento_generico_rigaKey() {
	super();
}

public Documento_generico_rigaKey(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_documento_generico,java.lang.Long progressivo_riga) {
	super();
	this.cd_cds = cd_cds;
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.esercizio = esercizio;
	this.pg_documento_generico = pg_documento_generico;
	this.progressivo_riga = progressivo_riga;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Documento_generico_rigaKey)) return false;
	Documento_generico_rigaKey k = (Documento_generico_rigaKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_tipo_documento_amm(),k.getCd_tipo_documento_amm())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_documento_generico(),k.getPg_documento_generico())) return false;
	if(!compareKey(getProgressivo_riga(),k.getProgressivo_riga())) return false;
	return true;
}

/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}

/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}

/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}

/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}

/* 
 * Getter dell'attributo pg_documento_generico
 */
public java.lang.Long getPg_documento_generico() {
	return pg_documento_generico;
}

/* 
 * Getter dell'attributo progressivo_riga
 */
public java.lang.Long getProgressivo_riga() {
	return progressivo_riga;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_tipo_documento_amm())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_documento_generico())+
		calculateKeyHashCode(getProgressivo_riga());
}

/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}

/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}

/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}

/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}

/* 
 * Setter dell'attributo pg_documento_generico
 */
public void setPg_documento_generico(java.lang.Long pg_documento_generico) {
	this.pg_documento_generico = pg_documento_generico;
}

/* 
 * Setter dell'attributo progressivo_riga
 */
public void setProgressivo_riga(java.lang.Long progressivo_riga) {
	this.progressivo_riga = progressivo_riga;
}
}