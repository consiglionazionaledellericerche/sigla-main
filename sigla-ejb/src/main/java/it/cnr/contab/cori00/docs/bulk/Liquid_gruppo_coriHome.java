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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.contab.docamm00.docs.bulk.Documento_amministrativo_passivoBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_comp_doc_cont_nmpBulk;
import it.cnr.contab.doccont00.core.bulk.Ass_mandato_mandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Collection;

public class Liquid_gruppo_coriHome extends BulkHome {
	/**
	  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_coriIHome</code>.
	  *
	**/
	public Liquid_gruppo_coriHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	/**
	  *  Costruttore utilizzato dalla sottoclasse <code>Liquid_gruppo_coriIHome</code>.
	  *
	**/
	public Liquid_gruppo_coriHome(Class clazz, java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Liquid_gruppo_coriHome(java.sql.Connection conn) {
		super(Liquid_gruppo_coriBulk.class,conn);
	}
	public Liquid_gruppo_coriHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Liquid_gruppo_coriBulk.class,conn,persistentCache);
	}

	public Collection<Liquid_gruppo_coriBulk> findByMandato(it.cnr.jada.UserContext userContext, MandatoBulk mandato ) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_doc",SQLBuilder.EQUALS, mandato.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds_doc",SQLBuilder.EQUALS, mandato.getCd_cds() );
		sql.addClause(FindClause.AND,"pg_doc",SQLBuilder.EQUALS, mandato.getPg_mandato() );
		return this.fetchAll( sql);
	}


	public Collection<Liquid_gruppo_cori_detBulk> findDettagli(it.cnr.jada.UserContext userContext, Liquid_gruppo_coriBulk liquidGruppoCoriBulk) throws PersistencyException
	{
		PersistentHome home = getHomeCache().getHome(Liquid_gruppo_cori_detBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_cds() );
		sql.addClause(FindClause.AND,"cd_unita_organizzativa",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND,"pg_liquidazione",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getPg_liquidazione() );
		sql.addClause(FindClause.AND,"cd_cds_origine",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_cds_origine() );
		sql.addClause(FindClause.AND,"cd_uo_origine",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_uo_origine() );
		sql.addClause(FindClause.AND,"pg_liquidazione_origine",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getPg_liquidazione_origine() );
		sql.addClause(FindClause.AND,"cd_gruppo_cr",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_gruppo_cr() );
		sql.addClause(FindClause.AND,"cd_regione",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getCd_regione() );
		sql.addClause(FindClause.AND,"pg_comune",SQLBuilder.EQUALS, liquidGruppoCoriBulk.getPg_comune() );
		return home.fetchAll( sql);
	}
}
