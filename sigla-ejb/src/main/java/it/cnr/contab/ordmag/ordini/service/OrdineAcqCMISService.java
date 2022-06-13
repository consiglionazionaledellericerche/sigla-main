/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.service;

import it.cnr.contab.firma.bulk.FirmaOTPBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.SignP7M;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceClient;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.spring.storage.MimeTypes;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class OrdineAcqCMISService extends StoreService {

	private transient static final Logger logger = LoggerFactory.getLogger(OrdineAcqCMISService.class);
	public static final String ASPECT_STAMPA_ORDINI = "P:ordini_acq_attachment:stampa";
	public static final String ASPECT_STATO_STAMPA_ORDINI = "P:ordini_acq_attachment:stato_stampa";
	public static final String ASPECT_ORDINI_DETTAGLIO = "P:ordini_acq_attachment:allegati_dettaglio";
	public static final String ASPECT_ALLEGATI_ORDINI = "P:ordini_acq_attachment:allegati";
	public static final String CMIS_ORDINI_ACQ_ANNO = "ordini_acq:anno";
	public static final String CMIS_ORDINI_ACQ_NUMERO = "ordini_acq:numero";
	public static final String CMIS_ORDINI_ACQ_OGGETTO = "ordini_acq:oggetto";
	public static final String CMIS_ORDINI_ACQ_DETTAGLIO_RIGA = "ordini_acq_dettaglio:riga";
	public static final String CMIS_ORDINI_ACQ_UOP = "ordini_acq:cd_unita_operativa";
	public static final String CMIS_ORDINI_ACQ_NUMERATORE = "ordini_acq:cd_numeratore";
	public static final String STATO_STAMPA_ORDINE_VALIDO = "VAL";
	public static final String STATO_STAMPA_ORDINE_ANNULLATO = "ANN";

	@Autowired
	private StorageDriver storageDriver;
	@Autowired
	private ArubaSignServiceClient arubaSignServiceClient;


	public List<StorageObject> getFilesOrdine(OrdineAcqBulk ordine) throws BusinessProcessException{
    	if ( Optional.ofNullable(recuperoFolderOrdineSigla(ordine)).isPresent())
			return getChildren(recuperoFolderOrdineSigla(ordine).getKey());
    	return Collections.EMPTY_LIST;
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
		metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), "F:ordini_acq:main");
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

			String path =Arrays.asList(
					SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
					allegatoParentBulk.getUnitaOperativaOrd().getCdUnitaOrganizzativa(),
					"Ordini",
					allegatoParentBulk.getCdNumeratore(),
					Optional.ofNullable(allegatoParentBulk.getEsercizio())
							.map(esercizio -> String.valueOf(esercizio))
							.orElse("0")
			).stream().collect(
					Collectors.joining(StorageDriver.SUFFIX)
			);
            return createFolderRichiestaIfNotPresent(path, allegatoParentBulk);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
	}

	public StorageObject getStorageObjectStampaOrdine(OrdineAcqBulk ordine)throws Exception{
		return getFilesOrdine(ordine).stream()
				.filter(storageObject -> hasAspect(storageObject, ASPECT_STAMPA_ORDINI))
				.findFirst().map(
						storageObject->storageObject
				).orElse(null);
	}

	public boolean alreadyExsistValidStampaOrdine(OrdineAcqBulk ordine) throws Exception{
		StorageObject s = getStorageObjectStampaOrdine(ordine);
		if (Optional.ofNullable(getStorageObjectStampaOrdine(ordine))
				.flatMap(storageObject -> Optional.ofNullable(storageObject.<String>getPropertyValue("ordine_acq:stato")))
				.filter(stato->stato.equalsIgnoreCase(OrdineAcqBulk.STATO.get(OrdineAcqBulk.STATO_ALLA_FIRMA).toString())
							  ||stato.equalsIgnoreCase(OrdineAcqBulk.STATO.get(OrdineAcqBulk.STATO_DEFINITIVO).toString())).isPresent())
					return true;
		return false;

	}
    public InputStream getStreamOrdine(OrdineAcqBulk ordine) throws Exception{
    	return Optional.ofNullable(getStorageObjectStampaOrdine( ordine)).map(
				storageObject -> getResource(storageObject.getKey())
		).orElse(null);
    }
	public String signOrdine(SignP7M signP7M, String path) throws ApplicationException {
		StorageObject storageObject = storageDriver.getObject(signP7M.getNodeRefSource());
		StorageObject docFirmato =null;
		try {

			byte[] bytesSigned = arubaSignServiceClient.pdfsignatureV2(
					signP7M.getUsername(),
					signP7M.getPassword(),
					signP7M.getOtp(),
					IOUtils.toByteArray(getResource(storageObject)));

			Map<String, Object> metadataProperties = new HashMap<>();
			metadataProperties.put(StoragePropertyNames.NAME.value(), signP7M.getNomeFile());
			docFirmato=updateStream(storageObject.getKey(),new ByteArrayInputStream(bytesSigned),MimeTypes.PDF.mimetype());
			addAspect(storageObject,"P:cnr:signedDocument");

			logger.info("Ordine {} firmato correttamente.", signP7M.getNomeFile());

			return docFirmato.getKey();
		} catch (ArubaSignServiceException _ex) {
			logger.error("ERROR firma fatture attive", _ex);
			throw new ApplicationException(FirmaOTPBulk.errorMessage(_ex.getMessage()));
		} catch (IOException e) {
			throw new ApplicationException( e );
		}
	}
}
