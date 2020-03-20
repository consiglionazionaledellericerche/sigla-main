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

package it.cnr.contab.web.rest.resource.doccont;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.contab.client.docamm.FatturaAttiva;
import it.cnr.contab.client.docamm.FatturaAttivaIntra;
import it.cnr.contab.client.docamm.FatturaAttivaRiga;
import it.cnr.contab.client.docamm.FatturaAttivaScad;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.intrastat.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.web.rest.config.FatturaAttivaCodiciEnum;
import it.cnr.contab.web.rest.exception.FatturaAttivaException;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.contab.web.rest.local.docamm.FatturaAttivaLocal;
import it.cnr.contab.web.rest.local.doccont.ComunicaDatiPagamentiLocal;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

@Stateless
public class ComunicaDatiPagamentiResource implements ComunicaDatiPagamentiLocal {
	private final Logger LOGGER = LoggerFactory.getLogger(ComunicaDatiPagamentiResource.class);
	@Context SecurityContext securityContext;

	@EJB MandatoComponentSession mandatoComponentSession;


	@Override
	public Response recuperoDatiPagamenti(@Context HttpServletRequest request) throws Exception {
		LOGGER.debug("REST request per recupero Dati Pagamenti." );
		CNRUserContext userContext = (CNRUserContext) securityContext.getUserPrincipal();
		try {
			List<MandatoComunicaDatiBulk> dati = mandatoComponentSession.recuperoDatiPagamenti(userContext, new MandatoComunicaDatiBulk());
			LOGGER.debug("Fine REST per recupero Dati Pagamenti." );
			return Response.ok(Optional.ofNullable(dati).orElseThrow(() -> new Exception())).build();
		} catch(Exception _ex) {
			return Response.status(Status.NOT_FOUND).entity(Collections.singletonMap("ERROR", "errore")).build();
		}
	}

}