/*
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcfin.cla.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Classificazione_voci_speHome extends Classificazione_vociHome {
	protected Classificazione_voci_speHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Classificazione_voci_speHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}

	public Classificazione_voci_speHome(java.sql.Connection conn) {
		super(Classificazione_voci_speBulk.class, conn);
	}
	public Classificazione_voci_speHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_speBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		return selectSpese(usercontext, compoundfindclause);
	}	
	public SQLBuilder selectSpese(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ti_gestione",SQLBuilder.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
		return sql;
	}	
}