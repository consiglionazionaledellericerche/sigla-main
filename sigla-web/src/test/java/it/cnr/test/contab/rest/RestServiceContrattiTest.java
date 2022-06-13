package it.cnr.test.contab.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.contab.web.rest.config.SIGLASecurityContext;
import it.cnr.contab.web.rest.model.AttachmentContratto;
import it.cnr.contab.web.rest.model.ContrattoDtoBulk;
import it.cnr.contab.web.rest.model.EnumTypeAttachmentContratti;
import it.cnr.jada.comp.ComponentException;
import it.cnr.si.spring.storage.MimeTypes;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;


@RunWith(JUnit4.class)
public class RestServiceContrattiTest {

    @Value("${test.value}")
    public String value;

    @Test
    public void testContrattiMaggioli()throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ContrattoDtoBulk c = new ContrattoDtoBulk();
        c.setEsercizio(2021);
        c.setCodiceFlussoAcquisti( "testCompleto2");
        c.setCd_unita_organizzativa("000.000");
        c.setCodfisPivaRupExt("ZNCMRT79E49H501E");
        c.setCodfisPivaAggiudicatarioExt("05923561004");
        c.setCodfisPivaFirmatarioExt("ZNCMRT79E49H501E");

        c.setDs_atto("DECISIONE A CONTRARRE");
        c.setOggetto("Oggetto Test Contratto");

        c.setIm_contratto_passivo(new BigDecimal("1200"));
        c.setIm_contratto_passivo_netto(new BigDecimal("1000"));
        c.setCd_tipo_norma_perla("30D");
        //c.setCd_tipo_atto("DEL");
        //c.setCd_tipo_contratto
        //c.setCd_proc_amm();
            //c.setCd_organo();


        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        c.setDt_stipula(new java.sql.Timestamp(dateFormat.parse("20211122").getTime()));
        c.setDt_inizio_validita(new java.sql.Timestamp(dateFormat.parse("20211122").getTime()));

        c.setDt_fine_validita(new java.sql.Timestamp(dateFormat.parse("20241122").getTime()));
        c.setDt_registrazione(new java.sql.Timestamp(dateFormat.parse("20211122").getTime()));

        //c.setCdCigExt();
        //c.setCdCupExt();
        //Tipologia
        //Tipo_norma_perlaBulk tipoNormaPerla da aggiungere al contrarroDtoBulk ( tipoNormaPerla)
        //Procedura amministrativa da aggiungere al contrattoDtoBulk (Procedure_amministrativeBulk)
        List<AttachmentContratto> l = new ArrayList<AttachmentContratto>();
        AttachmentContratto a = new AttachmentContratto();
        a.setNomeFile("contratto.pdf");
        a.setMimeTypes(MimeTypes.PDF);

        InputStream is = this.getClass().getResourceAsStream("/contratto.pdf");
        byte[] bytes = IOUtils.toByteArray(is);
        byte[] encoded= Base64.getEncoder().encode(bytes);

        a.setTypeAttachment(EnumTypeAttachmentContratti.CONTRATTO_FLUSSO);
        a.setBytes(encoded);
        l.add(a);
        c.setAttachments(l);
        String myJson = null;
        try {
            myJson = mapper.writeValueAsString(c);
        } catch (Exception ex) {
            throw new ComponentException("Errore nella generazione del file JSON per l'esecuzione della stampa ( errore joson).",ex);
        }
        HttpEntity e = new StringEntity(myJson.toString());
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("MAGGIOLI", "MAGGIOLI");
        provider.setCredentials(AuthScope.ANY, credentials);
        HttpClient client=HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        HttpPost method = new HttpPost("http://localhost:8080/SIGLA/restapi/contrattoMaggioli");
        method.addHeader("Accept-Language", Locale.getDefault().toString());
        method.setHeader("Content-Type", "application/json;charset=UTF-8");
        method.setHeader(SIGLASecurityContext.X_SIGLA_CD_CDS,"000");
        method.setEntity(e);
        HttpResponse response = client.execute(method);//Replace HttpPost with HttpGet if you need to perform a GET to login
        int statusCode = response.getStatusLine().getStatusCode();


        System.out.println("Response Code :"+ statusCode);

    }

    @Test
    public void jsonTest()throws Exception {

        System.out.println(value);
        /*
        final String regex = "[0-9A-Z]{3}\\.[0-9A-Z]{3}";
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        String unita="00.aA1";
        Boolean b = pattern.matcher(unita).matches();
        Assert.assertTrue(b);


        String text = "2009-10-20";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parsedDate = dateFormat.parse("20211122");
        Timestamp tm=new java.sql.Timestamp(parsedDate.getTime());
        ContrattoDtoBulk c = new ContrattoDtoBulk();
        c.setEsercizio(2021);
        List<AttachmentContratto> l = new ArrayList<AttachmentContratto>();
        AttachmentContratto a = new AttachmentContratto();
        a.setNomeFile("contratto.pdf");
        a.setMimeTypes(MimeTypes.PDF);


         //file = new File(classLoader.getResource("contratto.pdf"));
        InputStream is = this.getClass().getResourceAsStream("/contratto.pdf");
        byte[] bytes = IOUtils.toByteArray(is);
        byte[] encoded= Base64.getEncoder().encode(bytes);

        a.setTypeAttachment(EnumTypeAttachmentContratti.CONTRATTO_FLUSSO);
        a.setBytes(encoded);
        l.add(a);
        c.setAttachments(l);

        StampaInventarioDTO dto = new StampaInventarioDTO();
        dto.setDescCatGrp("ciao");
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory();
        String myJson = null;
        try {
            myJson = mapper.writeValueAsString(c);
        } catch (Exception ex) {
            throw new ComponentException("Errore nella generazione del file JSON per l'esecuzione della stampa ( errore joson).",ex);
        }
        System.out.println(myJson);
    */
    }

}
