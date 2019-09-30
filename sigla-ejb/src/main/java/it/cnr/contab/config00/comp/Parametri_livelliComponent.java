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
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliHome;
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
public class Parametri_livelliComponent extends CRUDComponent {
	/*
	 * Metodo che verifica se è possibile modificare la struttura dei parametri livelli relativamente
	 * alla parte ENTRATE.
	 * Ritorna FALSE se esistono classificazioni di Entrata
	 * Ritorna TRUE  se non esistono classificazioni di Entrata 
	 */
	public boolean isParametriLivelliEtrEnabled(UserContext userContext, Parametri_livelliBulk parLiv) throws ComponentException{
		try{
			Classificazione_vociHome home = (Classificazione_vociHome)getHome(userContext, Classificazione_vociBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, parLiv.getEsercizio());
			sql.addSQLClause("AND", "TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	/*
	 * Metodo che verifica se è possibile modificare la struttura dei parametri livelli relativamente
	 * alla parte SPESE.
	 * Ritorna FALSE se esistono classificazioni di Spesa
	 * Ritorna TRUE  se non esistono classificazioni di Spesa 
	 */
	public boolean isParametriLivelliSpeEnabled(UserContext userContext, Parametri_livelliBulk parLiv) throws ComponentException{
		try{
			Classificazione_vociHome home = (Classificazione_vociHome)getHome(userContext, Classificazione_vociBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, parLiv.getEsercizio());
			sql.addSQLClause("AND", "TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public Parametri_livelliBulk getParametriLivelli(UserContext userContext, Integer esercizio) throws ComponentException{
		try{
			Parametri_livelliHome home = (Parametri_livelliHome)getHome(userContext, Parametri_livelliBulk.class);
			Parametri_livelliBulk bulk = (Parametri_livelliBulk)home.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
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
			Parametri_livelliHome home = (Parametri_livelliHome)getHome(userContext, Parametri_livelliBulk.class);
			Parametri_livelliBulk bulkLivelli = (Parametri_livelliBulk)home.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
			if (bulkLivelli == null)
			  throw new ApplicationException("Parametri Livelli non presenti per l'anno "+esercizio);						
			getHomeCache(userContext).fetchAll(userContext,home);
			if (tipo.equals(Utility.TIPO_GESTIONE_SPESA))
			  return bulkLivelli.getDs_livello_spe(bulkCNR.getLivello_pdg_decis_spe().intValue());
			else if (tipo.equals(Utility.TIPO_GESTIONE_ENTRATA))
			  return bulkLivelli.getDs_livello_etr(bulkCNR.getLivello_pdg_decis_spe().intValue());
			return "";  
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}	

	/**
	 * Restituisce la descrizione del livello di Contrattazione Spese definito nei Parametri CNR
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	public String getDescrizioneLivelloContrSpese(UserContext userContext, Integer esercizio) throws ComponentException{
		try{
			Parametri_cnrBulk bulkCNR = (Parametri_cnrBulk)getHome(userContext, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(esercizio));		
			if (bulkCNR == null)
			  throw new ApplicationException("Parametri CNR non presenti per l'anno "+esercizio);
			Parametri_livelliHome home = (Parametri_livelliHome)getHome(userContext, Parametri_livelliBulk.class);
			Parametri_livelliBulk bulkLivelli = (Parametri_livelliBulk)home.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
			if (bulkLivelli == null)
			  throw new ApplicationException("Parametri Livelli non presenti per l'anno "+esercizio);						
			getHomeCache(userContext).fetchAll(userContext,home);
		  	return bulkLivelli.getDs_livello_spe(bulkCNR.getLivello_contratt_pdg_spe().intValue());
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}	
}