package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

@Remote
public interface PdGPreventivoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk annullaCDPSuPdg(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk caricaPdg(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk delDetByLA(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ApplicationException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isDettagliPdGModificabili(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaStatoPdG(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.contab.pdg00.comp.DiscrepanzeAggregatoException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk ribaltaCostiPdGArea(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk scaricaCDPSuPdg(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isUoPrincipale(it.cnr.contab.utenze00.bp.CNRUserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean controllaDiscrepanzeAggregatoForCons(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1, String param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
