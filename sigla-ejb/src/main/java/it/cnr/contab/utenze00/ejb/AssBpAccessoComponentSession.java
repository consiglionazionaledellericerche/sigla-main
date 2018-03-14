package it.cnr.contab.utenze00.ejb;

import javax.ejb.Remote;

@Remote
public interface AssBpAccessoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
    it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext userContext, String businessProcess, String tiFunzione) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
}
