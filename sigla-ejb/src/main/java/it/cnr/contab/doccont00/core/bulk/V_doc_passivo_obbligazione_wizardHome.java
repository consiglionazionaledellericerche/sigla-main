package it.cnr.contab.doccont00.core.bulk;

public class V_doc_passivo_obbligazione_wizardHome extends V_doc_passivo_obbligazioneHome {
	public V_doc_passivo_obbligazione_wizardHome(java.sql.Connection conn) {
		super(V_doc_passivo_obbligazione_wizardBulk.class,conn);
	}
	public V_doc_passivo_obbligazione_wizardHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(V_doc_passivo_obbligazione_wizardBulk.class,conn,persistentCache);
	}
}
