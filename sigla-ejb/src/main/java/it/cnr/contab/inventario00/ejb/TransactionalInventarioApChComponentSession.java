package it.cnr.contab.inventario00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalInventarioApChComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements InventarioApChComponentSession {
public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.inventario00.docs.bulk.OptionRequestParameter param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.jada.bulk.OggettoBulk)invoke("creaConBulk",new Object[] {
			param0,
			param1,
			param2 });
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
public it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chBulk loadInventarioApChAttuale(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	try {
		return (it.cnr.contab.inventario00.tabrif.bulk.Inventario_ap_chBulk)invoke("loadInventarioApChAttuale",new Object[] {
			param0 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.PersistencyException ex) {
			throw ex;
		} catch(it.cnr.jada.persistency.IntrospectionException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
