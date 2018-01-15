package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface RichiestaUopComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	Boolean isUtenteAbilitatoRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException, EJBException;
	Boolean isUtenteAbilitatoValidazioneRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException, EJBException;
	RichiestaUopBulk completaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException;
}
