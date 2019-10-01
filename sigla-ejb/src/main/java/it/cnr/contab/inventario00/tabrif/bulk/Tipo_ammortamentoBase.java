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

public class Tipo_ammortamentoBase extends Tipo_ammortamentoKey implements Keyed {
	// DS_TIPO_AMMORTAMENTO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_ammortamento;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// NUMERO_ANNI DECIMAL(4,0)
	private java.lang.Integer numero_anni;

	// PERC_PRIMO_ANNO DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal perc_primo_anno;

	// PERC_SUCCESSIVI DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal perc_successivi;

public Tipo_ammortamentoBase() {
	super();
}
public Tipo_ammortamentoBase(java.lang.String cd_tipo_ammortamento) {
	super(cd_tipo_ammortamento);
}
public Tipo_ammortamentoBase(java.lang.String cd_tipo_ammortamento,java.lang.String ti_ammortamento) {
	super(cd_tipo_ammortamento,ti_ammortamento);
}
/* 
 * Getter dell'attributo ds_tipo_ammortamento
 */
public java.lang.String getDs_tipo_ammortamento() {
	return ds_tipo_ammortamento;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo numero_anni
 */
public java.lang.Integer getNumero_anni() {
	return numero_anni;
}
/* 
 * Getter dell'attributo perc_primo_anno
 */
public java.math.BigDecimal getPerc_primo_anno() {
	return perc_primo_anno;
}
/* 
 * Getter dell'attributo perc_successivi
 */
public java.math.BigDecimal getPerc_successivi() {
	return perc_successivi;
}
/* 
 * Setter dell'attributo ds_tipo_ammortamento
 */
public void setDs_tipo_ammortamento(java.lang.String ds_tipo_ammortamento) {
	this.ds_tipo_ammortamento = ds_tipo_ammortamento;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo numero_anni
 */
public void setNumero_anni(java.lang.Integer numero_anni) {
	this.numero_anni = numero_anni;
}
/* 
 * Setter dell'attributo perc_primo_anno
 */
public void setPerc_primo_anno(java.math.BigDecimal perc_primo_anno) {
	this.perc_primo_anno = perc_primo_anno;
}
/* 
 * Setter dell'attributo perc_successivi
 */
public void setPerc_successivi(java.math.BigDecimal perc_successivi) {
	this.perc_successivi = perc_successivi;
}
}
