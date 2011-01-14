package it.cnr.contab.anagraf00.ejb;

import java.util.List;
import java.sql.Timestamp;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.jada.UserContext;

import javax.ejb.Remote;

@Remote
public interface TerzoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.util.RemoteIterator cercaBanchePerTerzoCessionario(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaModalita_pagamento_disponibiliByClause(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk cercaTerzoPerUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk inizializzaTerzoPerUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk setComune_sede(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaTerziSIP(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaTerziSIP_rendicontazione(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4, Timestamp timestamp, Timestamp timestamp2,String Dip) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findNazioniIban(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.BancaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
