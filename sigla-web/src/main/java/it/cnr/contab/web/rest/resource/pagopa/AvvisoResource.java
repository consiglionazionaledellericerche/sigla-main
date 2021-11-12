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

package it.cnr.contab.web.rest.resource.pagopa;

import it.cnr.contab.config00.ejb.Unita_organizzativaComponentSession;
import it.cnr.contab.pagopa.ejb.PendenzaPagopaComponentSession;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.pagopa.AvvisoLocal;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class AvvisoResource implements AvvisoLocal {
    private final Logger LOGGER = LoggerFactory.getLogger(AvvisoResource.class);
	@Context SecurityContext securityContext;
	@EJB CRUDComponentSession crudComponentSession;
	@EJB PendenzaPagopaComponentSession pendenzaPagopaComponentSession;
	@EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;

	@Override
	public Response recuperoAvviso(@Context HttpServletRequest request,
								   @PathParam("idDominio") String idDominio,
								   @PathParam("numeroAvviso") String numeroAvviso) throws Exception {
		LOGGER.info("REST request per recupero Dati Avviso PagoPA. Numero Avviso: "+numeroAvviso);
		CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
		Pendenza pendenza = new Pendenza();
		Optional.ofNullable(idDominio).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, id Dominio obbligatorio."));
		Optional.ofNullable(numeroAvviso).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, numero avviso obbligatorio."));

		try {
			pendenza = pendenzaPagopaComponentSession.getPendenza(userContext, numeroAvviso);
			if (pendenza == null){
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok(pendenza).build();
		} catch (Exception ex) {
				LOGGER.error("Errore durante il recupero Dati Avviso PagoPA. Numero Avviso: "+numeroAvviso);
				String msg = Arrays.stream(ex.getStackTrace())
						.map(Objects::toString)
						.collect(Collectors.joining("\n"));
				String subject = "PagoPA: Errore durante il recupero Dati Avviso PagoPA. Numero Avviso: "+numeroAvviso;
				SendMail.sendErrorMail(subject, msg);
				throw new RestException(Status.BAD_REQUEST, msg);
		}
	}
}