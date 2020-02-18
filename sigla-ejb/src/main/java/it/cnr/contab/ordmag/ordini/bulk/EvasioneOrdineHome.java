/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.ordmag.anag00.MagazzinoBulk;
import it.cnr.contab.ordmag.anag00.MagazzinoHome;
import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneMagHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class EvasioneOrdineHome extends BulkHome {
	private static final long serialVersionUID = 1L;

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

	public SQLBuilder selectMagazzinoAbilitatoByClause(UserContext userContext, EvasioneOrdineBulk bulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk,  
			CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
		SQLBuilder sqlMagazzini = magazzinoHome.selectMagazziniAbilitatiByClause(userContext, bulk.getUnitaOperativaAbilitata(), TipoOperazioneOrdBulk.EVASIONE_ORDINE, compoundfindclause);

		sqlMagazzini.addTableToHeader("NUMERAZIONE_MAG");
		sqlMagazzini.addSQLJoin("MAGAZZINO.CD_CDS", "NUMERAZIONE_MAG.CD_CDS");
		sqlMagazzini.addSQLJoin("MAGAZZINO.CD_MAGAZZINO", "NUMERAZIONE_MAG.CD_MAGAZZINO");
		sqlMagazzini.addSQLClause(FindClause.AND, "NUMERAZIONE_MAG.CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		sqlMagazzini.addSQLClause(FindClause.AND, "NUMERAZIONE_MAG.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		sqlMagazzini.addSQLClause(FindClause.AND, "NUMERAZIONE_MAG.CD_NUMERATORE_MAG", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.EVASIONE_ORDINE);

		return sqlMagazzini;
	}

	@SuppressWarnings("unchecked")
	public List<MagazzinoBulk> findMagazziniAbilitati(UserContext userContext, EvasioneOrdineBulk evasioneOrdine) throws PersistencyException, ComponentException {
		MagazzinoHome magazzinoHome = (MagazzinoHome)getHomeCache().getHome(MagazzinoBulk.class);
		return magazzinoHome.fetchAll(this.selectMagazzinoAbilitatoByClause(userContext, evasioneOrdine, magazzinoHome, new MagazzinoBulk(), new CompoundFindClause()));
	}	

	public NumerazioneMagBulk findNumerazioneMag(UserContext userContext, MagazzinoBulk magazzino) throws PersistencyException, ComponentException {
		NumerazioneMagHome numerazioneMagHome = (NumerazioneMagHome)getHomeCache().getHome(NumerazioneMagBulk.class);
		NumerazioneMagBulk numerazioneMag = new NumerazioneMagBulk(CNRUserContext.getCd_cds(userContext), 
				magazzino.getCdMagazzino(), CNRUserContext.getEsercizio(userContext), TipoOperazioneOrdBulk.EVASIONE_ORDINE);
		return (NumerazioneMagBulk)numerazioneMagHome.findByPrimaryKey(numerazioneMag);
	}	
}