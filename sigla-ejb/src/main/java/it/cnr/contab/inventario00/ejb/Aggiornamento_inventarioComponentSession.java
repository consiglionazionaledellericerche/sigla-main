package it.cnr.contab.inventario00.ejb;

import javax.ejb.Remote;

@Remote
public interface Aggiornamento_inventarioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    it.cnr.jada.util.RemoteIterator cerca(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,java.lang.Class param2,it.cnr.jada.bulk.OggettoBulk param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
    it.cnr.jada.util.RemoteIterator cercaBeniAggiornabili(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void modificaBeniAggiornati(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk AggiornaBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.bulk.OutdatedResourceException,it.cnr.jada.bulk.BusyResourceException;
    it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk aggiornaTuttiBeni(it.cnr.jada.UserContext param0,it.cnr.contab.inventario00.docs.bulk.Aggiornamento_inventarioBulk param1,it.cnr.jada.persistency.sql.CompoundFindClause param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
