package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (26/03/2003 10.28.22)
 * @author: Gennaro Borriello
 */
public class Stampa_avviso_pag_mandHome extends MandatoHome {
/**
 * Stampa_avviso_pag_mandHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_avviso_pag_mandHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_avviso_pag_mandHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_avviso_pag_mandHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_avviso_pag_mandHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_avviso_pag_mandHome(java.sql.Connection conn) {
	super(conn);
}
/**
 * Stampa_avviso_pag_mandHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_avviso_pag_mandHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(conn, persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param mandato	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public java.util.Collection findMandato_riga(it.cnr.jada.UserContext userContext,MandatoBulk mandato) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param mandato	
 * @return 
 * @throws PersistencyException	
 * @throws IntrospectionException	
 */
public Mandato_terzoBulk findMandato_terzo(it.cnr.jada.UserContext userContext,MandatoBulk mandato) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
	return null;
}
}
