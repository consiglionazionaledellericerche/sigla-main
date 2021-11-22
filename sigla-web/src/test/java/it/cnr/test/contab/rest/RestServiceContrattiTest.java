package it.cnr.test.contab.rest;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.contab.ordmag.magazzino.dto.StampaInventarioDTO;
import it.cnr.contab.web.rest.model.AttachmentContratto;
import it.cnr.contab.web.rest.model.ContrattoDtoBulk;
import it.cnr.contab.web.rest.model.ContrattoMaggioliDTOBulk;
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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;


public class RestServiceContrattiTest {

    @Test
    public void testContrattiMaggioli()throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ContrattoDtoBulk c = new ContrattoDtoBulk();
        c.setEsercizio(2021);
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
        method.setEntity(e);
        HttpResponse response = client.execute(method);//Replace HttpPost with HttpGet if you need to perform a GET to login
        int statusCode = response.getStatusLine().getStatusCode();


        System.out.println("Response Code :"+ statusCode);

    }

    @Test
    public void jsonTest()throws Exception {
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

    }

}
