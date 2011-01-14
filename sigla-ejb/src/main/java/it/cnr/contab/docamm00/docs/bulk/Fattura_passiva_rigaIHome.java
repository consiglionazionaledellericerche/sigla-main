package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_passiva_rigaIHome extends Fattura_passiva_rigaHome {
public Fattura_passiva_rigaIHome(java.sql.Connection conn) {
	super(Fattura_passiva_rigaIBulk.class,conn);
}
public Fattura_passiva_rigaIHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_passiva_rigaIBulk.class,conn,persistentCache);
}
protected SQLBuilder selectForObbligazioneExceptFor(
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fattura) {
	
	SQLBuilder sql = super.selectForObbligazioneExceptFor(scadenza, fattura);

	sql.addSQLClause("AND","FATTURA_PASSIVA.TI_FATTURA",sql.EQUALS, fattura.TIPO_FATTURA_PASSIVA);

	return sql;
}
}
