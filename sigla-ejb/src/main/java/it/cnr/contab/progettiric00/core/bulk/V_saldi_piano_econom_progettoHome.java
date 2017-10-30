/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;
import java.util.List;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_piano_econom_progettoHome extends BulkHome {
	public V_saldi_piano_econom_progettoHome(java.sql.Connection conn) {
		super(V_saldi_piano_econom_progettoBulk.class,conn);
	}
	
	public V_saldi_piano_econom_progettoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_saldi_piano_econom_progettoBulk.class,conn,persistentCache);
	}

	public V_saldi_piano_econom_progettoBulk cercaSaldoPianoEconomico(Progetto_piano_economicoBulk bulk, String tiGestione) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();	
		
		sql.addSQLClause(FindClause.AND,"PG_PROGETTO",SQLBuilder.EQUALS,bulk.getPg_progetto());
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio_piano());
	    sql.addSQLClause(FindClause.AND,"CD_UNITA_PIANO",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
	    sql.addSQLClause(FindClause.AND,"CD_VOCE_PIANO",SQLBuilder.EQUALS,bulk.getCd_voce_piano());
		sql.addSQLClause(FindClause.AND,"TI_GESTIONE",SQLBuilder.EQUALS,tiGestione);

		List<V_saldi_piano_econom_progettoBulk> list = fetchAll(sql);
		if (list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
}
