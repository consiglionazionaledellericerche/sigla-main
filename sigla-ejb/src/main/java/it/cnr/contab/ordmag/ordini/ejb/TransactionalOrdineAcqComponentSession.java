package it.cnr.contab.ordmag.ordini.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalOrdineAcqComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements OrdineAcqComponentSession {
public void gestioneStampaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,it.cnr.jada.comp.ComponentException{
	try {
		invoke("gestioneStampaOrdine",new Object[] {
			userContext,
			ordine });
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
public void completaOrdine(UserContext userContext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException{
	try {
		invoke("completaOrdine",new Object[] {
			userContext,
			ordine });
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
public Boolean isUtenteAbilitatoOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
	try {
		return (Boolean)invoke("isUtenteAbilitatoOrdine",new Object[] {
				usercontext,
				ordine });
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
public Boolean isUtenteAbilitatoValidazioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
	try {
		return (Boolean)invoke("isUtenteAbilitatoValidazioneOrdine",new Object[] {
				usercontext,
				ordine });
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
