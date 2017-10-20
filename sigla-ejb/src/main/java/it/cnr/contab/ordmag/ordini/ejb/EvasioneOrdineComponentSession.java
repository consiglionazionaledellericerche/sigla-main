package it.cnr.contab.ordmag.ordini.ejb;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface EvasioneOrdineComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	EvasioneOrdineBulk cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk evasioneOrdine) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
    void evadiOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine)throws ComponentException, PersistencyException,java.rmi.RemoteException;
	
}
