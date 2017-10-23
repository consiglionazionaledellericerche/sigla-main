/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import java.util.Calendar;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class BollaScaricoMagHome extends BulkHome {
	public BollaScaricoMagHome(Connection conn) {
		super(BollaScaricoMagBulk.class, conn);
	}
	public BollaScaricoMagHome(Connection conn, PersistentCache persistentCache) {
		super(BollaScaricoMagBulk.class, conn, persistentCache);
	}

	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,ApplicationException {
		try {
			NumeratoriOrdMagComponentSession progressiviSession = (NumeratoriOrdMagComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG_EJB_NumeratoriOrdMagComponentSession", NumeratoriOrdMagComponentSession.class);
			NumerazioneMagBulk numerazione = new NumerazioneMagBulk();
			BollaScaricoMagBulk bolla = (BollaScaricoMagBulk)bulk;
			numerazione.setCdCds(CNRUserContext.getCd_cds(userContext));
			numerazione.setCdMagazzino(bolla.getMagazzino().getCdMagazzino());
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(bolla.getDtBollaSca().getTime());
			numerazione.setEsercizio(cal.get(Calendar.YEAR));
			numerazione.setCdNumeratoreMag(NumerazioneMagBulk.NUMERAZIONE_BOLLA_SCARICO);
			bolla.setNumerazioneMag(numerazione);
			bolla.setPgBollaSca(progressiviSession.getNextPG(userContext, numerazione).intValue());
		}catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}}