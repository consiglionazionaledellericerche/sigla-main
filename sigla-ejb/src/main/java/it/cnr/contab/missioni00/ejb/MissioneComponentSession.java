package it.cnr.contab.missioni00.ejb;
import java.sql.Timestamp;
import java.util.Date;

import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.ejb.Remote;

@Remote
public interface MissioneComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaCompensoPhisically(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaDiariaPhisically(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaTappePhisically(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.tabrif.bulk.CambioBulk findCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk param1,java.sql.Timestamp param2) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Collection findInquadramenti(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk findInquadramentiETipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Collection findTipi_rapporto(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipi_trattamento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaDiaria(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk inizializzaDivisaCambioPerRimborsoKm(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,javax.ejb.EJBException,java.rmi.RemoteException,it.cnr.jada.bulk.ValidationException;
boolean isMissioneAnnullata(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk loadCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk setDivisaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk setNazioneDivisaCambioItalia(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void updateAnticipo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk updateCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
void validaEsercizioDataRegistrazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.MissioneBulk validaMassimaliSpesa(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException,it.cnr.jada.bulk.ValidationException;
void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
int validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isDiariaEditable(it.cnr.jada.UserContext param0, it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isTerzoCervellone(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaMissioniSIP(UserContext userContext,String query,String dominio,String uo,String terzo,String tipoRicerca,String string, String string2, String string3, Timestamp data_inizio,Timestamp data_fine)throws ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
