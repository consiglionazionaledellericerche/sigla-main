package it.cnr.contab.incarichi00.service;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;

import it.cnr.contab.cmis.bulk.CMISDocument;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

public class ContrattiService extends SiglaCMISService {
	public CMISDocument getCMISProceduraFolder(Incarichi_proceduraBulk incarichi_proceduraBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti:procedura");
		query.append(" where ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_proceduraBulk.getEsercizio());
		query.append(" and ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_proceduraBulk.getPg_procedura());
		ItemIterable<QueryResult> listNodePage =  search(query);
		if (listNodePage.getTotalNumItems() == 0)
			return null;
		if (listNodePage.getTotalNumItems() > 1)
			throw new ApplicationException("Errore di sistema, esistono più procedure di conferimento incarico ("+ incarichi_proceduraBulk.getEsercizio()+
					"/"+incarichi_proceduraBulk.getPg_procedura());
		for (QueryResult queryResult : listNodePage) {
			CMISDocument.construct((Document) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID)));
		}
		return null;
	}

	public CMISDocument getCMISDecisioneAContrattareDocument(Incarichi_procedura_archivioBulk incarichi_procedura_archivioBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti_attachment:decisione_a_contrattare");
		query.append(" where ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getEsercizio());
		query.append(" and ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getPg_procedura());
		ItemIterable<QueryResult> listNodePage =  search(query);
		if (listNodePage.getTotalNumItems() == 0)
			return null;
		if (listNodePage.getTotalNumItems() > 1)
			throw new ApplicationException("Errore di sistema, esistono più file di tipo decisione a contrattare per la procedura di conferimento incarichi "+
						incarichi_procedura_archivioBulk.getEsercizio()+"/"+incarichi_procedura_archivioBulk.getProgressivo_riga());
		for (QueryResult queryResult : listNodePage) {
			CMISDocument.construct((Document) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID)));
		}
		return null;
	}
}
