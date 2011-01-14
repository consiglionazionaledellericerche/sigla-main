package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliBulk;
import it.cnr.jada.UserContext;
@Remote
public interface AddizionaliComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
		AddizionaliBulk verifica_aggiornamento(UserContext usercontext, AddizionaliBulk addizionale) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void Aggiornamento_scaglione(UserContext usercontext, AddizionaliBulk addizionale) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void cancella_pendenti(UserContext usercontext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
