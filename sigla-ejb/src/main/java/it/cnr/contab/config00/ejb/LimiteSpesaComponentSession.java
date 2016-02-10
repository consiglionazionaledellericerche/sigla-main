package it.cnr.contab.config00.ejb;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;
@Remote
public interface LimiteSpesaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	void validaCds(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
