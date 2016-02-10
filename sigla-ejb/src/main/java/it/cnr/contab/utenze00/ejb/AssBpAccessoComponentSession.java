package it.cnr.contab.utenze00.ejb;
import javax.ejb.Remote;
@Remote
public interface AssBpAccessoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
