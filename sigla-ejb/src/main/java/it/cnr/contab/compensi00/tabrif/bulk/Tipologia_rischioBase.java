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

public class Tipologia_rischioBase extends Tipologia_rischioKey implements Keyed {
	// ALIQUOTA_ENTE DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota_ente;

	// ALIQUOTA_PERCIPIENTE DECIMAL(10,6) NOT NULL
	private java.math.BigDecimal aliquota_percipiente;

	// DS_TIPOLOGIA_RISCHIO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipologia_rischio;

	// DT_FINE_VALIDITA TIMESTAMP
	private java.sql.Timestamp dt_fine_validita;

public Tipologia_rischioBase() {
	super();
}
public Tipologia_rischioBase(java.lang.String cd_tipologia_rischio,java.sql.Timestamp dt_inizio_validita) {
	super(cd_tipologia_rischio,dt_inizio_validita);
}
/* 
 * Getter dell'attributo aliquota_ente
 */
public java.math.BigDecimal getAliquota_ente() {
	return aliquota_ente;
}
/* 
 * Getter dell'attributo aliquota_percipiente
 */
public java.math.BigDecimal getAliquota_percipiente() {
	return aliquota_percipiente;
}
/* 
 * Getter dell'attributo ds_tipologia_rischio
 */
public java.lang.String getDs_tipologia_rischio() {
	return ds_tipologia_rischio;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Setter dell'attributo aliquota_ente
 */
public void setAliquota_ente(java.math.BigDecimal aliquota_ente) {
	this.aliquota_ente = aliquota_ente;
}
/* 
 * Setter dell'attributo aliquota_percipiente
 */
public void setAliquota_percipiente(java.math.BigDecimal aliquota_percipiente) {
	this.aliquota_percipiente = aliquota_percipiente;
}
/* 
 * Setter dell'attributo ds_tipologia_rischio
 */
public void setDs_tipologia_rischio(java.lang.String ds_tipologia_rischio) {
	this.ds_tipologia_rischio = ds_tipologia_rischio;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
}
