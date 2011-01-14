package it.cnr.contab.pdg00.ejb;

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import javax.ejb.Remote;

@Remote
public interface PdGVariazioniComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk approva(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk respingi(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaSommeCdR(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaBilancioPreventivoCdsApprovato(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk cercaCdrPrimoLivello(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaAssociazioneCDRPerCancellazione(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaConsultazioneExcel(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1, java.lang.String param2, java.io.File param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String controllaTotPropostoEntrataSpesa(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaForPrintRiepilogo(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isCdsAbilitatoAdApprovare(it.cnr.jada.UserContext param0,String param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getDesTipoVariazione(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk statoPrecedente(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk apponiVistoDipartimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaVariazioniForApposizioneVisto(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
CompoundFindClause aggiornaClausole(UserContext context ,Pdg_variazioneBulk pdg,String tipo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaVariazioniForDocumentale(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, String param3, Boolean param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaVariazioneDocumentale(it.cnr.jada.UserContext userContext,	Pdg_variazioneBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
