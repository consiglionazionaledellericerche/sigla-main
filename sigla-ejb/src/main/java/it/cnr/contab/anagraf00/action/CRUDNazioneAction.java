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

import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.contab.anagraf00.bp.*;
import it.cnr.jada.action.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 16.10.22)
 * @author: CNRADM
 */
public class CRUDNazioneAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDNazioneAction constructor comment.
 */
public CRUDNazioneAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 16.10.52)
 * @return it.cnr.jada.action.Forward
 * @param context it.cnr.jada.action.ActionContext
 */
public Forward doOnTipoNazioneChange(ActionContext context) {

	try{
		fillModel(context);
		CRUDNazioneBP bp = (CRUDNazioneBP)getBusinessProcess(context);
		NazioneBulk nazione = (NazioneBulk)bp.getModel();
		if (nazione.isTipoIndifferente())
			nazione.setPg_nazione(new Long(0));
		else
			nazione.setPg_nazione(null);
		
		return context.findDefaultForward();

	}catch(it.cnr.jada.bulk.FillException ex){
		return handleException(context, ex);
	}
}
}
