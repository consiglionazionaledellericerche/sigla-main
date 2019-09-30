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

import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;
/**
 * Insert the type's description here.
 * Creation date: (19/12/2002 16.08.47)
 * @author: Simonetta Costa
 */
public class StampaPdcFinSpeseAction extends it.cnr.jada.util.action.BulkAction {
/**
 * StampaPdcFinSpeseAction constructor comment.
 */
public StampaPdcFinSpeseAction() {
	super();
}
/**
 * Gestisce un comando di stampa
 */
public Forward doPrint(ActionContext context) {
	try {

		StampaPdcFinSpeseBP bp = (StampaPdcFinSpeseBP)context.getBusinessProcess();
		fillModel(context);
		V_stampa_pdc_fin_speseBulk obj = (V_stampa_pdc_fin_speseBulk)bp.getModel();
		obj.validate();
		return doConfirmPrint(context,OptionBP.YES_BUTTON);

	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
