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

package it.cnr.contab.config00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;

/**
 * Creation date: (09/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cnrHome extends BulkHome {
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 */
	public Parametri_cnrHome(java.sql.Connection conn) {
		super(Parametri_cnrBulk.class, conn);
	}
	/**
	 * Costruisce un Configurazione_cnrHome
	 *
	 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
	 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
	 */
	public Parametri_cnrHome(
		java.sql.Connection conn,
		PersistentCache persistentCache) {
		super(Parametri_cnrBulk.class, conn, persistentCache);
	}
	public Integer livelloCofogObbligatorio(it.cnr.jada.UserContext userContext) throws PersistencyException
	{
		Parametri_cnrBulk parametri = (Parametri_cnrBulk)findByPrimaryKey( new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
		if (parametri == null||parametri.getLivello_pdg_cofog()==null)
		  return 0;
		else
		  return parametri.getLivello_pdg_cofog();  
	
	}
	public boolean isNuovoPdg(UserContext userContext) throws PersistencyException{
		return isNuovoPdg(CNRUserContext.getEsercizio(userContext));
	}

	public boolean isNuovoPdg(int esercizio) throws PersistencyException{
        Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)findByPrimaryKey(new Parametri_cnrBulk(esercizio));
        return parametriCnr!=null && parametriCnr.getFl_nuovo_pdg();
	}	

	public Parametri_cnrBulk getParametriCnr(UserContext userContext) throws PersistencyException{
		return (Parametri_cnrBulk)findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(userContext)));
	}	
}
