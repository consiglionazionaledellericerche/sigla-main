package it.cnr.contab.docamm00.ejb;
import java.rmi.*;

import it.cnr.jada.util.ejb.*;

public class TransactionalBeneServizioComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements BeneServizioComponentSession {
public it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk completaElementoVoceOf(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException {
	try {
		return (it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk)invoke("completaElementoVoceOf",new Object[] {
			param0,
			param1 });
	} catch(java.rmi.RemoteException e) {
		throw e;
	} catch(java.lang.reflect.InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch(it.cnr.jada.comp.ComponentException ex) {
			throw ex;
		} catch(Throwable ex) {
			throw new java.rmi.RemoteException("Uncaugth exception",ex);
		}
	}
}
}
