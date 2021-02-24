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

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

@Local
@Path("/help")
@Produces(MediaType.MEDIA_TYPE_WILDCARD)
@Api("Help")
public interface HelpLocal {

    @GET
    @ApiOperation(value = "Restituisce la URL dell'Help associata al BusinessProcess o alla pagina",
            notes = "Nel caso che non sia definita resitituisce la pagina iniziale",
            response = URI.class
    )
    Response get(@Context HttpServletRequest request, @QueryParam("jspName") String jspName, @QueryParam("bpName") String bpName) throws Exception;

}
