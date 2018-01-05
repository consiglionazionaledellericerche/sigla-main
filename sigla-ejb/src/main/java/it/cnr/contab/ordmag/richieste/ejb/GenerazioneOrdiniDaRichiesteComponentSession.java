package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
@Remote
public interface GenerazioneOrdiniDaRichiesteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	public OrdineAcqBulk generaOrdine (UserContext aUC,OrdineAcqBulk ordine) throws RemoteException, EJBException, ComponentException;
	public OrdineAcqBulk cercaRichieste(it.cnr.jada.UserContext param0,OrdineAcqBulk ordine) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
