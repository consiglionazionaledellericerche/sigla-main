package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_documento_ammHome extends BulkHome {
public Tipo_documento_ammHome(java.sql.Connection conn) {
	super(Tipo_documento_ammBulk.class,conn);
}
public Tipo_documento_ammHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_documento_ammBulk.class,conn,persistentCache);
}

}
