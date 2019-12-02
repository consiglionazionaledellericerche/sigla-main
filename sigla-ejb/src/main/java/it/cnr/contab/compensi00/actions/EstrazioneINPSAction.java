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

import it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSBulk;
import it.cnr.contab.compensi00.bp.EstrazioneINPSBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (16/03/2004 15.30.24)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSAction extends it.cnr.jada.util.action.BulkAction {
/**
 * EstrazioneINPSAction constructor comment.
 */
public EstrazioneINPSAction() {
	super();
}
public Forward doElaboraINPS(ActionContext context) {

    try {
        fillModel(context);
        EstrazioneINPSBP bp = (EstrazioneINPSBP) context.getBusinessProcess();

        bp.doElaboraINPS(context);        
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
