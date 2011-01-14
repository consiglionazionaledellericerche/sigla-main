package it.cnr.contab.prevent00.ejb;

import javax.ejb.Remote;

@Remote
public interface PdgAggregatoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk caricaPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isPdGAggregatoModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaStatoPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
