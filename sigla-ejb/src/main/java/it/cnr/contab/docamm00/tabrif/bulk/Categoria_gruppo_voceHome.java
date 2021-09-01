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

package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.List;

public class Categoria_gruppo_voceHome extends BulkHome {
public Categoria_gruppo_voceHome(java.sql.Connection conn) {
	super(Categoria_gruppo_voceBulk.class,conn);
}
public Categoria_gruppo_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Categoria_gruppo_voceBulk.class,conn,persistentCache);
}
public Categoria_gruppo_voceBulk findCategoria_gruppo_voceforvoce(Elemento_voceBulk elem) throws PersistencyException{
	it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
    sql.addTableToHeader("CATEGORIA_GRUPPO_INVENT");
    sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO");
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,elem.getEsercizio());
	sql.addSQLClause("AND","TI_APPARTENENZA",sql.EQUALS,elem.getTi_appartenenza());
	sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS,elem.getTi_gestione());
	sql.addSQLClause("AND","CD_ELEMENTO_VOCE",sql.EQUALS,elem.getCd_elemento_voce());
	//sql.addSQLClause("AND","CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_PADRE",sql.ISNULL,null);
	java.util.List lista = fetchAll(sql);
	if ( lista.size()>0 )
		return (Categoria_gruppo_voceBulk)lista.get(0);
	else
		return null;
	
}
	public List<Categoria_gruppo_voceBulk> findCategoriaGruppoForVoce(Elemento_voceBulk elem) throws PersistencyException{
		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,elem.getEsercizio());
		sql.addSQLClause(FindClause.AND,"TI_APPARTENENZA",SQLBuilder.EQUALS,elem.getTi_appartenenza());
		sql.addSQLClause(FindClause.AND,"TI_GESTIONE",SQLBuilder.EQUALS,elem.getTi_gestione());
		sql.addSQLClause(FindClause.AND,"CD_ELEMENTO_VOCE",SQLBuilder.EQUALS,elem.getCd_elemento_voce());
		return fetchAll(sql);
	}

}
