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

package it.cnr.contab.doccont00.consultazioni.action;

import it.cnr.contab.doccont00.consultazioni.bp.ConsNonAcquisitiBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaAction;

import java.util.Optional;

public class ConsNonAcquisitiAction extends SelezionatoreListaAction {
    public Forward doElimina(ActionContext actioncontext) throws BusinessProcessException {
        try {
            final ConsNonAcquisitiBP consNonAcquisitiBP = Optional.ofNullable(actioncontext.getBusinessProcess())
                    .filter(ConsNonAcquisitiBP.class::isInstance)
                    .map(ConsNonAcquisitiBP.class::cast)
                    .orElseThrow(() -> new ApplicationException("Business process non trovato!"));
            fillModel(actioncontext);
            consNonAcquisitiBP.setSelection(actioncontext);
            consNonAcquisitiBP.annulla(actioncontext);
            consNonAcquisitiBP.refresh(actioncontext);
            consNonAcquisitiBP.setMessage(ConsNonAcquisitiBP.INFO_MESSAGE, "Annullamento effettuato.");
        } catch (ApplicationException | FillException e) {
            return handleException(actioncontext, e);
        }
        return actioncontext.findDefaultForward();
    }
}
