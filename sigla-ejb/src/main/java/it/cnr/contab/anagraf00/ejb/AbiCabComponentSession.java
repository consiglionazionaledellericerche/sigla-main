package it.cnr.contab.anagraf00.ejb;

import javax.ejb.Remote;

@Remote
public interface AbiCabComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk findCaps(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isCancellatoLogicamente(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.tabrif.bulk.AbicabBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.BancaBulk caricaStrutturaIban(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.BancaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
