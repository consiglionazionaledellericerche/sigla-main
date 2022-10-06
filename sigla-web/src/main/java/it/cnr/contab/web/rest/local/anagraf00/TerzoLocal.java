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

package it.cnr.contab.web.rest.local.anagraf00;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import it.cnr.contab.anagraf00.core.bulk.RapportoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.contab.web.rest.model.AnagraficaInfoDTO;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/terzo")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.TERZO)
@Api("Terzo")
public interface TerzoLocal {

	@POST
    @ApiOperation(value = "Aggiorna un terzo",
            notes = "Accesso consentito solo alle utenze abilitate e con ruolo '" + SIGLARoles.TERZO +"'",
            response = TerzoBulk.class,
            authorizations = @Authorization(value = "BASIC")
    )
    Response update(@Context HttpServletRequest request, TerzoBulk terzoBulk) throws Exception;

	@GET
    @Path("/tiporapporto/{codicefiscale}")
    @ApiOperation(value = "Ritorna i rapporti associati al terzo",
            notes = "Accesso consentito solo alle utenze abilitate e con ruolo '" + SIGLARoles.TERZO +"'",
            response = RapportoBulk.class,
            responseContainer = "List",
            authorizations = @Authorization(value = "BASIC")
    )
    Response tipoRapporto(@PathParam("codicefiscale") String codicefiscale) throws Exception;

    @GET
    @Path("/info/{codicefiscale}")
    @ApiOperation(value = "Ritorna le informazioni anagrafiche associate al terzo",
            notes = "Accesso consentito solo alle utenze abilitate e con ruolo '" + SIGLARoles.TERZO +"'",
            response = AnagraficaInfoDTO.class,
            authorizations = @Authorization(value = "BASIC")
    )
    Response anagraficaInfo(@PathParam("codicefiscale") String codicefiscale) throws Exception;

}
