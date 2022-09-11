/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.action.*;

import java.rmi.RemoteException;
import java.util.Optional;

public class SelezionatoreSearchProviderAction extends SelezionatoreListaAction {

    @Override
    public Forward doCancellaFiltro(ActionContext context) {
        SelezionatoreListaBP bp = Optional.ofNullable(context.getBusinessProcess())
                .filter(SelezionatoreListaBP.class::isInstance)
                .map(SelezionatoreListaBP.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("Business process non trovato!"));
        bp.setCondizioneCorrente(null);
        SearchProvider searchProvider = Optional.ofNullable(context.getBusinessProcess())
                .filter(SearchProvider.class::isInstance)
                .map(SearchProvider.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("SearchProvider process non trovato!"));
        try {
            bp.setIterator(context,
                    searchProvider.search(context, new CompoundFindClause(), bp.getModel()));
        } catch (RemoteException|BusinessProcessException e) {
            return handleException(context, e);
        }
        return context.findDefaultForward();
    }

    @Override
    public Forward doRicercaLibera(ActionContext context) {
        try {
            SearchProvider searchProvider = Optional.ofNullable(context.getBusinessProcess())
                    .filter(SearchProvider.class::isInstance)
                    .map(SearchProvider.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("SearchProvider process non trovato!"));
            SelezionatoreListaBP selezionatoreListaBP = Optional.ofNullable(context.getBusinessProcess())
                    .filter(SelezionatoreListaBP.class::isInstance)
                    .map(SelezionatoreListaBP.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("SelezionatoreListaBP process non trovato!"));

                RicercaLiberaBP ricercaLiberaBP = (RicercaLiberaBP) context.createBusinessProcess("RicercaLibera");
                ricercaLiberaBP.setSearchProvider(searchProvider);
                ricercaLiberaBP.setFreeSearchSet("default");
                ricercaLiberaBP.setShowSearchResult(false);
                ricercaLiberaBP.setCanPerformSearchWithoutClauses(false);
                ricercaLiberaBP.setPrototype(selezionatoreListaBP.getModel());
                context.addHookForward("searchResult", this, "doRigheSelezionate");
                context.addHookForward("close", this, "doCloseRicercaLibera");
                return context.addBusinessProcess(ricercaLiberaBP);
        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
