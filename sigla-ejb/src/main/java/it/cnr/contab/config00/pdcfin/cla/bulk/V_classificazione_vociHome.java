/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.jada.persistency.PersistentCache;

public class V_classificazione_vociHome extends Classificazione_vociHome {
	public V_classificazione_vociHome(java.sql.Connection conn) {
		super(V_classificazione_vociBulk.class, conn);
	}
	public V_classificazione_vociHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_classificazione_vociBulk.class, conn, persistentCache);
	}
}