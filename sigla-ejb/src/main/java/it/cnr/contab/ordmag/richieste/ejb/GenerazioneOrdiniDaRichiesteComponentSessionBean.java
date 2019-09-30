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

package it.cnr.contab.ordmag.richieste.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.richieste.comp.GenerazioneOrdiniDaRichiesteComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
@Stateless(name="CNRORDMAG00_EJB_GenerazioneOrdiniDaRichiesteComponentSession")
public class GenerazioneOrdiniDaRichiesteComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements GenerazioneOrdiniDaRichiesteComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new GenerazioneOrdiniDaRichiesteComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new GenerazioneOrdiniDaRichiesteComponentSessionBean();
}
public OrdineAcqBulk generaOrdine (UserContext aUC,OrdineAcqBulk ordine) throws ComponentException, javax.ejb.EJBException {
	pre_component_invocation(aUC,componentObj);
	try {
		OrdineAcqBulk result = ((GenerazioneOrdiniDaRichiesteComponent)componentObj).generaOrdine(aUC, ordine);
		component_invocation_succes(aUC,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(aUC,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(aUC,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(aUC,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(aUC,componentObj,e);
	}
}
public OrdineAcqBulk cercaRichieste(it.cnr.jada.UserContext aUC,OrdineAcqBulk ordine) throws ComponentException, javax.ejb.EJBException {
	pre_component_invocation(aUC,componentObj);
	try {
		OrdineAcqBulk result = ((GenerazioneOrdiniDaRichiesteComponent)componentObj).cercaRichieste(aUC, ordine);
		component_invocation_succes(aUC,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(aUC,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(aUC,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(aUC,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(aUC,componentObj,e);
	}
}
}
