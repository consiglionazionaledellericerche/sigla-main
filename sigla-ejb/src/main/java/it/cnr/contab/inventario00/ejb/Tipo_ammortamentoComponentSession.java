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
public interface Tipo_ammortamentoComponentSession extends it.cnr.jada.ejb.CRUDDetailComponentSession {
void annullaModificaGruppi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void associaTuttiGruppi(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaGruppiAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaGruppiAssociabiliPerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaGruppiConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaGruppiConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator getAmmortamentoRemoteIteratorPerRiassocia(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
void inizializzaGruppi(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaGruppiPerModifica(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void modificaGruppi(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectGruppiByClause(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Tipo_ammortamentoBulk param1,java.lang.Class param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
