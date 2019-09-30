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

package it.cnr.contab.bollo00.tabrif.bulk;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import it.cnr.contab.config00.sto.bulk.Ass_uo_areaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

public class Tipo_atto_bolloHome extends BulkHome {
	private static final long serialVersionUID = 1L;

	public Tipo_atto_bolloHome(java.sql.Connection conn) {
		super(Tipo_atto_bolloBulk.class,conn);
	}
	public Tipo_atto_bolloHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Tipo_atto_bolloBulk.class,conn,persistentCache);
	}

	@Override
	public void initializePrimaryKeyForInsert(UserContext  usercontext,OggettoBulk oggettobulk) throws PersistencyException, ComponentException {
		try {
			Tipo_atto_bolloBulk tipoAtto = (Tipo_atto_bolloBulk)oggettobulk;
			tipoAtto.setId(
					new Integer(((Integer)findAndLockMax( oggettobulk, "id", new Integer(0) )).intValue()+1));
			super.initializePrimaryKeyForInsert(usercontext, tipoAtto);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	
	public Tipo_atto_bolloBulk findByCodiceAndData(String codice, Timestamp dataValidita) throws PersistencyException, ComponentException {
		SQLBuilder sql = this.createSQLBuilder();
		Optional.ofNullable(codice).ifPresent(el->sql.addClause(FindClause.AND, "codice", SQLBuilder.EQUALS, el));

		if (Optional.ofNullable(dataValidita).isPresent()) {
			sql.addClause(FindClause.AND,"dtIniValidita",SQLBuilder.LESS_EQUALS, dataValidita );

			sql.openParenthesis(FindClause.AND);
			sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.ISNULL, null);
			sql.addClause(FindClause.OR,"dtFinValidita",SQLBuilder.GREATER_EQUALS, dataValidita);
			sql.closeParenthesis();
		} else {
			sql.addClause(FindClause.AND,"dtFinValidita",SQLBuilder.ISNULL, null);
		}
 	    
		List<Tipo_atto_bolloBulk> l = this.fetchAll(sql);
		if (l.stream().count()>1)
			throw new ApplicationException("Errore nei dati: esiste per il codice atto "+codice+" più configurazioni valide per la data selezionata!");

		return l.stream().findFirst().orElse(null);
	}
	
}
