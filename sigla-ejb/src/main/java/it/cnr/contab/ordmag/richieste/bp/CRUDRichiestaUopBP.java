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

package it.cnr.contab.ordmag.richieste.bp;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaDettaglioBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.ejb.RichiestaUopComponentSession;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.upload.UploadedFile;

/**
 * Gestisce le catene di elementi correlate con il documento in uso.
 */
public class CRUDRichiestaUopBP extends AllegatiCRUDBP<AllegatoRichiestaBulk, RichiestaUopBulk>  {

	public boolean isInputReadonly() 
	{
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		if(richiesta == null)
			return super.isInputReadonly();
		return 	super.isInputReadonly() || (richiesta.getStato() != null && !richiesta.isInserita());
	}

	private final SimpleDetailCRUDController righe= new RichiestaUopRigaCRUDController("Righe", RichiestaUopRigaBulk.class, "righeRichiestaColl", this){
		@Override
		public OggettoBulk removeDetail(int i) {
			List list = getDetails();
			RichiestaUopRigaBulk dettaglio =(RichiestaUopRigaBulk)list.get(i);
			BulkList<AllegatoRichiestaDettaglioBulk> listaDettagliAllegati = dettaglio.getDettaglioAllegati();
			if (listaDettagliAllegati != null && !listaDettagliAllegati.isEmpty()){
				int k;
				for ( k = 0; k < listaDettagliAllegati.size(); k++ ){
					AllegatoRichiestaDettaglioBulk all = listaDettagliAllegati.get(k);
					all.setToBeDeleted();
				}
			}
			return super.removeDetail(i);
		}

	};

	private final SimpleDetailCRUDController dettaglioAllegatiController = new SimpleDetailCRUDController("AllegatiDettaglio", AllegatoRichiestaDettaglioBulk.class,"dettaglioAllegati",righe)
	{
		@Override
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk)oggettobulk;
			UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.Righe.AllegatiDettaglio.file");
			if (!(file == null || file.getName().equals(""))) {
				allegato.setFile(file.getFile());
				allegato.setContentType(file.getContentType());
				allegato.setNome(allegato.parseFilename(file.getName()));
				allegato.setAspectName(RichiesteCMISService.ASPECT_RICHIESTA_ORDINI_DETTAGLIO);
				allegato.setToBeUpdated();
				getParentController().setDirty(true);
			}
			oggettobulk.validate();		
			super.validate(actioncontext, oggettobulk);
		}
		@Override
		public OggettoBulk removeDetail(int i) {
			if (!getModel().isNew()){	
				List list = getDetails();
				AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)list.get(i);
				if (isPossibileCancellazioneDettaglioAllegato(all)) {
					return super.removeDetail(i);
				} else {
					return null;
				}
			}
			return super.removeDetail(i);
		}
		@Override
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			int add = super.addDetail(oggettobulk);
			AllegatoRichiestaDettaglioBulk all =(AllegatoRichiestaDettaglioBulk)oggettobulk;
			all.setIsDetailAdded(true);
			return add;
		}
	};	

	private RichiesteCMISService richiesteCMISService;
	public CRUDRichiestaUopBP() {
		super();
		setTab();
	}
	protected void setTab() {
		setTab("tab","tabRichiestaUop");
		setTab("tabRichiestaUopDettaglio","tabRichiestaDettaglio");
	}
	public CRUDRichiestaUopBP(String function) throws BusinessProcessException{
		super(function+"Tr");
		setTab();
	}

	public void create(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {
		try { 
			getModel().setToBeCreated();
			setModel(
					context,
					((RichiestaUopComponentSession)createComponentSession()).creaConBulk(
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
					((RichiestaUopComponentSession)createComponentSession()).modificaConBulk(
							context.getUserContext(),
							getModel()));
			archiviaAllegati(context);
			archiviaAllegatiDettaglio();
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	private void archiviaAllegatiDettaglio() throws ApplicationException, BusinessProcessException {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		for (Object oggetto : richiesta.getRigheRichiestaColl()) {
			RichiestaUopRigaBulk dettaglio = (RichiestaUopRigaBulk)oggetto;
			for (AllegatoRichiestaDettaglioBulk allegato : dettaglio.getDettaglioAllegati()) {
				if (allegato.isToBeCreated()){
					try {
						storeService.storeSimpleDocument(allegato,
								new FileInputStream(allegato.getFile()),
								allegato.getContentType(),
								allegato.getNome(),
                                richiesteCMISService.getStorePathDettaglio(dettaglio));
						allegato.setCrudStatus(OggettoBulk.NORMAL);
					} catch (FileNotFoundException e) {
						throw handleException(e);
					} catch (StorageException e) {
						if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
							throw new ApplicationException("File ["+allegato.getNome()+"] gia' presente. Inserimento non possibile!");
						throw handleException(e);
					}
				}else if (allegato.isToBeUpdated()) {
					if (isPossibileModifica(allegato)) {
						try {
							if (allegato.getFile() != null) {
								storeService.updateStream(allegato.getStorageKey(),
										new FileInputStream(allegato.getFile()),
										allegato.getContentType());
							}
							storeService.updateProperties(allegato, storeService.getStorageObjectBykey(allegato.getStorageKey()));
							allegato.setCrudStatus(OggettoBulk.NORMAL);
						} catch (FileNotFoundException e) {
							throw handleException(e);
						}
					}
				}
			}
			for (Iterator<AllegatoRichiestaDettaglioBulk> iterator = dettaglio.getDettaglioAllegati().deleteIterator(); iterator.hasNext();) {
				AllegatoRichiestaDettaglioBulk allegato = iterator.next();
				if (allegato.isToBeDeleted()){
					storeService.delete(allegato.getStorageKey());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				}
			}
		}

		for (Iterator<RichiestaUopRigaBulk> iterator = richiesta.getRigheRichiestaColl().deleteIterator(); iterator.hasNext();) {
			RichiestaUopRigaBulk dettaglio = iterator.next();
			for (Iterator<AllegatoRichiestaDettaglioBulk> iteratorAll = dettaglio.getDettaglioAllegati().iterator(); iteratorAll.hasNext();) {
				AllegatoRichiestaDettaglioBulk allegato = iteratorAll.next();
				if (allegato.isToBeDeleted()){
					storeService.delete(allegato.getStorageKey());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				}
			}
		}
	}

	@Override
	protected String getStorePath(RichiestaUopBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
		return richiesteCMISService.getStorePath(allegatoParentBulk);
	}

	@Override
	protected Class<AllegatoRichiestaBulk> getAllegatoClass() {
		return AllegatoRichiestaBulk.class;
	}
	public RichiesteCMISService getRichiesteCMISService() {
		return richiesteCMISService;
	}
	public void setRichiesteCMISService(RichiesteCMISService richiesteCMISService) {
		this.richiesteCMISService = richiesteCMISService;
	}
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		super.initialize(actioncontext);
		richiesteCMISService = SpringUtil.getBean("richiesteCMISService",
				RichiesteCMISService.class);	
	}
	@Override
	public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		RichiestaUopBulk allegatoParentBulk = (RichiestaUopBulk)oggettobulk;
		if (allegatoParentBulk.getCdUnitaOperativa() != null){
	        richiesteCMISService.getFilesRichiesta(allegatoParentBulk).stream()
            .filter(storageObject -> !richiesteCMISService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
            .filter(storageObject -> !richiesteCMISService.hasAspect(storageObject, RichiesteCMISService.ASPECT_STAMPA_RICHIESTA_ORDINI))
            .filter(storageObject -> !excludeChild(storageObject))
            .forEach(storageObject -> {
                try {
                    richiesteCMISService.recuperoAllegatiDettaglioRichiesta(allegatoParentBulk, storageObject);
                    AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk) Introspector.newInstance(getAllegatoClass(), storageObject);
                    allegato.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                    allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                    allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                    allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                    completeAllegato(allegato, storageObject);
                    allegato.setCrudStatus(OggettoBulk.NORMAL);
                    allegatoParentBulk.addToArchivioAllegati(allegato);
                } catch (NoSuchMethodException|IllegalAccessException|InstantiationException|InvocationTargetException|ApplicationException e) {
                    throw new RuntimeException(e);
                }
            });
		}
		return allegatoParentBulk;
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
		return "allegatiRichiesta";
	}

	public void scaricaAllegato(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoRichiestaBulk allegato = (AllegatoRichiestaBulk)getCrudArchivioAllegati().getModel();
        StorageObject storageObject = richiesteCMISService.getStorageObjectBykey(allegato.getStorageKey());
        InputStream is = richiesteCMISService.getResource(storageObject.getKey());
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

	public String getNomeAllegatoDettaglio() throws ApplicationException{
		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
		if (dettaglio!= null){
			return dettaglio.getNome();
		}
		return "";
	}

	public void scaricaDocumentoDettaglioCollegato(ActionContext actioncontext) throws Exception {
		AllegatoRichiestaDettaglioBulk dettaglio = (AllegatoRichiestaDettaglioBulk)getDettaglioAllegatiController().getModel();
		if (dettaglio!= null){
            StorageObject storageObject = richiesteCMISService.getStorageObjectBykey(dettaglio.getStorageKey());
            InputStream is = richiesteCMISService.getResource(storageObject.getKey());
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
		} else {
			throw new it.cnr.jada.action.MessageToUser( "Documenti non presenti sul documentale per la riga selezionata" );
		}
	}

	@Override
	protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
		return true;
	}
	protected Boolean isPossibileCancellazioneDettaglioAllegato(AllegatoGenericoBulk allegato) {
		return true;
	}
	public SimpleDetailCRUDController getDettaglioAllegatiController() {
		return dettaglioAllegatiController;
	}
	@Override
	protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato){
		return true;
	}
	@Override
	protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)allegatoParentBulk;
		super.gestioneCancellazioneAllegati(allegatoParentBulk);
	}
	public void gestionePostSalvataggio(it.cnr.jada.action.ActionContext context)
			throws	it.cnr.jada.action.BusinessProcessException {

		try {
			RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
            gestioneStampaRichiesta(context.getUserContext(), richiesta);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

    public void gestioneStampaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws BusinessProcessException {
        File file = lanciaStampaRichiesta(userContext, richiesta);
        archiviaFileCMIS(userContext, richiesta, file);
    }

    private void archiviaFileCMIS(UserContext userContext, RichiestaUopBulk richiesta, File file) throws BusinessProcessException{
        try {
            String path = richiesteCMISService.getStorePath(richiesta);
            AllegatoRichiestaBulk allegato = new AllegatoRichiestaBulk();
            allegato.setFile(file);
            allegato.setTitolo("Stampa Richiesta");
            allegato.setNome("Stampa Richiesta");
            allegato.setDescrizione("Stampa Richiesta");
            allegato.setContentType(MimeTypes.PDF.mimetype());
            allegato.setAspectName(RichiesteCMISService.ASPECT_STAMPA_RICHIESTA_ORDINI);
            FileInputStream is = new FileInputStream(allegato.getFile());
            richiesteCMISService.restoreSimpleDocument(
                    allegato,
                    new FileInputStream(allegato.getFile()),
                    allegato.getContentType(),
                    allegato.getNome(),
                    path,
                    false
            );
        } catch (Exception e){
            throw handleException(e);
        }
    }

    public File lanciaStampaRichiesta(
            UserContext userContext,
            RichiestaUopBulk richiesta) throws BusinessProcessException {
        try {
            String nomeProgrammaStampa = "richiesta_ordine_uop.jasper";
            String nomeFileStampaFattura = getOutputFileNameRichiesta(nomeProgrammaStampa, richiesta);
            File output = new File(System.getProperty("tmp.dir.SIGLAWeb")+"/tmp/", File.separator + nomeFileStampaFattura);
            Print_spoolerBulk print = new Print_spoolerBulk();
            print.setFlEmail(false);
            print.setReport("/ordmag/richiesta/"+ nomeProgrammaStampa);
            print.setNomeFile(nomeFileStampaFattura);
            print.setUtcr(userContext.getUser());
            print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
            print.addParam("esercizio",richiesta.getEsercizio(), Integer.class);
            print.addParam("cds",richiesta.getCdCds(), String.class);
            print.addParam("cd_unita_operativa",richiesta.getCdUnitaOperativa(), String.class);
            print.addParam("cd_numeratore",richiesta.getCdNumeratore(), String.class);
            print.addParam("numero",new Long(richiesta.getNumero()), Long.class);
            Report report = SpringUtil.getBean("printService",PrintService.class).executeReport(userContext,print);

            FileOutputStream f = new FileOutputStream(output);
            f.write(report.getBytes());
            return output;
        } catch (IOException|ComponentException e) {
            throw handleException(e);
        }
    }

    private String preparaFileNamePerStampa(String reportName) {
        String fileName = reportName;
        fileName = fileName.replace('/', '_');
        fileName = fileName.replace('\\', '_');
        if(fileName.startsWith("_"))
            fileName = fileName.substring(1);
        if(fileName.endsWith(".jasper"))
            fileName = fileName.substring(0, fileName.length() - 7);
        fileName = fileName + ".pdf";
        return fileName;
    }

    private String getOutputFileNameRichiesta(String reportName, RichiestaUopBulk richiesta) {
        String fileName = preparaFileNamePerStampa(reportName);
        LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.BASIC_ISO_DATE);
        fileName = LocalDate.now(ZoneId.systemDefault()).format(DateTimeFormatter.BASIC_ISO_DATE) + '_' + richiesta.recuperoIdRichiestaAsString() + '_' + fileName;
        return fileName;
    }

    public void stampaRichiesta(ActionContext actioncontext) throws Exception {
		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		InputStream is = richiesteCMISService.getStreamRichiesta(richiesta);
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
	public boolean isStampaRichiestaButtonHidden() {

		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null);
	}

	public boolean isSalvaDefinitivoButtonHidden() {

		RichiestaUopBulk richiesta = (RichiestaUopBulk)getModel();
		return (richiesta == null || richiesta.getNumero() == null || !richiesta.isInserita());
	}

	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[8];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.salvaDefinitivo");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.stampa");

		return toolbar;
	}
}
