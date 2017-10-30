package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface GenerazioneOrdiniDaRichiesteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	public RichiestaUopRigaBulk selezionaRichiestaPerOrdine (UserContext aUC,VRichiestaPerOrdiniBulk richiesta) throws RemoteException, EJBException, ComponentException;
}
