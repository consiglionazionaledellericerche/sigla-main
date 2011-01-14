package it.cnr.contab.doccont00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
@Remote
public interface ConsSospesiEntSpeComponentSession	 {
	it.cnr.jada.util.RemoteIterator findConsSospesiSpesa(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) throws ComponentException, RemoteException, IntrospectionException;
	it.cnr.jada.util.RemoteIterator findConsSospesiEntrata(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) throws ComponentException, RemoteException, IntrospectionException;
	}

