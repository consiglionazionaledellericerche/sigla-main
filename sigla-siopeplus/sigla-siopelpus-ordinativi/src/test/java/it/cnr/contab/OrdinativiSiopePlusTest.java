package it.cnr.contab;

import it.cnr.contab.model.Lista;
import it.cnr.contab.model.Risultato;
import it.cnr.contab.service.OrdinativiSiopePlusService;
import it.cnr.jada.firma.arss.ArubaSignServiceClient;
import it.cnr.jada.firma.arss.ArubaSignServiceException;
import it.cnr.jada.firma.arss.stub.XmlSignatureType;
import it.siopeplus.*;
import it.siopeplus.custom.ObjectFactory;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class OrdinativiSiopePlusTest {

    private Properties properties = new Properties();

    private static final String USERNAME = "utentefr";
    private static final String PASSWORD = "utentefr123";
    private static final String OTP = "6629961578";

    private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Before
    public void loadProperties() throws IOException {
        properties.load(this.getClass().getResourceAsStream("/META-INF/spring/siopeplus.properties"));
    }

    public Lista getACK() {
        OrdinativiSiopePlusService ordinativiSiopePlusService = new OrdinativiSiopePlusService();
        ordinativiSiopePlusService.urlACK = "https://certa2a.siopeplus.it/v1/A2A-31432329/PA/O5WZO8/flusso/ack/";
        ordinativiSiopePlusService.password = properties.getProperty("siopeplus.certificate.password");
        final Lista lista = ordinativiSiopePlusService.getListaMessaggi(OrdinativiSiopePlusService.Esito.ACK, null, null, null, null);
        Assert.notNull(lista);
        return lista;
    }

    @Test
    public void downloadACK() {
        OrdinativiSiopePlusService ordinativiSiopePlusService = new OrdinativiSiopePlusService();
        ordinativiSiopePlusService.password = properties.getProperty("siopeplus.certificate.password");
        final Lista lista = getACK();
        Optional.ofNullable(lista.getRisultati())
                .orElse(Collections.emptyList())
                .stream()
                .forEach(risultato -> {
                    System.out.println(risultato);
                    final MessaggioAckSiope messaggioAckSiope =
                            ordinativiSiopePlusService.getLocation(risultato.getLocation(), MessaggioAckSiope.class).getObject();
                    Assert.notNull(messaggioAckSiope);
                    System.out.println(messaggioAckSiope.getIdentificativoFlusso());
                });

    }

    @Test
    public void postFLUSSO() throws JAXBException, IOException, DatatypeConfigurationException, ArubaSignServiceException {
        final InputStream inputStream = generaFlusso();
        OrdinativiSiopePlusService ordinativiSiopePlusService = new OrdinativiSiopePlusService();
        ordinativiSiopePlusService.urlFlusso = "https://certa2a.siopeplus.it/v1/A2A-31432329/PA/O5WZO8/flusso/";
        ordinativiSiopePlusService.password = "contab";
        final Risultato risultato = ordinativiSiopePlusService.postFlusso(inputStream);
        Assert.notNull(risultato);
    }

    private InputStream generaFlusso() throws JAXBException, IOException, DatatypeConfigurationException, ArubaSignServiceException {

        LocalDateTime date = LocalDateTime.now();

        final it.siopeplus.ObjectFactory objectFactory = new it.siopeplus.ObjectFactory();
        FlussoOrdinativi flussoOrdinativi = objectFactory.createFlussoOrdinativi();

        final CtTestataFlusso testataFlusso = objectFactory.createCtTestataFlusso();
        testataFlusso.setCodiceABIBT("01005");
        testataFlusso.setRiferimentoEnte("A2A-31432329");
        testataFlusso.setIdentificativoFlusso("TEST-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        testataFlusso.setDataOraCreazioneFlusso(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterTime.format(date)));
        testataFlusso.setCodiceEnte("O5WZO8");
        testataFlusso.setCodiceEnteBT("0000767");
        testataFlusso.setCodiceTramiteEnte("A2A-31432329");
        testataFlusso.setCodiceTramiteBT("A2A-80647560");
        testataFlusso.setDescrizioneEnte("Consiglio Nazionale delle Ricerche");
        testataFlusso.setCodiceIstatEnte("000713516000000");
        testataFlusso.setCodiceFiscaleEnte("80054330586");
        flussoOrdinativi.getContent().add(objectFactory.createTestataFlusso(testataFlusso));

        flussoOrdinativi.getContent().add(objectFactory.createEsercizio(2018));

        final Mandato mandato = objectFactory.createMandato();
        mandato.setTipoOperazione("INSERIMENTO");
        mandato.setNumeroMandato(101047);
        mandato.setDataMandato(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterDate.format(date)));
        mandato.setImportoMandato(BigDecimal.valueOf(1150.00));
        mandato.setContoEvidenza("1");

        Mandato.InformazioniBeneficiario informazioniBeneficiario = objectFactory.createMandatoInformazioniBeneficiario();
        informazioniBeneficiario.setProgressivoBeneficiario(1);
        informazioniBeneficiario.setImportoBeneficiario(BigDecimal.valueOf(1150.00));
        informazioniBeneficiario.setTipoPagamento("CASSA");
        informazioniBeneficiario.setDestinazione("LIBERA");

        Mandato.InformazioniBeneficiario.Classificazione classificazione = objectFactory.createMandatoInformazioniBeneficiarioClassificazione();
        classificazione.setCodiceCgu("7019999999");
        classificazione.setImporto(BigDecimal.valueOf(1150.00));
        CtClassificazioneDatiSiopeUscite ctClassificazioneDatiSiopeUscite = objectFactory.createCtClassificazioneDatiSiopeUscite();

        ctClassificazioneDatiSiopeUscite.getTipoDebitoSiopeNcAndCodiceCigSiopeOrMotivoEsclusioneCigSiope().add(StTipoDebitoNonCommerciale.NON_COMMERCIALE);

        classificazione.setClassificazioneDatiSiopeUscite(ctClassificazioneDatiSiopeUscite);
        informazioniBeneficiario.getClassificazione().add(classificazione);

        Mandato.InformazioniBeneficiario.Bollo bollo = objectFactory.createMandatoInformazioniBeneficiarioBollo();
        bollo.setAssoggettamentoBollo("ESENTE BOLLO");
        bollo.setCausaleEsenzioneBollo("ESENTE -  Pagamenti effettuati con accred. in c/c bancario o postale (art. 7 tabella D.P.R. 642/72)");
        informazioniBeneficiario.setBollo(bollo);

        Mandato.InformazioniBeneficiario.Spese spese = objectFactory.createMandatoInformazioniBeneficiarioSpese();
        spese.setSoggettoDestinatarioDelleSpese("A CARICO BENEFICIARIO");
        informazioniBeneficiario.setSpese(spese);

        Beneficiario beneficiario = objectFactory.createBeneficiario();
        beneficiario.setAnagraficaBeneficiario("PARDINI ANTONELLA");
        beneficiario.setIndirizzoBeneficiario("VIA ARGINE VECCHIO, 357");
        beneficiario.setCapBeneficiario("56019");
        beneficiario.setLocalitaBeneficiario("VECCHIANO");
        beneficiario.setProvinciaBeneficiario("PI");
        informazioniBeneficiario.setBeneficiario(beneficiario);

        informazioniBeneficiario.setCausale("Pagamento netto per mancata corresponsione retribuzione di novembre 2018 a Pardini Antonella, matr");
        mandato.getInformazioniBeneficiario().add(informazioniBeneficiario);

        flussoOrdinativi.getContent().add(objectFactory.createMandato(mandato));

        final Reversale reversale = objectFactory.createReversale();
        reversale.setTipoOperazione("INSERIMENTO");
        reversale.setNumeroReversale(25383);
        reversale.setDataReversale(DatatypeFactory.newInstance().newXMLGregorianCalendar(formatterDate.format(date)));
        reversale.setImportoReversale(BigDecimal.valueOf(167.36));
        reversale.setContoEvidenza("1");

        Reversale.InformazioniVersante informazioniVersante = objectFactory.createReversaleInformazioniVersante();
        informazioniVersante.setProgressivoVersante(1);
        informazioniVersante.setImportoVersante(BigDecimal.valueOf(167.36));
        informazioniVersante.setTipoRiscossione("CASSA");
        informazioniVersante.setTipoEntrata("INFRUTTIFERO");
        informazioniVersante.setDestinazione("LIBERA");

        Reversale.InformazioniVersante.Classificazione classificazione1 = objectFactory.createReversaleInformazioniVersanteClassificazione();
        classificazione1.setCodiceCge("9010102001");
        classificazione1.setImporto(BigDecimal.valueOf(167.36));
        CtClassificazioneDatiSiopeEntrate ctClassificazioneDatiSiopeEntrate = objectFactory.createCtClassificazioneDatiSiopeEntrate();
        ctClassificazioneDatiSiopeEntrate.getContent().add(
                objectFactory.createCtClassificazioneDatiSiopeEntrateTipoDebitoSiopeNc(StTipoDebitoNonCommerciale.NON_COMMERCIALE)
        );
        classificazione1.setClassificazioneDatiSiopeEntrate(ctClassificazioneDatiSiopeEntrate);
        informazioniVersante.getClassificazione().add(classificazione1);

        Reversale.InformazioniVersante.Bollo bollo1 = objectFactory.createReversaleInformazioniVersanteBollo();
        bollo1.setAssoggettamentoBollo("ESENTE BOLLO");
        bollo1.setCausaleEsenzioneBollo("ESENTE -  Pagamenti effettuati con accred. in c/c bancario o postale (art. 7 tabella D.P.R. 642/72)");
        informazioniVersante.setBollo(bollo1);

        final Versante versante = objectFactory.createVersante();
        versante.setAnagraficaVersante("Sigma Aldrich s.r.l.");
        informazioniVersante.setVersante(versante);

        informazioniVersante.setCausale("REVERSALE IVA");
        reversale.getInformazioniVersante().add(informazioniVersante);


        flussoOrdinativi.getContent().add(objectFactory.createReversale(reversale));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JAXBContext jc = JAXBContext.newInstance("it.siopeplus");
        Marshaller jaxbMarshaller = jc.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        jaxbMarshaller.marshal(flussoOrdinativi, byteArrayOutputStream);

        String out = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        out = out.replace("</flusso_ordinativi>", "\n</flusso_ordinativi>");

        ArubaSignServiceClient client = new ArubaSignServiceClient();
        Properties props = getProperties();
        client.setProps(props);
        byte[] contentSigned = client.xmlSignature(USERNAME, PASSWORD, OTP, out.getBytes(), XmlSignatureType.XMLENVELOPED);

        Assert.isTrue(validateAgainstXSD(new ByteArrayInputStream(contentSigned), this.getClass().getResourceAsStream("/xsd/OPI_FLUSSO_ORDINATIVI_V_1_3_1.xsd")));

        return new ByteArrayInputStream(contentSigned);

    }


    private static Properties getProperties() throws IOException {
        InputStream is = OrdinativiSiopePlusTest.class.getClassLoader()
                .getResourceAsStream("aruba.properties");
        Properties props = new Properties();
        props.load(is);
        return props;
    }

    static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
    {
        try
        {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
}
