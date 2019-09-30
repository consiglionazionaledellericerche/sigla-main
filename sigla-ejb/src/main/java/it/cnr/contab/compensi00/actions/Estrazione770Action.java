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

package it.cnr.contab.compensi00.actions;

import it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk;
import it.cnr.contab.compensi00.bp.Estrazione770BP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Creation date: (24/09/2004)
 * @author: Aurelio D'Amico
 * @version: 1.0
 */
public class Estrazione770Action extends it.cnr.jada.util.action.BulkAction {
/**
 * Estrazione770Action constructor comment.
 */
public Estrazione770Action() {
	super();
}
public Forward doElabora770(ActionContext context) {

	try {
		fillModel(context);
		Estrazione770BP bp = (Estrazione770BP) context.getBusinessProcess();

		bp.doElabora770(context);        
		
		return context.findDefaultForward();
	} catch (Throwable t) {
		return handleException(context, t);
	}
}
}
