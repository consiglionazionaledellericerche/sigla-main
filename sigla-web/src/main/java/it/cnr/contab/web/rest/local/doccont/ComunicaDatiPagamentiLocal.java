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

package it.cnr.contab.web.rest.local.doccont;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import it.cnr.contab.doccont00.core.bulk.MandatoComunicaDatiBulk;
import it.cnr.contab.web.rest.config.SIGLARoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Local
@Path("/datiPagamenti")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.PORTALE)
@Api("Comunicazione Dati Pagamenti")
public interface ComunicaDatiPagamentiLocal {

    @GET
    @ApiOperation(value = "Recupera i dati dei pagamenti",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo PORTALE",
            response = MandatoComunicaDatiBulk.class,
            responseContainer = "List",
            authorizations = @Authorization(value = "BASIC")
    )
    Response recuperoDatiPagamenti(@Context HttpServletRequest request,
                                   @QueryParam("esercizio") Integer esercizio,
                                   @QueryParam("cdCds") String cdCds,
                                   @QueryParam("pgMandato") Long pgMandato,
                                   @QueryParam("daData") String daData,
                                   @QueryParam("aData") String aData) throws Exception;

}
