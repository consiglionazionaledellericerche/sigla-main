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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import java.io.InputStream;
import java.io.OutputStream;

import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class SelezionatoreMandatiInDistintaBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
	protected DocumentiContabiliService documentiContabiliService;
	public SelezionatoreMandatiInDistintaBP() {
	super();
}
	@Override
	protected void init(Config config, ActionContext actioncontext)
		throws BusinessProcessException {
		documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
		super.init(config, actioncontext);
	}
	public SelezionatoreMandatiInDistintaBP(String function) {
		super(function);
	}
	protected CRUDComponentSession getComponentSession() {
		return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
	}
	public void scaricaDocumento(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		Long numero_documento = Long.valueOf(((HttpActionContext)actioncontext).getParameter("numero_documento"));
		String tipo = ((HttpActionContext)actioncontext).getParameter("tipo");
		InputStream is = documentiContabiliService.getStreamDocumento(
				(StatoTrasmissione) getComponentSession().findByPrimaryKey(actioncontext.getUserContext(), new V_mandato_reversaleBulk(esercizio, tipo, cds, numero_documento))
		);
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
}
