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

package it.cnr.contab.incarichi00.ejb;

import javax.ejb.Remote;

@Remote
public interface IncarichiRichiestaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk pubblicaSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiRichiesta(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiElenco(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6,String param7) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findListaIncarichiElencoArt18(it.cnr.jada.UserContext param0,String param1,String param2,Integer param3,String param4,String param5,String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiRichiesta(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiCollaborazione(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List completaListaIncarichiElenco(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
