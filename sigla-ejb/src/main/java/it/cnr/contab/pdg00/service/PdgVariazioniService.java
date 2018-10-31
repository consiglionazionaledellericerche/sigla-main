package it.cnr.contab.pdg00.service;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.storage.PdgVariazioneDocument;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PdgVariazioniService extends DocumentiContabiliService {

    public PdgVariazioneDocument getPdgVariazioneDocument(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) throws DetailedException {
        return PdgVariazioneDocument.construct((Optional.ofNullable(getStorageObjectByPath(getCMISPath(archiviaStampaPdgVariazioneBulk)))
                .orElseGet(() -> {
                    StringBuffer query = new StringBuffer("select * from varpianogest:document");
                    query.append(" where ").append(SIGLAStoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getEsercizio());
                    query.append(" and ").append(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg());
                    List<StorageObject> storageObjects = super.search(query.toString());
                    if (!storageObjects.isEmpty())
                        return getStorageObjectBykey(storageObjects.get(0).getKey());
                    return null;
                })));
    }

    private String getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(),
                Optional.ofNullable(archiviaStampaPdgVariazioneBulk.getEsercizio())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
                sanitizeFolderName(archiviaStampaPdgVariazioneBulk.getCd_cds()),
                "CdR " + archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() +
                        " Variazione " + Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(), 5, '0'),
                "Variazione al PdG n. "
                        + archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
                        + " CdR proponente "
                        + archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf"
        ).stream().collect(
                Collectors.joining(StorageService.SUFFIX)
        );
    }

    public List<Integer> findVariazioniSigned(Integer esercizio, Unita_organizzativaBulk cds, String uo, Long variazionePdg) throws ApplicationException {
        String basePath = Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(), String.valueOf(esercizio)
        ).stream().collect(
                Collectors.joining(StorageService.SUFFIX)
        );
        if (Optional.ofNullable(cds).isPresent()) {
            basePath = basePath.concat(StorageService.SUFFIX)
                    .concat(cds.getCd_unita_organizzativa())
                    .concat(" - ")
                    .concat(cds.getDs_unita_organizzativa());
        }
        final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(getStorageObjectByPath(basePath));
        if (storageObjectByPath.isPresent() && Optional.ofNullable(cds).isPresent()) {
            return getChildren(storageObjectByPath.get().getKey(), -1).stream()
                    .filter(storageObject -> storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(SIGLAStoragePropertyNames.VARPIANOGEST_DOCUMENT.value()))
                    .filter(storageObject -> hasAspect(storageObject, SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value()))
                    .filter(storageObject -> {
                        if (Optional.ofNullable(uo).isPresent()) {
                            return storageObject.getPropertyValue(SIGLAStoragePropertyNames.STRORGUO_CODICE.value()).equals(uo);
                        } else {
                            return true;
                        }
                    })
                    .filter(storageObject -> {
                        if (Optional.ofNullable(variazionePdg).isPresent()) {
                            return storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).equals(variazionePdg);
                        } else {
                            return true;
                        }
                    })
                    .map(storageObject -> storageObject.<BigInteger>getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                    .map(BigInteger::intValue)
                    .collect(Collectors.toList());
        } else {
            StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
            query.append("var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value());
            query.append(" from varpianogest:document var");
            query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
            query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
            query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
            query.append(" where var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
            if (cds != null)
                query.append(" and cds.").append(SIGLAStoragePropertyNames.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds.getCd_unita_organizzativa()).append("'");
            if (uo != null)
                query.append(" and uo.").append(SIGLAStoragePropertyNames.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
            if (variazionePdg != null)
                query.append(" and var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);

            final StorageObject storageObjectByPath1 = getStorageObjectByPath(SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione());
            if (Optional.ofNullable(storageObjectByPath1).isPresent()) {
                query.append(" and IN_TREE(var, '").append(storageObjectByPath1.getKey()).append("')");
            }
            return search(query.toString()).stream()
                    .map(storageObject -> storageObject.<List<BigInteger>>getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                    .map(list -> list.get(0))
                    .map(BigInteger::intValue)
                    .collect(Collectors.toList());
        }

    }

    public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, Unita_organizzativaBulk cds, String uo) throws ApplicationException {
        return findVariazioniPresenti(esercizio, tiSigned, cds, uo, null);
    }

    public List<Integer> findVariazioniPresenti(Integer esercizio, String tiSigned, Unita_organizzativaBulk cds, String uo, Long variazionePdg) throws ApplicationException {
        String basePath = Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(), String.valueOf(esercizio)
        ).stream().collect(
                Collectors.joining(StorageService.SUFFIX)
        );
        if (Optional.ofNullable(cds).isPresent()) {
            basePath = basePath.concat(StorageService.SUFFIX)
                    .concat(cds.getCd_unita_organizzativa());
        }
        final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(getStorageObjectByPath(basePath));
        if (storageObjectByPath.isPresent() && Optional.ofNullable(cds).isPresent()) {
            return getChildren(storageObjectByPath.get().getKey(), -1).stream()
                    .filter(storageObject -> storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals(SIGLAStoragePropertyNames.VARPIANOGEST_DOCUMENT.value()))
                    .filter(storageObject -> {
                        if (Optional.ofNullable(variazionePdg).isPresent()) {
                            return storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).equals(BigInteger.valueOf(variazionePdg));
                        } else {
                            return true;
                        }
                    })
                    .filter(storageObject -> {
                        if (Optional.ofNullable(tiSigned)
                                .filter(s -> s.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED)).isPresent())
                            return hasAspect(storageObject, SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
                        else if (Optional.ofNullable(tiSigned)
                                .filter(s -> s.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED)).isPresent())
                            return !hasAspect(storageObject, SIGLAStoragePropertyNames.CNR_SIGNEDDOCUMENT.value());
                        else
                            return true;
                    })
                    .filter(storageObject -> {
                        if (Optional.ofNullable(uo).isPresent()) {
                            return storageObject.getPropertyValue(SIGLAStoragePropertyNames.STRORGUO_CODICE.value()).equals(uo);
                        } else {
                            return true;
                        }
                    })
                    .map(storageObject -> storageObject.<BigInteger>getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                    .map(BigInteger::intValue)
                    .collect(Collectors.toList());
        } else {
            StringBuffer query = new StringBuffer("select var.cmis:objectId, ");
            query.append("var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value());
            query.append(" from varpianogest:document var");
            query.append(" join strorg:cds as cds on var.cmis:objectId = cds.cmis:objectId");
            query.append(" join strorg:uo as uo on var.cmis:objectId = uo.cmis:objectId");
            if (tiSigned != null &&
                    tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_SIGNED))
                query.append(" join cnr:signedDocument as sig on var.cmis:objectId = sig.cmis:objectId");
            query.append(" where var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_ESERCIZIO.value()).append(" = ").append(esercizio);
            if (variazionePdg != null)
                query.append(" and var.").append(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()).append(" = ").append(variazionePdg);
            if (cds != null)
                query.append(" and cds.").append(SIGLAStoragePropertyNames.STRORGCDS_CODICE.value()).append(" = ").append("'").append(cds.getCd_unita_organizzativa()).append("'");
            if (uo != null)
                query.append(" and uo.").append(SIGLAStoragePropertyNames.STRORGUO_CODICE.value()).append(" = ").append("'").append(uo).append("'");
            final StorageObject storageObjectByPath1 = getStorageObjectByPath(SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione());
            if (Optional.ofNullable(storageObjectByPath1).isPresent()) {
                query.append(" and IN_TREE(var, '").append(
                        storageObjectByPath1.getKey()).append("')");
            }
            List<Integer> variazioniSigned = new ArrayList<Integer>();
            if (tiSigned != null &&
                    tiSigned.equals(ArchiviaStampaPdgVariazioneBulk.VIEW_NOT_SIGNED)) {
                variazioniSigned.addAll(findVariazioniSigned(esercizio, cds, uo, variazionePdg));
            }
            return search(query.toString()).stream()
                    .map(storageObject -> storageObject.<List<BigInteger>>getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                    .map(list -> list.get(0))
                    .map(BigInteger::intValue)
                    .filter(numeroVariazione -> !variazioniSigned.contains(numeroVariazione))
                    .collect(Collectors.toList());
        }
    }
}