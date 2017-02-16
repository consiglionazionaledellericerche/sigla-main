package it.cnr.contab.missioni00.service;

import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.util.StringUtils;

public class MissioniCMISService extends SiglaCMISService {
	
	private final static String ALLEGATI = "allegati";
	private final static String ALLEGATI_ANTICIPO = "allegati_anticipo";
	private final static String RICHIESTA_AUTO_PROPRIA = "uso_auto_propria";
	private final static String RICHIESTA_ANTICIPO = "richiesta_anticipo";
	private final static String ORDINE_MISSIONE = "ordine";
	private final static String SCONTRINI = "scontrini";
	private final static String RIMBORSO_MISSIONE = "rimborso";

	
	
	public ItemIterable<CmisObject> getFilesOrdineMissione(MissioneBulk missione) throws ComponentException{
		if (!StringUtils.isEmpty(missione.getIdFolderOrdineMissione())){
			Folder folderOrdine = null;
			try{
				folderOrdine = (Folder) getNodeByNodeRef(missione.getIdFolderOrdineMissione());
				
			} catch (CmisObjectNotFoundException ex){
				return null;
			}
			if (folderOrdine != null){
		        ItemIterable<CmisObject> children = folderOrdine.getChildren();
		        ItemIterable<QueryResult> results = getDocuments(missione.getIdFolderOrdineMissione(), null);
				for (QueryResult nodeFile : results) {
					String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
				}
		        return children;
			}
		}
		return null;
	}

	public ItemIterable<CmisObject> getFilesRimborsoMissione(MissioneBulk missione) throws ComponentException{
		if (!StringUtils.isEmpty(missione.getIdFolderRimborsoMissione())){
			Folder folderRimborso = null;
			try{
				folderRimborso = (Folder) getNodeByNodeRef(missione.getIdFolderRimborsoMissione());
				
			} catch (CmisObjectNotFoundException ex){
				return null;
			}
			if (folderRimborso != null){
		        ItemIterable<CmisObject> children = folderRimborso.getChildren();
		        ItemIterable<QueryResult> results = getDocuments(missione.getIdFolderRimborsoMissione(), null);
				for (QueryResult nodeFile : results) {
					String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
				}
		        return children;
			}
		}
		return null;
	}

//	public List<String> getNodeRefDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
//		List<String> ids = new ArrayList<String>();
//		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura); 
//		ItemIterable<QueryResult> results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_DOPO_PROTOCOLLO);
//		if (results.getTotalNumItems() == 0){
//			results = getDocuments(folder, TIPO_ALLEGATO_FATTURA_PRIMA_PROTOCOLLO);
//			if (results.getTotalNumItems() == 0){
//				return null;
//			} else{
//				getIds(ids, results);
//			}
//		} else {
//			getIds(ids, results);
//		}
//		return ids;
//	}
//
//	private List<String> getNodeRefFatturaAttivaXmlFirmato(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
//		List<String> ids = new ArrayList<String>();
//		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura); 
//		ItemIterable<QueryResult> results = getDocuments(folder, FILE_FATTURA_XML_FIRMATO);
//		if (results.getTotalNumItems() == 0)
//			return null;
//		else {
//			for (QueryResult nodeFile : results) {
//				String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
//				ids.add(file);
//			}
//		}
//		return ids;
//	}
//	
//	public List<String> getNodeRefAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura)throws DetailedException{
//		List<String> ids = new ArrayList<String>();
//		String folder = getFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura); 
//		ItemIterable<QueryResult> results = getDocuments(folder, TIPO_ALLEGATO_NON_INVIATO_SDI);
//		if (results.getTotalNumItems() == 0){
//			return null;
//		} else{
//			getIds(ids, results);
//		}
//		return ids;
//	}
//
	private ItemIterable<QueryResult> getDocuments(String folder, String tipoAllegato)
			throws ApplicationException {
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
//		query.append(" join sigla_fatture_attachment:"+tipoAllegato+" allegati on doc.cmis:objectId = allegati.cmis:objectId");
		query.append(" where IN_FOLDER(doc, '").append(folder).append("')");
		ItemIterable<QueryResult> results = search(query);
		for (QueryResult nodeFile : results) {
			String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
		}
		return results;
	}
//
//	private String getFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo,
//			Long pgFattura) throws DetailedException, ApplicationException {
//		Folder node = getNodeFolderDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
//		String folder = (String) node.getPropertyValue(PropertyIds.OBJECT_ID);
//		return folder;
//	}
//
//	private Folder getNodeFolderDocumentoAttivo(Integer esercizio, String cds, String cdUo,
//			Long pgFattura) throws DetailedException, ApplicationException {
//		Folder node = recuperoFolderFattura(esercizio, cds, cdUo, pgFattura);
//		if (node == null){
//			throw new ApplicationException("Non esistono documenti collegati alla fattura.  Anno:"+ esercizio+ " cds:" +cds +" uo:"+cdUo+" numero:"+pgFattura);
//		}
//		return node;
//	}
//
//	private void getIds(List<String> ids, ItemIterable<QueryResult> results) {
//		for (QueryResult nodeFile : results) {
//			String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
//			ids.add(file);
//		}
//	}
//	

	public Document recuperoFlows(String idFlusso)throws DetailedException{
		StringBuffer query = new StringBuffer("SELECT alfcmis:nodeRef,cmis:name from wfcnr:parametriFlusso ");
		query.append(" where wfcnr:wfInstanceId = '").append(idFlusso).append("'");
		query.append(" and wfcnr:tipologiaDocSpecifica = 'Riepilogo Flusso'");
		ItemIterable<QueryResult> resultsFolder = search(query);
		if (resultsFolder.getTotalNumItems() == 0)
			return null;
		else if (resultsFolder.getTotalNumItems() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' Riepiloghi Flusso.  ID Flusso:"+ idFlusso);
		} else {
			for (QueryResult queryResult : resultsFolder) {
				return (Document) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID));
			}
		}
		return null;
	}
//	
//	public InputStream getStreamDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
//		List<String> ids = getNodeRefDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
//		return getStream(ids);
//	}
//	
//	public InputStream getStreamAllegatiDocumentoAttivo(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
//		List<String> ids = getNodeRefAllegatiDocumentoAttivo(esercizio, cds, cdUo, pgFattura);
//		return getStream(ids);
//	}
//
//	private InputStream getStream(List<String> ids)
//			throws ApplicationException, COSVisitorException, IOException {
//		if (ids != null){
//			if (ids.size() == 1){
//				try{
//					return getResource(getNodeByNodeRef(ids.get(0)));
//				}catch (CmisObjectNotFoundException _ex){
//				}
//			}else{
//				PDFMergerUtility ut = new PDFMergerUtility();
//				ut.setDestinationStream(new ByteArrayOutputStream());
//				try {
//					for (String id : ids) {
//						ut.addSource(getResource(getNodeByNodeRef(id)));
//					}
//					ut.mergeDocuments();
//					return new ByteArrayInputStream(((ByteArrayOutputStream)ut.getDestinationStream()).toByteArray());
//				} catch (COSVisitorException e) {
//					throw e;
//				} catch (IOException e) {
//					throw e;
//				}catch (CmisObjectNotFoundException _ex){
//				}
//			}
//		}
//		return null;
//	}
//	
//	public InputStream getStreamXmlFirmatoFatturaAttiva(Integer esercizio, String cds, String cdUo, Long pgFattura) throws Exception{
//		List<String> ids = getNodeRefFatturaAttivaXmlFirmato(esercizio, cds, cdUo, pgFattura);
//		return getStream(ids);
//	}
//
//	public InputStream getStreamDocumento(Fattura_attivaBulk fattura) throws Exception{
//		return getStreamDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
//	}
//	
//	public InputStream getStreamAllegatiDocumento(Fattura_attivaBulk fattura) throws Exception{
//		return getStreamAllegatiDocumentoAttivo(fattura.getEsercizio(), fattura.getCd_cds(), fattura.getCd_uo(), fattura.getPg_fattura_attiva());
//	}
}
