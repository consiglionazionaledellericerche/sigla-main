package it.cnr.contab.util00.bp;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.upload.UploadedFile;

public class CRUDArchivioAllegatiType<T extends AllegatoGenericoBulk> extends SimpleDetailCRUDController {
	private static final long serialVersionUID = 1L;
	private boolean shrinkable = true;
	private boolean growable = true;
	private boolean readonlyOnEdit = false;

	public CRUDArchivioAllegatiType(Class<T> class1, FormController formcontroller) {
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
		if (getModel()!= null && getModel().getCrudStatus() == OggettoBulk.TO_BE_CREATED)
			return true;
		return !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() && super.isShrinkable() && shrinkable;
	}

	@Override
	public boolean isGrowable() {
		return !((it.cnr.jada.util.action.CRUDBP)getParentController()).isSearching() && super.isGrowable() && growable;
	}

	@Override
	public boolean isReadonly() {
		if (getModel()!= null && getModel().getCrudStatus() == OggettoBulk.NORMAL)
			return readonlyOnEdit;
		return super.isReadonly();
	}
	public void setShrinkable(boolean shrinkable) {
		this.shrinkable = shrinkable;
	}

	public void setGrowable(boolean growable) {
		this.growable = growable;
	}

	public void setReadonlyOnEdit(boolean readonlyOnEdit) {
		this.readonlyOnEdit = readonlyOnEdit;
	}

}