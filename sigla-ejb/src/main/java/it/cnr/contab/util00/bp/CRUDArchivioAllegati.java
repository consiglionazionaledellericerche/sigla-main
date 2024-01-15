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

package it.cnr.contab.util00.bp;

import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.TableCustomizer;
import it.cnr.jada.util.upload.UploadedFile;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class CRUDArchivioAllegati<T extends AllegatoGenericoBulk> extends SimpleDetailCRUDController implements TableCustomizer {
	private static final long serialVersionUID = 1L;
	private boolean shrinkable = true;
	private boolean growable = true;
	private boolean readonlyOnEdit = false;

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

	@Override
	public String getRowStyle(Object obj) {
		return null;
	}

	@Override
	public String getRowCSSClass(Object obj, boolean even) {
		return null;
	}

	@Override
	public boolean isRowEnabled(Object obj) {
		return true;
	}

	@Override
	public boolean isRowReadonly(Object obj) {
		return false;
	}

	@Override
	public String getTableClass() {
		return null;
	}

	@Override
	public void writeTfoot(JspWriter jspwriter) throws IOException {

	}
}