package it.cnr.contab.pdd.ws.client.impl;

import it.cnr.contab.pdd.ws.client.FatturazioneElettronicaClient;
import it.gov.fatturapa.sdi.messaggi.v1.NotificaEsitoCommittenteType;
import it.gov.fatturapa.sdi.ws.ricezione.v1_0.types.RispostaSdINotificaEsitoType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class FatturazioneElettronicaClientImpl implements FatturazioneElettronicaClient{
	
	private static final String PDD_USERNAME = "it.cnr.contab.pdd.servizio.applicativo.credenziali.user",
			PDD_PASSWORD = "it.cnr.contab.pdd.servizio.applicativo.credenziali.password",
			NOTIFICA_ESISTO_PD = "it.cnr.contab.pdd.servizio.notifica.esito.PD",
			NOTIFICA_ESISTO_SOAP_ACTION = "it.cnr.contab.pdd.servizio.notifica.esito.soapAction";
    private WebServiceTemplate webServiceTemplate;
    private final Map<String, String>  serverParameters;
    enum TipoIntegrazioneSDI {
    	SDI, PEC, SPC;
    }    
	public FatturazioneElettronicaClientImpl(
			Map<String, String> serverParameters) {
		super();
		this.serverParameters = serverParameters;		
	}

	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}

	public Marshaller getMarshaller() {
		return webServiceTemplate.getMarshaller();
	}
	
	public Unmarshaller getUnmarshaller() {
		return webServiceTemplate.getUnmarshaller();
	}
	
	@SuppressWarnings("unchecked")
	public JAXBElement<RispostaSdINotificaEsitoType> notificaEsito(JAXBElement<NotificaEsitoCommittenteType> fileSdIType, String nomeFile, 
			String tipoIntegrazioneSDI) throws IOException {
		if (tipoIntegrazioneSDI.equalsIgnoreCase(TipoIntegrazioneSDI.SPC.name())) {
			URL url = new URL(serverParameters.get(NOTIFICA_ESISTO_PD).concat("?NomeFile=").concat(nomeFile));
			URLConnection connection = url.openConnection();
			HttpURLConnection httpConn = (HttpURLConnection) connection;		
			httpConn.setRequestProperty("Content-Type","text/xml");
			httpConn.setRequestProperty("SOAPAction",serverParameters.get(NOTIFICA_ESISTO_SOAP_ACTION));
			httpConn.setRequestProperty("Authorization","Basic " + new String(
					Base64.encodeBase64((serverParameters.get(PDD_USERNAME)+":"+serverParameters.get(PDD_PASSWORD)).getBytes())));
			httpConn.setRequestMethod( "POST" );
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			webServiceTemplate.getMarshaller().marshal(fileSdIType, new StreamResult(httpConn.getOutputStream()));
			int resultHTTPOperation = httpConn.getResponseCode();
			if (resultHTTPOperation == HttpStatus.OK.value()){
				return (JAXBElement<RispostaSdINotificaEsitoType>) webServiceTemplate.getUnmarshaller().unmarshal(new StreamSource(httpConn.getInputStream()));
			} else {
				throw new RuntimeException("Errore durante la comunicazione della notifica esito!");
			}
		} else if (tipoIntegrazioneSDI.equalsIgnoreCase(TipoIntegrazioneSDI.SDI.name())) {
			webServiceTemplate.setInterceptors(getInterceptors(serverParameters.get(NOTIFICA_ESISTO_SOAP_ACTION)));
			JAXBElement<RispostaSdINotificaEsitoType> response = (JAXBElement<RispostaSdINotificaEsitoType>) webServiceTemplate.marshalSendAndReceive(
					serverParameters.get(NOTIFICA_ESISTO_PD).concat("?NomeFile=").concat(nomeFile), fileSdIType);
			return response;			
		} else if (tipoIntegrazioneSDI.equalsIgnoreCase(TipoIntegrazioneSDI.PEC.name())) {
			return null;
		}
		return null;
	}

	private ClientInterceptor[] getInterceptors(final String soapAction){
		ClientInterceptor clientInterceptor = new ClientInterceptor() {				
			public boolean handleResponse(MessageContext messageContext)
					throws WebServiceClientException {
				return false;
			}
			
			public boolean handleRequest(MessageContext messageContext)
					throws WebServiceClientException {
				((SaajSoapMessage)messageContext.getRequest()).setSoapAction(soapAction);
				return true;
			}
			
			public boolean handleFault(MessageContext messageContext)
					throws WebServiceClientException {
				return false;
			}
			
			@SuppressWarnings("unused")
			public void afterCompletion(MessageContext messageContext, Exception ex)
					throws WebServiceClientException {				
			}
		};
		return new ClientInterceptor[]{clientInterceptor};		
	}	
}
