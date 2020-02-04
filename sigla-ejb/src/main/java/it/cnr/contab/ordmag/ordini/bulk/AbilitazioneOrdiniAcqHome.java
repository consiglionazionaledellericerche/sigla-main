/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;

import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.ordmag.magazzino.bulk.AbilitazioneMagazzinoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;

public class AbilitazioneOrdiniAcqHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public AbilitazioneOrdiniAcqHome(Class clazz, Connection conn) {
		super(clazz, conn);
	}

	public AbilitazioneOrdiniAcqHome(Class clazz, Connection conn, PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOperativaAbilitataByClause(UserContext userContext, AbilitazioneOrdiniAcqBulk bulk, UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk,
			CompoundFindClause compoundfindclause) throws PersistencyException{
		return unitaOperativaHome.selectUnitaOperativeAbilitateByClause(userContext, compoundfindclause, TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO);
	}

	public SQLBuilder selectMagazzinoAbilitatoByClause(UserContext userContext, AbilitazioneOrdiniAcqBulk bulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk,
			CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
		return magazzinoHome.selectMagazziniAbilitatiByClause(userContext, bulk.getUnitaOperativaAbilitata(), TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO, compoundfindclause);
	}
}