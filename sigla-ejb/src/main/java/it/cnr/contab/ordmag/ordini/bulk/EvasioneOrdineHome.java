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
	
	public SQLBuilder selectUnitaOperativaAbilitataByClause(UserContext userContext, EvasioneOrdineBulk bulk, UnitaOperativaOrdHome home, 
			UnitaOperativaOrdBulk unitaOperativaBulk, CompoundFindClause compoundfindclause) throws PersistencyException{
		return home.selectUnitaOperativeAbilitateByClause(userContext, compoundfindclause, TipoOperazioneOrdBulk.EVASIONE_ORDINE);
	}

	public SQLBuilder selectNumerazioneMagByClause(UserContext userContext, EvasioneOrdineBulk bulk, 
			NumerazioneMagHome numerazioneHome, NumerazioneMagBulk numerazioneBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
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
			} else if (lista.size() == 1){
				abilMag = (AbilUtenteUopOperMagBulk)lista.iterator().next();
			}
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