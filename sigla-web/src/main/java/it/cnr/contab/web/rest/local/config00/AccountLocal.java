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
import it.cnr.contab.web.rest.model.AccountDTO;
import it.cnr.contab.web.rest.model.PasswordDTO;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/account")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllUserAllowedWithoutAbort
@Api("Account")
public interface AccountLocal {

    @GET
    @ApiOperation(value = "Fornisce le informazioni dell'account",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = AccountDTO.class,
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response get(@Context HttpServletRequest request) throws Exception;

    @OPTIONS
    @ApiOperation(value = "Fornisce le informazioni dell'account",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = AccountDTO.class,
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response optionsGet(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/{username}")
    @ApiOperation(value = "Fornisce le informazioni dell'account in base allo username",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = AccountDTO.class,
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response getUsername(@Context HttpServletRequest request, @PathParam("username") String username) throws Exception;

    @POST
    @Path("/change-password")
    @ApiOperation(value = "Cambia la passwod dell'utente collegato",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = AccountDTO.class,
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response changePassword(@Context HttpServletRequest request, PasswordDTO passwordDTO) throws Exception;

    @OPTIONS
    @Path("/change-password")
    @ApiOperation(value = "Cambia la passwod dell'utente collegato",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = AccountDTO.class,
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response changePassword(@Context HttpServletRequest request) throws Exception;

    AccountDTO getAccountDTO(HttpServletRequest request) throws Exception;
}
