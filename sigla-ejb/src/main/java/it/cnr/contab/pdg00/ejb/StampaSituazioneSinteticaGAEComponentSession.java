package it.cnr.contab.pdg00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
@Remote
public interface StampaSituazioneSinteticaGAEComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
void inserisciRecord(UserContext userContext,java.math.BigDecimal pg_stampa,java.util.List filtro)throws ComponentException,java.rmi.RemoteException;
java.math.BigDecimal getPgStampa(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
RemoteIterator selezionaGae (UserContext userContext,  CompoundFindClause clause) throws ComponentException, PersistencyException, RemoteException;
}
