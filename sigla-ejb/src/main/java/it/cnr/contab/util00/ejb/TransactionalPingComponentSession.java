package it.cnr.contab.util00.ejb;

import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalPingComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements PingComponentSession {
public boolean ping(String param0, Integer param1) throws RemoteException {
	try {
		return ((Boolean)invoke("ping",new Object[] {
			param0, param1})).booleanValue();
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
