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
import it.cnr.contab.ordmag.magazzino.dto.StampaInventarioDTO;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Print_spooler_paramBulk;

import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Insert the type's description here.
 * Creation date: (23/01/2003 16.03.39)
 * @author: Roberto Fantino
 */
public class Stampa_inventarioHome extends BulkHome {
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 */
	private transient static final Logger _log = LoggerFactory.getLogger(Stampa_inventarioHome.class);
	public Stampa_inventarioHome(Class clazz, java.sql.Connection conn) {
		super(clazz, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param clazz java.lang.Class
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_inventarioHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(clazz, conn, persistentCache);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 */
	public Stampa_inventarioHome(java.sql.Connection conn) {
		super(Stampa_consumiBulk.class, conn);
	}
	/**
	 * Stampa_consumiHome constructor comment.
	 * @param conn java.sql.Connection
	 * @param persistentCache it.cnr.jada.persistency.PersistentCache
	 */
	public Stampa_inventarioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
		super(Stampa_inventarioBulk.class, conn, persistentCache);
	}

	private static final String DATA_INVENTARIO="dataInventario";
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
		Print_spooler_paramBulk dataInventario=params.stream().
				filter(e->e.getNomeParam().equals(DATA_INVENTARIO)).findFirst().get();
		Date dt = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
		try {
			dt =dateFormatter.parse(dataInventario.getValoreParam());

		} catch (ParseException e) {
			e.printStackTrace();
		}

		//LottoMagHome lottoMagHome  = ( LottoMagHome)getHomeCache().getHome(LottoMagBulk.class,null,"stampa_inventario");
		LottoMagHome lottoMagHome  = ( LottoMagHome)getHomeCache().getHome(LottoMagBulk.class);
		SQLBuilder sql = lottoMagHome.createSQLBuilder();
		sql.addColumn("BENE_SERVIZIO.DS_BENE_SERVIZIO");
		//sql.addTableToHeader("BENE_SERVIZIO");
		sql.generateJoin(LottoMagBulk.class, Bene_servizioBulk.class, "beneServizio", "BENE_SERVIZIO");
		//sql.addSQLJoin("BENE_SERVIZIO.cd_bene_servizio","LOTTO_MAG.cd_bene_servizio");
		sql.addTableToHeader("MAGAZZINO","m");
		sql.addSQLJoin("m.cd_magazzino","LOTTO_MAG.cd_magazzino");
		sql.addTableToHeader("Categoria_Gruppo_Invent","c");
		sql.addSQLJoin("c.cd_categoria_gruppo","BENE_SERVIZIO.cd_categoria_gruppo(+)");
		sql.addSQLClause(FindClause.AND,"LOTTO_MAG.DT_CARICO",SQLBuilder.LESS_EQUALS, new Timestamp(dt.getTime()));
		try {
			List<LottoMagBulk> l=lottoMagHome.fetchAll(sql);
			getHomeCache().fetchAll(uc);
			for ( LottoMagBulk m:l){
				String descrBene=m.getBeneServizio().getDs_bene_servizio();
				_log.info("Descrizione Bene Servizio="+descrBene);
			}
			_log.info(String.valueOf(l.size()));

		} catch (PersistencyException e) {
			e.printStackTrace();
		}
		List<StampaInventarioDTO> inventario= new ArrayList<StampaInventarioDTO>();
		String sqlString="SELECT l.cd_magazzino cd_magazzino," +
				"l.cd_bene_servizio cod_articolo," +
				"(ROUND(nvl(l.giacenza,0),5)*100000) giacenza," +
				"l.esercizio anno_lotto," +
				"l.cd_numeratore_mag tipo_lotto," +
				"l.pg_lotto numero_lotto," +
				" b.cd_categoria_gruppo cd_cat_grp," +
				"b.ds_bene_servizio desc_articolo," +
				" b.unita_misura um," +
				"c.ds_categoria_gruppo desc_cat_grp" +
				" FROM Magazzino m inner join Lotto_mag l on  m.cd_magazzino = l.cd_magazzino "+
				" inner join bene_servizio b on b.cd_bene_servizio = l.cd_bene_servizio "+
				" left outer join Categoria_Gruppo_Invent c on b.cd_categoria_gruppo = c.cd_categoria_gruppo"+
				" where l.dt_carico<=?";
		try {
			LoggableStatement ps= new LoggableStatement(getConnection(),sqlString,true,this.getClass());
			try
			{
				ps.setObject( 1, new Timestamp(dt.getTime()) );
				ResultSet rs = ps.executeQuery();
				try
				{
					while (rs.next()) {
						StampaInventarioDTO l  =new StampaInventarioDTO();
							l.setCd_magazzino(rs.getString(1));
							l.setCod_articolo(rs.getString(2));
							l.setGiacenza(rs.getBigDecimal(3));
							l.setAnnoLotto(rs.getInt(4));
							l.setTipoLotto(rs.getString(5));
							l.setNumeroLotto(rs.getInt(6));
							l.setCategoriaGruppo(rs.getString(7));
							l.setDescArticolo(rs.getString(8));
							l.setUm(rs.getString(9));
							l.setDescCatGrp(rs.getString(10));
						inventario.add(l);
					}
				}
				finally
				{
					try{rs.close();}catch( java.sql.SQLException e ){};
				}
			}
			catch( SQLException e )
			{
				_log.info("errore",e);
			}
			finally
			{
				try{ps.close();}catch( java.sql.SQLException e ){};
			}
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		String json=null;
		try {
			json=createJsonForPrint( inventario);
		} catch (ComponentException e) {
			e.printStackTrace();
		}
		return json;
	}
}
