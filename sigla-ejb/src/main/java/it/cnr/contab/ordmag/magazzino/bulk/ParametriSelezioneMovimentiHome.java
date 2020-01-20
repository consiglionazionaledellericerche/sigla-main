/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import java.util.Optional;

import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperHome;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class ParametriSelezioneMovimentiHome extends AbilitazioneMagazzinoHome {
	private static final long serialVersionUID = 1L;

	public ParametriSelezioneMovimentiHome(Connection conn) {
		super(ParametriSelezioneMovimentiBulk.class, conn);
	}
	
	public ParametriSelezioneMovimentiHome(Connection conn, PersistentCache persistentCache) {
		super(ParametriSelezioneMovimentiBulk.class, conn, persistentCache);
	}

	public SQLBuilder selectTipoMovimentoMagByClause(UserContext userContext, ParametriSelezioneMovimentiBulk bulk, TipoMovimentoMagHome tipoMovimentoMagHome, TipoMovimentoMagBulk tipoMovimentoMagBulk,  
			CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
		CompoundFindClause clause = new CompoundFindClause();
		Optional.ofNullable(compoundfindclause).ifPresent(cfc->clause.addChild(cfc)); 
		clause.addClause(FindClause.AND, "tipo", SQLBuilder.NOT_EQUALS, TipoMovimentoMagBulk.STORNI);
		SQLBuilder sqlBuilder = tipoMovimentoMagHome.selectByClause(clause);
		
//		TipoMovimentoMagAzHome tipoAzhome = (TipoMovimentoMagAzHome)getHomeCache().getHome(TipoMovimentoMagAzBulk.class);
//		SQLBuilder sqlExists = tipoAzhome.createSQLBuilder();
//		sqlExists.addSQLJoin("TIPO_MOVIMENTO_MAG_AZ.CD_CDS", SQLBuilder.EQUALS, "TIPO_MOVIMENTO_MAG.CD_CDS");
//		sqlExists.addSQLJoin("TIPO_MOVIMENTO_MAG_AZ.CD_TIPO_MOVIMENTO", SQLBuilder.EQUALS, "TIPO_MOVIMENTO_MAG.CD_TIPO_MOVIMENTO");
//
//		sqlBuilder.addSQLExistsClause(FindClause.AND, sqlExists);
		return sqlBuilder;
	}

	public SQLBuilder selectUnitaOperativaOrdineByClause(UserContext userContext, ParametriSelezioneMovimentiBulk parametri, 
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