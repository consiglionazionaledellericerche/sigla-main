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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bp.StipendiCofiObbBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (10/04/2003 12.04.09)
 * @author: Gennaro Borriello
 */
public class StipendiCofiObbAction extends it.cnr.jada.util.action.CRUDAction {

public StipendiCofiObbAction() {
	super();
}
public Forward doReset(ActionContext context) {

    try {
    	StipendiCofiObbBP bp= (StipendiCofiObbBP) context.getBusinessProcess();

        bp.resetDati(context);

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
}