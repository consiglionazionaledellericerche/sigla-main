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

package it.cnr.contab.util00.action;

import it.cnr.contab.utenze00.bulk.AssBpAccessoBulk;
import it.cnr.contab.util00.bulk.HelpBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.util.Optional;

public class CRUDHelpAction extends CRUDAction {
    /**
     * Gestione della richiesta di consultare gli accessi
     *
     * @param context L'ActionContext della richiesta
     * @return Il Forward alla pagina di risposta
     */
    public Forward doConsultaAccessi(ActionContext context) {
        try {
            fillModel(context);
            SelezionatoreListaBP selezionatoreAssBpAccessoBP = (SelezionatoreListaBP) context.createBusinessProcess("SelezionatoreAssBpAccessoBP");
            context.addHookForward("seleziona", this, "doRiportaSelezioneAccesso");
            return context.addBusinessProcess(selezionatoreAssBpAccessoBP);
        } catch (Throwable ex) {
            return handleException(context, ex);
        }
    }

    /**
     * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
     */
    public Forward doRiportaSelezioneAccesso(ActionContext context) throws java.rmi.RemoteException {
        CRUDBP bp = (CRUDBP) context.getBusinessProcess();
        Optional.ofNullable(context.getCaller())
                .map(HookForward.class::cast)
                .map(hookForward -> hookForward.getParameter("focusedElement"))
                .filter(AssBpAccessoBulk.class::isInstance)
                .map(AssBpAccessoBulk.class::cast)
                .ifPresent(assBpAccessoBulk -> {
                    Optional.ofNullable(bp.getModel())
                            .map(HelpBulk.class::cast)
                            .ifPresent(helpBulk -> {
                                helpBulk.setBpName(assBpAccessoBulk.getBusinessProcess());
                            });
                });
        return context.findDefaultForward();
    }
}
