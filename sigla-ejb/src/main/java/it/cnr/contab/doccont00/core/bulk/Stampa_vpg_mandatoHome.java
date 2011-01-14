package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (21/02/2003 9.59.30)
 * @author: Roberto Fantino
 */
public class Stampa_vpg_mandatoHome extends MandatoHome {
/**
 * Stampa_vpg_mandatoHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 */
public Stampa_vpg_mandatoHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
/**
 * Stampa_vpg_mandatoHome constructor comment.
 * @param clazz java.lang.Class
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_mandatoHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
/**
 * Stampa_vpg_mandatoHome constructor comment.
 * @param conn java.sql.Connection
 */
public Stampa_vpg_mandatoHome(java.sql.Connection conn) {
	super(Stampa_vpg_mandatoBulk.class, conn);
}
/**
 * Stampa_vpg_mandatoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Stampa_vpg_mandatoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Stampa_vpg_mandatoBulk.class, conn, persistentCache);
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
