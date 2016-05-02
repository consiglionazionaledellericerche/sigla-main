package it.cnr.contab.docamm00.service;

import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class DocumentiCollegatiDocAmmService extends SiglaCMISService {
	
	private final static String TIPO_ALLEGATO_NON_INVIATO_SDI = "allegati_non_inviati_sdi";
	private final static String TIPO_ALLEGATO_FATTURA_DOPO_PROTOCOLLO = "stampa_fattura_dopo_protocollo";
	private final static String TIPO_ALLEGATO_FATTURA_PRIMA_PROTOCOLLO = "stampa_fattura_prima_protocollo";
	public List<String> getNodeRefDocumentoAttivo(Fattura_attivaBulk fattura)throws DetailedException{
		return getNodeRefDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}

	public List<String> getNodeRefDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		List<String> ids = new ArrayList<String>();
		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura); 
		ItemIterable<QueryResult> results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_DOPO_PROTOCOLLO);
		if (results.getTotalNumItems() == 0){
			results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_PRIMA_PROTOCOLLO);
			if (results.getTotalNumItems() == 0){
				return null;
			} else{
				getIds(ids, results);
			}
		} else {
			getIds(ids, results);
		}
		return ids;
	}

	public List<String> getNodeRefAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		List<String> ids = new ArrayList<String>();
		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura); 
		ItemIterable<QueryResult> results = getDocuments(folder, TIPO_ALLEGATO_NON_INVIATO_SDI);
		if (results.getTotalNumItems() == 0){
			return null;
		} else{
			getIds(ids, results);
		}
		return ids;
	}

	private ItemIterable<QueryResult> getDocuments(String folder, String tipoAllegato)
			throws ApplicationException {
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" join sigla_fatture_attachment:"+tipoAllegato+" allegati on doc.cmis:objectId = allegati.cmis:objectId");
		query.append(" where IN_FOLDER(doc, '").append(folder).append("')");
		ItemIterable<QueryResult> results = search(query);
		return results;
	}

	private String getFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo,
			Long pgFattura) throws DetailedException, ApplicationException {
		Folder node = getNodeFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		String folder = (String) node.getPropertyValue(PropertyIds.OBJECT_ID);
		return folder;
	}

	private Folder getNodeFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo,
			Long pgFattura) throws DetailedException, ApplicationException {
		Folder node = recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
		if (node == null){
			throw new ApplicationException("Non esistono documenti collegati alla fattura.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+" numero:"+pgFattura);
		}
		return node;
	}

	private void getIds(List<String> ids, ItemIterable<QueryResult> results) {
		for (QueryResult nodeFile : results) {
			String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
			ids.add(file);
		}
	}
	
	public Folder recuperoFolderFattura(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
		int posizionePunto = cdUo.indexOf(".");
		StringBuffer query = new StringBuffer("select fat.cmis:objectId from sigla_fatture:fatture_attive fat join strorg:uo uo on fat.cmis:objectId = uo.cmis:objectId ");
		query.append(" join strorg:cds cds on fat.cmis:objectId = cds.cmis:objectId ");
		query.append(" where fat.sigla_fatture:esercizio = ").append(esercizio);
		query.append(" and fat.sigla_fatture:pg_fattura = ").append(pgFattura);
		query.append(" and uo.strorguo:codice like '").append(cdUo.substring(0, posizionePunto)+"%").append("'");
		query.append(" and cds.strorgcds:codice = '").append(cds).append("'");
		//		query.append(" and contabili.sigla_contabili_aspect:num_mandato = ").append(pgMandato);
		//		query.append(" order by doc.cmis:creationDate DESC");
		ItemIterable<QueryResult> resultsFolder = search(query);
		if (resultsFolder.getTotalNumItems() == 0)
			return null;
		else if (resultsFolder.getTotalNumItems() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' fatture.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+
					" numero:"+pgFattura);
		} else {
			for (QueryResult queryResult : resultsFolder) {
				return (Folder) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID));
			}
		}
		return null;
	}
	
	public InputStream getStreamDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
		List<String> ids = getNodeRefDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		return getStream(ids);
	}
	
	public InputStream getStreamAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
		List<String> ids = getNodeRefAllegatiDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
		return getStream(ids);
	}

	private InputStream getStream(List<String> ids)
			throws ApplicationException, COSVisitorException, IOException {
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
	
	public InputStream getStreamDocumento(Fattura_attivaBulk fattura) throws Exception{
		return getStreamDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}
	
	public InputStream getStreamAllegatiDocumento(Fattura_attivaBulk fattura) throws Exception{
		return getStreamAllegatiDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
	}
}