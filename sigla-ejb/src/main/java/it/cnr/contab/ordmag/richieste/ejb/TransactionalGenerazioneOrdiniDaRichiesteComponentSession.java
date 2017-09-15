package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class TransactionalGenerazioneOrdiniDaRichiesteComponentSession extends
		it.cnr.jada.ejb.TransactionalCRUDComponentSession implements GenerazioneOrdiniDaRichiesteComponentSession {
	public RichiestaUopRigaBulk selezionaRichiestaPerOrdine(UserContext aUC, VRichiestaPerOrdiniBulk richiesta)
			throws ComponentException, EJBException, RemoteException {
		try {
			return (RichiestaUopRigaBulk) invoke("selezionaRichiestaPerOrdine", new Object[] { aUC, richiesta });
		} catch (java.rmi.RemoteException e) {
			throw e;
		} catch (java.lang.reflect.InvocationTargetException e) {
			try {
				throw e.getTargetException();
			} catch (it.cnr.jada.comp.ComponentException ex) {
				throw ex;
			} catch (Throwable ex) {
				throw new java.rmi.RemoteException("Uncaugth exception", ex);
			}
		}
	}
}
