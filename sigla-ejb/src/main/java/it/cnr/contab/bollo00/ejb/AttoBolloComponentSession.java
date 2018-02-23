package it.cnr.contab.bollo00.ejb;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRBOLLO00_EJB_AttoBolloComponentSession
 */
@Remote
public interface AttoBolloComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazioneDettaglio(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4, boolean param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
