package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_attiva_rigaIHome extends Fattura_attiva_rigaHome {
public Fattura_attiva_rigaIHome(java.sql.Connection conn) {
	super(Fattura_attiva_rigaIBulk.class,conn);
}
public Fattura_attiva_rigaIHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Fattura_attiva_rigaIBulk.class,conn,persistentCache);
}
protected SQLBuilder selectForAccertamentoExceptFor(
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza,
	Fattura_attivaBulk fattura) {
	
	SQLBuilder sql = super.selectForAccertamentoExceptFor(scadenza, fattura);

	sql.addSQLClause("AND","FATTURA_ATTIVA.TI_FATTURA",sql.EQUALS, fattura.TIPO_FATTURA_ATTIVA);

	return sql;
}
}
