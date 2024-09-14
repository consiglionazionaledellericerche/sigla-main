/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.config00.comp;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;

import java.util.*;

/*
 * Gestione attività di CRUD sulle Associazioni tra tipi di linee di attività e CDR
 */
public class Tipologia_linea_attivitaComponent extends it.cnr.jada.comp.CRUDDetailComponent implements ITipologia_linea_attivitaMgr, Cloneable,Serializable {
	public  Tipologia_linea_attivitaComponent() {
	}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha annullato l'associazione tar cdr e una tipologia linea attività
  *    PostCondition:
  *		 Effettua un rollback al savepoint impostato in inizializzaCdrAssociati
 */
public void annullaModificaCdrAssociati(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk tipo_la) throws it.cnr.jada.comp.ComponentException {
	try {
		rollbackToSavepoint(userContext,"ASS_TIPO_LA_CDR");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesta l'associazione di una tipologia linea attività con tutti i cdr disponibili
  *    PostCondition:
  *		 Inserisce nella tabella ASS_TIPO_LA_CDR tante righe quanti sono i cdr mancanti nella associazione
  *		 con la tipologia linea attività specificata;
  *		 Per ogni cdr associato crea una linea attività secondo le specifiche di modificaCdrAssociati
 */
public void associaTuttiCdr(UserContext userContext,Tipo_linea_attivitaBulk tipo_la) throws ComponentException {
	try {
		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
		String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
		java.sql.Timestamp dacr = getHome(userContext,tipo_la).getServerTimestamp();

		LoggableStatement ps = new LoggableStatement(getConnection(userContext),"INSERT INTO "+schema+"ASS_TIPO_LA_CDR ( CD_CENTRO_RESPONSABILITA, CD_TIPO_LINEA_ATTIVITA, UTCR, DUVA, UTUV, DACR, PG_VER_REC ) SELECT CD_CENTRO_RESPONSABILITA, ?, ?, ?, ?, ?, 1 FROM "+schema+"V_CDR_VALIDO WHERE ESERCIZIO = ? AND NOT EXISTS ( SELECT 1 FROM "+schema+"ASS_TIPO_LA_CDR WHERE V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA = ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA AND ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA = ? ) ",
				true,this.getClass());
		ps.setString(1,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.setString(2,user);  // UTCR
		ps.setTimestamp(3,dacr);   // DUVA
		ps.setString(4,user);   // UTUV
		ps.setTimestamp(5,dacr);    // DACR
		ps.setInt(6,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());   // ESERCIZIO
		ps.setString(7,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
		
		ps = new LoggableStatement(getConnection(userContext),"INSERT INTO "+schema+"LINEA_ATTIVITA ( CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, CD_TIPO_LINEA_ATTIVITA, DENOMINAZIONE, CD_GRUPPO_LINEA_ATTIVITA, CD_FUNZIONE, CD_NATURA, DS_LINEA_ATTIVITA, DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_CDR_COLLEGATO, CD_LA_COLLEGATO, ESERCIZIO_FINE, ESERCIZIO_INIZIO,TI_GESTIONE, FL_LIMITE_ASS_OBBLIG ) SELECT CD_CENTRO_RESPONSABILITA, ?, ?, ?, NULL, ?, ?, NULL, ?, ?, ?, ?, 1, NULL, NULL, 2100, ESERCIZIO, ? ,FL_LIMITE_ASS_OBBLIG  FROM "+schema+"V_CDR_VALIDO WHERE ESERCIZIO = ? AND NOT EXISTS ( SELECT 1 FROM "+schema+"LINEA_ATTIVITA WHERE V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA = LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA AND LINEA_ATTIVITA.CD_TIPO_LINEA_ATTIVITA = ? ) ",
				true,this.getClass());
		ps.setString(1,"C"+tipo_la.getCd_tipo_linea_attivita());   // CD_LINEA_ATTIVITA
		ps.setString(2,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.setString(3,tipo_la.getDs_tipo_linea_attivita());   // DENOMINAZIONE
		if (tipo_la.getFunzione() == null)
			ps.setNull(4,java.sql.Types.VARCHAR);
		else
			ps.setString(4,tipo_la.getFunzione().getCd_funzione());   // FUNZIONE
		ps.setString(5,tipo_la.getNatura().getCd_natura());   // NATURA
		ps.setTimestamp(6,dacr);    // DACR
		ps.setString(7,user);  // UTCR
		ps.setTimestamp(8,dacr);   // DUVA
		ps.setString(9,user);   // UTUV
		ps.setString(10,tipo_la.getTi_gestione());   // TI_GESTIONE
		ps.setInt(11,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());   // ESERCIZIO
		ps.setString(12,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto l'elenco dei cdr associabli ad una tipologia linea attivita
  *    PostCondition:
  *		 Effettua una query sulla tabella dei cdr che NON compaiono nella tabella ASS_TIPO_LA_CDR per
  *		 la tipologia linea attività specificata per l'esercizio di scrivania
 */
public RemoteIterator cercaCdrAssociabili(UserContext userContext,Tipo_linea_attivitaBulk tipo_la) throws ComponentException {
	SQLBuilder sql = getHome(userContext,CdrBulk.class,"V_CDR_VALIDO").createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND","NOT EXISTS ( SELECT 1 FROM ASS_TIPO_LA_CDR WHERE ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA = V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA AND ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA = ?)");
	sql.addParameter(tipo_la.getCd_tipo_linea_attivita(),java.sql.Types.VARCHAR,0);
	return iterator(userContext,sql,CdrBulk.class,null);
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto la creazione di una nuova tipologia linea attivita e delle associazioni della stessa con uno o più CDR
  *    PostCondition:
  *		 Imposta ti_tipo_la = 'C'
 */
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		Tipo_linea_attivitaBulk tipo_la = (Tipo_linea_attivitaBulk)super.creaConBulk(userContext,bulk);
		if (tipo_la.getTi_tipo_la() == null)
			tipo_la.setTi_tipo_la(tipo_la.COMUNE);
		return tipo_la;
	} catch (Throwable e) {
		throw handleException(bulk,e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *		 L'utente ha creato una associazione tra una tipologia linea attività e un cdr e viene chiesta la creazione della corrispondente linea di attivita comune.
  *    PostCondition:
  *		 Viene creata una nuova linea di attivita con i seguenti attributi:
  *			linea_attivita.Cd_tipo_linea_attivita = tipo_la.Cd_tipo_linea_attivita
  *			linea_attivita.cd_centro_responsabilita = tipo_la.cd_centro_responsabilita
  *			linea_attivita.Cd_linea_attivita = 'C'+tipo_la.Cd_tipo_linea_attivita
  *			linea_attivita.Esercizio_inizio = esercizio di scrivania
  *			linea_attivita.Esercizio_fine = 2100
  *			linea_attivita.Denominazione = tipo_la.Ds_tipo_linea_attivita()
  *			linea_attivita.Cd_funzione = tipo_la.Cd_funzione
  *			linea_attivita.Cd_natura = tipo_la.Cd_natura
 */
protected void creaLineaAttivitaComune(UserContext userContext,Ass_tipo_la_cdrBulk ass_tipo_la_cdr,Tipo_linea_attivitaBulk tipo_la) throws it.cnr.jada.comp.ComponentException {
	try {
		if (ass_tipo_la_cdr.isToBeCreated()) {
			ass_tipo_la_cdr.setCd_tipo_linea_attivita(tipo_la.getCd_tipo_linea_attivita());
			makeBulkPersistent(userContext,ass_tipo_la_cdr);
	   		WorkpackageBulk linea_attivita = new WorkpackageBulk();
	   		linea_attivita.setTipo_linea_attivita(tipo_la);
	   		linea_attivita.setCentro_responsabilita(ass_tipo_la_cdr.getCentro_responsabilita());
	   		linea_attivita.setCd_linea_attivita("C"+tipo_la.getCd_tipo_linea_attivita());
	   		linea_attivita.setEsercizio_inizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	   		linea_attivita.setEsercizio_fine(new Integer(2100));
	   		linea_attivita.setDenominazione(tipo_la.getDs_tipo_linea_attivita());
	   		linea_attivita.setFunzione(tipo_la.getFunzione());
	   		linea_attivita.setNatura(tipo_la.getNatura());
	   		linea_attivita.setUser(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
	   		insertBulk(userContext,linea_attivita);
		}
	} catch (Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'eleminazione di alcuni cdr associati ad una tipologia linea attivita
  *    PostCondition:
  *		 Vengono eliminate tutte le linee di attivita e le associazioni tipo linea attivita - cdr con la tipologia e i cdr specificati
 */
public void eliminaCdrAssociatiConBulk(UserContext userContext,OggettoBulk[] cdrs,Tipo_linea_attivitaBulk tipo_la) throws ComponentException {
	try {
		for (int i = 0;i < cdrs.length;i++) {
			CdrBulk cdr = (CdrBulk)cdrs[i];
			deleteBulk(userContext,new Ass_tipo_la_cdrBulk(cdr.getCd_centro_responsabilita(),tipo_la.getCd_tipo_linea_attivita()));
		}
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'eleminazione di tutti i cdr associati ad una tipologia linea attivita
  *    PostCondition:
  *		 Vengono eliminate tutte le linee di attivita e le associazioni tipo linea attivita - cdr con la tipologia specificata
 */
public void eliminaCdrAssociatiConBulk(UserContext userContext,Tipo_linea_attivitaBulk tipo_la) throws ComponentException {
	try {
		String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();

		LoggableStatement ps = new LoggableStatement(getConnection(userContext),"DELETE FROM "+schema+"LINEA_ATTIVITA WHERE CD_TIPO_LINEA_ATTIVITA = ?",
				true,this.getClass());
		ps.setString(1,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
		
		ps = new LoggableStatement(getConnection(userContext),"DELETE FROM "+schema+"ASS_TIPO_LA_CDR WHERE CD_TIPO_LINEA_ATTIVITA = ?",
				true,this.getClass());
		ps.setString(1,tipo_la.getCd_tipo_linea_attivita());   // CD_TIPO_LINEA_ATTIVITA
		ps.execute();
		try{ps.close();}catch( java.sql.SQLException e ){};
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'inserimento di una tipologia linea attivita
  *    PostCondition:
  *		 Il tipo linea attività viene inizializzato con CD_CDR_CREATORE uguale al
  *		 codice del CDR dell'utente
 */
public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try {
		Tipo_linea_attivitaBulk tipo_la = (Tipo_linea_attivitaBulk)super.inizializzaBulkPerInserimento(userContext,bulk);
		UtenteBulk utente = (UtenteBulk)getHome(userContext,UtenteBulk.class).findByPrimaryKey(new UtenteBulk(userContext.getUser()));
		tipo_la.setCd_cdr_creatore(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext));
		return tipo_la;
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto la modifica di una tipologia linea attivita
  *    PostCondition:
  *		 Viene posto un lock sulla tipologia linea attività specificata
 */
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws ComponentException {
	try {
		lockBulk(userContext,bulk);
		return super.inizializzaBulkPerModifica(userContext,bulk);
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw handleException(e);
	} catch(it.cnr.jada.bulk.OutdatedResourceException e) {
		throw handleException(e);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'associazione di nuovi cdr ad una tipologia linea attività
  *    PostCondition:
  *		 Inizializza un SAVEPOINT sulla transazione utente per poter eventualmente annullare
  *		 le modifiche alle associazioni
 */
public void inizializzaCdrAssociatiPerModifica(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk tipo_la) throws it.cnr.jada.comp.ComponentException {
	try {
		setSavepoint(userContext,"ASS_TIPO_LA_CDR");
	} catch(java.sql.SQLException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesta la modifica delle associazioni di una tipologia linea attività con uno
  * 	 o più cdr.
  *    PostCondition:
  *		 Per ogni cdr specificato viene eliminata o creata una associazione con la tipologia
  * 	 linea attività specificata. Per ogni nuova associazione creata viene inserita una linea
  * 	 di attività con le seguenti informazioni:
  *			CD_LINEA_ATTIVITA = "C"+tipo_la.CD_TIPO_LINEA_ATTIVITA
  *			CD_CENTRO_RESPONSABILITA = CD_CDR (dal cdr corrente)
  *			CD_TIPO_LINEA_ATTIVITA = tipo_la.CD_TIPO_LINEA_ATTIVITA
  *			DENOMINAZIONE = tipo_la.DS_TIPO_LINEA_ATTIVITA
  *			CD_GRUPPO_LINEA_ATTIVITA = NULL
  *			CD_FUNZIONE = tipo_la.CD_FUNZIONE
  *			CD_NATURA = tipo_la.CD_NATURA
  *			DS_LINEA_ATTIVITA = NULL
  *			CD_CDR_COLLEGATO = NULL
  *			CD_LA_COLLEGATO = NULL
  *			ESERCIZIO_INIZIO = esercizio di scrivania
  *			ESERCIZIO_FINE = 2100
  *			CD_INSIEME_LA = NULL
  *			TI_GESTIONE = tipo_la.TI_GESTIONE
 */

public void modificaCdrAssociati(UserContext userContext,Tipo_linea_attivitaBulk tipo_la,OggettoBulk[] cdrs,BitSet old_ass,BitSet ass) throws ComponentException {
	try {
		//lockBulk(userContext,tipo_la);
		for (int i = 0;i < cdrs.length;i++) {
			CdrBulk cdr = (CdrBulk)cdrs[i];
			if (old_ass.get(i) != ass.get(i)) {
				Ass_tipo_la_cdrBulk ass_tipo_la_cdr = new Ass_tipo_la_cdrBulk(cdr.getCd_centro_responsabilita(),tipo_la.getCd_tipo_linea_attivita());
				if (ass.get(i)) {
					ass_tipo_la_cdr.setUser(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
					insertBulk(userContext,ass_tipo_la_cdr);
			   		WorkpackageBulk linea_attivita = new WorkpackageBulk();
			   		linea_attivita.setTipo_linea_attivita(tipo_la);
			   		linea_attivita.setCentro_responsabilita(cdr);
			   		linea_attivita.setCd_linea_attivita("C"+tipo_la.getCd_tipo_linea_attivita());
			   		linea_attivita.setEsercizio_inizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			   		linea_attivita.setEsercizio_fine(new Integer(2100));
			   		linea_attivita.setDenominazione(tipo_la.getDs_tipo_linea_attivita());
			   		linea_attivita.setFunzione(tipo_la.getFunzione());
			   		linea_attivita.setNatura(tipo_la.getNatura());
			   		linea_attivita.setTi_gestione(tipo_la.getTi_gestione());
			   		linea_attivita.setUser(it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
			   		insertBulk(userContext,linea_attivita);
				} else {
					deleteBulk(userContext,ass_tipo_la_cdr);
				}
			}
		}
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto una ricerca di tipologie di linee attività
  *    PostCondition:
  *		 Effettua una query sulla tabella delle tipologie con le clausole richieste filtrando solo le tipologie comuni
 */
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	sql.addTableToHeader("UTENTE","UTCR");
	sql.addTableToHeader("UTENTE");
	sql.addSQLJoin("TIPO_LINEA_ATTIVITA.UTCR","UTCR.CD_UTENTE");
	sql.addSQLJoin("UTCR.CD_CDR","UTENTE.CD_CDR");
	sql.addSQLClause("AND","UTENTE.CD_UTENTE",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext));
	sql.addClause("AND","ti_tipo_la",sql.EQUALS,Tipo_linea_attivitaBulk.COMUNE);
	return sql;
}
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto l'elenco dei cdr associati ad una tipologia linea attivita
  *    PostCondition:
  *		 Effettua una query sulla tabella dei cdr che compaiono nella tabella ASS_TIPO_LA_CDR per
  *		 la tipologia linea attività specificata per l'esercizio di scrivania
 */
public SQLBuilder selectCdrAssociatiByClause(UserContext userContext, Tipo_linea_attivitaBulk tipo_la, Class bulkClass, CompoundFindClause clauses)  throws ComponentException {
	if (tipo_la.getCd_tipo_linea_attivita() == null) return null;
	SQLBuilder sql = getHome(userContext,CdrBulk.class,"V_CDR_VALIDO").createSQLBuilder();
	sql.addTableToHeader("ASS_TIPO_LA_CDR");
	sql.addSQLJoin("ASS_TIPO_LA_CDR.CD_CENTRO_RESPONSABILITA","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
	sql.addClause(clauses);
	sql.addSQLClause("AND","ASS_TIPO_LA_CDR.CD_TIPO_LINEA_ATTIVITA",sql.EQUALS,tipo_la.getCd_tipo_linea_attivita());
	sql.addSQLClause("AND","V_CDR_VALIDO.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	return sql;
}
/**
 * @author mspasiano
 * Inizializza la Funzione solo con la 01  
 */
public void initializeKeysAndOptionsInto(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	super.initializeKeysAndOptionsInto(aUC, bulk);
	if(bulk instanceof Tipo_linea_attivitaBulk) {
		try {
			FunzioneHome home = (FunzioneHome)getHome(aUC, FunzioneBulk.class);
			java.util.Collection coll = home.find(home.findByPrimaryKey(new FunzioneBulk("01")));
			((Tipo_linea_attivitaBulk)bulk).setFunzioni(coll);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}
}
