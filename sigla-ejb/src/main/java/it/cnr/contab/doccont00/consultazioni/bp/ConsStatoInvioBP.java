package it.cnr.contab.doccont00.consultazioni.bp;

import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_stato_invio;
import it.cnr.contab.doccont00.service.ContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.ConsultazioniBP;
import it.cnr.jada.util.jsp.Button;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class ConsStatoInvioBP extends ConsultazioniBP {
	private static final long serialVersionUID = 1L;
	private ContabiliService contabiliService;
	private boolean contabiliEnabled;
	
	public ConsStatoInvioBP() {
		super();
	}
	
	@Override
	public void setMultiSelection(boolean flag) {
		super.setMultiSelection(flag);
        super.table.setOnselect("select");
	}

	@Override
	protected void init(Config config, ActionContext context)
			throws BusinessProcessException {
		super.init(config, context);
		contabiliService = SpringUtil.getBean("contabiliService",
				ContabiliService.class);	
	}

	@Override
	public Button[] createToolbar() {
		Button[] baseToolbar = super.createToolbar();
		List<Button> listButton = new ArrayList<Button>();
		listButton.addAll(Arrays.asList(baseToolbar));
		listButton.add(new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().
				getProperties(getClass()),"CRUDToolbar.contabile"));
		return listButton.toArray(new Button[listButton.size()]);
	}
	
	@SuppressWarnings("unchecked")
	public void scaricaContabili(ActionContext actioncontext) throws Exception {
		List<V_cons_stato_invio> selectelElements = getSelectedElements(actioncontext);
		if (selectelElements == null || selectelElements.isEmpty()){
			((HttpActionContext)actioncontext).getResponse().setStatus(HttpStatus.SC_NO_CONTENT);
			return;
		}
		PDFMergerUtility ut = new PDFMergerUtility();
		ut.setDestinationStream(new ByteArrayOutputStream());
		for (V_cons_stato_invio cons : selectelElements) {
			InputStream isToAdd = contabiliService.getStreamContabile(
					cons.getEsercizio().intValue(), cons.getCd_cds(), cons.getProgressivo(), cons.getTipo());
			if (isToAdd != null)
				ut.addSource(isToAdd);
		}
		ut.mergeDocuments();
		InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
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
	
	public boolean isContabiliEnabled(){
		return contabiliEnabled;
	}
	
	public void setContabiliEnabled(ActionContext actioncontext) throws BusinessProcessException, ApplicationException {
		contabiliEnabled =  false;
		for (it.cnr.jada.util.action.SelectionIterator i = getSelection().iterator();i.hasNext();){
			V_cons_stato_invio cons = (V_cons_stato_invio) getElementAt(actioncontext,i.nextIndex());
			List<String> nodeRefs = contabiliService.getNodeRefContabile(cons.getEsercizio().intValue(), 
					cons.getCd_cds(), cons.getProgressivo(), cons.getTipo());
			if (nodeRefs != null && !nodeRefs.isEmpty()){
				contabiliEnabled =  true;
				break;
			}			
			
		}
	}
	
	public void scaricaContabile(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext)actioncontext).getParameter("esercizio"));
		String cds = ((HttpActionContext)actioncontext).getParameter("cds");
		Long numero_mandato = Long.valueOf(((HttpActionContext)actioncontext).getParameter("numero_mandato"));
		InputStream is = contabiliService.getStreamContabile(esercizio, cds, numero_mandato,((V_cons_stato_invio)getBulkClass().newInstance()).getTipo());
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