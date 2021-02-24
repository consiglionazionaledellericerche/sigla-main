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

package it.cnr.contab.web.rest.resource.doccont;

import it.cnr.contab.doccont00.core.bulk.MandatoComunicaDatiBulk;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.doccont.ComunicaDatiPagamentiLocal;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.DateUtils;
import org.apache.commons.lang.StringUtils;
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
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class ComunicaDatiPagamentiResource implements ComunicaDatiPagamentiLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(ComunicaDatiPagamentiResource.class);
    @Context
    SecurityContext securityContext;

    @EJB
    CRUDComponentSession crudComponentSession;

    @Override
    public Response recuperoDatiPagamenti(@Context HttpServletRequest request, @QueryParam("esercizio") Integer esercizio,
                                          @QueryParam("cdCds") String cdCds,
                                          @QueryParam("pgMandato") Long pgMandato, @QueryParam("daData") String daData,
                                          @QueryParam("aData") String aData) throws Exception {
        LOGGER.debug("REST request per recupero Dati Pagamenti.");
        CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
        final MandatoComunicaDatiBulk mandatoComunicaDatiBulk = new MandatoComunicaDatiBulk();
        Optional.ofNullable(daData).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, data di inizio selezione obbligatoria."));
        Optional.ofNullable(aData).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, data di fine selezione obbligatoria."));
        Optional.ofNullable(esercizio).ifPresent(integer -> mandatoComunicaDatiBulk.setEsercizio(esercizio));
        Optional.ofNullable(cdCds).ifPresent(integer -> mandatoComunicaDatiBulk.setCd_cds(cdCds));
        Optional.ofNullable(pgMandato).ifPresent(integer -> mandatoComunicaDatiBulk.setPg_mandato(pgMandato));

        Timestamp startDate = getTimestamp(daData);
        Timestamp endDate = getTimestamp(aData);

        try {
            List<MandatoComunicaDatiBulk> dati =
                    crudComponentSession.find(userContext, MandatoComunicaDatiBulk.class, "recuperoDati", userContext, mandatoComunicaDatiBulk, startDate, endDate);

            LOGGER.debug("Fine REST per recupero Dati Pagamenti.");
            return Response.ok(Optional.ofNullable(dati).orElse(Collections.emptyList())).header("Keep-Alive","timeout=86400").build();
        } catch (Exception _ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(Collections.singletonMap("ERROR", _ex)).build();
        }
    }

    private Timestamp getTimestamp(String data) throws Exception {
        if (StringUtils.isEmpty(data)){
            return null;
        }
        String aPattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(aPattern);
        try {
            Date fromDate = sdf.parse(data);
            return new Timestamp(fromDate.getTime());
        } catch (ParseException e) {
            throw new Exception("La data " + data + " non è nel formato " + aPattern);
        }
    }
}