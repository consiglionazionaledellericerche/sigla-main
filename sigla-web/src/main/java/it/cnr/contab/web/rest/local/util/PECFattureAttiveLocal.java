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

import it.cnr.contab.web.rest.config.AccessoAllowed;
import it.cnr.contab.util.enumeration.AccessoEnum;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Local
@Path("/fatture-attive")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PECFattureAttiveLocal {

    @GET
    @Path("/reinvia-pec")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response reinviaPEC(@Context HttpServletRequest request, @QueryParam("esercizio") Integer esercizio, @QueryParam("pgFatturaAttiva") Long pgFatturaAttiva) throws Exception;

    @GET
    @Path("/aggiorna-nome-file")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response aggiornaNomeFile(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/aggiorna-metadati")
    @AccessoAllowed(value= AccessoEnum.XXXHTTPSESSIONXXXXXX)
    Response aggiornaMetadati(@Context HttpServletRequest request, @QueryParam("esercizio") Integer esercizio, @QueryParam("cdCds") String cdCds, @QueryParam("pgFatturaAttiva") Long pgFatturaAttiva) throws Exception;
}
