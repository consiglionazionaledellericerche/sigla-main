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

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliHome;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoHome;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateHome;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
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
public class Parametri_cnrComponent extends CRUDComponent {
	public OggettoBulk creaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		validaLivelliPdgDecisionale(usercontext, (Parametri_cnrBulk)oggettobulk);
		return super.creaConBulk(usercontext, oggettobulk);
	}

	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		validaLivelliPdgDecisionale(usercontext, (Parametri_cnrBulk)oggettobulk);
		return super.modificaConBulk(usercontext, oggettobulk);
	}

	private void validaLivelliPdgDecisionale(UserContext userContext, Parametri_cnrBulk parCnr) throws ComponentException{
		try{
			Parametri_livelliHome home = (Parametri_livelliHome)getHome(userContext, Parametri_livelliBulk.class);
			Parametri_livelliBulk bulk = (Parametri_livelliBulk)home.findByPrimaryKey(new Parametri_livelliBulk(parCnr.getEsercizio()));
			if (bulk==null)
				throw new ApplicationException("Attenzione! Mancano i parametri livelli. Impostare a zero il valore del campo 'Livello Classificazione per Riparto Spese Accentrate'");
			if (parCnr.getLivello_pdg_decis_spe() != null && parCnr.getLivello_pdg_decis_spe().intValue()>0){
				if (parCnr.getLivello_pdg_decis_spe().compareTo(bulk.getLivelli_spesa())==1)
					throw new ApplicationException("Attenzione! Il campo 'Livello Classificazione Spese PDG Decisionale' non può assumere valore superiore a " + bulk.getLivelli_spesa() + ".");
			}
			if (parCnr.getLivello_pdg_decis_etr() != null && parCnr.getLivello_pdg_decis_etr().intValue()>0){
				if (parCnr.getLivello_pdg_decis_etr().compareTo(bulk.getLivelli_entrata())==1)
					throw new ApplicationException("Attenzione! Il campo 'Livello Classificazione Entrate PDG Decisionale' non può assumere valore superiore a " + bulk.getLivelli_entrata() + ".");
			}
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public boolean isLivelloPdgDecisionaleSpeEnabled(UserContext userContext, Parametri_cnrBulk parCnr) throws ComponentException{
		try{
			Pdg_piano_ripartoHome home = (Pdg_piano_ripartoHome)getHome(userContext, Pdg_piano_ripartoBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addToHeader("CLASSIFICAZIONE_VOCI");
			sql.addSQLJoin("PDG_PIANO_RIPARTO.ID_CLASSIFICAZIONE", "CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.ESERCIZIO", sql.EQUALS, parCnr.getEsercizio());
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public boolean isLivelloPdgDecisionaleEtrEnabled(UserContext userContext, Parametri_cnrBulk parCnr) throws ComponentException{
		try{
			Pdg_Modulo_EntrateHome home = (Pdg_Modulo_EntrateHome)getHome(userContext, Pdg_Modulo_EntrateBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addToHeader("CLASSIFICAZIONE_VOCI");
			sql.addSQLJoin("PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE", "CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.ESERCIZIO", sql.EQUALS, parCnr.getEsercizio());
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public boolean isLivelloPdgContrattazioneSpeEnabled(UserContext userContext, Parametri_cnrBulk parCnr) throws ComponentException{
		try{
			Pdg_contrattazione_speseHome home = (Pdg_contrattazione_speseHome)getHome(userContext, Pdg_contrattazione_speseBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
			List result = home.fetchAll( sql );
			if (result.size() > 0)
				return false;
			return true;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public Parametri_cnrBulk getParametriCnr(UserContext userContext, Integer esercizio) throws ComponentException{
		try{
			Parametri_cnrHome home = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk bulk = (Parametri_cnrBulk)home.findByPrimaryKey(new Parametri_cnrBulk(esercizio));
			getHomeCache(userContext).fetchAll(userContext,home);
			return bulk;
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
	public boolean isCofogObbligatorio(UserContext userContext) throws ComponentException {
		try {
			Parametri_cnrHome home = (Parametri_cnrHome)getHome(userContext,Parametri_cnrBulk.class);
			if (home.livelloCofogObbligatorio(userContext)!=0) 
					return true;
			
			return false;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}

}