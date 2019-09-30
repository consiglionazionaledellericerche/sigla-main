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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ruolo_accessoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ACCESSO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_accesso;

	// CD_RUOLO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_ruolo;

public Ruolo_accessoKey() {
	super();
}
public Ruolo_accessoKey(java.lang.String cd_accesso,java.lang.String cd_ruolo) {
	super();
	this.cd_accesso = cd_accesso;
	this.cd_ruolo = cd_ruolo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ruolo_accessoKey)) return false;
	Ruolo_accessoKey k = (Ruolo_accessoKey)o;
	if(!compareKey(getCd_accesso(),k.getCd_accesso())) return false;
	if(!compareKey(getCd_ruolo(),k.getCd_ruolo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_accesso
 */
public java.lang.String getCd_accesso() {
	return cd_accesso;
}
/* 
 * Getter dell'attributo cd_ruolo
 */
public java.lang.String getCd_ruolo() {
	return cd_ruolo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_accesso())+
		calculateKeyHashCode(getCd_ruolo());
}
/* 
 * Setter dell'attributo cd_accesso
 */
public void setCd_accesso(java.lang.String cd_accesso) {
	this.cd_accesso = cd_accesso;
}
/* 
 * Setter dell'attributo cd_ruolo
 */
public void setCd_ruolo(java.lang.String cd_ruolo) {
	this.cd_ruolo = cd_ruolo;
}
}
