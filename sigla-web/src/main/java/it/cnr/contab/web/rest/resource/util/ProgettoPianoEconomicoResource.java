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

package it.cnr.contab.web.rest.resource.util;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.web.rest.local.util.ProgettoPianoEconomicoLocal;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ProgettoPianoEconomicoResource implements ProgettoPianoEconomicoLocal {
    private transient static final Logger logger = LoggerFactory.getLogger(ProgettoPianoEconomicoResource.class);
    @EJB
    private SaldoComponentSession saldoComponentSession;
    @EJB
    CRUDComponentSession crudComponentSession;
    @Context
    SecurityContext securityContext;

    @Override
    public Response checkPdgPianoEconomico(HttpServletRequest request, String tipoVariazione, Integer esercizio, Long pgVariazioneMin, Long pgVariazioneMax) throws Exception {
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        if (tipoVariazione==null || esercizio==null || pgVariazioneMin==null)
            return Response.serverError().entity("Servizio non eseguibile. Parametri di lancio non corretti.").build();

        pgVariazioneMin = Optional.ofNullable(pgVariazioneMin).orElse(Long.valueOf(1));
        pgVariazioneMax = Optional.ofNullable(pgVariazioneMax).orElse(Long.valueOf(pgVariazioneMin));

        StringJoiner anomalie = new StringJoiner("   ********   ");
        if ("COM".equals(tipoVariazione)) {
            for (int pgVariazione = pgVariazioneMin.intValue(); pgVariazione <= pgVariazioneMax; pgVariazione++) {
                try {
                    OggettoBulk pdgVariazione = (OggettoBulk) crudComponentSession.findByPrimaryKey(userContext, new Pdg_variazioneBulk(esercizio, Long.valueOf(pgVariazione)));
                    if (Optional.ofNullable(pdgVariazione).filter(Pdg_variazioneBulk.class::isInstance).map(Pdg_variazioneBulk.class::cast)
                            .filter(el->!el.isAnnullata() && !el.isPropostaProvvisoria()).isPresent()) {
                        userContext.setCd_unita_organizzativa(((Pdg_variazioneBulk) pdgVariazione).getCentro_responsabilita().getCd_unita_organizzativa());
                        saldoComponentSession.checkPdgPianoEconomico(userContext, (Pdg_variazioneBulk) pdgVariazione);
                    }
                } catch (Exception e) {
                    anomalie.add("Variazione di competenza " + esercizio + "/" + pgVariazione + " - Errore: "+e.toString());
                }
            }
        } else if ("RES".equals(tipoVariazione)) {
            for (int pgVariazione = pgVariazioneMin.intValue(); pgVariazione <= pgVariazioneMax; pgVariazione++) {
                try {
                    OggettoBulk variazioneResidua = (OggettoBulk) crudComponentSession.findByPrimaryKey(userContext, new Var_stanz_resBulk(esercizio, Long.valueOf(pgVariazione)));
                    if (Optional.ofNullable(variazioneResidua).filter(Var_stanz_resBulk.class::isInstance).map(Var_stanz_resBulk.class::cast)
                            .filter(el->!el.isAnnullata() && !el.isPropostaProvvisoria()).isPresent()) {
                        userContext.setCd_unita_organizzativa(((Var_stanz_resBulk) variazioneResidua).getCd_cds() + ".000");
                        saldoComponentSession.checkPdgPianoEconomico(userContext, (Var_stanz_resBulk) variazioneResidua);
                    }
                } catch (Exception e) {
                    anomalie.add("Variazione residua " + esercizio + "/" + pgVariazione + " - Errore: "+e.toString());
                }
            }
        }
        if (anomalie.length()==0)
            return Response.ok().entity("Controllo terminato con successo.").build();
        else
            return Response.ok().entity(anomalie.toString()).build();
    }
}