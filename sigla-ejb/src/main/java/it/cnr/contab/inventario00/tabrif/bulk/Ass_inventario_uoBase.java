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

public class Ass_inventario_uoBase extends Ass_inventario_uoKey implements Keyed {
	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// FL_RESPONSABILE CHAR(1) NOT NULL
	private java.lang.Boolean fl_responsabile;

public Ass_inventario_uoBase() {
	super();
}
public Ass_inventario_uoBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Long pg_inventario) {
	super(cd_cds,cd_unita_organizzativa,pg_inventario);
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo fl_responsabile
 */
public java.lang.Boolean getFl_responsabile() {
	return fl_responsabile;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo fl_responsabile
 */
public void setFl_responsabile(java.lang.Boolean fl_responsabile) {
	this.fl_responsabile = fl_responsabile;
}
}
