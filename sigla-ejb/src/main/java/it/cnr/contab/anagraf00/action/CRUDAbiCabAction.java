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

package it.cnr.contab.anagraf00.action;

import it.cnr.contab.anagraf00.bp.CRUDAbiCabBP;
import it.cnr.contab.anagraf00.ejb.AbiCabComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;

/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 15.22.45)
 *
 * @author: CNRADM
 */
public class CRUDAbiCabAction extends it.cnr.jada.util.action.CRUDAction {
    /**
     * CRUDAbiCabAction constructor comment.
     */
    public CRUDAbiCabAction() {
        super();
    }

    public Forward doBlankSearchFind_comune(ActionContext context, AbicabBulk abicab) {

        if (abicab != null)
            abicab.resetComune();
        return context.findDefaultForward();
    }

    public Forward doBringBackSearchFind_comune(ActionContext context, AbicabBulk abicab, ComuneBulk comune) {

        try {
            if (abicab != null && comune != null) {
                CRUDAbiCabBP bp = (CRUDAbiCabBP) getBusinessProcess(context);

                abicab.setComune(comune);
                bp.findCaps(context);
            }
            return context.findDefaultForward();

        } catch (BusinessProcessException ex) {
            return handleException(context, ex);
        }

    }

	/**
	 * Ripristina un ABI/CAB cancellato logicamente
	 * @param context
	 * @return
	 * @throws java.rmi.RemoteException
	 */
	public Forward doRipristina(ActionContext context) throws java.rmi.RemoteException {

		try {
			fillModel(context);
			CRUDAbiCabBP bp = (CRUDAbiCabBP) getBusinessProcess(context);
			if (bp.isDirty()) {
				bp.setMessage("Non è possibile ripristinare in questo momento!");
			} else {
				AbicabBulk abicab = (AbicabBulk) bp.getModel();
				abicab.setFl_cancellato(Boolean.FALSE);
				abicab.setToBeUpdated();
				bp.update(context);
                bp.edit(context, bp.getModel());
				bp.setMessage("Ripristino effettuato");
			}
			return context.findDefaultForward();
		} catch (Throwable e) {
			return handleException(context, e);
		}
	}

    /**
     * Gestisce un comando di cancellazione.
     */
    public Forward doElimina(ActionContext context) throws java.rmi.RemoteException {

        try {
            fillModel(context);

            CRUDAbiCabBP bp = (CRUDAbiCabBP) getBusinessProcess(context);
            if (!bp.isEditing()) {
                bp.setMessage("Non è possibile cancellare in questo momento");
            } else {
                bp.delete(context);
                AbicabBulk abicab = (AbicabBulk) bp.getModel();
                AbiCabComponentSession session = (AbiCabComponentSession) bp.createComponentSession();
                if (session.isCancellatoLogicamente(context.getUserContext(), abicab)) {
                    bp.edit(context, abicab);
                    bp.setMessage("Annullamento effettuato");
                } else {
                    bp.reset(context);
                    bp.setMessage("Cancellazione effettuata");
                }
            }
            return context.findDefaultForward();

        } catch (Throwable e) {
            return handleException(context, e);
        }
    }
}
