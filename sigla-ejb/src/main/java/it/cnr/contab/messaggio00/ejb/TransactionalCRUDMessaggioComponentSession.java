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

package it.cnr.contab.messaggio00.ejb;

import java.rmi.RemoteException;

public class TransactionalCRUDMessaggioComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements CRUDMessaggioComponentSession {

    public void setMessaggioVisionato(it.cnr.jada.UserContext param0, it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("setMessaggioVisionato", new Object[]{
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

    public boolean isMessaggioVisionato(it.cnr.jada.UserContext param0, it.cnr.contab.messaggio00.bulk.MessaggioBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isMessaggioVisionato", new Object[]{
                    param0,
                    param1})).booleanValue();
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

	public void deleteMessaggi(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException {
		try {
			invoke("deleteMessaggi", new Object[]{
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
