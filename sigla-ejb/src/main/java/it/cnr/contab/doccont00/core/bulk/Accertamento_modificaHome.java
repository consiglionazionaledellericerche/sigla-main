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
import java.sql.Connection;

import it.cnr.contab.doccont00.ejb.NumTempDocContComponentSession;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Accertamento_modificaHome extends BulkHome {
	public Accertamento_modificaHome(Connection conn) {
		super(Accertamento_modificaBulk.class, conn);
	}
	public Accertamento_modificaHome(Connection conn, PersistentCache persistentCache) {
		super(Accertamento_modificaBulk.class, conn, persistentCache);
	}
	public java.util.List findAccertamento_mod_voceList( Accertamento_modificaBulk accertamento_modifica ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome omvHome = getHomeCache().getHome(Accertamento_mod_voceBulk.class, null, "default");
		SQLBuilder sql = omvHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, accertamento_modifica.getCd_cds());
		sql.addClause("AND","esercizio",sql.EQUALS, accertamento_modifica.getEsercizio());
		sql.addClause("AND","esercizio_originale",sql.EQUALS, accertamento_modifica.getEsercizio_originale());
		sql.addClause("AND","pg_modifica",sql.EQUALS, accertamento_modifica.getPg_modifica());
		sql.addOrderBy("pg_modifica");
		return omvHome.fetchAll(sql);
	}

	/**
	 * Imposta il pg_modifica di un oggetto <code>Accertamento_modificaBulk</code>.
	 *
	 * @param accertamento_modifica <code>OggettoBulk</code>
	 *
	 * @exception PersistencyException
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
	{
		try
		{
			Accertamento_modificaBulk accertamento_modifica = (Accertamento_modificaBulk) bulk;
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome( Numerazione_doc_contBulk.class );
			if (accertamento_modifica.getAccertamento()==null)
				throw new ComponentException("Impossibile creare la modifica senza l''indicazione dell''accertamento");
			if (accertamento_modifica.getPg_modifica()==null) {
				Long pg = (!userContext.isTransactional()) ?
							numHome.getNextPg(userContext,
									accertamento_modifica.getEsercizio(), 
									accertamento_modifica.getCd_cds(), 
									accertamento_modifica.getCd_tipo_documento_cont(),
									accertamento_modifica.getUser()) :
							((NumTempDocContComponentSession)EJBCommonServices.createEJB(
									"CNRDOCCONT00_EJB_NumTempDocContComponentSession",
									NumTempDocContComponentSession.class)).getNextTempPg(userContext, accertamento_modifica);
	
				accertamento_modifica.setPg_modifica( pg );
			}
		} catch ( ApplicationException e ) {
			throw new ComponentException(e);
		} catch ( Throwable e )	{
			throw new PersistencyException( e );
		}
	}
}