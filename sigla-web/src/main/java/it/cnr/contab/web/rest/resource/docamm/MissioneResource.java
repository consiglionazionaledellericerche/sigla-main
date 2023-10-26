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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.docamm.MissioneLocal;
import it.cnr.contab.web.rest.model.MassimaleSpesaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Stateless
public class MissioneResource implements MissioneLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(MissioneResource.class);
    @Context
    SecurityContext securityContext;
    @EJB
    CRUDComponentSession crudComponentSession;
    @EJB
    MissioneComponentSession missioneComponentSession;
    @EJB
    Unita_organizzativaComponentSession unita_organizzativaComponentSession;

    public Response validaMassimaleSpesa(@Context HttpServletRequest request, MassimaleSpesaBulk massimaleSpesaBulk) throws Exception {
        ResponseBuilder rb;
        try {
            LOGGER.debug("REST request per visualizzare la divisa per nazione");
            UserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
            Optional.ofNullable(massimaleSpesaBulk.getData()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, data dettaglio spesa obbligatoria."));
            Optional.ofNullable(massimaleSpesaBulk.getImportoSpesa()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, importo dettaglio spesa obbligatorio."));
            Optional.ofNullable(massimaleSpesaBulk.getDivisa()).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, divisa dettaglio spesa obbligatoria."));

            Timestamp dataMissione = new Timestamp(massimaleSpesaBulk.getData().getTime());
            Missione_tipo_spesaBulk tipoSpesa = null;

            NazioneBulk nazioneBulk = getNazione(userContext, massimaleSpesaBulk.getNazione());
            if (massimaleSpesaBulk.getCdTipoSpesa() != null) {
                List<?> lista = missioneComponentSession.recuperoTipiSpesa(userContext, dataMissione, massimaleSpesaBulk.getInquadramento(),
                        nazioneBulk.getPg_nazione(), false, massimaleSpesaBulk.getCdTipoSpesa());
                if (lista != null && !lista.isEmpty()) {
                    tipoSpesa = (Missione_tipo_spesaBulk) lista.get(0);
                }
                Optional.ofNullable(tipoSpesa).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipo Spesa non trovato in SIGLA."));
            } else {
                throw new RestException(Status.BAD_REQUEST, "Errore, parametro tipo spesa obbligatorio.");
            }

            Missione_tipo_pastoBulk tipoPasto = null;
            if (massimaleSpesaBulk.getCdTipoPasto() != null) {
                List<?> lista = missioneComponentSession.recuperoTipi_pasto(userContext, dataMissione, massimaleSpesaBulk.getInquadramento(), nazioneBulk, massimaleSpesaBulk.getCdTipoPasto(), null);
                if (lista != null && !lista.isEmpty()) {
                    tipoPasto = (Missione_tipo_pastoBulk) lista.get(0);
                }
                Optional.ofNullable(tipoPasto).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Tipo Pasto non trovato in SIGLA."));
            }
            MissioneBulk missioneBulk = new MissioneBulk();
            Missione_dettaglioBulk dettaglio = new Missione_dettaglioBulk();
            dettaglio.setTipo_pasto(tipoPasto);
            dettaglio.setTipo_spesa(tipoSpesa);
            dettaglio.setChilometri(Optional.ofNullable(massimaleSpesaBulk.getKm()).orElse(BigDecimal.ZERO));
            dettaglio.setPercentuale_maggiorazione(BigDecimal.ZERO);
            dettaglio.setIm_base_maggiorazione(BigDecimal.ZERO);
            dettaglio.setIm_spesa_euro(massimaleSpesaBulk.getImportoSpesa());
            dettaglio.setIm_spesa_divisa(dettaglio.getIm_spesa_euro());
            dettaglio.setCd_divisa_spesa(massimaleSpesaBulk.getDivisa());
            try {
                missioneComponentSession.validaMassimaliSpesa(userContext, missioneBulk, dettaglio);
            } catch (ValidationException e) {
                throw new RestException(Status.BAD_REQUEST, e.getMessage());
            }
            rb = Response.ok("OK");
        } catch (RestException restException) {
            rb = Response.status(restException.getStatus()).entity(Collections.singletonMap("ERROR", restException.getMessage()));
        }
        return rb.build();
    }

    public Response insert(@Context HttpServletRequest request, MissioneBulk missioneBulk) throws Exception {
        try {
            CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
            Optional.ofNullable(missioneBulk.getEsercizio()).filter(x -> userContext.getEsercizio().equals(x)).
                    orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Esercizio del contesto diverso da quello della Missione"));
            if (!isUoEnte(userContext)) {
                Optional.ofNullable(missioneBulk.getCd_cds()).filter(x -> userContext.getCd_cds().equals(x)).
                        orElseThrow(() -> new RestException(Status.BAD_REQUEST, "CdS del contesto diverso da quello della Missione"));
                Optional.ofNullable(missioneBulk.getCd_unita_organizzativa()).filter(x -> userContext.getCd_unita_organizzativa().equals(x)).
                        orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Unità Organizzativa del contesto diversa da quella della Missione"));
            }

            Calendar cal = Calendar.getInstance();
            missioneBulk.setObbligazione_scadenzario(Optional.ofNullable(missioneComponentSession.recuperoObbligazioneDaGemis(userContext, missioneBulk)).orElse(null));
            missioneBulk.setAnticipo(Optional.ofNullable(missioneComponentSession.recuperoAnticipoDaGemis(userContext, missioneBulk)).orElse(null));
            cal.setTime(missioneBulk.getDt_inizio_missione());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            missioneBulk.setDt_inizio_missione(new Timestamp(cal.getTimeInMillis()));
            cal.setTime(missioneBulk.getDt_fine_missione());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            missioneBulk.setDt_fine_missione(new Timestamp(cal.getTimeInMillis()));
            final MissioneBulk missione = (MissioneBulk) missioneComponentSession.inizializzaBulkPerInserimento(
                    userContext,
                    missioneBulk);

            missione.setToBeCreated();
            missione.getTappeMissioneColl().stream().forEach(x -> {
                x.setToBeCreated();
                x.setMissione(missione);
                cal.setTime(x.getDt_inizio_tappa());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                x.setDt_inizio_tappa(new Timestamp(cal.getTimeInMillis()));
                cal.setTime(x.getDt_fine_tappa());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                x.setDt_fine_tappa(new Timestamp(cal.getTimeInMillis()));
            });
            missione.getSpeseMissioneColl().stream().forEach(x -> {
                cal.setTime(x.getDt_inizio_tappa());
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                x.setDt_inizio_tappa(new Timestamp(cal.getTimeInMillis()));
            });
            Stream.concat(
                    Stream.concat(
                            missione.getSpeseMissioneColl().stream(),
                            missione.getDiariaMissioneColl().stream()
                    ),
                    missione.getRimborsoMissioneColl().stream()).forEach((x -> {
                x.setToBeCreated();
                x.setMissione(missione);
            }));
            MissioneBulk missioneCreated = (MissioneBulk) missioneComponentSession.creaConBulk(userContext, missione);
            missioneCreated.setToBeUpdated();
            missioneCreated.setMissioneIniziale(missioneCreated);
            missioneCreated = (MissioneBulk) missioneComponentSession.creaConBulk(userContext, missioneCreated);
            missioneCreated.setObbligazione_scadenzario(null);
            missioneCreated.setObbligazione_scadenzarioClone(null);
            return Response.status(Status.CREATED).entity(missioneCreated).build();
        } catch (ApplicationException _ex) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", _ex.getMessage())).build();
        }
    }

    private NazioneBulk getNazione(UserContext userContext, Long nazione) throws PersistencyException, ComponentException, RemoteException, EJBException {
        NazioneBulk nazioneBulk = new NazioneBulk(nazione);
        nazioneBulk = (NazioneBulk) crudComponentSession.findByPrimaryKey(userContext, nazioneBulk);
        return nazioneBulk;
    }

    private Unita_organizzativa_enteBulk getUoEnte(UserContext userContext)
            throws PersistencyException, ComponentException, java.rmi.RemoteException {
        Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) unita_organizzativaComponentSession.getUoEnte(userContext);
        return uoEnte;
    }

    private Boolean isUoEnte(UserContext userContext) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        return Optional.of(getUoEnte(userContext)).filter(x -> x.getCd_unita_organizzativa().equals(((CNRUserContext) userContext).getCd_unita_organizzativa())).isPresent();
    }

    public Response delete(@Context HttpServletRequest request, @PathParam("id") long idRimborsoMissione) throws Exception {
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        Optional.ofNullable(idRimborsoMissione).
                orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Id Rimborso missione Obbligatorio"));
        LOGGER.info("Inizio Cancellazione Missione " + idRimborsoMissione);
        try {
            missioneComponentSession.cancellazioneMissioneDaGemis(userContext, idRimborsoMissione);
        } catch (ApplicationException _ex) {
            return Response.status(Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", _ex.getMessage())).build();
        }
        return Response.ok("OK").build();

    }
}