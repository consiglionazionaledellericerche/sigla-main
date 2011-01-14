package it.cnr.contab.incarichi00.ejb;

import javax.ejb.Remote;

@Remote
public interface IncarichiRepertorioComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk completaTerzo(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param1, it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk stornaIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk chiudiIncaricoPubblicato(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk salvaDefinitivo(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.math.BigDecimal calcolaUtilizzato(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaDefinitivo(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean hasVariazioneIntegrazioneIncaricoProvvisoria(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaCancellazioneAssociazioneUo(it.cnr.jada.UserContext param0, it.cnr.contab.incarichi00.bulk.Ass_incarico_uoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
