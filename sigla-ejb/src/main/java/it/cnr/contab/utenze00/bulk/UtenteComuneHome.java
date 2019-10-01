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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
public class UtenteComuneHome extends UtenteHome {
protected UtenteComuneHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}

protected UtenteComuneHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
public UtenteComuneHome(java.sql.Connection conn) {
	super(UtenteComuneBulk.class,conn);
}
public UtenteComuneHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(UtenteComuneBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli Utenti quelli di tipo comune che non sono template
 * @return SQLBuilder 
 */

public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_utente", SQLBuilder.EQUALS, TIPO_COMUNE);
	sql.addClause("AND", "fl_utente_templ", SQLBuilder.EQUALS, new Boolean(false));		
	return sql; 
}
/**
 * Restituisce il SQLBuilder per selezionare i template definiti dal gestore corrente e ancora validi 
 * ( data corrente > data inizio validita del template e data corrente < data fine validita' del template )
 * @param utente UtenteComuneBulk ricevente
 * @param home home del bulk su cui si cerca
 * @param template è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */

public SQLBuilder selectTemplateByClause( UtenteComuneBulk utente, UtenteTemplateHome home, UtenteTemplateBulk template, CompoundFindClause clause ) throws PersistencyException
{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause( clause );
	sql.addClause("AND", "cd_gestore", SQLBuilder.EQUALS, utente.getCd_gestore());
	sql.addClause("AND", "dt_inizio_validita", SQLBuilder.LESS_EQUALS, getServerTimestamp());		
		
	SimpleFindClause s1 = new SimpleFindClause("AND", "dt_fine_validita", SQLBuilder.ISNULL, null );
	SimpleFindClause s2 = new SimpleFindClause("OR", "dt_fine_validita", SQLBuilder.GREATER_EQUALS, getServerTimestamp());
	CompoundFindClause c = new CompoundFindClause(s1, s2 );
	sql.addClause( c );
	
	return sql; 
}
}
