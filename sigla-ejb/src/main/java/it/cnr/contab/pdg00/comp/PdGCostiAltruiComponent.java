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

package it.cnr.contab.pdg00.comp;

import java.io.Serializable;
import java.util.Vector;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IMultipleCRUDMgr;

/**
  * Componente di gestione nel servente dei costi caricati da altra UO (Spese)
  */

public class PdGCostiAltruiComponent extends PdGComponent implements Cloneable,Serializable,IPdGCostiAltruiMgr,it.cnr.jada.comp.IMultipleCRUDMgr {
    public  PdGCostiAltruiComponent()
    {

        /*Default constructor*/


    }
    //^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio caricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
//^^@@
	public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException {
		throw new ApplicationException("Non è possibile eliminare il dettaglio caricato!");
	}

    //^^@@
/** 
  *  Piano di gestione chiuso
  *    PreCondition:
  *      Il piano di gestione del servito o del servente è chiuso (vedi checkChiusuraPdg)
  *    PostCondition:
  *      Restituisce un'eccezione
  *  Controllo livello di responsabilità
  *    PreCondition:
  *      L'utente che effettua la richiesta non ha un livello di responsabilità sufficiente per la modifica di un dettaglio nel centro servente (vedi checkLivelloResponsabilita)
  *    PostCondition:
  *      Restituisce un'eccezione
  *  default
  *    PreCondition:
  *      Viene richiesto di aggiornare lo stato del dettaglio
  *    PostCondition:
  *      Lo stato del dettaglio è aggiornato sulla base della scelta dell'utente sul servito e sul servente
 */
//^^@@	
	public OggettoBulk eseguiModificaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

		try {
			Pdg_preventivo_spe_detBulk pdgSpe = (Pdg_preventivo_spe_detBulk)bulk;

	        Pdg_preventivoBulk pdg = checkChiusuraPdg(userContext,pdgSpe);

	        if (isPdgApertoPerVariazioni(userContext,pdg) && Pdg_preventivo_spe_detBulk.ST_NESSUNA_AZIONE.equals(pdgSpe.getStato())) {
				Pdg_preventivo_spe_detBulk pdgSpeOriginale = (Pdg_preventivo_spe_detBulk)getHome(userContext,pdgSpe).findByPrimaryKey(pdgSpe);
				if (!pdgSpe.getStato().equals(pdgSpeOriginale.getStato()))
					throw new it.cnr.jada.comp.ApplicationException("Non è possibile riportare lo stato del dettaglio a 'Nessuna azione' in fase di variazione del pdg.");
	        }

			// 05/09/2003
			// Aggiunto controllo sulla chiusura dell'esercizio
			if (isEsercizioChiuso(userContext,pdg))
				throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare dettagli del pdg ad esercizio chiuso.");

            CdrBulk cdr = cdrFromUserContext(userContext);

         	checkLivelloResponsabilita(userContext, cdr, pdg);
	
			Pdg_preventivo_spe_detBulk dettaglioCentroServito = (Pdg_preventivo_spe_detBulk)getHome(userContext,Pdg_preventivo_spe_detBulk.class).findByPrimaryKey(
				new Pdg_preventivo_spe_detBulk(
					pdgSpe.getCd_centro_responsabilita_clgs(),
					pdgSpe.getCd_elemento_voce_clgs(),
					pdgSpe.getCd_linea_attivita_clgs(),
					pdgSpe.getEsercizio(),
					pdgSpe.getPg_spesa_clgs(),
					pdgSpe.getTi_appartenenza_clgs(),
																																				pdgSpe.getTi_gestione_clgs() ) );
            Pdg_preventivoBulk pdgServente = checkChiusuraPdg(userContext,dettaglioCentroServito);

			dettaglioCentroServito.setStato(pdgSpe.getStato());
			
			updateBulk(userContext, dettaglioCentroServito);

			super.eseguiModificaConBulk(userContext, bulk);
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}

		return bulk;
	}

/**	
  * Costruisce un SQL builder per l'estrazione dei dettagli di carico da altra uo nel servente
  */
	protected it.cnr.jada.persistency.sql.Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		if (clauses == null) clauses = bulk.buildFindClauses(null);
		if (clauses == null) clauses = new it.cnr.jada.persistency.sql.CompoundFindClause();

		clauses.addClause("AND",
						  "esercizio",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  ((it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)bulk).getEsercizio()
						 );
		clauses.addClause("AND",
						  "cd_centro_responsabilita",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  ((it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk)bulk).getCd_centro_responsabilita()
						 );
		clauses.addClause("AND",
						  "categoria_dettaglio",
						  it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk.CAT_CARICO
						 );
		//clauses.addClause("AND",
						  //"stato",
						  //it.cnr.jada.persistency.sql.SQLBuilder.EQUALS,
						  //it.cnr.contab.pdg00.bulk.Pdg_preventivo_detBulk.ST_NESSUNA_AZIONE
						 //);

		return super.select(userContext,clauses,bulk);
	}

}
