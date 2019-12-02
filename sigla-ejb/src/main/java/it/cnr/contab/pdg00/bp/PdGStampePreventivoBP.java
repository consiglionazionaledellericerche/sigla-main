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

package it.cnr.contab.pdg00.bp;
/**
 * Business Process per la gestione delle stampe su PDG
 */
import it.cnr.contab.utenze00.bp.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;

public class PdGStampePreventivoBP extends it.cnr.jada.util.action.BulkBP {
public PdGStampePreventivoBP() {
	super();
}

public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, java.lang.String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}

public boolean isUoPrincipale(CNRUserContext userContext) throws javax.ejb.EJBException,java.rmi.RemoteException, ComponentException {
	it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession comp = (it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_PdGPreventivoComponentSession", it.cnr.contab.pdg00.ejb.PdGPreventivoComponentSession.class);
	return comp.isUoPrincipale(userContext);
	}
}