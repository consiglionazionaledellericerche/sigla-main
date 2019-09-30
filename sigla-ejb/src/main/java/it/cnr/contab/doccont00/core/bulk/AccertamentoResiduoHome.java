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

import java.util.Collections;
import java.util.List;

import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
/**
 * Insert the type's description here.
 * Creation date: (09/10/2002 18.09.58)
 * @author: Ilaria Gorla
 */
public class AccertamentoResiduoHome extends AccertamentoHome {
/**
 * AccertamentoResiduoHome constructor comment.
 * @param conn java.sql.Connection
 */
public AccertamentoResiduoHome(java.sql.Connection conn) {
	super(AccertamentoResiduoBulk.class, conn);
}
/**
 * AccertamentoResiduoHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public AccertamentoResiduoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(AccertamentoResiduoBulk.class, conn, persistentCache);
}
/**
 * Metodo per selezionare gli accertamenti su partita di giro.
 *
 * @return sql il risultato della selezione
 */
public SQLBuilder createSQLBuilder() {
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, new Boolean(false));
	sql.addClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_ACR_RES );
	return sql;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param capitoliList	
 * @return 
 * @throws IntrospectionException	
 * @throws PersistencyException	
 */
public java.util.List findCdr( List capitoliList, AccertamentoBulk accertamento ) throws IntrospectionException,PersistencyException 
{
	int size = capitoliList.size() ;
		
	if ( size == 0 )
		return Collections.EMPTY_LIST;
			
	Voce_fBulk capitolo = (Voce_fBulk) capitoliList.iterator().next();

	PersistentHome cdrHome = getHomeCache().getHome(CdrBulk.class);

	SQLBuilder sql = cdrHome.createSQLBuilder();
	sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, capitolo.getCd_unita_organizzativa());

	return cdrHome.fetchAll(sql);
}
public void initializePrimaryKeyForInsert(UserContext userContext, OggettoBulk bulk) throws PersistencyException, ComponentException {
	AccertamentoBulk accertamento = (AccertamentoBulk)bulk;
	if (accertamento.getPg_accertamento()==null)
		super.initializePrimaryKeyForInsert(userContext, bulk);
}
}
