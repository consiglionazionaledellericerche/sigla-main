package it.cnr.contab.missioni00.ejb;

import it.cnr.contab.docamm00.ejb.IDocumentoAmministrativoSpesaComponentSession;

import javax.ejb.Remote;

@Remote
public interface AnticipoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, IDocumentoAmministrativoSpesaComponentSession {
it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk creaRimborsoCompleto(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk gestisciCambioDataRegistrazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.sql.SQLException,java.rmi.RemoteException;
boolean isAnticipoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
