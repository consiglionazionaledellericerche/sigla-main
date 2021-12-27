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

import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
import java.util.HashMap;

@Remote
public interface Inventario_beniComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk caricaInventario(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.util.Collection findTipiAmmortamento(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.lang.String getLocalTransactionID(it.cnr.jada.UserContext param0,boolean param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectBeniAccessoriFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectBuonoFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectFatturaFor(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Boolean isContab(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
HashMap<Obbligazione_scadenzarioBulk, Boolean> creaUtilizzatori(UserContext userContext, Obbligazione_scadenzarioBulk obbligazione_scadenzarioBulk, Buono_carico_scarico_dettBulk buono) throws ComponentException,java.rmi.RemoteException;
}
