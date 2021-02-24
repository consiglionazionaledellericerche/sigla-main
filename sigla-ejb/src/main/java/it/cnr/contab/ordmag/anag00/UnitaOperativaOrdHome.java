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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.ordmag.magazzino.bulk.LottoMagBulk;
import it.cnr.contab.ordmag.magazzino.bulk.ScaricoMagazzinoRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class UnitaOperativaOrdHome extends BulkHome {
	public UnitaOperativaOrdHome(Connection conn) {
		super(UnitaOperativaOrdBulk.class, conn);
	}
	public UnitaOperativaOrdHome(Connection conn, PersistentCache persistentCache) {
		super(UnitaOperativaOrdBulk.class, conn, persistentCache);
	}
	public java.util.List findAssUnitaOperativaList(UnitaOperativaOrdBulk uop ) throws IntrospectionException,PersistencyException, ApplicationException
	{
		PersistentHome repHome = getHomeCache().getHome(AssUnitaOperativaOrdBulk.class);
		SQLBuilder sql = repHome.createSQLBuilder();

		sql.addSQLClause( "AND", "cd_unita_operativa", SQLBuilder.EQUALS, uop.getCdUnitaOperativa());
		return repHome.fetchAll(sql);
	}
	@Override
	public SQLBuilder selectByClause(UserContext userContext,
			CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(userContext, compoundfindclause);
		filtraUO(userContext, sql, true);
		return sql;
	}

	private void filtraUO(UserContext userContext, SQLBuilder sql, boolean join) throws PersistencyException{
		Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) 
				getHomeCache().getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
		if (!CNRUserContext.getCd_unita_organizzativa(userContext).equals(ente.getCd_unita_organizzativa())){
			Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk)getHomeCache().getHome(Unita_organizzativaBulk.class).
					findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext)));
			if(!uoScrivania.isUoCds())
				sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));
			else {
				if (join){
					sql.addTableToHeader("UNITA_ORGANIZZATIVA");
					sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
					sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_UNITA_PADRE", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
				}else {
					sql.addSQLClause("AND","CD_UNITA_PADRE",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				}
			}
		}
	}
	
	public SQLBuilder selectUnitaOperativeAbilitateByClause(UserContext userContext, CompoundFindClause compoundfindclause, String... tipoOperazione) throws PersistencyException{
		SQLBuilder sql = this.selectByClause(compoundfindclause);
		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		sql.addSQLClause(FindClause.AND, "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());
		Optional.ofNullable(tipoOperazione)
			.ifPresent(e->{
				sql.openParenthesis(FindClause.AND);
				Arrays.asList(e).stream()
					.forEach(tipoOpe->sql.addSQLClause(FindClause.OR, "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, tipoOpe));
				sql.closeParenthesis();
			});
		return sql;
	}
	

	public List<UnitaOperativaOrdBulk> findUnitaOperativeAbilitate(UserContext userContext, String tipoOperazione) throws PersistencyException {
		return this.fetchAll(selectUnitaOperativeAbilitateByClause(userContext, new CompoundFindClause(), tipoOperazione));
	}	
}