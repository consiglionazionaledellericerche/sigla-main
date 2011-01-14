package it.cnr.contab.preventvar00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
@Remote
public interface ConsAssCompPerDataComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	RemoteIterator findVariazioniDettaglio(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4, OggettoBulk param5) throws ComponentException,RemoteException;
	RemoteIterator findVariazioni(UserContext param0, OggettoBulk param1) throws  ComponentException, RemoteException;
}
