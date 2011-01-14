package it.cnr.contab.inventario00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalIdInventarioComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements IdInventarioComponentSession {
public it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk findInventarioFor(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,boolean param3) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk)invoke("findInventarioFor",new Object[] {
			param0,
			param1,
			param2,
			new Boolean(param3) });
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
public boolean isAperto(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isAperto",new Object[] {
			param0,
			param1,
			param2 })).booleanValue();
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
