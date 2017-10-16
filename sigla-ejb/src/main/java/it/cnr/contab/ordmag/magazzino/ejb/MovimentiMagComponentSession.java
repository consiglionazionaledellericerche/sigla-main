package it.cnr.contab.ordmag.magazzino.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface MovimentiMagComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	public MovimentiMagBulk caricaOrdine(it.cnr.jada.UserContext userContext, EvasioneOrdineBulk evasioneOrdine) throws RemoteException,ComponentException, PersistencyException;
}
