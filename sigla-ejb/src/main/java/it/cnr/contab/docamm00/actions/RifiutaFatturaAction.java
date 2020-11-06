/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.docamm00.actions;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.BulkBP;

public class RifiutaFatturaAction extends BulkAction {
    private static final long serialVersionUID = 1L;

    public Forward doAnnulla(ActionContext context) {
        try {
            context.closeBusinessProcess();
        } catch (BusinessProcessException e) {
            return handleException(context,e);
        }
        return context.findDefaultForward();
    }

    public Forward doConferma(ActionContext context) {
        try {
            BulkBP bp = (BulkBP)context.getBusinessProcess();
            bp.fillModel(context);
            HookForward hookforward = (HookForward)context.findForward("motivoRifiuto");
            hookforward.addParameter("motivoRifiuto", bp.getModel());
            context.closeBusinessProcess();
            return hookforward;
        }  catch (FillException e) {
            return handleException(context,e);
        } catch (BusinessProcessException e) {
            return handleException(context,e);
        }
    }

}
