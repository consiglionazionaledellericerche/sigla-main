/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.bp;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;

import it.cnr.contab.config00.pdcep.bulk.ContoBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.docamm00.bp.ObbligazioniCRUDController;
import it.cnr.contab.docamm00.bp.VoidableBP;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Voidable;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.doccont00.bp.IDefferedUpdateSaldiBP;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaHome;
import it.cnr.contab.ordmag.ordini.ejb.OrdineAcqComponentSession;
import it.cnr.contab.ordmag.ordini.service.OrdineAcqCMISService;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import org.apache.commons.io.IOUtils;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDOrdineAcqBP extends AllegatiCRUDBP<AllegatoRichiestaBulk, OrdineAcqBulk> implements IDocumentoAmministrativoBP, VoidableBP, IDefferedUpdateSaldiBP  {

	private static final DateFormat PDF_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	private final SimpleDetailCRUDController dettaglioObbligazioneController;
	private it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm = null;
	private boolean carryingThrough = false;
	protected it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager = null;
	private boolean isDeleting = false;

	public boolean isInputReadonly()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		if(ordine == null)
			return super.isInputReadonly();
		return 	super.isInputReadonly() || (ordine.getStato() != null && !ordine.isStatoInserito() && !ordine.isStatoInApprovazione()) ;
	}

	private final SimpleDetailCRUDController righe= new OrdineAcqRigaCRUDController("Righe", OrdineAcqRigaBulk.class, "righeOrdineColl", this){
		public void validateForDelete(ActionContext context, OggettoBulk oggetto) throws ValidationException 
		{
			OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk)oggetto;
			if (riga.getDspObbligazioneScadenzario() != null && riga.getDspObbligazioneScadenzario().getPg_obbligazione() != null){
				throw new ValidationException( "Impossibile cancellare una riga associata ad impegni");
			}
		}

		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			OrdineAcqRigaBulk dettaglio =(OrdineAcqRigaBulk)list.get(i);
			for (int k=0;k<dettaglio.getRigheConsegnaColl().size();k++) {
				dettaglio.removeFromRigheConsegnaColl(k);
			}
			return super.removeDetail(i);
		}

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int index = super.addDetail(oggettobulk);
			OrdineAcqRigaBulk dettaglio =(OrdineAcqRigaBulk)oggettobulk;
			dettaglio.setDspMagazzino(dettaglio.getOrdineAcq().getUnicoMagazzinoAbilitato());
			if (dettaglio.getDspMagazzino() != null){
				dettaglio.setDspLuogoConsegna(dettaglio.getDspMagazzino().getLuogoConsegnaMag());
			}
			return index;
		}

	};

	private final SimpleDetailCRUDController consegne = new SimpleDetailCRUDController("Consegne",OrdineAcqConsegnaBulk.class,"righeConsegnaColl",righe){

		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)oggettobulk;
			OrdineAcqRigaBulk dettaglio =consegna.getOrdineAcqRiga();
			consegna.setTipoConsegna(dettaglio.getTipoConsegnaDefault());
			consegna.setMagazzino(dettaglio.getOrdineAcq().getUnicoMagazzinoAbilitato());
			if (consegna.getMagazzino() != null){
				consegna.setLuogoConsegnaMag(consegna.getMagazzino().getLuogoConsegnaMag());
			}

			int index = super.addDetail(oggettobulk);
			return index;
		}
		
	};

	private final ObbligazioniCRUDController obbligazioniController = new ObbligazioniCRUDController(
			"Obbligazioni",
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk.class,
			"ordineObbligazioniHash", this) {

				@Override
				public boolean isGrowable() {
					return false;
				}
		
		
	};

	public final it.cnr.jada.util.action.SimpleDetailCRUDController getConsegne() {
		return consegne;
	}
	
	private OrdineAcqCMISService ordineAcqCMISService;

	public CRUDOrdineAcqBP() {
		this(OrdineAcqConsegnaBulk.class);
	}
	protected void setTab() {
		setTab("tab","tabOrdineAcq");
		setTab("tabOrdineAcqDettaglio","tabOrdineDettaglio");
	}

	public CRUDOrdineAcqBP(Class dettObbligazioniControllerClass) {
		super("Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", dettObbligazioniControllerClass,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDOrdineAcqBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			@Override
			public boolean isGrowable() {
				return false;
				
//				return super.isGrowable()
//						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
//								.getParentController()).isSearching();
			}

			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};

	}

	/**
	 * CRUDAnagraficaBP constructor comment.
	 * 
	 * @param function
	 *            java.lang.String
	 */
	public CRUDOrdineAcqBP(String function)
			throws BusinessProcessException {
		super(function + "Tr");

		setTab();
		dettaglioObbligazioneController = new SimpleDetailCRUDController(
				"DettaglioObbligazioni", OrdineAcqConsegnaBulk.class,
				"ordineObbligazioniHash", obbligazioniController) {

			public java.util.List getDetails() {

				OrdineAcqBulk ordine = (OrdineAcqBulk) CRUDOrdineAcqBP.this
						.getModel();
				java.util.Vector lista = new java.util.Vector();
				if (ordine != null) {
					java.util.Hashtable h = ordine
							.getOrdineObbligazioniHash();
					if (h != null && getParentModel() != null)
						lista = (java.util.Vector) h.get(getParentModel());
				}
				return lista;
			}

			@Override
			public boolean isGrowable() {
				return false;
				
//				return super.isGrowable()
//						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
//								.getParentController()).isSearching();
			}


			public boolean isShrinkable() {

				return super.isShrinkable()
						&& !((it.cnr.jada.util.action.CRUDBP) getParentController()
								.getParentController()).isSearching();
			}
		};

	}

	
	
	
	public void create(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try { 
			getModel().setToBeCreated();
			setModel(
					context,
					((OrdineAcqComponentSession)createComponentSession()).creaConBulk(
							context.getUserContext(),
							getModel()));
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public final SimpleDetailCRUDController getRighe() {
		return righe;
	}
	/**
	 * Imposta il valore della proprietà 'userConfirm'
	 *
	 * @param newUserConfirm	Il valore da assegnare a 'userConfirm'
	 */
	public void update(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			getModel().setToBeUpdated();
			setModel(
					context,
					((OrdineAcqComponentSession)createComponentSession()).modificaConBulk(
							context.getUserContext(),
							getModel()));
			archiviaAllegati(context);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	@Override
	protected String getStorePath(OrdineAcqBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
		return ordineAcqCMISService.getStorePath(allegatoParentBulk);
	}

	@Override
	protected Class<AllegatoRichiestaBulk> getAllegatoClass() {
		return AllegatoRichiestaBulk.class;
	}
	public OrdineAcqCMISService getOrdineAcqCMISService() {
		return ordineAcqCMISService;
	}
	public void setRichiesteCMISService(OrdineAcqCMISService ordineAcqCMISService) {
		this.ordineAcqCMISService = ordineAcqCMISService;
	}
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		ordineAcqCMISService = SpringUtil.getBean("ordineAcqCMISService",
				OrdineAcqCMISService.class);	
	}
	@Override
	public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk)
			throws BusinessProcessException {

		OrdineAcqBulk allegatoParentBulk = (OrdineAcqBulk)oggettobulk;

		return oggettobulk;	
	}
	@Override
	protected void completeAllegato(AllegatoRichiestaBulk allegato, StorageObject storageObject) throws ApplicationException {
		Optional.ofNullable(storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
				.filter(strings -> !strings.isEmpty())
				.ifPresent(strings -> {
					allegato.setAspectName(strings.stream()
							.filter(s -> AllegatoRichiestaBulk.aspectNamesKeys.get(s) != null)
							.findAny().orElse(RichiesteCMISService.ASPECT_ALLEGATI_RICHIESTA_ORDINI));
				});
		super.completeAllegato(allegato, storageObject);
	}

	@Override
	public String getAllegatiFormName() {
		super.getAllegatiFormName();
		return "allegatiOrdine";
	}
	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk)getCrudArchivioAllegati().getModel();
		StorageObject storageObject = ordineAcqCMISService.getStorageObjectBykey(allegato.getStorageKey());
		InputStream is = ordineAcqCMISService.getResource(storageObject.getKey());
		((HttpActionContext)actioncontext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
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
//	public String getNomeAllegatoDettaglio() throws ApplicationException{
//		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
//		if (dettaglio!= null){
//			return dettaglio.getNome();
//		}
//		return "";
//	}
//	public void scaricaDocumentoDettaglioCollegato(ActionContext actioncontext) throws Exception {
//		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
//		if (dettaglio!= null){
//			Document document = dettaglio.getDocument(richiesteCMISService);
//			if (document != null){
//				InputStream is = richiesteCMISService.getResource(document);
//				((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(document.getContentStreamLength()).intValue());
//				((HttpActionContext)actioncontext).getResponse().setContentType(document.getContentStreamMimeType());
//				OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
//				((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
//				byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
//				int buflength;
//				while ((buflength = is.read(buffer)) > 0) {
//					os.write(buffer,0,buflength);
//				}
//				is.close();
//				os.flush();
//			} else {
//				throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
//			}
//		} else {
//			throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
//		}
//	}
	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		return true;
	}
	protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
		return true;
	}
//	public SimpleDetailCRUDController getDettaglioAllegatiController() {
//		return dettaglioAllegatiController;
//	}
	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato){
		return true;
	}
	@Override
	protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
		OrdineAcqBulk ordine = (OrdineAcqBulk)allegatoParentBulk;
		super.gestioneCancellazioneAllegati(allegatoParentBulk);
	}
	public void gestionePostSalvataggio(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			OrdineAcqBulk ordine = (OrdineAcqBulk)getModel(); 
//			((OrdineAcqComponentSession)createComponentSession()).gestioneStampaOrdine(context.getUserContext(), ordine);
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public void stampaRichiesta(ActionContext actioncontext) throws Exception {
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		InputStream is = ordineAcqCMISService.getStreamOrdine(ordine);
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
	public boolean isStampaOrdineButtonHidden() {

		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null);
	}

	private String preparaFileNamePerStampa(String reportName) {
		String fileName = reportName;
		fileName = fileName.replace('/', '_');
		fileName = fileName.replace('\\', '_');
		if (fileName.startsWith("_"))
			fileName = fileName.substring(1);
		if (fileName.endsWith(".jasper"))
			fileName = fileName.substring(0, fileName.length() - 7);
		fileName = fileName + ".pdf";
		return fileName;
	}
	private String getOutputFileNameOrdine(String reportName, OrdineAcqBulk ordine) {
		String fileName = preparaFileNamePerStampa(reportName);
		fileName = PDF_DATE_FORMAT.format(new java.util.Date()) + '_' + ordine.recuperoIdOrdineAsString() + '_' + fileName;
		return fileName;
	}
	public void stampaOrdine(ActionContext actioncontext) throws Exception {
		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		UserContext userContext = actioncontext.getUserContext();
		File f = stampaOrdine(userContext,ordine);

		((HttpActionContext)actioncontext).getResponse().setContentType("application/pdf");
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		IOUtils.copy(new FileInputStream(f), os);
//		byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
//		int buflength;
//		while ((buflength = fo.read(buffer)) > 0) {
//			os.write(buffer,0,buflength);
//		}
//		is.close();
		os.flush();

	}
	public File stampaOrdine(
			UserContext userContext,
			OrdineAcqBulk ordine) throws ComponentException {
		try {
			String jasperOrdineName = "ordini_acq.jasper";
			String nomeFileOrdineOut = getOutputFileNameOrdine(jasperOrdineName, ordine);
			File output = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", File.separator + nomeFileOrdineOut);
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setFlEmail(false);
			print.setReport("/ordmag/ordini/iss/" + jasperOrdineName);
			print.setNomeFile(nomeFileOrdineOut);
			print.setUtcr(userContext.getUser());
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.addParam("CD_CDS", ordine.getCdCds(), String.class);
			print.addParam("CD_UNITA_OPERATIVA", ordine.getCdUnitaOperativa(), String.class);
			print.addParam("ESERCIZIO", ordine.getEsercizio(), Integer.class);
			print.addParam("CD_NUMERATORE", ordine.getCdNumeratore(), String.class);
			print.addParam("NUMERO", ordine.getNumero(), Integer.class);
			Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);

			FileOutputStream f = new FileOutputStream(output);
			f.write(report.getBytes());
			return output;
		} catch (IOException e) {
			throw new GenerazioneReportException("Generazione Stampa non riuscita", e);
		}
	}
	public boolean isSalvaDefinitivoButtonHidden() {

		OrdineAcqBulk ordine = (OrdineAcqBulk)getModel();
		return (ordine == null || ordine.getNumero() == null || !ordine.isStatoAllaFirma());
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");

		return toolbar;
	}
	public boolean areBottoniObbligazioneAbilitati()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return 	ordine != null && 
				!isSearching() && 
				!isViewing() ;
	}
	public boolean isBottoneObbligazioneAggiornaManualeAbilitato()
	{
		OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();

		return(	ordine != null && !isSearching() && 
				!isViewing() );
	}
	public IDefferUpdateSaldi getDefferedUpdateSaldiBulk() {

		if (isDeleting() && getParent() != null)
			return getDefferedUpdateSaldiParentBP()
					.getDefferedUpdateSaldiBulk();
		return (IDefferUpdateSaldi) getDocumentoAmministrativoCorrente();
	}

	public IDefferedUpdateSaldiBP getDefferedUpdateSaldiParentBP() {

		if (isDeleting() && getParent() != null)
			return ((IDefferedUpdateSaldiBP) getParent())
					.getDefferedUpdateSaldiParentBP();
		return this;
	}

	public it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk getDeleteManager() {

		if (deleteManager == null)
			deleteManager = new it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk();
		else
			deleteManager.reset();
		return deleteManager;
	}

	@Override
	public boolean isModelVoided() {
		return !isSearching() && getModel() != null
				&& ((Voidable) getModel()).isAnnullato();
	}
	public it.cnr.jada.util.RemoteIterator findObbligazioni(
			it.cnr.jada.UserContext userContext,
			it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk filtro)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			return ((OrdineAcqComponentSession)createComponentSession()).cercaObbligazioni(userContext, filtro);

		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}

	public it.cnr.jada.util.RemoteIterator findObbligazioniAttributes(
			it.cnr.jada.action.ActionContext actionContext,
			it.cnr.jada.persistency.sql.CompoundFindClause clauses,
			it.cnr.jada.bulk.OggettoBulk bulk,
			it.cnr.jada.bulk.OggettoBulk context, java.lang.String property)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(
					actionContext, ((OrdineAcqComponentSession)createComponentSession()).cerca(actionContext.getUserContext(),
							clauses, bulk, context, property));
		} catch (it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}
	@Override
	public Accertamento_scadenzarioBulk getAccertamento_scadenziario_corrente() {
		return null;
	}
	@Override
	public IDocumentoAmministrativoBulk getBringBackDocAmm() {
		return getDocumentoAmministrativoCorrente();
	}

	@Override
	public IDocumentoAmministrativoBulk getDocumentoAmministrativoCorrente() {
		return (IDocumentoAmministrativoBulk) getModel();
	}

	@Override
	public Obbligazione_scadenzarioBulk getObbligazione_scadenziario_corrente() {
		if (getObbligazioniController() == null)
			return null;
		return (Obbligazione_scadenzarioBulk) getObbligazioniController()
				.getModel();
	}
	@Override
	public boolean isAutoGenerated() {
		return false;
	}
	@Override
	public boolean isDeleting() {
		return isDeleting;
	}
	@Override
	public boolean isManualModify() {
		return true;
	}
	@Override
	public void setIsDeleting(boolean newIsDeleting) {
		isDeleting = newIsDeleting;
	}
	@Override
	public void validaObbligazionePerDocAmm(ActionContext actionContext, OggettoBulk bulk)
			throws BusinessProcessException {
		return;
	}
	public ObbligazioniCRUDController getObbligazioniController() {
		return obbligazioniController;
	}
	public void setDeleteManager(it.cnr.contab.docamm00.docs.bulk.Risultato_eliminazioneVBulk deleteManager) {
		this.deleteManager = deleteManager;
	}
	public it.cnr.contab.doccont00.core.bulk.OptionRequestParameter getUserConfirm() {
		return userConfirm;
	}
	public void setUserConfirm(it.cnr.contab.doccont00.core.bulk.OptionRequestParameter userConfirm) {
		this.userConfirm = userConfirm;
	}
	public boolean isCarryingThrough() {
		return carryingThrough;
	}
	public void setCarryingThrough(boolean carryingThrough) {
		this.carryingThrough = carryingThrough;
	}
	public SimpleDetailCRUDController getDettaglioObbligazioneController() {
		return dettaglioObbligazioneController;
	}
	public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
	{
		int crudStatus = getModel().getCrudStatus();
		try 
		{
			OrdineAcqBulk ordine = (OrdineAcqBulk) getModel();
			if ( !ordine.isStatoInserito()){
				throw new ApplicationException( "Non è possibile cancellare un ordine in stato diverso da inserito");
			} else {
				ordine = ((OrdineAcqComponentSession)createComponentSession()).cancellaOrdine(context.getUserContext(),(OrdineAcqBulk)getModel());
				setModel( context, ordine );
				setMessage("Cancellazione effettuata");			
			}
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
		}
	}

	public ContoBulk recuperoContoDefault(
			ActionContext context,
			Categoria_gruppo_inventBulk categoria_gruppo_inventBulk)
			throws it.cnr.jada.action.BusinessProcessException {

		try {

			return ((OrdineAcqComponentSession)createComponentSession()).recuperoContoDefault(context.getUserContext(), categoria_gruppo_inventBulk);

		} catch (it.cnr.jada.comp.ComponentException| PersistencyException e) {
			throw handleException(e);
		} catch (java.rmi.RemoteException e) {
			throw handleException(e);
		}
	}
}
