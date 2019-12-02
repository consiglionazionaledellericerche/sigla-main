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

package it.cnr.contab.doccont00.action;

import it.cnr.contab.doccont00.bp.CRUDMandatoBP;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;

import java.util.Optional;

public class ListaSospesiCNRPerCdsSelezionatoreAction extends SelezionatoreListaAction {
    /**
     * Crea un nuovo Mandato in sostituzione al Mandato di riaccredito presente sul sospeso
     *
     * @param actionContext
     * @return
     */
    public Forward doSostituisciMandato(ActionContext actionContext) throws BusinessProcessException, ApplicationException {
        final MandatoBulk mandatoBulk = Optional.ofNullable(actionContext.getBusinessProcess())
                .filter(SelezionatoreListaBP.class::isInstance)
                .map(SelezionatoreListaBP.class::cast)
                .flatMap(selezionatoreListaBP -> Optional.ofNullable(selezionatoreListaBP.getModel()))
                .filter(SospesoBulk.class::isInstance)
                .map(SospesoBulk.class::cast)
                .flatMap(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()))
                .orElseThrow(() -> new ApplicationException("Sul sospeso selezionato non è presente il Mandato!"));
        if (Optional.ofNullable(mandatoBulk.getStatoVarSos())
                .map(m -> m.equalsIgnoreCase(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value())).orElse(Boolean.FALSE)) {
            throw new ApplicationException("Il Mandato già risulta Annullato per Sostituzione");
        }
        CRUDMandatoBP mandatoBP = (CRUDMandatoBP) actionContext.createBusinessProcess("CRUDMandatoBP", new Object[]{"MRSW", mandatoBulk});
        actionContext.addHookForward("bringback", this, "doBringBackCreaMandatoRiaccredito");
        return actionContext.addBusinessProcess(mandatoBP);
    }

    public Forward doBringBackCreaMandatoRiaccredito(ActionContext context) throws BusinessProcessException {
        try {
            HookForward hook = (HookForward) context.getCaller();
            MandatoBulk mandatoBulk = (MandatoBulk) hook.getParameter("bringback");
            return context.findDefaultForward();
        } catch (Exception e) {
            return handleException(context, e);
        }
    }
}
