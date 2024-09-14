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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.time.format.DateTimeFormatter;

public class V_terzo_per_compensoHome extends BulkHome {
public V_terzo_per_compensoHome(Class clazz, java.sql.Connection conn) {
	super(clazz,conn);
}
public V_terzo_per_compensoHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
	super(clazz,conn,persistentCache);
}
public V_terzo_per_compensoHome(java.sql.Connection conn) {
	super(V_terzo_per_compensoBulk.class,conn);
}
public V_terzo_per_compensoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_terzo_per_compensoBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (26/02/2002 11.22.00)
 * @return java.util.Collection
 * @param terzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public Tipo_rapportoBulk findTipoRapporto(V_terzo_per_compensoBulk vTerzo) throws PersistencyException{

	Tipo_rapportoHome rappHome = (Tipo_rapportoHome)getHomeCache().getHome(Tipo_rapportoBulk.class);
	return rappHome.findTipoRapporto(vTerzo.getCd_tipo_rapporto());

}
/**
  * Carico il V_terzo_per_compensoBulk con:
  *		- codice <codiceTerzo>
  * 	- tipo anagrafico  <tipoDipendenteAltro>
  *		- valido in data <dtValiditaTerzo>
  *
  * @param tipoDipendenteAltro: tipo anagrafico del terzo
  * @param codiceTerzo: codice del terzo cercato
  * @param dtValiditaTerzo: data di validita del terzo
  * @return il V_terzo_per_compensoBulk valido
  *
**/
public V_terzo_per_compensoBulk loadVTerzo(it.cnr.jada.UserContext userContext,String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo) throws PersistencyException,ApplicationException{

	return loadVTerzo(userContext,tipoDipendenteAltro, codiceTerzo,dtValiditaTerzo, null);
}
/**
  * Costruisce l'struzione SQL per la ricerca di tutti i vTerzo
  * con codice <codiceTerzo>, aventi come tipo anagrafico <tipoDipendenteAltro>
  * validi in data <dtValiditaTerzo>
  *
  * @param tipoDipendenteAltro: tipo anagrafico del terzo
  * @param codiceTerzo: codice del terzo cercato
  * @param dtValiditaTerzo: data di validita del terzo
  * @return istruzione SQL
  *
**/
public SQLBuilder selectVTerzo(String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo) throws PersistencyException{

	return selectVTerzo(tipoDipendenteAltro, codiceTerzo, dtValiditaTerzo, null, null);
}

/**
  * Inserisce le clausole statiche di validità
  * Prendo tutti i terzi validi alla data <data>
  *
  * @param sql SQL statement a cui vengono aggiunte le clausole di validita
  * @param dtValiditaTerzo Data di validita del terzo
  * @param dtValiditaRapp Data di validita del tipo rapporto
  *
**/
private void addClauseValidita(SQLBuilder sql, java.sql.Timestamp dtValiditaTerzo, java.sql.Timestamp dtValiditaRapp) throws PersistencyException{

	// Validita del terzo
	CompoundFindClause clause = CompoundFindClause.or(
		new SimpleFindClause("dt_fine_validita_terzo",sql.GREATER_EQUALS,dtValiditaTerzo),
		new SimpleFindClause("dt_fine_validita_terzo",sql.ISNULL, null));
	sql.addClause(clause);

	// Validita tipo rapporto
	sql.addSQLClause("AND","DT_INI_VALIDITA", sql.LESS_EQUALS, dtValiditaRapp);
	sql.addSQLClause("AND","DT_FIN_VALIDITA", sql.GREATER_EQUALS, dtValiditaRapp);
}

/**
  * Carico il V_terzo_per_compensoBulk con:
  *		- codice <codiceTerzo>
  * 	- tipo anagrafico  <tipoDipendenteAltro>
  *		- valido in data <dtValiditaTerzo>
  *		- con tipi rapporto validi in data <dtValiditaRapp>
  *
  * @param tipoDipendenteAltro: tipo anagrafico del terzo
  * @param codiceTerzo: codice del terzo cercato
  * @param dtValiditaTerzo: data di validita del terzo
  * @param dtValiditaRapp: data di validita del tipo di rapporto
  * @return il V_terzo_per_compensoBulk valido
  *
**/
public V_terzo_per_compensoBulk loadVTerzo(it.cnr.jada.UserContext userContext,String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo, java.sql.Timestamp dtValiditaRapp) throws PersistencyException,ApplicationException{

	SQLBuilder sql = selectVTerzo(tipoDipendenteAltro, codiceTerzo, dtValiditaTerzo, dtValiditaRapp);

	V_terzo_per_compensoBulk vTerzo = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		vTerzo = (V_terzo_per_compensoBulk)fetch(broker);
	broker.close();
	getHomeCache().fetchAll(userContext);

	if (vTerzo==null)
		throw new ApplicationMessageFormatException(
				"Impossibile recuperare il terzo con codice: {0} per il periodo di validità del terzo {1} e del rapporto {2}.",
				String.valueOf(codiceTerzo),
				dtValiditaTerzo.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
				dtValiditaRapp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
		);

	return vTerzo;
	
}

/**
  * Costruisce l'struzione SQL per la ricerca di tutti i vTerzo
  * con codice <codiceTerzo>, aventi come tipo anagrafico <tipoDipendenteAltro>
  * validi in data <dtValiditaTerzo> e con tipi rapporto validi in data <dtValiditaRapp>
  *
  * @param tipoDipendenteAltro: tipo anagrafico del terzo
  * @param codiceTerzo: codice del terzo cercato
  * @param dtValiditaTerzo: data di validita del terzo
  * @param dtValiditaRapp: data di validita del rapporto
  * @return istruzione SQL
  *
**/
public SQLBuilder selectVTerzo(String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo, java.sql.Timestamp dtValiditaRapp) throws PersistencyException{

	return selectVTerzo(tipoDipendenteAltro, codiceTerzo, dtValiditaTerzo, dtValiditaRapp, null);
}

/**
  * Costruisce l'struzione SQL per la ricerca di tutti i vTerzo
  * con codice <codiceTerzo>, aventi come tipo anagrafico <tipoDipendenteAltro>
  * validi in data <dtValiditaTerzo> e con tipi rapporto validi in data <dtValiditaRapp>
  *	Aggiunge eventuali clausole <clauses> selezionate dall'utente
  *
  * @param tipoDipendenteAltro: tipo anagrafico del terzo
  * @param codiceTerzo: codice del terzo cercato
  * @param dtValiditaTerzo: data di validita del terzo
  * @param dtValiditaRapp: data di validita del rapporto
  * @param clauses L'albero logico delle clausole da applicare alla ricerca
  * @return istruzione SQL
  *
**/
public SQLBuilder selectVTerzo(String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo, java.sql.Timestamp dtValiditaRapp, CompoundFindClause clauses) throws PersistencyException{

	return selectVTerzo(tipoDipendenteAltro, codiceTerzo, dtValiditaTerzo, dtValiditaRapp, clauses, true);
}

private SQLBuilder selectVTerzo(String tipoDipendenteAltro, Integer codiceTerzo, java.sql.Timestamp dtValiditaTerzo, java.sql.Timestamp dtValiditaRapp, CompoundFindClause clauses, Boolean filtroValidita) throws PersistencyException{

	SQLBuilder sql = createSQLBuilder();
	sql.setDistinctClause(true);
	sql.addSQLClause("AND","TI_DIPENDENTE_ALTRO",sql.EQUALS,tipoDipendenteAltro);
	sql.addSQLClause("AND","CD_TERZO",sql.EQUALS,codiceTerzo);
	if (filtroValidita){
		addClauseValidita(sql, dtValiditaTerzo, dtValiditaRapp);
	}
	sql.addClause(clauses);

	return sql;
}

public SQLBuilder selectVTerzo(Integer codiceTerzo, CompoundFindClause clauses) throws PersistencyException{

	return selectVTerzo(null, codiceTerzo, null, null, clauses, false);
}
}
