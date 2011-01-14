package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vp_liquid_iva_annualeHome extends BulkHome {
public Vp_liquid_iva_annualeHome(java.sql.Connection conn) {
	super(Vp_liquid_iva_annualeBulk.class,conn);
}
public Vp_liquid_iva_annualeHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Vp_liquid_iva_annualeBulk.class,conn,persistentCache);
}
/**
 * Insert the method's description here.
 * Creation date: (02/12/2003 17.06.34)
 */
public BulkList findDettagliPerTipo(
	Liquidazione_iva_annualeVBulk bulk,
	String tipo) 
	throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "id", SQLBuilder.EQUALS, bulk.getId_report());
	sql.addClause("AND", "tipo", SQLBuilder.EQUALS, tipo);
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio());
	
	return new BulkList(fetchAll(sql));
}
}
