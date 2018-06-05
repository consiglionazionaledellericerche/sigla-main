package it.cnr.contab.missioni00.service;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MissioniCMISService extends StoreService {
    private transient static final Logger logger = LoggerFactory.getLogger(MissioniCMISService.class);
	public static final String ASPECT_MISSIONE_SIGLA_DETTAGLIO = "P:missioni_sigla_attachment:scontrini";
	public static final String ASPECT_MISSIONE_RIMOBORSO_DETTAGLIO_SCONTRINI = "P:missioni_rimborso_attachment:scontrini";
	public static final String ASPECT_ALLEGATI_MISSIONE_SIGLA = "P:missioni_sigla_attachment:allegati";
	public static final String ASPECT_CMIS_MISSIONE_SIGLA = "P:missioni_sigla:missione_sigla";
	public static final String CMIS_MISSIONE_SIGLA_ANNO = "missioni_sigla:anno";
	public static final String CMIS_MISSIONE_SIGLA_NUMERO = "missioni_sigla:numero";
	public static final String CMIS_MISSIONE_SIGLA_OGGETTO = "missioni_sigla:oggetto";
	public static final String CMIS_MISSIONE_DETTAGLIO_SIGLA_RIGA = "missioni_dettaglio_sigla:riga";

	public List<StorageObject> getFilesOrdineMissione(MissioneBulk missione) throws ComponentException{
		return Optional.ofNullable(missione.getIdFolderOrdineMissione())
				.map(s -> getStorageObjectBykey(s))
				.map(storageObject -> getChildren(storageObject.getKey()))
				.orElse(null);
	}

	public String getCMISPathFromFolderRimborso(MissioneBulk missione) {
		return Optional.ofNullable(getStorageObjectBykey(missione.getIdFolderRimborsoMissione()))
				.map(StorageObject::getPath)
				.orElse(null);
	}

	public String getCMISPathFromFolderDettaglio(Missione_dettaglioBulk dettaglio) {
		return Optional.ofNullable(getStorageObjectBykey(dettaglio.getIdFolderDettagliGemis()))
				.map(StorageObject::getPath)
				.orElse(null);
	}

	private String getCMISPathFromFolder(StorageObject folderRimborso) {
		return Optional.ofNullable(folderRimborso)
				.map(StorageObject::getPath )
				.orElse(null);
	}

	public List<StorageObject> getFilesRimborsoMissione(MissioneBulk missione) {
		return Optional.ofNullable(missione.getIdFolderRimborsoMissione())
				.map(key -> getStorageObjectBykey(key))
				.map(storageObject -> getChildren(storageObject.getKey()))
				.orElse(null);
	}

	public List<StorageObject> getFilesMissioneSigla(MissioneBulk missione) throws ApplicationException {
		return Optional.ofNullable(recuperoFolderMissioneSigla(missione))
				.map(storageObject -> getChildren(storageObject.getKey()))
				.orElse(null);
	}

	private List<StorageObject> getDocuments(String folder, String tipoAllegato) {
		return getChildren(folder);
	}

	public StorageObject recuperoFlows(String idFlusso) throws DetailedException{
	    try {
            StringBuffer query = new StringBuffer("SELECT alfcmis:nodeRef, cmis:name, cmis:objectId from wfcnr:parametriFlusso ");
            query.append(" where wfcnr:wfInstanceId = '").append(idFlusso).append("'");
            query.append(" and wfcnr:tipologiaDocSpecifica = 'Riepilogo Flusso'");
            List<StorageObject> resultsFolder = search(query.toString());
            if (resultsFolder.size() == 0)
                return null;
            else if (resultsFolder.size() > 1){
                throw new ApplicationException("Errore di sistema, esistono sul documentale piu' Riepiloghi Flusso.  ID Flusso:"+ idFlusso);
            } else {
                return getStorageObjectBykey(resultsFolder.get(0).getKey());
            }
        } catch(RuntimeException _ex) {
            logger.error("Errore nel recupero del flusso", _ex);
	        return null;
        }
	}

	public StorageObject recuperoFolderMissioneSigla(MissioneBulk missione) throws ApplicationException {
		final List<String> path = Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathMissioni(),
				Optional.ofNullable(missione)
						.flatMap(missioneBulk -> Optional.ofNullable(missioneBulk.getCd_unita_organizzativa()))
								.orElse(""),
				"Rimborso Missione",
				Optional.ofNullable(missione)
						.flatMap(missioneBulk ->Optional.ofNullable(missione.getEsercizio()))
						.map(esercizio -> "Anno ".concat(String.valueOf(esercizio)))
						.orElse("0")
		);
		return Optional.ofNullable(missione)
                .map(missioneBulk -> Stream.concat(path.stream(), Stream.of(missione.constructCMISNomeFile()))
                        .collect(Collectors.joining(StorageService.SUFFIX)))
                .flatMap(pathRimborso -> Optional.ofNullable(getStorageObjectByPath(pathRimborso)))
                .orElseGet(() ->  getStorageObjectByPath(
                        createFolderMissioneSiglaIfNotPresent(path.stream().collect(Collectors.joining(StorageService.SUFFIX)), missione))
                );
	}

	public String createFolderMissioneSiglaIfNotPresent(String  path, MissioneBulk missione) {
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String name = missione.constructCMISNomeFile();
		String folderName = name;
		folderName = sanitizeFolderName(folderName);
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "F:missioni_sigla:main");
		metadataProperties.put(StoragePropertyNames.DESCRIPTION.value(), folderName);
		metadataProperties.put(StoragePropertyNames.NAME.value(), folderName);
		metadataProperties.put(StoragePropertyNames.TITLE.value(), folderName);
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
		metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspectsToAdd);
		path = createFolderIfNotPresent(getStorageObjectByPath(path, true, true).getPath(), folderName, metadataProperties);
		return path;
	}

	public String createFolderDettaglioIfNotPresent(String path, Missione_dettaglioBulk dettaglio) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String name = dettaglio.constructCMISNomeFile();
		String folderName = name;
		folderName = sanitizeFolderName(folderName);
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "F:missioni_dettaglio_sigla:main");
		metadataProperties.put(StoragePropertyNames.DESCRIPTION.value(), folderName);
		metadataProperties.put(StoragePropertyNames.NAME.value(), folderName);
		metadataProperties.put(StoragePropertyNames.TITLE.value(), folderName);
		metadataProperties.put(MissioniCMISService.CMIS_MISSIONE_DETTAGLIO_SIGLA_RIGA, dettaglio.getPg_riga());

		List<String> aspectsToAdd = new ArrayList<String>();
		aspectsToAdd.add("P:cm:titled");
		metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspectsToAdd);
		path = createFolderIfNotPresent(getStorageObjectByPath(path, true, true).getPath(), folderName, metadataProperties);
		return path;
	}
}