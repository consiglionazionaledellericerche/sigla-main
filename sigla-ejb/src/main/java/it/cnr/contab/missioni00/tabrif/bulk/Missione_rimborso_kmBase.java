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

public class Missione_rimborso_kmBase extends Missione_rimborso_kmKey implements Keyed {
	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// INDENNITA_CHILOMETRICA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal indennita_chilometrica;

public Missione_rimborso_kmBase() {
	super();
}
public Missione_rimborso_kmBase(java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_nazione,java.lang.String ti_area_geografica,java.lang.String ti_auto) {
	super(dt_inizio_validita,pg_nazione,ti_area_geografica,ti_auto);
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
 * Getter dell'attributo indennita_chilometrica
 */
public java.math.BigDecimal getIndennita_chilometrica() {
	return indennita_chilometrica;
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
 * Setter dell'attributo indennita_chilometrica
 */
public void setIndennita_chilometrica(java.math.BigDecimal indennita_chilometrica) {
	this.indennita_chilometrica = indennita_chilometrica;
}
}
