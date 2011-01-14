package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

@Remote
public interface RicostruzioneResiduiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession  {
it.cnr.contab.config00.sto.bulk.CdrBulk findCdrUo(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk findCdr(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isUOScrivaniaEnte(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public it.cnr.contab.pdg00.bulk.Pdg_residuoBulk calcolaDispCassaPerCds (it.cnr.jada.UserContext userContext, it.cnr.contab.pdg00.bulk.Pdg_residuoBulk residuo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public boolean isCdrSAC(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException;
public it.cnr.contab.pdg00.bulk.Pdg_residuoBulk caricaDettagliFiltrati(it.cnr.jada.UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.contab.pdg00.bulk.Pdg_residuoBulk testata) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
