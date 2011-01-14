package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_ap_chHome extends BulkHome {
public Inventario_ap_chHome(java.sql.Connection conn) {
	super(Inventario_ap_chBulk.class,conn);
}
public Inventario_ap_chHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Inventario_ap_chBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public Inventario_ap_chBulk findLastAperturaChiusuraObjFor(Id_inventarioBulk inv, Integer esercizio) throws IntrospectionException, PersistencyException, ApplicationException {

	if (inv==null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non esiste alcun inventario associato alla UO");
	else if (inv.getPg_inventario()==null)	     
       return null;
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inv.getPg_inventario());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,esercizio);
	sql.addOrderBy("DT_APERTURA DESC");

	Broker broker = createBroker(sql);
	Inventario_ap_chBulk obj = null;
	if (broker.next())
		obj = (Inventario_ap_chBulk)fetch(broker);

	broker.close();
	return obj;
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 */
public java.util.List findStoriaApChFor(Id_inventarioBulk inv, Integer esercizio) throws IntrospectionException, PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inv.getPg_inventario());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,esercizio);

	return fetchAll(sql);
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isAperto(Id_inventarioBulk inv, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	Inventario_ap_chBulk invApCh = findLastAperturaChiusuraObjFor(inv,esercizio);
	if ( (invApCh != null) && (invApCh.getStato().equals(Inventario_ap_chBulk.OPEN)) )
		return true;

	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isAperto(Inventario_ap_chBulk invApCh, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	return isAperto(invApCh.getInventario(), esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isChiuso(Id_inventarioBulk inv, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	Inventario_ap_chBulk invApCh = findLastAperturaChiusuraObjFor(inv, esercizio);
	if ( (invApCh != null) && (invApCh.getStato().equals(Inventario_ap_chBulk.CLOSE)) )
		return true;

	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isChiuso(Inventario_ap_chBulk invApCh, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	return isChiuso(invApCh.getInventario(), esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isPending(Id_inventarioBulk inv, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	Inventario_ap_chBulk invApCh = findLastAperturaChiusuraObjFor(inv,esercizio);
	if (invApCh == null)
		return true;

	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (06/12/2001 14.42.23)
 * @throws ApplicationException 
 */
public boolean isPending(Inventario_ap_chBulk invApCh, Integer esercizio) throws PersistencyException, IntrospectionException, ApplicationException {

	return isPending(invApCh.getInventario(),esercizio);
}
}
