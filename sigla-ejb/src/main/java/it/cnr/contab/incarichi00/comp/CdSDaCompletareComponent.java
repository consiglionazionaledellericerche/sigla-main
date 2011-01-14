package it.cnr.contab.incarichi00.comp;
/**
 * Insert the type's description here.
 * Creation date: (08/10/2001 15:39:09)
 * @author: CNRADM
 */
import java.util.List;


import it.cnr.contab.incarichi00.bulk.V_terzi_da_completareBulk;
import it.cnr.contab.incarichi00.bulk.V_terzi_da_completareHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CdSDaCompletareComponent extends CRUDComponent {

	@SuppressWarnings("unchecked")
	public List<V_terzi_da_completareBulk> findTerzi(UserContext userContext, it.cnr.contab.config00.sto.bulk.CdsBulk cds) throws ComponentException{
		try {
			V_terzi_da_completareHome home = (V_terzi_da_completareHome) getHome(userContext, V_terzi_da_completareBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, cds.getCd_unita_organizzativa());
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	@Override
	protected Query select(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		Query query = super.select(usercontext, compoundfindclause, oggettobulk);
		V_terzi_da_completareHome home = (V_terzi_da_completareHome) getHome(usercontext, V_terzi_da_completareBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLJoin("V_TERZI_DA_COMPLETARE.CD_CDS","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		((SQLBuilder)query).addSQLExistsClause(FindClause.AND, sql);
		return query;
	}	
}