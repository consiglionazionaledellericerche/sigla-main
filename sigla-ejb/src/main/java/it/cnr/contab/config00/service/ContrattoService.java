package it.cnr.contab.config00.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;

public class ContrattoService extends StoreService {
	private transient static final Logger logger = LoggerFactory.getLogger(ContrattoService.class);

	public StorageObject getFolderContratto(ContrattoBulk contratto) throws ApplicationException{
		return Optional.ofNullable(getStorageObjectByPath(getCMISPathFolderContratto(contratto)))
				.orElseGet(() -> {
					StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
					query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
					query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:esercizio").append(" = ").append(contratto.getEsercizio());
					query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:stato").append(" = '").append(contratto.getStato()).append("'");
					query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:progressivo").append(" = ").append(contratto.getPg_contratto());
					List<StorageObject> storageObjects = super.search(query.toString());
					if (!storageObjects.isEmpty())
						return storageObjects.get(0);
					return null;
				});
	}

	private List<String> getBasePath(ContrattoBulk contrattoBulk) {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(contrattoBulk.getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse(""),
				"Contratti",
				Optional.ofNullable(contrattoBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0")
		);
	}

	public String getCMISPath(ContrattoBulk contrattoBulk) {
		return getBasePath(contrattoBulk).stream().collect(
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	public String getCMISPathFolderContratto(ContrattoBulk contrattoBulk) {
		return Stream.concat(getBasePath(contrattoBulk).stream(), Stream.of(contrattoBulk.getCMISFolderName())).collect(
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	public List<StorageObject> findContrattiDefinitivi() {
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:stato = 'D'");
		return super.search(query.toString());
	}

	public void findContrattiDefinitiviWithoutFile() {
		List<StorageObject> nodes = findContrattiDefinitivi();
		for (StorageObject storageObject : nodes) {
			boolean exist = false;
			List<StorageObject> childs = getChildren(storageObject.getKey());
			for (StorageObject child : childs) {
				if (child.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(AllegatoContrattoDocumentBulk.CONTRATTO))
					exist = true;
			}
			if (!exist)
				logger.error(
						(String)storageObject.getPropertyValue("strorguo:codice")+" "+
								storageObject.getPropertyValue("sigla_contratti_aspect_appalti:progressivo"));
		}
	}


	public List<StorageObject> findNodeAllegatiContratto(ContrattoBulk contratto) throws ApplicationException{
		StorageObject folderContratto = getFolderContratto(contratto);
		if (folderContratto != null)
			return super.getChildren(folderContratto.getKey());
		return null;
	}

	public boolean isDocumentoContrattoPresent(ContrattoBulk contratto) throws ApplicationException{
		List<AllegatoContrattoDocumentBulk> result = findAllegatiContratto(contratto);
		for (AllegatoContrattoDocumentBulk allegatoContrattoDocumentBulk : result) {
			if (allegatoContrattoDocumentBulk.getType().equalsIgnoreCase(AllegatoContrattoDocumentBulk.CONTRATTO))
				return true;
		}
		return false;
	}

	public List<AllegatoContrattoDocumentBulk> findAllegatiContratto(ContrattoBulk contratto) throws ApplicationException{
		List<AllegatoContrattoDocumentBulk> result = new ArrayList<AllegatoContrattoDocumentBulk>();
		List<StorageObject> children = findNodeAllegatiContratto(contratto);
		if (children != null){
			for (StorageObject child : children) {
				AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
				allegato.setContentType(child.<String>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
				allegato.setNome(child.<String>getPropertyValue("sigla_contratti_attachment:original_name"));
				allegato.setDescrizione(child.<String>getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
				allegato.setTitolo(child.<String>getPropertyValue(StoragePropertyNames.TITLE.value()));
				allegato.setType(child.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
				allegato.setLink(child.<String>getPropertyValue("sigla_contratti_aspect_link:url"));
				allegato.setCrudStatus(OggettoBulk.NORMAL);
				result.add(allegato);
			}
		}
		return result;
	}

	public String getCMISPathAlternativo(AllegatoContrattoDocumentBulk allegato) {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegato.getContrattoBulk().getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse(""),
				"Contratti",
				(String)allegato.getContrattoBulk().getTi_natura_contabileKeys().get(allegato.getContrattoBulk().getNatura_contabile()),
				(String)allegato.getTi_allegatoKeys().get(allegato.getType())
		).stream().collect(
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	public String getCMISPath(AllegatoContrattoDocumentBulk allegato) {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegato.getContrattoBulk().getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse(""),
				"Contratti",
				Optional.ofNullable(allegato.getContrattoBulk().getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				allegato.getContrattoBulk().getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageService.SUFFIX)
		);

	}

	public void costruisciAlberaturaAlternativa(AllegatoContrattoDocumentBulk allegato, StorageObject source) throws ApplicationException {
		try {
			copyNode(source, getStorageObjectByPath(getCMISPathAlternativo(allegato), true));
		} catch (StorageException _ex) {
			logger.error("Errore in costruisciAlberaturaAlternativa per il nodo " + source.getKey(), _ex);
		}
	}

	public void changeProgressivoNodeRef(StorageObject oldStorageObject, ContrattoBulk contratto) throws ApplicationException {
		List<StorageObject> children = getChildren(oldStorageObject.getKey());
		for (StorageObject child : children) {
			AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
			allegato.setNome(child.<String>getPropertyValue("sigla_contratti_attachment:original_name"));
			allegato.setType(child.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
			allegato.setContrattoBulk(contratto);
			updateProperties(allegato, child);
			if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
				costruisciAlberaturaAlternativa(allegato, child);
		}
		updateProperties(contratto, oldStorageObject);
	}
}