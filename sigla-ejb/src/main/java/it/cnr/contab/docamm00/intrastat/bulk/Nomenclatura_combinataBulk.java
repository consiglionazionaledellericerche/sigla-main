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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.CRUDBP;

public class Nomenclatura_combinataBulk extends Nomenclatura_combinataBase {

public Nomenclatura_combinataBulk() {
	super();
}
public Nomenclatura_combinataBulk(java.lang.Long id_nomenclatura_combinata) {
	super(id_nomenclatura_combinata);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 11:42:49 AM)
 * @return boolean
 */
public boolean isROnomenclatura_combinata() {
	return getCrudStatus() == OggettoBulk.NORMAL;
}
@Override
public OggettoBulk initializeForInsert(CRUDBP crudbp,
		ActionContext actioncontext) {
	setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
	setEsercizio_inizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
	setEsercizio_fine(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
	return super.initializeForInsert(crudbp, actioncontext);
}
	public OggettoBulk initializeForSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForSearch(crudbp, actioncontext);
	}
}
