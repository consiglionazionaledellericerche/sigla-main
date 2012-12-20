package it.cnr.contab.config00.service;

import java.util.ArrayList;
import java.util.List;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.jada.bulk.OggettoBulk;

public class ContrattoService extends CMISService {

	public Node getFolderContratto(ContrattoBulk contratto){
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:esercizio").append(" = ").append(contratto.getEsercizio());
		query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:progressivo").append(" = ").append(contratto.getPg_contratto());
		ListNodePage<Node> listNodePage = super.search(query, Boolean.TRUE);
		if (!listNodePage.isEmpty())
			return listNodePage.get(0);
		return null;
	}
	
	public ListNodePage<Node> findNodeAllegatiContratto(ContrattoBulk contratto){
		Node node = getFolderContratto(contratto);
		if (node != null)
			return super.getChildren(node, null, null);
		return null;
	}
	
	public List<AllegatoContrattoDocumentBulk> findAllegatiContratto(ContrattoBulk contratto){
		List<AllegatoContrattoDocumentBulk> result = new ArrayList<AllegatoContrattoDocumentBulk>();
		ListNodePage<Node> children = findNodeAllegatiContratto(contratto);
		if (children != null){
			for (Node child : children) {
				AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
				allegato.setContentType(child.getContentType());
				allegato.setNome((String) child.getPropertyValue("sigla_contratti_attachment:original_name"));
				allegato.setDescrizione(child.getDescription());
				allegato.setTitolo(child.getTitle());
				allegato.setType(child.getTypeId());
				allegato.setLink((String) child.getPropertyValue("sigla_contratti_aspect_link:url"));
				allegato.setCrudStatus(OggettoBulk.NORMAL);
				result.add(allegato);
			}
		}
		return result;
	}
	
}
