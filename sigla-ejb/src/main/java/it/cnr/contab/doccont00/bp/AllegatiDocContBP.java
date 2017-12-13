package it.cnr.contab.doccont00.bp;

import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                    final List<AllegatoGenericoBulk> allegatiDocumentiAmministrativi = Optional.ofNullable(this.getParentModel())
                            .filter(Mandato_rigaIBulk.class::isInstance)
                            .map(Mandato_rigaIBulk.class::cast)
                            .map(mandato_rigaIBulk -> {
                                try {
                                    return Optional.ofNullable(Utility.createMandatoComponentSession().getDocumentoAmministrativoSpesaBulk(null, mandato_rigaIBulk))
                                            .filter(AllegatoStorePath.class::isInstance)
                                            .map(AllegatoStorePath.class::cast)
                                            .map(allegatoStorePath -> {
                                                StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
                                                return Optional.ofNullable(allegatoStorePath.getStorePath())
                                                        .filter(storePaths -> !storePaths.isEmpty())
                                                        .map(storePaths ->
                                                            storePaths.stream()
                                                                    .map(storePath -> Optional.ofNullable(storeService.getStorageObjectByPath(storePath))
                                                                            .map(StorageObject::getKey)
                                                                            .map(key -> storeService.getChildren(key, -1))
                                                                            .orElseGet(() -> Collections.emptyList()))
                                                                    .collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList())
                                                        ).orElseGet(() -> Collections.emptyList());
                                            }).map(list -> list.stream()
                                                    .filter(storageObject -> !Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()))
                                                            .map(String.class::cast)
                                                            .filter(s -> s.equals(StoragePropertyNames.CMIS_FOLDER.value()))
                                                            .isPresent())
                                                    .map(storageObject -> {
                                                AllegatoGenericoBulk allegato = new AllegatoGenericoBulk(storageObject.getKey());
                                                allegato.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                                                allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                                                allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                                                allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                                                return allegato;
                                            }).collect(Collectors.toCollection(ArrayList<AllegatoGenericoBulk>::new)))
                                            .orElseGet(() -> new ArrayList<AllegatoGenericoBulk>());
                                } catch (ComponentException | RemoteException e) {
                                    return new ArrayList<AllegatoGenericoBulk>();
                                }
                            }).orElseGet(() -> new ArrayList<AllegatoGenericoBulk>());
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
                        return v_mandato_reversaleBulk;
                    } catch (ComponentException | RemoteException | BusinessProcessException e) {
                        throw new DetailedRuntimeException(e);
                    }
                }).orElseGet(() -> {
                    try {
                        return Optional.ofNullable(super.initializeModelForEdit(actioncontext, oggettobulk))
                                .map(V_mandato_reversaleBulk.class::cast).orElse(null);
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
}