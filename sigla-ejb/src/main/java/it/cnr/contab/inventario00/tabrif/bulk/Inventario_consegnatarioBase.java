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

package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_consegnatarioBase extends Inventario_consegnatarioKey implements Keyed {
	// CD_CONSEGNATARIO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_consegnatario;

	// CD_DELEGATO DECIMAL(8,0)
	private java.lang.Integer cd_delegato;

	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

public Inventario_consegnatarioBase() {
	super();
}
public Inventario_consegnatarioBase(java.sql.Timestamp dt_inizio_validita,java.lang.Long pg_inventario) {
	super(dt_inizio_validita,pg_inventario);
}
/* 
 * Getter dell'attributo cd_consegnatario
 */
public java.lang.Integer getCd_consegnatario() {
	return cd_consegnatario;
}
/* 
 * Getter dell'attributo cd_delegato
 */
public java.lang.Integer getCd_delegato() {
	return cd_delegato;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Setter dell'attributo cd_consegnatario
 */
public void setCd_consegnatario(java.lang.Integer cd_consegnatario) {
	this.cd_consegnatario = cd_consegnatario;
}
/* 
 * Setter dell'attributo cd_delegato
 */
public void setCd_delegato(java.lang.Integer cd_delegato) {
	this.cd_delegato = cd_delegato;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
}
