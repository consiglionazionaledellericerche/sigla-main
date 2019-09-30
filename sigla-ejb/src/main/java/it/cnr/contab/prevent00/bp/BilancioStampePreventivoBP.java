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

package it.cnr.contab.prevent00.bp;
/**
 * Business Process di gestione delle stampe del bilancio preventivo
 */

public class BilancioStampePreventivoBP extends it.cnr.jada.util.action.BulkBP {
public BilancioStampePreventivoBP() {
	super();
}

/**
 * @see it.cnr.jada.util.action.BulkBP
 */
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
}