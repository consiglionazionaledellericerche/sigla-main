/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/05/2017
 */
package it.cnr.contab.ordmag.richieste.bulk;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
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
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
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
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
		sql.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());

		return sql;
	}

	public SQLBuilder selectNumerazioneOrdByClause(UserContext userContext, RichiestaUopBulk richiestaBulk, 
			NumerazioneOrdHome numerazioneHome, NumerazioneOrdBulk numerazioneBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		if (richiestaBulk == null || richiestaBulk.getCdUnitaOperativa() == null){
			throw new PersistencyException("Selezionare prima l'unità operativa");
		}
		SQLBuilder sql = numerazioneHome.selectByClause(userContext, compoundfindclause);
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

	public void inizializzaBulk(UserContext usercontext, OggettoBulk oggettobulk)
			throws PersistencyException, ComponentException {
		super.initializeBulkForInsert(usercontext, oggettobulk);
		RichiestaUopBulk richiesta = (RichiestaUopBulk)oggettobulk;
		if (richiesta.getCdUnitaOperativa() == null){
			UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome)getHomeCache().getHome(UnitaOperativaOrdBulk.class);
			SQLBuilder sql = selectUnitaOperativaOrdByClause(usercontext, richiesta, uopHome, new UnitaOperativaOrdBulk(), new CompoundFindClause());
			List listUop=uopHome.fetchAll(sql);
			if (listUop != null && listUop.size() == 1){
				richiesta.setUnitaOperativaOrd((UnitaOperativaOrdBulk)listUop.get(0));
			}
		}
	}
}