package it.cnr.contab.anagraf00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalAbiCabComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AbiCabComponentSession {
public it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk findCaps(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk)invoke("findCaps",new Object[] {
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
public boolean isCancellatoLogicamente(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isCancellatoLogicamente",new Object[] {
			param0,
			param1 })).booleanValue();
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
public it.cnr.contab.anagraf00.core.bulk.BancaBulk caricaStrutturaIban(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.BancaBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.anagraf00.core.bulk.BancaBulk)invoke("caricaStrutturaIban",new Object[] {
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
