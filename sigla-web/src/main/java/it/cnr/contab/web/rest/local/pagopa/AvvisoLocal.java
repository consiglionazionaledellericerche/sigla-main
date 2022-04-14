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

package it.cnr.contab.web.rest.local.pagopa;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.web.rest.config.SIGLARoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/govpay/avvisi/{idDominio}/{numeroAvviso}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.GOVPAY)
@Api("PagoPA")
public interface AvvisoLocal {
    /**
     * GET  /restapi/govpay/avviso -> return Avviso
     */
    @GET
    @ApiOperation(value = "Verifica gli estremi della pendenza da pagare",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo GOVPAY",
            response = Pendenza.class,
            authorizations = @Authorization(value = "BASIC")
    )

    Response recuperoAvviso(@Context HttpServletRequest request,
                            @PathParam("idDominio") String idDominio,
                            @PathParam("numeroAvviso") String numeroAvviso) throws Exception;
}
