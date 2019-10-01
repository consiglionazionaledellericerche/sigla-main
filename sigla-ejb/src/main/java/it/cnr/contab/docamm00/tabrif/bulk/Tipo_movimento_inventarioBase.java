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

public class Tipo_movimento_inventarioBase extends Tipo_movimento_inventarioKey implements KeyedPersistent {
	// DS_TIPO_MOVIMENTO_INVENTARIO VARCHAR(100)
	private java.lang.String ds_tipo_movimento_inventario;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// FLAG_CARICO_SCARICO VARCHAR(1)
	private java.lang.String flag_carico_scarico;

public Tipo_movimento_inventarioBase() {
	super();
}
public Tipo_movimento_inventarioBase(java.lang.String cd_tipo_movimento_inventario) {
	super(cd_tipo_movimento_inventario);
}
/* 
 * Getter dell'attributo ds_tipo_movimento_inventario
 */
public java.lang.String getDs_tipo_movimento_inventario() {
	return ds_tipo_movimento_inventario;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo flag_carico_scarico
 */
public java.lang.String getFlag_carico_scarico() {
	return flag_carico_scarico;
}
/* 
 * Setter dell'attributo ds_tipo_movimento_inventario
 */
public void setDs_tipo_movimento_inventario(java.lang.String ds_tipo_movimento_inventario) {
	this.ds_tipo_movimento_inventario = ds_tipo_movimento_inventario;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo flag_carico_scarico
 */
public void setFlag_carico_scarico(java.lang.String flag_carico_scarico) {
	this.flag_carico_scarico = flag_carico_scarico;
}
}
