package it.cnr.contab.progettiric00.comp;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceHome;
import it.cnr.contab.doccont00.core.bulk.Stampa_elenco_progetti_laBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_etr_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_preventivo_spe_detBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_entrate_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaHome;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateHome;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_finanziatoreBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_partner_esternoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_uoBulk;
import it.cnr.contab.progettiric00.core.bulk.Stampa_anag_progettiVBulk;
import it.cnr.contab.progettiric00.core.bulk.Stampa_progettiVBulk;
import it.cnr.contab.progettiric00.core.bulk.TipoFinanziamentoBulk;
import it.cnr.contab.progettiric00.core.bulk.TipoFinanziamentoHome;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoHome;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.EuroFormat;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
public class ProgettoRicercaComponent extends it.cnr.jada.comp.CRUDComponent implements IProgettoRicercaMgr,IPrintMgr {
	public static final String TIPO_PROGETTO = "C";
/**
 * ProgettoRicercaComponent constructor comment.
 */
public ProgettoRicercaComponent() {
		super();
}
/**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *                      UO = l'UO coordinatrice del Progetto
 *                      Responsabile = Responsabile del Progetto
 *                      Importo = Importo del Progetto
 *
 * Pre:  Controllo somma importo dettagli != da importo del Progetto
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del Progetto"
 *
 */
		public OggettoBulk creaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
			try{		
				Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(uc);
				if (parEnte.getFl_informix().booleanValue())
					throw new ComponentException("Operazione creazione progetto non possibile in presenza del sistema Informix!");
				
				Optional<ProgettoBulk> optBulk = Optional.ofNullable(bulk).filter(ProgettoBulk.class::isInstance).map(ProgettoBulk.class::cast);
				optBulk.filter(el->!Optional.ofNullable(el.getImporto_progetto()).isPresent())
					.ifPresent(el->el.setImporto_progetto(Optional.ofNullable(el.getProgettopadre())
							  .flatMap(el1->Optional.ofNullable(el1.getImporto_progetto()))
							  .orElse(BigDecimal.ZERO)));

				optBulk.filter(el->!Optional.ofNullable(el.getDurata_progetto()).isPresent())
					.ifPresent(el->el.setDurata_progetto(Optional.ofNullable(el.getProgettopadre())
							  .flatMap(el1->Optional.ofNullable(el1.getDurata_progetto()))
							  .orElse(ProgettoBulk.DURATA_PROGETTO_ANNUALE)));

				optBulk.filter(el->!Optional.ofNullable(el.getDt_inizio()).isPresent())
					.ifPresent(el->el.setDt_inizio(Optional.ofNullable(el.getProgettopadre())
							  .flatMap(el1->Optional.ofNullable(el1.getDt_inizio()))
							  .orElse(DateUtils.firstDateOfTheYear(CNRUserContext.getEsercizio(uc)))));

				intBulk(uc, (ProgettoBulk)bulk, parEnte.getFl_informix().booleanValue());
			}catch(Throwable throwable){
	            throw handleException(throwable);
	        }

			//Parametri_cdsBulk param = parametriCds(uc, (ProgettoBulk)bulk);
			// inserimento automatico del codice
			if (((ProgettoBulk)bulk).getParametriCds().getFl_progetto_numeratore().booleanValue())
				((ProgettoBulk)bulk).setCd_progetto(creaCodiceProgetto(uc, (ProgettoBulk)bulk) );

			java.math.BigDecimal sq_progetto;
			sq_progetto = getSequence(uc);
			((ProgettoBulk)bulk).setPg_progetto(sq_progetto);
			((Progetto_uoBulk) ((ProgettoBulk)bulk).getDettagli().get(0)).setPg_progetto(new Integer(sq_progetto.intValue()));
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale().size() > i; i++) {
			  ((Progetto_piano_economicoBulk) ((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}	
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente().size() > i; i++) {
			  ((Progetto_piano_economicoBulk) ((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}	
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni().size() > i; i++) {
			  ((Progetto_piano_economicoBulk) ((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}	
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliFinanziatori().size() > i; i++) {
			  ((Progetto_finanziatoreBulk) ((ProgettoBulk)bulk).getDettagliFinanziatori().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}	
			for(int i = 0; ((ProgettoBulk)bulk).getDettagliPartner_esterni().size() > i; i++) {
			  ((Progetto_partner_esternoBulk) ((ProgettoBulk)bulk).getDettagliPartner_esterni().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}	                
			for(int i = 0; ((ProgettoBulk)bulk).getDettagli().size() > i; i++) {
			 ((Progetto_uoBulk) ((ProgettoBulk)bulk).getDettagli().get(i)).setPg_progetto(new Integer(sq_progetto.intValue()));
			}

			((ProgettoBulk)bulk).setStato(ProgettoBulk.TIPO_STATO_APPROVATO);
			try {
				validaCreaConBulk(uc, bulk);
				if (((ProgettoBulk)bulk).getOtherField()!=null) {
					((ProgettoBulk)bulk).getOtherField().setPg_progetto(((ProgettoBulk)bulk).getPg_progetto());
					((ProgettoBulk)bulk).getOtherField().setUser(bulk.getUser());
					getHome(uc, Progetto_other_fieldBulk.class).insert(((ProgettoBulk)bulk).getOtherField(), uc);
					
					((ProgettoBulk)bulk).setPg_progetto_other_field(((ProgettoBulk)bulk).getOtherField().getPg_progetto());
				}
				if (((ProgettoBulk)bulk).getFl_previsione()) {
					((ProgettoBulk)bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
					((ProgettoBulk)bulk).setTipo_fase_progetto_padre(ProgettoBulk.TIPO_FASE_PREVISIONE);
					getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent)bulk, uc);
				}
				if (((ProgettoBulk)bulk).getFl_gestione()) {
					((ProgettoBulk)bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_GESTIONE);
					((ProgettoBulk)bulk).setTipo_fase_progetto_padre(ProgettoBulk.TIPO_FASE_GESTIONE);
					getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent)bulk, uc);
				}
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagli());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliFinanziatori());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPartner_esterni());

				allineaAbilitazioniTerzoLivello(uc, (ProgettoBulk)bulk);

				validaDatePianoEconomico(uc, (ProgettoBulk)bulk);
				validaPianoEconomico(uc, (ProgettoBulk)bulk);
			}catch(Throwable throwable){
	            throw handleException(throwable);
	        }

			return bulk;
		}

		public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
			validaEliminaConBulk(aUC, bulk);
			try{		
			  /*Se sto cancellando il progetto cancello anche tutti i dettagli */
			  if (bulk instanceof ProgettoBulk){
				  ProgettoHome progettohome = (ProgettoHome)getHome(aUC, ProgettoBulk.class,"V_PROGETTO_PADRE");
				  SQLBuilder sql = progettohome.createSQLBuilder();
				  sql.addSQLClause(FindClause.AND, "P_PG_PROGETTO", SQLBuilder.EQUALS, ((ProgettoBulk)bulk).getPg_progetto());
				  List<ProgettoBulk> progettiFigli = progettohome.fetchAll(sql);

				  boolean flNuovoPdg = Utility.createParametriCnrComponentSession().getParametriCnr(aUC,CNRUserContext.getEsercizio(aUC)).getFl_nuovo_pdg();
				  if (!progettiFigli.isEmpty() && !flNuovoPdg)
					  throw new it.cnr.jada.comp.ApplicationException("Esistono livelli di progetti collegati. Eliminazione non possibile.");

				  List dettagliCopy = new BulkList<>();
				  dettagliCopy.addAll(((ProgettoBulk)bulk).getDettagli());
				  dettagliCopy.stream().forEach(e->{
					  ((ProgettoBulk)bulk).removeFromDettagli(((ProgettoBulk)bulk).getDettagli().indexOf(e));
				  });

				  List dettagliPianoEconomicoTotaleCopy = new BulkList<>();
				  dettagliPianoEconomicoTotaleCopy.addAll(((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale());
				  dettagliPianoEconomicoTotaleCopy.stream().forEach(e->{
					  ((ProgettoBulk)bulk).removeFromDettagliPianoEconomicoTotale(((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale().indexOf(e));
				  });
				  
				  List dettagliPianoEconomicoAnnoCorrenteCopy = new BulkList<>();
				  dettagliPianoEconomicoAnnoCorrenteCopy.addAll(((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente());
				  dettagliPianoEconomicoAnnoCorrenteCopy.stream().forEach(e->{
					  ((ProgettoBulk)bulk).removeFromDettagliPianoEconomicoAnnoCorrente(((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente().indexOf(e));
				  });

				  List dettagliPianoEconomicoAltriAnniCopy = new BulkList<>();
				  dettagliPianoEconomicoAltriAnniCopy.addAll(((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni());
				  dettagliPianoEconomicoAltriAnniCopy.stream().forEach(e->{
					  ((ProgettoBulk)bulk).removeFromDettagliPianoEconomicoAltriAnni(((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni().indexOf(e));
				  });

				  for(int i = 0; ((ProgettoBulk)bulk).getDettagliFinanziatori().size() > i; i++) {
					  ((Progetto_finanziatoreBulk) ((ProgettoBulk)bulk).getDettagliFinanziatori().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
				  }
				  for(int i = 0; ((ProgettoBulk)bulk).getDettagliPartner_esterni().size() > i; i++) {
					  ((Progetto_partner_esternoBulk) ((ProgettoBulk)bulk).getDettagliPartner_esterni().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
				  }

				  ProgettoBulk progettoPrev = (ProgettoBulk)getHome(aUC, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk)bulk).getEsercizio(), ((ProgettoBulk)bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE));
				  if (progettoPrev!=null)
					  getHome(aUC, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoPrev, aUC);
	
				  ProgettoBulk progettoGest = (ProgettoBulk)getHome(aUC, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk)bulk).getEsercizio(), ((ProgettoBulk)bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_GESTIONE));
				  if (progettoGest!=null)
					  getHome(aUC, ProgettoBulk.class, "PROGETTO_SIP").delete(progettoGest, aUC);

				  makeBulkListPersistent(aUC, ((ProgettoBulk)bulk).getDettagli());
				  makeBulkListPersistent(aUC, ((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale());
				  makeBulkListPersistent(aUC, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente());
				  makeBulkListPersistent(aUC, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni());				  
				  
				  if (((ProgettoBulk)bulk).getOtherField()!=null)
					getHome(aUC, Progetto_other_fieldBulk.class).delete(((ProgettoBulk)bulk).getOtherField(), aUC);

				  allineaAbilitazioniTerzoLivello(aUC, (ProgettoBulk)bulk);
				}
		   }catch(ComponentException ex) {
		   		throw ex;
		   }catch(Throwable throwable){
		       throw handleException(throwable);
		   }		  
		}

		public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
				// inizializzazione per gestire la codifica automatica
				((ProgettoBulk)bulk).setParametriCds(parametriCds(aUC, (ProgettoBulk) bulk));
				((ProgettoBulk)bulk).setEsercizio(CNRUserContext.getEsercizio(aUC));
				((ProgettoBulk)bulk).setFl_utilizzabile(Boolean.TRUE);
				((ProgettoBulk)bulk).setFl_piano_triennale(Boolean.TRUE);
				((ProgettoBulk)bulk).setFl_piano_triennale(Boolean.TRUE);
				((ProgettoBulk)bulk).setStato("INI");				
				return super.inizializzaBulkPerInserimento(aUC, bulk);
		}

/**
 * Pre:  Preparare l'oggetto alle modifiche;
 * Post: carica la lista di dettagli associati a un Progetto
 */
		public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
				try {
						ProgettoBulk testata = (ProgettoBulk)super.inizializzaBulkPerModifica(userContext,bulk);
						ProgettoHome testataHome = (ProgettoHome)getHome(userContext, ProgettoBulk.class);
						testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findDettagli(testata)));
						testata.setDettagliFinanziatori(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliFinanziatori(testata)));
						testata.setDettagliPartner_esterni(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPartner_esterni(testata)));
						testata.setSpeseEsercizio(new it.cnr.jada.bulk.BulkList(testataHome.findDettagliSpese(userContext,testata)));
						
						List<Progetto_piano_economicoBulk> progettoPiano = new it.cnr.jada.bulk.BulkList(testataHome.findDettagliPianoEconomico(userContext,testata.getPg_progetto()));
						progettoPiano.stream().forEach(el->el.setProgetto(testata));
						testata.setDettagliPianoEconomicoTotale(new it.cnr.jada.bulk.BulkList(progettoPiano.stream().filter(e->e.getEsercizio_piano().equals(Integer.valueOf(0))).collect(Collectors.toList())));
						testata.setDettagliPianoEconomicoAnnoCorrente(new it.cnr.jada.bulk.BulkList(progettoPiano.stream().filter(e->e.getEsercizio_piano().equals(testata.getEsercizio())).collect(Collectors.toList())));
						testata.setDettagliPianoEconomicoAltriAnni(new it.cnr.jada.bulk.BulkList(progettoPiano.stream().filter(e->!e.getEsercizio_piano().equals(Integer.valueOf(0)) && !e.getEsercizio_piano().equals(testata.getEsercizio())).collect(Collectors.toList())));

						// controllo per evitare che il progetto padre sia modificabile nel caso
						// in cui tale progetto sia stato inserito nel piano di gestione preventivo
						if (!isProgettoPadreModificabile(userContext,testata))
							testata.getProgettopadre().setOperabile(false);

						ProgettoBulk progettoPrev = (ProgettoBulk)((ProgettoHome)getHome(userContext, ProgettoBulk.class)).findByPrimaryKey(new ProgettoBulk(testata.getEsercizio(), testata.getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE));
						ProgettoBulk progettoGest = (ProgettoBulk)((ProgettoHome)getHome(userContext, ProgettoBulk.class)).findByPrimaryKey(new ProgettoBulk(testata.getEsercizio(), testata.getPg_progetto(), ProgettoBulk.TIPO_FASE_GESTIONE));

						testata.setFl_previsione(progettoPrev!=null);
						testata.setFl_gestione(progettoGest!=null);

						BulkList<Pdg_moduloBulk> pdgModuliList = new it.cnr.jada.bulk.BulkList(testataHome.findPdgModuliAssociati(userContext, testata.getPg_progetto()));

						it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
				   		BigDecimal annoFrom = configSession.getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
				   		if (Optional.ofNullable(annoFrom).isPresent())
				   			testata.setPdgModuli(new BulkList(pdgModuliList.stream().filter(el->el.getEsercizio().compareTo(annoFrom.intValue())>=0).collect(Collectors.toList())));
				   		else
				   			testata.setPdgModuli(pdgModuliList);
						getHomeCache(userContext).fetchAll(userContext);
						return testata;
				} catch(Exception e) {
						throw handleException(e);
				}
		}

		private boolean isProgettoPadreModificabile(it.cnr.jada.UserContext userContext, ProgettoBulk testata) throws it.cnr.jada.comp.ComponentException {
			try {
				if (testata.getProgettopadre().getPg_progetto()==null)
					return true;
				SQLBuilder sql_exists = getHome(userContext,Pdg_preventivo_etr_detBulk.class).createSQLBuilder();
				sql_exists.addTableToHeader("PROGETTO");
				sql_exists.addSQLJoin("PROGETTO.PG_PROGETTO", "LINEA_ATTIVITA.PG_PROGETTO");
				sql_exists.addTableToHeader("LINEA_ATTIVITA");
				sql_exists.addSQLJoin("PDG_PREVENTIVO_ETR_DET.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
				sql_exists.addSQLJoin("PDG_PREVENTIVO_ETR_DET.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
				sql_exists.addSQLClause("AND","PROGETTO.PG_PROGETTO_PADRE",sql_exists.EQUALS,testata.getPg_progetto());
				if (sql_exists.executeExistsQuery(getConnection(userContext)))
					return false;
				sql_exists = getHome(userContext,Pdg_preventivo_spe_detBulk.class).createSQLBuilder();
				sql_exists.addTableToHeader("PROGETTO");
				sql_exists.addSQLJoin("PROGETTO.PG_PROGETTO", "LINEA_ATTIVITA.PG_PROGETTO");
				sql_exists.addTableToHeader("LINEA_ATTIVITA");
				sql_exists.addSQLJoin("PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA", "LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
				sql_exists.addSQLJoin("PDG_PREVENTIVO_SPE_DET.CD_LINEA_ATTIVITA", "LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
				sql_exists.addSQLClause("AND","PROGETTO.PG_PROGETTO_PADRE",sql_exists.EQUALS,testata.getPg_progetto());
				if (sql_exists.executeExistsQuery(getConnection(userContext)))
					return false;
				return true;
			} catch(java.sql.SQLException e) {
				throw handleException(e);
			}
		}

		public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerRicercaLibera(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
				return super.inizializzaBulkPerRicercaLibera(aUC, bulk);
		}
		protected boolean cercaUocordinatrice(ProgettoBulk progetto)
		{
		  for(int i = 0; progetto.getDettagli().size() > i; i++) {
			if (((Progetto_uoBulk)(progetto.getDettagli().get(i))).getCd_unita_organizzativa().equals(progetto.getCd_unita_organizzativa())){
			   return false;	
			}  
		  }        	
		  return true;
		}
		private ProgettoBulk intBulk(UserContext userContext, ProgettoBulk bulk, boolean isInformix) throws ComponentException {
	        if (!isInformix) {
				if (bulk.getTipo() == null)
			     throw new it.cnr.jada.comp.ApplicationException("Attenzione: il campo Tipo deve essere valorizzato!");
				     
				if (!bulk.getFl_previsione() && !bulk.getFl_gestione())
					throw new it.cnr.jada.comp.ApplicationException("Indicare almeno una fase di operatività del progetto.");
	
				//se data di fine esiste deve essere minore di data inizio
				if(bulk.getDt_fine() != null && bulk.getDt_inizio().after( bulk.getDt_fine() ))
					throw new it.cnr.jada.comp.ApplicationException("Data di fine deve essere maggiore della data di inizio!");
	
				//se data di fine non esiste non deve esistere data di proroga
				if(bulk.getDt_fine() == null && bulk.getDt_proroga() != null)
					throw new it.cnr.jada.comp.ApplicationException("Non può esistere una data di proroga se non si indica una data di fine!");
	
				//se data di proroga esiste deve essere minore di data fine
				if(bulk.getDt_proroga() != null && bulk.getDt_fine().after( bulk.getDt_proroga() ))
					throw new it.cnr.jada.comp.ApplicationException("Data di proroga deve essere maggiore della data di fine!");
	
				if (bulk.getUnita_organizzativa() == null)
					throw new it.cnr.jada.comp.ApplicationException("L'unità organizzativa è obbligatoria.");
	
				if (bulk.getProgettopadre() == null || bulk.getProgettopadre().getPg_progetto() == null)
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: Per salvare il progetto è necessario inserire il progetto padre!");	                	
	
				if ((ProgettoBulk)bulk.getProgettopadre() == null)
				  ((ProgettoBulk)bulk).setLivello(new Integer(1));
	
				//se nei dettagli non è presente la UO cordinatrice viene creata
				if( cercaUocordinatrice(bulk) ) {
				   Progetto_uoBulk dett = new Progetto_uoBulk(bulk.getPg_progetto(), bulk.getUnita_organizzativa());
				   dett.setCrudStatus( OggettoBulk.TO_BE_CREATED );
				   dett.setUser( bulk.getUser() );
				   bulk.addToDettagli(dett);
				}
			}
				
			if (!((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente().isEmpty() ||
				!((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni().isEmpty()) {
				BulkList<Progetto_piano_economicoBulk> allPiano = new BulkList<>();
				allPiano.addAll(((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente());
				allPiano.addAll(((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni());
				if (!allPiano.isEmpty() && !((ProgettoBulk)bulk).isPianoEconomicoRequired())
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: E' stato inserito un piano economico anche se la tipologia di finanziamento non lo prevede!");	                	

				for (Iterator iterator = allPiano.iterator(); iterator.hasNext();) {
					Progetto_piano_economicoBulk pianoeco = (Progetto_piano_economicoBulk) iterator.next();
					if (pianoeco.getEsercizio_piano()!=null && !pianoeco.getEsercizio_piano().equals(0))
						if (pianoeco.getEsercizio_piano().compareTo(((ProgettoBulk)bulk).getAnnoInizioOf())<0 || 
							pianoeco.getEsercizio_piano().compareTo(((ProgettoBulk)bulk).getAnnoFineOf())>0)
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: E' stato inserito nel piano economico un anno non compatibile con la durata del progetto!");	                	
				}
			}
					
			//VERIFICO CHE UNA VOCE DI BILANCIO NON SIA ASSOCIATA PIù VOLTE
			BulkList<Ass_progetto_piaeco_voceBulk> newList = new BulkList<Ass_progetto_piaeco_voceBulk>();
			((ProgettoBulk)bulk).getAllDetailsProgettoPianoEconomico().stream()
					.map(Progetto_piano_economicoBulk::getVociBilancioAssociate).forEach(el->newList.addAll(el));
			Optional<Elemento_voceBulk> optChiaveVoceMultipla = newList.stream()
                    .filter(ass_progetto_piaeco_voceBulk -> Optional.ofNullable(ass_progetto_piaeco_voceBulk.getElemento_voce()).isPresent())
					.collect(Collectors.groupingBy(Ass_progetto_piaeco_voceBulk::getElemento_voce, Collectors.counting()))
					.entrySet()
					.stream()
					.filter(el->el.getValue()>1)
					.map(el->el.getKey())
					.findFirst();
			if (optChiaveVoceMultipla.isPresent())
				throw new ApplicationMessageFormatException("Attenzione: la voce di bilancio {0}/{1}/{2} risulta associata a più voci del piano economico. " +
                        "Operazione non consentita!",
                        String.valueOf(optChiaveVoceMultipla.get().getEsercizio()),
                        optChiaveVoceMultipla.get().getTi_gestione(),
                        optChiaveVoceMultipla.get().getCd_elemento_voce());
			return bulk;
		}

		protected java.math.BigDecimal getSequence(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException {
	//ricavo il progressivo unico pg_progetto
	java.math.BigDecimal pg_progetto= new java.math.BigDecimal(0);
	try {
		LoggableStatement ps= new LoggableStatement(getConnection(userContext),
				"select CNRSEQ00_PG_PROGETTO.nextval from dual",true,this.getClass());
		try {
			java.sql.ResultSet rs= ps.executeQuery();
			try {
				if (rs.next())
					pg_progetto= rs.getBigDecimal(1);
			} finally {
				try{rs.close();}catch( java.sql.SQLException e ){};
			}
		} catch (java.sql.SQLException e) {
			throw handleException(e);
		} finally {
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	}
	return pg_progetto;
  }
/**
 * Pre:  Ricerca progetti disponibili
 * Post: Limitazione ai progetti della UO in scrivania tranne per l'ente.
 */
        
		public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
		{
		   ProgettoBulk progetto = (ProgettoBulk)bulk;
		   ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
		   SQLBuilder sql = progettohome.createSQLBuilder();
		   sql.addClause(clauses);
		   sql.addClause(bulk.buildFindClauses(new Boolean(true)));
		   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
		   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS,ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
		   sql.addClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_NON_DEFINITA);

		   SQLBuilder sqlExistPrevisione = ((ProgettoHome)getHome(userContext, ProgettoBulk.class)).createSQLBuilder();
		   sqlExistPrevisione.addSQLJoin("V_PROGETTO_PADRE.ESERCIZIO", "PROGETTO.ESERCIZIO");
		   sqlExistPrevisione.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO", "PROGETTO.PG_PROGETTO");
		   sqlExistPrevisione.addClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_PREVISIONE);

		   SQLBuilder sqlExistGestione = ((ProgettoHome)getHome(userContext, ProgettoBulk.class)).createSQLBuilder();
		   sqlExistGestione.addSQLJoin("V_PROGETTO_PADRE.ESERCIZIO", "PROGETTO.ESERCIZIO");
		   sqlExistGestione.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO", "PROGETTO.PG_PROGETTO");
		   sqlExistGestione.addClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_GESTIONE);

		   if (ProgettoBulk.TIPO_FASE_SEARCH_SOLO_PREVISIONE.equals(progetto.getTipoFaseToSearch())) {
			   sql.addSQLExistsClause(FindClause.AND, sqlExistPrevisione);
			   sql.addSQLNotExistsClause(FindClause.AND, sqlExistGestione);
		   } else if (ProgettoBulk.TIPO_FASE_SEARCH_SOLO_GESTIONE.equals(progetto.getTipoFaseToSearch())) {
			   sql.addSQLNotExistsClause(FindClause.AND, sqlExistPrevisione);
			   sql.addSQLExistsClause(FindClause.AND, sqlExistGestione);
		   } else if (ProgettoBulk.TIPO_FASE_SEARCH_PREVISIONE_E_GESTIONE.equals(progetto.getTipoFaseToSearch())) {
			   sql.addSQLExistsClause(FindClause.AND, sqlExistPrevisione);
			   sql.addSQLExistsClause(FindClause.AND, sqlExistGestione);
		   }

		   Optional.ofNullable(progetto.getOtherField())
		   		   .map(el->{
		   			   Optional.ofNullable(el.getStato()).ifPresent(stato->{
		   				   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.STATO_OTHER_FIELD", SQLBuilder.EQUALS,stato);
			   		   });
		   			   Optional.ofNullable(el.getTipoFinanziamento()).flatMap(tipofin->Optional.ofNullable(tipofin.getId())).ifPresent(idTipoFin->{
		   				   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ID_TIPO_FINANZIAMENTO", SQLBuilder.EQUALS,idTipoFin);
			   		   });
					   Optional.ofNullable(el.getDtInizio()).ifPresent(dt->{
						   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.DT_INIZIO_OTHER_FIELD", SQLBuilder.EQUALS,dt);
					   });
					   Optional.ofNullable(el.getDtFine()).ifPresent(dt->{
						   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.DT_FINE_OTHER_FIELD", SQLBuilder.EQUALS,dt);
					   });
					   Optional.ofNullable(el.getDtProroga()).ifPresent(dt->{
						   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.DT_PROROGA_OTHER_FIELD", SQLBuilder.EQUALS,dt);
					   });
					   Optional.ofNullable(el.getImFinanziato()).ifPresent(im->{
						   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.IM_FINANZIATO_OTHER_FIELD", SQLBuilder.EQUALS,im);
					   });
					   Optional.ofNullable(el.getImCofinanziato()).ifPresent(im->{
						   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.IM_COFINANZIATO_OTHER_FIELD", SQLBuilder.EQUALS,im);
					   });
				   	   return true;
				   	});

		   // Se uo 999.000 in scrivania: visualizza tutti i progetti
		   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			   try {
				  sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
				} catch (Exception e) {
					throw handleException(e);
				}
		   }
		   return sql;
		}

 /**
 * Pre:  Controllo Dt_inizio > Dt_fine
 * Post: Segnalazione "Data di fine deve essere maggiore della data di inizio!"
 *
 * Pre:  Controllo se Dt_fine = null e Dt_proroga != null
 * Post: Segnalazione "Non può esistere una data di proroga se non si indica una data di fine!"
 *
 * Pre:  Controllo Dt_fine > Dt_proroga
 * Post: Segnalazione "Data di proroga deve essere maggiore della data di fine!"
 *
 * Pre:  Controllo se la lista dei dettagli è vuota
 * Post: Se vuota viene creato un unico dettaglio che ha:
 *                      UO = l'UO coordinatrice del progetto
 *                      Responsabile = Responsabile del progetto
 *                      Importo = Importo del progetto
 *
 * Pre:  Controllo somma importo dettagli != da importo del progetto
 * Post: Segnalazione "La somma degli importi degli assegnatari è diversa dall'importo del progetto"
 *
 */
		public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
			try{		
				Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(uc);
				intBulk(uc, (ProgettoBulk)bulk, parEnte.getFl_informix().booleanValue());

				if (!parEnte.getFl_informix().booleanValue()) {
				   validateBulkForInsert(uc, bulk);
				   ProgettoBulk progettoPrev = (ProgettoBulk)getHome(uc, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk)bulk).getEsercizio(), ((ProgettoBulk)bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_PREVISIONE));
				   if (progettoPrev!=null)
					   getHome(uc, bulk, "PROGETTO_SIP").delete(progettoPrev, uc);
	
				   ProgettoBulk progettoGest = (ProgettoBulk)getHome(uc, ProgettoBulk.class).findByPrimaryKey(new ProgettoBulk(((ProgettoBulk)bulk).getEsercizio(), ((ProgettoBulk)bulk).getPg_progetto(), ProgettoBulk.TIPO_FASE_GESTIONE));
				   if (progettoGest!=null)
					   getHome(uc, bulk, "PROGETTO_SIP").delete(progettoGest, uc);
	
					if (((ProgettoBulk)bulk).getFl_previsione()) {
						((ProgettoBulk)bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_PREVISIONE);
						((ProgettoBulk)bulk).setTipo_fase_progetto_padre(ProgettoBulk.TIPO_FASE_PREVISIONE);
						getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent)bulk, uc);
					}
	
					if (((ProgettoBulk)bulk).getFl_gestione()) {
						((ProgettoBulk)bulk).setTipo_fase(ProgettoBulk.TIPO_FASE_GESTIONE);
						((ProgettoBulk)bulk).setTipo_fase_progetto_padre(ProgettoBulk.TIPO_FASE_GESTIONE);
						getHome(uc, bulk, "PROGETTO_SIP").insert((Persistent)bulk, uc);
					}
	
					makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagli());
					makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliFinanziatori());
					makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPartner_esterni());
				}

				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoTotale());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAnnoCorrente());
				makeBulkListPersistent(uc, ((ProgettoBulk)bulk).getDettagliPianoEconomicoAltriAnni());

				if (((ProgettoBulk)bulk).getOtherField()!=null) {
					((ProgettoBulk)bulk).getOtherField().setUser(bulk.getUser());
					if (((ProgettoBulk)bulk).getOtherField().isToBeCreated())
						getHome(uc, Progetto_other_fieldBulk.class).insert(((ProgettoBulk)bulk).getOtherField(), uc);
					else 
						getHome(uc, Progetto_other_fieldBulk.class).update(((ProgettoBulk)bulk).getOtherField(), uc);
				}

				if (!parEnte.getFl_informix().booleanValue())
					allineaAbilitazioniTerzoLivello(uc, (ProgettoBulk)bulk);

				validaDatePianoEconomico(uc, (ProgettoBulk)bulk);
				validaPianoEconomico(uc, (ProgettoBulk)bulk);
		   }catch(Throwable throwable){
		       throw handleException(throwable);
		   }
			return bulk;
		}
	/**
	 * Pre:  Ricerca progettopadre
	 * Post: Limitazione ai progetti diversi da quello in oggetto.
	 */
			public SQLBuilder selectProgettopadreByClause (UserContext userContext,
												  OggettoBulk bulk,
												  Progetto_sipBulk progettopadre,
												  CompoundFindClause clause)
			throws ComponentException, PersistencyException
			{
					if (clause == null) 
					  clause = progettopadre.buildFindClauses(null);
					SQLBuilder sql = getHome(userContext, progettopadre).createSQLBuilder();
					sql.addSQLClause("AND", "PG_PROGETTO", sql.NOT_EQUALS, ((ProgettoBulk)bulk).getPg_progetto());
					if (((ProgettoBulk)bulk).getLivello() != null)
					   sql.addSQLClause("AND", "LIVELLO", sql.EQUALS, new Integer(((ProgettoBulk)bulk).getLivello().intValue()-1));
					if (clause != null) 
					  sql.addClause(clause);
					return sql;
			}
        
/**
 * Pre:  Ricerca progettopadre
 * Post: Limitazione ai progetti diversi da quello in oggetto.
 */
		public SQLBuilder selectProgettopadreByClause (UserContext userContext,
											  OggettoBulk bulk,
											  ProgettoBulk progettopadre,
											  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) 
				  clause = progettopadre.buildFindClauses(null);
				SQLBuilder sql = getHome(userContext, progettopadre,"V_PROGETTO_PADRE").createSQLBuilder();
				sql.addSQLClause(FindClause.AND, "PG_PROGETTO", SQLBuilder.NOT_EQUALS, ((ProgettoBulk)bulk).getPg_progetto());
				sql.addSQLClause(FindClause.AND, "TIPO_FASE", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
			    if (((ProgettoBulk)bulk).getLivello() != null)
				   sql.addSQLClause(FindClause.AND, "LIVELLO", SQLBuilder.EQUALS, new Integer(((ProgettoBulk)bulk).getLivello().intValue()-1));
				if (clause != null) 
				  sql.addClause(clause);
				return sql;
		}
/**
 * Pre:  Ricerca terzo ente
 * Post: Limitazione ai terzi ancora validi.
 */
		public SQLBuilder selectEnteByClause (UserContext userContext,
											  OggettoBulk bulk,
											  TerzoBulk ente,
											  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = ente.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, ente).createSQLBuilder();
				sql.addClause(
						it.cnr.jada.persistency.sql.CompoundFindClause.or(
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, ente).getServerTimestamp())
						)
				);

				if (clause != null) sql.addClause(clause);

				return sql;
		}

/**
 * Pre:  Ricerca terzo responsabile
 * Post: Limitazione ai terzi ancora validi.
 */
		public SQLBuilder selectResponsabileByClause(UserContext userContext,
													 OggettoBulk bulk,
													 TerzoBulk responsabile,
													 CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = responsabile.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, responsabile,"V_TERZO_CF_PI").createSQLBuilder();
				sql.addClause(
						it.cnr.jada.persistency.sql.CompoundFindClause.or(
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.ISNULL, null),
								new it.cnr.jada.persistency.sql.SimpleFindClause("AND", "dt_fine_rapporto", sql.GREATER, getHome(userContext, responsabile).getServerTimestamp())
						)
				);

				if (clause != null) sql.addClause(clause);

				return sql;
		}
/**
 * Pre:  Ricerca UO
 * Post: Limitazione alle UO valide.
 */
		public SQLBuilder selectUnita_organizzativaByClause (UserContext userContext,
															  OggettoBulk bulk,
															  Unita_organizzativaBulk uo,
															  CompoundFindClause clause)
		throws ComponentException, PersistencyException
		{
				if (clause == null) clause = uo.buildFindClauses(null);

				SQLBuilder sql = getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA").createSQLBuilder();
				sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

				if (clause != null) sql.addClause(clause);

				return sql;
		}
	private void inizializzaBulkPerStampa(UserContext usercontext, Stampa_progettiVBulk stampa_progettivbulk)
		throws ComponentException
	{
		stampa_progettivbulk.setCd_cds(CNRUserContext.getCd_cds(usercontext));
		stampa_progettivbulk.setEsercizio(CNRUserContext.getEsercizio(usercontext));
	}

	private void inizializzaBulkPerStampa(UserContext usercontext, Stampa_anag_progettiVBulk stampa_progettivbulk)
		throws ComponentException
	{
	  String cd_uo = CNRUserContext.getCd_unita_organizzativa(usercontext);	
	  try{
		   stampa_progettivbulk.setLivello(stampa_progettivbulk.LIVELLO_PROGETTO_ALL);
		   Unita_organizzativaHome uoHome = (Unita_organizzativaHome)getHome(usercontext, Unita_organizzativaBulk.class);
		   Unita_organizzativaBulk uo = (Unita_organizzativaBulk)uoHome.findByPrimaryKey(new Unita_organizzativaBulk(cd_uo));

		  Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( usercontext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		  if (!((CNRUserContext) usercontext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
			stampa_progettivbulk.setUnita_organizzativaForPrint(uo);
			stampa_progettivbulk.setIsUOForPrintEnabled(false);
		  } else {
			stampa_progettivbulk.setUnita_organizzativaForPrint(new Unita_organizzativaBulk());
			stampa_progettivbulk.setIsUOForPrintEnabled(true);
		  }
	  } catch (it.cnr.jada.persistency.PersistencyException pe){
		throw new ComponentException(pe);
		}
	}
    
	public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException
	{
		if(oggettobulk instanceof Stampa_progettiVBulk)
			inizializzaBulkPerStampa(usercontext, (Stampa_progettiVBulk)oggettobulk);
		if(oggettobulk instanceof Stampa_anag_progettiVBulk)
			inizializzaBulkPerStampa(usercontext, (Stampa_anag_progettiVBulk)oggettobulk);  
		if(oggettobulk instanceof Stampa_elenco_progetti_laBulk)
			inizializzaBulkPerStampa(usercontext, (Stampa_elenco_progetti_laBulk)oggettobulk);          
		return oggettobulk;
	}

		public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext,Stampa_elenco_progetti_laBulk stampa) throws ComponentException { 
		//	Imposta l'Esercizio come quello di scrivania
			stampa.setEsercizio(CNRUserContext.getEsercizio(usercontext));
		return stampa;
		}
/**
 * stampaConBulk method comment.
 */
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	if (bulk instanceof Stampa_elenco_progetti_laBulk) 
			return  stampaConBulk(aUC,(Stampa_elenco_progetti_laBulk)bulk);
	return bulk;
}        
        
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di un Progetto.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il progetto di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/ 
public RemoteIterator getChildren(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	ProgettoBulk ubi = (ProgettoBulk)bulk;
	ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"V_PROGETTO_PADRE");
	return iterator(
		userContext,
		ubiHome.selectChildrenFor(userContext,ubi),
		ProgettoBulk.class,
		null);
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di un Progetto.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il progetto di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/ 
public RemoteIterator getChildrenForSip(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	Progetto_sipBulk ubi = (Progetto_sipBulk)bulk;
	Progetto_sipHome ubiHome = (Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class);
	return iterator(
		userContext,
		ubiHome.selectChildrenFor(userContext,ubi),
		Progetto_sipBulk.class,
		"tipoFinanziamento");
}

/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di creazione di un Iteratore su tutti i nodi figli 
  *		di un Progetto.
  *    PostCondition:
  *		 Viene restituito il RemoteIterator con l'elenco degli eventuali nodi figli del progetto di riferimento.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il progetto di riferimento.
  *
  * @return remoteIterator <code>RemoteIterator</code> l'Iterator creato.
**/ 
public RemoteIterator getChildrenWorkpackage(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	ProgettoBulk ubi = (ProgettoBulk)bulk;
	ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"V_PROGETTO_PADRE");
	return iterator(
		userContext,
		ubiHome.selectChildrenForWorkpackage(userContext,ubi),
		ProgettoBulk.class,
		null);
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca del Progetto padre del Progetto specificato negli argomenti.
  *    PostCondition:
  *		 Viene restituito l'oggetto ProgettoBulk che è il Progetto padre cercato.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return bulk <code>OggettoBulk</code> il Progetto cercato.
**/ 
public OggettoBulk getParent(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		ProgettoBulk ubi = (ProgettoBulk)bulk;
		ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ProgettoBulk.class,"V_PROGETTO_PADRE");
		return ubiHome.getParent(ubi);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}        
/** 
  *  Controlla che il progetto sia una foglia.
  *    PreCondition:
  *      E' stata generata la richiesta di controllare se il Progetto specificato è una foglia,
  *		ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che il Progetto in 
  *		questione non ha dei Progetti figli.
  *    PostCondition:
  *		 Viene restituito un valore booleano:
  *			- true: il Progetto è una foglia;
  *			- false: il Progetto non è una foglia.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return il risultato <code>boolean</code> del controllo.
**/ 
public boolean isLeaf(UserContext userContext, OggettoBulk bulk) throws ComponentException{
	try {
		ProgettoBulk ubi = (ProgettoBulk)bulk;
		ProgettoHome ubiHome = (ProgettoHome)getHome(userContext,ubi,"V_PROGETTO_PADRE");
		return (!ubiHome.selectChildrenFor(userContext,ubi).executeExistsQuery(getConnection(userContext)));
	} catch(Throwable e) {
		throw handleException(e);
	}
}
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      E' stata generata la richiesta di ricerca del Progetto padre del Progetto specificato negli argomenti.
  *    PostCondition:
  *		 Viene restituito l'oggetto ProgettoBulk che è il Progetto padre cercato.
  *      
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return bulk <code>OggettoBulk</code> il Progetto cercato.
**/ 
public OggettoBulk getParentForSip(UserContext userContext, OggettoBulk bulk) throws ComponentException{

	try{
		Progetto_sipBulk ubi = (Progetto_sipBulk)bulk;
		Progetto_sipHome ubiHome = (Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class);
		return ubiHome.getParent(ubi);
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk,ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}        
/** 
  *  Controlla che il progetto sia una foglia.
  *    PreCondition:
  *      E' stata generata la richiesta di controllare se il Progetto specificato è una foglia,
  *		ossia se il suo livello è l'ultimo, (3). Questo implicherebbe che il Progetto in 
  *		questione non ha dei Progetti figli.
  *    PostCondition:
  *		 Viene restituito un valore booleano:
  *			- true: il Progetto è una foglia;
  *			- false: il Progetto non è una foglia.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Progetto di riferimento.
  *
  * @return il risultato <code>boolean</code> del controllo.
**/ 
public boolean isLeafForSip(UserContext userContext, OggettoBulk bulk) throws ComponentException{
	try {
		Progetto_sipBulk ubi = (Progetto_sipBulk)bulk;
		Progetto_sipHome ubiHome = (Progetto_sipHome)getHome(userContext,ubi);
		return (!ubiHome.selectChildrenFor(userContext,ubi).executeExistsQuery(getConnection(userContext)));
	} catch(Throwable e) {
		throw handleException(e);
	}
}

public ProgettoBulk cercaWorkpackages(UserContext userContext, ProgettoBulk commessa) throws it.cnr.jada.comp.ComponentException {

	try
	{
		ProgettoHome commessaHome = (ProgettoHome) getHome(userContext, commessa.getClass() );

		Collection result1 = commessaHome.findWorkpackage_collegati(userContext,commessa);
		for (java.util.Iterator i = result1.iterator();i.hasNext();) 
		{
			commessa.addToWorkpackage_collegati((WorkpackageBulk)i.next());
		}
		
		Collection result2 = commessaHome.findWorkpackage_disponibili(userContext,commessa);
		for (java.util.Iterator i = result2.iterator();i.hasNext();) 
		{
			WorkpackageBulk wp = (WorkpackageBulk)i.next();
			if (wp.getProgetto()!=null) {
				ProgettoBulk pgkey = new ProgettoBulk(((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio(),wp.getProgetto().getPg_progetto(),ProgettoBulk.TIPO_FASE_PREVISIONE);
				ProgettoBulk pg = (ProgettoBulk) getHome( userContext, ProgettoBulk.class ).findByPrimaryKey( 
					pgkey);
				wp.setProgetto(pg);
			}
			commessa.addToWorkpackage_disponibili(wp);
		}
	}
	catch (Exception e )
	{
		throw handleException( e );
	}	
	return commessa;

}
public String creaCodiceProgetto(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	try
	{
		LoggableStatement cs = new LoggableStatement(getConnection( aUC ), 
			"{ ? = call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
			"creaCodiceProgetto(?, ?, ?)}",false,this.getClass());
		try
		{
			cs.registerOutParameter( 1, java.sql.Types.CHAR );		
			cs.setString( 2, bulk.getProgettopadre().getCd_progetto());
			cs.setString( 3, TIPO_PROGETTO );		
			cs.setObject( 4, bulk.getParametriCds().getProgetto_numeratore_cifre());
			cs.executeQuery();
				
			String result = cs.getString( 1 );
			return result;
		}
		catch ( SQLException e )
		{
			throw handleException( e );
		}	
		finally
		{
			cs.close();
		}
	}
	catch ( SQLException e )
	{
		throw handleException( e );
	}	
}
public Parametri_cdsBulk parametriCds(UserContext aUC, ProgettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	Parametri_cdsBulk param;
	try {
		 param = (Parametri_cdsBulk) getHome( aUC, Parametri_cdsBulk.class ).findByPrimaryKey( 
			new Parametri_cdsBulk(
				((CNRUserContext) aUC).getCd_cds(), 
				((CNRUserContext) aUC).getEsercizio()));
	} catch (PersistencyException ex) {
		throw handleException(ex);
	} catch (ComponentException ex) {
		throw handleException(ex);
	}
	if (param == null) {
		//throw new ApplicationException("Parametri CDS non trovati per il CDS "+((CNRUserContext) aUC).getCd_cds());
		// se si vuole gestire un default
		param = new Parametri_cdsBulk();
		param.setFl_progetto_numeratore(Boolean.FALSE);
	}
	return param;
}
/** 
  *  Verifica se è possibile eliminare l'associazione della UO al modulo di attività,
  *  dato che ciò non è possibile se esiste un workpackage associato al modulo di attività
  *  il cui cdr appartiene alla UO associata al modulo stesso 
  *
  *    PreCondition:
  *      E' stata generata la richiesta di cancellare l'associazione fatta durante 
  *		la sessione di lavoro.
  *    PostCondition:
  *      Verifica se l'associazione può essere eliminata
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param progetto il <code>ProgettoBulk</code> progetto di ricerca.
  * @param dett <code>OggettoBulk</code> la UO di cui fare la verifica.
**/ 
public void validaCancellazioneUoAssociata(UserContext userContext, ProgettoBulk progetto, OggettoBulk dett) throws ComponentException{
	Progetto_uoBulk pruo = (Progetto_uoBulk) dett;
	
	try {
		BulkHome home = getHome(userContext,it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA");
		SQLBuilder sql = home.createSQLBuilder();
		sql.addTableToHeader("CDR");
		sql.addSQLJoin("CDR.CD_CENTRO_RESPONSABILITA","V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA");
		//sql.addTableToHeader("UNITA_ORGANIZZATIVA");
		//sql.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","CDR.CD_UNITA_ORGANIZZATIVA");
		sql.addTableToHeader("PROGETTO_UO");
		sql.addSQLJoin("PROGETTO_UO.PG_PROGETTO","V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO");
		sql.addSQLClause("AND","PROGETTO_UO.PG_PROGETTO",sql.EQUALS,pruo.getPg_progetto());
		sql.addSQLClause("AND","PROGETTO_UO.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,pruo.getCd_unita_organizzativa());
		sql.addSQLClause("AND","CDR.CD_UNITA_ORGANIZZATIVA",sql.EQUALS,pruo.getCd_unita_organizzativa());
		List ris = home.fetchAll(sql);
		if (!ris.isEmpty())
			throw new ApplicationException("Impossibile cancellare la UO partecipante "+pruo.getCd_unita_organizzativa()+" in quanto\n"+
               "il livello di progetto è collegato al GAE "+((WorkpackageBulk)ris.get(0)).getCd_linea_attivita());
		
		BulkHome moduloHome = getHome(userContext,Pdg_moduloBulk.class);
		SQLBuilder sqlModulo = moduloHome.createSQLBuilder();
		
		sqlModulo.addClause(FindClause.AND, "pg_progetto",SQLBuilder.EQUALS,pruo.getPg_progetto());

		sqlModulo.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "STRUTTURA_MODULO");
		sqlModulo.addSQLJoin("PDG_MODULO.ESERCIZIO", "STRUTTURA_MODULO.ESERCIZIO");
		sqlModulo.addSQLJoin("PDG_MODULO.CD_CENTRO_RESPONSABILITA", "STRUTTURA_MODULO.CD_ROOT");

		sqlModulo.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "STRUTTURA_PROGETTO");
		sqlModulo.addSQLJoin("STRUTTURA_MODULO.ESERCIZIO", "STRUTTURA_PROGETTO.ESERCIZIO");
		sqlModulo.addSQLJoin("STRUTTURA_MODULO.CD_CDS", "STRUTTURA_PROGETTO.CD_CDS");

		sqlModulo.addSQLClause(FindClause.AND, "STRUTTURA_PROGETTO.CD_ROOT",SQLBuilder.EQUALS,pruo.getCd_unita_organizzativa());

		List result = moduloHome.fetchAll(sqlModulo);
		if (!result.isEmpty())
			throw new ApplicationException("Impossibile cancellare la UO partecipante "+pruo.getCd_unita_organizzativa()+" in quanto\n"+
               "il livello di progetto è già stato inserito nel Piano di Gestione "+((Pdg_moduloBulk)result.get(0)).getEsercizio());

	} catch(Throwable e) {
		throw handleException(e);
	}
}

public OggettoBulk stampaConBulk(UserContext usercontext, Stampa_elenco_progetti_laBulk stampa) throws ComponentException {
	if ( stampa.getflg_pdg()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'PdG'");
	if ( stampa.getflg_impegno()==null )
			throw new ApplicationException( "E' necessario valorizzare il campo 'Impegno/Accertamento'");
	return stampa;
}

public SQLBuilder selectProgettoForPrintByClause (UserContext userContext,OggettoBulk stampa,ProgettoBulk progetto,CompoundFindClause clause) throws ComponentException, PersistencyException{
    ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	sql.addClause( clause );
	sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND", "tipo_fase", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_PRIMO);
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
	  return sql;
	}else{
		sql.addSQLExistsClause("AND",progettohome.abilitazioniProgetti(userContext));		
	}
	return sql;
}	

public SQLBuilder selectProgettoForPrintByClause (UserContext userContext,Stampa_elenco_progetti_laBulk stampa,ProgettoBulk progetto,CompoundFindClause clause) throws ComponentException, PersistencyException{
	return selectProgettoForPrintByClause(userContext,(OggettoBulk)stampa,progetto,clause);
}	
	
public SQLBuilder selectProgettoForPrintByClause (UserContext userContext,Stampa_anag_progettiVBulk stampa,ProgettoBulk progetto,CompoundFindClause clause) throws ComponentException, PersistencyException{
	return selectProgettoForPrintByClause(userContext,(OggettoBulk)stampa,progetto,clause);
}	

public SQLBuilder selectCommessaForPrintByClause (UserContext userContext,Stampa_elenco_progetti_laBulk stampa,ProgettoBulk commessa,CompoundFindClause clause) throws ComponentException, PersistencyException{
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	String progetto = stampa.getCdProgettoForPrint();
	sql.addClause( clause ); 
	sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND", "tipo_fase", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	Optional.ofNullable(stampa)
			.ifPresent(stampa_elenco_progetti_laBulk -> {
				Optional.ofNullable(stampa_elenco_progetti_laBulk.getProgettoForPrint())
						.map(progettoBulk -> progettoBulk.getPg_progetto())
						.ifPresent(pgProgetto -> {
							sql.addClause("AND","pg_progetto_padre",SQLBuilder.EQUALS, pgProgetto);
						});
			});
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		sql.addSQLClause("AND","V_PROGETTO_PADRE.PG_PROGETTO_PADRE IN ( SELECT DISTINCT PG_PROGETTO FROM V_PROGETTO_PADRE WHERE CD_PROGETTO = ?)");
		sql.addParameter(progetto ,java.sql.Types.VARCHAR,0);
	    return sql;
	}else{
		sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
	}
	return sql;
}

public SQLBuilder selectModuloForPrintByClause (UserContext userContext,Stampa_elenco_progetti_laBulk stampa,ProgettoBulk modulo,CompoundFindClause clause) throws ComponentException, PersistencyException{
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	String commessa = stampa.getCdCommessaForPrint();
	sql.addClause( clause );
	sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addClause("AND", "tipo_fase", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
	sql.addClause("AND","pg_progetto_padre",sql.EQUALS,stampa.getCommessaForPrint().getPg_progetto());
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		sql.addSQLClause("AND","V_PROGETTO_PADRE.PG_PROGETTO_PADRE IN ( SELECT DISTINCT PG_PROGETTO FROM V_PROGETTO_PADRE WHERE CD_PROGETTO = ?)");
		sql.addParameter(commessa ,java.sql.Types.VARCHAR,0);
	    return sql;
	}else{
		sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));		
	}
	return sql;	
}

	private void allineaAbilitazioniTerzoLivello(UserContext uc, ProgettoBulk prg) throws ComponentException, PersistencyException{
		allineaAbilitazioniTerzoLivello(uc, prg, ProgettoBulk.TIPO_FASE_PREVISIONE, prg.getFl_previsione());
		allineaAbilitazioniTerzoLivello(uc, prg, ProgettoBulk.TIPO_FASE_GESTIONE, prg.getFl_gestione());
	}
	
	private void allineaAbilitazioniTerzoLivello(UserContext uc, ProgettoBulk prg, String pTipoFase, Boolean pFaseAttiva) throws ComponentException, PersistencyException{
		ProgettoHome homeSip = (ProgettoHome)getHome(uc, ProgettoBulk.class, "PROGETTO_SIP");

		SQLBuilder sql = homeSip.createSQLBuilder();
		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, prg.getEsercizio());
		sql.addClause(FindClause.AND, "tipo_fase", SQLBuilder.EQUALS, pTipoFase);
		sql.addClause(FindClause.AND, "progettopadre", SQLBuilder.EQUALS, prg);
		sql.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO.intValue());
			
		List<ProgettoBulk> list = homeSip.fetchAll(sql);
		List<ProgettoBulk> listAbilitate = new ArrayList<>();
		List<ProgettoBulk> listToDelete = new ArrayList<>();

		for (ProgettoBulk progettoBulk : list) {
			if (!pFaseAttiva) {
				listToDelete.add(progettoBulk);
			} else {
				boolean uoAbilitata = false;
				for(int i = 0; prg.getDettagli().size() > i; i++) {
					Progetto_uoBulk progettoUO = (Progetto_uoBulk)prg.getDettagli().get(i);
					if (progettoUO.getCd_unita_organizzativa().equals(progettoBulk.getCd_unita_organizzativa())) {
						uoAbilitata = true;
						listAbilitate.add(progettoBulk);
						break;
					}
				}
				if (!uoAbilitata)
					listToDelete.add(progettoBulk);
			}
		}
		
		for (ProgettoBulk progettoBulk : listToDelete)
			homeSip.delete(progettoBulk, uc);

		if (pFaseAttiva) { 
			for(int i = 0; prg.getDettagli().size() > i; i++) {
				Progetto_uoBulk progettoUO = (Progetto_uoBulk)prg.getDettagli().get(i);
			
				boolean uoPresente = false;
				for (ProgettoBulk progettoBulk : listAbilitate) {
					if (progettoUO.getCd_unita_organizzativa().equals(progettoBulk.getCd_unita_organizzativa()))
						uoPresente = true;
				}
				if (!uoPresente) {
					Integer pgProgetto = null;
	
					ProgettoHome home = (ProgettoHome)getHome(uc, ProgettoBulk.class);
					SQLBuilder sqlPgProgetto = home.createSQLBuilder();
					sqlPgProgetto.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, prg.getEsercizio());
					sqlPgProgetto.addClause(FindClause.AND, "tipo_fase", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
					sqlPgProgetto.addClause(FindClause.AND, "progettopadre", SQLBuilder.EQUALS, prg);
					sqlPgProgetto.addClause(FindClause.AND, "unita_organizzativa", SQLBuilder.EQUALS, progettoUO.getUnita_organizzativa());
					sqlPgProgetto.addClause(FindClause.AND, "livello", SQLBuilder.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO.intValue());
					List<ProgettoBulk> listPgProgetto = home.fetchAll(sqlPgProgetto);
					if (!listPgProgetto.isEmpty())
						pgProgetto = listPgProgetto.get(0).getPg_progetto();
	
					ProgettoBulk progettoTerzo = copyProgettoAbilUo(uc, prg, pgProgetto, progettoUO.getUnita_organizzativa());
					progettoTerzo.setTipo_fase(pTipoFase);
					progettoTerzo.setTipo_fase_progetto_padre(pTipoFase);					
					homeSip.insert(progettoTerzo, uc);
				}
			}
		}
	}	 
	
	private ProgettoBulk copyProgettoAbilUo(UserContext uc, ProgettoBulk prgToCopy, Integer pgProgetto, Unita_organizzativaBulk uo) throws ComponentException{
		ProgettoBulk progettoTerzo = new ProgettoBulk();
		progettoTerzo.setEsercizio(prgToCopy.getEsercizio());
		progettoTerzo.setTipo_fase(prgToCopy.getTipo_fase());

		if (pgProgetto!=null) 
			progettoTerzo.setPg_progetto(pgProgetto);
		else {
			java.math.BigDecimal sq_progetto;
			sq_progetto = getSequence(uc);
			progettoTerzo.setPg_progetto(sq_progetto);
		}

		StringBuffer cdProgetto = new StringBuffer(prgToCopy.getCd_progetto()+'.'+uo.getCd_unita_organizzativa());
		if (cdProgetto.length()>30)
			progettoTerzo.setCd_progetto(cdProgetto.substring(1, 29));
		else
			progettoTerzo.setCd_progetto(cdProgetto.toString());
		
		progettoTerzo.setProgettopadre(prgToCopy);
		progettoTerzo.setDs_progetto(prgToCopy.getDs_progetto());
		progettoTerzo.setUnita_organizzativa(uo);
		progettoTerzo.setResponsabile(prgToCopy.getResponsabile());
		progettoTerzo.setDt_inizio(prgToCopy.getDt_inizio());
		progettoTerzo.setImporto_progetto(BigDecimal.ZERO);
		progettoTerzo.setStato(prgToCopy.getStato());
		progettoTerzo.setDurata_progetto(prgToCopy.getDurata_progetto());
		progettoTerzo.setLivello(ProgettoBulk.LIVELLO_PROGETTO_TERZO.intValue());
		progettoTerzo.setFl_piano_triennale(prgToCopy.getFl_piano_triennale());
		progettoTerzo.setFl_utilizzabile(prgToCopy.getFl_utilizzabile());
		progettoTerzo.setUser(prgToCopy.getUser());
		progettoTerzo.setToBeCreated();
		return progettoTerzo;
	}
    /**
     * Esegue una la parte di validazione di eliminaConBulk.
     */
    protected void validaEliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
    	try{
    		Progetto_sipHome moduli_utilizzatiHome = ((Progetto_sipHome)getHome(usercontext,Progetto_sipBulk.class,"V_SIP_MODULI_VALIDI"));

			SQLBuilder sqlModuli = moduli_utilizzatiHome.createSQLBuilderAll();
			sqlModuli.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, ((ProgettoBulk)oggettobulk).getPg_progetto());

			sqlModuli.openParenthesis(FindClause.AND);
			sqlModuli.addSQLClause(FindClause.OR, "V_SIP_MODULI_VALIDI.FL_CANCELLABILE", SQLBuilder.EQUALS, "N");
			sqlModuli.addSQLClause(FindClause.OR, "V_SIP_MODULI_VALIDI.FL_TERMINABILE", SQLBuilder.EQUALS, "N");
			sqlModuli.closeParenthesis();
			
			SQLBroker brokerUtilizzati = moduli_utilizzatiHome.createBroker(sqlModuli);
			if (brokerUtilizzati.next())
				throw new ApplicationException("Impossibile cancellare il progetto. Risulta già essere utilizzato.");
				
			for (Iterator<Progetto_uoBulk> iterator = ((ProgettoBulk)oggettobulk).getDettagli().iterator(); iterator.hasNext();) {
				validaCancellazioneUoAssociata(usercontext, (ProgettoBulk)oggettobulk, iterator.next());
			}
        }catch(Throwable throwable){
            throw handleException(throwable);
        }
    }
    
    public SQLBuilder selectVoce_piano_economicoByClause(UserContext userContext, Progetto_piano_economicoBulk pianoEconomico, Voce_piano_economico_prgBulk vocePiano, CompoundFindClause clauses) throws ComponentException {

    	Voce_piano_economico_prgHome home = (Voce_piano_economico_prgHome)getHome(userContext, Voce_piano_economico_prgBulk.class);
    	SQLBuilder sql = home.createSQLBuilder();
    	sql.addTableToHeader("UNITA_ORGANIZZATIVA");
    	sql.addSQLJoin("VOCE_PIANO_ECONOMICO_PRG.CD_UNITA_ORGANIZZATIVA", "UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");

    	sql.openParenthesis(FindClause.AND);
    	sql.addClause(FindClause.OR, "cd_unita_organizzativa", SQLBuilder.EQUALS, pianoEconomico.getProgetto().getCd_unita_organizzativa());
    	sql.addSQLClause(FindClause.OR, "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", SQLBuilder.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
    	sql.closeParenthesis();

    	Optional.ofNullable(pianoEconomico.getProgetto()).flatMap(el->Optional.ofNullable(el.getOtherField()))
    			.flatMap(el->Optional.ofNullable(el.getTipoFinanziamento()))
    			.ifPresent(tipoFin->{
    				if (!tipoFin.getFlAssCatVociInd() || !tipoFin.getFlAssCatVociDet() || !tipoFin.getFlAssCatVociAltro()) {
    			    	sql.openParenthesis(FindClause.AND);
    			    	sql.addClause(FindClause.OR, "tipologia", SQLBuilder.ISNULL, null);
    			    	if (!tipoFin.getFlAssCatVociInd())	
	    					sql.addClause(FindClause.OR, "tipologia", SQLBuilder.NOT_EQUALS, Voce_piano_economico_prgBulk.PERSONALE_INDET);
	    				if (!tipoFin.getFlAssCatVociDet())
	    					sql.addClause(FindClause.OR, "tipologia", SQLBuilder.NOT_EQUALS, Voce_piano_economico_prgBulk.PERSONALE_DETER);
	    				if (!tipoFin.getFlAssCatVociAltro())
	    					sql.addClause(FindClause.OR, "tipologia", SQLBuilder.NOT_EQUALS, Voce_piano_economico_prgBulk.PERSONALE_OTHER);
	    		    	sql.closeParenthesis();
    				}   				
    			});

    	sql.addClause(clauses);
    	sql.addOrderBy("cd_voce_piano");
    	return sql;

    }

    public void validaCancellazionePianoEconomicoAssociato(UserContext userContext, ProgettoBulk progetto, OggettoBulk dett) throws ComponentException{
    	Progetto_piano_economicoBulk piano = (Progetto_piano_economicoBulk) dett;
    	
    	try {
    		Pdg_Modulo_EntrateHome homeEtr = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
    		SQLBuilder sqlEtr = homeEtr.createSQLBuilder();
    		sqlEtr.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,piano.getPg_progetto());    		
    		sqlEtr.addClause(FindClause.AND,"cd_unita_piano",SQLBuilder.EQUALS,piano.getVoce_piano_economico().getCd_unita_organizzativa());
    		sqlEtr.addClause(FindClause.AND,"cd_voce_piano",SQLBuilder.EQUALS,piano.getVoce_piano_economico().getCd_voce_piano());    		

    		List resultEtr = homeEtr.fetchAll(sqlEtr);
    		if (!resultEtr.isEmpty())
    			throw new ApplicationException("Impossibile cancellare la voce "+piano.getCd_voce_piano()+" in quanto "+
                   "è già stata collegata al preventivo decisionale del progetto -  parte entrate.");

    		Pdg_modulo_speseHome homeSpe = (Pdg_modulo_speseHome)getHome(userContext,Pdg_modulo_speseBulk.class);
    		SQLBuilder sqlSpe = homeSpe.createSQLBuilder();
    		sqlSpe.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,piano.getPg_progetto());    		
    		sqlSpe.addClause(FindClause.AND,"cd_unita_piano",SQLBuilder.EQUALS,piano.getVoce_piano_economico().getCd_unita_organizzativa());
    		sqlSpe.addClause(FindClause.AND,"cd_voce_piano",SQLBuilder.EQUALS,piano.getVoce_piano_economico().getCd_voce_piano());    		

    		List resultSpe = homeSpe.fetchAll(sqlSpe);
    		if (!resultSpe.isEmpty())
    			throw new ApplicationException("Impossibile cancellare la voce "+piano.getCd_voce_piano()+" in quanto "+
                   "è già stata collegata al preventivo decisionale del progetto -  parte spese.");

    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }
    
    public void validaCancellazioneVoceAssociataPianoEconomico(UserContext userContext, Progetto_piano_economicoBulk prgPiano, OggettoBulk dett) throws ComponentException{
    	Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk) dett;
    	
    	try {
    		if (Elemento_voceHome.GESTIONE_ENTRATE.equals(assVoce.getTi_gestione())) {
    			Pdg_modulo_entrate_gestHome homeEtr = (Pdg_modulo_entrate_gestHome)getHome(userContext,Pdg_modulo_entrate_gestBulk.class);
    			SQLBuilder sqlEtr = homeEtr.createSQLBuilder();
    			sqlEtr.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,assVoce.getPg_progetto());    		
    			sqlEtr.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,assVoce.getEsercizio_voce());
    			sqlEtr.addClause(FindClause.AND,"ti_appartenenza",SQLBuilder.EQUALS,assVoce.getTi_appartenenza());    		
    			sqlEtr.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,assVoce.getTi_gestione());
    			sqlEtr.addClause(FindClause.AND,"cd_elemento_voce",SQLBuilder.EQUALS,assVoce.getCd_elemento_voce());

    			List resultEtr = homeEtr.fetchAll(sqlEtr);
	    		if (!resultEtr.isEmpty())
	    			throw new ApplicationException("Impossibile cancellare la voce "+assVoce.getCd_elemento_voce()+" in quanto "+
	                   "già collegata al progetto nel preventivo gestionale -  parte entrate.");
    		} else {
    			
        		Pdg_modulo_spese_gestHome homeSpe = (Pdg_modulo_spese_gestHome)getHome(userContext,Pdg_modulo_spese_gestBulk.class);
        		SQLBuilder sqlSpe = homeSpe.createSQLBuilder();
        		sqlSpe.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,assVoce.getPg_progetto());    		
    			sqlSpe.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,assVoce.getEsercizio_voce());
    			sqlSpe.addClause(FindClause.AND,"ti_appartenenza",SQLBuilder.EQUALS,assVoce.getTi_appartenenza());    		
    			sqlSpe.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,assVoce.getTi_gestione());
    			sqlSpe.addClause(FindClause.AND,"cd_elemento_voce",SQLBuilder.EQUALS,assVoce.getCd_elemento_voce());

        		List resultSpe = homeSpe.fetchAll(sqlSpe);
        		if (!resultSpe.isEmpty())
        			throw new ApplicationException("Impossibile cancellare la voce "+assVoce.getCd_elemento_voce()+" in quanto "+
                       "già collegata al progetto nel preventivo gestionale -  parte spese.");
    			
        		Pdg_variazione_riga_gestHome homeVarSpe = (Pdg_variazione_riga_gestHome)getHome(userContext,Pdg_variazione_riga_gestBulk.class);
        		SQLBuilder sqlVarSpe = homeVarSpe.createSQLBuilder();
        		sqlVarSpe.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,assVoce.getPg_progetto());    		
        		sqlVarSpe.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,assVoce.getEsercizio_voce());
        		sqlVarSpe.addClause(FindClause.AND,"ti_appartenenza",SQLBuilder.EQUALS,assVoce.getTi_appartenenza());    		
        		sqlVarSpe.addClause(FindClause.AND,"ti_gestione",SQLBuilder.EQUALS,assVoce.getTi_gestione());
        		sqlVarSpe.addClause(FindClause.AND,"cd_elemento_voce",SQLBuilder.EQUALS,assVoce.getCd_elemento_voce());

        		List resultVarSpe = homeVarSpe.fetchAll(sqlVarSpe);
        		if (!resultVarSpe.isEmpty())
        			throw new ApplicationException("Impossibile cancellare la voce "+assVoce.getCd_elemento_voce()+" in quanto "+
                       "già collegata al progetto in una variazione di competenza -  parte spese.");
    		}

    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }    
	
    public SQLBuilder selectElemento_voceByClause (UserContext userContext, Ass_progetto_piaeco_voceBulk assPiaecoVoce, Elemento_voceBulk voce, CompoundFindClause clauses) throws ComponentException {
    	try {
	    	Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
	    	SQLBuilder sql = home.createSQLBuilder();
	        
    		if (!Optional.of(assPiaecoVoce.getEsercizio_piano()).isPresent())
    			sql.addSQLClause(FindClause.AND, "1!=1"); //Condizione inserita per far fallire la query
    		else {
    	    	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
    	    	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(assPiaecoVoce.getEsercizio_piano()));
				final Integer livelloPdgDecisSpe = Optional.ofNullable(parCnrBulk)
						.flatMap(parametri_cnrBulk -> Optional.ofNullable(parametri_cnrBulk.getLivello_pdg_decis_spe()))
						.orElse(6);

				sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, assPiaecoVoce.getEsercizio_piano());
		
		    	sql.openParenthesis(FindClause.AND);
		    	sql.openParenthesis(FindClause.OR);
		    	sql.addClause(FindClause.AND, "cd_unita_piano", SQLBuilder.ISNULL, null);
		    	sql.addClause(FindClause.AND, "cd_voce_piano", SQLBuilder.ISNULL, null);
		    	sql.closeParenthesis();
		    	sql.openParenthesis(FindClause.OR);
		    	sql.addClause(FindClause.AND, "cd_unita_piano", SQLBuilder.EQUALS, assPiaecoVoce.getCd_unita_organizzativa());
		    	sql.addClause(FindClause.AND, "cd_voce_piano", SQLBuilder.EQUALS, assPiaecoVoce.getCd_voce_piano());
		    	sql.closeParenthesis();
		    	sql.closeParenthesis();
		
		    	sql.addClause(clauses);
		    	
		        sql.addTableToHeader("PARAMETRI_LIVELLI");
		        sql.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");
		
		        sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL", "CLASS_VOCE");
		        sql.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "CLASS_VOCE.ID_CLASSIFICAZIONE");
		        sql.addSQLJoin("CLASS_VOCE.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");
		
				sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI", "CLASS_PARENT");
				sql.addSQLJoin("CLASS_VOCE.ID_LIV" + livelloPdgDecisSpe,"CLASS_PARENT.ID_CLASSIFICAZIONE");
				sql.addSQLClause(FindClause.AND, "CLASS_PARENT.NR_LIVELLO", SQLBuilder.EQUALS, livelloPdgDecisSpe);
			    sql.openParenthesis(FindClause.AND);
			      sql.addSQLClause(FindClause.AND, "CLASS_PARENT.FL_ACCENTRATO", SQLBuilder.EQUALS, "Y");
			      sql.addSQLClause(FindClause.OR, "CLASS_PARENT.FL_DECENTRATO", SQLBuilder.EQUALS, "Y");
			    sql.closeParenthesis();
			    sql.addSQLClause(FindClause.AND, "CLASS_PARENT.ESERCIZIO", SQLBuilder.EQUALS, assPiaecoVoce.getEsercizio_piano());
			    sql.addSQLClause(FindClause.AND, "CLASS_PARENT.TI_GESTIONE", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE);
			    sql.addSQLClause(FindClause.AND, "CLASS_PARENT.FL_SOLO_GESTIONE", SQLBuilder.EQUALS,"N");
    		}
	    	sql.addOrderBy("cd_elemento_voce");
	
	    	return sql;
    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }

    private void validaPianoEconomico(UserContext userContext, ProgettoBulk progetto) throws ComponentException {
    	try {
	   		it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
	   		if (Optional.ofNullable(annoFrom).isPresent()) {
		   		validaVociPianoEconomicoDecisionale(userContext, progetto, annoFrom.intValue());
		   		validaVociPianoEconomicoGestionale(userContext, progetto, annoFrom.intValue());
		   		validaSaldiPianoEconomico(userContext, progetto, annoFrom.intValue());
		   		validaTipoFinanziamento(userContext, progetto, annoFrom.intValue());
		   		validaQuadraturaPianoEconomico(userContext, progetto, annoFrom.intValue());
	   		};
    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }

    private void validaVociPianoEconomicoDecisionale(UserContext userContext, ProgettoBulk progetto, Integer annoFrom) throws ComponentException {
    	try {
	    	Elemento_voceHome elementoVoceHome = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
	    	V_classificazione_vociHome classVoceHome = (V_classificazione_vociHome)getHome(userContext, V_classificazione_vociBulk.class);
	
	    	Pdg_modulo_speseHome pdgModuloHome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
			SQLBuilder sqlPdgModulo = pdgModuloHome.createSQLBuilder();
			sqlPdgModulo.addClause(FindClause.AND,"esercizio",SQLBuilder.GREATER_EQUALS,annoFrom);
			sqlPdgModulo.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,progetto.getPg_progetto());
			sqlPdgModulo.addClause(FindClause.AND,"cd_unita_piano",SQLBuilder.ISNOTNULL,null);
			sqlPdgModulo.addClause(FindClause.AND,"cd_voce_piano",SQLBuilder.ISNOTNULL,null);
	
			List<Pdg_modulo_speseBulk> pdgModuloList = new it.cnr.jada.bulk.BulkList(pdgModuloHome.fetchAll(sqlPdgModulo));
		
			pdgModuloList.stream().collect(Collectors.groupingBy(Pdg_modulo_speseBulk::getEsercizio))
			.entrySet().forEach(entryEse->{
				try {
					Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
					Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(entryEse.getKey()));

					entryEse.getValue().stream().collect(Collectors.groupingBy(Pdg_modulo_speseBulk::getVoce_piano_economico))
						.entrySet().forEach(entryVpe->{
							if (!progetto.getAllDetailsProgettoPianoEconomico().stream()
									.filter(el->el.getEsercizio_piano().equals(entryEse.getKey()))
									.filter(el->el.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey())).findAny().isPresent())
								throw new ApplicationRuntimeException("Attenzione: non risulta presente la voce del piano economico "+
										entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" già inserita in previsione (Es: " + 
										entryEse.getKey()+" - Cdr: "+
										entryVpe.getValue().stream().map(Pdg_modulo_speseBulk::getCd_centro_responsabilita).findFirst().orElse(null)+
										"). Operazione non consentita!");
	
							BigDecimal imDecentrataInt = entryVpe.getValue().stream()
									.map(pme->Optional.ofNullable(pme.getIm_spese_gest_decentrata_int()).orElse(BigDecimal.ZERO))
									.reduce((x, y) -> x.add(y)).get();
							
							BigDecimal imDecentrataInt2 = progetto.getAllDetailsProgettoPianoEconomico().stream()
									.filter(ppe->ppe.getEsercizio_piano().equals(entryEse.getKey()))
									.filter(ppe->ppe.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey()))
									.map(ppe->Optional.ofNullable(ppe.getIm_spesa_cofinanziato()).orElse(BigDecimal.ZERO))
									.reduce((x, y) -> x.add(y)).get();
							
							if (imDecentrataInt2.compareTo(imDecentrataInt)<0)
								throw new ApplicationRuntimeException("Attenzione: l'importo cofinanziato per la voce del piano economico "+
										entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" non può essere inferiore all'importo già inserito in previsione "	+ 
										"per le fonti decentrate interne ("+new EuroFormat().format(imDecentrataInt)+"). Operazione non consentita!");
								
							BigDecimal imDecentrataEst = entryVpe.getValue().stream()
									.map(pme->Optional.ofNullable(pme.getIm_spese_gest_decentrata_est()).orElse(BigDecimal.ZERO))
									.reduce((x, y) -> x.add(y)).get();
							
							BigDecimal imDecentrataEst2 = progetto.getAllDetailsProgettoPianoEconomico().stream()
									.filter(ppe->ppe.getEsercizio_piano().equals(entryEse.getKey()))
									.filter(ppe->ppe.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey()))
									.map(ppe->Optional.ofNullable(ppe.getIm_spesa_finanziato()).orElse(BigDecimal.ZERO))
									.reduce((x, y) -> x.add(y)).get();
							
							if (imDecentrataEst2.compareTo(imDecentrataEst)<0)
								throw new ApplicationRuntimeException("Attenzione: l'importo finanziato per la voce del piano economico "+
										entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" non può essere inferiore all'importo già inserito in previsione "	+ 
										"per le fonti decentrate esterne ("+new EuroFormat().format(imDecentrataEst)+"). Operazione non consentita!");
	
							BulkList<Elemento_voceBulk> vociAssociatePianoEconomico = new BulkList<Elemento_voceBulk>();
							progetto.getAllDetailsProgettoPianoEconomico().stream()
									.filter(el->el.getEsercizio_piano().equals(entryEse.getKey()))
									.filter(el->el.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey()))
									.map(Progetto_piano_economicoBulk::getVociBilancioAssociate)
									.forEach(el->vociAssociatePianoEconomico.addAll(el.stream()
											.map(Ass_progetto_piaeco_voceBulk::getElemento_voce)
											.collect(Collectors.toList())));

							entryVpe.getValue().stream().forEach(pms->{
								List<Elemento_voceBulk> vociAssociateClassificazione;
								V_classificazione_vociBulk classVoce;
								try {
									classVoce = (V_classificazione_vociBulk)classVoceHome.findByPrimaryKey(pms.getClassificazione());
									SQLBuilder sqlElementoVoce = elementoVoceHome.createSQLBuilder();

									sqlElementoVoce.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,entryEse.getKey());

									sqlElementoVoce.addTableToHeader("PARAMETRI_LIVELLI");
									sqlElementoVoce.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");
									sqlElementoVoce.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
									sqlElementoVoce.addSQLJoin("ELEMENTO_VOCE.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
									sqlElementoVoce.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");
									sqlElementoVoce.addSQLClause(FindClause.AND, "V_CLASSIFICAZIONE_VOCI_ALL.ID_LIV"+parCnrBulk.getLivello_pdg_decis_spe(), SQLBuilder.EQUALS, pms.getId_classificazione());

									vociAssociateClassificazione = new it.cnr.jada.bulk.BulkList(elementoVoceHome.fetchAll(sqlElementoVoce));
								} catch(Throwable e) {
									throw new ApplicationRuntimeException(e);
								}

								if (!vociAssociatePianoEconomico.stream()
									.filter(voce->Optional.ofNullable(vociAssociateClassificazione).orElse(Collections.emptyList()).contains(voce))
									.findAny().isPresent())
									throw new ApplicationRuntimeException("Attenzione: non risulta presente per la voce del piano economico "+
											entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" l'associazione alle voci di bilancio già inserite in previsione (Es: " + 
											entryEse.getKey()+" - Cdr: "+pms.getCd_centro_responsabilita()+
											" - Classificazione: "+(parCnrBulk.getFl_pdg_codlast()?classVoce.getCd_livello_last():classVoce.getCd_classificazione())+
											"). Operazione non consentita!");
							});
						});
			    	} catch(Throwable e) {
			    		throw new ApplicationRuntimeException(e);
			    	}
				});
    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }

    private void validaVociPianoEconomicoGestionale(UserContext userContext, ProgettoBulk progetto, Integer annoFrom) throws ComponentException {
    	try {
	    	Pdg_modulo_spese_gestHome pdgModuloGestHome = (Pdg_modulo_spese_gestHome)getHome(userContext, Pdg_modulo_spese_gestBulk.class);
			SQLBuilder sqlPdgModuloGest = pdgModuloGestHome.createSQLBuilder();
			sqlPdgModuloGest.addClause(FindClause.AND,"esercizio",SQLBuilder.GREATER_EQUALS,annoFrom);
			sqlPdgModuloGest.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,progetto.getPg_progetto());
			sqlPdgModuloGest.addTableToHeader("PDG_MODULO_SPESE");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.ESERCIZIO", "PDG_MODULO_SPESE_GEST.ESERCIZIO");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.CD_CENTRO_RESPONSABILITA", "PDG_MODULO_SPESE_GEST.CD_CENTRO_RESPONSABILITA");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.PG_PROGETTO", "PDG_MODULO_SPESE_GEST.PG_PROGETTO");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.ID_CLASSIFICAZIONE", "PDG_MODULO_SPESE_GEST.ID_CLASSIFICAZIONE");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.CD_CDS_AREA", "PDG_MODULO_SPESE_GEST.CD_CDS_AREA");
			sqlPdgModuloGest.addSQLJoin("PDG_MODULO_SPESE.PG_DETTAGLIO", "PDG_MODULO_SPESE_GEST.PG_DETTAGLIO");
			sqlPdgModuloGest.addSQLClause(FindClause.AND,"PDG_MODULO_SPESE.CD_UNITA_PIANO",SQLBuilder.ISNOTNULL,null);
			sqlPdgModuloGest.addSQLClause(FindClause.AND,"PDG_MODULO_SPESE.CD_VOCE_PIANO",SQLBuilder.ISNOTNULL,null);
	
			List<Pdg_modulo_spese_gestBulk> pdgModuloGestList = new it.cnr.jada.bulk.BulkList(pdgModuloGestHome.fetchAll(sqlPdgModuloGest));

			pdgModuloGestList.stream().collect(Collectors.groupingBy(Pdg_modulo_spese_gestBulk::getEsercizio))
			.entrySet().forEach(entryEse->{
				entryEse.getValue().stream().collect(Collectors.groupingBy(el->el.getPdg_modulo_spese().getVoce_piano_economico()))
					.entrySet().forEach(entryVpe->{
						if (!progetto.getAllDetailsProgettoPianoEconomico().stream()
								.filter(el->el.getEsercizio_piano().equals(entryEse.getKey()))
								.filter(el->el.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey())).findAny().isPresent())
							throw new ApplicationRuntimeException("Attenzione: non risulta presente la voce del piano economico "+
									entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" già inserita in previsione (Es: " + 
									entryEse.getKey()+" - Cdr: "+
									entryVpe.getValue().stream().map(Pdg_modulo_spese_gestBulk::getCd_centro_responsabilita).findFirst().orElse(null)+
									"). Operazione non consentita!");

						BulkList<Elemento_voceBulk> vociAssociatePianoEconomico = new BulkList<Elemento_voceBulk>();
						progetto.getAllDetailsProgettoPianoEconomico().stream()
								.filter(el->el.getEsercizio_piano().equals(entryEse.getKey()))
								.filter(el->el.getVoce_piano_economico().equalsByPrimaryKey(entryVpe.getKey()))
								.map(Progetto_piano_economicoBulk::getVociBilancioAssociate)
								.forEach(el->vociAssociatePianoEconomico.addAll(el.stream()
										.map(Ass_progetto_piaeco_voceBulk::getElemento_voce)
										.collect(Collectors.toList())));

						entryVpe.getValue().stream().collect(Collectors.groupingBy(Pdg_modulo_spese_gestBulk::getElemento_voce))
							.keySet().stream().filter(voce->!vociAssociatePianoEconomico.contains(voce)).findFirst().ifPresent(voce->{
								throw new ApplicationRuntimeException("Attenzione: non risulta presente per la voce del piano economico "+
									entryEse.getKey()+"/"+entryVpe.getKey().getCd_voce_piano()+" l'associazione alla voce di bilancio già inserita in previsione (Es: " + 
									entryEse.getKey()+" - Cdr: "+
									entryVpe.getValue().stream().map(Pdg_modulo_spese_gestBulk::getCd_centro_responsabilita).findFirst().orElse(null)+
									" - Voce: "+voce.getEsercizio()+"/"+voce.getTi_gestione()+"/"+voce.getCd_elemento_voce()+
									"). Operazione non consentita!");
							});
					});						
			});
    	} catch(Throwable e) {
    		throw handleException(e);
    	}
    }

    private void validaSaldiPianoEconomico(UserContext userContext, ProgettoBulk progetto, Integer annoFrom) throws ComponentException {
		try{
			if (!progetto.isPianoEconomicoRequired()) return;
			progetto.getAllDetailsProgettoPianoEconomico().stream()
				.filter(el->el.getEsercizio_piano().compareTo(annoFrom)>=0).forEach(ppe->{
	   			V_saldi_piano_econom_progettoBulk saldo;
	   			try{
	                saldo = ((V_saldi_piano_econom_progettoHome)getHome( userContext,V_saldi_piano_econom_progettoBulk.class )).
	                        cercaSaldoPianoEconomico(ppe, "S");
		    	} catch(Throwable e) {
		    		throw new ApplicationRuntimeException(e);
		    	}    			
	
	   			Optional.ofNullable(saldo).filter(el->el.getDispResiduaFinanziamento().compareTo(BigDecimal.ZERO)<0).ifPresent(el->{
	   	           	throw new ApplicationRuntimeException("Attenzione: l'importo finanziato per la voce del piano economico "+
	   	    				ppe.getEsercizio_piano()+"/"+ppe.getCd_voce_piano()+" non può essere inferiore all'importo assestato per le fonti decentrate interne ("+
	   	           			el.getAssestatoFinanziamento()+"). Operazione non consentita!");
				});
	
	  			Optional.ofNullable(saldo).filter(el->el.getDispResiduaCofinanziamento().compareTo(BigDecimal.ZERO)<0).ifPresent(el->{
	               	throw new ApplicationRuntimeException("Attenzione: l'importo cofinanziato per la voce del piano economico "+
	               			ppe.getEsercizio_piano()+"/"+ppe.getCd_voce_piano()+" non può essere inferiore all'importo assestato per le fonti decentrate interne ("+
	    					el.getAssestatoCofinanziamento()+"). Operazione non consentita!");
				});
	   		});
			
			Voce_f_saldi_cdr_lineaHome saldiHome = (Voce_f_saldi_cdr_lineaHome)getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class);
			SQLBuilder sqlSaldi = saldiHome.createSQLBuilder();
			sqlSaldi.addSQLClause(FindClause.AND,"VOCE_F_SALDI_CDR_LINEA.ESERCIZIO",SQLBuilder.GREATER_EQUALS,annoFrom);
			sqlSaldi.addSQLClause(FindClause.AND,"VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES",SQLBuilder.GREATER_EQUALS,annoFrom);
			sqlSaldi.addSQLClause(FindClause.AND,"VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE",SQLBuilder.GREATER_EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
			sqlSaldi.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
			sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","VOCE_F_SALDI_CDR_LINEA.ESERCIZIO");
			sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA","VOCE_F_SALDI_CDR_LINEA.CD_CENTRO_RESPONSABILITA");
			sqlSaldi.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA","VOCE_F_SALDI_CDR_LINEA.CD_LINEA_ATTIVITA");
			sqlSaldi.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO",SQLBuilder.EQUALS,progetto.getPg_progetto());

			sqlSaldi.openParenthesis(FindClause.AND);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.IM_STANZ_INIZIALE_A1",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_PIU",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VARIAZIONI_MENO",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.IM_STANZ_RES_IMPROPRIO",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VAR_PIU_STANZ_RES_IMP",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VAR_MENO_STANZ_RES_IMP",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VAR_PIU_OBBL_RES_PRO",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.addSQLClause(FindClause.OR,"VOCE_F_SALDI_CDR_LINEA.VAR_MENO_OBBL_RES_PRO",SQLBuilder.GREATER,BigDecimal.ZERO);
			sqlSaldi.closeParenthesis();
			
			Ass_progetto_piaeco_voceHome assPiaecoHome = (Ass_progetto_piaeco_voceHome)getHome(userContext, Ass_progetto_piaeco_voceBulk.class);
			SQLBuilder sqlExist = assPiaecoHome.createSQLBuilder();
			sqlExist.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.PG_PROGETTO","V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO");
			sqlExist.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.ESERCIZIO_PIANO","VOCE_F_SALDI_CDR_LINEA.ESERCIZIO_RES");
			sqlExist.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_APPARTENENZA","VOCE_F_SALDI_CDR_LINEA.TI_APPARTENENZA");
			sqlExist.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.TI_GESTIONE","VOCE_F_SALDI_CDR_LINEA.TI_GESTIONE");
			sqlExist.addSQLJoin("ASS_PROGETTO_PIAECO_VOCE.CD_ELEMENTO_VOCE","VOCE_F_SALDI_CDR_LINEA.CD_ELEMENTO_VOCE");			
			
			sqlSaldi.addSQLNotExistsClause(FindClause.AND, sqlExist);

			List<Voce_f_saldi_cdr_lineaBulk> saldiList = new it.cnr.jada.bulk.BulkList(saldiHome.fetchAll(sqlSaldi));
			saldiList.stream().findFirst().ifPresent(el->{
               	throw new ApplicationRuntimeException("Attenzione: risulta movimentata, per il progetto e per l'anno contabile "
               			+el.getEsercizio_res()+", la voce di bilancio " + el.getTi_gestione()+"/"+el.getCd_voce()+
               			" che non risulta associata a nessuna voce del piano economico per l'anno "+el.getEsercizio_res()+". " + 
               			"Operazione non consentita!");
			});
		} catch(Throwable e) {
			throw handleException(e);
		}
    }

    private void validaTipoFinanziamento(UserContext userContext, ProgettoBulk progetto, Integer annoFrom) throws ComponentException {
		try{
			Optional<TipoFinanziamentoBulk> optTipoFin = Optional.ofNullable(progetto.getOtherField()).flatMap(el->Optional.ofNullable(el.getTipoFinanziamento()));
	    	if (progetto.isDettagliPianoEconomicoPresenti()) {
	    		optTipoFin.filter(TipoFinanziamentoBulk::getFlPianoEcoFin)
	    				.orElseThrow(()-> new ApplicationException("Attenzione: Sul progetto risulta caricato un piano economico. "
	    						+ "Il tipo finanziamento selezionato non è coerente! Selezionare un tipo di finanziamento che non prevede "
	    						+ "piano economico o eliminare il piano economico stesso!"));
	    		
	    		if (optTipoFin.filter(el->!el.getFlAssCatVociDet()).isPresent() && 
	    			progetto.getAllDetailsProgettoPianoEconomico().stream()
	    					.filter(el2->el2.getVoce_piano_economico().isVocePersonaleTempoDeterminato())
	    					.findFirst().isPresent())
	    			throw new ApplicationException("Attenzione: Il tipo finanziamento selezionato non è coerente! "
	    					  		+ "Sul progetto risulta caricata una voce di piano economico di tipo 'Personale a Tempo Determinato' "
	    					  		+ "non consentita dal tipo di finanziamento.");
	
	    		if (optTipoFin.filter(el->!el.getFlAssCatVociInd()).isPresent() &&
	    			progetto.getAllDetailsProgettoPianoEconomico().stream()
	    					.filter(el2->el2.getVoce_piano_economico().isVocePersonaleTempoIndeterminato())
	    					.findFirst().isPresent())
	    			throw new ApplicationException("Attenzione: Il tipo finanziamento selezionato non è coerente! "
							  		+ "Sul progetto risulta caricata una voce di piano economico di tipo 'Personale a Tempo Indeterminato' "
							  		+ "non consentita dal tipo di finanziamento.");

	    		if (optTipoFin.filter(el->!el.getFlAssCatVociInd()).isPresent() &&
	    			progetto.getAllDetailsProgettoPianoEconomico().stream()
							.filter(el2->el2.getVoce_piano_economico().isVocePersonaleAltraTipologia())
							.findFirst().isPresent())
	    			throw new ApplicationException("Attenzione: Il tipo finanziamento selezionato non è coerente! "
							  		+ "Sul progetto risulta caricata una voce di piano economico di tipo 'Altro Personale' "
							  		+ "non consentita dal tipo di finanziamento.");
	    	}

	    	if (optTipoFin.flatMap(el->Optional.ofNullable(el.getFlPrevEntSpesa())).filter(el->el.equals(Boolean.FALSE)).isPresent()) {
		    	Pdg_Modulo_EntrateHome pdgModuloEntrateHome = (Pdg_Modulo_EntrateHome)getHome(userContext, Pdg_Modulo_EntrateBulk.class);
		        SQLBuilder sqlPdgModuloEntrate = pdgModuloEntrateHome.createSQLBuilder();
			   	if (Optional.ofNullable(annoFrom).isPresent())
			       	sqlPdgModuloEntrate.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, progetto.getEsercizio());
		        sqlPdgModuloEntrate.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, progetto.getPg_progetto());
		        
	    		if (sqlPdgModuloEntrate.executeExistsQuery(getConnection(userContext)))
	    			throw new ApplicationException("Attenzione: Il tipo finanziamento selezionato non è coerente! "
	    								+ "Sul progetto risultano caricati dati previsionali di entrata non previsti "
	    								+ "dal tipo di finanziamento.");
	
		    	Pdg_modulo_speseHome pdgModuloSpeseHome = (Pdg_modulo_speseHome)getHome(userContext, Pdg_modulo_speseBulk.class);
		        SQLBuilder sqlPdgModuloSpese = pdgModuloSpeseHome.createSQLBuilder();
			   	if (Optional.ofNullable(annoFrom).isPresent())
			       	sqlPdgModuloSpese.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, progetto.getEsercizio());
		        sqlPdgModuloSpese.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, progetto.getPg_progetto());
		        
		        sqlPdgModuloSpese.openParenthesis(FindClause.AND);
		        sqlPdgModuloSpese.addClause(FindClause.OR, "im_spese_gest_decentrata_int", SQLBuilder.NOT_EQUALS, BigDecimal.ZERO);
		        sqlPdgModuloSpese.addClause(FindClause.OR, "im_spese_gest_decentrata_est", SQLBuilder.NOT_EQUALS, BigDecimal.ZERO);
		        sqlPdgModuloSpese.closeParenthesis();
		        	
	    		if (sqlPdgModuloSpese.executeExistsQuery(getConnection(userContext)))
	    			throw new ApplicationException("Attenzione: Il tipo finanziamento selezionato non è coerente! "
	    								+ "Sul progetto risultano caricati dati previsionali di spesa con importi decentrati valorizzati "
	    								+ "non previsti dal tipo di finanziamento.");
	    	}
		} catch(Throwable e) {
			throw handleException(e);
		}
    }

    private void validaQuadraturaPianoEconomico(UserContext userContext, ProgettoBulk progetto, Integer annoFrom) throws ComponentException {
		try{
			Optional<TipoFinanziamentoBulk> optTipoFin = Optional.ofNullable(progetto.getOtherField()).flatMap(el->Optional.ofNullable(el.getTipoFinanziamento()));
	    	if (optTipoFin.flatMap(el->Optional.ofNullable(el.getFlAllPrevFin())).filter(el->el.equals(Boolean.TRUE)).isPresent()) {
				progetto.getAllDetailsProgettoPianoEconomico().stream()
						.filter(el->Optional.ofNullable(el.getVoce_piano_economico()).map(Voce_piano_economico_prgBulk::getFlAllPrevFin).orElse(Boolean.FALSE))
						.map(Progetto_piano_economicoBulk::getEsercizio_piano)
						.filter(annoPiano->Optional.ofNullable(annoFrom).map(el->annoPiano.compareTo(el)>=0).orElse(Boolean.TRUE))
						.distinct().forEach(annoPiano->{
							try {
						   		Pdg_modulo_costiHome pdgModuloHome = (Pdg_modulo_costiHome)getHome(userContext, Pdg_modulo_costiBulk.class);
						        SQLBuilder sqlPdgModulo = pdgModuloHome.createSQLBuilder();
						        sqlPdgModulo.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, annoPiano);
						        sqlPdgModulo.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, progetto.getPg_progetto());
						        sqlPdgModulo.addTableToHeader("PDG_MODULO");
						        sqlPdgModulo.addSQLJoin("PDG_MODULO_COSTI.ESERCIZIO", "PDG_MODULO.ESERCIZIO");
						        sqlPdgModulo.addSQLJoin("PDG_MODULO_COSTI.CD_CENTRO_RESPONSABILITA", "PDG_MODULO.CD_CENTRO_RESPONSABILITA");
						        sqlPdgModulo.addSQLJoin("PDG_MODULO_COSTI.PG_PROGETTO", "PDG_MODULO.PG_PROGETTO");
						        sqlPdgModulo.addSQLClause(FindClause.AND, "PDG_MODULO.STATO", SQLBuilder.NOT_EQUALS, Pdg_moduloBulk.STATO_AC);

								List<Pdg_modulo_costiBulk> pdgModuloList = new it.cnr.jada.bulk.BulkList(pdgModuloHome.fetchAll(sqlPdgModulo));
								pdgModuloList.stream().forEach(modCosti->{
									try {
										Utility.createSaldoComponentSession().checkDispPianoEconomicoProgetto(userContext, modCosti, Boolean.TRUE);
									} catch(Throwable e) {
										throw new ApplicationRuntimeException(e);
									}
						        });
							} catch(Throwable e) {
								throw new ApplicationRuntimeException(e);
							}
						});
			}
	        	
		} catch(Throwable e) {
			throw handleException(e);
		}
    }
    
    public SQLBuilder selectOtherField_tipoFinanziamentoByClause(UserContext userContext,ProgettoBulk progetto,TipoFinanziamentoBulk tipoFinanziamento,CompoundFindClause clauses) throws ComponentException, PersistencyException{
    	try {
	    	TipoFinanziamentoHome home = (TipoFinanziamentoHome)getHome(userContext, TipoFinanziamentoBulk.class);
	    	SQLBuilder sql = home.createSQLBuilder();
	        
	    	if (progetto.isDettagliPianoEconomicoPresenti()) {
	    		sql.addClause(FindClause.AND, "flPianoEcoFin", SQLBuilder.EQUALS, Boolean.TRUE);
	    		
	        	if (progetto.getAllDetailsProgettoPianoEconomico().stream()
	        			.filter(el->el.getVoce_piano_economico().isVocePersonaleTempoDeterminato())
	        			.count()>0)
	        		sql.addClause(FindClause.AND, "flAssCatVociDet", SQLBuilder.EQUALS, Boolean.TRUE);
	
	        	if (progetto.getAllDetailsProgettoPianoEconomico().stream()
	        			.filter(el->el.getVoce_piano_economico().isVocePersonaleTempoIndeterminato())
	        			.count()>0)
	        		sql.addClause(FindClause.AND, "flAssCatVociInd", SQLBuilder.EQUALS, Boolean.TRUE);
	
	        	if (progetto.getAllDetailsProgettoPianoEconomico().stream()
	        			.filter(el->el.getVoce_piano_economico().isVocePersonaleAltraTipologia())
	        			.count()>0)
	        		sql.addClause(FindClause.AND, "flAssCatVociAltro", SQLBuilder.EQUALS, Boolean.TRUE);
	    	}        	
	
	   		it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
	
	   		Pdg_modulo_costiHome pdgModuloHome = (Pdg_modulo_costiHome)getHome(userContext, Pdg_modulo_costiBulk.class);
	        SQLBuilder sqlPdgModulo = pdgModuloHome.createSQLBuilder();
		   	if (Optional.ofNullable(annoFrom).isPresent())
		       	sqlPdgModulo.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, annoFrom);
	        sqlPdgModulo.addClause(FindClause.AND, "pg_progetto", SQLBuilder.EQUALS, progetto.getPg_progetto());
	        	
	        if (sqlPdgModulo.executeExistsQuery(getConnection(userContext)))
	           	sql.addClause(FindClause.AND, "flPrevEntSpesa", SQLBuilder.EQUALS, Boolean.TRUE);
	
	    	sql.addClause(clauses);
	    	return sql;
		} catch(Throwable e) {
			throw handleException(e);
		}
    }
    
    private void validaDatePianoEconomico(UserContext userContext, ProgettoBulk progetto) throws ComponentException {
		try{
	    	if (progetto.isDatePianoEconomicoRequired()) {
	    		Optional.ofNullable(progetto.getOtherField().getDtInizio())
	    				.orElseThrow(()-> new ApplicationException("Attenzione: E' necessario indicare la data di inizio del progetto!"));
	    		Optional.ofNullable(progetto.getOtherField().getDtFine())
	    				.orElseThrow(()-> new ApplicationException("Attenzione: E' necessario indicare la data di fine del progetto!"));

	    		//cerco la data min e max di impegni fatti su GAE del progetto
	    		{
	    			ObbligazioneHome obblHome = (ObbligazioneHome)getHome(userContext, ObbligazioneBulk.class);
		    		SQLBuilder sqlObb = obblHome.createSQLBuilder();
		    		
		    		Obbligazione_scad_voceHome scadVoceHome = (Obbligazione_scad_voceHome)getHome(userContext, Obbligazione_scad_voceBulk.class);
		    		SQLBuilder sqlExist = scadVoceHome.createSQLBuilder();
		    		sqlExist.addSQLJoin("OBBLIGAZIONE.CD_CDS", "OBBLIGAZIONE_SCAD_VOCE.CD_CDS");
		    		sqlExist.addSQLJoin("OBBLIGAZIONE.ESERCIZIO", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO");
		    		sqlExist.addSQLJoin("OBBLIGAZIONE.ESERCIZIO_ORIGINALE", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO_ORIGINALE");
		    		sqlExist.addSQLJoin("OBBLIGAZIONE.PG_OBBLIGAZIONE", "OBBLIGAZIONE_SCAD_VOCE.PG_OBBLIGAZIONE");
		    		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "OBBLIGAZIONE_SCAD_VOCE.ESERCIZIO");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "OBBLIGAZIONE_SCAD_VOCE.CD_CENTRO_RESPONSABILITA");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "OBBLIGAZIONE_SCAD_VOCE.CD_LINEA_ATTIVITA");
		    		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());
		    		
		    		sqlObb.addSQLExistsClause(FindClause.AND, sqlExist);
		    		
		    		List<ObbligazioneBulk> listObb = obblHome.fetchAll(sqlObb);
	
		    		listObb.stream()
		    			   .min((p1, p2) -> p1.getDt_registrazione().compareTo(p2.getDt_registrazione()))
		    			   .filter(el->el.getDt_registrazione().before(progetto.getOtherField().getDtInizio()))
		    			   .ifPresent(el->{
		    				   throw new ApplicationRuntimeException("Attenzione! Esiste l'obbligazione "
		    				   		+ el.getEsercizio()+"/"+el.getEsercizio_originale()+"/"+el.getPg_obbligazione()
		    				   		+ " associata al progetto con data registrazione "
		    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_registrazione())
									+ " inferiore alla data di inizio "
									+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
									+ " del progetto stesso! Aggiornare la data di inizio del progetto con un valore coerente!");
		    			   });
		    		listObb.stream()
			 			   .max((p1, p2) -> p1.getDt_registrazione().compareTo(p2.getDt_registrazione()))
			 			   .filter(el->el.getDt_registrazione().after(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine())))
			 			   .ifPresent(el->{
	 				   throw new ApplicationRuntimeException("Attenzione! Esiste l'obbligazione "
	    				   		+ el.getEsercizio()+"/"+el.getEsercizio_originale()+"/"+el.getPg_obbligazione()
	    				   		+ " associata al progetto con data registrazione "
	    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_registrazione())
								+ " superiore alla data di fine/proroga "
								+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
								+ " del progetto stesso! Aggiornare la data di fine/proroga del progetto con un valore coerente!");
	 			    });
	    		}

	    		//cerco la data min e max di variazioni di bilancio di competenza
	    		{
		    		Pdg_variazioneHome pdgVarHome = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);
		    		SQLBuilder sqlVar = pdgVarHome.createSQLBuilder();
		    		sqlVar.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.DT_CHIUSURA", SQLBuilder.ISNOTNULL, null);
		    		sqlVar.addSQLClause(FindClause.AND, "PDG_VARIAZIONE.DT_ANNULLAMENTO", SQLBuilder.ISNULL, null);
		    		
		    		Pdg_variazione_riga_gestHome pdgVarRigaHome = (Pdg_variazione_riga_gestHome)getHome(userContext, Pdg_variazione_riga_gestBulk.class);
		    		SQLBuilder sqlExist = pdgVarRigaHome.createSQLBuilder();
		    		sqlExist.addSQLJoin("PDG_VARIAZIONE.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		    		sqlExist.addSQLJoin("PDG_VARIAZIONE.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG");
		    		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");
		    		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());
		    		
		    		sqlVar.addSQLExistsClause(FindClause.AND, sqlExist);
		    		
		    		List<Pdg_variazioneBulk> listVar = pdgVarHome.fetchAll(sqlVar);
	
		    		listVar.stream()
		    			   .min((p1, p2) -> p1.getDt_chiusura().compareTo(p2.getDt_chiusura()))
		    			   .filter(el->el.getDt_chiusura().before(progetto.getOtherField().getDtInizio()))
		    			   .ifPresent(el->{
		    				   throw new ApplicationRuntimeException("Attenzione! Esiste la variazione di competenza "
			    				   		+ el.getEsercizio()+"/"+el.getPg_variazione_pdg()
			    				   		+ " associata al progetto con data di chiusura "
			    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_chiusura())
										+ " inferiore alla data di inizio "
										+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
										+ " del progetto stesso! Aggiornare la data di inizio del progetto con un valore coerente!");
		    			   });
		    		listVar.stream()
			 			   .max((p1, p2) -> p1.getDt_chiusura().compareTo(p2.getDt_chiusura()))
			 			   .filter(el->el.getDt_chiusura().after(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine())))
			 			   .ifPresent(el->{
		    				   throw new ApplicationRuntimeException("Attenzione! Esiste la variazione di competenza "
			    				   		+ el.getEsercizio()+"/"+el.getPg_variazione_pdg()
			    				   		+ " associata al progetto con data di chiusura "
			    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_chiusura())
										+ " superiore alla data di fine/proroga "
										+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
										+ " del progetto stesso! Aggiornare la data di fine/proroga del progetto con un valore coerente!");
	 			   });
	    		}

	    		//cerco la data min e max di variazioni di bilancio di residuo
	    		{
		    		Var_stanz_resHome varHome = (Var_stanz_resHome)getHome(userContext, Var_stanz_resBulk.class);
		    		SQLBuilder sqlVar = varHome.createSQLBuilder();
		    		sqlVar.addSQLClause(FindClause.AND, "VAR_STANZ_RES.DT_CHIUSURA", SQLBuilder.ISNOTNULL, null);
		    		sqlVar.addSQLClause(FindClause.AND, "VAR_STANZ_RES.DT_ANNULLAMENTO", SQLBuilder.ISNULL, null);
		    		
		    		Var_stanz_res_rigaHome varRigaHome = (Var_stanz_res_rigaHome)getHome(userContext, Var_stanz_res_rigaBulk.class);
		    		SQLBuilder sqlExist = varRigaHome.createSQLBuilder();
					sqlExist.resetColumns();
					sqlExist.addColumn("1");
		    		sqlExist.addSQLJoin("VAR_STANZ_RES.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
		    		sqlExist.addSQLJoin("VAR_STANZ_RES.PG_VARIAZIONE", "VAR_STANZ_RES_RIGA.PG_VARIAZIONE");
		    		sqlExist.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VAR_STANZ_RES_RIGA.CD_CDR");
		    		sqlExist.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VAR_STANZ_RES_RIGA.CD_LINEA_ATTIVITA");
		    		sqlExist.addSQLClause(FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO", SQLBuilder.EQUALS, progetto.getPg_progetto());
		    		
		    		sqlVar.addSQLExistsClause(FindClause.AND, sqlExist);
		    		
		    		List<Var_stanz_resBulk> listVar = varHome.fetchAll(sqlVar);
	
		    		listVar.stream()
		    			   .min((p1, p2) -> p1.getDt_chiusura().compareTo(p2.getDt_chiusura()))
		    			   .filter(el->el.getDt_chiusura().before(progetto.getOtherField().getDtInizio()))
		    			   .ifPresent(el->{
		    				   throw new ApplicationRuntimeException("Attenzione! Esiste la variazione di residuo "
			    				   		+ el.getEsercizio()+"/"+el.getPg_variazione()
			    				   		+ " associata al progetto con data di chiusura "
			    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_chiusura())
										+ " inferiore alla data di inizio "
										+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
										+ " del progetto stesso! Aggiornare la data di inizio del progetto con un valore coerente!");
		    			   });
		    		listVar.stream()
			 			   .max((p1, p2) -> p1.getDt_chiusura().compareTo(p2.getDt_chiusura()))
			 			   .filter(el->el.getDt_chiusura().after(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine())))
			 			   .ifPresent(el->{
		    				   throw new ApplicationRuntimeException("Attenzione! Esiste la variazione di residuo "
		    						    + el.getEsercizio()+"/"+el.getPg_variazione()
			    				   		+ " associata al progetto con data di chiusura "
			    				   		+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDt_chiusura())
										+ " superiore alla data di fine/proroga "
										+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
										+ " del progetto stesso! Aggiornare la data di fine/proroga del progetto con un valore coerente!");
	 			   });
	    		}
	    	}	    					   
		} catch(Throwable e) {
			throw handleException(e);
		}
    }
 
}
