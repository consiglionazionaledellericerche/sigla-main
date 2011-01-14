package it.cnr.contab.config00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalLunghezza_chiaviComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements Lunghezza_chiaviComponentSession {
public java.lang.String extractCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("extractCdsKey",new Object[] {
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
public java.lang.String extractUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("extractUoKey",new Object[] {
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
public java.lang.String formatCapocontoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatCapocontoKey",new Object[] {
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
public java.lang.String formatCdrKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatCdrKey",new Object[] {
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
public java.lang.String formatCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatCdsKey",new Object[] {
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
public java.lang.String formatContoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatContoKey",new Object[] {
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
public java.lang.String formatKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2,java.lang.String param3,java.lang.Integer param4,java.lang.String param5) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatKey",new Object[] {
			param0,
			param1,
			param2,
			param3,
			param4,
			param5 });
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
public java.lang.String formatLinea_attivitaKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatLinea_attivitaKey",new Object[] {
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
public java.lang.String formatUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("formatUoKey",new Object[] {
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
public java.lang.String leftPadding(java.lang.String param0,int param1) throws RemoteException {
	try {
		return (java.lang.String)invoke("leftPadding",new Object[] {
			param0,
			new Integer(param1) });
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
