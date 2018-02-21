package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AutofatturaHome extends BulkHome {
public AutofatturaHome(java.sql.Connection conn) {
	super(AutofatturaBulk.class,conn);
}
public AutofatturaHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(AutofatturaBulk.class,conn,persistentCache);
}
public AutofatturaBulk findFor(Fattura_passivaBulk fatturaPassiva) 
	throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND", "CD_CDS_FT_PASSIVA", sql.EQUALS, fatturaPassiva.getCd_cds());
	sql.addSQLClause("AND", "CD_UO_FT_PASSIVA", sql.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
	sql.addSQLClause("AND", "PG_FATTURA_PASSIVA", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, fatturaPassiva.getEsercizio());

	java.util.List result = fetchAll(sql);
	if (result == null || result.isEmpty()) return null;
	if (result.size() != 1)
		throw new PersistencyException("Trovate più autofatture per fattura passiva " + fatturaPassiva.getPg_fattura_passiva().longValue());
	AutofatturaBulk autof = (AutofatturaBulk)result.get(0);
	autof.setFattura_passiva(fatturaPassiva);
	return autof;
}
}
