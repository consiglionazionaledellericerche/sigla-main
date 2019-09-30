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
 * Definisce una relazione fra un Utente, una Unita' Organizzativa e un Ruolo
 *	
 */

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_ruoloBulk extends Utente_unita_ruoloBase {
	private RuoloBulk ruolo;

public Utente_unita_ruoloBulk() {
	super();
}
public Utente_unita_ruoloBulk(java.lang.String cd_ruolo,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super(cd_ruolo,cd_unita_organizzativa,cd_utente);
}
public java.lang.String getCd_ruolo() {
	it.cnr.contab.utenze00.bulk.RuoloBulk ruolo = this.getRuolo();
	if (ruolo == null)
		return null;
	return ruolo.getCd_ruolo();
}
/**
 * @return it.cnr.contab.utenze00.bulk.RuoloBulk
 */
public RuoloBulk getRuolo() {
	return ruolo;
}
public void setCd_ruolo(java.lang.String cd_ruolo) {
	this.getRuolo().setCd_ruolo(cd_ruolo);
}
/**
 * @param newRuolo it.cnr.contab.utenze00.bulk.RuoloBulk
 */
public void setRuolo(RuoloBulk newRuolo) {
	ruolo = newRuolo;
}
}
