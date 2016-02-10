package it.cnr.contab.bilaterali00.ejb;

import it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRBILATERALI00_EJB_BltVisiteComponentSession
 */
@Remote
public interface BltVisiteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionarioAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.util.Collection findListaBancheAnticipo(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	java.util.Collection findListaBanche(it.cnr.jada.UserContext param0,Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
	java.math.BigDecimal findRimborsoNettoPrevisto(it.cnr.jada.UserContext param0, Blt_visiteBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
