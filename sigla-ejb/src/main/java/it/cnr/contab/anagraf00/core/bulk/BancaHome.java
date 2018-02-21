package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class BancaHome extends BulkHome {
	public BancaHome(java.sql.Connection conn) {
		super(BancaBulk.class,conn);
	}
	public BancaHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(BancaBulk.class,conn,persistentCache);
	}
	/**
	 * Imposta il pg_banca di un oggetto <code>BancaBulk</code>.
	 *
	 * @param bank <code>BancaBulk</code>
	 *
	 * @exception PersistencyException
	 */

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bank) throws PersistencyException {
		try {
			((BancaBulk)bank).setPg_banca(
				new Long(
					((Long)findAndLockMax( bank, "pg_banca", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
/**
 * Imposta il pg_banca di un oggetto <code>BancaBulk</code>.
 *
 * @param bank <code>BancaBulk</code>
 *
 * @exception PersistencyException
 */

public SQLBuilder selectBancaFor(
	Rif_modalita_pagamentoBulk rifModPag,
	Integer codiceTerzo) {

	SQLBuilder sql = createSQLBuilder();
	sql.setOrderBy("pg_banca",it.cnr.jada.util.OrderConstants.ORDER_DESC);
	sql.addSQLClause("AND", "BANCA.CD_TERZO", sql.EQUALS, codiceTerzo);
	sql.addSQLClause("AND", "BANCA.TI_PAGAMENTO", sql.EQUALS, rifModPag.getTi_pagamento());
	sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", sql.EQUALS, "N");

	if (rifModPag.getFl_per_cessione() != null && 
		rifModPag.getFl_per_cessione().booleanValue())	{

		sql.addTableToHeader("MODALITA_PAGAMENTO");
		sql.addSQLJoin("BANCA.CD_TERZO","MODALITA_PAGAMENTO.CD_TERZO");
		// RP. 17/04/2013 commentato perchè possono essere + delegati attivi contemporaneamente
		//sql.addSQLJoin("BANCA.CD_TERZO_DELEGATO","MODALITA_PAGAMENTO.CD_TERZO_DELEGATO");
		sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", sql.ISNOTNULL, null);
		sql.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_MODALITA_PAG", sql.EQUALS, rifModPag.getCd_modalita_pag());
	} else
		sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", sql.ISNULL, null);
	
	return sql;
}
}
