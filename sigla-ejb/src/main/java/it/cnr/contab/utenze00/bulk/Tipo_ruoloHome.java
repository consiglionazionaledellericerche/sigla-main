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

/*
* Created by Generator 1.0
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Tipo_ruoloHome extends BulkHome {
	
	public Tipo_ruoloHome(java.sql.Connection conn) {
		super(Tipo_ruoloBulk.class, conn);
	}
	public Tipo_ruoloHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Tipo_ruoloBulk.class, conn, persistentCache);
	}
	/**
	 * Recupera la lista di privilegi (PrivilegioBulk) di tipo pubblico ordinandoli per codice
	 * @param gestore parametro non usato
	 * @return List lista di PrivilegioBulk
	 */

	public java.util.List findPrivilegi_disponibili( UtenteBulk gestore ) throws IntrospectionException, PersistencyException 
	{
		PersistentHome privilegioHome = getHomeCache().getHome( PrivilegioBulk.class);
		SQLBuilder sql = privilegioHome.createSQLBuilder();
		if ( !"*".equals( gestore.getCd_cds_configuratore() ))
			sql.addSQLClause("AND","TI_PRIVILEGIO",SQLBuilder.EQUALS,PrivilegioBulk.TIPO_PUBBLICO);
		else{
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","TI_PRIVILEGIO",SQLBuilder.EQUALS,PrivilegioBulk.TIPO_PUBBLICO);
			sql.addSQLClause("OR","TI_PRIVILEGIO",SQLBuilder.EQUALS,PrivilegioBulk.TIPO_RISERVATO_CNR);
			sql.closeParenthesis();
		}
		sql.addOrderBy( "CD_PRIVILEGIO");
		return privilegioHome.fetchAll(sql);

	}
	/**
	 * Recupera la lista di associazioni tipo_ruolo-privilegio (Ass_tipo_ruolo_privilegioBulk) definite per un ruolo 
	 * @param ruolo PrivilegioBulk per cui cercare le associazioni
	 * @return List lista di Ass_tipo_ruolo_privilegioBulk
	 */

	public java.util.Collection findTipo_ruolo_privilegi(Tipo_ruoloBulk tipo_ruolo) throws IntrospectionException, PersistencyException 
	{
		PersistentHome raHome = getHomeCache().getHome( Ass_tipo_ruolo_privilegioBulk.class);
		SQLBuilder sql = raHome.createSQLBuilder();
		sql.addSQLClause("AND","TIPO",SQLBuilder.EQUALS,tipo_ruolo.getTipo());
		return raHome.fetchAll(sql);
	}
	
}