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

package it.cnr.contab.web.rest.local.util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;
import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.Risultato;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Local
@Path("/messaggi")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "SIOPE+")
public interface MessaggiSiopePlusLocal {

    @GET
    @Path("/siopeplus")
    @AccessoAllowed(value = AccessoEnum.XXXHTTPSESSIONXXXXXX)
    @ApiOperation(value = "Forza la lettura dei Messaggi SIOPE+ dalla piattaforma",
            notes = "Accesso consentito solo alle utenze abilitate a XXXHTTPSESSIONXXXXXX",
            response = Void.class,
            authorizations = @Authorization(value = "BASIC")
    )
    Response messaggiSiopeplus(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/siopeplus/{esito}")
    @AccessoAllowed(value = AccessoEnum.XXXHTTPSESSIONXXXXXX)
    @ApiOperation(value = "Legge i messaggi SIOPE+ dalla piattaforma",
            notes = "Accesso consentito solo alle utenze abilitate a XXXHTTPSESSIONXXXXXX",
            response = Risultato.class,
            responseContainer = "Map",
            authorizations = @Authorization(value = "BASIC")
    )
    Response esito(@Context HttpServletRequest request, @PathParam("esito") Esito esito,
                   @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("download") Boolean download) throws Exception;


    @GET
    @Path("/siopeplus/{esito}/downloadxml")
    @AccessoAllowed(value = AccessoEnum.XXXHTTPSESSIONXXXXXX)
    @ApiOperation(value = "Legge i file associati ai messaggi SIOPE+ e li scarica nella folder locale ${localfolder}",
            notes = "Accesso consentito solo alle utenze abilitate a XXXHTTPSESSIONXXXXXX",
            response = Risultato.class,
            responseContainer = "Map",
            authorizations = @Authorization(value = "BASIC")
    )
    Response downloadxml(@Context HttpServletRequest request, @PathParam("esito") Esito esito,
                         @QueryParam("dataDa") String dataDa, @QueryParam("dataA") String dataA, @QueryParam("download") Boolean download, @QueryParam("localfolder") String localFolder) throws Exception;

}
