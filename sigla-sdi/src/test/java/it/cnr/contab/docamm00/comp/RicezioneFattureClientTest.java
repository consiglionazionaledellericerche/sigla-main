package it.cnr.contab.docamm00.comp;

import it.gov.fatturapa.FileSdIConMetadatiType;
import it.gov.fatturapa.RicezioneFatture;
import it.gov.fatturapa.RicezioneFattureService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;

import javax.activation.DataHandler;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class RicezioneFattureClientTest {
	private static final Logger LOGGER = Logger.getLogger(RicezioneFattureClientTest.class);
	//@Test
	public void testFatturaSingolaConUnaLineaDiFattura() throws IOException {
		BigInteger identificativoSdI = BigInteger.ONE;
		FileSdIConMetadatiType fileSdIType = getFileSdIType("IT01234567890_11001.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	//@Test
	public void testFatturaSingolaConPiuLineeDiFattura() throws IOException {
		BigInteger identificativoSdI = BigInteger.ONE.add(BigInteger.ONE);
		FileSdIConMetadatiType fileSdIType = getFileSdIType("IT01234567890_11002.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	//@Test
	public void testLottoDiFatture() throws IOException {
		BigInteger identificativoSdI = BigInteger.ONE.add(BigInteger.ONE).add(BigInteger.ONE);
		FileSdIConMetadatiType fileSdIType = getFileSdIType("IT01234567890_11003.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	//@Test
	public void testFatturaXSL() throws IOException {
		BigInteger identificativoSdI = BigInteger.valueOf(4);
		FileSdIConMetadatiType fileSdIType = getFileSdIType("IT01234567890_11004.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	//@Test
	public void testFatturaSigla100() throws IOException {
		BigInteger identificativoSdI = BigInteger.valueOf(100);
		FileSdIConMetadatiType fileSdIType = getFileSdIType("IT01138480031_00001.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}

	//@Test
	public void testFatturaSigla101() throws IOException {
		BigInteger identificativoSdI = BigInteger.valueOf(101);
		FileSdIConMetadatiType fileSdIType = getFileSdIType("ITGNFPQL50M09F158Y_00001.xml", identificativoSdI);
		RicezioneFattureService ricezioneFattureService = 
				new RicezioneFattureService(
						new URL("http://localhost:8180/SIGLA-SIGLAEJB_1/RicezioneFatture?wsdl"), 
						new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFattureService"));
		ricezioneFattureService.getPort(
				new QName("http://www.fatturapa.gov.it/sdi/ws/ricezione/v1.0", "RicezioneFatturePort"), 
				RicezioneFatture.class).riceviFatture(fileSdIType);
	    LOGGER.info("Chiamata WS effettuata.");
	}
	
	private FileSdIConMetadatiType getFileSdIType(String nomeFile, BigInteger identificativoSdI) {
		try {
			InputStream iss = RicezioneFattureClientTest.class.getClassLoader()
			        .getResourceAsStream(nomeFile);
			byte[] bytes = Base64.encodeBase64(IOUtils.toByteArray(iss));

			FileSdIConMetadatiType fileSdIConMetadatiType = new FileSdIConMetadatiType();
			fileSdIConMetadatiType.setIdentificativoSdI(identificativoSdI);
			fileSdIConMetadatiType.setNomeFile(nomeFile + ".p7m");
			fileSdIConMetadatiType.setNomeFileMetadati(nomeFile);
			fileSdIConMetadatiType.setMetadati(new DataHandler(new ByteArrayDataSource(bytes, "application/xml")));
			fileSdIConMetadatiType.setFile(new DataHandler(new ByteArrayDataSource(bytes, "application/p7m")));
			return fileSdIConMetadatiType;
		} catch (Exception e) {
		}
		return null;
	}

}