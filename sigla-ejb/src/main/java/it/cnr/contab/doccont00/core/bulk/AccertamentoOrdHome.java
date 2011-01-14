package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.sql.*;
public class AccertamentoOrdHome extends AccertamentoHome {
public AccertamentoOrdHome(java.sql.Connection conn) 
{
	super(AccertamentoOrdBulk.class, conn);
}
public AccertamentoOrdHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(AccertamentoOrdBulk.class, conn, persistentCache);
}
/**
 * Metodo per cercare gli accertamenti di sistema (di tipo ACR_SIST)
 *
 * @return sql Il risultato della selezione
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.openParenthesis("AND");
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR);
	sql.addClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_PLUR);	
	sql.closeParenthesis();
	return sql;
}
}
