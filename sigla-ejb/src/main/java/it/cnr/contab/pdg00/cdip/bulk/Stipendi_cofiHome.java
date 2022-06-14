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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Collection;
import java.util.Optional;

public class Stipendi_cofiHome extends BulkHome {
public Stipendi_cofiHome(java.sql.Connection conn) {
	super(Stipendi_cofiBulk.class,conn);
}
	public Stipendi_cofiHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Stipendi_cofiBulk.class,conn,persistentCache);
	}
	public java.util.Collection findStipendiCofiAnno(UserContext context) throws PersistencyException{
		return findStipendiCofiAnno(((it.cnr.contab.utenze00.bp.CNRUserContext)context).getEsercizio());
	}

	public java.util.Collection findStipendiCofiAnno(int esercizio) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause(FindClause.AND,"ESERCIZIO",SQLBuilder.EQUALS,esercizio);
		sql.addOrderBy("MESE DESC");
		return fetchAll(sql);
	}

	public Stipendi_cofiBulk findStipendiCofi(CompensoBulk compensoBulk) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND,"cd_cds_comp",SQLBuilder.EQUALS,compensoBulk.getCd_cds());
		sql.addClause(FindClause.AND,"cd_uo_comp",SQLBuilder.EQUALS,compensoBulk.getCd_unita_organizzativa());
		sql.addClause(FindClause.AND,"esercizio_comp",SQLBuilder.EQUALS,compensoBulk.getEsercizio());
		sql.addClause(FindClause.AND,"pg_comp",SQLBuilder.EQUALS,compensoBulk.getPg_compenso());
		Collection<Stipendi_cofiBulk> result = fetchAll(sql);
		return result.stream().findFirst().orElse(null);
	}

	public Stipendi_cofiBulk findStipendiCofi(MandatoBulk mandatoBulk) throws PersistencyException{
		SQLBuilder sql = createSQLBuilder();
		sql.addClause(FindClause.AND,"cd_cds_mandato",SQLBuilder.EQUALS,mandatoBulk.getCd_cds());
		sql.addClause(FindClause.AND,"esercizio_mandato",SQLBuilder.EQUALS,mandatoBulk.getEsercizio());
		sql.addClause(FindClause.AND,"pg_mandato",SQLBuilder.EQUALS,mandatoBulk.getPg_mandato());
		Collection<Stipendi_cofiBulk> result = fetchAll(sql);
		return result.stream().findFirst().orElse(null);
	}
}
