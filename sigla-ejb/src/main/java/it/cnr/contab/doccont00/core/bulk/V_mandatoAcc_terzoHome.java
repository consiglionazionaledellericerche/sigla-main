package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (10/12/2002 15.02.53)
 * @author: Ilaria Gorla
 */
public class V_mandatoAcc_terzoHome extends MandatoAccreditamentoHome {
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 */
public V_mandatoAcc_terzoHome(java.sql.Connection conn) {
	super(V_mandatoAcc_terzoBulk.class, conn);
}
/**
 * V_manadato_terzoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public V_mandatoAcc_terzoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(V_mandatoAcc_terzoBulk.class, conn, persistentCache);
}
}
