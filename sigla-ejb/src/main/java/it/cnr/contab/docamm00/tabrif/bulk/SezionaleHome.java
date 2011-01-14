package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SezionaleHome extends BulkHome {
public SezionaleHome(java.sql.Connection conn) {
	super(SezionaleBulk.class,conn);
}
public SezionaleHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(SezionaleBulk.class,conn,persistentCache);
}
public boolean verificaStatoEsercizio(SezionaleBulk sezionale) throws PersistencyException, IntrospectionException {

	it.cnr.contab.config00.esercizio.bulk.EsercizioBulk esercizio = (it.cnr.contab.config00.esercizio.bulk.EsercizioBulk) getHomeCache().getHome(it.cnr.contab.config00.esercizio.bulk.EsercizioBulk.class).findByPrimaryKey( 
		new it.cnr.contab.config00.esercizio.bulk.EsercizioBulk( sezionale.getCd_cds(), sezionale.getEsercizio()));
	if (esercizio == null || esercizio.STATO_CHIUSO_DEF.equals(esercizio.getSt_apertura_chiusura()))
		return false;
	return true;
}
}
