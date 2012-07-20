package it.cnr.contab.bilaterali00.ejb;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRBILATERALI00_EJB_BltAccordiComponentSession
 */
@Remote
public interface BltAccordiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk setComuneEnteDiAppartenenza(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk setComuneEnteResponsIta(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
