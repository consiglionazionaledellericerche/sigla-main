/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.doccont00.core.bulk;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_obb_acr_pgiroHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_obb_acr_pgiroHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Ass_obb_acr_pgiroHome(java.sql.Connection conn) {
	super(Ass_obb_acr_pgiroBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Ass_obb_acr_pgiroHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Ass_obb_acr_pgiroHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Ass_obb_acr_pgiroBulk.class,conn,persistentCache);
}

public boolean findPgiroLiquidCentroAperte( it.cnr.jada.UserContext userContext,Reversale_rigaBulk reversale_riga) throws SQLException, PersistencyException, IntrospectionException
{
	Collection PgiroReversaleriga;
	//boolean pgiroLiquidCentroAperte;
	PgiroReversaleriga = findPgiroReversaleriga(userContext,reversale_riga);
	Ass_obb_acr_pgiroBulk ass_obb;
	for ( Iterator j = PgiroReversaleriga.iterator();j.hasNext(); )
	{
		ass_obb = (Ass_obb_acr_pgiroBulk) j.next();
		PersistentHome home = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("LIQUID_GRUPPO_CENTRO");
		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.CD_CDS","LIQUID_GRUPPO_CENTRO.CD_CDS_OBB_ACCENTR");
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO","LIQUID_GRUPPO_CENTRO.ESERCIZIO_OBB_ACCENTR");		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_OBBLIGAZIONE","LIQUID_GRUPPO_CENTRO.ESERCIZIO_ORI_OBB_ACCENTR");		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.PG_OBBLIGAZIONE","LIQUID_GRUPPO_CENTRO.PG_OBB_ACCENTR");
		
		sql.addSQLClause("AND","LIQUID_GRUPPO_CENTRO.STATO",sql.EQUALS,"I");
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.cd_cds",sql.EQUALS,ass_obb.getCd_cds());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.esercizio",sql.EQUALS,ass_obb.getEsercizio());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.esercizio_ori_accertamento",sql.EQUALS,ass_obb.getEsercizio_ori_accertamento());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.pg_accertamento",sql.EQUALS,ass_obb.getPg_accertamento());
		
		if (sql.executeCountQuery(getConnection())>0)							
		    return true;		
	}
	return false;
}

public boolean findPgiroLiqIvaCentroAperte( it.cnr.jada.UserContext userContext,Reversale_rigaBulk reversale_riga) throws SQLException, PersistencyException, IntrospectionException
{
	Collection PgiroReversaleriga;
	PgiroReversaleriga = findPgiroReversaleriga(userContext,reversale_riga);
	Ass_obb_acr_pgiroBulk ass_obb;
	for ( Iterator j = PgiroReversaleriga.iterator();j.hasNext(); )
	{
		ass_obb = (Ass_obb_acr_pgiroBulk) j.next();
		PersistentHome home = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("LIQUIDAZIONE_IVA_CENTRO");
		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.CD_CDS","LIQUIDAZIONE_IVA_CENTRO.CD_CDS_OBB_ACCENTR");
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO","LIQUIDAZIONE_IVA_CENTRO.ESERCIZIO_OBB_ACCENTR");		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.ESERCIZIO_ORI_OBBLIGAZIONE","LIQUIDAZIONE_IVA_CENTRO.ESERCIZIO_ORI_OBB_ACCENTR");		
		sql.addSQLJoin("ASS_OBB_ACR_PGIRO.PG_OBBLIGAZIONE","LIQUIDAZIONE_IVA_CENTRO.PG_OBB_ACCENTR");
		
		sql.addSQLClause("AND","LIQUIDAZIONE_IVA_CENTRO.STATO",sql.EQUALS,"I");
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.cd_cds",sql.EQUALS,ass_obb.getCd_cds());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.esercizio",sql.EQUALS,ass_obb.getEsercizio());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.esercizio_ori_accertamento",sql.EQUALS,ass_obb.getEsercizio_ori_accertamento());
		sql.addSQLClause("AND","ASS_OBB_ACR_PGIRO.pg_accertamento",sql.EQUALS,ass_obb.getPg_accertamento());
		
		if (sql.executeCountQuery(getConnection())>0)							
		    return true;		
	}
	return false;
}

public Collection findPgiroReversaleriga( it.cnr.jada.UserContext userContext,Reversale_rigaBulk reversale_riga ) throws PersistencyException
{
	PersistentHome home = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class );
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause( "AND", "cd_cds", sql.EQUALS, reversale_riga.getCd_cds());
	sql.addClause( "AND", "esercizio", sql.EQUALS, reversale_riga.getEsercizio_accertamento());	
	sql.addClause( "AND", "esercizio_ori_accertamento", sql.EQUALS, reversale_riga.getEsercizio_ori_accertamento());	
	sql.addClause( "AND", "pg_accertamento", sql.EQUALS, reversale_riga.getPg_accertamento());
	Collection result = home.fetchAll( sql);
	getHomeCache().fetchAll(userContext);
	return result;
}

	public Collection findPgiroMandatoRiga(it.cnr.jada.UserContext userContext,Mandato_rigaBulk mandato_riga) throws PersistencyException
	{
		PersistentHome home = getHomeCache().getHome( Ass_obb_acr_pgiroBulk.class );
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( "AND", "cd_cds", sql.EQUALS, mandato_riga.getCd_cds());
		sql.addClause( "AND", "esercizio", sql.EQUALS, mandato_riga.getEsercizio_obbligazione());
		sql.addClause( "AND", "esercizio_ori_obbligazione", sql.EQUALS, mandato_riga.getEsercizio_ori_obbligazione());
		sql.addClause( "AND", "pg_obbligazione", sql.EQUALS, mandato_riga.getPg_obbligazione());
		Collection result = home.fetchAll( sql);
		getHomeCache().fetchAll(userContext);
		return result;
	}
}
