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
 * Created on Apr 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.segnalazioni00.comp;

import java.io.Serializable;
import java.util.Collection;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.config00.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseHome;
import it.cnr.contab.segnalazioni00.bulk.ConfrontoSiglaDwhBulk;
import it.cnr.contab.segnalazioni00.bulk.ConfrontoSiglaDwhHome;
import it.cnr.contab.segnalazioni00.bulk.Stampa_attivita_siglaBulk;
import it.cnr.contab.segnalazioni00.bulk.Stampa_confronto_sigla_dwhBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StampaConfrontoSiglaDwhComponent extends it.cnr.jada.comp.CRUDComponent implements Cloneable, Serializable, IPrintMgr {
	
	
	/**
	 * inizializzaBulkPerStampa method comment.
	 */
	
	public OggettoBulk inizializzaBulkPerStampa(UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk) throws ComponentException {
		if(bulk instanceof Stampa_confronto_sigla_dwhBulk)
			inizializzaDate(userContext, (Stampa_confronto_sigla_dwhBulk )bulk);
		if(bulk instanceof Stampa_attivita_siglaBulk)
			inizializzaBulkPerStampa(userContext, (Stampa_attivita_siglaBulk) bulk);
			return bulk;
	}
	
	
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(UserContext userContext, Stampa_attivita_siglaBulk stampa) throws it.cnr.jada.comp.ComponentException {
		inizializzaEsercizio(userContext, (Stampa_attivita_siglaBulk)stampa);
		stampa.setTerzoForPrintEnabled(false);
//		stampa.setStato(((Stampa_attivita_siglaBulk) stampa).STATO_TUTTI);
//		stampa.setTipo_attivita(((Stampa_attivita_siglaBulk) stampa).TIPO_TUTTI);
		return stampa;	
	}
	
	
	protected OggettoBulk inizializzaDate(UserContext usercontext, Stampa_confronto_sigla_dwhBulk stampa) throws ComponentException{
		try {
			ConfrontoSiglaDwhHome confHome = (ConfrontoSiglaDwhHome)getHome(usercontext, ConfrontoSiglaDwhBulk.class);
			java.util.Collection date;
			confHome.setColumnMap("STAMPA");
			date = confHome.findDt_elaborazioni((Stampa_confronto_sigla_dwhBulk)stampa);
			((Stampa_confronto_sigla_dwhBulk)stampa).setDate(date);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}	
		return stampa;	
	}
	
	
	protected OggettoBulk inizializzaEsercizio(UserContext usercontext, Stampa_attivita_siglaBulk stampa) throws ComponentException{
		try {
			Esercizio_baseHome esercizioBaseHome = (Esercizio_baseHome)getHome(usercontext, Esercizio_baseBulk.class);
			java.util.Collection esercizi;
			esercizi =  esercizioBaseHome.findEsercizi((Stampa_attivita_siglaBulk)stampa);
			((Stampa_attivita_siglaBulk)stampa).setAnni(esercizi);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		}	
		return stampa;	
	}
	
	
		/**
		 * stampaBulkPerStampa method comment.
		 */
		
	public OggettoBulk stampaConBulk(UserContext aUC, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

		if (bulk instanceof Stampa_confronto_sigla_dwhBulk)
			return  stampaConBulk(aUC, (Stampa_confronto_sigla_dwhBulk) bulk);
		if (bulk instanceof Stampa_attivita_siglaBulk)
			return bulk;
		return bulk;
	}


	public OggettoBulk stampaConBulk(UserContext userContext, Stampa_confronto_sigla_dwhBulk stampa) throws ComponentException {
		if ( stampa.getData()==null || stampa.getData().getDt_elaborazione()==null)
				throw new ApplicationException( "Il campo Data Elaborazione e' obbligatorio");
		return stampa;
	}

	public SQLBuilder selectTerzoForPrintByClause(UserContext userContext, Stampa_attivita_siglaBulk stampa, TerzoBulk terzo,CompoundFindClause clause) throws ComponentException {
		
		TerzoHome home = (TerzoHome)getHome(userContext, terzo);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause( clause );
		return sql;
	}
		

}
