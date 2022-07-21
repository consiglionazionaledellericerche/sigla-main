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
 * Date 27/09/2006
 */
package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_amministrativo_passivoBulk;
import it.cnr.contab.pagopa.model.Documento;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.Collection;

public class Ass_comp_doc_cont_nmpHome extends BulkHome {
	public Ass_comp_doc_cont_nmpHome(Connection conn) {
		super(Ass_comp_doc_cont_nmpBulk.class, conn);
	}
	public Ass_comp_doc_cont_nmpHome(Connection conn, PersistentCache persistentCache) {
		super(Ass_comp_doc_cont_nmpBulk.class, conn, persistentCache);
	}

	public Collection<Ass_comp_doc_cont_nmpBulk> findByDocumento(it.cnr.jada.UserContext userContext, IDocumentoCogeBulk doccoge ) throws PersistencyException
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND,"esercizio_doc",SQLBuilder.EQUALS, doccoge.getEsercizio() );
		sql.addClause(FindClause.AND,"cd_cds_doc",SQLBuilder.EQUALS, doccoge.getCd_cds() );
		sql.addClause(FindClause.AND,"pg_doc",SQLBuilder.EQUALS, doccoge.getPg_doc() );
		if (doccoge.getTipoDocumentoEnum().isMandato())
			sql.addClause(FindClause.AND,"cd_tipo_doc",SQLBuilder.EQUALS, "M" );
		else if (doccoge.getTipoDocumentoEnum().isReversale())
			sql.addClause(FindClause.AND,"cd_tipo_doc",SQLBuilder.EQUALS, "R" );
		else
			sql.addClause(FindClause.AND,"cd_tipo_doc",SQLBuilder.EQUALS, doccoge.getCd_tipo_doc() );
		return this.fetchAll( sql);
	}
}