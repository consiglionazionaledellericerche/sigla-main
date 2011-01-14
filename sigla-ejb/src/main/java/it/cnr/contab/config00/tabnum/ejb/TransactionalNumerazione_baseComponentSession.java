package it.cnr.contab.config00.tabnum.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalNumerazione_baseComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Numerazione_baseComponentSession {
public java.lang.Long creaNuovoProgressivo(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.Long)invoke("creaNuovoProgressivo",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
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
public java.lang.Long creaNuovoProgressivoTemp(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.Long)invoke("creaNuovoProgressivoTemp",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4 });
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
