package it.cnr.contab.doccont00.consultazioni.ejb;

import javax.ejb.Remote;
@Remote
public interface ConsGAEComResSintComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazione(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}
