/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class MovimentiMagHome extends BulkHome {
	public MovimentiMagHome(Connection conn) {
		super(MovimentiMagBulk.class, conn);
	}
	public MovimentiMagHome(Connection conn, PersistentCache persistentCache) {
		super(MovimentiMagBulk.class, conn, persistentCache);
	}
	public Long recuperoProgressivoMovimento(it.cnr.jada.UserContext userContext) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		return new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_MOVIMENTI_MAG").longValue());
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
		MovimentiMagBulk movimento = (MovimentiMagBulk)bulk;
		if (movimento.getPgMovimento() == null)
			movimento.setPgMovimento(recuperoProgressivoMovimento(userContext));
	}
	@Override
	public void insert(Persistent persistent, UserContext userContext) throws PersistencyException {
		MovimentiMagBulk movimentiMag = (MovimentiMagBulk)persistent;
    	LottoMagHome lottoHome = (LottoMagHome)getHomeCache().getHome(LottoMagBulk.class);
    	LottoMagBulk lotto;
		try {
			lotto = (LottoMagBulk)lottoHome.findAndLock(movimentiMag.getLottoMag());
		} catch (OutdatedResourceException | BusyResourceException e) {
			throw new PersistencyException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		}
		lotto = lottoHome.aggiornaValori(userContext, lotto, movimentiMag);
		lotto.setToBeUpdated();
		lottoHome.update(lotto, userContext);
		super.insert(persistent, userContext);
	}
	public java.util.List recuperoMovimentiDaLotto(Persistent persistent) throws IntrospectionException,PersistencyException {
		MovimentiMagBulk movimentoMag = (MovimentiMagBulk)persistent;
		SQLBuilder sql = createSQLBuilder();

		sql.addClause("AND","cdCds",SQLBuilder.EQUALS, movimentoMag.getCdCdsLotto());
		sql.addClause("AND","cdMagazzino",SQLBuilder.EQUALS, movimentoMag.getCdMagazzinoLotto());
		sql.addClause("AND","esercizio",SQLBuilder.EQUALS, movimentoMag.getEsercizioLotto());
		sql.addClause("AND","cdNumeratoreMag",SQLBuilder.EQUALS, movimentoMag.getCdNumeratoreLotto());
		sql.addClause("AND","stato",SQLBuilder.EQUALS, MovimentiMagBulk.STATO_INSERITO);
		sql.addClause("AND","pgLotto",SQLBuilder.EQUALS, movimentoMag.getPgLotto());
		return fetchAll(sql);

	}
}