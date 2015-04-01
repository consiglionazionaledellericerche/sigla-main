package it.cnr.contab.docamm00.service;

import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.RicezioneFatturePA;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.mail.SimplePECMail;
import it.gov.fatturapa.sdi.messaggi.v1.MetadatiInvioFileType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.XmlMappingException;

public class FatturaPassivaElettronicaService implements InitializingBean{
	private transient final static Log logger = LogFactory.getLog(FatturaPassivaElettronicaService.class);
	
	private FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession;
	private RicezioneFatturePA ricezioneFattureService;
	private FatturazioneElettronicaClient fatturazioneElettronicaClient;
	private UserContext userContext;
	private Boolean pecScanDisable;
	private Properties pecMailConf;
	private String pecHostName, pecURLName, pecSDIAddress, 
		pecSDIFromStringTerm, pecSDISubjectRiceviFattureTerm, pecScanFolderName;
	
	public void setFatturaElettronicaPassivaComponentSession(
			FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession) {
		this.fatturaElettronicaPassivaComponentSession = fatturaElettronicaPassivaComponentSession;
	}
	public void setRicezioneFattureService(
			RicezioneFatturePA ricezioneFattureService) {
		this.ricezioneFattureService = ricezioneFattureService;
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
	public void setPecSDIFromStringTerm(String pecSDIFromStringTerm) {
		this.pecSDIFromStringTerm = pecSDIFromStringTerm;
	}
	public void setPecSDISubjectRiceviFattureTerm(
			String pecSDISubjectRiceviFattureTerm) {
		this.pecSDISubjectRiceviFattureTerm = pecSDISubjectRiceviFattureTerm;
	}
	
	public void setPecScanFolderName(String pecScanFolderName) {
		this.pecScanFolderName = pecScanFolderName;
	}
	public void execute() {
		try{
			if (pecScanDisable){
				logger.info("PEC scan is disabled");
			} else {
				logger.info("PEC SCAN for ricevi Fatture started at: "+new Date());
				fatturaElettronicaPassivaComponentSession.scanPECProtocollo(userContext);
				logger.info("PEC SCAN for ricevi Fatture finished at: "+new Date());				
			}
		}catch(Throwable _ex){
			logger.error("ScheduleExecutor error", _ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void pecScanForRiceviFatture(String userName, String password) throws ComponentException {	
		logger.info("PEC SCAN for ricevi Fatture email: "+userName);
		Properties props = System.getProperties();
		props.putAll(pecMailConf);
		try {
			try {
				password = StringEncrypter.decrypt(userName, password).replaceAll("[^a-zA-Z0-9_-]", "");
			} catch (EncryptionException e) {
				throw new AuthenticationFailedException("Cannot decrypt password");
			}					
			final Session session = Session.getDefaultInstance(props, null);
			URLName urlName = new URLName(pecURLName);
			final Store store = session.getStore(urlName);
			store.connect(userName, password);
			Folder inbox = store.getFolder(pecScanFolderName);	
		    inbox.open(Folder.READ_WRITE);
		    if (inbox.exists()) {
		    	List<SearchTerm> terms = new ArrayList<SearchTerm>();
		    	Calendar cal = Calendar.getInstance();
		    	cal.setTime(new Date());
		    	cal.add(Calendar.DATE, -1);
		    	Date dateBeforeOneDays = cal.getTime();			    	
		    	terms.add(new ReceivedDateTerm(ComparisonTerm.GE, dateBeforeOneDays));
		    	terms.add(new FromStringTerm(pecSDIFromStringTerm));
		    	terms.add(new SubjectTerm(pecSDISubjectRiceviFattureTerm));
		    	Message messages[] = inbox.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
		    	logger.info("Recuperati " + messages.length +" messaggi dalla casella PEC:" + userName);
			    for (int i = 0; i < messages.length; i++) {
			    	try {
			    		Message message = messages[i];
			    		List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
			    		if (bodyParts.isEmpty()) {
			    			logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
			    					" non ha file allegati.");
			    			continue;
			    		}
			    		if (bodyParts.size() > 2) {
			    			logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
			    					" ha più di due file allegati.");
			    			continue;
			    		}
			    		BodyPart bodyPartFattura = null, bodyPartMetadati = null;
			    		for (BodyPart bodyPart : bodyParts) {
			    			if (isBodyPartMetadati(bodyPart)){
			    				bodyPartMetadati = bodyPart;
			    			} else {
			    				bodyPartFattura = bodyPart;
			    			}
						}
			    		if (bodyPartFattura != null && bodyPartMetadati != null) {
			    	    	JAXBElement<MetadatiInvioFileType> metadatiInvioFileType = (JAXBElement<MetadatiInvioFileType>) 
			    	    			fatturazioneElettronicaClient.getUnmarshaller().
			    	    			unmarshal(new StreamSource(bodyPartMetadati.getInputStream()));
			    	    	
			    	    	DocumentoEleTrasmissioneBulk bulk = new DocumentoEleTrasmissioneBulk();
			    	    	bulk.setIdentificativoSdi(metadatiInvioFileType.getValue().getIdentificativoSdI().longValue());
			    	    	RemoteIterator iterator = fatturaElettronicaPassivaComponentSession.cerca(userContext, null, bulk);
			    	    	int elements = iterator.countElements();
			    	    	EJBCommonServices.closeRemoteIterator(iterator);
			    	    	if (elements == 0) {
				    	    	ricezioneFattureService.riceviFatturaSIGLA(
				    	    			metadatiInvioFileType.getValue().getIdentificativoSdI(), 
				    	    			bodyPartFattura.getFileName(), 
										new DataHandler(new ByteArrayDataSource(
												IOUtils.toByteArray(bodyPartFattura.getInputStream()),bodyPartFattura.getContentType())), 
										bodyPartMetadati.getFileName(), 
										new DataHandler(new ByteArrayDataSource(
												IOUtils.toByteArray(bodyPartMetadati.getInputStream()), bodyPartMetadati.getContentType())));				    	    					    	    			
			    	    	}
			    		} else {
			    			logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
			    					" è stato precessato ma gli allegati presenti non sono conformi.");
			    		}
					} catch (Exception e) {
						logger.error("PEC scan error while importing file.", e);
					}
				}
		    }
		    inbox.close(true);
			store.close();
		} catch (AuthenticationFailedException e) {
			logger.error("Error while scan PEC email:" +userName + " - password:"+password);
		} catch (NoSuchProviderException e) {
			logger.error("Error while scan PEC email:" +userName, e);
		} catch (MessagingException e) {
			logger.error("Error while scan PEC email:" +userName, e);
		}				
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
	
	public void notificaEsito(String userName, String password, DocumentoEleTestataBulk bulk, JAXBElement<NotificaEsitoCommittenteType> notificaEsitoCommittenteType) throws EmailException, XmlMappingException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(userName, password);
		email.setHostName(pecHostName);
		email.addTo(pecSDIAddress, "SdI - Sistema Di Interscambio");
		email.setFrom(userName, userName);
		email.setSubject(" Notifica di esito " + bulk.getIdentificativoSdi());
		email.setMsg("Il file trasmesso con identificativo SdI:" + bulk.getIdentificativoSdi() + 
				(bulk.isRifiutata() ? " è stato Rifiutato (EC02) poichè " + bulk.getMotivoRifiuto() :" è stato Accettato (EC01)") +
				", in allegato la notifica di esito.");

		// add the attachment
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		fatturazioneElettronicaClient.getMarshaller().marshal(notificaEsitoCommittenteType, new StreamResult(outputStream));
		email.attach(new ByteArrayDataSource(outputStream.toByteArray()), bulk.getNomeFile("EC"), "", EmailAttachment.ATTACHMENT);
		// send the email
		email.send();
		logger.info("Inviata notifica di esito per IdentificativoSdi:"+bulk.getIdentificativoSdi());
	}
	private List<BodyPart> estraiBodyPart(Object content) throws MessagingException, IOException {
		List<BodyPart> results = new ArrayList<BodyPart>();
		estraiParte(content, results);
		return results;
	}
	
	private void estraiParte(Object obj, List<BodyPart> bodyParts) throws MessagingException, IOException {
		if (obj instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) obj;
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);
				estraiParte(bodyPart.getContent(), bodyParts);
			}
		} else if (obj instanceof MimeBodyPart) {
			MimeBodyPart multipart = (MimeBodyPart) obj;
			estraiParte(multipart.getContent(), bodyParts);
		} else if (obj instanceof MimeMessage) {
			MimeMessage mimemessage = (MimeMessage) obj;
			forwardedEmail(mimemessage.getContent(), bodyParts);
		}
	}
	private void forwardedEmail(Object obj, List<BodyPart> bodyParts) throws MessagingException, IOException {
		if (obj instanceof MimeMultipart) {
			MimeMultipart multipart = (MimeMultipart) obj;
			for (int j = 0; j < multipart.getCount(); j++) {
				BodyPart bodyPart = multipart.getBodyPart(j);
				String disposition = bodyPart.getDisposition();
				if (disposition != null && disposition.equals(Part.ATTACHMENT)) {
					bodyParts.add(bodyPart);
					if (logger.isDebugEnabled()) {
						logger.debug("Content type:" + bodyPart.getContentType());
						logger.debug("File name:" + bodyPart.getFileName());
					}
				}
			}
		}		
	}
	public void afterPropertiesSet() throws Exception {
    	userContext = new WSUserContext("SDI",null, 
    			new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),
    			null,null,null);		
	}	
}