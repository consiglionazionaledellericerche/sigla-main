package it.cnr.contab.prevent00.bulk;
/**
 * Home del bulk che gestisce i dati iniziali per le Spese aggregate
 * iniziali.
 */

public class Pdg_aggregato_spe_det_inizialeHome extends Pdg_aggregato_etr_detHome {
public Pdg_aggregato_spe_det_inizialeHome(java.sql.Connection conn) {
	super(Pdg_aggregato_spe_det_inizialeBulk.class,conn);
}

public Pdg_aggregato_spe_det_inizialeHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(Pdg_aggregato_spe_det_inizialeBulk.class,conn, persistentCache);
}

public it.cnr.jada.persistency.sql.SQLBuilder createSQLBuilder() {
	it.cnr.jada.persistency.sql.SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND","ti_aggregato",sql.EQUALS,"I");
	return sql;
}
}