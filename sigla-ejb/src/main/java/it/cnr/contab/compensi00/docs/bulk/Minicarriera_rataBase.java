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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Minicarriera_rataBase extends Minicarriera_rataKey implements Keyed {
	// CD_CDS_COMPENSO VARCHAR(30)
	private java.lang.String cd_cds_compenso;

	// CD_UO_COMPENSO VARCHAR(30)
	private java.lang.String cd_uo_compenso;

	// DT_FINE_RATA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_rata;

	// DT_INIZIO_RATA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_inizio_rata;

	// DT_SCADENZA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_scadenza;

	// ESERCIZIO_COMPENSO DECIMAL(4,0)
	private java.lang.Integer esercizio_compenso;

	// IM_RATA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rata;

	// PG_COMPENSO DECIMAL(10,0)
	private java.lang.Long pg_compenso;

	// STATO_ASS_COMPENSO CHAR(1) NOT NULL
	private java.lang.String stato_ass_compenso;

public Minicarriera_rataBase() {
	super();
}
public Minicarriera_rataBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_minicarriera,java.lang.Long pg_rata) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_minicarriera,pg_rata);
}
/* 
 * Getter dell'attributo cd_cds_compenso
 */
public java.lang.String getCd_cds_compenso() {
	return cd_cds_compenso;
}
/* 
 * Getter dell'attributo cd_uo_compenso
 */
public java.lang.String getCd_uo_compenso() {
	return cd_uo_compenso;
}
/* 
 * Getter dell'attributo dt_fine_rata
 */
public java.sql.Timestamp getDt_fine_rata() {
	return dt_fine_rata;
}
/* 
 * Getter dell'attributo dt_inizio_rata
 */
public java.sql.Timestamp getDt_inizio_rata() {
	return dt_inizio_rata;
}
/* 
 * Getter dell'attributo dt_scadenza
 */
public java.sql.Timestamp getDt_scadenza() {
	return dt_scadenza;
}
/* 
 * Getter dell'attributo esercizio_compenso
 */
public java.lang.Integer getEsercizio_compenso() {
	return esercizio_compenso;
}
/* 
 * Getter dell'attributo im_rata
 */
public java.math.BigDecimal getIm_rata() {
	return im_rata;
}
/* 
 * Getter dell'attributo pg_compenso
 */
public java.lang.Long getPg_compenso() {
	return pg_compenso;
}
/* 
 * Getter dell'attributo stato_ass_compenso
 */
public java.lang.String getStato_ass_compenso() {
	return stato_ass_compenso;
}
/* 
 * Setter dell'attributo cd_cds_compenso
 */
public void setCd_cds_compenso(java.lang.String cd_cds_compenso) {
	this.cd_cds_compenso = cd_cds_compenso;
}
/* 
 * Setter dell'attributo cd_uo_compenso
 */
public void setCd_uo_compenso(java.lang.String cd_uo_compenso) {
	this.cd_uo_compenso = cd_uo_compenso;
}
/* 
 * Setter dell'attributo dt_fine_rata
 */
public void setDt_fine_rata(java.sql.Timestamp dt_fine_rata) {
	this.dt_fine_rata = dt_fine_rata;
}
/* 
 * Setter dell'attributo dt_inizio_rata
 */
public void setDt_inizio_rata(java.sql.Timestamp dt_inizio_rata) {
	this.dt_inizio_rata = dt_inizio_rata;
}
/* 
 * Setter dell'attributo dt_scadenza
 */
public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
	this.dt_scadenza = dt_scadenza;
}
/* 
 * Setter dell'attributo esercizio_compenso
 */
public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
	this.esercizio_compenso = esercizio_compenso;
}
/* 
 * Setter dell'attributo im_rata
 */
public void setIm_rata(java.math.BigDecimal im_rata) {
	this.im_rata = im_rata;
}
/* 
 * Setter dell'attributo pg_compenso
 */
public void setPg_compenso(java.lang.Long pg_compenso) {
	this.pg_compenso = pg_compenso;
}
/* 
 * Setter dell'attributo stato_ass_compenso
 */
public void setStato_ass_compenso(java.lang.String stato_ass_compenso) {
	this.stato_ass_compenso = stato_ass_compenso;
}
}
