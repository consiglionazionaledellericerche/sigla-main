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

package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_sing_contoBase extends V_stm_paramin_sing_contoKey  implements Persistent {
	// CD_CDS VARCHAR(200)
	private java.lang.String cd_cds;

	// CHIAVE VARCHAR(100) NOT NULL
	private java.lang.String chiave;

	// SEQUENZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal sequenza;

	// TIPO CHAR(1) NOT NULL
	private java.lang.String tipo;

	// TI_COMPETENZA_RESIDUO VARCHAR(200)
	private java.lang.String ti_competenza_residuo;
public V_stm_paramin_sing_contoBase() {
	super();
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo chiave
 */
public java.lang.String getChiave() {
	return chiave;
}
/* 
 * Getter dell'attributo sequenza
 */
public java.math.BigDecimal getSequenza() {
	return sequenza;
}
/* 
 * Getter dell'attributo ti_competenza_residuo
 */
public java.lang.String getTi_competenza_residuo() {
	return ti_competenza_residuo;
}
/* 
 * Getter dell'attributo tipo
 */
public java.lang.String getTipo() {
	return tipo;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo chiave
 */
public void setChiave(java.lang.String chiave) {
	this.chiave = chiave;
}
/* 
 * Setter dell'attributo sequenza
 */
public void setSequenza(java.math.BigDecimal sequenza) {
	this.sequenza = sequenza;
}
/* 
 * Setter dell'attributo ti_competenza_residuo
 */
public void setTi_competenza_residuo(java.lang.String ti_competenza_residuo) {
	this.ti_competenza_residuo = ti_competenza_residuo;
}
/* 
 * Setter dell'attributo tipo
 */
public void setTipo(java.lang.String tipo) {
	this.tipo = tipo;
}
}
