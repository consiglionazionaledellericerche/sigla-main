package it.cnr.contab.incarichi00.ejb;

import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Remote interface for Enterprise Bean: CNRINCARICHI00_EJB_IncarichiProceduraComponentSession
 */
@Remote
public interface IncarichiProceduraComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.bulk.OggettoBulk pubblicaSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaPubblicazioneSulSito(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk stornaProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk chiudiProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal calcolaUtilizzato(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_procedura_annoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk concludiProceduraIncaricoPubblicata(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk concludiIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk caricaSedeUnitaOrganizzativa(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException; 
it.cnr.contab.incarichi00.tabrif.bulk.Incarichi_parametriBulk getIncarichiParametri(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void salvaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaDefinitivoCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
List getIncarichiForMigrateFromDBToCMIS(it.cnr.jada.UserContext param0, Integer param1, Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void migrateAllegatiFromDBToCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
List getIncarichiForMergeWithCMIS(it.cnr.jada.UserContext param0, Integer param1, Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List<String> mergeAllegatiWithCMIS(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaAllegati(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_proceduraBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
