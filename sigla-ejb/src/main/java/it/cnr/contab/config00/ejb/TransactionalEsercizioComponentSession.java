package it.cnr.contab.config00.ejb;
import java.rmi.RemoteException;

import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class TransactionalEsercizioComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements EsercizioComponentSession {
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk apriPianoDiGestione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk)invoke("apriPianoDiGestione",new Object[] {
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk)invoke("cambiaStatoConBulk",new Object[] {
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
public boolean isEsercizioChiuso(UserContext userContext) throws ComponentException, RemoteException {
	try {
		
		return ((Boolean)invoke("isEsercizioChiuso",new Object[] {
				userContext})).booleanValue();
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk getLastEsercizioOpen( UserContext param0 ) throws ComponentException, RemoteException {
	try {
		return (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk)invoke("getLastEsercizioOpen",new Object[] {
			param0 });
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
public boolean isEsercizioAperto(UserContext userContext)
		throws ComponentException, RemoteException {
try {
		
		return ((Boolean)invoke("isEsercizioAperto",new Object[] {
				userContext})).booleanValue();
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
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk getEsercizio( UserContext param0 ) throws ComponentException, RemoteException {
	try {
		return (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk)invoke("getEsercizio",new Object[] {
			param0 });
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
