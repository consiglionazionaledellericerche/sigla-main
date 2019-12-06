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
import it.cnr.contab.doccont00.bp.ListaSospesiCNRPerCdsSelezionatoreBP;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.util.enumeration.StatoVariazioneSostituzione;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.FormBP;
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
        final MandatoBulk mandatoBulk = getCurrentMandato(actionContext);
        if (Optional.ofNullable(mandatoBulk.getStatoVarSos())
                .map(m -> m.equalsIgnoreCase(StatoVariazioneSostituzione.ANNULLATO_PER_SOSTITUZIONE.value())).orElse(Boolean.FALSE)) {
            throw new ApplicationException("Il Mandato già risulta Annullato per Sostituzione");
        }
        CRUDMandatoBP mandatoBP = (CRUDMandatoBP) actionContext.createBusinessProcess("CRUDMandatoBP", new Object[]{"MRSWTh", mandatoBulk});
        actionContext.addHookForward("bringback", this, "doBringBackCreaMandatoRiaccredito");
        return actionContext.addBusinessProcess(mandatoBP);
    }

    public Forward doBringBackCreaMandatoRiaccredito(ActionContext actionContext) throws BusinessProcessException {
        try {
            final ListaSospesiCNRPerCdsSelezionatoreBP listaSospesiCNRPerCdsSelezionatoreBP = Optional.ofNullable(actionContext.getBusinessProcess())
                    .filter(ListaSospesiCNRPerCdsSelezionatoreBP.class::isInstance)
                    .map(ListaSospesiCNRPerCdsSelezionatoreBP.class::cast)
                    .orElseThrow(() -> new ApplicationException("BusinessProcess non corretto!"));

            HookForward hook = (HookForward) actionContext.getCaller();
            final MandatoBulk mandatoBulk = getCurrentMandato(actionContext);
            MandatoBulk mandatoNew = (MandatoBulk) hook.getParameter("bringback");
            if (Optional.ofNullable(mandatoNew).isPresent()) {
                if (!Optional.ofNullable(mandatoNew.getDs_mandato()).equals(Optional.ofNullable(mandatoBulk.getDs_mandato()))) {
                    throw new ApplicationException("Causale di Pagamento deve coincidere con quella del Mandato originario!");
                }
                listaSospesiCNRPerCdsSelezionatoreBP.confermaMandato(actionContext.getUserContext(), mandatoBulk, mandatoNew);
                listaSospesiCNRPerCdsSelezionatoreBP.refresh(actionContext);
                listaSospesiCNRPerCdsSelezionatoreBP.setModel(actionContext, null);
                setMessage(actionContext, FormBP.INFO_MESSAGE, "Operazione Effettuata");
            }
            return actionContext.findDefaultForward();
        } catch (Exception e) {
            return handleException(actionContext, e);
        }
    }

    private MandatoBulk getCurrentMandato(ActionContext actionContext) throws ApplicationException{
        return Optional.ofNullable(actionContext.getBusinessProcess())
                .filter(SelezionatoreListaBP.class::isInstance)
                .map(SelezionatoreListaBP.class::cast)
                .flatMap(selezionatoreListaBP -> Optional.ofNullable(selezionatoreListaBP.getModel()))
                .filter(SospesoBulk.class::isInstance)
                .map(SospesoBulk.class::cast)
                .flatMap(sospesoBulk -> Optional.ofNullable(sospesoBulk.getMandatoRiaccredito()))
                .orElseThrow(() -> new ApplicationException("Sul sospeso selezionato non è presente il Mandato!"));
    }
}
