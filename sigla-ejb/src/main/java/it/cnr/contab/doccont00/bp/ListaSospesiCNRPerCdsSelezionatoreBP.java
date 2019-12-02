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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public class ListaSospesiCNRPerCdsSelezionatoreBP extends SelezionatoreListaBP {
    private boolean isAbilitatoReversaleIncasso;

    @Override
    protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
        super.init(config, actioncontext);
        try {
            isAbilitatoReversaleIncasso = UtenteBulk.isAbilitatoReversaleIncasso(actioncontext.getUserContext());
        } catch (ComponentException|RemoteException e) {
            throw handleException(e);
        }
    }

    public boolean isSostituisciButtonHidden() {
        return true;
        /*
        return !(isAbilitatoReversaleIncasso && Optional.ofNullable(getModel())
                .filter(SospesoBulk.class::isInstance)
                .map(SospesoBulk.class::cast)
                .flatMap(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()))
                .isPresent());
         */
    }

    @Override
    public List<Button> createToolbarList() {
        final List<Button> toolbarList = super.createToolbarList();
        toolbarList.add(new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.sostituisci"));
        return toolbarList;
    }
}
