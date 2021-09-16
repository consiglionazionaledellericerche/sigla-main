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

package it.cnr.contab.docamm00.service;

import it.cnr.contab.chiusura00.ejb.RicercaDocContComponentSession;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.docamm00.fatturapa.bulk.*;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.spring.service.UtilService;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.mail.SimplePECMail;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.gov.fatturapa.sdi.messaggi.v1.MetadatiInvioFileType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.XmlMappingException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.*;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FatturaPassivaElettronicaService implements InitializingBean {
    private transient final static Logger logger = LoggerFactory.getLogger(FatturaPassivaElettronicaService.class);

    private FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession;
    private FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession;
    private RicercaDocContComponentSession ricercaDocContComponentSession;
    private DocAmmFatturazioneElettronicaComponentSession docAmmFatturazioneElettronicaComponentSession;
    private CRUDComponentSession crudComponentSession;

    private RicezioneFatturePA ricezioneFattureService;
    private TrasmissioneFatturePA trasmissioneFattureService;
    private FatturazioneElettronicaClient fatturazioneElettronicaClient;
    private UserContext userContext;
    private Boolean pecScanDisable;
    private Properties pecMailConf;
    private final List<String> listaMessageIdAlreadyScanned = new ArrayList<String>();

    @Autowired
    private StoreService storeService;
    @Autowired
    private UtilService utilService;

    @Value("${pec.sdi.replyTo:}")
    private String replyTo;

    private String pecHostName, pecURLName, pecSDIAddress, pecSDISubjectFatturaAttivaInvioTerm, pecSDISubjectNotificaPecTerm, pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm,
            pecSDIFromStringTerm, pecSDISubjectRiceviFattureTerm, pecSDISubjectFatturaAttivaRicevutaConsegnaTerm, pecSDISubjectFatturaAttivaNotificaScartoTerm, pecSDISubjectFatturaAttivaMancataConsegnaTerm,
            pecSDISubjectNotificaEsitoTerm, pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm,
            pecSDISubjectNotificaRifiutoTerm,
            pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm, pecHostAddressReturn,
            pecSDISubjectMancataConsegnaPecTerm;
    private List<String> pecScanFolderName, pecScanReceiptFolderName, pecHostAddress;


    public void afterPropertiesSet() throws Exception {
        userContext = new WSUserContext("SDI", null,
                new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
                null, null, null);
        this.fatturaElettronicaPassivaComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession"))
                .filter(FatturaElettronicaPassivaComponentSession.class::isInstance)
                .map(FatturaElettronicaPassivaComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCAMM00_EJB_FatturaElettronicaPassivaComponentSession"));

        this.ricezioneFattureService = Optional.ofNullable(EJBCommonServices.createEJB("RicezioneFatture!it.cnr.contab.docamm00.ejb.RicezioneFatturePA"))
                .filter(RicezioneFatturePA.class::isInstance)
                .map(RicezioneFatturePA.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb RicezioneFatture!it.cnr.contab.docamm00.ejb.RicezioneFatturePA"));

        this.trasmissioneFattureService = Optional.ofNullable(EJBCommonServices.createEJB("TrasmissioneFatture!it.cnr.contab.docamm00.ejb.TrasmissioneFatturePA"))
                .filter(TrasmissioneFatturePA.class::isInstance)
                .map(TrasmissioneFatturePA.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb TrasmissioneFatture!it.cnr.contab.docamm00.ejb.TrasmissioneFatturePA"));

        this.fatturaAttivaSingolaComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession"))
                .filter(FatturaAttivaSingolaComponentSession.class::isInstance)
                .map(FatturaAttivaSingolaComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession"));

        this.ricercaDocContComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRCHIUSURA00_EJB_RicercaDocContComponentSession"))
                .filter(RicercaDocContComponentSession.class::isInstance)
                .map(RicercaDocContComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRCHIUSURA00_EJB_RicercaDocContComponentSession"));

        this.docAmmFatturazioneElettronicaComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession"))
                .filter(DocAmmFatturazioneElettronicaComponentSession.class::isInstance)
                .map(DocAmmFatturazioneElettronicaComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCAMM00_EJB_DocAmmFatturazioneElettronicaComponentSession"));

        this.crudComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb JADAEJB_CRUDComponentSession"));

    }

    public RicercaDocContComponentSession getRicercaDocContComponentSession() {
        return ricercaDocContComponentSession;
    }

    public void setRicercaDocContComponentSession(
            RicercaDocContComponentSession ricercaDocContComponentSession) {
        this.ricercaDocContComponentSession = ricercaDocContComponentSession;
    }

    public void setPecSDISubjectFatturaAttivaRicevutaConsegnaTerm(
            String pecSDISubjectFatturaAttivaRicevutaConsegnaTerm) {
        this.pecSDISubjectFatturaAttivaRicevutaConsegnaTerm = pecSDISubjectFatturaAttivaRicevutaConsegnaTerm;
    }

    public void setPecSDISubjectFatturaAttivaNotificaScartoTerm(
            String pecSDISubjectFatturaAttivaNotificaScartoTerm) {
        this.pecSDISubjectFatturaAttivaNotificaScartoTerm = pecSDISubjectFatturaAttivaNotificaScartoTerm;
    }

    public void setPecSDISubjectFatturaAttivaMancataConsegnaTerm(
            String pecSDISubjectFatturaAttivaMancataConsegnaTerm) {
        this.pecSDISubjectFatturaAttivaMancataConsegnaTerm = pecSDISubjectFatturaAttivaMancataConsegnaTerm;
    }

    public void setPecSDISubjectNotificaEsitoTerm(
            String pecSDISubjectNotificaEsitoTerm) {
        this.pecSDISubjectNotificaEsitoTerm = pecSDISubjectNotificaEsitoTerm;
    }

    public void setPecSDISubjectFatturaAttivaDecorrenzaTerminiTerm(
            String pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm) {
        this.pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm = pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm;
    }

    public void setPecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm(
            String pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm) {
        this.pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm = pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm;
    }

    public String getPecSDISubjectMancataConsegnaPecTerm() {
        return pecSDISubjectMancataConsegnaPecTerm;
    }

    public void setPecSDISubjectMancataConsegnaPecTerm(
            String pecSDISubjectMancataConsegnaPecTerm) {
        this.pecSDISubjectMancataConsegnaPecTerm = pecSDISubjectMancataConsegnaPecTerm;
    }

    public void setPecSDISubjectNotificaRifiutoTerm(String pecSDISubjectNotificaRifiutoTerm) {
        this.pecSDISubjectNotificaRifiutoTerm = pecSDISubjectNotificaRifiutoTerm;
    }

    public void setFatturaElettronicaPassivaComponentSession(
            FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession) {
        this.fatturaElettronicaPassivaComponentSession = fatturaElettronicaPassivaComponentSession;
    }

    public void setDocAmmFatturazioneElettronicaComponentSession(
            DocAmmFatturazioneElettronicaComponentSession docAmmFatturazioneElettronicaComponentSession) {
        this.docAmmFatturazioneElettronicaComponentSession = docAmmFatturazioneElettronicaComponentSession;
    }

    public void setRicezioneFattureService(
            RicezioneFatturePA ricezioneFattureService) {
        this.ricezioneFattureService = ricezioneFattureService;
    }

    public void setTrasmissioneFattureService(
            TrasmissioneFatturePA trasmissioneFattureService) {
        this.trasmissioneFattureService = trasmissioneFattureService;
    }

    public void setFatturazioneElettronicaClient(
            FatturazioneElettronicaClient fatturazioneElettronicaClient) {
        this.fatturazioneElettronicaClient = fatturazioneElettronicaClient;
    }

    public void setPecScanDisable(Boolean pecScanDisable) {
        this.pecScanDisable = pecScanDisable;
    }

    public void setPecMailConf(Properties pecMailConf) {
        this.pecMailConf = pecMailConf;
    }

    public void setPecHostName(String pecHostName) {
        this.pecHostName = pecHostName;
    }

    public void setPecURLName(String pecURLName) {
        this.pecURLName = pecURLName;
    }

    public void setPecSDIAddress(String pecSDIAddress) {
        this.pecSDIAddress = pecSDIAddress;
    }

    public void setPecScanReceiptFolderName(String pecScanReceiptFolderName) {
        this.pecScanReceiptFolderName = Arrays.asList(StringUtils.split(pecScanReceiptFolderName, ","));
	}

    public void setPecSDISubjectFatturaAttivaInvioTerm(
            String pecSDISubjectFatturaAttivaInvioTerm) {
        this.pecSDISubjectFatturaAttivaInvioTerm = pecSDISubjectFatturaAttivaInvioTerm;
    }

    public void setPecSDISubjectFatturaPassivaNotificaScartoEsitoTerm(
            String pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm) {
        this.pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm = pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm;
    }

    public void setPecSDISubjectNotificaPecTerm(
            String pecSDISubjectNotificaPecTerm) {
        this.pecSDISubjectNotificaPecTerm = pecSDISubjectNotificaPecTerm;
    }

    public void setPecSDIFromStringTerm(String pecSDIFromStringTerm) {
        this.pecSDIFromStringTerm = pecSDIFromStringTerm;
    }

    public void setPecSDISubjectRiceviFattureTerm(
            String pecSDISubjectRiceviFattureTerm) {
        this.pecSDISubjectRiceviFattureTerm = pecSDISubjectRiceviFattureTerm;
    }

    public void setPecScanFolderName(String pecScanFolderName) {
        this.pecScanFolderName = Arrays.asList(StringUtils.split(pecScanFolderName, ","));
    }

    public void setPecHostAddress(String pecHostAddress) {
        this.pecHostAddress = Arrays.asList(StringUtils.split(pecHostAddress, ","));
    }

    public void setPecHostAddressReturn(
            String pecHostAddressReturn) {
        this.pecHostAddressReturn = pecHostAddressReturn;
    }

    public void execute() {
        caricaFatture(-1);
    }

    public void caricaFatture(Integer daysBefore) {
        caricaFatture(daysBefore, null);
    }

    public void caricaFatture(Integer daysBefore, String filterOggetto) {
        try {
            if (pecScanDisable) {
                logger.info("PEC scan is disabled");
            } else {
                logger.info("PEC SCAN for ricevi Fatture started at: " + new Date());
                Configurazione_cnrBulk email = fatturaElettronicaPassivaComponentSession.getEmailPecSdi(userContext, true);
                if (email == null) {
                    logger.info("PEC SCAN for ricevi Fatture alredy started in another server.");
                } else {
                    try {
                        pecScan(email.getVal01(), email.getVal02(), daysBefore, filterOggetto);
                    } finally {
                        fatturaElettronicaPassivaComponentSession.unlockEmailPEC(userContext);
                    }
                }
                logger.info("PEC SCAN for ricevi Fatture finished at: " + new Date());
                fatturaAttivaSingolaComponentSession.gestioneAvvisoInvioMailFattureAttive(userContext);
            }
        } catch (Throwable _ex) {
            logger.error("ScheduleExecutor error", _ex);
        }
    }

    public void allineaNotificheExecute() {
        try {
            if (pecScanDisable) {
                logger.info("PEC scan is disabled");
            } else {
                logger.info("Allinea notifiche started at: " + new Date());
                fatturaElettronicaPassivaComponentSession.
                        allineaEsitoCommitente(userContext, TipoIntegrazioneSDI.PEC);
                logger.info("Allinea notifiche finished at: " + new Date());
            }
        } catch (Throwable _ex) {
            logger.error("ScheduleExecutor error", _ex);
        }
    }

    private void riceviFattura(Message message, String userName) throws ComponentException {
        try {
            List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
            if (bodyParts.isEmpty()) {
                logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName +
                        " non ha file allegati.");
                return;
            }
            if (bodyParts.size() > 2) {
                logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName +
                        " ha più di due file allegati.");
                return;
            }
            BodyPart bodyPartFattura = null, bodyPartMetadati = null;
            for (BodyPart bodyPart : bodyParts) {
                if (isBodyPartMetadati(bodyPart)) {
                    bodyPartMetadati = bodyPart;
                } else {
                    bodyPartFattura = bodyPart;
                }
            }
            if (bodyPartFattura != null && bodyPartMetadati != null) {
                @SuppressWarnings("unchecked")
                JAXBElement<MetadatiInvioFileType> metadatiInvioFileType = ((JAXBElement<MetadatiInvioFileType>)
                        fatturazioneElettronicaClient.getUnmarshaller().
                                unmarshal(new StreamSource(bodyPartMetadati.getInputStream())));
                String replyTo = getReplyTo(message);
                List<DocumentoEleTrasmissioneBulk> documentoEleTrasmissioneBulks =
                        fatturaElettronicaPassivaComponentSession.recuperoTrasmissione(userContext,
                                new Long(metadatiInvioFileType.getValue().getIdentificativoSdI()));
                boolean existsIdentificativo = documentoEleTrasmissioneBulks.stream()
                        .map(DocumentoEleTrasmissioneBase::getCmisNodeRef)
                        .anyMatch(s -> storeService.getChildren(s)
                                .stream()
                                .anyMatch(storageObject -> {
                                	return storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value())
											.contains("P:sigla_fatture_attachment:trasmissione_fattura");
                                }));
                if (!existsIdentificativo) {
                    DataSource byteArrayDataSourceFattura = new UploadedFileDataSource(bodyPartFattura);
                    DataSource byteArrayDataSourceMetadati = new UploadedFileDataSource(bodyPartMetadati);
                    ricezioneFattureService.riceviFatturaSIGLA(
                            new BigInteger(metadatiInvioFileType.getValue().getIdentificativoSdI()),
                            bodyPartFattura.getFileName(),
                            replyTo,
                            new DataHandler(byteArrayDataSourceFattura),
                            bodyPartMetadati.getFileName(),
                            new DataHandler(byteArrayDataSourceMetadati));
                } else {

                }
            } else {
                logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName + " è stato precessato ma gli allegati presenti non sono conformi.");
            }
        } catch (Throwable _ex) {
            logger.error("PEC scan error while importing file.", _ex);
        }
    }

    private String getReplyTo(Message message) throws MessagingException {
        Address[] repliesAddress = message.getReplyTo();
        String replyTo = null;
        if (repliesAddress != null) {
            if (repliesAddress.length > 0) {
                replyTo = repliesAddress[0].toString();
            }
        }
        logger.info("ReplyTo: " + replyTo);
        return replyTo;
    }

    private void notificaFatturaPassivaScartoEsito(Message message, String userName) throws ComponentException {
        try {
            String[] headerMessage = getMessageID(message);
            if (headerMessage != null) {
                String messageID = Arrays.asList(headerMessage).toString();
                String messageIDWithUser = userName + " " + messageID;
                logger.info("MessageIDWithUser " + messageIDWithUser);
                if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)) {
                    logger.info(messageIDWithUser);
                    try {
                        List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
                        if (bodyParts.isEmpty()) {
                            logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName +
                                    " non ha file allegati.");
                            return;
                        }
                        if (bodyParts.size() > 2) {
                            logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName +
                                    " ha più di due file allegati.");
                            return;
                        }
                        boolean trovatoFile = false;
                        for (BodyPart bodyPart : bodyParts) {
                            String fileName = extractFileName(bodyPart);
                            if (fileName.toLowerCase().endsWith("xml")) {
                                ricezioneFattureService.notificaScartoEsito(fileName, createDataHandler(bodyPart), message.getReceivedDate());
                                trovatoFile = true;
                                break;
                            }
                        }
                        if (!trovatoFile) {
                            logger.warn("Il messaggio con id:" + message.getMessageNumber() + " recuperato dalla casella PEC:" + userName + " è stato precessato ma gli allegati presenti non sono conformi.");
                            SendMail.sendErrorMail("Fatture Elettroniche: Passive: Scarto Esito. Allegati non conformi. Mail:" + userName, message.getDescription());
                        }
                        listaMessageIdAlreadyScanned.add(messageIDWithUser);
                        logger.info("Message Added");
                    } catch (Exception e) {
                        logger.error("PEC scan error while importing file.", e);
                    }
                }
            }
        } catch (MessagingException e) {
            logger.error("Errore durante il recupero dell'ID del messaggio.", e);
        }
    }

    public void pecScan(String userName, String password, Integer daysBefore, String filterOggetto) throws ComponentException {
        logger.info("PEC SCAN for ricevi Fatture email: {} pwd: {} ", userName, password);
        Properties props = System.getProperties();
        props.putAll(pecMailConf);
        try {
            try {
                password = StringEncrypter.decrypt(userName, password);
            } catch (EncryptionException e1) {
                new AuthenticationFailedException("Cannot decrypt password");
            }
            final Session session = Session.getInstance(props);
            URLName urlName = new URLName(pecURLName);
            Store store = session.getStore(urlName);
            store.connect(userName, password);
            searchMailFromPec(userName, password, store, daysBefore, filterOggetto);
            searchMailFromSdi(userName, store, daysBefore, filterOggetto);
            searchMailFromReturn(userName, store, daysBefore, filterOggetto);
            //TODO Per ora disabilitato
            //searchMailForRifiutoPEC(userName, store, daysBefore, filterOggetto);
            store.close();
        } catch (AuthenticationFailedException e) {
            logger.error("Error while scan PEC email:" + userName + " - password:" + password);
        } catch (NoSuchProviderException e) {
            logger.error("Error while scan PEC email:" + userName, e);
        } catch (MessagingException e) {
            logger.error("Error while scan PEC email:" + userName, e);
        }
    }

    private void searchMailFromPec(String userName, String password, final Store store, Integer daysBefore, String filterOggetto)
            throws MessagingException {
        List<Folder> folders = new ArrayList<Folder>();
        for (String folderName : pecScanReceiptFolderName) {
            folders.add(store.getFolder(folderName));
        }
        for (Folder folder : folders) {
            if (folder.exists()) {
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                processingMailFromHostPec(folder, userName, password, daysBefore, filterOggetto);
                if (folder.isOpen())
                    folder.close(true);
            }
        }
    }

    private void searchMailFromSdi(String userName, final Store store, Integer daysBefore, String filterOggetto)
            throws MessagingException {
        List<Folder> folders = new ArrayList<Folder>();
        for (String folderName : pecScanFolderName) {
            folders.add(store.getFolder(folderName));
        }
        for (Folder folder : folders) {
            if (folder.exists()) {
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                processingMailFromSdi(folder, userName, daysBefore, filterOggetto);
                if (folder.isOpen())
                    folder.close(true);
            }
        }
    }

    private void searchMailFromReturn(String userName, final Store store, Integer daysBefore, String filterOggetto)
            throws MessagingException {
        List<Folder> folders = new ArrayList<Folder>();
        for (String folderName : pecScanFolderName) {
            folders.add(store.getFolder(folderName));
        }
        for (Folder folder : folders) {
            if (folder.exists()) {
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                processingMailFromReturn(folder, userName, daysBefore, filterOggetto);
                if (folder.isOpen())
                    folder.close(true);
            }
        }
    }

    private void searchMailForRifiutoPEC(String userName, final Store store, Integer daysBefore, String filterOggetto)
            throws MessagingException {
        List<Folder> folders = new ArrayList<Folder>();
        for (String folderName : pecScanFolderName) {
            folders.add(store.getFolder(folderName));
        }
        for (Folder folder : folders) {
            if (folder.exists()) {
                if (!folder.isOpen())
                    folder.open(Folder.READ_ONLY);
                processingMailForRifiutoPEC(folder, userName, daysBefore, filterOggetto);
                if (folder.isOpen())
                    folder.close(true);
            }
        }
    }

    private void processingMailFromSdi(Folder folder, String userName, Integer daysBefore, String filterOggetto) throws MessagingException {
        if (isDateForRecoveryNotifier()) {
            List<SearchTerm> newTerms = createTermsForSearchSdi(true, daysBefore);
            Optional.ofNullable(filterOggetto)
                    .ifPresent(s -> newTerms.add(new SubjectTerm(s)));
            Message[] newMessages = folder.search(new AndTerm(newTerms.toArray(new SearchTerm[newTerms.size()])));
            logger.info("Recuperati " + newMessages.length + " messaggi SDI dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
            for (int i = 0; i < newMessages.length; i++) {
                try {
                    Message message = newMessages[i];
                    if (message.getSubject() != null) {
                        if (message.getSubject().contains(pecSDISubjectNotificaEsitoTerm)) {
                            notificaFatturaAttivaEsito(message);
                        } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaNotificaScartoTerm)) {
                            notificaFatturaAttivaScarto(message);
                        } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm)) {
                            notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(message);
                        }
                    }
                } catch (Exception e) {
                    logger.error("PEC scan error while importing file.", e);
                    java.io.StringWriter sw = new java.io.StringWriter();
                    e.printStackTrace(new java.io.PrintWriter(sw));
                    SendMail.sendErrorMail("Errore. Fatture Elettroniche: Processing mail SDI ", sw.toString());
                }
            }
        }
        List<SearchTerm> terms = createTermsForSearchSdi(false, daysBefore);
        Optional.ofNullable(filterOggetto)
                .ifPresent(s -> terms.add(new SubjectTerm(s)));
        Message[] messages = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
        logger.info("Recuperati " + messages.length + " messaggi SDI dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
        for (int i = 0; i < messages.length; i++) {
            try {
                Message message = messages[i];
                if (message.getSubject() != null) {
                    logger.info("Oggetto:" + message.getSubject());
                    if (message.getSubject().contains(pecSDISubjectRiceviFattureTerm)) {
                        riceviFattura(message, userName);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm)) {
                        notificaFatturaPassivaScartoEsito(message, userName);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaRicevutaConsegnaTerm)) {
                        notificaFatturaAttivaRicevutaConsegna(message, userName);
                    } else if (message.getSubject().contains(pecSDISubjectNotificaEsitoTerm) && !isDateForRecoveryNotifier()) {
                        notificaFatturaAttivaEsito(message);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaNotificaScartoTerm) && !isDateForRecoveryNotifier()) {
                        notificaFatturaAttivaScarto(message);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaMancataConsegnaTerm)) {
                        notificaFatturaAttivaMancataConsegna(message);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm)) {
                        notificaFatturaAttivaDecorrenzaTermini(message);
                    } else if (message.getSubject().contains(pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm) && !isDateForRecoveryNotifier()) {
                        notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(message);
                    } else {
                        logger.warn("Fatture Elettroniche: Attive: Oggetto dell'e-mail non gestito." + message.getSubject());
                        SendMail.sendErrorMail("Fatture Elettroniche: Oggetto dell'e-mail non gestito. Mail:" + userName, message.getDescription());
                    }
                }
            } catch (Exception e) {
                logger.error("PEC scan error while importing file.", e);
                java.io.StringWriter sw = new java.io.StringWriter();
                e.printStackTrace(new java.io.PrintWriter(sw));
                SendMail.sendErrorMail("Errore. Fatture Elettroniche: Processing mail SDI ", sw.toString());
            }
        }
    }

    private Boolean isDateForRecoveryNotifier() {
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
		return today.get(Calendar.MONTH) == Calendar.JANUARY;
	}

    private void processingMailFromReturn(Folder folder, String userName, Integer daysBefore, String filterOggetto) throws MessagingException {
        List<SearchTerm> terms = createTermsForSearchReturn(daysBefore);
        Optional.ofNullable(filterOggetto)
                .ifPresent(s -> terms.add(new SubjectTerm(s)));
        Message[] messages = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
        logger.info("Recuperati " + messages.length + " messaggi di mail non recapitate dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
        for (int i = 0; i < messages.length; i++) {
            try {
                Message message = messages[i];
                if (message.getSubject() != null) {
                    try {
                        String[] headerMessage = getMessageID(message);
                        if (headerMessage != null) {
                            String messageID = Arrays.asList(headerMessage).toString();
                            String messageIDWithUser = userName + " " + messageID;
                            logger.info("MessageIDWithUser " + messageIDWithUser);
                            if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)) {
                                logger.info(messageIDWithUser);
                                try {
                                    DataHandler data = message.getDataHandler();
                                    estraiBodyPart(data.getContent(), false);
                                    listaMessageIdAlreadyScanned.add(messageIDWithUser);
                                    logger.info("Message Added");
                                } catch (Exception e) {
                                    logger.error("PEC scan error while importing file.", e);
                                }
                            }
                        }
                    } catch (MessagingException e) {
                        logger.error("Errore durante il recupero dell'ID del messaggio.", e);
                    }

                }
            } catch (Exception e) {
                logger.error("PEC scan error while importing file.", e);
                java.io.StringWriter sw = new java.io.StringWriter();
                e.printStackTrace(new java.io.PrintWriter(sw));
                SendMail.sendErrorMail("Errore. Fatture Elettroniche: Processing mail SDI ", sw.toString());
            }
        }
    }

    private void processingMailForRifiutoPEC(Folder folder, String userName, Integer daysBefore, String filterOggetto) throws MessagingException {
        List<SearchTerm> terms = new ArrayList<SearchTerm>();
        addConditionDate(terms, daysBefore);
        Optional.ofNullable(filterOggetto)
                .ifPresent(s -> terms.add(new SubjectTerm(s)));
        terms.add(new SubjectTerm(pecSDISubjectNotificaRifiutoTerm));
        Message[] messages = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
        logger.info("Recuperati {} messaggi di rifiuto dalla casella PEC {} nella folder {}", messages.length, userName, folder.getFullName());
        Arrays.asList(messages)
                .stream()
                .filter(message -> {
                    try {
                        return Optional.ofNullable(message.getSubject()).isPresent();
                    } catch (MessagingException e) {
                        throw new DetailedRuntimeException(e);
                    }
                })
                .forEach(message -> {
                    try {
                        String[] headerMessage = getMessageID(message);
                        if (headerMessage != null) {
                            String messageID = Arrays.asList(headerMessage).toString();
                            String messageIDWithUser = userName + " " + messageID;
                            logger.info("MessageIDWithUser {}", messageIDWithUser);
                            if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)) {
                                try {
                                    String subject = message.getSubject();
                                    Long identificativoSdI = Long.valueOf(
                                            subject.substring(
                                                    subject.indexOf(pecSDISubjectNotificaRifiutoTerm) + pecSDISubjectNotificaRifiutoTerm.length() + 2
                                            )
                                    );
                                    fatturaElettronicaPassivaComponentSession.recuperoDocumento(
                                            userContext,
                                            identificativoSdI
                                    ).forEach(documentoEleTestataBulk -> {
                                        if (!subject.startsWith("ACCETTAZIONE")) {
                                            if (subject.startsWith("AVVISO DI MANCATA CONSEGNA")) {
                                                // TODO Abilitare stato documento
                                                //documentoEleTestataBulk.setStatoDocumento(StatoDocumentoEleEnum.PEC_NON_INVIATA.name());
                                            } else {
                                                documentoEleTestataBulk.setStatoDocumento(StatoDocumentoEleEnum.DA_STORNARE.name());
                                            }
                                            documentoEleTestataBulk.setToBeUpdated();
                                            try {
                                                crudComponentSession.modificaConBulk(userContext, documentoEleTestataBulk);
                                            } catch (ComponentException | RemoteException e) {
                                                throw new DetailedRuntimeException(e);
                                            }
                                        }
                                    });
                                    listaMessageIdAlreadyScanned.add(messageIDWithUser);
                                } catch (Exception e) {
                                    logger.error("PEC scan error while importing file.", e);
                                }
                            }
                        }
                    } catch (MessagingException e) {
                        logger.error("Errore durante il recupero dell'ID del messaggio.", e);
                    }
                });
    }

    public List<SearchTerm> createTermsForSearchSdi(Boolean dateForRecoveryNotifier, Integer daysBefore) {
        List<SearchTerm> terms = new ArrayList<SearchTerm>();
        addConditionDate(terms, dateForRecoveryNotifier, daysBefore);
        terms.add(new FromStringTerm(pecSDIFromStringTerm));
        return terms;
    }

    public List<SearchTerm> createTermsForSearchReturn(Integer daysBefore) {
        List<SearchTerm> terms = new ArrayList<SearchTerm>();
        addConditionDate(terms, daysBefore);
        terms.add(new FromStringTerm(pecHostAddressReturn));
        return terms;
    }

    private void processingMailFromHostPec(Folder folder, String userName, String password, Integer daysBefore, String filterOggetto) throws MessagingException {
        List<SearchTerm> terms = createTermsForSearchPec(daysBefore);
        Optional.ofNullable(filterOggetto)
                .ifPresent(s -> terms.add(new SubjectTerm(s)));
        Message[] messages = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
        logger.info("Recuperati " + messages.length + " messaggi PEC dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
        for (int i = 0; i < messages.length; i++) {
            try {
                Message message = messages[i];
                if (message.getSubject().contains(getSubjectTermForMancataConsegnaFatturaAttivaPec())) {
                    String nomeFile = message.getSubject().substring(getSubjectTermForMancataConsegnaFatturaAttivaPec().length() + 1);
                    if (nomeFile != null) {
                        InputStream streamSigned = trasmissioneFattureService.mancataConsegnaPecInvioFatturaAttiva(userContext, nomeFile);
                        if (streamSigned != null) {
                            File fileSigned = new File(System.getProperty("tmp.dir.SIGLAWeb") + "/tmp/", nomeFile);
                            OutputStream outputStream = new FileOutputStream(fileSigned);
                            IOUtils.copy(streamSigned, outputStream);
                            outputStream.close();
                            inviaFatturaElettronica(userName, password, fileSigned, nomeFile);
                        }
                    }
                } else if (message.getSubject().contains(getSubjectTermForFatturaAttivaPec())) {
                    String nomeFile = message.getSubject().substring(getSubjectTermForFatturaAttivaPec().length() + 1);
                    if (nomeFile != null) {
                        trasmissioneFattureService.notificaFatturaAttivaConsegnaPec(userContext, nomeFile, message.getSentDate());
                    }
                } else if (message.getSubject().contains(getSubjectTermForFatturaPassivaPec())) {
                    String[] headerMessage = getMessageID(message);
                    if (headerMessage != null) {
                        String messageID = Arrays.asList(headerMessage).toString();
                        String messageIDWithUser = userName + " " + messageID;
                        logger.info("MessageIDWithUser " + messageIDWithUser);
                        if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)) {
                            logger.info(messageIDWithUser);
                            String identificativoSdi = message.getSubject().substring(getSubjectTermForFatturaPassivaPec().length() + 1);
                            if (identificativoSdi != null) {
                                ricezioneFattureService.notificaFatturaPassivaConsegnaEsitoPec(identificativoSdi.trim(), message.getReceivedDate());
                                listaMessageIdAlreadyScanned.add(messageIDWithUser);
                                logger.info("Message Added");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("PEC scan error while updating send PEC. ", e);
            }
        }
    }

    private String[] getMessageID(Message message) throws MessagingException {
        String[] headerMessage = message.getHeader("Message-ID");
        return headerMessage;
    }

    public List<SearchTerm> createTermsForSearchPec(Integer daysBefore) {
        List<SearchTerm> terms = new ArrayList<SearchTerm>();
        addConditionDate(terms, daysBefore);
        addConditionFromAddress(terms);
        addConditionSubject(terms);
        return terms;
    }

    private void addConditionSubject(List<SearchTerm> terms) {
        List<SearchTerm> listSubject = new ArrayList<SearchTerm>();
        listSubject.add(new SubjectTerm(getSubjectTermForFatturaAttivaPec()));
        listSubject.add(new SubjectTerm(getSubjectTermForFatturaPassivaPec()));
        listSubject.add(new SubjectTerm(getSubjectTermForMancataConsegnaFatturaAttivaPec()));
        SearchTerm[] termsSubjects = listSubject.toArray(new SearchTerm[listSubject.size()]);
        terms.add(new OrTerm(termsSubjects));
    }

    private void addConditionFromAddress(List<SearchTerm> terms) {
        List<SearchTerm> listAddress = new ArrayList<SearchTerm>();
        for (String address : pecHostAddress) {
            listAddress.add(new FromStringTerm(address));
        }
        SearchTerm[] termsAddress = listAddress.toArray(new SearchTerm[listAddress.size()]);
        terms.add(new OrTerm(termsAddress));
    }

    public String getSubjectTermForFatturaAttivaPec() {
        String subjectTerm = pecSDISubjectNotificaPecTerm + " " + pecSDISubjectFatturaAttivaInvioTerm;
        return subjectTerm;
    }

    public String getSubjectTermForMancataConsegnaFatturaAttivaPec() {
        String subjectTerm = pecSDISubjectMancataConsegnaPecTerm + " " + pecSDISubjectFatturaAttivaInvioTerm;
        return subjectTerm;
    }

    public String getSubjectTermForFatturaPassivaPec() {
        String subjectTerm = pecSDISubjectNotificaPecTerm + " " + pecSDISubjectNotificaEsitoTerm;
        return subjectTerm;
    }

    public void addConditionDate(List<SearchTerm> terms, Integer daysBefore) {
        addConditionDate(terms, false, daysBefore);
    }

    public void addConditionDate(List<SearchTerm> terms, Boolean dateForRecoveryNotifier, Integer daysBefore) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Date dateFromSearch = null;
        if (dateForRecoveryNotifier) {
            dateFromSearch = DateUtils.dataContabile(cal.getTime(), cal.get(Calendar.YEAR) - 1);
            dateFromSearch = cal.getTime();
        } else {
            cal.add(Calendar.DATE, daysBefore);
            dateFromSearch = cal.getTime();
        }

        terms.add(new ReceivedDateTerm(ComparisonTerm.GE, dateFromSearch));
    }

    public Date getSystemDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        Date data = cal.getTime();
        return data;
    }

    @SuppressWarnings("unchecked")
    private boolean isBodyPartMetadati(BodyPart bodyPart) {
        try {
            JAXBElement<MetadatiInvioFileType> metadatiInvioFileType = (JAXBElement<MetadatiInvioFileType>)
                    fatturazioneElettronicaClient.getUnmarshaller().
                            unmarshal(new StreamSource(bodyPart.getInputStream()));
            if (logger.isDebugEnabled())
                logger.debug("metadatiInvioFileType :" + metadatiInvioFileType.getValue().getIdentificativoSdI());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(Message message) throws ComponentException {
        logger.info("Fatture Elettroniche: Attive: Inizio Avvenuta Trasmissione con impossibilità di recapito.");
        try {
            BodyPart bodyPartZip = estraiBodyPartZipNotificaFatturaAttiva(message);
            if (bodyPartZip != null) {
                logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
                ZipInputStream zis = new ZipInputStream(bodyPartZip.getInputStream());
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    System.out.println("entry: " + entry.getName() + ", " + entry.getSize());
                    if (entry.getName().toLowerCase().endsWith("xml")) {
                        String contentType = new MimetypesFileTypeMap().getContentType(entry.getName());
                        DataHandler dataHandler = createDataHandler(zis, contentType);
                        trasmissioneFattureService.notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(userContext, entry.getName(), dataHandler);
                        break;
                    }
                }

            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private void notificaFatturaAttivaDecorrenzaTermini(Message message) throws ComponentException {
        logger.info("Fatture Elettroniche: Inizio Notifica Decorrenza Termini.");
        try {
            BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
            if (bodyPartXml != null) {
                logger.info("Fatture Elettroniche: Decorrenza Termini. Estratto Body Part.");
                String fileName = extractFileName(bodyPartXml);
                logger.info("File Name:" + fileName);
                if (fileName.startsWith(docAmmFatturazioneElettronicaComponentSession.recuperoInizioNomeFile(userContext))) {
                    if (!trasmissioneFattureService.notificaFatturaAttivaDecorrenzaTermini(userContext, fileName, createDataHandler(bodyPartXml))) {
                        logger.info("Fatture Elettroniche: Decorrenza Termini. Incongruenza nel nome del file. Verifica se esistono fatture passive con l'ID.");
                        ricezioneFattureService.notificaDecorrenzaTermini(fileName, createDataHandler(bodyPartXml));
                    }
                } else {
                    ricezioneFattureService.notificaDecorrenzaTermini(fileName, createDataHandler(bodyPartXml));
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private DataHandler createDataHandler(BodyPart bodyPart)
            throws IOException, MessagingException {
        DataSource byteArrayDataSource = new UploadedFileDataSource(bodyPart);
        return new DataHandler(byteArrayDataSource);
    }

    private DataHandler createDataHandler(InputStream stream, String contentType)
            throws IOException, MessagingException {
        DataSource byteArrayDataSource = new UploadedFileDataSourceStream(stream, contentType);
        return new DataHandler(byteArrayDataSource);
    }

    public DataSource createDataSource(InputStream stream, String contentType)
            throws IOException, MessagingException {
        return new UploadedFileDataSourceStream(stream, contentType);
    }

    private void notificaFatturaAttivaEsito(Message message) throws ComponentException {
        logger.info("Fatture Elettroniche: Attive: Inizio Notifica Esito.");
        try {
            BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
            if (bodyPartXml != null) {
                trasmissioneFattureService.notificaFatturaAttivaEsito(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private void notificaFatturaAttivaScarto(Message message) throws ComponentException {
        logger.info("Fatture Elettroniche: Attive: Inizio Notifica Scarto.");
        try {
            BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
            if (bodyPartXml != null) {
                logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
                logger.info("File Name:" + bodyPartXml.getFileName());
                trasmissioneFattureService.notificaFatturaAttivaScarto(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private void notificaFatturaAttivaRicevutaConsegna(Message message, String username) throws ComponentException {
        logger.info("Fatture Elettroniche: Attive: Inizio Ricevuta Consegna.");
        try {
            BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
            if (bodyPartXml != null) {
                logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
                logger.info("File Name:" + bodyPartXml.getFileName());
                trasmissioneFattureService.notificaFatturaAttivaRicevutaConsegna(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
                String[] replies = message.getHeader("Reply-To");
                if (replies != null) {
                    String replyTo = replies[0];
                    logger.info("Reply To: " + replyTo + ". Username: " + username);
                }
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private void notificaFatturaAttivaMancataConsegna(Message message) throws ComponentException {
        logger.info("Fatture Elettroniche: Attive: Inizio Mancata Consegna.");
        try {
            BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
            if (bodyPartXml != null) {
                logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
                logger.info("File Name:" + bodyPartXml.getFileName());
                trasmissioneFattureService.notificaFatturaAttivaMancataConsegna(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
            }
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private BodyPart estraiBodyPartXmlNotificaFatturaAttiva(Message message) throws ComponentException {
        try {
            List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
            for (BodyPart bodyPart : bodyParts) {
                String fileName = extractFileName(bodyPart);

                if (fileName.toLowerCase().endsWith("xml")) {
                    return bodyPart;
                }
            }
            return null;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private BodyPart estraiBodyPartZipNotificaFatturaAttiva(Message message) throws ComponentException {
        try {
            List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
            for (BodyPart bodyPart : bodyParts) {
                String fileName = extractFileName(bodyPart);

                if (fileName.toLowerCase().endsWith("zip")) {
                    return bodyPart;
                }
            }
            return null;
        } catch (Exception e) {
            throw new ComponentException(e);
        }
    }

    private String extractFileName(BodyPart bodyPart) throws MessagingException,
            UnsupportedEncodingException {
        String fileName = bodyPart.getFileName();
        if (fileName != null && fileName.startsWith("=?") && fileName.endsWith("?=")) {
            fileName = MimeUtility.decodeText(fileName);
        }
        return fileName;
    }

    public void notificaEsito(String userName, String password, DocumentoEleTestataBulk bulk, JAXBElement<NotificaEsitoCommittenteType> notificaEsitoCommittenteType) throws EmailException, XmlMappingException, IOException {
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(userName, password);
        String replyTo = null;
        if (bulk.getDocumentoEleTrasmissione() != null) {
            replyTo = Optional.ofNullable(this.replyTo).filter(s -> !s.isEmpty()).orElse(bulk.getDocumentoEleTrasmissione().getReplyTo());
        }
        email.addTo(replyTo == null ? pecSDIAddress : replyTo, "SdI - Sistema Di Interscambio");
        email.setFrom(userName, userName);
        email.setSubject(" Notifica di esito " + bulk.getIdentificativoSdi());
        email.setMsg("Il file trasmesso con identificativo SdI:" + bulk.getIdentificativoSdi() +
                (bulk.isRifiutata() ? " è stato Rifiutato (EC02) poichè " + bulk.getMotivoRifiuto() : " è stato Accettato (EC01)") +
                ", in allegato la notifica di esito.");

        // add the attachment
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        fatturazioneElettronicaClient.getMarshaller().marshal(notificaEsitoCommittenteType, new StreamResult(outputStream));
        DataSource byteArrayDataSource = new UploadedFileDataSourceOutputStream(outputStream);
        email.attach(byteArrayDataSource, bulk.getNomeFile("EC"), "", EmailAttachment.ATTACHMENT);
        // send the email
        email.send();
        logger.info("Inviata notifica di esito per IdentificativoSdi:" + bulk.getIdentificativoSdi());
    }

    public void inviaFatturaElettronica(String userName, String password, File fatturaAttivaSigned, String idFattura) throws EmailException, XmlMappingException, IOException {
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(userName, password);
        email.addTo(pecSDIAddress, "SdI - Sistema Di Interscambio");
        email.setFrom(userName, userName);
        email.setSubject(pecSDISubjectFatturaAttivaInvioTerm + " " + idFattura);
        email.setMsg("Invio Fattura Elettronica. " + idFattura);
        email.attach(fatturaAttivaSigned);
        // send the email
        email.send();
    }

    public void inviaFatturaElettronica(String userName, String password, DataSource fatturaAttivaSigned, String idFattura) throws EmailException, XmlMappingException, IOException {
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(userName, password);
        email.addTo(pecSDIAddress, "SdI - Sistema Di Interscambio");
        email.setFrom(userName, userName);
        email.setSubject(pecSDISubjectFatturaAttivaInvioTerm + " " + idFattura);
        email.setMsg("Invio Fattura Elettronica. " + idFattura);
        email.attach(fatturaAttivaSigned, idFattura, "");
        // send the email
        email.send();
    }

    public void inviaPECFornitore(UserContext userContext, DataSource attach, String filename, String emailPEC, String subject, String msg) throws ComponentException, EmailException {
        Configurazione_cnrBulk emailPecSdi = fatturaElettronicaPassivaComponentSession.getEmailPecSdi(userContext, false);
        String userName = emailPecSdi.getVal01();
        String password = null;
        try {
            password = StringEncrypter.decrypt(userName, emailPecSdi.getVal02());
        } catch (EncryptionException e1) {
            new AuthenticationFailedException("Cannot decrypt password");
        }
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(userName, password);
        email.addTo(emailPEC);
        email.setFrom(userName, userName);
        email.setSubject(subject);
        email.setMsg(msg);
        email.attach(attach, filename, "");
        // send the email
        email.send();
    }

    private List<BodyPart> estraiBodyPart(Object content, Boolean perAllegati) throws MessagingException, IOException {
        List<BodyPart> results = new ArrayList<BodyPart>();
        estraiParte(content, results, perAllegati);
        return results;
    }

    private List<BodyPart> estraiBodyPart(Object content) throws MessagingException, IOException {
        return estraiBodyPart(content, true);
    }

    private void estraiParte(Object obj, List<BodyPart> bodyParts, Boolean perAllegati) throws MessagingException, IOException {
        if (obj instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) obj;
            for (int j = 0; j < multipart.getCount(); j++) {
                BodyPart bodyPart = multipart.getBodyPart(j);
                estraiParte(bodyPart.getContent(), bodyParts, perAllegati);
            }
        } else if (obj instanceof MimeBodyPart) {
            MimeBodyPart multipart = (MimeBodyPart) obj;
            estraiParte(multipart.getContent(), bodyParts, perAllegati);
        } else if (obj instanceof MimeMessage) {
            MimeMessage mimemessage = (MimeMessage) obj;
            if (!perAllegati && mimemessage.getSubject() != null && mimemessage.getSubject().contains(pecSDISubjectNotificaEsitoTerm)) {
                int partenza = mimemessage.getSubject().indexOf(pecSDISubjectNotificaEsitoTerm);
                String identificativoSdi = mimemessage.getSubject().substring(partenza + pecSDISubjectNotificaEsitoTerm.length() + 1);
                try {
                    ricezioneFattureService.notificaScartoMailNotificaNonRicevibile(mimemessage, identificativoSdi, getSystemDate());
                } catch (ComponentException e) {
                    logger.error("NotificaScartoMailNotificaNonRicevibile", e);
                }
            } else {
                forwardedEmail(mimemessage.getContent(), bodyParts, perAllegati);
            }
        }
    }

    private void forwardedEmail(Object obj, List<BodyPart> bodyParts, Boolean perAllegati) throws MessagingException, IOException {
        if (obj instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) obj;
            for (int j = 0; j < multipart.getCount(); j++) {
                BodyPart bodyPart = multipart.getBodyPart(j);
                if (perAllegati) {
                    String disposition = bodyPart.getDisposition();
                    if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
                        bodyParts.add(bodyPart);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Content type:" + bodyPart.getContentType());
                            logger.debug("File name:" + bodyPart.getFileName());
                        }
                    }
                } else {
                    if (bodyPart instanceof MimeBodyPart) {
                        MimeBodyPart body = (MimeBodyPart) bodyPart;
                        estraiParte(body.getContent(), bodyParts, perAllegati);
                    }
                }
            }
        }
    }

    public FatturaAttivaSingolaComponentSession getFatturaAttivaSingolaComponentSession() {
        return fatturaAttivaSingolaComponentSession;
    }

    public void setFatturaAttivaSingolaComponentSession(
            FatturaAttivaSingolaComponentSession fatturaAttivaSingolaComponentSession) {
        this.fatturaAttivaSingolaComponentSession = fatturaAttivaSingolaComponentSession;
    }

    class UploadedFileDataSourceStream implements DataSource {

        private final InputStream inputStream;
        private final String contentType;

        public UploadedFileDataSourceStream(InputStream inputStream, String contentType) {
            this.inputStream = inputStream;
            this.contentType = contentType;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, output);
            return output;
        }

        @Override
        public String getName() {
            return "DATASOURCE";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public String getContentType() {
            return contentType;
        }
    }

    class UploadedFileDataSourceOutputStream implements DataSource {

        private final OutputStream outputStream;

        public UploadedFileDataSourceOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return outputStream;
        }

        @Override
        public String getName() {
            return "DATASOURCE";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            if (outputStream != null) {
                ByteArrayOutputStream bos = (ByteArrayOutputStream) outputStream;
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream inStream = new ByteArrayInputStream(byteArray);
                return inStream;
            }
            return null;
        }

        @Override
        public String getContentType() {
            return "www/unknow";
        }
    }

    class UploadedFileDataSource implements DataSource {

        private final BodyPart bodypart;

        public UploadedFileDataSource(BodyPart bodypart) {
            this.bodypart = bodypart;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                IOUtils.copy(bodypart.getInputStream(), output);
            } catch (MessagingException e) {
                logger.error("UploadedFileDataSource::getOutputStream", e);
            }
            return output;
        }

        @Override
        public String getName() {
            try {
                return bodypart.getFileName();
            } catch (MessagingException e) {
                logger.error("UploadedFileDataSource::getName", e);
            }
            return null;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            try {
                return bodypart.getInputStream();
            } catch (MessagingException e) {
                logger.error("UploadedFileDataSource::getInputStream", e);
            }
            return null;
        }

        @Override
        public String getContentType() {
            try {
                return bodypart.getContentType();
            } catch (MessagingException e) {
                logger.error("UploadedFileDataSource::getContentType", e);
            }
            return null;
        }
    }
}