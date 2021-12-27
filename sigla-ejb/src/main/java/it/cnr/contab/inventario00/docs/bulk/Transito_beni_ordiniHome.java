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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioHome;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgHome;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.*;

import java.sql.Timestamp;
import java.util.List;

public class Transito_beni_ordiniHome extends BulkHome {

public Transito_beni_ordiniHome(java.sql.Connection conn) {
	super(Transito_beni_ordiniBulk.class,conn);
}
public Transito_beni_ordiniHome(java.sql.Connection conn, PersistentCache persistentCache) {
	super(Transito_beni_ordiniBulk.class,conn,persistentCache);
}
	public Long recuperoId(UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_TRANSITO_BENI_ORDINI").longValue());
	}
	public void initializePrimaryKeyForInsert(UserContext userContext, OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		Transito_beni_ordiniBulk transito = (Transito_beni_ordiniBulk)bulk;
		if (transito.getId() == null)
			transito.setId(recuperoId(userContext));
	}

	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		CompoundFindClause clauses = new CompoundFindClause();
		sql.openParenthesis("AND");
		sql.addClause("AND", "stato", SQLBuilder.EQUALS, Transito_beni_ordiniBulk.STATO_COMPLETO);
		sql.addClause("OR", "stato", SQLBuilder.EQUALS, Transito_beni_ordiniBulk.STATO_INSERITO);
		sql.closeParenthesis();

		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHomeCache().getHome(Id_inventarioBulk.class);
		try {
			Id_inventarioBulk inventario = inventarioHome.findInventarioFor(usercontext,false);
			clauses.addClause("AND", "pg_inventario", SQLBuilder.EQUALS, inventario.getPg_inventario());
		} catch (IntrospectionException e) {
			throw new PersistencyException(e);
		}
		sql.addOrderBy("id");
		return sql;
	}

	public SQLBuilder selectBeniInTransitoDaInventariare(UserContext usercontext, Transito_beni_ordiniBulk transito, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sqlBuilder = super.createSQLBuilder();
		if(compoundfindclause == null){
			if(transito != null)
				compoundfindclause = transito.buildFindClauses(null);
		} else {
			compoundfindclause = CompoundFindClause.and(compoundfindclause, transito.buildFindClauses(Boolean.FALSE));
		}
		sqlBuilder.addClause(compoundfindclause);

		sqlBuilder.addClause("AND", "stato", SQLBuilder.EQUALS, Transito_beni_ordiniBulk.STATO_COMPLETO);
		Id_inventarioHome inventarioHome = (Id_inventarioHome) getHomeCache().getHome(Id_inventarioBulk.class);
		try {
			Id_inventarioBulk inventario = inventarioHome.findInventarioFor(usercontext,false);
			sqlBuilder.addClause("AND", "pg_inventario", SQLBuilder.EQUALS, inventario.getPg_inventario());
		} catch (IntrospectionException e) {
			throw new PersistencyException(e);
		}


		return sqlBuilder;
	}

	@Override
	public void update(Persistent persistent, UserContext userContext) throws PersistencyException {
		Transito_beni_ordiniBulk transito = (Transito_beni_ordiniBulk)persistent;
		if (!transito.isStatoTrasferito() && !transito.isStatoAnnullato()){
			if (transito.isTuttiCampiValorizzatiPerInventariazione()){
				transito.setStato(Transito_beni_ordiniBulk.STATO_COMPLETO);
			} else {
				transito.setStato(Transito_beni_ordiniBulk.STATO_INSERITO);
			}
		}
		if (transito.getFl_ammortamento() != null && !transito.getFl_ammortamento()){
			transito.setTi_ammortamento(null);
		}
		super.update(persistent, userContext);
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		return super.completeBulkRowByRow(userContext, persistent);
	}
}
