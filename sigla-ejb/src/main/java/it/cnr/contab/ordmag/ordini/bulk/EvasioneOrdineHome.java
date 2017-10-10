/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import java.util.Collection;

import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagHome;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class EvasioneOrdineHome extends BulkHome {
	public EvasioneOrdineHome(Connection conn) {
		super(EvasioneOrdineBulk.class, conn);
	}
	public EvasioneOrdineHome(Connection conn, PersistentCache persistentCache) {
		super(EvasioneOrdineBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectUnitaOperativaOrdByClause(UserContext userContext, EvasioneOrdineBulk bulk, 
			UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);
		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.EVASIONE_ORDINE);
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());
		return sql;
	}
	public SQLBuilder selectNumerazioneMagByClause(UserContext userContext, EvasioneOrdineBulk bulk, 
			NumerazioneMagHome numerazioneHome, NumerazioneMagBulk numerazioneBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		if (bulk == null || bulk.getCdCds() == null){
			throw new PersistencyException("CDS non valorizzato");
		}
		AbilUtenteUopOperMagBulk abilMag = null;
		if (bulk.getCdUnitaOperativa() != null){
			AbilUtenteUopOperMagHome abilHome = (AbilUtenteUopOperMagHome)getHomeCache().getHome(AbilUtenteUopOperMagBulk.class);
			Collection lista = null;
			try {
				lista = abilHome.findMagazziniAbilitati(userContext, TipoOperazioneOrdBulk.EVASIONE_ORDINE, bulk.getUnitaOperativaAbilitata());
			} catch (PersistencyException | IntrospectionException e) {
				throw new PersistencyException(e);
			}
			if (lista == null || lista.size() == 0){
				throw new PersistencyException("Non esistono magazzini abilitati per la unità operativa scelta");
			} else if (lista.size() == 1){
				abilMag = (AbilUtenteUopOperMagBulk)lista.iterator().next();
			}
		} else {
			throw new PersistencyException("Valorizzare prima la unità operativa");
		}
		
		SQLBuilder sql = numerazioneHome.selectByClause(userContext, compoundfindclause);
		sql.addSQLClause("AND", "NUMERAZIONE_MAG.CD_CDS", SQLBuilder.EQUALS, bulk.getCdCds());
		sql.addSQLClause("AND", "NUMERAZIONE_MAG.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "NUMERAZIONE_MAG.CD_NUMERATORE_MAG", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.EVASIONE_ORDINE);
		if (abilMag != null){
			sql.addSQLClause("AND", "NUMERAZIONE_MAG.CD_MAGAZZINO", SQLBuilder.EQUALS, abilMag.getCdMagazzino());
		}
		return sql;
	}

}