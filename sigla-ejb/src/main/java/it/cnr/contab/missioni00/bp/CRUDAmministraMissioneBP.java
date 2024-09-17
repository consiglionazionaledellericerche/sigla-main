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

package it.cnr.contab.missioni00.bp;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.comp.ApplicationException;

public class CRUDAmministraMissioneBP extends CRUDMissioneBP{

    public CRUDAmministraMissioneBP() {
    }

    public CRUDAmministraMissioneBP(String function) {
        super(function);
    }
    @Override
    public boolean isAmministra() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isInputReadonly() {
        return Boolean.FALSE;
    }

    @Override
    public boolean isInputReadonlyFieldName(String fieldName) {
        return Boolean.FALSE;
    }

    @Override
    public void verificoUnitaENTE(ActionContext context) throws ApplicationException {
        it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = null;
        unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
    }
}
