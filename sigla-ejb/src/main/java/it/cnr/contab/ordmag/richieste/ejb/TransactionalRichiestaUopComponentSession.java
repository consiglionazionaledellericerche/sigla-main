package it.cnr.contab.ordmag.richieste.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class TransactionalRichiestaUopComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements RichiestaUopComponentSession {

	public void completaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException{
		try {
			invoke("completaRichiesta",new Object[] {
				userContext,
				richiesta });
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
	public Boolean isUtenteAbilitatoRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
		try {
			return (Boolean)invoke("isUtenteAbilitatoRichiesta",new Object[] {
					usercontext,
				richiesta });
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
	public Boolean isUtenteAbilitatoValidazioneRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException,javax.ejb.EJBException, RemoteException{
		try {
			return (Boolean)invoke("isUtenteAbilitatoValidazioneRichiesta",new Object[] {
					usercontext,
				richiesta });
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
