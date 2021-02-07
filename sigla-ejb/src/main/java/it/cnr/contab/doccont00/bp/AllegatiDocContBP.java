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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.docamm00.service.DocumentiCollegatiDocAmmService;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.doccont00.service.ContabiliService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormBP;
import it.cnr.jada.util.upload.UploadedFile;
import it.cnr.si.spring.storage.StorageException;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoStorePath;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import javax.servlet.ServletException;
import java.io.*;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllegatiDocContBP extends AllegatiCRUDBP<AllegatoDocContBulk, StatoTrasmissione> {
    private static final long serialVersionUID = 1L;
    private final SimpleDetailCRUDController documentiPassiviSelezionati =
            new SimpleDetailCRUDController("DocumentiPassiviSelezionati", Mandato_rigaIBulk.class, "mandato_rigaColl", this);
    private final SimpleDetailCRUDController dettaglioAllegati =
            new SimpleDetailCRUDController("DettaglioAllegati", AllegatoGenericoBulk.class, "allegatiColl", documentiPassiviSelezionati) {
                @Override
                public List getDetails() {
                    return Optional.ofNullable(this.getParentModel())
                            .filter(Mandato_rigaBulk.class::isInstance)
                            .map(Mandato_rigaBulk.class::cast)
                            .map(mandato_rigaBulk ->
                                    Optional.ofNullable(mandato_rigaBulk.getAllegatiDocumentiAmministrativi())
                                            .orElseGet(() -> getAllegatiDocumentiAmministrativi(mandato_rigaBulk))
                            ).orElseGet(() -> Collections.emptyList());
                }

                private List<AllegatoGenericoBulk> getAllegatiDocumentiAmministrativi(Mandato_rigaBulk mandato_rigaBulk) {
                    final List<AllegatoGenericoBulk> allegatiDocumentiAmministrativi =
                            SpringUtil.getBean("documentiCollegatiDocAmmService", DocumentiCollegatiDocAmmService.class).getAllegatiDocumentiAmministrativi(mandato_rigaBulk);
                    mandato_rigaBulk.setAllegatiDocumentiAmministrativi(allegatiDocumentiAmministrativi);
                    return allegatiDocumentiAmministrativi;
                }
            };
    private String allegatiFormName;
    private Map<String, String> rifModalitaPagamento = new HashMap<String, String>();

    public AllegatiDocContBP() {
        super();
    }

    public AllegatiDocContBP(String s) {
        super(s);
    }

    @Override
    public boolean isSearchButtonHidden() {
        return true;
    }

    @Override
    public boolean isFreeSearchButtonHidden() {
        return true;
    }

    @Override
    public boolean isDeleteButtonHidden() {
        return true;
    }

    @Override
    protected boolean excludeChild(StorageObject storageObject) {
        if (storageObject.getPropertyValue(StoragePropertyNames.OBJECT_TYPE_ID.value()).equals("D:doccont:document"))
            return true;
        return super.excludeChild(storageObject);
    }

    @Override
    public String getAllegatiFormName() {
        return allegatiFormName;
    }

    public void setAllegatiFormName(String allegatiFormName) {
        this.allegatiFormName = allegatiFormName;
    }

    public void addToRifModalitaPagamento(String key, String value) {
        rifModalitaPagamento.put(key, value);
    }

    @Override
    protected boolean isChildGrowable(boolean isGrowable) {
        return true;
    }

    @Override
    protected void getChildDetail(OggettoBulk oggettobulk) {
        AllegatoDocContBulk allegatoDocContBulk = (AllegatoDocContBulk) oggettobulk;
        initializeRifModalitaPagamentoKeys(allegatoDocContBulk);
        if (allegatoDocContBulk.getRifModalitaPagamento() != null && !allegatoDocContBulk.getRifModalitaPagamento().equalsIgnoreCase("GEN") &&
                (((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_INSERITO) ||
                        ((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO) ||
                        ((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA)))
            setStatus(VIEW);
        else
            setStatus(EDIT);
        super.getChildDetail(allegatoDocContBulk);
    }

    private void initializeRifModalitaPagamentoKeys(AllegatoDocContBulk allegatoDocContBulk) {
        OrderedHashtable rifModalitaPagamentoKeys = allegatoDocContBulk.getRifModalitaPagamentoKeys();
        rifModalitaPagamentoKeys.put("GEN", "Generico");
        if (!allegatoDocContBulk.isToBeCreated() || !(((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_INSERITO) ||
                ((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO) ||
                ((StatoTrasmissione) getModel()).getStato_trasmissione().equalsIgnoreCase(MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA))) {
            for (String key : rifModalitaPagamento.keySet()) {
                rifModalitaPagamentoKeys.put(key, rifModalitaPagamento.get(key));
            }
        }
    }

    @Override
    protected void completeAllegato(AllegatoDocContBulk allegato) throws ApplicationException {
        super.completeAllegato(allegato);
        Optional.ofNullable(storeService.getStorageObjectBykey(allegato.getStorageKey()))
                .map(storageObject -> storageObject.getPropertyValue("doccont:rif_modalita_pagamento"))
                .map(String.class::cast)
                .ifPresent(s -> allegato.setRifModalitaPagamento(s));
    }

    @Override
    public boolean isInputReadonly() {
        return super.isInputReadonly();
    }

    @Override
    public boolean isNewButtonHidden() {
        return true;
    }

    @Override
    public boolean isSaveButtonEnabled() {
        return super.isSaveButtonEnabled();
    }

    @Override
    public void update(ActionContext actioncontext)
            throws BusinessProcessException {
        try {
            archiviaAllegati(actioncontext);
        } catch (ApplicationException e) {
            throw handleException(e);
        }
    }

    @Override
    protected String getStorePath(StatoTrasmissione allegatoParentBulk, boolean create) throws BusinessProcessException {
        return allegatoParentBulk.getStorePath();
    }

    @Override
    protected Class<AllegatoDocContBulk> getAllegatoClass() {
        return AllegatoDocContBulk.class;
    }

    public final SimpleDetailCRUDController getDocumentiPassiviSelezionati() {
        return documentiPassiviSelezionati;
    }

    public final SimpleDetailCRUDController getDettaglioAllegati() {
        return dettaglioAllegati;
    }

    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
        return Optional.ofNullable(allegato)
                .filter(AllegatoDocContBulk.class::isInstance)
                .map(AllegatoDocContBulk.class::cast)
                .map(AllegatoDocContBulk::isCancellabile)
                .orElse(Boolean.TRUE);
    }

    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
        return Optional.ofNullable(allegato)
                .filter(AllegatoDocContBulk.class::isInstance)
                .map(AllegatoDocContBulk.class::cast)
                .map(AllegatoDocContBulk::isModificabile)
                .orElse(Boolean.TRUE);
    }

    @Override
    public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        final ContabiliService contabiliService = SpringUtil.getBean("contabiliService", ContabiliService.class);
        return Optional.ofNullable(oggettobulk)
                .filter(StatoTrasmissione.class::isInstance)
                .map(StatoTrasmissione.class::cast)
                .filter(statoTrasmissione -> statoTrasmissione.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN))
                .map(statoTrasmissione -> {
                    try {
                        OggettoBulk oggettoBulk = super.initializeModelForEditAllegati(actioncontext, oggettobulk);
                        MandatoBulk mandatoBulk = (MandatoBulk) createComponentSession().findByPrimaryKey(actioncontext.getUserContext(),
                                new MandatoIBulk(statoTrasmissione.getCd_cds(), statoTrasmissione.getEsercizio(), statoTrasmissione.getPg_documento_cont()));
                        contabiliService.getNodeRefContabile(mandatoBulk)
                                .stream()
                                .forEach(key ->  {
                                    final StorageObject storageObject = contabiliService.getStorageObjectBykey(key);
                                    AllegatoDocContBulk allegato = new AllegatoDocContBulk(storageObject.getKey());
                                    allegato.setRifModalitaPagamento("CONTABILE");
                                    allegato.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                                    allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                                    allegato.setDescrizione(
                                            Optional.ofNullable(storageObject.<String>getPropertyValue(StoragePropertyNames.DESCRIPTION.value()))
                                                .orElse("Contabile")
                                    );
                                    allegato.setTitolo(
                                            Optional.ofNullable(storageObject.<String>getPropertyValue(StoragePropertyNames.TITLE.value()))
                                                    .orElse("Contabile")
                                    );
                                    allegato.setCrudStatus(OggettoBulk.NORMAL);
                                    allegato.setCancellabile(Boolean.FALSE);
                                    allegato.setModificabile(Boolean.FALSE);
                                    ((AllegatoParentBulk)oggettoBulk).addToArchivioAllegati(allegato);
                                });
                        return oggettoBulk;
                    } catch (BusinessProcessException|ComponentException|RemoteException e) {
                        throw new DetailedRuntimeException(e);
                    }
                }).orElseGet(() -> {
                    try {
                        return super.initializeModelForEditAllegati(actioncontext, oggettobulk);
                    } catch (BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        return Optional.ofNullable(oggettobulk)
                .filter(StatoTrasmissione.class::isInstance)
                .map(StatoTrasmissione.class::cast)
                .filter(statoTrasmissione -> statoTrasmissione.getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN))
                .map(V_mandato_reversaleBulk.class::cast)
                .map(v_mandato_reversaleBulk -> {
                    setTab("tab", "tabAllegati");
                    try {
                        MandatoBulk mandatoBulk = (MandatoBulk) createComponentSession().findByPrimaryKey(actioncontext.getUserContext(),
                                new MandatoIBulk(v_mandato_reversaleBulk.getCd_cds(), v_mandato_reversaleBulk.getEsercizio(), v_mandato_reversaleBulk.getPg_documento_cont()));
                        Tipo_documento_ammBulk tipo_documento_ammBulk = new Tipo_documento_ammBulk();
                        tipo_documento_ammBulk.setTi_entrata_spesa("S");
                        final List<Tipo_documento_ammBulk> tipoDocAmms = Optional.ofNullable(createComponentSession().find(
                                actioncontext.getUserContext(), tipo_documento_ammBulk.getClass(),
                                "find", tipo_documento_ammBulk))
                                .filter(List.class::isInstance)
                                .map(List.class::cast)
                                .orElse(null);
                        mandatoBulk.setTipoDocumentoKeys(
                                Optional.ofNullable(tipoDocAmms.stream()
                                        .collect(Collectors.toMap(
                                                Tipo_documento_ammBulk::getCd_tipo_documento_amm,
                                                Tipo_documento_ammBulk::getDs_tipo_documento_amm,
                                                (u, v) -> {
                                                    throw new IllegalStateException(
                                                            String.format("Cannot have 2 values (%s, %s) for the same key", u, v)
                                                    );
                                                }, Hashtable::new)))
                                        .orElse(null)
                        );
                        final List<Mandato_rigaBulk> findMandato_riga = createComponentSession().find(actioncontext.getUserContext(), MandatoIBulk.class,
                                "findMandato_riga", actioncontext.getUserContext(), mandatoBulk)
                                .stream()
                                .filter(Mandato_rigaBulk.class::isInstance)
                                .map(Mandato_rigaBulk.class::cast)
                                .map(mandato_rigaBulk -> {
                                    mandato_rigaBulk.setMandato(mandatoBulk);
                                    return Optional.ofNullable(mandato_rigaBulk.getCd_tipo_documento_amm())
                                            .filter(cdTipoDocumentoAmm -> cdTipoDocumentoAmm.equals(Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA))
                                            .map(s -> {
                                                try {
                                                    return Utility.createMandatoComponentSession().inizializzaTi_fattura(actioncontext.getUserContext(), mandato_rigaBulk);
                                                } catch (ComponentException | RemoteException e) {
                                                    throw new DetailedRuntimeException(e);
                                                }
                                            }).orElseGet(() -> mandato_rigaBulk);
                                })
                                .collect(Collectors.toList());
                        v_mandato_reversaleBulk = (V_mandato_reversaleBulk) super.initializeModelForEdit(actioncontext, v_mandato_reversaleBulk);
                        v_mandato_reversaleBulk.setMandato_rigaColl(new BulkList<Mandato_rigaIBulk>(findMandato_riga));
                        return (OggettoBulk)v_mandato_reversaleBulk;
                    } catch (ComponentException | RemoteException | BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                }).orElseGet(() -> {
                    try {
                        return Optional.ofNullable(super.initializeModelForEdit(actioncontext, oggettobulk))
                                .orElse(null);
                    } catch (BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                });
    }

    public String getNomeAllegatoAmministrativo() throws ApplicationException {
        return Optional.ofNullable((AllegatoGenericoBulk) getDettaglioAllegati().getModel())
                .map(AllegatoGenericoBulk::getNome)
                .orElse(null);
    }

    public void scaricaAllegatoAmministrativo(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
        AllegatoGenericoBulk allegato = (AllegatoGenericoBulk) getDettaglioAllegati().getModel();
        StorageObject storageObject = storeService.getStorageObjectBykey(allegato.getStorageKey());
        InputStream is = storeService.getResource(allegato.getStorageKey());
        ((HttpActionContext) actioncontext).getResponse().setContentLength(
                (storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value())).intValue()
        );
        ((HttpActionContext) actioncontext).getResponse().setContentType(
                (String) storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value())
        );
        OutputStream os = ((HttpActionContext) actioncontext).getResponse().getOutputStream();
        ((HttpActionContext) actioncontext).getResponse().setDateHeader("Expires", 0);
        byte[] buffer = new byte[((HttpActionContext) actioncontext).getResponse().getBufferSize()];
        int buflength;
        while ((buflength = is.read(buffer)) > 0) {
            os.write(buffer, 0, buflength);
        }
        is.close();
        os.flush();
    }

    public void aggiungiAllegati(ActionContext actioncontext, List<UploadedFile> uploadedFiles) throws BusinessProcessException{
        final V_mandato_reversaleBulk v_mandato_reversaleBulk = Optional.ofNullable(getModel())
                .filter(V_mandato_reversaleBulk.class::isInstance)
                .map(V_mandato_reversaleBulk.class::cast)
                .orElseThrow(() -> handleException(new ApplicationException("Modello non trovato!")));
        for (UploadedFile uploadedFile : uploadedFiles) {
            AllegatoGenericoBulk allegato = new AllegatoGenericoBulk();
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
            try {
                final Optional<StorageObject> parentFolder =
                        Optional.ofNullable(storeService.getStorageObjectByPath(v_mandato_reversaleBulk.getStorePath()));
                if (parentFolder.isPresent()) {
                    storeService.storeSimpleDocument(
                            allegato,
                            new FileInputStream(allegato.getFile()),
                            allegato.getContentType(),
                            allegato.getNome(),
                            v_mandato_reversaleBulk.getStorePath()
                    );
                }
            } catch (FileNotFoundException e) {
                throw handleException(e);
            } catch (StorageException e) {
                if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                    throw handleException(new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!"));
                throw handleException(e);
            }
        }
        edit(actioncontext, v_mandato_reversaleBulk);
        setMessage(FormBP.INFO_MESSAGE, "Allegati inseriti correttamente al documento.");
    }
}