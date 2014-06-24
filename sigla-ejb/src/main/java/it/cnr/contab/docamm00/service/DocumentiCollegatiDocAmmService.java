package it.cnr.contab.docamm00.service;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import com.google.gson.Gson;

public class DocumentiCollegatiDocAmmService extends CMISService {
	private HttpClient httpClient;
	private Gson gson;
	
	public List<String> getNodeRefContabile(Fattura_attivaBulk fattura)throws DetailedException{
		return getNodeRefContabile(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva(), Filtro_ricerca_doc_ammVBulk.DOC_ATT_GRUOP);
	}

	public List<String> getNodeRefContabile(Integer esercizio, String cds, String cdUo, Long pgFattura, String tipoDocumento)throws DetailedException{
		List<String> ids = new ArrayList<String>();
		Node node = recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
		if (node == null){
			throw new ApplicationException("Non esistono documenti collegati alla fattura.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+" numero:"+pgFattura);
		}
		String folder = (String) node.getPropertyValue("cmis:objectId"); 
		StringBuffer query = new StringBuffer("select cmis:objectId from sigla_fatture_attachment:stampa_fattura_prima_protocollo ");
		//				query.append(" join sigla_contabili_aspect:document contabili on doc.cmis:objectId = contabili.cmis:objectId");
		query.append(" where IN_FOLDER('").append(folder).append("')");
		//				query.append(" and contabili.sigla_contabili_aspect:cds = '").append(cds).append("'");
		//				query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		//				query.append(" order by doc.cmis:creationDate DESC");
		ListNodePage<Node> results = search(query, Boolean.FALSE);

		if (results.isEmpty())
			return null;
		else {
			for (Node nodeFile : results) {
				String file = (String) nodeFile.getPropertyValue("cmis:objectId"); 
				ids.add(file);
			}
		}
		return ids;
	}
	
	public Node recuperoFolderFattura(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		int posizionePunto = cdUo.indexOf(".");
		StringBuffer query = new StringBuffer("select fat.cmis:objectId from sigla_fatture:fatture_attive fat join strorg:uo uo on fat.cmis:objectId = uo.cmis:objectId ");
		query.append(" join strorg:cds cds on fat.cmis:objectId = cds.cmis:objectId ");
		query.append(" where fat.sigla_fatture:esercizio = ").append(esercizio);
		query.append(" and fat.sigla_fatture:pg_fattura = ").append(pgFattura);
//		query.append(" and uo.strorguo:codice like '").append(cdUo.replace(".", "%")).append("'");
		query.append(" and uo.strorguo:codice like '").append(cdUo.substring(0, posizionePunto)+"%").append("'");
		query.append(" and cds.strorgcds:codice = '").append(cds).append("'");
		//		query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		//		query.append(" order by doc.cmis:creationDate DESC");
		ListNodePage<Node> resultsFolder = search(query, Boolean.FALSE);
		if (resultsFolder.isEmpty())
			return null;
		else if (resultsFolder.size() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' fatture.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+
					" numero:"+pgFattura);
		} else {
			return resultsFolder.get(0);
		}
	}
	
	public InputStream getStreamContabile(Integer esercizio, String cds, String cdUo, Long pgFattura, String tipoDocumento) throws Exception{
		List<String> ids = getNodeRefContabile(esercizio, cds, cdUo, pgFattura, tipoDocumento);
		if (ids != null){
			if (ids.size() == 1){
				try{
					return getResource(getNodeByNodeRef(ids.get(0)));
				}catch (CmisObjectNotFoundException _ex){
				}
			}else{
				PDFMergerUtility ut = new PDFMergerUtility();
				ut.setDestinationStream(new ByteArrayOutputStream());
				try {
					for (String id : ids) {
						ut.addSource(getResource(getNodeByNodeRef(id)));
					}
					ut.mergeDocuments();
					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
				} catch (COSVisitorException e) {
					throw e;
				} catch (IOException e) {
					throw e;
				}catch (CmisObjectNotFoundException _ex){
				}
			}
		}
		return null;
	}
	
	public InputStream getStreamContabile(Fattura_attivaBulk fattura) throws Exception{
		return getStreamContabile(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva(), Filtro_ricerca_doc_ammVBulk.DOC_ATT_GRUOP);
	}

	public Credentials getCredentials(){
		return systemCredentials;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
}
