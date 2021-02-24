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

package it.cnr.contab.incarichi00.service;

import it.cnr.contab.util.SIGLAGroups;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import it.cnr.si.spring.storage.bulk.StorageDocument;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContrattiService extends StoreService {
	public StorageDocument getCMISProceduraFolder(Incarichi_proceduraBulk incarichi_proceduraBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti:procedura");
		query.append(" where ").append(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_proceduraBulk.getEsercizio());
		query.append(" and ").append(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_proceduraBulk.getPg_procedura());
		List<StorageObject> storageObjects =  search(query.toString());
		if (storageObjects.size() == 0)
			return null;
		if (storageObjects.size() > 1)
			throw new ApplicationException("Errore di sistema, esistono più procedure di conferimento incarico ("+ incarichi_proceduraBulk.getEsercizio()+
					"/"+incarichi_proceduraBulk.getPg_procedura());
		return StorageDocument.construct(storageObjects.get(0));
	}

	public StorageDocument getCMISDecisioneAContrattareDocument(Incarichi_procedura_archivioBulk incarichi_procedura_archivioBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti_attachment:decisione_a_contrattare");
		query.append(" where ").append(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getEsercizio());
		query.append(" and ").append(StorageContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getPg_procedura());
		List<StorageObject> storageObjects =  search(query.toString());
		if (storageObjects.size() == 0)
			return null;
		if (storageObjects.size() > 1)
			throw new ApplicationException("Errore di sistema, esistono più file di tipo decisione a contrattare per la procedura di conferimento incarichi "+
					incarichi_procedura_archivioBulk.getEsercizio()+"/"+incarichi_procedura_archivioBulk.getProgressivo_riga());
		return StorageDocument.construct(storageObjects.get(0));
	}

	@Override
	public String createFolderIfNotPresent(String path, String name, Map<String, Object> metadataProperties) {
		boolean present = Optional.ofNullable(
				getStorageObjectByPath(
						path.concat(path.equals(StorageDriver.SUFFIX) ? "" : StorageDriver.SUFFIX).concat(name),
						true,
						false
				)
		).isPresent();
		String folderPath = super.createFolderIfNotPresent(path, name, metadataProperties);
		if (!present) {
			StorageObject storageObjectByPath = getStorageObjectByPath(folderPath);
			addConsumer(storageObjectByPath, SIGLAGroups.GROUP_CONTRATTI.name());
			addConsumer(storageObjectByPath, SIGLAGroups.GROUP_INCARICHI.name());
		}
		return folderPath;
	}
}