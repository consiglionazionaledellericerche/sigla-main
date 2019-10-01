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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.docamm00.bp.ContabilizzaOrdineBP;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;
import java.util.Optional;

public class ContabilizzaOrdineAction extends SelezionatoreListaAction {

    public ContabilizzaOrdineAction() {
    }

    public Forward doCancellaFiltro(ActionContext context) {
        ContabilizzaOrdineBP bp = (ContabilizzaOrdineBP) context.getBusinessProcess();
        bp.setCondizioneCorrente(null);
        try {
            bp.setIterator(context,bp.search(context, null, new EvasioneOrdineRigaBulk()));
        } catch (RemoteException|BusinessProcessException e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }
    public Forward doRicercaLibera(ActionContext context) {
        try {
            ContabilizzaOrdineBP bp = (ContabilizzaOrdineBP) context.getBusinessProcess();
            RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP)context.createBusinessProcess("RicercaLibera");
            ricercaLiberaBP.setSearchProvider(bp);
            ricercaLiberaBP.setFreeSearchSet("fattura_passiva");
            ricercaLiberaBP.setShowSearchResult(false);
            ricercaLiberaBP.setCanPerformSearchWithoutClauses(true);
            ricercaLiberaBP.setPrototype( new EvasioneOrdineRigaBulk());
            Optional.ofNullable(bp.getCondizioneCorrente())
                    .filter(condizioneComplessaBulk -> !condizioneComplessaBulk.children.isEmpty())
                    .ifPresent(condizioneRicercaBulk -> {
                        ricercaLiberaBP.setCondizioneRadice(condizioneRicercaBulk);
                        ricercaLiberaBP.setCondizioneCorrente(
                                (CondizioneRicercaBulk)condizioneRicercaBulk.children.stream()
                                        .filter(CondizioneSempliceBulk.class::isInstance)
                                        .map(CondizioneSempliceBulk.class::cast)
                                        .reduce((first, second) -> second)
                                        .orElse(null)
                        );
                        ricercaLiberaBP.setRadice(condizioneRicercaBulk);
                    });
            context.addHookForward("searchResult",this,"doRigheSelezionate");
            return context.addBusinessProcess(ricercaLiberaBP);
        } catch(Throwable e) {
            return handleException(context,e);
        }
    }

}
