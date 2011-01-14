package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2003 10.26.41)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_reversaleHome extends ReversaleHome {
/**
 * Stampa_vpg_reversaleHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_vpg_reversaleHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_vpg_reversaleHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_reversaleHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_vpg_reversaleHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_reversaleHome(java.sql.Connection conn) {
	super(Stampa_vpg_reversaleBulk.class, conn);
}
/**
 * Stampa_vpg_reversaleHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_reversaleHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_reversaleBulk.class, conn, persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public java.util.Collection findReversale_riga(it.cnr.jada.UserContext userContext,ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public Reversale_terzoBulk findReversale_terzo(it.cnr.jada.UserContext userContext,ReversaleBulk reversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	return null;
}
/**
 * Carica la reversale <reversale> con tutti gli oggetti complessi
 *
 * @param reversale	
 * @return 
 * @throws PersistencyException	
 */
public ReversaleBulk loadReversale(it.cnr.jada.UserContext userContext,String cdCds, Integer esercizio, Long pgReversale) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	return null;
}
}
