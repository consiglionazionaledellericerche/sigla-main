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

package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_unita_organizzativaKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_UNITA VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_tipo_unita;

public Tipo_unita_organizzativaKey() {
	super();
}
public Tipo_unita_organizzativaKey(java.lang.String cd_tipo_unita) {
	super();
	this.cd_tipo_unita = cd_tipo_unita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_unita_organizzativaKey)) return false;
	Tipo_unita_organizzativaKey k = (Tipo_unita_organizzativaKey)o;
	if(!compareKey(getCd_tipo_unita(),k.getCd_tipo_unita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_unita
 */
public java.lang.String getCd_tipo_unita() {
	return cd_tipo_unita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_unita());
}
/* 
 * Setter dell'attributo cd_tipo_unita
 */
public void setCd_tipo_unita(java.lang.String cd_tipo_unita) {
	this.cd_tipo_unita = cd_tipo_unita;
}
}
