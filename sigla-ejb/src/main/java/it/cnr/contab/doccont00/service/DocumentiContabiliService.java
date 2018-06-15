package it.cnr.contab.doccont00.service;

import com.google.gson.GsonBuilder;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.PdfSignApparence;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.SignP7M;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.firma.arss.ArubaSignServiceClient;
import it.cnr.jada.firma.arss.ArubaSignServiceException;
import it.cnr.jada.util.mail.SimplePECMail;
import it.cnr.si.spring.storage.*;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

public class DocumentiContabiliService extends StoreService {
    private transient static final Logger logger = LoggerFactory.getLogger(DocumentiContabiliService.class);
    @Autowired
    private StorageService storageService;
    @Value("${sign.document.png.url}")
    private String signDocumentURL;
    private ArubaSignServiceClient arubaSignServiceClient;
    private String pecHostName, pecMailFromBanca, pecMailFromBancaPassword, pecMailToBancaNoEuroSepa, pecMailToBancaItaliaF23F24;

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

    public String getDocumentKey(StatoTrasmissione bulk) {
        return getDocumentKey(bulk, false);
    }

    public String getDocumentKey(StatoTrasmissione bulk, boolean fullNodeRef) {
        return Optional.ofNullable(bulk)
                .map(statoTrasmissione -> Optional.ofNullable(getStorageObjectByPath(statoTrasmissione.getStorePath()
                        .concat(StorageService.SUFFIX)
                        .concat(statoTrasmissione.getCMISName())))
                        .map(storageObject ->
                                fullNodeRef ? Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()))
                                        .map(String.class::cast)
                                        .orElse(storageObject.getKey()) : storageObject.getKey())
                        .orElse(null)).orElse(null);
    }

    public InputStream getStreamDocumento(StatoTrasmissione bulk) {
        return Optional.ofNullable(getStorageObjectByPath(bulk.getStorePath().concat(StorageService.SUFFIX).concat(bulk.getCMISName())))
                .map(StorageObject::getKey)
                .map(key -> getResource(key))
                .orElse(null);
    }

    public Map<String, String> getCertSubjectDN(String username, String password) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        Principal principal = arubaSignServiceClient.getUserCertSubjectDN(username, password);
        if (principal == null)
            return null;
        String subjectArray[] = principal.toString().split(",");
        for (String s : subjectArray) {
            String[] str = s.trim().split("=");
            String key = str[0];
            String value = str[1];
            result.put(key, value);
        }
        return result;
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
        SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
        email.setHostName(pecHostName);
        if (isNoEuroOrSepa) {
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
        if (nrDistinta != null)
            email.setSubject("Invio Distinta 1210 " + nrDistinta);
        else
            email.setSubject("Invio Distinta 1210");

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
        // Create the email message
        SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
        email.setHostName(pecHostName);

        if (isNoEuroOrSepa) {
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
        if (nrDistinta != null)
            email.setSubject("Invio Distinta " + nrDistinta + " e Documenti");
        else
            email.setSubject("Invio Distinta e Documenti");
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

    public Set<String> getAllegatoForModPag(String key, String modPag) {
        return getChildren(key).stream()
                .filter(storageObject -> modPag.equals(storageObject.getPropertyValue("doccont:rif_modalita_pagamento")))
                .map(StorageObject::getKey)
                .collect(Collectors.toSet());
    }

    public String signDocuments(PdfSignApparence pdfSignApparence, String url) throws StorageException {
        if (storageService.getStoreType().equals(StorageService.StoreType.CMIS)) {
            return signDocuments(new GsonBuilder().create().toJson(pdfSignApparence), url);
        } else {
            List<byte[]> bytes = Optional.ofNullable(pdfSignApparence)
                    .map(pdfSignApparence1 -> pdfSignApparence1.getNodes())
                    .map(list ->
                            list.stream()
                                    .map(s -> storageService.getInputStream(s))
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
                it.cnr.jada.firma.arss.stub.PdfSignApparence apparence = new it.cnr.jada.firma.arss.stub.PdfSignApparence();
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
                    storageService.updateStream(
                            pdfSignApparence.getNodes().get(i),
                            new ByteArrayInputStream(bytesSigned.get(i)),
                            MimeTypes.PDF.mimetype()
                    );
                }

            } catch (ArubaSignServiceException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            }
            return null;
        }
    }

    public String signDocuments(SignP7M signP7M, String url) throws StorageException {
        if (storageService.getStoreType().equals(StorageService.StoreType.CMIS)) {
            return signDocuments(new GsonBuilder().create().toJson(signP7M), url);
        } else {
            StorageObject storageObject = storageService.getObject(signP7M.getNodeRefSource());
            try {
                final byte[] bytes = arubaSignServiceClient.pkcs7SignV2(
                        signP7M.getUsername(),
                        signP7M.getPassword(),
                        signP7M.getOtp(),
                        IOUtils.toByteArray(storageService.getInputStream(signP7M.getNodeRefSource())));
                Map<String, Object> metadataProperties = new HashMap<>();
                metadataProperties.put(StoragePropertyNames.NAME.value(), signP7M.getNomeFile());
                metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), SIGLAStoragePropertyNames.CNR_ENVELOPEDDOCUMENT.value());

                return storeSimpleDocument(
                        new ByteArrayInputStream(bytes),
                        MimeTypes.P7M.mimetype(),
                        storageObject.getPath().substring(0, storageObject.getPath().lastIndexOf(StorageService.SUFFIX) + 1),
                        metadataProperties).getKey();
            } catch (ArubaSignServiceException | IOException e) {
                throw new StorageException(StorageException.Type.GENERIC, e);
            } finally {
                List<String> aspects = storageObject.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
                aspects.add("P:cnr:signedDocument");
                updateProperties(Collections.singletonMap(
                        StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                        aspects
                ), storageObject);
            }
        }
    }

    private String signDocuments(String json, String url) throws StorageException {
        return storageService.signDocuments(json, url);
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
