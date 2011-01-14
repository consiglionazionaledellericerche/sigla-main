package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface TrattamentoCORIComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk fillAllRows(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
