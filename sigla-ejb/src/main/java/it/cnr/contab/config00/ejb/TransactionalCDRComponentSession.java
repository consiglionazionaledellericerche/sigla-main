package it.cnr.contab.config00.ejb;
import java.rmi.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.*;

import java.util.List;

public class TransactionalCDRComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements CDRComponentSession {
public boolean isCdrEnte(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isCdrEnte",new Object[] {
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
public boolean isEnte(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return ((Boolean)invoke("isEnte",new Object[] {
			param0 })).booleanValue();
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
public it.cnr.contab.config00.sto.bulk.CdrBulk cdrFromUserContext(it.cnr.jada.UserContext param0) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.config00.sto.bulk.CdrBulk)invoke("cdrFromUserContext",new Object[] {
			param0});
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

public CdrBulk getCdrEnte(UserContext param0) throws it.cnr.jada.comp.ComponentException  {
	try {
		return (it.cnr.contab.config00.sto.bulk.CdrBulk)invoke("getCdrEnte",new Object[] {
			param0});
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new it.cnr.jada.comp.ComponentException(ex);
		}
	} catch (RemoteException e) {
		throw new it.cnr.jada.comp.ComponentException(e);
	}
}
public List findListaCDRWS(UserContext userContext, String uo, String query,
		String dominio, String tipoRicerca) throws ComponentException,
		RemoteException {
	try {
		return (List)invoke("findListaCDRWS",new Object[] {
				userContext,
				uo,
				query,
				dominio,
				tipoRicerca});
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
