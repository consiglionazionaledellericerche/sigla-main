package it.cnr.contab.config00.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.contratto.bulk.AllegatoContrattoDocumentBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.OggettoBulk;

public class ContrattoService extends SiglaCMISService {
	private transient static final Log logger = LogFactory.getLog(ContrattoService.class);

	public Folder getFolderContratto(ContrattoBulk contratto){
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:esercizio").append(" = ").append(contratto.getEsercizio());
		query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:stato").append(" = '").append(contratto.getStato()).append("'");
		query.append(" and ").append("aspect.sigla_contratti_aspect_appalti:progressivo").append(" = ").append(contratto.getPg_contratto());
		List<CmisObject> listNodePage = super.searchAndFetchNode(query);
		if (!listNodePage.isEmpty())
			return (Folder) listNodePage.get(0);
		return null;
	}
	
	public List<CmisObject> findContrattiDefinitivi(){
		StringBuffer query = new StringBuffer("select appalti.cmis:objectId from sigla_contratti:appalti as appalti");
		query.append(" join sigla_contratti_aspect:appalti as aspect on appalti.cmis:objectId = aspect.cmis:objectId");
		query.append(" where ").append("aspect.sigla_contratti_aspect_appalti:stato = 'D'");
		return super.searchAndFetchNode(query);
	}
	
	public void findContrattiDefinitiviWithoutFile(){
		List<CmisObject> nodes = findContrattiDefinitivi();
		for (CmisObject cmisObject : nodes) {
			boolean exist = false;
			ItemIterable<CmisObject> childs = getChildren((Folder) cmisObject);
			for (CmisObject child : childs) {
				if (child.getType().getId().equals(AllegatoContrattoDocumentBulk.CONTRATTO))
					exist = true;
			}
			if (!exist)
				logger.error(
						(String)cmisObject.getPropertyValue("strorguo:codice")+" "+
								cmisObject.getPropertyValue("sigla_contratti_aspect_appalti:progressivo"));
		}
	}
	
	
	public ItemIterable<CmisObject> findNodeAllegatiContratto(ContrattoBulk contratto){
		Folder node = getFolderContratto(contratto);
		if (node != null)
			return super.getChildren(node);
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
		ItemIterable<CmisObject> children = findNodeAllegatiContratto(contratto);
		if (children != null){
			for (CmisObject child : children) {
				Document cmisContratto = (Document) child;
				AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(cmisContratto);
				allegato.setContentType(cmisContratto.getContentStreamMimeType());
				allegato.setNome((String) cmisContratto.getPropertyValue("sigla_contratti_attachment:original_name"));
				allegato.setDescrizione(cmisContratto.getProperty(PROPERTY_DESCRIPTION).getValueAsString());
				allegato.setTitolo(cmisContratto.getProperty(PROPERTY_TITLE).getValueAsString());
				allegato.setType(cmisContratto.getType().getId());
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
			AllegatoContrattoDocumentBulk allegato, Document source) {
		copyNode(source, (Folder) getNodeByPath(getCMISPathAlternativo(allegato)));
	}	
	
	public void changeProgressivoNodeRef(Folder oldNode, ContrattoBulk contratto) {
		updateProperties(contratto, oldNode);
		ItemIterable<CmisObject> children = getChildren(oldNode);
		for (CmisObject child : children) {
			Document cmisContratto = (Document) child;
			AllegatoContrattoDocumentBulk allegato = AllegatoContrattoDocumentBulk.construct(cmisContratto);
			allegato.setNome((String) child.getPropertyValue("sigla_contratti_attachment:original_name"));
			allegato.setType(child.getType().getId());
			allegato.setContrattoBulk(contratto);
			updateProperties(allegato, child);
			if (contratto.isDefinitivo() && !allegato.getType().equals(AllegatoContrattoDocumentBulk.GENERICO))
				costruisciAlberaturaAlternativa(allegato, cmisContratto);
		}		
	}	
}
