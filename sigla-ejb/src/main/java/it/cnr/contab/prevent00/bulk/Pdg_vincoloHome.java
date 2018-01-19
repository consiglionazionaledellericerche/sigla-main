/*
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Pdg_vincoloHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Pdg_vincoloHome(java.sql.Connection conn) {
		super(Pdg_vincoloBulk.class,conn);
	}

	public Pdg_vincoloHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Pdg_vincoloBulk.class,conn,persistentCache);
	}

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk pdgVincolo) throws PersistencyException {
		try {
			((Pdg_vincoloBulk)pdgVincolo).setPg_vincolo(
				new Long(
					((Long)findAndLockMax( pdgVincolo, "pg_vincolo", new Long(0) )).longValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}

	public java.util.List<Pdg_vincoloBulk> cercaDettagliVincolati(Voce_f_saldi_cdr_lineaBulk saldo) throws PersistencyException, IntrospectionException
	{
		SQLBuilder sql = this.createSQLBuilder();	
		
		sql.addClause(FindClause.AND,"esercizio_res",SQLBuilder.EQUALS,saldo.getEsercizio_res());
	    sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,saldo.getCd_centro_responsabilita());
	    sql.addClause(FindClause.AND,"cd_linea_attivita",SQLBuilder.EQUALS,saldo.getCd_linea_attivita());
		sql.addClause(FindClause.AND,"ti_appartenenza",SQLBuilder.EQUALS,saldo.getTi_appartenenza());	
		sql.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,saldo.getTi_gestione());
		sql.addClause(FindClause.AND,"cd_elemento_voce",SQLBuilder.EQUALS,saldo.getCd_voce());
		sql.addClause(FindClause.AND,"fl_attivo",SQLBuilder.EQUALS,Boolean.TRUE);

		return this.fetchAll(sql);
	}
}
