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

package it.cnr.contab.config00.action;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * <!-- @TODO: da completare -->
 */

public class CRUDTipo_linea_attivitaAction extends it.cnr.jada.util.action.CRUDAction {
public CRUDTipo_linea_attivitaAction() {
	super();
}

/**
 * Gestisce una richiesta di cancellazione dal controller "cdrAssociati"
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doAddToCRUDMain_cdrAssociati(ActionContext context) {
	try {
		fillModel(context);
		CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)context.getBusinessProcess();
		it.cnr.jada.util.RemoteIterator ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,((it.cnr.contab.config00.ejb.Tipologia_linea_attivitaComponentSession)bp.createComponentSession()).cercaCdrAssociabili(context.getUserContext(),(Tipo_linea_attivitaBulk)bp.getModel()));
		int count = ri.countElements();
		if (count == 0) {
			bp.setMessage("Nessun cdr associabile");
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
		} else {
			SelezionatoreListaBP slbp = select(context,ri,it.cnr.jada.bulk.BulkInfo.getBulkInfo(CdrBulk.class),null,"doSelezionaCdr_associati",null,bp);
			slbp.setMultiSelection(true);
		}
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}

/**
 * Seleziona i CDR associati al tipo di linea di attività in processo
 *
 * @param context	L'ActionContext della richiesta
 * @return Il Forward alla pagina di risposta
 */
public Forward doSelezionaCdr_associati(ActionContext context) {
	try {
		CRUDTipo_linea_attivitaBP bp = (CRUDTipo_linea_attivitaBP)context.getBusinessProcess();
		bp.getCdrAssociati().reset(context);
		bp.setDirty(true);
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
}