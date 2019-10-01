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

package it.cnr.contab.consultazioni.comp;

import java.util.Optional;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniRestComponent extends CRUDComponent {

	@Override
	protected Query select(UserContext userContext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		SQLBuilder sql =  (SQLBuilder)super.select(userContext, compoundfindclause, oggettobulk);
        final Optional<ConsultazioniRestHome> consultazioniRestHome = Optional.ofNullable(getHome(userContext, oggettobulk))
                .filter(ConsultazioniRestHome.class::isInstance)
                .map(ConsultazioniRestHome.class::cast);
        if (consultazioniRestHome.isPresent()) {
            sql = consultazioniRestHome.get().restSelect(userContext, sql, compoundfindclause, oggettobulk);
        }
		return sql;
	}
}