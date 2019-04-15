package it.cnr.contab.progettiric00.comp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_ppeBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_voceBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class RimodulaProgettoRicercaComponent extends it.cnr.jada.comp.CRUDComponent {

	/**
	 * RimodulaProgettoRicercaComponent constructor comment.
	 */
	public RimodulaProgettoRicercaComponent() {
			super();
	}
	
	public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		SQLBuilder sqlExist = selectProgettoByClause(userContext, bulk, null, null);
		sqlExist.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO", "PROGETTO_RIMODULAZIONE.PG_PROGETTO"); 
        
		sql.addSQLExistsClause(FindClause.AND, sqlExist);
		return sql;
	}
	
	public SQLBuilder selectProgettoByClause (UserContext userContext, OggettoBulk bulk, ProgettoBulk progetto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
	   ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	   SQLBuilder sql = progettohome.createSQLBuilder();
	   sql.addClause(clauses);
	   sql.addClause(bulk.buildFindClauses(new Boolean(true)));
	   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
	   sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.LIVELLO", SQLBuilder.EQUALS,ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	   sql.addClause(FindClause.AND,"tipo_fase",SQLBuilder.EQUALS,ProgettoBulk.TIPO_FASE_NON_DEFINITA);

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
    
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)	throws ComponentException {
		try {
			Progetto_rimodulazioneBulk testata = (Progetto_rimodulazioneBulk)super.inizializzaBulkPerModifica(usercontext,oggettobulk);
			Progetto_rimodulazioneHome testataHome = (Progetto_rimodulazioneHome)getHome(usercontext, Progetto_rimodulazioneBulk.class);
			testata.setDettagliRimodulazione(new BulkList<Progetto_rimodulazione_ppeBulk>(testataHome.findDettagliRimodulazione(usercontext,testata)));
			testata.setDettagliVoceRimodulazione(new BulkList<Progetto_rimodulazione_voceBulk>(testataHome.findDettagliVoceRimodulazione(usercontext,testata)));
			getHomeCache(usercontext).fetchAll(usercontext);
			return testata;
		} catch(Exception e) {
			throw handleException(e);
		}			
	}
	
	@Override
	public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
	
			Progetto_rimodulazioneHome prgRimodulazioneHome = (Progetto_rimodulazioneHome)getHome(usercontext, Progetto_rimodulazioneBulk.class);
			BulkList<Progetto_rimodulazioneBulk> listRimodulazioni = new BulkList(prgRimodulazioneHome.findRimodulazioni(usercontext, progettoRimodulazione.getPg_progetto()));
			listRimodulazioni.stream().filter(el->!el.isStatoApprovato())
				.findFirst().ifPresent(el->{
					throw new ApplicationRuntimeException("Attenzione! La rimodulazione n."+el.getPg_rimodulazione()+" del progetto "
							+ progettoRimodulazione.getProgetto().getCd_progetto() +" non risulta ancora in stato approvato."
							+ " Operazione non possibile.");
				});
			progettoRimodulazione.setPg_rimodulazione(listRimodulazioni.stream().mapToInt(Progetto_rimodulazioneBulk::getPg_rimodulazione).max().orElse(1));
			progettoRimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_PROVVISORIO);
			progettoRimodulazione.setImVarFinanziato(progettoRimodulazione.getImFinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImFinanziato()));
			progettoRimodulazione.setImVarCofinanziato(progettoRimodulazione.getImCofinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImCofinanziato()));
			
			//Individuo le righe di variazione da creare
			progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
				.filter(Progetto_piano_economicoBulk::isDetailRimodulato)
				.forEach(ppe->{
					Progetto_rimodulazione_ppeBulk detail = new Progetto_rimodulazione_ppeBulk();
					detail.setVocePianoEconomico(ppe.getVoce_piano_economico());
					detail.setEsercizio_piano(ppe.getEsercizio_piano());
					detail.setImVarEntrata(BigDecimal.ZERO);
					detail.setImVarSpesaFinanziato(ppe.getImSpesaFinanziatoRimodulato().subtract(ppe.getIm_spesa_finanziato()));
					detail.setImVarSpesaCofinanziato(ppe.getImSpesaCofinanziatoRimodulato().subtract(ppe.getIm_spesa_cofinanziato()));
					detail.setToBeCreated();
					progettoRimodulazione.addToDettagliRimodulazione(detail);
				});
			
			//Aggiorno associazione voci di bilancio
			progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
				.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
				.filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulato)
				.forEach(ppeVoce->{
					Progetto_rimodulazione_voceBulk rimVoce = new Progetto_rimodulazione_voceBulk();
					rimVoce.setVocePianoEconomico(ppeVoce.getProgetto_piano_economico().getVoce_piano_economico());
					rimVoce.setEsercizio_piano(ppeVoce.getEsercizio_piano());
					rimVoce.setElementoVoce(ppeVoce.getElemento_voce());
					rimVoce.setImVarSpesaFinanziato(BigDecimal.ZERO);
					rimVoce.setImVarSpesaCofinanziato(BigDecimal.ZERO);
					if (ppeVoce.isDetailRimodulatoEliminato())
						rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_ELIMINATO);
					else {
						rimVoce.setImVarSpesaFinanziato(ppeVoce.getImVarFinanziatoRimodulato());
						rimVoce.setImVarSpesaCofinanziato(ppeVoce.getImVarCofinanziatoRimodulato());
						if (ppeVoce.isDetailRimodulatoAggiunto())
							rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_AGGIUNTO);
						else 
							rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_MODIFICA);
					}							
					rimVoce.setPg_variazione(progettoRimodulazione.getDettagliVoceRimodulazione().stream()
							.mapToInt(Progetto_rimodulazione_voceBulk::getPg_variazione)
							.max().orElse(0)+1);
					rimVoce.setToBeCreated();
					progettoRimodulazione.addToDettagliVoceRimodulazione(rimVoce);
				});
			
			if (progettoRimodulazione.getDettagliRimodulazione().isEmpty() && progettoRimodulazione.getDettagliVoceRimodulazione().isEmpty())
				throw new ApplicationException("Salvataggio non consentito. Non risulta alcuna variazione sul piano economico.");
			return super.creaConBulk(usercontext, oggettobulk);
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}
	
	@Override
	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		//Individuo le righe di variazione da creare/aggiornare
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		progettoRimodulazione.setImVarFinanziato(progettoRimodulazione.getImFinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImFinanziato()));
		progettoRimodulazione.setImVarCofinanziato(progettoRimodulazione.getImCofinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImCofinanziato()));

		progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.filter(Progetto_piano_economicoBulk::isDetailRimodulato)
			.forEach(ppe->{
				Progetto_rimodulazione_ppeBulk detail = 
						progettoRimodulazione.getDettagliRimodulazione().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(ppe.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(ppe.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(ppe.getEsercizio_piano()))
							.findFirst().orElseGet(()->{
								Progetto_rimodulazione_ppeBulk newDetail = new Progetto_rimodulazione_ppeBulk();
								newDetail.setProgettoRimodulazione(progettoRimodulazione);
								newDetail.setVocePianoEconomico(ppe.getVoce_piano_economico());
								newDetail.setEsercizio_piano(ppe.getEsercizio_piano());
								newDetail.setToBeCreated();
								progettoRimodulazione.addToDettagliRimodulazione(newDetail);
								return newDetail;
							});
				detail.setImVarEntrata(BigDecimal.ZERO);
				detail.setImVarSpesaFinanziato(ppe.getImSpesaFinanziatoRimodulato().subtract(ppe.getIm_spesa_finanziato()));
				detail.setImVarSpesaCofinanziato(ppe.getImSpesaCofinanziatoRimodulato().subtract(ppe.getIm_spesa_cofinanziato()));
				detail.setToBeUpdated();
			});

		//Aggiorno associazione voci di bilancio
		progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
			.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
			.filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulato)
			.forEach(ppeVoce->{
				Progetto_rimodulazione_voceBulk rimVoce =
						progettoRimodulazione.getDettagliVoceRimodulazione().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(ppeVoce.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(ppeVoce.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(ppeVoce.getEsercizio_piano()))
							.filter(el->el.getEsercizio_voce().equals(ppeVoce.getEsercizio_voce()))
							.filter(el->el.getTi_appartenenza().equals(ppeVoce.getTi_appartenenza()))
							.filter(el->el.getTi_gestione().equals(ppeVoce.getTi_gestione()))
							.filter(el->el.getCd_elemento_voce().equals(ppeVoce.getCd_elemento_voce()))
							.findFirst().orElseGet(()->{
								Progetto_rimodulazione_voceBulk newRimVoce = new Progetto_rimodulazione_voceBulk();
								newRimVoce.setVocePianoEconomico(ppeVoce.getProgetto_piano_economico().getVoce_piano_economico());
								newRimVoce.setEsercizio_piano(ppeVoce.getEsercizio_piano());
								newRimVoce.setElementoVoce(ppeVoce.getElemento_voce());
								newRimVoce.setImVarSpesaFinanziato(BigDecimal.ZERO);
								newRimVoce.setImVarSpesaCofinanziato(BigDecimal.ZERO);
								newRimVoce.setPg_variazione(progettoRimodulazione.getDettagliVoceRimodulazione().stream()
										.mapToInt(Progetto_rimodulazione_voceBulk::getPg_variazione)
										.max().orElse(0)+1);
								newRimVoce.setToBeCreated();
								progettoRimodulazione.addToDettagliVoceRimodulazione(newRimVoce);
								return newRimVoce;
							});
				if (ppeVoce.isDetailRimodulatoEliminato()) {
					rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_ELIMINATO);
					rimVoce.setImVarSpesaFinanziato(BigDecimal.ZERO);
					rimVoce.setImVarSpesaCofinanziato(BigDecimal.ZERO);
				} else {
					rimVoce.setImVarSpesaFinanziato(Optional.ofNullable(ppeVoce.getImVarFinanziatoRimodulato()).orElse(BigDecimal.ZERO));
					rimVoce.setImVarSpesaCofinanziato(Optional.ofNullable(ppeVoce.getImVarCofinanziatoRimodulato()).orElse(BigDecimal.ZERO));
					if (ppeVoce.isDetailRimodulatoAggiunto())
						rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_AGGIUNTO);
					else 
						rimVoce.setTi_operazione(Progetto_rimodulazione_voceBulk.TIPO_OPERAZIONE_MODIFICA);
				}							
				rimVoce.setToBeUpdated();
			});				

		//Rimuovo i dettagli non utilizzati
		List<Progetto_rimodulazione_ppeBulk> bulkToDelete = 
				progettoRimodulazione.getDettagliRimodulazione().stream()
					.filter(rim->!progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
									.filter(Progetto_piano_economicoBulk::isDetailRimodulato)
									.filter(ppe->ppe.getCd_unita_organizzativa().equals(rim.getCd_unita_organizzativa()))
									.filter(ppe->ppe.getCd_voce_piano().equals(rim.getCd_voce_piano()))
									.filter(ppe->ppe.getEsercizio_piano().equals(rim.getEsercizio_piano()))
									.findFirst().isPresent())
					.collect(Collectors.toList());

		bulkToDelete.stream().forEach(el->{
			el.setToBeDeleted();
			progettoRimodulazione.removeFromDettagliRimodulazione(progettoRimodulazione.getDettagliRimodulazione().indexOf(el));
		});

		//Rimuovo i dettagli voce non utilizzati
		List<Progetto_rimodulazione_voceBulk> bulkVoceToDelete = 
				progettoRimodulazione.getDettagliVoceRimodulazione().stream()
					.filter(rim->!progettoRimodulazione.getAllDetailsProgettoPianoEconomico().stream()
									.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
									.filter(Ass_progetto_piaeco_voceBulk::isDetailRimodulato)
									.filter(ppeVoce->ppeVoce.getCd_unita_organizzativa().equals(rim.getCd_unita_organizzativa()))
									.filter(ppeVoce->ppeVoce.getCd_voce_piano().equals(rim.getCd_voce_piano()))
									.filter(ppeVoce->ppeVoce.getEsercizio_piano().equals(rim.getEsercizio_piano()))
									.filter(ppeVoce->ppeVoce.getEsercizio_voce().equals(rim.getEsercizio_voce()))
									.filter(ppeVoce->ppeVoce.getTi_appartenenza().equals(rim.getTi_appartenenza()))
									.filter(ppeVoce->ppeVoce.getTi_gestione().equals(rim.getTi_gestione()))
									.filter(ppeVoce->ppeVoce.getCd_elemento_voce().equals(rim.getCd_elemento_voce()))
									.findFirst().isPresent())
					.collect(Collectors.toList());

		bulkVoceToDelete.stream().forEach(el->{
			el.setToBeDeleted();
			progettoRimodulazione.removeFromDettagliVoceRimodulazione(progettoRimodulazione.getDettagliVoceRimodulazione().indexOf(el));
		});

		if (progettoRimodulazione.getDettagliRimodulazione().isEmpty() && progettoRimodulazione.getDettagliVoceRimodulazione().isEmpty())
			throw new ApplicationException("Salvataggio non consentito. Non risulta alcuna variazione sul piano economico.");
		return super.modificaConBulk(usercontext, oggettobulk);
	}
	
	@Override
	protected void validaCreaModificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.validaCreaModificaConBulk(usercontext, oggettobulk);

		Optional<Progetto_rimodulazioneBulk> optProgettoRimodulato = Optional.ofNullable(oggettobulk).filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast);
		Optional<ProgettoBulk> optProgetto = optProgettoRimodulato.map(Progetto_rimodulazioneBulk::getProgetto);
		if (optProgetto.get().isPianoEconomicoRequired() && !optProgettoRimodulato.get().isStatoProvvisorio()) {
			if (!optProgettoRimodulato.map(Progetto_rimodulazioneBulk::getImTotaleRimodulato).filter(el->el.compareTo(BigDecimal.ZERO)>0).isPresent())
				throw new ApplicationException("Operazione non possibile! Indicare almeno un importo rimodulato positivo tra quello finanziato e cofinanziato!");
			if (!optProgettoRimodulato.map(Progetto_rimodulazioneBulk::getImFinanziatoRimodulatoDaRipartire).filter(el->el.compareTo(BigDecimal.ZERO)==0).isPresent())
				throw new ApplicationException("Operazione non possibile! E' obbligatorio ripartire correttamente nel piano economico tutto l'importo finanziato rimodulato del progetto!");
			if (!optProgettoRimodulato.map(Progetto_rimodulazioneBulk::getImCofinanziatoRimodulatoDaRipartire).filter(el->el.compareTo(BigDecimal.ZERO)==0).isPresent())
				throw new ApplicationException("Operazione non possibile! E' obbligatorio ripartire correttamente nel piano economico tutto l'importo cofinanziato rimodulato del progetto!");
		}
	}
}
