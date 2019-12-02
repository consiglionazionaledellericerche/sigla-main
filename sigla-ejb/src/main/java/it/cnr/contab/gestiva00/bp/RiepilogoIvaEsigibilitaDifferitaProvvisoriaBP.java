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
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.BulkBP;
import it.cnr.jada.util.ejb.EJBCommonServices;


public class RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP extends RiepilogoIvaEsigibilitaDifferitaBP {

	private int status = INSERT;
/**
 * Insert the method's description here.
 * Creation date: (2/24/2003 2:05:58 PM)
 */
public RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP() {

	this("");
}
public RiepilogoIvaEsigibilitaDifferitaProvvisoriaBP(String function) {
	super(function+"Tr");
}
public Riepilogo_iva_esigibilita_differitaVBulk createNewBulk(
	ActionContext context)
	throws BusinessProcessException {
		
	try {
		Riepilogo_iva_esigibilita_differita_provvisoriaVBulk bulk = new Riepilogo_iva_esigibilita_differita_provvisoriaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		return (Riepilogo_iva_esigibilita_differita_provvisoriaVBulk)bulk.initializeForSearch(this,context);
		
	} catch(Throwable e) {
		throw handleException(e);
	}
}
}
