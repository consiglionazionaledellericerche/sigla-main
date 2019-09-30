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

package it.cnr.contab.bilaterali00.ejb;

import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRBILATERALI00_EJB_BltVisiteComponentSession
 */
@Remote
public interface BltVisiteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionarioAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.util.Collection findListaBancheAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	java.util.Collection findListaBanche(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	java.math.BigDecimal findRimborsoNettoPrevisto(it.cnr.jada.UserContext param0, Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
