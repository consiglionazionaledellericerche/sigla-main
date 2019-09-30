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

package it.cnr.contab.prevent00.comp;

import java.sql.PreparedStatement;

import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.contab.pdg00.bulk.*;
import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class PdgAggregatoComponent extends CRUDComponent implements IPdgAggregatoMgr {
/**
 * PdgAggregatoComponent constructor comment.
 */
public PdgAggregatoComponent() {
	super();
}
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il caricamento della testata di un pdg aggregato
  *    PostCondition:
  *      Viene cercato e caricato un Pdg_aggregatoBulk usando l'esercizio definito da userContext e il cdr specificato
 */
public OggettoBulk caricaPdg_aggregato (UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws ComponentException {
	try {
		
		CdrBulk cdrUtente = cdrFromUserContext(userContext);
		CdrBulk cdr = pdg_aggregato == null ? null : pdg_aggregato.getCdr();

		if (cdr == null && cdrUtente == null) 
			throw new ApplicationException("L'utente non è configurato correttamente per l'utilizzo del pdg aggregato");

		if (cdr == null)
			cdr = cdrUtente;
		
		BulkHome home = getHome(userContext,Pdg_aggregatoBulk.class);
		pdg_aggregato = (Pdg_aggregatoBulk)home.findByPrimaryKey(
			new Pdg_aggregatoBulk(
				cdr.getCd_centro_responsabilita(),
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)));

		if (pdg_aggregato == null)
			if (isCdrEnte(userContext,cdrUtente))
				return new Pdg_aggregatoBulk(null,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			else
				throw new ApplicationException("Pdg aggregato non esistente per il CDR selezionato");

		initializeKeysAndOptionsInto(userContext,pdg_aggregato);
		getHomeCache(userContext).fetchAll(userContext);

		// Lo stato del pdg aggregato è modificabile solo
		// dal cdr ente o dal cdr stesso (solo in stato B o E)
		if (isCdrEnte(userContext,cdrUtente) ||
			pdg_aggregato.STATO_B.equals(pdg_aggregato.getStato()) ||
			pdg_aggregato.STATO_E.equals(pdg_aggregato.getStato()))
			return pdg_aggregato;

		return asRO(pdg_aggregato,"Utente non abilitato alla modifica del pdg aggregato");
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
	private CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {

		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );

			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
private void checkAttualizzScrAltraUo(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws ComponentException {
	try {			
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB050.checkAttualizzScrAltraUo(?, ?)}",false,this.getClass());
		try {
			cs.setInt( 1, pdg_aggregato.getEsercizio().intValue() );
			cs.setString( 2, pdg_aggregato.getCd_centro_responsabilita());
			cs.execute();
		} finally {
		    cs.close();
		}	
	} catch (Throwable e) {
		throw handleException(e);
	}
}

/**
 *  Esistono pdg dipendenti in stato diverso da quelli specificati
 *    PreCondition:
 *      Esiste almeno un pdg figlio del pdg specificato che si trova in uno stato diverso da quelli specificificati
 *    PostCondition:
 *      Genera una ApplicationException con il messaggio "Il pdg del cdr XXX.XXX.XXX è in stato Y che non è compatibile con l'operazione richiesta."
 *  Tutti i controlli superati
 *    PreCondition:
 *      Nessun'altra precondizione verificata
 *    PostCondition:
 *      Esce senza alcuna eccezione
 */
private boolean controllaStatoPdgFigliPadre(UserContext userContext,Pdg_preventivoBulk pdg,String[] stati) throws ComponentException {
	try {
		it.cnr.jada.bulk.BulkHome home = getHome(userContext, Pdg_preventivoBulk.class);
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("V_PDG_CDR_FIGLI_PADRE");
		sql.addSQLJoin("PDG_PREVENTIVO.ESERCIZIO","V_PDG_CDR_FIGLI_PADRE.ESERCIZIO");
		sql.addSQLJoin("PDG_PREVENTIVO.CD_CENTRO_RESPONSABILITA","V_PDG_CDR_FIGLI_PADRE.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND", "V_PDG_CDR_FIGLI_PADRE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND", "V_PDG_CDR_FIGLI_PADRE.CD_CDR_ROOT", sql.EQUALS, pdg.getCd_centro_responsabilita());
		sql.setForUpdateOf("stato");

		LoggableStatement stm = sql.prepareStatement(getConnection(userContext));
		try {
			java.sql.ResultSet rs = stm.executeQuery();
			try {
				while (rs.next()) {
					String stato_figlio = rs.getString("STATO");
					boolean trovato = false;
					for (int i = 0;!trovato && i < stati.length;i++)
						trovato = stati[i].equals(stato_figlio);

					if (!trovato)
							throw new it.cnr.jada.comp.ApplicationException("Il pdg del cdr "+rs.getString("CD_CENTRO_RESPONSABILITA")+" è in stato "+stato_figlio+" che non è compatibile con l'operazione richiesta.");
				}
			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} finally {
			try{stm.close();}catch( java.sql.SQLException e ){};
		}
		return true;
	} catch(Throwable e) {
		throw handleException(e);
	}
}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei CDS compatibili per il dettaglio di spesa specificato con un elenco di clausole specificate
  *      
  *    PostCondition:
  *      Viene restituito l'elenco dei CDS che compaiono nei dettagli di spesa del PDG specificato
 */
//^^@@

	public java.util.Collection findCdss(UserContext userContext,Pdg_aggregato_spe_detBulk spe_det) throws ComponentException, IntrospectionException, PersistencyException
	{
		BulkHome home = getHome(userContext,CdsBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.setDistinctClause(true);
		sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"PDG_AGGREGATO_SPE_DET WHERE ( PDG_AGGREGATO_SPE_DET.CD_CDS = UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA) AND (PDG_AGGREGATO_SPE_DET.ESERCIZIO = "+spe_det.getEsercizio()+" ) AND ( PDG_AGGREGATO_SPE_DET.CD_CENTRO_RESPONSABILITA = '"+spe_det.getCdr().getCd_centro_responsabilita()+"' ) )");
		return home.fetchAll(sql);
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei titoli compatibili per il dettaglio di spesa specificato con un elenco di clausole specificate
  *  	 Questo per tutti i CDS tranne il SAC    
  *    PostCondition:
  *      Viene restituita una query sulla tabella ELEMENTO_VOCE con le clausole specificate più: 
  *      esercizio= dettaglioSpesa.esercizio
  *      ti_appartenenza = 'D' (APPARTENENZA_CDS)
  *      ti_gestione = 'S' ( GESTIONE_SPESE )
  *      ti_elemento_voce = 'T'  ( TITOLO )
  *      
 */
//^^@@

	public java.util.Collection findElementi_voci(UserContext userContext,Pdg_aggregato_spe_detBulk dettaglioSpesa) throws IntrospectionException, ComponentException, PersistencyException {
		BulkHome home = getHome(userContext,Elemento_voceBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause("AND","esercizio",sql.EQUALS,dettaglioSpesa.getEsercizio());
		sql.addClause("AND","ti_appartenenza",sql.EQUALS,Elemento_voceHome.APPARTENENZA_CDS);
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
		sql.addClause("AND","ti_elemento_voce",sql.EQUALS,Elemento_voceHome.TIPO_TITOLO);
		return home.fetchAll(sql);
	}

private Pdg_preventivoBulk getPdg_preventivo(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws ComponentException {
	try {
		return 
			(Pdg_preventivoBulk)getHome(userContext,Pdg_preventivoBulk.class).findAndLock(
				new Pdg_preventivoBulk(
					pdg_aggregato.getEsercizio(),
					pdg_aggregato.getCd_centro_responsabilita()));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
	/**
	 * Metodo per i controlli da effettuare su un oggetto di tipo {@link it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk } .
	 *
	 * Nome: Controlli per modifica;
	 * Implementa tutti i controlli che garantiscano l'integrita del record rappresentato dall oggetto.
	 *
	 * @param pdgEtr Oggetto da analizzare.
	 *
	 * @return L'oggetto controllato.
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 */

	private OggettoBulk initEtr(UserContext userContext,Pdg_aggregato_etr_detBulk pdgEtr) throws it.cnr.jada.comp.ComponentException {
		if(
			   (pdgEtr.getIm_ra_rce() == null)
			|| (pdgEtr.getIm_rb_rse() == null)
			|| (pdgEtr.getIm_rc_esr() == null)
			|| (pdgEtr.getIm_rd_a2_ricavi() == null)
			|| (pdgEtr.getIm_re_a2_entrate() == null)
			|| (pdgEtr.getIm_rf_a3_ricavi() == null)
			|| (pdgEtr.getIm_rg_a3_entrate() == null)
	    )
			throw new it.cnr.jada.comp.ApplicationException("Alcuni importi non sono stati specificati");

		
		return pdgEtr;
	}

	/**
	 * Metodo per i controlli da effettuare su un oggetto di tipo {@link it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk } .
	 *
	 * Nome: Controlli per modifica;
	 * Implementa tutti i controlli che garantiscano l'integrita del record rappresentato dall oggetto.
	 *
	 * @param pdg Oggetto da analizzare.
	 *
	 * @return L'oggetto controllato.
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 */

	private OggettoBulk initSpe(UserContext userContext,Pdg_aggregato_spe_detBulk pdgSpe) throws it.cnr.jada.comp.ComponentException {
		// Controllo che non ci siano campi a null

		if (
		       (pdgSpe.getIm_raa_a2_costi_finali() == null)
			|| (pdgSpe.getIm_rab_a2_costi_altro_cdr() == null)
            || (pdgSpe.getIm_rac_a2_spese_odc() == null)
            || (pdgSpe.getIm_rad_a2_spese_odc_altra_uo() == null)
            || (pdgSpe.getIm_rae_a2_spese_ogc() == null)
            || (pdgSpe.getIm_raf_a2_spese_ogc_altra_uo() == null)
            || (pdgSpe.getIm_rag_a2_spese_costi_altrui() == null)
			|| (pdgSpe.getIm_rah_a3_costi_finali() == null)
			|| (pdgSpe.getIm_rai_a3_costi_altro_cdr() == null)
			|| (pdgSpe.getIm_ral_a3_spese_odc() == null)
			|| (pdgSpe.getIm_ram_a3_spese_odc_altra_uo() == null)
			|| (pdgSpe.getIm_ran_a3_spese_ogc() == null)
			|| (pdgSpe.getIm_rao_a3_spese_ogc_altra_uo() == null)
			|| (pdgSpe.getIm_rap_a3_spese_costi_altrui() == null)
			|| (pdgSpe.getIm_rh_ccs_costi() == null)
			|| (pdgSpe.getIm_ri_ccs_spese_odc() == null)
			|| (pdgSpe.getIm_rj_ccs_spese_odc_altra_uo() == null)
			|| (pdgSpe.getIm_rk_ccs_spese_ogc() == null)
			|| (pdgSpe.getIm_rl_ccs_spese_ogc_altra_uo() == null)
			|| (pdgSpe.getIm_rm_css_ammortamenti() == null)
			|| (pdgSpe.getIm_rn_css_rimanenze() == null)
			|| (pdgSpe.getIm_ro_css_altri_costi() == null)
			|| (pdgSpe.getIm_rp_css_verso_altro_cdr() == null)
			|| (pdgSpe.getIm_rq_ssc_costi_odc() == null)
			|| (pdgSpe.getIm_rr_ssc_costi_odc_altra_uo() == null)
			|| (pdgSpe.getIm_rs_ssc_costi_ogc() == null)
			|| (pdgSpe.getIm_rt_ssc_costi_ogc_altra_uo() == null)
			|| (pdgSpe.getIm_ru_spese_costi_altrui() == null)
			|| (pdgSpe.getIm_rv_pagamenti() == null)
		   )
			throw new it.cnr.jada.comp.ApplicationException("Alcuni importi non sono stati specificati");

        // Controllo di quadratura dei costi con spese		
        
		if(pdgSpe.getIm_rh_ccs_costi().compareTo(
						pdgSpe.getIm_ri_ccs_spese_odc().add(
							pdgSpe.getIm_rj_ccs_spese_odc_altra_uo().add(
								pdgSpe.getIm_rk_ccs_spese_ogc().add(
									pdgSpe.getIm_rl_ccs_spese_ogc_altra_uo()
								)
							)
						)
				) != 0
		)
		{
			throw new it.cnr.jada.comp.ApplicationException("Il campo \"Costi\" non contiene la somma dei campi relativi ai Costi Con Spese");
		}


		return pdgSpe;
	}

//^^@@
/** 
  *  Dettaglio entrate modificato non esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_etr_detBulk e 
  *      non esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_etr_det_inizialeBulk inizializzato creando un nuovo Pdg_aggregato_etr_detBulk con le stessa chiave (e ti_aggregato = "M") e gli stessi importi del dettaglio passato
  *  Dettaglio spese modificato non esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_spe_detBulk e 
  *      non esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_spe_det_inizialeBulk inizializzato creando un nuovo Pdg_aggregato_spe_detBulk con le stessa chiave (e ti_aggregato = "M") e gli stessi importi del dettaglio passato
  *  Dettaglio entrate modificato esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_etr_detBulk e 
  *      esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_etr_det_inizialeBulk con il Pdg_aggregato_etr_detBulk corrispondente
  *  Dettaglio spese modificato esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_spe_detBulk e 
  *      esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_spe_det_inizialeBulk con il Pdg_aggregato_spe_detBulk corrispondente
 */
//^^@@
public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try {
		bulk = super.inizializzaBulkPerModifica(userContext,bulk);
		
		// identifica il bulk corrente (etr o spe)
		if (bulk instanceof Pdg_aggregato_etr_det_inizialeBulk) { // è il bulk delle entrate o una sua sottoclasse
			// istanzia il bulk per le entrate e il bulk-home 
			Pdg_aggregato_etr_det_inizialeBulk etr_det = (Pdg_aggregato_etr_det_inizialeBulk)bulk;

			BulkHome home = getHome(userContext,Pdg_aggregato_etr_detBulk.class);
			/* istanzia un key-bulk per le entrate "modificate" settando la kiave: cdr, voce, natura, esercizio, modificabile, 
			   appartenenza e gestione; con questo bulk si accede alla relativa tabella cercando una riga che contenga le
			   modifiche ("M"): l'accesso avverrà tramite quella chiave */
			Pdg_aggregato_etr_detBulk etr_det_mod_key = new Pdg_aggregato_etr_detBulk(
				etr_det.getCd_centro_responsabilita(),
				etr_det.getCd_elemento_voce(),
				etr_det.getCd_natura(),
				etr_det.getEsercizio(),
				"M",
				etr_det.getTi_appartenenza(),
				etr_det.getTi_gestione());

			// istanzia il bulk per le entrate modificate fetchando il record con la kiave di cui sopra
			Pdg_aggregato_etr_detBulk etr_det_mod = (Pdg_aggregato_etr_detBulk)home.findByPrimaryKey(etr_det_mod_key);

			// vede se il record fetchato è vuoto (= non sono state registrate modifiche sino ad ora)
			if (etr_det_mod == null) {
				etr_det_mod = etr_det_mod_key; // scrive nel bulk la kiave del record degli importi modificati
				etr_det_mod.copiaImportiDa(etr_det); // setta gli importi proposti = agli importi imposti
			}
			// salva comunque le modifiche
			etr_det.setEtr_modificato(etr_det_mod);
		
		} else if (bulk instanceof Pdg_aggregato_spe_det_inizialeBulk) { // è il bulk delle spese o una sua sottoclasse
			// come nel ramo if, solo che ci si riferisce alle spese invece che alle entrate
			Pdg_aggregato_spe_det_inizialeBulk spe_det = (Pdg_aggregato_spe_det_inizialeBulk)bulk;

			BulkHome home = getHome(userContext,Pdg_aggregato_spe_detBulk.class);

			Pdg_aggregato_spe_detBulk spe_det_mod_key = new Pdg_aggregato_spe_detBulk(
				spe_det.getCd_cds(),
				spe_det.getCd_centro_responsabilita(),
				spe_det.getCd_elemento_voce(),
				spe_det.getCd_funzione(),
				spe_det.getCd_natura(),
				spe_det.getEsercizio(),
				"M",
				spe_det.getTi_appartenenza(),
				spe_det.getTi_gestione());

			Pdg_aggregato_spe_detBulk spe_det_mod = (Pdg_aggregato_spe_detBulk)home.findByPrimaryKey(spe_det_mod_key);
			
			if (spe_det_mod == null) {
				spe_det_mod = spe_det_mod_key;
				spe_det_mod.copiaImportiDa(spe_det);
			}

			spe_det.setSpe_modificato(spe_det_mod);
		}

		Pdg_aggregato_det pdg_aggregato_det = (Pdg_aggregato_det)bulk;
		Pdg_aggregatoBulk pdg_aggregato = (Pdg_aggregatoBulk)getHome(userContext,Pdg_aggregatoBulk.class).findByPrimaryKey(new Pdg_aggregatoBulk(
			pdg_aggregato_det.getCd_centro_responsabilita(),
			pdg_aggregato_det.getEsercizio()));

		if (!pdg_aggregato.STATO_A.equals(pdg_aggregato.getStato()) &&
			!pdg_aggregato.STATO_M.equals(pdg_aggregato.getStato()))
			return asRO(bulk,"Il dettaglio non è modificabile perchè il pdg aggregato è in stato "+pdg_aggregato.getStato());


		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg_aggregato))
			return asRO(bulk,"Dettaglio non modificabile ad esercizio chiuso.");

		else if (!isUtenteEnte(userContext))
			return asRO(bulk,null);

		return bulk;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
private boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException {
	try {
		getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
		return cdr.isCdrAC();
	} catch(Throwable e) {
		throw handleException(e);
	}
}
protected boolean isEsercizioChiuso(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws ComponentException {
	try {
		EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
		CdrBulk cdr = (CdrBulk)getHome(userContext,pdg_aggregato.getCdr()).findByPrimaryKey(pdg_aggregato.getCdr());
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
		return home.isEsercizioChiuso(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),uo.getCd_unita_padre());
	} catch(PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Utente non AC
  *    PreCondition:
  *      L'utente non è l'Amministrazione Centrale (utente.cdr.livello <> 1 o utente.cdr.unita_organizzativa.cd_tipo_unita <> 'ENTE')
  *    PostCondition:
  *      Ritorna false
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Ritorna true
 */
public boolean isPdGAggregatoModificabile(UserContext userContext,Pdg_aggregatoBulk pdg) throws ComponentException {
	return isUtenteEnte(userContext);
}
private boolean isStatoCompatibile(String vecchioStato,String nuovoStato) {
	if (Pdg_aggregatoBulk.STATO_A.equals(vecchioStato))
		return Pdg_aggregatoBulk.STATO_B.equals(nuovoStato);
	if (Pdg_aggregatoBulk.STATO_B.equals(vecchioStato))
		return Pdg_aggregatoBulk.STATO_M.equals(nuovoStato);
	if (Pdg_aggregatoBulk.STATO_M.equals(vecchioStato))
		return Pdg_aggregatoBulk.STATO_E.equals(nuovoStato);
	if (Pdg_aggregatoBulk.STATO_E.equals(vecchioStato))
		return 
			Pdg_aggregatoBulk.STATO_B.equals(nuovoStato) ||
			Pdg_aggregatoBulk.STATO_M.equals(nuovoStato);
	return false;
}
private boolean isStatoPdGAggregatoModificabile(UserContext userContext,CdrBulk cdr_utente,Pdg_aggregatoBulk pdg) throws ComponentException {
	if (pdg.STATO_M.equals(pdg.getStato()))
		return cdr_utente.isCdrAC();
	return true;
}
private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
	return isCdrEnte(userContext,cdrFromUserContext(userContext));
}
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "L'utente non è abilitato alla visualizzazione dei PDG aggregati"
  *  Utente AC
  *    PreCondition:
  *      userContext.utente.LIVELLO = 1 e userContext.cdr.unita_organizzativa.cd_tipo_unita = 'ENTE'
  *    PostCondition:
  *      Restituisce una collezione con l'elenco di tutti i CDR di I livello per l'esercizio selezionato per cui esiste un PDG
  *  Utente CDR I livello
  *    PreCondition:
  *      Il cdr dell'utente è di I livello (LIVELLO = 1)
  *    PostCondition:
  *      Restituisce una collezione contenente il solo cdr dell'utente
  *  Utente AREA
  *    PreCondition:
  *      Il cdr dell'utente è un'area di ricerca (livello = II e tipo unità = 'AREA')
  *    PostCondition:
  *      Restituisce una collezione contenente il solo cdr dell'utente
 */
//^^@@
public SQLBuilder listaCdrPdGAggregatoPerUtente(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	try {
		it.cnr.contab.utenze00.bp.CNRUserContext uc = (it.cnr.contab.utenze00.bp.CNRUserContext)userContext;
		BulkHome utenteHome = getHome(userContext,UtenteBulk.class);
		UtenteBulk utente = (UtenteBulk)utenteHome.findByPrimaryKey(new UtenteKey(userContext.getUser()));
		if (utente == null)
			throw new ApplicationException("Utente non abilitato alla funzione");
		CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class);
		CdrBulk cdr = (CdrBulk)cdrHome.findByPrimaryKey(new CdrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext)));
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa()));
		if (cdr.getLivello().intValue() == 1 && Tipo_unita_organizzativaHome.TIPO_UO_ENTE.equalsIgnoreCase(uo.getCd_tipo_unita())) {
			cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_PDG_AGGREGATO");
			SQLBuilder sql = cdrHome.createSQLBuilder();
			sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"PDG_AGGREGATO WHERE ( PDG_AGGREGATO.ESERCIZIO = V_CDR_PDG_AGGREGATO.ESERCIZIO ) AND ( PDG_AGGREGATO.CD_CENTRO_RESPONSABILITA = V_CDR_PDG_AGGREGATO.CD_CENTRO_RESPONSABILITA ) )");
			sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			return sql;
		} else {
			cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_PDG_AGGREGATO");
			SQLBuilder sql = cdrHome.createSQLBuilder();
			sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"PDG_AGGREGATO WHERE ( PDG_AGGREGATO.ESERCIZIO = V_CDR_PDG_AGGREGATO.ESERCIZIO ) AND ( PDG_AGGREGATO.CD_CENTRO_RESPONSABILITA = V_CDR_PDG_AGGREGATO.CD_CENTRO_RESPONSABILITA ) )");
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());
			sql.addSQLClause("AND","ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			return sql;
		}
/*
		if (cdr.getLivello().intValue() == 1)
			return java.util.Collections.singleton(cdr);
		if (cdr.getLivello().intValue() == 2 && Tipo_unita_organizzativaHome.TIPO_UO_AREA.equalsIgnoreCase(uo.getCd_tipo_unita()))
			return java.util.Collections.singleton(cdr);
		return java.util.Collections.EMPTY_LIST;
*/		
	} catch(Throwable e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondition è verificata
  *    PostCondition:
  *      Dal dettaglio di spesa o entrata specificato viene estratto il dettaglio contenente gli importi modificati e quest'ultimo viene reso persistente.
 */
//^^@@
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	try {
		Pdg_aggregatoBulk pdg_aggregato = (Pdg_aggregatoBulk)getHome(userContext,Pdg_aggregatoBulk.class).findByPrimaryKey(new Pdg_aggregatoBulk(
			((Pdg_aggregato_det)bulk).getCd_centro_responsabilita(),
			((Pdg_aggregato_det)bulk).getEsercizio()));

		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg_aggregato))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare dettagli del pdg aggregato ad esercizio chiuso.");

		if (bulk instanceof Pdg_aggregato_etr_det_inizialeBulk) {
			
			Pdg_aggregato_etr_det_inizialeBulk etr_det = (Pdg_aggregato_etr_det_inizialeBulk)bulk;

            initEtr(userContext, etr_det.getEtr_modificato());
			
			if (etr_det.getEtr_modificato().getCrudStatus() == OggettoBulk.UNDEFINED)
				etr_det.getEtr_modificato().setToBeCreated();
			else
				etr_det.getEtr_modificato().setToBeUpdated();
			etr_det.getEtr_modificato().setUser(userContext.getUser());
			makeBulkPersistent(userContext,etr_det.getEtr_modificato());
		} else if (bulk instanceof Pdg_aggregato_spe_det_inizialeBulk) {
			Pdg_aggregato_spe_det_inizialeBulk spe_det = (Pdg_aggregato_spe_det_inizialeBulk)bulk;

			initSpe(userContext, spe_det.getSpe_modificato());
			
			if (spe_det.getSpe_modificato().getCrudStatus() == OggettoBulk.UNDEFINED)
				spe_det.getSpe_modificato().setToBeCreated();
			else
				spe_det.getSpe_modificato().setToBeUpdated();
			spe_det.getSpe_modificato().setUser(userContext.getUser());
			makeBulkPersistent(userContext,spe_det.getSpe_modificato());
		}
		return bulk;
	} catch(java.lang.Exception e) {
		throw handleException(e);
	}
}
public OggettoBulk modificaStatoPdg_aggregato(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {
    try {
		// 05/09/2003
		// Aggiunto controllo sulla chiusura dell'esercizio
		if (isEsercizioChiuso(userContext,pdg_aggregato))
			throw new ApplicationException("Stato del pdg aggregato non modificabile ad esercizio chiuso.");

		Pdg_aggregatoBulk oldPdg_aggregato = (Pdg_aggregatoBulk)getHome(userContext,pdg_aggregato).findAndLock(pdg_aggregato);

		String nuovoStato = pdg_aggregato.getStato();
		String vecchioStato = oldPdg_aggregato.getStato();

		if (!isStatoCompatibile(vecchioStato,nuovoStato))
			throw new ApplicationException("Lo stato "+nuovoStato+" non è compatibile con lo stato attuale del pdg aggregato ("+vecchioStato+")");

		// 09/06/2005
		// Aggiunto controllo sul cambiamento di stato da quello finale, obbligo l'utilizzo delle variazioni al PDG
		if (vecchioStato.equals(Pdg_aggregatoBulk.STATO_B))
			throw new it.cnr.jada.comp.ApplicationException("Non è possibile modificare lo stato del pdg aggregato poichè è in stato Finale.");
		
		pdg_aggregato.setUser(userContext.getUser());

		// Invoco il metodo modificaStato_x_y()
		try {
			it.cnr.jada.util.Introspector.invoke(
				this,
				"modificaStatoPdg_aggregato_"+vecchioStato+"_"+nuovoStato,
				new Object[] {
					userContext,
					//cdrUtente,
					pdg_aggregato
				});
		} catch(java.lang.reflect.InvocationTargetException ex) {
			throw ex.getTargetException();
		}

		pdg_aggregato.setToBeUpdated();

		updateBulk(userContext,pdg_aggregato);

		return caricaPdg_aggregato(userContext,pdg_aggregato);
	} catch(ComponentException e) {
		throw e;
	} catch(Throwable e) {
		throw handleException(e);
	}
}
public void modificaStatoPdg_aggregato_A_B(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {
	Pdg_preventivoBulk pdg = getPdg_preventivo(userContext,pdg_aggregato);

	// Controllo che anche il pdg preventivo e i suoi figli siano in stato C o F
	controllaStatoPdgFigliPadre(userContext,pdg,new String[] { pdg.ST_C_CHIUSURA_II, pdg.ST_F_CHIUSO_DFNT });

	checkAttualizzScrAltraUo(userContext,pdg_aggregato);
}
/**
 * Controlla che non ci siano importi relativi ad altra UO non completamente attualizzati in spese scaricate su
 * UO servente
 * Tale controllo risulta necessario per impedire la generazione di un BILANCIO FINANZIARIO dell'ENTE che non
 * contiene le spese per costi altrui attualizzate dalla creazione di dettagli successiva all'approvazione del bilancio
 * finanziario.
 * 
 * Parametri
 *   aEs -> esercizio contabile
 *   aCdCdrPrimo -> codice del cdr di primo livello su cui effetuare il controllo
 */
public void modificaStatoPdg_aggregato_B_M(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {

	try {
		Pdg_preventivoBulk pdg = getPdg_preventivo(userContext,pdg_aggregato);

		// Controllo che anche il pdg preventivo e i suoi figli siano in stato F
		controllaStatoPdgFigliPadre(userContext,pdg,new String[] { pdg.ST_F_CHIUSO_DFNT });

		pdg.setStato(pdg.ST_M_MODIFICATO_PER_VARIAZIONI);
		pdg.setUser(userContext.getUser());
		getHome(userContext,pdg).update(pdg, userContext);

	} catch(PersistencyException e) {
		throw handleException(e);
	}	
}
public void modificaStatoPdg_aggregato_E_B(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {

	// Controllo che anche il pdg preventivo sia in stato F
	Pdg_preventivoBulk pdg = getPdg_preventivo(userContext,pdg_aggregato);
	if (!pdg.ST_F_CHIUSO_DFNT.equals(pdg.getStato()))
		throw new ApplicationException("Per poter tornare in stato B il pdg preventivo deve essere in stato F");
}
public void modificaStatoPdg_aggregato_E_M(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {

	// Controllo che anche il pdg preventivo sia in stato M
	Pdg_preventivoBulk pdg = getPdg_preventivo(userContext,pdg_aggregato);
	if (!pdg.ST_M_MODIFICATO_PER_VARIAZIONI.equals(pdg.getStato()))
		throw new ApplicationException("Per poter passare in stato M anche il pdg preventivo deve essere in stato M");
}
public void modificaStatoPdg_aggregato_M_E(UserContext userContext,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException {
}
/**
 * @param userContext Lo userContext in cui si sta eseguendo l'operazione
 * @param clauses L'albero logico delle clausole da applicare alla ricerca
 * @param bulk l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
 * 		costruite delle clausole aggiuntive (mediante il metodo <code>buildFindClauses</code> 
 * 		di OggettoBulk) che vengono aggiunte in AND alle clausole specificate.
 * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
 * 		della query.
 */
public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder aSB = (SQLBuilder)super.select(userContext,clauses,bulk);
    aSB.addSQLClause("AND", "ESERCIZIO",SQLBuilder.EQUALS, ((Pdg_aggregato_det)bulk).getEsercizio());
    aSB.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, ((Pdg_aggregato_det)bulk).getCd_centro_responsabilita());
    return aSB;
}
//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco dei centri di responsabilità compatibili con il livello di responsabilità dell'utente
  *    PostCondition:
  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del pdg specificato
 */
//^^@@

 	public SQLBuilder selectCdrByClause (UserContext userContext,
											Pdg_aggregatoBulk pdg,
											CdrBulk cdr,
											CompoundFindClause clause)
 	throws ComponentException, PersistencyException
 	{
	 	SQLBuilder aSQL = (listaCdrPdGAggregatoPerUtente (userContext));
	 	if(clause != null)
	 	 aSQL.addClause(clause);
	 	return aSQL; 
	}

//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto l'elenco degli elementi voce compatibili per il dettaglio di entrata specificato con un elenco di clausole specificate
  *    PostCondition:
  *      Viene restituita una query sulla vista V_ELEMENTO_VOCE_PDG_ETR che contiene le clausole specificate più la clausola CD_NATURA =  dettaglioSpesa.cd_natura
  *      Se il CDR non appartiene alla SAC, viene imposta l'ulteriore condizione che la voce del piano non sia riservata SAC
  */
//^^@@

public SQLBuilder selectElemento_voceByClause(UserContext userContext,
											Pdg_aggregato_etr_detBulk dettaglioEntrata,
											Elemento_voceBulk elementoVoce,
											CompoundFindClause clause)
	throws ComponentException, PersistencyException {
	SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_ETR").createSQLBuilder();
	sql.addClause(clause);
	sql.addSQLClause("AND","CD_NATURA",sql.EQUALS,dettaglioEntrata.getCd_natura());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	CdrBulk aCDR = new CdrBulk();
	aCDR.setCd_centro_responsabilita(dettaglioEntrata.getCd_centro_responsabilita());
	CdrHome aH = new CdrHome(this.getConnection(userContext));
	aCDR = (CdrBulk)aH.findByPrimaryKey(aCDR);
	Unita_organizzativaBulk aUO = new Unita_organizzativaBulk();
	aUO.setCd_unita_organizzativa(aCDR.getUnita_padre().getCd_unita_organizzativa());
	Unita_organizzativaHome aUOHome = new Unita_organizzativaHome(this.getConnection(userContext));
	aUO=(Unita_organizzativaBulk)aUOHome.findByPrimaryKey(aUO);
        if (aUO.getCd_tipo_unita() != null
         && !aUO.getCd_tipo_unita().equals(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_SAC)
     ) {
		sql.addSQLClause("AND","FL_VOCE_SAC",sql.EQUALS,"N");	      
    }        
    return sql;
}
//^^@@
/** 
  *  default
  *    PreCondition:
  *      Viene richiesta una ricerca degli elementi voce compatibili con un dettaglio di spesa del pdg aggregato
  *    PostCondition:
  *      Viene restituita una query sulla tabella degli ELEMENTI_VOCE esistenti nella tabella PDG_AGGREGATO_SPE_DET del CDR selezionato con le clausole specificate più le seguenti: 
  *      esercizio = dettaglioSpesa.getEsercizio()
  *      ti_appartenenza = dipendente dal dai dettagli presenti in aggregato )
  *      ti_gestione = 'S'  ( GESTIONE_SPESE )
  *      ti_elemento_voce = 'C' o 'T' in dipendenza del contenuto dei dettagli dell'aggregato
  *      
 */
//^^@@

	public SQLBuilder selectElemento_voceByClause(UserContext userContext,
												Pdg_aggregato_spe_detBulk dettaglioSpesa,
												Elemento_voceBulk elementoVoce,
												CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
		SQLBuilder sql = getHome(userContext, elementoVoce).createSQLBuilder();
		sql.addClause(clause);
		sql.addClause("AND","esercizio",sql.EQUALS,dettaglioSpesa.getEsercizio());
//		sql.addClause("AND","ti_appartenenza",sql.EQUALS,Elemento_voceHome.APPARTENENZA_CDS);
		sql.addClause("AND","ti_gestione",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
		sql.addSQLClause("AND","ti_elemento_voce in ('"+Elemento_voceHome.TIPO_CAPITOLO+"','"+Elemento_voceHome.TIPO_TITOLO+"')");
		sql.addSQLClause("AND","EXISTS ( SELECT 1 FROM "+it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()+"PDG_AGGREGATO_SPE_DET WHERE ( PDG_AGGREGATO_SPE_DET.CD_ELEMENTO_VOCE = ELEMENTO_VOCE.CD_ELEMENTO_VOCE ) AND ( PDG_AGGREGATO_SPE_DET.ESERCIZIO = ELEMENTO_VOCE.ESERCIZIO ) AND ( PDG_AGGREGATO_SPE_DET.TI_APPARTENENZA = ELEMENTO_VOCE.TI_APPARTENENZA ) AND ( PDG_AGGREGATO_SPE_DET.TI_GESTIONE = ELEMENTO_VOCE.TI_GESTIONE ) AND ( PDG_AGGREGATO_SPE_DET.CD_CENTRO_RESPONSABILITA = ? ) )");
		sql.addParameter(dettaglioSpesa.getCd_centro_responsabilita(),java.sql.Types.VARCHAR,0);
		return sql;
	}
}
