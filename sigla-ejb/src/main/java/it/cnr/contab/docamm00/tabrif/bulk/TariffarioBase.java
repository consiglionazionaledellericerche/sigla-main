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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class TariffarioBase extends TariffarioKey implements Keyed {
	// CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_iva;

	// DS_TARIFFARIO VARCHAR(100) NOT NULL
	private java.lang.String ds_tariffario;

	// DT_FINE_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fine_validita;

	// IM_TARIFFARIO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_tariffario;

	// UNITA_MISURA VARCHAR(10) NOT NULL
	private java.lang.String unita_misura;

public TariffarioBase() {
	super();
}
public TariffarioBase(java.lang.String cd_tariffario,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_ini_validita) {
	super(cd_tariffario,cd_unita_organizzativa,dt_ini_validita);
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo ds_tariffario
 */
public java.lang.String getDs_tariffario() {
	return ds_tariffario;
}
/* 
 * Getter dell'attributo dt_fine_validita
 */
public java.sql.Timestamp getDt_fine_validita() {
	return dt_fine_validita;
}
/* 
 * Getter dell'attributo im_tariffario
 */
public java.math.BigDecimal getIm_tariffario() {
	return im_tariffario;
}
/* 
 * Getter dell'attributo unita_misura
 */
public java.lang.String getUnita_misura() {
	return unita_misura;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo ds_tariffario
 */
public void setDs_tariffario(java.lang.String ds_tariffario) {
	this.ds_tariffario = ds_tariffario;
}
/* 
 * Setter dell'attributo dt_fine_validita
 */
public void setDt_fine_validita(java.sql.Timestamp dt_fine_validita) {
	this.dt_fine_validita = dt_fine_validita;
}
/* 
 * Setter dell'attributo im_tariffario
 */
public void setIm_tariffario(java.math.BigDecimal im_tariffario) {
	this.im_tariffario = im_tariffario;
}
/* 
 * Setter dell'attributo unita_misura
 */
public void setUnita_misura(java.lang.String unita_misura) {
	this.unita_misura = unita_misura;
}
}
