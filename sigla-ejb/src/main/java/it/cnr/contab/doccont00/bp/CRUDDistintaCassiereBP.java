package it.cnr.contab.doccont00.bp;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.cori00.ejb.Liquid_coriComponentSession;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.Apparence;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.doccont00.intcass.bulk.ExtCassiereCdsBulk;
import it.cnr.contab.doccont00.intcass.bulk.PdfSignApparence;
import it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.intcass.xmlbnl.FlussoOrdinativi;
import it.cnr.contab.doccont00.intcass.xmlbnl.Mandato;
import it.cnr.contab.doccont00.intcass.xmlbnl.ObjectFactory;
import it.cnr.contab.doccont00.intcass.xmlbnl.Reversale;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.reports.bp.OfflineReportPrintBP;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import it.cnr.contab.spring.storage.StorageException;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.utenze00.bulk.CNRUserInfo;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.action.RemoteDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.IOUtils;

import com.google.gson.GsonBuilder;

/**
 * Business Process che gestisce le attivit� di CRUD per l'entita' Distinta
 * Cassiere
 * 
 * @version 1.1 by Aurelio D'Amico [08/11/2006] conversione stampa Crystal in
 *          Jasper Reports
 */
public class CRUDDistintaCassiereBP extends
		it.cnr.jada.util.action.SimpleCRUDBP {
	private final RemoteDetailCRUDController distintaCassDet = new RemoteDetailCRUDController(
			"DistintaCassDet",
			it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk.class,
			"distinta_cassiere_detColl",
			"CNRDOCCONT00_EJB_DistintaCassiereComponentSession", this);
	private final RemoteDetailCRUDController distinteCassCollegateDet = new RemoteDetailCRUDController(
			"DistinteCassCollegateDet",
			it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk.class,
			"distinte_cassiere_detCollegateColl",
			"CNRDOCCONT00_EJB_DistintaCassiereComponentSession", this);
	private Parametri_cnrBulk parametriCnr;
	public boolean elencoConUo;
	public Boolean flusso;
	public Boolean sepa;
	public Boolean annulli;
	
	private String file;
	private Unita_organizzativaBulk uoSrivania;
	protected DocumentiContabiliService documentiContabiliService;
	private UtenteFirmaDettaglioBulk firmatarioDistinta;
	protected String controlloCodiceFiscale;

	public CRUDDistintaCassiereBP() {
		super("Tn");

	}

	public CRUDDistintaCassiereBP(String function) {
		super(function + "Tn");

	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();

		if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue()
				&& !isFlusso()) {
			Button[] newToolbarTesoreria = new Button[toolbar.length + 3];
			int i;
			for (i = 0; i < toolbar.length; i++)
				newToolbarTesoreria[i] = toolbar[i];
			newToolbarTesoreria[i] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.stampaProv");
			newToolbarTesoreria[i + 1] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.firma");
			newToolbarTesoreria[i + 2] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.downloadFirmato");
			return newToolbarTesoreria;
		}
		if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue()
				&& isFlusso()) {
			Button[] newToolbarTesoreria = new Button[toolbar.length + 5];
			int i;
			for (i = 0; i < toolbar.length; i++)
				newToolbarTesoreria[i] = toolbar[i];
			newToolbarTesoreria[i] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.stampaProv");
			newToolbarTesoreria[i + 1] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.salvaDef");
			newToolbarTesoreria[i + 2] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.firma");
			newToolbarTesoreria[i + 3] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.downloadnew");
			newToolbarTesoreria[i + 4] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.downloadFirmato");
			return newToolbarTesoreria;
		} else if (this.isFlusso()) {
			Button[] newToolbar = new Button[toolbar.length + 2];
			int i;
			for (i = 0; i < toolbar.length; i++)
				newToolbar[i] = toolbar[i];
			newToolbar[i] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.flusso");
			newToolbar[i + 1] = new it.cnr.jada.util.jsp.Button(
					it.cnr.jada.util.Config.getHandler().getProperties(
							getClass()), "CRUDToolbar.download");
			return newToolbar;
		} else
			return toolbar;
	}

	/**
	 * Viene richiesta alla component che gestisce la distinta cassiere di
	 * calcolare gli importi totali della distinta
	 */
	public void calcolaTotali(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			Distinta_cassiereBulk distinta = ((DistintaCassiereComponentSession) createComponentSession())
					.calcolaTotali(context.getUserContext(),
							(Distinta_cassiereBulk) getModel());
			setModel(context, distinta);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * Viene richiesta alla component che gestisce la distinta cassiere di
	 * ricercare mandati e reversali
	 */
	public RemoteIterator findMandatiEReversali(ActionContext context,
			CompoundFindClause clauses, V_mandato_reversaleBulk model,
			Distinta_cassiereBulk contesto)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			return it.cnr.jada.util.ejb.EJBCommonServices
					.openRemoteIterator(
							context,
							((DistintaCassiereComponentSession) createComponentSession())
									.cercaMandatiEReversali(
											context.getUserContext(), clauses,
											model, contesto));
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	/**
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.RemoteDetailCRUDController getDistintaCassDet() {
		return distintaCassDet;
	}

	protected void init(it.cnr.jada.action.Config config, ActionContext context)
			throws BusinessProcessException {
		try {
			context.getBusinessProcess("/GestioneUtenteBP").removeChild(
					"CRUDDistintaCassiereBP");
			this.setFlusso(new Boolean(config.getInitParameter("flusso")));
			this.setSepa(new Boolean(config.getInitParameter("sepa")));
			this.setAnnulli(new Boolean(config.getInitParameter("annulli")));
			
			setParametriCnr(Utility.createParametriCnrComponentSession()
					.getParametriCnr(
							context.getUserContext(),
							it.cnr.contab.utenze00.bulk.CNRUserInfo
									.getEsercizio(context)));
			if (this.getParametriCnr().getFl_tesoreria_unica().booleanValue()
					&& !isUoDistintaTuttaSac(context))
				throw new ApplicationException(
						"Funzione non abilitata per la uo");
			else
				isUoDistintaTuttaSac(context);
			documentiContabiliService = SpringUtil.getBean(
					"documentiContabiliService",
					DocumentiContabiliService.class);

			firmatarioDistinta = ((UtenteComponentSession) createComponentSession(
					"CNRUTENZE00_EJB_UtenteComponentSession",
					UtenteComponentSession.class)).isUtenteAbilitatoFirma(
					context.getUserContext(), AbilitatoFirma.DIST);
			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
			controlloCodiceFiscale = sess.getVal01(context.getUserContext(),
					"CONTROLLO_CF_FIRMA_DOCCONT");

		} catch (ComponentException e) {
			throw handleException(e);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		super.init(config, context);

	}

	/*
	 * inizializza il BP delle stampe impostando il nome del report da stampare
	 * e i suoi parametri
	 */
	protected void initializePrintBP(AbstractPrintBP bp) {
		OfflineReportPrintBP printbp = (OfflineReportPrintBP) bp;
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();

		// ver1.1 (start)
		// printbp.setReportName("/doccont/doccont/distinta_cassiere.rpt");
		// printbp.setReportParameter(0,distinta.getEsercizio().toString());
		// printbp.setReportParameter(1,distinta.getCd_cds());
		// printbp.setReportParameter(2,distinta.getCd_unita_organizzativa());
		// printbp.setReportParameter(3,distinta.getPg_distinta().toString());
		if (distinta.getPg_distinta_def() != null)
			printbp.setReportName("/doccont/doccont/distinta_cassiere.jasper");
		else
			printbp.setReportName("/doccont/doccont/distinta_cassiere_provvisoria.jasper");
		Print_spooler_paramBulk param = new Print_spooler_paramBulk();
		param.setNomeParam("esercizio");
		param.setValoreParam(distinta.getEsercizio().toString());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cd_cds");
		param.setValoreParam(distinta.getCd_cds());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		param = new Print_spooler_paramBulk();
		param.setNomeParam("cd_unita_organizzativa");
		param.setValoreParam(distinta.getCd_unita_organizzativa());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		param = new Print_spooler_paramBulk();
		param.setNomeParam("pg_distinta");
		param.setValoreParam(distinta.getPg_distinta().toString());
		param.setParamType("java.lang.String");
		printbp.addToPrintSpoolerParam(param);
		// ver1.1 (end)
	}

	/**
	 * Abilito il bottone di aggiunta doc. contabili solo se la distinta e' in
	 * fase di modifica/inserimento e la data di invio � nulla.
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica/inserimento
	 */
	public boolean isAddDocContabiliButtonEnabled() {
		return isEditable()
				&& !((Distinta_cassiereBulk) getModel()).isCreateByOtherUo()
				&& (isInserting() || (isEditing() && ((Distinta_cassiereBulk) getModel())
						.getDt_invio() == null));

	}

	/**
	 * Abilito il bottone di delete se la data di invio della distinta � nulla.
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica/inserimento
	 */
	public boolean isDeleteButtonEnabled() {
		return super.isDeleteButtonEnabled()
				&& !((Distinta_cassiereBulk) getModel()).isCreateByOtherUo()
				&& ((Distinta_cassiereBulk) getModel()).getDt_invio() == null;

	}

	public boolean isPrintButtonHidden() {
		if (((Distinta_cassiereBulk) getModel()).getPg_distinta_def() == null)
			return true;
		return super.isPrintButtonHidden() || isInserting() || isSearching();
	}

	public boolean isPrintProvButtonHidden() {
		if (((Distinta_cassiereBulk) getModel()).getPg_distinta_def() != null)
			return true;
		return super.isPrintButtonHidden() || isInserting() || isSearching();
	}

	/**
	 * Abilito il bottone di eliminazione doc. contabili solo se la distinta e'
	 * in fase di modifica/inserimento e la data di invio � nulla.
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica/inserimento
	 */
	public boolean isRemoveDocContabiliButtonEnabled() {
		return isEditable()
				&& !((Distinta_cassiereBulk) getModel()).isCreateByOtherUo()
				&& (isInserting() || (isEditing() && ((Distinta_cassiereBulk) getModel())
						.getDt_invio() == null));
	}

	/**
	 * Abilito il bottone di save solo se la distinta e' in fase di
	 * modifica/inserimento e la data di invio � nulla.
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica/inserimento
	 */
	public boolean isSaveButtonEnabled() {
		return isEditable()
				&& !((Distinta_cassiereBulk) getModel()).isCreateByOtherUo()
				&& (isInserting() || (isEditing() && ((Distinta_cassiereBulk) getModel())
						.getDt_invio() == null));

	}

	/**
	 * Abilito il bottone di visualizzazione dettagli totali solo se la distinta
	 * e' in fase di modifica/inserimento
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica/inserimento
	 */
	public boolean isVisualizzaDettagliTotaliButtonEnabled() {
		return isEditable() && (isInserting() || isEditing());
	}

	/**
	 * Abilito il bottone di visualizzazione dettagli totali trasmessi solo se
	 * la distinta e' in fase di modifica
	 *
	 * isEditable = FALSE se la distinta e' in visualizzazione = TRUE se la
	 * distinta e' in modifica
	 */
	public boolean isVisualizzaDettagliTotaliTrasmessiButtonEnabled() {
		return isEditable();
	}

	protected void resetTabs(ActionContext context) {
		setTab("tab", "tabDistinta");
	}

	/**
	 * controlla se i mandati di versamento cori/iva accentrati sono stati
	 * selezionati per la cancellazione dalla distinta
	 */
	public void controllaEliminaMandati(ActionContext context)
			throws BusinessProcessException {

		if (isInserisciMandatiVersamentoCori(context)) {
			it.cnr.jada.util.action.RemoteDetailCRUDController rdc = getDistintaCassDet();
			int[] sel = rdc.getSelectedRows(context);
			V_mandato_reversaleBulk doc;
			// controllo prima il selezionato col focus
			int k = rdc.getSelection().getFocus();
			rdc.setModelIndex(context, k);
			doc = (V_mandato_reversaleBulk) rdc.getModel();
			if (doc != null && doc.getVersamento_cori() != null
					&& doc.getVersamento_cori().booleanValue())
				throw new it.cnr.jada.action.MessageToUser(
						"Non � possibile eliminare i mandati di versamento CORI/IVA accentrati dalla distinta!");
			// controllo poi i selezionati con flag
			for (int i = 0; i < sel.length; i++) {
				rdc.setModelIndex(context, sel[i]);
				doc = (V_mandato_reversaleBulk) rdc.getModel();
				if (doc != null && doc.getVersamento_cori() != null
						&& doc.getVersamento_cori().booleanValue()) {
					throw new it.cnr.jada.action.MessageToUser(
							"Non � possibile eliminare i mandati di versamento CORI/IVA accentrati dalla distinta!");
				}
			}

		}
	}

	/**
	 * E' vero se � stato impostato il flag nei parametri generali
	 * FL_VERSAMENTO_CORI che indica se inserire i mandati di versamento CORI in
	 * modo obbligatorio e automatico
	 */
	public boolean isInserisciMandatiVersamentoCori(
			it.cnr.jada.action.ActionContext context)
			throws BusinessProcessException {
		if (getParametriCnr() == null) {
			try {
				setParametriCnr(((DistintaCassiereComponentSession) createComponentSession())
						.parametriCnr(context.getUserContext()));
			} catch (ComponentException e) {
				throw handleException(e);
			} catch (RemoteException e) {
				throw handleException(e);
			} catch (BusinessProcessException e) {
				throw handleException(e);
			}
		}

		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);

		if (getParametriCnr().getFl_versamenti_cori().booleanValue()
				&& day >= getParametriCnr().getVersamenti_cori_giorno()
						.intValue())
			return (true);
		else
			return (false);
	}

	/**
	 * @return
	 */
	public Parametri_cnrBulk getParametriCnr() {
		return parametriCnr;
	}

	/**
	 * @param bulk
	 */
	public void setParametriCnr(Parametri_cnrBulk bulk) {
		parametriCnr = bulk;
	}

	public boolean isUoDistintaTuttaSac(ActionContext context)
			throws ComponentException, RemoteException {
		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
				.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		if (sess.getVal01(context.getUserContext(), new Integer(0), null,
				"UO_SPECIALE", "UO_DISTINTA_TUTTA_SAC") == null)
			throw new ApplicationException(
					"Configurazione CNR: non sono stati impostati i valori per UO SPECIALE - UO DISTINTA TUTTA SAC");
		if (sess.getVal01(context.getUserContext(), new Integer(0), null,
				"UO_SPECIALE", "UO_DISTINTA_TUTTA_SAC").equals(
				it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(
						context).getCd_unita_organizzativa())) {
			setElencoConUo(true);
			return true;
		}
		setElencoConUo(false);
		return false;
	}

	public boolean isElencoConUo() {
		return elencoConUo;
	}

	public void setElencoConUo(boolean elencoConUo) {
		this.elencoConUo = elencoConUo;
	}

	/**
	 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
	 */
	public final it.cnr.jada.util.action.RemoteDetailCRUDController getDistinteCassCollegateDet() {
		return distinteCassCollegateDet;
	}

	public boolean isUoEnte() {
		return (getUoSrivania()
				.getCd_tipo_unita()
				.compareTo(
						it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE) == 0);
	}

	public Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	public void setUoSrivania(Unita_organizzativaBulk bulk) {
		uoSrivania = bulk;
	}

	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		super.initialize(actioncontext);
		setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo
				.getUnita_organizzativa(actioncontext));
		if (this.isEditable()) {
			((Distinta_cassiereBulk) this.getModel()).setFl_flusso(flusso);
			((Distinta_cassiereBulk) this.getModel()).setFl_sepa(sepa);
			((Distinta_cassiereBulk) this.getModel()).setFl_annulli(annulli);
		}
	}

	public OggettoBulk createNewSearchBulk(ActionContext context)
			throws BusinessProcessException {
		Distinta_cassiereBulk fs = (Distinta_cassiereBulk) super
				.createNewSearchBulk(context);
		this.setFile(null);
		if (this.isEditable()) {
			fs.setFl_flusso(isFlusso());
			fs.setFl_sepa(isSepa());
			fs.setFl_annulli(isAnnulli());
		}
		return fs;
	}

	public Boolean isFlusso() {
		return flusso;
	}

	public void setFlusso(Boolean flusso) {
		this.flusso = flusso;
	}

	public boolean isScaricaButtonEnabled() {
		if (!isEstraiButtonHidden() && getFile() != null)
			return true;
		else
			return false;
	}

	public boolean isEstraiButtonHidden() {
		return (((Distinta_cassiereBulk) getModel()).getDt_invio() == null);
	}

	public void writeToolbar(PageContext pagecontext) throws IOException,
			ServletException {
		Button[] toolbar = getToolbar();
		if (!this.getParametriCnr().getFl_tesoreria_unica().booleanValue()) {
			if (getFile() != null) {
				HttpServletResponse httpservletresp = (HttpServletResponse) pagecontext
						.getResponse();
				HttpServletRequest httpservletrequest = (HttpServletRequest) pagecontext
						.getRequest();
				StringBuffer stringbuffer = new StringBuffer();
				stringbuffer.append(pagecontext.getRequest().getScheme());
				stringbuffer.append("://");
				stringbuffer.append(pagecontext.getRequest().getServerName());
				stringbuffer.append(':');
				stringbuffer.append(pagecontext.getRequest().getServerPort());
				stringbuffer.append(JSPUtils.getAppRoot(httpservletrequest));
				toolbar[10].setHref("javascript:doPrint('" + stringbuffer
						+ getFile() + "')");
			}
		}
		super.writeToolbar(pagecontext);
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void generaXML(ActionContext context) throws ComponentException,
			RemoteException, BusinessProcessException {
		try {

			JAXBContext jc = JAXBContext
					.newInstance("it.cnr.contab.doccont00.intcass.xmlbnl");
			ObjectFactory obj = new ObjectFactory();
			// creo i file del flusso

			// Testata
			FlussoOrdinativi currentFlusso = null;
			currentFlusso = obj.createFlussoOrdinativi();

			Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
					.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
			if (sess.getVal01(context.getUserContext(),
					it.cnr.contab.utenze00.bulk.CNRUserInfo
							.getEsercizio(context), null, "FLUSSO_ORDINATIVI",
					"CODICE_ABI_BT") == null)
				throw new ApplicationException(
						"Configurazione mancante per flusso Ordinativo");
			Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) this
					.getModel();
			String CodiceAbi = sess.getVal01(context.getUserContext(),
					it.cnr.contab.utenze00.bulk.CNRUserInfo
							.getEsercizio(context), null, "FLUSSO_ORDINATIVI",
					"CODICE_ABI_BT");
			currentFlusso.setCodiceABIBT(CodiceAbi);
			currentFlusso
					.setIdentificativoFlusso(it.cnr.contab.utenze00.bulk.CNRUserInfo
							.getEsercizio(context).toString()
							+ "-"
							+ distinta.getCd_unita_organizzativa()
							+ "-"
							+ distinta.getPg_distinta_def().toString() + "-I");// Inserito
																				// "I"
																				// alla
																				// fine
																				// in
																				// caso
																				// di
																				// gestione
																				// Rinvio
			GregorianCalendar gcdi = new GregorianCalendar();
			gcdi.setTimeInMillis(it.cnr.jada.util.ejb.EJBCommonServices
					.getServerTimestamp().getTime());
			currentFlusso.setDataOraCreazioneFlusso(DatatypeFactory
					.newInstance().newXMLGregorianCalendar(
							new GregorianCalendar(gcdi.get(Calendar.YEAR), gcdi
									.get(Calendar.MONTH), gcdi
									.get(Calendar.DAY_OF_MONTH), gcdi
									.get(Calendar.HOUR_OF_DAY), gcdi
									.get(Calendar.MINUTE), gcdi
									.get(Calendar.SECOND))));

			ExtCassiereCdsBulk extcas = ((DistintaCassiereComponentSession) createComponentSession())
					.recuperaCodiciCdsCassiere(context.getUserContext(),
							(Distinta_cassiereBulk) getModel());

			currentFlusso.setCodiceEnte(Formatta(extcas.getCodiceProto(), "D",
					6, "0"));
					
					Liquid_coriComponentSession component = (Liquid_coriComponentSession)this.createComponentSession("CNRCORI00_EJB_Liquid_coriComponentSession",Liquid_coriComponentSession.class );
					AnagraficoBulk uoEnte=null;
			if (Utility.createParametriCnrComponentSession().getParametriCnr(context.getUserContext(),distinta.getEsercizio()).getFl_tesoreria_unica().booleanValue()){
			 	uoEnte=(AnagraficoBulk)component.getAnagraficoEnte(context.getUserContext());
			 	currentFlusso.setDescrizioneEnte(uoEnte.getRagione_sociale());
			}
			else 
				currentFlusso.setDescrizioneEnte(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getDs_unita_organizzativa());

			BancaBulk banca = ((DistintaCassiereComponentSession) createComponentSession())
					.recuperaIbanUo(context.getUserContext(),
							((Distinta_cassiereBulk) getModel())
									.getUnita_organizzativa());
			currentFlusso.setCodiceEnteBT(currentFlusso.getCodiceEnte() + "-"
					+ banca.getCodice_iban() + "-" + extcas.getCodiceSia());
			currentFlusso.setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo
					.getEsercizio(context));

			List dettagliRev = ((DistintaCassiereComponentSession) createComponentSession())
					.dettagliDistinta(
							context.getUserContext(),
							distinta,
							it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV);
			// Elaboriamo prima le reversali
			Reversale currentReversale = null;
			for (Iterator i = dettagliRev.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
						.next();
				currentReversale = (Reversale) recuperaDatiReversaleFlusso(
						context.getUserContext(), bulk);
				if (bulk.getTi_cc_bi().compareTo(SospesoBulk.TIPO_BANCA_ITALIA) == 0) {
					// bisogna aggiornare l'iban se banca d'italia ma lo posso
					// sapere solo in questo punto
					currentFlusso.setCodiceEnteBT(currentFlusso.getCodiceEnte()
							+ "-"
							+ component.getContoSpecialeEnteF24(context
									.getUserContext()) + "-"
							+ extcas.getCodiceSia());
				}
				currentFlusso.getReversale().add(currentReversale);
			}
			List dettagliMan = ((DistintaCassiereComponentSession) createComponentSession())
					.dettagliDistinta(
							context.getUserContext(),
							distinta,
							it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);
			// Mandati
			Mandato currentMandato = null;
			for (Iterator i = dettagliMan.iterator(); i.hasNext();) {
				V_mandato_reversaleBulk bulk = (V_mandato_reversaleBulk) i
						.next();
				currentMandato = recuperaDatiMandatoFlusso(
						context.getUserContext(), bulk);
				currentFlusso.getMandato().add(currentMandato);
			}
			String fileName = currentFlusso.getIdentificativoFlusso() + ".xslt";
			File file = new File(System.getProperty("tmp.dir.SIGLAWeb")
					+ "/tmp/", fileName);

			FileOutputStream fileOutputStream = new FileOutputStream(file);

			Marshaller jaxbMarshaller = jc.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			jaxbMarshaller.marshal(currentFlusso, fileOutputStream);

			fileOutputStream.flush();
			fileOutputStream.close();
			setFile("/tmp/" + file.getName());
			setMessage("Flusso generato");
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private Mandato recuperaDatiMandatoFlusso(UserContext userContext,
			V_mandato_reversaleBulk bulk) throws ComponentException,
			RemoteException, BusinessProcessException {
		try {
			DistintaCassiereComponentSession componentDistinta = ((DistintaCassiereComponentSession) createComponentSession());
			BancaBulk bancauo = componentDistinta.recuperaIbanUo(userContext,
					bulk.getUo());
			Mandato man = new Mandato();
			List list = componentDistinta
					.findDocumentiFlusso(userContext, bulk);
			man.setTipoOperazione("INSERIMENTO");
			GregorianCalendar gcdi = new GregorianCalendar();

			boolean obb_iban = false;
			boolean obb_conto = false;
			boolean obb_dati_beneficiario = false;
			it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario infoben = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario();
			it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
			it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Bollo bollo = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Bollo();
			it.cnr.contab.doccont00.intcass.xmlbnl.SepaCreditTransfer sepa = new it.cnr.contab.doccont00.intcass.xmlbnl.SepaCreditTransfer();
			it.cnr.contab.doccont00.intcass.xmlbnl.Piazzatura piazzatura = new it.cnr.contab.doccont00.intcass.xmlbnl.Piazzatura();
			it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk docContabile = null;
			it.cnr.contab.doccont00.intcass.xmlbnl.Beneficiario benef = new it.cnr.contab.doccont00.intcass.xmlbnl.Beneficiario();
			it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Sospeso sosp = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Sospeso();
			it.cnr.contab.doccont00.intcass.xmlbnl.Ritenute riten = new it.cnr.contab.doccont00.intcass.xmlbnl.Ritenute();
			it.cnr.contab.doccont00.intcass.xmlbnl.InformazioniAggiuntive aggiuntive = new it.cnr.contab.doccont00.intcass.xmlbnl.InformazioniAggiuntive();
			for (Iterator i = list.iterator(); i.hasNext();) {
				docContabile = (it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk) i
						.next();
				obb_iban = false;
				obb_conto = false;
				obb_dati_beneficiario = false;
				man.setNumeroMandato(docContabile.getPgDocumento().intValue());
				gcdi.setTime(docContabile.getDtEmissione());
				XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcdi);
				xgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setSecond(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setMinute(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setHour(DatatypeConstants.FIELD_UNDEFINED);
				man.setDataMandato(xgc);
				man.setImportoMandato(docContabile.getImDocumento().setScale(2,
						BigDecimal.ROUND_HALF_UP));
				man.setContoEvidenza(bancauo.getNumero_conto());
				infoben.setProgressivoBeneficiario(1);// Dovrebbe essere sempre
														// 1 ?
				infoben.setImportoBeneficiario(docContabile.getImDocumento()
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0)
					infoben.setTipoPagamento("REGOLARIZZAZIONE");
				else if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_PAGAMENTO) == 0
						&& docContabile.getModalitaPagamento() != null
						&& (docContabile.getModalitaPagamento()
								.compareTo("ASC") == 0 || docContabile
								.getModalitaPagamento().compareTo("ASCNT") == 0)) {
					infoben.setTipoPagamento("ASSEGNO CIRCOLARE");
					obb_dati_beneficiario = true;
				} else if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_PAGAMENTO) == 0
						&& docContabile.getCdIso() != null
						&& docContabile.getCdIso().compareTo("IT") == 0) {
					// 18/06/2014 BNL non gestisce sepa
					// infoben.setTipoPagamento("SEPA CREDIT TRANSFER");
					infoben.setTipoPagamento("BONIFICO BANCARIO E POSTALE");
					// 08/09/2014 resi obbligatori come da mail ricevuta da
					// ANGELINI/MESSERE
					obb_dati_beneficiario = true;
					obb_iban = true;
				} else if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_PAGAMENTO) == 0
						&& docContabile.getCdIso() != null
						&& docContabile.getCdIso().compareTo("IT") != 0) {
					infoben.setTipoPagamento("SEPA CREDIT TRANSFER");
					// 11/07/2015 Verificare se per i mandati circuito sepa
					// escluso quelli italiani funziona
					// obb_dati_beneficiario=true;
					obb_iban = true;
				} else if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_PAGAMENTO) == 0
						&& docContabile.getModalitaPagamento() != null
						&& docContabile.getModalitaPagamento().compareTo("RD") == 0) {
					infoben.setTipoPagamento("CASSA");
				} else if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_PAGAMENTO) == 0
						&& docContabile.getModalitaPagamento() != null
						&& docContabile.getModalitaPagamento().compareTo("CCP") == 0) {
					infoben.setTipoPagamento("ACCREDITO CONTO CORRENTE POSTALE");
					obb_conto = true;
				}
				// 19/11/2015 MANDATI a NETTO 0, richiesta modifica tipo
				// pagamento
				// if(bulk.getIm_documento_cont().compareTo(bulk.getIm_ritenute())==0)
				// infoben.setTipoPagamento("COMPENSAZIONE");
				// Classificazioni
				List listClass = componentDistinta.findDocumentiFlussoClass(
						userContext, bulk);
				BigDecimal totAssSiope = BigDecimal.ZERO;
				BigDecimal totAssCup = BigDecimal.ZERO;
				VDocumentiFlussoBulk oldDoc = null;
				for (Iterator c = listClass.iterator(); c.hasNext();) {
					VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
					if (doc.getCdSiope() != null) {
						// cambio codice siope senza cup dovrebbe essere il
						// resto
						if (oldDoc != null
								&& oldDoc.getCdSiope().compareTo(
										doc.getCdSiope()) != 0
								&& doc.getCdCup() == null
								&& totAssSiope.compareTo(totAssCup) != 0
								&& totAssCup.compareTo(BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(oldDoc.getCdSiope());
							clas.setImporto((totAssSiope.subtract(totAssCup))
									.setScale(2, BigDecimal.ROUND_HALF_UP));
							totAssCup = BigDecimal.ZERO;
							totAssSiope = BigDecimal.ZERO;
							infoben.getClassificazione().add(clas);

						} else
						// stesso codice siope senza cup resto
						if (oldDoc != null
								&& oldDoc.getCdSiope().compareTo(
										doc.getCdSiope()) == 0
								&& doc.getCdCup() == null
								&& totAssSiope.compareTo(totAssCup) != 0
								&& totAssCup.compareTo(BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(oldDoc.getCdSiope());
							clas.setImporto(totAssSiope.subtract(totAssCup)
									.setScale(2, BigDecimal.ROUND_HALF_UP));
							totAssCup = BigDecimal.ZERO;
							totAssSiope = BigDecimal.ZERO;
							infoben.getClassificazione().add(clas);
						} else // stesso codice siope con cup
						if (oldDoc != null
								&& oldDoc.getCdSiope().compareTo(
										doc.getCdSiope()) == 0
								&& doc.getCdCup() != null
								&& totAssSiope.compareTo(totAssCup) != 0
								&& totAssCup.compareTo(BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(doc.getCdSiope());
							clas.setCodiceCup(doc.getCdCup());
							clas.setImporto(doc.getImportoCup().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							totAssCup = totAssCup.add(doc.getImportoCup());
							// Causale Cup
							if (infoben.getCausale() != null) {
								if (!infoben.getCausale().contains(
										doc.getCdCup()))
									infoben.setCausale(infoben.getCausale()
											+ "-" + doc.getCdCup());
							} else
								infoben.setCausale("CUP " + doc.getCdCup());
							infoben.getClassificazione().add(clas);
						} else // stesso siope con cup null precedente
								// completamente associato a cup
						if (oldDoc != null
								&& oldDoc.getCdSiope().compareTo(
										doc.getCdSiope()) == 0
								&& doc.getCdCup() == null
								&& totAssSiope.compareTo(totAssCup) == 0
								&& totAssCup.compareTo(BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(doc.getCdSiope());
							clas.setImporto(doc.getImportoCge().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							totAssSiope = BigDecimal.ZERO;
							totAssCup = BigDecimal.ZERO;
							infoben.getClassificazione().add(clas);
						} else // diverso siope con cup null e precedente
								// completamente associato a cup
						if (oldDoc != null
								&& oldDoc.getCdSiope().compareTo(
										doc.getCdSiope()) != 0
								&& doc.getCdCup() == null
								&& totAssSiope.compareTo(totAssCup) == 0
								&& totAssCup.compareTo(BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(doc.getCdSiope());
							clas.setImporto(doc.getImportoCge().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							totAssSiope = BigDecimal.ZERO;
							totAssCup = BigDecimal.ZERO;
							infoben.getClassificazione().add(clas);
						} else
						// primo inserimento
						if (doc.getCdCup() != null
								&& doc.getImportoCup().compareTo(
										BigDecimal.ZERO) != 0) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(doc.getCdSiope());
							clas.setCodiceCup(doc.getCdCup());
							clas.setImporto(doc.getImportoCup().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							totAssCup = doc.getImportoCup();
							totAssSiope = doc.getImportoCge();
							// Causale Cup
							if (infoben.getCausale() != null) {
								if (!infoben.getCausale().contains(
										doc.getCdCup()))
									infoben.setCausale(infoben.getCausale()
											+ "-" + doc.getCdCup());
							} else
								infoben.setCausale("CUP " + doc.getCdCup());
							infoben.getClassificazione().add(clas);
						} else {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
							clas.setCodiceCgu(doc.getCdSiope());
							clas.setImporto(doc.getImportoCge().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							totAssSiope = doc.getImportoCge();
							infoben.getClassificazione().add(clas);
						}
						oldDoc = doc;
					}
				}
				// differenza ultimo
				if (totAssSiope.subtract(totAssCup).compareTo(BigDecimal.ZERO) > 0) {
					if (totAssCup.compareTo(BigDecimal.ZERO) != 0
							&& totAssCup.compareTo(totAssSiope) != 0) {
						clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Classificazione();
						clas.setCodiceCgu(oldDoc.getCdSiope());
						clas.setImporto((totAssSiope.subtract(totAssCup))
								.setScale(2, BigDecimal.ROUND_HALF_UP));
						infoben.getClassificazione().add(clas);
					}
				}
				bollo.setAssoggettamentoBollo(docContabile
						.getAssoggettamentoBollo());
				bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());
				infoben.setBollo(bollo);
				benef.setAnagraficaBeneficiario(RemoveAccent
						.convert(docContabile.getDenominazioneSede())
						.replace('"', ' ').replace('�', ' '));
				// benef.setStatoBeneficiario(docContabile.getCdIso());
				if (obb_dati_beneficiario) {
					benef.setIndirizzoBeneficiario(RemoveAccent
							.convert(docContabile.getViaSede())
							.replace('"', ' ').replace('�', ' '));
					if (docContabile.getCapComuneSede() == null)
						throw new ApplicationException(
								"Impossibile generare il flusso, Cap benificiario non valorizzato per il terzo "
										+ docContabile.getCdTerzo()
										+ " cds "
										+ docContabile.getCdCds()
										+ " mandato "
										+ docContabile.getPgDocumento());
					benef.setCapBeneficiario(docContabile.getCapComuneSede());
					benef.setLocalitaBeneficiario(RemoveAccent
							.convert(docContabile.getDsComune())
							.replace('"', ' ').replace('�', ' '));
					benef.setProvinciaBeneficiario(docContabile
							.getCdProvincia());
				}
				infoben.setBeneficiario(benef);
				if (obb_iban && docContabile.getCdIso().compareTo("IT") == 0) {
					piazzatura.setAbiBeneficiario(docContabile.getAbi());
					piazzatura.setCabBeneficiario(docContabile.getCab());
					piazzatura.setNumeroContoCorrenteBeneficiario(docContabile
							.getNumeroConto());
					piazzatura.setCaratteriControllo(docContabile
							.getCodiceIban().substring(2, 4));
					piazzatura.setCodiceCin(docContabile.getCin());
					piazzatura.setCodicePaese(docContabile.getCdIso());
					infoben.setPiazzatura(piazzatura);
					// 18/06/2014 BNL non gestisce sepa
					/*
					 * sepa.setIban(docContabile.getNumeroConto());
					 * if(docContabile.getBic()!=null &&
					 * docContabile.getNumeroConto()!=null &&
					 * (docContabile.getBic().length()>=8 &&
					 * docContabile.getBic().length()<=11 ))// &&
					 * docContabile.getNumeroConto().substring(0,
					 * 2).compareTo("IT")!=0 )
					 * sepa.setBic(docContabile.getBic()); else throw new
					 * ApplicationException
					 * ("Formato del codice bic non valido.");
					 * sepa.setIdentificativoEndToEnd
					 * (docContabile.getEsercizio()
					 * .toString()+"-"+docContabile.getCdUoOrigine
					 * ()+"-"+docContabile.getPgDocumento().toString());
					 * infoben.setSepaCreditTransfer(sepa);
					 */
				}
				if (obb_conto) {
					aggiuntive.setRiferimentoDocumentoEsterno(docContabile
							.getNumeroConto().toString());
					infoben.setInformazioniAggiuntive(aggiuntive);
				}
				if (obb_iban && docContabile.getCdIso().compareTo("IT") != 0) {
					// 11/07/2015 BNL non gestisce sepa
					sepa.setIban(docContabile.getCodiceIban());
					if (docContabile.getBic() != null
							&& docContabile.getCodiceIban() != null
							&& (docContabile.getBic().length() >= 8 && docContabile
									.getBic().length() <= 11))// &&
																// docContabile.getNumeroConto().substring(0,
																// 2).compareTo("IT")!=0
																// )
						sepa.setBic(docContabile.getBic());
					else
						throw new ApplicationException(
								"Formato del codice bic non valido.");
					sepa.setIdentificativoEndToEnd(docContabile.getEsercizio()
							.toString()
							+ "-"
							+ docContabile.getCdUoOrigine()
							+ "-" + docContabile.getPgDocumento().toString());
					infoben.setSepaCreditTransfer(sepa);
				}
				if (infoben.getCausale() != null
						&& (infoben.getCausale() + docContabile
								.getDsDocumento()).length() > 99)
					infoben.setCausale((infoben.getCausale() + " " + docContabile
							.getDsDocumento()).substring(0, 98));
				else if (infoben.getCausale() != null)
					infoben.setCausale(infoben.getCausale() + " "
							+ docContabile.getDsDocumento());
				else if (docContabile.getDsDocumento().length() > 99)
					infoben.setCausale(docContabile.getDsDocumento().substring(
							0, 98));
				else
					infoben.setCausale(docContabile.getDsDocumento());
				infoben.setCausale(RemoveAccent.convert(infoben.getCausale())
						.replace('"', ' ').replace('�', ' '));
				// SOSPESO
				if (docContabile.getTiDocumento().compareTo(
						MandatoBulk.TIPO_REGOLAM_SOSPESO) == 0) {

					List listSosp = componentDistinta
							.findDocumentiFlussoSospeso(userContext, bulk);
					for (Iterator c = listSosp.iterator(); c.hasNext();) {
						VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c
								.next();
						if (doc.getCdSospeso() != null) {
							sosp = new it.cnr.contab.doccont00.intcass.xmlbnl.Mandato.InformazioniBeneficiario.Sospeso();
							try {
								sosp.setNumeroProvvisorio(new Long(
										doc.getCdSospeso()
												.substring(
														0,
														doc.getCdSospeso()
																.indexOf(".")))
										.longValue());
							} catch (NumberFormatException e) {
								throw new ApplicationException(
										"Formato del codice del sospeso non compatibile.");
							}
							sosp.setImportoProvvisorio(doc.getImAssociato()
									.setScale(2, BigDecimal.ROUND_HALF_UP));
							infoben.getSospeso().add(sosp);
						}
					}
				}
				// Fine sospeso

				if (bulk.getIm_ritenute().compareTo(BigDecimal.ZERO) != 0) {
					List list_rev = componentDistinta.findReversali(
							userContext, bulk);
					for (Iterator iRev = list_rev.iterator(); iRev.hasNext();) {
						riten = new it.cnr.contab.doccont00.intcass.xmlbnl.Ritenute();
						V_mandato_reversaleBulk rev = (V_mandato_reversaleBulk) iRev
								.next();
						riten.setImportoRitenute(rev.getIm_documento_cont()
								.setScale(2, BigDecimal.ROUND_HALF_UP));
						riten.setNumeroReversale(rev.getPg_documento_cont()
								.intValue());
						riten.setProgressivoVersante(1);// ???
						infoben.getRitenute().add(riten);
					}
				}
				man.getInformazioniBeneficiario().add(infoben);
			}
			return man;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	private Reversale recuperaDatiReversaleFlusso(UserContext userContext,
			V_mandato_reversaleBulk bulk) throws ComponentException,
			RemoteException, BusinessProcessException {
		try {
			DistintaCassiereComponentSession componentDistinta = ((DistintaCassiereComponentSession) createComponentSession());
			Reversale rev = new Reversale();
			List list = componentDistinta
					.findDocumentiFlusso(userContext, bulk);
			rev.setTipoOperazione("INSERIMENTO");
			GregorianCalendar gcdi = new GregorianCalendar();

			it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante infover = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante();
			it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Classificazione clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Classificazione();
			it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Bollo bollo = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Bollo();
			it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk docContabile = null;
			it.cnr.contab.doccont00.intcass.xmlbnl.Versante versante = new it.cnr.contab.doccont00.intcass.xmlbnl.Versante();
			it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Sospeso sosp = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Sospeso();

			for (Iterator i = list.iterator(); i.hasNext();) {
				docContabile = (it.cnr.contab.doccont00.intcass.bulk.VDocumentiFlussoBulk) i
						.next();
				rev.setNumeroReversale(docContabile.getPgDocumento().intValue());
				gcdi.setTime(docContabile.getDtEmissione());
				XMLGregorianCalendar xgc = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(gcdi);
				xgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setSecond(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setMinute(DatatypeConstants.FIELD_UNDEFINED);
				xgc.setHour(DatatypeConstants.FIELD_UNDEFINED);
				rev.setDataReversale(xgc);
				rev.setImportoReversale(docContabile.getImDocumento().setScale(
						2, BigDecimal.ROUND_HALF_UP));
				rev.setContoEvidenza(docContabile.getNumeroConto());

				infover.setProgressivoVersante(1);// Dovrebbe essere sempre 1 ?
				infover.setImportoVersante(docContabile.getImDocumento()
						.setScale(2, BigDecimal.ROUND_HALF_UP));
				// tipologia non gestita da committare
				// if(bulk.getPg_documento_cont_padre()!=bulk.getPg_documento_cont())
				// infover.setTipoRiscossione("COMPENSAZIONE");
				if (docContabile.getTiDocumento().compareTo(
						ReversaleBulk.TIPO_REGOLAM_SOSPESO) == 0)
					infover.setTipoRiscossione("REGOLARIZZAZIONE");
				// da committare
				// if(docContabile.getTiDocumento().compareTo(ReversaleBulk.TIPO_INCASSO)==0
				// &&
				// bulk.getPg_documento_cont_padre().compareTo(bulk.getPg_documento_cont())==0)
				if (docContabile.getTiDocumento().compareTo(
						ReversaleBulk.TIPO_INCASSO) == 0)
					infover.setTipoRiscossione("CASSA");
				// Classificazioni

				List listClass = componentDistinta
						.findDocumentiFlussoClassReversali(userContext, bulk);
				VDocumentiFlussoBulk oldDoc = null;
				for (Iterator c = listClass.iterator(); c.hasNext();) {
					VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c.next();
					if (doc.getCdSiope() != null && oldDoc != null
							&& oldDoc.getCdSiope() != null) {
						if ((oldDoc.getCdSiope().compareTo(doc.getCdSiope()) != 0)
								|| (oldDoc.getCdTipoDocumentoAmm() != null && oldDoc
										.getCdTipoDocumentoAmm().compareTo(
												doc.getCdTipoDocumentoAmm()) != 0)
								|| (oldDoc.getPgDocAmm() != null && oldDoc
										.getPgDocAmm().compareTo(
												doc.getPgDocAmm()) != 0)) {
							clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Classificazione();
							clas.setCodiceCge(doc.getCdSiope());
							clas.setImporto(doc.getImportoCge().setScale(2,
									BigDecimal.ROUND_HALF_UP));
							infover.getClassificazione().add(clas);
							oldDoc = doc;
						}
					} else if (doc.getCdSiope() != null) {
						clas = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Classificazione();
						clas.setCodiceCge(doc.getCdSiope());
						clas.setImporto(doc.getImportoCge().setScale(2,
								BigDecimal.ROUND_HALF_UP));
						infover.getClassificazione().add(clas);
						oldDoc = doc;
					}
					if (infover.getCausale() != null && doc.getCdCup() != null) {
						if (!infover.getCausale().contains(doc.getCdCup()))
							infover.setCausale(infover.getCausale() + "-"
									+ doc.getCdCup());
					} else if (doc.getCdCup() != null)
						infover.setCausale("CUP " + doc.getCdCup());
				}
				// Fine classificazioni
				bollo = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Bollo();
				bollo.setAssoggettamentoBollo(docContabile
						.getAssoggettamentoBollo());
				bollo.setCausaleEsenzioneBollo(docContabile.getCausaleBollo());

				infover.setBollo(bollo);
				versante.setAnagraficaVersante(RemoveAccent
						.convert(docContabile.getDenominazioneSede())
						.replace('"', ' ').replace('�', ' '));
				infover.setVersante(versante);

				// gestito inserimento cup nella CAUSALE
				if (infover.getCausale() != null
						&& (infover.getCausale() + docContabile
								.getDsDocumento()).length() > 99)
					infover.setCausale((infover.getCausale() + " " + docContabile
							.getDsDocumento()).substring(0, 98));
				else if (infover.getCausale() != null)
					infover.setCausale(infover.getCausale() + " "
							+ docContabile.getDsDocumento());
				else if (docContabile.getDsDocumento().length() > 99)
					infover.setCausale(docContabile.getDsDocumento().substring(
							0, 98));
				else
					infover.setCausale(docContabile.getDsDocumento());
				infover.setCausale(RemoveAccent.convert(infover.getCausale())
						.replace('"', ' ').replace('�', ' '));
				// SOSPESO
				if (docContabile.getTiDocumento().compareTo(
						ReversaleBulk.TIPO_REGOLAM_SOSPESO) == 0) {
					List listSosp = componentDistinta
							.findDocumentiFlussoSospeso(userContext, bulk);
					for (Iterator c = listSosp.iterator(); c.hasNext();) {
						VDocumentiFlussoBulk doc = (VDocumentiFlussoBulk) c
								.next();
						if (doc.getCdSospeso() != null) {
							sosp = new it.cnr.contab.doccont00.intcass.xmlbnl.Reversale.InformazioniVersante.Sospeso();
							try {
								sosp.setNumeroProvvisorio(new Long(doc
										.getCdSospeso().substring(
												0,
												doc.getCdSospeso().lastIndexOf(
														"."))).longValue());
							} catch (NumberFormatException e) {
								throw new ApplicationException(
										"Formato del codice del sospeso non compatibile.");
							}
							sosp.setImportoProvvisorio(doc.getImAssociato()
									.setScale(2, BigDecimal.ROUND_HALF_UP));
							infover.getSospeso().add(sosp);
						}
					}
					// Fine sospeso
				}
				rev.getInformazioniVersante().add(infover);
			}
			return rev;
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public String Formatta(String s, String allineamento, Integer dimensione,
			String riempimento) {
		if (s == null)
			s = riempimento;
		if (s.length() < dimensione) {
			if (allineamento.compareTo("D") == 0) {
				while (s.length() < dimensione)
					s = riempimento + s;
				return s.toUpperCase();
			} else {
				while (s.length() < dimensione)
					s = s + riempimento;
				return s.toUpperCase();
			}
		} else if (s.length() > dimensione) {
			s = s.substring(0, dimensione);
			return s.toUpperCase();
		}
		return s.toUpperCase();
	}

	public boolean isSignButtonEnabled() {
		if (firmatarioDistinta == null)
			return false;
		if (super.isDeleteButtonEnabled()
				&& (((Distinta_cassiereBulk) getModel()).getDt_invio_pec() == null)
				&& (!isFlusso()))
			return true;
		if (super.isDeleteButtonEnabled()
				&& (((Distinta_cassiereBulk) getModel()).getDt_invio() != null)
				&& (isFlusso()))
			return true;
		else
			return false;
	}

	public boolean isSalvaDefButtonHidden() {
		if (!isFlusso())
			return true;
		else
			return false;
	}

	public boolean isSalvaDefButtonEnabled() {
		if (isSaveButtonEnabled()
				&& (((Distinta_cassiereBulk) getModel()).getDt_invio() == null))
			return true;
		else
			return false;
	}

	public void scaricaDocumento(ActionContext actioncontext) throws Exception {
		Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext)
				.getParameter("esercizio"));
		String cds = ((HttpActionContext) actioncontext).getParameter("cds");
		Long numero_documento = Long
				.valueOf(((HttpActionContext) actioncontext)
						.getParameter("numero_documento"));
		String tipo = ((HttpActionContext) actioncontext).getParameter("tipo");
		InputStream is = documentiContabiliService.getStreamDocumento(
				new V_mandato_reversaleBulk(esercizio, tipo, cds, numero_documento)
		);
		if (is != null) {
			((HttpActionContext) actioncontext).getResponse().setContentType(
					"application/pdf");
			OutputStream os = ((HttpActionContext) actioncontext).getResponse()
					.getOutputStream();
			((HttpActionContext) actioncontext).getResponse().setDateHeader(
					"Expires", 0);
			byte[] buffer = new byte[((HttpActionContext) actioncontext)
					.getResponse().getBufferSize()];
			int buflength;
			while ((buflength = is.read(buffer)) > 0) {
				os.write(buffer, 0, buflength);
			}
			is.close();
			os.flush();
		}
	}

	public StorageObject inviaDistinta(ActionContext context,
                                       Distinta_cassiereBulk distinta)
			throws it.cnr.jada.action.BusinessProcessException {
		try {
			// Invio al cassiere serve per salvare in definitivo la distinta
			// prima di archiviare la stampa e firmarla
			it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession distintaComp = (it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession) createComponentSession();
			distinta = distintaComp.inviaDistinta(context.getUserContext(),
					distinta);
			commitUserTransaction();
			try {
				basicEdit(context, getModel(), true);
			} catch (BusinessProcessException businessprocessexception) {
				setModel(context, null);
				setDirty(false);
				throw businessprocessexception;
			}
			Format dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);

			print.setReport("/doccont/doccont/distinta_cassiere.jasper");
			print.setNomeFile("Distinta n. " + distinta.getPg_distinta_def()
					+ ".pdf");
			print.setUtcr(context.getUserContext().getUser());
			print.addParam("cd_cds", distinta.getCd_cds(), String.class);
			print.addParam("cd_unita_organizzativa",
					distinta.getCd_unita_organizzativa(), String.class);
			print.addParam("esercizio", distinta.getEsercizio().toString(),
					String.class);
			print.addParam("pg_distinta", distinta.getPg_distinta().toString(),
					String.class);

			Report report = SpringUtil.getBean("printService",
					PrintService.class).executeReport(context.getUserContext(),
					print);

			return documentiContabiliService.storeSimpleDocument(
			        distinta, report.getInputStream(), report.getContentType(), report.getName(), distinta.getStorePath()
            );
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
	}

	public void invia(ActionContext context, FirmaOTPBulk firmaOTPBulk)
			throws Exception {
		Map<String, String> subjectDN = documentiContabiliService
				.getCertSubjectDN(firmaOTPBulk.getUserName(),
						firmaOTPBulk.getPassword());
		if (subjectDN == null)
			throw new ApplicationException(
					"Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!");
		String codiceFiscale = subjectDN.get("SERIALNUMBER").substring(3);
		UtenteBulk utente = ((CNRUserInfo) context.getUserInfo()).getUtente();
		if (controlloCodiceFiscale != null
				&& controlloCodiceFiscale.equalsIgnoreCase("Y")
				&& utente.getCodiceFiscaleLDAP() != null 
				&& !utente.getCodiceFiscaleLDAP().equalsIgnoreCase(
						codiceFiscale)) {
			throw new ApplicationException("Il codice fiscale \""
					+ codiceFiscale + "\" presente sul certicato di Firma, "
					+ "� diverso da quello dell'utente collegato \""
					+ utente.getCodiceFiscaleLDAP() + "\"!");
		}
		if (!this.isFlusso()) {
			final Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
			// spostato nel salva definitivo anche in questo caso
			StorageObject distintaStorageObject = Optional.ofNullable(distinta.getPg_distinta_def())
                    .map(paDistintaDef -> documentiContabiliService.getStorageObjectByPath(
                            distinta.getStorePath().concat(StoreService.BACKSLASH).concat(distinta.getCMISName())
                    )).orElse(inviaDistinta(context, distinta));
			List<String> nodes = Arrays.asList((String)distintaStorageObject.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()));
			List<V_mandato_reversaleBulk> dettagliRev = ((DistintaCassiereComponentSession) createComponentSession())
					.dettagliDistinta(
							context.getUserContext(),
							distinta,
							it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV);
            dettagliRev.stream()
                    .map(v_mandato_reversaleBulk -> documentiContabiliService.getDocumentKey(v_mandato_reversaleBulk, true))
                    .forEach(s -> nodes.add(s));

			List<V_mandato_reversaleBulk> dettagliMan = ((DistintaCassiereComponentSession) createComponentSession())
					.dettagliDistinta(
							context.getUserContext(),
							distinta,
							it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);
            dettagliMan.stream()
                    .map(v_mandato_reversaleBulk -> documentiContabiliService.getDocumentKey(v_mandato_reversaleBulk, true))
                    .forEach(s -> nodes.add(s));

			PdfSignApparence pdfSignApparence = new PdfSignApparence();
			pdfSignApparence.setNodes(nodes);
			pdfSignApparence.setUsername(firmaOTPBulk.getUserName());
			pdfSignApparence.setPassword(firmaOTPBulk.getPassword());
			pdfSignApparence.setOtp(firmaOTPBulk.getOtp());

			Apparence apparence = new Apparence(null, "Rome", "Firma ",
					"per invio all'Istituto cassiere\nFirmato dal "
							+ getTitolo() + "\n" + subjectDN.get("GIVENNAME")
							+ " " + subjectDN.get("SURNAME"), 300, 40, 1, 550,
					80);
			pdfSignApparence.setApparence(apparence);
			try {
				documentiContabiliService.signDocuments(new GsonBuilder().create().toJson(pdfSignApparence), "service/sigla/firma/doccont");
			} catch (StorageException _ex) {
				throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
			}
			try {
				if(!this.isAnnulli()){
					if (distinta.getEsercizio() != null
							&& distinta.getPg_distinta_def() != null)
						documentiContabiliService.inviaDistintaPEC(nodes,
								this.isSepa(), distinta.getEsercizio() + "/"
										+ distinta.getPg_distinta_def());
					else
						documentiContabiliService.inviaDistintaPEC(nodes,
								this.isSepa(), null);
				}	
				distinta.setDt_invio_pec(DateServices.getDt_valida(context
						.getUserContext()));
				distinta.setUser(((CNRUserContext) context.getUserContext())
						.getUser());
				distinta.setToBeUpdated();
				createComponentSession().modificaConBulk(context.getUserContext(), distinta);
				commitUserTransaction();
				setMessage("Invio effettuato correttamente.");

			} catch (IOException e) {
				throw new BusinessProcessException(e);
			} catch (Exception e) {
				throw new BusinessProcessException(e);
			}
		} else {
			Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
			generaXML(context);
			File file = new File(System.getProperty("tmp.dir.SIGLAWeb")
					+ getFile());
			CMISFile cmisFile = new CMISFile(file, file.getName());
			if (cmisFile != null) {
				// E' previsto solo l'inserimento ma non l'aggiornamento
				try {
                    StorageObject storageObject = documentiContabiliService.storeSimpleDocument(
                            cmisFile, cmisFile.getInputStream(), cmisFile.getContentType(),
                            cmisFile.getFileName(), distinta.getStorePath(), false
                    );
					cmisFile.setStorageObject(storageObject);
				} catch (StorageException e) {
					if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
						throw new ApplicationException(
								"File ["
										+ cmisFile.getFileName()
										+ "] gi� presente o non completo di tutte le propriet� obbligatorie. Inserimento non possibile!");
					throw new ApplicationException(
							"Errore nella registrazione del file XML sul Documentale (" + e.getMessage() + ")");
				}
				if (cmisFile.getStorageObject().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0) {
                    Optional.ofNullable(documentiContabiliService
                            .getStorageObjectByPath(distinta
                                    .getStorePath().concat(StoreService.BACKSLASH)
                                    .concat(String.valueOf(distinta.getEsercizio()))
                                    .concat("-").concat(distinta.getCd_unita_organizzativa())
                                    .concat("-").concat(String.valueOf(distinta.getPg_distinta_def()))
                                    .concat("-I.xslt.p7m")))
                            .ifPresent(storageObject -> documentiContabiliService.delete(storageObject));

					String nomeFile = file.getName();
					String nomeFileP7m = nomeFile + ".p7m";
					String json = "{"
							+ "\"nodeRefSource\" : \""
							+ cmisFile.getStorageObject().<String>getPropertyValue(
											StoragePropertyNames.ALFCMIS_NODEREF.value()) + "\","
							+ "\"username\" : \"" + firmaOTPBulk.getUserName()
							+ "\"," + "\"password\" : \""
							+ firmaOTPBulk.getPassword() + "\","
							+ "\"otp\" : \"" + firmaOTPBulk.getOtp() + "\""
							+ "}";
                    try {
                        Optional.ofNullable(documentiContabiliService.signDocuments(json, "service/sigla/firma/p7m"))
                            .ifPresent(key -> {
                                File fileSigned = new File(
                                        System.getProperty("tmp.dir.SIGLAWeb")
                                                + "/tmp/", nomeFileP7m);
                                try {
                                    IOUtils.copyLarge(documentiContabiliService.getResource(key), new FileOutputStream(fileSigned));
                                } catch (IOException e) {
                                    throw new StorageException(StorageException.Type.GENERIC, e);
                                }
                            });
                    } catch (StorageException _ex) {
                        throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
                    }
				} else {
					throw new ApplicationException(
							"Errore durante il processo di firma elettronica. Ripetere l'operazione di firma!");
				}
			}
		}
	}

	private String getTitolo() {
		if (firmatarioDistinta.getTitoloFirma().equalsIgnoreCase(
				UtenteFirmaDettaglioBulk.TITOLO_FIRMA_DELEGATO))
			return "";
		return UtenteFirmaDettaglioBulk.titoloKeys.get(firmatarioDistinta
				.getTitoloFirma()) + "\n";
	}

	public String getDocumento() {
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
		if (distinta != null) {
			return String
					.valueOf(distinta.getEsercizio())
					.concat("-")
					.concat(distinta.getCd_unita_organizzativa() == null ? ""
							: distinta.getCd_unita_organizzativa()).concat("-")
					.concat(String.valueOf(distinta.getPg_distinta_def()))
					.concat("-I.xslt");
		}
		return null;
	}

	public void scaricaDistinta(ActionContext actioncontext) throws Exception {
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
		final HttpActionContext httpActionContext = (HttpActionContext) actioncontext;
		Optional.ofNullable(this.getParametriCnr())
				.map(Parametri_cnrBulk::getFl_tesoreria_unica)
				.filter(tesoreriaUnica -> tesoreriaUnica.equals(Boolean.TRUE))
				.ifPresent(tesoreriaUnica -> {
					Optional.ofNullable(documentiContabiliService.getStorageObjectByPath(
							distinta.getStorePath()
							.concat(StoreService.BACKSLASH)
							.concat(String.valueOf(distinta.getEsercizio()))
							.concat("-")
							.concat(distinta.getCd_unita_organizzativa())
							.concat("-")
							.concat(String.valueOf(distinta.getPg_distinta_def())).concat("-I.xslt")
					))
					.ifPresent(storageObject -> {
						httpActionContext.getResponse().setCharacterEncoding("UTF-8");
						httpActionContext.getResponse().setContentType(
								storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value())
						);
						httpActionContext.getResponse().setDateHeader("Expires", 0);
						try {
							IOUtils.copyLarge(documentiContabiliService.getResource(storageObject), httpActionContext.getResponse().getOutputStream());
						} catch (IOException e) {
							throw new StorageException(StorageException.Type.GENERIC, e);
						}
					});
				});
	}

	public void scaricaDistintaFirmata(ActionContext actioncontext)
			throws Exception {
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
		final HttpActionContext httpActionContext = (HttpActionContext) actioncontext;
		if (Optional.ofNullable(this.getParametriCnr())
				.map(Parametri_cnrBulk::getFl_tesoreria_unica)
				.filter(tesoreriaUnica -> tesoreriaUnica.equals(Boolean.TRUE)).isPresent()) {
			String path;
			if (isFlusso()) {
				path = distinta.getStorePath()
						.concat(StoreService.BACKSLASH)
						.concat(String.valueOf(distinta.getEsercizio()))
						.concat("-")
						.concat(distinta.getCd_unita_organizzativa())
						.concat("-")
						.concat(String.valueOf(distinta
								.getPg_distinta_def()))
						.concat("-I.xslt.p7m");
			} else {
				path = distinta.getStorePath()
						.concat(StoreService.BACKSLASH)
						.concat("Distinta n. ")
						.concat(String.valueOf(distinta
								.getPg_distinta_def())).concat(".pdf");
			}
			Optional.ofNullable(documentiContabiliService.getStorageObjectByPath(path))
					.ifPresent(storageObject -> {
						httpActionContext.getResponse().setCharacterEncoding("UTF-8");
						httpActionContext.getResponse().setContentType(
								storageObject.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value())
						);
						httpActionContext.getResponse().setDateHeader("Expires", 0);
						try {
							IOUtils.copyLarge(documentiContabiliService.getResource(storageObject), httpActionContext.getResponse().getOutputStream());
						} catch (IOException e) {
							throw new StorageException(StorageException.Type.GENERIC, e);
						}
					});
		}
	}

	public Boolean isSepa() {
		return sepa;
	}

	public void setSepa(Boolean sepa) {
		this.sepa = sepa;
	}

	public boolean checkPresenteSuDocumentale(Distinta_cassiereBulk distinta, boolean firmata) {
		if (Optional.ofNullable(this.getParametriCnr())
				.map(Parametri_cnrBulk::getFl_tesoreria_unica)
				.filter(tesoreriaUnica -> tesoreriaUnica.equals(Boolean.TRUE)).isPresent()) {
			String path;
			if (firmata) {
				if (isFlusso()) {
					path = distinta.getStorePath()
							.concat(StoreService.BACKSLASH)
							.concat(String.valueOf(distinta
									.getEsercizio()))
							.concat("-")
							.concat(distinta.getCd_unita_organizzativa())
							.concat("-")
							.concat(String.valueOf(distinta.getPg_distinta_def()))
							.concat("-I.xslt.p7m");
				} else {
					path = distinta.getStorePath()
							.concat(StoreService.BACKSLASH)
							.concat("Distinta n. ")
							.concat(String.valueOf(distinta.getPg_distinta_def()))
							.concat(".pdf");
				}
			} else {
				path = distinta.getStorePath()
						.concat(StoreService.BACKSLASH)
						.concat(String.valueOf(distinta
								.getEsercizio()))
						.concat("-")
						.concat(distinta.getCd_unita_organizzativa())
						.concat("-")
						.concat(String.valueOf(distinta.getPg_distinta_def()))
						.concat("-I.xslt");
			}
			return Optional.ofNullable(documentiContabiliService.getStorageObjectByPath(path)).isPresent();
		}
		return true;
	}

	public boolean isDownloadDocumentaleButtonHidden() {
		if (((Distinta_cassiereBulk) getModel()).getDt_invio() == null)
			return true;
		if (((Distinta_cassiereBulk) getModel()) != null
				&& ((Distinta_cassiereBulk) getModel()).getDt_invio() != null
				&& !checkPresenteSuDocumentale(
						((Distinta_cassiereBulk) getModel()), false))
			return true;
		else
			return false;

	}

	public boolean isDownloadFirmatoDocumentaleButtonHidden() {
		if (((Distinta_cassiereBulk) getModel()).getDt_invio() == null)
			return true;
		if (((Distinta_cassiereBulk) getModel()) != null
				&& ((Distinta_cassiereBulk) getModel()).getDt_invio() != null
				&& !checkPresenteSuDocumentale(
						((Distinta_cassiereBulk) getModel()), true))
			return true;
		else
			return false;

	}

	public String getDocumentoFirmato() {
		Distinta_cassiereBulk distinta = (Distinta_cassiereBulk) getModel();
		if (distinta != null) {
			if (!isFlusso())
				return "Distinta "
						.concat(String.valueOf(distinta.getEsercizio()))
						.concat("-")
						.concat(distinta.getCd_unita_organizzativa() == null ? ""
								: distinta.getCd_unita_organizzativa())
						.concat("-")
						.concat(String.valueOf(distinta.getPg_distinta_def()));
			else
				return String
						.valueOf(distinta.getEsercizio())
						.concat("-")
						.concat(distinta.getCd_unita_organizzativa() == null ? ""
								: distinta.getCd_unita_organizzativa())
						.concat("-")
						.concat(String.valueOf(distinta.getPg_distinta_def()))	
						.concat("-I.xslt.p7m");
		}
		return null;
	}

	@Override
	protected void closed(ActionContext context)
			throws BusinessProcessException {
		try {
			distintaCassDet.closed(context);
			distinteCassCollegateDet.closed(context);
		} catch (RemoteException e) {
			throw handleException(e);
		}
		super.closed(context);
	}

    public void setAnnulli(Boolean annulli) {
        this.annulli = annulli;
    }
    public Boolean isAnnulli() {
        return annulli;
    }
}

