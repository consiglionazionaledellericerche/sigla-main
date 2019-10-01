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

import it.cnr.contab.compensi00.bp.EstrazioneINPSMensileBP;
import it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (02/02/2004 16.44.25)
 * @author: Gennaro Borriello
 */
public class EstrazioneINPSMensileAction extends it.cnr.jada.util.action.BulkAction {
/**
 * EstrazioneINPSMensileAction constructor comment.
 */
public EstrazioneINPSMensileAction() {
	super();
}
public Forward doElaboraINPSMensile(ActionContext context) {

    try {
        fillModel(context);
        EstrazioneINPSMensileBP bp = (EstrazioneINPSMensileBP) context.getBusinessProcess();

        bp.doElaboraINPSMensile(context);        
		
        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
}
