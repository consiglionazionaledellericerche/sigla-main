/*
 * Created on Jan 19, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg01.comp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.ejb.EJBException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.CostantiTi_gestione;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.messaggio00.bulk.MessaggioBulk;
import it.cnr.contab.messaggio00.bulk.MessaggioHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrHome;
import it.cnr.contab.pdg00.comp.PdGVariazioniComponent;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestHome;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneBulk;
import it.cnr.contab.pdg01.bulk.Tipo_variazioneHome;
import it.cnr.contab.prevent00.bulk.V_assestatoBulk;
import it.cnr.contab.prevent00.bulk.V_assestatoHome;
import it.cnr.contab.prevent00.bulk.Voce_f_saldi_cdr_lineaBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteHome;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailHome;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.OptionRequestException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SQLExceptionHandler;
import it.cnr.jada.util.Config;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class CRUDPdgVariazioneGestionaleComponent extends PdGVariazioniComponent {
	private class CtrlPianoEco {
		public CtrlPianoEco(ProgettoBulk progetto, Timestamp dtScadenza, boolean cashFund) {
			super();
			this.progetto = progetto;
			this.dtScadenza = dtScadenza;
			this.cashFund = cashFund;
		}
		private ProgettoBulk progetto;
		private Timestamp dtScadenza;
		private boolean cashFund;
		private BigDecimal impPositivi = BigDecimal.ZERO;
		private BigDecimal impNegativi = BigDecimal.ZERO;
		private BigDecimal impPositiviNaturaReimpiego = BigDecimal.ZERO;
		private BigDecimal impNegativiNaturaReimpiego = BigDecimal.ZERO;
		private BigDecimal impPositiviVoceSpeciale = BigDecimal.ZERO;
		private BigDecimal impNegativiVoceSpeciale = BigDecimal.ZERO;
		public ProgettoBulk getProgetto() {
			return progetto;
		}
		public void setProgetto(ProgettoBulk progetto) {
			this.progetto = progetto;
		}
		public Timestamp getDtScadenza() {
			return dtScadenza;
		}
		public void setDtScadenza(Timestamp dtScadenza) {
			this.dtScadenza = dtScadenza;
		}
		public boolean isCashFund() {
			return cashFund;
		}
		public void setCashFund(boolean cashFund) {
			this.cashFund = cashFund;
		}
		public BigDecimal getImpPositivi() {
			return impPositivi;
		}
		public void setImpPositivi(BigDecimal impPositivi) {
			this.impPositivi = impPositivi;
		}
		public BigDecimal getImpNegativi() {
			return impNegativi;
		}
		public void setImpNegativi(BigDecimal impNegativi) {
			this.impNegativi = impNegativi;
		}
		public BigDecimal getImpPositiviNaturaReimpiego() {
			return impPositiviNaturaReimpiego;
		}
		public void setImpPositiviNaturaReimpiego(BigDecimal impPositiviNaturaReimpiego) {
			this.impPositiviNaturaReimpiego = impPositiviNaturaReimpiego;
		}
		public BigDecimal getImpNegativiNaturaReimpiego() {
			return impNegativiNaturaReimpiego;
		}
		public void setImpNegativiNaturaReimpiego(BigDecimal impNegativiNaturaReimpiego) {
			this.impNegativiNaturaReimpiego = impNegativiNaturaReimpiego;
		}
		public BigDecimal getImpPositiviVoceSpeciale() {
			return impPositiviVoceSpeciale;
		}
		public void setImpPositiviVoceSpeciale(BigDecimal impPositiviVoceSpeciale) {
			this.impPositiviVoceSpeciale = impPositiviVoceSpeciale;
		}
		public BigDecimal getImpNegativiVoceSpeciale() {
			return impNegativiVoceSpeciale;
		}
		public void setImpNegativiVoceSpeciale(BigDecimal impNegativiVoceSpeciale) {
			this.impNegativiVoceSpeciale = impNegativiVoceSpeciale;
		}
	}
	
	private static final java.math.BigDecimal ZERO = new java.math.BigDecimal(0);

	public  CRUDPdgVariazioneGestionaleComponent()
	{
		/*Default constructor*/
	}

	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		if (((Pdg_variazioneBulk)bulk).getDs_delibera()==null)
			((Pdg_variazioneBulk)bulk).setDs_delibera(".");
		return super.creaConBulk(userContext, bulk);
	}
	public Pdg_variazioneBulk salvaDefinitivo(UserContext userContext, Pdg_variazioneBulk pdg) throws ComponentException{
		pdg.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
		pdg.setDt_chiusura(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		pdg.setToBeUpdated();
		inizializzaSommeCdR(userContext, pdg);
		if (pdg.getAssociazioneCDR().isEmpty()) 
			throw new ApplicationException("Associare almeno un Centro di Responsabilità alla Variazione.");
		//	P.R.: Controllo proveniente da ModificaConBulk e CreaConBulk
		//	Deciso con Angelini di spostare i controlli in fase di salvataggio definitivo
		validaDettagliEntrataSpesa(userContext, pdg);
		/*
		 * Confermo l'operazione
		 * E' importante salvare in questo momento in controllo di disponibilità avviene tramite
		 * procedura Pl-Sql che deve già trovare sul DB la variazione con stato Definitivo altrimenti non la 
		 * considera ai fini del controllo  
		 */ 
		pdg = (Pdg_variazioneBulk)super.modificaConBulk(userContext, pdg);
			
		try{
			for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){			
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();
				Ass_pdg_variazione_cdrHome ass_pdgHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);
	
				if (ass_pdgHome.findDettagliSpesaVariazioneGestionale(ass_pdg).isEmpty()) { 
					if (ass_pdgHome.findDettagliEntrataVariazioneGestionale(ass_pdg).isEmpty()) 
						throw new ApplicationException("Associare almeno un dettaglio di variazione al Centro di Responsabilità " + ass_pdg.getCd_centro_responsabilita());
				}
	
				if (ass_pdg.getEntrata_diff().compareTo(Utility.ZERO) != 0)
					throw new ApplicationException("La Differenza di entrata ("+new it.cnr.contab.util.EuroFormat().format(ass_pdg.getEntrata_diff())+")"+
												   "\n" + "per il Cdr "+ ass_pdg.getCd_centro_responsabilita()+ " è diversa da zero. ");
				if (ass_pdg.getSpesa_diff().compareTo(Utility.ZERO) != 0)
					throw new ApplicationException("La Differenza di spesa ("+new it.cnr.contab.util.EuroFormat().format(ass_pdg.getSpesa_diff())+")"+
												   "\n" + "per il Cdr "+ ass_pdg.getCd_centro_responsabilita()+ " è diversa da zero. ");
			}

			if (!pdg.isStorno() && !pdg.getTipo_variazione().isMovimentoSuFondi())
				controllaQuadraturaImportiAree(userContext, pdg);
			
			controllaPdgPianoEconomico(userContext, pdg);
			aggiornaLimiteSpesa(userContext, pdg);
			/*
			 * Verifico che l'assestato di tutte le combinazioni scelte sia positivo in modo da avvertire
			 * l'utente del problema di approvazione che avrebbe  
			 */
			checkDispAssestatoCdrGAEVoce(userContext, pdg, "onSalvaDefinitivoDispAssestatoCdrGAEVoceFailed");
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}	
		return pdg;
	}
	

private void aggiornaLimiteSpesa(UserContext userContext,Pdg_variazioneBulk pdg) throws ComponentException {

	try {
		LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
			"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
			+ "CNRCTB053.aggiornaLimiteSpesaVar(?,?,?,?)}",false,this.getClass());
		cs.setObject( 1, pdg.getEsercizio() );
		cs.setObject( 2, pdg.getPg_variazione_pdg() );
		cs.setObject( 3,"C"); //competenza 
		cs.setObject( 4, userContext.getUser());
		try {
			lockBulk(userContext,pdg);
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(pdg,e);
		} finally {
			cs.close();
		}	
	} catch (java.sql.SQLException e) {
		// Gestisce eccezioni SQL specifiche (errori di lock,...)
		throw handleSQLException(e);
	}
}

	protected Voce_f_saldi_cdr_lineaBulk trovaSaldo(UserContext userContext, Pdg_variazione_riga_gestBulk pdg_det,Voce_fBulk voce) throws PersistencyException, ComponentException{
		Voce_f_saldi_cdr_lineaBulk saldo = (Voce_f_saldi_cdr_lineaBulk)getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class).findByPrimaryKey(
		   new Voce_f_saldi_cdr_lineaBulk(pdg_det.getEsercizio(),
										  pdg_det.getEsercizio(),
										  pdg_det.getCd_cdr_assegnatario(),
										  pdg_det.getCd_linea_attivita(),
										  pdg_det.getTi_appartenenza(),
										  pdg_det.getTi_gestione(),
										  voce.getCd_voce()));
		return saldo;									  	
	}
	
	public V_assestatoBulk trovaAssestato(UserContext userContext, Pdg_variazione_riga_gestBulk pdg_det) throws ComponentException{
		try {
			V_assestatoBulk assestato = (V_assestatoBulk)getHome(userContext, V_assestatoBulk.class).findByPrimaryKey(
				   new V_assestatoBulk(pdg_det.getEsercizio(),
									   pdg_det.getEsercizio(),
									   pdg_det.getCd_cdr_assegnatario(),
									   pdg_det.getCd_linea_attivita(),
									   pdg_det.getTi_appartenenza(),
									   pdg_det.getTi_gestione(),
									   pdg_det.getCd_elemento_voce()));
			return assestato;									  	
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (ComponentException e) {
			throw new ComponentException(e);
		}
	}

	private void aggiornaSaldiCdrLinea(UserContext userContext, Pdg_variazione_riga_gestBulk varRiga, Boolean sottraiImportoDaVariazioneEsistente) throws ComponentException{
		try {
			BigDecimal impDaAggiornare = Utility.ZERO;
			Voce_f_saldi_cdr_lineaBulk saldo = new Voce_f_saldi_cdr_lineaBulk(varRiga.getEsercizio(), 
			                                                                  varRiga.getEsercizio(), 
																			  varRiga.getCd_cdr_assegnatario(), 
																			  varRiga.getCd_linea_attivita(), 
																			  varRiga.getElemento_voce().getTi_appartenenza(), 
																			  varRiga.getElemento_voce().getTi_gestione(), 
																			  getVoce_FdaEV(userContext, 
																					        varRiga.getEsercizio(), 
																					        varRiga.getElemento_voce().getTi_appartenenza(),
																						    varRiga.getElemento_voce().getTi_gestione(), 
																					        varRiga.getCd_elemento_voce(),
																					        varRiga.getCd_cdr_assegnatario(), 
																							varRiga.getCd_linea_attivita()));
			Voce_f_saldi_cdr_lineaBulk saldi = (Voce_f_saldi_cdr_lineaBulk) getHome(userContext, Voce_f_saldi_cdr_lineaBulk.class).findByPrimaryKey(saldo);
			if (saldi == null){
				saldo.setToBeCreated();
				saldo.inizializzaSommeAZero();
				saldo.setCd_elemento_voce(varRiga.getCd_elemento_voce());
				saldi = (Voce_f_saldi_cdr_lineaBulk)((CRUDComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("JADAEJB_CRUDComponentSession",it.cnr.jada.ejb.CRUDComponentSession.class)).creaConBulk(userContext,saldo);
			}
			
			Voce_f_saldi_cdr_lineaBulk saldoNew = Utility.createSaldoComponentSession().aggiornaVariazioneStanziamento(userContext, 
																				 varRiga.getCd_cdr_assegnatario(), 
																				 varRiga.getCd_linea_attivita(), 
																				 saldo.getVoce(), 
																				 varRiga.getEsercizio(), 
																				 Voce_f_saldi_cdr_lineaBulk.TIPO_COMPETENZA, 
																				 varRiga.getIm_variazione(),
																				 sottraiImportoDaVariazioneEsistente);
			if (saldo.getTi_gestione().equalsIgnoreCase(Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_SPESA)||
				varRiga.getIm_variazione().compareTo(Utility.ZERO)==-1){
				String err = Utility.createSaldoComponentSession().getMessaggioSfondamentoDisponibilita(userContext, 
																									    saldoNew);
				if (!(err==null ||err.equals("")))
					throw new ApplicationException(err);
		        }
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
	}

	public void aggiornaSaldiCdrLinea(UserContext userContext, Pdg_variazione_riga_gestBulk varRiga) throws ComponentException{
		aggiornaSaldiCdrLinea(userContext, varRiga, false);
	}

	public void allineaSaldiVariazioneApprovata(UserContext userContext, Ass_pdg_variazione_cdrBulk ass) throws ComponentException {
		boolean primoGiro = true;
		Configurazione_cnrBulk config;
		try {
			config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
		} catch (ComponentException e1) {
			throw handleException(e1);
		} catch (RemoteException e1) {
			throw handleException(e1);
		} catch (EJBException e1) {
			throw handleException(e1);
		}
		for (java.util.Iterator i =  ass.getRigheVariazioneSpeGest().iterator();i.hasNext();) {
			Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk)i.next();
			try {
				aggiornaSaldiCdrLinea(userContext,varRiga);
				if (primoGiro){
					Pdg_variazione_riga_gestBulk rigaCloned = new Pdg_variazione_riga_gestBulk();
					rigaCloned.setElemento_voce(varRiga.getElemento_voce());
					rigaCloned.setPdg_variazione(varRiga.getPdg_variazione());
					rigaCloned.setCdr_assegnatario(varRiga.getCdr_assegnatario());
					rigaCloned.setCd_linea_attivita(config.getVal01());
					rigaCloned.setIm_spese_gest_accentrata_est(ass.getIm_spesa().multiply(new BigDecimal(-1)));
					aggiornaSaldiCdrLinea(userContext,rigaCloned, true);
					primoGiro = false;
				}
			} catch (ComponentException e) {
				throw handleException(e);
			}
		}
	}

	/**
	 * Crea la ComponentSession da usare per effettuare le operazioni di lettura della Configurazione CNR
	 *
	 * @return Configurazione_cnrComponentSession l'istanza di <code>Configurazione_cnrComponentSession</code> che serve per leggere i parametri di configurazione del CNR
	 */
	private Configurazione_cnrComponentSession createConfigurazioneCnrComponentSession() throws ComponentException 
	{
		try
		{
			return (Configurazione_cnrComponentSession)EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}

	public Pdg_variazioneBulk approva(UserContext userContext, Pdg_variazioneBulk varPdg) throws ComponentException{
		try {
			//Verifichiamo che il piano economico sia quadrato 
			Utility.createSaldoComponentSession().checkDispPianoEconomicoProgetto(userContext, varPdg);

			varPdg.setStato(Pdg_variazioneBulk.STATO_APPROVATA);
			varPdg.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
			varPdg.setToBeUpdated();
			varPdg = (Pdg_variazioneBulk)super.modificaConBulk(userContext, varPdg);
	
			gestioneVistoDipartimenti(userContext, varPdg);
			
			Pdg_variazioneHome testataHome = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);

			ribaltaCostiPdGArea(userContext,varPdg);

			/*
			 * Ricarico il BULK con i dati presenti sul DB che, nel frattempo, potrebbero essere
			 * aumentati a causa di procedure DB lanciate da comandi precedenti (es. ribaltaCostiPdGArea)
			 */
			varPdg = (Pdg_variazioneBulk)testataHome.findByPrimaryKey(varPdg);
			varPdg.setAssociazioneCDR(new it.cnr.jada.bulk.BulkList(testataHome.findAssociazioneCDR(varPdg)));
	
			/*
			 * Spedisco i messaggi di avvertimento a tutti i CDR interessati alla Variazione 
			 */
			UtenteHome utenteHome = (UtenteHome)getHome(userContext,UtenteBulk.class);
			if (!varPdg.isVariazioneInternaIstituto()){
				for (java.util.Iterator j=varPdg.getAssociazioneCDR().iterator();j.hasNext();){			
					Ass_pdg_variazione_cdrBulk ass_var = (Ass_pdg_variazione_cdrBulk)j.next();		
					for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(ass_var.getCd_centro_responsabilita()).iterator();i.hasNext();){
						UtenteBulk utente = (UtenteBulk)i.next();
						MessaggioBulk messaggio = generaMessaggio(userContext,utente,varPdg,Pdg_variazioneBulk.STATO_APPROVATA);
						super.creaConBulk(userContext, messaggio);
					}
				}
			}

			for (Iterator righe = testataHome.findDettagliVariazioneGestionale(varPdg).iterator();righe.hasNext();){
				Pdg_variazione_riga_gestBulk varRiga = (Pdg_variazione_riga_gestBulk)righe.next();
				if (!varRiga.isDettaglioScaricato())
					aggiornaSaldiCdrLinea(userContext,varRiga);
			}

			generaVariazioneBilancio(userContext, varPdg);
			if (!varPdg.isVariazioneInternaIstituto()){
				String soggetto = "E' stata approvata la Variazione al Pdg n° "+varPdg.getPg_variazione_pdg();
				generaEMAIL(userContext, varPdg,soggetto,soggetto +" del "+varPdg.getEsercizio()+"<BR>",null, "APP");			    	
			}						
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		} catch (EJBException e) {
			throw new ComponentException(e);
		}
		return varPdg;
	}

	public Pdg_variazioneBulk respingi(UserContext userContext, Pdg_variazioneBulk pdg) throws ComponentException{
		if (pdg.getCd_causale_respinta()==null)
			throw new it.cnr.jada.comp.ApplicationException("Indicare la causale della Mancata Approvazione.");
		
		pdg.setStato(Pdg_variazioneBulk.STATO_RESPINTA);
		pdg.setDt_approvazione(DateUtils.dataContabile(EJBCommonServices.getServerDate(), CNRUserContext.getEsercizio(userContext)));
		pdg.setToBeUpdated();	
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);

		try {
			MessaggioHome messHome = (MessaggioHome)getHome(userContext,MessaggioBulk.class);
			UtenteHome utenteHome = (UtenteHome)getHome(userContext,UtenteBulk.class);
			for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){			
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();		
				for (java.util.Iterator i= utenteHome.findUtenteByCDRIncludeFirstLevel(ass_pdg.getCd_centro_responsabilita()).iterator();i.hasNext();){
					UtenteBulk utente = (UtenteBulk)i.next();
					MessaggioBulk messaggio = generaMessaggio(userContext,utente,pdg,Pdg_variazioneBulk.STATO_RESPINTA);
					super.creaConBulk(userContext, messaggio);
				}
			}				
		} catch (IntrospectionException e) {
			throw new ComponentException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		super.modificaConBulk(userContext, pdg);
		aggiornaLimiteSpesa(userContext, pdg);
		return pdg;
	}		

	/**
	  * Viene richiesta l'eliminazione dell'oggetto selezionato
	  *
	  * Pre-post-conditions:
	  *
	  * @param	userContext	lo UserContext che ha generato la richiesta
	  * @param	bulk l'OggettoBulk da eliminare
	  * @return	void
	  *
	**/
	public void eliminaConBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException{
		try {		
			String stato_prec=null;
			Pdg_variazioneBulk var = (Pdg_variazioneBulk) bulk;
			if (var.getStato().compareTo(Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA)==0)
				stato_prec=Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA;
			
			if (bulk instanceof ICancellatoLogicamente){
					((ICancellatoLogicamente)bulk).cancellaLogicamente();
					updateBulk(userContext, bulk);
				if(stato_prec!=null)		
					aggiornaLimiteSpesa(userContext, var);
			}else{
				super.eliminaConBulk(userContext, bulk);				
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}


	/**
	  * Inizializza con valori di Default i campi delle tabelle coinvolte eventualmente
	  * non valorizzati dall'utente 
	  *
	  * Pre-post-conditions:
	  *
	  * @param	bulk l'OggettoBulk da eliminare
	  * @return	void
	  *
	**/
	protected void inizializzaValoriDefaultCampi(OggettoBulk oggettobulk)	{
		Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)oggettobulk;
		for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){			
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();
			if (ass_pdg.getIm_entrata()==null)
			  ass_pdg.setIm_entrata(Utility.ZERO);
			if (ass_pdg.getIm_spesa()==null)
			  ass_pdg.setIm_spesa(Utility.ZERO);
		}			
	}

	protected void inizializzaSommeDiSpesa(UserContext userContext, Pdg_variazioneBulk pdg) throws ComponentException, IntrospectionException, PersistencyException{
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);
		for (java.util.Iterator spesa = testataHome.findDettagliSpesaVariazioneGestionale(pdg).iterator();spesa.hasNext();){
			Pdg_variazione_riga_gestBulk spesa_det = (Pdg_variazione_riga_gestBulk)spesa.next();			
			if (!spesa_det.isDettaglioScaricato()) {
				pdg.setSomma_spesa_var_piu(pdg.getSomma_spesa_var_piu().add(
					(sommaVariazioniPos(spesa_det.getIm_spese_gest_accentrata_int())).add(
					(sommaVariazioniPos(spesa_det.getIm_spese_gest_accentrata_est())).add(
					(sommaVariazioniPos(spesa_det.getIm_spese_gest_decentrata_int())).add(
					(sommaVariazioniPos(spesa_det.getIm_spese_gest_decentrata_est())))))
				));
				pdg.setSomma_spesa_var_meno(pdg.getSomma_spesa_var_meno().add(
					(sommaVariazioniNeg(spesa_det.getIm_spese_gest_accentrata_int())).add(
					(sommaVariazioniNeg(spesa_det.getIm_spese_gest_accentrata_est())).add(
					(sommaVariazioniNeg(spesa_det.getIm_spese_gest_decentrata_int())).add(
					(sommaVariazioniNeg(spesa_det.getIm_spese_gest_decentrata_est())))))
				));
			}
		}
		pdg.setSomma_spesa_diff(pdg.getSomma_spesa_diff().add(
		  pdg.getSomma_spesa_var_piu().subtract(pdg.getSomma_spesa_var_meno()).abs()
		));
		pdg.setSomma_costi_diff(pdg.getSomma_costi_diff().add(
		  pdg.getSomma_costi_var_piu().subtract(pdg.getSomma_costi_var_meno()).abs()
		));		
	}

	protected void inizializzaSommeDiEntrata(UserContext userContext, Pdg_variazioneBulk pdg) throws ComponentException, IntrospectionException, PersistencyException{
		Pdg_variazioneHome testataHome = (Pdg_variazioneHome)getHome(userContext, Pdg_variazioneBulk.class);
		for (java.util.Iterator entrate = testataHome.findDettagliEntrataVariazioneGestionale(pdg).iterator();entrate.hasNext();){
			Pdg_variazione_riga_gestBulk etr_det = (Pdg_variazione_riga_gestBulk)entrate.next();			
			if (!etr_det.isDettaglioScaricato()) {
				pdg.setSomma_entrata_var_piu(pdg.getSomma_entrata_var_piu().add(
					(sommaVariazioniPos(etr_det.getIm_entrata()))
				));
				pdg.setSomma_entrata_var_meno(pdg.getSomma_entrata_var_meno().add(
					(sommaVariazioniNeg(etr_det.getIm_entrata()))
				));
			}
		}
		pdg.setSomma_entrata_diff(pdg.getSomma_entrata_diff().add(
		  pdg.getSomma_entrata_var_piu().subtract(pdg.getSomma_entrata_var_meno()).abs()
		));
		pdg.setSomma_ricavi_diff(pdg.getSomma_ricavi_diff().add(
		  pdg.getSomma_ricavi_var_piu().subtract(pdg.getSomma_ricavi_var_meno()).abs()
		));		
	}
	/*
	 * Controlli da effettuare per tipo di variazione gestionale:
	 *
	 * STORNO_SPESA_STESSO_ISTITUTO: 
	 * 		le righe di variazione possono essere solo per la spesa ed il saldo algebrico pari a zero.
	 * 		I CdR chiamati a partecipare possono essere solo quelli appartenenti al CDS che ha aperto 
	 * 		la variazione.
	 * STO_E_CDS: le righe di variazione possono essere solo per l'entrata ed il saldo algebrico pari a zero.
	 * 			  I CdR chiamati a partecipare possono essere solo quelli appartenenti al CDS che ha aperto 
	 * 			  la variazione.
	 * STO_S_TOT: le righe di variazione possono essere solo per la spesa ed il saldo algebrico pari a zero.
	 * 			  I CdR chiamati a partecipare possono anche appartenere a CDS diversi da quello che ha aperto 
	 * 			  la variazione.
	 * STO_E_TOT: le righe di variazione possono essere solo per l'entrata ed il saldo algebrico pari a zero.
	 * 			  I CdR chiamati a partecipare possono anche appartenere a CDS diversi da quello che ha aperto
	 * 			  la variazione.
	 * VAR_PIU_CDS: le righe di variazione devono essere sia di entrata che di spesa (obbligatoriamente), 
	 * 			    gli importi solo positivi e uguali tra entrata e spesa.
	 * 				Inoltre i CdR chiamati a partecipare devono essere solo quelli appartenenti al CDS che ha 
	 * 				aperto la variazione.
	 * VAR_MENO_CDS: le righe di variazione devono essere sia di entrata che di spesa (obbligatoriamente), 
	 * 				 gli importi solo negativi e uguali tra entrata e spesa.
	 *     			 Inoltre i CdR chiamati a partecipare devono essere solo quelli appartenenti al CDS che ha 
	 *     			 aperto la variazione.
	 * VAR_PIU_TOT: le righe di variazione devono essere sia di entrata che di spesa (obbligatoriamente), 
	 * 				gli importi solo positivi e uguali tra entrata e spesa.
	 * 				I CdR chiamati a partecipare possono anche appartenere a CDS diversi da quello che ha 
	 * 				aperto la variazione.
	 * VAR_MENO_TOT: le righe di variazione devono essere sia di entrata che di spesa (obbligatoriamente), 
	 * 				 gli importi solo negativi e uguali tra entrata e spesa.
	 * 				 I CdR chiamati a partecipare possono anche appartenere a CDS diversi da quello che ha aperto 
	 * 				 la variazione.
	 */
	protected void validaDettagliEntrataSpesa(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {		
		try {		
			Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)oggettobulk;
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome)getHome(usercontext, Ass_pdg_variazione_cdrBulk.class);
			BigDecimal totImportoEntrataPositivo = Utility.ZERO;
			BigDecimal totImportoEntrataNegativo = Utility.ZERO;
			BigDecimal totImportoSpesaPositivo = Utility.ZERO;
			BigDecimal totImportoSpesaNegativo = Utility.ZERO;
			BigDecimal totSommaEntrata = Utility.ZERO;
			BigDecimal totSommaSpesa = Utility.ZERO;
			BigDecimal impTotaleEntrateDaPrel = Utility.ZERO;
			BigDecimal impTotaleSpesePrel = Utility.ZERO;
			int contaRigheEntrata = Utility.ZERO.intValue();
			int contaRigheSpesa = Utility.ZERO.intValue();

			boolean existDettPersonale = true;
			String cdrPersonale = null;
			if (Optional.ofNullable(pdg.getTiMotivazioneVariazione()).isPresent()) { 
				cdrPersonale = Optional.ofNullable(((ObbligazioneHome)getHome(usercontext, ObbligazioneBulk.class)).recupero_cdr_speciale_stipendi())
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
				existDettPersonale = false;
			} 
				
			for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();

				existDettPersonale = existDettPersonale||cdrPersonale.equals(ass_pdg.getCd_centro_responsabilita());

				if (pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_SPESA_STESSO_ISTITUTO) ||
					pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_ENTRATA_STESSO_ISTITUTO) ||
					pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_STESSO_ISTITUTO) ||
					pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_STESSO_ISTITUTO))
					if (!pdg.getCentro_responsabilita().getCd_cds().equals(ass_pdg.getCentro_responsabilita().getCd_cds()))
						throw new ApplicationException("In un variazione di tipo 'Storno\\Variazione Stesso Istituto' i CDR " +
								"partecipanti devono appartenere allo stesso istituto del CDR proponente (" + 
								pdg.getCentro_responsabilita().getCd_cds() + ")");
						
				//Calcolo il totale delle entrate per il CDR selezionato e lo confronto con il totale
				//assegnato
				BigDecimal sommaEntrata = Utility.ZERO;
				for (java.util.Iterator entrate = testataHome.findDettagliEntrataVariazioneGestionale(ass_pdg).iterator();entrate.hasNext();){
					Pdg_variazione_riga_gestBulk etr_det = (Pdg_variazione_riga_gestBulk)entrate.next();
					sommaEntrata = sommaEntrata.add(etr_det.getIm_variazione());

					contaRigheEntrata = ++contaRigheEntrata;
					if (etr_det.getIm_variazione().compareTo(Utility.ZERO)>0)
						totImportoEntrataPositivo = totImportoEntrataPositivo.add(etr_det.getIm_variazione());
					else
						totImportoEntrataNegativo = totImportoEntrataNegativo.add(etr_det.getIm_variazione());
				}
				//Aggiorno il totalizzatore complessivo
				totSommaEntrata = totSommaEntrata.add(sommaEntrata);

				if (ass_pdg.getIm_entrata() != null){
					if(ass_pdg.getIm_entrata().compareTo(sommaEntrata) < 0)
					  throw new ApplicationException("La Somma dei dettagli di entrata ("+new it.cnr.contab.util.EuroFormat().format(sommaEntrata)+")"+
													 "\n" + "per il Cdr "+ ass_pdg.getCd_centro_responsabilita()+ " supera la quota di entrata assegnata di "+
													 new it.cnr.contab.util.EuroFormat().format(sommaEntrata.subtract(ass_pdg.getIm_entrata())));
				}

				//Calcolo il totale delle spese per il CDR selezionato e lo confronto con il totale
				//assegnato
				BigDecimal sommaSpesa = Utility.ZERO;
				for (java.util.Iterator spese = testataHome.findDettagliSpesaVariazioneGestionale(ass_pdg).iterator();spese.hasNext();){
					Pdg_variazione_riga_gestBulk spesa_det = (Pdg_variazione_riga_gestBulk)spese.next();
					sommaSpesa = sommaSpesa.add(spesa_det.getIm_variazione());

					contaRigheSpesa = ++contaRigheSpesa;
					if (spesa_det.getIm_variazione().compareTo(Utility.ZERO)>0)
						totImportoSpesaPositivo = totImportoSpesaPositivo.add(spesa_det.getIm_variazione());
					else
						totImportoSpesaNegativo = totImportoSpesaNegativo.add(spesa_det.getIm_variazione());
					
					if (!existDettPersonale) {
						Elemento_voceBulk voce = (Elemento_voceBulk)getHome(usercontext, Elemento_voceBulk.class).findByPrimaryKey(spesa_det.getElemento_voce());
						Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(usercontext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
						existDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());
					}
				}
				//Aggiorno il totalizzatore complessivo
				totSommaSpesa = totSommaSpesa.add(sommaSpesa);

				if (ass_pdg.getIm_spesa() != null){
					if(ass_pdg.getIm_spesa().compareTo(sommaSpesa) < 0)
						throw new ApplicationException("La Somma dei dettagli di spesa ("+new it.cnr.contab.util.EuroFormat().format(sommaSpesa)+")"+
												   "\n" + "per il Cdr "+ ass_pdg.getCd_centro_responsabilita()+ " supera la quota di spesa assegnata di "+
												   new it.cnr.contab.util.EuroFormat().format(sommaSpesa.subtract(ass_pdg.getIm_spesa())));
				}
			}
			if (totSommaEntrata.compareTo(totSommaSpesa)!=0 && !isUoPdgUoEnte(usercontext, pdg))
				throw new ApplicationException("Il totale delle variazioni di spesa ("+new it.cnr.contab.util.EuroFormat().format(totSommaSpesa)+")"+
										   "\n" + "non è uguale al totale delle variazioni di entrata ("+
										   new it.cnr.contab.util.EuroFormat().format(totSommaEntrata)+")");
			if (!existDettPersonale)
				throw new ApplicationException("In un variazione di tipo 'Personale' occorre selezionare almeno una voce accentrata "
						+ "verso il CDR del personale o scegliere tra i CDR partecipanti anche quello del personale ("+cdrPersonale+").");
			if (pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_SPESA_STESSO_ISTITUTO) ||
				pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_SPESA_ISTITUTI_DIVERSI)) {
				if (contaRigheEntrata>Utility.ZERO.intValue())
					throw new ApplicationException("Non è possibile inserire dettagli di entrata in un variazione di tipo 'Storno Spesa'");
				if (totSommaSpesa.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Storno Spesa' il saldo algebrico deve essere nullo");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_ENTRATA_STESSO_ISTITUTO) ||
					   pdg.getTipologia().equals(Tipo_variazioneBulk.STORNO_ENTRATA_ISTITUTI_DIVERSI)) {
				if (contaRigheSpesa>Utility.ZERO.intValue())
					throw new ApplicationException("Non è possibile inserire dettagli di spesa in un variazione di tipo 'Storno Entrata'");
				if (totSommaEntrata.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Storno Entrata' il saldo algebrico deve essere nullo");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.PRELIEVO_FONDI)) {
				if (contaRigheEntrata>Utility.ZERO.intValue())
					throw new ApplicationException("Non è possibile inserire dettagli di entrata in un variazione di tipo 'Prelievo Fondi'");
				if (totSommaSpesa.compareTo(Utility.ZERO)!=1)
					throw new ApplicationException("In un variazione di tipo 'Prelievo da Fondi' il saldo algebrico deve essere positivo");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_FONDI)) {
				if (contaRigheSpesa>Utility.ZERO.intValue())
					throw new ApplicationException("Non è possibile inserire dettagli di spesa in un variazione di tipo 'Decremento Fondi'");
				if (totImportoEntrataPositivo.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Decremento Fondi' non è possibile inserire dettagli di entrata con importi positivi.");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_FONDI)) {
				if (contaRigheSpesa>Utility.ZERO.intValue())
					throw new ApplicationException("Non è possibile inserire dettagli di spesa in un variazione di tipo 'Incremento\\Decremento Fondi'");
				if (totImportoEntrataNegativo.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Incremento Fondi' non è possibile inserire dettagli di entrata con importi negativi.");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_STESSO_ISTITUTO) ||
					   pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_POSITIVA_ISTITUTI_DIVERSI)) {
				if (contaRigheSpesa==Utility.ZERO.intValue()|| contaRigheEntrata==Utility.ZERO.intValue())
					throw new ApplicationException("E' necessario inserire sia dettagli di spesa che di entrata in un variazione di tipo 'Variazione Positiva'");
				if (totImportoSpesaNegativo.compareTo(Utility.ZERO)!=0 || totImportoEntrataNegativo.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Variazione Positiva' non è possibile inserire dettagli di entrata/spesa con importi negativi.");			
								CdrBulk cdr_prel=null;
								Pdg_variazioneHome pdgHome = (Pdg_variazioneHome)getHome(usercontext, Pdg_variazioneBulk.class);
								//Calcolo il totale delle entrate 
								for (java.util.Iterator entrate = pdgHome.findDettagliEntrateVariazioneGestionaleSoggettePrelievo(pdg).iterator();entrate.hasNext();){
									Pdg_variazione_riga_gestBulk etr_det = (Pdg_variazione_riga_gestBulk)entrate.next();
									Elemento_voceBulk ev = (Elemento_voceBulk)getHome(usercontext, Elemento_voceBulk.class).findByPrimaryKey(etr_det.getElemento_voce());
									if(etr_det.getElemento_voce()!=null &&etr_det.getElemento_voce().getPerc_prelievo_pdgp_entrate().compareTo(ZERO)!=0){
										CdrBulk cdr = (CdrBulk)getHome(usercontext, CdrBulk.class).findByPrimaryKey(etr_det.getCdr_assegnatario());
										cdr.setUnita_padre((Unita_organizzativaBulk)getHome(usercontext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));
										if(!etr_det.getCdr_assegnatario().isCdrSAC()){
											impTotaleEntrateDaPrel = impTotaleEntrateDaPrel.add(etr_det.getIm_entrata().multiply(ev.getPerc_prelievo_pdgp_entrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_DOWN));
										    cdr_prel=etr_det.getCdr_assegnatario();
										}
									}
								}
								//Calcolo il totale delle spese 
								for (java.util.Iterator spese = pdgHome.findDettagliSpesaVariazioneGestionalePrelievo(pdg).iterator();spese.hasNext();){
									Pdg_variazione_riga_gestBulk spesa_det = (Pdg_variazione_riga_gestBulk)spese.next();
									if(cdr_prel!= null && cdr_prel.getCd_centro_responsabilita()!=null && spesa_det.getCdr_assegnatario().getCd_centro_responsabilita().compareTo(cdr_prel.getCd_centro_responsabilita())==0)
										impTotaleSpesePrel = impTotaleSpesePrel.add(spesa_det.getIm_spese_gest_accentrata_est()).add(spesa_det.getIm_spese_gest_decentrata_est());
								}
							if (impTotaleEntrateDaPrel.compareTo(ZERO)!=0){
								//if(impTotaleSpesePrel.compareTo(ZERO)!=0){
									if(impTotaleEntrateDaPrel.compareTo(impTotaleSpesePrel)!=0)
										throw new ApplicationException("Il contributo per l'attività ordinaria per il cdr "+cdr_prel.getCd_centro_responsabilita()+" è pari a "+ new it.cnr.contab.util.EuroFormat().format(impTotaleEntrateDaPrel)+
											". Impossibile salvare, poichè è stato imputato sulla voce dedicata l'importo di "+new it.cnr.contab.util.EuroFormat().format(impTotaleSpesePrel)+".");
								//}		
							}
						if (totSommaEntrata.compareTo(totSommaSpesa)!=0)
								throw new ApplicationException("In un variazione di tipo 'Variazione Positiva' il totale delle variazioni di spesa ("+
											   new it.cnr.contab.util.EuroFormat().format(totSommaSpesa)+")"+
											   "\n" + "deve essere uguale al totale delle variazioni di entrata ("+
											   new it.cnr.contab.util.EuroFormat().format(totSommaEntrata)+")");
			} else if (pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_STESSO_ISTITUTO)||
					   pdg.getTipologia().equals(Tipo_variazioneBulk.VARIAZIONE_NEGATIVA_ISTITUTI_DIVERSI)) {
				if (contaRigheSpesa==Utility.ZERO.intValue() || contaRigheEntrata==Utility.ZERO.intValue())
					throw new ApplicationException("E' necessario inserire sia dettagli di spesa che di entrata in un variazione di tipo 'Variazione Negativa'");
				if (totImportoSpesaPositivo.compareTo(Utility.ZERO)!=0 || totImportoEntrataPositivo.compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("In un variazione di tipo 'Variazione Negativa' non è possibile inserire dettagli di entrata/spesa con importi positivi.");
				if (totSommaEntrata.compareTo(totSommaSpesa)!=0) {
					throw new ApplicationException("In un variazione di tipo 'Variazione Negativa' il totale delle variazioni di spesa ("+
											   new it.cnr.contab.util.EuroFormat().format(totSommaSpesa)+")"+
											   "\n" + "deve essere uguale al totale delle variazioni di entrata ("+
											   new it.cnr.contab.util.EuroFormat().format(totSommaEntrata)+")");
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}					
	}

	public void inizializzaSommeCdR(UserContext userContext, Pdg_variazioneBulk pdg) throws ComponentException{
		try {		
			Ass_pdg_variazione_cdrHome testataHome = (Ass_pdg_variazione_cdrHome)getHome(userContext, Ass_pdg_variazione_cdrBulk.class);
			for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){			
				Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();
				BigDecimal sommaEntrata = Utility.ZERO;
				for (java.util.Iterator entrate = testataHome.findDettagliEntrataVariazioneGestionale(ass_pdg).iterator();entrate.hasNext();){
					Pdg_variazione_riga_gestBulk etr_det = (Pdg_variazione_riga_gestBulk)entrate.next();
					sommaEntrata = (sommaEntrata.add(
						(etr_det.getIm_entrata())
					));
				}
				ass_pdg.setEntrata_ripartita(sommaEntrata);
				ass_pdg.setEntrata_diff(nvl(ass_pdg.getIm_entrata()).subtract(sommaEntrata));
				BigDecimal sommaSpesa = Utility.ZERO;
				for (java.util.Iterator spese = testataHome.findDettagliSpesaVariazioneGestionale(ass_pdg).iterator();spese.hasNext();){
					Pdg_variazione_riga_gestBulk spesa_det = (Pdg_variazione_riga_gestBulk)spese.next();
					sommaSpesa = (sommaSpesa.add(
						(spesa_det.getIm_spese_gest_accentrata_int()).add(
						(spesa_det.getIm_spese_gest_accentrata_est()).add(
						(spesa_det.getIm_spese_gest_decentrata_int()).add(
						(spesa_det.getIm_spese_gest_decentrata_est()))))
					));                     
				}
				ass_pdg.setSpesa_ripartita(sommaSpesa);
				ass_pdg.setSpesa_diff(nvl(ass_pdg.getIm_spesa()).subtract(sommaSpesa));
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}							
	}

	/** 
	  *  Tutti controlli superati
	  *    PreCondition:
	  *      Viene richiesto il ribaltamento dei costi del Piano di Gestione del CdR specificato all'area di ricerca a cui afferisce. Il bilancio del CNR è già stato approvato
	  *    PostCondition:
	  *      La procedura Oracle CNRCTB053.ribaltaSuAreaPDG viene eseguita per l'anno di esercizio ed il CdR specificati.
	 */
	protected void ribaltaCostiPdGArea(UserContext userContext,Pdg_variazioneBulk pdg) throws it.cnr.jada.comp.ApplicationException, it.cnr.jada.comp.ComponentException {
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),
				"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+ "CNRCTB053.ribaltaSuAreaPDG_da_gest_var(?,?,?)}",false,this.getClass());
			cs.setObject( 1, pdg.getEsercizio() );
			cs.setObject( 2, pdg.getPg_variazione_pdg() );
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,pdg);
				cs.executeQuery();
			} catch (Throwable e) {
				throw handleException(pdg,e);
			} finally {
				cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

	public void controllaBilancioPreventivoCdsApprovato(UserContext userContext,CdrBulk cdr) throws ComponentException {
		try {
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(cdr.getUnita_padre());
			Pdg_esercizioBulk bilancio = (Pdg_esercizioBulk)getHome(userContext,Pdg_esercizioBulk.class).findByPrimaryKey(new Pdg_esercizioBulk(
				it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),
				cercaCdrPrimoLivello(userContext, cdr).getCd_centro_responsabilita()));
			if (bilancio == null)
				throw new it.cnr.jada.comp.ApplicationException("Piano di Gestione inesistente per il cds "+uo.getCd_unita_padre());
			if (!bilancio.STATO_CHIUSURA_GESTIONALE_CDR.equalsIgnoreCase(bilancio.getStato()))
				throw new it.cnr.jada.comp.ApplicationException("Il Piano di Gestione del cdr "+cdr.getCd_centro_responsabilita()+" deve essere approvato per registrare le variazioni");
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	
	public String getDesTipoVariazione(UserContext userContext, Pdg_variazioneBulk tipovar) throws ComponentException {
		try {
			if (tipovar.getTipo_variazione()!=null) {
				if (tipovar.getTipo_variazione().getDs_tipo_variazione()!=null)
					return tipovar.getTipo_variazione().getDs_tipo_variazione();
			}
			return null;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	public SQLBuilder selectCentro_responsabilitaByClause (UserContext userContext, Ass_pdg_variazione_cdrBulk ass_pdg, CdrBulk cdr, CompoundFindClause clause)	throws ComponentException, PersistencyException{
		SQLBuilder sql = getHome(userContext, CdrBulk.class,"V_CDR_VALIDO").createSQLBuilder();
		sql.addSQLClause("AND","V_CDR_VALIDO.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		if (ass_pdg.getPdg_variazione().isVariazioneInternaIstituto()) {
			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sql.addSQLJoin("V_CDR_VALIDO.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
			sql.addSQLJoin("V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");
			sql.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDS",sql.EQUALS,ass_pdg.getPdg_variazione().getCentro_responsabilita().getCd_cds());
		}
		else if (!ass_pdg.getPdg_variazione().getCentro_responsabilita().getUnita_padre().isUoArea()) {
			sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA");
			sql.addSQLJoin("V_CDR_VALIDO.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
			sql.addSQLJoin("V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA", "V_STRUTTURA_ORGANIZZATIVA.CD_ROOT");


			Parametri_cnrBulk parametriCnr = (Parametri_cnrBulk)getHome(userContext,Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)));
			if (parametriCnr==null || !parametriCnr.getFl_nuovo_pdg().booleanValue()){
				sql.addToHeader("V_STRUTTURA_ORGANIZZATIVA B");
				sql.addSQLJoin("B.ESERCIZIO", "V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
				sql.addSQLJoin("B.CD_ROOT", "V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
				sql.addSQLClause("AND","B.CD_TIPO_UNITA",sql.NOT_EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_AREA);
			}
		}

		if(clause != null)
		  sql.addClause(clause);
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return sql; 
	}	

	public SQLBuilder selectCentro_responsabilitaByClause (UserContext userContext, Pdg_variazioneBulk pdg, CdrBulk cdr, CompoundFindClause clause)	throws ComponentException, PersistencyException{
		SQLBuilder sql = getHome(userContext, CdrBulk.class,"V_CDR_VALIDO").createSQLBuilder();
		sql.addSQLClause("AND","V_CDR_VALIDO.ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		if(clause != null)
		  sql.addClause(clause);
		sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
		return sql; 
	}	

	public it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(UserContext userContext, it.cnr.jada.bulk.OggettoBulk oggettoBulk) throws ComponentException{
		Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)oggettoBulk;
		LoggableStatement cs = null;
		try	{
		  cs = new LoggableStatement(getConnection(userContext), 
			  "{call " +
			  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			  "CNRCTB053.genera_varente_da_Var_Pdg(?,?,?,?,?,?,?)}",false,this.getClass());
		  cs.setObject(1, pdgVar.getEsercizio() );
		  cs.setObject(2, pdgVar.getPg_variazione_pdg() );
		  cs.setObject(3, CNRUserContext.getUser(userContext) );
		  cs.registerOutParameter( 4, java.sql.Types.VARCHAR);
		  cs.registerOutParameter( 5, java.sql.Types.INTEGER);
		  cs.registerOutParameter( 6, java.sql.Types.CHAR);
		  cs.registerOutParameter( 7, java.sql.Types.INTEGER);		  
		  cs.executeQuery();
		  String cds_var_bil = cs.getString(4);
		  if (cds_var_bil != null ){
			  pdgVar.setCds_var_bil(cds_var_bil);
			  pdgVar.setEs_var_bil(new Integer(cs.getString(5)));
			  pdgVar.setTi_app_var_bil(new Character(cs.getString(6).charAt(0)));
			  pdgVar.setPg_var_bil(new Integer(cs.getString(7)));
		  }
		}catch (Throwable e) {
			throw handleException(e);
		} finally {
		  if (cs != null) 
			  try {
				  cs.close();
			  } catch (SQLException e1) {
				  throw handleException(e1);
			  }
		}
		return pdgVar;
	}

	public it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(UserContext userContext, it.cnr.jada.bulk.OggettoBulk oggettoBulk) throws ComponentException{
		Pdg_variazioneBulk pdgVar = (Pdg_variazioneBulk)oggettoBulk;
		if (pdgVar.getCds_var_bil() != null){
			LoggableStatement cs = null;		
			try	{
			  cs = new LoggableStatement(getConnection(userContext), 
				  "{call " +
				  it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				  "CNRCTB055.esitaVariazioneBilancio(?,?,?,?,?)}",false,this.getClass());
			  cs.setObject(1, pdgVar.getEs_var_bil() );
			  cs.setObject(2, pdgVar.getCds_var_bil());
			  cs.setObject(3, pdgVar.getTi_app_var_bil().toString() );
			  cs.setObject(4, pdgVar.getPg_var_bil() );
			  cs.setObject(5, CNRUserContext.getUser(userContext) );
			  cs.executeQuery();
			}catch (SQLException e) {
				try {
					SQLExceptionHandler sqlException = SQLExceptionHandler.getInstance();
					PersistencyException eccezione = sqlException.handleSQLException(e, pdgVar);
					String soggetto = "Si è verificato un errore durante l'approvazione della variazione sul bilancio dell'ente "+pdgVar.getEs_var_bil()+"/"+pdgVar.getPg_var_bil();
					
					String preText = "Si è verificato il seguente errore durante l'approvazione della variazione sul bilancio dell'ente "+pdgVar.getEs_var_bil()+"/"+pdgVar.getPg_var_bil() + 
					                 "<BR>" + "generata in automatico a seguito della Variazione al PdG n°"+pdgVar.getPg_variazione_pdg()+
					                 " del "+  pdgVar.getEsercizio()+".<BR><BR>"+
									 "<b>"+eccezione.getMessage()+"</b><BR><BR>"+
					                 "La Variazione al bilancio dell'Ente rimarrà pertanto PROVVISORIA.<BR>";
					generaEMAIL(userContext, pdgVar, soggetto, preText, null,"ERR");
					pdgVar.setErroreEsitaVariazioneBilancio(true);
				}catch (IntrospectionException e1) {
					throw handleException(e1);
				} catch (PersistencyException e1) {
					throw handleException(e1);
				}
			} finally {
			  if (cs != null) 
				  try {
					  cs.close();
				  } catch (SQLException e1) {
					  throw handleException(e1);
				  }
			}
		}	
		return pdgVar;
	}

	private void generaEMAIL(UserContext userContext, Pdg_variazioneBulk pdgVar, String soggetto, String preText, String postText,String tipo) throws ComponentException, IntrospectionException, PersistencyException{
		String formDate = "dd/MM/yyyy";
		SimpleDateFormat formatterDate = new SimpleDateFormat(formDate,Config.getHandler().getLocale());
		String text = new String();
		Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome)getHome(userContext,Utente_indirizzi_mailBulk.class);
		if (preText != null)
			text += preText + "<BR>";
		text = text +"CdR proponente: "+pdgVar.getCentro_responsabilita().getCd_ds_cdr()+"<BR>";
		text = text +"Tipologia: "+pdgVar.getTipo_variazione().getTi_tipologieKeys().get(pdgVar.getTipologia())+"<BR>";
		if (pdgVar.getTipologia_fin() != null)
		  text = text + pdgVar.getTi_tipologia_finKeys().get(pdgVar.getTipologia_fin())+"<BR>";
		text = text +"Data di approvazione: "+formatterDate.format(pdgVar.getDt_approvazione())+"<BR>";		
		text = text +"<BR>";
		text = text +"CdR abilitati a concorrervi:<BR>";
		String addressTO = null;
		for (java.util.Iterator j=pdgVar.getAssociazioneCDR().iterator();j.hasNext();){			
			Ass_pdg_variazione_cdrBulk ass_var = (Ass_pdg_variazione_cdrBulk)j.next();
			text = text + "CdR:"+ass_var.getCentro_responsabilita().getCd_ds_cdr()+ " quota assegnata " +new it.cnr.contab.util.EuroFormat().format(ass_var.getIm_spesa())+"<BR>";		
		}
		if (tipo.equalsIgnoreCase("ERR")){
			for (java.util.Iterator i= utente_indirizzi_mailHome.findUtenteMancataApprovazioneVariazioniBilancioEnteComp().iterator();i.hasNext();){
				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
				if (addressTO == null)
				  addressTO = new String();
				else
				  addressTO = addressTO + ",";    
				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
			}
		}else if (tipo.equalsIgnoreCase("APP")){
			for (java.util.Iterator i= utente_indirizzi_mailHome.findUtenteApprovaVariazioniBilancio(pdgVar).iterator();i.hasNext();){
				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
				if (addressTO == null)
				  addressTO = new String();
				else
				  addressTO = addressTO + ",";    
				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
			}
		}
		if (postText != null)
			text += "<BR>" + postText+ "<BR>";
		if (addressTO != null){
			try {
				SendMail.sendMail(soggetto,text,InternetAddress.parse(addressTO));
			} catch (AddressException e) {
			}
		}
	}
	
	public void aggiungiDettaglioVariazione(UserContext usercontext, Pdg_variazioneBulk pdgVar, V_assestatoBulk saldo)throws ComponentException {
		try {
			//Verifico innanzitutto se esiste l'associazione con il CDR altrimenti la creo
			Ass_pdg_variazione_cdrHome AssHome = (Ass_pdg_variazione_cdrHome)getHome(usercontext, Ass_pdg_variazione_cdrBulk.class);
			Ass_pdg_variazione_cdrBulk ass_cdrOld = new Ass_pdg_variazione_cdrBulk(pdgVar.getEsercizio(), pdgVar.getPg_variazione_pdg(), saldo.getCd_centro_responsabilita());
			Ass_pdg_variazione_cdrBulk ass_cdrNew = (Ass_pdg_variazione_cdrBulk)AssHome.findByPrimaryKey(ass_cdrOld);
            if (ass_cdrNew == null){
				ass_cdrOld.setIm_spesa(Utility.ZERO);
				ass_cdrOld.setToBeCreated();
				insertBulk(usercontext,ass_cdrOld);
				ass_cdrNew = ass_cdrOld;
            }

			
			//Ora posso inserire la riga di variazione
			Pdg_variazione_riga_gestBulk pdgVarRigaGest = new Pdg_variazione_riga_gestBulk();
			if (saldo.getTi_gestione().equals(CostantiTi_gestione.TI_GESTIONE_ENTRATE))
				ass_cdrOld.addToRigheVariazioneEtrGest(pdgVarRigaGest);
			else
				ass_cdrOld.addToRigheVariazioneSpeGest(pdgVarRigaGest);
				
			getHomeCache(usercontext).fetchAll(usercontext);

			pdgVarRigaGest.setLinea_attivita(new WorkpackageBulk(saldo.getCd_centro_responsabilita(),saldo.getCd_linea_attivita()));
			pdgVarRigaGest.setElemento_voce(new Elemento_voceBulk(saldo.getCd_elemento_voce(),saldo.getEsercizio(),saldo.getTi_appartenenza(),saldo.getTi_gestione()));
			pdgVarRigaGest.setIm_variazione(saldo.getImp_da_assegnare());
			pdgVarRigaGest.setArea((CdsBulk)getHome(usercontext,CdsBulk.class).findByPrimaryKey(new CdsBulk(ass_cdrNew.getCentro_responsabilita().getCd_cds())));
			pdgVarRigaGest.setToBeCreated();
			insertBulk(usercontext,pdgVarRigaGest);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}

	public SQLBuilder selectAssestatoEntrateByClause (UserContext userContext, Pdg_variazioneBulk pdgVar, V_assestatoBulk assestato, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, V_assestatoBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_ENTRATE);		
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.NOT_EQUALS, "C");
		sql.addClause("AND", "esercizio_res", SQLBuilder.EQUALS, pdgVar.getEsercizio());
		if (pdgVar.getTipologia_fin() != null){
			sql.addTableToHeader("NATURA");
			sql.addTableToHeader("LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO.CD_LINEA_ATTIVITA","LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA","LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","NATURA.TIPO",SQLBuilder.EQUALS,pdgVar.getTipologia_fin());			
		}		
		if (pdgVar.getCentro_responsabilita() != null){
			sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());		
		}else{
			if (pdgVar.getCentro_responsabilita().getLivello().intValue() > 1){
				sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
			}else{
				if (!pdgVar.getTipo_variazione().isMovimentoSuFondi())
				  sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
				else{
					SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
					sqlStruttura.addSQLJoin("V_ASSESTATO.ESERCIZIO","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
					sqlStruttura.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA","V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA");
					sqlStruttura.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
	                sql.addSQLExistsClause("AND",sqlStruttura);
				}
			}
		}
		return sql;
	}
	public SQLBuilder selectAssestatoSpeseByClause (UserContext userContext, Pdg_variazioneBulk pdgVar, V_assestatoBulk assestato, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, V_assestatoBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addClause("AND", "ti_appartenenza", SQLBuilder.NOT_EQUALS, "C");
		sql.addClause("AND", "esercizio_res", SQLBuilder.EQUALS, pdgVar.getEsercizio());
		if (pdgVar.getTipologia_fin() != null){
			sql.addTableToHeader("NATURA");
			sql.addTableToHeader("LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO.CD_LINEA_ATTIVITA","LINEA_ATTIVITA.CD_LINEA_ATTIVITA");
			sql.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA","LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA");
			sql.addSQLJoin("LINEA_ATTIVITA.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","NATURA.TIPO",SQLBuilder.EQUALS,pdgVar.getTipologia_fin());			
		}		
		if (pdgVar.getCentro_responsabilita() != null){
			sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());		
		}else{
			if (pdgVar.getCentro_responsabilita().getLivello().intValue() > 1){
				sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
			}else{
				if (!pdgVar.getTipo_variazione().isMovimentoSuFondi())
				  sql.addClause("AND", "cd_centro_responsabilita", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
				else{
					SQLBuilder sqlStruttura = getHome(userContext, V_struttura_organizzativaBulk.class).createSQLBuilder();
					sqlStruttura.addSQLJoin("V_ASSESTATO.ESERCIZIO","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO");
					sqlStruttura.addSQLJoin("V_ASSESTATO.CD_CENTRO_RESPONSABILITA","V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA");
					sqlStruttura.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_CDR_AFFERENZA", SQLBuilder.EQUALS, pdgVar.getCentro_responsabilita().getCd_centro_responsabilita());
	                sql.addSQLExistsClause("AND",sqlStruttura);
				}
			}
		}
		return sql;
	}
	private void checkDispAssestatoCdrGAEVoce(UserContext userContext, Pdg_variazioneBulk pdgVariazione, String nomeAction) throws ComponentException {
	   if (pdgVariazione.isCheckDispAssestatoCdrGAEVoceEseguito())
		   return;

	   Pdg_variazioneHome detHome = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
	   V_assestatoHome assHome = (V_assestatoHome)getHome(userContext,V_assestatoBulk.class);
	   BigDecimal totVariazioneEtr = new BigDecimal(0);
	   BigDecimal totVariazioneSpe = new BigDecimal(0);
	   Pdg_variazione_riga_gestBulk rigaDB;
	   String messaggio = "";

	   try{
			for (java.util.Iterator dett = detHome.findDettagliVariazioneGestionale(pdgVariazione).iterator();dett.hasNext();){
				Pdg_variazione_riga_gestBulk rigaVar = (Pdg_variazione_riga_gestBulk)dett.next();

				if (rigaVar.getTi_gestione().equals(Elemento_voceHome.GESTIONE_ENTRATE))
					totVariazioneEtr = totVariazioneEtr.add(rigaVar.getIm_variazione());
				else
					totVariazioneSpe = totVariazioneSpe.add(rigaVar.getIm_variazione());
					
				totVariazioneEtr = totVariazioneEtr.add(rigaVar.getIm_variazione());
				V_assestatoBulk assestato = (V_assestatoBulk)assHome.findByPrimaryKey(
												new V_assestatoBulk(rigaVar.getEsercizio(), 
																    rigaVar.getEsercizio(),
																    rigaVar.getCd_cdr_assegnatario(),
																    rigaVar.getCd_linea_attivita(),
																	rigaVar.getTi_appartenenza(),
																    rigaVar.getTi_gestione(),
																    rigaVar.getCd_elemento_voce()));
	
				if (assestato==null || assestato.getAssestato_finale().compareTo(Utility.ZERO) == -1) {
					if (messaggio!=null && messaggio.length()>0) 
						messaggio = messaggio+ "<BR>";
					messaggio = messaggio +  
					     "La disponibilità del CdR "+rigaVar.getCd_cdr_assegnatario()+
					     " per la Voce " + rigaVar.getCd_elemento_voce() + " e GAE " + rigaVar.getCd_linea_attivita() + 
					     " non è sufficiente a coprire<BR>la variazione che risulta di " + 
					     new it.cnr.contab.util.EuroFormat().format(rigaVar.getIm_variazione()) + ".<BR>";
				}
			}

			/*
			 * Se è una variazione di tipo "Movimentazione da Fondi" effettuo la verifica che il Fondo prescelto
			 * abbia una disponibilità sufficiente a coprire la variazione  
			 */
			if (pdgVariazione.getTipo_variazione().isMovimentoSuFondi()) {
				it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_ATTIVITA_SPESA_ENTE );

				if ( config == null  )
					throw new ApplicationException("Configurazione CNR: manca la definizione del GAE SPESA ENTE");
				
				V_assestatoBulk assestato = (V_assestatoBulk)assHome.findByPrimaryKey(
						new V_assestatoBulk(pdgVariazione.getEsercizio(), 
										    pdgVariazione.getEsercizio(),
										    pdgVariazione.getCd_centro_responsabilita(),
										    config.getVal02(),
										    pdgVariazione.getTi_appartenenza(),
										    pdgVariazione.getTi_gestione(),
										    pdgVariazione.getCd_elemento_voce()));

				if (assestato==null || assestato.getAssestato_finale().subtract(totVariazioneSpe).compareTo(Utility.ZERO) == -1) {
					if (messaggio!=null && messaggio.length()>0) 
						messaggio = messaggio+ "<BR>";
					messaggio = messaggio +  
					     "La Voce del Fondo " + pdgVariazione.getCd_elemento_voce() +
					     " del CdR "+pdgVariazione.getCd_centro_responsabilita() +
					     " non è sufficiente a coprire la variazione che risulta di " + 
					     new it.cnr.contab.util.EuroFormat().format(totVariazioneSpe) + ".<BR>";
				}
			}
			
			String messaggioPianoEco = Utility.createSaldoComponentSession().getMessaggioSfondamentoPianoEconomico(userContext, pdgVariazione);
			if (messaggioPianoEco!=null && messaggioPianoEco.length()>0) {
				if (messaggio!=null) 
					messaggio = messaggio+ "<BR>";
				messaggio = messaggio + messaggioPianoEco.replace("\n", "<BR>");
			}
	   } catch (PersistencyException e) {
		   throw new ComponentException(e);
	   }catch (RemoteException e) {
		   throw new ComponentException(e);
	   } catch (EJBException e) {
		   throw new ComponentException(e);
	   }
   
	   if (!messaggio.equals(""))
		   throw handleException( new OptionRequestException(nomeAction, "Attenzione!<BR>"+messaggio+"<BR>Vuoi continuare per il momento?"));
	}
	
	private void controllaQuadraturaImportiAree(UserContext userContext, Pdg_variazioneBulk pdgVar) throws ComponentException {
		try {
			SQLBuilder sql = sqlImportiAree(userContext, pdgVar);

			String area = null;
			BigDecimal impSpe = Utility.ZERO;
			BigDecimal impEtr = Utility.ZERO;
			try {
				java.sql.ResultSet rs = null;
				LoggableStatement ps = null;
				try {
					ps = sql.prepareStatement(getConnection(userContext));
					try {
						rs = ps.executeQuery();
						while (rs.next()) {
						area = rs.getString(1);
						if (rs.getBigDecimal(2)!= null)
							impSpe = impSpe.add(rs.getBigDecimal(2));
						if (rs.getBigDecimal(3)!= null)
							impEtr = impEtr.add(rs.getBigDecimal(3));
								
						if (!area.equals(pdgVar.getCd_centro_responsabilita()) && impSpe.compareTo(impEtr)!=0)
							throw new ApplicationException("L'importo assegnato all'Area "+area+" di parte spese (" +
										new it.cnr.contab.util.EuroFormat().format(impSpe) + 
										") è diverso dall'importo assegnato alla stessa Area di parte entrate (" +
										new it.cnr.contab.util.EuroFormat().format(impEtr) + ").");
						}
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
		} catch (ComponentException e) {
			throw handleException(e);
		}
	}

	private SQLBuilder sqlImportiAree(UserContext userContext, Pdg_variazioneBulk pdgVar) throws ComponentException {
		try {
			Pdg_variazione_riga_gestHome home = (Pdg_variazione_riga_gestHome)getHome(userContext,Pdg_variazione_riga_gestBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.resetColumns();
			sql.addColumn("CD_CDS_AREA");
			sql.addColumn("NVL(SUM(NVL(IM_SPESE_GEST_ACCENTRATA_EST,0)" + 
					            "+ NVL(IM_SPESE_GEST_ACCENTRATA_INT,0)" + 
					            "+ NVL(IM_SPESE_GEST_DECENTRATA_EST,0)" +
					            "+ NVL(IM_SPESE_GEST_DECENTRATA_INT,0)), 0)");
			sql.addColumn("NVL(SUM(IM_ENTRATA),0)");
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,pdgVar.getEsercizio());
			sql.addClause("AND","pg_variazione_pdg",SQLBuilder.EQUALS,pdgVar.getPg_variazione_pdg());
			sql.addSQLClause("AND","substr(cd_cdr_assegnatario,1,3)!=cd_cds_area");
			sql.addSQLGroupBy("CD_CDS_AREA")	;
			
			return sql;
			
		} catch (ComponentException e) {
			throw handleException(e);
		}
		
	}
	public SQLBuilder selectElemento_voceByClause (UserContext userContext, Pdg_variazioneBulk pdgVar, Elemento_voceBulk voce, CompoundFindClause clause) throws ComponentException, PersistencyException{	
		SQLBuilder sql = getHome(userContext, Elemento_voceBulk.class).createSQLBuilder();
		sql.addClause( clause );
		sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
		sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, CostantiTi_gestione.TI_GESTIONE_SPESE);		
		sql.addClause("AND", "fl_voce_fondo", SQLBuilder.EQUALS, Boolean.TRUE);	
		return sql;
	}
    public boolean isCdsAbilitatoAdApprovare(UserContext userContext,String cd_cds, Pdg_variazioneBulk pdg) throws ComponentException{
    	try {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk)getHome(userContext, Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(cd_cds,((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio()));
			if (!param_cds.getFl_approva_var_pdg().booleanValue())
			   return false;
			if (pdg.getTipologia()!=null) {
				Tipo_variazioneHome tipoVarHome = (Tipo_variazioneHome)getHome(userContext, Tipo_variazioneBulk.class);
				Tipo_variazioneBulk tipoVarBulk = (Tipo_variazioneBulk)tipoVarHome.findByPrimaryKey(new Tipo_variazioneBulk(pdg.getEsercizio(), pdg.getTipologia()));
				if (tipoVarBulk	!= null)
					return tipoVarBulk.getTi_approvazione()!=null &&
						   tipoVarBulk.getTi_approvazione().equals(Tipo_variazioneBulk.APPROVAZIONE_CDS);
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
    	return false;
    }
	/**
	  * Verifica che il CDR associato alla variazione è eliminabile 
	  *
	  * Pre-post-conditions:
	  *
	  * @param	userContext 
	  * @param	assBulk l'OggettoBulk della associazione CDR della variazione
	  * @return	boolean
	  *
	**/
	public void validaAssociazioneCDRPerCancellazione(UserContext userContext, Ass_pdg_variazione_cdrBulk assBulk) throws ComponentException {
		try{
			Ass_pdg_variazione_cdrHome assHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);

			if (!assHome.findDettagliEntrataVariazioneGestionale(assBulk).isEmpty() ||
				!assHome.findDettagliSpesaVariazioneGestionale(assBulk).isEmpty())
				throw new ComponentException("Non è possibile eliminare l'associazione della variazione con il CDR " + assBulk.getCd_centro_responsabilita() + " in quanto esistono dettagli di entrata/spesa collegati.");
		} catch (it.cnr.jada.persistency.PersistencyException pe){
			throw new ComponentException(pe);
		}
	}
	public String controllaTotPropostoEntrataSpesa(it.cnr.jada.UserContext usercontext,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk pdg) throws it.cnr.jada.comp.ComponentException {
		BigDecimal totSommaEntrata = ZERO;
		BigDecimal totSommaSpesa = ZERO;

		if (pdg == null || pdg.getAssociazioneCDR() == null) return null;

		for (java.util.Iterator j=pdg.getAssociazioneCDR().iterator();j.hasNext();){			
			Ass_pdg_variazione_cdrBulk ass_pdg = (Ass_pdg_variazione_cdrBulk)j.next();
			if (ass_pdg.getIm_entrata() != null) {
			  totSommaEntrata = totSommaEntrata.add(ass_pdg.getIm_entrata());
			  if (pdg.getTipo_variazione().isPrelievoFondi() && ass_pdg.getIm_entrata().compareTo(ZERO)!=0)
				  return ("Non possono essere assegnate quote di entrate in una variazione di tipo 'Prelievo Fondi'.");
			  if (pdg.getTipo_variazione().isRestituzioneFondi() && ass_pdg.getIm_entrata().compareTo(ZERO)!=0)
				  return ("Non possono essere assegnate quote di entrate in una variazione di tipo 'Restituzione Fondi'.");
			  if (pdg.getTipo_variazione().isVariazionePositivaSuFondi() && ass_pdg.getIm_entrata().compareTo(ZERO)==-1)
				  return ("Non possono essere assegnate quote di entrate negative in una variazione di tipo 'Incremento Fondi'.");
			  if (pdg.getTipo_variazione().isVariazioneNegativaSuFondi() && ass_pdg.getIm_entrata().compareTo(ZERO)==1)
				  return ("Non possono essere assegnate quote di entrate positive in una variazione di tipo 'Decremento Fondi'.");
			}
			if (ass_pdg.getIm_spesa() != null) {  
			  totSommaSpesa = totSommaSpesa.add(ass_pdg.getIm_spesa());
			  if (pdg.getTipo_variazione().isPrelievoFondi() && ass_pdg.getIm_spesa().compareTo(ZERO)==-1)
				  return ("Non possono essere assegnate quote di spesa negative in una variazione di tipo 'Prelievo Fondi'.");
			  if (pdg.getTipo_variazione().isRestituzioneFondi() && ass_pdg.getIm_spesa().compareTo(ZERO)==1)
				  return ("Non possono essere assegnate quote di spesa positive in una variazione di tipo 'Restituzione Fondi'.");
			  if (pdg.getTipo_variazione().isVariazioneSuFondi() && ass_pdg.getIm_spesa().compareTo(ZERO)!=0)
				  return ("Non possono essere assegnate quote di spesa in una variazione di tipo 'Incremento/Decremento Fondi'.");
			}
		}
		if (!pdg.getTipo_variazione().isMovimentoSuFondi() && totSommaEntrata.compareTo(totSommaSpesa)!=0) {
			return ("La quota di spesa assegnata ("+new it.cnr.contab.util.EuroFormat().format(totSommaSpesa)+")"+
									   "\n" + "non è uguale alla quota di entrata assegnata ("+
									   new it.cnr.contab.util.EuroFormat().format(totSommaEntrata)+")");
		}
	  return null;
	}	
	public it.cnr.jada.bulk.OggettoBulk statoPrecedente(
			UserContext userContext, it.cnr.jada.bulk.OggettoBulk oggettoBulk)
			throws ComponentException {
		Pdg_variazioneBulk var = (Pdg_variazioneBulk) oggettoBulk;
		var.setStato(Pdg_variazioneBulk.STATO_PROPOSTA_PROVVISORIA);
		var.setDt_chiusura(null);
		var.setToBeUpdated(); 
		var = (Pdg_variazioneBulk) super.modificaConBulk(userContext, var);
		aggiornaLimiteSpesa(userContext, var);
		return var;
	}
	private void controllaPdgPianoEconomico(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException{
		try {
			if (Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(userContext, CNRUserContext.getEsercizio(userContext))) {
				List<CtrlPianoEco> listCtrlPianoEco = new ArrayList<CtrlPianoEco>();

				String cdNaturaReimpiego = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_NATURA_REIMPIEGO);
				String cdVoceSpeciale = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), null, Configurazione_cnrBulk.PK_ELEMENTO_VOCE_SPECIALE, Configurazione_cnrBulk.SK_TEMPO_IND_SU_PROGETTI_FINANZIATI);

				String cdrPersonale = Optional.ofNullable(((ObbligazioneHome)getHome(userContext, ObbligazioneBulk.class)).recupero_cdr_speciale_stipendi())
						.orElseThrow(() -> new ComponentException("Non è possibile individuare il codice CDR del Personale."));
				CdrBulk cdrPersonaleBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(cdrPersonale));

				Ass_pdg_variazione_cdrHome ass_cdrHome = (Ass_pdg_variazione_cdrHome)getHome(userContext,Ass_pdg_variazione_cdrBulk.class);
				java.util.Collection<Pdg_variazione_riga_gestBulk> dettagliSpesa = ass_cdrHome.findDettagliSpesa(variazione);
	
				for (Pdg_variazione_riga_gestBulk varStanzRiga : dettagliSpesa) {
					//verifico se si tratta di area
					CdrBulk cdrBulk = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(new CdrBulk(varStanzRiga.getCd_cdr_assegnatario()));
					Unita_organizzativaBulk uoBulk = (Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdrBulk.getCd_unita_organizzativa()));
					boolean isUoArea = uoBulk.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA);
					
					//verifico se si tratta di cdr Personale
					boolean isDettPersonale = uoBulk.getCd_unita_organizzativa().equals(cdrPersonaleBulk.getCd_unita_organizzativa());
					if (!isDettPersonale) {
						//verifico se si tratta di voce accentrata verso il personale
						Elemento_voceBulk voce = (Elemento_voceBulk)getHome(userContext, Elemento_voceBulk.class).findByPrimaryKey(varStanzRiga.getElemento_voce());
						Classificazione_vociBulk classif = (Classificazione_vociBulk)getHome(userContext, Classificazione_vociBulk.class).findByPrimaryKey(new Classificazione_vociBulk(voce.getId_classificazione()));
						isDettPersonale = classif.getFl_accentrato()&&cdrPersonale.equals(classif.getCdr_accentratore());
					}

					//recupero la GAE
					BulkHome lattHome = getHome(userContext, WorkpackageBulk.class, "V_LINEA_ATTIVITA_VALIDA");
					SQLBuilder sql = lattHome.createSQLBuilder();
	
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,varStanzRiga.getEsercizio());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,varStanzRiga.getCd_cdr_assegnatario());
					sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",SQLBuilder.EQUALS,varStanzRiga.getCd_linea_attivita());
						
					List<WorkpackageBulk> listGAE = lattHome.fetchAll(sql);
					
					if (listGAE.isEmpty() || listGAE.size()>1)
						throw new ApplicationException("Errore in fase di ricerca linea_attivita "+varStanzRiga.getEsercizio()+"/"+varStanzRiga.getCd_centro_responsabilita()+"/"+varStanzRiga.getCd_linea_attivita()+".");
					
					WorkpackageBulk linea = listGAE.get(0);

					//recupero il progetto per verificare se è scaduto
					ProgettoBulk progetto = linea.getProgetto();
					getHomeCache(userContext).fetchAll(userContext);

					//effettuo controlli sulla validità del progetto
					Optional.ofNullable(progetto.getOtherField()).filter(Progetto_other_fieldBulk::isStatoApprovato)
					.orElseThrow(()->new ApplicationException("Attenzione! Il progetto "+progetto.getCd_progetto()
							+ " non risulta in stato approvato. Variazione non consentita!"));
						
					Optional.ofNullable(progetto.getOtherField().getDtInizio())
					.filter(dt->!dt.after(variazione.getDt_chiusura()))
					.orElseThrow(()->new ApplicationException("Attenzione! GAE "+linea.getCd_linea_attivita()+" non selezionabile. "
							+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
							+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
							+ "rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+")."));
		
					Timestamp dtScadenza = Optional.ofNullable(
							Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
							.orElse(null);

					boolean isNaturaReimpiego = Optional.ofNullable(cdNaturaReimpiego).map(el->el.equals(linea.getNatura().getCd_natura()))
							.orElse(Boolean.FALSE);
					boolean isVoceSpeciale = Optional.ofNullable(cdVoceSpeciale).map(el->el.equals(linea.getNatura().getCd_natura()))
							.orElse(Boolean.FALSE);
					
					boolean isCashFund = isNaturaReimpiego||isDettPersonale||isUoArea;
					
					//recupero il record se presente altrimenti ne creo uno nuovo
					CtrlPianoEco pianoEco = listCtrlPianoEco.stream()
							.filter(el->el.getProgetto().getPg_progetto().equals(progetto.getPg_progetto()))
							.filter(el->isCashFund?el.isCashFund():!el.isCashFund())
							.findFirst()
							.orElse(new CtrlPianoEco(progetto, dtScadenza, isCashFund));

					if (varStanzRiga.getIm_variazione().compareTo(BigDecimal.ZERO)>0) {
						pianoEco.setImpPositivi(pianoEco.getImpPositivi().add(varStanzRiga.getIm_variazione().abs()));
						if (isNaturaReimpiego)
							pianoEco.setImpPositiviNaturaReimpiego(pianoEco.getImpPositiviNaturaReimpiego().add(varStanzRiga.getIm_variazione().abs()));
						if (isVoceSpeciale)
							pianoEco.setImpPositiviVoceSpeciale(pianoEco.getImpPositiviVoceSpeciale().add(varStanzRiga.getIm_variazione().abs()));
					} else {
						pianoEco.setImpNegativi(pianoEco.getImpNegativi().add(varStanzRiga.getIm_variazione().abs()));
						if (isNaturaReimpiego)
							pianoEco.setImpNegativiNaturaReimpiego(pianoEco.getImpNegativiNaturaReimpiego().add(varStanzRiga.getIm_variazione().abs()));
						if (isVoceSpeciale)
							pianoEco.setImpNegativiVoceSpeciale(pianoEco.getImpNegativiVoceSpeciale().add(varStanzRiga.getIm_variazione().abs()));
					}
					
					listCtrlPianoEco.add(pianoEco);
				}

				/**
				 * 1. non è possibile attribuire fondi ad un progetto scaduto
				 */
				listCtrlPianoEco.stream()
					.filter(el->Optional.ofNullable(el.getDtScadenza()).map(dt->dt.before(variazione.getDt_chiusura())).orElse(Boolean.FALSE))
					.filter(el->el.getImpPositivi().compareTo(BigDecimal.ZERO)>0)
					.findFirst().ifPresent(el->{
						throw new DetailedRuntimeException("Attenzione! Non è possibile attribuire fondi al progetto "+
								el.getProgetto().getCd_progetto()+
								" in quanto scaduto ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(el.getDtScadenza()) +
								") rispetto alla data di chiusura della variazione ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(variazione.getDt_chiusura())+").");});
				
				BigDecimal impNegativiPrgScaduti = listCtrlPianoEco.stream()
						.filter(el->Optional.ofNullable(el.getDtScadenza()).map(dt->dt.before(variazione.getDt_chiusura())).orElse(Boolean.FALSE))
						.filter(el->el.getImpNegativi().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpNegativi)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				BigDecimal impPositiviCashFund = listCtrlPianoEco.stream()
						.filter(CtrlPianoEco::isCashFund)
						.map(CtrlPianoEco::getImpPositivi)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				BigDecimal impPositiviNaturaReimpiego = listCtrlPianoEco.stream()
						.filter(CtrlPianoEco::isCashFund)
						.filter(el->el.getImpPositiviNaturaReimpiego().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpPositiviNaturaReimpiego)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				/**
				 * 2. è possibile attribuire fondi ad un progetto di natura 6 solo se ne vengono sottratti equivalenti da:
				 * 		a. un progetto scaduto
				 * 		b. dalla voce speciale (11048)
				 */
				if (impPositiviNaturaReimpiego.compareTo(BigDecimal.ZERO)>0) {
					BigDecimal impNegativiVoceSpecialePrgInCorso = listCtrlPianoEco.stream()
							.filter(el->!Optional.ofNullable(el.getDtScadenza()).map(dt->dt.before(variazione.getDt_chiusura())).orElse(Boolean.FALSE))
							.filter(el->el.getImpNegativiVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
							.map(CtrlPianoEco::getImpNegativiVoceSpeciale)
							.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
					boolean existsPrgScaduti = impNegativiPrgScaduti.compareTo(BigDecimal.ZERO)>0;
					boolean existsVoceSpeciale = impNegativiVoceSpecialePrgInCorso.compareTo(BigDecimal.ZERO)>0;
					if (impPositiviNaturaReimpiego.compareTo(impNegativiPrgScaduti.add(impNegativiVoceSpecialePrgInCorso))!=0)
						throw new ApplicationException("Attenzione! Risultano trasferimenti a GAE di natura 6 - 'Reimpiego di risorse' "
								+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impPositiviNaturaReimpiego)
								+ " che non corrisponde all'importo prelevato da"
								+ (existsPrgScaduti?" progetti scaduti ":"")
								+ (existsPrgScaduti&&existsVoceSpeciale?"e da":"")
								+ (existsVoceSpeciale?"lla Voce "+cdVoceSpeciale:"")
								+" ("
								+ new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti)+").");
				}

				/**
				 * 3. se vengono sottratti importi ad un progetto scaduto devono essere girati a GaeNatura6 o al CDRPersonale o alle Aree
				 */
				if (impNegativiPrgScaduti.compareTo(BigDecimal.ZERO)>0 && impNegativiPrgScaduti.compareTo(impPositiviCashFund)>0)
					throw new ApplicationException("Attenzione! Risultano prelievi da progetti scaduti"
							+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiPrgScaduti)
							+ " che non risultano totalmente coperto da variazioni a favore"
							+ " di GAE di natura 6 - 'Reimpiego di risorse' o del CDR Personale o di Aree ("
							+ new it.cnr.contab.util.EuroFormat().format(impPositiviCashFund)+").");

				/**
				 * 4. se un progetto non è scaduto è possibile attribuire somme solo se stornate dallo stesso progetto
				 */
				listCtrlPianoEco.stream()
				.filter(el->!Optional.ofNullable(el.getDtScadenza()).map(dt->dt.before(variazione.getDt_chiusura())).orElse(Boolean.FALSE))
				.filter(el->el.getImpPositivi().compareTo(BigDecimal.ZERO)>0)
				.filter(el->el.getImpPositivi().compareTo(el.getImpNegativi())!=0)
				.findFirst().ifPresent(el->{
					throw new DetailedRuntimeException("Attenzione! Sono stati attribuiti fondi al progetto "+
							el.getProgetto().getCd_progetto()+"(" + 
							new it.cnr.contab.util.EuroFormat().format(el.getImpPositivi()) +
							") non compensati da un equivalente prelievo nell'ambito dello stesso progetto ("+
							new it.cnr.contab.util.EuroFormat().format(el.getImpNegativi()) + ")");});
				
				/**
				 * 5. non è possibile attribuire fondi alla voce speciale (11048)
				 */
				BigDecimal impPositiviVoceSpeciale = listCtrlPianoEco.stream()
						.filter(el->el.getImpPositiviVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpPositiviVoceSpeciale)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);
				
				if (impPositiviVoceSpeciale.compareTo(BigDecimal.ZERO)>0)
					throw new ApplicationException("Attenzione! Non è possibile attribuire fondi alla voce "
							+ cdVoceSpeciale + " ("
							+ new it.cnr.contab.util.EuroFormat().format(impPositiviVoceSpeciale)+").");
				
				/**
				 * 6. se vengono spostate somme dalla voce speciale (11048) devono essere girate a GaeNatura6
				 */
				BigDecimal impNegativiVoceSpeciale = listCtrlPianoEco.stream()
						.filter(el->el.getImpNegativiVoceSpeciale().compareTo(BigDecimal.ZERO)>0)
						.map(CtrlPianoEco::getImpNegativiVoceSpeciale)
						.reduce((x,y)->x.add(y)).orElse(BigDecimal.ZERO);

				if (impNegativiVoceSpeciale.compareTo(BigDecimal.ZERO)>0 && impNegativiVoceSpeciale.compareTo(impPositiviNaturaReimpiego)>0)
					throw new ApplicationException("Attenzione! Risultano prelievi dalla voce " + cdVoceSpeciale
							+ " per un importo di "	+ new it.cnr.contab.util.EuroFormat().format(impNegativiVoceSpeciale)
							+ " che non risultano totalmente coperto da variazioni a favore"
							+ " di GAE di natura 6 - 'Reimpiego di risorse' ("
							+ new it.cnr.contab.util.EuroFormat().format(impPositiviNaturaReimpiego)+").");				
			}
        } catch (DetailedRuntimeException _ex) {
            throw new ApplicationException(_ex.getMessage());
        } catch (PersistencyException|RemoteException|IntrospectionException e) {
			throw new ComponentException(e);
		}
	}
}
