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

package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_linea_attivitaKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_linea_attivita;

/* 
 * Getter dell'attributo cd_tipo_linea_attivita
 */
public java.lang.String getCd_tipo_linea_attivita() {
	return cd_tipo_linea_attivita;
}

/* 
 * Setter dell'attributo cd_tipo_linea_attivita
 */
public void setCd_tipo_linea_attivita(java.lang.String cd_tipo_linea_attivita) {
	this.cd_tipo_linea_attivita = cd_tipo_linea_attivita;
}

public Tipo_linea_attivitaKey() {
	super();
}


public Tipo_linea_attivitaKey(java.lang.String cd_tipo_linea_attivita) {
	super();
	this.cd_tipo_linea_attivita = cd_tipo_linea_attivita;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_linea_attivitaKey)) return false;
	Tipo_linea_attivitaKey k = (Tipo_linea_attivitaKey)o;
	if(!compareKey(getCd_tipo_linea_attivita(),k.getCd_tipo_linea_attivita())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_linea_attivita());
}

}
