package it.cnr.contab.prevent01.ejb;

import javax.ejb.Remote;

@Remote
public interface PdgAggregatoModuloComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.config00.sto.bulk.CdrBulk cdrFromUserContext(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaStatoPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isPdGAggregatoModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void findAndInsertBulkForMacro(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk scaricaDipendentiSuPdGP(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaScaricaDipendentiSuPdGP(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getCdrPdGP(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaCancellazionePdgModulo(it.cnr.jada.UserContext userContext, it.cnr.contab.prevent01.bulk.Pdg_moduloBulk pdg_modulo) throws  it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
