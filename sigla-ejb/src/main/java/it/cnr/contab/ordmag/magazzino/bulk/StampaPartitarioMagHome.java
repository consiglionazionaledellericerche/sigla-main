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
import it.cnr.contab.ordmag.magazzino.dto.StampaPartitarioMagDTO;
import it.cnr.contab.ordmag.magazzino.dto.StampaPartitarioMovMagDTO;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public class StampaPartitarioMagHome extends BulkHome {
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	private transient static final Logger _log = LoggerFactory.getLogger(StampaPartitarioMagHome.class);
	public StampaPartitarioMagHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
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
		super(Stampa_consumiBulk.class, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public StampaPartitarioMagHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Stampa_inventarioBulk.class, conn, persistentCache);
	}

	private static final String COD_MAGAZZINO = "cdMagazzino";
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

	public String getJsonDataSource(UserContext uc, Print_spoolerBulk print_spoolerBulk)  {
		BulkList<Print_spooler_paramBulk> params= print_spoolerBulk.getParams();
		Print_spooler_paramBulk codMagazzinoParam=params.stream().
				filter(e->e.getNomeParam().equals(COD_MAGAZZINO)).findFirst().get();

		Print_spooler_paramBulk daDataMovimentoParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_DATA_MOVIMENTO)).findFirst().get();

		Print_spooler_paramBulk aDataMovimentoParam=params.stream().
				filter(e->e.getNomeParam().equals(A_DATA_MOVIMENTO)).findFirst().get();

		Print_spooler_paramBulk daDataCompetenzaParam=params.stream().
				filter(e->e.getNomeParam().equals(DA_DATA_COMPETENZA)).findFirst().get();

		Print_spooler_paramBulk aDataCompetenzaParam=params.stream().
				filter(e->e.getNomeParam().equals(A_DATA_COMPETENZA)).findFirst().get();


		String codMag = null;
		if(codMagazzinoParam != null){
			codMag = codMagazzinoParam.getValoreParam();
		}
		Date dt = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			dt =dateFormatter.parse(daDataMovimentoParam.getValoreParam());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		//LottoMagHome lottoMagHome  = ( LottoMagHome)getHomeCache().getHome(LottoMagBulk.class,null,"stampa_inventario");
		MovimentiMagHome movimentiMagHome = (MovimentiMagHome) getHomeCache().getHome(MovimentiMagBulk.class);
		SQLBuilder sql = movimentiMagHome.createSQLBuilder();
		sql.generateJoin(MovimentiMagBulk.class, TipoMovimentoMagBulk.class, "tipoMovimentoMag", "TIPO_MOVIMENTO_MAG");
		sql.generateJoin(MovimentiMagBulk.class, LottoMagBulk.class, "lottoMag", "LOTTO_MAG");
		sql.generateJoin(LottoMagBulk.class, OrdineAcqConsegnaBulk.class, "ordineAcqConsegna", "ORDINE_ACQ_CONSEGNA");
		sql.generateJoin(LottoMagBulk.class, MagazzinoBulk.class, "magazzino", "MAGAZZINO");
//		sql.generateJoin(OrdineAcqRigaBulk.class, OrdineAcqBulk.class, "ordineAcq", "ORDINE_ACQ");
//		sql.generateJoin(OrdineAcqRigaBulk.class, Bene_servizioBulk.class, "beneServizio", "BENE_SERVIZO");
		sql.addTableToHeader("bolla_scarico_riga_mag","bs");
		sql.addSQLJoin("bs.pg_movimento","movimenti_mag.pg_movimento(+)");
		sql.addSQLClause(FindClause.AND,"LOTTO_MAG.DT_CARICO",SQLBuilder.LESS_EQUALS, new Timestamp(dt.getTime()));

		List<StampaPartitarioMovMagDTO> partitario= new ArrayList<StampaPartitarioMovMagDTO>();
		try {
			List<MovimentiMagBulk> movMag=movimentiMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);

			for ( MovimentiMagBulk m:movMag){

				StampaPartitarioMovMagDTO mov = new StampaPartitarioMovMagDTO();
				mov.setPg_movimento(m.getPgMovimento());
				//mov.setCd_magazzino(m.getCdMagazzino());
				//mov.setDesc_magazzino(m.getMagazzino().getDsMagazzino());
				//mov.setCod_articolo(m.getCdBeneServizio());
				//mov.setGiacenza(m.getGiacenza());
				//mov.setAnnoLotto(m.getEsercizio());
				//mov.setTipoLotto(m.getCdNumeratoreMag());
				//mov.setNumeroLotto(m.getPgLotto());
				//mov.setCategoriaGruppo(m.getBeneServizio().getCd_categoria_gruppo());
				//mov.setDescArticolo(m.getBeneServizio().getDs_bene_servizio());
				//mov.setCod_categoria(m.getBeneServizio().getCategoria_gruppo().getCd_categoria_padre());
				//mov.setCod_gruppo(m.getBeneServizio().getCategoria_gruppo().getCd_proprio());
				//mov.setUm(m.getBeneServizio().getUnitaMisura().getCdUnitaMisura());
				//mov.setDescCatGrp(m.getBeneServizio().getCategoria_gruppo().getDs_categoria_gruppo());
				//mov.setImportoUnitario(m.getCostoUnitario());
				//mov.setCdCds(m.getCdCds());
				partitario.add(mov);

			}
			MovimentiMagHome movimentoMagHome = (MovimentiMagHome)getHomeCache().getHome(MovimentiMagBulk.class);

			sql = movimentoMagHome.createSQLBuilder();
			sql.generateJoin(MovimentiMagBulk.class, LottoMagBulk.class, "lottoMag", "LOTTO_MAG");
			sql.generateJoin(MovimentiMagBulk.class, TipoMovimentoMagBulk.class, "tipoMovimentoMag","TIPO_MOVIMENTO_MAG");
			sql.addTableToHeader("BENE_SERVIZIO","BENE_SERVIZIO");
			sql.addSQLJoin("BENE_SERVIZIO.CD_BENE_SERVIZIO","LOTTO_MAG.CD_BENE_SERVIZIO");

			// tipo movimento != CHIUSURE (CH)
			sql.addSQLClause(FindClause.AND,"TIPO_MOVIMENTO_MAG.TIPO",SQLBuilder.NOT_EQUALS, TipoMovimentoMagBulk.CHIUSURE);
			// stato movimento = STATO_INSERITO (INS)
			sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.STATO",SQLBuilder.EQUALS, MovimentiMagBulk.STATO_INSERITO);
			// data movimento maggiore/uguale della data in input
			sql.addSQLClause(FindClause.AND,"MOVIMENTI_MAG.DT_MOVIMENTO",SQLBuilder.GREATER_EQUALS, new Timestamp(dt.getTime()));
			// codice magazzino uguale a quello in input
			sql.addSQLClause(FindClause.AND,"LOTTO_MAG.CD_MAGAZZINO_MAG",SQLBuilder.EQUALS, codMag);
			// data carico lotto minore/uguale della data in input
			sql.addSQLClause(FindClause.AND,"LOTTO_MAG.DT_CARICO",SQLBuilder.LESS_EQUALS, new Timestamp(dt.getTime()));

			List<MovimentiMagBulk> movimenti=movimentoMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);

		} catch (PersistencyException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		String json=null;
		try {
			json=createJsonForPrint(partitario);
		} catch (ComponentException e) {
			e.printStackTrace();
		}

		return json;
	}
}
