package it.cnr.contab.pdg00.service;

import it.cnr.cmisdl.model.Node;
import it.cnr.cmisdl.model.paging.ListNodePage;
import it.cnr.contab.cmis.CMISProperty;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.cmis.PdgVariazioneDocument;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;

import com.google.gson.Gson;

public class PdgVariazioniService extends CMISService {
	private HttpClient httpClient;
	private Gson gson;

	public PdgVariazioneDocument getPdgVariazioneDocument(Pdg_variazioneBulk pdg_variazioneBulk) throws DetailedException{
		StringBuffer query = new StringBuffer("select * from varpianogest:document");
		query.append(" where ").append(CMISProperty.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(pdg_variazioneBulk.getEsercizio());
		query.append(" and ").append(CMISProperty.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(pdg_variazioneBulk.getPg_variazione_pdg());
		ListNodePage<Node> listNodePage =  search(query);
		if (listNodePage.isEmpty())
			return null;
		if (listNodePage.getNumItems() > 1)
			throw new ApplicationException("Errore di sistema, esistone piu' variazioni per l'anno "+ pdg_variazioneBulk.getEsercizio()+
					" e numero "+pdg_variazioneBulk.getPg_variazione_pdg());
		return PdgVariazioneDocument.construct(listNodePage.get(0));
	}

	public List<Integer> findVariazioniSigned(Integer esercizio, String cds, String uo, Long variazionePdg){
		List<Integer> result = new ArrayList<Integer>();
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
		ListNodePage<Node> listNodePage =  search(query, Boolean.FALSE);
		for (Node node : listNodePage) {
			result.add(PdgVariazioneDocument.construct(node).getNumeroVariazione());
		}
		return result;
		
	}	

	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo){
		return findVariazioniPresenti(esercizio, tiSigned, cds, uo, null);
	}
	
	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo, Long variazionePdg){
		List<Integer> result = new ArrayList<Integer>();
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
		ListNodePage<Node> listNodePage =  search(query, Boolean.FALSE);
		for (Node node : listNodePage) {
			result.add(PdgVariazioneDocument.construct(node).getNumeroVariazione());
		}
		if (tiSigned != null &&
				tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED)){
			result.removeAll(findVariazioniSigned(esercizio, cds, uo, variazionePdg));
		}
		return result;
	}
	
	public Credentials getCredentials(){
		return systemCredentials;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public Gson getGson() {
		return gson;
	}

	public void setGson(Gson gson) {
		this.gson = gson;
	}
}
