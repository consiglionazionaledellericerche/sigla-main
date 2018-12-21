package it.cnr.contab.docamm00.bp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.contab.docamm00.storage.StorageFileFatturaAttiva;
import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.SignP7M;
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
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.jsp.Button;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.FatturaElettronicaType;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;

public class CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP extends SelezionatoreListaBP {
    private transient final static Logger logger = LoggerFactory.getLogger(CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP.class);
    private static final long serialVersionUID = 1L;
    private DocumentiCollegatiDocAmmService documentiCollegatiDocAmmService;
    private boolean utenteNonAbilitatoFirma;

    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP() {
        this("");
    }


    /**
     * DocumentiAmministrativiProtocollabiliBP constructor comment.
     *
     * @param function java.lang.String
     */
    public CRUDSelezionatoreDocumentiAmministrativiFatturazioneElettronicaBP(String function) {
        super(function + "Tr");
    }

    public boolean isUtenteNonAbilitatoFirma() {
        return utenteNonAbilitatoFirma;
    }

    public void setUtenteNonAbilitatoFirma(boolean utenteNonAbilitatoFirma) {
        this.utenteNonAbilitatoFirma = utenteNonAbilitatoFirma;
    }

    @Override
    protected void init(Config config, ActionContext context)
            throws BusinessProcessException {
        super.init(config, context);
        UserContext userContext = context.getUserContext();
        try {
            setUtenteNonAbilitatoFirma(isUtenteNonAbilitatoFirma(userContext));
        } catch (ComponentException e) {
            logger.error("Utente Non Abilitato Firma", e);
        }
        documentiCollegatiDocAmmService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
    }

    public void scaricaDocumentiCollegati(ActionContext actioncontext) throws Exception {
        Integer esercizio = Integer.valueOf(((HttpActionContext) actioncontext).getParameter("esercizio"));
        String cds = ((HttpActionContext) actioncontext).getParameter("cds");
        String cdUo = ((HttpActionContext) actioncontext).getParameter("cdUo");
        Long pgFattura = Long.valueOf(((HttpActionContext) actioncontext).getParameter("pgFattura"));
        StorageObject storageObject = documentiCollegatiDocAmmService.recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
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
        Button[] toolbar = new Button[3];
        int i = 0;
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.print");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "Toolbar.excel");
        toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.FirmaInvia");
        return toolbar;
    }

    public DocAmmFatturazioneElettronicaComponentSession createComponentSession()
            throws BusinessProcessException {
        return (DocAmmFatturazioneElettronicaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession",
                DocAmmFatturazioneElettronicaComponentSession.class);
    }

    public void firmaOTP(ActionContext context, FirmaOTPBulk firmaOTPBulk) throws Exception {
        UserContext userContext = context.getUserContext();
        List<OggettoBulk> lista = getSelectedElements(context);
        DocAmmFatturazioneElettronicaComponentSession component = createComponentSession();

        FatturaAttivaSingolaComponentSession componentFatturaAttiva = (FatturaAttivaSingolaComponentSession) createComponentSession(
                "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession",
                FatturaAttivaSingolaComponentSession.class);

        for (Iterator<OggettoBulk> i = lista.iterator(); i.hasNext(); ) {
            OggettoBulk docAmm = i.next();
            if (docAmm instanceof Fattura_attivaBulk) {
                Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk) docAmm;
                logger.info("Processo la fattura");
                Configurazione_cnrBulk config = component.getAuthenticatorPecSdi(userContext);
                logger.info("Recuperata Autenticazione PEC");
                File file = creaFileXml(userContext, fatturaAttiva);

                logger.info("Creato file XML");
                if (fatturaAttiva.getProtocollo_iva() == null){
                    fatturaAttiva = protocollazione(userContext, fatturaAttiva);
                    logger.info("Creato protocollazione");
                }
                final Fattura_attivaBulk fatturaProtocollata = fatturaAttiva; 

                List<StorageFile> storageFileCreate = new ArrayList<StorageFile>();
                List<StorageFile> storageFileAnnullati = new ArrayList<StorageFile>();
                try {
                    StorageFile storageFile = new StorageFileFatturaAttiva(file, fatturaProtocollata,
                            "application/xml", "FAXA" + fatturaProtocollata.constructCMISNomeFile() + ".xml");

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
                                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
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
                        if (storageFile.getStorageObject().<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue() > 0) {
                            String nomeFile = file.getName();
                            String nomeFileP7m = nomeFile + ".p7m";
                            SignP7M signP7M = new SignP7M(
                                    storageFile.getStorageObject().getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()),
                                    firmaOTPBulk.getUserName(),
                                    firmaOTPBulk.getPassword(),
                                    firmaOTPBulk.getOtp(),
                                    nomeFileP7m
                            );
                            try {
                                Optional.ofNullable(documentiCollegatiDocAmmService.signDocuments(signP7M, "service/sigla/firma/fatture"))
                                        .map(key -> documentiCollegatiDocAmmService.getStorageObjectBykey(key))
                                        .ifPresent(storageObject -> {
                                            InputStream streamSigned = documentiCollegatiDocAmmService.getResource(storageObject);
                                            File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", nomeFileP7m);
                                            try {
                                                OutputStream outputStream = new FileOutputStream(fileSigned);
                                                IOUtils.copy(streamSigned, outputStream);
                                                outputStream.close();
                                                logger.info("Salvato file firmato temporaneo");
                                                fatturaProtocollata.setNomeFileInvioSdi(nomeFileP7m);
                                                componentFatturaAttiva.aggiornaFatturaInvioSDI(userContext, fatturaProtocollata);
                                                logger.info("Fattura con progressivo univoco " + fatturaProtocollata.getEsercizio() + "/" + fatturaProtocollata.getProgrUnivocoAnno() + " aggiornata.");
                                                if (!fatturaProtocollata.isNotaCreditoDaNonInviareASdi()) {
                                                	FatturaPassivaElettronicaService fatturaService = SpringUtil.getBean(FatturaPassivaElettronicaService.class);
                                                	String password = null;
                                                	try {
                                                		password = StringEncrypter.decrypt(config.getVal01(), config.getVal02());
                                                	} catch (EncryptionException e1) {
                                                		new ApplicationException("Cannot decrypt password");
                                                	}
                                                	fatturaService.inviaFatturaElettronica(config.getVal01(), password, fileSigned, nomeFileP7m);
                                                	logger.info("File firmato inviato");

                                                }
                                            } catch (Exception ex) {
                                                logger.error("Errore nell'invio del file " + ex.getMessage() == null ? (ex.getCause() == null ? "" : ex.getCause().toString()) : ex.getMessage());

                                                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                                                aspects.remove(SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
                                                documentiCollegatiDocAmmService.updateProperties(
                                                        Collections.singletonMap(
                                                                StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                                                                aspects),
                                                        storageObject);
                                                documentiCollegatiDocAmmService.delete(storageObject);
                                                throw new DetailedRuntimeException("Errore nell'invio della mail PEC per la fatturazione elettronica. Ripetere l'operazione di firma!");
                                            }
                                        });
                            } catch (DetailedRuntimeException _ex) {
                                throw new ApplicationException(_ex.getMessage());
                            } catch (StorageException _ex) {
                                throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
                            }
                            commitUserTransaction();
                        } else {
                            logger.error("Errore. Il file XML salvato era vuoto.");
                            throw new ApplicationException("Errore durante il processo di firma elettronica. Ripetere l'operazione di firma!");
                        }
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
                    rollbackUserTransaction();
                    extracted(e);
                }
                documentiCollegatiDocAmmService.gestioneAllegatiPerFatturazioneElettronica(userContext, fatturaProtocollata);
            }
        }
        setFocusedElement(context, null);
        refresh(context);
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

    private File creaFileXml(UserContext userContext, Fattura_attivaBulk fattura) throws Exception {
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
