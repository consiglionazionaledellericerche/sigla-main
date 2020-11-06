/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.client.docamm.Terzo;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.ordmag.magazzino.bulk.AbilitazioneMagazzinoHome;
import it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.Optional;

public class ParametriSelezioneOrdiniAcqHome extends AbilitazioneOrdiniAcqHome {
	private static final long serialVersionUID = 1L;

	public ParametriSelezioneOrdiniAcqHome(Connection conn) {
		super(ParametriSelezioneMovimentiBulk.class, conn);
	}

	public ParametriSelezioneOrdiniAcqHome(Connection conn, PersistentCache persistentCache) {
		super(ParametriSelezioneMovimentiBulk.class, conn, persistentCache);
	}



	public SQLBuilder selectTerzoByClause(UserContext userContext, ParametriSelezioneOrdiniAcqBulk parametri,
															TerzoHome terzoHome, TerzoBulk terzoBulk,
															CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
		SQLBuilder sql = terzoHome.createSQLBuilder();
		if (Optional.ofNullable(terzoBulk).map(TerzoBulk::getDenominazione_sede).isPresent())
			sql.addClause( "AND", "denominazione_sede", sql.LIKE, Optional.ofNullable(terzoBulk.getDenominazione_sede()).get().concat("%"));
		return sql;
	}
	public SQLBuilder selectUnitaOperativaRiceventeByClause(UserContext userContext, ParametriSelezioneMovimentiBulk parametri,
														 UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk,
														 CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException{
		if (parametri == null || parametri.getMagazzinoAbilitato() == null || parametri.getMagazzinoAbilitato().getCdMagazzino() == null){
			throw new PersistencyException("Selezionare prima il codice del magazzino");
		}
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);
		AbilUtenteUopOperHome home = (AbilUtenteUopOperHome) getHomeCache().getHome(AbilUtenteUopOperBulk.class);
		SQLBuilder sqlExists = home.createSQLBuilder();
		sqlExists.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
		sqlExists.openParenthesis("AND");
		sqlExists.addSQLClause("OR", "ABIL_UTENTE_UOP_OPER.TUTTI_MAGAZZINI", SQLBuilder.EQUALS, "Y");

		AbilUtenteUopOperMagHome homeAbilMag = (AbilUtenteUopOperMagHome) getHomeCache().getHome(AbilUtenteUopOperMagBulk.class);
		SQLBuilder sqlExistsMag = homeAbilMag.createSQLBuilder();
		sqlExistsMag.addSQLJoin("ABIL_UTENTE_UOP_OPER_MAG.CD_UTENTE", "ABIL_UTENTE_UOP_OPER.CD_UTENTE");
		sqlExistsMag.addSQLJoin("ABIL_UTENTE_UOP_OPER_MAG.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		sqlExistsMag.addSQLJoin("ABIL_UTENTE_UOP_OPER_MAG.CD_TIPO_OPERAZIONE", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE");
		sqlExistsMag.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER_MAG.CD_MAGAZZINO", SQLBuilder.EQUALS, parametri.getMagazzinoAbilitato().getCdMagazzino());
		sqlExists.addSQLExistsClause("OR", sqlExistsMag);
		sqlExists.closeParenthesis();

		sql.addSQLExistsClause("AND", sqlExists);

		return sql;
	}

	public SQLBuilder selectNumerazioneOrdByClause(UserContext userContext, ParametriSelezioneMovimentiBulk parametri,
												   NumerazioneOrdHome numerazioneHome, NumerazioneOrdBulk numerazioneBulk,
												   CompoundFindClause compoundfindclause) throws PersistencyException{
		if (parametri == null || parametri.getUnitaOperativaOrdine() == null || parametri.getUnitaOperativaOrdine().getCdUnitaOperativa() == null ){
			throw new PersistencyException("Selezionare prima l'unità operativa");
		}
		SQLBuilder sql = numerazioneHome.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, parametri.getUnitaOperativaOrdine().getCdUnitaOperativa());
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
		return sql;
	}}



