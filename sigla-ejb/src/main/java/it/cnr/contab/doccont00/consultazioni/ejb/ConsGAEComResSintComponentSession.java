package it.cnr.contab.doccont00.consultazioni.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.Remote;
@Remote
public interface ConsGAEComResSintComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazione(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator findConsultazioneRend(it.cnr.jada.UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause baseclause,it.cnr.jada.persistency.sql.CompoundFindClause param4)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}
