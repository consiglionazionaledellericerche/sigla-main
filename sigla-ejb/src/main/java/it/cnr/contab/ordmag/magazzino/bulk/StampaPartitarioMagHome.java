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
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
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
/*
		MovimentiMagHome movimentiMagHome  = ( MovimentiMagHome)getHomeCache().getHome(LottoMagBulk.class);
		SQLBuilder sql = movimentiMagHome.createSQLBuilder();
		sql.addColumn("BENE_SERVIZIO.DS_BENE_SERVIZIO");
		sql.generateJoin(MovimentiMagBulk.class, TipoMovimentoMagBulk.class, "tipoMovimentoMag", "TIPO_MOVIMENTO_MAG");
		sql.generateJoin(MovimentiMagBulk.class, LottoMagBulk.class, "lottoMag", "LOTTO_MAG");
		sql.generateJoin(LottoMagBulk.class, OrdineAcqRigaBulk.class, "ordineAcqConsegna", "ORDINE_ACQ_RIGA");
		sql.generateJoin(LottoMagBulk.class, MagazzinoBulk.class, "magazzino", "MAGAZZINO");
		sql.generateJoin(OrdineAcqRigaBulk.class, OrdineAcqBulk.class, "ordineAcq", "ORDINE_ACQ");
		sql.generateJoin(OrdineAcqRigaBulk.class, Bene_servizioBulk.class, "beneServizio", "BENE_SERVIZO");
		sql.addTableToHeader("bolla_scarico_riga_mag","bs");
		sql.addSQLJoin("bs.pg_movimento","LOTTO_MAG.pg_movimento(+)");
		sql.addSQLClause(FindClause.AND,"LOTTO_MAG.DT_CARICO",SQLBuilder.LESS_EQUALS, new Timestamp(dt.getTime()));
*/
		/*
		List<StampaInventarioDTO> inventario= new ArrayList<StampaInventarioDTO>();
		try {
			List<LottoMagBulk> lotti=lottoMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);

			for ( LottoMagBulk m:lotti){

				StampaInventarioDTO inv  =new StampaInventarioDTO();
				inv.setCd_magazzino(m.getCdMagazzino());
				inv.setDesc_magazzino(m.getMagazzino().getDsMagazzino());
				inv.setCod_articolo(m.getCdBeneServizio());
				inv.setGiacenza(m.getGiacenza());
				inv.setAnnoLotto(m.getEsercizio());
				inv.setTipoLotto(m.getCdNumeratoreMag());
				inv.setNumeroLotto(m.getPgLotto());
				inv.setCategoriaGruppo(m.getBeneServizio().getCd_categoria_gruppo());
				inv.setDescArticolo(m.getBeneServizio().getDs_bene_servizio());
				inv.setCod_categoria(m.getBeneServizio().getCategoria_gruppo().getCd_categoria_padre());
				inv.setCod_gruppo(m.getBeneServizio().getCategoria_gruppo().getCd_proprio());
				inv.setUm(m.getBeneServizio().getUnitaMisura().getCdUnitaMisura());
				inv.setDescCatGrp(m.getBeneServizio().getCategoria_gruppo().getDs_categoria_gruppo());
				inv.setImportoUnitario(m.getCostoUnitario());
				inv.setCdCds(m.getCdCds());
				inventario.add(inv);

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

			if(catGruppo != null && !catGruppo.equals(Stampa_inventarioBulk.TUTTI)){
				sql.addTableToHeader("CATEGORIA_GRUPPO_INVENT","CATEGORIA_GRUPPO_INVENT");
				sql.addSQLJoin("CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO","BENE_SERVIZIO.CD_CATEGORIA_GRUPPO");
				//categoria gruppo uguale a quella in input
				sql.addSQLClause(FindClause.AND,"CATEGORIA_GRUPPO_INVENT.CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS, catGruppo);
			}

			List<MovimentiMagBulk> movimenti=movimentoMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);



			for(StampaInventarioDTO invDto : inventario){
				for(MovimentiMagBulk movimento : movimenti){
					if( invDto.getAnnoLotto().compareTo(movimento.getLottoMag().getEsercizio()) == 0 &&
						invDto.getNumeroLotto().compareTo(movimento.getLottoMag().getPgLotto()) == 0 &&
						invDto.getTipoLotto().equals(movimento.getLottoMag().getCdNumeratoreMag()) &&
						invDto.getCd_magazzino().equals(movimento.getLottoMag().getCdMagazzino()) &&
						invDto.getCdCds().equals(movimento.getLottoMag().getCdCds()) &&
						invDto.getCategoriaGruppo().equals(movimento.getLottoMag().getBeneServizio().getCd_categoria_gruppo()) &&
						invDto.getCod_articolo().equals(movimento.getLottoMag().getCdBeneServizio()) )
					{
						if(movimento.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOTTRAE)){
							invDto.setGiacenza(invDto.getGiacenza().add(movimento.getQuantita()));
						}
						if(movimento.getTipoMovimentoMag().getModAggQtaMagazzino().equals(TipoMovimentoMagBulk.AZIONE_SOMMA)){
							invDto.setGiacenza(invDto.getGiacenza().subtract(movimento.getQuantita()));
						}

					}
				}
			}
			if(ordinamentoParam!=null){
				if(ordinamentoParam.getValoreParam().equals(Stampa_inventarioBulk.ORD_CODICE)){
					inventario = inventario.stream().sorted(Comparator.comparing(StampaInventarioDTO::getCod_articolo)).collect(Collectors.toList());
				}
				if(ordinamentoParam.getValoreParam().equals(Stampa_inventarioBulk.ORD_DENOMINAZIONE)){
					inventario = inventario.stream().sorted(Comparator.comparing(StampaInventarioDTO::getDescArticolo)).collect(Collectors.toList());
				}
			}



			// ORDER BY PARAM CON STREAM

		} catch (PersistencyException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		*/

		//String json="{}";
		String json=null;
		/*
		try {
			json=createJsonForPrint( inventario);
		} catch (ComponentException e) {
			e.printStackTrace();
		}*/
		return json;
	}
}
