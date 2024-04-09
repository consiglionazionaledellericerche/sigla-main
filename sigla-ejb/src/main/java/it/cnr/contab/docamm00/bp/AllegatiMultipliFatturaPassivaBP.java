/*
 * Copyright (C) 2021  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.IAllegatoFatturaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.ejb.FatturaPassivaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.AllegatoFatturaBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllegatiMultipliFatturaPassivaBP extends SimpleCRUDBP {
    private final List<IAllegatoFatturaBulk> documents;
    protected StoreService storeService;
    private final String type;

    public AllegatiMultipliFatturaPassivaBP(List<IAllegatoFatturaBulk> documents, String type) {
        this.documents = documents;
        this.type = type;
    }

    public AllegatiMultipliFatturaPassivaBP(String s, List<IAllegatoFatturaBulk> documents, String type) {
        super(s);
        this.documents = documents;
        this.type = type;
    }

    @Override
    protected void initialize(ActionContext actioncontext)
            throws BusinessProcessException {
        storeService = SpringUtil.getBean("storeService", StoreService.class);
        super.initialize(actioncontext);
        AllegatoFatturaBulk allegato = (AllegatoFatturaBulk) getModel();
        allegato.setAspectName(this.type);
    }

    @Override
    public RemoteIterator find(ActionContext actionContext, CompoundFindClause compoundFindClause, OggettoBulk oggettoBulk, OggettoBulk oggettoBulk1, String s) throws BusinessProcessException {
        return null;
    }

    public String getLabel() {
        return documents.stream().findAny().get().getTitleAllegatoMultiplo() + documentsLabel(documents);
    }

    private String documentsLabel(List<IAllegatoFatturaBulk> list) {
        return list
                .stream()
                .map(iAllegatoFatturaBulk -> iAllegatoFatturaBulk.getAllegatoLabel())
                .collect(Collectors.joining("-"));
    }

    public String getAllegatiFormName() {
        final String allegatiFormName = "default";
        if (Optional.ofNullable(getModel())
                .filter(AllegatoFatturaBulk.class::isInstance)
                .map(AllegatoFatturaBulk.class::cast)
                .flatMap(afb -> Optional.ofNullable(afb.getAspectName()))
                .filter(s -> s.equalsIgnoreCase(AllegatoFatturaBulk.P_SIGLA_FATTURE_ATTACHMENT_LIQUIDAZIONE))
                .isPresent()) {
            return "protocollo";
        }
        return allegatiFormName.equalsIgnoreCase("default") ? "base" : allegatiFormName;
    }

    @Override
    public void save(ActionContext actioncontext) throws ValidationException, BusinessProcessException {
        AllegatoFatturaBulk allegato = (AllegatoFatturaBulk) getModel();
        List<UploadedFile> uploadedFiles = ((it.cnr.jada.action.HttpActionContext) actioncontext)
                .getMultipartParameters("main.file");
        if (uploadedFiles.isEmpty())
            throw new ValidationException("Inserire l'allegato!");
        for (UploadedFile uploadedFile : uploadedFiles) {
            allegato.setContentType(
                    Optional.ofNullable(uploadedFile)
                            .flatMap(uploadedFile1 -> Optional.ofNullable(uploadedFile1.getContentType()))
                            .orElseThrow(() -> handleException(new ApplicationException("Non è stato possibile determinare il tipo di file!")))
            );
            allegato.setNome(
                    Optional.ofNullable(uploadedFile)
                            .flatMap(uploadedFile1 -> Optional.ofNullable(uploadedFile1.getName()))
                            .orElseThrow(() -> handleException(new ApplicationException("Non è stato possibile determinare il nome del file!")))

            );
            allegato.setFile(
                    Optional.ofNullable(uploadedFile)
                            .flatMap(uploadedFile1 -> Optional.ofNullable(uploadedFile1.getFile()))
                            .orElseThrow(() -> handleException(new ApplicationException("File non presente!")))
            );
            allegato.validate();
            try {
                for (IAllegatoFatturaBulk iAllegatoFatturaBulk : documents) {
                    final String s = iAllegatoFatturaBulk.getStorePath().get(0);
                    final Optional<StorageObject> parentFolder =
                            Optional.ofNullable(storeService.getStorageObjectByPath(s));
                    if (parentFolder.isPresent()) {
                        storeService.storeSimpleDocument(
                                allegato,
                                new FileInputStream(allegato.getFile()),
                                allegato.getContentType(),
                                allegato.getNome(),
                                parentFolder.get().getPath()
                        );
                    }
                    if (iAllegatoFatturaBulk instanceof Fattura_passiva_IBulk) {
                        Fattura_passiva_IBulk fatturaPassivaIBulk = (Fattura_passiva_IBulk) iAllegatoFatturaBulk;
                        final Optional<AllegatoFatturaBulk> provvedimentoLiquidazione = Optional.of(allegato)
                                .filter(allegatoFatturaBulk -> AllegatoFatturaBulk.P_SIGLA_FATTURE_ATTACHMENT_LIQUIDAZIONE.equalsIgnoreCase(allegatoFatturaBulk.getAspectName()))
                                .filter(allegatoFatturaBulk -> Optional.ofNullable(allegatoFatturaBulk.getDataProtocollo()).isPresent());
                        final FatturaPassivaComponentSession fatturaPassivaComponentSession =
                                (FatturaPassivaComponentSession) EJBCommonServices.createEJB("CNRDOCAMM00_EJB_FatturaPassivaComponentSession");
                        if (provvedimentoLiquidazione.isPresent()) {
                            fatturaPassivaIBulk = (Fattura_passiva_IBulk) fatturaPassivaComponentSession
                                    .inizializzaBulkPerModifica(actioncontext.getUserContext(), fatturaPassivaIBulk);
                            fatturaPassivaIBulk.setToBeUpdated();
                            fatturaPassivaIBulk.setDt_protocollo_liq(
                                    Optional.ofNullable(provvedimentoLiquidazione.get().getDataProtocollo())
                                            .map(Date::getTime)
                                            .map(aLong -> new Timestamp(aLong))
                                            .orElse(null)
                            );
                            fatturaPassivaIBulk.setNr_protocollo_liq(provvedimentoLiquidazione.get().getNumProtocollo());
                            fatturaPassivaIBulk.setStato_liquidazione(IDocumentoAmministrativoBulk.LIQ);
                            fatturaPassivaIBulk.setCausale(null);
                            fatturaPassivaComponentSession
                                    .modificaConBulk(actioncontext.getUserContext(), fatturaPassivaIBulk);
                        } else if (
                                Optional.ofNullable(fatturaPassivaIBulk.getStato_liquidazione())
                                        .filter(s1 -> !s1.equalsIgnoreCase(IDocumentoAmministrativoBulk.LIQ))
                                        .isPresent()
                        ) {
                            fatturaPassivaIBulk = (Fattura_passiva_IBulk) fatturaPassivaComponentSession
                                    .inizializzaBulkPerModifica(actioncontext.getUserContext(), fatturaPassivaIBulk);
                            fatturaPassivaIBulk.setToBeUpdated();
                            fatturaPassivaIBulk.setStato_liquidazione(IDocumentoAmministrativoBulk.LIQ);
                            fatturaPassivaIBulk.setCausale(null);
                            fatturaPassivaComponentSession
                                    .modificaConBulk(actioncontext.getUserContext(), fatturaPassivaIBulk);
                        }
                    }
                }
            } catch (FileNotFoundException | ComponentException | RemoteException e) {
                throw handleException(e);
            } catch (StorageException e) {
                if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                    throw handleException(new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!"));
                throw handleException(e);
            }
        }
        allegato.setNome(null);
        setMessage(FormBP.INFO_MESSAGE, "Allegati inseriti correttamente ai documenti.");
        setDirty(Boolean.FALSE);
    }

    @Override
    public boolean isSearchButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isDeleteButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isNewButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isFreeSearchButtonHidden() {
        return Boolean.TRUE;
    }

    @Override
    public void openForm(PageContext context, String action, String target) throws IOException, ServletException {
        openForm(context, action, target, "multipart/form-data");
    }
}
