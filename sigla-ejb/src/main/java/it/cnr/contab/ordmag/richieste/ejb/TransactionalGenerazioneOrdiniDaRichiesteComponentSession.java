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

package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.comp.ComponentException;

public class TransactionalGenerazioneOrdiniDaRichiesteComponentSession extends
		it.cnr.jada.ejb.TransactionalCRUDComponentSession implements GenerazioneOrdiniDaRichiesteComponentSession {
	public OrdineAcqBulk generaOrdine(it.cnr.jada.UserContext userContext, OrdineAcqBulk ordine)
			throws ComponentException, EJBException, RemoteException {
		try {
			return (OrdineAcqBulk) invoke("generaOrdine", new Object[] { userContext, ordine });
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
	public OrdineAcqBulk cercaRichieste(it.cnr.jada.UserContext param0,OrdineAcqBulk ordine)
			throws ComponentException, EJBException, RemoteException {
		try {
			return (OrdineAcqBulk) invoke("cercaRichieste", new Object[] { param0, ordine });
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
