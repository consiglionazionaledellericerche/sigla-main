package it.cnr.contab.util00.ejb;

import javax.ejb.Remote;

@Remote
public interface PingComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
boolean ping(String param0, Integer param1) throws java.rmi.RemoteException;
}
