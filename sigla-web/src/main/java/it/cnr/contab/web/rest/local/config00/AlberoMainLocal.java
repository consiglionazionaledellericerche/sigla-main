/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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
import it.cnr.contab.web.rest.config.AllUserAllowedWithoutAbort;
import it.cnr.contab.web.rest.config.SIGLASecurityContext;

import javax.annotation.security.PermitAll;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Local
@Path("/tree")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllUserAllowedWithoutAbort
@Api("Albero delle funzioni")
public interface AlberoMainLocal {

    @DELETE
    @OPTIONS
    @ApiOperation(value = "Elimina la cache e ricalcola l'albero delle funzioni abilitate",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = Map.class,
            responseContainer = "Map",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response evictCacheTree(@Context HttpServletRequest request) throws Exception;

    /**
     * GET  /restapi/tree -> return Albero delle funzioni
     */
    @GET
    @ApiOperation(value = "Fornisce l'albero delle funzioni abilitate",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = Map.class,
            responseContainer = "Map",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response tree(@Context HttpServletRequest request, @QueryParam("esercizio") Integer esercizio, @QueryParam("uo") String uo) throws Exception;
}
