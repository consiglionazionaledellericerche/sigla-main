package it.cnr.contab.ordmag.richieste.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaDettaglioBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.Introspector;

public class RichiesteCMISService extends StoreService {
	
	public static final String ASPECT_STAMPA_RICHIESTA_ORDINI = "P:ordini_richieste_attachment:stampa";
	public static final String ASPECT_RICHIESTA_ORDINI_DETTAGLIO = "P:ordini_richieste_attachment:allegati_dettaglio";
	public static final String ASPECT_ALLEGATI_RICHIESTA_ORDINI = "P:ordini_richieste_attachment:allegati";
	public static final String CMIS_RICHIESTA_ORDINI_ANNO = "ordini_richieste:anno";
	public static final String CMIS_RICHIESTA_ORDINI_NUMERO = "ordini_richieste:numero";
	public static final String CMIS_RICHIESTA_ORDINI_OGGETTO = "ordini_richieste:oggetto";
	public static final String CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA = "ordini_richieste_dettaglio:riga";
	public static final String CMIS_RICHIESTA_ORDINI_UOP = "ordini_richieste:cd_unita_operativa";
	public static final String CMIS_RICHIESTA_ORDINI_NUMERATORE = "ordini_richieste:cd_numeratore";
    public static final String F_ORDINI_RICHIESTE_DETTAGLIO_MAIN = "F:ordini_richieste_dettaglio:main";

    public List<StorageObject> getFilesRichiesta(RichiestaUopBulk richiesta) throws BusinessProcessException{
        return getChildren(recuperoFolderRichiestaSigla(richiesta).getKey());
	}

	private List<StorageObject> getDocuments(String storageObjectKey, String tipoAllegato) throws ApplicationException {
		return getChildren(storageObjectKey).stream()
				.filter(storageObject -> tipoAllegato == null || storageObject.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(tipoAllegato))
				.collect(Collectors.toList());
	}

	public StorageObject recuperoFolderRichiestaSigla(RichiestaUopBulk richiesta) throws BusinessProcessException {
	    return getStorageObjectByPath(getStorePath(richiesta));
	}

	public String getStorePath(RichiestaUopBulk richiestaUop) throws BusinessProcessException{
        try {
            String path = Arrays.asList(
                    SpringUtil.getBean(StorePath.class).getPathRichiesteOrdini(),
                    richiestaUop.getCdUnitaOperativa(),
                    richiestaUop.getCdNumeratore(),
                    Optional.ofNullable(richiestaUop.getEsercizio())
                            .map(esercizio -> "Anno ".concat(String.valueOf(esercizio)))
                            .orElse("0")
            ).stream().collect(
                    Collectors.joining(StorageService.SUFFIX)
            );
            path = createFolderRichiestaIfNotPresent(path, richiestaUop);
            return path;
        } catch (ComponentException e) {
            throw new BusinessProcessException(e);
        }
    }

    public String getStorePathDettaglio(RichiestaUopRigaBulk dettaglioBulk) throws BusinessProcessException{
        try {
            String path = getStorePath(dettaglioBulk.getRichiestaUop());
            path = createFolderDettaglioIfNotPresent(path, dettaglioBulk);
            return path;
        } catch (ComponentException e) {
            throw new BusinessProcessException(e);
        }
    }

	public String createFolderRichiestaIfNotPresent(String path, RichiestaUopBulk richiesta) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String folderName = sanitizeFolderName(richiesta.constructCMISNomeFile());
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "F:ordini_richieste:main");
		metadataProperties.put(StoragePropertyNames.NAME.value(), folderName);
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_NUMERATORE, richiesta.getCdNumeratore());
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_ANNO, richiesta.getEsercizio());
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_NUMERO, richiesta.getNumero());
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_UOP, richiesta.getCdUnitaOperativa());
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_OGGETTO, richiesta.getDsRichiesta());

		metadataProperties.put("sigla_commons_aspect:utente_applicativo", richiesta.getUtuv());
		List<String> aspectsToAdd = new ArrayList<String>();
		aspectsToAdd.add("P:cm:titled");
		aspectsToAdd.add("P:sigla_commons_aspect:utente_applicativo_sigla");
        metadataProperties.put(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(), aspectsToAdd);

        return createFolderIfNotPresent(getStorageObjectByPath(path, true, true).getPath(),
                folderName, metadataProperties);
	}

	public String createFolderDettaglioIfNotPresent(String path, RichiestaUopRigaBulk dettaglio) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String folderName = sanitizeFolderName(dettaglio.constructCMISNomeFile());
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), F_ORDINI_RICHIESTE_DETTAGLIO_MAIN);
		metadataProperties.put(StoragePropertyNames.NAME.value(), folderName);
		metadataProperties.put(RichiesteCMISService.CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA, dettaglio.getRiga());
        return createFolderIfNotPresent(getStorageObjectByPath(path, true, true).getPath(),
                folderName, metadataProperties);
	}
	
	public void recuperoAllegatiDettaglioRichiesta(RichiestaUopBulk allegatoParentBulk, StorageObject storageObject)
			throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Optional.ofNullable(storageObject)
                .map(storageObject1 -> storageObject1.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()))
                .filter(objectTypeId -> objectTypeId.equals(F_ORDINI_RICHIESTE_DETTAGLIO_MAIN))
                .ifPresent(s -> {
                    getChildren(storageObject.getKey()).stream()
                            .filter(storageObject2 -> storageObject2.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()).
                                    equals(StoragePropertyNames.CMIS_DOCUMENT.value()))
                            .forEach(storageObject2 -> {
                                BigInteger riga = storageObject2.getPropertyValue(CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA);
                                try {
                                    AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk) Introspector.newInstance(AllegatoRichiestaDettaglioBulk.class, storageObject);
                                    allegato.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                                    allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                                    allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                                    allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                                    allegato.setCrudStatus(OggettoBulk.NORMAL);
                                    for (java.util.Iterator i = allegatoParentBulk.getRigheRichiestaColl().iterator(); i.hasNext(); ) {
                                        RichiestaUopRigaBulk richiestaUopRigaBulk = (RichiestaUopRigaBulk) i.next();
                                        if (richiestaUopRigaBulk.getRiga().compareTo(riga.intValue()) == 0) {
                                            richiestaUopRigaBulk.addToDettaglioAllegati(allegato);
                                        }
                                    }
                                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                });
    }

    public InputStream getStreamRichiesta(RichiestaUopBulk richiestaUopBulk) throws Exception{
        return getFilesRichiesta(richiestaUopBulk).stream()
                .filter(storageObject -> hasAspect(storageObject, ASPECT_STAMPA_RICHIESTA_ORDINI))
                .findAny().map(
                  storageObject -> getResource(storageObject.getKey())
                ).orElse(null);
    }

}
