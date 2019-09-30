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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;


public class CRUDTariffarioAction extends it.cnr.jada.util.action.CRUDAction {
	/**
 * CRUDFatturaAttivaAction constructor comment.
 */
public CRUDTariffarioAction() {
	super();
}
/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doEliminaTariffario(ActionContext context) throws java.rmi.RemoteException {

    try {
        CRUDTariffarioBP bp = (CRUDTariffarioBP) context.getBusinessProcess();
        TariffarioBulk tariffario = (TariffarioBulk) bp.getModel();

		try {
			java.sql.Timestamp dataAttuale = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
	        tariffario.setDataFineValidita(dataAttuale);
	        bp.setModel(context, tariffario);
	        if (tariffario.getDt_ini_validita().after(dataAttuale))
	            doElimina(context);
	        else
	        	doSalva(context);
		} catch (javax.ejb.EJBException e) {
			throw new it.cnr.jada.DetailedRuntimeException(e);
		}

        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }

}
}
