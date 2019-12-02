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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Carico_familiare_anagBase extends Carico_familiare_anagKey implements Keyed {
	// CODICE_FISCALE VARCHAR(20)
	private java.lang.String codice_fiscale;

	// DT_FINE_FIGLIO_HA_TREANNI TIMESTAMP
	private java.sql.Timestamp dt_fine_figlio_ha_treanni;

	// DT_FIN_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_validita;

	// DT_NASCITA_FIGLIO TIMESTAMP
	private java.sql.Timestamp dt_nascita_figlio;

	// FL_HANDICAP CHAR(1) NOT NULL
	private java.lang.Boolean fl_handicap;

	// FL_PRIMO_FIGLIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_primo_figlio;

	// PRC_CARICO DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal prc_carico;

	// TI_PERSONA CHAR(1) NOT NULL
	private java.lang.String ti_persona;

	// FL_PRIMO_FIGLIO_MANCA_CON CHAR(1) NOT NULL
	private java.lang.Boolean fl_primo_figlio_manca_con;
	// CODICE_FISCALE_ALTRO_GEN VARCHAR(20)
	private java.lang.String codice_fiscale_altro_gen;
public Carico_familiare_anagBase() {
	super();
}
public Carico_familiare_anagBase(java.lang.Integer cd_anag,java.sql.Timestamp dt_ini_validita,java.lang.Long pg_carico_anag) {
	super(cd_anag,dt_ini_validita,pg_carico_anag);
}
/* 
 * Getter dell'attributo codice_fiscale
 */
public java.lang.String getCodice_fiscale() {
	return codice_fiscale;
}
/* 
 * Getter dell'attributo dt_fin_validita
 */
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
/* 
 * Getter dell'attributo dt_fine_figlio_ha_treanni
 */
public java.sql.Timestamp getDt_fine_figlio_ha_treanni() {
	return dt_fine_figlio_ha_treanni;
}
/* 
 * Getter dell'attributo dt_nascita_figlio
 */
public java.sql.Timestamp getDt_nascita_figlio() {
	return dt_nascita_figlio;
}
/* 
 * Getter dell'attributo fl_handicap
 */
public java.lang.Boolean getFl_handicap() {
	return fl_handicap;
}
/* 
 * Getter dell'attributo fl_primo_figlio
 */
public java.lang.Boolean getFl_primo_figlio() {
	return fl_primo_figlio;
}
/* 
 * Getter dell'attributo prc_carico
 */
public java.math.BigDecimal getPrc_carico() {
	return prc_carico;
}
/* 
 * Getter dell'attributo ti_persona
 */
public java.lang.String getTi_persona() {
	return ti_persona;
}
/* 
 * Setter dell'attributo codice_fiscale
 */
public void setCodice_fiscale(java.lang.String codice_fiscale) {
	this.codice_fiscale = codice_fiscale;
}
/* 
 * Setter dell'attributo dt_fin_validita
 */
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
/* 
 * Setter dell'attributo dt_fine_figlio_ha_treanni
 */
public void setDt_fine_figlio_ha_treanni(java.sql.Timestamp dt_fine_figlio_ha_treanni) {
	this.dt_fine_figlio_ha_treanni = dt_fine_figlio_ha_treanni;
}
/* 
 * Setter dell'attributo dt_nascita_figlio
 */
public void setDt_nascita_figlio(java.sql.Timestamp dt_nascita_figlio) {
	this.dt_nascita_figlio = dt_nascita_figlio;
}
/* 
 * Setter dell'attributo fl_handicap
 */
public void setFl_handicap(java.lang.Boolean fl_handicap) {
	this.fl_handicap = fl_handicap;
}
/* 
 * Setter dell'attributo fl_primo_figlio
 */
public void setFl_primo_figlio(java.lang.Boolean fl_primo_figlio) {
	this.fl_primo_figlio = fl_primo_figlio;
}
/* 
 * Setter dell'attributo prc_carico
 */
public void setPrc_carico(java.math.BigDecimal prc_carico) {
	this.prc_carico = prc_carico;
}
/* 
 * Setter dell'attributo ti_persona
 */
public void setTi_persona(java.lang.String ti_persona) {
	this.ti_persona = ti_persona;
}
	/**
	 * @return
	 */
	public java.lang.Boolean getFl_primo_figlio_manca_con() {
		return fl_primo_figlio_manca_con;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_primo_figlio_manca_con(java.lang.Boolean boolean1) {
		fl_primo_figlio_manca_con = boolean1;
	}
	public java.lang.String getCodice_fiscale_altro_gen() {
		return codice_fiscale_altro_gen;
	}
	public void setCodice_fiscale_altro_gen(
			java.lang.String codice_fiscale_altro_gen) {
		this.codice_fiscale_altro_gen = codice_fiscale_altro_gen;
	}

}
