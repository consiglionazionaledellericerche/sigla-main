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

public class Condizione_consegnaBulk extends Condizione_consegnaBase {

public Condizione_consegnaBulk() {
	super();
}
public Condizione_consegnaBulk(java.lang.String cd_incoterm,java.lang.Integer esercizio) {
	super(cd_incoterm,esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (5/17/2002 11:42:49 AM)
 * @return boolean
 */
public boolean isROcondizione_consegna() {
	return getCrudStatus() == OggettoBulk.NORMAL;
}
@Override
public OggettoBulk initializeForInsert(CRUDBP crudbp,
		ActionContext actioncontext) {
	setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
	return super.initializeForInsert(crudbp, actioncontext);
}
@Override
	public OggettoBulk initializeForSearch(CRUDBP crudbp,
			ActionContext actioncontext) {
		setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(actioncontext.getUserContext()));
		return super.initializeForSearch(crudbp, actioncontext);
	}
public String getCd_ds_condizione_consegna() {

	String result = "";
	result += ((getCd_incoterm() == null) ? "n.n." : getCd_incoterm()) + " - ";
	result += ((getDs_condizione_consegna() == null) ? "n.n." : getDs_condizione_consegna());
	return result;
}
}
