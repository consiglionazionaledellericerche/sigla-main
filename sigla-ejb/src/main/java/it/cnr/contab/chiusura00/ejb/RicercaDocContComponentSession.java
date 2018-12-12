package it.cnr.contab.chiusura00.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;
@Remote
public interface RicercaDocContComponentSession extends it.cnr.jada.ejb.RicercaComponentSession {
void callAnnullamentoDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportoNextEsDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportoNextEsDocContVoce(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportoPrevEsDocCont(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void clearSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void clearSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void clearSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk initializeSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicerca(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void selectAllPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void selectAllPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void selectAllPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void selectAllPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSelectionPerAnnullamento(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSelectionPerRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSelectionPerRiportaAvantiEvoluto(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSelectionPerRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.chiusura00.bulk.V_obb_acc_xxxBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext param0, String param1, Integer esercizio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.CdsBulk param1, Integer esercizio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRibaltato(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1, Integer esercizio) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isSfondataDispCdS(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRicosResiduiChiusa(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRibaltaDispImproprie(UserContext userContext) throws ComponentException,java.rmi.RemoteException;
OggettoBulk updateParametriCds (UserContext context) throws EJBException, ComponentException, PersistencyException, RemoteException;
boolean getCdsRibaltato(UserContext context)throws ComponentException, RemoteException;
boolean isRiaccertamentoChiuso(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isRiobbligazioneChiusa(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGaeCollegateProgetti(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isProgettiCollegatiGaeApprovati(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaResiduiForRiaccertamento(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaGaeSenzaProgettiForRibaltamento(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaProgettiCollegatiGaeNonApprovatiForRibaltamento(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
