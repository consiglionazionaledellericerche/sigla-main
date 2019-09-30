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

public class Progetto_finanziatoreBulk extends Progetto_finanziatoreBase {

	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk finanziatore;
	
public Progetto_finanziatoreBulk() {
	super();
}
public Progetto_finanziatoreBulk(java.lang.Integer pg_progetto,it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo) {
	super(pg_progetto, terzo.getCd_terzo());
	setFinanziatore(terzo);
}
public java.lang.Integer getCd_finanziatore_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk finanziatore = this.getFinanziatore();
	if (finanziatore == null)
		return null;
	return finanziatore.getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getFinanziatore() {
	return finanziatore;
}
public void setCd_finanziatore_terzo(java.lang.Integer cd_finanziatore_terzo) {
	this.getFinanziatore().setCd_terzo(cd_finanziatore_terzo);
}
/**
 * Insert the method's description here.
 * Creation date: (18/12/2001 15.18.10)
 * @param newResponsabile it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setFinanziatore(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newFinanziatore) {
	finanziatore = newFinanziatore;
}

}