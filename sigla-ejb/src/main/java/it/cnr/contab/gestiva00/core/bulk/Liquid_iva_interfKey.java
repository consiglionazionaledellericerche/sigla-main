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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquid_iva_interfKey extends OggettoBulk implements KeyedPersistent {
	// PG_CARICAMENTO DECIMAL(10) NOT NULL (PK)
	private java.lang.Integer pg_caricamento;
	
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;
	
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;
	
	//	TI_LIQUIDAZIONE CHAR(1) NOT NULL (PK)
	 private java.lang.String ti_liquidazione;
	 		
	// DT_INIZIO TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio;
	
	// DT_FINE TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_fine;

public Liquid_iva_interfKey() {
	super();
}
public Liquid_iva_interfKey(java.lang.Integer pg_caricamento,java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.String ti_liquidazione) {
	super();
	this.pg_caricamento = pg_caricamento;
	this.cd_cds = cd_cds;
	this.esercizio = esercizio;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.dt_fine = dt_fine;
	this.dt_inizio = dt_inizio;
	this.ti_liquidazione = ti_liquidazione;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Liquid_iva_interfKey)) return false;
	Liquid_iva_interfKey k = (Liquid_iva_interfKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getDt_fine(),k.getDt_fine())) return false;
	if(!compareKey(getDt_inizio(),k.getDt_inizio())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_caricamento(),k.getPg_caricamento())) return false;
	if(!compareKey(getTi_liquidazione(),k.getTi_liquidazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo dt_fine
 */
public java.sql.Timestamp getDt_fine() {
	return dt_fine;
}
/* 
 * Getter dell'attributo dt_inizio
 */
public java.sql.Timestamp getDt_inizio() {
	return dt_inizio;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo report_id
 */
public java.lang.Integer getPg_caricamento() {
	return pg_caricamento;
}
/* 
 * Getter dell'attributo tipo_liquidazione
 */
public java.lang.String getTi_liquidazione() {
	return ti_liquidazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getDt_fine())+
		calculateKeyHashCode(getDt_inizio())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_caricamento())+
		calculateKeyHashCode(getTi_liquidazione());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo dt_fine
 */
public void setDt_fine(java.sql.Timestamp dt_fine) {
	this.dt_fine = dt_fine;
}
/* 
 * Setter dell'attributo dt_inizio
 */
public void setDt_inizio(java.sql.Timestamp dt_inizio) {
	this.dt_inizio = dt_inizio;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo report_id
 */
public void setPg_caricamento(java.lang.Integer pg_caricamento) {
	this.pg_caricamento = pg_caricamento;
}
/* 
 * Setter dell'attributo tipo_liquidazione
 */
public void setTi_liquidazione(java.lang.String ti_liquidazione) {
	this.ti_liquidazione = ti_liquidazione;
}
}
