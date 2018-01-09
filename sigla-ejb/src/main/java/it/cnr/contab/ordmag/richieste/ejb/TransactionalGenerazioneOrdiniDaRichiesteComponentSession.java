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
