package it.cnr.contab.docamm00.service;

import it.cnr.contab.config00.sto.bulk.UnitaOrganizzativaPecBulk;
import it.cnr.contab.docamm00.ejb.DocAmmFatturazioneElettronicaComponentSession;
import it.cnr.contab.docamm00.ejb.FatturaElettronicaPassivaComponentSession;
import it.cnr.contab.docamm00.ejb.RicezioneFatturePA;
import it.cnr.contab.docamm00.ejb.TrasmissioneFatturePA;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.StringEncrypter;
import it.cnr.contab.util.StringEncrypter.EncryptionException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.mail.SimplePECMail;
import it.gov.fatturapa.sdi.messaggi.v1.MetadatiInvioFileType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.Address;
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
import javax.mail.internet.MimeUtility;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FromStringTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.XmlMappingException;

public class FatturaPassivaElettronicaService implements InitializingBean{
	private transient final static Logger logger = LoggerFactory.getLogger(FatturaPassivaElettronicaService.class);
	
	private FatturaElettronicaPassivaComponentSession fatturaElettronicaPassivaComponentSession;
	private DocAmmFatturazioneElettronicaComponentSession docAmmFatturazioneElettronicaComponentSession;
	private RicezioneFatturePA ricezioneFattureService;
	private TrasmissioneFatturePA trasmissioneFattureService;
	private FatturazioneElettronicaClient fatturazioneElettronicaClient;
	private UserContext userContext;
	private Boolean pecScanDisable;
	private Properties pecMailConf;

	private List<String> listaMessageIdAlreadyScanned = new ArrayList<String>();

	private String pecHostName, pecURLName, pecSDIAddress, pecSDISubjectFatturaAttivaInvioTerm, pecSDISubjectNotificaPecTerm, pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm,
		pecSDIFromStringTerm, pecSDISubjectRiceviFattureTerm, pecSDISubjectFatturaAttivaRicevutaConsegnaTerm, pecSDISubjectFatturaAttivaNotificaScartoTerm, pecSDISubjectFatturaAttivaMancataConsegnaTerm,
		pecSDISubjectNotificaEsitoTerm, pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm, pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm;
	private List<String> pecScanFolderName, pecScanReceiptFolderName, pecHostAddress;
	
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
		this.pecScanReceiptFolderName = Arrays.asList(StringUtils.split(pecScanReceiptFolderName, ","));;
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
		this.pecScanFolderName =  Arrays.asList(StringUtils.split(pecScanFolderName, ","));
	}
	public void setPecHostAddress(String pecHostAddress) {
		this.pecHostAddress =  Arrays.asList(StringUtils.split(pecHostAddress, ","));
	}
	public void execute() {
		try{
			if (pecScanDisable){
				logger.info("PEC scan is disabled");
			} else {
				logger.info("PEC SCAN for ricevi Fatture started at: "+new Date());
				for (UnitaOrganizzativaPecBulk unitaOrganizzativaPecBulk : fatturaElettronicaPassivaComponentSession.scanPECProtocollo(userContext)) {
					pecScan(
							unitaOrganizzativaPecBulk.getEmailPecProtocollo(), 
							unitaOrganizzativaPecBulk.getCodPecProtocollo());
				}
				logger.info("PEC SCAN for ricevi Fatture finished at: "+new Date());				
			}
		}catch(Throwable _ex){
			logger.error("ScheduleExecutor error", _ex);
		}
	}
	
	private void riceviFattura(Message message, String userName) throws ComponentException {
		try {
			List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
			if (bodyParts.isEmpty()) {
				logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
						" non ha file allegati.");
				return;
			}
			if (bodyParts.size() > 2) {
				logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
						" ha più di due file allegati.");
				return;
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
					String replyTo = getReplyTo(message);
			  	if (!fatturaElettronicaPassivaComponentSession.existsIdentificativo(userContext, metadatiInvioFileType.getValue().getIdentificativoSdI().longValue())) {
			    	ricezioneFattureService.riceviFatturaSIGLA(
			    			metadatiInvioFileType.getValue().getIdentificativoSdI(), 
			    			bodyPartFattura.getFileName(), 
			    			replyTo,
						new DataHandler(new ByteArrayDataSource(
								IOUtils.toByteArray(bodyPartFattura.getInputStream()),bodyPartFattura.getContentType())), 
						bodyPartMetadati.getFileName(), 
						new DataHandler(new ByteArrayDataSource(
								IOUtils.toByteArray(bodyPartMetadati.getInputStream()), bodyPartMetadati.getContentType())));				    	    					    	    			
			  	}
			} else {
				logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName + " è stato precessato ma gli allegati presenti non sono conformi.");
			}
		} catch (Exception e) {
			logger.error("PEC scan error while importing file.", e);
		}
	}

	private String getReplyTo(Message message) throws MessagingException {
		Address[] repliesAddress = message.getReplyTo();
		String replyTo = null;
		if (repliesAddress != null){
			logger.info("Replies: "+repliesAddress.toString());
			if (repliesAddress.length > 0){
				replyTo = repliesAddress[0].toString();
			}
		}
		logger.info("ReplyTo: "+replyTo);
		return replyTo;
	}

	private void notificaFatturaPassivaScartoEsito(Message message, String userName) throws ComponentException {
		try{
			String[] headerMessage = getMessageID(message);
			if (headerMessage != null){
				String messageID = Arrays.asList(headerMessage).toString();
				String messageIDWithUser = userName + " "+messageID;
	    	logger.info("MessageIDWithUser " + messageIDWithUser);
				if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)){
					logger.info(messageIDWithUser);
					try {
						List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
						if (bodyParts.isEmpty()) {
							logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
									" non ha file allegati.");
							return;
						}
						if (bodyParts.size() > 2) {
							logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName +
									" ha più di due file allegati.");
							return;
						}
						boolean trovatoFile = false;
						for (BodyPart bodyPart : bodyParts) {
							String fileName = extractFileName(bodyPart);
							if (fileName.toLowerCase().endsWith("xml")){
								ricezioneFattureService.notificaScartoEsito(fileName, createDataHandler(bodyPart), message.getReceivedDate());
								trovatoFile = true;
								break;
							}
						}
						if (!trovatoFile){
							logger.warn("Il messaggio con id:"+message.getMessageNumber()+" recuperato dalla casella PEC:"+userName + " è stato precessato ma gli allegati presenti non sono conformi.");
							SendMail.sendErrorMail("Fatture Elettroniche: Passive: Scarto Esito. Allegati non conformi. Mail:"+userName, message.getDescription());
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


	@SuppressWarnings("unchecked")
	public void pecScan(String userName, String password) throws ComponentException {
		logger.info("PEC SCAN for ricevi Fatture email: "+userName + "pwd :" +password);
		Properties props = System.getProperties();
		props.putAll(pecMailConf);
		try {
			try {
				password = StringEncrypter.decrypt(userName, password);
			} catch (EncryptionException e1) {
				new AuthenticationFailedException("Cannot decrypt password");
			}
			final Session session = Session.getDefaultInstance(props, null);
			URLName urlName = new URLName(pecURLName);
			final Store store = session.getStore(urlName);
			store.connect(userName, password);
			searchMailFromPec(userName, store);
			searchMailFromSdi(userName, store);
			store.close();
		} catch (AuthenticationFailedException e) {
			logger.error("Error while scan PEC email:" +userName + " - password:"+password);
		} catch (NoSuchProviderException e) {
			logger.error("Error while scan PEC email:" +userName, e);
		} catch (MessagingException e) {
			logger.error("Error while scan PEC email:" +userName, e);
		}				
	}
	private void searchMailFromPec(String userName, final Store store)
			throws MessagingException {
		List<Folder> folders = new ArrayList<Folder>();
		for (String folderName : pecScanReceiptFolderName) {
			folders.add(store.getFolder(folderName));
		}
		for (Folder folder : folders) {
		    if (folder.exists()) {
				folder.open(Folder.READ_WRITE);
				processingMailFromHostPec(folder, userName);
				folder.close(true);				
		    }
		}
	}
	private void searchMailFromSdi(String userName, final Store store)
			throws MessagingException {
		List<Folder> folders = new ArrayList<Folder>();
		for (String folderName : pecScanFolderName) {
			folders.add(store.getFolder(folderName));
		}
		for (Folder folder : folders) {
		    if (folder.exists()) {
				folder.open(Folder.READ_WRITE);
				processingMailFromSdi(folder, userName);
				folder.close(true);				
		    }
		}
	}

	private void processingMailFromSdi(Folder folder, String userName) throws MessagingException{
    	List<SearchTerm> terms = createTermsForSearchSdi();
    	Message messages[] = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
    	logger.info("Recuperati " + messages.length +" messaggi SDI dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
	    for (int i = 0; i < messages.length; i++) {
	    	try {
	    		Message message = messages[i];
				if (message.getSubject() != null){
					if (message.getSubject().contains(pecSDISubjectRiceviFattureTerm)){
						riceviFattura(message, userName);
					} else if (message.getSubject().contains(pecSDISubjectFatturaPassivaNotificaScartoEsitoTerm)){
						notificaFatturaPassivaScartoEsito(message, userName);
					} else if (message.getSubject().contains(pecSDISubjectFatturaAttivaRicevutaConsegnaTerm)){
						notificaFatturaAttivaRicevutaConsegna(message, userName);
					} else if (message.getSubject().contains(pecSDISubjectNotificaEsitoTerm)){
						notificaFatturaAttivaEsito(message);
					} else if (message.getSubject().contains(pecSDISubjectFatturaAttivaNotificaScartoTerm)){
						notificaFatturaAttivaScarto(message);
					} else if (message.getSubject().contains(pecSDISubjectFatturaAttivaMancataConsegnaTerm)){
						notificaFatturaAttivaMancataConsegna(message);
					} else if (message.getSubject().contains(pecSDISubjectFatturaAttivaDecorrenzaTerminiTerm)){
						notificaFatturaAttivaDecorrenzaTermini(message);
					} else if (message.getSubject().contains(pecSDISubjectFatturaAttivaAttestazioneTrasmissioneFatturaTerm)){
						notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(message);
					} else {
						logger.warn("Fatture Elettroniche: Attive: Oggetto dell'e-mail non gestito." + message.getSubject());
						SendMail.sendErrorMail("Fatture Elettroniche: Oggetto dell'e-mail non gestito. Mail:"+userName, message.getDescription());
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
	public List<SearchTerm> createTermsForSearchSdi() {
		List<SearchTerm> terms = new ArrayList<SearchTerm>();
    	addConditionDate(terms);
    	terms.add(new FromStringTerm(pecSDIFromStringTerm));
		return terms;
	}
	
	private void processingMailFromHostPec(Folder folder, String userName) throws MessagingException{
		List<SearchTerm> terms = createTermsForSearchPec();
    	Message messages[] = folder.search(new AndTerm(terms.toArray(new SearchTerm[terms.size()])));
    	logger.info("Recuperati " + messages.length +" messaggi PEC dalla casella PEC:" + userName + " nella folder:" + folder.getFullName());
	    for (int i = 0; i < messages.length; i++) {
	    	try {
	    		Message message = messages[i];
	    		
	    		if (message.getSubject().contains(getSubjectTermForFatturaAttivaPec())){
		    		String nomeFile = message.getSubject().substring(getSubjectTermForFatturaAttivaPec().length() + 1 );
		    		if (nomeFile != null){
						trasmissioneFattureService.notificaFatturaAttivaConsegnaPec(userContext, nomeFile, message.getSentDate());
		    		}
	    		} else if (message.getSubject().contains(getSubjectTermForFatturaPassivaPec())){
		    		String[] headerMessage = getMessageID(message);
		    		if (headerMessage != null){
						String messageID = Arrays.asList(headerMessage).toString();
						String messageIDWithUser = userName + " "+messageID;
			    	logger.info("MessageIDWithUser " + messageIDWithUser);
					if (!listaMessageIdAlreadyScanned.contains(messageIDWithUser)){
							logger.info(messageIDWithUser);
				    		String identificativoSdi = message.getSubject().substring(getSubjectTermForFatturaPassivaPec().length() + 1 );
				    		if (identificativoSdi != null){
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
		String [] headerMessage = message.getHeader("Message-ID");
		return headerMessage;
	}
	public List<SearchTerm> createTermsForSearchPec() {
		List<SearchTerm> terms = new ArrayList<SearchTerm>();
    	addConditionDate(terms);
		addConditionFromAddress(terms);
		addConditionSubject(terms);
		return terms;
	}
	private void addConditionSubject(List<SearchTerm> terms) {
		List<SearchTerm> listSubject = new ArrayList<SearchTerm>();
		listSubject.add(new SubjectTerm(getSubjectTermForFatturaAttivaPec()));
		listSubject.add(new SubjectTerm(getSubjectTermForFatturaPassivaPec()));
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
		String subjectTerm = pecSDISubjectNotificaPecTerm+" "+pecSDISubjectFatturaAttivaInvioTerm;
		return subjectTerm;
	}
	public String getSubjectTermForFatturaPassivaPec() {
		String subjectTerm = pecSDISubjectNotificaPecTerm+" "+pecSDISubjectNotificaEsitoTerm;
		return subjectTerm;
	}
	public void addConditionDate(List<SearchTerm> terms) {
		Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DATE, -1);
    	Date dateBeforeOneDays = cal.getTime();			    	
    	terms.add(new ReceivedDateTerm(ComparisonTerm.GE, dateBeforeOneDays));
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
			if (bodyPartZip != null){
				logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
		        ZipInputStream zis = new ZipInputStream(bodyPartZip.getInputStream());
		        ZipEntry entry;
		        InputStream streamXml = null; 
		        while ((entry = zis.getNextEntry()) != null)
		        {
		            System.out.println("entry: " + entry.getName() + ", " + entry.getSize());
		            if (entry.getName().toLowerCase().endsWith("xml")){
		            	String contentType = new MimetypesFileTypeMap().getContentType(entry.getName());
		            	DataHandler dataHandler = createDataHandler(zis, contentType);
		            	streamXml = dataHandler.getInputStream();
						trasmissioneFattureService.notificaFatturaAttivaAvvenutaTrasmissioneNonRecapitata(userContext, entry.getName(), dataHandler);
						return;
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
			if (bodyPartXml != null){
				logger.info("Fatture Elettroniche: Decorrenza Termini. Estratto Body Part.");
				String fileName = extractFileName(bodyPartXml);
				if (fileName.startsWith(docAmmFatturazioneElettronicaComponentSession.recuperoInizioNomeFile(userContext))){
					trasmissioneFattureService.notificaFatturaAttivaDecorrenzaTermini(userContext, fileName, createDataHandler(bodyPartXml));
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
		return new DataHandler(new ByteArrayDataSource(IOUtils.toByteArray(bodyPart.getInputStream()),bodyPart.getContentType()));
	}
	
	private DataHandler createDataHandler(InputStream stream, String contentType)
			throws IOException, MessagingException {
		return new DataHandler(new ByteArrayDataSource(IOUtils.toByteArray(stream),contentType));
	}
	
	private void notificaFatturaAttivaEsito(Message message) throws ComponentException {
		logger.info("Fatture Elettroniche: Attive: Inizio Notifica Esito.");
		try {
			BodyPart bodyPartXml = estraiBodyPartXmlNotificaFatturaAttiva(message);
			if (bodyPartXml != null){
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
			if (bodyPartXml != null){
				logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
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
			if (bodyPartXml != null){
				logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
				trasmissioneFattureService.notificaFatturaAttivaRicevutaConsegna(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
				String[] replies = message.getHeader("Reply-To");
				if (replies != null){
					String replyTo = replies[0];
					logger.info("Reply To: "+ replyTo + ". Username: "+username);
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
			if (bodyPartXml != null){
				logger.info("Fatture Elettroniche: Attive: Estratto Body Part.");
				trasmissioneFattureService.notificaFatturaAttivaMancataConsegna(userContext, bodyPartXml.getFileName(), createDataHandler(bodyPartXml));
			}
		} catch (Exception e) {
			throw new ComponentException(e);
		}
	}
	
	private BodyPart estraiBodyPartXmlNotificaFatturaAttiva(Message message) throws ComponentException{
		try{
			List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
			for (BodyPart bodyPart : bodyParts) {
				String fileName = extractFileName(bodyPart);
				
				if (fileName.toLowerCase().endsWith("xml")){
					return bodyPart;
				}
			}
			return null;
		} catch (Exception e) {
			throw new ComponentException(e);
		}
	}

	private BodyPart estraiBodyPartZipNotificaFatturaAttiva(Message message) throws ComponentException{
		try{
			List<BodyPart> bodyParts = estraiBodyPart(message.getContent());
			for (BodyPart bodyPart : bodyParts) {
				String fileName = extractFileName(bodyPart);
				
				if (fileName.toLowerCase().endsWith("zip")){
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
		if (fileName != null && fileName.startsWith("=?") && fileName.endsWith("?=")){
			fileName = MimeUtility.decodeText(fileName); 
		}
		return fileName;
	}

	public void notificaEsito(String userName, String password, DocumentoEleTestataBulk bulk, JAXBElement<NotificaEsitoCommittenteType> notificaEsitoCommittenteType) throws EmailException, XmlMappingException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(userName, password);
		email.setHostName(pecHostName);
		String replyTo = null; 
		if (bulk.getDocumentoEleTrasmissione() != null){
			replyTo = bulk.getDocumentoEleTrasmissione().getReplyT);
		if (fileName != null && fileName.startsWith("=?") && fileName.endsWith("?=")){
			fileName = MimeUtility.decodeText(fileName); 
		}
		return fileName;
	}

	public void notificaEsito(String userName, String password, DocumentoEleTestataBulk bulk, JAXBElement<NotificaEsitoCommittenteType> notificaEsitoCommittenteType) throws EmailException, XmlMappingException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(userName, password);
		email.setHostName(pecHostName);
		String replyTo = null; 
		if (bulk.getDocumentoEleTrasmissione() != null){
			replyTo = bulk.getDocumentoEleTrasmissione().getReplyTo();
		}
		email.addTo(replyTo == null ? pecSDIAddress : replyTo , "SdI - Sistema Di Interscambio");
		email.setFrom(userName, userName);
		email.setSubject("Notifica di esito " + bulk.getIdentificativoSdi());
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

	public void inviaFatturaElettronica(String userName, String password, File fatturaAttivaSigned, String idFattura) throws EmailException, XmlMappingException, IOException {
		// Create the email message
		SimplePECMail email = new SimplePECMail(userName, password);
		email.setHostName(pecHostName);
		email.addTo(pecSDIAddress, "SdI - Sistema Di Interscambio");
		email.setFrom(userName, userName);
		email.setSubject(pecSDISubjectFatturaAttivaInvioTerm + " "+idFattura);
		email.setMsg("Invio Fattura Elettronica. "+ idFattura);
		email.attach(fatturaAttivaSigned);
		// send the email
		email.send();		
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