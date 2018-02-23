package it.cnr.contab.reports.bp;

import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.ServizioPecBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.firma.DatiPEC;
import it.cnr.jada.firma.FirmaInfos;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import java.rmi.RemoteException;
import java.util.Arrays;

import javax.ejb.EJBException;
import javax.servlet.http.HttpServletRequest;

/**
 * Insert the type's description here.
 * Creation date: (11/04/2002 17:28:04)
 * @author: CNRADM
 */
public class PrintSpoolerBP extends it.cnr.jada.util.action.SelezionatoreListaBP implements FirmaInfos {
	private boolean signEnabled;
	private boolean signFile;
	private DatiPEC datiPEC = new DatiPEC();
	public static String PEC_PROTOCOLLO = "PROTOCOLLO";
	public static String PEC_BILANCIO = "BILANCIO";

	/**
	 * SpoolerStatusBP constructor comment.
	 * @param function java.lang.String
	 */
	public PrintSpoolerBP() {
		super();
		table.setMultiSelection(true);
		//table = new it.cnr.jada.util.jsp.Table("main");
		//table.setMultiSelection(true);
		//table.setOnselect("javascript:select");
		//table.setReadonly(true);
		//table.setStatus(it.cnr.jada.util.action.FormController.EDIT);
		//table.setEditableOnFocus(false);
		//table.setOrderable(null);
		setBulkInfo(BulkInfo.getBulkInfo(Print_spoolerBulk.class));
		//setColumns(BulkInfo.getBulkInfo(Print_spoolerBulk.class).getColumnFieldPropertyDictionary());
		//table.setSelectable(true);
		//table.setReadonly(true);
		//table.setName("main");
	}
	public it.cnr.contab.reports.ejb.OfflineReportComponentSession createComponentSession() throws BusinessProcessException {
		return
				(it.cnr.contab.reports.ejb.OfflineReportComponentSession)createComponentSession(
						"BREPORTS_EJB_OfflineReportComponentSession",
						it.cnr.contab.reports.ejb.OfflineReportComponentSession.class);
	}
	public it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = new Button[4];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.refresh");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"Toolbar.close");
		return toolbar;
	}
	protected void init(Config config,ActionContext context) throws BusinessProcessException {
		super.init(config,context);
		Print_spoolerBulk print_spooler = new Print_spoolerBulk();
		print_spooler.setTiVisibilita(print_spooler.TI_VISIBILITA_UTENTE);
		setModel(context,print_spooler);
		refresh(context);
	}
	public boolean isPrintButtonEnabled() {
		return 
				getFocusedElement() != null &&
				((Print_spoolerBulk)getFocusedElement()).isEseguita();
	}
	public boolean isSignButtonEnabled() {
		return 
				getFocusedElement() != null &&
				((Print_spoolerBulk)getFocusedElement()).isEseguita();
	}
	public void refresh(ActionContext context) throws BusinessProcessException {
		try {		
			setIterator(context,createComponentSession().queryJobs(
					context.getUserContext(),
					((Print_spoolerBulk)getModel()).getTiVisibilita()));
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	/* 11/09/2009 R.P. Eliminato per impostare il model e visualizzare le info della mail
	 * 
	 * Riscritto perchè in questo caso non voglio che quando l'utente
	 * seleziona una riga nel selezionatore venga anche impostato
	 * il modello del BulkBP

protected void setFocusedElement(it.cnr.jada.action.ActionContext context,Object element) throws it.cnr.jada.action.BusinessProcessException {
	OggettoBulk model = getModel();
	super.setFocusedElement(context,element);
	setModel(context,model);
}
	 */
	public void writeToolbar(javax.servlet.jsp.PageContext pageContext) throws java.io.IOException,javax.servlet.ServletException {
		Button[] toolbar = getToolbar();
		Print_spoolerBulk print_spool = (Print_spoolerBulk)getFocusedElement();
		if (print_spool != null && Print_spoolerBulk.STATO_ESEGUITA.equalsIgnoreCase(print_spool.getStato()))
			toolbar[1].setHref("doPrint('"+ JSPUtils.getAppRoot((HttpServletRequest) pageContext.getRequest()) + "offline_report/"+print_spool.getNomeFile()+"?pg="+print_spool.getPgStampa().longValue()+"')");
		else
			toolbar[1].setHref(null);
		writeToolbar(pageContext.getOut(), toolbar);
	}
	public void sign(ActionContext context) throws BusinessProcessException {
		try {
			caricaDatiPEC(context);
			setSignEnabled(true);
			setSignFile(true);
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}
	public boolean isSignEnabled() {
		return signEnabled;
	}
	public void setSignEnabled(boolean signEnabled) {
		this.signEnabled = signEnabled;
	}
	public boolean isSignFile() {
		return signFile;
	}
	public void setSignFile(boolean signFile) {
		this.signFile = signFile;
	}
	public String getFileName() {
		Print_spoolerBulk print_spool = (Print_spoolerBulk)getFocusedElement();
		if (print_spool != null)
			return print_spool.getNomeFile();
		else
			return (null);
	}
	public String getDownloadFile(javax.servlet.jsp.PageContext pageContext) {
		Print_spoolerBulk print_spool = (Print_spoolerBulk)getFocusedElement();
		if (print_spool != null && print_spool.STATO_ESEGUITA.equalsIgnoreCase(print_spool.getStato()))
			return JSPUtils.buildAbsoluteUrl(
					pageContext,
					null,
					"offline_report/"+print_spool.getNomeFile());
		else
			return (null);
	}
	public String getPgStampa() {
		Print_spoolerBulk print_spool = (Print_spoolerBulk)getFocusedElement();
		if (print_spool!=null)
			return ""+print_spool.getPgStampa().longValue();
		else
			return null;
	}
	public String getUploadFile(javax.servlet.jsp.PageContext pageContext) {
		String actionExtension = pageContext.getServletConfig().getInitParameter("extension");
		if(actionExtension == null)
			actionExtension = ".do";   
		String path = getDefaultAction()+actionExtension;
		return JSPUtils.buildAbsoluteUrl(
				pageContext,
				null,
				path);
	}
	public boolean isEMailEnabled(){
		if (this.getModel()!=null && ((Print_spoolerBulk)this.getModel()).getFlEmail() != null && (((Print_spoolerBulk)this.getModel()).getFlEmail()))
			return true;
		return false;
	}
	public String descrizione() {
		Print_spoolerBulk pbulk = (Print_spoolerBulk) getFocusedElement();
		if (pbulk!=null)
			return pbulk.getDsStampa();
		return null;
	}
	public DatiPEC datiPEC() {
		return datiPEC;
	}
	public void caricaDatiPEC(ActionContext context) throws ValidationException {
		Print_spoolerBulk pbulk = (Print_spoolerBulk) getFocusedElement();
		String cdServizioPEC=null;
		if (pbulk!=null)
			cdServizioPEC = pbulk.getCd_servizio_pec();

		boolean testMode=true;
		Parametri_enteBulk parametriEnte = null;
		try {
			parametriEnte = Utility.createParametriEnteComponentSession().getParametriEnte(context.getUserContext());
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		// PEC del protocollo
		ServizioPecBulk servizioPecProtocollo = null;
		try {
			servizioPecProtocollo = Utility.createParametriEnteComponentSession().getServizioPec(context.getUserContext(),PEC_PROTOCOLLO);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (servizioPecProtocollo == null)
			throw new ValidationException("Non è presente l'email per l'invio della posta certificata per l'ufficio del Protocollo. Impossibile procedere!");
		// PEC derivata dalla stampa specifica
		ServizioPecBulk servizioPec = null;
		try {
			servizioPec = Utility.createParametriEnteComponentSession().getServizioPec(context.getUserContext(),cdServizioPEC);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (servizioPec == null)
			throw new ValidationException("L'ufficio di competenza per l'invio della posta certificata non è definito. Impossibile procedere!");
		if (!parametriEnte.getTipo_db().equals(Parametri_enteBulk.DB_PRODUZIONE)){
			testMode=true;
		}
		CdsBulk cds = null;
		Unita_organizzativaBulk uo = null;
		CdrBulk cdr = null;
		try {
			cds = Utility.createParametriEnteComponentSession().getCds(context.getUserContext(),CNRUserContext.getCd_cds((CNRUserContext)context.getUserContext()));
			uo = Utility.createParametriEnteComponentSession().getUo(context.getUserContext(),CNRUserContext.getCd_unita_organizzativa((CNRUserContext)context.getUserContext()));
			cdr = Utility.createParametriEnteComponentSession().getCdr(context.getUserContext(),CNRUserContext.getCd_cdr((CNRUserContext)context.getUserContext()));
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}

		String cdUoPec = null;

		if (cds.getCd_tipo_unita().equals(
				Tipo_unita_organizzativaHome.TIPO_UO_SAC)) {
			datiPEC.setCds("000");
			datiPEC.setDsCds("CNR - Consiglio Nazionale delle Ricerche");
			datiPEC.setUo(CNRUserContext
					.getCd_unita_organizzativa((CNRUserContext) context
							.getUserContext()));
			datiPEC.setDsUo(uo.getDs_unita_organizzativa());
			cdUoPec = CNRUserContext
					.getCd_unita_organizzativa((CNRUserContext) context
							.getUserContext());
		} else {
			datiPEC.setCds(CNRUserContext.getCd_cds((CNRUserContext) context
					.getUserContext()));
			datiPEC.setDsCds(cds.getDs_unita_organizzativa());
			datiPEC.setUo(CNRUserContext.getCd_cds((CNRUserContext) context
					.getUserContext()));
			datiPEC.setDsUo(cds.getDs_unita_organizzativa());
			cdUoPec = CNRUserContext.getCd_cds((CNRUserContext) context
					.getUserContext());
		}

		UnitaOrganizzativaPecBulk uoPec = null;
		try {
			uoPec = Utility.createParametriEnteComponentSession().getUnitaOrganizzativaPec(context.getUserContext(),cdUoPec);
		} catch (ComponentException e) {
			handleException(e);
		} catch (EJBException e) {
			handleException(e);
		} catch (RemoteException e) {
			handleException(e);
		}
		if (uoPec == null || (uoPec.getEmailPec().equals("") && uoPec.getEmailPecDirettore().equals("")))
			throw new ValidationException("L'indirizzo email di posta certificata per la unità organizzativa "+cdUoPec+" non è definito. Impossibile procedere!");

		if (!pbulk.getDs_oggetto_pec().equals(""))
			datiPEC.setOggetto(pbulk.getDs_oggetto_pec());
		else
			datiPEC.setOggetto(descrizione());
		datiPEC.setDenominazioneServizio(servizioPec.getDsServizio());
		if (testMode){
			datiPEC.setEmailProtocollo(servizioPecProtocollo.getEmailTest());
			datiPEC.setEmailServizio(servizioPec.getEmailTest());
		}else{
			datiPEC.setEmailProtocollo(servizioPecProtocollo.getEmail());
			datiPEC.setEmailProtocollo2(servizioPecProtocollo.getEmail2());
			datiPEC.setEmailProtocollo3(servizioPecProtocollo.getEmail3());
			datiPEC.setEmailServizio(servizioPec.getEmail());
			datiPEC.setEmailServizio2(servizioPec.getEmail2());
			datiPEC.setEmailServizio3(servizioPec.getEmail3());
			datiPEC.setEmailIstituto(uoPec.getEmailPec());
			datiPEC.setEmailIstituto2(uoPec.getEmailPecDirettore());
		}
	}
	public void rendiPersistente(String file) {
		// TODO Auto-generated method stub	
	}
	public String tipoPersistenza() {
		return TIPO_PERSISTENZA_INTERNA;
	}
}
