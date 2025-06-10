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

package it.cnr.contab.inventario00.ejb;

import javax.ejb.Remote;

@Remote
public interface Aggiornamento_inventarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,java.lang.Class param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
    it.cnr.jada.util.RemoteIterator cercaBeniAggiornabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void modificaBeniAggiornati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.bulk.OutdatedResourceException,it.cnr.jada.bulk.BusyResourceException;
    it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaStatoBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.bulk.OutdatedResourceException,it.cnr.jada.bulk.BusyResourceException;
    it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
