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
* Date 23/06/2006
*/
package it.cnr.contab.doccont00.core.bulk;
import java.util.Iterator;

import it.cnr.contab.doccont00.ejb.NumTempDocContComponentSession;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Obbligazione_modificaHome extends BulkHome {
	
	public Obbligazione_modificaHome(java.sql.Connection conn) {
		super(Obbligazione_modificaBulk.class, conn);
	}

	public Obbligazione_modificaHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Obbligazione_modificaBulk.class, conn, persistentCache);
	}

	public java.util.List findObbligazione_mod_voceList( Obbligazione_modificaBulk obbligazione_modifica ) throws IntrospectionException,PersistencyException 
	{
		PersistentHome omvHome = getHomeCache().getHome(Obbligazione_mod_voceBulk.class, null, "default");
		SQLBuilder sql = omvHome.createSQLBuilder();
		sql.addClause("AND","cd_cds",sql.EQUALS, obbligazione_modifica.getCd_cds());
		sql.addClause("AND","esercizio",sql.EQUALS, obbligazione_modifica.getEsercizio());
		sql.addClause("AND","esercizio_originale",sql.EQUALS, obbligazione_modifica.getEsercizio_originale());
		sql.addClause("AND","pg_modifica",sql.EQUALS, obbligazione_modifica.getPg_modifica());
		sql.addOrderBy("pg_modifica");
		return omvHome.fetchAll(sql);
	}

	/**
	 * Imposta il pg_modifica di un oggetto <code>Obbligazione_modificaBulk</code>.
	 *
	 * @param obbligazione_modifica <code>OggettoBulk</code>
	 *
	 * @exception PersistencyException
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
	{
		try
		{
			Obbligazione_modificaBulk obbligazione_modifica = (Obbligazione_modificaBulk) bulk;
			Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache().getHome( Numerazione_doc_contBulk.class );
			if (obbligazione_modifica.getObbligazione()==null)
				throw new ComponentException("Impossibile creare la modifica senza l''indicazione dell''obbligazione");
			if (obbligazione_modifica.getPg_modifica()==null) {
				Long pg = (!userContext.isTransactional()) ?
							numHome.getNextPg(userContext,
									obbligazione_modifica.getEsercizio(), 
									obbligazione_modifica.getCd_cds(), 
									obbligazione_modifica.getCd_tipo_documento_cont(),
									obbligazione_modifica.getUser()) :
							((NumTempDocContComponentSession)EJBCommonServices.createEJB(
									"CNRDOCCONT00_EJB_NumTempDocContComponentSession",
									NumTempDocContComponentSession.class)).getNextTempPg(userContext, obbligazione_modifica);
	
				obbligazione_modifica.setPg_modifica( pg );
			}
		} catch ( ApplicationException e ) {
			throw new ComponentException(e);
		} catch ( Throwable e )	{
			throw new PersistencyException( e );
		}
	}
}