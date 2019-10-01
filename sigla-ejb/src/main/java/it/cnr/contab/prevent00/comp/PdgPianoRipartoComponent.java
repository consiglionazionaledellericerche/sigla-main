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
 * Created on Sep 14, 2005
 *
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.comp;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.*;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoBulk;
import it.cnr.contab.prevent00.bulk.Pdg_piano_ripartoHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class PdgPianoRipartoComponent extends CRUDComponent implements Cloneable,Serializable {

	public PdgPianoRipartoComponent()
	{
		super();
	}

	public OggettoBulk modificaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk.setCrudStatus(OggettoBulk.NORMAL);
		return super.modificaConBulk(usercontext, oggettobulk);
	}

	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)	throws ComponentException {
		try {
			OggettoBulk cla = super.inizializzaBulkPerModifica(usercontext,oggettobulk);
			Classificazione_vociHome home = (Classificazione_vociHome)getHome(usercontext, cla.getClass());
			((Classificazione_vociBulk)cla).setPdgPianoRipartoSpese(new it.cnr.jada.bulk.BulkList(home.findPdgPianoRipartoSpese((Classificazione_vociBulk)oggettobulk)));
			aggiornaTotaleGeneraleImpdaRipartire(usercontext, (Classificazione_vociBulk)cla);
			getHomeCache(usercontext).fetchAll(usercontext);
			return cla;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}

	/**
	  *  Ricerca di una Classificazione Ufficiale
	  *    PreCondition:
	  *      E' stato richiesto di cercare una Classificazione Ufficiale.
	  *    PostCondition:
	  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Classificazione_vociBulk);
	  *		Le Classificazioni, inoltre, devono essere di livello pari a quello indicato in PARAMETRI_LIVELLI.LIVELLO_PDG_DECIS_SPE.
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
	  * @param bulk <code>OggettoBulk</code> la Classificazione Ufficiale modello
	  *
	  * @return sql <code>SQLBuilder</code> Risultato della selezione.
	**/
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
		SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
		if (bulk instanceof V_classificazione_vociBulk) {
			Parametri_cnrHome parCnrHome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrHome.findByPrimaryKey(new Parametri_cnrBulk(((Classificazione_vociBulk)bulk).getEsercizio()));
			sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.NR_LIVELLO", sql.EQUALS, parCnrBulk.getLivello_pdg_decis_spe());
			sql.addClause("AND", "fl_piano_riparto", sql.EQUALS, Boolean.TRUE);
		}
		return sql;
	}

	/**
	 * 	Ritorna il bulk dei parametri livelli <ParametriLivelliBulk> dell'Esercizio indicato <esercizio>.
	 */
	public Parametri_livelliBulk findParametriLivelli(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
		try
		{
			Parametri_livelliHome parametri_livelliHome = (Parametri_livelliHome) getHome(userContext, Parametri_livelliBulk.class );
			Parametri_livelliBulk parametri_livelliBulk = (Parametri_livelliBulk)parametri_livelliHome.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
			if (parametri_livelliBulk==null)
				throw new ApplicationException("Parametri Livelli non definiti per l'esercizio " + esercizio + ".");
			return parametri_livelliBulk;
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}

	/**
	 * Ritorna TRUE se il Piano di Riparto delle Spese accentrate è definitivo 
	 * Ritorna FALSE se il Piano di Riparto delle Spese accentrate è provvisorio 
	 *
	 * @param userContext
	 * @param esercizio		l'esercizio di riferimento del Piano di Riparto
	 *
	 * @return TRUE se definitivo, altrimenti FALSE
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 */
	public boolean isPdgPianoRipartoDefinitivo(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_piano_ripartoHome pianoHome = (Pdg_piano_ripartoHome)getHome(userContext, Pdg_piano_ripartoBulk.class);
			SQLBuilder sql = pianoHome.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, esercizio);
			sql.addSQLClause("AND", "STATO", sql.EQUALS, Pdg_piano_ripartoHome.STATO_DEFINITIVO);
			List result = pianoHome.fetchAll( sql );
			if (result.size() > 0)
				return true;
			return false;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}

	/**
	 * Aggiorna lo stato del Piano di Riparto delle Spese accentrate 
	 *
	 * @param userContext
	 * @param cla		elemento modello per individuare il Piano di Riparto da aggiornare
	 * @param stato		lo stato da attribuire al piano di riparto individuato tramite il parametro <cla>
	 *
	 * @return l'oggetto <Classificazione_vociBulk> aggiornato
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 *
	 */
	private Classificazione_vociBulk cambiaStatoPdgPianoRiparto(UserContext userContext, Classificazione_vociBulk cla, String stato) throws it.cnr.jada.comp.ComponentException {
		try {
			/*Aggiorno tutti i record presenti in tabella PDG_PIANO_RIPARTO*/
			Pdg_piano_ripartoHome pianoHome = (Pdg_piano_ripartoHome)getHome(userContext, Pdg_piano_ripartoBulk.class);
			SQLBuilder sql = pianoHome.createSQLBuilder();
			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, cla.getEsercizio());
			List result = pianoHome.fetchAll( sql );
			for ( Iterator j = result.iterator(); j.hasNext(); ) {
				Pdg_piano_ripartoBulk piano = (Pdg_piano_ripartoBulk) j.next();
				piano.setStato(stato);
				piano.setToBeUpdated();
				super.modificaConBulk(userContext, piano);
			}
			return cla;			
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}

	/**
	 * Aggiorna lo stato del Piano di Riparto delle Spese accentrate da provvisorio a definitivo 
	 *
	 * @param userContext
	 * @param cla		elemento modello per individuare il Piano di Riparto da aggiornare
	 *
	 * @return l'oggetto <Classificazione_vociBulk> aggiornato
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 *
	 */
	public Classificazione_vociBulk rendiPdgPianoRipartoDefinitivo(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
		return cambiaStatoPdgPianoRiparto(userContext, cla, Pdg_piano_ripartoHome.STATO_DEFINITIVO);
	}

	/**
	 * Aggiorna lo stato del Piano di Riparto delle Spese accentrate da definitivo a provvisorio 
	 *
	 * @param userContext
	 * @param cla		elemento modello per individuare il Piano di Riparto da aggiornare
	 *
	 * @return l'oggetto <Classificazione_vociBulk> aggiornato
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 *
	 */
	public Classificazione_vociBulk rendiPdgPianoRipartoProvvisorio(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
		return cambiaStatoPdgPianoRiparto(userContext, cla, Pdg_piano_ripartoHome.STATO_PROVVISORIO);
	}


	/**
	 * Aggiorna il Totale Generale degli importi da ripartire del Piano di Riparto associato alla Classificazione <cla> 
	 *
	 * @param userContext
	 * @param cla		classificazione di cui aggiornare il campo Totale Generale Piano di Riparto
	 *
	 * @return l'oggetto <Classificazione_vociBulk> aggiornato
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 *
	 */
	public Classificazione_vociBulk aggiornaTotaleGeneraleImpdaRipartire(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
		try {
			Classificazione_vociHome testataHome = (Classificazione_vociHome)getHome(userContext, Classificazione_vociBulk.class);
			cla.setTot_imp_piano_riparto_spese(new java.math.BigDecimal(0));
			for (java.util.Iterator pianoRiparto = cla.getPdgPianoRipartoSpese().iterator();pianoRiparto.hasNext();){
				Pdg_piano_ripartoBulk pianoRiparto_det = (Pdg_piano_ripartoBulk)pianoRiparto.next();			
				if (pianoRiparto_det.getIm_tot_spese_acc()!=null)
					cla.setTot_imp_piano_riparto_spese(cla.getTot_imp_piano_riparto_spese().add(pianoRiparto_det.getIm_tot_spese_acc()));
			}

		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
		return cla;
	}

	/**
	 * Precarica la classificazione di bilancio con tutti i CDR di primo livello assegnandogli importo nullo 
	 *
	 * @param userContext
	 * @param cla		classificazione di cui precaricare la struttura
	 *
	 * @return l'oggetto <Classificazione_vociBulk> aggiornato
	 *
	 * @throw it.cnr.jada.comp.ComponentException
	 *
	 */
	public Classificazione_vociBulk caricaStruttura(UserContext userContext, Classificazione_vociBulk cla) throws it.cnr.jada.comp.ComponentException {
		try {
			boolean trovato = false;
		    java.util.Collection coll = getHome(userContext, CdrBulk.class).fetchAll(selectCentro_responsabilitaByClause(userContext, null, null, null));
			for (java.util.Iterator listaCdr = coll.iterator(); listaCdr.hasNext();){
				trovato = false;
				CdrBulk selCdr = (CdrBulk)listaCdr.next(); 
				for (java.util.Iterator pianoRiparto = cla.getPdgPianoRipartoSpese().iterator();pianoRiparto.hasNext();){
					Pdg_piano_ripartoBulk pianoRiparto_det = (Pdg_piano_ripartoBulk)pianoRiparto.next();			
					if (pianoRiparto_det.getCentro_responsabilita().equalsByPrimaryKey(selCdr))
						trovato = true;
				}
				if (!trovato){ 
					Pdg_piano_ripartoBulk newPiano = new Pdg_piano_ripartoBulk();
					newPiano.setEsercizio(cla.getEsercizio());
					newPiano.setV_classificazione_voci(new V_classificazione_vociBulk(cla.getId_classificazione()));
					newPiano.setCentro_responsabilita(selCdr);
					newPiano.setStato(Pdg_piano_ripartoHome.STATO_PROVVISORIO);
					newPiano.setToBeCreated();
					insertBulk(userContext, newPiano);
				}
			}
		}
		catch(it.cnr.jada.persistency.PersistencyException e)	{
			throw handleException(e);
		}	
		return cla;
	}

    /** 
	  *  Normale
	  *    PreCondition:
	  *      Viene richiesto l'elenco dei centri di responsabilità di primo livello o di tipo AREA 
	  *    PostCondition:
  	  *      Viene restituito una query sui cdr con le clausole specificate e una clausola sull'esercizio uguale a quello del piano di riparto specificato
     */
	public SQLBuilder selectCentro_responsabilitaByClause (UserContext userContext,
													       Pdg_piano_ripartoBulk piano,
														   CdrBulk cdr,
											  			   CompoundFindClause clause)
    throws ComponentException, PersistencyException
	{
		CdrHome cdrHome = (CdrHome)getHome(userContext,CdrBulk.class,"V_CDR_VALIDO");
		SQLBuilder sql = cdrHome.createSQLBuilder();
		sql.addToHeader("V_UNITA_ORGANIZZATIVA_VALIDA");
		sql.addSQLJoin("V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA", "V_UNITA_ORGANIZZATIVA_VALIDA.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("V_CDR_VALIDO.ESERCIZIO", "V_UNITA_ORGANIZZATIVA_VALIDA.ESERCIZIO");
		sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.openParenthesis("AND");
		sql.addSQLClause("AND", "V_CDR_VALIDO.LIVELLO", sql.EQUALS, CdrHome.CDR_PRIMO_LIVELLO);
		sql.addSQLClause("OR", "V_UNITA_ORGANIZZATIVA_VALIDA.CD_TIPO_UNITA", sql.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
		sql.closeParenthesis();
		if (clause != null)
		   sql.addClause(clause);
		return sql; 
	}
}
