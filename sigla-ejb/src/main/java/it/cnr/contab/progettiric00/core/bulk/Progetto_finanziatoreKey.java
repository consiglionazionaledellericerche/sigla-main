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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_finanziatoreKey extends OggettoBulk implements KeyedPersistent {
	// CD_FINANZIATORE_TERZO   NUMBER (8)    NOT NULL (PK)
	private java.lang.Integer cd_finanziatore_terzo;

	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

public Progetto_finanziatoreKey() {
	super();
}
public Progetto_finanziatoreKey(java.lang.Integer pg_progetto,java.lang.Integer cd_finanziatore_terzo) {
	super();
	this.pg_progetto = pg_progetto;
	this.cd_finanziatore_terzo = cd_finanziatore_terzo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Progetto_finanziatoreKey)) return false;
	Progetto_finanziatoreKey k = (Progetto_finanziatoreKey)o;
	if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
	if(!compareKey(getCd_finanziatore_terzo(),k.getCd_finanziatore_terzo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
/* 
 * Getter dell'attributo cd_finanziatore_terzo
 */
public java.lang.Integer getCd_finanziatore_terzo() {
	return cd_finanziatore_terzo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_progetto())+
		calculateKeyHashCode(getCd_finanziatore_terzo());
}
/* 
 * Setter dell'attributo cd_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}
/* 
 * Setter dell'attributo cd_finanziatore_terzo
 */
public void setCd_finanziatore_terzo(java.lang.Integer cd_finanziatore_terzo) {
	this.cd_finanziatore_terzo = cd_finanziatore_terzo;
}
}