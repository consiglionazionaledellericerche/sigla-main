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
import it.cnr.contab.ordmag.anag00.TipoMovimentoMagBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.magazzino.dto.StampaConsumiDTO;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public class Stampa_consumiHome extends BulkHome {
	private static final String DATA_MOVIMENTO_DA="daDataMovimento";
	private static final String DATA_MOVIMENTO_A="aDataMovimento";
	private static final String DATA_RIFERIMENTO="dataRiferimento";
	private static final String UNITA_OP_DA="cdDaUop";
	private static final String UNITA_OP_A="cdAUop";
	private static final String MAGAZZINO_CDS="cdsMagazzino";
	private static final String MAGAZZINO_CODICE="cdMagazzino";
	private static final String CAT_GRUPPO_DA="cdDaCatGrp";
	private static final String CAT_GRUPPO_A="cdACatGrp";
	private static final String ARTICOLO_DA="cdDaArticolo";
	private static final String ARTICOLO_A="cdAArticolo";






	/**
	 * Stampa_consumiHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	public Stampa_consumiHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_consumiHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public Stampa_consumiHome(java.sql.Connection conn) {
		super(Stampa_consumiBulk.class, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_consumiHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Stampa_consumiBulk.class, conn, persistentCache);
	}
	public String getJsonDataSource(UserContext uc, Print_spoolerBulk print_spoolerBulk){
		BulkList<Print_spooler_paramBulk> params= print_spoolerBulk.getParams();
		Print_spooler_paramBulk dataMovimentoDaParam=params.stream().
				filter(e->e.getNomeParam().equals(DATA_MOVIMENTO_DA)).findFirst().get();
		Print_spooler_paramBulk dataMovimentoAParam=params.stream().
				filter(e->e.getNomeParam().equals(DATA_MOVIMENTO_A)).findFirst().get();
		Print_spooler_paramBulk dataRiferimentoParam=params.stream().
				filter(e->e.getNomeParam().equals(DATA_RIFERIMENTO)).findFirst().get();
		Print_spooler_paramBulk unitaOpDaParam=params.stream().
				filter(e->e.getNomeParam().equals(UNITA_OP_DA)).findFirst().get();
		Print_spooler_paramBulk unitaOpAParam=params.stream().
				filter(e->e.getNomeParam().equals(UNITA_OP_A)).findFirst().get();
		Print_spooler_paramBulk magazzinoCdsParam=params.stream().
				filter(e->e.getNomeParam().equals(MAGAZZINO_CDS)).findFirst().get();
		Print_spooler_paramBulk magazzinoCodParam=params.stream().
				filter(e->e.getNomeParam().equals(MAGAZZINO_CODICE)).findFirst().get();
		Print_spooler_paramBulk catGruppoDaParam=params.stream().
				filter(e->e.getNomeParam().equals(CAT_GRUPPO_DA)).findFirst().get();
		Print_spooler_paramBulk catGruppoAParam=params.stream().
				filter(e->e.getNomeParam().equals(CAT_GRUPPO_A)).findFirst().get();
		Print_spooler_paramBulk articoloDaParam=params.stream().
				filter(e->e.getNomeParam().equals(ARTICOLO_DA)).findFirst().get();
		Print_spooler_paramBulk articoloAParam=params.stream().
				filter(e->e.getNomeParam().equals(ARTICOLO_A)).findFirst().get();

		Date dataMovimentoDa = null;
		Date dataMovimentoA = null;
		Date dataRiferimento = null;

		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		try {
			dataMovimentoDa =dateFormatter.parse(dataMovimentoDaParam.getValoreParam());
			dataMovimentoA =dateFormatter.parse(dataMovimentoAParam.getValoreParam());
			dataRiferimento =dateFormatter.parse(dataRiferimentoParam.getValoreParam());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<StampaConsumiDTO> consumiDto = new ArrayList<StampaConsumiDTO>();
		try {
			MovimentiMagHome movimentoMagHome = (MovimentiMagHome) getHomeCache().getHome(MovimentiMagBulk.class);
			SQLBuilder sql = movimentoMagHome.createSQLBuilder();
			sql.generateJoin(MovimentiMagBulk.class, TipoMovimentoMagBulk.class, "tipoMovimentoMag","TIPO_MOVIMENTO_MAG");
			// stato movimento = STATO_INSERITO (INS)
			sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.STATO", SQLBuilder.EQUALS, MovimentiMagBulk.STATO_INSERITO);
			// data movimento compresa tra
			sql.addSQLClause("AND", "MOVIMENTI_MAG.DT_MOVIMENTO BETWEEN ? AND ?");
			if(dataMovimentoDa.compareTo(dataMovimentoA) !=0) {
					// data movimento compresa tra quelle in input (DA/A)
					sql.addParameter(new Timestamp(dataMovimentoDa.getTime()), Types.DATE, 0);
					sql.addParameter(new Timestamp(dataMovimentoA.getTime()), Types.DATE, 1);
			}else{
					//data movimento uguale a quella in input (fatta between perchè l'uguaglianza viene fatta sul timestamp dunque considera anche l'orario 00:00:00)
					cal.setTime(dataMovimentoDa);
					cal.add(Calendar.DATE, 1); //minus number would decrement the days
					Date giornoDopoDataMov =  cal.getTime();

					sql.addParameter(new Timestamp(dataMovimentoDa.getTime()), Types.DATE, 0);
					sql.addParameter(new Timestamp(giornoDopoDataMov.getTime()), Types.DATE, 1);
			}
			//data riferimento uguale a quella in input (fatta between perchè l'uguaglianza viene fatta sul timestamp dunque considera anche l'orario 00:00:00)
			cal.setTime(dataRiferimento);
			cal.add(Calendar.DATE, 1); //minus number would decrement the days
			Date giornoDopoDataRif =  cal.getTime();

			sql.addSQLClause("AND", "MOVIMENTI_MAG.DT_RIFERIMENTO BETWEEN ? AND ?");
			sql.addParameter(new Timestamp(dataRiferimento.getTime()), Types.DATE, 0);
			sql.addParameter(new Timestamp(giornoDopoDataRif.getTime()), Types.DATE, 1);
			//sql.addSQLClause(FindClause.AND, "MOVIMENTI_MAG.DT_RIFERIMENTO", SQLBuilder.GREATER_EQUALS, new Timestamp(dataRiferimento.getTime()));
			// tipo movimento = Y
			sql.addSQLClause(FindClause.AND, "TIPO_MOVIMENTO_MAG.FL_CONSUMO", SQLBuilder.EQUALS, "Y");
			sql.generateJoin(MovimentiMagBulk.class, LottoMagBulk.class, "lottoMag", "LOTTO_MAG");
			sql.addTableToHeader("BENE_SERVIZIO", "BENE_SERVIZIO");
			sql.addSQLJoin("BENE_SERVIZIO.CD_BENE_SERVIZIO", "LOTTO_MAG.CD_BENE_SERVIZIO");

			if (!magazzinoCodParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI)) {
				// codice magazzino uguale a quello in input
				sql.addSQLClause(FindClause.AND, "LOTTO_MAG.CD_MAGAZZINO_MAG", SQLBuilder.EQUALS, magazzinoCodParam.getValoreParam());
			}
			if (!unitaOpDaParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI) && !unitaOpAParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI)) {
				// uo compresa tra
				sql.generateJoin(MovimentiMagBulk.class, UnitaOperativaOrdBulk.class, "unitaOperativaOrd", "UNITA_OPERATIVA_ORD");
				if(!unitaOpDaParam.getValoreParam().equals(unitaOpAParam.getValoreParam())) {
					// l'UO compresa tra quelle in input
					sql.addSQLClause("AND", "UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA BETWEEN ? AND ?");
					sql.addParameter(unitaOpDaParam.getValoreParam(), Types.VARCHAR, 0);
					sql.addParameter(unitaOpAParam.getValoreParam(), Types.VARCHAR, 1);
				}else{
					sql.addSQLClause(FindClause.AND, "UNITA_OPERATIVA_ORD.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, unitaOpDaParam.getValoreParam());
				}
			}

			if (!catGruppoDaParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI) && !catGruppoAParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI)) {
				// categoria gruppo compresa tra
				sql.addTableToHeader("CATEGORIA_GRUPPO_INVENT", "CATEGORIA_GRUPPO_INVENT");
				sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO", "BENE_SERVIZIO.CD_CATEGORIA_GRUPPO");

				if(!catGruppoDaParam.getValoreParam().equals(catGruppoAParam.getValoreParam())) {
					//categoria gruppo compresa tra quelle in input
					sql.addSQLClause("AND", "CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO BETWEEN ? AND ?");
					sql.addParameter(catGruppoDaParam.getValoreParam(), Types.VARCHAR, 0);
					sql.addParameter(catGruppoAParam.getValoreParam(), Types.VARCHAR, 1);
				}
				else{
					sql.addSQLClause(FindClause.AND, "CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO", SQLBuilder.EQUALS, catGruppoDaParam.getValoreParam());
				}
			}
			if (!articoloDaParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI) && !articoloAParam.getValoreParam().equals(Stampa_consumiBulk.TUTTI)) {
				// articolo compreso tra
				if(!articoloDaParam.getValoreParam().equals(articoloAParam.getValoreParam())) {
					sql.addSQLClause("AND", "BENE_SERVIZIO.CD_BENE_SERVIZIO BETWEEN ? AND ?");
					sql.addParameter(articoloDaParam.getValoreParam(), Types.VARCHAR, 0);
					sql.addParameter(articoloAParam.getValoreParam(), Types.VARCHAR, 1);
				}else{
					sql.addSQLClause(FindClause.AND, "BENE_SERVIZIO.CD_BENE_SERVIZIO", SQLBuilder.EQUALS, articoloDaParam.getValoreParam());
				}
			}
			List<MovimentiMagBulk> movimenti = movimentoMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);

			for(MovimentiMagBulk movimento : movimenti){
				StampaConsumiDTO consDto = new StampaConsumiDTO();
				consDto.setCod_articolo(movimento.getLottoMag().getCdBeneServizio());
				consDto.setCod_cat_gruppo(movimento.getLottoMag().getBeneServizio().getCd_categoria_gruppo());
				consDto.setCod_categoria(movimento.getLottoMag().getBeneServizio().getCategoria_gruppo().getCd_categoria_padre());
				consDto.setCod_gruppo(movimento.getLottoMag().getBeneServizio().getCategoria_gruppo().getCd_proprio());
				consDto.setCod_magazzino(movimento.getLottoMag().getCdMagazzino());
				consDto.setDesc_articolo(movimento.getLottoMag().getBeneServizio().getDs_bene_servizio());
				consDto.setDesc_magazzino(movimento.getLottoMag().getMagazzino().getDsMagazzino());
				consDto.setDesc_uo(movimento.getUnitaOperativaOrd().getUnitaOrganizzativa().getDs_unita_organizzativa());
				if(movimento.getLottoMag().getBeneServizio().getCategoria_gruppo().getCd_categoria_padre() == null) {
					consDto.setDesc_cat_gruppo(movimento.getLottoMag().getBeneServizio().getDs_bene_servizio());
				}else{
					String descCategoriaPadre = movimento.getLottoMag().getBeneServizio().getCategoria_gruppo().getNodoPadre().getDs_categoria_gruppo();
					String descCategoriaGruppo = movimento.getLottoMag().getBeneServizio().getCategoria_gruppo().getDs_categoria_gruppo();
					consDto.setDesc_cat_gruppo(descCategoriaPadre+"-"+descCategoriaGruppo);
				}
				consDto.setImporto_unitario(movimento.getLottoMag().getCostoUnitario());
				consDto.setSigla_uo(movimento.getUnitaOperativaOrd().getUnitaOrganizzativa().getCd_unita_organizzativa());
				consDto.setUnita_misura(movimento.getLottoMag().getBeneServizio().getUnitaMisura().getCdUnitaMisura());
				consDto.setGiacenza(movimento.getLottoMag().getGiacenza());

				consumiDto.add(consDto);

			}
		}
		catch (PersistencyException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		String json=null;
		try {
			json=createJsonForPrint( consumiDto);
		} catch (ComponentException e) {
			e.printStackTrace();
		}
		return json;
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


}
