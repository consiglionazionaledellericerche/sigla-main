/*
 * Copyright (C) 2021  Consiglio Nazionale delle Ricerche
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

import it.cnr.contab.coepcoan00.comp.ScritturaPartitaDoppiaNotRequiredException;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.docamm00.bp.IDocAmmEconomicaBP;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormBP;

import java.rmi.RemoteException;
import java.util.Optional;

public abstract class EconomicaAction extends CRUDAction {
    public Forward doGeneraScritturaEconomica(ActionContext actionContext) throws BusinessProcessException {
        IDocAmmEconomicaBP bp = Optional.ofNullable(actionContext.getBusinessProcess())
                .filter(IDocAmmEconomicaBP.class::isInstance)
                .map(IDocAmmEconomicaBP.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Business process non compatibile!"));
        final IDocumentoCogeBulk documentoCogeBulk = Optional.ofNullable(bp.getModel())
                .filter(IDocumentoCogeBulk.class::isInstance)
                .map(IDocumentoCogeBulk.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Modello di business non compatibile!"));
        try {
            documentoCogeBulk.setScrittura_partita_doppia(Utility.createScritturaPartitaDoppiaComponentSession().proposeScritturaPartitaDoppia(
                    actionContext.getUserContext(),
                    documentoCogeBulk)
            );
            Utility.createScritturaPartitaDoppiaFromDocumentoComponentSession().loadScritturaPatrimoniale(
                    actionContext.getUserContext(),
                    documentoCogeBulk);
            bp.getMovimentiAvere().reset(actionContext);
            bp.getMovimentiDare().reset(actionContext);
            bp.setMessage(FormBP.INFO_MESSAGE, "Scrittura di economica generata correttamente.");
            bp.setDirty(true);
        } catch (ScritturaPartitaDoppiaNotRequiredException e) {
            bp.setMessage(FormBP.INFO_MESSAGE, e.getMessage());
        } catch (ComponentException | RemoteException e) {
            return handleException(actionContext, e);
        }
        return actionContext.findDefaultForward();
    }
}
