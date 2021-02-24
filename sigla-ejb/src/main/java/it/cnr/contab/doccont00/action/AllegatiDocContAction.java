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

package it.cnr.contab.doccont00.action;

import java.rmi.RemoteException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.doccont00.bp.AllegatiDocContBP;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;
import it.cnr.jada.util.action.OptionBP;
import it.cnr.jada.util.upload.UploadedFile;

public class AllegatiDocContAction extends CRUDAction {
	private static final long serialVersionUID = 1L;

	@Override
	public Forward doElimina(ActionContext actioncontext)
			throws RemoteException {
		super.doElimina(actioncontext);
		HookForward hookforward = (HookForward) actioncontext.findForward("close");
		if (hookforward != null)
			return hookforward;
		else
			return actioncontext.findDefaultForward();

	}
	public Forward doCaricaFolder(ActionContext actioncontext) throws BusinessProcessException {
		try {
			List<UploadedFile> uploadedFiles = ((it.cnr.jada.action.HttpActionContext) actioncontext)
					.getMultipartParameters("main.folder");
			if (uploadedFiles.isEmpty()) {
				throw new ValidationException("Nessun file da caricare!");
			}
			final OptionBP option = openConfirm(actioncontext,
					"Attenzione verranno caricati " + uploadedFiles.size() + " files, confermi il caricamento?",
					OptionBP.CONFIRM_YES_NO,
					"doConfirmCaricaFolder");
			option.addAttribute("uploadedFiles", uploadedFiles);
		} catch (Throwable throwable) {
			return handleException(actioncontext, throwable);
		}
		return actioncontext.findDefaultForward();
	}

	public Forward doConfirmCaricaFolder(ActionContext actioncontext, OptionBP option)
			throws RemoteException, BusinessProcessException {
		if (option.getOption() == OptionBP.YES_BUTTON) {
			try {
				final AllegatiDocContBP allegatiDocContBP = Optional.ofNullable(getBusinessProcess(actioncontext))
						.filter(AllegatiDocContBP.class::isInstance)
						.map(AllegatiDocContBP.class::cast)
						.orElseThrow(() -> new DetailedRuntimeException("BusinessProcess non trovato!"));
				final List<UploadedFile> uploadedFiles = Optional.ofNullable(option.getAttribute("uploadedFiles"))
						.filter(List.class::isInstance)
						.map(List.class::cast)
						.orElse(Collections.emptyList());
				allegatiDocContBP.aggiungiAllegati(actioncontext, uploadedFiles);
			} catch (Throwable throwable) {
				return handleException(actioncontext, throwable);
			}
		}
		return actioncontext.findDefaultForward();
	}
}