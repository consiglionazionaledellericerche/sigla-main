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

package it.cnr.contab.gestiva00.actions;

import java.sql.Timestamp;

import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.gestiva00.bp.RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP;
import it.cnr.contab.gestiva00.core.bulk.Riepilogo_iva_esigibilita_differita_provvisoriaVBulk;
import it.cnr.jada.action.ActionContext;

/**
 * Insert the type's description here.
 * Creation date: (2/25/2003 10:31:57 AM)
 * @author: Roberto Peli
 */
public class RiepilogoIvaEsigibilitaDifferitaProvvisoriaAction extends RiepilogoIvaEsigibilitaDifferitaAction {
/**
 * RiepilogoIvaEsigibilitaDifferitaProvvisoriaAction constructor comment.
 */
public RiepilogoIvaEsigibilitaDifferitaProvvisoriaAction() {
	super();
}
public it.cnr.jada.action.Forward doSettaInteroAnno(ActionContext context) {

	RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP bp= (RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP) context.getBusinessProcess();
	try {
		bp.fillModel(context);

	} catch (Exception e) {

	}
	return setDateInteroAnno(context, (Riepilogo_iva_esigibilita_differita_provvisoriaVBulk) bp.getModel());
}
protected it.cnr.jada.action.Forward setDateInteroAnno(
	ActionContext context,
	Riepilogo_iva_esigibilita_differita_provvisoriaVBulk stampaBulk) {
		int esercizio = stampaBulk.getEsercizio().intValue();
		if (stampaBulk.getIntero_anno().booleanValue())
		{
		stampaBulk.setData_da(DateServices.getFirstDayOfYear(esercizio));
		stampaBulk.setData_a(DateServices.getLastDayOfYear(esercizio));
		}
		else
		{
		stampaBulk.setData_da(null);
		stampaBulk.setData_a(null);
		}
		stampaBulk.setMese(null);
	return context.findDefaultForward();
}
public it.cnr.jada.action.Forward doOnMeseChange(ActionContext context) {
     super.doOnMeseChange(context);
     
	 RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP bp= (RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP) context.getBusinessProcess();
	 Riepilogo_iva_esigibilita_differita_provvisoriaVBulk stampaBulk = (Riepilogo_iva_esigibilita_differita_provvisoriaVBulk) bp.getModel();
	 stampaBulk.setIntero_anno(new Boolean(false));
     
	 return context.findDefaultForward();
}
}
