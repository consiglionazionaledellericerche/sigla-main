package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ConguaglioHome extends BulkHome {
public ConguaglioHome(java.sql.Connection conn) {
	super(ConguaglioBulk.class,conn);
}
public ConguaglioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(ConguaglioBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2002 12.44.50)
 * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 * @param compenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public ConguaglioBulk findConguaglio(CompensoBulk compenso) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND", "CD_CDS_COMPENSO",sql.EQUALS,compenso.getCd_cds());
	sql.addSQLClause("AND", "CD_UO_COMPENSO",sql.EQUALS,compenso.getCd_unita_organizzativa());
	sql.addSQLClause("AND", "ESERCIZIO_COMPENSO",sql.EQUALS,compenso.getEsercizio());
	sql.addSQLClause("AND", "PG_COMPENSO",sql.EQUALS,compenso.getPg_compenso());

	ConguaglioBulk conguaglio = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		conguaglio = (ConguaglioBulk)fetch(broker);
	broker.close();

	return conguaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (12/07/2002 12.44.50)
 * @return it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk
 * @param compenso it.cnr.contab.compensi00.docs.bulk.CompensoBulk
 */
public ConguaglioBulk findConguaglioAssociatoACompenso(CompensoBulk compenso) throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("ASS_COMPENSO_CONGUAGLIO");
	sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.CD_CDS_CONGUAGLIO","CONGUALIO.CD_CDS");
	sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.CD_UO_CONGUAGLIO","CONGUALIO.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.ESERCIZIO_CONGUAGLIO","CONGUALIO.ESERCIZIO");
	sql.addSQLJoin("ASS_COMPENSO_CONGUAGLIO.PG_CONGUAGLIO","CONGUALIO.PG_CONGUAGLIO");
	
	sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.CD_CDS_COMPENSO",sql.EQUALS, compenso.getCd_cds());
	sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.CD_UO_COMPENSO",sql.EQUALS, compenso.getCd_unita_organizzativa());
	sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.ESERCIZIO_COMPENSO",sql.EQUALS, compenso.getEsercizio());
	sql.addSQLClause("AND","ASS_COMPENSO_CONGUAGLIO.PG_COMPENSO",sql.EQUALS, compenso.getPg_compenso());

	ConguaglioBulk conguaglio = null;
	Broker broker = createBroker(sql);
	if (broker.next())
		conguaglio = (ConguaglioBulk)fetch(broker);
	broker.close();

	return conguaglio;
}
}
