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

public class Report_statoKey extends OggettoBulk implements KeyedPersistent {
	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}

/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
	// DT_FINE TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_fine;

/* 
 * Getter dell'attributo dt_fine
 */
public java.sql.Timestamp getDt_fine() {
	return dt_fine;
}

/* 
 * Setter dell'attributo dt_fine
 */
public void setDt_fine(java.sql.Timestamp dt_fine) {
	this.dt_fine = dt_fine;
}
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}

/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
	// STATO CHAR(1) NOT NULL (PK)
	private java.lang.String stato;

/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}

/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
	// TIPO_REPORT VARCHAR(50) NOT NULL (PK)
	private java.lang.String tipo_report;

/* 
 * Getter dell'attributo tipo_report
 */
public java.lang.String getTipo_report() {
	return tipo_report;
}

/* 
 * Setter dell'attributo tipo_report
 */
public void setTipo_report(java.lang.String tipo_report) {
	this.tipo_report = tipo_report;
}
	// CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_sezionale;

/* 
 * Getter dell'attributo cd_tipo_sezionale
 */
public java.lang.String getCd_tipo_sezionale() {
	return cd_tipo_sezionale;
}

/* 
 * Setter dell'attributo cd_tipo_sezionale
 */
public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
	this.cd_tipo_sezionale = cd_tipo_sezionale;
}
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}

/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
	// TI_DOCUMENTO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_documento;

/* 
 * Getter dell'attributo ti_documento
 */
public java.lang.String getTi_documento() {
	return ti_documento;
}

/* 
 * Setter dell'attributo ti_documento
 */
public void setTi_documento(java.lang.String ti_documento) {
	this.ti_documento = ti_documento;
}
	// DT_INIZIO TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_inizio;

/* 
 * Getter dell'attributo dt_inizio
 */
public java.sql.Timestamp getDt_inizio() {
	return dt_inizio;
}

/* 
 * Setter dell'attributo dt_inizio
 */
public void setDt_inizio(java.sql.Timestamp dt_inizio) {
	this.dt_inizio = dt_inizio;
}

public Report_statoKey() {
	super();
}


public Report_statoKey(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_documento,java.lang.String tipo_report) {
	this.cd_cds = cd_cds;
	this.cd_tipo_sezionale = cd_tipo_sezionale;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.dt_fine = dt_fine;
	this.dt_inizio = dt_inizio;
	this.esercizio = esercizio;
	this.stato = stato;
	this.ti_documento = ti_documento;
	this.tipo_report = tipo_report;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Report_statoKey)) return false;
	Report_statoKey k = (Report_statoKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_tipo_sezionale(),k.getCd_tipo_sezionale())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getDt_fine(),k.getDt_fine())) return false;
	if(!compareKey(getDt_inizio(),k.getDt_inizio())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getStato(),k.getStato())) return false;
	if(!compareKey(getTipo_report(),k.getTipo_report())) return false;
	if(!compareKey(getTi_documento(),k.getTi_documento())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_tipo_sezionale())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getDt_fine())+
		calculateKeyHashCode(getDt_inizio())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getStato())+
		calculateKeyHashCode(getTipo_report())+
		calculateKeyHashCode(getTi_documento());
}

}
