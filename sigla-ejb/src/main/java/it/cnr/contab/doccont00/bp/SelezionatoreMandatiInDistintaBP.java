package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.comp.ComponentException;

import java.io.InputStream;
import java.io.OutputStream;

import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.jsp.*;

public class SelezionatoreMandatiInDistintaBP extends it.cnr.jada.util.action.SelezionatoreListaBP {
	protected SiglaCMISService cmisService;
	protected DocumentiContabiliService documentiContabiliService;
public SelezionatoreMandatiInDistintaBP() {
	super();
}
@Override
protected void init(Config config, ActionContext actioncontext)
		throws BusinessProcessException {
	cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);	
	documentiContabiliService = SpringUtil.getBean("documentiContabiliService", DocumentiContabiliService.class);
	super.init(config, actioncontext);
}
public SelezionatoreMandatiInDistintaBP(String function) {
	super(function);
}
public void scaricaDocumento(ActionContext actioncontext) throws Exception {
	Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
	String cds = ((HttpActionContext)actioncontext).getParameter("cds");
	Long numero_documento = Long.valueOf(((HttpActionContext)actioncontext).getParameter("numero_documento"));
	String tipo = ((HttpActionContext)actioncontext).getParameter("tipo");
	InputStream is = documentiContabiliService.getStreamDocumento(esercizio, cds, numero_documento, tipo);
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
