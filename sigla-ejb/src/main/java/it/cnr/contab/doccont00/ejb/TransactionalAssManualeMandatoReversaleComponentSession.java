package it.cnr.contab.doccont00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalAssManualeMandatoReversaleComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements AssManualeMandatoReversaleComponentSession {
public java.util.Collection loadReversaliAssociate(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.Collection)invoke("loadReversaliAssociate",new Object[] {
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
public java.util.List loadReversaliDisponibili(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.util.List)invoke("loadReversaliDisponibili",new Object[] {
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
