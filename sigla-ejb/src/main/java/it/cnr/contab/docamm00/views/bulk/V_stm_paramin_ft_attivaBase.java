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

package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_ft_attivaBase extends V_stm_paramin_ft_attivaKey implements Persistent {
	// CD_CDS VARCHAR(200)
	private java.lang.String cd_cds;

	// CD_UO VARCHAR(200)
	private java.lang.String cd_uo;

	// DESCRIZIONE VARCHAR(100)
	private java.lang.String descrizione;

	// ESERCIZIO DECIMAL(20,3)
	private java.math.BigDecimal esercizio;

	// GRUPPO VARCHAR(100) NOT NULL
	private java.lang.String gruppo;

	// PG_FATTURA_ATTIVA DECIMAL(20,3)
	private java.math.BigDecimal pg_fattura_attiva;

	// SEQUENZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal sequenza;

	// TIPOLOGIA_RIGA CHAR(1) NOT NULL
	private java.lang.String tipologia_riga;

public V_stm_paramin_ft_attivaBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_uo
 */
public java.lang.String getCd_uo() {
	return cd_uo;
}
/* 
 * Getter dell'attributo descrizione
 */
public java.lang.String getDescrizione() {
	return descrizione;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.math.BigDecimal getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo gruppo
 */
public java.lang.String getGruppo() {
	return gruppo;
}
/* 
 * Getter dell'attributo pg_fattura_attiva
 */
public java.math.BigDecimal getPg_fattura_attiva() {
	return pg_fattura_attiva;
}
/* 
 * Getter dell'attributo sequenza
 */
public java.math.BigDecimal getSequenza() {
	return sequenza;
}
/* 
 * Getter dell'attributo tipologia_riga
 */
public java.lang.String getTipologia_riga() {
	return tipologia_riga;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_uo
 */
public void setCd_uo(java.lang.String cd_uo) {
	this.cd_uo = cd_uo;
}
/* 
 * Setter dell'attributo descrizione
 */
public void setDescrizione(java.lang.String descrizione) {
	this.descrizione = descrizione;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.math.BigDecimal esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo gruppo
 */
public void setGruppo(java.lang.String gruppo) {
	this.gruppo = gruppo;
}
/* 
 * Setter dell'attributo pg_fattura_attiva
 */
public void setPg_fattura_attiva(java.math.BigDecimal pg_fattura_attiva) {
	this.pg_fattura_attiva = pg_fattura_attiva;
}
/* 
 * Setter dell'attributo sequenza
 */
public void setSequenza(java.math.BigDecimal sequenza) {
	this.sequenza = sequenza;
}
/* 
 * Setter dell'attributo tipologia_riga
 */
public void setTipologia_riga(java.lang.String tipologia_riga) {
	this.tipologia_riga = tipologia_riga;
}
}
