package it.cnr.contab.cori00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
@Remote
public interface Liquid_coriComponentSession extends it.cnr.jada.ejb.CRUDDetailComponentSession {
it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk calcolaLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk eseguiLiquidazione(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk eseguiLiquidazioneMassaCori(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaBatch(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquidazione_massa_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRibalta(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk cambiaStato(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean esisteRiga(it.cnr.jada.UserContext param0,it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_centroBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List EstraiLista(UserContext userContext,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getContoSpecialeEnteF24(UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void Popola_f24(UserContext userContext,Liquid_coriBulk liquidazione) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List EstraiListaTot(UserContext userContext,it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk param1)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void Popola_f24Tot(UserContext userContext,Liquid_coriBulk liquidazione) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getSedeInpsF24(UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Configurazione_cnrBulk getSedeInailF24(UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getSedeInpdapF24(UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
