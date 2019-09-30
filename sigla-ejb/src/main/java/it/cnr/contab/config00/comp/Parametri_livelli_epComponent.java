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
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.comp;

import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcep.bulk.Voce_epHome;
import it.cnr.contab.config00.pdcep.cla.bulk.Classificazione_voci_epBulk;
import it.cnr.contab.config00.pdcep.cla.bulk.Classificazione_voci_epHome;
import it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk;
import it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Parametri_livelli_epComponent extends CRUDComponent {
	
	public boolean isParametriLivelliEcoEnabled(UserContext userContext, Parametri_livelli_epBulk parLiv) throws ComponentException{
		try{
			Classificazione_voci_epHome home = (Classificazione_voci_epHome)getHome(userContext, Classificazione_voci_epBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, parLiv.getEsercizio());
			sql.addSQLClause("AND", "TIPO", sql.EQUALS, Voce_epHome.ECONOMICA);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	
	public boolean isParametriLivelliPatEnabled(UserContext userContext, Parametri_livelli_epBulk parLiv) throws ComponentException{
		try{
			Classificazione_voci_epHome home = (Classificazione_voci_epHome)getHome(userContext, Classificazione_voci_epBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, parLiv.getEsercizio());
			sql.addSQLClause("AND", "TIPO", sql.EQUALS, Voce_epHome.PATRIMONIALE);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public Parametri_livelli_epBulk getParametriLivelli(UserContext userContext, Integer esercizio) throws ComponentException{
		try{
			Parametri_livelli_epHome home = (Parametri_livelli_epHome)getHome(userContext, Parametri_livelli_epBulk.class);
			Parametri_livelli_epBulk bulk = (Parametri_livelli_epBulk)home.findByPrimaryKey(new Parametri_livelli_epBulk(esercizio));
			getHomeCache(userContext).fetchAll(userContext,home);
			return bulk;
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	/**
	 * Restituisce la descrizione del livello di Entrata/Spesa definito nei Parametri CNR
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	public String getDescrizioneLivello(UserContext userContext, Integer esercizio, String tipo) throws ComponentException{
		try{
			Parametri_cnrBulk bulkCNR = (Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(esercizio));		
			if (bulkCNR == null)
			  throw new ApplicationException("Parametri CNR non presenti per l'anno "+esercizio);
			Parametri_livelli_epHome home = (Parametri_livelli_epHome)getHome(userContext, Parametri_livelli_epBulk.class);
			Parametri_livelli_epBulk bulkLivelli = (Parametri_livelli_epBulk)home.findByPrimaryKey(new Parametri_livelli_epBulk(esercizio));
			if (bulkLivelli == null)
			  throw new ApplicationException("Parametri Livelli non presenti per l'anno "+esercizio);						
			getHomeCache(userContext).fetchAll(userContext,home);
			if (tipo.equals(Voce_epHome.PATRIMONIALE))
			  return bulkLivelli.getDs_livello_pat(bulkCNR.getLivello_pat().intValue());
			else if (tipo.equals(Voce_epHome.ECONOMICA))
			  return bulkLivelli.getDs_livello_eco(bulkCNR.getLivello_eco().intValue());
			return "";  
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}	
	
}