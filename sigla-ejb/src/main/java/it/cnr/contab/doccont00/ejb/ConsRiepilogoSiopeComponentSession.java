package it.cnr.contab.doccont00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

@Remote
public interface ConsRiepilogoSiopeComponentSession	extends it.cnr.jada.ejb.CRUDComponentSession {
	RemoteIterator findSiopeDettaglioMandati(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4, OggettoBulk param5) throws ComponentException,RemoteException;
	RemoteIterator findSiopeMandati(UserContext param0, OggettoBulk param1) throws  ComponentException, RemoteException;
	RemoteIterator findSiopeDettaglioReversali(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4, OggettoBulk param5) throws ComponentException,RemoteException;
	RemoteIterator findSiopeReversali(UserContext param0, OggettoBulk param1) throws  ComponentException, RemoteException;
}
