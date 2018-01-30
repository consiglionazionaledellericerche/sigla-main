package it.cnr.contab.utente00.ejb;

import it.cnr.contab.utenze00.bulk.AbilitatoFirma;
import it.cnr.contab.utenze00.bulk.UtenteFirmaDettaglioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import javax.ejb.Remote;

@Remote
public interface UtenteComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.utenze00.bulk.UtenteBulk cercaAccessi(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2, CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.utenze00.bulk.UtenteBulk cercaRuoli(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection cercaUOAccessiPropri(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection cercaUORuoliPropri(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.utenze00.bulk.UtenteBulk resetPassword(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.UtenteBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Boolean isCdrConfiguratoreAll(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.utenze00.bulk.SelezionaCdsBulk findCds(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.SelezionaCdsBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.utenze00.bulk.SelezionaCdsBulk findUo(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.SelezionaCdsBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk findCdrEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaSelezionaCds(it.cnr.jada.UserContext param0,it.cnr.contab.utenze00.bulk.SelezionaCdsBulk param1, Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
UtenteFirmaDettaglioBulk isUtenteAbilitatoFirma(UserContext param0, AbilitatoFirma codice) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isSupervisore(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
