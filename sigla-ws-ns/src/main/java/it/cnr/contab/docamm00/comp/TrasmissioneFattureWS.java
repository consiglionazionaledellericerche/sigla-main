package it.cnr.contab.docamm00.comp;

import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.docamm00.cmis.CMISDocAmmAspect;
import it.cnr.contab.docamm00.cmis.CMISFileFatturaAttiva;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.WSUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.gov.fatturapa.FileSdIType;
import it.gov.fatturapa.sdi.messaggi.v1.AttestazioneTrasmissioneFatturaType;
import it.gov.fatturapa.sdi.messaggi.v1.ErroreType;
import it.gov.fatturapa.sdi.messaggi.v1.EsitoCommittenteType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaDecorrenzaTerminiType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaMancataConsegnaType;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaScartoType;
import it.gov.fatturapa.sdi.messaggi.v1.RicevutaConsegnaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConstraintException;
import org.jboss.wsf.spi.annotation.WebContext;
@Stateless
@WebService(endpointInterface="it.gov.fatturapa.TrasmissioneFatture")
@WebContext(contextRoot="/SIGLA-SIGLAEJB_1")
public class TrasmissioneFattureWS {
	private static String STATO_RICEVUTA_CONSEGNA = "CON";
	private static String STATO_MANCATA_CONSEGNA = "NRE";
	private static String STATO_SCARTO = "SCA";
	private static String STATO_ESITO_ACCETTATO = "ACC";
	private static String STATO_ESITO_RIFIUTATO = "RIF";
	private static String STATO_DECORRENZA_TERMINI = "DEC";
	private static String STATO_TRASMISSIONE_FATTURA = "COS";
	
	public void ricevutaConsegna(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'CON'
		 * NOTE_INVIO_SDI  = Campo Note presente nel messaggio
		 * DT_CONSEGNA_SDI = Data Ora Consegna presente nel file		
		 */
		RicevutaConsegnaType currentType = (RicevutaConsegnaType)getJAXBElement(ricevuta).getValue();
    	UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	String currentStato = STATO_RICEVUTA_CONSEGNA;
    	String currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_RICEVUTA_CONSEGNA.value();
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, currentType.getNote(), currentType.getDataOraConsegna(), false);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	public void notificaMancataConsegna(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'NRE'
		 * NOTE_INVIO_SDI  = Campo Note presente nel messaggio
		 * DT_CONSEGNA_SDI = Data Ora Consegna presente nel file		
		 */
		NotificaMancataConsegnaType currentType = (NotificaMancataConsegnaType)getJAXBElement(ricevuta).getValue(); 
    	UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	String currentStato = STATO_MANCATA_CONSEGNA;
    	String currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_MANCATA_CONSEGNA.value();
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, currentType.getNote(), currentType.getDataOraRicezione(), false);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	public void notificaScarto(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'SCA'
		 * NOTE_INVIO_SDI  = Campo Note presente nel messaggio + ERRORI
		 * DT_CONSEGNA_SDI = NULL
		 * 
		 * STORNO FATTURA
		 */
		NotificaScartoType currentType = (NotificaScartoType)getJAXBElement(ricevuta).getValue(); 

		StringBuffer note = new StringBuffer();
		note.append(currentType.getNote());
		for (ErroreType errore : currentType.getListaErrori().getErrore())
			note.append("-("+errore.getCodice()+":"+errore.getDescrizione()+")");
    	
		UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	String currentStato = STATO_SCARTO;
    	String currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_SCARTO.value();
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, note.toString(), null, true);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	public void notificaEsito(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'ACC' SE ESITO = 'EC01', 'RIF' SE ESITO = 'EC02'
		 * NOTE_INVIO_SDI  = NULL
		 * DT_CONSEGNA_SDI = NULL
		 * 
		 * STORNO FATTURA SE ESITO = 'EC02'
		 */
		NotificaEsitoType currentType = (NotificaEsitoType)getJAXBElement(ricevuta).getValue();
		String currentStato=null, currentAspect=null;
		if (currentType.getEsitoCommittente().getEsito().equals(EsitoCommittenteType.EC_01)) {
			currentStato = STATO_ESITO_ACCETTATO;
			currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_ACCETTATO.value();
		} else if (currentType.getEsitoCommittente().getEsito().equals(EsitoCommittenteType.EC_02)) {
			currentStato = STATO_ESITO_RIFIUTATO;
			currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_ESITO_RIFIUTATO.value();
		}
		
    	UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, null, null, currentStato.equals(STATO_ESITO_RIFIUTATO)?true:false);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	public void notificaDecorrenzaTermini(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'DEC'
		 * NOTE_INVIO_SDI  = Campo Note presente nel messaggio
		 * DT_CONSEGNA_SDI = NULL
		 */
		NotificaDecorrenzaTerminiType currentType = (NotificaDecorrenzaTerminiType)getJAXBElement(ricevuta).getValue(); 

    	UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	String currentStato = STATO_DECORRENZA_TERMINI;
    	String currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_DECORRENZA_TERMINI.value();
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, currentType.getNote(), null, false);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	public void attestazioneTrasmissioneFattura(FileSdIType ricevuta) {
		/*
		 * STATO_INVIO_SDI = 'COS'
		 * NOTE_INVIO_SDI  = Campo Note presente nel messaggio
		 * DT_CONSEGNA_SDI = NULL
		 */
		AttestazioneTrasmissioneFatturaType currentType = (AttestazioneTrasmissioneFatturaType)getJAXBElement(ricevuta).getValue(); 

		UserContext userContext = new WSUserContext("IIT",null,new Integer(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)),null,null,null);
    	String currentStato = STATO_TRASMISSIONE_FATTURA;
    	String currentAspect = CMISDocAmmAspect.SIGLA_FATTURE_ATTACHMENT_TRASMISSIONE_FATTURA.value();
    	FatturaAttivaSingolaComponentSession comp;
		try {
			comp = Utility.createFatturaAttivaSingolaComponentSession();
			Fattura_attiva_IBulk fatturaAttiva = comp.ricercaFatturaSDI(userContext, String.valueOf(currentType.getIdentificativoSdI()));
			if (fatturaAttiva!=null) {
				archiviaFile(ricevuta, fatturaAttiva, currentAspect, currentStato);
				comp.aggiornaDatiFatturaSDI(userContext, fatturaAttiva, currentStato, currentType.getNote(), null, false);
			} else
				throw new SOAPFaultException(generaFault("Attenzione! Non esiste la fattura con identificativo SDI "+String.valueOf(currentType.getIdentificativoSdI())));		
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
	}

	private JAXBElement<?> getJAXBElement(FileSdIType fileSdiType) {
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance("it.gov.fatturapa.sdi.messaggi.v1");
		} catch (JAXBException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di inizializzazione di un oggetto JAXB. "+e.getMessage()));
		}
		
		File file;
		try {
			file = File.createTempFile(fileSdiType.getNomeFile(), ".tmp");
		} catch (IOException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file temporaneo. "+e.getMessage()));
		}
		
		OutputStream out;
		try {
			out = new FileOutputStream( file );
			fileSdiType.getFile().writeTo( out );
			out.close();
		} catch (FileNotFoundException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file di risposta. "+e.getMessage()));
		} catch (IOException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file di risposta. "+e.getMessage()));
		}
			
		JAXBElement<?> element = null; 
		try{
			element = (JAXBElement<?>)jc.createUnmarshaller().unmarshal(file);
		} catch (ClassCastException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file temporaneo. "+e.getMessage()));
		} catch (JAXBException e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di caricamento del file. "+e.getMessage()));
		}
		return element;
	}

	private SOAPFault generaFault(String stringFault) {
		try{
			MessageFactory factory = MessageFactory.newInstance();
			SOAPMessage message = factory.createMessage(); 
			SOAPFactory soapFactory = SOAPFactory.newInstance();
			SOAPBody body = message.getSOAPBody(); 
			SOAPFault fault = body.addFault();
			Name faultName = soapFactory.createName("","", SOAPConstants.URI_NS_SOAP_ENVELOPE);
			fault.setFaultCode(faultName);
			fault.setFaultString(stringFault);
			return fault;
		} catch(SOAPException e) {
			return null;
		}
	}

	private File archiviaFile(FileSdIType fileSdiType, Fattura_attiva_IBulk fatturaAttiva, String aspect, String stato) {
		File file;
		try {
			file = File.createTempFile(fileSdiType.getNomeFile(), ".tmp");
		} catch (IOException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file temporaneo. "+e.getMessage()));
		}
		
		OutputStream out;
		try {
			out = new FileOutputStream( file );
			fileSdiType.getFile().writeTo( out );
			out.close();
		} catch (FileNotFoundException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file di risposta. "+e.getMessage()));
		} catch (IOException e) {
			throw new SOAPFaultException(generaFault("Errore in fase di creazione file di risposta. "+e.getMessage()));
		}

		DocumentiCollegatiDocAmmService cmisService = SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class);
		try {
			CMISFile cmisFile = new CMISFileFatturaAttiva(file, fatturaAttiva, 
					"application/xml", stato + fatturaAttiva.constructCMISNomeFile() + "-" + fileSdiType.getNomeFile() + ".xml");

			if (cmisFile!=null) {
				//E' previsto solo l'inserimento ma non l'aggiornamento
				CMISPath path = cmisFile.getCMISParentPath(cmisService);
				try{
					Document node = cmisService.restoreSimpleDocument(cmisFile, 
							cmisFile.getInputStream(),
							cmisFile.getContentType(),
							cmisFile.getFileName(), 
							path);
					if (aspect!=null)
						cmisService.addAspect(node, aspect);
					cmisFile.setDocument(node);
				} catch (Exception e) {
					if (e.getCause() instanceof CmisConstraintException)
						throw new ApplicationException("CMIS - File ["+cmisFile.getFileName()+"] già presente o non completo di tutte le proprietà obbligatorie. Inserimento non possibile!");
					throw new ApplicationException("CMIS - Errore nella registrazione del file XML sul Documentale (" + e.getMessage() + ")");
				}
			}
		} catch (Exception e) {
			throw new SOAPFaultException(generaFault("Errore generico in fase di aggiornamento fattura. "+e.getMessage()));		
		}
		return file;
	}
}
                   
