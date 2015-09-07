package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface CupComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	String recuperoCds(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
