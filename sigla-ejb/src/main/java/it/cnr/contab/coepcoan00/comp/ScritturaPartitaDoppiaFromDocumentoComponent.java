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

package it.cnr.contab.coepcoan00.comp;

import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;
import java.util.Optional;

public class ScritturaPartitaDoppiaFromDocumentoComponent extends CRUDComponent {

    protected void caricaScrittura(UserContext userContext, IDocumentoCogeBulk documentoCogeBulk) throws ComponentException {
        try {
            if (Optional.ofNullable(getHome(userContext, Configurazione_cnrBulk.class))
                    .filter(Configurazione_cnrHome.class::isInstance)
                    .map(Configurazione_cnrHome.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Configurazione Home not found")).isAttivaEconomicaParallela(userContext)) {
                Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
                        .filter(Scrittura_partita_doppiaHome.class::isInstance)
                        .map(Scrittura_partita_doppiaHome.class::cast)
                        .orElseThrow(() -> new DetailedRuntimeException("Partita doppia Home not found"));
                final Optional<Scrittura_partita_doppiaBulk> scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(documentoCogeBulk);
                if (scritturaOpt.isPresent()) {
                    Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
                    scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                            .findMovimentiDareColl(userContext, scrittura)));
                    scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                            .findMovimentiAvereColl(userContext, scrittura)));
                    documentoCogeBulk.setScrittura_partita_doppia(scrittura);
                }
            }
        } catch (PersistencyException | ComponentException e) {
            throw handleException((OggettoBulk) documentoCogeBulk, e);
        }
    }

    @Override
    protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        super.validaCreaModificaConBulk(usercontext, oggettobulk);
        try {
            if (Optional.ofNullable(getHome(usercontext, Configurazione_cnrBulk.class))
                    .filter(Configurazione_cnrHome.class::isInstance)
                    .map(Configurazione_cnrHome.class::cast)
                    .orElseThrow(() -> new DetailedRuntimeException("Configurazione Home not found")).isAttivaEconomicaParallela(usercontext)) {
                final Optional<IDocumentoCogeBulk> optionalIDocumentoCogeBulk = Optional.ofNullable(oggettobulk)
                        .filter(IDocumentoCogeBulk.class::isInstance)
                        .map(IDocumentoCogeBulk.class::cast);
                if (optionalIDocumentoCogeBulk.isPresent() && !(
                        optionalIDocumentoCogeBulk.get().getTipoDocumentoEnum().isMandato() ||
                                optionalIDocumentoCogeBulk.get().getTipoDocumentoEnum().isReversale()
                )){
                    final Optional<Scrittura_partita_doppiaBulk> optionalScrittura_partita_doppiaBulk = Optional.ofNullable(optionalIDocumentoCogeBulk.get())
                            .map(IDocumentoCogeBulk::getScrittura_partita_doppia);
                    if (optionalScrittura_partita_doppiaBulk.isPresent()) {
                        if (optionalScrittura_partita_doppiaBulk.get().isToBeCreated()) {
                            super.creaConBulk(usercontext, optionalScrittura_partita_doppiaBulk.get());
                        } else if (optionalScrittura_partita_doppiaBulk.get().isToBeUpdated()) {
                            super.modificaConBulk(usercontext, optionalScrittura_partita_doppiaBulk.get());
                        }
                    } else {
                        final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaBulk =
                                Optional.ofNullable(Utility.createScritturaPartitaDoppiaComponentSession()
                                        .proposeScritturaPartitaDoppia(usercontext, optionalIDocumentoCogeBulk.get()));
                        if (optionalScritturaPartitaDoppiaBulk.isPresent()) {
                            super.creaConBulk(usercontext, optionalScritturaPartitaDoppiaBulk.get());
                        }
                    }
                }
            }
        } catch (PersistencyException | RemoteException e) {
            throw handleException(e);
        }
    }
}
