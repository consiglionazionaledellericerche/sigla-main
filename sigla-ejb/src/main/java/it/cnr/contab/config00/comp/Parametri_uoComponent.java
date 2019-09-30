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
 * Created on Sep 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.comp;

import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_uoBulk;
import it.cnr.contab.config00.bulk.Parametri_uoHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.bp.ModuloAttivitaBP;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Parametri_uoComponent extends CRUDComponent {
	public OggettoBulk creaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		validaGestioneModuli(usercontext, (Parametri_uoBulk)oggettobulk);
		return super.creaConBulk(usercontext, oggettobulk);
	}

	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		validaGestioneModuli(usercontext, (Parametri_uoBulk)oggettobulk);
		return super.modificaConBulk(usercontext, oggettobulk);
	}

	/**
	 *  
	 * Verifica, in caso di disattivazione della gestione, se è possibile effettuarla
	 * Se è stata già avviata la gestione non è possibile modificare il valore.  
	 * 
	 * TODO Metodo da implementare
	 * 
	 * 
	 * @param userContext
	 * @param parUo
	 * @throws ComponentException
	 */
	private void validaGestioneModuli(UserContext userContext, Parametri_uoBulk parUo) throws ComponentException{
		if (parUo.getFl_gestione_moduli().booleanValue() == false) {
			if (parUo.getPg_modulo_default()==null)
				throw new ApplicationException("Attenzione! Non essendo attivata la Gestione Moduli è ' " +
					"obbligatorio indicare almeno un modulo di default che sarà automaticamente utilizzato " +
					"nelle movimentazioni.");
		}
	}

	/**
	 * Verifica se è possibile modificare il valore del flag 'Gestione Moduli'
	 * 
	 * @param userContext
	 * @param parCnr
	 * @return
	 * @throws ComponentException
	 */
	public boolean isFlGestioneModuliEnabled(UserContext userContext, Parametri_uoBulk parUo) throws ComponentException{
		return true;
	}
	/**
	 * Ritorna la Uo dell'esercizio corrente
	 * 
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	public Parametri_uoBulk getParametriUo(UserContext userContext, Integer esercizio, Unita_organizzativaBulk uo) throws ComponentException{
		try{
			Parametri_uoHome home = (Parametri_uoHome)getHome(userContext, Parametri_uoBulk.class);
			Parametri_uoBulk bulk = (Parametri_uoBulk)home.findByPrimaryKey(new Parametri_uoBulk(uo.getCd_unita_organizzativa(), esercizio));
			getHomeCache(userContext).fetchAll(userContext,home);
			return bulk;
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	/**
	 *  Normale
	 *    PreCondition:
	 *      E' stata generata la richiesta di inserire/aggiornare il Modulo di Attività di default.
	 *    PostCondition:
	 *      Viene restituita una query sulla tabella PROGETTO con le clausole specificate
	 */
	public SQLBuilder selectModulo_defaultByClause(UserContext userContext,Parametri_uoBulk parUo,ProgettoBulk modulo,CompoundFindClause clause) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = getHome(userContext,modulo).createSQLBuilder();
		sql.addClause(clause);
		
		sql.addSQLClause("AND","livello",sql.EQUALS,modulo.LIVELLO_PROGETTO_TERZO);
		
		return sql;
	}
}