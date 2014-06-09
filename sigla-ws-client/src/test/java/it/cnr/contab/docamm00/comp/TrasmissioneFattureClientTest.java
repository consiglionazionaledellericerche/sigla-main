package it.cnr.contab.docamm00.comp;

import it.gov.fatturapa.FileSdIType;
import it.gov.fatturapa.ObjectFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TrasmissioneFattureClientTest {
	private static final Logger LOGGER = Logger.getLogger(TrasmissioneFattureClientTest.class);

	@Test
	public void testNotificaScarto() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_NS_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.notificaScarto(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	@Test
	public void testRicevutaConsegna() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_RC_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.ricevutaConsegna(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	@Test
	public void testNotificaMancataConsegna() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_MC_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.notificaMancataConsegna(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	
	@Test
	public void testNotificaEsito() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_NE_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.notificaEsito(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	@Test
	public void testNotificaDecorrenzaTermini() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_DT_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.notificaDecorrenzaTermini(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	@Test
	public void testAttestazioneTrasmissioneFattura() throws IOException {
		FileSdIType fileSdIType = getFileSdIType("IT01234567890_11111_AT_001.xml");
		TrasmissioneFattureClient client = new TrasmissioneFattureClient();
		client.attestazioneTrasmissioneFattura(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	private FileSdIType getFileSdIType(String nomeFile) {
		try {
			InputStream iss = TrasmissioneFattureClientTest.class.getClassLoader()
			        .getResourceAsStream(nomeFile);

		    ObjectFactory obj = new ObjectFactory();
			FileSdIType fileSdIType = obj.createFileSdIType();
			fileSdIType.setIdentificativoSdI(BigInteger.valueOf(1));
			fileSdIType.setNomeFile(nomeFile);
			fileSdIType.setFile(new DataHandler(new ByteArrayDataSource(iss, "application/xml")));

			return fileSdIType;
		} catch (Exception e) {
		}
		return null;
	}

}