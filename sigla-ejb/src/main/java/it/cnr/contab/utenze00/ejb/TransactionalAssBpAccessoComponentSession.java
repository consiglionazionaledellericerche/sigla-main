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

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class TransactionalAssBpAccessoComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AssBpAccessoComponentSession {
    public java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (List) invoke("findAccessoByBP", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext param0, String param1, String param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.utenze00.bulk.AssBpAccessoBulk) invoke("finAssBpAccesso", new Object[]{
                    param0,
                    param1,
                    param2
            });
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    @Override
    public Map<String, String> findDescrizioneBP(UserContext param0) throws ComponentException, RemoteException {
        try {
            return (Map<String, String>) invoke("findDescrizioneBP", new Object[]{
                    param0
            });
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }
}
