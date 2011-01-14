package it.cnr.contab.logregistry00.ejb;

import javax.ejb.Remote;

@Remote
public interface LogRegistryComponentSession extends it.cnr.jada.ejb.RicercaComponentSession {
it.cnr.jada.util.RemoteIterator cercaTabelleDiLog(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
