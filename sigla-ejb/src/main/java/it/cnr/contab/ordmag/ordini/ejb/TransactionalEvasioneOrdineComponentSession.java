package it.cnr.contab.ordmag.ordini.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;

public class TransactionalEvasioneOrdineComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements EvasioneOrdineComponentSession {
public it.cnr.jada.util.RemoteIterator cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.util.RemoteIterator)invoke("cercaOrdini",new Object[] {
			param0,
			param1 });
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
