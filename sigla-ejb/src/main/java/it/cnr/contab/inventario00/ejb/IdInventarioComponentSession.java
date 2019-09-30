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
public interface IdInventarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isAperto(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
