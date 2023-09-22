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
 * Date 12/09/2016
 */
package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;

public class VDocammElettroniciAttiviHome extends BulkHome {
	public VDocammElettroniciAttiviHome(Connection conn) {
		super(VDocammElettroniciAttiviBulk.class, conn);
	}
	public VDocammElettroniciAttiviHome(Connection conn, PersistentCache persistentCache) {
		super(VDocammElettroniciAttiviBulk.class, conn, persistentCache);
	}

	public AutofatturaBulk findAutofattura(VDocammElettroniciAttiviBulk vDocammElettroniciAttiviBulk) throws PersistencyException {
		if (vDocammElettroniciAttiviBulk.isAutofattura()) {
			AutofatturaHome autofatturaHome = (AutofatturaHome)getHomeCache().getHome(AutofatturaBulk.class);
			SQLBuilder sqlAuto = autofatturaHome.createSQLBuilder();
			sqlAuto.addSQLClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getCd_cds());
			sqlAuto.addSQLClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getCd_unita_organizzativa());
			sqlAuto.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getEsercizio());
			sqlAuto.addSQLClause(FindClause.AND, "pg_autofattura", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getPg_docamm());

			java.util.List resultAuto = autofatturaHome.fetchAll(sqlAuto);
			if (resultAuto == null || resultAuto.isEmpty()) return null;
			if (resultAuto.size() != 1)
				throw new PersistencyException("Trovate più autofatture!");
			AutofatturaBulk autof = (AutofatturaBulk) resultAuto.get(0);

			//Carico la fattura passiva collegata
			Fattura_passivaHome fatturaPassivaHome = (Fattura_passivaHome)getHomeCache().getHome(Fattura_passiva_IBulk.class);
			SQLBuilder sqlFatpas = fatturaPassivaHome.createSQLBuilder();
			sqlFatpas.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, autof.getCd_cds_ft_passiva());
			sqlFatpas.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, autof.getCd_uo_ft_passiva());
			sqlFatpas.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, autof.getEsercizio());
			sqlFatpas.addClause(FindClause.AND, "pg_fattura_passiva", SQLBuilder.EQUALS, autof.getPg_fattura_passiva());

			java.util.List resultFatpas = fatturaPassivaHome.fetchAll(sqlFatpas);
			if (resultFatpas == null || resultFatpas.isEmpty()) return null;
			if (resultFatpas.size() != 1)
				throw new PersistencyException("Trovate più autofatture!");
			Fattura_passivaBulk fatpas = (Fattura_passivaBulk) resultFatpas.get(0);

			autof.setFattura_passiva(fatpas);
			return autof;
		}
		return null;
	}

	public Fattura_attivaBulk findFatturaAttiva(VDocammElettroniciAttiviBulk vDocammElettroniciAttiviBulk) throws PersistencyException {
		if (vDocammElettroniciAttiviBulk.isFatturaAttiva()) {
			SQLBuilder sql = getHomeCache().getHome(Fattura_attivaHome.class).createSQLBuilder();
			sql.addSQLClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getCd_cds());
			sql.addSQLClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getCd_unita_organizzativa());
			sql.addSQLClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getEsercizio());
			sql.addSQLClause(FindClause.AND, "pg_fattura_attiva", SQLBuilder.EQUALS, vDocammElettroniciAttiviBulk.getPg_docamm());

			java.util.List result = fetchAll(sql);
			if (result == null || result.isEmpty()) return null;
			if (result.size() != 1)
				throw new PersistencyException("Trovate più autofatture!");
			Fattura_attivaBulk fatatt = (Fattura_attivaBulk) result.get(0);
			return fatatt;
		}
		return null;
	}

	/**
	 * Ritorna un SQLBuilder con la columnMap del ricevente
	 */
	public SQLBuilder selectByClauseForFatturazioneElettronica(UserContext usercontext, VDocammElettroniciAttiviBulk docamm, CompoundFindClause compoundfindclause) throws PersistencyException {
		SQLBuilder sqlBuilder = super.createSQLBuilder();
		if(compoundfindclause == null) {
			if(docamm != null)
				compoundfindclause = docamm.buildFindClauses(null);
		} else {
			compoundfindclause = CompoundFindClause.and(compoundfindclause, docamm.buildFindClauses(Boolean.FALSE));
		}
		sqlBuilder.addClause(compoundfindclause);

		CompoundFindClause clauses = new CompoundFindClause();
		clauses.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(usercontext));
		clauses.addClause(FindClause.AND, "cdCdsOrigine", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(usercontext));
		clauses.addClause(FindClause.AND, "cdUoOrigine", SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(usercontext));

		if (VDocammElettroniciAttiviBulk.DA_PREDISPORRE_ALLA_FIRMA.equals(docamm.getStatoFattElett()))
			clauses.addClause(FindClause.AND, "statoInvioSdi", SQLBuilder.EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_ALLA_FIRMA);
		else if (VDocammElettroniciAttiviBulk.DA_FIRMARE.equals(docamm.getStatoFattElett()))
			clauses.addClause(FindClause.AND, "statoInvioSdi", SQLBuilder.EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_PREDISPOSTA_FIRMA);
		else {
			clauses.addClause(FindClause.AND, "statoInvioSdi", SQLBuilder.NOT_EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_ALLA_FIRMA);
			clauses.addClause(FindClause.AND, "statoInvioSdi", SQLBuilder.NOT_EQUALS, VDocammElettroniciAttiviBulk.FATT_ELETT_PREDISPOSTA_FIRMA);
		}

		sqlBuilder.addClause(clauses);
		sqlBuilder.addOrderBy("esercizio");
		sqlBuilder.addOrderBy("tipo_docamm");
		sqlBuilder.addOrderBy("pg_docamm");

		return sqlBuilder;
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext, Persistent persistent) throws PersistencyException {
		VDocammElettroniciAttiviBulk docamm = (VDocammElettroniciAttiviBulk) persistent;
		if (((CNRUserContext)userContext).isFromBootstrap()) {
			docamm.setCollegamentoDocumentale("<a class='btn btn-link' onclick='"+
					"doVisualizzaSingoloDocumentiCollegati(\"" + docamm.getTipoDocamm()+"\","+docamm.getEsercizio() + ",\"" + docamm.getCd_cds() + "\",\"" + docamm.getCd_unita_organizzativa() + "\"," + docamm.getPg_docamm() + "); return false' " +
					"title='Visualizza Documenti Collegati'><i class='fa fa-fw fa-2x fa-file-pdf-o text-danger' aria-hidden='true'></i></a>");
		} else {
			docamm.setCollegamentoDocumentale("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) " +
					"doVisualizzaSingoloDocumentiCollegati(\"" + docamm.getTipoDocamm()+"\","+docamm.getEsercizio() + ",\"" + docamm.getCd_cds() + "\",\"" + docamm.getCd_unita_organizzativa() + "\"," + docamm.getPg_docamm() + "); return false' " +
					"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' " +
					"title='Visualizza Documenti Collegati'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
		}
		return super.completeBulkRowByRow(userContext, persistent);
	}
}