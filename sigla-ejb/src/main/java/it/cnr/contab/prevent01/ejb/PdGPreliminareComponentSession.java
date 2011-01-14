package it.cnr.contab.prevent01.ejb;

import javax.ejb.Remote;

@Remote
public interface PdGPreliminareComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk cambiaStatoConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk riportaStatoPrecedenteConBulk(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
