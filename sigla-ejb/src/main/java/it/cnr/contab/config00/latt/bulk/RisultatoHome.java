package it.cnr.contab.config00.latt.bulk;

import java.sql.PreparedStatement;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RisultatoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 */
public RisultatoHome(java.sql.Connection conn) {
	super(RisultatoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param conn	
 * @param persistentCache	
 */
public RisultatoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(RisultatoBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	try {
		RisultatoBulk risultato = (RisultatoBulk)bulk;

			SQLBuilder sql = createSQLBuilder();

// La gestione user delle linee di attività è limitata a quelle proprie.
// Tali linee hanno una numerazione del tipo PXX..XX dove XX..XX è il progressivo
// Devono essere inoltre gestite le linee di attività di tipo SISTEMA SXX..XX dove XX..XX è il progressivo
	
			sql.setHeader("SELECT MAX(PG_RISULTATO)");
			sql.addClause("AND","cd_linea_attivita",sql.EQUALS,risultato.getLinea_attivita().getCd_linea_attivita());
			sql.addClause("AND","cd_centro_responsabilita",sql.EQUALS,risultato.getLinea_attivita().getCentro_responsabilita().getCd_centro_responsabilita());
			LoggableStatement stm = sql.prepareStatement(getConnection());
			try {
				java.sql.ResultSet rs = stm.executeQuery();
	            java.math.BigDecimal aMaxVal;
				if (rs.next()) {
	             aMaxVal = rs.getBigDecimal(1);
	             if (aMaxVal != null)
	             	risultato.setPg_risultato(new Long(aMaxVal.longValue()+1));
	             else
	             	risultato.setPg_risultato(new Long(1));
	             try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				try{stm.close();}catch( java.sql.SQLException e ){};
			}
			
	} catch(NumberFormatException e) {
		throw new it.cnr.jada.comp.ApplicationException("Non è possibile fornire una numerazione automatica perchè esisitono codici assegnati manualmente non numerici",e);
	}catch(java.sql.SQLException e) {
		throw new PersistencyException(e);
	}
}
}
