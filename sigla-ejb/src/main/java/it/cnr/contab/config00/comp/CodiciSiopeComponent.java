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

package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.bulk.Codici_siopeBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CodiciSiopeComponent extends CRUDComponent {
	private transient static final Logger logger = LoggerFactory.getLogger(CodiciSiopeComponent.class);

	@Override
	public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		// TODO Auto-generated method stub
		OggettoBulk ob = super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
		if (ob instanceof Codici_siopeBulk){
			Codici_siopeBulk siopeBulk = (Codici_siopeBulk)ob;
			siopeBulk.setEsercizio(CNRUserContext.getEsercizio(usercontext));
		}
		return ob;
	}

	@Override
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = getHome(userContext, Codici_siopeBulk.class).createSQLBuilder();

	    sql.addClause("AND","esercizio",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
		return sql;
	}

	@Override
	public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		OggettoBulk ob = super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
		if (ob instanceof Codici_siopeBulk){
			Codici_siopeBulk siopeBulk = (Codici_siopeBulk)ob;
			siopeBulk.setEsercizio(CNRUserContext.getEsercizio(usercontext));
		}
		return ob;
	}

}
