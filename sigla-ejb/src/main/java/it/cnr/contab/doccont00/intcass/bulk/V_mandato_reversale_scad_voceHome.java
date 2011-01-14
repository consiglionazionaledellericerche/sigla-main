package it.cnr.contab.doccont00.intcass.bulk;

import java.util.List;

import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;


public class V_mandato_reversale_scad_voceHome extends BulkHome {

	public V_mandato_reversale_scad_voceHome(java.sql.Connection conn) {
		super(V_mandato_reversale_scad_voceBulk.class,conn);
	}
	public V_mandato_reversale_scad_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_mandato_reversale_scad_voceBulk.class,conn,persistentCache);
	}
    public List findReversaleList(ReversaleBulk reversale) throws PersistencyException{
    	SQLBuilder sql = createSQLBuilder();
    	sql.addClause("AND","esercizio",SQLBuilder.EQUALS,reversale.getEsercizio());
    	sql.addClause("AND","pg_documento",SQLBuilder.EQUALS,reversale.getPg_reversale());
    	sql.addClause("AND","cd_cds",SQLBuilder.EQUALS,reversale.getCd_cds());
    	sql.addClause("AND","ti_documento",SQLBuilder.EQUALS,"R");
    	sql.addClause("AND","ti_gestione",SQLBuilder.EQUALS,CostantiTi_gestione.TI_GESTIONE_ENTRATE);    	
    	return fetchAll(sql);
    }

}
