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

package it.cnr.contab.utenze00.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
import java.util.Map;

@Remote
public interface AssBpAccessoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
    it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext userContext, String businessProcess, String tiFunzione) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
    Map<String, String> findDescrizioneBP(UserContext userContext) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
}
