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

package it.cnr.contab.gestiva00.bp;

import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class QuadriVaVeBP extends QuadriLiqAnnualeBP {

	private int status = INSERT;
public QuadriVaVeBP() {
	this("");
}
public QuadriVaVeBP(String function) {
	super(function+"Tr");
}
public Liquidazione_iva_annualeVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Quadri_va_veVBulk bulk = new Quadri_va_veVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return (Liquidazione_iva_annualeVBulk)bulk.initializeForSearch(this,context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk manage(it.cnr.jada.action.ActionContext context, it.cnr.contab.gestiva00.core.bulk.Liquidazione_iva_annualeVBulk bulk) throws it.cnr.jada.action.BusinessProcessException {

	try {
		return createComponentSession().tabCodIvaVendite(context.getUserContext(), bulk);
	} catch (Throwable t) {
		throw handleException(t);
	}	

}
}
