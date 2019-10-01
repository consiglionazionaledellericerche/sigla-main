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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_contributo_ritenutaBase extends Tipo_contributo_ritenutaKey implements Keyed {
	// CD_CLASSIFICAZIONE_CORI CHAR(2) NOT NULL
	private java.lang.String cd_classificazione_cori;

	// CD_TRIBUTO_ERARIO VARCHAR(10)
	private java.lang.String cd_tributo_erario;

	// DS_CONTRIBUTO_RITENUTA VARCHAR(100) NOT NULL
	private java.lang.String ds_contributo_ritenuta;

	// DT_FIN_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fin_validita;

	// FL_ASSISTENZA_FISCALE CHAR(1) NOT NULL
	private java.lang.Boolean fl_assistenza_fiscale;

	// FL_SCRIVI_MONTANTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_scrivi_montanti;

	// FL_USO_IN_LORDIZZA CHAR(1) NOT NULL
	private java.lang.Boolean fl_uso_in_lordizza;

	// PG_CLASSIFICAZIONE_MONTANTI DECIMAL(10,0)
	private java.lang.Long pg_classificazione_montanti;

	// PRECISIONE DECIMAL(9,6)
	private java.math.BigDecimal precisione;

	// TI_CASSA_COMPETENZA CHAR(2) NOT NULL
	private java.lang.String ti_cassa_competenza;

	// FL_gla CHAR(1) NOT NULL
	private java.lang.Boolean fl_gla;
	
	private java.lang.String  cd_ente_prev_sti;

	// FL_SOSPENSIONE_IRPEF CHAR(1) NOT NULL
	private java.lang.Boolean fl_sospensione_irpef;
	
	private java.lang.Boolean fl_credito_irpef;
	
public Tipo_contributo_ritenutaBase() {
	super();
}
public Tipo_contributo_ritenutaBase(java.lang.String cd_contributo_ritenuta,java.sql.Timestamp dt_ini_validita) {
	super(cd_contributo_ritenuta,dt_ini_validita);
}
/* 
 * Getter dell'attributo cd_classificazione_cori
 */
public java.lang.String getCd_classificazione_cori() {
	return cd_classificazione_cori;
}
/* 
 * Getter dell'attributo cd_tributo_erario
 */
public java.lang.String getCd_tributo_erario() {
	return cd_tributo_erario;
}
/* 
 * Getter dell'attributo ds_contributo_ritenuta
 */
public java.lang.String getDs_contributo_ritenuta() {
	return ds_contributo_ritenuta;
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Getter dell'attributo fl_assistenza_fiscale
 */
public java.lang.Boolean getFl_assistenza_fiscale() {
	return fl_assistenza_fiscale;
}
/* 
 * Getter dell'attributo fl_scrivi_montanti
 */
public java.lang.Boolean getFl_scrivi_montanti() {
	return fl_scrivi_montanti;
}
/* 
 * Getter dell'attributo fl_uso_in_lordizza
 */
public java.lang.Boolean getFl_uso_in_lordizza() {
	return fl_uso_in_lordizza;
}
/* 
 * Getter dell'attributo pg_classificazione_montanti
 */
public java.lang.Long getPg_classificazione_montanti() {
	return pg_classificazione_montanti;
}
/* 
 * Getter dell'attributo precisione
 */
public java.math.BigDecimal getPrecisione() {
	return precisione;
}
/* 
 * Getter dell'attributo ti_cassa_competenza
 */
public java.lang.String getTi_cassa_competenza() {
	return ti_cassa_competenza;
}
/* 
 * Setter dell'attributo cd_classificazione_cori
 */
public void setCd_classificazione_cori(java.lang.String cd_classificazione_cori) {
	this.cd_classificazione_cori = cd_classificazione_cori;
}
/* 
 * Setter dell'attributo cd_tributo_erario
 */
public void setCd_tributo_erario(java.lang.String cd_tributo_erario) {
	this.cd_tributo_erario = cd_tributo_erario;
}
/* 
 * Setter dell'attributo ds_contributo_ritenuta
 */
public void setDs_contributo_ritenuta(java.lang.String ds_contributo_ritenuta) {
	this.ds_contributo_ritenuta = ds_contributo_ritenuta;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
/* 
 * Setter dell'attributo fl_assistenza_fiscale
 */
public void setFl_assistenza_fiscale(java.lang.Boolean fl_assistenza_fiscale) {
	this.fl_assistenza_fiscale = fl_assistenza_fiscale;
}
/* 
 * Setter dell'attributo fl_scrivi_montanti
 */
public void setFl_scrivi_montanti(java.lang.Boolean fl_scrivi_montanti) {
	this.fl_scrivi_montanti = fl_scrivi_montanti;
}
/* 
 * Setter dell'attributo fl_uso_in_lordizza
 */
public void setFl_uso_in_lordizza(java.lang.Boolean fl_uso_in_lordizza) {
	this.fl_uso_in_lordizza = fl_uso_in_lordizza;
}
/* 
 * Setter dell'attributo pg_classificazione_montanti
 */
public void setPg_classificazione_montanti(java.lang.Long pg_classificazione_montanti) {
	this.pg_classificazione_montanti = pg_classificazione_montanti;
}
/* 
 * Setter dell'attributo precisione
 */
public void setPrecisione(java.math.BigDecimal precisione) {
	this.precisione = precisione;
}
/* 
 * Setter dell'attributo ti_cassa_competenza
 */
public void setTi_cassa_competenza(java.lang.String ti_cassa_competenza) {
	this.ti_cassa_competenza = ti_cassa_competenza;
}
public java.lang.Boolean getFl_gla() {
	return fl_gla;
}
public void setFl_gla(java.lang.Boolean fl_gla) {
	this.fl_gla = fl_gla;
}
public java.lang.String getCd_ente_prev_sti() {
	return cd_ente_prev_sti;
}
public void setCd_ente_prev_sti(java.lang.String cd_ente_prev_sti) {
	this.cd_ente_prev_sti = cd_ente_prev_sti;
}
public java.lang.Boolean getFl_sospensione_irpef() {
	return fl_sospensione_irpef;
}
public void setFl_sospensione_irpef(java.lang.Boolean fl_sospensione_irpef) {
	this.fl_sospensione_irpef = fl_sospensione_irpef;
}
public java.lang.Boolean getFl_credito_irpef() {
	return fl_credito_irpef;
}
public void setFl_credito_irpef(java.lang.Boolean fl_credito_irpef) {
	this.fl_credito_irpef = fl_credito_irpef;
}
}
