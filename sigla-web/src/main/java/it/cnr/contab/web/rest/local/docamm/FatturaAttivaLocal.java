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

package it.cnr.contab.web.rest.local.docamm;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.util.enumeration.AccessoEnum;
import it.cnr.contab.web.rest.config.SIGLASecurityContext;

import java.util.List;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/fatturaattiva")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api("Fatturazione Attiva")
public interface FatturaAttivaLocal {
	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    @ApiOperation(value = "Recupera i dati della Fattura Attiva",
            notes = "Accesso consentito solo alle utenze abilitate con accesso AMMFATTURDOCSFATATTV",
            response = Fattura_attivaBulk.class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    Response ricercaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception;

	/**
	 * POST  /restapi/fatturaattiva-> return Fattura attiva
	 */
	@POST
	@AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTM)
    @ApiOperation(value = "Inserisce una o più Fatture Attive",
            notes = "Accesso consentito solo alle utenze abilitate con accesso AMMFATTURDOCSFATATTM",
            response = Fattura_attivaBulk.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    Response inserisciFatture(@Context HttpServletRequest request, List<FatturaAttiva> fatture) throws Exception;
	
	/**
     * GET  /restapi/fatturaattiva/ricerca -> return Fattura attiva
     */
    @GET
    @Path("/print")
    @AccessoAllowed(value=AccessoEnum.AMMFATTURDOCSFATATTV)
    @Produces("application/pdf")
    @ApiOperation(value = "Fornisce la stampa della Fattura Attiva in pdf",
            notes = "Accesso consentito solo alle utenze abilitate con accesso AMMFATTURDOCSFATATTV",
            response = byte[].class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    Response stampaFattura(@Context HttpServletRequest request, @QueryParam ("pg") Long pg) throws Exception;

}
