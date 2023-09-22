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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.AutoFatturaComponentSession;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileAutofattura;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CondizioneComplessaBulk;
import it.cnr.jada.util.action.SearchProvider;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.jsp.Button;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.util.ByteArrayDataSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP extends SelezionatoreListaBP implements SearchProvider {
    private transient final static Logger logger = LoggerFactory.getLogger(CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP.class);
    private static final long serialVersionUID = 1L;
    private DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService;
    private boolean utenteNonAbilitatoFirma;

    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP() {
        super();
    }


    /**
     * DocumentiAmministrativiProtocollabiliBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP(String function) {
        super(function);
    }

    public boolean isUtenteNonAbilitatoFirma() {
        return utenteNonAbilitatoFirma;
    }

    public void setUtenteNonAbilitatoFirma(boolean utenteNonAbilitatoFirma) {
        this.utenteNonAbilitatoFirma = utenteNonAbilitatoFirma;
    }

    @Override
    public void setMultiSelection(boolean flag) {
        super.setMultiSelection(flag);
    }

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        try {
            setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(getClass().getClassLoader().loadClass(config.getInitParameter("bulkClassName"))));
            setMultiSelection(true);
            OggettoBulk model = (OggettoBulk) getBulkInfo().getBulkClass().newInstance();
            UserContext userContext = context.getUserContext();
            try {
                setUtenteNonAbilitatoFirma(isUtenteNonAbilitatoFirma(userContext));
            } catch (ComponentException e) {
                logger.error("Utente Non Abilitato Firma", e);
            }
            if (isUtenteNonAbilitatoFirma()) {
                ((VDocammElettroniciAttiviBulk) model).setStatoFattElett(VDocammElettroniciAttiviBulk.DA_PREDISPORRE_ALLA_FIRMA);
            } else {
                ((VDocammElettroniciAttiviBulk) model).setStatoFattElett(VDocammElettroniciAttiviBulk.DA_FIRMARE);
            }
            setModel(context, model);
            super.init(config, context);
            openIterator(context);
            documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
        } catch (InstantiationException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (ClassNotFoundException e) {
            throw handleException(e);
        }
    }

    public void openIterator(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            setIterator(actioncontext, search(
                    actioncontext,
                    Optional.ofNullable(getCondizioneCorrente())
                            .map(CondizioneComplessaBulk::creaFindClause)
                            .filter(CompoundFindClause.class::isInstance)
                            .map(CompoundFindClause.class::cast)
                            .orElseGet(() -> new CompoundFindClause()),
                    getModel())
            );
        } catch (RemoteException e) {
            throw new BusinessProcessException(e);
        }
    }

    public RemoteIterator search(
            ActionContext actioncontext,
            CompoundFindClause compoundfindclause,
            OggettoBulk oggettobulk)
            throws BusinessProcessException {
        VDocammElettroniciAttiviBulk docamm = (VDocammElettroniciAttiviBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    docamm,
                    "selectByClauseForFatturazioneElettronica");
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }

    protected CRUDComponentSession getComponentSession() {
        return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
    }

    public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
        String tipoDocamm = ((HttpActionContext) actioncontext).getParameter("tipoDocamm");
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String cdUo = ((HttpActionContext) actioncontext).getParameter("cdUo");
        Long pgFattura = Long.valueOf(((HttpActionContext) actioncontext).getParameter("pgFattura"));

        StorageObject storageObject=null;
        if (Numerazione_doc_ammBulk.TIPO_FATTURA_ATTIVA.equals(tipoDocamm))
            storageObject = documentiCollegatiDocAmmService.recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
        else if (Numerazione_doc_ammBulk.TIPO_AUTOFATTURA.equals(tipoDocamm))
            storageObject = documentiCollegatiDocAmmService.recuperoFolderAutofattura(esercizio, cds, cdUo, pgFattura);

        InputStream is = null;
        if (storageObject == null) {
            is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
        } else {
            is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
            if (is == null) {
                is = getStreamNewDocument(actioncontext, esercizio, cds, cdUo, pgFattura);
            }
        }
        if (is != null) {
            ((HttpActionContext) actioncontext).getResponse().setContentType("application/pdf");
            OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
            ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
            byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
            int buflength;
            while ((buflength = is.read(buffer)) > 0) {
                os.write(buffer, 0, buflength);
            }
            is.close();
            os.flush();
        }
    }

    private InputStream getStreamNewDocument(ActionContext actioncontext,
                                             Integer esercizio, String cds, String cdUo, Long pgFattura)
            throws Exception {
        InputStream is;
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);
        UserContext userContext = actioncontext.getUserContext();
        Fattura_attivaBulk fattura = componentFatturaAttiva.ricercaFatturaByKey(userContext, esercizio.longValue(), cds, cdUo, pgFattura);
        SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fattura);
        is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
        return is;
    }

    public boolean isUtenteNonAbilitatoFirma(UserContext userContext) throws ApplicationException {
        try {
            return !UtenteBulk.isAbilitatoFirmaFatturazioneElettronica(userContext);
        } catch (ComponentException | RemoteException e) {
            throw new ApplicationException(e);
        }
    }

    public it.cnr.jada.util.jsp.Button[] createToolbar() {
        Button[] baseToolbar = super.createToolbar();
        Button[] toolbar = new Button[baseToolbar.length + 2];
        int i = 0;
        for (Button button : baseToolbar) {
            toolbar[i++] = button;
        }
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.predisponi");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.FirmaInvia");
        return toolbar;
    }

    public DocAmmFatturazioneElettronicaComponentSession createComponentSession()
            throws BusinessProcessException {
        return (DocAmmFatturazioneElettronicaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession",
                DocAmmFatturazioneElettronicaComponentSession.class);
    }

    public void predisponiPerLaFirma(ActionContext context) throws BusinessProcessException {
        try {
            logger.info("Inizio Predisposizione per la firma");
            UserContext userContext = context.getUserContext();
            List<OggettoBulk> lista = getSelectedElements(context);
            if (lista == null || lista.isEmpty())
                throw new ApplicationException("Selezionare almeno un elemento!");
            int contaScartati = 0;
            int contaAggiornati = 0;
            for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
                OggettoBulk docAmm = i.next();
                if (docAmm instanceof VDocammElettroniciAttiviBulk &&
                        VDocammElettroniciAttiviBulk.DA_PREDISPORRE_ALLA_FIRMA.equals(((VDocammElettroniciAttiviBulk)docAmm).getStatoInvioSdi())) {
                    protocollaECreaFileXml(userContext, (VDocammElettroniciAttiviBulk) docAmm);
                    contaAggiornati++;
                } else
                    contaScartati++;
            }
            setFocusedElement(context, null);
            refresh(context);
            this.setMessage("Sono stati predisposti per la firma "+contaAggiornati+" documenti."+
                    (contaScartati>0?" Ne sono stati scartati "+contaScartati+" che non risultano da predisporre per la firma.":""));
        } catch (ApplicationException e) {
            throw handleException(e);
        } catch (ComponentException e) {
            throw handleException(e);
        } catch (IOException e) {
            throw handleException(e);
        } catch (PersistencyException e) {
            throw handleException(e);
        }
    }

    public IDocumentoAmministrativoElettronicoBulk protocollaECreaFileXml(UserContext userContext, VDocammElettroniciAttiviBulk vDocamm)
            throws BusinessProcessException, ComponentException, RemoteException, PersistencyException {
        logger.info("Processo il documento elettronico {}/{}/{}", vDocamm.getTipoDocamm(), vDocamm.getEsercizio(), vDocamm.getPg_docamm());

        if (VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI.equals(vDocamm.getStatoInvioSdi()))
            throw new ApplicationException("Operazione non possibile! Il documento elettronico "+ vDocamm.getTipoDocamm()+"/"+vDocamm.getEsercizio()+"/"+vDocamm.getPg_docamm()+
                    " risulta firmato.");

        DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();

        IDocumentoAmministrativoElettronicoBulk docammElettronico = component.castDocumentoElettronico(userContext, vDocamm);

        if (docammElettronico instanceof AutofatturaBulk && docammElettronico.getProgrUnivocoAnno()==null)
            docammElettronico = Utility.createAutoFatturaComponentSession().impostaDatiPerFatturazioneElettronica(userContext, (AutofatturaBulk)docammElettronico);

        // Questo metodo va invocato perchè fa tutti i controlli prima che la fattura venga protocollata
        component.preparaFattura(userContext, docammElettronico);

        if (docammElettronico.getProtocollo_iva() == null) {
            if (docammElettronico instanceof Fattura_attivaBulk) {
                Fattura_attivaBulk fatturaAttivaProtocollata = protocollazione(userContext, (Fattura_attivaBulk) docammElettronico);
                docammElettronico = fatturaAttivaProtocollata;
                logger.info("Creato protocollazione {}/{}/{}", docammElettronico.getTipoDocumentoElettronico(), docammElettronico.getEsercizio(), docammElettronico.getPg_docamm());
            } else {
                throw new ApplicationException("Protocollo Iva non presente sul documento elettronico "+ docammElettronico.getTipoDocumentoElettronico()+"/"+docammElettronico.getEsercizio()+"/"+docammElettronico.getPg_docamm());
            }
        }

        File file = creaFileXml(userContext, docammElettronico);
        logger.info("Creato file XML {}/{}/{}", docammElettronico.getTipoDocumentoElettronico(), docammElettronico.getEsercizio(), docammElettronico.getPg_docamm());

        List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
        List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
        try {
            StorageFile storageFile;

            if (docammElettronico instanceof AutofatturaBulk)
                storageFile = new StorageFileAutofattura(file, (AutofatturaBulk)docammElettronico,
                    "application/xml", "FAXA" + ((AutofatturaBulk)docammElettronico).constructCMISNomeFile() + ".xml");
            else if (docammElettronico instanceof Fattura_attivaBulk)
                storageFile = new StorageFileFatturaAttiva(file, (Fattura_attivaBulk)docammElettronico,
                        "application/xml", "FAXA" + ((Fattura_attivaBulk)docammElettronico).constructCMISNomeFile() + ".xml");
            else
                storageFile = null;

            if (storageFile != null) {
                //E' previsto solo l'inserimento ma non l'aggiornamento
                String path = storageFile.getStorageParentPath();
                try {
                    Optional.ofNullable(documentiCollegatiDocAmmService.restoreSimpleDocument(
                            storageFile,
                            storageFile.getInputStream(),
                            storageFile.getContentType(),
                            storageFile.getFileName(),
                            path,
                            false
                    )).ifPresent(storageObject -> {
                        List<String> aspects = new ArrayList<>();
                        aspects.addAll(storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()));
                        aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA.value());
                        Map<String, Object> metadataProperties = new HashMap<>();
                        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspects);
                        documentiCollegatiDocAmmService.updateProperties(
                                metadataProperties,
                                storageObject);
                        storageFile.setStorageObject(storageObject);
                        storageFileCreate.add(storageFile);
                    });
                    logger.info("Salvato file XML sul Documentale");
                } catch (StorageException _ex) {
                    if (_ex.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                        throw new ApplicationException("CMIS - File [" + storageFile.getFileName() + "] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
                    throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + _ex.getMessage() + ")");
                }
                docammElettronico = component.aggiornaDocammElettronicoPredispostoAllaFirma(userContext, docammElettronico);
            }
        } catch (Exception e) {
			/*
			    Codice per riallineare il documentale allo stato precedente rispetto alle modifiche
			 */
            for (StorageFile storageFile : storageFileAnnullati) {
                String cmisFileName = storageFile.getFileName();
                String cmisFileEstensione = cmisFileName.substring(cmisFileName.lastIndexOf(".") + 1);
                String stringToDelete = cmisFileName.substring(cmisFileName.indexOf("-ANNULLATO"));
                storageFile.setFileName(cmisFileName.replace(stringToDelete, "." + cmisFileEstensione));
                documentiCollegatiDocAmmService.updateProperties(storageFile, storageFile.getStorageObject());
            }
            throw new BusinessProcessException(e);
        }
        documentiCollegatiDocAmmService.gestioneAllegatiPerFatturazioneElettronica(userContext, docammElettronico);
        return docammElettronico;
    }

    public void firmaOTP(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        final Integer firmaFatture = firmaFatture(context.getUserContext(), firmaOTPBulk, getSelectedElements(context));
        setMessage(INFO_MESSAGE, "Sono state firmate e inviate correttamente " + firmaFatture + " Fatture.");
        refresh(context);
    }

    public Integer firmaFatture(UserContext userContext, FirmaOTPBulk firmaOTPBulk, List<VDocammElettroniciAttiviBulk> listVDocammElettronici) throws ApplicationException, BusinessProcessException {
        try {
            DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();
            Configurazione_cnrBulk config = component.getAuthenticatorPecSdi(userContext);
            logger.info("Recuperata Autenticazione PEC");
            FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean(FatturaPassivaElettronicaService.class);
            String pwd = null;
            try {
                pwd = StringEncrypter.decrypt(config.getVal01(), config.getVal02());
            } catch (EncryptionException e1) {
                new ApplicationException("Cannot decrypt password");
            }
            final String password = pwd;
            logger.info("Decrypt password");

            final List<byte[]> fattureFirmate = documentiCollegatiDocAmmService
                    .getArubaSignServiceClient()
                    .pkcs7SignV2Multiple(
                            firmaOTPBulk.getUserName(),
                            firmaOTPBulk.getPassword(),
                            firmaOTPBulk.getOtp(),
                            listVDocammElettronici.stream()
                                    .filter(vDocammElettronico -> !vDocammElettronico.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI))
                                    .map(VDocammElettroniciAttiviBulk::getStorageObject)
                                    .filter(storageObject -> Optional.ofNullable(storageObject).isPresent())
                                    .filter(storageObject -> storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0)
                                    .map(storageObject -> documentiCollegatiDocAmmService.getResource(storageObject))
                                    .map(inputStream -> {
                                        try {
                                            return IOUtils.toByteArray(inputStream);
                                        } catch (IOException _ex) {
                                            logger.info("Cannot read input stream", _ex);
                                            throw new DetailedRuntimeException(_ex);
                                        }
                                    }).collect(Collectors.toList())
                    );
            AtomicInteger index = new AtomicInteger();
            AtomicInteger indexInviate = new AtomicInteger();
            listVDocammElettronici.stream()
                    .filter(vDocammElettronico -> Optional.ofNullable(vDocammElettronico.getStorageObject()).isPresent())
                    .filter(vDocammElettronico -> vDocammElettronico.getStorageObject().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0)
                    .filter(vDocammElettronico -> !vDocammElettronico.getStatoInvioSdi().equals(VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI))
                    .forEach(vDocammElettronico -> {
                        StorageObject storageObject = vDocammElettronico.getStorageObject();
                        final int indexAndIncrement = index.getAndIncrement();
                        String nomeFile = storageObject.getPropertyValue(StoragePropertyNames.NAME.value());
                        String nomeFileP7m = nomeFile + ".p7m";
                        final byte[] byteSigned = fattureFirmate.get(indexAndIncrement);
                        if (Optional.ofNullable(byteSigned).isPresent()) {
                            Map<String, Object> metadataProperties = new HashMap<>();
                            metadataProperties.put(StoragePropertyNames.NAME.value(), nomeFileP7m);
                            metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(),
                                    SIGLAStoragePropertyNames.CNR_ENVELOPEDDOCUMENT.value());
                            metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                                    Arrays.asList(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_POST_FIRMA.value()));

                            IDocumentoAmministrativoElettronicoBulk docammElettronico = null;
                            try {
                                docammElettronico = createComponentSession().castDocumentoElettronico(userContext, vDocammElettronico);
                            } catch (ComponentException | RemoteException |  BusinessProcessException e) {
                                throw new RuntimeException(e);
                            }

                            final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(
                                    documentiCollegatiDocAmmService.getStorageObjectByPath(
                                            documentiCollegatiDocAmmService.recuperoFolderDocammElettronicoByPath(docammElettronico).getPath()
                                                    .concat(StorageDriver.SUFFIX).concat(nomeFileP7m)));

                            if (storageObjectByPath.isPresent()) {
                                /**
                                 * Se trovo il p7m caricato manualmente allora non aggiorno il contenuto
                                 */
                                if (!Optional.ofNullable(storageObjectByPath.get())
                                        .flatMap(storageObject1 -> Optional.ofNullable(storageObject1.<String>getPropertyValue("cm:title")))
                                        .filter(title -> title.equalsIgnoreCase("skip"))
                                        .isPresent()
                                ) {
                                    documentiCollegatiDocAmmService.updateStream(
                                            storageObjectByPath.get().getKey(),
                                            new ByteArrayInputStream(byteSigned),
                                            MimeTypes.P7M.mimetype()
                                    );
                                }
                            } else {
                                documentiCollegatiDocAmmService.storeSimpleDocument(
                                        new ByteArrayInputStream(byteSigned),
                                        MimeTypes.P7M.mimetype(),
                                        documentiCollegatiDocAmmService.recuperoFolderDocammElettronicoByPath(docammElettronico).getPath(),
                                        metadataProperties);
                            }

                            List<String> aspects = storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                            try {
                                boolean daInviare = true;
                                if (documentiCollegatiDocAmmService.hasAspect(storageObject, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_INVIATA.value())){
                                    daInviare = false;
                                } else {
                                    if (docammElettronico instanceof Fattura_attivaBulk && !((Fattura_attivaBulk)docammElettronico).isNotaCreditoDaNonInviareASdi()){
                                        aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_INVIATA.value());
                                    }
                                }
                                aspects.add(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());

                                if (docammElettronico instanceof Fattura_attivaBulk) {
                                    Fattura_attivaBulk fattura_attivaBulk = (Fattura_attivaBulk) docammElettronico;

                                    FatturaAttivaSingolaComponentSession componentFatturaAttiva = Utility.createFatturaAttivaSingolaComponentSession();

                                    final Fattura_attivaBulk fatturaAttivaByPrimaryKey =
                                            Optional.ofNullable(componentFatturaAttiva.findByPrimaryKey(userContext, fattura_attivaBulk))
                                                    .filter(Fattura_attivaBulk.class::isInstance)
                                                    .map(Fattura_attivaBulk.class::cast)
                                                    .orElseThrow(() -> new DetailedRuntimeException("Fattura non trovata!"));
                                    if (!fattura_attivaBulk.isNotaCreditoDaNonInviareASdi()) {
                                        final String nomeFileInvioSDI = component.recuperoNomeFileXml(userContext, fatturaAttivaByPrimaryKey).concat(".p7m");
                                        fatturaAttivaByPrimaryKey.setNomeFileInvioSdi(nomeFileInvioSDI);
                                        if (daInviare) {
                                            fatturaService.inviaFatturaElettronica(
                                                    config.getVal01(),
                                                    password,
                                                    new ByteArrayDataSource(new ByteArrayInputStream(byteSigned), MimeTypes.P7M.mimetype()),
                                                    nomeFileInvioSDI);
                                            logger.info("File firmato inviato");
                                        }
                                    }
                                    documentiCollegatiDocAmmService.updateProperties(
                                            Collections.singletonMap(
                                                    StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                                                    aspects),
                                            storageObject);
                                    componentFatturaAttiva.aggiornaFatturaInvioSDI(userContext, fatturaAttivaByPrimaryKey);
                                    logger.info("Fattura con progressivo univoco {}/{} aggiornata.", fattura_attivaBulk.getEsercizio(), fattura_attivaBulk.getProgrUnivocoAnno());
                                    indexInviate.getAndIncrement();
                                } else if (docammElettronico instanceof AutofatturaBulk) {
                                    AutofatturaBulk autofatturaBulk = (AutofatturaBulk) docammElettronico;

                                    AutoFatturaComponentSession componentAutofattura = Utility.createAutoFatturaComponentSession();

                                    final AutofatturaBulk autofatturaByPrimaryKey =
                                            Optional.ofNullable(componentAutofattura.findByPrimaryKey(userContext, autofatturaBulk))
                                                    .filter(AutofatturaBulk.class::isInstance)
                                                    .map(AutofatturaBulk.class::cast)
                                                    .orElseThrow(() -> new DetailedRuntimeException("Autofattura non trovata!"));

                                    final String nomeFileInvioSDI = component.recuperoNomeFileXml(userContext, autofatturaByPrimaryKey).concat(".p7m");
                                    autofatturaByPrimaryKey.setNomeFileInvioSdi(nomeFileInvioSDI);
                                    if (daInviare) {
                                        fatturaService.inviaFatturaElettronica(
                                                config.getVal01(),
                                                password,
                                                new ByteArrayDataSource(new ByteArrayInputStream(byteSigned), MimeTypes.P7M.mimetype()),
                                                nomeFileInvioSDI);
                                        logger.info("File firmato inviato");
                                    }

                                    documentiCollegatiDocAmmService.updateProperties(
                                            Collections.singletonMap(
                                                    StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                                                    aspects),
                                            storageObject);
                                    componentAutofattura.aggiornaAutofatturaInvioSDI(userContext, autofatturaByPrimaryKey);
                                    logger.info("Autofattura con progressivo univoco {}/{} aggiornata.", autofatturaBulk.getEsercizio(), autofatturaBulk.getProgrUnivocoAnno());
                                    indexInviate.getAndIncrement();
                                }
                            } catch (PersistencyException | ComponentException | IOException | EmailException e) {
                                throw new DetailedRuntimeException("Errore nell'invio della mail PEC per la fatturazione elettronica. Ripetere l'operazione di firma!", e);
                            }
                        }
                    });
            return indexInviate.get();
        } catch (ArubaSignServiceException _ex) {
            logger.error("ERROR firma fatture attive", _ex);
            throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
        } catch (BusinessProcessException | RemoteException | ComponentException _ex) {
            throw handleException(_ex);
        }
    }


    public void recuperoFilePerFirma(ActionContext context, List<VDocammElettroniciAttiviBulk> lista)
            throws ComponentException, IOException, BusinessProcessException {
        if (lista == null || lista.isEmpty())
            throw new ApplicationException("Selezionare almeno un elemento!");
        int contaScartati = 0;
        int contaDaAggiornare = 0;
        for (Iterator<VDocammElettroniciAttiviBulk> i = lista.iterator(); i.hasNext(); ) {
            VDocammElettroniciAttiviBulk vDocamm = i.next();
            if (VDocammElettroniciAttiviBulk.FATT_ELETT_INVIATA_SDI.equals(vDocamm.getStatoInvioSdi()))
                contaScartati++;
            else {
                if (VDocammElettroniciAttiviBulk.FATT_ELETT_ALLA_FIRMA.equals(vDocamm.getStatoInvioSdi())) {
                    try {
                        protocollaECreaFileXml(context.getUserContext(), vDocamm);
                    } catch (BusinessProcessException | ComponentException | PersistencyException e) {
                        throw new ApplicationException(e);
                    }
                }
                IDocumentoAmministrativoElettronicoBulk docammElettronico = createComponentSession().castDocumentoElettronico(context.getUserContext(), vDocamm);
                StorageObject so = documentiCollegatiDocAmmService.getFileXmlDocammElettronico(docammElettronico);
                if (so != null) {
                    logger.info("Recuperato File XML");
                    vDocamm.setStorageObject(so);
                }
                contaDaAggiornare++;
            }
        }
        if (contaDaAggiornare==0)
            throw new ApplicationException("Attenzione! Non risulta selezionato alcun documento da firmare."+
                    (contaScartati>0?" Ne sono stati scartati "+contaScartati+" che non risultano da firmare.":""));
    }

    public Fattura_attivaBulk protocollazione(UserContext userContext,
                                              Fattura_attivaBulk fattura) throws BusinessProcessException,
            ComponentException, RemoteException, PersistencyException {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);

        Long pgProtocollazione = componentFatturaAttiva.callGetPgPerProtocolloIVA(userContext);
        Long pgStampa = componentFatturaAttiva.callGetPgPerStampa(userContext);
        Timestamp dataStampa = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
        Integer offSet = 0;
        componentFatturaAttiva.preparaProtocollazioneEProtocolla(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
        return componentFatturaAttiva.ricercaFatturaByKey(userContext, new Long(fattura.getEsercizio()), fattura.getCd_cds(), fattura.getCd_unita_organizzativa(), fattura.getPg_fattura_attiva());

    }

    private void extracted(Exception e) throws ApplicationException {
        throw new ApplicationException(e.getMessage());
    }

    private File creaFileXml(UserContext userContext, IDocumentoAmministrativoElettronicoBulk docammElettronico) throws BusinessProcessException {
        try {
            DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();

            JAXBElement<FatturaElettronicaType> fatturaType = component.creaFatturaElettronicaType(userContext, docammElettronico);
            String nomeFile = component.recuperoNomeFileXml(userContext, docammElettronico);
            File file = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", nomeFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            JAXBContext jaxbContext = JAXBContext.newInstance("it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1");
            jaxbContext.createMarshaller().marshal(fatturaType, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            return file;
        } catch (Exception e) {
            throw new BusinessProcessException(e);
        }
    }
}
