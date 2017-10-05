package it.cnr.contab.ordmag.ordini.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface EvasioneOrdineComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
//	public Boolean isUtenteAbilitatoEvasioneOrdine(UserContext usercontext, OrdineAcqBulk ordine) throws RemoteException,ComponentException, PersistencyException, EJBException;
	it.cnr.jada.util.RemoteIterator cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk evasioneOrdine) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
