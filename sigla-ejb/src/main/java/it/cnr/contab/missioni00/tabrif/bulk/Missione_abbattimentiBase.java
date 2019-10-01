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

public class Missione_abbattimentiBase extends Missione_abbattimentiKey implements Keyed {
	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// PERCENTUALE_ABBATTIMENTO DECIMAL(9,6) NOT NULL
	private java.math.BigDecimal percentuale_abbattimento;

public Missione_abbattimentiBase() {
	super();
}
public Missione_abbattimentiBase(java.sql.Timestamp dt_inizio_validita,java.lang.String durata_ore,java.lang.Boolean fl_alloggio,java.lang.Boolean fl_alloggio_gratuito,java.lang.Boolean fl_navigazione,java.lang.Boolean fl_pasto,java.lang.Boolean fl_trasporto,java.lang.Boolean fl_vitto_alloggio_gratuito,java.lang.Boolean fl_vitto_gratuito,java.lang.Long pg_nazione,java.lang.Long pg_rif_inquadramento,java.lang.String ti_area_geografica) {
	super(dt_inizio_validita,durata_ore,fl_alloggio,fl_alloggio_gratuito,fl_navigazione,fl_pasto,fl_trasporto,fl_vitto_alloggio_gratuito,fl_vitto_gratuito,pg_nazione,pg_rif_inquadramento,ti_area_geografica);
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
 * Getter dell'attributo percentuale_abbattimento
 */
public java.math.BigDecimal getPercentuale_abbattimento() {
	return percentuale_abbattimento;
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
 * Setter dell'attributo percentuale_abbattimento
 */
public void setPercentuale_abbattimento(java.math.BigDecimal percentuale_abbattimento) {
	this.percentuale_abbattimento = percentuale_abbattimento;
}
}
