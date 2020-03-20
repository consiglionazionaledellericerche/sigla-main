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

import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.web.rest.config.AccessoEnum;
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
public interface ComunicaDatiPagamentiLocal {
	/**
     * GET  /restapi/datiPagamenti/ricerca -> return Fattura attiva
     */
    @GET
    public Response recuperoDatiPagamenti(@Context HttpServletRequest request) throws Exception;

}
