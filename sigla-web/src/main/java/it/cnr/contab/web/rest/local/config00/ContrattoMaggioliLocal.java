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

package it.cnr.contab.web.rest.local.config00;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import it.cnr.contab.config00.contratto.bulk.ContrattoDatiSintesiBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.contab.web.rest.config.SIGLASecurityContext;
import it.cnr.contab.web.rest.model.ContrattoDtoBulk;
import it.cnr.contab.web.rest.model.ContrattoMaggioliDTOBulk;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/contrattoMaggioli")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
@Api("Contratti")
public interface ContrattoMaggioliLocal {
    /**
     * PUT  /restapi/contratto -> return Contratto
     */
    @POST
    @Valid
    @ApiOperation(value = "Inserisce un contratto",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo CONTRATTO",
            response = ContrattoDtoBulk.class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    public Response insertContratto(@Context HttpServletRequest request, @Valid ContrattoDtoBulk contrattoMaggioliBulk) throws Exception;

    /**
     * GET  /restapi/contratto -> return Contratto
     */
    @GET
    @PermitAll
    @ApiOperation(value = "Recupera i dati dei contratti",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo CONTRATTO",
            response = ContrattoDatiSintesiBulk.class,
            responseContainer = "List",
            authorizations = @Authorization(value = "BASIC")
    )
    Response recuperoDatiContratto(@Context HttpServletRequest request,
                                   @QueryParam("uo") String uo,
                                   @QueryParam("cdTerzo") Integer cdTerzo) throws Exception;
}
