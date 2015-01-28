package it.cnr.contab.pdg00.service;

import it.cnr.contab.cmis.CMISProperty;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.cmis.PdgVariazioneDocument;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.util.OperationContextUtils;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

public class PdgVariazioniService extends SiglaCMISService {

	public PdgVariazioneDocument getPdgVariazioneDocument(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) throws DetailedException{
		try {
			return PdgVariazioneDocument.construct((Document) getNodeByPath(getCMISPath(archiviaStampaPdgVariazioneBulk)));
		} catch(CmisObjectNotFoundException _ex){			
			StringBuffer query = new StringBuffer("select * from varpianogest:document");
			query.append(" where ").append(CMISProperty.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getEsercizio());
			query.append(" and ").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg());
			ItemIterable<QueryResult> listNodePage = search(query);
			if (listNodePage.getTotalNumItems() == 0)
				return null;
			if (listNodePage.getTotalNumItems() > 1)
				throw new ApplicationException("Errore di sistema, esistone piu' variazioni per l'anno "+ archiviaStampaPdgVariazioneBulk.getEsercizio()+
						" e numero "+archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg());
			for (QueryResult queryResult : listNodePage) {
				return PdgVariazioneDocument.construct((Document) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID)));
			}		
			return null;
		}
	}

	private CMISPath getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk){
		CMISPath cmisPath = SpringUtil.getBean("cmisPathVariazioniAlPianoDiGestione",CMISPath.class);		
		cmisPath = cmisPath.appendToPath(archiviaStampaPdgVariazioneBulk.getEsercizio().toString()); 
		cmisPath = cmisPath.appendToPath(archiviaStampaPdgVariazioneBulk.getCd_cds()+" - "+archiviaStampaPdgVariazioneBulk.getDs_cds());
		cmisPath = cmisPath.appendToPath(
				"CdR "+archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita()+
				" Variazione "+ Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(),5,'0'));
		cmisPath = cmisPath.appendToPath("Variazione al PdG n. "
					+ archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
					+ " CdR proponente "
					+ archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf");		
		return cmisPath;
	}
	
	public List<Integer> findVariazioniSigned(Integer esercizio, String cds, String uo, Long variazionePdg){
		StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
		query.append("var.").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value());
		query.append(" from varpianogest:document var");
		query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
		query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
		query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
		query.append(" where var.").append(CMISProperty.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
		if (cds != null)
			query.append(" and cds.").append(CMISProperty.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds).append("'");
		if (uo != null)
			query.append(" and uo.").append(CMISProperty.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
		if (variazionePdg != null)
			query.append(" and var.").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);		
		OperationContext operationContext = OperationContextUtils.createMinimumOperationContext();
		operationContext.setMaxItemsPerPage(Integer.MAX_VALUE);
		ItemIterable<QueryResult> listNodePage =  search(query, operationContext);
		List<Integer> result = new ArrayList<Integer>(Long.valueOf(listNodePage.getTotalNumItems()).intValue());
		for (QueryResult node : listNodePage) {
			result.add(((BigInteger) node.getPropertyValueById(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue());
		}
		return result;
		
	}	

	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo){
		return findVariazioniPresenti(esercizio, tiSigned, cds, uo, null);
	}
	
	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo, Long variazionePdg){
		StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
		query.append("var.").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value());
		query.append(" from varpianogest:document var");
		query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
		query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
		if (tiSigned != null &&
				tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED))
			query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
		query.append(" where var.").append(CMISProperty.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
		if (variazionePdg != null)
			query.append(" and var.").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);
		if (cds != null)
			query.append(" and cds.").append(CMISProperty.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds).append("'");
		if (uo != null)
			query.append(" and uo.").append(CMISProperty.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
		OperationContext operationContext = OperationContextUtils.createMinimumOperationContext();
		operationContext.setMaxItemsPerPage(Integer.MAX_VALUE);
		
		ItemIterable<QueryResult> listNodePage =  search(query, operationContext);
		List<Integer> result = new ArrayList<Integer>(Long.valueOf(listNodePage.getTotalNumItems()).intValue());
		for (QueryResult node : listNodePage) {
			result.add(((BigInteger) node.getPropertyValueById(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value())).intValue());
		}
		if (tiSigned != null &&
				tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED)){
			result.removeAll(findVariazioniSigned(esercizio, cds, uo, variazionePdg));
		}
		return result;
	}	
}
