package it.cnr.contab.util00.bp;

import it.cnr.contab.util00.bulk.cmis.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.upload.UploadedFile;

public class CRUDArchivioAllegati<T extends AllegatoGenericoBulk> extends SimpleDetailCRUDController {
	private static final long serialVersionUID = 1L;
	
	public CRUDArchivioAllegati(Class<T> class1, FormController formcontroller) {
		super("ArchivioAllegati", class1, "archivioAllegati", formcontroller);
	}
	
	@SuppressWarnings("unchecked")
	protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
		T allegato = (T)oggettobulk;
		UploadedFile file = ((it.cnr.jada.action.HttpActionContext)actioncontext).getMultipartParameter("main.ArchivioAllegati.file");
		if (!(file == null || file.getName().equals(""))) {
			allegato.setFile(file.getFile());
			allegato.setContentType(file.getContentType());
			allegato.setNome(allegato.parseFilename(file.getName()));
			allegato.setToBeUpdated();
			getParentController().setDirty(true);
		}
		oggettobulk.validate();		
		super.validate(actioncontext, oggettobulk);
	}
	
	@Override
	public boolean isShrinkable() {		
		return !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() && super.isShrinkable();
	}
	
	@Override
	public boolean isGrowable() {
		return !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() && super.isGrowable();
	}
}
