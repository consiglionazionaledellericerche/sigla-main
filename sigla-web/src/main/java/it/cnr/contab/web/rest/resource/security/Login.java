/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.web.rest.resource.security;

import it.cnr.contab.web.rest.exception.UnprocessableEntityException;
import it.cnr.contab.web.rest.local.config00.AccountLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Optional;

@Path("login")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class Login {
    private final Logger LOGGER = LoggerFactory.getLogger(Login.class);
    @Context
    SecurityContext securityContext;

    @EJB
    private AccountLocal accountLocal;

    @POST
    public Response postLogin(@Context HttpServletRequest request, @FormParam("j_username") String username, @FormParam("j_password") String password) throws Exception {
        try {
            if (!Optional.ofNullable(securityContext.getUserPrincipal()).isPresent())
                request.login(username, password);
            return Response.ok(accountLocal.getAccountDTO(request)).build();
        } catch (ServletException e) {
            LOGGER.trace("Login error for user:{} password:{}", username, password, e);
            return Response.status(Response.Status.UNAUTHORIZED) .build();
        } catch (UnprocessableEntityException e) {
            LOGGER.trace("Login error for user:{} password:{}", username, password, e);
            return Response.status(Response.Status.NOT_ACCEPTABLE) .build();
        }
    }
}
