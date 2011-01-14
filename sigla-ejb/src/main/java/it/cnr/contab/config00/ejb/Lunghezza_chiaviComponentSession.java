package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Lunghezza_chiaviComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
java.lang.String extractCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String extractUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCapocontoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCdrKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatCdsKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatContoKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatKey(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2,java.lang.String param3,java.lang.Integer param4,java.lang.String param5) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatLinea_attivitaKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String formatUoKey(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.String leftPadding(java.lang.String param0,int param1) throws java.rmi.RemoteException;
}
