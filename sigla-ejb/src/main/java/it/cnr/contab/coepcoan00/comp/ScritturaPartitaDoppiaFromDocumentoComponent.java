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
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.ejb.CompensoComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.ejb.DocumentoGenericoComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.ejb.AnticipoComponentSession;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ScritturaPartitaDoppiaFromDocumentoComponent extends CRUDComponent {
    private Optional<Scrittura_partita_doppiaBulk> getScrittura(UserContext userContext, IDocumentoCogeBulk documentoCogeBulk) throws ComponentException {
        try {
            Optional<Scrittura_partita_doppiaBulk> scritturaOpt = Optional.empty();
            if (Utility.createConfigurazioneCnrComponentSession().isAttivaEconomica(userContext)) {
                Scrittura_partita_doppiaHome partitaDoppiaHome = Optional.ofNullable(getHome(userContext, Scrittura_partita_doppiaBulk.class))
                        .filter(Scrittura_partita_doppiaHome.class::isInstance)
                        .map(Scrittura_partita_doppiaHome.class::cast)
                        .orElseThrow(() -> new DetailedRuntimeException("Partita doppia Home not found"));
                scritturaOpt = partitaDoppiaHome.findByDocumentoAmministrativo(documentoCogeBulk);
                if (scritturaOpt.isPresent()) {
                    Scrittura_partita_doppiaBulk scrittura = scritturaOpt.get();
                    scrittura.setMovimentiDareColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                            .findMovimentiDareColl(userContext, scrittura)));
                    scrittura.setMovimentiAvereColl(new BulkList(((Scrittura_partita_doppiaHome) getHome(userContext, scrittura.getClass()))
                            .findMovimentiAvereColl(userContext, scrittura)));
                }
            }
            return scritturaOpt;
        } catch (PersistencyException | ComponentException | RemoteException e) {
            throw handleException((OggettoBulk) documentoCogeBulk, e);
        }
    }

    private void removeScrittura(UserContext userContext, Scrittura_partita_doppiaBulk scrittura) throws ComponentException {
        try {
            scrittura.getAllMovimentiColl().forEach(movcoge->{
               movcoge.setToBeDeleted();
               try {
                  super.deleteBulk(userContext, movcoge);
               } catch (ComponentException | PersistencyException e) {
                  throw new DetailedRuntimeException(e);
               }
            });
            scrittura.setToBeDeleted();
            super.deleteBulk(userContext, scrittura);
        } catch (ComponentException | PersistencyException e) {
            throw handleException(scrittura, e);
        }
    }
    protected void caricaScrittura(UserContext userContext, IDocumentoCogeBulk documentoCogeBulk) throws ComponentException {
        this.getScrittura(userContext, documentoCogeBulk).ifPresent(documentoCogeBulk::setScrittura_partita_doppia);
    }

    @Override
    protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
        super.validaCreaModificaConBulk(usercontext, oggettobulk);
        try {
            if (Utility.createConfigurazioneCnrComponentSession().isAttivaEconomica(usercontext)) {
                final Optional<IDocumentoCogeBulk> optionalIDocumentoCogeBulk = Optional.ofNullable(oggettobulk)
                        .filter(IDocumentoCogeBulk.class::isInstance)
                        .map(IDocumentoCogeBulk.class::cast);
                if (optionalIDocumentoCogeBulk.isPresent() && !(
                        optionalIDocumentoCogeBulk.get().getTipoDocumentoEnum().isMandato() ||
                                optionalIDocumentoCogeBulk.get().getTipoDocumentoEnum().isReversale()
                )){
                    if (Utility.createConfigurazioneCnrComponentSession().isBloccoScrittureProposte(usercontext)) {
                        final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaOldBulk = this.getScrittura(usercontext, optionalIDocumentoCogeBulk.get());
                        final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaPropostaBulk =
                                Optional.ofNullable(Utility.createScritturaPartitaDoppiaComponentSession()
                                        .proposeScritturaPartitaDoppia(usercontext, optionalIDocumentoCogeBulk.get()));
                        optionalScritturaPartitaDoppiaOldBulk.ifPresent(oldScrittura->{
                            //Elimino vecchia scrittura
                            try {
                                optionalScritturaPartitaDoppiaPropostaBulk.ifPresent(prop->prop.setPg_scrittura(oldScrittura.getPg_scrittura()));
                                this.removeScrittura(usercontext, oldScrittura);
                            } catch (ComponentException e) {
                                throw new DetailedRuntimeException(e);
                            }
                        });
                        //Ricreo
                        if (optionalScritturaPartitaDoppiaPropostaBulk.isPresent())
                            super.creaConBulk(usercontext, optionalScritturaPartitaDoppiaPropostaBulk.get());
                    } else {
                        final Optional<Scrittura_partita_doppiaBulk> optionalScrittura_partita_doppiaBulk = optionalIDocumentoCogeBulk
                                .map(IDocumentoCogeBulk::getScrittura_partita_doppia);
                        if (optionalScrittura_partita_doppiaBulk.isPresent()) {
                            if (optionalScrittura_partita_doppiaBulk.get().isToBeCreated()) {
                                final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaOldBulk = this.getScrittura(usercontext, optionalIDocumentoCogeBulk.get());
                                optionalScritturaPartitaDoppiaOldBulk.ifPresent(oldScrittura->{
                                    //Elimino vecchia scrittura
                                    try {
                                        optionalScrittura_partita_doppiaBulk.ifPresent(prop->prop.setPg_scrittura(oldScrittura.getPg_scrittura()));
                                        this.removeScrittura(usercontext, oldScrittura);
                                    } catch (ComponentException e) {
                                        throw new DetailedRuntimeException(e);
                                    }
                                });
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
            }
        } catch (RemoteException e) {
            throw handleException(e);
        }
    }

    public void loadAllScritture(UserContext userContext, Integer esercizio, String cdCds) throws PersistencyException, ComponentException {
        List<IDocumentoCogeBulk> allDocuments = new ArrayList<>();

        {
            PersistentHome anticipoHome = getHome(userContext, AnticipoBulk.class);
            SQLBuilder sql = anticipoHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            anticipoHome.fetchAll(sql).stream().forEach(anticipo->{
                try {
                    AnticipoComponentSession component = (AnticipoComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_AnticipoComponentSession", AnticipoComponentSession.class);
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) anticipo));
                } catch (ComponentException | RemoteException e) {
                throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome missioneHome = getHome(userContext, MissioneBulk.class);
            SQLBuilder sql = missioneHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            missioneHome.fetchAll(sql).stream().forEach(missione->{
                try {
                    MissioneComponentSession component = (MissioneComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRMISSIONI00_EJB_MissioneComponentSession", MissioneComponentSession.class);
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) missione));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome compensoHome = getHome(userContext, CompensoBulk.class);
            SQLBuilder sql = compensoHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            compensoHome.fetchAll(sql).stream().forEach(compenso->{
                try {
                    CompensoComponentSession component = Utility.createCompensoComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) compenso));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome docgenHome = getHome(userContext, Documento_genericoBulk.class);
            SQLBuilder sql = docgenHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            docgenHome.fetchAll(sql).stream().forEach(docgen->{
                try {
                    DocumentoGenericoComponentSession component = Utility.createDocumentoGenericoComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) docgen));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome fatattHome = getHome(userContext, Fattura_attivaBulk.class);
            SQLBuilder sql = fatattHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            fatattHome.fetchAll(sql).stream().forEach(fatatt->{
                try {
                    FatturaAttivaSingolaComponentSession component = Utility.createFatturaAttivaSingolaComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) fatatt));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome fatpasHome = getHome(userContext, Fattura_passivaBulk.class);
            SQLBuilder sql = fatpasHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            fatpasHome.fetchAll(sql).stream().forEach(fatpas->{
                try {
                    FatturaPassivaComponentSession component = Utility.createFatturaPassivaComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) fatpas));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome mandatoHome = getHome(userContext, MandatoBulk.class);
            SQLBuilder sql = mandatoHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            mandatoHome.fetchAll(sql).stream().forEach(mandato->{
                try {
                    MandatoComponentSession component = Utility.createMandatoComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) mandato));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }
        {
            PersistentHome reversaleHome = getHome(userContext, ReversaleBulk.class);
            SQLBuilder sql = reversaleHome.createSQLBuilder();
            sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, esercizio);
            Optional.ofNullable(cdCds).ifPresent(el -> sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, el));
            reversaleHome.fetchAll(sql).stream().forEach(reversale->{
                try {
                    ReversaleComponentSession component = Utility.createReversaleComponentSession();
                    allDocuments.add((IDocumentoCogeBulk)component.inizializzaBulkPerModifica(userContext, (OggettoBulk) reversale));
                } catch (ComponentException | RemoteException e) {
                    throw new DetailedRuntimeException(e);
                }
            });
        }

        allDocuments.stream().sorted(Comparator.comparing(IDocumentoCogeBulk::getDt_contabilizzazione)).forEach(el->{
            try {
                final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaOldBulk = this.getScrittura(userContext, el);
                final Optional<Scrittura_partita_doppiaBulk> optionalScritturaPartitaDoppiaPropostaBulk =
                        Optional.ofNullable(Utility.createScritturaPartitaDoppiaComponentSession()
                                .proposeScritturaPartitaDoppia(userContext, el));
                optionalScritturaPartitaDoppiaOldBulk.ifPresent(oldScrittura->{
                    //Elimino vecchia scrittura
                    try {
                        optionalScritturaPartitaDoppiaPropostaBulk.ifPresent(prop->prop.setPg_scrittura(oldScrittura.getPg_scrittura()));
                        this.removeScrittura(userContext, oldScrittura);
                    } catch (ComponentException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
                //Ricreo
                if (optionalScritturaPartitaDoppiaPropostaBulk.isPresent())
                    super.creaConBulk(userContext, optionalScritturaPartitaDoppiaPropostaBulk.get());
            } catch (ComponentException | RemoteException e) {
                throw new DetailedRuntimeException(e);
            }
        });

    }
}
