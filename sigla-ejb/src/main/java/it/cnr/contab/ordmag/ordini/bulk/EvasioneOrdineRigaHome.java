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
import java.util.Collection;
import java.util.List;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class EvasioneOrdineRigaHome extends BulkHome {
	public EvasioneOrdineRigaHome(Connection conn) {
		super(EvasioneOrdineRigaBulk.class, conn);
	}
	public EvasioneOrdineRigaHome(Connection conn, PersistentCache persistentCache) {
		super(EvasioneOrdineRigaBulk.class, conn, persistentCache);
	}
	public EvasioneOrdineRigaBulk findByConsegna(OrdineAcqConsegnaBulk consegna) throws PersistencyException {
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.CD_CDS_ORDINE", SQLBuilder.EQUALS, consegna.getCdCds());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.CD_UNITA_OPERATIVA", SQLBuilder.EQUALS, consegna.getCdUnitaOperativa());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.ESERCIZIO_ORDINE", SQLBuilder.EQUALS, consegna.getEsercizio());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.CD_NUMERATORE_ORDINE", SQLBuilder.EQUALS, consegna.getCdNumeratore());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.NUMERO_ORDINE", SQLBuilder.EQUALS, consegna.getNumero());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.RIGA_ORDINE", SQLBuilder.EQUALS, consegna.getRiga());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.CONSEGNA", SQLBuilder.EQUALS, consegna.getConsegna());
		sqlBuilder.addSQLClause(FindClause.AND, "EVASIONE_ORDINE_RIGA.STATO", SQLBuilder.EQUALS, OrdineAcqConsegnaBulk.STATO_INSERITA);
		List lista = fetchAll(sqlBuilder);
		if (lista != null && !lista.isEmpty()){
			return (EvasioneOrdineRigaBulk)lista.get(0);
		}
		return null;
	}
}