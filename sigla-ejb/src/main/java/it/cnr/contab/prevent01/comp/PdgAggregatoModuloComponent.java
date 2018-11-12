package it.cnr.contab.prevent01.comp;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.esercizio.bulk.EsercizioHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrHome;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_speHome;
//import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateHome;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseHome;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseHome;
import it.cnr.contab.prevent01.bulk.Stampa_pdgp_bilancioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_piano_econom_progettoHome;
//import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.IPrintMgr;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.ObjectNotFoundException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.OptionBP;

public class PdgAggregatoModuloComponent extends CRUDComponent implements IPrintMgr {
/**
 * PdgAggregatoComponent constructor comment.
 */
	public PdgAggregatoModuloComponent() {
		super();
	}
	
	private OggettoBulk intBulk(UserContext userContext, OggettoBulk bulk) throws ComponentException {
		try {
			Optional.ofNullable(bulk).filter(CdrBulk.class::isInstance).map(CdrBulk.class::cast)
				.flatMap(el->Optional.ofNullable(el.getDettagli())).map(BulkList<Pdg_moduloBulk>::stream)
				.orElse(Stream.empty()).forEach(el->{
					if (!Optional.ofNullable(el.getProgetto()).isPresent())
						throw new ApplicationRuntimeException("Salvataggio non possibile! Non risulta indicato il progetto su una riga!");
					else if (!Optional.ofNullable(el.getProgetto().getOtherField())
							.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getStato()))
							.filter(stato -> Arrays.asList(Progetto_other_fieldBulk.STATO_NEGOZIAZIONE, Progetto_other_fieldBulk.STATO_APPROVATO).indexOf(stato) != -1).isPresent()) {
						throw new ApplicationRuntimeException("Attenzione: il progetto "+el.getProgetto().getCd_progetto()+" non ha uno stato utile alla previsione! Deve essere completato dalla UO responsabile! "
								+ "Eliminare l'associazione al bilancio!");
					}
					if (!Optional.ofNullable(el.getProgetto().getOtherField())
							.flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
							.filter(tipoFinanziamentoBulk -> tipoFinanziamentoBulk.getFlPrevEntSpesa() || tipoFinanziamentoBulk.getFlRipCostiPers()).isPresent())
						throw new ApplicationRuntimeException("Attenzione: per il progetto "+el.getProgetto().getCd_progetto()+" non è consentita la previsione! "
								+ "Eliminare l'associazione al bilancio!");
				});
			return bulk;
    	} catch(Throwable e) {
    		throw handleException(e);
    	}
	}
	
	public OggettoBulk creaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
		intBulk(uc, (CdrBulk)bulk );
		for(int i = 0; ((CdrBulk)bulk).getDettagli().size() > i; i++) {
			((Pdg_moduloBulk) ((CdrBulk)bulk).getDettagli().get(i)).setCd_centro_responsabilita(((CdrBulk)bulk).getCd_centro_responsabilita());
		}
		return super.creaConBulk(uc, bulk);
	}
	
	public void eliminaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		for(int i = 0; ((CdrBulk)bulk).getDettagli().size() > i; i++) {
			((Pdg_moduloBulk) ((CdrBulk)bulk).getDettagli().get(i)).setCrudStatus(bulk.TO_BE_DELETED);
		}
		super.eliminaConBulk(aUC, bulk);
	}
	public OggettoBulk modificaConBulk(UserContext uc, OggettoBulk bulk) throws ComponentException {
		intBulk(uc, (CdrBulk)bulk );
		bulk.setCrudStatus(OggettoBulk.NORMAL);
		return super.modificaConBulk(uc, bulk);
	}  
	public OggettoBulk inizializzaBulkPerModifica(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			CdrBulk testata = (CdrBulk)super.inizializzaBulkPerModifica(userContext,bulk);
			CdrHome testataHome = (CdrHome)getHome(userContext, CdrBulk.class);
			Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
			testata.setDettagli(new it.cnr.jada.bulk.BulkList(testataHome.findPdgModuloDettagli(userContext, testata, parCnrBulk.getLivelloProgetto())));

			Pdg_moduloHome pdgModuloHome = (Pdg_moduloHome)getHome(userContext, Pdg_moduloBulk.class);
			for (Iterator iterator = testata.getDettagli().iterator(); iterator.hasNext();) {
				Pdg_moduloBulk moduloBulk = (Pdg_moduloBulk) iterator.next();
				moduloBulk.setExistGestionaleE(pdgModuloHome.existsGestionaleE(moduloBulk));
				moduloBulk.setExistGestionaleS(pdgModuloHome.existsGestionaleS(moduloBulk));
				moduloBulk.setExistDecisionaleE(moduloBulk.getExistGestionaleE()?Boolean.TRUE:pdgModuloHome.existsDecisionaleE(moduloBulk));
				moduloBulk.setExistDecisionaleS(moduloBulk.getExistGestionaleS()?Boolean.TRUE:pdgModuloHome.existsDecisionaleS(moduloBulk));

				Pdg_modulo_costiBulk moduloCosti = (Pdg_modulo_costiBulk)((Pdg_modulo_costiHome)getHome(userContext, Pdg_modulo_costiBulk.class)).findByPrimaryKey(new Pdg_modulo_costiBulk(moduloBulk));
				moduloBulk.setExistDecisionaleR(moduloCosti!=null &&
						                        (moduloCosti.getTot_risorse_presunte_es_prec().compareTo(BigDecimal.ZERO)>0 ||
											     moduloCosti.getTot_risorse_provenienti_es_prec().compareTo(BigDecimal.ZERO)>0));
				moduloBulk.setExistDecisionaleC(moduloCosti!=null && moduloCosti.getTot_costi().compareTo(BigDecimal.ZERO)>0);
			}
			getHomeCache(userContext).fetchAll(userContext);
			return testata;
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	public Query select(UserContext userContext,it.cnr.jada.persistency.sql.CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
	
		SQLBuilder sql = (SQLBuilder) super.select(userContext, clauses, bulk);
		CdrBulk cdr = (CdrBulk) bulk;
		sql = getHome(userContext, cdr, "V_CDR_VALIDO_LIV1").createSQLBuilder();
		sql.addSQLClause("AND", "esercizio", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		sql.addClause(clauses);
			
		CdrBulk cdrUtente = cdrFromUserContext(userContext);
	
		if (cdr == null && cdrUtente == null) 
			throw new ApplicationException("L'utente non è configurato correttamente per l'utilizzo del pdg preliminare");
		
		CdrBulk cdrLiv1=null;
		if (cdrUtente.getCd_cdr_afferenza()!= null){
			 cdrLiv1 = new CdrBulk(cdrUtente.getCd_cdr_afferenza());
			 cdrLiv1 = (CdrBulk)getHome(userContext, cdrLiv1).findByPrimaryKey(cdrLiv1);
		}
		Unita_organizzativaBulk uoCdrLiv1 = null;
		if (cdrUtente.getCd_cdr_afferenza()!= null)
			uoCdrLiv1 = (Unita_organizzativaBulk) getHome(userContext,cdrLiv1.getUnita_padre()).findByPrimaryKey(cdrLiv1.getUnita_padre());		

		if (cdrUtente.getLivello().compareTo(new Integer(1))==0
			|| uoCdrLiv1 == null
			|| uoCdrLiv1.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_SAC) 
			|| uoCdrLiv1.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA)) {

			cdrLiv1 = cdrUtente;
		}
					
		// riempiamo i dati di cdrUtente.getUnita_padre() dato che ci servono
		Unita_organizzativaBulk uo = (Unita_organizzativaBulk) getHome(userContext,cdrLiv1.getUnita_padre()).findByPrimaryKey(cdrLiv1.getUnita_padre());		

		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);


		if (!uoEnte.equalsByPrimaryKey(uo)) {
				sql.addSQLClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, uo.getCd_unita_organizzativa());
		}
		
		return sql;
	
	}

	public CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {

		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );

			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
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

	public SQLBuilder selectProgettoByClause (UserContext userContext,
										  Pdg_moduloBulk dett,
										  Progetto_sipBulk progetto,
										  CompoundFindClause clause)
	throws ComponentException, PersistencyException
	{
			Progetto_sipHome progettohome = (Progetto_sipHome)getHome(userContext, Progetto_sipBulk.class,"V_PROGETTO_PADRE");
			SQLBuilder sql = progettohome.createSQLBuilder();
			sql.addClause( clause );
			sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
			sql.addClause("AND", "pg_progetto", sql.EQUALS, dett.getPg_progetto());

			Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
			if (parCnrBulk.getFl_nuovo_pdg())
				sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
			else
				sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);
			sql.addClause("AND", "fl_utilizzabile", sql.EQUALS, Boolean.TRUE);
			// Se uo 999.000 in scrivania: visualizza tutti i progetti
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
			if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
				if (parCnrBulk.getFl_nuovo_pdg())
					sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
				else
					sql.addSQLExistsClause("AND",progettohome.abilitazioniModuli(userContext));
			}
                
			if (clause != null) 
			  sql.addClause(clause);
			return sql;
	}

	public OggettoBulk modificaStatoPdg_aggregato(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		try {
			if (isEsercizioChiuso(userContext,pdg_modulo))
				throw new ApplicationException("Stato del PdGP non modificabile ad esercizio chiuso.");

			Pdg_moduloBulk oldPdg_modulo = null;
			try {
				oldPdg_modulo = (Pdg_moduloBulk)getHome(userContext,pdg_modulo).findAndLock(pdg_modulo);
			}
			catch (ObjectNotFoundException e) {
				throw new ApplicationException("Lo stato del PdGP non è modificabile per record non ancora salvati.");
			}
			String nuovoStato = pdg_modulo.getCambia_stato();
			String vecchioStato = oldPdg_modulo.getStato();

			if (!isStatoCompatibile(vecchioStato,nuovoStato))
				throw new ApplicationException("Lo stato "+nuovoStato+" non è compatibile con lo stato attuale del PdGP ("+vecchioStato+")");

			Pdg_esercizioBulk es = findPdgEsercizio(userContext, pdg_modulo); 
			if (es!=null &&
				(es.getStato().equals(Pdg_esercizioBulk.STATO_APPROVAZIONE_CDR) ||
				 /*es.getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR) ||*/
				 es.getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR)))
				throw new ApplicationException("Non è possibile modificare lo stato del PdGP poichè risulta chiusa la fase previsionale per il CdR "+es.getCd_centro_responsabilita());

			pdg_modulo.setUser(userContext.getUser());
			
			// Invoco il metodo modificaStato_x_y()
			try {
				it.cnr.jada.util.Introspector.invoke(
					this,
					"modificaStatoPdg_aggregato_"+vecchioStato+"_"+nuovoStato,
					new Object[] {
						userContext,
						//cdrUtente,
						pdg_modulo
					});
			} catch(java.lang.reflect.InvocationTargetException ex) {
				throw ex.getTargetException();
			}

			pdg_modulo.setUser(userContext.getUser());
			pdg_modulo.setStato(pdg_modulo.getCambia_stato());
			
			pdg_modulo.setToBeUpdated();

			updateBulk(userContext,pdg_modulo);

			return pdg_modulo;
		} catch(ComponentException e) {
			throw e;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	public void modificaStatoPdg_aggregato_AC_CC(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		try {
			CdrBulk cdr = pdg_modulo.getCdr();
			if (!isStatoAggiornabilePerCostiDip(userContext, cdr))
				throw new ApplicationException("Non è stato effettuato lo scarico dei dipendenti sul PdGP - CDR per il CdR "+cdr.getCd_centro_responsabilita()+".\nImpossibile modificare lo stato.");
			
			controllaImportiFontiEsterne(userContext, pdg_modulo);
			
			Utility.createSaldoComponentSession().checkDispPianoEconomicoProgetto(userContext,new Pdg_modulo_costiBulk(pdg_modulo),true);
			
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	public void modificaStatoPdg_aggregato_CC_AC(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {

		Pdg_esercizioBulk es = findPdgEsercizio(userContext, pdg_modulo); 

		if (es==null)
			throw new ApplicationException("Non è stato impostato lo stato iniziale di apertura del PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
		if (!es.getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_CDR))
			throw new ApplicationException("Stato non modificabile poichè non è APERTO lo stato del PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
	}

	public void modificaStatoPdg_aggregato_CC_AD(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
	}

	public void modificaStatoPdg_aggregato_AP_CC(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
	}
	public void modificaStatoPdg_aggregato_CG_AG(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		Pdg_esercizioBulk es = findPdgEsercizio(userContext, pdg_modulo); 

		if (es==null)
			throw new ApplicationException("Non è stato impostato lo stato iniziale di apertura del PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
		if (!es.getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR))
			throw new ApplicationException("Stato non modificabile poichè non è 'Apertura Gestionale del CDR', lo stato del PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
	}
	public void modificaStatoPdg_aggregato_AP_AD(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {

		Pdg_esercizioBulk es = findPdgEsercizio(userContext, pdg_modulo); 

		if (es==null)
			throw new ApplicationException("Non è stato impostato lo stato iniziale di apertura del PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
		if (es.getStato().equals(Pdg_esercizioBulk.STATO_APPROVAZIONE_CDR) ||
			es.getStato().equals(Pdg_esercizioBulk.STATO_APERTURA_GESTIONALE_CDR) ||
			es.getStato().equals(Pdg_esercizioBulk.STATO_CHIUSURA_GESTIONALE_CDR))
			throw new ApplicationException("Stato non modificabile poichè risulta chiusa la fase previsionale per il PdGP - CDR per il CdR "+es.getCd_centro_responsabilita());
	}

	public void modificaStatoPdg_aggregato_AD_AP(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		try {
			CdrBulk cdr = pdg_modulo.getCdr();
			if (!isStatoAggiornabilePerCostiDip(userContext, cdr))
				throw new ApplicationException("Non è stato effettuato lo scarico dei dipendenti sul PdGP - CDR per il CdR "+cdr.getCd_centro_responsabilita()+" Impossibile modificare lo stato.");
			
			controllaStatoModuloAttivita(userContext, pdg_modulo);
			
			controllaImportiFontiEsterne(userContext, pdg_modulo);
			
			Utility.createSaldoComponentSession().checkDispPianoEconomicoProgetto(userContext,new Pdg_modulo_costiBulk(pdg_modulo),true);

			controllaAdeguamentoContrattazione(userContext, pdg_modulo);
			
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	public void modificaStatoPdg_aggregato_AG_CG(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		try {
		
			controllaRipartizioneDecisionale(userContext, pdg_modulo);
			
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	/**
	 * Trova il bulk Pdg_esercizioBulk "padre" di Pdg_moduloBulk con chiave
	 * Pdg_moduloBulk.cd_centro_responsabilita e Pdg_moduloBulk.esercizio
	 * 
	 */
	private Pdg_esercizioBulk findPdgEsercizio(UserContext userContext, Pdg_moduloBulk pdg_modulo)  throws ComponentException {
		Pdg_esercizioBulk es = new Pdg_esercizioBulk();
		//es.setEsercizio(CNRUserContext.getEsercizio(userContext));
		es.setEsercizio(pdg_modulo.getEsercizio());
		es.setCdr(new CdrBulk(pdg_modulo.getCd_centro_responsabilita()));
		try {
			es = (Pdg_esercizioBulk) getHome(userContext,es).findByPrimaryKey(es);
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
		return es;
	}

	protected boolean isEsercizioChiuso(UserContext userContext,Pdg_moduloBulk pdg_modulo) throws ComponentException {
		try {
			EsercizioHome home = (EsercizioHome)getHome(userContext,EsercizioBulk.class);
			CdrBulk cdr = (CdrBulk)getHome(userContext,pdg_modulo.getCdr()).findByPrimaryKey(pdg_modulo.getCdr());
			Unita_organizzativaBulk uo = (Unita_organizzativaBulk)getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
			return home.isEsercizioChiuso(userContext,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext),uo.getCd_unita_padre());
		} catch(PersistencyException e) {
			throw handleException(e);
		}
	}
	private boolean isStatoCompatibile(String vecchioStato,String nuovoStato) {
		if (Pdg_moduloBulk.STATO_AC.equals(vecchioStato))
			return Pdg_moduloBulk.STATO_CC.equals(nuovoStato);
		if (Pdg_moduloBulk.STATO_CC.equals(vecchioStato))
			return (Pdg_moduloBulk.STATO_AD.equals(nuovoStato)||
					Pdg_moduloBulk.STATO_AC.equals(nuovoStato));
		if (Pdg_moduloBulk.STATO_AD.equals(vecchioStato))
			return Pdg_moduloBulk.STATO_AP.equals(nuovoStato);
		if (Pdg_moduloBulk.STATO_AP.equals(vecchioStato))
			return 
				Pdg_moduloBulk.STATO_AC.equals(nuovoStato) ||
				Pdg_moduloBulk.STATO_AD.equals(nuovoStato) ||
				Pdg_moduloBulk.STATO_AG.equals(nuovoStato);
		if (Pdg_moduloBulk.STATO_AG.equals(vecchioStato))
			return Pdg_moduloBulk.STATO_CG.equals(nuovoStato);
		// nuovo cambio stato per gestione limite spesa
		if (Pdg_moduloBulk.STATO_CG.equals(vecchioStato))
			return Pdg_moduloBulk.STATO_AG.equals(nuovoStato);
		return false;
	}

	public boolean isPdGAggregatoModificabile(UserContext userContext,Pdg_moduloBulk pdg) throws ComponentException {
		return isUtenteEnte(userContext);
	}
	private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
		return isCdrEnte(userContext,cdrFromUserContext(userContext));
	}
	//Ritorna l'oggetto solo se creato
	public Pdg_moduloBulk findAndInsertBulkForMacro(it.cnr.jada.UserContext userContext,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException{
		try {
			Pdg_moduloHome home = (Pdg_moduloHome)getHome(userContext,Pdg_moduloBulk.class);
			if (home.findByPrimaryKey(pdg_modulo) == null){	
				Progetto_sipHome homeProgetto = (Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class,"V_PROGETTO_PADRE");
				SQLBuilder sql = homeProgetto.createSQLBuilder();
				sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg_modulo.getProgetto().getPg_progetto());
				sql.addSQLClause("AND", "FL_UTILIZZABILE", sql.EQUALS, "Y");
				sql.addSQLExistsClause("AND",homeProgetto.abilitazioniCommesse(userContext));
				SQLBroker broker = homeProgetto.createBroker( sql );
				if (( broker.next() )){
					Progetto_sipBulk modulo = (Progetto_sipBulk) broker.fetch(Progetto_sipBulk.class );
					if (ProgettoBulk.LIVELLO_PROGETTO_SECONDO.compareTo(modulo.getLivello())!=0) 
						throw new ApplicationException("Livello Progetto non corretto! Inserimento non possibile.");
					pdg_modulo.setEsercizio(CNRUserContext.getEsercizio(userContext));
					pdg_modulo.setStato(Pdg_moduloBulk.STATO_AC);
					pdg_modulo.setToBeCreated();
					return (Pdg_moduloBulk)super.creaConBulk(userContext,pdg_modulo);
				}else{
					throw new ApplicationException("Progetto non presente o CDR non abilitato al suo utilizzo!");
				}
			}
			return null;
		} catch(PersistencyException e) {
			throw handleException(e);
		}
	}
	
	/** 
	  *  default
	  *    PreCondition:
	  *      Viene richiesto l'esecuzione dello scarico dei costi del dipendente per un pdg
	  *    PostCondition:
	  *      Viene invocata la stored procedure CNRCTB061.scaricaCDPSuPdgP
	 */
	public OggettoBulk scaricaDipendentiSuPdGP(UserContext userContext, CdrBulk cdr) throws it.cnr.jada.comp.ComponentException {

		try {
			List listaModuli = findPdgModuloNE(userContext, cdr, Pdg_moduloBulk.STATO_AC);
			List listaModuli2 = findPdgModuloNE(userContext, cdr, Pdg_moduloBulk.STATO_AD);
			// se gli stati sono tutti AC oppure sono tutti AD può procedere con lo scarico
			if (!(listaModuli.isEmpty() || listaModuli2.isEmpty()))
				throw new ApplicationException("Lo scarico dei dipendenti non può essere effettuato poichè non tutte le righe del PdGP hanno stato "+Pdg_moduloBulk.STATO_AC+" oppure "+Pdg_moduloBulk.STATO_AD);
		} catch (Throwable e) {
			throw handleException(cdr,e);
		}
		
		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), "{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
					+ "CNRCTB061.scaricaCDPSuPdgP(?,?,?)}",false,this.getClass());
			cs.setObject( 1, CNRUserContext.getEsercizio(userContext));
			cs.setString( 2, cdr.getCd_centro_responsabilita());
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,cdr);
				cs.executeQuery();
				return cdr;
			} catch (Throwable e) {
				throw handleException(cdr,e);
			} finally {
				cs.close();
			}	
		} catch (java.sql.SQLException e) {
			// Gestisce eccezioni SQL specifiche (errori di lock,...)
			throw handleSQLException(e);
		}
	}

	/** 
	  *  default
	  *    PreCondition:
	  *      Viene richiesto l'annullamento dello scarico dei costi del dipendente per un pdg
	  *    PostCondition:
	  *      Viene invocata la stored procedure ORACLE annullaCDPSuPdg
	 */
	public OggettoBulk annullaScaricaDipendentiSuPdGP(UserContext userContext, CdrBulk cdr) throws ComponentException {

		try {
			List listaModuli = findPdgModulo(userContext, cdr, Pdg_moduloBulk.STATO_CC);
			if (!listaModuli.isEmpty())
				throw new ApplicationException("Lo scarico dei dipendenti non può essere annullato poichè non tutte le righe del PdGP hanno stato "+Pdg_moduloBulk.STATO_AC);
		} catch (Throwable e) {
			throw handleException(cdr,e);
		}

		try {
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ),"{call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
					+ "CNRCTB061.annullaCDPSuPdgP(?,?,?)}",false,this.getClass());
			cs.setObject( 1, CNRUserContext.getEsercizio(userContext));
			cs.setString( 2, cdr.getCd_centro_responsabilita());
			cs.setObject( 3, userContext.getUser());
			try {
				lockBulk(userContext,cdr);
				cs.executeQuery();
				return cdr;
			} catch (Throwable e) {
				throw handleException(cdr,e);
			} finally {
				cs.close();
			}
		} catch (java.sql.SQLException e) {
			throw handleSQLException(e);
		}
	}

	private boolean isStatoAggiornabilePerCostiDip(UserContext userContext, CdrBulk cdr) throws it.cnr.jada.comp.ComponentException {
		try {
			CostiDipendenteComponentSession costiDip = (CostiDipendenteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession",CostiDipendenteComponentSession.class);
			
			if (!costiDip.isCostiDipendenteCaricati(userContext, cdr))
				return true;
			if (costiDip.isCostiDipendenteRipartiti(userContext, cdr))
				return true;

			return false;

		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	private void controllaImportiFontiEsterne(UserContext userContext, Pdg_moduloBulk pdg) throws it.cnr.jada.comp.ComponentException {
		try{
			Pdg_moduloHome pdgHome = (Pdg_moduloHome)getHome(userContext,Pdg_moduloBulk.class);
			for(Iterator i=pdgHome.getAreeUtilizzate(pdg).iterator(); i.hasNext();)
				controllaImportiFontiEsterne(userContext, pdg, (CdsBulk)i.next());
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	private void controllaImportiFontiEsterne(UserContext userContext, Pdg_moduloBulk pdg, CdsBulk cds) throws it.cnr.jada.comp.ComponentException {
		BigDecimal impTotaleEntrate = new BigDecimal(0);
		BigDecimal impTotaleSpese = new BigDecimal(0);
		BigDecimal impTotaleSpesePrel = new BigDecimal(0);
		BigDecimal impTotaleEntrateDaPrel = new BigDecimal(0);
		try {
			Pdg_Modulo_EntrateHome home = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
			SQLBuilder sql = home.createSQLBuilder();

			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
			if (cds!=null && cds.getCd_unita_organizzativa()!=null)
				sql.addClause("AND","cd_cds_area",SQLBuilder.EQUALS,cds.getCd_unita_organizzativa());

			sql.addTableToHeader("NATURA");
			sql.addSQLJoin("PDG_MODULO_ENTRATE.CD_NATURA","NATURA.CD_NATURA");
			sql.addSQLClause("AND","NATURA.TIPO",SQLBuilder.EQUALS,NaturaBulk.TIPO_NATURA_FONTI_ESTERNE);

			CdrBulk cdr = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(pdg.getCdr());
			cdr.setUnita_padre((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));

			if (pdg.getCdr().isCdrSAC()) {
				sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
				sql.addSQLJoin("PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE","CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
				sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.FL_ESTERNA_DA_QUADRARE_SAC",SQLBuilder.EQUALS,"Y");
			}

			SQLBroker broker = home.createBroker(sql);
			
			while(broker.next()) {
				Pdg_Modulo_EntrateBulk pdge = (Pdg_Modulo_EntrateBulk)broker.fetch(Pdg_Modulo_EntrateBulk.class);
				if (pdge.getIm_entrata()!=null)
					impTotaleEntrate = impTotaleEntrate.add(pdge.getIm_entrata());
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}

		try {
			Pdg_modulo_speseHome home = (Pdg_modulo_speseHome)getHome(userContext,Pdg_modulo_speseBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
			if (cds!=null && cds.getCd_unita_organizzativa()!=null)
				sql.addClause("AND","cd_cds_area",SQLBuilder.EQUALS,cds.getCd_unita_organizzativa());
			SQLBroker broker = home.createBroker(sql);
			
			while(broker.next()) {
				Pdg_modulo_speseBulk pdgs = (Pdg_modulo_speseBulk)broker.fetch(Pdg_modulo_speseBulk.class);
				if (pdgs.getIm_spese_gest_accentrata_est()!=null)
					impTotaleSpese = impTotaleSpese.add(pdgs.getIm_spese_gest_accentrata_est());
				if (pdgs.getIm_spese_gest_decentrata_est()!=null)
					impTotaleSpese = impTotaleSpese.add(pdgs.getIm_spese_gest_decentrata_est());
			}

		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
		try{
			String labelProgetto = "modulo";

			Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
			Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
			if (parCnrBulk.getFl_nuovo_pdg())
				labelProgetto = "progetto";
			
			CdrBulk cdr = (CdrBulk)getHome(userContext, CdrBulk.class).findByPrimaryKey(pdg.getCdr());
			cdr.setUnita_padre((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cdr.getCd_unita_organizzativa())));
		
			if (!pdg.getCdr().isCdrSAC()){
				
				Pdg_modulo_speseHome home = (Pdg_modulo_speseHome)getHome(userContext,Pdg_modulo_speseBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addTableToHeader("ELEMENTO_VOCE");
				sql.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
				sql.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
				sql.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
				sql.addSQLClause("AND","FL_PRELIEVO",SQLBuilder.EQUALS,"Y");
				sql.addSQLJoin("PDG_MODULO_SPESE.ID_CLASSIFICAZIONE","ELEMENTO_VOCE.ID_CLASSIFICAZIONE");
				if (cds!=null && cds.getCd_unita_organizzativa()!=null)
					sql.addClause("AND","cd_cds_area",SQLBuilder.EQUALS,cds.getCd_unita_organizzativa());
				SQLBroker broker = home.createBroker(sql);
				while(broker.next()) {
					Pdg_modulo_speseBulk pdgs = (Pdg_modulo_speseBulk)broker.fetch(Pdg_modulo_speseBulk.class);
					if (pdgs.getIm_spese_gest_accentrata_est()!=null)
						impTotaleSpesePrel = impTotaleSpesePrel.add(pdgs.getIm_spese_gest_accentrata_est());
					if (pdgs.getIm_spese_gest_decentrata_est()!=null)
						impTotaleSpesePrel = impTotaleSpesePrel.add(pdgs.getIm_spese_gest_decentrata_est());
				}
				Pdg_Modulo_EntrateHome homeEntr = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
				SQLBuilder sqlEntr = homeEntr.createSQLBuilder();
				sqlEntr.addTableToHeader("CLASSIFICAZIONE_VOCI,ELEMENTO_VOCE");
				sqlEntr.addSQLJoin("PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE","CLASSIFICAZIONE_VOCI.ID_CLASSIFICAZIONE");
				sqlEntr.addSQLJoin("PDG_MODULO_ENTRATE.ID_CLASSIFICAZIONE","ELEMENTO_VOCE.ID_CLASSIFICAZIONE");
				sqlEntr.addClause("AND","esercizio",SQLBuilder.EQUALS,pdg.getEsercizio());
				sqlEntr.addClause("AND","cd_centro_responsabilita",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
				sqlEntr.addClause("AND","pg_progetto",SQLBuilder.EQUALS,pdg.getPg_progetto());
				sqlEntr.addSQLClause("AND","FL_SOGGETTO_PRELIEVO",SQLBuilder.EQUALS,"Y");
				if (cds!=null && cds.getCd_unita_organizzativa()!=null)
					sqlEntr.addClause("AND","cd_cds_area",SQLBuilder.EQUALS,cds.getCd_unita_organizzativa());

				SQLBroker brokerEntr = homeEntr.createBroker(sqlEntr);
				
				while(brokerEntr.next()) {
					Pdg_Modulo_EntrateBulk pdge = (Pdg_Modulo_EntrateBulk)brokerEntr.fetch(Pdg_Modulo_EntrateBulk.class);
					if (pdge.getIm_entrata()!=null){
						Elemento_voceHome home_voce =(Elemento_voceHome)getHome(userContext,Elemento_voceBulk.class);
						SQLBuilder sql_voce = home_voce.createSQLBuilder();
						sql_voce.addSQLClause("AND","ID_CLASSIFICAZIONE" ,SQLBuilder.EQUALS,pdge.getId_classificazione());
						sql_voce.addSQLClause("AND","FL_SOGGETTO_PRELIEVO",SQLBuilder.EQUALS,"Y");
						java.util.List voci=home_voce.fetchAll(sql_voce);
						if(voci.size() >1)// non dovrebbe capitare mai
							throw new ApplicationException("Esistono più voci sulla stessa classificazione.");
							for(Iterator i=voci.iterator();i.hasNext();){
								Elemento_voceBulk ev = (Elemento_voceBulk)i.next();
								if(ev!=null && ev.getPerc_prelievo_pdgp_entrate().compareTo(Utility.ZERO)!=0)
									impTotaleEntrateDaPrel = impTotaleEntrateDaPrel.add(pdge.getIm_entrata().multiply(ev.getPerc_prelievo_pdgp_entrate()).divide(new BigDecimal("100"),2,BigDecimal.ROUND_HALF_DOWN));
							}
					}
				}
				// se non ci sono entrate soggette a prelievo bisogna fare lo stesso il controllo
				if(impTotaleEntrateDaPrel.compareTo(BigDecimal.ZERO)!=0)
					if(impTotaleEntrateDaPrel.compareTo(impTotaleSpesePrel)!=0)
						throw new ApplicationException("Per il "+labelProgetto+" "+pdg.getCd_progetto()+" il contributo per l'attività ordinaria è pari a "+ new it.cnr.contab.util.EuroFormat().format(impTotaleEntrateDaPrel)+
									". Impossibile salvare, poichè è stato imputato sulla voce dedicata l'importo di "+new it.cnr.contab.util.EuroFormat().format(impTotaleSpesePrel)+".");
			}
			if (parCnrBulk.getFl_pdg_quadra_fonti_esterne() && impTotaleSpese.compareTo(impTotaleEntrate)!=0)
				if ( cds!=null ) {
					if ( cds.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_AREA) ) 
						throw new ApplicationException("Per l'area " + cds.getCd_unita_organizzativa() + " e per il "+labelProgetto+" "+ pdg.getCd_progetto()+", il totale degli importi provenienti dalle fonti esterne delle entrate non corrisponde a quello delle spese. Impossibile procedere.");
					else
						throw new ApplicationException("Per il CDS " + cds.getCd_unita_organizzativa() + " e per il "+labelProgetto+" "+ pdg.getCd_progetto()+", il totale degli importi provenienti dalle fonti esterne delle entrate non corrisponde a quello delle spese. Impossibile procedere.");
				}
				else
					throw new ApplicationException("Per il "+labelProgetto+" "+ pdg.getCd_progetto()+" il totale degli importi provenienti dalle fonti esterne delle entrate non corrisponde a quello delle spese. Impossibile procedere.");
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
	}


	public Pdg_esercizioBulk getCdrPdGP(UserContext userContext, CdrBulk cdr) throws it.cnr.jada.comp.ComponentException {
		Pdg_esercizioBulk es = new Pdg_esercizioBulk();
		es.setEsercizio(CNRUserContext.getEsercizio(userContext));
		es.setCdr(cdr);
		try {
			es = (Pdg_esercizioBulk) getHome(userContext,es).findByPrimaryKey(es);
			return(es);
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
	}

	/**
	 * trova le righe con stato UGUALE a quello del parametro
	 */
	private java.util.List findPdgModulo(UserContext userContext, CdrBulk cdr, String stato ) throws ComponentException, PersistencyException, BusyResourceException, OutdatedResourceException {

		BulkHome home = getHome(userContext,Pdg_moduloBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","STATO",sql.EQUALS,stato);
		java.util.List moduli_list = getHome(userContext,Pdg_moduloBulk.class).fetchAll(sql);
		return moduli_list;
	}

	/**
	 * trova le righe con stato DIVERSO da quello del parametro
	 */
	private java.util.List findPdgModuloNE(UserContext userContext, CdrBulk cdr, String stato ) throws ComponentException, PersistencyException, BusyResourceException, OutdatedResourceException {

		BulkHome home = getHome(userContext,Pdg_moduloBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdr.getCd_centro_responsabilita());
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","STATO",sql.NOT_EQUALS,stato);
		java.util.List moduli_list = getHome(userContext,Pdg_moduloBulk.class).fetchAll(sql);
		return moduli_list;
	}

	public void validaCancellazionePdgModulo(UserContext userContext, Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		if (isModuloInCostiDipendentiLA(userContext, pdg_modulo))
			throw new ComponentException("La riga del PdGP non può essere eliminata perchè utilizzata nella ripartizione del personale.");
		if (isPdgModuloInFigli(userContext, pdg_modulo))
			throw new ComponentException("La riga del PdGP non può essere eliminata perchè utilizzata nelle tabelle del PdGP.");		
	}

	private boolean isModuloInCostiDipendentiLA(UserContext userContext, Pdg_moduloBulk pdg_modulo) throws it.cnr.jada.comp.ComponentException {
		if (!pdg_modulo.isToBeDeleted())
			return false;
			
		try {
			CostiDipendenteComponentSession costiDip = (CostiDipendenteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession",CostiDipendenteComponentSession.class);
					
			if (!costiDip.isModuloUtilizzato(userContext, pdg_modulo.getCdr(), pdg_modulo.getProgetto()))
				return true;
		
			return false;
				
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	private boolean isPdgModuloInFigli(UserContext userContext, Pdg_moduloBulk pdg)  throws it.cnr.jada.comp.ComponentException {
		if (pdg.isToBeCreated())
			return false;

		try {
				Pdg_modulo_speseHome home = (Pdg_modulo_speseHome)getHome(userContext,Pdg_modulo_speseBulk.class);
				SQLBuilder sql = home.createSQLBuilder();
				sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
				sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
				sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
				java.util.List list = getHome(userContext,Pdg_modulo_speseBulk.class).fetchAll(sql);
				
				if (!list.isEmpty())
					return true;
				
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}

		try {
			Pdg_modulo_costiHome home = (Pdg_modulo_costiHome)getHome(userContext,Pdg_modulo_costiBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
			java.util.List list = getHome(userContext,Pdg_modulo_costiBulk.class).fetchAll(sql);
			
			if (!list.isEmpty())
				return true;
				
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}

		try {
			Pdg_Modulo_EntrateHome home = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
			java.util.List list = getHome(userContext,Pdg_Modulo_EntrateBulk.class).fetchAll(sql);
			
			if (!list.isEmpty())
				return true;
				
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}

		return false;
	}

	private void controllaStatoModuloAttivita(UserContext userContext, Pdg_moduloBulk pdg) throws it.cnr.jada.comp.ComponentException {
		try {
			Progetto_sipHome homeSpese = (Progetto_sipHome)getHome(userContext,Progetto_sipBulk.class);
			Progetto_sipBulk modAtt = new Progetto_sipBulk(pdg.getEsercizio(),pdg.getPg_progetto(),ProgettoBulk.TIPO_FASE_PREVISIONE);
			modAtt = (Progetto_sipBulk) homeSpese.findByPrimaryKey(modAtt);
			
			if (modAtt==null)
				throw new ApplicationException("Non è stato trovata la riga corrispondente nella tabella dei moduli di attività. Impossibile procedere.");

			if (!modAtt.getStato().equals(ProgettoBulk.TIPO_STATO_APPROVATO))
				throw new ApplicationException("Impossibile modificare lo STATO perchè il modulo non è stato Approvato in fase in contrattazione.");

		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
	}

	private void controllaAdeguamentoContrattazione(UserContext userContext, Pdg_moduloBulk pdg) throws it.cnr.jada.comp.ComponentException {
		try {
			// controlliamo per le spese
			
			// otteniamo la testata con tutti i dettagli della contrattazione valorizzati
			Pdg_modulo_costiHome homeSpese = (Pdg_modulo_costiHome)getHome(userContext,Pdg_modulo_costiBulk.class);
			Pdg_modulo_costiBulk modCosti = new Pdg_modulo_costiBulk(pdg.getEsercizio(),pdg.getCdr().getCd_centro_responsabilita(),pdg.getProgetto().getPg_progetto());
			modCosti.setDettagliContrSpese(new it.cnr.jada.bulk.BulkList(homeSpese.findPdgModuloContrSpeseDettagli(userContext, modCosti)));

			Pdg_contrattazione_speseHome pdgContrHome = (Pdg_contrattazione_speseHome)getHome(userContext, Pdg_contrattazione_speseBulk.class);
			for(Iterator dettagli = modCosti.getDettagliContrSpese().iterator(); dettagli.hasNext();){
				Pdg_contrattazione_speseBulk pdg_contrattazione_spese = (Pdg_contrattazione_speseBulk)dettagli.next();
				pdg_contrattazione_spese.setTotalePropostoModificatoFE(calcolaImporto(userContext, 
					pdgContrHome.calcolaTotalePropostoModificatoFE(userContext,pdg_contrattazione_spese,pdg_contrattazione_spese.getArea())));
				pdg_contrattazione_spese.setTotalePropostoModificatoFI(calcolaImporto(userContext, 
					pdgContrHome.calcolaTotalePropostoModificatoFI(userContext,pdg_contrattazione_spese,pdg_contrattazione_spese.getArea())));
			}

			for(Iterator dettagli = modCosti.getDettagliContrSpese().iterator(); dettagli.hasNext();){
				Pdg_contrattazione_speseBulk pdg_contrattazione_spese = (Pdg_contrattazione_speseBulk)dettagli.next();
				if (pdg_contrattazione_spese.getDifferenzaFE().compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("Il totale proposto modificato per le fonti esterne non è uguale al totale approvato. Impossibile procedere.");
				if (pdg_contrattazione_spese.getDifferenzaFI().compareTo(Utility.ZERO)!=0)
					throw new ApplicationException("Il totale proposto modificato per le fonti interne non è uguale al totale approvato. Impossibile procedere.");
			}

			// controlliamo per le entrate
			Pdg_Modulo_EntrateHome home = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
			java.util.List list = getHome(userContext,Pdg_Modulo_EntrateBulk.class).fetchAll(sql);
			
			for(Iterator dettagli = list.iterator(); dettagli.hasNext();){
				Pdg_Modulo_EntrateBulk entrata = (Pdg_Modulo_EntrateBulk)dettagli.next();
				if (Utility.nvl(entrata.getIm_entrata()).compareTo(Utility.nvl(entrata.getIm_entrata_app()))!=0)
					throw new ApplicationException("L'importo delle entrate proposto modificato non è uguale all'importo approvato. Impossibile procedere.");
			}

		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IntrospectionException e) {
			throw handleException(e);
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

	private void controllaRipartizioneDecisionale(UserContext userContext, Pdg_moduloBulk pdg) throws it.cnr.jada.comp.ComponentException {

		try {
			SQLBuilder sql;
			
			V_cons_pdgp_pdgg_etrHome home_etr = (V_cons_pdgp_pdgg_etrHome)getHome(userContext,V_cons_pdgp_pdgg_etrBulk.class);
			sql = home_etr.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
			sql.addSQLClause("AND","IM_ENTRATA_DA_RIP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			java.util.List list_etr = getHome(userContext,V_cons_pdgp_pdgg_etrBulk.class).fetchAll(sql);

			if (!list_etr.isEmpty())
				throw new ApplicationException("Impossibile modificare lo STATO perchè le somme derivanti dal PdG decisionale per le entrate non sono state completamente ripartite.");

			V_cons_pdgp_pdgg_speHome home_spe = (V_cons_pdgp_pdgg_speHome)getHome(userContext,V_cons_pdgp_pdgg_speBulk.class);
			sql = home_spe.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,pdg.getEsercizio());
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,pdg.getCd_centro_responsabilita());
			sql.addSQLClause("AND","PG_PROGETTO",SQLBuilder.EQUALS,pdg.getPg_progetto());
			sql.addSQLClause("AND","PG_PROGETTO NOT IN "+
				"(select PG_PROGETTO from ass_cdp_pdgp "+
				"where V_cons_pdgp_pdgg_spe.ESERCIZIO = ass_cdp_pdgp.ESERCIZIO "+
				"and V_cons_pdgp_pdgg_spe.CD_CENTRO_RESPONSABILITA = ass_cdp_pdgp.CD_CENTRO_RESPONSABILITA "+
				"and V_cons_pdgp_pdgg_spe.PG_PROGETTO = ass_cdp_pdgp.PG_PROGETTO_COSTI " +
				"and V_cons_pdgp_pdgg_spe.id_classificazione = ass_cdp_pdgp.id_classificazione) ");
			sql.openParenthesis("AND");
			sql.addSQLClause("OR","IM_SPESE_GEST_DEC_INT_DA_RIP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			sql.addSQLClause("OR","IM_SPESE_GEST_DEC_EST_DA_RIP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			sql.addSQLClause("OR","IM_SPESE_GEST_ACC_INT_DA_RIP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			sql.addSQLClause("OR","IM_SPESE_GEST_ACC_EST_DA_RIP",SQLBuilder.NOT_EQUALS,Utility.ZERO);
			sql.closeParenthesis();
			java.util.List list_spe = getHome(userContext,V_cons_pdgp_pdgg_speBulk.class).fetchAll(sql);

			if (!list_spe.isEmpty())
				throw new ApplicationException("Impossibile modificare lo STATO perchè le somme derivanti dal PdG decisionale per le spese non sono state completamente ripartite.");

		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (ComponentException e) {
			throw handleException(e);
		}
	}

	@Override
	public OggettoBulk inizializzaBulkPerStampa(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		return oggettobulk;
	}

	@Override
	public OggettoBulk stampaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		return oggettobulk;
	}
	
	public void stampaBilancioCallAggiornaDati(UserContext userContext, Stampa_pdgp_bilancioBulk bulk, 
			boolean aggPrevAC, boolean aggResiduiAC, boolean aggPrevAP, boolean aggResiduiAP, boolean aggCassaAC) throws it.cnr.jada.comp.ComponentException {
		try
		{
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
				"call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"PRC_LOAD_TABLE_STAMPA_BILANCIO(?, ?, ?, ?, ?, ?, ?, ?)",false,this.getClass());
				try
				{
					cs.setInt( 1, bulk.getEsercizio().intValue());
					cs.setString( 2, aggPrevAC?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 3, aggResiduiAC?String.valueOf("Y"):String.valueOf("N"));
					cs.setString( 4, aggPrevAP?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 5, aggResiduiAP?String.valueOf("Y"):String.valueOf("N"));
					cs.setString( 6, aggCassaAC?String.valueOf("Y"):String.valueOf("N"));
					cs.setInt( 7, bulk.getPercCassa()!=null?bulk.getPercCassa():Integer.valueOf(0).intValue());
					cs.setString( 8, userContext.getUser());

					cs.executeQuery();
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

	public void stampaRendicontoCallAggiornaDati(UserContext userContext, Stampa_pdgp_bilancioBulk bulk, boolean aggCompAC, boolean aggResiduiAC, boolean aggCassaAC, boolean aggCompAP, boolean aggResiduiAP, boolean aggCassaAP) throws it.cnr.jada.comp.ComponentException {
		try
		{
			LoggableStatement cs = new LoggableStatement(getConnection( userContext ), 
				"call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +			
				"PRC_LOAD_TABLE_STAMPA_RENDIC(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",false,this.getClass());
				try
				{
					cs.setInt( 1, bulk.getEsercizio().intValue());
					cs.setString( 2, null);
					cs.setInt( 3, Integer.valueOf(0));
					cs.setString( 4, null);
					cs.setString( 5, null);
					cs.setString( 6, null);
					cs.setString( 7, "N");		
					cs.setString( 8, aggCompAC?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 9, aggResiduiAC?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 10, aggCassaAC?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 11, aggCompAP?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 12, aggResiduiAP?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 13, aggCassaAP?String.valueOf("Y"):String.valueOf("N"));		
					cs.setString( 14, userContext.getUser());

					cs.executeQuery();
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
}
