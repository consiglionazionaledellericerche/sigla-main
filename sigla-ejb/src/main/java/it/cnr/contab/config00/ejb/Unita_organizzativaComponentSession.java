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
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
@Remote
public interface Unita_organizzativaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk findUOByCodice(it.cnr.jada.UserContext param0,String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getUoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String getIndirizzoUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaUOWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca, String cds)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List<CdsBulk> findListaCds(UserContext userContext, int esercizio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
