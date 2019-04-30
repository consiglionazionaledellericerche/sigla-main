package it.cnr.contab.progettiric00.comp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import it.cnr.contab.util.Utility;
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
	private static final long serialVersionUID = 1L;

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
			BulkList<Progetto_rimodulazioneBulk> listRimodulazioni = new BulkList<Progetto_rimodulazioneBulk>(prgRimodulazioneHome.findRimodulazioni(usercontext, progettoRimodulazione.getPg_progetto()));
			listRimodulazioni.stream()
				.filter(el->!el.equalsByPrimaryKey(this))
				.filter(el->!el.isStatoApprovato())
				.findFirst().ifPresent(el->{
					throw new ApplicationRuntimeException("Attenzione! La rimodulazione n."+el.getPg_rimodulazione()+" del progetto "
							+ progettoRimodulazione.getProgetto().getCd_progetto() +" non risulta ancora in stato approvato."
							+ " Operazione non possibile.");
				});
			progettoRimodulazione.setPg_rimodulazione(listRimodulazioni.stream().mapToInt(Progetto_rimodulazioneBulk::getPg_rimodulazione).max().orElse(0)+1);
			progettoRimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_PROVVISORIO);
			progettoRimodulazione.setImVarFinanziato(progettoRimodulazione.getImFinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImFinanziato()));
			progettoRimodulazione.setImVarCofinanziato(progettoRimodulazione.getImCofinanziatoRimodulato().subtract(progettoRimodulazione.getProgetto().getImCofinanziato()));

		    if (progettoRimodulazione.isRimodulatoDtInizio()) {
	    		progettoRimodulazione.setDtInizioOld(progettoRimodulazione.getProgetto().getOtherField().getDtInizio());
	    		progettoRimodulazione.setDtInizio(progettoRimodulazione.getDtInizioRimodulato());
		    }
	    	if (progettoRimodulazione.isRimodulatoDtFine()) {
				progettoRimodulazione.setDtFineOld(progettoRimodulazione.getProgetto().getOtherField().getDtFine());
				progettoRimodulazione.setDtFine(progettoRimodulazione.getDtFineRimodulato());
	    	}
	    	if (progettoRimodulazione.isRimodulatoDtProroga()) {
				progettoRimodulazione.setDtProrogaOld(progettoRimodulazione.getProgetto().getOtherField().getDtProroga());
				progettoRimodulazione.setDtProroga(progettoRimodulazione.getDtProrogaRimodulato());
	    	}
	    	
		    Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHome(usercontext, Progetto_rimodulazioneBulk.class);
		    List<Progetto_rimodulazione_ppeBulk> listRim = rimodHome.getDettagliRimodulazioneAggiornato(usercontext, progettoRimodulazione);

		    //Individuo le righe di variazione da creare
		    listRim.stream()
				.forEach(newDett->{
					newDett.setToBeCreated();
					progettoRimodulazione.addToDettagliRimodulazione(newDett);
				});

		    List<Progetto_rimodulazione_voceBulk> listRimVoce = rimodHome.getDettagliRimodulazioneVoceAggiornato(usercontext, progettoRimodulazione);

			//Aggiorno associazione voci di bilancio
		    listRimVoce.stream()
		    	.forEach(rimVoce->{
		    		rimVoce.setPg_variazione(progettoRimodulazione.getDettagliVoceRimodulazione().stream()
							.mapToInt(Progetto_rimodulazione_voceBulk::getPg_variazione)
							.max().orElse(0)+1);
					rimVoce.setToBeCreated();
					progettoRimodulazione.addToDettagliVoceRimodulazione(rimVoce);
				});
			
			if (progettoRimodulazione.getDettagliRimodulazione().isEmpty() && progettoRimodulazione.getDettagliVoceRimodulazione().isEmpty() &&
				progettoRimodulazione.getImFinanziatoRimodulato().compareTo(progettoRimodulazione.getProgetto().getImFinanziato())==0 && 
				progettoRimodulazione.getImCofinanziatoRimodulato().compareTo(progettoRimodulazione.getProgetto().getImCofinanziato())==0 && 
			    (!progettoRimodulazione.getProgetto().isDatePianoEconomicoRequired() ||
			     (progettoRimodulazione.getDtInizioRimodulato().compareTo(progettoRimodulazione.getProgetto().getOtherField().getDtInizio())==0 &&
			      progettoRimodulazione.getDtFineRimodulato().compareTo(progettoRimodulazione.getProgetto().getOtherField().getDtFine())==0)))
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
	    if (progettoRimodulazione.isRimodulatoDtInizio()) {
    		progettoRimodulazione.setDtInizioOld(progettoRimodulazione.getProgetto().getOtherField().getDtInizio());
    		progettoRimodulazione.setDtInizio(progettoRimodulazione.getDtInizioRimodulato());
	    } else {
	    	progettoRimodulazione.setDtInizioOld(null);
	    	progettoRimodulazione.setDtInizio(null);
	    }
	    	
	    if (progettoRimodulazione.isRimodulatoDtFine()) {
			progettoRimodulazione.setDtFineOld(progettoRimodulazione.getProgetto().getOtherField().getDtFine());
			progettoRimodulazione.setDtFine(progettoRimodulazione.getDtFineRimodulato());
	    } else {
	    	progettoRimodulazione.setDtFineOld(null);
	    	progettoRimodulazione.setDtFine(null);
	    }
		
	    if (progettoRimodulazione.isRimodulatoDtProroga()) {
			progettoRimodulazione.setDtProrogaOld(progettoRimodulazione.getProgetto().getOtherField().getDtProroga());
			progettoRimodulazione.setDtProroga(progettoRimodulazione.getDtProrogaRimodulato());
	    } else {
	    	progettoRimodulazione.setDtProrogaOld(null);
	    	progettoRimodulazione.setDtProroga(null);
	    }

	    Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHome(usercontext, Progetto_rimodulazioneBulk.class);
	    List<Progetto_rimodulazione_ppeBulk> listRim = rimodHome.getDettagliRimodulazioneAggiornato(usercontext, progettoRimodulazione);

	    //Aggiorno o aggiungo i dettagli presenti
	    listRim.stream()
			.forEach(newDett->{
				Progetto_rimodulazione_ppeBulk detail = 
						progettoRimodulazione.getDettagliRimodulazione().stream()
							.filter(dett->dett.getCd_unita_organizzativa().equals(newDett.getCd_unita_organizzativa()))
							.filter(dett->dett.getCd_voce_piano().equals(newDett.getCd_voce_piano()))
							.filter(dett->dett.getEsercizio_piano().equals(newDett.getEsercizio_piano()))
							.findFirst().orElseGet(()->{
								newDett.setToBeCreated();
								progettoRimodulazione.addToDettagliRimodulazione(newDett);
								return newDett;
							});
				detail.setImVarEntrata(BigDecimal.ZERO);
				detail.setImVarSpesaFinanziato(newDett.getImVarSpesaFinanziato());
				detail.setImVarSpesaCofinanziato(newDett.getImVarSpesaCofinanziato());
				detail.setToBeUpdated();
			});

		//Rimuovo i dettagli non presenti nella lista
		List<Progetto_rimodulazione_ppeBulk> bulkToDelete = 
				progettoRimodulazione.getDettagliRimodulazione().stream()
					.filter(rim->!listRim.stream()
									.filter(newDett->newDett.getCd_unita_organizzativa().equals(rim.getCd_unita_organizzativa()))
									.filter(newDett->newDett.getCd_voce_piano().equals(rim.getCd_voce_piano()))
									.filter(newDett->newDett.getEsercizio_piano().equals(rim.getEsercizio_piano()))
									.findFirst().isPresent())
					.collect(Collectors.toList());

		bulkToDelete.stream().forEach(el->{
			el.setToBeDeleted();
			progettoRimodulazione.removeFromDettagliRimodulazione(progettoRimodulazione.getDettagliRimodulazione().indexOf(el));
		});

	    List<Progetto_rimodulazione_voceBulk> listRimVoce = rimodHome.getDettagliRimodulazioneVoceAggiornato(usercontext, progettoRimodulazione);

	    //Aggiorno o aggiungo le voci presenti
	    listRimVoce.stream()
			.forEach(newDettVoce->{
				Progetto_rimodulazione_voceBulk detail = 
						progettoRimodulazione.getDettagliVoceRimodulazione().stream()
							.filter(el->el.getCd_unita_organizzativa().equals(newDettVoce.getCd_unita_organizzativa()))
							.filter(el->el.getCd_voce_piano().equals(newDettVoce.getCd_voce_piano()))
							.filter(el->el.getEsercizio_piano().equals(newDettVoce.getEsercizio_piano()))
							.filter(el->el.getEsercizio_voce().equals(newDettVoce.getEsercizio_voce()))
							.filter(el->el.getTi_appartenenza().equals(newDettVoce.getTi_appartenenza()))
							.filter(el->el.getTi_gestione().equals(newDettVoce.getTi_gestione()))
							.filter(el->el.getCd_elemento_voce().equals(newDettVoce.getCd_elemento_voce()))
							.findFirst().orElseGet(()->{
								newDettVoce.setPg_variazione(progettoRimodulazione.getDettagliVoceRimodulazione().stream()
										.mapToInt(Progetto_rimodulazione_voceBulk::getPg_variazione)
										.max().orElse(0)+1);								
								newDettVoce.setToBeCreated();
								progettoRimodulazione.addToDettagliVoceRimodulazione(newDettVoce);
								return newDettVoce;
							});
				detail.setTi_operazione(newDettVoce.getTi_operazione());
				detail.setImVarSpesaFinanziato(newDettVoce.getImVarSpesaFinanziato());
				detail.setImVarSpesaCofinanziato(newDettVoce.getImVarSpesaCofinanziato());
				detail.setToBeUpdated();
			});

		//Rimuovo i dettagli voce non presenti nella lista
		List<Progetto_rimodulazione_voceBulk> bulkVoceToDelete = 
				progettoRimodulazione.getDettagliVoceRimodulazione().stream()
					.filter(rim->!listRimVoce.stream()
									.filter(rimVoce->rimVoce.getCd_unita_organizzativa().equals(rim.getCd_unita_organizzativa()))
									.filter(rimVoce->rimVoce.getCd_voce_piano().equals(rim.getCd_voce_piano()))
									.filter(rimVoce->rimVoce.getEsercizio_piano().equals(rim.getEsercizio_piano()))
									.filter(rimVoce->rimVoce.getEsercizio_voce().equals(rim.getEsercizio_voce()))
									.filter(rimVoce->rimVoce.getTi_appartenenza().equals(rim.getTi_appartenenza()))
									.filter(rimVoce->rimVoce.getTi_gestione().equals(rim.getTi_gestione()))
									.filter(rimVoce->rimVoce.getCd_elemento_voce().equals(rim.getCd_elemento_voce()))
									.findFirst().isPresent())
					.collect(Collectors.toList());

		bulkVoceToDelete.stream().forEach(el->{
			el.setToBeDeleted();
			progettoRimodulazione.removeFromDettagliVoceRimodulazione(progettoRimodulazione.getDettagliVoceRimodulazione().indexOf(el));
		});

		if (progettoRimodulazione.getDettagliRimodulazione().isEmpty() && progettoRimodulazione.getDettagliVoceRimodulazione().isEmpty() &&
				progettoRimodulazione.getImFinanziatoRimodulato().compareTo(progettoRimodulazione.getProgetto().getImFinanziato())==0 && 
				progettoRimodulazione.getImCofinanziatoRimodulato().compareTo(progettoRimodulazione.getProgetto().getImCofinanziato())==0 && 
			    (!progettoRimodulazione.getProgetto().isDatePianoEconomicoRequired() ||
			     (progettoRimodulazione.getDtInizioRimodulato().compareTo(progettoRimodulazione.getProgetto().getOtherField().getDtInizio())==0 &&
			      progettoRimodulazione.getDtFineRimodulato().compareTo(progettoRimodulazione.getProgetto().getOtherField().getDtFine())==0)))
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
	
	public Progetto_rimodulazioneBulk approva(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		Optional.of(rimodulazione).filter(Progetto_rimodulazioneBulk::isStatoDefinitivo)
		.orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! Lo stato approvato può essere assegnato solo a rimodulazioni in stato definitivo!"));
	
		return approvaInt(userContext, rimodulazione);
	}

	private Progetto_rimodulazioneBulk approvaInt(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try{
			//Ricostruisco il progetto sulla base della nuova rimodulazione e aggiorno il dato
			//Nell'aggiornamento viene rifatta la validazione
			ProgettoBulk progettoRimodulato = getProgettoRimodulato(rimodulazione);
			Utility.createProgettoRicercaComponentSession().modificaConBulk(userContext, progettoRimodulato);

			rimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_APPROVATO);
			rimodulazione.setToBeUpdated();
			return (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	public Progetto_rimodulazioneBulk respingi(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		Optional.of(rimodulazione).filter(Progetto_rimodulazioneBulk::isStatoDefinitivo)
		.orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! Lo stato respinto può essere assegnato solo a rimodulazioni in stato definitivo!"));

		rimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_RESPINTO);
		rimodulazione.setToBeUpdated();
		return (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
	}

	public Progetto_rimodulazioneBulk salvaDefinitivo(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try{
			//Prima salvo il progetto per aggiornare i dettagli della rimodulazione
			if (rimodulazione.isToBeCreated())
				rimodulazione = (Progetto_rimodulazioneBulk)this.creaConBulk(userContext, rimodulazione);
			else
				rimodulazione = (Progetto_rimodulazioneBulk)this.modificaConBulk(userContext, rimodulazione);
	
			Optional.of(rimodulazione).filter(Progetto_rimodulazioneBulk::isStatoProvvisorio)
			.orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! Lo stato definitivo può essere assegnato solo a rimodulazioni in stato provvisorio!"));
	
		    Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHome(userContext, Progetto_rimodulazioneBulk.class);
		    List<OggettoBulk> listVariazioni = rimodHome.constructVariazioniBilancio(userContext, rimodulazione);

		    //Se la rimodulazione non prevede variazioni procedo direttamente con l'approvazione
		    if (Optional.ofNullable(listVariazioni).map(List::isEmpty).orElse(Boolean.TRUE))
		    	return approvaInt(userContext, rimodulazione);
		    else {
			    //Ricostruisco il progetto sulla base della nuova rimodulazione e rifaccio la validazione
				ProgettoBulk progettoRimodulato = getProgettoRimodulato(rimodulazione);
				Utility.createProgettoRicercaComponentSession().validaPianoEconomico(userContext, progettoRimodulato);
				
				rimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_DEFINITIVO);
				rimodulazione.setToBeUpdated();
				return (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
		    }
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch(Exception e) {
			throw handleException(e);
		}
	}
	
	private ProgettoBulk getProgettoRimodulato(Progetto_rimodulazioneBulk rimodulazione) {
		ProgettoBulk progetto = rimodulazione.getProgetto();
		
		if (rimodulazione.isRimodulatoDtInizio())
			progetto.getOtherField().setDtInizio(rimodulazione.getDtInizioRimodulato());
		if (rimodulazione.isRimodulatoDtFine())
			progetto.getOtherField().setDtFine(rimodulazione.getDtFineRimodulato());
		if (rimodulazione.isRimodulatoImportoFinanziato())
			progetto.getOtherField().setImFinanziato(rimodulazione.getImFinanziatoRimodulato());
		if (rimodulazione.isRimodulatoImportoCofinanziato())
			progetto.getOtherField().setImCofinanziato(rimodulazione.getImCofinanziatoRimodulato());

		progetto.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
		progetto.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
		progetto.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());

		rimodulazione.getDettagliPianoEconomicoAnnoCorrente().stream()
				.filter(ppeRim->!ppeRim.isDetailRimodulatoEliminato())
				.forEach(ppeRim->{
					Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
					newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
					newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
					newPpe.setIm_entrata(ppeRim.getIm_entrata());
					newPpe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					newPpe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());
					
					ppeRim.getVociBilancioAssociate().stream()
					.filter(ppeRimVoc->!ppeRimVoc.isDetailRimodulatoEliminato())
					.forEach(ppeRimVoc->{
						Ass_progetto_piaeco_voceBulk newPpeRimVoc = new Ass_progetto_piaeco_voceBulk();
						newPpeRimVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
						newPpe.addToVociBilancioAssociate(newPpeRimVoc);
					});
					
					progetto.addToDettagliPianoEconomicoAnnoCorrente(newPpe);
				});
		
		rimodulazione.getDettagliPianoEconomicoAltriAnni().stream()
				.filter(ppeRim->!ppeRim.isDetailRimodulatoEliminato())
				.forEach(ppeRim->{
					Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
					newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
					newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
					newPpe.setIm_entrata(ppeRim.getIm_entrata());
					newPpe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					newPpe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());

					ppeRim.getVociBilancioAssociate().stream()
					.filter(ppeRimVoc->!ppeRimVoc.isDetailRimodulatoEliminato())
					.forEach(ppeRimVoc->{
						Ass_progetto_piaeco_voceBulk newPpeRimVoc = new Ass_progetto_piaeco_voceBulk();
						newPpeRimVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
						newPpe.addToVociBilancioAssociate(newPpeRimVoc);
					});
					
					progetto.addToDettagliPianoEconomicoAltriAnni(newPpe);
				});

		rimodulazione.getDettagliPianoEconomicoTotale().stream()
				.filter(ppeRim->!ppeRim.isDetailRimodulatoEliminato())
				.forEach(ppeRim->{
					Progetto_piano_economicoBulk newPpe = new Progetto_piano_economicoBulk();
					newPpe.setVoce_piano_economico(ppeRim.getVoce_piano_economico());
					newPpe.setEsercizio_piano(ppeRim.getEsercizio_piano());
					newPpe.setIm_entrata(ppeRim.getIm_entrata());
					newPpe.setIm_spesa_finanziato(ppeRim.getImSpesaFinanziatoRimodulato());
					newPpe.setIm_spesa_cofinanziato(ppeRim.getImSpesaCofinanziatoRimodulato());

					ppeRim.getVociBilancioAssociate().stream()
					.filter(ppeRimVoc->!ppeRimVoc.isDetailRimodulatoEliminato())
					.forEach(ppeRimVoc->{
						Ass_progetto_piaeco_voceBulk newPpeRimVoc = new Ass_progetto_piaeco_voceBulk();
						newPpeRimVoc.setElemento_voce(ppeRimVoc.getElemento_voce());
						newPpe.addToVociBilancioAssociate(newPpeRimVoc);
					});

					progetto.addToDettagliPianoEconomicoTotale(newPpe);
				});
		return progetto;
	}
}
