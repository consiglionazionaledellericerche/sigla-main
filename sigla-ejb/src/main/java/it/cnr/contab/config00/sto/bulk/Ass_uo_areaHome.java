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
* Date 12/09/2005
*/
package it.cnr.contab.config00.sto.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Ass_uo_areaHome extends BulkHome {
	public Ass_uo_areaHome(java.sql.Connection conn) {
		super(Ass_uo_areaBulk.class, conn);
	}
	public Ass_uo_areaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Ass_uo_areaBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}

	/**
	 * Ritorna l'UO bulk dell'unità organizzativa presidente dell'area collegata all'UO in processo
	 * Creation date: (13/09/2008 08:32:22)
	 * @param assUoArea il legame tra area e Uo
	 * @return il bulk dell'UO presidente o null
	 */
	public Unita_organizzativaBulk getUOPresidenteArea(Ass_uo_areaBulk assUoArea) throws ApplicationException, PersistencyException
	{
		if(assUoArea.getUnita_organizzativa().getUnita_padre() == null)
			throw new ApplicationException("Riferimenti all'uo padre non specificati!");

		if(assUoArea.getCds_area_ricerca() == null)
			throw new ApplicationException("Riferimenti all'area di ricerca non specificati!");

		Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHomeCache().getHome(Unita_organizzativaBulk.class);

		SQLBuilder sql = this.createSQLBuilder();

		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, assUoArea.getEsercizio() );
		sql.addClause("AND", "cd_area_ricerca", SQLBuilder.EQUALS, assUoArea.getCd_area_ricerca() );
		sql.addClause("AND", "fl_presidente_area", SQLBuilder.EQUALS, new Boolean(true) );

 	    java.util.Collection aC = fetchAll(sql);
	
		if(aC.size()>1)
			throw new ApplicationException("Errore nei dati: esiste più di un responsabile per l'area di ricerca " + assUoArea.getCd_area_ricerca() + "!");
	
		if(aC.size()==1)
			return (Unita_organizzativaBulk)uoHome.findByPrimaryKey(((Ass_uo_areaBulk)aC.iterator().next()).getUnita_organizzativa());
	
	    return null;
	}    
}