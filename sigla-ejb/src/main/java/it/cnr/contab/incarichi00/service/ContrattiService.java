package it.cnr.contab.incarichi00.service;

import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import it.cnr.si.spring.storage.bulk.StorageDocument;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.storage.StorageContrattiProperty;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.util.List;

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
}