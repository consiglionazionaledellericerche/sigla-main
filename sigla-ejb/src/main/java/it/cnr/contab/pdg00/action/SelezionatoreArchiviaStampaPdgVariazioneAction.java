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

package it.cnr.contab.pdg00.action;

import it.cnr.contab.pdg00.bulk.ArchiviaStampaPdgVariazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.SelezionatoreListaAction;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class SelezionatoreArchiviaStampaPdgVariazioneAction extends
		SelezionatoreListaAction {
    
	public Forward doCambiaVisibilita(ActionContext context) {
		SelezionatoreListaBP bp = (SelezionatoreListaBP)context.getBusinessProcess();
		ArchiviaStampaPdgVariazioneBulk bulk = (ArchiviaStampaPdgVariazioneBulk)bp.getModel();
		String tiSigned = bulk.getTiSigned();
		try {
			fillModel(context);
			refresh(context, bp);
			return context.findDefaultForward();
		} catch(Throwable e) {
			bulk.setTiSigned(tiSigned);
			return handleException(context,e);
		}
	}

	public PdGVariazioniComponentSession createComponentSession(ActionContext context) throws BusinessProcessException {
		SelezionatoreListaBP bp = (SelezionatoreListaBP)context.getBusinessProcess();
		return
			(PdGVariazioniComponentSession)bp.createComponentSession(
			"CNRPDG00_EJB_PdGVariazioniComponentSession",PdGVariazioniComponentSession.class);
	}

	public void refresh(ActionContext context, SelezionatoreListaBP bp) throws BusinessProcessException {
		try {
			bp.setIterator(context,EJBCommonServices.openRemoteIterator(context,
					createComponentSession(context).
					cercaVariazioniForDocumentale(context.getUserContext(), null, 
								new Pdg_variazioneBulk(),
								((ArchiviaStampaPdgVariazioneBulk)bp.getModel()).getTiSigned(),
								Boolean.TRUE)));
		} catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

}
