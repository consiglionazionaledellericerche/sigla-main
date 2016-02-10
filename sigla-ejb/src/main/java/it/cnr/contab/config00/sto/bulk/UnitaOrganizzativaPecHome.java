/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 14/06/2010
 */
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

import java.sql.Connection;
public class UnitaOrganizzativaPecHome extends BulkHome {
	public UnitaOrganizzativaPecHome(Connection conn) {
		super(UnitaOrganizzativaPecBulk.class, conn);
	}
	public UnitaOrganizzativaPecHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaOrganizzativaPecBulk.class, conn, persistentCache);
	}
	public UnitaOrganizzativaPecBulk recuperoUoPec(Unita_organizzativaBulk unita_organizzativa)throws PersistencyException{
		String uoPec;
		if (unita_organizzativa.getCd_tipo_unita().equalsIgnoreCase(Tipo_unita_organizzativaHome.TIPO_UO_SAC))
			uoPec = unita_organizzativa.getCd_unita_organizzativa();
		else
			uoPec = unita_organizzativa.getCd_unita_padre();
		return (UnitaOrganizzativaPecBulk)findByPrimaryKey(new UnitaOrganizzativaPecBulk(uoPec));
	}
}