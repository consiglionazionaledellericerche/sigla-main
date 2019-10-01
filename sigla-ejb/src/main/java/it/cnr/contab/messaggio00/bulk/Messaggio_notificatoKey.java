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

package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_notificatoKey extends OggettoBulk implements KeyedPersistent {
	// CD_UTENTE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_utente;

public Messaggio_notificatoKey() {
	super();
}
public Messaggio_notificatoKey(java.lang.String cd_utente) {
	super();
	this.cd_utente = cd_utente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Messaggio_notificatoKey)) return false;
	Messaggio_notificatoKey k = (Messaggio_notificatoKey)o;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_utente
 */
public java.lang.String getCd_utente() {
	return cd_utente;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_utente());
}
/* 
 * Setter dell'attributo cd_utente
 */
public void setCd_utente(java.lang.String cd_utente) {
	this.cd_utente = cd_utente;
}
}
