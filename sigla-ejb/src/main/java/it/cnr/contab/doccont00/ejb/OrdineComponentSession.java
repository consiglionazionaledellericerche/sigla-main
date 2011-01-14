package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface OrdineComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.doccont00.ordine.bulk.OrdineBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.ordine.bulk.OrdineBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.ordine.bulk.OrdineBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
