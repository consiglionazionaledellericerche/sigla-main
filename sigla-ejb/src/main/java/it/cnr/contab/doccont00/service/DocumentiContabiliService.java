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

package it.cnr.contab.doccont00.service;

import com.google.gson.GsonBuilder;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.doccont00.comp.DistintaCassiereComponent;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.ejb.MandatoComponentSession;
import it.cnr.contab.doccont00.ejb.ReversaleComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.Distinta_cassiereBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_ext_cassiere00Bulk;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.intcass.giornaliera.FlussoGiornaleDiCassaBulk;
import it.cnr.contab.doccont00.intcass.giornaliera.FlussoGiornaleDiCassaKey;
import it.cnr.contab.doccont00.intcass.giornaliera.InformazioniContoEvidenzaBulk;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.contab.siope.plus.bulk.SIOPEPlusEsitoBulk;
import it.cnr.contab.siope.plus.bulk.SIOPEPlusRisultatoBulk;
import it.cnr.contab.spring.service.UtilService;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.*;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.mail.SimplePECMail;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceClient;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.siopeplus.EsitoFlusso;
import it.cnr.si.siopeplus.MessaggiEsitoApplicativo;
import it.cnr.si.siopeplus.MessaggioRifiutoFlusso;
import it.cnr.si.siopeplus.giornaledicassa.FlussoGiornaleDiCassa;
import it.cnr.si.siopeplus.model.Esito;
import it.cnr.si.siopeplus.model.Lista;
import it.cnr.si.siopeplus.model.MessaggioXML;
import it.cnr.si.siopeplus.model.Risultato;
import it.cnr.si.siopeplus.service.GiornaleDiCassaSiopePlusService;
import it.cnr.si.siopeplus.service.OrdinativiSiopePlusService;
import it.cnr.si.spring.storage.*;
import it.cnr.si.spring.storage.bulk.StorageFile;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.siopeplus.*;
import it.siopeplus.giornaledicassa.InformazioniContoEvidenza;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.xml.sax.SAXParseException;

import javax.activation.DataSource;
import java.io.*;
import java.rmi.RemoteException;
import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentiContabiliService extends StoreService implements InitializingBean {
    public static final String SIOPEPLUS = "SIOPEPLUS";
    public static final String DISTINTA_PEC_PDF = "Distinta_PEC.pdf";

    private transient static final Logger logger = LoggerFactory.getLogger(DocumentiContabiliService.class);
    final String pattern = "dd MMMM YYYY' alle 'HH:mm:ss";
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    @Autowired
    private StorageDriver storageDriver;
    @Autowired
    private OrdinativiSiopePlusService ordinativiSiopePlusService;
    @Autowired
    private GiornaleDiCassaSiopePlusService giornaleDiCassaSiopePlusService;
    @Autowired
    private UtilService utilService;

    @Value("${sign.document.png.url}")
    private String signDocumentURL;
    @Value("${sign.documents.from.repository}")
    private boolean signDocumentsFromRepository;

    private ArubaSignServiceClient arubaSignServiceClient;
    private String pecHostName, pecMailFromBanca,
            pecMailFromBancaPassword, pecMailToBancaNoEuroSepa,
            pecMailToBancaItaliaF23F24, pecMailToBancaForStipendi;

    private CRUDComponentSession crudComponentSession;
    private MandatoComponentSession mandatoComponentSession;
    private ReversaleComponentSession reversaleComponentSession;
    private DistintaCassiereComponentSession distintaCassiereComponentSession;
    private Configurazione_cnrComponentSession configurazione_cnrComponentSession;

    private UserContext userContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.userContext = new WSUserContext(SIOPEPLUS, null,
                new Integer(Calendar.getInstance().get(Calendar.YEAR)),
                null, null, null);
        this.crudComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession"))
                .filter(CRUDComponentSession.class::isInstance)
                .map(CRUDComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb JADAEJB_CRUDComponentSession"));
        this.mandatoComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCCONT00_EJB_MandatoComponentSession"))
                .filter(MandatoComponentSession.class::isInstance)
                .map(MandatoComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCCONT00_EJB_MandatoComponentSession"));
        this.reversaleComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ReversaleComponentSession"))
                .filter(ReversaleComponentSession.class::isInstance)
                .map(ReversaleComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCCONT00_EJB_ReversaleComponentSession"));
        this.distintaCassiereComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRDOCCONT00_EJB_DistintaCassiereComponentSession"))
                .filter(DistintaCassiereComponentSession.class::isInstance)
                .map(DistintaCassiereComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRDOCCONT00_EJB_DistintaCassiereComponentSession"));
        this.configurazione_cnrComponentSession = Optional.ofNullable(EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession"))
                .filter(Configurazione_cnrComponentSession.class::isInstance)
                .map(Configurazione_cnrComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRCONFIG00_EJB_Configurazione_cnrComponentSession"));
    }

    public ArubaSignServiceClient getArubaSignServiceClient() {
        return arubaSignServiceClient;
    }

    public void setArubaSignServiceClient(
            ArubaSignServiceClient arubaSignServiceClient) {
        this.arubaSignServiceClient = arubaSignServiceClient;
    }

    public String getPecHostName() {
        return pecHostName;
    }

    public void setPecHostName(String pecHostName) {
        this.pecHostName = pecHostName;
    }

    public String getPecMailFromBanca() {
        return pecMailFromBanca;
    }

    public void setPecMailFromBanca(String pecMailFromBanca) {
        this.pecMailFromBanca = pecMailFromBanca;
    }

    public String getPecMailFromBancaPassword() {
        return pecMailFromBancaPassword;
    }

    public void setPecMailFromBancaPassword(String pecMailFromBancaPassword) {
        this.pecMailFromBancaPassword = pecMailFromBancaPassword;
    }

    public String getPecMailToBancaNoEuroSepa() {
        return pecMailToBancaNoEuroSepa;
    }

    public void setPecMailToBancaNoEuroSepa(String pecMailToBancaNoEuroSepa) {
        this.pecMailToBancaNoEuroSepa = pecMailToBancaNoEuroSepa;
    }

    public String getPecMailToBancaItaliaF23F24() {
        return pecMailToBancaItaliaF23F24;
    }

    public void setPecMailToBancaItaliaF23F24(String pecMailToBancaItaliaF23F24) {
        this.pecMailToBancaItaliaF23F24 = pecMailToBancaItaliaF23F24;
    }

    public String getPecMailToBancaForStipendi() {
        return pecMailToBancaForStipendi;
    }

    public void setPecMailToBancaForStipendi(String pecMailToBancaForStipendi) {
        this.pecMailToBancaForStipendi = pecMailToBancaForStipendi;
    }

    public String getDocumentKey(StatoTrasmissione bulk) {
        return getDocumentKey(bulk, false);
    }

    public String getDocumentKey(StatoTrasmissione bulk, boolean fullNodeRef) {
        return Optional.ofNullable(bulk)
                .map(statoTrasmissione -> Optional.ofNullable(getStorageObjectByPath(statoTrasmissione.getStorePath()
                        .concat(storageDriver.SUFFIX)
                        .concat(statoTrasmissione.getCMISName())))
                        .map(storageObject ->
                                fullNodeRef ? Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()))
                                        .map(String.class::cast)
                                        .orElse(storageObject.getKey()) : storageObject.getKey())
                        .orElse(null)).orElse(null);
    }

    public InputStream getStreamDocumento(StatoTrasmissione bulk) {
        return Optional.ofNullable(getStorageObjectByPath(bulk.getStorePath().concat(storageDriver.SUFFIX).concat(bulk.getCMISName())))
                .map(StorageObject::getKey)
                .map(key -> getResource(key))
                .orElse(null);
    }

    public Map<String, String> getCertSubjectDN(String username, String password) throws Exception {
        try {
            Map<String, String> result = new HashMap<String, String>();
            Principal principal = arubaSignServiceClient.getUserCertSubjectDN(username, password);
            if (principal == null)
                return null;
            String[] subjectArray = principal.toString().split(",");
            for (String s : subjectArray) {
                String[] str = s.trim().split("=");
                String key = str[0];
                String value = str[1];
                result.put(key, value);
            }
            return result;
        } catch (Exception _ex) {
            logger.error("ERROR on ARUBA Client", _ex);
            java.io.StringWriter sw = new java.io.StringWriter();
            _ex.printStackTrace(new java.io.PrintWriter(sw));
            SendMail.sendErrorMail("ERROR on ARUBA Client", sw.toString());
            throw new ApplicationException("Si è verificato un errore nel recupero delle informazioni per eseguire la Firma Remota, riprovare successivamente.");
        }
    }

    public void controllaCodiceFiscale(Map<String, String> subjectDN, UtenteBulk utente) throws Exception {
        String codiceFiscale = subjectDN.get("SERIALNUMBER");
        if (Optional.ofNullable(utente.getCodiceFiscaleLDAP())
                .map(codiceFiscaleLDAP -> !codiceFiscale.contains(codiceFiscaleLDAP))
                .orElse(Boolean.FALSE)) {
            throw new ApplicationException("Il codice fiscale \"" + codiceFiscale + "\" presente sul certicato di Firma, " +
                    "è diverso da quello dell'utente collegato \"" + utente.getCodiceFiscaleLDAP() + "\"!");
        }
    }

    public void controllaCodiceFiscale(String username, String password, UtenteBulk utente) throws Exception {
        Map<String, String> subjectDN = Optional.ofNullable(getCertSubjectDN(username, password))
                .orElseThrow(() -> new ApplicationException("Errore nella lettura dei certificati!\nVerificare Nome Utente e Password!"));
        controllaCodiceFiscale(subjectDN, utente);
    }

    public void inviaDistintaPEC1210(List<String> nodes) throws EmailException, ApplicationException, IOException {
        inviaDistintaPEC1210(nodes, true, null);
    }

    public void inviaDistintaPEC1210(List<String> nodes, boolean isNoEuroOrSepa, String nrDistinta) throws EmailException, ApplicationException, IOException {
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
        String subject = "";
        if (isNoEuroOrSepa) {
            subject += "Bonifici Estero - ";
            if (pecMailToBancaNoEuroSepa != null && pecMailToBancaNoEuroSepa.split(";").length != 0) {
                email.addTo(pecMailToBancaNoEuroSepa.split(";"));
            } else {
                email.addTo(pecMailToBancaNoEuroSepa, pecMailToBancaNoEuroSepa);
            }
        } else {
            if (pecMailToBancaItaliaF23F24 != null && pecMailToBancaItaliaF23F24.split(";").length != 0) {
                email.addTo(pecMailToBancaItaliaF23F24.split(";"));
            } else {
                email.addTo(pecMailToBancaItaliaF23F24, pecMailToBancaItaliaF23F24);
            }
        }

        email.setFrom(pecMailFromBanca, pecMailFromBanca);
        subject +="Invio Distinta 1210";
        if (nrDistinta != null)
            subject +=" " + nrDistinta;

        email.setSubject(subject);

        // add the attachment
        for (String key : nodes) {
            StorageObject storageObject = getStorageObjectBykey(key);
            StorageDataSource dataSource = new StorageDataSource(storageObject);
            email.attach(dataSource, dataSource.getName(), "", EmailAttachment.ATTACHMENT);
        }
        // send the email
        email.send();
        logger.debug("Inviata distinta PEC");
    }

    public void inviaDistintaPEC(List<String> nodes, boolean isNoEuroOrSepa, String nrDistinta, boolean isDistintaStipendi) throws EmailException, ApplicationException, IOException {
        // Create the email message
        SimplePECMail email = utilService.createSimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
        String subject = "";
        if (isDistintaStipendi) {
            email.addTo(
                    Optional.ofNullable(pecMailToBancaForStipendi)
                            .map(s -> s.split(";"))
                            .filter(strings -> strings.length != 0)
                            .orElse(new String[]{
                                    pecMailToBancaForStipendi
                            }));
        } else {
            if (isNoEuroOrSepa) {
                subject += "Bonifici Estero - ";
                if (pecMailToBancaNoEuroSepa != null && pecMailToBancaNoEuroSepa.split(";").length != 0) {
                    email.addTo(pecMailToBancaNoEuroSepa.split(";"));
                } else {
                    email.addTo(pecMailToBancaNoEuroSepa, pecMailToBancaNoEuroSepa);
                }
            } else {
                if (pecMailToBancaItaliaF23F24 != null && pecMailToBancaItaliaF23F24.split(";").length != 0) {
                    email.addTo(pecMailToBancaItaliaF23F24.split(";"));
                } else {
                    email.addTo(pecMailToBancaItaliaF23F24, pecMailToBancaItaliaF23F24);
                }
            }
        }
        email.setFrom(pecMailFromBanca, pecMailFromBanca);
        subject += (nrDistinta != null ? nrDistinta : "Invio Distinta e Documenti");

        email.setSubject(subject);
        email.setMsg("In allegato i documenti");
        // add the attachment
        for (String key : nodes) {
            StorageObject storageObject = getStorageObjectBykey(key);
            StorageDataSource dataSource = new StorageDataSource(storageObject);
            email.attach(dataSource, dataSource.getName(), "", EmailAttachment.ATTACHMENT);
        }
        // send the email
        email.send();
        logger.debug("Inviata distinta PEC");
    }

    public void inviaDistintaPEC(List<String> nodes, boolean isNoEuroOrSepa, String nrDistinta) throws EmailException, ApplicationException, IOException {
        inviaDistintaPEC(nodes, isNoEuroOrSepa, nrDistinta, Boolean.FALSE);
    }

    public Set<String> getAllegatoForModPag(String key, String modPag) {
        return getChildren(key).stream()
                .filter(storageObject -> modPag.equals(storageObject.getPropertyValue("doccont:rif_modalita_pagamento")))
                .map(StorageObject::getKey)
                .collect(Collectors.toSet());
    }

    public String signDocuments(PdfSignApparence pdfSignApparence, String url) throws StorageException {
        if (storageDriver.getStoreType().equals(StorageDriver.StoreType.CMIS) && signDocumentsFromRepository) {
            return signDocuments(new GsonBuilder().create().toJson(pdfSignApparence), url);
        } else {
            List<byte[]> bytes = Optional.ofNullable(pdfSignApparence)
                    .map(pdfSignApparence1 -> pdfSignApparence1.getNodes())
                    .map(list ->
                            list.stream()
                                    .map(s -> storageDriver.getInputStream(s))
                                    .map(inputStream -> {
                                        try {
                                            return IOUtils.toByteArray(inputStream);
                                        } catch (IOException e) {
                                            throw new StorageException(StorageException.Type.GENERIC, e);
                                        }
                                    })
                                    .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            try {
                it.cnr.si.firmadigitale.firma.arss.stub.PdfSignApparence apparence = new it.cnr.si.firmadigitale.firma.arss.stub.PdfSignApparence();
                apparence.setImage(signDocumentURL);
                apparence.setLeftx(pdfSignApparence.getApparence().getLeftx());
                apparence.setLefty(pdfSignApparence.getApparence().getLefty());
                apparence.setLocation(pdfSignApparence.getApparence().getLocation());
                apparence.setPage(pdfSignApparence.getApparence().getPage());
                apparence.setReason(pdfSignApparence.getApparence().getReason());
                apparence.setRightx(pdfSignApparence.getApparence().getRightx());
                apparence.setRighty(pdfSignApparence.getApparence().getRighty());
                apparence.setTesto(pdfSignApparence.getApparence().getTesto());

                List<byte[]> bytesSigned = arubaSignServiceClient.pdfsignatureV2Multiple(
                        pdfSignApparence.getUsername(),
                        pdfSignApparence.getPassword(),
                        pdfSignApparence.getOtp(),
                        bytes,
                        apparence
                );
                for (int i = 0; i < pdfSignApparence.getNodes().size(); i++) {
                    final byte[] bytes1 = Optional.ofNullable(bytesSigned.get(i))
                                    .orElseThrow(() -> new StorageException(StorageException.Type.GENERIC, "0011", new ApplicationException()));
                    storageDriver.updateStream(
                            pdfSignApparence.getNodes().get(i),
                            new ByteArrayInputStream(bytes1),
                            MimeTypes.PDF.mimetype()
                    );
                }

            } catch (ArubaSignServiceException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            }
            return null;
        }
    }

    public String signDocuments(SignP7M signP7M, String url, String path) throws StorageException {
        if (storageDriver.getStoreType().equals(StorageDriver.StoreType.CMIS) && signDocumentsFromRepository) {
            return signDocuments(new GsonBuilder().create().toJson(signP7M), url);
        } else {
            StorageObject storageObject = storageDriver.getObject(signP7M.getNodeRefSource());
            try {
                final byte[] bytes = arubaSignServiceClient.pkcs7SignV2(
                        signP7M.getUsername(),
                        signP7M.getPassword(),
                        signP7M.getOtp(),
                        IOUtils.toByteArray(storageDriver.getInputStream(signP7M.getNodeRefSource())));
                Map<String, Object> metadataProperties = new HashMap<>();
                metadataProperties.put(StoragePropertyNames.NAME.value(), signP7M.getNomeFile());
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), SIGLAStoragePropertyNames.CNR_ENVELOPEDDOCUMENT.value());

                return storeSimpleDocument(
                        new ByteArrayInputStream(bytes),
                        MimeTypes.P7M.mimetype(),
                        path,
                        metadataProperties).getKey();
            } catch (ArubaSignServiceException | IOException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            } finally {
                List<String> aspects = storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                aspects.add("P:cnr:signedDocument");
                updateProperties(Collections.singletonMap(
                        StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                        aspects
                ), storageObject);
            }
        }
    }

    private String signDocuments(String json, String url) throws StorageException {
        return storageDriver.signDocuments(json, url);
    }

    private Distinta_cassiereBulk fetchDistinta_cassiereBulk(String identificativoFlusso) throws RemoteException, ComponentException {
        Distinta_cassiereBulk distinta = Optional.ofNullable(Distinta_cassiereBulk.fromIdentificativoFlusso(identificativoFlusso))
                .orElseThrow(() -> new ApplicationMessageFormatException("Distinta non trovata per identificativo flusso: {0}", identificativoFlusso));
        final List<Distinta_cassiereBulk> findDistintaCasserie = crudComponentSession.find(userContext, Distinta_cassiereBulk.class, "findDistintaCasserie", distinta);
        return findDistintaCasserie.stream()
                .findFirst()
                .orElseThrow(() -> new ApplicationMessageFormatException("Distinta non trovata per identificativo flusso: {0}", identificativoFlusso));
    }

    private MandatoBulk fetchMandatoBulk(CtEsitoMandati ctEsitoMandati) {
        MandatoBulk mandato = new MandatoIBulk();
        mandato.setEsercizio(ctEsitoMandati.getEsercizio());
        mandato.setPg_mandato(ctEsitoMandati.getNumeroMandato().longValue());
        try {
            final List<MandatoBulk> findMandato = crudComponentSession.find(userContext, MandatoIBulk.class, "findMandato", mandato);
            return findMandato.stream()
                    .findFirst()
                    .orElseThrow(() -> new ApplicationMessageFormatException("Mandato non trovato per esito flusso esercizio: {0} numero: {1}",
                            ctEsitoMandati.getEsercizio(), ctEsitoMandati.getNumeroMandato()));

        } catch (ComponentException | RemoteException _ex) {
            throw new DetailedRuntimeException(_ex);
        }
    }


    private ReversaleBulk fetchReversaleBulk(CtEsitoReversali ctEsitoReversali) {
        ReversaleBulk reversale = new ReversaleIBulk();
        reversale.setEsercizio(ctEsitoReversali.getEsercizio());
        reversale.setPg_reversale(ctEsitoReversali.getNumeroReversale().longValue());
        try {
            final List<ReversaleBulk> findReversale = crudComponentSession.find(userContext, ReversaleIBulk.class, "findReversale", reversale);
            return findReversale.stream()
                    .findFirst()
                    .orElseThrow(() -> new ApplicationMessageFormatException("Reversale non trovata per esito flusso esercizio: {0} numero: {1}",
                            ctEsitoReversali.getEsercizio(), ctEsitoReversali.getNumeroReversale()));
        } catch (ComponentException | RemoteException _ex) {
            throw new DetailedRuntimeException(_ex);
        }
    }

    private FlussoGiornaleDiCassaBulk messaggioGiornaleDiCassa(Risultato risultato) throws RemoteException, ComponentException {
        final MessaggioXML<FlussoGiornaleDiCassa> messaggioXML = giornaleDiCassaSiopePlusService.getLocation(risultato.getLocation(), FlussoGiornaleDiCassa.class);
        final FlussoGiornaleDiCassa flussoGiornaleDiCassa = messaggioXML.getObject();
        logger.info("Identificativo flusso BT: {}", flussoGiornaleDiCassa.getIdentificativoFlussoBT());
        String identificativoFlusso = Optional.ofNullable(flussoGiornaleDiCassa.getIdentificativoFlussoBT())
                .map(s -> s.substring(0, s.indexOf("#")))
                .orElseThrow(() -> new ApplicationMessageFormatException("IdentificativoFlusso non trovato per location {0}", risultato.getLocation()));
        return messaggioGiornaleDiCassa(flussoGiornaleDiCassa, identificativoFlusso);
    }

    public FlussoGiornaleDiCassaBulk messaggioGiornaleDiCassa(FlussoGiornaleDiCassa flussoGiornaleDiCassa, String identificativoFlusso) throws RemoteException, ComponentException {
        final FlussoGiornaleDiCassaBulk flussoGiornaleDiCassaBulk = Optional.ofNullable(
                crudComponentSession.findByPrimaryKey(
                        userContext,
                        new FlussoGiornaleDiCassaBulk(flussoGiornaleDiCassa.getEsercizio(), identificativoFlusso)
                ))
                .filter(FlussoGiornaleDiCassaBulk.class::isInstance)
                .map(FlussoGiornaleDiCassaBulk.class::cast)
                .orElseGet(() -> {
                    FlussoGiornaleDiCassaBulk flusso = new FlussoGiornaleDiCassaBulk(flussoGiornaleDiCassa.getEsercizio(), identificativoFlusso);
                    flusso.setUser(userContext.getUser());
                    flusso.setCodiceAbiBt(new Long(flussoGiornaleDiCassa.getTestataMessaggio().getCodiceABIBT()));
                    flusso.setDataOraCreazioneFlusso(new Timestamp(flussoGiornaleDiCassa.getTestataMessaggio().getDataOraCreazioneFlusso().toGregorianCalendar().getTime().getTime()));
                    flusso.setDataInizioPeriodoRif(
                            Optional.ofNullable(flussoGiornaleDiCassa.getDataRiferimentoGdC())
                                .map(xmlGregorianCalendar -> new Timestamp(xmlGregorianCalendar.toGregorianCalendar().getTime().getTime()))
                                .orElse(null)
                    );
                    flusso.setDataFinePeriodoRif(
                            Optional.ofNullable(flussoGiornaleDiCassa.getDataRiferimentoGdC())
                                    .map(xmlGregorianCalendar -> new Timestamp(xmlGregorianCalendar.toGregorianCalendar().getTime().getTime()))
                                    .orElse(null)
                    );
                    flusso.setCodiceEnte(flussoGiornaleDiCassa.getTestataMessaggio().getCodiceEnte());
                    flusso.setDescrizioneEnte(flussoGiornaleDiCassa.getTestataMessaggio().getDescrizioneEnte());
                    flusso.setCodiceEnteBt(flussoGiornaleDiCassa.getTestataMessaggio().getCodiceEnteBT());
                    flusso.setToBeCreated();
                    return flusso;
                });
        for (InformazioniContoEvidenza informazioniContoEvidenza : flussoGiornaleDiCassa.getInformazioniContoEvidenza()) {
            InformazioniContoEvidenzaBulk infoBulk = Optional.ofNullable(
                    crudComponentSession.findByPrimaryKey(userContext, new InformazioniContoEvidenzaBulk(
                            flussoGiornaleDiCassa.getEsercizio(),
                            identificativoFlusso,
                            informazioniContoEvidenza.getContoEvidenza())
                    ))
                    .filter(InformazioniContoEvidenzaBulk.class::isInstance)
                    .map(InformazioniContoEvidenzaBulk.class::cast)
                    .orElseGet(() -> {
                        InformazioniContoEvidenzaBulk informazioniContoEvidenzaBulk = new InformazioniContoEvidenzaBulk(
                                flussoGiornaleDiCassa.getEsercizio(),
                                identificativoFlusso,
                                informazioniContoEvidenza.getContoEvidenza());
                        informazioniContoEvidenzaBulk.setDescrizioneContoEvidenza(informazioniContoEvidenza.getDescrizioneContoEvidenza());
                        informazioniContoEvidenzaBulk.setSaldoPrecedenteContoEvid(informazioniContoEvidenza.getSaldoPrecedenteContoEvidenza());
                        informazioniContoEvidenzaBulk.setTotaleEntrateContoEvidenza(informazioniContoEvidenza.getTotaleEntrateContoEvidenza());
                        informazioniContoEvidenzaBulk.setTotaleUsciteContoEvidenza(informazioniContoEvidenza.getTotaleUsciteContoEvidenza());
                        informazioniContoEvidenzaBulk.setSaldoFinaleContoEvidenza(informazioniContoEvidenza.getSaldoFinaleContoEvidenza());
                        informazioniContoEvidenzaBulk.setToBeCreated();
                        return informazioniContoEvidenzaBulk;
                    });


            AtomicInteger index = new AtomicInteger();
            final MovimentoContoEvidenzaBulk movimentoContoEvidenzaBulk = new MovimentoContoEvidenzaBulk();
            movimentoContoEvidenzaBulk.setEsercizio(flussoGiornaleDiCassa.getEsercizio());
            movimentoContoEvidenzaBulk.setIdentificativoFlusso(identificativoFlusso);
            index.set(
                    distintaCassiereComponentSession.findMaxMovimentoContoEvidenza(
                            userContext,
                            movimentoContoEvidenzaBulk
                    ).intValue()
            );
            informazioniContoEvidenza.getMovimentoContoEvidenza()
                    .stream()
                    .forEach(movimentoContoEvidenza -> {
                        MovimentoContoEvidenzaBulk movBulk = new MovimentoContoEvidenzaBulk(
                                flussoGiornaleDiCassa.getEsercizio(),
                                identificativoFlusso,
                                informazioniContoEvidenza.getContoEvidenza(),
                                "I",
                                new Long(index.incrementAndGet()));
                        movBulk.setTipoMovimento(movimentoContoEvidenza.getTipoMovimento());
                        movBulk.setTipoDocumento(movimentoContoEvidenza.getTipoDocumento());
                        movBulk.setTipoOperazione(movimentoContoEvidenza.getTipoOperazione());
                        movBulk.setTiPagamentoFunzDelegato(movimentoContoEvidenza.getTipologiaPagamentoFunzionarioDelegato());
                        movBulk.setNumPagFunzDelegato(movimentoContoEvidenza.getNumeroPagamentoFunzionarioDelegato());
                        movBulk.setNumeroDocumento(movimentoContoEvidenza.getNumeroDocumento());
                        movBulk.setProgressivoDocumento(movimentoContoEvidenza.getProgressivoDocumento().longValue());
                        movBulk.setImporto(movimentoContoEvidenza.getImporto());
                        movBulk.setImportoRitenute(movimentoContoEvidenza.getImportoRitenute());
                        if (movimentoContoEvidenza.getNumeroBollettaQuietanza() != null)
                            movBulk.setNumeroBollettaQuietanza(movimentoContoEvidenza.getNumeroBollettaQuietanza().toString());
                        if (movimentoContoEvidenza.getNumeroBollettaQuietanzaStorno() != null)
                            movBulk.setNumeroBollettaQuietanzaS(movimentoContoEvidenza.getNumeroBollettaQuietanzaStorno().toString());
                        movBulk.setDataMovimento(new Timestamp(movimentoContoEvidenza.getDataMovimento().toGregorianCalendar().getTime().getTime()));
                        if (movimentoContoEvidenza.getDataValutaEnte() != null)
                            movBulk.setDataValutaEnte(new Timestamp(movimentoContoEvidenza.getDataValutaEnte().toGregorianCalendar().getTime().getTime()));
                        movBulk.setTipoEsecuzione(movimentoContoEvidenza.getTipoEsecuzione());
                        movBulk.setCoordinate(movimentoContoEvidenza.getCoordinate());
                        movBulk.setCodiceRifOperazione(movimentoContoEvidenza.getCodiceRiferimentoOperazione());
                        movBulk.setCodiceRifInterno(movimentoContoEvidenza.getCodiceRiferimentoInterno());
                        movBulk.setTipoContabilita(movimentoContoEvidenza.getTipoContabilita());
                        movBulk.setDestinazione(movimentoContoEvidenza.getDestinazione());
                        movBulk.setAssoggettamentoBollo(movimentoContoEvidenza.getAssoggettamentoBollo());
                        movBulk.setImportoBollo(movimentoContoEvidenza.getImportoBollo());
                        movBulk.setAssoggettamentoSpese(movimentoContoEvidenza.getAssoggettamentoSpese());
                        movBulk.setImportoSpese(movimentoContoEvidenza.getImportoSpese());
                        movBulk.setAssoggettamentoCommissioni(movimentoContoEvidenza.getAssoggettamentoCommissioni());
                        movBulk.setImportoCommissioni(movimentoContoEvidenza.getImportoCommissioni());
                        if (movimentoContoEvidenza.getCliente() != null) {
                            movBulk.setAnagraficaCliente(movimentoContoEvidenza.getCliente().getAnagraficaCliente());
                            movBulk.setIndirizzoCliente(movimentoContoEvidenza.getCliente().getIndirizzoCliente());
                            movBulk.setCapCliente(movimentoContoEvidenza.getCliente().getCapCliente());
                            movBulk.setLocalitaCliente(movimentoContoEvidenza.getCliente().getLocalitaCliente());
                            movBulk.setProvinciaCliente(movimentoContoEvidenza.getCliente().getProvinciaCliente());
                            movBulk.setStatoCliente(movimentoContoEvidenza.getCliente().getStatoCliente());
                            movBulk.setPartitaIvaCliente(movimentoContoEvidenza.getCliente().getPartitaIvaCliente());
                            movBulk.setCodiceFiscaleCliente(movimentoContoEvidenza.getCliente().getCodiceFiscaleCliente());
                        }
                        if (movimentoContoEvidenza.getDelegato() != null) {
                            movBulk.setAnagraficaDelegato(movimentoContoEvidenza.getDelegato().getAnagraficaDelegato());
                            movBulk.setIndirizzoDelegato(movimentoContoEvidenza.getDelegato().getIndirizzoDelegato());
                            movBulk.setCapDelegato(movimentoContoEvidenza.getDelegato().getCapDelegato());
                            movBulk.setLocalitaDelegato(movimentoContoEvidenza.getDelegato().getLocalitaDelegato());
                            movBulk.setProvinciaDelegato(movimentoContoEvidenza.getDelegato().getProvinciaDelegato());
                            movBulk.setStatoDelegato(movimentoContoEvidenza.getDelegato().getStatoDelegato());
                            movBulk.setCodiceFiscaleDelegato(movimentoContoEvidenza.getDelegato().getCodiceFiscaleDelegato());
                        }
                        if (movimentoContoEvidenza.getCreditoreEffettivo() != null) {
                            movBulk.setAnagraficaCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getAnagraficaCreditoreEffettivo());
                            movBulk.setIndirizzoCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getIndirizzoCreditoreEffettivo());
                            movBulk.setCapCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getCapCreditoreEffettivo());
                            movBulk.setLocalitaCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getLocalitaCreditoreEffettivo());
                            movBulk.setProvinciaCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getProvinciaCreditoreEffettivo());
                            movBulk.setStatoCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getStatoCreditoreEffettivo());
                            movBulk.setPartitaIvaCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getPartitaIvaCreditoreEffettivo());
                            movBulk.setCodiceFiscaleCreditoreEff(movimentoContoEvidenza.getCreditoreEffettivo().getCodiceFiscaleCreditoreEffettivo());
                        }
                        movBulk.setCausale(movimentoContoEvidenza.getCausale());
                        movBulk.setNumeroSospeso(
                                movimentoContoEvidenza.getSospeso().stream()
                                    .map(InformazioniContoEvidenza.MovimentoContoEvidenza.Sospeso::getNumeroProvvisorio)
                                    .findAny().orElse(null)
                        );
                        movBulk.setToBeCreated();
                        infoBulk.addToMovConto(movBulk);
                    });
            flussoGiornaleDiCassaBulk.addToInfoConto(infoBulk);

        }
        flussoGiornaleDiCassaBulk.setSaldoComplessivoPrec(flussoGiornaleDiCassa.getSaldoComplessivoPrecedente());
        flussoGiornaleDiCassaBulk.setTotaleComplessivoEntrate(flussoGiornaleDiCassa.getTotaleComplessivoEntrate());
        flussoGiornaleDiCassaBulk.setTotaleComplessivoUscite(flussoGiornaleDiCassa.getTotaleComplessivoUscite());
        flussoGiornaleDiCassaBulk.setSaldoComplessivoFinale(flussoGiornaleDiCassa.getSaldoComplessivoFinale());
        if (flussoGiornaleDiCassa.getTotaliEsercizio() != null) {
            flussoGiornaleDiCassaBulk.setFondoDiCassa(flussoGiornaleDiCassa.getTotaliEsercizio().getFondoDiCassa());
            flussoGiornaleDiCassaBulk.setTotaleReversaliRiscosse(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleReversaliRiscosse());
            flussoGiornaleDiCassaBulk.setTotaleSospesiEntrata(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleSospesiEntrata());
            flussoGiornaleDiCassaBulk.setTotaleEntrate(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleEntrate());
            flussoGiornaleDiCassaBulk.setDeficitDiCassa(flussoGiornaleDiCassa.getTotaliEsercizio().getDeficitDiCassa());
            flussoGiornaleDiCassaBulk.setTotaleMandatiPagati(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleMandatiPagati());
            flussoGiornaleDiCassaBulk.setTotaleSospesiUscita(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleSospesiUscita());
            flussoGiornaleDiCassaBulk.setTotaleUscite(flussoGiornaleDiCassa.getTotaliEsercizio().getTotaleUscite());
            flussoGiornaleDiCassaBulk.setSaldoEsercizio(flussoGiornaleDiCassa.getTotaliEsercizio().getSaldoEsercizio());
        }
        if (flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide() != null) {
            flussoGiornaleDiCassaBulk.setSaldoContiCorrenti(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getSaldoContiCorrenti());
            flussoGiornaleDiCassaBulk.setSaldoContiBi(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getSaldoContiBI());
            flussoGiornaleDiCassaBulk.setTotaleConti(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getTotaleConti());
            flussoGiornaleDiCassaBulk.setVincoliContiCorrenti(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getVincoliContiCorrenti());
            flussoGiornaleDiCassaBulk.setVincoliContiBi(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getVincoliContiBI());
            flussoGiornaleDiCassaBulk.setTotaleVincoli(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getTotaleVincoli());
            flussoGiornaleDiCassaBulk.setAnticipazioneAccordata(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getAnticipazioneAccordata());
            flussoGiornaleDiCassaBulk.setAnticipazioneUtilizzata(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getAnticipazioneUtilizzata());
            flussoGiornaleDiCassaBulk.setDisponibilita(flussoGiornaleDiCassa.getTotaliDisponibilitaLiquide().getDisponibilita());
        }
        if (flussoGiornaleDiCassaBulk.isToBeCreated()) {
            crudComponentSession.creaConBulk(userContext, flussoGiornaleDiCassaBulk);
        } else {
            crudComponentSession.modificaConBulk(userContext, flussoGiornaleDiCassaBulk);
        }
        return flussoGiornaleDiCassaBulk;
    }

    private void messaggioACK(Risultato risultato) throws RemoteException, ComponentException {
        final MessaggioXML<MessaggioAckSiope> messaggioXML = ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggioAckSiope.class);
        final MessaggioAckSiope messaggioAckSiope = messaggioXML.getObject();
        logger.info("Identificativo flusso: {}", messaggioAckSiope.getIdentificativoFlusso());
        Distinta_cassiereBulk distinta = fetchDistinta_cassiereBulk(messaggioAckSiope.getIdentificativoFlusso());
        /**
         * Carico il file del messaggio
         */
        StorageFile storageFile = new StorageFile(messaggioXML.getContent(), MimeTypes.XML.mimetype(), messaggioXML.getName());
        storageFile.setTitle("Acquisito il " + formatter.format(risultato.getDataProduzione().toInstant().atZone(ZoneId.systemDefault())));

        final Integer progFlusso = risultato.getProgFlusso();
        StringBuffer description = new StringBuffer();
        switch (messaggioAckSiope.getStatoFlusso()) {
            case WARNING: {
                description.append("WARNING:\n");
                messaggioAckSiope.getWarning()
                        .stream()
                        .map(ctErroreACK -> ctErroreACK.getDescrizione().concat(" - ").concat(ctErroreACK.getElemento()))
                        .peek(logger::warn)
                        .forEach(s -> {
                            description.append(s.concat("\n"));
                        });
                Optional.ofNullable(distinta.getStato())
                        .filter(s -> s.equals(Distinta_cassiereBulk.Stato.TRASMESSA.value()))
                        .ifPresent(s -> distinta.setStato(Distinta_cassiereBulk.Stato.ACCETTATO_SIOPEPLUS));
                break;
            }
            case OK: {
                Optional.ofNullable(distinta.getStato())
                        .filter(s -> s.equals(Distinta_cassiereBulk.Stato.TRASMESSA.value()))
                        .ifPresent(s -> distinta.setStato(Distinta_cassiereBulk.Stato.ACCETTATO_SIOPEPLUS));
                break;
            }
            case KO: {
                description.append("ERROR:\n");
                messaggioAckSiope.getErrore()
                        .stream()
                        .map(ctErroreACK -> ctErroreACK.getDescrizione().concat(" - ").concat(ctErroreACK.getElemento()))
                        .peek(logger::error)
                        .forEach(s -> {
                            description.append(s.concat("\n"));
                        });
                distinta.setStato(Distinta_cassiereBulk.Stato.RIFIUTATO_SIOPEPLUS);
                break;
            }
        }
        storageFile.setDescription(description.toString());
        final StorageObject storageObject = restoreSimpleDocument(
                storageFile,
                new ByteArrayInputStream(storageFile.getBytes()),
                storageFile.getContentType(),
                storageFile.getFileName(),
                distinta.getStorePath(),
                true);
        distinta.setToBeUpdated();
        crudComponentSession.modificaConBulk(userContext, distinta);

    }

    private void messaggioEsito(Risultato risultato) throws RemoteException, ComponentException {
        final MessaggioXML<EsitoFlusso> messaggioXML = ordinativiSiopePlusService.getLocation(risultato.getLocation(), EsitoFlusso.class);
        final EsitoFlusso esitoFlusso = messaggioXML.getObject();
        logger.info("Identificativo flusso: {}", esitoFlusso.getIdentificativoFlusso());
        Distinta_cassiereBulk distinta = fetchDistinta_cassiereBulk(esitoFlusso.getIdentificativoFlusso());
        /**
         * Carico il file del messaggio
         */
        StorageFile storageFile = new StorageFile(messaggioXML.getContent(), MimeTypes.XML.mimetype(),
                String.valueOf(risultato.getProgFlusso()).concat("-").concat(messaggioXML.getName()));
        storageFile.setTitle("Acquisito il " + formatter.format(risultato.getDataUpload().toInstant().atZone(ZoneId.systemDefault())));

        distinta.setIdentificativoFlussoBT(esitoFlusso.getIdentificativoFlussoBT());
        StringBuffer description = new StringBuffer();
        if (esitoFlusso.isRifiutato()) {
            distinta.setStato(Distinta_cassiereBulk.Stato.RIFIUTATO_BT);
            MessaggioRifiutoFlusso messaggioRifiutoFlusso = (MessaggioRifiutoFlusso) esitoFlusso;
            messaggioRifiutoFlusso
                    .getErrore()
                    .stream()
                    .map(ctErrore -> ctErrore.getCodice().toString().concat(" - ").concat(ctErrore.getDescrizione()))
                    .peek(logger::error)
                    .forEach(s -> {
                        description.append(s.concat("\n"));
                    });
        } else {
            distinta.setStato(Distinta_cassiereBulk.Stato.ACCETTATO_BT);
            /**
             * In questo caso invio i documenti allegati alla Distinta via PEC
             *
             */
            if (!Optional.ofNullable(risultato.getDownload()).orElse(Boolean.TRUE) &&
                    Optional.ofNullable(distinta.getInviaPEC()).orElse(Boolean.TRUE)) {
                inviaPEC(userContext, distinta);
            }
        }
        storageFile.setDescription(description.toString());
        final StorageObject storageObject = restoreSimpleDocument(
                storageFile,
                new ByteArrayInputStream(storageFile.getBytes()),
                storageFile.getContentType(),
                storageFile.getFileName(),
                distinta.getStorePath(),
                true);
        distinta.setToBeUpdated();
        crudComponentSession.modificaConBulk(userContext, distinta);
    }

    public boolean inviaPEC(UserContext context, Distinta_cassiereBulk distinta) throws ComponentException {
        try {
            List<String> nodes = new ArrayList<String>();
            StorageObject distintaStorageObject = getStorageObjectByPath(
                    distinta.getStorePath().concat(storageDriver.SUFFIX).concat(distinta.getCMISName()));
            nodes.add(distintaStorageObject.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()));
            List<V_mandato_reversaleBulk> dettagliMan = distintaCassiereComponentSession
                    .dettagliDistinta(
                            userContext,
                            distinta,
                            it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN);

            return inviaPEC(distinta, dettagliMan, true, nodes) ||
                    inviaPEC(distinta, dettagliMan, false, nodes);

        } catch (PersistencyException | IOException _ex) {
            logger.error("Invio distinta {} fallito", distinta.getPg_distinta_def(), _ex);
        }
        return Boolean.FALSE;
    }

    private boolean inviaPEC(Distinta_cassiereBulk distinta, List<V_mandato_reversaleBulk> dettagliMan, boolean isEstero, List<String> args) throws ComponentException {
        try {
            List<String> nodes = new ArrayList<>();
            nodes.addAll(args);
            dettagliMan.stream()
                    .filter(v_mandato_reversaleBulk -> {
                        try {
                            return isRiferimentoDocumentoEsterno(v_mandato_reversaleBulk, isEstero);
                        } catch (RemoteException | ComponentException e) {
                            logger.error("SIOPE+ Invia PEC", e);
                            return Boolean.FALSE;
                        }
                    })
                    .map(v_mandato_reversaleBulk -> getDocumentKey(v_mandato_reversaleBulk, true))
                    .filter(s -> s != null)
                    .forEach(s -> nodes.add(s));
            boolean inviaDistinta = nodes.size() > 1;
            if (distinta.getFl_sepa()) {
                PDFMergerUtility ut = new PDFMergerUtility();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ut.setDestinationStream(out);
                nodes.stream()
                        .map(key -> getResource(key))
                        .forEach(inputStream -> ut.addSource(inputStream));
                try {
                    ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
                } catch (IOException _ex) {
                    logger.error("SIOPE+ Invia PEC", _ex);
                }
                StorageObject distintaPEC = restoreSimpleDocument(
                        distinta,
                        new ByteArrayInputStream(out.toByteArray()),
                        MimeTypes.PDF.mimetype(),
                        DISTINTA_PEC_PDF,
                        distinta.getStorePath(),
                        false
                );
                nodes.clear();
                nodes.add(distintaPEC.getKey());
            }
            if (inviaDistinta) {
                inviaDistintaPEC(nodes, isEstero,
                        "Invio Distinta Identificativo_flusso: " + distinta.getIdentificativoFlusso() +
                                " Progressivo Flusso: " + distinta.getProgFlusso() +
                                " Identificativo Flusso BT: " + distinta.getIdentificativoFlussoBT() +
                                " e Documenti",
                        isDistintaStipendi(dettagliMan));
                return Boolean.TRUE;
            }
        } catch (EmailException | IOException _ex) {
            logger.error("Invio distinta {} fallito", distinta.getPg_distinta_def(), _ex);
        }
        return Boolean.FALSE;
    }

    private Boolean isDistintaStipendi(List<V_mandato_reversaleBulk> dettagliMan) {
        return dettagliMan.stream()
                .map(bulk -> {
                    try {
                        return distintaCassiereComponentSession.findModPag(userContext, bulk);
                    } catch (ComponentException | RemoteException e) {
                        return null;
                    }
                })
                .filter(o -> Optional.ofNullable(o).isPresent())
                .filter(o -> o.getCd_modalita_pag().equalsIgnoreCase(DistintaCassiereComponent.STIPENDI))
                .findAny().isPresent();
    }

    private void messaggioEsitoApplicativo(Risultato risultato, boolean annullaMandati, boolean annullaReversali, boolean riportaMandatoDaFirmare) throws Exception {
        final MessaggioXML<MessaggiEsitoApplicativo> messaggioXML = ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggiEsitoApplicativo.class);
        final MessaggiEsitoApplicativo messaggiEsitoApplicativo = messaggioXML.getObject();
        final List<Object> esitoReversaliOrEsitoMandati = messaggiEsitoApplicativo.getEsitoReversaliOrEsitoMandati();
        AtomicReference<Exception> _ex = new AtomicReference<>();
        final List<OggettoBulk> mandatoStream = esitoReversaliOrEsitoMandati
                .stream()
                .filter(CtEsitoMandati.class::isInstance)
                .map(CtEsitoMandati.class::cast)
                .map(ctEsitoMandato -> {
                    logger.info("Identificativo flusso: {} Mandato {}/{}",
                            ctEsitoMandato.getIdentificativoFlusso(),
                            ctEsitoMandato.getEsercizio(),
                            ctEsitoMandato.getNumeroMandato());
                    StringBuffer error = new StringBuffer();
                    ctEsitoMandato.getListaErrori()
                            .stream()
                            .map(errore -> errore.getCodiceErrore().toString()
                                    .concat(" - ")
                                    .concat(errore.getDescrizione())
                                    .concat(" - ")
                                    .concat(errore.getElemento()))
                            .peek(logger::error)
                            .forEach(s -> {
                                error.append(s.concat("\n"));
                            });
                    MandatoBulk mandato = fetchMandatoBulk(ctEsitoMandato);
                    if (Optional.ofNullable(ctEsitoMandato.getEsitoOperazione())
                            .filter(stEsitoOperazioneMandato ->
                                    stEsitoOperazioneMandato.equals(StEsitoOperazioneMandato.VARIATO) ||
                                            stEsitoOperazioneMandato.equals(StEsitoOperazioneMandato.NON_VARIATO)).isPresent()) {
                        mandato.setStatoVarSos(EsitoOperazione.getValueFromLabel(ctEsitoMandato.getEsitoOperazione().value()));
                    } else {
                        mandato.setEsitoOperazione(EsitoOperazione.getValueFromLabel(ctEsitoMandato.getEsitoOperazione().value()));
                    }
                    mandato.setDtOraEsitoOperazione(
                            new Timestamp(ctEsitoMandato
                                    .getDataOraEsitoOperazione()
                                    .toGregorianCalendar()
                                    .getTimeInMillis())
                    );
                    mandato.setErroreSiopePlus(
                            Optional.ofNullable(error)
                                    .filter(stringBuffer -> stringBuffer.length() > 0)
                                    .map(StringBuffer::toString)
                                    .orElse(null)
                    );
                    if (!annullaMandati && riportaMandatoDaFirmare && mandato.getEsitoOperazione().equalsIgnoreCase(EsitoOperazione.NON_ACQUISITO.value())) {
                        mandato.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                    }
                    mandato.setToBeUpdated();
                    try {
                        SIOPEPlusEsitoBulk siopePlusEsitoBulk = new SIOPEPlusEsitoBulk();
                        siopePlusEsitoBulk.setMandato(mandato);
                        siopePlusEsitoBulk.setProgEsitoApplicativo(risultato.getProgEsitoApplicativo());
                        siopePlusEsitoBulk.setDataUpload(Optional.ofNullable(risultato.getDataUpload())
                                .map(Date::getTime)
                                .map(aLong -> new Timestamp(aLong))
                                .orElse(null));
                        siopePlusEsitoBulk.setIdentificativoFlusso(ctEsitoMandato.getIdentificativoFlusso());
                        siopePlusEsitoBulk.setEsitoOperazione(EsitoOperazione.getValueFromLabel(ctEsitoMandato.getEsitoOperazione().value()));
                        siopePlusEsitoBulk.setDtOraEsitoOperazione(
                                new Timestamp(ctEsitoMandato
                                        .getDataOraEsitoOperazione()
                                        .toGregorianCalendar()
                                        .getTimeInMillis()
                                )
                        );
                        siopePlusEsitoBulk.setToBeCreated();
                        crudComponentSession.creaConBulk(userContext, siopePlusEsitoBulk);
                    } catch (ComponentException | RemoteException e) {
                        logger.error("SIOPE+ CREAZIONE MANDATO ESITO [{}/{}] ERROR", mandato.getEsercizio(), mandato.getPg_mandato(), e);
                    }
                    try {
                        return crudComponentSession.modificaConBulk(userContext, mandato);
                    } catch (ComponentException | RemoteException e) {
                        _ex.set(e);
                        logger.error("SIOPE+ AGGIORNA MANDATO [{}/{}] ERROR", mandato.getEsercizio(), mandato.getPg_mandato(), e);
                        return null;
                    }
                }).collect(Collectors.toList());
        final List<OggettoBulk> reversaleStream = esitoReversaliOrEsitoMandati
                .stream()
                .filter(CtEsitoReversali.class::isInstance)
                .map(CtEsitoReversali.class::cast)
                .map(ctEsitoReversale -> {
                    logger.info("Identificativo flusso: {} Reversale {}/{}", ctEsitoReversale.getIdentificativoFlusso(), ctEsitoReversale.getEsercizio(), ctEsitoReversale.getNumeroReversale());
                    StringBuffer error = new StringBuffer();
                    ctEsitoReversale.getListaErrori()
                            .stream()
                            .map(errore -> errore.getCodiceErrore().toString()
                                    .concat(" - ")
                                    .concat(errore.getDescrizione())
                                    .concat(" - ")
                                    .concat(errore.getElemento()))
                            .peek(logger::error)
                            .forEach(s -> {
                                error.append(s.concat("\n"));
                            });
                    ReversaleBulk reversale = fetchReversaleBulk(ctEsitoReversale);
                    if (Optional.ofNullable(ctEsitoReversale.getEsitoOperazione())
                            .filter(stEsitoOperazioneReversale ->
                                    stEsitoOperazioneReversale.equals(StEsitoOperazioneReversale.VARIATO) ||
                                            stEsitoOperazioneReversale.equals(StEsitoOperazioneReversale.NON_VARIATO)).isPresent()) {
                        reversale.setStatoVarSos(EsitoOperazione.getValueFromLabel(ctEsitoReversale.getEsitoOperazione().value()));
                    } else {
                        reversale.setEsitoOperazione(EsitoOperazione.getValueFromLabel(ctEsitoReversale.getEsitoOperazione().value()));
                    }
                    reversale.setDtOraEsitoOperazione(
                            new Timestamp(ctEsitoReversale
                                    .getDataOraEsitoOperazione()
                                    .toGregorianCalendar()
                                    .getTimeInMillis())
                    );
                    reversale.setErroreSiopePlus(
                            Optional.ofNullable(error)
                                    .filter(stringBuffer -> stringBuffer.length() > 0)
                                    .map(StringBuffer::toString)
                                    .orElse(null)
                    );
                    reversale.setToBeUpdated();
                    try {
                        SIOPEPlusEsitoBulk siopePlusEsitoBulk = new SIOPEPlusEsitoBulk();
                        siopePlusEsitoBulk.setReversale(reversale);
                        siopePlusEsitoBulk.setProgEsitoApplicativo(risultato.getProgEsitoApplicativo());
                        siopePlusEsitoBulk.setDataUpload(Optional.ofNullable(risultato.getDataUpload())
                                .map(Date::getTime)
                                .map(aLong -> new Timestamp(aLong))
                                .orElse(null));
                        siopePlusEsitoBulk.setIdentificativoFlusso(ctEsitoReversale.getIdentificativoFlusso());
                        siopePlusEsitoBulk.setEsitoOperazione(EsitoOperazione.getValueFromLabel(ctEsitoReversale.getEsitoOperazione().value()));
                        siopePlusEsitoBulk.setDtOraEsitoOperazione(
                                new Timestamp(ctEsitoReversale
                                        .getDataOraEsitoOperazione()
                                        .toGregorianCalendar()
                                        .getTimeInMillis()
                                )
                        );
                        siopePlusEsitoBulk.setToBeCreated();
                        crudComponentSession.creaConBulk(userContext, siopePlusEsitoBulk);
                    } catch (ComponentException | RemoteException e) {
                        logger.error("SIOPE+ CREAZIONE REVERSALE ESITO [{}/{}] ERROR", reversale.getEsercizio(), reversale.getPg_reversale(), e);
                    }
                    try {
                        return crudComponentSession.modificaConBulk(userContext, reversale);
                    } catch (ComponentException | RemoteException e) {
                        _ex.set(e);
                        logger.error("SIOPE+ AGGIORNA REVERSALE [{}/{}] ERROR", reversale.getEsercizio(), reversale.getPg_reversale(), e);
                        return null;
                    }
                }).collect(Collectors.toList());
        if (annullaMandati) {
            mandatoStream
                    .stream()
                    .filter(oggettoBulk -> Optional.ofNullable(oggettoBulk).isPresent())
                    .filter(MandatoIBulk.class::isInstance)
                    .map(MandatoIBulk.class::cast)
                    .filter(mandatoBulk -> mandatoBulk.getEsitoOperazione().equals(EsitoOperazione.NON_ACQUISITO.value()))
                    .forEach(mandatoBulk -> {
                        try {
                            logger.info("SIOPE+ ANNULLA MANDATO [{}/{}]", mandatoBulk.getEsercizio(), mandatoBulk.getPg_mandato());
                            mandatoComponentSession.annullaMandato(userContext, mandatoBulk, true);
                        } catch (ComponentException | RemoteException e) {
                            logger.error("SIOPE+ ANNULLA MANDATO [{}/{}] ERROR", mandatoBulk.getEsercizio(), mandatoBulk.getPg_mandato(), e);
                            if (riportaMandatoDaFirmare && mandatoBulk.getEsitoOperazione().equalsIgnoreCase(EsitoOperazione.NON_ACQUISITO.value())) {
                                mandatoBulk.setStato_trasmissione(MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO);
                                mandatoBulk.setToBeUpdated();
                                try {
                                    crudComponentSession.modificaConBulk(userContext, mandatoBulk);
                                } catch (ComponentException | RemoteException ex) {
                                    logger.error("SIOPE+ AGGIORNA MANDATO [{}/{}] ERROR", mandatoBulk.getEsercizio(), mandatoBulk.getPg_mandato(), e);
                                }
                            }
                        }
                    });
        }
        if (annullaReversali) {
            reversaleStream
                    .stream()
                    .filter(oggettoBulk -> Optional.ofNullable(oggettoBulk).isPresent())
                    .filter(ReversaleIBulk.class::isInstance)
                    .map(ReversaleIBulk.class::cast)
                    .filter(reversaleBulk -> reversaleBulk.getEsitoOperazione().equals(EsitoOperazione.NON_ACQUISITO.value()))
                    .forEach(reversaleBulk -> {
                        try {
                            logger.info("SIOPE+ ANNULLA REVERSALE [{}/{}]", reversaleBulk.getEsercizio(), reversaleBulk.getPg_reversale());
                            reversaleComponentSession.annullaReversale(userContext, reversaleBulk, true);
                        } catch (ComponentException | RemoteException e) {
                            _ex.set(e);
                            logger.error("SIOPE+ ANNULLA REVERSALE [{}/{}] ERROR", reversaleBulk.getEsercizio(), reversaleBulk.getPg_reversale(), e);
                        }
                    });
        }
        if (Optional.ofNullable(_ex).flatMap(exceptionAtomicReference -> Optional.ofNullable(exceptionAtomicReference.get())).isPresent())
            throw _ex.get();
    }


    public void executeMessaggiSiopeplus() {
        try {
            logger.info("SIOPE+ SCAN started at: {}", LocalDateTime.now());
            Configurazione_cnrBulk configurazione_cnrBulk = distintaCassiereComponentSession.lockMessaggiSIOPEPlus(userContext);
            if (configurazione_cnrBulk == null) {
                logger.info("SIOPE+ SCAN alredy started in another server.");
            } else {
                try {
                    if (Boolean.valueOf(configurazione_cnrBulk.getVal01())) {
                        messaggiSiopeplus(null, null, false);
                    } else {
                        logger.warn("SIOPE+ SCAN disabled");
                    }
                } finally {
                    distintaCassiereComponentSession.unlockMessaggiSIOPEPlus(userContext);
                }
            }
            logger.info("SIOPE+ SCAN end at: {}", LocalDateTime.now());
        } catch (Throwable _ex) {
            logger.error("SIOPE+ ScheduleExecutor error", _ex);
        }
    }

    public Stream<Risultato> downloadGiornalieraDiCassa(LocalDateTime dataDa, LocalDateTime dataA, Boolean download, String userForGiornaleDiCassa) {
        final Lista listaGiornaliDiCassa = giornaleDiCassaSiopePlusService.getListaMessaggi(dataDa, dataA, download, null);
        logger.info("Lista gioraliera di cassa: {}", listaGiornaliDiCassa);
        final List<GiornalieraDiCassaRisultato> result = Optional.ofNullable(listaGiornaliDiCassa.getRisultati())
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .map(risultato -> {
                    FlussoGiornaleDiCassaBulk flussoGiornaleDiCassaBulk = null;
                    try {
                        final OggettoBulk siopePlusRisultatoBulk = crudComponentSession.creaConBulk(userContext,
                                new SIOPEPlusRisultatoBulk(Esito.GIORNALEDICASSA.name(), risultato));

                        flussoGiornaleDiCassaBulk = messaggioGiornaleDiCassa(risultato);
                        logger.info("SIOPE+ GIORNALEDICASSA elaborato risultato: {}", risultato);

                        siopePlusRisultatoBulk.setToBeDeleted();
                        crudComponentSession.eliminaConBulk(userContext, siopePlusRisultatoBulk);
                    } catch (Exception _ex) {
                        risultato.setError(_ex);
                        logger.error("SIOPE+ GIORNALEDICASSA ERROR for risultato: {}", risultato, _ex);
                    }
                    return new GiornalieraDiCassaRisultato(risultato, flussoGiornaleDiCassaBulk);
                }).collect(Collectors.toList());

        result.stream()
                .map(GiornalieraDiCassaRisultato::getFlussoGiornaleDiCassaBulk)
                .filter(Utility.distinctByKey(FlussoGiornaleDiCassaKey::getIdentificativoFlusso))
                .forEach(flussoGiornaleDiCassaBulk -> {
                    V_ext_cassiere00Bulk v_ext_cassiere00Bulk = new V_ext_cassiere00Bulk();
                    v_ext_cassiere00Bulk.setNome_file(flussoGiornaleDiCassaBulk.getIdentificativoFlusso());
                    v_ext_cassiere00Bulk.setEsercizio(flussoGiornaleDiCassaBulk.getEsercizio());
                    try {
                        distintaCassiereComponentSession.processaFile(
                                new WSUserContext(userForGiornaleDiCassa, null,
                                        flussoGiornaleDiCassaBulk.getEsercizio(),
                                        null, null, null),
                                v_ext_cassiere00Bulk);
                    } catch (ApplicationException _ex) {
                        logger.info("SIOPE+ GIORNALEDICASSA processing file name: {} esercizio: {} messaggio: {}",
                                flussoGiornaleDiCassaBulk.getIdentificativoFlusso(), flussoGiornaleDiCassaBulk.getEsercizio(), _ex.getMessage());
                    } catch (ComponentException | RemoteException _ex) {
                        logger.error("SIOPE+ GIORNALEDICASSA ERROR for processing file: {}", flussoGiornaleDiCassaBulk.getIdentificativoFlusso(), _ex);
                    }
                });

        return result.stream().map(GiornalieraDiCassaRisultato::getRisultato);
    }

    public Stream<Risultato> downloadMessaggiACK(LocalDateTime dataDa, LocalDateTime dataA, Boolean download) {
        final List<Risultato> allMessaggi = ordinativiSiopePlusService.getAllMessaggi(Esito.ACK,
                dataDa, dataA, download, null);
        logger.info("Lista ACK: {}", allMessaggi);
        return Optional.ofNullable(allMessaggi)
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .map(risultato -> {
                    try {
                        final OggettoBulk siopePlusRisultatoBulk = crudComponentSession.creaConBulk(userContext,
                                new SIOPEPlusRisultatoBulk(Esito.ACK.name(), risultato));
                        messaggioACK(risultato);
                        logger.info("SIOPE+ ACK elaborato risultato: {}", risultato);

                        siopePlusRisultatoBulk.setToBeDeleted();
                        crudComponentSession.eliminaConBulk(userContext, siopePlusRisultatoBulk);
                    } catch (Exception _ex) {
                        risultato.setError(_ex);
                        logger.error("SIOPE+ ACK ERROR for risultato: {}", risultato, _ex);
                    }
                    return risultato;
                });
    }

    public Stream<Risultato> downloadMessaggiEsito(LocalDateTime dataDa, LocalDateTime dataA, Boolean download) {
        final List<Risultato> allMessaggi = ordinativiSiopePlusService.getAllMessaggi(Esito.ESITO,
                dataDa, dataA, download, null);
        logger.info("SIOPE+ Lista Esito: {}", allMessaggi);
        return Optional.ofNullable(allMessaggi)
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .map(risultato -> {
                    try {
                        final OggettoBulk siopePlusRisultatoBulk = crudComponentSession.creaConBulk(userContext,
                                new SIOPEPlusRisultatoBulk(Esito.ESITO.name(), risultato));
                        messaggioEsito(risultato);
                        logger.info("SIOPE+ ESITO elaborato risultato: {}", risultato);

                        siopePlusRisultatoBulk.setToBeDeleted();
                        crudComponentSession.eliminaConBulk(userContext, siopePlusRisultatoBulk);
                    } catch (Exception _ex) {
                        risultato.setError(_ex);
                        logger.error("SIOPE+ ESITO ERROR for risultato: {}", risultato, _ex);
                    }
                    return risultato;
                });
    }

    public Stream<Risultato> downloadMessaggiEsitoApplicativo(
            LocalDateTime dataDa,
            LocalDateTime dataA,
            Boolean download,
            boolean annullaMandati,
            boolean annullaReversali,
            boolean riportaMandatoDaFirmare
    ) {
        final List<Risultato> allMessaggi = ordinativiSiopePlusService.getAllMessaggi(Esito.ESITOAPPLICATIVO,
                dataDa, dataA, download, null);
        logger.info("SIOPE+ Lista Esito Applicativo: {}", allMessaggi);
        return Optional.ofNullable(allMessaggi)
                .orElseGet(() -> Collections.emptyList())
                .stream()
                .map(risultato -> {
                    try {
                        final OggettoBulk siopePlusRisultatoBulk = crudComponentSession.creaConBulk(userContext,
                                new SIOPEPlusRisultatoBulk(Esito.ESITOAPPLICATIVO.name(), risultato));
                        messaggioEsitoApplicativo(risultato, annullaMandati, annullaReversali, riportaMandatoDaFirmare);
                        logger.info("SIOPE+ ESITO APPLICATIVO elaborato risultato: {}", risultato);

                        siopePlusRisultatoBulk.setToBeDeleted();
                        crudComponentSession.eliminaConBulk(userContext, siopePlusRisultatoBulk);
                    } catch (Exception _ex) {
                        risultato.setError(_ex);
                        logger.error("SIOPE+ ESITO APPLICATIVO ERROR for risultato: {}", risultato, _ex);
                    }
                    return risultato;
                });
    }

    /**
     * Leggi messaggi dalla piattaforma SIOPE+
     */
    public void messaggiSiopeplus(LocalDateTime dataDa, LocalDateTime dataA, Boolean download) {
        boolean annullaMandati = Boolean.FALSE,
                annullaReversali = Boolean.FALSE,
                riportaMandatoDaFirmare = Boolean.FALSE;
        String userForGiornaleDiCassa = SIOPEPLUS;
        try {
            riportaMandatoDaFirmare =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_RIPORTA_MANDATO_DAFIRMARE
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);

            annullaMandati =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_ANNULLA_MANDATI
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);
            annullaReversali =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal01(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_ANNULLA_REVERSALI
                    ))
                            .map(s -> Boolean.valueOf(s))
                            .orElse(Boolean.FALSE);
            userForGiornaleDiCassa =
                    Optional.ofNullable(configurazione_cnrComponentSession.getVal02(
                            userContext,
                            Calendar.getInstance().get(Calendar.YEAR),
                            "*",
                            Configurazione_cnrBulk.PK_FLUSSO_ORDINATIVI,
                            Configurazione_cnrBulk.SK_ATTIVO_SIOPEPLUS
                    ))
                            .orElse(SIOPEPLUS);


        } catch (ComponentException | RemoteException _ex) {
            logger.error("SIOPE+ ESITO ERROR recupera configurazione_cnr per annullamento Mandati e Reversali", _ex);
        }
        try {
            final List<SIOPEPlusRisultatoBulk> risultati = crudComponentSession.find(userContext, SIOPEPlusRisultatoBulk.class, "findAll");
            for (SIOPEPlusRisultatoBulk siopePlusRisultatoBulk : risultati) {
                try {
                    Risultato risultato = new Risultato();
                    risultato.setLocation(siopePlusRisultatoBulk.getLocation());
                    risultato.setProgFlusso(siopePlusRisultatoBulk.getProg_flusso());
                    risultato.setProgEsitoApplicativo(siopePlusRisultatoBulk.getProg_esito_applicativo());
                    risultato.setDataProduzione(siopePlusRisultatoBulk.getData_produzione());
                    risultato.setDataUpload(siopePlusRisultatoBulk.getData_upload());
                    switch (siopePlusRisultatoBulk.getEsitoEnum()) {
                        case ACK: {
                            messaggioACK(risultato);
                            break;
                        }
                        case ESITO: {
                            messaggioEsito(risultato);
                            break;
                        }
                        case ESITOAPPLICATIVO: {
                            messaggioEsitoApplicativo(risultato, annullaMandati, annullaReversali, riportaMandatoDaFirmare);
                            break;
                        }
                        case GIORNALEDICASSA: {
                            messaggioGiornaleDiCassa(risultato);
                            break;
                        }
                    }
                    siopePlusRisultatoBulk.setToBeDeleted();
                    crudComponentSession.eliminaConBulk(userContext, siopePlusRisultatoBulk);
                } catch (Exception e) {
                    if (siopePlusRisultatoBulk.getPg_ver_rec() > 5) {
                        java.io.StringWriter sw = new java.io.StringWriter();
                        e.printStackTrace(new java.io.PrintWriter(sw));
                        SendMail.sendErrorMail("SIOPE+ Elabora risultato " +
                                        siopePlusRisultatoBulk.getEsito() + " " +
                                        siopePlusRisultatoBulk.getLocation(),
                                sw.toString());
                    } else {
                        siopePlusRisultatoBulk.setToBeUpdated();
                        crudComponentSession.modificaConBulk(userContext, siopePlusRisultatoBulk);
                    }
                    logger.error("SIOPE+ MESSAGGI error during find risultato", e);
                }
            }
        } catch (ComponentException | RemoteException e) {
            logger.error("SIOPE+ MESSAGGI error during find risultato", e);
        }

        downloadMessaggiACK(dataDa, dataA, download)
                .map(Risultato::toString)
                .forEach(logger::debug);
        downloadMessaggiEsito(dataDa, dataA, download)
                .map(Risultato::toString)
                .forEach(logger::debug);
        downloadMessaggiEsitoApplicativo(dataDa, dataA, download, annullaMandati, annullaReversali, riportaMandatoDaFirmare)
                .map(Risultato::toString)
                .forEach(logger::debug);
        downloadGiornalieraDiCassa(dataDa, dataA, download, userForGiornaleDiCassa)
                .map(Risultato::toString)
                .forEach(logger::debug);

    }

    public boolean isRiferimentoDocumentoEsterno(V_mandato_reversaleBulk bulk, boolean isEstero) throws RemoteException, ComponentException {
        final Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk =
                Optional.ofNullable(distintaCassiereComponentSession.findModPag(userContext, bulk))
                        .filter(Rif_modalita_pagamentoBulk.class::isInstance)
                        .map(Rif_modalita_pagamentoBulk.class::cast)
                        .orElseThrow(() -> new ApplicationMessageFormatException("Modalità di pagamento non trovata: {0}", String.valueOf(bulk.getPg_documento_cont())));

        return Arrays.asList(
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.DISPOSIZIONEDOCUMENTOESTERNO,
                Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.ACCREDITOCONTOCORRENTEPOSTALE
        ).contains(
                Optional.ofNullable(rif_modalita_pagamentoBulk.getTipo_pagamento_siope())
                        .map(s -> Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.getValueFrom(s))
                        .orElseGet(() -> Rif_modalita_pagamentoBulk.TipoPagamentoSiopePlus.REGOLARIZZAZIONE)
        ) && !bulk.getTi_documento_cont().equals(MandatoBulk.TIPO_REGOLAM_SOSPESO) &&
                (!isEstero || rif_modalita_pagamentoBulk.getTi_pagamento().equals(Rif_modalita_pagamentoBulk.IBAN));
    }

    class GiornalieraDiCassaRisultato {
        private final Risultato risultato;
        private final FlussoGiornaleDiCassaBulk flussoGiornaleDiCassaBulk;

        public GiornalieraDiCassaRisultato(Risultato risultato, FlussoGiornaleDiCassaBulk flussoGiornaleDiCassaBulk) {
            this.risultato = risultato;
            this.flussoGiornaleDiCassaBulk = flussoGiornaleDiCassaBulk;
        }

        public Risultato getRisultato() {
            return risultato;
        }

        public FlussoGiornaleDiCassaBulk getFlussoGiornaleDiCassaBulk() {
            return flussoGiornaleDiCassaBulk;
        }
    }

    class StorageDataSource implements DataSource {

        private StorageObject storageObject;

        public StorageDataSource(StorageObject storageObject) {
            this.storageObject = storageObject;
        }


        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new NotImplementedException();
        }

        @Override
        public String getName() {
            return Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()))
                    .map(String.class::cast)
                    .orElse(null);
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return getResource(storageObject);
        }

        @Override
        public String getContentType() {
            return Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()))
                    .map(String.class::cast)
                    .orElse(null);
        }
    }
}
