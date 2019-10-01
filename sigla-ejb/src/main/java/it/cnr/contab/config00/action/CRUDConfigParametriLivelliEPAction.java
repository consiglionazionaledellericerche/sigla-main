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

/*
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.action;

import it.cnr.contab.config00.bp.CRUDConfigParametriLivelliEPBP;
import it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigParametriLivelliEPAction extends CRUDAction {

	/**
	 * Construtor from superclass
	 *
	 */
	public CRUDConfigParametriLivelliEPAction() {
		super();
	}
	
	/**
	 * Viene richiamato nel momento in cui viene cambiato il livello delle Economica
	 */
	public Forward doCambiaLivelliEconomica(ActionContext context) {
		try {	
			CRUDConfigParametriLivelliEPBP bp = (CRUDConfigParametriLivelliEPBP)getBusinessProcess(context);
			Parametri_livelli_epBulk parLiv = (Parametri_livelli_epBulk)bp.getModel();
			Integer livOld = parLiv.getLivelli_eco();
			try {
				fillModel( context );
				if (!bp.isSearching())
					parLiv.validaLivelliValorizzati();

				return context.findDefaultForward();
			} catch(ValidationException e) {
				parLiv.setLivelli_eco(livOld);
				bp.setModel(context,parLiv);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Viene richiamato nel momento in cui viene cambiato il livello delle Patrimoniale
	 */
	public Forward doCambiaLivelliPatrimoniale(ActionContext context) {
		try {	
			CRUDConfigParametriLivelliEPBP bp = (CRUDConfigParametriLivelliEPBP)getBusinessProcess(context);
			Parametri_livelli_epBulk parLiv = (Parametri_livelli_epBulk)bp.getModel();
			Integer livOld = parLiv.getLivelli_pat();
			try {
				fillModel( context );
				if (!bp.isSearching())
					parLiv.validaLivelliValorizzati();

				return context.findDefaultForward();
			} catch(ValidationException e) {
				parLiv.setLivelli_pat(livOld);
				bp.setModel(context,parLiv);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
}
