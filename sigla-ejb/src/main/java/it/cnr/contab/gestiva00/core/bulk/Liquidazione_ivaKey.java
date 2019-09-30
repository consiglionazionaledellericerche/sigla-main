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

public class Liquidazione_ivaKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// DT_FINE TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_fine;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TIPO_LIQUIDAZIONE CHAR(1) NOT NULL (PK)
	private java.lang.String tipo_liquidazione;

	// REPORT_ID DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long report_id;

	// DT_INIZIO TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio;

public Liquidazione_ivaKey() {
	super();
}
public Liquidazione_ivaKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id) {
	super();
	this.cd_cds = cd_cds;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.dt_fine = dt_fine;
	this.dt_inizio = dt_inizio;
	this.esercizio = esercizio;
	this.report_id = report_id;
}
public Liquidazione_ivaKey(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id,java.lang.String tipo_liquidazione) {
	super();
	this.cd_cds = cd_cds;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.dt_fine = dt_fine;
	this.dt_inizio = dt_inizio;
	this.esercizio = esercizio;
	this.report_id = report_id;
	this.tipo_liquidazione = tipo_liquidazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Liquidazione_ivaKey)) return false;
	Liquidazione_ivaKey k = (Liquidazione_ivaKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getDt_fine(),k.getDt_fine())) return false;
	if(!compareKey(getDt_inizio(),k.getDt_inizio())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getReport_id(),k.getReport_id())) return false;
	if(!compareKey(getTipo_liquidazione(),k.getTipo_liquidazione())) return false;
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
public java.lang.Long getReport_id() {
	return report_id;
}
/* 
 * Getter dell'attributo tipo_liquidazione
 */
public java.lang.String getTipo_liquidazione() {
	return tipo_liquidazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getDt_fine())+
		calculateKeyHashCode(getDt_inizio())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getReport_id())+
		calculateKeyHashCode(getTipo_liquidazione());
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
public void setReport_id(java.lang.Long report_id) {
	this.report_id = report_id;
}
/* 
 * Setter dell'attributo tipo_liquidazione
 */
public void setTipo_liquidazione(java.lang.String tipo_liquidazione) {
	this.tipo_liquidazione = tipo_liquidazione;
}
}
