package it.cnr.contab.anagraf00.comp;

import java.util.GregorianCalendar;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.anagraf00.tabter.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Insert the type's description here.
 * Creation date: (08/08/2002 16:31:47)
 * @author: CNRADM
 */
public class UtilitaAnagraficaComponent extends it.cnr.jada.comp.CRUDComponent {
/**
 * UtilitaAnagraficaComponent constructor comment.
 */
public UtilitaAnagraficaComponent() {
	super();
}
public boolean isTerzoSpeciale(UserContext userContext,TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException {
	try {
		BulkHome home = getHome(userContext,it.cnr.contab.config00.bulk.Configurazione_cnrBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CHIAVE_PRIMARIA",sql.EQUALS,"COSTANTI");
		sql.addSQLClause("AND","CD_CHIAVE_SECONDARIA",sql.EQUALS,"CODICE_ANAG_ENTE");
		sql.addSQLClause("AND","IM01",sql.EQUALS,terzo.getCd_terzo());
		if (sql.executeExistsQuery(getConnection(userContext)))
			return true;

		sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CHIAVE_PRIMARIA",SQLBuilder.EQUALS,"TERZO_SPECIALE" );
		sql.addSQLClause("AND","IM01",sql.EQUALS,terzo.getCd_terzo());
		if (sql.executeExistsQuery(getConnection(userContext)))
			return true;

		return false;
	} catch(java.sql.SQLException e) {
		throw handleSQLException(e);
	}
}
}
