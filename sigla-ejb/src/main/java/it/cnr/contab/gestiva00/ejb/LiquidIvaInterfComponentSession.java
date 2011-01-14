package it.cnr.contab.gestiva00.ejb;

import javax.ejb.Remote;

@Remote
public interface LiquidIvaInterfComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	boolean contaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void inserisciRighe(it.cnr.jada.UserContext param0, it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}