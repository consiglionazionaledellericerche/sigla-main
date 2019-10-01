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

package it.cnr.contab.progettiric00.tabrif.bulk;

import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Voce_piano_economico_prgHome extends BulkHome {

	public Voce_piano_economico_prgHome(java.sql.Connection conn) {
		super(Voce_piano_economico_prgBulk.class,conn);
	}
	
	public Voce_piano_economico_prgHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Voce_piano_economico_prgBulk.class,conn,persistentCache);
	}

	public SQLBuilder findVocePianoEconomicoPrgList( Integer pgProgetto ) throws PersistencyException {
		PersistentHome vocePianoHome = getHomeCache().getHome(Voce_piano_economico_prgBulk.class); 
		SQLBuilder sql = vocePianoHome.createSQLBuilder();

		PersistentHome prgPianoEconomicoHome = getHomeCache().getHome(Progetto_piano_economicoBulk.class);  
		SQLBuilder sqlExists = prgPianoEconomicoHome.createSQLBuilder();    	
		sqlExists.addSQLJoin("PROGETTO_PIANO_ECONOMICO.CD_UNITA_ORGANIZZATIVA","VOCE_PIANO_ECONOMICO_PRG.CD_UNITA_ORGANIZZATIVA");
		sqlExists.addSQLJoin("PROGETTO_PIANO_ECONOMICO.CD_VOCE_PIANO","VOCE_PIANO_ECONOMICO_PRG.CD_VOCE_PIANO");
		if (Optional.ofNullable(pgProgetto).isPresent())
			sqlExists.addSQLClause(FindClause.AND, "PROGETTO_PIANO_ECONOMICO.PG_PROGETTO",SQLBuilder.EQUALS,pgProgetto);
		else
			sqlExists.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
			
		sql.addSQLExistsClause(FindClause.AND, sqlExists);
		return sql;
	}

	public SQLBuilder selectVocePianoEconomicoPrgList( java.lang.Integer esercizio, java.lang.Integer pgProgetto, Integer idClassificazione ) throws PersistencyException {
		PersistentHome vocePianoHome = getHomeCache().getHome(Voce_piano_economico_prgBulk.class); 
		SQLBuilder sql = vocePianoHome.createSQLBuilder();

		Progetto_piano_economicoHome prgPianoEconomicoHome = (Progetto_piano_economicoHome)getHomeCache().getHome(Progetto_piano_economicoBulk.class);  
		SQLBuilder sqlExists = prgPianoEconomicoHome.selectProgettoPianoEconomicoList(esercizio, pgProgetto, idClassificazione);    	
		sqlExists.addSQLJoin("PROGETTO_PIANO_ECONOMICO.CD_UNITA_ORGANIZZATIVA","VOCE_PIANO_ECONOMICO_PRG.CD_UNITA_ORGANIZZATIVA");
		sqlExists.addSQLJoin("PROGETTO_PIANO_ECONOMICO.CD_VOCE_PIANO","VOCE_PIANO_ECONOMICO_PRG.CD_VOCE_PIANO");
		if (Optional.ofNullable(pgProgetto).isPresent())
			sqlExists.addSQLClause(FindClause.AND, "PROGETTO_PIANO_ECONOMICO.PG_PROGETTO",SQLBuilder.EQUALS,pgProgetto);
		else
			sqlExists.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
			
		sql.addSQLExistsClause(FindClause.AND, sqlExists);
		return sql;
	}
}
