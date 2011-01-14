package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.compensi00.tabrif.bulk.Esenzioni_addizionaliBulk;
import it.cnr.jada.UserContext;
@Remote
public interface Esenzioni_addizionaliComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Esenzioni_addizionaliBulk verifica_aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void Aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void cancella_pendenti(UserContext usercontext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
