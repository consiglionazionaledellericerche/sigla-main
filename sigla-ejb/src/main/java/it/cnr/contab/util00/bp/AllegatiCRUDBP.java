package it.cnr.contab.util00.bp;

import it.cnr.contab.cmis.CMISAspect;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.contab.util00.cmis.bulk.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.Introspector;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import javax.servlet.ServletException;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;

public abstract class AllegatiCRUDBP<T extends AllegatoGenericoBulk, K extends AllegatoParentBulk> extends SimpleCRUDBP {
	private static final long serialVersionUID = 1L;
	protected SiglaCMISService cmisService;
	private CRUDArchivioAllegati<T> crudArchivioAllegati = new CRUDArchivioAllegati<T>(getAllegatoClass(), this);
			
	protected abstract CMISPath getCMISPath(K allegatoParentBulk, boolean create) throws BusinessProcessException;
	protected abstract Class<T> getAllegatoClass();
	
	public AllegatiCRUDBP() {
		super();
	}

	public AllegatiCRUDBP(String s) {
		super(s);
	}	
	/* 
	 * Necessario per la creazione di una form con enctype di tipo "multipart/form-data"
	 * Sovrascrive quello presente nelle superclassi
	 * 
	*/
	public void openForm(javax.servlet.jsp.PageContext context,String action,String target) throws java.io.IOException,javax.servlet.ServletException {
		openForm(context,action,target,"multipart/form-data");
	}

	@Override
	protected void initialize(ActionContext actioncontext)
			throws BusinessProcessException {
		cmisService = SpringUtil.getBean("cmisService",
				SiglaCMISService.class);		
		super.initialize(actioncontext);
	}

	public SimpleDetailCRUDController getCrudArchivioAllegati() {
		return crudArchivioAllegati;
	}

	public CRUDArchivioAllegati<T> getArchivioAllegati() {
		return crudArchivioAllegati;
	}
	
	public String getNomeAllegato(){
		AllegatoGenericoBulk allegato = (AllegatoGenericoBulk)getCrudArchivioAllegati().getModel();
		if (allegato != null && allegato.getDocument() != null)
			return allegato.getDocument().getName();
		return null;
	}

	public void scaricaAllegatoGenerico(ActionContext actioncontext) throws IOException, ServletException, ApplicationException {
		AllegatoGenericoBulk allegato = (T)crudArchivioAllegati.getModel();
		Document document = allegato.getDocument();
		InputStream is = cmisService.getResource(document);
		((HttpActionContext)actioncontext).getResponse().setContentLength(Long.valueOf(document.getContentStreamLength()).intValue());
		((HttpActionContext)actioncontext).getResponse().setContentType(document.getContentStreamMimeType());
		OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
		((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer,0,buflength);
		}
		is.close();
		os.flush();
	}

	public OggettoBulk initializeModelForEditAllegati(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk)oggettobulk;
		try {
			CMISPath path = getCMISPath((K) oggettobulk, false);
			if (path == null)
				return oggettobulk;
			Folder parent = (Folder) cmisService.getNodeByPath(path);
			for (CmisObject cmisObject : parent.getChildren()) {
				if (cmisService.hasAspect(cmisObject, CMISAspect.SYS_ARCHIVED.value()))
					continue;
				if (excludeChild(cmisObject))
					continue;
				if (cmisObject.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) {
					Document document = (Document) cmisObject;
					T allegato = (T) Introspector.newInstance(getAllegatoClass(), document);
					allegato.setContentType(document.getContentStreamMimeType());
					allegato.setNome(cmisObject.getName());
					allegato.setDescrizione((String)document.getPropertyValue(SiglaCMISService.PROPERTY_DESCRIPTION));
					allegato.setTitolo((String)document.getPropertyValue(SiglaCMISService.PROPERTY_TITLE));
					completeAllegato(allegato);
					allegato.setCrudStatus(OggettoBulk.NORMAL);
					allegatoParentBulk.addToArchivioAllegati(allegato);					
				}
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
	
	protected void completeAllegato(T allegato) {
	}
	
	protected boolean excludeChild(CmisObject cmisObject) {
		return false;
	}
	@Override
	public void update(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			archiviaAllegati(actioncontext, null);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		super.update(actioncontext);
	}
	
	@Override
	public void create(ActionContext actioncontext)
			throws BusinessProcessException {
		try {
			archiviaAllegati(actioncontext, null);
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		super.create(actioncontext);
	}
	
	@Override
	public void delete(ActionContext actioncontext)
			throws BusinessProcessException {
		AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk)getModel();
		try {
			for (AllegatoGenericoBulk allegato : allegatoParentBulk.getArchivioAllegati()) {
				cmisService.deleteNode(allegato.getDocument());
			}
		} catch (ApplicationException e) {
			throw handleException(e);
		}
		super.delete(actioncontext);
	}
	
	public String getAllegatiFormName() {
		return "default";
	}
	
	@SuppressWarnings("unchecked")
	protected void archiviaAllegati(ActionContext actioncontext, Document document) throws BusinessProcessException, ApplicationException{
		AllegatoParentBulk allegatoParentBulk = (AllegatoParentBulk)getModel();
		for (AllegatoGenericoBulk allegato : allegatoParentBulk.getArchivioAllegati()) {
			if (allegato.isToBeCreated()){
				try {
					Document node = cmisService.storeSimpleDocument(allegato, 
							new FileInputStream(allegato.getFile()),
							allegato.getContentType(),
							allegato.getNome(), getCMISPath((K) allegatoParentBulk, true));
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					throw handleException(e);
				} catch(CmisContentAlreadyExistsException _ex) {
					throw new ApplicationException("Il file " + allegato.getNome() +" già esiste!");
				}
			}else if (allegato.isToBeUpdated()) {
				try {
					if (allegato.getFile() != null)
						cmisService.updateContent(allegato.getDocument().getId(), 
								new FileInputStream(allegato.getFile()),
								allegato.getContentType());
					cmisService.updateProperties(allegato, allegato.getDocument());
					allegato.setCrudStatus(OggettoBulk.NORMAL);
				} catch (FileNotFoundException e) {
					throw handleException(e);
				}
			}
		}
		for (Iterator<AllegatoGenericoBulk> iterator = allegatoParentBulk.getArchivioAllegati().deleteIterator(); iterator.hasNext();) {
			AllegatoGenericoBulk allegato = iterator.next();
			if (allegato.isToBeDeleted()){
				cmisService.deleteNode(allegato.getDocument());
				allegato.setCrudStatus(OggettoBulk.NORMAL);
			}
		}
	}	
}
