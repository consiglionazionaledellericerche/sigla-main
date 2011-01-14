package it.cnr.contab.config00.tabnum.ejb;

import javax.ejb.Remote;

@Remote
public interface Numerazione_baseComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
java.lang.Long creaNuovoProgressivo(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException,java.rmi.RemoteException;
java.lang.Long creaNuovoProgressivoTemp(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException,java.rmi.RemoteException;
}
