package it.cnr.contab.doccont00.core.bulk;

public class MandatoAccreditamento_terzoHome extends Mandato_terzoHome {
public MandatoAccreditamento_terzoHome(java.sql.Connection conn) {
	super(MandatoAccreditamento_terzoBulk.class, conn);
}
public MandatoAccreditamento_terzoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(MandatoAccreditamento_terzoBulk.class, conn, persistentCache);
}
}
