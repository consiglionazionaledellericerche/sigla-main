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

package it.cnr.contab.reports.servlet;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrKey;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spoolerKey;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteKey;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.sql.HomeCache;

import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;


public class OfflineReportServlet extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * DownloadOfflinePrintServlet constructor comment.
	 */
	public OfflineReportServlet() {
		super();
	}
	public void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		try {
			HttpActionContext context = new HttpActionContext(this,request,response);
			if (context.getBusinessProcessRoot(false) == null ||
					context.getUserContext() == null ||
					context.getUserContext().getUser() == null) {
				unauthorized(request,response);
				return;
			}
			/*
			 * se la stampa è richiamata dall'applet di firma digitale questa maldigerisce i parametri
			 * cablati nell'url secondo le specifiche dell'istruzione GET per cui è necessario inserire
			 * nella sessione il parametro del progressivo della stampa selezionata da firmare
			 */
			String pg = request.getParameter("pg");
			Long pg_stampa=null;
			if (pg!=null)
				pg_stampa = new Long(pg);
			if (pg_stampa==null) {
				String sign_pg_stampa = (String) request.getSession().getAttribute("sign_pg_stampa");
				if (sign_pg_stampa!=null)
					pg_stampa = new Long(sign_pg_stampa);
			}

			java.sql.Connection conn = it.cnr.jada.util.ejb.EJBCommonServices.getConnection();

			try {
				Print_spoolerBulk print_spooler = getPrint_spoolerBulk(conn,context.getUserContext(),pg_stampa);

				if (print_spooler == null) {
					unauthorized(request,response);
					return;
				}

				StringBuffer server = new StringBuffer(print_spooler.getServer());
				String file = print_spooler.getNomeFile();
				if (context.getBusinessProcess() == null) {
					unauthorized(request,response);
				} else if (server == null || file == null) {
					unauthorized(request,response);
				} else {
					HttpClient httpclient = HttpClientBuilder.create().build();
					server.append("/").append(print_spooler.getUtcr());
					server.append("/").append(print_spooler.getNomeFile());
					HttpGet method = new HttpGet(server.toString());
					method.addHeader("Accept-Language", Locale.getDefault().toString());
					HttpResponse httpResponse = httpclient.execute(method);				
					int status = httpResponse.getStatusLine().getStatusCode();
					if (status != HttpStatus.SC_OK)
						unauthorized(request,response);
					java.io.InputStream is = httpResponse.getEntity().getContent();
					if (is == null) {
						unauthorized(request,response);
					} else {
						response.setContentType("application/pdf");
						//response.setContentLength(method.getContentLength());
						response.setDateHeader("Expires", 0);
						javax.servlet.ServletOutputStream os = response.getOutputStream();
						byte[] buffer = new byte[response.getBufferSize()];
						int buflength;
						while ((buflength = is.read(buffer)) > 0) {
							os.write(buffer,0,buflength);
						}
						is.close();
						os.flush();
					}
				}
			} finally {
				conn.close();
			}
		} catch(java.io.FileNotFoundException e) {
			response.setStatus(HttpStatus.SC_NOT_FOUND);
			response.getWriter().println("Report inesistente.");
		} catch(Throwable e) {
			System.out.println(e);
			response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		}
	}
	public Print_spoolerBulk getPrint_spoolerBulk(java.sql.Connection conn,it.cnr.jada.UserContext userContext,Long pg_stampa) throws ApplicationException {
		try {
			HomeCache homeCache = new HomeCache(conn);
			Print_spoolerBulk print_spooler = (Print_spoolerBulk)homeCache.getHome(Print_spoolerBulk.class).findByPrimaryKey(new Print_spoolerKey(pg_stampa));

			if (print_spooler == null)
				return null;

			String ti_visibilita = print_spooler.getTiVisibilita();

			// Controllo che il tipo visibilità del report sia compatibile con l'utente

			if (Print_spoolerBulk.TI_VISIBILITA_UTENTE.equals(ti_visibilita)) {
				// ti_visibilita = TI_VISIBILITA_UTENTE
				// solo se l'utente corrisponde all'utente di creazione
				if (!CNRUserContext.getUser(userContext).equals(print_spooler.getUtcr()))
					return null;
			} else if (Print_spoolerBulk.TI_VISIBILITA_CDR.equals(ti_visibilita)) {
				// ti_visibilita = TI_VISIBILITA_CDR
				// solo se il cdr dell'utente corrisponde al CDR impostato nel report
				UtenteBulk utente = (UtenteBulk)homeCache.getHome(UtenteBulk.class).findByPrimaryKey(new UtenteKey(CNRUserContext.getUser(userContext)));
				if (utente == null || 
						it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) == null ||
						!it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext).equals(print_spooler.getVisibilita()))
					return null;
			} else if (Print_spoolerBulk.TI_VISIBILITA_UNITA_ORGANIZZATIVA.equals(ti_visibilita)) {
				// ti_visibilita = TI_VISIBILITA_UNITA_ORGANIZZATIVA
				// solo se la uo di scrivania corrisponde alla UO impostata nel report
				if (CNRUserContext.getCd_unita_organizzativa(userContext) == null ||
						!print_spooler.getVisibilita().equals(CNRUserContext.getCd_unita_organizzativa(userContext)))
					return null;
			} else if (Print_spoolerBulk.TI_VISIBILITA_CDS.equals(ti_visibilita)) {
				// ti_visibilita = TI_VISIBILITA_CDS
				// solo se la uo di scrivania corrisponde al CDS impostato nel report
				if (CNRUserContext.getCd_cds(userContext) == null ||
						!print_spooler.getVisibilita().equals(CNRUserContext.getCd_cds(userContext)))
					return null;
			} else if (Print_spoolerBulk.TI_VISIBILITA_CNR.equals(ti_visibilita)) {
				// ti_visibilita = TI_VISIBILITA_CNR
				// solo se l'utente appartiene ad un CDR dell'ente
				UtenteBulk utente = (UtenteBulk)homeCache.getHome(UtenteBulk.class).findByPrimaryKey(new UtenteKey(CNRUserContext.getUser(userContext)));
				if (utente == null || it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) == null)
					return null;
				CdrBulk cdr = (CdrBulk)homeCache.getHome(CdrBulk.class).findByPrimaryKey(new CdrKey(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext)));
				if (cdr == null)
					return null;
				homeCache.fetchAll(userContext);
				if (!Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equals(cdr.getUnita_padre().getUnita_padre().getCd_tipo_unita()))
					return null;
			} 

			return print_spooler;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw new ApplicationException(e);
		}
	}
	private void unauthorized(javax.servlet.http.HttpServletRequest request,javax.servlet.http.HttpServletResponse response) throws java.io.IOException {
		response.setStatus(javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().println("Impossibile aprire il file pdf.<BR>");
		response.getWriter().println("Consultare il <a href=\"http://contab.cnr.it/manuali/000%20-%2001%20requisiti%20browser.doc\">Manuale della Procedura di Contabilità</a> e verificare le Impostazioni del Browser.");
		response.flushBuffer();
	}
}
