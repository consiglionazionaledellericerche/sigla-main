package it.cnr.contab.ordmag.ordini.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;

import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
//import it.cnr.contab.ordmag.ordini.bulk.AllegatoRichiestaDettaglioBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.DetailedException;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

public class OrdineAcqCMISService extends SiglaCMISService {
	
	public static final String ASPECT_STAMPA_ORDINI = "P:ordini_acq_attachment:stampa";
	public static final String ASPECT_RICHIESTA_ORDINI_DETTAGLIO = "P:ordini_richieste_attachment:allegati_dettaglio";
	public static final String ASPECT_ALLEGATI_ORDINI = "P:ordini_acq_attachment:allegati";
	public static final String CMIS_RICHIESTA_ORDINI_ANNO = "ordini_richieste:anno";
	public static final String CMIS_RICHIESTA_ORDINI_NUMERO = "ordini_richieste:numero";
	public static final String CMIS_RICHIESTA_ORDINI_OGGETTO = "ordini_richieste:oggetto";
	public static final String CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA = "ordini_richieste_dettaglio:riga";
	public static final String CMIS_RICHIESTA_ORDINI_UOP = "ordini_richieste:cd_unita_operativa";
	public static final String CMIS_RICHIESTA_ORDINI_NUMERATORE = "ordini_richieste:cd_numeratore";

	
	
	public ItemIterable<CmisObject> getFilesOrdine(OrdineAcqBulk ordine) throws ComponentException{
			Folder folderRichiesta;
			try {
				folderRichiesta = recuperoFolderOrdineSigla(ordine);
			} catch (DetailedException e) {
				throw new ComponentException(e);
			}
			
			if (folderRichiesta != null){
		        ItemIterable<CmisObject> children = folderRichiesta.getChildren();
		        return children;
			}
		return null;
	}

	private ItemIterable<QueryResult> getDocuments(String folder, String tipoAllegato)
			throws ApplicationException {
		StringBuffer query = new StringBuffer("select doc.cmis:objectId from cmis:document doc ");
		query.append(" where IN_FOLDER(doc, '").append(folder).append("')");
		ItemIterable<QueryResult> results = search(query);
		for (QueryResult nodeFile : results) {
			String file = nodeFile.getPropertyValueById(PropertyIds.OBJECT_ID);
		}
		return results;
	}

	public Folder recuperoFolderOrdineSigla(OrdineAcqBulk ordine)throws DetailedException{
		StringBuffer query = new StringBuffer("select ric.cmis:objectId from ordini_richieste:main ric ");
		query.append(" where ric.ordini_richieste:anno = ").append(ordine.getEsercizio());
		query.append(" and ric.ordini_richieste:numero = ").append(ordine.getNumero());
		query.append(" and ric.ordini_richieste:cd_numeratore = '").append(ordine.getCdNumeratore()).append("'");
		query.append(" and ric.ordini_richieste:cd_unita_operativa = '").append(ordine.getCdUnitaOperativa()).append("'");
		ItemIterable<QueryResult> resultsFolder = search(query);
		if (resultsFolder.getTotalNumItems() == 0)
			return null;
		else if (resultsFolder.getTotalNumItems() > 1){
			throw new ApplicationException("Errore di sistema, esistono sul documentale piu' Richieste.  Anno:"+ ordine.getEsercizio()+ " UOP:" +ordine.getCdUnitaOperativa() +" Numeratore:"+ordine.getCdNumeratore()+
					" numero:"+ordine.getNumero());
		} else {
			for (QueryResult queryResult : resultsFolder) {
				return (Folder) getNodeByNodeRef((String) queryResult.getPropertyValueById(PropertyIds.OBJECT_ID));
			}
		}
		return null;
	}
	
	public CMISPath createFolderRichiestaIfNotPresent(CMISPath cmisPath, OrdineAcqBulk ordine) throws ApplicationException{
		Map<String, Object> metadataProperties = new HashMap<String, Object>();
		String name = ordine.constructCMISNomeFile();
		String folderName = name;
		folderName = sanitizeFolderName(folderName);
		metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, "F:ordini_richieste:main");
		metadataProperties.put(OrdineAcqCMISService.PROPERTY_DESCRIPTION, folderName);
		metadataProperties.put(PropertyIds.NAME, folderName);
		metadataProperties.put(OrdineAcqCMISService.PROPERTY_TITLE, folderName);
		metadataProperties.put(OrdineAcqCMISService.CMIS_RICHIESTA_ORDINI_NUMERATORE, ordine.getCdNumeratore());
		metadataProperties.put(OrdineAcqCMISService.CMIS_RICHIESTA_ORDINI_ANNO, ordine.getEsercizio());
		metadataProperties.put(OrdineAcqCMISService.CMIS_RICHIESTA_ORDINI_NUMERO, ordine.getNumero());
		metadataProperties.put(OrdineAcqCMISService.CMIS_RICHIESTA_ORDINI_UOP, ordine.getCdUnitaOperativa());

		metadataProperties.put("sigla_commons_aspect:utente_applicativo", ordine.getUtuv());
		List<String> aspectsToAdd = new ArrayList<String>();
		aspectsToAdd.add("P:cm:titled");
		aspectsToAdd.add("P:sigla_commons_aspect:utente_applicativo_sigla");
		cmisPath = createFolderIfNotPresent(cmisPath, metadataProperties, aspectsToAdd, folderName);
		return cmisPath;
	}
//	public CMISPath createFolderDettaglioIfNotPresent(CMISPath cmisPath, OrdineAcqRigaBulk dettaglio) throws ApplicationException{
//		Map<String, Object> metadataProperties = new HashMap<String, Object>();
//		String name = dettaglio.constructCMISNomeFile();
//		String folderName = name;
//		folderName = sanitizeFolderName(folderName);
//		metadataProperties.put(PropertyIds.OBJECT_TYPE_ID, "F:ordini_richieste_dettaglio:main");
//		metadataProperties.put(OrdineAcqCMISService.PROPERTY_DESCRIPTION, folderName);
//		metadataProperties.put(PropertyIds.NAME, folderName);
//		metadataProperties.put(OrdineAcqCMISService.PROPERTY_TITLE, folderName);
//		metadataProperties.put(OrdineAcqCMISService.CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA, dettaglio.getRiga());
//
//		List<String> aspectsToAdd = new ArrayList<String>();
//		aspectsToAdd.add("P:cm:titled");
//		cmisPath = createFolderIfNotPresent(cmisPath, metadataProperties, aspectsToAdd, folderName);
//		return cmisPath;
//	}
//	
//	public void recuperoAllegatiDettaglioRichiesta(OrdineAcqBulk allegatoParentBulk, CmisObject cmisObject)
//			throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
//		if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)) {
//			Property<?> type = cmisObject.getProperty("cmis:objectTypeId");
//			String prop = type.getValueAsString();
//			if (prop.equals("F:ordini_richieste_dettaglio:main")){
//				Property<?> typeRiga = cmisObject.getProperty(CMIS_RICHIESTA_ORDINI_DETTAGLIO_RIGA);
//				String rigaString = typeRiga.getValueAsString();
//				Folder folderDettaglio = (Folder) cmisObject;
//		        ItemIterable<CmisObject> children = folderDettaglio.getChildren();
//				if (children != null){
//					for (CmisObject doc : children) {
//						if (doc.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
//							Document document = (Document) doc;
//							if (document != null){
//								AllegatoRichiestaDettaglioBulk allegato = (AllegatoRichiestaDettaglioBulk) Introspector.newInstance(AllegatoRichiestaDettaglioBulk.class, document);
//								allegato.setContentType(document.getContentStreamMimeType());
//								allegato.setNome(document.getName());
//								allegato.setDescrizione((String)document.getPropertyValue(SiglaCMISService.PROPERTY_DESCRIPTION));
//								allegato.setTitolo((String)document.getPropertyValue(SiglaCMISService.PROPERTY_TITLE));
//								allegato.setCrudStatus(OggettoBulk.NORMAL);
//
//								for ( java.util.Iterator i = allegatoParentBulk.getRigheRichiestaColl().iterator(); i.hasNext(); )
//								{
//									OrdineAcqRigaBulk riga = (OrdineAcqRigaBulk) i.next();
//									if (riga.getRiga().compareTo(new Integer(rigaString)) == 0){
//										riga.addToDettaglioAllegati(allegato);
//									}
//								}
//							}
//						}
//					}
//				}
//			} 
//		}
//	}
	public CMISPath getCMISPath(OrdineAcqBulk allegatoParentBulk, boolean create) throws BusinessProcessException{
		try {
			CMISPath cmisPath = null;
			cmisPath = SpringUtil.getBean("cmisPathOrdini",CMISPath.class);

			if (create) {
				cmisPath = createFolderIfNotPresent(cmisPath, allegatoParentBulk.getCdUnitaOperativa(), allegatoParentBulk.getCdUnitaOperativa(), allegatoParentBulk.getCdUnitaOperativa());
				cmisPath = createFolderIfNotPresent(cmisPath, allegatoParentBulk.getCdNumeratore(), allegatoParentBulk.getCdNumeratore(), allegatoParentBulk.getCdNumeratore());
				cmisPath = createFolderIfNotPresent(cmisPath, "Anno "+allegatoParentBulk.getEsercizio().toString(), "Anno "+allegatoParentBulk.getEsercizio().toString(), "Anno "+allegatoParentBulk.getEsercizio().toString());
				cmisPath = createFolderRichiestaIfNotPresent(cmisPath, allegatoParentBulk);
			} else {
				try {
					getNodeByPath(cmisPath);
				} catch (CmisObjectNotFoundException _ex) {
					return null;
				}
			}			
			return cmisPath;
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
	}

	public InputStream getStreamOrdine(OrdineAcqBulk ordine) throws Exception{
		ItemIterable<CmisObject> files = getFilesOrdine(ordine);
		if (files != null){
			for (CmisObject cmisObject : files) {
				if (hasAspect(cmisObject, ASPECT_STAMPA_ORDINI)){
					return ((Document)cmisObject).getContentStream().getStream();
				}
			}
		}
		return null;
	}
}
