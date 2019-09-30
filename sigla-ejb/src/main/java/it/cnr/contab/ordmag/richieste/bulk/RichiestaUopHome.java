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
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.sql.Connection;

import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class RichiestaUopHome extends BulkHome {
	public RichiestaUopHome(Connection conn) {
		super(RichiestaUopBulk.class, conn);
	}
	public RichiestaUopHome(Connection conn, PersistentCache persistentCache) {
		super(RichiestaUopBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOperativaOrdByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);
		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		if (richiestaBulk.getIsForApprovazione()){
			sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_VALIDAZIONE_RICHIESTA);
		} else {
			sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		}
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());

		return sql;
	}

	public void addConditionAbilUtenteUop(UserContext userContext, SQLBuilder sql) {
		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER");
		sql.openParenthesis("AND");
		sql.openParenthesis("AND");
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		sql.closeParenthesis();
		sql.openParenthesis("OR");
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_VALIDAZIONE_RICHIESTA);
		sql.openParenthesis("AND");
		sql.addSQLClause("OR", "RICHIESTA_UOP.STATO", SQLBuilder.EQUALS, RichiestaUopBulk.STATO_DEFINITIVA);
		sql.addSQLClause("OR", "RICHIESTA_UOP.STATO", SQLBuilder.EQUALS, RichiestaUopBulk.STATO_INVIATA_ORDINE);
		sql.closeParenthesis();
		sql.closeParenthesis();
		sql.closeParenthesis();
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());
	}

	public SQLBuilder selectNumerazioneOrdByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			NumerazioneOrdHome numerazioneHome, NumerazioneOrdBulk numerazioneBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		if (richiestaBulk == null || richiestaBulk.getCdUnitaOperativa() == null){
			throw new PersistencyException("Selezionare prima l'unità operativa");
		}
		SQLBuilder sql = numerazioneHome.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, richiestaBulk.getCdUnitaOperativa());
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		return sql;
	}

	public SQLBuilder selectUnitaOperativaOrdDestByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException, ApplicationException{
		SQLBuilder sql = unitaOperativaHome.createSQLBuilder();
		sql.addClause(compoundfindclause);

		sql.addTableToHeader("ASS_UNITA_OPERATIVA_ORD");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ASS_UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA_RIF");
		if (richiestaBulk == null || ((richiestaBulk.getNumerazioneOrd() == null|| richiestaBulk.getNumerazioneOrd().getCdUnitaOperativa() == null) && 
				(richiestaBulk.getUnitaOperativaOrd() == null|| richiestaBulk.getUnitaOperativaOrd().getCdUnitaOperativa() == null))){
			throw new ApplicationException("Selezionare prima l'unità operativa");
		}
		sql.addSQLClause("AND", "ASS_UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, richiestaBulk.getCdUnitaOperativa());

		return sql;
	}

	@Override
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return super.selectByClause(usercontext, compoundfindclause);
	}
	@Override
	public void delete(Persistent persistent, UserContext userContext) throws PersistencyException {
		((RichiestaUopBulk)persistent).setStato(RichiestaUopBulk.STATO_ANNULLATA);
		 super.update(persistent, userContext);
	}
}