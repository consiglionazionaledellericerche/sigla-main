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
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.web.rest.config.AllUserAllowedWithoutAbort;
import it.cnr.contab.web.rest.model.UtenteIndirizziMailDTO;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Local
@Path("/context")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AllUserAllowedWithoutAbort
@Api("Servizi di contesto applicativo")
public interface ContextLocal {

    @GET
    @Path("/esercizi")
    @ApiOperation(value = "Ritorna la lista degli esercizi possibili",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response esercizi(@Context HttpServletRequest request, @QueryParam("cds") String cds) throws Exception;

    @GET
    @Path("/uo")
    @ApiOperation(value = "Ritorna la lista delle Unità Organizzative abilitate",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response findUnitaOrganizzativeAbilitate(@Context HttpServletRequest request, @QueryParam("cds") String cds) throws Exception;

    @GET
    @Path("/cds")
    @ApiOperation(value = "Ritorna la lista dei CdS abilitati",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response findCdSAbilitati(@Context HttpServletRequest request, @QueryParam("uo") String uo) throws Exception;

    @GET
    @Path("/cdr")
    @ApiOperation(value = "Ritorna la lista dei CdR",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response findCdR(@Context HttpServletRequest request, @QueryParam("uo") String uo) throws Exception;


    @GET
    @Path("/preferiti")
    @ApiOperation(value = "Ritorna la lista dei Preferiti per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response findPreferiti(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/messaggi")
    @ApiOperation(value = "Ritorna la lista dei Messaggi per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response findMessaggi(@Context HttpServletRequest request) throws Exception;


    @POST
    @Path("/messaggi")
    @ApiOperation(value = "Cancella la lista dei Messaggi per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response deleteMessaggi(@Context HttpServletRequest request, List<MessaggioBulk> messaggi) throws Exception;

    @OPTIONS
    @Path("/messaggi")
    @ApiOperation(value = "Cancella la lista dei Messaggi per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response deleteMessaggi(@Context HttpServletRequest request) throws Exception;

    @GET
    @Path("/indirizzi-mail")
    @ApiOperation(value = "Ritorna la lista degli indirizzi email per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response indirizziMail(@Context HttpServletRequest request) throws Exception;

    @POST
    @OPTIONS
    @Path("/indirizzi-mail")
    @ApiOperation(value = "Inserisce la lista degli indirizzi email per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response inserisciIndirizziMail(@Context HttpServletRequest request, List<UtenteIndirizziMailDTO> utente_indirizzi_mailBulks) throws Exception;

    @OPTIONS
    @Path("/indirizzi-mail")
    @ApiOperation(value = "Inserisce la lista degli indirizzi email per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response inserisciIndirizziMail(@Context HttpServletRequest request) throws Exception;

    @DELETE
    @OPTIONS
    @Path("/indirizzi-mail/{indirizzi:.+}/delete")
    @ApiOperation(value = "Elimina la lista degli indirizzi email per Utente",
            notes = "Accesso consentito a tutte le utenze registrate",
            response = List.class,
            responseContainer = "List",
            authorizations = {
                    @Authorization(value = "BASIC")
            }
    )
    Response eliminaIndirizziMail(@Context HttpServletRequest request, @PathParam("indirizzi") String indirizzi) throws Exception;

}
