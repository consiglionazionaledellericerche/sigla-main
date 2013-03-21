package it.cnr.contab.config00.service;

import java.util.ArrayList;
import java.util.List;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;

public class ContrattoService extends CMISService {

	public Node getFolderContratto(ContrattoBulk contratto){
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:esercizio").append(" = ").append(contratto.getEsercizio());
		query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:stato").append(" = '").append(contratto.getStato()).append("'");
		query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:progressivo").append(" = ").append(contratto.getPg_contratto());
		ListNodePage<Node> listNodePage = super.search(query, Boolean.TRUE);
		if (!listNodePage.isEmpty())
			return listNodePage.get(0);
		return null;
	}
	
	public ListNodePage<Node> findContrattiDefinitivi(){
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:stato = 'D'");
		return super.search(query, Boolean.TRUE);
	}
	
	public void findContrattiDefinitiviWithoutFile(){
		ListNodePage<Node> nodes = findContrattiDefinitivi();
		for (Node node : nodes) {
			boolean exist = false;
			ListNodePage<Node> childs = getChildren(node, null, null);
			for (Node child : childs) {
				if (child.getTypeId().equals(AllegatoContrattoDocumentBulk.CONTRATTO))
					exist = true;
			}
			if (!exist)
				System.out.println(
						(String)node.getPropertyValue("strorguo:codice")+" "+
						node.getPropertyValue("sigla_contratti_aspect_appalti:progressivo"));
		}
	}
	
	
	public ListNodePage<Node> findNodeAllegatiContratto(ContrattoBulk contratto){
		Node node = getFolderContratto(contratto);
		if (node != null)
			return super.getChildren(node, null, null);
		return null;
	}
	
	public boolean isDocumentoContrattoPresent(ContrattoBulk contratto){
		List<AllegatoContrattoDocumentBulk> result = findAllegatiContratto(contratto);
		for (AllegatoContrattoDocumentBulk allegatoContrattoDocumentBulk : result) {
			if (allegatoContrattoDocumentBulk.getType().equalsIgnoreCase(AllegatoContrattoDocumentBulk.CONTRATTO))
				return true;
		}
		return false;
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
	
	public CMISPath getCMISPathAlternativo(AllegatoContrattoDocumentBulk allegato){
		CMISPath cmisPath = SpringUtil.getBean("cmisPathContratti",CMISPath.class);
		cmisPath = createFolderIfNotPresent(cmisPath, allegato.getContrattoBulk().getUnita_organizzativa().getCd_unita_organizzativa(), 
				allegato.getContrattoBulk().getUnita_organizzativa().getDs_unita_organizzativa(), 
				allegato.getContrattoBulk().getUnita_organizzativa().getDs_unita_organizzativa());
		cmisPath = createFolderIfNotPresent(cmisPath,"Contratti","Contratti","Contratti");
		cmisPath = createFolderIfNotPresent(cmisPath, 
					(String)allegato.getContrattoBulk().getTi_natura_contabileKeys().get(allegato.getContrattoBulk().getNatura_contabile()), 
				null, 
				null);
		cmisPath = createFolderIfNotPresent(cmisPath, 
				(String)allegato.getTi_allegatoKeys().get(allegato.getType()), 
			null, 
			null);
		return cmisPath;
	}
	
	public CMISPath getCMISPath(AllegatoContrattoDocumentBulk allegato){
		CMISPath cmisPath = SpringUtil.getBean("cmisPathContratti",CMISPath.class);
		cmisPath = createFolderIfNotPresent(cmisPath, allegato.getContrattoBulk().getUnita_organizzativa().getCd_unita_organizzativa(), 
				allegato.getContrattoBulk().getUnita_organizzativa().getDs_unita_organizzativa(), 
				allegato.getContrattoBulk().getUnita_organizzativa().getDs_unita_organizzativa());
		cmisPath = createFolderIfNotPresent(cmisPath,"Contratti","Contratti","Contratti");
		cmisPath = createFolderIfNotPresent(cmisPath, allegato.getContrattoBulk().getEsercizio().toString(), 
				"Esercizio :"+allegato.getContrattoBulk().getEsercizio().toString(), 
				"Esercizio :"+allegato.getContrattoBulk().getEsercizio().toString());		
		cmisPath = createFolderIfNotPresent(cmisPath, allegato.getContrattoBulk().getCMISFolderName(), 
				null, 
				null, allegato.getContrattoBulk());
		return cmisPath;
	}	
	
	public void costruisciAlberaturaAlternativa(
			AllegatoContrattoDocumentBulk allegato, Node node) {
		copyNode(node, getNodeByPath(getCMISPathAlternativo(allegato)));
	}	
	
	public void changeProgressivoNodeRef(Node oldNode, ContrattoBulk contratto) {
		updateProperties(contratto, oldNode);
		ListNodePage<Node> children = getChildren(oldNode, null, null);
		for (Node child : children) {
			AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(child);
			allegato.setNome((String) child.getPropertyValue("sigla_contratti_attachment:original_name"));
			allegato.setType(child.getTypeId());
			allegato.setContrattoBulk(contratto);
			updateProperties(allegato, child);
			if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
				costruisciAlberaturaAlternativa(allegato, child);
		}		
	}	
}
