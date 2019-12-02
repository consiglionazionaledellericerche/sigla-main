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
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_passivo_obbligazioneHome extends BulkHome {
	public V_doc_passivo_obbligazioneHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	public V_doc_passivo_obbligazioneHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_doc_passivo_obbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_doc_passivo_obbligazioneHome(java.sql.Connection conn) {
	super(V_doc_passivo_obbligazioneBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_doc_passivo_obbligazioneHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_doc_passivo_obbligazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_doc_passivo_obbligazioneBulk.class,conn,persistentCache);
}
/**
 * Ricerca se esistono altre scadenze di obbligazione contabilizzate sulla stessa lettera di pagamento
 * 
 *
 * @param Mandato_rigaIBulk	riga del mandato
 * @return Collection di istanze V_doc_passivo_obbligazioneBulk
 * @throws PersistencyException	
 */
public Collection find1210Collegati( Mandato_rigaIBulk riga ) throws PersistencyException
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "cd_cds", sql.EQUALS, riga.getCd_cds_doc_amm());
	sql.addClause( "AND", "cd_unita_organizzativa", sql.EQUALS, riga.getCd_uo_doc_amm());
	
	// Err. 774 - Borriello
	// La condizione deve essere impostata su ESERCIZIO_OBBLIGAZIONE e prende come riferimento l'esercizio del mandato
	//sql.addClause( "AND", "esercizio", sql.EQUALS, riga.getEsercizio_doc_amm());
	sql.addClause( "AND", "esercizio_obbligazione", sql.EQUALS, riga.getEsercizio());

	
	sql.addClause( "AND", "pg_lettera", sql.EQUALS, riga.getPg_lettera());
	sql.openParenthesis( "AND" );
	sql.openParenthesis( "AND" );
	sql.addClause( "AND", "esercizio_ori_obbligazione", sql.NOT_EQUALS, riga.getEsercizio_ori_obbligazione());
	sql.closeParenthesis();
	sql.openParenthesis( "OR" );
	sql.addClause( "AND", "esercizio_ori_obbligazione", sql.EQUALS, riga.getEsercizio_ori_obbligazione());
	sql.addClause( "AND", "pg_obbligazione", sql.NOT_EQUALS, riga.getPg_obbligazione());
	sql.closeParenthesis();
	sql.openParenthesis( "OR" );
	sql.addClause( "AND", "esercizio_ori_obbligazione", sql.EQUALS, riga.getEsercizio_ori_obbligazione());
	sql.addClause( "AND", "pg_obbligazione", sql.EQUALS, riga.getPg_obbligazione());
	sql.addClause( "AND", "pg_obbligazione_scadenzario", sql.NOT_EQUALS, riga.getPg_obbligazione_scadenzario());
	sql.closeParenthesis();
	sql.closeParenthesis();		
		
	return fetchAll( sql );
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoKeys( V_doc_passivo_obbligazioneBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "S" );
	List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
	Hashtable ht = new Hashtable();
	Tipo_documento_ammBulk tipo;
	for (Iterator i = result.iterator(); i.hasNext(); )
	{
		tipo = (Tipo_documento_ammBulk) i.next();
		ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
	}	
	return ht;
}
}
