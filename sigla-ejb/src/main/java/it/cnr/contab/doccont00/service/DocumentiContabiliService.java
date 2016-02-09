package it.cnr.contab.doccont00.service;

import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.firma.arss.ArubaSignServiceClient;
import it.cnr.jada.util.mail.SimplePECMail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentiContabiliService extends SiglaCMISService {
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

	public List<String> getNodeRefDocumento(StatoTrasmissione bulk) throws ApplicationException{
		return getNodeRefDocumento(bulk, false);
	}

	public List<String> getNodeRefDocumento(StatoTrasmissione bulk, boolean fullNodeRef) throws ApplicationException{
		CmisObject cmisObject;
		try {
			cmisObject = getNodeByPath(bulk.getCMISPath(this).getPath() + "/" + bulk.getCMISName());
		} catch (CmisObjectNotFoundException _ex) {
			return null;
		}
		return Arrays.asList(fullNodeRef ? cmisObject.getProperty(ALFCMIS_NODEREF).getValueAsString(): cmisObject.getId());
	}

	public List<String> getNodeRefDocumento(Integer esercizio, String cds, Long pgDocumento, String tipo) throws ApplicationException{
		return getNodeRefDocumento(esercizio, cds, pgDocumento, tipo, false);
	}
	
	public List<String> getNodeRefDocumento(Integer esercizio, String cds, Long pgDocumento, String tipo, boolean fullNodeRef) throws ApplicationException{
		List<String> ids = new ArrayList<String>();
		StringBuffer query = new StringBuffer("select " + (fullNodeRef ? "doc.alfcmis:nodeRef" :"doc.cmis:objectId") + " from doccont:document doc");
		if (cds != null)
			query.append(" join strorg:cds cds on doc.cmis:objectId = cds.cmis:objectId");
		query.append(" where doc.doccont:esercizioDoc = ").append(esercizio);
		if (cds != null)
			query.append(" and cds.strorgcds:codice = '").append(cds).append("'");
		query.append(" and doc.doccont:tipo = '").append(tipo).append("'");
		query.append(" and doc.doccont:numDoc = ").append(pgDocumento);
		try {
			ItemIterable<QueryResult> results = search(query);
			if (results.getTotalNumItems() == 0)
				return null;
			else {
				for (QueryResult node : results) {
					ids.add((String) node.getPropertyValueById(fullNodeRef ? "alfcmis:nodeRef" :PropertyIds.OBJECT_ID));
				}
				return ids;
			}			
		} catch (CmisObjectNotFoundException _ex) {
			logger.error("CmisObjectNotFoundException dopo la query: " + query , _ex);
			return null;
		}
	}
	public InputStream getStreamDocumento(Integer esercizio, String cds, Long pgMandato, String tipo) throws Exception{
		List<String> ids = getNodeRefDocumento(esercizio, cds, pgMandato, tipo);
		if (ids != null){
			if (ids.size() == 1){
				try{
					return getResource(getNodeByNodeRef(ids.get(0)));
				}catch (CmisObjectNotFoundException _ex){
				}
			}else{
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.setDestinationStream(new ByteArrayOutputStream());
				try {
					for (String id : ids) {
						ut.addSource(getResource(getNodeByNodeRef(id)));
					}
					ut.mergeDocuments();
					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
				} catch (COSVisitorException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}catch (CmisObjectNotFoundException _ex){
				}
			}
		}
		return null;
	}
	
	public InputStream getStreamDocumento(V_mandato_reversaleBulk bulk) throws Exception{
		return getStreamDocumento(bulk.getEsercizio(), bulk.getCd_cds(), bulk.getPg_documento_cont(), bulk.getCd_tipo_documento_cont());
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
		inviaDistintaPEC1210(nodes, true);
	}	
	
	public void inviaDistintaPEC1210(List<String> nodes, boolean isNoEuroOrSepa) throws EmailException, ApplicationException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
		email.setHostName(pecHostName);
		if (isNoEuroOrSepa)
			email.addTo(pecMailToBancaNoEuroSepa, pecMailToBancaNoEuroSepa);
		else
			email.addTo(pecMailToBancaItaliaF23F24, pecMailToBancaItaliaF23F24);			
		email.setFrom(pecMailFromBanca, pecMailFromBanca);
		email.setSubject("Invio Documenti 1210");
		email.setMsg("In allegato i documenti 1210");
		// add the attachment
		for (String nodeRef : nodes) {
			CmisObject cmisObject = getNodeByNodeRef(nodeRef);
			email.attach(new ByteArrayDataSource(IOUtils.toByteArray(getResource(cmisObject))), cmisObject.getName(), "", EmailAttachment.ATTACHMENT);
		}
		// send the email
		email.send();
		logger.debug("Inviata distinta PEC");
	}
	public void inviaDistintaPEC(List<String> nodes, boolean isNoEuroOrSepa) throws EmailException, ApplicationException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(pecMailFromBanca, pecMailFromBancaPassword);
		email.setHostName(pecHostName);
		if (isNoEuroOrSepa)
			email.addTo(pecMailToBancaNoEuroSepa, pecMailToBancaNoEuroSepa);
		else
			email.addTo(pecMailToBancaItaliaF23F24, pecMailToBancaItaliaF23F24);			
		email.setFrom(pecMailFromBanca, pecMailFromBanca);
		email.setSubject("Invio Distinta e Documenti");
		email.setMsg("In allegato i documenti");
		// add the attachment
		for (String nodeRef : nodes) {
			CmisObject cmisObject = getNodeByNodeRef(nodeRef);
			email.attach(new ByteArrayDataSource(IOUtils.toByteArray(getResource(cmisObject))), cmisObject.getName(), "", EmailAttachment.ATTACHMENT);
		}
		// send the email
		email.send();
		logger.debug("Inviata distinta PEC");
	}

	public Set<String> getAllegatoForModPag(Folder folderMandato, String modPag) {
		Set<String> documents = new HashSet<String>();
		for (CmisObject child : folderMandato.getChildren()) {
			if (modPag.equals(child.getPropertyValue("doccont:rif_modalita_pagamento")))
				documents.add(child.getId());			
		}		
		return documents;
	}	
}