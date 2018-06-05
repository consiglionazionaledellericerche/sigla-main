package it.cnr.contab.ordmag.ordini.service;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

public class OrdineAcqCMISService extends StoreService {
	
	public static final String ASPECT_STAMPA_ORDINI = "P:ordini_acq_attachment:stampa";
	public static final String ASPECT_ORDINI_DETTAGLIO = "P:ordini_acq_attachment:allegati_dettaglio";
	public static final String ASPECT_ALLEGATI_ORDINI = "P:ordini_acq_attachment:allegati";
	public static final String CMIS_ORDINI_ACQ_ANNO = "ordini_acq:anno";
	public static final String CMIS_ORDINI_ACQ_NUMERO = "ordini_acq:numero";
	public static final String CMIS_ORDINI_ACQ_OGGETTO = "ordini_acq:oggetto";
	public static final String CMIS_ORDINI_ACQ_DETTAGLIO_RIGA = "ordini_acq_dettaglio:riga";
	public static final String CMIS_ORDINI_ACQ_UOP = "ordini_acq:cd_unita_operativa";
	public static final String CMIS_ORDINI_ACQ_NUMERATORE = "ordini_acq:cd_numeratore";

    public List<StorageObject> getFilesOrdine(OrdineAcqBulk ordine) throws BusinessProcessException{
        return getChildren(recuperoFolderOrdineSigla(ordine).getKey());
    }

    private List<StorageObject> getDocuments(String storageObjectKey, String tipoAllegato) throws ApplicationException {
        return getChildren(storageObjectKey).stream()
                .filter(storageObject -> tipoAllegato == null || storageObject.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(tipoAllegato))
                .collect(Collectors.toList());
    }

	public StorageObject recuperoFolderOrdineSigla(OrdineAcqBulk ordine) throws BusinessProcessException{
        return getStorageObjectByPath(getStorePath(ordine));
	}
	
	public String createFolderRichiestaIfNotPresent(String path, OrdineAcqBulk ordine) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String folderName = sanitizeFolderName(ordine.constructCMISNomeFile());
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "F:ordini_richieste:main");
		metadataProperties.put(StoragePropertyNames.NAME.value(), folderName);
		metadataProperties.put(OrdineAcqCMISService.CMIS_ORDINI_ACQ_NUMERATORE, ordine.getCdNumeratore());
		metadataProperties.put(OrdineAcqCMISService.CMIS_ORDINI_ACQ_ANNO, ordine.getEsercizio());
		metadataProperties.put(OrdineAcqCMISService.CMIS_ORDINI_ACQ_NUMERO, ordine.getNumero());
		metadataProperties.put(OrdineAcqCMISService.CMIS_ORDINI_ACQ_UOP, ordine.getCdUnitaOperativa());
		metadataProperties.put("sigla_commons_aspect:utente_applicativo", ordine.getUtuv());
		List<String> aspectsToAdd = new ArrayList<String>();
		aspectsToAdd.add("P:cm:titled");
		aspectsToAdd.add("P:sigla_commons_aspect:utente_applicativo_sigla");
        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspectsToAdd);
        return createFolderIfNotPresent(path, folderName, metadataProperties);
	}

	public String getStorePath(OrdineAcqBulk allegatoParentBulk) throws BusinessProcessException{
		try {
            String path = Arrays.asList(
                    SpringUtil.getBean(StorePath.class).getPathRichiesteOrdini(),
                    allegatoParentBulk.getCdUnitaOperativa(),
                    allegatoParentBulk.getCdNumeratore(),
                    Optional.ofNullable(allegatoParentBulk.getEsercizio())
                            .map(esercizio -> "Anno ".concat(String.valueOf(esercizio)))
                            .orElse("0")
            ).stream().collect(
                    Collectors.joining(StorageService.SUFFIX)
            );
            return createFolderRichiestaIfNotPresent(path, allegatoParentBulk);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
	}

    public InputStream getStreamOrdine(OrdineAcqBulk ordine) throws Exception{
        return getFilesOrdine(ordine).stream()
                .filter(storageObject -> hasAspect(storageObject, ASPECT_STAMPA_ORDINI))
                .findAny().map(
                        storageObject -> getResource(storageObject.getKey())
                ).orElse(null);
    }
}
