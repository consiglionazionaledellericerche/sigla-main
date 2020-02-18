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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.sql.Connection;
import java.util.Optional;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class AbilUtenteUopOperHome extends BulkHome {
	public AbilUtenteUopOperHome(Connection conn) {
		super(AbilUtenteUopOperBulk.class, conn);
	}
	public AbilUtenteUopOperHome(Connection conn, PersistentCache persistentCache) {
		super(AbilUtenteUopOperBulk.class, conn, persistentCache);
	}
	public Boolean isUtenteAbilitato(UserContext userContext, String tipoOperazione, 
			String unitaOperativa) throws IntrospectionException, PersistencyException {	
		AbilUtenteUopOperBulk abil = (AbilUtenteUopOperBulk)findByPrimaryKey(userContext, new AbilUtenteUopOperBulk(userContext.getUser(), unitaOperativa, tipoOperazione));
		if (abil != null){
			return true;
		}
		return false;
	}

	public Boolean isUtenteAbilitatoTipoOperazione(UserContext userContext, String tipoOperazione)throws  ComponentException {
		try{
			SQLBuilder sql = this.createSQLBuilder();
			sql.addClause("AND", "cdTipoOperazione", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_ORDINE);
			sql.addClause("AND", "cdUtente", SQLBuilder.EQUALS, userContext.getUser());

			Object abil = fetchMax(sql,"cdTipoOperazione",null);
			if (Optional.ofNullable(abil).isPresent())
				return true;

			return false;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}