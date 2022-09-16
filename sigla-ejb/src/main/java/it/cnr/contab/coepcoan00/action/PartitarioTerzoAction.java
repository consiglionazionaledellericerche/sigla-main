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

package it.cnr.contab.coepcoan00.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.coepcoan00.consultazioni.bp.ConsultazionePartitarioBP;
import it.cnr.contab.coepcoan00.filter.bulk.FiltroRicercaTerzoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.FillException;
import it.cnr.jada.util.action.BulkAction;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.FormBP;

import java.util.Optional;

public class PartitarioTerzoAction extends BulkAction {

    public Forward doBlankSearchTerzo(ActionContext context, FiltroRicercaTerzoBulk filtroRicercaTerzoBulk) {
        final TerzoBulk terzoBulk = new TerzoBulk();
        terzoBulk.setAnagrafico(new AnagraficoBulk());
        filtroRicercaTerzoBulk.setTerzo(terzoBulk);
        return context.findDefaultForward();
    }

    public Forward doPartitario(ActionContext context) throws BusinessProcessException, FillException {
        final BulkBP bulkBP = (BulkBP) context.getBusinessProcess();
        bulkBP.fillModel(context);
        final Optional<TerzoBulk> terzoBulk = Optional.ofNullable(bulkBP.getModel())
                .filter(FiltroRicercaTerzoBulk.class::isInstance)
                .map(FiltroRicercaTerzoBulk.class::cast)
                .flatMap(filtroRicercaTerzoBulk -> Optional.ofNullable(filtroRicercaTerzoBulk.getTerzo()));
        final Boolean isDettaglioTributi = Optional.ofNullable(bulkBP.getModel())
                .filter(FiltroRicercaTerzoBulk.class::isInstance)
                .map(FiltroRicercaTerzoBulk.class::cast)
                .flatMap(filtroRicercaTerzoBulk -> Optional.ofNullable(filtroRicercaTerzoBulk.getDettaglioTributi()))
                .orElse(Boolean.FALSE);
        if (terzoBulk.isPresent()) {
            ConsultazionePartitarioBP consBP = (ConsultazionePartitarioBP) context.createBusinessProcess(
                    "ConsultazionePartitarioBP",
                    new Object[]{terzoBulk.get(), isDettaglioTributi, "partitario"}
            );
            consBP.openIterator(context);
            context.addBusinessProcess(consBP);
            return context.findDefaultForward();
        }
        setMessage(context, FormBP.WARNING_MESSAGE, "Valorizzare il Tezo!");
        return context.findDefaultForward();
    }
}
