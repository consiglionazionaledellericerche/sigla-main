/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;

import it.cnr.jada.persistency.PersistentCache;

public class V_classificazione_voci_epHome extends Classificazione_voci_epHome {
	public V_classificazione_voci_epHome(java.sql.Connection conn) {
		super(V_classificazione_voci_epBulk.class, conn);
	}
	public V_classificazione_voci_epHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(V_classificazione_voci_epBulk.class, conn, persistentCache);
	}
}