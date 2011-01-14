package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

@Remote
public interface ConguaglioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doAbilitaConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk doCreaCompensoConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isConguaglioAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk reloadConguaglio(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
int validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDetrazioniFamily(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaAltriDatiEsterni(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String verificaIncoerenzaCarichiFam(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
