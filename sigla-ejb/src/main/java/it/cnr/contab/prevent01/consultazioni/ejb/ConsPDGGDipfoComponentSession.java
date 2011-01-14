package it.cnr.contab.prevent01.consultazioni.ejb;

import javax.ejb.Remote;

@Remote
public interface ConsPDGGDipfoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazioneEtr(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
	it.cnr.jada.util.RemoteIterator findConsultazioneSpe(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}

