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
import it.cnr.contab.pagopa.model.NotificaPagamento;
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
@Path("/govpay/pagamenti/{idDominio}/{iuv}")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(SIGLARoles.GOVPAY)
@Api("PagoPA")
public interface NotificaPagamentoLocal {
    /**
     * POST  /restapi/govpay/pagamenti -> send Pagamento
     */
    @POST
    @ApiOperation(value = "Riceve il pagamento da PagoPA",
            notes = "Accesso consentito solo alle utenze abilitate al ruolo GOVPAY",
            response = NotificaPagamento.class,
            authorizations = @Authorization(value = "BASIC")
    )
    Response comunicazionePagamento(@Context HttpServletRequest request,
                                    @PathParam("idDominio") String idDominio,
                                    @PathParam("iuv") String iuv,
                                    String base64) throws Exception;
}
