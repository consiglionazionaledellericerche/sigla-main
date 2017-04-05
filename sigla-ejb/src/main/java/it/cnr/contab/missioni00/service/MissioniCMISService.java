package it.cnr.contab.missioni00.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.springframework.util.StringUtils;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

public class MissioniCMISService extends SiglaCMISService {
	
	public static final String ASPECT_ALLEGATI_MISSIONE_SIGLA = "P:missioni_sigla_attachment:allegati";
	public static final String ASPECT_CMIS_MISSIONE_SIGLA = "P:missioni_sigla:missione_sigla";
	public static final String CMIS_MISSIONE_SIGLA_ANNO = "missioni_sigla:anno";
	public static final String CMIS_MISSIONE_SIGLA_NUMERO = "missioni_sigla:numero";
	public static final String CMIS_MISSIONE_SIGLA_OGGETTO = "missioni_sigla:oggetto";

	
	
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
	
	public CMISPath getCMISPathFromFolderRimborso(MissioneBulk missione) throws ComponentException{
		Folder folderRimborso = (Folder) getNodeByNodeRef(missione.getIdFolderRimborsoMissione());
		return getCMISPathFromFolder(folderRimborso);
	}

	private CMISPath getCMISPathFromFolder(Folder folderRimborso) {
		if (folderRimborso != null){
			return CMISPath.construct(folderRimborso.getPath());
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
		        return children;
			}
		}
		return null;
	}

	public ItemIterable<CmisObject> getFilesMissioneSigla(MissioneBulk missione) throws ComponentException{
			Folder folderMissione;
			try {
				folderMissione = recuperoFolderMissioneSigla(missione);
			} catch (DetailedException e) {
				throw new ComponentException(e);
			}
			
			if (folderMissione != null){
		        ItemIterable<CmisObject> children = folderMissione.getChildren();
		        return children;
			}
		return null;
	}

	private ItemIterable<QueryResult> getDocuments(String folder, String tipoAllegato)
			throws ApplicationException {
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" where IN_FOLDER(doc, '").append(folder).append("')");
		ItemIterable<QueryResult> results = search(query);
		for (QueryResult nodeFile : results) {
			String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
		}
		return results;
	}

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
				return (Document) getNodeByNodeRef((String) queryResult.getPropertyValueByQueryName(MissioniCMISService.ALFCMIS_NODEREF));
			}
		}
		return null;
	}

	public Folder recuperoFolderMissioneSigla(MissioneBulk missione)throws DetailedException{
		int posizionePunto = missione.getCd_unita_organizzativa().indexOf(".");
		StringBuffer query = new StringBuffer("select mis.cmis:objectId from missioni_sigla:main mis join strorg:uo uo on mis.cmis:objectId = uo.cmis:objectId ");
		query.append(" join strorg:cds cds on mis.cmis:objectId = cds.cmis:objectId ");
		query.append(" where mis.missioni_sigla:anno = ").append(missione.getEsercizio());
		query.append(" and mis.missioni_sigla:numero = ").append(missione.getPg_missione());
		query.append(" and uo.strorguo:codice like '").append(missione.getCd_unita_organizzativa().substring(0, posizionePunto)+"%").append("'");
		query.append(" and cds.strorgcds:codice = '").append(missione.getCd_cds()).append("'");
		ItemIterable<QueryResult> resultsFolder = search(query);
		if (resultsFolder.getTotalNumItems() == 0)
			return null;
		else if (resultsFolder.getTotalNumItems() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' Missioni.  Anno:"+ missione.getEsercizio()+ " cds:" +missione.getCd_cds() +" uo:"+missione.getCd_unita_organizzativa()+
					" numero:"+missione.getPg_missione());
		} else {
			for (QueryResult queryResult : resultsFolder) {
				return (Folder) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID));
			}
		}
		return null;
	}
	
	public CMISPath createLastFolderIfNotPresent(CMISPath cmisPath, MissioneBulk missione) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String name = missione.constructCMISNomeFile();
		String folderName = name;
		folderName = sanitizeFolderName(folderName);
		metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, "F:missioni_sigla:main");
		metadataProperties.put(MissioniCMISService.PROPERTY_DESCRIPTION, folderName);
		metadataProperties.put(PropertyIds.NAME, folderName);
		metadataProperties.put(MissioniCMISService.PROPERTY_TITLE, folderName);
		metadataProperties.put(MissioniCMISService.CMIS_MISSIONE_SIGLA_NUMERO, missione.getPg_missione());
		metadataProperties.put(MissioniCMISService.CMIS_MISSIONE_SIGLA_ANNO, missione.getEsercizio());
		metadataProperties.put(MissioniCMISService.CMIS_MISSIONE_SIGLA_OGGETTO, missione.getDs_missione());

		metadataProperties.put("sigla_commons_aspect:utente_applicativo", missione.getUtuv());
		metadataProperties.put("strorguo:codice", missione.getCd_unita_organizzativa());
		metadataProperties.put("strorgcds:codice", missione.getCd_cds());
		List<String> aspectsToAdd = new ArrayList<String>();
		aspectsToAdd.add("P:cm:titled");
		aspectsToAdd.add("P:strorg:cds");
		aspectsToAdd.add("P:strorg:uo");
		aspectsToAdd.add("P:sigla_commons_aspect:utente_applicativo_sigla");
		aspectsToAdd.add(MissioniCMISService.ASPECT_CMIS_MISSIONE_SIGLA);
		cmisPath = createFolderIfNotPresent(cmisPath, metadataProperties, aspectsToAdd, folderName);
		return cmisPath;
	}
}
