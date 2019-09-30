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

/**
 * Definisce una relazione fra un Utente, una Unita' Organizzativa e un Accesso
 *	
 */


import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_accessoBulk extends Utente_unita_accessoBase 
{
	private AccessoBulk accesso;

public Utente_unita_accessoBulk() {
	super();
}
public Utente_unita_accessoBulk(java.lang.String cd_accesso,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super(cd_accesso,cd_unita_organizzativa,cd_utente);
}
/**
 * @return it.cnr.contab.utenze00.bulk.AccessoBulk
 */
public AccessoBulk getAccesso() {
	return accesso;
}
public java.lang.String getCd_accesso() {
	it.cnr.contab.utenze00.bulk.AccessoBulk accesso = this.getAccesso();
	if (accesso == null)
		return null;
	return accesso.getCd_accesso();
}
/**
 * @param newAccesso it.cnr.contab.utenze00.bulk.AccessoBulk
 */
public void setAccesso(AccessoBulk newAccesso) {
	accesso = newAccesso;
}
public void setCd_accesso(java.lang.String cd_accesso) {
	this.getAccesso().setCd_accesso(cd_accesso);
}
}
