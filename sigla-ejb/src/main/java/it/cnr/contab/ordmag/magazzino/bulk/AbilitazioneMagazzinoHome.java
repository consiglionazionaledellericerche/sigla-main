/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AbilitazioneMagazzinoHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public AbilitazioneMagazzinoHome(Class clazz, Connection conn) {
		super(clazz, conn);
	}
	
	public AbilitazioneMagazzinoHome(Class clazz, Connection conn, PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}

	public SQLBuilder selectUnitaOperativaAbilitataByClause(UserContext userContext, AbilitazioneMagazzinoBulk bulk, UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk, 
			CompoundFindClause compoundfindclause) throws PersistencyException{
		return unitaOperativaHome.selectUnitaOperativeAbilitateByClause(userContext, compoundfindclause, TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO);
	}
	
	public SQLBuilder selectMagazzinoAbilitatoByClause(UserContext userContext, AbilitazioneMagazzinoBulk bulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk,  
			CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
		return magazzinoHome.selectMagazziniAbilitatiByClause(userContext, bulk.getUnitaOperativaAbilitata(), TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO, compoundfindclause);
	}
}