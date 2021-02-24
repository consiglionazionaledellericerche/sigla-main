/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
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
			NumeratoriOrdMagComponentSession progressiviSession = Utility.createNumeratoriOrdMagComponentSession();
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
		}catch(Exception e) {
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
				"doStampaBollaScarico("+bolla.getEsercizio()+",\""+bolla.getCdCds()+"\",\""+bolla.getCdMagazzino()+"\","+bolla.getCdNumeratoreMag()+"\","+bolla.getPgBollaSca()+"\"); return false' "+
				"onMouseOver='mouseOver(this)' onMouseOut='mouseOut(this)' onMouseDown='mouseDown(this)' onMouseUp='mouseUp(this)' "+
				"title='Visualizza Documenti Collegati'><img align='middle' class='Button' src='img/application-pdf.png'></button>");
		return super.completeBulkRowByRow(userContext, persistent);
	}
	
	@SuppressWarnings("unchecked")
	public java.util.List<BollaScaricoRigaMagBulk> findBollaScaricoRigaMagList( BollaScaricoMagBulk bollaScaricoMag ) throws IntrospectionException,PersistencyException {
		PersistentHome rigaHome = getHomeCache().getHome(BollaScaricoRigaMagBulk.class);
		SQLBuilder sql = rigaHome.createSQLBuilder();
		sql.addClause(FindClause.AND,"cdCds",SQLBuilder.EQUALS, bollaScaricoMag.getCdCds());
		sql.addClause(FindClause.AND,"cdMagazzino",SQLBuilder.EQUALS, bollaScaricoMag.getCdMagazzino());
		sql.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS, bollaScaricoMag.getEsercizio());
		sql.addClause(FindClause.AND,"cdNumeratoreMag",SQLBuilder.EQUALS, bollaScaricoMag.getCdNumeratoreMag());
		sql.addClause(FindClause.AND,"pgBollaSca",SQLBuilder.EQUALS, bollaScaricoMag.getPgBollaSca());
		sql.addOrderBy("riga_Bolla_Sca");
		return rigaHome.fetchAll(sql);
	}	
}