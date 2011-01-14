package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.sql.*;

public class AccertamentoCdsHome extends AccertamentoHome {
public AccertamentoCdsHome(java.sql.Connection conn) 
{
	super(AccertamentoCdsBulk.class, conn);
}
public AccertamentoCdsHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(AccertamentoCdsBulk.class, conn, persistentCache);
}
/**
 * Metodo per cercare gli accertamenti di sistema (di tipo ACR_SIST)
 *
 * @return sql Il risultato della selezione
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_SIST);
	return sql;
}
}
