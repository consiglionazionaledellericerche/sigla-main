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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.progettiric00.core.bulk;
import java.util.List;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_saldi_piano_econom_progettoHome extends BulkHome {
	private static final long serialVersionUID = 5918224310476589096L;
	
	public V_saldi_piano_econom_progettoHome(java.sql.Connection conn) {
		super(V_saldi_piano_econom_progettoBulk.class,conn);
	}
	
	public V_saldi_piano_econom_progettoHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(V_saldi_piano_econom_progettoBulk.class,conn,persistentCache);
	}

	public V_saldi_piano_econom_progettoBulk cercaSaldoPianoEconomico(Progetto_piano_economicoBulk bulk, String tiGestione) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();	
		
		sql.addSQLClause(FindClause.AND,"PG_PROGETTO",SQLBuilder.EQUALS,bulk.getPg_progetto());
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,bulk.getEsercizio_piano());
	    sql.addSQLClause(FindClause.AND,"CD_UNITA_PIANO",SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
	    sql.addSQLClause(FindClause.AND,"CD_VOCE_PIANO",SQLBuilder.EQUALS,bulk.getCd_voce_piano());
		sql.addSQLClause(FindClause.AND,"TI_GESTIONE",SQLBuilder.EQUALS,tiGestione);

		List<V_saldi_piano_econom_progettoBulk> list = fetchAll(sql);
		if (list!=null && !list.isEmpty())
			return list.get(0);
		return null;
	}
}
