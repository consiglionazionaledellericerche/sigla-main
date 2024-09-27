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

package it.cnr.contab.util00.action;

import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormBP;

import java.util.Optional;

public class AllegatiCRUDAction extends CRUDAction {

    public Forward doArchiviaAllegati(ActionContext actioncontext) throws BusinessProcessException {
        final Optional<AllegatiCRUDBP> allegatiCRUDBP = Optional.ofNullable(getBusinessProcess(actioncontext))
                .filter(AllegatiCRUDBP.class::isInstance)
                .map(AllegatiCRUDBP.class::cast);
        if (allegatiCRUDBP.isPresent()) {
            try {
                allegatiCRUDBP.get().fillModel(actioncontext);
                allegatiCRUDBP.get().validate(actioncontext);
                allegatiCRUDBP.get().archiviaAllegati(actioncontext);
                allegatiCRUDBP.get().setMessage(FormBP.INFO_MESSAGE, "Operazione effettuata con successo.");
            } catch (ApplicationException | FillException | ValidationException e) {
                return handleException(actioncontext, e);
            }
        }
        return actioncontext.findDefaultForward();
    }
}
