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

package it.cnr.contab.prevent00.bp;

import it.cnr.contab.prevent00.ejb.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
/**
 * Adatta ed implementa {@link it.cnr.jada.util.action.BulkBP } per le funzionalità richieste
 * 		al CRUD del PdG aggregato
 * 
 * @author: Vincenzo Bisquadro
 */
public class CRUDPdGAggregatoBP extends it.cnr.jada.util.action.BulkBP implements it.cnr.jada.util.action.FindBP  {
/**
 * Costruttore standard di CRUDPdGAggregatoBP.
 */
public CRUDPdGAggregatoBP() {
	super();
}
/**
 * Costruttore di CRUDPdGAggregatoBP cui viene passata la funzione in ingresso.
 *
 * @param function java.lang.String
 */
public CRUDPdGAggregatoBP(String function) {
	super(function);
}
public Pdg_aggregatoBulk caricaPdg_aggregato(ActionContext context) throws BusinessProcessException {
	try {
		setModel(context,((PdgAggregatoComponentSession)createComponentSession()).caricaPdg_aggregato(context.getUserContext(),(Pdg_aggregatoBulk)getModel()));
		return (Pdg_aggregatoBulk)getModel();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public it.cnr.contab.prevent00.ejb.PdgAggregatoComponentSession createComponentSession() throws javax.ejb.EJBException,java.rmi.RemoteException {
	return (it.cnr.contab.prevent00.ejb.PdgAggregatoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT00_EJB_PdgAggregatoComponentSession", it.cnr.contab.prevent00.ejb.PdgAggregatoComponentSession.class);
}

public RemoteIterator find(ActionContext context,CompoundFindClause clause,OggettoBulk model)  throws BusinessProcessException {
	try {
		return null;
	} catch(Exception e) {
		throw handleException(e);
	}
}

public RemoteIterator find(ActionContext actionContext,CompoundFindClause clause,OggettoBulk bulk,OggettoBulk context,String attribute) throws BusinessProcessException {
	try {
		return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(actionContext,createComponentSession().cerca(actionContext.getUserContext(),clause,bulk,context,attribute));
	} catch(Exception e) {
		throw handleException(e);
	}
}
public Pdg_aggregatoBulk getPdg_aggregato() {
	return (Pdg_aggregatoBulk)getModel();
}
/**
 * Init dei dati e degli oggetti
 *
 * @param config {@link it.cnr.jada.action.Config } in uso.
 * 
 * @param context {@link it.cnr.jada.action.ActionContext } in uso.
 *
 * @exception handleException
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	super.init(config,context);
	caricaPdg_aggregato(context);
}

}
