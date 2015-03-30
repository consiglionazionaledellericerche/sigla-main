package it.cnr.contab.pdd.ws.client;


import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.RispostaSdINotificaEsitoType;

import java.io.IOException;

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

public interface FatturazioneElettronicaClient {
	JAXBElement<RispostaSdINotificaEsitoType> notificaEsito(JAXBElement<NotificaEsitoCommittenteType> fileSdIType, String nomeFile, String tipoIntegrazioneSDI) throws IOException;
	public Marshaller getMarshaller();
	public Unmarshaller getUnmarshaller();
}
