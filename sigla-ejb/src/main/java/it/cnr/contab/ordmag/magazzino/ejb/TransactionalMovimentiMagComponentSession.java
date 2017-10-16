package it.cnr.contab.ordmag.magazzino.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalMovimentiMagComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements MovimentiMagComponentSession {
public MovimentiMagBulk caricaOrdine(it.cnr.jada.UserContext userContext, EvasioneOrdineBulk evasioneOrdine) throws RemoteException,ComponentException, PersistencyException{
	try {
		return (MovimentiMagBulk)invoke("caricaOrdine",new Object[] {
				userContext,
				evasioneOrdine});
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
