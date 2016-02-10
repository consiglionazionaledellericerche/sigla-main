package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.BulkHome;

public class Ass_linea_attivita_esercizioHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Ass_linea_attivita_esercizioHome(java.sql.Connection conn) {
		super(Ass_linea_attivita_esercizioBulk.class,conn);
	}

	public Ass_linea_attivita_esercizioHome(java.sql.Connection conn,it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Ass_linea_attivita_esercizioBulk.class,conn,persistentCache);
	}
}
