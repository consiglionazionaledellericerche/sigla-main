package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_inquadramentoHome extends BulkHome {
public Rif_inquadramentoHome(java.sql.Connection conn) {
	super(Rif_inquadramentoBulk.class,conn);
}
public Rif_inquadramentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Rif_inquadramentoBulk.class,conn,persistentCache);
}
/**
  * Ritorna TRUE se esiste l'inquadramento con le seguenti caratteristiche:
  *		- progressivo inquadramento <pgInquadramento>
  *	  	- inquadramento associato al tipo rapporto <aRapporto>
  * 	- inquadramento associato al codice anagrafico del terzo <aTerzo>  
  *		- inquadramento valido in data <dataValiditaInquadramento>
  *
  * @param aRapporto					Tipo rapporto
  * @param aTerzo						Terzo 
  * @param tipoAnagrafico				Tipo anagrafico del terzo associato (null se non ci sono terzi)
  * @param dataValiditaInquadramento	Data di validita dell' inquadramento
  * @return L'SQL statement relativo
  *
**/

public boolean isInquadramentoValido(Long pgInquadramento, Tipo_rapportoBulk aRapporto, it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk aTerzo, java.sql.Timestamp dataValiditaInquadramento) throws java.sql.SQLException, PersistencyException
{
	SQLBuilder sql = selectInquadramenti(pgInquadramento, aRapporto, aTerzo, dataValiditaInquadramento);
	return sql.executeExistsQuery(getConnection());
}
/**
  * Ritorna l'SQL statement per la ricerca degli inquadramenti
  * con le seguenti clausole:
  *		- se valorizzato progressivo inquadramento <pgInquadramento>  
  * 	- inquadramento associato al tipo rapporto <aRapporto>
  * 	- inquadramento associato al codice anagrafico del terzo <aTerzo>  
  *		- Inquadramento valido in data <dataValiditaInquadramento>
  *
  * @param aRapporto					Tipo rapporto
  * @param aTerzo						Terzo 
  * @param tipoAnagrafico				Tipo anagrafico del terzo associato (null se non ci sono terzi)
  * @param dataValiditaInquadramento	Data di validita dell' inquadramento
  * @param dataValiditaRapporto			Data di validita del Rapporto
  * @return L'SQL statement relativo
  *
**/

public SQLBuilder selectInquadramenti(Long pgInquadramento, Tipo_rapportoBulk aRapporto, it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk aTerzo, java.sql.Timestamp dataValiditaInquadramento)
{
	SQLBuilder sql = createSQLBuilder();
	
	sql.addTableToHeader( "	INQUADRAMENTO");
	sql.addSQLJoin("rif_inquadramento.pg_rif_inquadramento","inquadramento.pg_rif_inquadramento" );
	sql.addSQLClause( "AND", "inquadramento.cd_tipo_rapporto", sql.EQUALS, aRapporto.getCd_tipo_rapporto());
	sql.addSQLClause( "AND", "inquadramento.cd_anag", sql.EQUALS, aTerzo.getCd_anag());
	sql.addSQLClause("AND","inquadramento.dt_ini_validita",sql.LESS_EQUALS, dataValiditaInquadramento);
	sql.addSQLClause("AND","inquadramento.dt_fin_validita",sql.GREATER_EQUALS, dataValiditaInquadramento);		
	sql.addSQLClause( "AND", "inquadramento.pg_rif_inquadramento", sql.EQUALS, pgInquadramento);		

	return sql;
}


public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException {
	try {((Rif_inquadramentoBulk)bulk).setPg_rif_inquadramento(	new Long((Long)findAndLockMax(bulk, "pg_rif_inquadramento", new Long(0))).longValue()+1);
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
	throw new PersistencyException(e);
	}
	   }
}
