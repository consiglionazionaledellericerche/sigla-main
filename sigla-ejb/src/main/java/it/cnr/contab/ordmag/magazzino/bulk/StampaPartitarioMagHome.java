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

package it.cnr.contab.ordmag.magazzino.bulk;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.ordmag.anag00.*;
import it.cnr.contab.ordmag.magazzino.dto.StampaPartitarioBeneServizioDTO;
import it.cnr.contab.ordmag.magazzino.dto.StampaPartitarioMagDTO;
import it.cnr.contab.ordmag.magazzino.dto.StampaPartitarioMovMagDTO;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.OrderConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Insert the type's description here.
 * @author: Mario Incarnato
 */
public class StampaPartitarioMagHome extends BulkHome {
	/**
	 * StampaPartitarioMagHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	private transient static final Logger _log = LoggerFactory.getLogger(StampaPartitarioMagHome.class);
	public StampaPartitarioMagHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * StampaPartitarioMagHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public StampaPartitarioMagHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public StampaPartitarioMagHome(java.sql.Connection conn) {
		super(StampaPartitarioMagBulk.class, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public StampaPartitarioMagHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(StampaPartitarioMagBulk.class, conn, persistentCache);
	}

	private static final String COD_MAGAZZINO = "cdMagazzino";
	private static final String UOP = "uop";
	private static final String DA_FORNITORE = "daCdFornitore";
	private static final String A_FORNITORE = "aCdFornitore";
	private static final String DA_BENE_SERVIZIO = "daCdBeneServizio";
	private static final String A_BENE_SERVIZIO = "aCdBeneServizio";
	private static final String DA_DATA_MOVIMENTO = "daDataMovimento";
	private static final String A_DATA_MOVIMENTO = "aDataMovimento";
	private static final String DA_DATA_COMPETENZA = "daDataCompetenza";
	private static final String A_DATA_COMPETENZA = "aDataCompetenza";

	public SQLBuilder selectUnitaOperativaAbilitataByClause(UserContext userContext, StampaPartitarioMagBulk bulk, UnitaOperativaOrdHome unitaOperativaHome, UnitaOperativaOrdBulk unitaOperativaBulk,
															CompoundFindClause compoundfindclause) throws PersistencyException{
		return unitaOperativaHome.selectUnitaOperativeAbilitateByClause(userContext, compoundfindclause, TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO);
	}

	public SQLBuilder selectMagazzinoAbilitatoByClause(UserContext userContext, StampaPartitarioMagBulk bulk, MagazzinoHome magazzinoHome, MagazzinoBulk magazzinoBulk,
													   CompoundFindClause compoundfindclause) throws PersistencyException, ComponentException {
		return magazzinoHome.selectMagazziniAbilitatiByClause(userContext, bulk.getUnitaOperativaAbilitata(), TipoOperazioneOrdBulk.OPERAZIONE_MAGAZZINO, compoundfindclause);
	}
	public String createJsonForPrint(Object object) throws ComponentException {
		ObjectMapper mapper = new ObjectMapper();
		String myJson = null;
		try {
			myJson = mapper.writeValueAsString(object);
		} catch (Exception ex) {
			throw new ComponentException("Errore nella generazione del file JSON per l'esecuzione della stampa ( errore joson).",ex);
		}
		return myJson;
	}

	public String getJsonDataSource(UserContext uc, Print_spoolerBulk print_spoolerBulk) throws ComponentException {

		String json=null;
		StampaPartitarioMagDTO stampaPartitarioMag = new StampaPartitarioMagDTO();
		try {

			LinkedHashSet<StampaPartitarioBeneServizioDTO> articoliSet = new LinkedHashSet<StampaPartitarioBeneServizioDTO>();
			List<MovimentiMagBulk> movMag = findMovimentiMagForJson(uc, print_spoolerBulk);

			for (MovimentiMagBulk m:movMag) {

				StampaPartitarioBeneServizioDTO art = new StampaPartitarioBeneServizioDTO();
				art.setCodiceBeneServizio(m.getLottoMag().getBeneServizio().getCd_bene_servizio());
				art.setDescrBeneServizio(m.getLottoMag().getBeneServizio().getDs_bene_servizio());
				art.setCodiceMagazzino(m.getLottoMag().getMagazzino().getCdMagazzino());
				art.setCodiceCds(m.getLottoMag().getMagazzino().getCdCds());
				art.setCodiceUnitaMisura(m.getCdUnitaMisura());
				art.setDescrUnitaMisura(m.getUnitaMisura().getDsUnitaMisura());
				art.setCodiceDivisa(m.getCdDivisa());
				articoliSet.add(art);

			}

			List<StampaPartitarioBeneServizioDTO> articoli = articoliSet.stream().collect(Collectors.toList());
			articoli = articoli.stream().sorted(Comparator.comparing(StampaPartitarioBeneServizioDTO::getCodiceBeneServizio)).collect(Collectors.toList());
			stampaPartitarioMag.setBeniServizio(articoli);

			for (MovimentiMagBulk m:movMag) {

				StampaPartitarioMovMagDTO mov = new StampaPartitarioMovMagDTO();
				mov.setPgMovimento(m.getPgMovimento());
				mov.setDataMovimento(Optional.ofNullable(m.getDtMovimento())
						.map(s -> new SimpleDateFormat("dd/MM/yyyy").format(s))
						.orElse(""));
				mov.setCausaleMovimento(m.getTipoMovimentoMag().getDsTipoMovimento());
				mov.setOrigine(m.getLottoMag().getMagazzino().getCdMagazzino()+"-"+m.getLottoMag().getMagazzino().getDsMagazzino());
				mov.setDescrizione(Optional.ofNullable(m.getBollaScaricoMag())
						.map(s -> s.getMagazzino().getCdMagazzino()+"-"+s.getMagazzino().getDsMagazzino())
						.orElse(""));
				mov.setDataCompetenza(Optional.ofNullable(m.getDataBolla())
						.map(s -> new SimpleDateFormat("dd/MM/yyyy").format(s))
						.orElse(""));
				mov.setBolla(Optional.ofNullable(m.getNumeroBolla())
							.orElse("")
						+Optional.of(Optional.ofNullable(m.getDataBolla())
							.map(s -> " - "+new SimpleDateFormat("dd/MM/yyyy").format(s))
							.orElse("")).get());

				if (m.getLottoMag().getCostoUnitario()!=null && m.getQuantita()!=null && m.getCoeffConv()!=null)
					mov.setImporto(m.getLottoMag().getCostoUnitario().multiply(m.getQuantita().subtract(m.getCoeffConv())));

				if(m.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOTTRAE)){
					mov.setUscita(m.getQuantita());
				}
				if(m.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOMMA)){
					mov.setEntrata(m.getQuantita());
				}
				mov.setGiacenza(m.getLottoMag().getGiacenza());

				for (StampaPartitarioBeneServizioDTO n:articoli) {
					if (Optional.ofNullable(n.getCodiceBeneServizio()).equals(Optional.ofNullable(m.getLottoMag().getBeneServizio().getCd_bene_servizio())) &&
						Optional.ofNullable(n.getCodiceUnitaMisura()).equals(Optional.ofNullable(m.getCdUnitaMisura())) &&
						Optional.ofNullable(n.getCodiceDivisa()).equals(Optional.ofNullable(m.getCdDivisa()))) {
							if (n.getMovimenti()==null)
								n.setMovimenti(new ArrayList<StampaPartitarioMovMagDTO>());
							n.getMovimenti().add(mov);
							continue;
					}
				}
			}

			List<MovimentiMagBulk> movMagGiacenza = findMovimentiMagForJson(uc, print_spoolerBulk, true);

			for (StampaPartitarioBeneServizioDTO invDto : articoli){
				for(MovimentiMagBulk movimento : movMagGiacenza){
					if(invDto.getCodiceBeneServizio().equals(movimento.getLottoMag().getCdBeneServizio()) &&
						invDto.getCodiceMagazzino().equals(movimento.getLottoMag().getMagazzino().getCdMagazzino()) &&
						invDto.getCodiceCds().equals(movimento.getLottoMag().getMagazzino().getCdCds()))
					{
						if(movimento.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOTTRAE)){
							invDto.setGiacenza(invDto.getGiacenza()!=null ? invDto.getGiacenza().subtract(movimento.getQuantita()) : BigDecimal.ZERO);
						}
						if(movimento.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOMMA)){
							invDto.setGiacenza(invDto.getGiacenza()!=null ? invDto.getGiacenza().add(movimento.getQuantita()) : BigDecimal.ZERO);
						}

					}
				}
			}

		} catch (PersistencyException e) {
			e.printStackTrace();
			throw new ComponentException("Errore nella ricerca dei dati per la generazione del file JSON per l'esecuzione della stampa (errore json).",e);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new ComponentException("Errore nella produzione della stampa.",e);
		}

		try {
			json=createJsonForPrint(stampaPartitarioMag);
		} catch (ComponentException e) {
			e.printStackTrace();
			throw e;
		}
		return json;
	}

	private List<MovimentiMagBulk> findMovimentiMagForJson(UserContext userContext, Print_spoolerBulk print_spoolerBulk) throws PersistencyException, ParseException {
		return findMovimentiMagForJson(userContext, print_spoolerBulk, false);
	}

	private List<MovimentiMagBulk> findMovimentiMagForJson(UserContext userContext, Print_spoolerBulk print_spoolerBulk, boolean perGiacenza) throws PersistencyException, ParseException {
		BulkList<Print_spooler_paramBulk> params= print_spoolerBulk.getParams();
		Print_spooler_paramBulk uopParam=params.stream().
				filter(e->e.getNomeParam().equals(UOP)).findFirst().get();

		Print_spooler_paramBulk codMagazzinoParam=params.stream().
				filter(e->e.getNomeParam().equals(COD_MAGAZZINO)).findFirst().get();

		Print_spooler_paramBulk daFornitoreParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_FORNITORE)).findFirst().get();

		Print_spooler_paramBulk aFornitoreParam=params.stream().
				filter(e->e.getNomeParam().equals(A_FORNITORE)).findFirst().get();

		Print_spooler_paramBulk daBeneServizioParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_BENE_SERVIZIO)).findFirst().get();

		Print_spooler_paramBulk aBeneServizioParam=params.stream().
				filter(e->e.getNomeParam().equals(A_BENE_SERVIZIO)).findFirst().get();

		Print_spooler_paramBulk daDataMovimentoParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_DATA_MOVIMENTO)).findFirst().get();

		Print_spooler_paramBulk aDataMovimentoParam=params.stream().
				filter(e->e.getNomeParam().equals(A_DATA_MOVIMENTO)).findFirst().get();

		Print_spooler_paramBulk daDataCompetenzaParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_DATA_COMPETENZA)).findFirst().get();

		Print_spooler_paramBulk aDataCompetenzaParam=params.stream().
				filter(e->e.getNomeParam().equals(A_DATA_COMPETENZA)).findFirst().get();

		String uop = null;
		if(uopParam != null && !"*".equals(uopParam.getValoreParam())){
			uop = uopParam.getValoreParam();
		}
		String codMag = null;
		if(codMagazzinoParam != null && !"*".equals(codMagazzinoParam.getValoreParam())){
			codMag = codMagazzinoParam.getValoreParam();
		}
		Integer daFornitore = null;
		if(daFornitoreParam != null && !"*".equals(daFornitoreParam.getValoreParam())){
			daFornitore = Integer.valueOf(daFornitoreParam.getValoreParam());
		}
		Integer aFornitore = null;
		if(aFornitoreParam != null && !"*".equals(aFornitoreParam.getValoreParam())){
			 aFornitore = Integer.valueOf(aFornitoreParam.getValoreParam());
		}
		String daBeneServizio = null;
		if(daBeneServizioParam != null && !"*".equals(daBeneServizioParam.getValoreParam())){
			daBeneServizio = daBeneServizioParam.getValoreParam();
		}
		String aBeneServizio = null;
		if(aBeneServizioParam != null && !"*".equals(aBeneServizioParam.getValoreParam())){
			aBeneServizio = aBeneServizioParam.getValoreParam();
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		Date daDataMovimento = null;
		try {
			if (daDataMovimentoParam != null && !"*".equals(daDataMovimentoParam.getValoreParam()))
				daDataMovimento = dateFormatter.parse(daDataMovimentoParam.getValoreParam());
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		Date aDataMovimento = null;
		try {
			if (aDataMovimentoParam != null && !"*".equals(aDataMovimentoParam.getValoreParam()))
				aDataMovimento = dateFormatter.parse(aDataMovimentoParam.getValoreParam());
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		Date daDataCompetenza = null;
		try {
			if (daDataCompetenzaParam != null && !"*".equals(daDataCompetenzaParam.getValoreParam()))
				daDataCompetenza = dateFormatter.parse(daDataCompetenzaParam.getValoreParam());
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}
		Date aDataCompetenza = null;
		try {
			if (aDataCompetenzaParam != null && !"*".equals(aDataCompetenzaParam.getValoreParam()))
				aDataCompetenza = dateFormatter.parse(aDataCompetenzaParam.getValoreParam());
		} catch (ParseException e) {
			e.printStackTrace();
			throw e;
		}

		MovimentiMagHome movimentiMagHome = (MovimentiMagHome) getHomeCache().getHome(MovimentiMagBulk.class);
		SQLBuilder sql = movimentiMagHome.createSQLBuilder();
		sql.addColumn("BENE_SERVIZIO.CD_BENE_SERVIZIO");
		sql.addColumn("BENE_SERVIZIO.DS_BENE_SERVIZIO");
		sql.addColumn("BENE_SERVIZIO.UNITA_MISURA");
		sql.addColumn("MOVIMENTI_MAG.CD_DIVISA");
		sql.addColumn("MOVIMENTI_MAG.CD_UNITA_MISURA");
		sql.generateJoin(MovimentiMagBulk.class, TipoMovimentoMagBulk.class, "tipoMovimentoMag", "TIPO_MOVIMENTO_MAG");
		sql.generateJoin(MovimentiMagBulk.class, LottoMagBulk.class, "lottoMag", "LOTTO_MAG");
		sql.generateJoin(LottoMagBulk.class, MagazzinoBulk.class, "magazzino", "MAGAZZINO");
		sql.generateJoin(LottoMagBulk.class, Bene_servizioBulk.class, "beneServizio", "BENE_SERVIZIO");
		sql.addTableToHeader("bolla_scarico_riga_mag","bs");
		sql.addSQLJoin("movimenti_mag.pg_movimento","bs.pg_movimento (+)");
		sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.STATO",SQLBuilder.EQUALS, MovimentiMagBulk.STATO_INSERITO);
		if (uop!=null)
			sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.CD_UOP",SQLBuilder.EQUALS, uop);
		if (codMag!=null)
			sql.addSQLClause(FindClause.AND,"LOTTO_MAG.CD_MAGAZZINO",SQLBuilder.EQUALS, codMag);
		if (daBeneServizio!=null)
			sql.addSQLClause(FindClause.AND,"LOTTO_MAG.CD_BENE_SERVIZIO",SQLBuilder.GREATER_EQUALS, daBeneServizio);
		if (aBeneServizio!=null)
			sql.addSQLClause(FindClause.AND,"LOTTO_MAG.CD_BENE_SERVIZIO",SQLBuilder.EQUALS, aBeneServizio);
		if (daFornitore!=null)
			sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.CD_TERZO",SQLBuilder.GREATER_EQUALS, daFornitore);
		if (aFornitore!=null)
			sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.CD_TERZO",SQLBuilder.LESS_EQUALS, aFornitore);
		if (!perGiacenza) {
			if (daDataMovimento != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_MOVIMENTO", SQLBuilder.GREATER_EQUALS, new Timestamp(daDataMovimento.getTime()));
			if (aDataMovimento != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_MOVIMENTO", SQLBuilder.LESS_EQUALS, new Timestamp(aDataMovimento.getTime()));
			if (daDataCompetenza != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_RIFERIMENTO", SQLBuilder.GREATER_EQUALS, new Timestamp(daDataCompetenza.getTime()));
			if (aDataCompetenza != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_RIFERIMENTO", SQLBuilder.LESS_EQUALS, new Timestamp(aDataCompetenza.getTime()));
		} else {
			if (daDataMovimento != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_MOVIMENTO", SQLBuilder.LESS, new Timestamp(daDataMovimento.getTime()));
			if (daDataCompetenza != null)
				sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_RIFERIMENTO", SQLBuilder.LESS, new Timestamp(daDataCompetenza.getTime()));
		}
		sql.setOrderBy("movimenti_mag.pg_movimento", OrderConstants.ORDER_ASC);

		List<MovimentiMagBulk> result = movimentiMagHome.fetchAll(sql);
		getHomeCache().fetchAll(userContext);
		return result;
	}

}
