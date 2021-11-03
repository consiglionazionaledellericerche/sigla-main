/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.config00.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;
import it.cnr.contab.docamm00.storage.StorageDocAmmAspect;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoFlussoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;

public class ContrattoService extends StoreService {
	private transient static final Logger logger = LoggerFactory.getLogger(ContrattoService.class);
	private String folderFlowsName;
	public String getFolderFlowsName() {
		return folderFlowsName;
	}

	public void setFolderFlowsName(String folderFlowsName) {
		this.folderFlowsName = folderFlowsName;
	}
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
		if (contrattoBulk.isFromFlussoAcquisti()){
			return Arrays.asList(
					SpringUtil.getBean(StorePath.class).getPathComunicazioniAl(),
					getFolderFlowsName(),
					"acquisti",
					Optional.ofNullable(contrattoBulk.getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse("").replace(".", ""),
					Optional.ofNullable(contrattoBulk.getEsercizio())
							.map(esercizio -> String.valueOf(esercizio))
							.orElse("0")
			);
		}
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
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public String getCMISPathFolderContratto(ContrattoBulk contrattoBulk) {
		return Stream.concat(getBasePath(contrattoBulk).stream(), Stream.of(contrattoBulk.getCMISFolderName())).collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public List<StorageObject> findNodeAllegatiContratto(ContrattoBulk contratto) throws ApplicationException{
		StorageObject folderContratto = getFolderContratto(contratto);
		if (folderContratto != null)
			return super.getChildren(folderContratto.getKey());
		return null;
	}

	public boolean isDocumentoContrattoPresent(ContrattoBulk contratto) throws ApplicationException{
		if (contratto.isFromFlussoAcquisti()){
			List<AllegatoContrattoFlussoDocumentBulk> result = findAllegatiFlussoContratto(contratto);
			for (AllegatoContrattoFlussoDocumentBulk allegatoContrattoFlussoDocumentBulk : result) {
				if (allegatoContrattoFlussoDocumentBulk.isTipoContratto())
					return true;
			}
			
		} else {
			List<AllegatoContrattoDocumentBulk> result = findAllegatiContratto(contratto);
			for (AllegatoContrattoDocumentBulk allegatoContrattoDocumentBulk : result) {
				if (allegatoContrattoDocumentBulk.getType().equalsIgnoreCase(AllegatoContrattoDocumentBulk.CONTRATTO))
					return true;
			}
		}
		return false;
	}

	public List<AllegatoContrattoFlussoDocumentBulk> findAllegatiFlussoContratto(ContrattoBulk contratto) throws ApplicationException{
		List<AllegatoContrattoFlussoDocumentBulk> result = new ArrayList<AllegatoContrattoFlussoDocumentBulk>();
		List<StorageObject> children = findNodeAllegatiContratto(contratto);
		if (children != null){
			for (StorageObject child : children) {
				if (contratto.isFromFlussoAcquisti()){
					AllegatoContrattoFlussoDocumentBulk allegato = AllegatoContrattoFlussoDocumentBulk.construct(child);
					Optional.ofNullable(child.<List<String>>getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value()))
							.map(strings -> strings.stream())
							.ifPresent(stringStream -> {
								stringStream
								.filter(s -> AllegatoContrattoFlussoDocumentBulk.ti_allegatoFlussoKeys.get(s) != null)
								.findFirst()
								.ifPresent(s -> allegato.setType(s));
							});
					allegato.setContentType(child.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
					allegato.setDescrizione(child.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
					allegato.setTitolo(child.getPropertyValue(StoragePropertyNames.TITLE.value()));
					allegato.setNome(allegato.getTitolo());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
					allegato.setTrasparenza(child.<Boolean>getPropertyValue("sigla_commons_aspect:pubblicazione_trasparenza"));
					allegato.setUrp(child.<Boolean>getPropertyValue("sigla_commons_aspect:pubblicazione_trasparenza"));
					allegato.setLabel(child.<String>getPropertyValue("sigla_contratti_aspect_allegato:label"));
					result.add(allegato);
				}
			}
		}
		return result;
	}

	public List<AllegatoContrattoDocumentBulk> findAllegatiContratto(ContrattoBulk contratto) throws ApplicationException{
		List<AllegatoContrattoDocumentBulk> result = new ArrayList<AllegatoContrattoDocumentBulk>();
		List<StorageObject> children = findNodeAllegatiContratto(contratto);
		if (children != null){
			for (StorageObject child : children) {
				if (!contratto.isFromFlussoAcquisti()){
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
				Collectors.joining(StorageDriver.SUFFIX)
		);
	}

	public String getCMISPath(AllegatoContrattoDocumentBulk allegato) {
		if (allegato.getContrattoBulk().isFromFlussoAcquisti()){
			return Arrays.asList(
					SpringUtil.getBean(StorePath.class).getPathComunicazioniAl(),
					"flows-demo",
					"acquisti",
					Optional.ofNullable(allegato.getContrattoBulk().getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse("").replace(".", ""),
					Optional.ofNullable(allegato.getContrattoBulk().getEsercizio())
							.map(esercizio -> String.valueOf(esercizio))
							.orElse("0"),
					allegato.getContrattoBulk().getCMISFolderName()
			).stream().collect(
					Collectors.joining(StorageDriver.SUFFIX)
			);
		}
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegato.getContrattoBulk().getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse(""),
				"Contratti",
				Optional.ofNullable(allegato.getContrattoBulk().getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				allegato.getContrattoBulk().getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageDriver.SUFFIX)
		);

	}

	public void costruisciAlberaturaAlternativa(AllegatoContrattoDocumentBulk allegato, StorageObject source) throws ApplicationException {
		try {
			copyNode(source, getStorageObjectByPath(getCMISPathAlternativo(allegato), true));
		} catch (StorageException _ex) {
			//logger.error("Errore in costruisciAlberaturaAlternativa per il nodo " + source.getKey(), _ex);
			throw  new ApplicationException(_ex);
		}
	}

	public void changeProgressivoNodeRef(StorageObject oldStorageObject, ContrattoBulk contratto) throws ApplicationException {
		List<StorageObject> children = getChildren(oldStorageObject.getKey());
		for (StorageObject child : children) {
			AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
			allegato.setNome(child.<String>getPropertyValue("sigla_contratti_attachment:original_name"));
			allegato.setType(child.<String>getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()));
			allegato.setTitolo(child.<String>getPropertyValue(StoragePropertyNames.TITLE.value()));
			allegato.setDescrizione(child.<String>getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
			allegato.setContrattoBulk(contratto);
			updateProperties(allegato, child);
			if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
				costruisciAlberaturaAlternativa(allegato, child);
		}
		updateProperties(contratto, oldStorageObject);
	}

}