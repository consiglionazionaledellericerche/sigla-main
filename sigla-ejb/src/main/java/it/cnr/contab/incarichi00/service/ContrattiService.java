package it.cnr.contab.incarichi00.service;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.bulk.CMISDocument;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.cmis.CMISContrattiProperty;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

public class ContrattiService extends CMISService {
	public CMISDocument getCMISProceduraFolder(Incarichi_proceduraBulk incarichi_proceduraBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti:procedura");
		query.append(" where ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_proceduraBulk.getEsercizio());
		query.append(" and ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_proceduraBulk.getPg_procedura());
		ListNodePage<Node> listNodePage =  search(query);
		if (listNodePage.isEmpty())
			return null;
		if (listNodePage.getNumItems() > 1)
			throw new ApplicationException("Errore di sistema, esistono più procedure di conferimento incarico ("+ incarichi_proceduraBulk.getEsercizio()+
					"/"+incarichi_proceduraBulk.getPg_procedura());
		return CMISDocument.construct(listNodePage.get(0));
	}

	public CMISDocument getCMISDecisioneAContrattareDocument(Incarichi_procedura_archivioBulk incarichi_procedura_archivioBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from sigla_contratti_attachment:decisione_a_contrattare");
		query.append(" where ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_ESERCIZIO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getEsercizio());
		query.append(" and ").append(CMISContrattiProperty.SIGLA_CONTRATTI_PROCEDURA_PROGRESSIVO.value()).append(" = ").append(incarichi_procedura_archivioBulk.getPg_procedura());
		ListNodePage<Node> listNodePage =  search(query);
		if (listNodePage.isEmpty())
			return null;
		if (listNodePage.getNumItems() > 1)
			throw new ApplicationException("Errore di sistema, esistono più file di tipo decisione a contrattare per la procedura di conferimento incarichi "+
						incarichi_procedura_archivioBulk.getEsercizio()+"/"+incarichi_procedura_archivioBulk.getProgressivo_riga());
		return CMISDocument.construct(listNodePage.get(0));
	}
}
