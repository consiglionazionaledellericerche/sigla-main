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
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.List;

public class Buono_carico_scarico_dettHome extends BulkHome {
	public Buono_carico_scarico_dettHome(java.sql.Connection conn) {
		super(Buono_carico_scarico_dettBulk.class, conn);
	}
	public Buono_carico_scarico_dettHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Buono_carico_scarico_dettBulk.class, conn, persistentCache);
	}
	public java.util.List getDetailsFor(Buono_carico_scaricoBulk buono) throws it.cnr.jada.persistency.PersistencyException{

		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();

		sql.addClause("AND","esercizio",sql.EQUALS,buono.getEsercizio());
		sql.addClause("AND","pg_inventario",sql.EQUALS,buono.getPg_inventario());
		sql.addClause("AND","ti_documento",sql.EQUALS,buono.getTi_documento());
		sql.addClause("AND","pg_buono_c_s",sql.EQUALS,buono.getPg_buono_c_s());

		return fetchAll(sql);
	}
	/**
	  *	Ritorna TRUE se esiste un'associazione con un documento amministrativo
	**/
	public boolean isAssociato_documento(Buono_carico_scarico_dettBulk dettaglio_buono) throws java.sql.SQLException{

		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("ASS_INV_BENE_FATTURA");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.NR_INVENTARIO","BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO");
		sql.addSQLJoin("ASS_INV_BENE_FATTURA.PROGRESSIVO","BUONO_CARICO_SCARICO_DETT.PROGRESSIVO");
		
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",sql.EQUALS,dettaglio_buono.getEsercizio());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",sql.EQUALS,dettaglio_buono.getPg_inventario());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",sql.EQUALS,dettaglio_buono.getTi_documento());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",sql.EQUALS,dettaglio_buono.getPg_buono_c_s());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO",sql.EQUALS,dettaglio_buono.getNr_inventario());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PROGRESSIVO",sql.EQUALS,dettaglio_buono.getProgressivo());

		return sql.executeExistsQuery(getConnection());
	}
	/**
	  *	Ritorna TRUE se un buono con data registrazione superiore
	**/
	public boolean isNonUltimo(Buono_carico_scarico_dettBulk dettaglio_buono) throws java.sql.SQLException{

		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("BUONO_CARICO_SCARICO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.ESERCIZIO","BUONO_CARICO_SCARICO_DETT.ESERCIZIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.PG_INVENTARIO","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.TI_DOCUMENTO","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO.PG_BUONO_C_S","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S");
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",sql.EQUALS,dettaglio_buono.getPg_inventario());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO",sql.EQUALS,dettaglio_buono.getNr_inventario());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PROGRESSIVO",sql.EQUALS,dettaglio_buono.getProgressivo());
		sql.addSQLClause("AND","DATA_REGISTRAZIONE",sql.GREATER,dettaglio_buono.getBuono_cs().getData_registrazione());
		return sql.executeExistsQuery(getConnection());
	}
	public java.util.List findBuoniFor(Inventario_beniBulk bene) throws it.cnr.jada.persistency.PersistencyException{

		it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();

		sql.addClause("AND","pg_inventario",sql.EQUALS,bene.getPg_inventario());
		sql.addClause("AND","nr_inventario",sql.EQUALS,bene.getNr_inventario());
		sql.addClause("AND","progressivo",sql.EQUALS,bene.getProgressivo());
		return fetchAll(sql);
	}
	public Buono_carico_scarico_dettBulk findCaricoDaOrdine(Inventario_beniBulk inventario_beniBulk) throws PersistencyException {
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.setAutoJoins(true);
		sqlBuilder.generateJoin("buono_cs", "BUONO_CARICO_SCARICO");
		sqlBuilder.generateJoin(Buono_carico_scaricoBulk.class, Tipo_carico_scaricoBulk.class, "tipoMovimento", "TIPO_CARICO_SCARICO");
		sqlBuilder.addSQLClause(FindClause.AND, "TIPO_CARICO_SCARICO.FL_DA_ORDINI", SQLBuilder.EQUALS, "Y");
		sqlBuilder.addSQLClause(FindClause.AND, "BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO", SQLBuilder.EQUALS, inventario_beniBulk.getPg_inventario());
		sqlBuilder.addSQLClause(FindClause.AND, "BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO", SQLBuilder.EQUALS, inventario_beniBulk.getNr_inventario());
		sqlBuilder.addSQLClause(FindClause.AND, "BUONO_CARICO_SCARICO_DETT.PROGRESSIVO", SQLBuilder.EQUALS, inventario_beniBulk.getProgressivo());
		List lista = fetchAll(sqlBuilder);
		if (lista != null && !lista.isEmpty()){
			return (Buono_carico_scarico_dettBulk) lista.get(0);
		}
		return null;
	}
}