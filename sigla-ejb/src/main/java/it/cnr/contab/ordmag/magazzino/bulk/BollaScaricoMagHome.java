/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
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
	}

	public SQLBuilder selectBolleGenerate(List<BollaScaricoMagBulk> bolle) {

		SQLBuilder sql = createSQLBuilder();

		BollaScaricoMagBulk bollaScaricoMagBulk = bolle.get(0);

		sql.addSQLClause("AND", "BOLLA_SCARICO_MAG.ESERCIZIO", SQLBuilder.EQUALS, bollaScaricoMagBulk.getEsercizio());
		sql.addSQLClause("AND", "BOLLA_SCARICO_MAG.CD_CDS", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdCds());
		sql.addSQLClause("AND", "BOLLA_SCARICO_MAG.CD_NUMERATORE_MAG", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdNumeratoreMag());
		sql.addSQLClause("AND", "BOLLA_SCARICO_MAG.CD_MAGAZZINO", SQLBuilder.EQUALS, bollaScaricoMagBulk.getCdMagazzino());

		sql.openParenthesis("AND");
		for (BollaScaricoMagBulk bolla : bolle){
			sql.addSQLClause("OR", "PG_BOLLA_SCA", SQLBuilder.EQUALS, bolla.getPgBollaSca());
		}
		sql.closeParenthesis();

		return sql;
	}

	@Override
	public Persistent completeBulkRowByRow(UserContext userContext,
			Persistent persistent) throws PersistencyException {
		BollaScaricoMagBulk bolla = (BollaScaricoMagBulk)persistent;
		bolla.setStampaBollaScarico("<button class='Button' style='width:60px;' onclick='cancelBubble(event); if (disableDblClick()) "+
				"doStampaBollaScarico("+bolla.getEsercizio()+",\""+bolla.getCdCds()+"\",\""+bolla.getCdMagazzino()+"\","+bolla.getCdNumeratoreMag()+"\","+bolla.getPgBollaSca()+",\""+Filtro_ricerca_doc_ammVBulk.DOC_ATT_GRUOP+"\"); return false' "+
				"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
				"title='Visualizza Documenti Collegati'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
		return super.completeBulkRowByRow(userContext, persistent);
		}
}