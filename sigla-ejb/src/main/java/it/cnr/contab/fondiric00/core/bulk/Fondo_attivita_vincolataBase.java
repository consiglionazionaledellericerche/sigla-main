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

package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_attivita_vincolataBase extends Fondo_attivita_vincolataKey implements Keyed {
	// CD_DIVISA VARCHAR(10)
	private java.lang.String cd_divisa;

	// CD_ENTE_FIN DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_ente_fin;

	// CD_RESPONSABILE_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_responsabile_terzo;

	// CD_TIPO_FONDO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_fondo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;

	// DS_FONDO VARCHAR(100) NOT NULL
	private java.lang.String ds_fondo;

	// DT_FINE TIMESTAMP
	private java.sql.Timestamp dt_fine;

	// DT_INIZIO TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_inizio;

	// DT_PROROGA TIMESTAMP
	private java.sql.Timestamp dt_proroga;

	// IMPORTO_DIVISA DECIMAL(20,6)
	private java.math.BigDecimal importo_divisa;

	// IMPORTO_FONDO DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal importo_fondo;

	// NOTE VARCHAR(2000)
	private java.lang.String note;

public Fondo_attivita_vincolataBase() {
	super();
}
public Fondo_attivita_vincolataBase(java.lang.String cd_fondo) {
	super(cd_fondo);
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo cd_ente_fin
 */
public java.lang.Integer getCd_ente_fin() {
	return cd_ente_fin;
}
/* 
 * Getter dell'attributo cd_responsabile_terzo
 */
public java.lang.Integer getCd_responsabile_terzo() {
	return cd_responsabile_terzo;
}
/* 
 * Getter dell'attributo cd_tipo_fondo
 */
public java.lang.String getCd_tipo_fondo() {
	return cd_tipo_fondo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo ds_fondo
 */
public java.lang.String getDs_fondo() {
	return ds_fondo;
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
 * Getter dell'attributo dt_proroga
 */
public java.sql.Timestamp getDt_proroga() {
	return dt_proroga;
}
/* 
 * Getter dell'attributo importo_divisa
 */
public java.math.BigDecimal getImporto_divisa() {
	return importo_divisa;
}
/* 
 * Getter dell'attributo importo_fondo
 */
public java.math.BigDecimal getImporto_fondo() {
	return importo_fondo;
}
/* 
 * Getter dell'attributo note
 */
public java.lang.String getNote() {
	return note;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo cd_ente_fin
 */
public void setCd_ente_fin(java.lang.Integer cd_ente_fin) {
	this.cd_ente_fin = cd_ente_fin;
}
/* 
 * Setter dell'attributo cd_responsabile_terzo
 */
public void setCd_responsabile_terzo(java.lang.Integer cd_responsabile_terzo) {
	this.cd_responsabile_terzo = cd_responsabile_terzo;
}
/* 
 * Setter dell'attributo cd_tipo_fondo
 */
public void setCd_tipo_fondo(java.lang.String cd_tipo_fondo) {
	this.cd_tipo_fondo = cd_tipo_fondo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo ds_fondo
 */
public void setDs_fondo(java.lang.String ds_fondo) {
	this.ds_fondo = ds_fondo;
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
 * Setter dell'attributo dt_proroga
 */
public void setDt_proroga(java.sql.Timestamp dt_proroga) {
	this.dt_proroga = dt_proroga;
}
/* 
 * Setter dell'attributo importo_divisa
 */
public void setImporto_divisa(java.math.BigDecimal importo_divisa) {
	this.importo_divisa = importo_divisa;
}
/* 
 * Setter dell'attributo importo_fondo
 */
public void setImporto_fondo(java.math.BigDecimal importo_fondo) {
	this.importo_fondo = importo_fondo;
}
/* 
 * Setter dell'attributo note
 */
public void setNote(java.lang.String note) {
	this.note = note;
}
}
