package it.cnr.contab.pdg00.service;

import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.storage.PdgVariazioneDocument;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.SiglaStorageService;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PdgVariazioniService extends StoreService {

	public PdgVariazioneDocument getPdgVariazioneDocument(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) throws DetailedException{
		return PdgVariazioneDocument.construct((Optional.ofNullable(getStorageObjectByPath(getCMISPath(archiviaStampaPdgVariazioneBulk)))
				.orElseGet(() -> {
					StringBuffer query = new StringBuffer("select * from varpianogest:document");
					query.append(" where ").append(StoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getEsercizio());
					query.append(" and ").append(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg());
					List<StorageObject> storageObjects = super.search(query.toString());
					if (!storageObjects.isEmpty())
						return storageObjects.get(0);
					return null;
				})));
	}

	private String getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk){
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(),
                Optional.ofNullable(archiviaStampaPdgVariazioneBulk.getEsercizio())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
                archiviaStampaPdgVariazioneBulk.getCd_cds()+" - "+archiviaStampaPdgVariazioneBulk.getDs_cds(),
                "CdR "+archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita()+
                        " Variazione "+ Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(),5,'0'),
                "Variazione al PdG n. "
                        + archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
                        + " CdR proponente "
                        + archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf"
		).stream().collect(
				Collectors.joining(SiglaStorageService.SUFFIX)
		);
	}
	
	public List<Integer> findVariazioniSigned(Integer esercizio, String cds, String uo, Long variazionePdg) throws ApplicationException{
		StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
		query.append("var.").append(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value());
		query.append(" from varpianogest:document var");
		query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
		query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
		query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
		query.append(" where var.").append(StoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
		if (cds != null)
			query.append(" and cds.").append(StoragePropertyNames.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds).append("'");
		if (uo != null)
			query.append(" and uo.").append(StoragePropertyNames.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
		if (variazionePdg != null)
			query.append(" and var.").append(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);
        return search(query.toString()).stream()
                .map(storageObject -> storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                .map(BigInteger::intValue)
                .collect(Collectors.toList());
	}	

	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo) throws ApplicationException{
		return findVariazioniPresenti(esercizio, tiSigned, cds, uo, null);
	}
	
	public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, String cds, String uo, Long variazionePdg) throws ApplicationException{
		StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
		query.append("var.").append(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value());
		query.append(" from varpianogest:document var");
		query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
		query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
		if (tiSigned != null &&
				tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED))
			query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
		query.append(" where var.").append(StoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
		if (variazionePdg != null)
			query.append(" and var.").append(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);
		if (cds != null)
			query.append(" and cds.").append(StoragePropertyNames.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds).append("'");
		if (uo != null)
			query.append(" and uo.").append(StoragePropertyNames.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
		List<Integer> variazioniSigned = new ArrayList<Integer>();
        if (tiSigned != null &&
                tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED)){
            variazioniSigned.addAll(findVariazioniSigned(esercizio, cds, uo, variazionePdg));
        }
        return search(query.toString()).stream()
                .map(storageObject -> storageObject.<List<BigInteger>>getPropertyValue(StoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                .map(list -> list.get(0))
				.map(BigInteger::intValue)
                .filter(numeroVariazione -> !variazioniSigned.contains(numeroVariazione))
                .collect(Collectors.toList());
	}	
}
