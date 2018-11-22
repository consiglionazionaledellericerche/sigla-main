package it.cnr.contab.pdg01.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.PreparedStatement;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Classificazione_vociComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.EuroFormat;
import it.cnr.contab.util.EuroPositivoFormat;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class CRUDPdgModuloSpeseGestComponent extends it.cnr.jada.comp.CRUDComponent {
	/**
	  * CRUDPdgModuloEntrateGestComponent constructor comment.
	  */
	public CRUDPdgModuloSpeseGestComponent() {
		super();
	}

	/**
	 * Carica un modulo del Pdg con tutti i dettagli gestionali correlati.
	 *
	 * Nome: Inizializzazione;
	 * Pre:  Preparare l'oggetto alle modifiche;
	 * Post: Si procede, oltre che alla normale procedura di inizializzazione di un oggetto bulk,
	 *       anche al caricamento dei dettagli gestionali e al calcolo delle somme già ripartite.
	 *
	 * @param bulk dovrà essere sempre <code>Pdg_modulo_speseBulk</code>.
	 *
	 * @return un <code>OggettoBulk</code> che sarà sempre un <code>Pdg_modulo_speseBulk</code>.
	 */
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Pdg_modulo_speseBulk testata = (Pdg_modulo_speseBulk)super.inizializzaBulkPerModifica(userContext,bulk);
			Pdg_modulo_speseHome testataHome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
			testata.setDettagli_gestionali(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliGestionaliOriginali(testata)));
			valorizzaImportoTotali(userContext,testata);
			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch(Exception e) {
			throw handleException(e);
		}	
	}	 

	/**
	 * Esegue una operazione di modifica di un Pdg_Modulo_SpeseBulk. 
	 *
	 * Pre-post-conditions:
	 *
	 * Nome: Modifica di un Modulo
	 * Pre:  La richiesta di modifica di un Modulo è stata generata
	 * Post: Viene loccato il record PDG_MODULO_SPESE con l'istruzione findAndLock per evitare che salvataggi 
	 *       simultanei da parte di più utenti possano alterare i controlli.
	 * 		 Vengono verificati i dettagli gestionali associati al modulo per controllare che il totale 
	 *       degli importi ripartiti non sia superiore agli importi previsti in Bilancio.
	 * 	     In caso affermativo viene generata una ApplicationException per segnalare all'utente 
	 *       l'impossibilità di modificare i dettagli gestionali associati al Modulo.
	 *
	 * @param	usercontext	lo UserContext che ha generato la richiesta
	 * @param	oggettobulk il Pdg_Modulo_SpeseBulk che deve essere modificato
	 * @return	il Pdg_Modulo_SpeseBulk risultante dopo l'operazione di modifica.
	 */	
	public OggettoBulk modificaConBulk(UserContext userContext,	OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk.setCrudStatus(OggettoBulk.NORMAL);
			Pdg_modulo_speseHome testataHome = (Pdg_modulo_speseHome)getHome(userContext,oggettobulk);
			Pdg_modulo_speseBulk clone = (Pdg_modulo_speseBulk)testataHome.findAndLock(oggettobulk);
			Pdg_modulo_speseBulk testata = (Pdg_modulo_speseBulk)super.modificaConBulk(userContext, oggettobulk);
			valorizzaImportoTotali(userContext,testata);
			if (testata.getTotale_spese_accentrate_interne_gest().compareTo(testata.getIm_spese_gest_accentrata_int())>0)
				throw new ApplicationException("Impossibile salvare: il totale della spese accentrate interne ripartite (" + new it.cnr.contab.util.EuroFormat().format(testata.getTotale_spese_accentrate_interne_gest()) + ") è superiore all'importo previsto in Bilancio (" + new it.cnr.contab.util.EuroFormat().format(testata.getIm_spese_gest_accentrata_int()) + ").");					
			if (testata.getTotale_spese_accentrate_esterne_gest().compareTo(testata.getIm_spese_gest_accentrata_est())>0)
				throw new ApplicationException("Impossibile salvare: il totale della spese accentrate esterne ripartite (" + new it.cnr.contab.util.EuroFormat().format(testata.getTotale_spese_accentrate_esterne_gest()) + ") è superiore all'importo previsto in Bilancio (" + new it.cnr.contab.util.EuroFormat().format(testata.getIm_spese_gest_accentrata_est()) + ").");					
			if (testata.getTotale_spese_decentrate_interne_gest().compareTo(testata.getIm_spese_gest_decentrata_int())>0)
				throw new ApplicationException("Impossibile salvare: il totale della spese decentrate interne ripartite (" + new it.cnr.contab.util.EuroFormat().format(testata.getTotale_spese_decentrate_interne_gest()) + ") è superiore all'importo previsto in Bilancio (" + new it.cnr.contab.util.EuroFormat().format(testata.getIm_spese_gest_decentrata_int()) + ").");					
			if (testata.getTotale_spese_decentrate_esterne_gest().compareTo(testata.getIm_spese_gest_decentrata_est())>0)
				throw new ApplicationException("Impossibile salvare: il totale della spese decentrate esterne ripartite (" + new it.cnr.contab.util.EuroFormat().format(testata.getTotale_spese_decentrate_esterne_gest()) + ") è superiore all'importo previsto in Bilancio (" + new it.cnr.contab.util.EuroFormat().format(testata.getIm_spese_gest_decentrata_est()) + ").");					
			return testata;	
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (BusyResourceException e) {
			try{
				String ds_classificazione = Utility.createClassificazioneVociComponentSession(). 
												getDsLivelloClassificazione(userContext,
																			CNRUserContext.getEsercizio(userContext),
																			Elemento_voceHome.GESTIONE_SPESE,
																			((Pdg_modulo_speseBulk)oggettobulk).getClassificazione().getNr_livello());
				
				throw new ApplicationException("Operazione effettuata al momento da un'altro utente sullo stesso Modulo e " + ds_classificazione + ". Riprovare a salvare successivamente.");
			} catch (ComponentException e1) {
				throw handleException(e1);
			} catch (RemoteException e1) {
				throw handleException(e1);
			} 
		} catch(Exception e) {
			throw handleException(e);
		}	
	}

	/**
	 * Aggiunge delle clausole a tutte le operazioni di ricerca eseguite su CdrBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di un CDR
	 * Post: Vengono restituiti tutti i CDR appartenenti al CDS del dettaglio gestionale Pdg_modulo_speseBulk;
	 * 		 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */
	public SQLBuilder selectCdr_assegnatarioByClause (UserContext userContext, 
													  Pdg_modulo_spese_gestBulk dett,
													  it.cnr.contab.config00.sto.bulk.CdrBulk cdr, 
                                                	  CompoundFindClause clause) throws ComponentException, PersistencyException {	
		SQLBuilder sql = getHome(userContext, cdr, "V_CDR_VALIDO").createSQLBuilder();

		sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
		sql.addSQLClause("AND","V_CDR_VALIDO.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLJoin("V_CDR_VALIDO.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
		sql.addSQLJoin("V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
		sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDS",sql.EQUALS,CNRUserContext.getCd_cds(userContext));
		
		if (!dett.getPdg_modulo_spese().getPdg_modulo_costi().getPdg_modulo().getCdr().getUnita_padre().getCd_unita_organizzativa().equals(CNRUserContext.getCd_unita_organizzativa(userContext)))
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,CNRUserContext.getCd_unita_organizzativa(userContext));

		if (clause != null) sql.addClause(clause);

		return sql; 
	}	

	/**
	 * Aggiunge delle clausole a tutte le operazioni di ricerca eseguite su WorkpackageBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di una Linea di Attività
	 * Post: Vengono restituite tutte le Linee di Attività che:
	 * 		 - sono associate al CDR Assegnatario del dettaglio gestionale Pdg_modulo_spese_gestBulk;
	 * 		 - sono associate al Modulo di Attività del dettaglio gestionale Pdg_modulo_speseBulk;
	 * 		 - siano Linee utilizzabile nella Gestione spese (TI_GESTIONE='S')
	 * 		 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */
	public SQLBuilder selectLinea_attivitaByClause (UserContext userContext, 
													Pdg_modulo_spese_gestBulk dett,
													WorkpackageBulk latt, 
													CompoundFindClause clause) throws ComponentException, PersistencyException {	
		SQLBuilder sql = getHome(userContext, latt, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
 
		sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,dett.getCd_cdr_assegnatario());
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,dett.getPg_progetto());

		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_SPESE);
		sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
		sql.closeParenthesis();
		
		if (dett.getPdg_modulo_spese()!=null && dett.getPdg_modulo_spese().getCd_cofog()!=null )
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_COFOG",SQLBuilder.EQUALS,dett.getPdg_modulo_spese().getCd_cofog());

		if (dett.getPdg_modulo_spese().getVoce_piano_economico()!=null && dett.getPdg_modulo_spese().getVoce_piano_economico().getCd_voce_piano()!=null ) {
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_UNITA_PIANO",SQLBuilder.EQUALS,dett.getPdg_modulo_spese().getVoce_piano_economico().getCd_unita_organizzativa());
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_VOCE_PIANO",SQLBuilder.EQUALS,dett.getPdg_modulo_spese().getVoce_piano_economico().getCd_voce_piano());
		}

		Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
		Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
		if (parCnrBulk!=null && parCnrBulk.getFl_nuovo_pdg()) {
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_PROGRAMMA",SQLBuilder.ISNOTNULL,null);
			sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_MISSIONE",SQLBuilder.ISNOTNULL,null);
			if (dett.getPdg_modulo_spese().getCd_missione()!=null)
				sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_MISSIONE",SQLBuilder.EQUALS,dett.getPdg_modulo_spese().getCd_missione());
		}
		
		sql.addTableToHeader("NATURA");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
		sql.addSQLClause(FindClause.AND, "NATURA.FL_SPESA",SQLBuilder.EQUALS,"Y");

		sql.addTableToHeader("FUNZIONE");
		sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE","FUNZIONE.CD_FUNZIONE");
		sql.addSQLClause(FindClause.AND, "FUNZIONE.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

		/**
		 * Escludo la linea di attività dell'IVA C20
		 */
		it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		if (config != null){
			sql.addSQLClause( FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  SQLBuilder.NOT_EQUALS, config.getVal01());
		}

		if (clause != null) sql.addClause(clause);
		
		return sql; 
	}	

	/**
	 * Aggiunge delle clausole a tutte le operazioni di ricerca eseguite su Elemento_voceBulk 
	 *	
	 * Pre-post-conditions:
	 *
	 * Nome: Default
	 * Pre:  E' stata generata la richiesta di ricerca di Elemento Voce
	 * Post: Vengono restituiti tutti gli Elementi Voce che:
	 * 		 - sono associate a classificazioni di un livello pari a quello definito in Parametri_Ente;
	 * 		 - le classificazioni associate sono figlie della classificazione del dettaglio gestionale Pdg_modulo_spese_gestBulk
	 * 		 - non sia una partita di giro
	 * 		 - abbia la FUNZIONE uguale a quella della Linea di Attività del dettaglio gestionale Pdg_modulo_spese_gestBulk
	 * 		 - il CD_TIPO_UNITA è uguale a quello della UO associata al CDR
	 * 
	 * @param userContext lo userContext che ha generato la richiesta
	 * @param clauses clausole di ricerca gia' specificate dall'utente
	 * @return il SQLBuilder con la clausola aggiuntiva sul gestore
	 */
	public SQLBuilder selectElemento_voceByClause (UserContext userContext, 
												   Pdg_modulo_spese_gestBulk dett,
												   Elemento_voceBulk elementoVoce, 
												   CompoundFindClause clause) throws ComponentException, PersistencyException {
		try {
			Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
	
			if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);
	
			SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_SPE").createSQLBuilder();
			
			if(clause != null) sql.addClause(clause);
	
			sql.addSQLClause("AND", "V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
	
			sql.addTableToHeader("PARAMETRI_LIVELLI");
			sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");
	
			sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
			sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_SPE.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
			sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");
	
			sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+parCnrBulk.getLivello_pdg_decis_spe(), sql.EQUALS, dett.getId_classificazione());	
	
			if (Utility.createParametriEnteComponentSession().getParametriEnte(userContext).isEnteCNR()) {
				sql.openParenthesis("AND");
				sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", sql.ISNULL, null);	
				sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", sql.EQUALS, "N");	
				sql.closeParenthesis();
			}
			sql.addSQLClause( "AND", "V_ELEMENTO_VOCE_PDG_SPE.FL_SOLO_RESIDUO", sql.EQUALS, "N");
			if (dett.getLinea_attivita() != null)
				sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_SPE.CD_FUNZIONE",sql.EQUALS,dett.getLinea_attivita().getCd_funzione());
			if(!parCnrBulk.getFl_nuovo_pdg())
				if (dett.getPdg_modulo_spese().getPdg_modulo_costi().getPdg_modulo().getCdr() != null)
					sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_SPE.CD_TIPO_UNITA",sql.EQUALS,dett.getPdg_modulo_spese().getPdg_modulo_costi().getPdg_modulo().getCdr().getUnita_padre().getCd_tipo_unita());
	
			if (clause != null) sql.addClause(clause);
	
			return sql;
		} catch(RemoteException e) {
			throw new it.cnr.jada.comp.ComponentException(e);
		}		
	}
	
	private BigDecimal calcolaImporto(UserContext userContext, SQLBuilder sql) throws ComponentException{
		BigDecimal totale = Utility.ZERO;
		try {
			java.sql.ResultSet rs = null;
			LoggableStatement ps = null;
			try {
				ps = sql.prepareStatement(getConnection(userContext));
				try {
					rs = ps.executeQuery();
					if (rs.next() && rs.getBigDecimal(1)!= null)
					  totale = totale.add(rs.getBigDecimal(1));
				} catch (java.sql.SQLException e) {
					throw handleSQLException(e);
				} finally {
					if (rs != null) try{rs.close();}catch( java.sql.SQLException e ){};
				}
			} finally {
				if (ps != null) try{ps.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException ex) {
			throw handleException(ex);
		}		
		return totale;
	}

	private void valorizzaImportoTotali(UserContext userContext, Pdg_modulo_speseBulk pdg) throws ComponentException{
    	try {
    		Pdg_modulo_speseHome testataHome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
			pdg.setTotale_spese_accentrate_esterne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliAccEst(userContext,pdg)));						
			pdg.setTotale_spese_accentrate_interne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliAccInt(userContext,pdg)));						
			pdg.setTotale_spese_decentrate_esterne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliDecEst(userContext,pdg)));						
			pdg.setTotale_spese_decentrate_interne_gest(calcolaImporto(userContext,testataHome.calcolaTotaleDettagliGestionaliDecInt(userContext,pdg)));						
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}	
	}		
}
