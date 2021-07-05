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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;

import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagHome;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CHARToBooleanConverter;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.Orderable;
public class LottoMagHome extends BulkHome {
	public LottoMagHome(Connection conn) {
		super(LottoMagBulk.class, conn);
	}
	public LottoMagHome(Connection conn, PersistentCache persistentCache) {
		super(LottoMagBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,ApplicationException {
		try {
			NumeratoriOrdMagComponentSession progressiviSession = (NumeratoriOrdMagComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG_EJB_NumeratoriOrdMagComponentSession", NumeratoriOrdMagComponentSession.class);
			NumerazioneMagBulk numerazione = new NumerazioneMagBulk();
			LottoMagBulk lotto = (LottoMagBulk)bulk;
			numerazione.setCdCds(CNRUserContext.getCd_cds(userContext));
			numerazione.setCdMagazzino(lotto.getMagazzino().getCdMagazzino());
			numerazione.setEsercizio(lotto.getEsercizio());
			numerazione.setCdNumeratoreMag(NumerazioneMagBulk.NUMERAZIONE_LOTTO);
			lotto.setNumerazioneMag(numerazione);
			lotto.setPgLotto(progressiviSession.getNextPG(userContext, numerazione).intValue());
		}catch(Throwable e) {
			throw new PersistencyException(e);
		}
	}

	public LottoMagBulk aggiornaValori(it.cnr.jada.UserContext userContext, LottoMagBulk lotto, MovimentiMagBulk movimentoMag) throws PersistencyException {
		TipoMovimentoMagBulk tipoMovimento = new TipoMovimentoMagBulk(movimentoMag.getTipoMovimentoMag().getCdCds(), movimentoMag.getTipoMovimentoMag().getCdTipoMovimento());
		TipoMovimentoMagHome home = (TipoMovimentoMagHome) getHomeCache().getHome(TipoMovimentoMagBulk.class);
		tipoMovimento = (TipoMovimentoMagBulk)home.findByPrimaryKey(tipoMovimento);
		if (tipoMovimento != null){
			lotto.setLottoFornitore(tipoMovimento.getRiportaLottoFornitore() ? movimentoMag.getLottoFornitore() : null);
			lotto.setDtCarico(tipoMovimento.getAggDataUltimoCarico() ?  movimentoMag.getDtRiferimento() : lotto.getDtCarico());

			switch (tipoMovimento.getModAggQtaMagazzino()) {
				case TipoMovimentoMagBulk.AZIONE_AZZERA: lotto.setGiacenza(BigDecimal.ZERO);
					break;
				case TipoMovimentoMagBulk.AZIONE_SOMMA: lotto.setGiacenza(Utility.nvl(lotto.getGiacenza()).add(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOTTRAE: lotto.setGiacenza(Utility.nvl(lotto.getGiacenza()).subtract(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOSTITUISCE: lotto.setGiacenza(movimentoMag.getQuantita());
					break;
				default: break;
			}
			switch (tipoMovimento.getQtaCaricoLotto()) {
				case TipoMovimentoMagBulk.AZIONE_AZZERA: lotto.setQuantitaCarico(BigDecimal.ZERO);
					break;
				case TipoMovimentoMagBulk.AZIONE_SOMMA: lotto.setQuantitaCarico(Utility.nvl(lotto.getGiacenza()).add(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOTTRAE: lotto.setQuantitaCarico(Utility.nvl(lotto.getGiacenza()).subtract(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOSTITUISCE: lotto.setQuantitaCarico(movimentoMag.getQuantita());
					break;
				default: break;
			}
			switch (tipoMovimento.getModAggQtaValMagazzino()) {
				case TipoMovimentoMagBulk.AZIONE_AZZERA: lotto.setQuantitaValore(BigDecimal.ZERO);
					break;
				case TipoMovimentoMagBulk.AZIONE_SOMMA: lotto.setQuantitaValore(Utility.nvl(lotto.getQuantitaValore()).add(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOTTRAE: lotto.setQuantitaValore(Utility.nvl(lotto.getQuantitaValore()).subtract(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOSTITUISCE: lotto.setQuantitaValore(movimentoMag.getQuantita());
					break;
				default: break;
			}
			switch (tipoMovimento.getModAggValoreLotto()) {
				case TipoMovimentoMagBulk.AZIONE_AZZERA:
					lotto.setValoreUnitario(BigDecimal.ZERO);
					lotto.setCostoUnitario(BigDecimal.ZERO);
					break;
				case TipoMovimentoMagBulk.AZIONE_SOMMA:
					lotto.setValoreUnitario(Utility.nvl(lotto.getValoreUnitario()).add(movimentoMag.getPrezzoUnitario()));
					lotto.setCostoUnitario(Utility.nvl(lotto.getCostoUnitario()).add(movimentoMag.getPrezzoUnitario()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOTTRAE:
					lotto.setValoreUnitario(Utility.nvl(lotto.getValoreUnitario()).subtract(movimentoMag.getPrezzoUnitario()));
					lotto.setCostoUnitario(Utility.nvl(lotto.getCostoUnitario()).subtract(movimentoMag.getPrezzoUnitario()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOSTITUISCE:
					lotto.setValoreUnitario(movimentoMag.getPrezzoUnitario());
					lotto.setCostoUnitario(movimentoMag.getPrezzoUnitario());
					break;
				default: break;
			}

			switch (tipoMovimento.getModAggQtaInizioAnno()) {
				case TipoMovimentoMagBulk.AZIONE_AZZERA: lotto.setQuantitaInizioAnno(BigDecimal.ZERO);
					break;
				case TipoMovimentoMagBulk.AZIONE_SOMMA: lotto.setQuantitaInizioAnno(Utility.nvl(lotto.getQuantitaInizioAnno()).add(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOTTRAE: lotto.setQuantitaInizioAnno(Utility.nvl(lotto.getQuantitaInizioAnno()).subtract(movimentoMag.getQuantita()));
					break;
				case TipoMovimentoMagBulk.AZIONE_SOSTITUISCE: lotto.setQuantitaValore(movimentoMag.getQuantita());
					break;
				default: break;
			}
		}

		return lotto;
	}
	
	@SuppressWarnings("rawtypes")
	public List<LottoMagBulk> findLottiMagazzinoByClause( MovimentiMagazzinoRigaBulk movimentiMagazzinoRigaBulk ) throws PersistencyException 
	{
		SQLBuilder sql = this.createSQLBuilder();

		sql.addClause(FindClause.AND, "magazzino", SQLBuilder.EQUALS, movimentiMagazzinoRigaBulk.getMovimentiMagazzinoBulk().getMagazzinoAbilitato());
		sql.addClause(FindClause.AND, "beneServizio", SQLBuilder.EQUALS, movimentiMagazzinoRigaBulk.getBeneServizio());
		sql.addClause(FindClause.AND, "giacenza", SQLBuilder.GREATER, BigDecimal.ZERO);
		sql.addClause(FindClause.AND, "dtCarico", SQLBuilder.LESS_EQUALS, DateUtils.truncate(movimentiMagazzinoRigaBulk.getMovimentiMagazzinoBulk().getDataCompetenza()));

//		TipoMovimentoMagAzBulk tipoMovimentoAz = new TipoMovimentoMagAzBulk(movimentiMagazzinoRigaBulk.getMovimentiMagazzinoBulk().getTipoMovimentoMag().getCdCds(), movimentiMagazzinoRigaBulk.getMovimentiMagazzinoBulk().getTipoMovimentoMag().getCdTipoMovimento());
//		TipoMovimentoMagAzHome home = (TipoMovimentoMagAzHome)getHomeCache().getHome(TipoMovimentoMagAzBulk.class);
//		tipoMovimentoAz = (TipoMovimentoMagAzBulk)home.findByPrimaryKey(tipoMovimentoAz);

		TipoMovimentoMagBulk tipoMovimento=movimentiMagazzinoRigaBulk.getMovimentiMagazzinoBulk().getTipoMovimentoMag();
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, LottoMagBulk.STATO_INSERITO);
		if (tipoMovimento.getFlMovimentaLottiBloccati()) {
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, LottoMagBulk.STATO_SCADUTO);
			sql.addClause(FindClause.OR, "stato", SQLBuilder.EQUALS, LottoMagBulk.STATO_VERIFICA);
		}
		sql.closeParenthesis();
			
		sql.setOrderBy("dtCarico", Orderable.ORDER_ASC);

		List lista =  this.fetchAll(sql);
		return lista;
	}
	
}