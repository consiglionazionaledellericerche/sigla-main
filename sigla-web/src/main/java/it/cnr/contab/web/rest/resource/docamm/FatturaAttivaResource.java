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

package it.cnr.contab.web.rest.resource.docamm;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.client.docamm.FatturaAttivaIntra;
import it.cnr.contab.client.docamm.FatturaAttivaRiga;
import it.cnr.contab.client.docamm.FatturaAttivaScad;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.config.FatturaAttivaCodiciEnum;
import it.cnr.contab.web.rest.exception.FatturaAttivaException;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.docamm.FatturaAttivaLocal;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonProtocollataException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Stateless
public class FatturaAttivaResource implements FatturaAttivaLocal {
    private static final String ENTE = "ENTE";
    private static final String CONTO_CORRENTE_SPECIALE = "CONTO_CORRENTE_SPECIALE";
    private static final String FATTURA_AUTOMATICA = "Fattura_automatica";
    private static final String FATTURA_ATTIVA_NON_PRESENTE = "Fattura attiva non presente.";
    private static final String FATTURA_ATTIVA_NON_PROTOCOLLATA = "Fattura attiva non protocollata.";
    private final Logger LOGGER = LoggerFactory.getLogger(FatturaAttivaResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
    @EJB
    Configurazione_cnrComponentSession configurazione_cnrComponentSession;
    @EJB
    AccertamentoComponentSession accertamentoComponentSession;
    @EJB
    ObbligazioneComponentSession obbligazioneComponentSession;


    @Override
    public Response ricercaFattura(@Context HttpServletRequest request, Integer esercizio, Long pg) throws Exception {
        LOGGER.debug("REST request per ricercare una fattura attiva.");
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        try {
            Fattura_attivaBulk fatturaAttivaBulk = fatturaAttivaSingolaComponentSession.ricercaFattura(userContext, esercizio.longValue(),
                    userContext.getCd_cds(), userContext.getCd_unita_organizzativa(), pg);
            final FatturaAttiva fatturaAttiva = Optional.ofNullable(fatturaAttivaBulk)
                    .map(f -> {
                        return convert(userContext, f);
                    })
                    .orElseThrow(() -> new FatturaNonTrovataException(FATTURA_ATTIVA_NON_PRESENTE));
            return Response.ok(fatturaAttiva).build();
        } catch (FatturaNonTrovataException _ex) {
            return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Response inserisciFatture(@Context HttpServletRequest request, List<FatturaAttiva> fatture) throws Exception {
        LOGGER.debug("REST request per inserire fatture attive.");
        List<Fattura_attivaBulk> fattureCreate = new ArrayList<Fattura_attivaBulk>();
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        fatture.stream().forEach(fattura -> {
            Fattura_attivaBulk testata;

            Optional.ofNullable(fattura.getEsercizio()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, Esercizio obbligatorio!"));
            Optional.ofNullable(fattura.getCd_cds_origine()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, Cds origine obbligatorio!"));
            Optional.ofNullable(fattura.getCd_uo_origine()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, UO origine obbligatoria!"));

            Optional.ofNullable(fattura.getEsercizio()).filter(x -> userContext.getEsercizio().equals(x)).
                    orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Esercizio del contesto diverso da quello della Fattura!"));
            Optional.ofNullable(fattura.getCd_cds_origine()).filter(x -> userContext.getCd_cds().equals(x)).
                    orElseThrow(() -> new RestException(Status.BAD_REQUEST, "CdS del contesto diverso da quello della Fattura!"));
            Optional.ofNullable(fattura.getCd_uo_origine()).filter(x -> userContext.getCd_unita_organizzativa().equals(x)).
                    orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Unità Organizzativa del contesto diversa da quella della Fattura!"));
            Optional.ofNullable(fattura.getTi_causale_emissione()).filter(x -> Stream.of("C", "L", "T").anyMatch(y -> y.equals(x)))
                    .orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Causale emissione non valida!"));
            Optional.ofNullable(fattura.getTi_bene_servizio()).filter(x -> Stream.of("B", "S", "*").anyMatch(y -> y.equals(x)))
                    .orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipologia bene/servizio non valida!"));

            Optional.ofNullable(fattura.getTi_fattura()).filter(x -> Stream.of(
                            Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO,
                            Fattura_attivaBulk.TIPO_FATTURA_ATTIVA
                    ).anyMatch(y -> y.equals(x)))
                    .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_99));

            if (fattura.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO)) {
                testata = new Nota_di_credito_attivaBulk();
            } else {
                testata = new Fattura_attiva_IBulk();
            }
            testata.setToBeCreated();
            testata.setEsercizio(fattura.getEsercizio());
            testata.setTi_fattura(fattura.getTi_fattura());
            testata.setCd_cds_origine(fattura.getCd_cds_origine());
            testata.setTi_bene_servizio(fattura.getTi_bene_servizio());
            testata.setFl_pagamento_anticipato(Optional.ofNullable(fattura.getFl_pagamento_anticipato()).orElse("N"));
            testata.setCd_uo_origine(fattura.getCd_uo_origine());
            testata.setPg_fattura_esterno(fattura.getPg_fattura_esterno());
            testata.setFl_intra_ue(Optional.ofNullable(fattura.getFl_intra_ue()).orElse(Boolean.FALSE));
            testata.setFl_extra_ue(Optional.ofNullable(fattura.getFl_extra_ue()).orElse(Boolean.FALSE));
            testata.setFl_san_marino(Optional.ofNullable(fattura.getFl_san_marino()).orElse(Boolean.FALSE));
            Optional.of(Stream.of(
                            Optional.ofNullable(fattura.getFl_intra_ue()).orElse(Boolean.FALSE),
                            Optional.ofNullable(fattura.getFl_extra_ue()).orElse(Boolean.FALSE),
                            Optional.ofNullable(fattura.getFl_san_marino()).orElse(Boolean.FALSE))
                            .filter(x -> x.equals(Boolean.TRUE)).count())
                            .filter(x -> x <= 1)
                    .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_129));

            Optional.of((
                        Optional.ofNullable(fattura.getFl_intra_ue()).orElse(Boolean.FALSE) ||
                        Optional.ofNullable(fattura.getFl_extra_ue()).orElse(Boolean.FALSE)
                    ) && fattura.getTi_bene_servizio().equals("*"))
                    .filter(x -> x.equals(Boolean.FALSE))
                    .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_142));

            try {
                userContext.setTransactional(true);
                // richiesta dall'inizializza (Fattura_attiva_IBulk)
                testata = (Fattura_attivaBulk) fatturaAttivaSingolaComponentSession.inizializzaBulkPerInserimento(userContext, testata);
                fatturaAttivaSingolaComponentSession.setSavePoint(userContext, FATTURA_AUTOMATICA);
                // potrebbe non essere univoca per tipologia
                Optional.of(fatturaAttivaSingolaComponentSession.verificaDuplicati(userContext, testata)).filter(x -> x.equals(Boolean.FALSE))
                        .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_102));

                testata.setTipo_sezionale(new Tipo_sezionaleBulk(fattura.getCd_tipo_sezionale()));
                Optional.of(testata.getSezionali().stream().filter(x -> x.getCd_tipo_sezionale().equals(
                                fattura.getCd_tipo_sezionale())).count()).filter(x -> x == 1).
                        orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_127));

                testata.setDt_registrazione(
                        Optional.ofNullable(fattura.getDt_registrazione())
                                .map(date -> new Timestamp(date.getTime()))
                                .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_152))
                );
                testata.setTi_causale_emissione(fattura.getTi_causale_emissione());
                testata.setFl_liquidazione_differita(fattura.getFl_liquidazione_differita());
                testata.setDs_fattura_attiva(fattura.getDs_fattura_attiva());
                testata.setRiferimento_ordine(fattura.getRif_ordine());
                testata.setCliente(new TerzoBulk(fattura.getCd_terzo()));
                testata.setCliente((TerzoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, testata.getCliente()));
                Optional.ofNullable(testata.getCliente()).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_105));
                Optional.of(testata.getCliente().getAnagrafico().getTi_italiano_estero().equals(testata.getSupplierNationType())).
                        filter(x -> x.equals(Boolean.TRUE))
                        .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_140));
                testata.setCd_terzo(fattura.getCd_terzo());
                testata = fatturaAttivaSingolaComponentSession.completaTerzo(userContext, testata, testata.getCliente());
                Optional.of(Stream.of(
                        testata.getCliente().isAnagraficoScaduto(),
                        testata.getCliente().getTi_terzo().equals(TerzoBulk.CREDITORE),
                        testata.getCliente().getAnagrafico().getTi_entita().equals(AnagraficoBulk.DIVERSI)
                ).filter(x -> x.equals(Boolean.TRUE)).count()).filter(x -> x == 0).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_125));

                Optional.ofNullable(fattura.getFl_liquidazione_differita())
                                .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_124));
                testata.setFl_liquidazione_differita(fattura.getFl_liquidazione_differita());
                Optional.of(testata).filter(x -> !(x.getFl_liquidazione_differita().equals(Boolean.TRUE) &&
                                !x.getFl_liquidazione_differita().equals(x.getCliente().getAnagrafico().getFl_fatturazione_differita())))
                        .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_124));

                Optional.of(testata).filter(x -> x.getCd_terzo_uo_cds().equals(fattura.getCd_terzo_uo_cds()))
                        .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_133));

                testata.setCd_modalita_pag_uo_cds(fattura.getCd_modalita_pag_uo_cds());
                testata.setModalita_pagamento_uo(((Rif_modalita_pagamentoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new Rif_modalita_pagamentoBulk(fattura.getCd_modalita_pag_uo_cds()))));
                Optional.ofNullable(testata.getModalita_pagamento_uo()).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_132));
                testata.setPg_banca_uo_cds(
                        Optional.ofNullable(fattura.getPg_banca_uo_cds())
                                .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_134))
                );
                testata.setBanca_uo(((BancaBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new BancaBulk(testata.getCd_terzo_uo_cds(), testata.getPg_banca_uo_cds()))));
                Configurazione_cnrBulk config = configurazione_cnrComponentSession.getConfigurazione(userContext, 0, "*", CONTO_CORRENTE_SPECIALE, ENTE);
                if (Rif_modalita_pagamentoBulk.BANCARIO.equals(testata.getModalita_pagamento_uo().getTi_pagamento())) {
                    Optional.of(Stream.of(
                            testata.getBanca_uo().getAbi().equalsIgnoreCase(config.getVal01()),
                            testata.getBanca_uo().getCab().equalsIgnoreCase(config.getVal02()),
                            testata.getBanca_uo().getNumero_conto().contains(config.getVal03())
                    ).filter(x -> x.equals(Boolean.FALSE)).count()).filter(x -> x == 0).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_134));
                }
                List<BancaBulk> listaBanche = fatturaAttivaSingolaComponentSession.
                        findListaBancheWS(userContext, String.valueOf(testata.getCd_terzo_uo_cds()), testata.getModalita_pagamento_uo().getCd_modalita_pag(), "", "", "");
                listaBanche.stream().filter(banca -> banca.getPg_banca().equals(fattura.getPg_banca_uo_cds())).findFirst().
                        orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_134));

                if ((testata.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO))) {
                    Optional.ofNullable(fattura.getCd_modalita_pag()).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_111));
                    ((Nota_di_credito_attivaBulk) testata).setModalita_pagamento(Optional.ofNullable((Rif_modalita_pagamentoBulk) fatturaAttivaSingolaComponentSession.
                                    completaOggetto(userContext, new Rif_modalita_pagamentoBulk(fattura.getCd_modalita_pag()))).
                            orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_111)));

                    Optional.ofNullable(fattura.getPg_banca()).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_110));
                    ((Nota_di_credito_attivaBulk) testata).setBanca(Optional.ofNullable((BancaBulk) fatturaAttivaSingolaComponentSession.
                                    completaOggetto(userContext, new BancaBulk(testata.getCd_terzo(), fattura.getPg_banca()))).
                            orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_118)));

                    listaBanche = fatturaAttivaSingolaComponentSession.
                            findListaBancheWS(userContext, String.valueOf(testata.getCd_terzo()), ((Nota_di_credito_attivaBulk) testata).getModalita_pagamento().getCd_modalita_pag(), "", "", "");
                    listaBanche.stream().filter(banca -> banca.getPg_banca().equals(fattura.getPg_banca())).findFirst().
                            orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_118));
                }

                testata.setCd_divisa(Optional.ofNullable(fattura.getCd_divisa()).orElse("EURO"));
                testata.setCambio(Optional.ofNullable(fattura.getCambio()).orElse(BigDecimal.ONE));
                testata.setNote(fattura.getNote());
                testata.validate();
                java.util.ArrayList<FatturaAttivaRiga> listOfRighe = Optional.ofNullable(fattura.getRighefat())
                        .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_98));

                for (FatturaAttivaRiga fatr : listOfRighe) {
                    Fattura_attiva_rigaBulk riga = new Fattura_attiva_rigaIBulk();
                    if ((testata.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO))) {
                        riga = new Nota_di_credito_attiva_rigaBulk();
                    }
                    testata.addToFattura_attiva_dettColl(riga);
                    riga.setToBeCreated();
                    Optional.ofNullable(fatr.getCd_bene_servizio())
                            .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_139));
                    riga.setBene_servizio(Optional.ofNullable((Bene_servizioBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new Bene_servizioBulk(fatr.getCd_bene_servizio()))).
                            orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_139)));

                    Optional.of(Stream.of(
                                    fattura.getTi_bene_servizio().equals("*"),
                                    riga.getBene_servizio().getTi_bene_servizio().equals(fattura.getTi_bene_servizio())
                            ).filter(x -> x.equals(Boolean.FALSE)).count()).filter(x -> x < 2).
                            orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_141));

                    if (testata.getTi_causale_emissione().equals(Fattura_attivaBulk.TARIFFARIO)) {
                        Optional.ofNullable(fatr.getCd_tariffario()).
                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_103));
                        riga.setCd_tariffario(fatr.getCd_tariffario());
                        riga.setTariffario(Optional.ofNullable(fatturaAttivaSingolaComponentSession.findTariffario(userContext, riga)).
                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_128)));
                        riga.setVoce_iva(riga.getTariffario().getVoce_iva());
                        riga.setVoce_iva(Optional.ofNullable((Voce_ivaBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, riga.getVoce_iva())).
                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_106)));
                        riga.setPrezzo_unitario(riga.getTariffario().getIm_tariffario());
                        riga.setQuantita(new BigDecimal(riga.getTariffario().getUnita_misura()));
                        riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
                        if (Optional.ofNullable(fatr.getFl_iva_forzata()).orElse(Boolean.FALSE)) {
                            riga.setIm_iva(fatr.getIm_iva());
                        } else {
                            riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).divide(
                                    new BigDecimal(100)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                        }
                        riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
                        riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
                        riga.setDs_riga_fattura(riga.getTariffario().getDs_tariffario());
                    } else {
                        riga.setPrezzo_unitario(Optional.ofNullable(fatr.getPrezzo_unitario()).orElse(BigDecimal.ZERO));
                        riga.setQuantita(Optional.ofNullable(fatr.getQuantita()).orElse(BigDecimal.ZERO));
                        riga.setVoce_iva(Optional.ofNullable((Voce_ivaBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new Voce_ivaBulk(fatr.getCd_voce_iva()))).
                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_106)));
                        riga.setIm_imponibile(riga.getPrezzo_unitario().multiply(riga.getQuantita()));
                        if (Optional.ofNullable(fatr.getFl_iva_forzata()).orElse(Boolean.FALSE)) {
                            riga.setIm_iva(fatr.getIm_iva());
                        } else {
                            riga.setIm_iva(riga.getIm_imponibile().multiply(riga.getVoce_iva().getPercentuale()).
                                    divide(new BigDecimal(100)).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
                        }
                        riga.setIm_totale_divisa(riga.getIm_imponibile().add(riga.getIm_iva()));
                        riga.setIm_diponibile_nc(riga.getIm_totale_divisa());
                        riga.setDs_riga_fattura(Optional.ofNullable(fatr.getDs_riga_fattura()).orElse(null));
                        if ((testata.getTi_fattura().equals(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO))) {
                            Optional.of(Stream.of(
                                            fatr.getPg_fattura_assncna_fin() != null,
                                            fatr.getPg_riga_assncna_fin() != null,
                                            fatr.getEsercizio_assncna_fin() != null
                                    ).filter(x -> x.equals(Boolean.FALSE)).count()).filter(x -> x == 0).
                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_109));
                            ((Nota_di_credito_attiva_rigaBulk) riga).setRiga_fattura_associata(Optional.ofNullable((Fattura_attiva_rigaIBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext,
                                            new Fattura_attiva_rigaIBulk(
                                                    testata.getCd_cds(),
                                                    testata.getCd_unita_organizzativa(),
                                                    fatr.getEsercizio_assncna_fin(),
                                                    fatr.getPg_fattura_assncna_fin(),
                                                    fatr.getPg_riga_assncna_fin()))).
                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_109)));
                            Fattura_attiva_rigaIBulk rigaFP = ((Nota_di_credito_attiva_rigaBulk) riga).getRiga_fattura_associata();
                            BigDecimal nuovoImportoDisponibile = rigaFP.getIm_diponibile_nc().subtract(riga.getIm_totale_divisa());

                            if (nuovoImportoDisponibile.compareTo(BigDecimal.ZERO) < 0) {
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_117);
                            } else {
                                rigaFP.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, BigDecimal.ROUND_HALF_UP));
                                ((Nota_di_credito_attiva_rigaBulk) riga).setRiga_fattura_associata(rigaFP);
                            }
                            rigaFP.setFattura_attivaI((Fattura_attiva_IBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext,
                                    new Fattura_attiva_IBulk(
                                            testata.getCd_cds(), testata.getCd_unita_organizzativa(),
                                            rigaFP.getEsercizio(),
                                            rigaFP.getPg_fattura_attiva())));
                            rigaFP.getFattura_attivaI().setCliente(
                                    (TerzoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext,
                                            rigaFP.getFattura_attivaI().getCliente()));
                            //Controllo codice iva coerente fattura - NC
                            if (rigaFP != null && rigaFP.getCd_voce_iva() != null && rigaFP.getCd_voce_iva().compareTo(riga.getCd_voce_iva()) != 0)
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_120);
                            //controllo causale emissione coerente fattura - Nc
                            if (rigaFP != null && rigaFP.getFattura_attivaI().getTi_causale_emissione() != null &&
                                    rigaFP.getFattura_attivaI().getTi_causale_emissione().compareTo(riga.getFattura_attiva().getTi_causale_emissione()) != 0)
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_121);
                            //controllo sezionale coerente fattura - Nc
                            if (rigaFP != null && rigaFP.getFattura_attivaI().getTipo_sezionale() != null &&
                                    rigaFP.getFattura_attivaI().getTipo_sezionale().getCd_tipo_sezionale().compareTo(riga.getFattura_attiva().getTipo_sezionale().getCd_tipo_sezionale()) != 0)
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_122);
                            //controllo tariffario coerente fattura - Nc
                            if (rigaFP != null && rigaFP.getCd_tariffario() != null && rigaFP.getCd_tariffario().compareTo(riga.getCd_tariffario()) != 0)
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_123);//controllo flag coerente fattura - Nc
                            if (rigaFP != null &&
                                    (rigaFP.getFattura_attivaI().getFl_extra_ue().compareTo(riga.getFattura_attiva().getFl_extra_ue()) != 0 ||
                                            rigaFP.getFattura_attivaI().getFl_intra_ue().compareTo(riga.getFattura_attiva().getFl_intra_ue()) != 0 ||
                                            rigaFP.getFattura_attivaI().getFl_san_marino().compareTo(riga.getFattura_attiva().getFl_san_marino()) != 0 ||
                                            rigaFP.getFattura_attivaI().getFl_liquidazione_differita().compareTo(riga.getFattura_attiva().getFl_liquidazione_differita()) != 0))
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_126);
                            testata.setCd_cds(testata.getCd_cds_origine());
                            testata.setCd_unita_organizzativa(testata.getCd_uo_origine());
                        }
                        riga.setFl_iva_forzata(fatr.getFl_iva_forzata());
                        riga.setUtcr(testata.getUtcr());
                        fatr.setProgressivo_riga(riga.getProgressivo_riga());
                        if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO) == 0)) {
                            AccertamentoBulk acc = new AccertamentoBulk();
                            acc.setToBeCreated();
                            acc.setEsercizio(riga.getEsercizio());
                            acc.setEsercizio_originale(riga.getEsercizio());
                            acc.setCd_cds(riga.getCd_cds());
                            acc.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
                            acc.setCd_cds_origine(testata.getCd_cds_origine());
                            acc.setCd_uo_origine(testata.getCd_uo_origine());
                            acc.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_ACR);
                            acc.setFl_pgiro(new Boolean(false));
                            acc.setRiportato("N");
                            acc.setFromDocAmm(new Boolean(true));
                            acc.setFl_calcolo_automatico(new Boolean(false));
                            acc.setUtcr(testata.getUtcr());
                            acc.setUser(testata.getUtcr());

                            Elemento_voceBulk voce = new Elemento_voceBulk();
                            voce.setEsercizio(acc.getEsercizio());
                            voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
                            voce.setTi_gestione(Elemento_voceHome.GESTIONE_ENTRATE);

                            voce.setCd_elemento_voce(fatr.getCd_voce());
                            voce = Optional.ofNullable(((Elemento_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, voce)))
                                    .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_107));
                            acc.setCapitolo(new V_voce_f_partita_giroBulk(voce.getCd_voce(), voce.getEsercizio(), voce.getTi_appartenenza(), voce.getTi_gestione()));
                            acc.setTi_appartenenza(voce.getTi_appartenenza());
                            acc.setTi_gestione(voce.getTi_gestione());
                            acc.setCd_elemento_voce(voce.getCd_elemento_voce());
                            acc.setCd_voce(fatr.getCd_voce());

                            Elemento_voceBulk v = new Elemento_voceBulk(voce.getCd_elemento_voce(), voce.getEsercizio(), voce.getTi_appartenenza(), voce.getTi_gestione());
                            v = ((Elemento_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, v));
                            if (v.getFl_solo_residuo())
                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_107);
                            //acc.setDt_registrazione(testata.getDt_registrazione());
                            ///?????
                            acc.setDt_registrazione((DateUtils.truncate(new Timestamp(
                                    Optional.ofNullable(fatr.getDt_scadenza())
                                            .map(date -> date.getTime())
                                            .orElse(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).toInstant().toEpochMilli())
                            ))));
                            acc.setDs_accertamento(
                                    Optional.ofNullable(fatr.getDs_accertamento())
                                            .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_153))
                            );
                            //??? importo riga?????
                            if (fatturaAttivaSingolaComponentSession.isAttivoSplitPayment(userContext, testata.getDt_registrazione()) && testata.getFl_liquidazione_differita())
                                acc.setIm_accertamento(riga.getIm_imponibile());
                            else
                                acc.setIm_accertamento(riga.getIm_totale_divisa());
                            acc.setCd_terzo(testata.getCd_terzo());
                            acc.setEsercizio_competenza(testata.getEsercizio());

                            Accertamento_scadenzarioBulk acc_scadenza = new Accertamento_scadenzarioBulk();
                            acc_scadenza.setUtcr(acc.getUtcr());
                            acc_scadenza.setToBeCreated();

                            acc_scadenza.setAccertamento(acc);
                            acc_scadenza.setDt_scadenza_incasso(acc.getDt_registrazione());
                            acc_scadenza.setDs_scadenza(acc.getDs_accertamento());
                            acc.addToAccertamento_scadenzarioColl(acc_scadenza);
                            acc_scadenza.setIm_scadenza(acc.getIm_accertamento());
                            acc_scadenza.setIm_associato_doc_amm(acc.getIm_accertamento());
                            acc_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
                            if (fatr.getEsercizio_contratto() != null &&
                                    fatr.getStato_contratto() != null &&
                                    fatr.getPg_contratto() != null) {
                                acc.setContratto(new ContrattoBulk(fatr.getEsercizio_contratto(), fatr.getStato_contratto(), fatr.getPg_contratto()));
                                acc.setContratto((ContrattoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, acc.getContratto()));
                                Optional.ofNullable(acc.getContratto()).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_104));
                                acc.setCheckDisponibilitaContrattoEseguito(true);
                            } else {
                                if ((fatr.getEsercizio_contratto() != null ||
                                        fatr.getStato_contratto() != null ||
                                        fatr.getPg_contratto() != null) &&
                                        (fatr.getEsercizio_contratto() == null &&
                                                fatr.getStato_contratto() == null &&
                                                fatr.getPg_contratto() == null))
                                    throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_108);
                            }
                            List<FatturaAttivaScad> listOfScad = Optional.ofNullable(fatr.getRighescadvoc())
                                    .orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_98));
                            for (FatturaAttivaScad fatrs : listOfScad) {
                                Accertamento_scad_voceBulk acc_scad_voce = new Accertamento_scad_voceBulk();
                                acc_scad_voce.setUtcr(testata.getUtcr());
                                acc_scad_voce.setToBeCreated();
                                acc_scad_voce.setAccertamento_scadenzario(acc_scadenza);
                                if (fatturaAttivaSingolaComponentSession.isAttivoSplitPayment(userContext, testata.getDt_registrazione()) && testata.getFl_liquidazione_differita()) {
                                    if (listOfScad.size() != 1) {
                                        throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_98);
                                    }
                                    acc_scad_voce.setIm_voce(acc.getIm_accertamento());
                                } else {
                                    acc_scad_voce.setIm_voce(fatrs.getIm_voce());
                                }
                                CdrBulk cdr_db = new CdrBulk();
                                cdr_db = (((CdrBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new CdrBulk(fatrs.getCdr()))));
                                Optional.ofNullable(cdr_db).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_136));
                                acc_scad_voce.setLinea_attivita(((WorkpackageBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext,
                                        new WorkpackageBulk(fatrs.getCdr(), fatrs.getGae()))));
                                if (acc_scad_voce.getLinea_attivita() != null) {
                                    if (acc_scad_voce.getLinea_attivita().getTi_gestione().compareTo(acc.getTi_gestione()) != 0)
                                        throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_138);
                                    acc_scadenza.getAccertamento_scad_voceColl().add((acc_scad_voce));
                                } else {
                                    throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_137);
                                }
                            }
                            // fine for listOfScad
                            acc = (AccertamentoBulk) accertamentoComponentSession.creaConBulk(userContext, acc);
                            fatr.setPg_accertamento_scadenzario(acc.getAccertamento_scadenzarioColl().get(0).getPg_accertamento_scadenzario());
                            for (Accertamento_scadenzarioBulk scad : acc.getAccertamento_scadenzarioColl()) {
                                BulkList<Fattura_attiva_rigaBulk> righesel = new BulkList<Fattura_attiva_rigaBulk>();
                                righesel.add(riga);
                                testata = fatturaAttivaSingolaComponentSession.contabilizzaDettagliSelezionati(userContext, testata, righesel, scad);
                                //testata.addToFattura_attiva_accertamentiHash(scad, riga);
                                testata.addToDefferredSaldi(scad.getAccertamento(), scad.getAccertamento().getSaldiInfo());
                            }
                            //intrastat
                            List<FatturaAttivaIntra> listOfIntra = fattura.getRigheIntra();
                            boolean obbligatorio = false;
                            for (Fattura_attiva_rigaBulk dettaglio : testata.getFattura_attiva_dettColl()) {
                                if (dettaglio.getBene_servizio() != null && dettaglio.getBene_servizio().getFl_obb_intrastat_ven().booleanValue())
                                    obbligatorio = true;
                            }

                            if (fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue()) {
                                if (listOfIntra != null && !listOfIntra.isEmpty() &&
                                        testata.getCliente() != null && testata.getCliente().getAnagrafico() != null &&
                                        testata.getCliente().getAnagrafico().getPartita_iva() != null && obbligatorio) {
                                    for (FatturaAttivaIntra intra : listOfIntra) {
                                        Fattura_attiva_intraBulk fa_intra = new Fattura_attiva_intraBulk();
                                        fa_intra.setUtcr(testata.getUtcr());
                                        fa_intra.setToBeCreated();
                                        fa_intra.setFattura_attiva(testata);
                                        if (testata.getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO) == 0) {
                                            fa_intra.setModalita_erogazione(((Modalita_erogazioneBulk) fatturaAttivaSingolaComponentSession.completaOggetto(
                                                    userContext, new Modalita_erogazioneBulk(testata.getEsercizio(), intra.getCod_erogazione()))));
                                            Optional.ofNullable(fa_intra.getModalita_erogazione()).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_143));

                                            fa_intra.setModalita_incasso(Optional.ofNullable(((Modalita_incassoBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Modalita_incassoBulk(testata.getEsercizio(), intra.getCod_incasso())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_144)));

                                            fa_intra.setCodici_cpa(Optional.ofNullable(((Codici_cpaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Codici_cpaBulk(intra.getId_cpa())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_145)));

                                            if (!fa_intra.getCodici_cpa().getEsercizio().equals(testata.getEsercizio())) {
                                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_145);
                                            }
                                        } else {
                                            // per il momento non richiesto da Pisa da testare totalmente anche i WS di servizio
                                            fa_intra.setNomenclatura_combinata(Optional.ofNullable(((Nomenclatura_combinataBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Nomenclatura_combinataBulk(intra.getId_nomenclatura_combinata())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_147)));
                                            if (!fa_intra.getNomenclatura_combinata().getEsercizio().equals(testata.getEsercizio())) {
                                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_147);
                                            }

                                            fa_intra.setNatura_transazione(Optional.ofNullable(((Natura_transazioneBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Natura_transazioneBulk(intra.getId_natura_transazione())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_148)));

                                            fa_intra.setCondizione_consegna(Optional.ofNullable(((Condizione_consegnaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Condizione_consegnaBulk(intra.getCd_consegna(), testata.getEsercizio())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_149)));

                                            fa_intra.setModalita_trasporto(Optional.ofNullable(((Modalita_trasportoBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Modalita_trasportoBulk(intra.getCd_trasporto(), testata.getEsercizio())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_150)));

                                            fa_intra.setProvincia_origine(Optional.ofNullable(((ProvinciaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new ProvinciaBulk(intra.getCd_provincia())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_151)));

                                            if (intra.getMassa() != null)
                                                fa_intra.setMassa_netta(intra.getMassa());
                                            if (intra.getValore_statistico() != null)
                                                fa_intra.setValore_statistico(intra.getValore_statistico());
                                            if (intra.getUnita_supplementari() != null)
                                                fa_intra.setUnita_supplementari(intra.getUnita_supplementari());
                                        }
                                        fa_intra.setNazione_destinazione(Optional.ofNullable(((NazioneBulk) fatturaAttivaSingolaComponentSession.
                                                        completaOggetto(userContext, new NazioneBulk(intra.getPg_nazione())))).
                                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_146)));

                                        fa_intra.setAmmontare_euro(intra.getAmmontare_euro());
                                        fa_intra.setFl_inviato(false);
                                        testata.getFattura_attiva_intrastatColl().add(fa_intra);
                                    }// fine for listOfIntra
                                }// fine if intra
                            }//fine intrastat
                        } else { //fine non Nota Credito
                            //Nota Credito
                            ObbligazioneBulk obb = new ObbligazioneBulk();
                            obb.setToBeCreated();
                            obb.setEsercizio(riga.getEsercizio());
                            obb.setEsercizio_originale(riga.getEsercizio());
                            obb.setCds((it.cnr.contab.config00.sto.bulk.CdsBulk) fatturaAttivaSingolaComponentSession.
                                    completaOggetto(userContext, new CdsBulk(riga.getCd_cds())));
                            obb.setCd_unita_organizzativa(riga.getCd_unita_organizzativa());
                            obb.setCd_cds_origine(testata.getCd_cds_origine());
                            obb.setCd_uo_origine(testata.getCd_uo_origine());
                            obb.setCd_tipo_documento_cont(Numerazione_doc_contBulk.TIPO_OBB);
                            obb.setFl_pgiro(new Boolean(false));
                            obb.setRiportato("N");
                            obb.setFromDocAmm(new Boolean(true));
                            obb.setFl_calcolo_automatico(new Boolean(false));
                            obb.setFl_spese_costi_altrui(new Boolean(false));
                            obb.setFl_gara_in_corso(new Boolean(false));
                            obb.setUtcr(testata.getUtcr());
                            obb.setUser(testata.getUtcr());
                            it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk voce = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
                            voce.setEsercizio(obb.getEsercizio());
                            voce.setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
                            voce.setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
                            voce.setCd_elemento_voce(fatr.getCd_voce());
                            //voce.setCd_unita_organizzativa(testata.getCd_uo_origine());
                            voce = ((it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, voce));
                            Optional.ofNullable(voce).orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_107));
                            obb.setElemento_voce(((Elemento_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new Elemento_voceBulk(voce.getCd_elemento_voce(), voce.getEsercizio(), voce.getTi_appartenenza(), voce.getTi_gestione()))));
                            obb = obbligazioneComponentSession.listaCapitoliPerCdsVoce(userContext, obb);
                            obb.setCapitoliDiSpesaCdsSelezionatiColl(obb.getCapitoliDiSpesaCdsColl());
                            obb.setDt_registrazione((DateUtils.truncate(new Timestamp(fatr.getDt_scadenza().getTime()))));
                            if (fatr.getDs_obbligazione() != null)
                                obb.setDs_obbligazione(fatr.getDs_obbligazione());
                            if (fatr.getMotivazione() != null)
                                obb.setMotivazione(fatr.getMotivazione());
                            //??? importo riga?????
                            obb.setIm_obbligazione(riga.getIm_totale_divisa());
                            obb.setIm_costi_anticipati(new java.math.BigDecimal(0));
                            //??
                            obb.setStato_obbligazione(ObbligazioneBulk.STATO_OBB_DEFINITIVO);
                            obb.setUser(obb.getUtcr());
                            obb.setCd_terzo(testata.getCd_terzo());
                            obb.setEsercizio_competenza(testata.getEsercizio());

                            Obbligazione_scadenzarioBulk obb_scadenza = new Obbligazione_scadenzarioBulk();
                            obb_scadenza.setUtcr(obb.getUtcr());
                            obb_scadenza.setToBeCreated();

                            obb_scadenza.setObbligazione(obb);
                            obb_scadenza.setDt_scadenza(obb.getDt_registrazione());
                            obb_scadenza.setDs_scadenza(obb.getDs_obbligazione());
                            obb.addToObbligazione_scadenzarioColl(obb_scadenza);
                            obb_scadenza.setIm_scadenza(obb.getIm_obbligazione());
                            obb_scadenza.setIm_associato_doc_amm(riga.getIm_totale_divisa());
                            obb_scadenza.setIm_associato_doc_contabile(new BigDecimal(0));
                            if (fatr.getEsercizio_contratto() != null && fatr.getStato_contratto() != null && fatr.getPg_contratto() != null) {
                                obb.setContratto(new ContrattoBulk(fatr.getEsercizio_contratto(), fatr.getStato_contratto(), fatr.getPg_contratto()));
                                obb.setContratto(Optional.ofNullable((ContrattoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, obb.getContratto())).
                                        orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_104)));
                                obb.setCheckDisponibilitaContrattoEseguito(true);
                            } else {
                                if ((fatr.getEsercizio_contratto() != null || fatr.getStato_contratto() != null || fatr.getPg_contratto() != null) &&
                                        (fatr.getEsercizio_contratto() == null && fatr.getStato_contratto() == null && fatr.getPg_contratto() == null))
                                    throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_108);
                            }
                            List<FatturaAttivaScad> listOfScad = fatr.getRighescadvoc();
                            for (FatturaAttivaScad fatrs : listOfScad) {
                                Obbligazione_scad_voceBulk obb_scad_voce = new Obbligazione_scad_voceBulk();
                                obb_scad_voce.setUtcr(testata.getUtcr());
                                obb_scad_voce.setToBeCreated();
                                obb_scad_voce.setObbligazione_scadenzario(obb_scadenza);
                                obb_scad_voce.setIm_voce(fatrs.getIm_voce());
                                obb_scad_voce.setCd_voce(voce.getCd_voce());
                                obb_scad_voce.setTi_gestione(voce.getTi_gestione());
                                obb_scad_voce.setTi_appartenenza(voce.getTi_appartenenza());
                                Optional.ofNullable(fatturaAttivaSingolaComponentSession.completaOggetto(userContext, new CdrBulk(fatrs.getCdr()))).
                                        orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_136));
                                obb_scad_voce.setLinea_attivita(Optional.ofNullable(((WorkpackageBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext,
                                                new WorkpackageBulk(fatrs.getCdr(), fatrs.getGae())))).
                                        orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_137)));
                                if (obb_scad_voce.getLinea_attivita().getTi_gestione().compareTo(obb.getTi_gestione()) != 0)
                                    throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_138);
                                Linea_attivitaBulk nuovaLatt = new Linea_attivitaBulk();
                                nuovaLatt.setLinea_att(obb_scad_voce.getLinea_attivita());
                                if (obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza().compareTo(new BigDecimal(0)) != 0)
                                    nuovaLatt.setPrcImputazioneFin(obb_scad_voce.getIm_voce().multiply(new BigDecimal(100)).divide(
                                            obb_scad_voce.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP));
                                nuovaLatt.setObbligazione(obb);
                                obb.getNuoveLineeAttivitaColl().add(nuovaLatt);
                                obb_scadenza.getObbligazione_scad_voceColl().add((obb_scad_voce));
                            }
                            //intrastat
                            java.util.ArrayList<FatturaAttivaIntra> listOfIntra = fattura.getRigheIntra();
                            boolean obbligatorio = testata.getFattura_attiva_dettColl().stream().filter(
                                    x -> Optional.ofNullable(x.getBene_servizio()).get().getFl_obb_intrastat_ven() == true
                            ).findAny().isPresent();

                            if (Optional.ofNullable(fattura.getFl_intra_ue()).get()) {
                                if (listOfIntra != null && !listOfIntra.isEmpty() &&
                                        testata.getCliente() != null && testata.getCliente().getAnagrafico() != null &&
                                        testata.getCliente().getAnagrafico().getPartita_iva() != null && obbligatorio) {
                                    for (FatturaAttivaIntra intra : fattura.getRigheIntra()) {
                                        Fattura_attiva_intraBulk fa_intra = new Fattura_attiva_intraBulk();
                                        fa_intra.setUtcr(testata.getUtcr());
                                        fa_intra.setToBeCreated();
                                        fa_intra.setFattura_attiva(testata);
                                        if (testata.getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO) == 0) {
                                            fa_intra.setModalita_erogazione(Optional.ofNullable(((Modalita_erogazioneBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Modalita_erogazioneBulk(testata.getEsercizio(), intra.getCod_erogazione())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_143)));

                                            fa_intra.setModalita_incasso(Optional.ofNullable(((Modalita_incassoBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Modalita_incassoBulk(testata.getEsercizio(), intra.getCod_incasso())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_144)));

                                            fa_intra.setCodici_cpa(Optional.ofNullable(((Codici_cpaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Codici_cpaBulk(intra.getId_cpa())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_145)));
                                            if (fa_intra.getCodici_cpa().getEsercizio() != testata.getEsercizio())
                                                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_145);
                                        } else {
                                            // per il momento non richiesto da Pisa da testare totalmente anche i WS di servizio
                                            fa_intra.setNomenclatura_combinata(Optional.ofNullable(((Nomenclatura_combinataBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Nomenclatura_combinataBulk(intra.getId_nomenclatura_combinata())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_147)));

                                            fa_intra.setNatura_transazione(Optional.ofNullable(((Natura_transazioneBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Natura_transazioneBulk(intra.getId_natura_transazione())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_148)));

                                            fa_intra.setCondizione_consegna(Optional.ofNullable(((Condizione_consegnaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Condizione_consegnaBulk(intra.getCd_consegna(), testata.getEsercizio())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_149)));

                                            fa_intra.setModalita_trasporto(Optional.ofNullable(((Modalita_trasportoBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new Modalita_trasportoBulk(intra.getCd_trasporto(), testata.getEsercizio())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_150)));

                                            fa_intra.setProvincia_origine(Optional.ofNullable(((ProvinciaBulk) fatturaAttivaSingolaComponentSession.
                                                            completaOggetto(userContext, new ProvinciaBulk(intra.getCd_provincia())))).
                                                    orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_151)));
                                            if (intra.getMassa() != null)
                                                fa_intra.setMassa_netta(intra.getMassa());
                                            if (intra.getValore_statistico() != null)
                                                fa_intra.setValore_statistico(intra.getValore_statistico());
                                            if (intra.getUnita_supplementari() != null)
                                                fa_intra.setUnita_supplementari(intra.getUnita_supplementari());

                                        }
                                        fa_intra.setNazione_destinazione(Optional.ofNullable(((NazioneBulk) fatturaAttivaSingolaComponentSession.
                                                        completaOggetto(userContext, new NazioneBulk(intra.getPg_nazione())))).
                                                orElseThrow(() -> FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_146)));
                                        fa_intra.setAmmontare_euro(intra.getAmmontare_euro());
                                        fa_intra.setFl_inviato(false);
                                        testata.getFattura_attiva_intrastatColl().add(fa_intra);
                                    }// fine for listOfIntra
                                }// fine if(fat.getFl_intra_ue()!=null && fat.getFl_intra_ue().booleanValue()){
                            }//fine intrastat

                            //fine for listOfScad
                            obb = (ObbligazioneBulk) obbligazioneComponentSession.creaConBulk(userContext, obb);
                            fatr.setPg_obbligazione_scadenzario(obb.getObbligazione_scadenzarioColl().get(0).getPg_obbligazione_scadenzario());
                            obb_scadenza.setObbligazione(obb);
                            Nota_di_credito_attivaBulk nc = (Nota_di_credito_attivaBulk) testata;
                            Nota_di_credito_attiva_rigaBulk riga_nc = (Nota_di_credito_attiva_rigaBulk) riga;
                            riga_nc.setObbligazione_scadenzario(obb_scadenza);
                            Hashtable<Nota_di_credito_attiva_rigaBulk, Obbligazione_scadenzarioBulk> relationsHash = new Hashtable<Nota_di_credito_attiva_rigaBulk, Obbligazione_scadenzarioBulk>();
                            relationsHash.put(riga_nc, riga_nc.getObbligazione_scadenzario());

                            nc = fatturaAttivaSingolaComponentSession.stornaDettagli(userContext, nc, nc.getFattura_attiva_dettColl(), relationsHash);
                            nc.addToDefferredSaldi(obb_scadenza.getObbligazione(), obb_scadenza.getObbligazione().getSaldiInfo());
                            testata = nc;
                        }
                        // Fine Nota Credito
                    }
                    //fine righe
                    if (fattura.getCod_errore() == null) {
                        testata = (Fattura_attivaBulk) fatturaAttivaSingolaComponentSession.creaConBulk(userContext, testata);
                        fattura.setPg_fattura_attiva(testata.getPg_fattura_attiva());
                        fattura.setIm_totale_imponibile(testata.getIm_totale_imponibile());
                        fattura.setIm_totale_iva(testata.getIm_totale_iva());
                        for (int ra = 0; ra < fattura.getRighefat().size(); ra++) {
                            FatturaAttivaRiga fatr_agg = listOfRighe.get(ra);
                            if (!(testata.getTi_fattura().compareTo(Fattura_attivaBulk.TIPO_NOTA_DI_CREDITO) == 0)) {
                                Fattura_attiva_rigaIBulk riga_fat = (Fattura_attiva_rigaIBulk) testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
                                        new Fattura_attiva_rigaIBulk(testata.getCd_cds(), testata.getCd_unita_organizzativa(), testata.getEsercizio(), testata.getPg_fattura_attiva(), fatr_agg.getProgressivo_riga())));
                                fatr_agg.setPg_accertamento(riga_fat.getAccertamento_scadenzario().getPg_accertamento());
                            } else {
                                Nota_di_credito_attiva_rigaBulk riga_fat = (Nota_di_credito_attiva_rigaBulk) testata.getFattura_attiva_dettColl().get(testata.getFattura_attiva_dettColl().indexOfByPrimaryKey(
                                        new Nota_di_credito_attiva_rigaBulk(testata.getCd_cds(), testata.getCd_unita_organizzativa(), testata.getEsercizio(), testata.getPg_fattura_attiva(), new Long(fatr_agg.getProgressivo_riga()))));
                                fatr_agg.setPg_obbligazione(riga_fat.getObbligazione_scadenzario().getPg_obbligazione());
                            }
                        }
                    } else {
                        fatturaAttivaSingolaComponentSession.rollbackToSavePoint(userContext, "Fattura_automatica");
                    }

                    fattureCreate.add(testata);
                }

            } catch (RemoteException | ComponentException | PersistencyException | ValidationException | IntrospectionException ex) {
                LOGGER.error("ERROR while importing ", ex);
                if (ex instanceof ApplicationException) {
                    throw new FatturaAttivaException(Status.BAD_REQUEST, ex.getMessage(), FatturaAttivaCodiciEnum.ERRORE_FA_999);
                }
                throw FatturaAttivaException.newInstance(Status.BAD_REQUEST, FatturaAttivaCodiciEnum.ERRORE_FA_999);
            }

        });
        return Response.status(Status.CREATED).entity(fattureCreate.stream().map(fattura_attivaBulk -> {
            return convert(userContext, fattura_attivaBulk);
        }).collect(Collectors.toList())).build();
    }

    @Override
    public Response inserisciDatiPerStampa(HttpServletRequest request, Integer esercizio, Long pgFattura) throws Exception {
        LOGGER.debug("REST request per stampa IVA di una fattura attiva.");
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        try {
            final Long pgStampa = fatturaAttivaSingolaComponentSession
                    .inserisciDatiPerStampaIva(userContext,
                            esercizio.longValue(),
                            userContext.getCd_cds(),
                            userContext.getCd_unita_organizzativa(),
                            pgFattura);
            return Response.ok().entity(pgStampa).build();
        } catch (FatturaNonTrovataException _ex) {
            return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        } catch (FatturaNonProtocollataException _ex) {
            return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PROTOCOLLATA)).build();
        }
    }

    @Override
    public Response stampaFattura(@Context HttpServletRequest request, Long pgStampa) throws Exception {
        LOGGER.debug("REST request per stampa una fattura attiva.");
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        try {
            byte[] stampa = fatturaAttivaSingolaComponentSession.lanciaStampa(userContext, pgStampa);
            return Response.ok().entity(stampa).build();
        } catch (FatturaNonTrovataException _ex) {
            return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", FATTURA_ATTIVA_NON_PRESENTE)).build();
        }
    }

    private FatturaAttiva convert(UserContext userContext, Fattura_attivaBulk fatturaAttivaBulk) {
        try {
            FatturaAttiva fatturaAttiva = new FatturaAttiva();
            java.util.ArrayList<FatturaAttivaRiga> righe = new java.util.ArrayList<FatturaAttivaRiga>();
            java.util.ArrayList<FatturaAttivaScad> righescad = new java.util.ArrayList<FatturaAttivaScad>();
            java.util.ArrayList<FatturaAttivaIntra> righeIntra = new java.util.ArrayList<FatturaAttivaIntra>();
            FatturaAttivaRiga rigaRitorno = new FatturaAttivaRiga();
            FatturaAttivaScad scad = new FatturaAttivaScad();
            FatturaAttivaIntra intra = new FatturaAttivaIntra();

            fatturaAttiva.setCambio(fatturaAttivaBulk.getCambio());
            fatturaAttiva.setCd_cds_origine(fatturaAttivaBulk.getCd_cds_origine());
            fatturaAttiva.setCd_divisa(fatturaAttivaBulk.getCd_divisa());

            fatturaAttiva.setCd_modalita_pag_uo_cds(fatturaAttivaBulk.getModalita_pagamento_uo().getCd_modalita_pag());
            fatturaAttiva.setCd_terzo(fatturaAttivaBulk.getCd_terzo());
            fatturaAttiva.setCd_terzo_uo_cds(fatturaAttivaBulk.getCd_terzo_uo_cds());
            fatturaAttiva.setCd_tipo_sezionale(fatturaAttivaBulk.getCd_tipo_sezionale());
            fatturaAttiva.setCd_uo_origine(fatturaAttivaBulk.getCd_uo_origine());
            fatturaAttiva.setDs_fattura_attiva(fatturaAttivaBulk.getDs_fattura_attiva());
            fatturaAttiva.setDt_registrazione(fatturaAttivaBulk.getDt_registrazione());
            fatturaAttiva.setEsercizio(fatturaAttivaBulk.getEsercizio());
            fatturaAttiva.setFl_extra_ue(fatturaAttivaBulk.getFl_extra_ue());
            fatturaAttiva.setFl_intra_ue(fatturaAttivaBulk.getFl_intra_ue());
            fatturaAttiva.setFl_liquidazione_differita(fatturaAttivaBulk.getFl_liquidazione_differita());
            fatturaAttiva.setFl_san_marino(fatturaAttivaBulk.getFl_san_marino());
            fatturaAttiva.setNote(fatturaAttivaBulk.getNote());
            fatturaAttiva.setPg_banca_uo_cds(fatturaAttivaBulk.getBanca_uo().getPg_banca());
            fatturaAttiva.setPg_fattura_attiva(fatturaAttivaBulk.getPg_fattura_attiva());
            fatturaAttiva.setPg_fattura_esterno(fatturaAttivaBulk.getPg_fattura_esterno());
            fatturaAttiva.setRif_ordine(fatturaAttivaBulk.getRiferimento_ordine());
            fatturaAttiva.setTi_causale_emissione(fatturaAttivaBulk.getTi_causale_emissione());
            fatturaAttiva.setTi_fattura(fatturaAttivaBulk.getTi_fattura());
            fatturaAttiva.setUtcr(fatturaAttivaBulk.getUtcr());
            fatturaAttiva.setNr_protocollo_iva(fatturaAttivaBulk.getProtocollo_iva());
            fatturaAttiva.setDt_protocollo(fatturaAttivaBulk.getDt_emissione());
            fatturaAttiva.setFl_pagamento_anticipato(fatturaAttivaBulk.getFl_pagamento_anticipato());
            for (Iterator i = fatturaAttivaBulk.getFattura_attiva_intrastatColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_intraBulk riga_intra = (Fattura_attiva_intraBulk) i.next();
                intra = new FatturaAttivaIntra();
                intra.setAmmontare_euro(riga_intra.getAmmontare_euro());
                intra.setCod_erogazione(riga_intra.getCd_modalita_erogazione());
                intra.setCod_incasso(riga_intra.getCd_modalita_incasso());
                intra.setId_cpa(riga_intra.getId_cpa());
                intra.setPg_nazione(riga_intra.getPg_nazione_destinazione());
                righeIntra.add(intra);
            }
            fatturaAttiva.setRigheIntra(righeIntra);

            for (Iterator i = fatturaAttivaBulk.getFattura_attiva_dettColl().iterator(); i.hasNext(); ) {
                Fattura_attiva_rigaBulk riga = (Fattura_attiva_rigaBulk) i.next();
                rigaRitorno = new FatturaAttivaRiga();
                rigaRitorno.setCd_tariffario(riga.getCd_tariffario());
                rigaRitorno.setCd_voce_iva(riga.getCd_voce_iva());
                rigaRitorno.setDs_riga_fattura(riga.getDs_riga_fattura());
                rigaRitorno.setFl_iva_forzata(riga.getFl_iva_forzata());
                rigaRitorno.setEsercizio_assncna_fin(riga.getEsercizio_assncna_fin());
                rigaRitorno.setPg_fattura_assncna_fin(riga.getPg_fattura_assncna_fin());
                rigaRitorno.setPg_riga_assncna_fin(riga.getPg_riga_assncna_fin());
                rigaRitorno.setPrezzo_unitario(riga.getPrezzo_unitario());
                rigaRitorno.setProgressivo_riga(riga.getProgressivo_riga());
                rigaRitorno.setQuantita(riga.getQuantita());
                if (fatturaAttivaBulk instanceof Fattura_attiva_IBulk) {
                    AccertamentoBulk acc_db = new AccertamentoBulk(riga.getAccertamento_scadenzario().getAccertamento().getCd_cds(), riga.getAccertamento_scadenzario().getAccertamento().getEsercizio(), riga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale(), riga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento());
                    acc_db = (((AccertamentoBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, acc_db)));
                    rigaRitorno.setCd_voce(acc_db.getCapitolo().getCd_voce());
                    rigaRitorno.setDs_accertamento(acc_db.getDs_accertamento());
                    rigaRitorno.setEsercizio_contratto(acc_db.getEsercizio_contratto());
                    rigaRitorno.setStato_contratto(acc_db.getStato_contratto());
                    rigaRitorno.setPg_contratto(acc_db.getPg_contratto());
                    rigaRitorno.setPg_accertamento(acc_db.getPg_accertamento());
                    Accertamento_scadenzarioBulk acc_scad_db = new Accertamento_scadenzarioBulk(riga.getAccertamento_scadenzario().getAccertamento().getCd_cds(), riga.getAccertamento_scadenzario().getAccertamento().getEsercizio(), riga.getAccertamento_scadenzario().getAccertamento().getEsercizio_originale(), riga.getAccertamento_scadenzario().getAccertamento().getPg_accertamento(), riga.getAccertamento_scadenzario().getPg_accertamento_scadenzario());
                    acc_scad_db = (((Accertamento_scadenzarioBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, acc_scad_db)));
                    rigaRitorno.setDt_scadenza(acc_scad_db.getDt_scadenza_incasso());
                    rigaRitorno.setPg_accertamento_scadenzario(acc_scad_db.getPg_accertamento_scadenzario());
                    List scadenzevoce = fatturaAttivaSingolaComponentSession.recuperoScadVoce(userContext, riga.getAccertamento_scadenzario());
                    for (Iterator s = scadenzevoce.iterator(); s.hasNext(); ) {
                        scad = new FatturaAttivaScad();
                        Accertamento_scad_voceBulk scadVoce = (Accertamento_scad_voceBulk) s.next();
                        scadVoce = (((Accertamento_scad_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, scadVoce)));
                        scad.setCdr(scadVoce.getLinea_attivita().getCd_centro_responsabilita());
                        scad.setGae(scadVoce.getLinea_attivita().getCd_linea_attivita());
                        scad.setIm_voce(scadVoce.getIm_voce());
                        righescad.add(scad);
                    }
                    rigaRitorno.setRighescadvoc(righescad);
                } else {
                    Nota_di_credito_attivaBulk nc = (Nota_di_credito_attivaBulk) fatturaAttivaBulk;
                    Nota_di_credito_attiva_rigaBulk nc_riga = (Nota_di_credito_attiva_rigaBulk) riga;
                    fatturaAttiva.setCd_modalita_pag(nc.getModalita_pagamento().getCd_modalita_pag());
                    fatturaAttiva.setPg_banca(nc.getBanca().getPg_banca());
                    ObbligazioneBulk obb_db = nc_riga.getObbligazione_scadenzario().getObbligazione();
                    obb_db = (((ObbligazioneBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, obb_db)));
                    rigaRitorno.setCd_voce(obb_db.getCd_elemento_voce());
                    rigaRitorno.setDs_obbligazione(obb_db.getDs_obbligazione());
                    rigaRitorno.setMotivazione(obb_db.getMotivazione());
                    rigaRitorno.setEsercizio_contratto(obb_db.getEsercizio_contratto());
                    rigaRitorno.setStato_contratto(obb_db.getStato_contratto());
                    rigaRitorno.setPg_contratto(obb_db.getPg_contratto());
                    rigaRitorno.setPg_obbligazione(obb_db.getPg_obbligazione());
                    Obbligazione_scadenzarioBulk obb_scad_db = nc_riga.getObbligazione_scadenzario();
                    obb_scad_db = (((Obbligazione_scadenzarioBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, obb_scad_db)));
                    rigaRitorno.setDt_scadenza(obb_scad_db.getDt_scadenza());
                    rigaRitorno.setPg_obbligazione_scadenzario(obb_scad_db.getPg_obbligazione_scadenzario());
                    java.util.List scadenzevoce = fatturaAttivaSingolaComponentSession.recuperoScadVoce(userContext, obb_scad_db);
                    for (Iterator s = scadenzevoce.iterator(); s.hasNext(); ) {
                        scad = new FatturaAttivaScad();
                        Obbligazione_scad_voceBulk scadVoce = (Obbligazione_scad_voceBulk) s.next();
                        scadVoce = (((Obbligazione_scad_voceBulk) fatturaAttivaSingolaComponentSession.completaOggetto(userContext, scadVoce)));
                        scad.setCdr(scadVoce.getLinea_attivita().getCd_centro_responsabilita());
                        scad.setGae(scadVoce.getLinea_attivita().getCd_linea_attivita());
                        scad.setIm_voce(scadVoce.getIm_voce());
                        righescad.add(scad);
                    }
                    rigaRitorno.setRighescadvoc(righescad);
                }
                righe.add(rigaRitorno);
                righescad = new java.util.ArrayList<FatturaAttivaScad>();
            }
            fatturaAttiva.setRighefat(righe);
            if (fatturaAttivaBulk.getProtocollo_iva() != null) {
                fatturaAttiva.setNumeroFattura(fatturaAttivaBulk.recuperoIdFatturaAsString());
            }
            return fatturaAttiva;
        } catch (ComponentException | RemoteException | PersistencyException e) {
            throw new RuntimeException(e);
        }
    }
}