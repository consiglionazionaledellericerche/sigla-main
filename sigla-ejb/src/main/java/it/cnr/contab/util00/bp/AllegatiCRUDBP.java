package it.cnr.contab.util00.bp;

import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.storage.StorageException;
import it.cnr.contab.spring.storage.StorageObject;
import it.cnr.contab.spring.storage.StoreService;
import it.cnr.contab.spring.storage.config.StoragePropertyNames;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import javax.servlet.ServletException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Optional;

public abstract class AllegatiCRUDBP<T extends AllegatoGenericoBulk, K extends AllegatoParentBulk> extends SimpleCRUDBP {
    private static final long serialVersionUID = 1L;
    protected StoreService storeService;
    private CRUDArchivioAllegati<T> crudArchivioAllegati = new CRUDArchivioAllegati<T>(getAllegatoClass(), this) {

        public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
            addChildDetail(oggettobulk);
            return super.addDetail(oggettobulk);
        }

        ;

        protected OggettoBulk getDetail(int i) {
            OggettoBulk oggettoBulk = super.getDetail(i);
            getChildDetail(oggettoBulk);
            return oggettoBulk;
        }

        ;

        public boolean isGrowable() {
            return isChildGrowable(super.isGrowable());
        }

        ;

        public boolean isShrinkable() {
            return isPossibileCancellazione((AllegatoGenericoBulk) getModel()) && super.isShrinkable();
        }

        ;

        public OggettoBulk removeDetail(int i) {
            AllegatoGenericoBulk all = (AllegatoGenericoBulk) getDetails().get(i);
            if (all.isNew() || isPossibileCancellazione(all))
                return super.removeDetail(i);
            return null;
        }
    };

    public AllegatiCRUDBP() {
        super();
    }

    public AllegatiCRUDBP(String s) {
        super(s);
    }

    protected abstract String getStorePath(K allegatoParentBulk, boolean create) throws BusinessProcessException;

    protected abstract Class<T> getAllegatoClass();

    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
        return true;
    }

    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
        return true;
    }

    /*
     * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
     * Sovrascrive quello presente nelle superclassi
     *
     */
    public void openForm(javax.servlet.jsp.PageContext context, String action, String target) throws java.io.IOException, javax.servlet.ServletException {
        openForm(context, action, target, "multipart/form-data");
    }

    protected boolean isChildGrowable(boolean isGrowable) {
        return isGrowable;
    }

    protected void getChildDetail(OggettoBulk oggettobulk) {
    }

    protected void addChildDetail(OggettoBulk oggettobulk) {
    }

    @Override
    protected void initialize(ActionContext actioncontext)
            throws BusinessProcessException {
        storeService = SpringUtil.getBean("storeService", StoreService.class);
        super.initialize(actioncontext);
    }

    public SimpleDetailCRUDController getCrudArchivioAllegati() {
        return crudArchivioAllegati;
    }

    public CRUDArchivioAllegati<T> getArchivioAllegati() {
        return crudArchivioAllegati;
    }

    public String getNomeAllegato() throws ApplicationException {
        return Optional.ofNullable((AllegatoGenericoBulk) getCrudArchivioAllegati().getModel())
                .map(AllegatoGenericoBulk::getStorageKey)
                .map(storageKey -> storeService.getStorageObjectBykey(storageKey).getPropertyValue(StoragePropertyNames.NAME.value()))
                .map(String.class::cast)
                .orElse(null);
    }

    public void scaricaAllegatoGenerico(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
        AllegatoGenericoBulk allegato = (T) crudArchivioAllegati.getModel();
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

    public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk) oggettobulk;
        try {
            String path = getStorePath((K) oggettobulk, false);
            if (path == null)
                return oggettobulk;
            if (storeService.getStorageObjectByPath(path) == null)
                return oggettobulk;
            for (StorageObject storageObject : storeService.getChildren(storeService.getStorageObjectByPath(path).getKey())) {
                if (storeService.hasAspect(storageObject, StoragePropertyNames.SYS_ARCHIVED.value()))
                    continue;
                if (excludeChild(storageObject))
                    continue;
                if (Optional.ofNullable(storageObject.getPropertyValue(StoragePropertyNames.BASE_TYPE_ID.value()))
                        .map(String.class::cast)
                        .filter(s -> s.equals(StoragePropertyNames.CMIS_FOLDER.value()))
                        .isPresent())
                    continue;
                T allegato = (T) Introspector.newInstance(getAllegatoClass(), storageObject.getKey());
                allegato.setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
                allegato.setNome(storageObject.getPropertyValue(StoragePropertyNames.NAME.value()));
                allegato.setDescrizione(storageObject.getPropertyValue(StoragePropertyNames.DESCRIPTION.value()));
                allegato.setTitolo(storageObject.getPropertyValue(StoragePropertyNames.TITLE.value()));
                completeAllegato(allegato);
                allegato.setCrudStatus(OggettoBulk.NORMAL);
                allegatoParentBulk.addToArchivioAllegati(allegato);
            }
        } catch (ApplicationException e) {
            throw handleException(e);
        } catch (NoSuchMethodException e) {
            throw handleException(e);
        } catch (IllegalAccessException e) {
            throw handleException(e);
        } catch (InstantiationException e) {
            throw handleException(e);
        } catch (InvocationTargetException e) {
            throw handleException(e);
        }
        return oggettobulk;
    }

    @Override
    public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
        oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
        return initializeModelForEditAllegati(actioncontext, oggettobulk);
    }

    protected void completeAllegato(T allegato) throws ApplicationException {
    }

    protected boolean excludeChild(StorageObject storageObject) {
        return false;
    }

    @Override
    public void update(ActionContext actioncontext)
            throws BusinessProcessException {
        super.update(actioncontext);
        try {
            archiviaAllegati(actioncontext);
        } catch (ApplicationException e) {
            throw handleException(e);
        }
    }

    @Override
    public void create(ActionContext actioncontext)
            throws BusinessProcessException {
        super.create(actioncontext);
        try {
            archiviaAllegati(actioncontext);
        } catch (ApplicationException e) {
            throw handleException(e);
        }
    }

    @Override
    public void delete(ActionContext actioncontext)
            throws BusinessProcessException {
        AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk) getModel();
        for (AllegatoGenericoBulk allegato : allegatoParentBulk.getArchivioAllegati()) {
        	if (!allegato.getDaNonEliminare()){
                storeService.delete(allegato.getStorageKey());
        	}
        }
        super.delete(actioncontext);
    }

    public String getAllegatiFormName() {
        if (this.getCrudArchivioAllegati().getModel() != null && !this.getCrudArchivioAllegati().getModel().isNew())
            if (!isPossibileModifica((AllegatoGenericoBulk) this.getCrudArchivioAllegati().getModel()))
                return "readonly";
        return "default";
    }

    @SuppressWarnings("unchecked")
    protected void archiviaAllegati(ActionContext actioncontext) throws BusinessProcessException, ApplicationException {
        AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk) getModel();
        for (AllegatoGenericoBulk allegato : allegatoParentBulk.getArchivioAllegati()) {
            if (allegato.isToBeCreated()) {
                try {
                    storeService.storeSimpleDocument(allegato,
                            new FileInputStream(allegato.getFile()),
                            allegato.getContentType(),
                            allegato.getNome(),
                            getStorePath((K) allegatoParentBulk,
                                    true));
                    allegato.setCrudStatus(OggettoBulk.NORMAL);
                } catch (FileNotFoundException e) {
                    throw handleException(e);
                } catch (StorageException e) {
                    if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                        throw new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!");
                    throw handleException(e);
                }
            } else if (allegato.isToBeUpdated()) {
                if (isPossibileModifica(allegato)) {
                    try {
                        if (allegato.getFile() != null) {
                            storeService.updateStream(allegato.getStorageKey(),
                                    new FileInputStream(allegato.getFile()),
                                    allegato.getContentType());
                        }
                        storeService.updateProperties(allegato, storeService.getStorageObjectBykey(allegato.getStorageKey()));
                        allegato.setCrudStatus(OggettoBulk.NORMAL);
                    } catch (FileNotFoundException e) {
                        throw handleException(e);
                    } catch (StorageException e) {
                        if (e.getType().equals(StorageException.Type.CONSTRAINT_VIOLATED))
                            throw new ApplicationException("File [" + allegato.getNome() + "] gia' presente. Inserimento non possibile!");
                        throw handleException(e);
                    }
                }
            }
        }
        gestioneCancellazioneAllegati(allegatoParentBulk);
    }

    protected void gestioneCancellazioneAllegati(AllegatoParentBulk allegatoParentBulk) throws ApplicationException {
        for (Iterator<AllegatoGenericoBulk> iterator = allegatoParentBulk.getArchivioAllegati().deleteIterator(); iterator.hasNext(); ) {
            AllegatoGenericoBulk allegato = iterator.next();
            if (allegato.isToBeDeleted()) {
                Optional.ofNullable(allegato)
                        .flatMap(allegatoGenericoBulk -> Optional.ofNullable(allegatoGenericoBulk.getStorageKey()))
                        .ifPresent(storageKey -> storeService.delete(storageKey));
                allegato.setCrudStatus(OggettoBulk.NORMAL);
            }
        }
    }
}
