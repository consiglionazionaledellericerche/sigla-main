package it.cnr.contab.docamm00.consultazioni.ejb;

import javax.ejb.Remote;

@Remote
public interface MonitoCococoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	void inserisciRighe(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.consultazioni.bulk.Monito_cococoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}
