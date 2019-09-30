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

package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Lunghezza_chiaviComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
java.lang.String extractCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String extractUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCapocontoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCdrKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatContoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2,java.lang.String param3,java.lang.Integer param4,java.lang.String param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatLinea_attivitaKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String leftPadding(java.lang.String param0,int param1) throws java.rmi.RemoteException;
}
