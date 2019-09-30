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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_altra_forma_ass_inpsBase extends Codici_altra_forma_ass_inpsKey implements Keyed {
	// DS_ALTRA_ASS_PREVID_INPS VARCHAR(200) NOT NULL
	private java.lang.String ds_altra_ass_previd_inps;

	// FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

public Codici_altra_forma_ass_inpsBase() {
	super();
}
public Codici_altra_forma_ass_inpsBase(java.lang.String altra_ass_previd_inps) {
	super(altra_ass_previd_inps);
}
/* 
 * Getter dell'attributo ds_altra_ass_previd_inps
 */
public java.lang.String getDs_altra_ass_previd_inps() {
	return ds_altra_ass_previd_inps;
}
/* 
 * Getter dell'attributo fl_cancellato
 */
public java.lang.Boolean getFl_cancellato() {
	return fl_cancellato;
}
/* 
 * Setter dell'attributo ds_altra_ass_previd_inps
 */
public void setDs_altra_ass_previd_inps(java.lang.String ds_altra_ass_previd_inps) {
	this.ds_altra_ass_previd_inps = ds_altra_ass_previd_inps;
}
/* 
 * Setter dell'attributo fl_cancellato
 */
public void setFl_cancellato(java.lang.Boolean fl_cancellato) {
	this.fl_cancellato = fl_cancellato;
}
}
