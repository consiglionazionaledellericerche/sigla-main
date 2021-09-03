/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.magazzino.ejb;

import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.ordmag.magazzino.bulk.*;
import it.cnr.contab.ordmag.magazzino.comp.MovimentiMagComponent;
import it.cnr.contab.ordmag.magazzino.comp.TransitoBeniOrdiniComponent;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.List;

@Stateless(name="CNRINVENTARIO00_EJB_TransitoBeniOrdiniComponentSession")
public class TransitoBeniOrdiniComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements TransitoBeniOrdiniComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new TransitoBeniOrdiniComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new TransitoBeniOrdiniComponentSessionBean();
}

public Transito_beni_ordiniBulk gestioneTransitoInventario(UserContext userContext, MovimentiMagBulk movimentoCarico)  throws ComponentException, PersistencyException, RemoteException, ApplicationException{
	pre_component_invocation(userContext,componentObj);
	try {
		Transito_beni_ordiniBulk result = ((TransitoBeniOrdiniComponent)componentObj).gestioneTransitoInventario(userContext, movimentoCarico);
		component_invocation_succes(userContext,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(userContext,componentObj);
		throw e;
	} catch(ComponentException e) {
		component_invocation_failure(userContext,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(userContext,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(userContext,componentObj,e);
	}
}

}
