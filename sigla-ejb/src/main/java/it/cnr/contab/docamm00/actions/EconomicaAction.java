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
import it.cnr.contab.coepcoan00.consultazioni.bp.ConsultazionePartitarioBP;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Movimento_cogeBulk;
import it.cnr.contab.docamm00.bp.IDocAmmEconomicaBP;
import it.cnr.contab.docamm00.consultazioni.bp.ConsDocammAnagBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.FormBP;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Forward doPartitario(ActionContext actionContext) throws BusinessProcessException {
        IDocAmmEconomicaBP bp = Optional.ofNullable(actionContext.getBusinessProcess())
                .filter(IDocAmmEconomicaBP.class::isInstance)
                .map(IDocAmmEconomicaBP.class::cast)
                .orElseThrow(() -> new BusinessProcessException("Business process non compatibile!"));
        final Optional<IDocumentoAmministrativoBulk> documentoAmministrativoBulk = Optional.ofNullable(bp.getModel())
                .filter(IDocumentoAmministrativoBulk.class::isInstance)
                .map(IDocumentoAmministrativoBulk.class::cast);
        if (documentoAmministrativoBulk.isPresent()) {
            List<IDocumentoAmministrativoBulk> iDocumentoAmministrativoBulks = documentoAmministrativoBulk
                    .flatMap(documentoAmministrativoBulk1 -> Optional.ofNullable(documentoAmministrativoBulk1.getScrittura_partita_doppia()))
                    .flatMap(scrittura_partita_doppiaBulk -> Optional.ofNullable(scrittura_partita_doppiaBulk.getAllMovimentiColl()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(Movimento_cogeBulk::getDocumentoAmministrativo)
                    .distinct()
                    .collect(Collectors.toList());
            iDocumentoAmministrativoBulks.add(documentoAmministrativoBulk.get());
            ConsultazionePartitarioBP consBP = (ConsultazionePartitarioBP) actionContext.createBusinessProcess(
                    "ConsultazionePartitarioBP",
                    new Object[] { iDocumentoAmministrativoBulks, "partitario_amministrativo"}
            );
            consBP.openIterator(actionContext);
            actionContext.addBusinessProcess(consBP);
            return actionContext.findDefaultForward();
        }
        final Optional<IDocumentoCogeBulk> documentoCogeBulk = Optional.ofNullable(bp.getModel())
                .filter(IDocumentoCogeBulk.class::isInstance)
                .map(IDocumentoCogeBulk.class::cast);
        if (documentoCogeBulk.isPresent()) {
            final List<IDocumentoAmministrativoBulk> iDocumentoAmministrativoBulks = documentoCogeBulk
                    .flatMap(documentoCogeBulk1 -> Optional.ofNullable(documentoCogeBulk1.getScrittura_partita_doppia()))
                    .flatMap(scrittura_partita_doppiaBulk -> Optional.ofNullable(scrittura_partita_doppiaBulk.getAllMovimentiColl()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(Movimento_cogeBulk::getDocumentoAmministrativo)
                    .collect(Collectors.toList());
            if (!iDocumentoAmministrativoBulks.isEmpty()) {
                ConsultazionePartitarioBP consBP = (ConsultazionePartitarioBP) actionContext.createBusinessProcess(
                        "ConsultazionePartitarioBP",
                        new Object[] { iDocumentoAmministrativoBulks, "partitario_contabile" }
                );
                consBP.openIterator(actionContext);
                actionContext.addBusinessProcess(consBP);
                return actionContext.findDefaultForward();
            }
        }
        bp.setMessage(FormBP.WARNING_MESSAGE, "Non ci sono elementi da visualizzare!");
        return actionContext.findDefaultForward();
    }
}
