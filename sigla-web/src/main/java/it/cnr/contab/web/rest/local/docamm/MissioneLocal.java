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
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;
import it.cnr.contab.web.rest.config.SIGLASecurityContext;
import it.cnr.contab.web.rest.model.MassimaleSpesaBulk;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/missioni")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.MISSIONI)
@Api("Missioni. Gestione dei rimborsi delle trasferte.")
public interface MissioneLocal {
    /**
     * POST  /restapi/missioni/validaMassimaleSpesa -> return OK o KO
     */
    @POST
    @Path(value = "/validaMassimaleSpesa")
    @ApiOperation(value = "Verifica se la spesa è consentina in funzione dei massimali presenti",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo MISSIONI",
            response = String.class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    Response validaMassimaleSpesa(@Context HttpServletRequest request, MassimaleSpesaBulk massimaleSpesaBulk) throws Exception;

    /**
     * PUT  /restapi/missioni -> return Missione
     */
    @PUT
    @ApiOperation(value = "Inserisce una missione.",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo MISSIONI",
            response = MissioneBulk.class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    public Response insert(@Context HttpServletRequest request, MissioneBulk missioneBulk) throws Exception;

    /**
     * DELETE  /restapi/missioni -> return OK
     */
    @DELETE
    @Path("{id}")
    @ApiOperation(value = "Elimina una missione.",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo MISSIONI",
            response = String.class,
            authorizations = {
                    @Authorization(value = "BASIC"),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_ESERCIZIO),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDS),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_UNITA_ORGANIZZATIVA),
                    @Authorization(value = SIGLASecurityContext.X_SIGLA_CD_CDR),
            }
    )
    public Response delete(@Context HttpServletRequest request, @PathParam("id") long idRimborsoMissione) throws Exception;
}
