/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.pdg00.service;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.service.DocumentiContabiliService;
import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.storage.PdgVariazioneDocument;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.SIGLAStoragePropertyNames;
import it.cnr.contab.util.SignP7M;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceClient;
import it.cnr.si.firmadigitale.firma.arss.ArubaSignServiceException;
import it.cnr.si.spring.storage.*;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.search.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class PdgVariazioniService extends DocumentiContabiliService {
    private transient static final Logger logger = LoggerFactory.getLogger(PdgVariazioniService.class);
    private Properties pecMailConf;
    @Autowired
    private StorageDriver storageDriver;
    @Autowired
    private ArubaSignServiceClient arubaSignServiceClient;

    @Value("${pec.variazioni.username}")
    private String pecVariazioniUsername;
    @Value("${pec.variazioni.password}")
    private String pecVariazioniPassword;
    @Value("${variazioni.ente.show.all}")
    private Boolean showAll;

    public PdgVariazioneDocument getPdgVariazioneDocument(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) {
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

    public String getCMISPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) {
        return Arrays.asList(
                getParentPath(archiviaStampaPdgVariazioneBulk),
                "Variazione al PdG n. "
                        + archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg()
                        + " CdR proponente "
                        + archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() + ".pdf"
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
    }

    public String getParentPath(ArchiviaStampaPdgVariazioneBulk archiviaStampaPdgVariazioneBulk) {
        return Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(),
                Optional.ofNullable(archiviaStampaPdgVariazioneBulk.getEsercizio())
                        .map(esercizio -> String.valueOf(esercizio))
                        .orElse("0"),
                sanitizeFolderName(archiviaStampaPdgVariazioneBulk.getCd_cds()),
                "CdR " + archiviaStampaPdgVariazioneBulk.getCd_centro_responsabilita() +
                        " Variazione " + Utility.lpad(archiviaStampaPdgVariazioneBulk.getPg_variazione_pdg(), 5, '0')
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
    }

    public List<Integer> findVariazioniSigned(Integer esercizio, Unita_organizzativaBulk cds, String uo, Long variazionePdg) throws ApplicationException {
        String basePath = Arrays.asList(
                SpringUtil.getBean(StorePath.class).getPathVariazioniPianoDiGestione(), String.valueOf(esercizio)
        ).stream().collect(
                Collectors.joining(StorageDriver.SUFFIX)
        );
        if (Optional.ofNullable(cds).isPresent()) {
            basePath = basePath.concat(StorageDriver.SUFFIX)
                    .concat(cds.getCd_unita_organizzativa());
        }
        final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(getStorageObjectByPath(basePath));
        if (storageObjectByPath.isPresent() && Optional.ofNullable(cds).isPresent() && !showAll) {
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
            if (cds != null && !cds.isUoEnte())
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
                Collectors.joining(StorageDriver.SUFFIX)
        );
        if (Optional.ofNullable(cds).isPresent()) {
            basePath = basePath.concat(StorageDriver.SUFFIX)
                    .concat(cds.getCd_unita_organizzativa());
        }
        final Optional<StorageObject> storageObjectByPath = Optional.ofNullable(getStorageObjectByPath(basePath));
        if (storageObjectByPath.isPresent() && Optional.ofNullable(cds).isPresent() && !showAll) {
            return getChildren(storageObjectByPath.get().getKey()).stream()
                    .map(storageObject -> getChildren(storageObject.getKey()))
                    .collect(ArrayList::new, List::addAll, List::addAll)
                    .stream()
                    .map(StorageObject.class::cast)
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
                    .map(storageObject -> storageObject.getPropertyValue(SIGLAStoragePropertyNames.VARPIANOGEST_NUMEROVARIAZIONE.value()))
                    .map(o -> {
                        if (o instanceof BigInteger) {
                            return ((BigInteger)o).intValue();
                        }
                        return new BigInteger(String.valueOf(o)).intValue();
                    })
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
            if (cds != null && !cds.isUoEnte())
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

    public void setPecMailConf(Properties pecMailConf) {
        this.pecMailConf = pecMailConf;
    }

    public void executeDeletePECMessage() {
        final javax.mail.Session session = javax.mail.Session.getInstance(pecMailConf);
        URLName urlName = new URLName(String.valueOf(pecMailConf.get("pec.url.name")));
        Store store = null;
        javax.mail.Folder folder = null;
        try {
            store = session.getStore(urlName);
            store.connect(pecVariazioniUsername, pecVariazioniPassword);
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            final List<SearchTerm> searchTerms = Arrays.asList(new SubjectTerm("Variazione al PdG"),
                    new ReceivedDateTerm(ComparisonTerm.LE,
                            Date.from(LocalDate.now().minusDays(10)
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant())));
            final List<Message> search = Arrays.asList(
                    folder.search(new AndTerm(searchTerms.toArray((new SearchTerm[searchTerms.size()]))))
            );
            search.stream()
                    .forEach(message -> {
                                try {
                                    message.setFlag(Flags.Flag.DELETED, true);
                                } catch (MessagingException e) {
                                    logger.error("PEC DELETE Message cannot be delete", e);
                                }
                            }
                    );
            logger.info("PEC DELETE deleted {} message.", search.size());
        } catch (AuthenticationFailedException e) {
            logger.warn("PEC DELETE with username {} failed to authenticate", pecVariazioniUsername);
        } catch (MessagingException e) {
            logger.error("PEC DELETE", e);
        } finally {
            Optional.ofNullable(folder).ifPresent(x -> {
                try {
                    x.close(true);
                } catch (Exception e) {
                    logger.error("PEC DELETE", e);
                }
            });
            Optional.ofNullable(store).ifPresent(x -> {
                try {
                    x.close();
                } catch (Exception e) {
                    logger.error("PEC DELETE", e);
                }
            });
        }
    }

    public String signVariazioni(SignP7M signP7M, String path) throws StorageException {
        StorageObject storageObject = storageDriver.getObject(signP7M.getNodeRefSource());
        StorageObject docFirmato =null;
        try {
            byte[] bytesSigned = arubaSignServiceClient.pdfsignatureV2(
                    signP7M.getUsername(),
                    signP7M.getPassword(),
                    signP7M.getOtp(),
                    IOUtils.toByteArray(getResource(storageObject)));

            Map<String, Object> metadataProperties = new HashMap<>();
            metadataProperties.put(StoragePropertyNames.NAME.value(), signP7M.getNomeFile());
            metadataProperties.put(StoragePropertyNames.OBJECT_TYPE_ID.value(), SIGLAStoragePropertyNames.CNR_ENVELOPEDDOCUMENT.value());

            docFirmato = storeSimpleDocument(
                    new ByteArrayInputStream(bytesSigned),
                    MimeTypes.PDF.mimetype(),
                    path,
                    metadataProperties);

            logger.info("VARIAZIONE {} firmata correttamente.", signP7M.getNomeFile());

            List<String> aspects = storageObject.getPropertyValue(StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value());
            aspects.add("P:cnr:signedDocument");
            updateProperties(Collections.singletonMap(
                    StoragePropertyNames.SECONDARY_OBJECT_TYPE_IDS.value(),
                    aspects
            ), storageObject);
            if (Optional.ofNullable(docFirmato).isPresent()) {
                createRelationship(storageObject.getKey(), docFirmato.getKey(), "R:varpianogest:allegatiVarBilancio");
                createRelationship(docFirmato.getKey(), storageObject.getKey(),  "R:cnr:signedDocumentAss");
            }
            return docFirmato.getKey();
        } catch (ArubaSignServiceException | IOException e) {
            throw new StorageException(StorageException.Type.GENERIC, e);
        }
    }
}