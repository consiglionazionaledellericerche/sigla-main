package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface NumTempDocContComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.lang.Long getNextTempPg(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long getNextTempPg(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long getNextTempPg(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long getNextTempPg(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
