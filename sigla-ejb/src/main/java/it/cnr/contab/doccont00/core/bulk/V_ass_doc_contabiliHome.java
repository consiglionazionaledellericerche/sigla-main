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

import java.util.*;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ass_doc_contabiliHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_ass_doc_contabiliHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_ass_doc_contabiliHome(java.sql.Connection conn) {
	super(V_ass_doc_contabiliBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_ass_doc_contabiliHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_ass_doc_contabiliHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_ass_doc_contabiliBulk.class,conn,persistentCache);
}
/**
 * Metodo per cercare i documenti contabili associati ad un mandato
 *
 * @param mandato <code>MandatoBulk</code> il mandato
 *
 * @return result i documenti contabili associati al mandato
 */
public Collection findDoc_contabili_coll( it.cnr.jada.UserContext userContext,MandatoBulk mandato ) throws PersistencyException, IntrospectionException
{
	SQLBuilder sql = createSQLBuilder();
	sql.openParenthesis( "AND" );	
	sql.openParenthesis( "AND" );
	sql.addClause("AND","cd_tipo_documento_cont",sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN );	
	sql.addClause("AND","esercizio",sql.EQUALS, mandato.getEsercizio() );
	sql.addClause("AND","cd_cds",sql.EQUALS, mandato.getCds().getCd_unita_organizzativa() );
	sql.addClause("AND","pg_documento_cont",sql.EQUALS, mandato.getPg_mandato() );
	sql.closeParenthesis();
	sql.openParenthesis( "OR" );
	sql.addClause("AND","cd_tipo_documento_cont_coll",sql.EQUALS, Numerazione_doc_contBulk.TIPO_MAN );	
	sql.addClause("AND","esercizio_coll",sql.EQUALS, mandato.getEsercizio() );
	sql.addClause("AND","cd_cds_coll",sql.EQUALS, mandato.getCds().getCd_unita_organizzativa() );
	sql.addClause("AND","pg_documento_cont_coll",sql.EQUALS, mandato.getPg_mandato() );
	sql.addClause("AND","fl_con_man_prc",sql.EQUALS, Boolean.TRUE );		
	sql.closeParenthesis();
	sql.closeParenthesis();
	Collection result =   fetchAll( sql);
	for ( Iterator i = result.iterator(); i.hasNext(); )
		((V_ass_doc_contabiliBulk) i.next()).setMan_rev( mandato );
	return result;	

}	

/**
 * Metodo per cercare i documenti contabili associati ad una reversale
 *
 * @param reversale <code>ReversaleBulk</code> la reversale
 *
 * @return result i documenti contabili associati alla reversale
 */
public Collection findDoc_contabili_coll( ReversaleBulk reversale ) throws PersistencyException, IntrospectionException
{
	SQLBuilder sql = createSQLBuilder();
	sql.openParenthesis( "AND" );	
	sql.openParenthesis( "AND" );
	sql.addClause("AND","cd_tipo_documento_cont",sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV );	
	sql.addClause("AND","esercizio",sql.EQUALS, reversale.getEsercizio() );
	sql.addClause("AND","cd_cds",sql.EQUALS, reversale.getCds().getCd_unita_organizzativa() );
	sql.addClause("AND","pg_documento_cont",sql.EQUALS, reversale.getPg_reversale() );
	sql.closeParenthesis();
	sql.openParenthesis( "OR" );
	sql.addClause("AND","cd_tipo_documento_cont_coll",sql.EQUALS, Numerazione_doc_contBulk.TIPO_REV );	
	sql.addClause("AND","esercizio_coll",sql.EQUALS, reversale.getEsercizio() );
	sql.addClause("AND","cd_cds_coll",sql.EQUALS, reversale.getCds().getCd_unita_organizzativa() );
	sql.addClause("AND","pg_documento_cont_coll",sql.EQUALS, reversale.getPg_reversale() );
	sql.addClause("AND","fl_con_man_prc",sql.EQUALS, Boolean.TRUE );			
	sql.closeParenthesis();
	sql.closeParenthesis();
	Collection result =   fetchAll( sql);
	for ( Iterator i = result.iterator(); i.hasNext(); )
		((V_ass_doc_contabiliBulk) i.next()).setMan_rev( reversale );
	return result;	

}	

}
