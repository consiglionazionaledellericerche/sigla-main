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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;
import java.sql.Connection;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Mandato_siopeHome extends BulkHome {
	public Mandato_siopeHome(Class clazz, java.sql.Connection conn) {
		super(clazz,conn);
	}
	public Mandato_siopeHome(Class clazz,java.sql.Connection conn,PersistentCache persistentCache) {
		super(clazz,conn,persistentCache);
	}
	public Mandato_siopeHome(Connection conn) {
		super(Mandato_siopeBulk.class, conn);
	}
	public Mandato_siopeHome(Connection conn, PersistentCache persistentCache) {
		super(Mandato_siopeBulk.class, conn, persistentCache);
	}
	public java.util.Collection findCodiciSiopeCupCollegati(UserContext usercontext, Mandato_siopeBulk riga) throws PersistencyException {
		PersistentHome mandatoSiopeCupHome = getHomeCache().getHome(MandatoSiopeCupIBulk.class);
		SQLBuilder sql = mandatoSiopeCupHome.createSQLBuilder();
		sql.addClause("AND", "cdCds", SQLBuilder.EQUALS, riga.getCd_cds());
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, riga.getEsercizio());
		sql.addClause("AND", "pgMandato", SQLBuilder.EQUALS, riga.getPg_mandato());
		sql.addClause("AND", "esercizioObbligazione", SQLBuilder.EQUALS, riga.getEsercizio_obbligazione());
		sql.addClause("AND", "esercizioOriObbligazione", SQLBuilder.EQUALS, riga.getEsercizio_ori_obbligazione());
		sql.addClause("AND", "pgObbligazione", SQLBuilder.EQUALS, riga.getPg_obbligazione());
		sql.addClause("AND", "pgObbligazioneScadenzario", SQLBuilder.EQUALS, riga.getPg_obbligazione_scadenzario());
		sql.addClause("AND", "cdCdsDocAmm", SQLBuilder.EQUALS, riga.getCd_cds_doc_amm());
		sql.addClause("AND", "cdUoDocAmm", SQLBuilder.EQUALS, riga.getCd_uo_doc_amm());
		sql.addClause("AND", "esercizioDocAmm", SQLBuilder.EQUALS, riga.getEsercizio_doc_amm());
		sql.addClause("AND", "cdTipoDocumentoAmm", SQLBuilder.EQUALS, riga.getCd_tipo_documento_amm());
		sql.addClause("AND", "pgDocAmm", SQLBuilder.EQUALS, riga.getPg_doc_amm());
		sql.addClause("AND", "esercizioSiope", SQLBuilder.EQUALS, riga.getEsercizio_siope());
		sql.addClause("AND", "tiGestione", SQLBuilder.EQUALS, riga.getTi_gestione());
		sql.addClause("AND", "cdSiope", SQLBuilder.EQUALS, riga.getCd_siope());
		return mandatoSiopeCupHome.fetchAll(sql);
	}
}