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

package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Missione_tipo_spesaBase extends Missione_tipo_spesaKey implements Keyed {
	// CD_DIVISA VARCHAR(10) NOT NULL
	private java.lang.String cd_divisa;

	// DS_TI_SPESA VARCHAR(100) NOT NULL
	private java.lang.String ds_ti_spesa;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// FL_ALLOGGIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_alloggio;

	// FL_GIUSTIFICATIVO_RICHIESTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_giustificativo_richiesto;

	// FL_PASTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_pasto;

	// FL_RIMBORSO_KM CHAR(1) NOT NULL
	private java.lang.Boolean fl_rimborso_km;

	// FL_TRASPORTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_trasporto;

	// LIMITE_MAX_SPESA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal limite_max_spesa;

	// PERCENTUALE_MAGGIORAZIONE DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal percentuale_maggiorazione;

	// FL_AMMISSIBILE_CON_RIMBORSO CHAR(1) NOT NULL
	private java.lang.Boolean fl_ammissibile_con_rimborso;
	
public Missione_tipo_spesaBase() {
	super();
}
public Missione_tipo_spesaBase(java.lang.String cd_ti_spesa,java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica) {
	super(cd_ti_spesa,dt_inizio_validita,pg_nazione,pg_rif_inquadramento,ti_area_geografica);
}
/* 
 * Getter dell'attributo cd_divisa
 */
public java.lang.String getCd_divisa() {
	return cd_divisa;
}
/* 
 * Getter dell'attributo ds_ti_spesa
 */
public java.lang.String getDs_ti_spesa() {
	return ds_ti_spesa;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo fl_alloggio
 */
public java.lang.Boolean getFl_alloggio() {
	return fl_alloggio;
}
/* 
 * Getter dell'attributo fl_giustificativo_richiesto
 */
public java.lang.Boolean getFl_giustificativo_richiesto() {
	return fl_giustificativo_richiesto;
}
/* 
 * Getter dell'attributo fl_pasto
 */
public java.lang.Boolean getFl_pasto() {
	return fl_pasto;
}
/* 
 * Getter dell'attributo fl_rimborso_km
 */
public java.lang.Boolean getFl_rimborso_km() {
	return fl_rimborso_km;
}
/* 
 * Getter dell'attributo fl_trasporto
 */
public java.lang.Boolean getFl_trasporto() {
	return fl_trasporto;
}
/* 
 * Getter dell'attributo limite_max_spesa
 */
public java.math.BigDecimal getLimite_max_spesa() {
	return limite_max_spesa;
}
/* 
 * Getter dell'attributo percentuale_maggiorazione
 */
public java.math.BigDecimal getPercentuale_maggiorazione() {
	return percentuale_maggiorazione;
}
/* 
 * Setter dell'attributo cd_divisa
 */
public void setCd_divisa(java.lang.String cd_divisa) {
	this.cd_divisa = cd_divisa;
}
/* 
 * Setter dell'attributo ds_ti_spesa
 */
public void setDs_ti_spesa(java.lang.String ds_ti_spesa) {
	this.ds_ti_spesa = ds_ti_spesa;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo fl_alloggio
 */
public void setFl_alloggio(java.lang.Boolean fl_alloggio) {
	this.fl_alloggio = fl_alloggio;
}
/* 
 * Setter dell'attributo fl_giustificativo_richiesto
 */
public void setFl_giustificativo_richiesto(java.lang.Boolean fl_giustificativo_richiesto) {
	this.fl_giustificativo_richiesto = fl_giustificativo_richiesto;
}
/* 
 * Setter dell'attributo fl_pasto
 */
public void setFl_pasto(java.lang.Boolean fl_pasto) {
	this.fl_pasto = fl_pasto;
}
/* 
 * Setter dell'attributo fl_rimborso_km
 */
public void setFl_rimborso_km(java.lang.Boolean fl_rimborso_km) {
	this.fl_rimborso_km = fl_rimborso_km;
}
/* 
 * Setter dell'attributo fl_trasporto
 */
public void setFl_trasporto(java.lang.Boolean fl_trasporto) {
	this.fl_trasporto = fl_trasporto;
}
/* 
 * Setter dell'attributo limite_max_spesa
 */
public void setLimite_max_spesa(java.math.BigDecimal limite_max_spesa) {
	this.limite_max_spesa = limite_max_spesa;
}
/* 
 * Setter dell'attributo percentuale_maggiorazione
 */
public void setPercentuale_maggiorazione(java.math.BigDecimal percentuale_maggiorazione) {
	this.percentuale_maggiorazione = percentuale_maggiorazione;
}
public java.lang.Boolean getFl_ammissibile_con_rimborso() {
	return fl_ammissibile_con_rimborso;
}
public void setFl_ammissibile_con_rimborso(
		java.lang.Boolean fl_ammissibile_con_rimborso) {
	this.fl_ammissibile_con_rimborso = fl_ammissibile_con_rimborso;
}
}
