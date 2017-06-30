package it.cnr.contab.doccont00.service;

import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.spring.config.StorageObject;
import it.cnr.contab.spring.config.StoragePropertyNames;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.firma.arss.ArubaSignServiceClient;
import it.cnr.jada.util.mail.SimplePECMail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import javax.activation.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentiContabiliService extends StoreService {
	private transient static final Logger logger = LoggerFactory.getLogger(DocumentiContabiliService.class);
	private ArubaSignServiceClient arubaSignServiceClient;
	private String pecHostName,pecMailFromBanca,pecMailFromBancaPassword,pecMailToBancaNoEuroSepa,pecMailToBancaItaliaF23F24;
	
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
		return Optional.ofNullable(getStorageObjectByPath(bulk.getStorePath().concat(StoreService.BACKSLASH).concat(bulk.getCMISName())))
				.map(storageObject ->
                        fullNodeRef ? Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.ALFCMIS_NODEREF.value()))
                            .map(String.class::cast)
                            .orElse(storageObject.getKey()) : storageObject.getKey())
				.orElse(null);
	}

	public InputStream getStreamDocumento(StatoTrasmissione bulk) throws ApplicationException{
        return Optional.ofNullable(getStorageObjectByPath(bulk.getStorePath().concat(StoreService.BACKSLASH).concat(bulk.getCMISName())))
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
	
	public void inviaDistintaPEC1210(List<String> nodes) throws EmailException, ApplicationException, IOException {
		inviaDistintaPEC1210(nodes, true,null);
	}	
	
	public void inviaDistintaPEC1210(List<String> nodes, boolean isNoEuroOrSepa, String nrDistinta) throws EmailException, ApplicationException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
		email.setHostName(pecHostName);
		if (isNoEuroOrSepa){
			if (pecMailToBancaNoEuroSepa!=null && pecMailToBancaNoEuroSepa.split(";").length!=0){
			        email.addTo(pecMailToBancaNoEuroSepa.split(";"));
			}else{ 
			       	email.addTo(pecMailToBancaNoEuroSepa,pecMailToBancaNoEuroSepa);
			}
		}        
		else{
			if(pecMailToBancaItaliaF23F24!=null && pecMailToBancaItaliaF23F24.split(";").length!=0 ){
				email.addTo(pecMailToBancaItaliaF23F24.split(";"));
			}
			else{
				email.addTo(pecMailToBancaItaliaF23F24,pecMailToBancaItaliaF23F24);
			}
		}
		
		email.setFrom(pecMailFromBanca, pecMailFromBanca);
		if (nrDistinta!=null)
			email.setSubject("Invio Distinta 1210 "+nrDistinta);
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
	public void inviaDistintaPEC(List<String> nodes, boolean isNoEuroOrSepa, String nrDistinta ) throws EmailException, ApplicationException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
		email.setHostName(pecHostName);
		
		if (isNoEuroOrSepa){
			if (pecMailToBancaNoEuroSepa!=null && pecMailToBancaNoEuroSepa.split(";").length!=0){
			        email.addTo(pecMailToBancaNoEuroSepa.split(";"));
			}else{ 
			       	email.addTo(pecMailToBancaNoEuroSepa,pecMailToBancaNoEuroSepa);
			}
		}        
		else{
			if(pecMailToBancaItaliaF23F24!=null && pecMailToBancaItaliaF23F24.split(";").length!=0 ){
				email.addTo(pecMailToBancaItaliaF23F24.split(";"));
			}
			else{
				email.addTo(pecMailToBancaItaliaF23F24,pecMailToBancaItaliaF23F24);
			}
		}
		email.setFrom(pecMailFromBanca, pecMailFromBanca);
		if (nrDistinta!=null)
			email.setSubject("Invio Distinta "+nrDistinta+" e Documenti");
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
			return Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()))
					.map(String.class::cast)
					.orElse(null);
		}
		
		@Override
		public InputStream getInputStream() throws IOException {
			return getResource(storageObject);
		}
		
		@Override
		public String getContentType() {
			return Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()))
					.map(String.class::cast)
					.orElse(null);
		}
	}
	
}