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
import it.cnr.contab.docamm00.docs.bulk.Documento_amministrativo_attivoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
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
                ((Fattura_attivaBulk) model).setStatoFattElett(Fattura_attivaBulk.DA_PREDISPORRE_ALLA_FIRMA);
            } else {
                ((Fattura_attivaBulk) model).setStatoFattElett(Fattura_attivaBulk.DA_PREDISPORRE_E_FIRMARE);
            }
            setModel(context, model);
            super.init(config, context);
            setColumns(getBulkInfo().getColumnFieldPropertyDictionary("fatturazioneElettronicaSet"));
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
        Fattura_attivaBulk fattura = (Fattura_attivaBulk) oggettobulk;
        try {
            return getComponentSession().cerca(
                    actioncontext.getUserContext(),
                    compoundfindclause,
                    fattura,
                    "selectByClauseForFatturazioneElettronica");
        } catch (ComponentException | RemoteException e) {
            throw handleException(e);
        }
    }

    protected CRUDComponentSession getComponentSession() {
        return (CRUDComponentSession) EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession");
    }

    public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String cdUo = ((HttpActionContext) actioncontext).getParameter("cdUo");
        Long pgFattura = Long.valueOf(((HttpActionContext) actioncontext).getParameter("pgFattura"));

        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);

       Fattura_attivaBulk fattura = ( Fattura_attivaBulk) componentFatturaAttiva.findByPrimaryKey(actioncontext.getUserContext(), new Documento_amministrativo_attivoBulk(cds,cdUo,esercizio,pgFattura) );
        //StorageObject storageObject = documentiCollegatiDocAmmService.recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
        StorageObject storageObject = documentiCollegatiDocAmmService.recuperoFolderFattura(fattura);
        InputStream is = null;
        if (storageObject == null) {
            is = getStreamNewDocument(actioncontext, fattura);
        } else {
            //is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
            is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(fattura);
            if (is == null) {
                is = getStreamNewDocument(actioncontext, fattura);
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
                                             Fattura_attivaBulk fattura )
            throws Exception {
        InputStream is;
        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);
        UserContext userContext = actioncontext.getUserContext();
        SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).gestioneAllegatiPerFatturazioneElettronica(userContext, fattura);
        //is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
        is = documentiCollegatiDocAmmService.getStreamDocumentoAttivo(fattura);
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

            FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                    "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                    FatturaAttivaSingolaComponentSession.class);

            for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
                OggettoBulk docAmm = i.next();
                if (docAmm instanceof Fattura_attivaBulk) {
                    protocollaECreaFileXml(userContext, componentFatturaAttiva, (Fattura_attivaBulk) docAmm);
                }
            }
            setFocusedElement(context, null);
            refresh(context);

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


    public Fattura_attivaBulk protocollaECreaFileXml(UserContext userContext,
                                                     FatturaAttivaSingolaComponentSession componentFatturaAttiva, Fattura_attivaBulk fatturaAttiva)
            throws BusinessProcessException, ComponentException, RemoteException, PersistencyException {
        logger.info("Processo la fattura {}/{}", fatturaAttiva.getEsercizio(), fatturaAttiva.getPg_fattura_attiva());
        DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();
        // Questo metodo va invocato perchè fa tutti i controlli prima che la fattura venga protocollata
        component.preparaFattura(userContext, fatturaAttiva);

        if (fatturaAttiva.getProtocollo_iva() == null) {
            Fattura_attivaBulk fatturaAttivaProtocollata = protocollazione(userContext, fatturaAttiva);
            fatturaAttiva = fatturaAttivaProtocollata;
            logger.info("Creato protocollazione {}/{}", fatturaAttiva.getEsercizio(), fatturaAttiva.getPg_fattura_attiva());
        }
        File file = creaFileXml(userContext, fatturaAttiva);
        logger.info("Creato file XML {}/{}", fatturaAttiva.getEsercizio(), fatturaAttiva.getPg_fattura_attiva());
        List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
        List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
        try {
            StorageFile storageFile = new StorageFileFatturaAttiva(file, fatturaAttiva,
                    "application/xml", "FAXA" + fatturaAttiva.constructCMISNomeFile() + ".xml");

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
                        List<String> aspects = storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                        aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_XML_ANTE_FIRMA.value());
                        documentiCollegatiDocAmmService.updateProperties(
                                Collections.singletonMap(
                                        StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                                        aspects),
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
                fatturaAttiva = componentFatturaAttiva.aggiornaFatturaPredispostaAllaFirma(userContext, fatturaAttiva);
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
        documentiCollegatiDocAmmService.gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaAttiva);
        return fatturaAttiva;
    }

    public void firmaOTP(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        final Integer firmaFatture = firmaFatture(context.getUserContext(), firmaOTPBulk, getSelectedElements(context));
        setMessage(INFO_MESSAGE, "Sono state firmate e inviate correttamente " + firmaFatture + " Fatture.");
        refresh(context);
    }

    public Integer firmaFatture(UserContext userContext, FirmaOTPBulk firmaOTPBulk, List<Fattura_attivaBulk> listFattura) throws ApplicationException, BusinessProcessException {
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
            FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                    "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                    FatturaAttivaSingolaComponentSession.class);
            final List<byte[]> fattureFirmate = documentiCollegatiDocAmmService
                    .getArubaSignServiceClient()
                    .pkcs7SignV2Multiple(
                            firmaOTPBulk.getUserName(),
                            firmaOTPBulk.getPassword(),
                            firmaOTPBulk.getOtp(),
                            listFattura.stream()
                                    .filter(fattura_attivaBulk -> !fattura_attivaBulk.getStatoInvioSdi().equals(Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI))
                                    .map(Fattura_attivaBulk::getStorageObject)
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
            listFattura.stream()
                    .filter(fattura_attivaBulk -> Optional.ofNullable(fattura_attivaBulk.getStorageObject()).isPresent())
                    .filter(fattura_attivaBulk -> fattura_attivaBulk.getStorageObject().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0)
                    .filter(fattura_attivaBulk -> !fattura_attivaBulk.getStatoInvioSdi().equals(Fattura_attivaBulk.FATT_ELETT_INVIATA_SDI))
                    .forEach(fattura_attivaBulk -> {
                        StorageObject storageObject = fattura_attivaBulk.getStorageObject();
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

                            final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(
                                    documentiCollegatiDocAmmService.getStorageObjectByPath(
                                            documentiCollegatiDocAmmService.recuperoFolderFatturaByPath(fattura_attivaBulk).getPath()
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
                                        documentiCollegatiDocAmmService.recuperoFolderFatturaByPath(fattura_attivaBulk).getPath(),
                                        metadataProperties);
                            }

                            List<String> aspects = storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                            try {
                                boolean daInviare = true;
                                if (documentiCollegatiDocAmmService.hasAspect(storageObject, StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_INVIATA.value())){
                                    daInviare = false;
                                } else {
                                    if (!fattura_attivaBulk.isNotaCreditoDaNonInviareASdi()){
                                        aspects.add(StorageDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_FATTURA_ELETTRONICA_INVIATA.value());
                                    }
                                }
                                aspects.add(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
                                final Fattura_attivaBulk fatturaAttivaByPrimaryKey =
                                        Optional.ofNullable(componentFatturaAttiva.findByPrimaryKey(userContext, fattura_attivaBulk))
                                                .filter(Fattura_attivaBulk.class::isInstance)
                                                .map(Fattura_attivaBulk.class::cast)
                                                .orElseThrow(() -> new DetailedRuntimeException("Fattura non trovata!"));
                                if (!fattura_attivaBulk.isNotaCreditoDaNonInviareASdi() ) {
                                    final String nomeFileInvioSDI = component.recuperoNomeFileXml(userContext, fatturaAttivaByPrimaryKey).concat(".p7m");
                                    fatturaAttivaByPrimaryKey.setNomeFileInvioSdi(nomeFileInvioSDI);
                                    if (daInviare){
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


    public void recuperoFilePerFirma(ActionContext context, List<Fattura_attivaBulk> lista)
            throws ApplicationException, IOException {
        FatturaAttivaSingolaComponentSession componentFatturaAttiva;
        try {
            componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                    "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                    FatturaAttivaSingolaComponentSession.class);
        } catch (BusinessProcessException e1) {
            throw new ApplicationException(e1);
        }
        for (Iterator<Fattura_attivaBulk> i = lista.iterator(); i.hasNext(); ) {
            Fattura_attivaBulk docAmm = i.next();
            if (Fattura_attivaBulk.FATT_ELETT_ALLA_FIRMA.equals(docAmm.getStatoInvioSdi())) {
                try {
                    protocollaECreaFileXml(context.getUserContext(), componentFatturaAttiva, docAmm);
                } catch (BusinessProcessException | ComponentException | PersistencyException e) {
                    throw new ApplicationException(e);
                }
            }
            StorageObject so = documentiCollegatiDocAmmService.getFileXmlFatturaAttiva(docAmm);
            if (so != null) {
                logger.info("Recuperato File XML");
                docAmm.setStorageObject(so);
            }
        }
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

    private File creaFileXml(UserContext userContext, Fattura_attivaBulk fattura) throws BusinessProcessException {
        try {
            DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();

            JAXBElement<FatturaElettronicaType> fatturaType = component.creaFatturaElettronicaType(userContext, fattura);
            String nomeFile = component.recuperoNomeFileXml(userContext, fattura);
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
