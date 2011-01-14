package it.cnr.contab.docamm00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalRiportoDocAmmComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements RiportoDocAmmComponentSession {
public java.lang.String getStatoRiporto(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("getStatoRiporto",new Object[] {
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
public java.lang.String getStatoRiportoInScrivania(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (java.lang.String)invoke("getStatoRiportoInScrivania",new Object[] {
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk)invoke("riportaAvanti",new Object[] {
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
public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk)invoke("riportaIndietro",new Object[] {
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
