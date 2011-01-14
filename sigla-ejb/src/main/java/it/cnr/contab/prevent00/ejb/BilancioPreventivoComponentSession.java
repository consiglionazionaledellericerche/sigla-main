package it.cnr.contab.prevent00.ejb;

import javax.ejb.Remote;

@Remote
public interface BilancioPreventivoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk approvaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk caricaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void creaStanziamentiInizialiCNR(it.cnr.jada.UserContext param0,short param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void creaStanziamentiInizialiCdS(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCNR(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCdS(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
