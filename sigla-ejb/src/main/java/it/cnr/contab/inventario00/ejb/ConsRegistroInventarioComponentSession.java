package it.cnr.contab.inventario00.ejb;

import it.cnr.contab.inventario00.consultazioni.bulk.VInventarioRicognizioneBulk;

import javax.ejb.Remote;

@Remote
public interface ConsRegistroInventarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazione(it.cnr.jada.UserContext param0,java.lang.String param1,it.cnr.jada.persistency.sql.CompoundFindClause param2,it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.util.RemoteIterator findConsultazioneRicognizione(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.persistency.sql.CompoundFindClause param2,VInventarioRicognizioneBulk inv) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}