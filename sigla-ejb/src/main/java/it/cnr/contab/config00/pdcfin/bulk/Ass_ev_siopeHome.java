/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.config00.pdcfin.bulk;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.List;

import it.cnr.contab.config00.bulk.Codici_siopeHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_ev_siopeHome extends BulkHome {
	public Ass_ev_siopeHome(Connection conn) {
		super(Ass_ev_siopeBulk.class, conn);
	}
	public Ass_ev_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_ev_siopeBulk.class, conn, persistentCache);
	}
	
	public SQLBuilder selectElemento_voceByClause( Ass_ev_siopeBulk bulk, Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS,bulk.getTi_appartenenza());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, bulk.getTi_gestione());
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CAPITOLO);
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
		sql.addClause( clause );
		return sql;
	}
	
	public SQLBuilder selectCodici_siopeByClause( Ass_ev_siopeBulk bulk, Codici_siopeHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, bulk.getTi_gestione());
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );	
		sql.addClause( clause );
		return sql;
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		return sql;
	}
	
	/*public Hashtable loadTi_gestioneKeys(Elemento_voceBulk bulk) throws ApplicationException {
		return new Elemento_voceHome( getConnection()).loadTi_gestioneKeys(bulk);
	}
	
	
	public Hashtable loadTi_appartenenzaKeys(Elemento_voceBulk bulk) throws ApplicationException {
		return new Elemento_voceHome( getConnection()).loadTi_appartenenzaKeys( bulk);
	}*/
	
	
}