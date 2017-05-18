/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.sql.Connection;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AssUnitaOperativaOrdBulk;
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
		filtraUO(userContext, sql, false);
		
		sql.addTableToHeader("ABIL_UTENTE_UOP_OPER");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());

		return sql;
	}

	public SQLBuilder selectNumerazioneOrdByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			NumerazioneOrdHome numerazioneHome, NumerazioneOrdBulk numerazioneBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = numerazioneHome.selectByClause(userContext, compoundfindclause);
		if (richiestaBulk == null || richiestaBulk.getCdUnitaOperativa() == null){
			throw new PersistencyException("Selezionare prima l'unità operativa");
		}
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, richiestaBulk.getCdUnitaOperativa());
		sql.addSQLClause("AND", "NUMERAZIONE_ORD.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		return sql;
	}

	public SQLBuilder selectUnitaOperativaOrdDestByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		SQLBuilder sql = unitaOperativaHome.selectByClause(userContext, compoundfindclause);

		sql.addTableToHeader("ASS_UNITA_OPERATIVA_ORD");
		sql.addSQLJoin("UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", "ASS_UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA_RIF");
		if (richiestaBulk == null || richiestaBulk.getCdUnitaOperativa() == null){
			throw new PersistencyException("Selezionare prima l'unità operativa");
		}
		sql.addSQLClause("AND", "ASS_UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, richiestaBulk.getCdUnitaOperativa());

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
}