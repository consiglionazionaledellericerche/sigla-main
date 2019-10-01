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

package it.cnr.contab.fondiric00.bp;

import it.cnr.contab.fondiric00.core.bulk.*;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class TestataFondiRicercaBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private SimpleDetailCRUDController crudDettagli = new SimpleDetailCRUDController( "Dettagli", Fondo_assegnatarioBulk.class, "dettagli", this);

/**
 * TestataFondiRicercaBP constructor comment.
 */
public TestataFondiRicercaBP() {
	super();
}
/**
 * TestataFondiRicercaBP constructor comment.
 * @param function java.lang.String
 */
public TestataFondiRicercaBP(String function) {
	super(function);
}
	public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudDettagli() {
		return crudDettagli;
	}

protected void resetTabs(it.cnr.jada.action.ActionContext context) {
	setTab("tab","tabTestata");
}
}
