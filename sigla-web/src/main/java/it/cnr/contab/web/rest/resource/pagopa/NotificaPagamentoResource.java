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
import it.cnr.contab.pagopa.model.pagamento.NotificaPagamento;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.pagopa.NotificaPagamentoLocal;
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
public class NotificaPagamentoResource implements NotificaPagamentoLocal {
    private final Logger logger = LoggerFactory.getLogger(NotificaPagamentoResource.class);
	@Context SecurityContext securityContext;
	@EJB CRUDComponentSession crudComponentSession;
	@EJB PendenzaPagopaComponentSession pendenzaPagopaComponentSession;
	@EJB Unita_organizzativaComponentSession unita_organizzativaComponentSession;

	@Override
	public Response comunicazionePagamento(@Context HttpServletRequest request,
										   @PathParam("idDominio") String idDominio,
										   @PathParam("iuv") String iuv,
										   String payload) throws Exception {
		NotificaPagamento pagamento = null;
		CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
		Optional.ofNullable(iuv).orElseThrow(() -> new RestException(Status.BAD_REQUEST, "Errore, indicare il codice iuv."));
		logger.info("Ricevuta Notifica Pagamento Iuv: "+iuv);
		try{
			com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
			pagamento = mapper.readValue(payload, NotificaPagamento.class);
			logger.info("Caricato Pagamento con iuv: "+iuv);
		} catch (Exception ex) {
			logger.error("Errore durante l'elaborazione della notifica di pagamento Iuv: "+iuv);
			String msg = Arrays.stream(ex.getStackTrace())
					.map(Objects::toString)
					.collect(Collectors.joining("\n"));
			String subject = "PagoPA: Errore durante l'elaborazione della notifica di pagamento. Iuv: "+iuv;
			SendMail.sendErrorMail(subject, msg);
			throw new RestException(Status.BAD_REQUEST, msg);
		}
		pendenzaPagopaComponentSession.notificaPagamento(userContext, pagamento, iuv);
		return Response.status(Status.OK).entity(pagamento).build();
	}
}