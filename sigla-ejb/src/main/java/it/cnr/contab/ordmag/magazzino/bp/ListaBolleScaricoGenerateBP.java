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

package it.cnr.contab.ordmag.magazzino.bp;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.service.ContrattoService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagKey;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;

public class ListaBolleScaricoGenerateBP extends SelezionatoreListaBP {
	private transient final static Logger logger = LoggerFactory.getLogger(ListaBolleScaricoGenerateBP.class);
	private static final long serialVersionUID = 1L;
	private static final String NOME_REPORT = "/ordmag/bolla_scarico.jasper";
	private List<BollaScaricoMagBulk> bolle;

	@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
	}

	public ListaBolleScaricoGenerateBP() {
		this("");
	}

	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[2];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.excel");
		return toolbar;
	}

	public ListaBolleScaricoGenerateBP(String function) {
		super(function);
	}
	
	public ListaBolleScaricoGenerateBP(String function, List<BollaScaricoMagBulk> bolle) {
		super(function);
	}


	public List<BollaScaricoMagBulk> getBolle() {
		return bolle;
	}


	public void setBolle(List<BollaScaricoMagBulk> bolle) {
		this.bolle = bolle;
	}
	
	public void stampaBollaScarico(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		String magazzino = ((HttpActionContext)actioncontext).getParameter("magazzino");
		String numeratore = ((HttpActionContext)actioncontext).getParameter("numeratore");
		Integer pgBolla = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("pgBolla"));
		BollaScaricoMagBulk bolla = new BollaScaricoMagBulk(cds, magazzino, esercizio, numeratore, pgBolla);
		InputStream is = getStreamBollaScarico(actioncontext, bolla);
		if (is != null){
			((HttpActionContext)actioncontext).getResponse().setContentType("application/pdf");
			OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
			((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
			byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
			int buflength;
			while ((buflength = is.read(buffer)) > 0) {
				os.write(buffer,0,buflength);
			}
			is.close();
			os.flush();
		}
	}


	private InputStream getStreamBollaScarico(ActionContext actioncontext,
			BollaScaricoMagKey bollaScarico)
			throws Exception {
		InputStream is;
		Print_spoolerBulk print = new Print_spoolerBulk();
		print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
		print.setFlEmail(false);
		print.setReport(NOME_REPORT);
		print.setNomeFile(bollaScarico.toString());
		print.setUtcr(actioncontext.getUserContext().getUser());
		print.addParam("cd_cds", bollaScarico.getCdCds(), String.class);
		print.addParam("esercizio", bollaScarico.getEsercizio().intValue(), Integer.class);
		print.addParam("cd_magazzino", bollaScarico.getCdMagazzino(), String.class);
		print.addParam("cd_numeratore_mag", bollaScarico.getCdNumeratoreMag(), String.class);
		print.addParam("pg_bolla_sca", bollaScarico.getPgBollaSca().longValue(), Long.class);

		Report report = SpringUtil.getBean("printService",
				PrintService.class).executeReport(actioncontext.getUserContext(),
				print);
		return report.getInputStream();
	}
	
}
