package it.cnr.contab.bilaterali00.ejb;

import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;

import java.rmi.RemoteException;

public class TransactionalBltVisiteComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements BltVisiteComponentSession {
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionarioAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk)invoke("findCessionarioAnticipo",new Object[] {
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
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
		try {
			return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk)invoke("findCessionario",new Object[] {
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
	public java.util.Collection findListaBancheAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
		try {
			return (java.util.Collection)invoke("findListaBancheAnticipo",new Object[] {
				param0,
				param1 });
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
	public java.util.Collection findListaBanche(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
		try {
			return (java.util.Collection)invoke("findListaBanche",new Object[] {
				param0,
				param1 });
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
	public java.math.BigDecimal findRimborsoNettoPrevisto(it.cnr.jada.UserContext param0, Blt_visiteBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
		try {
			return (java.math.BigDecimal)invoke("findRimborsoPrevisto",new Object[] {
				param0,
				param1 });
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

