package it.cnr.contab.gestiva00.ejb;

import javax.ejb.Remote;

@Remote
public interface LiquidIvaInterfComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	boolean contaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void inserisciRighe(it.cnr.jada.UserContext param0, it.cnr.contab.gestiva00.core.bulk.Liquid_iva_interfBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
	it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk inizializzaMese(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
	void saveRipartizioneFinanziaria(it.cnr.jada.UserContext param0,it.cnr.contab.gestiva00.core.bulk.Liquidazione_definitiva_ivaVBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
}