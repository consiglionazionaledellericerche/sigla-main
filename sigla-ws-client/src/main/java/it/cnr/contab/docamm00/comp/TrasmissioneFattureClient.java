package it.cnr.contab.docamm00.comp;

import it.gov.fatturapa.FileSdIType;
import it.gov.fatturapa.ObjectFactory;
import it.gov.fatturapa.TrasmissioneFatture;
import it.gov.fatturapa.TrasmissioneFattureWSService;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

public class TrasmissioneFattureClient {
	private static final Logger LOGGER = Logger.getLogger(TrasmissioneFattureClient.class);

	public void notificaScarto(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createNotificaScarto(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.notificaScarto(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}

	public void ricevutaConsegna(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createRicevutaConsegna(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.ricevutaConsegna(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}

	public void notificaMancataConsegna(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createNotificaMancataConsegna(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.notificaMancataConsegna(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}

	public void notificaEsito(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createNotificaEsito(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.notificaEsito(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}

	public void notificaDecorrenzaTermini(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createNotificaDecorrenzaTermini(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.notificaDecorrenzaTermini(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}

	public void attestazioneTrasmissioneFattura(FileSdIType fileSdIType) throws IOException {
		try {
			ObjectFactory obj = new ObjectFactory();
			JAXBElement<FileSdIType> element = obj.createAttestazioneTrasmissioneFattura(fileSdIType);

			TrasmissioneFatture trasmissioneFatture = new TrasmissioneFattureWSService().getTrasmissioneFattureWSPort();
			trasmissioneFatture.attestazioneTrasmissioneFattura(element.getValue());
		} catch (Exception e) {
		    LOGGER.info("Errore Client WS");
		}
	}
}