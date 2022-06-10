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

package it.cnr.contab.progettiric00.comp;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.GsonBuilder;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazione_wizardBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resHome;
import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoRimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_ppeBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_voceBulk;
import it.cnr.contab.progettiric00.enumeration.AllegatoProgettoRimodulazioneType;
import it.cnr.contab.progettiric00.enumeration.StatoProgettoRimodulazione;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.StoreService;

public class RimodulaProgettoRicercaComponent extends it.cnr.jada.comp.CRUDComponent {
	private static final long serialVersionUID = 1L;

	private class ModelPpeForPrint {
		String cdProgetto;
		String dsProgetto;
		String pgRimodulazione;
		String pgGenRimodulazione;
		String uoCoordinatrice;
		String tipoFinanziamento;
		String dataInizio;
		String dataFine;
		String dataProroga;
		BigDecimal importoFinanziato;
		BigDecimal importoCofinanziato;
		BigDecimal importoFinanziatoRimodulato;
		BigDecimal importoCofinanziatoRimodulato;
		String dataProrogaRimodulata;
		List<ModelPpeAnnoForPrint> anniPianoEconomico;
		public ModelPpeForPrint() {
			super();
		}
		public String getCdProgetto() {
			return cdProgetto;
		}
		public void setCdProgetto(String cdProgetto) {
			this.cdProgetto = cdProgetto;
		}
		public String getDsProgetto() {
			return dsProgetto;
		}
		public void setDsProgetto(String dsProgetto) {
			this.dsProgetto = dsProgetto;
		}
		public String getPgRimodulazione() {
			return pgRimodulazione;
		}
		public void setPgRimodulazione(String pgRimodulazione) {
			this.pgRimodulazione = pgRimodulazione;
		}
		public String getPgGenRimodulazione() {
			return pgGenRimodulazione;
		}
		public void setPgGenRimodulazione(String pgGenRimodulazione) {
			this.pgGenRimodulazione = pgGenRimodulazione;
		}
		public String getUoCoordinatrice() {
			return uoCoordinatrice;
		}
		public void setUoCoordinatrice(String uoCoordinatrice) {
			this.uoCoordinatrice = uoCoordinatrice;
		}
		public String getTipoFinanziamento() {
			return tipoFinanziamento;
		}
		public void setTipoFinanziamento(String tipoFinanziamento) {
			this.tipoFinanziamento = tipoFinanziamento;
		}
		public String getDataInizio() {
			return dataInizio;
		}
		public void setDataInizio(String dataInizio) {
			this.dataInizio = dataInizio;
		}
		public String getDataFine() {
			return dataFine;
		}
		public void setDataFine(String dataFine) {
			this.dataFine = dataFine;
		}
		public String getDataProroga() {
			return dataProroga;
		}
		public void setDataProroga(String dataProroga) {
			this.dataProroga = dataProroga;
		}
		public BigDecimal getImportoFinanziato() {
			return importoFinanziato;
		}
		public void setImportoFinanziato(BigDecimal importoFinanziato) {
			this.importoFinanziato = importoFinanziato;
		}
		public BigDecimal getImportoCofinanziato() {
			return importoCofinanziato;
		}
		public void setImportoCofinanziato(BigDecimal importoCofinanziato) {
			this.importoCofinanziato = importoCofinanziato;
		}
		public BigDecimal getImportoFinanziatoRimodulato() {
			return importoFinanziatoRimodulato;
		}
		public void setImportoFinanziatoRimodulato(BigDecimal importoFinanziatoRimodulato) {
			this.importoFinanziatoRimodulato = importoFinanziatoRimodulato;
		}
		public BigDecimal getImportoCofinanziatoRimodulato() {
			return importoCofinanziatoRimodulato;
		}
		public void setImportoCofinanziatoRimodulato(BigDecimal importoCofinanziatoRimodulato) {
			this.importoCofinanziatoRimodulato = importoCofinanziatoRimodulato;
		}
		public String getDataProrogaRimodulata() {
			return dataProrogaRimodulata;
		}
		public void setDataProrogaRimodulata(String dataProrogaRimodulata) {
			this.dataProrogaRimodulata = dataProrogaRimodulata;
		}
		public List<ModelPpeAnnoForPrint> getAnniPianoEconomico() {
			return anniPianoEconomico;
		}
		public void setAnniPianoEconomico(List<ModelPpeAnnoForPrint> anniPianoEconomico) {
			this.anniPianoEconomico = anniPianoEconomico;
		}
	}
	
	private class ModelPpeAnnoForPrint {
		Integer anno;
		List<ModelPpeVoceForPrint> vociPianoEconomico = new ArrayList<ModelPpeVoceForPrint>();
		public ModelPpeAnnoForPrint() {
			super();
		}
		public Integer getAnno() {
			return anno;
		}
		public void setAnno(Integer anno) {
			this.anno = anno;
		}
		public List<ModelPpeVoceForPrint> getVociPianoEconomico() {
			return vociPianoEconomico;
		}
		public void setVociPianoEconomico(List<ModelPpeVoceForPrint> vociPianoEconomico) {
			this.vociPianoEconomico = vociPianoEconomico;
		}
	}

	private class ModelPpeVoceForPrint {
		String cdVoce;
		String dsVoce;
		BigDecimal quotaFinanziataCorrente;
		BigDecimal quotaFinanziataRimodulata;
		BigDecimal quotaFinanziataStanziata;
		BigDecimal quotaFinanziataPagata;
		BigDecimal quotaCofinanziataCorrente;
		BigDecimal quotaCofinanziataRimodulata;
		BigDecimal quotaCofinanziataStanziata;
		BigDecimal quotaCofinanziataPagata;
		public ModelPpeVoceForPrint() {
			super();
		}
		public String getCdVoce() {
			return cdVoce;
		}
		public void setCdVoce(String cdVoce) {
			this.cdVoce = cdVoce;
		}
		public String getDsVoce() {
			return dsVoce;
		}
		public void setDsVoce(String dsVoce) {
			this.dsVoce = dsVoce;
		}
		public BigDecimal getQuotaFinanziataCorrente() {
			return quotaFinanziataCorrente;
		}
		public void setQuotaFinanziataCorrente(BigDecimal quotaFinanziataCorrente) {
			this.quotaFinanziataCorrente = quotaFinanziataCorrente;
		}
		public BigDecimal getQuotaFinanziataRimodulata() {
			return quotaFinanziataRimodulata;
		}
		public void setQuotaFinanziataRimodulata(BigDecimal quotaFinanziataRimodulata) {
			this.quotaFinanziataRimodulata = quotaFinanziataRimodulata;
		}
		public BigDecimal getQuotaFinanziataStanziata() {
			return quotaFinanziataStanziata;
		}
		public void setQuotaFinanziataStanziata(BigDecimal quotaFinanziataStanziata) {
			this.quotaFinanziataStanziata = quotaFinanziataStanziata;
		}
		public BigDecimal getQuotaFinanziataPagata() {
			return quotaFinanziataPagata;
		}
		public void setQuotaFinanziataPagata(BigDecimal quotaFinanziataPagata) {
			this.quotaFinanziataPagata = quotaFinanziataPagata;
		}
		public BigDecimal getQuotaCofinanziataCorrente() {
			return quotaCofinanziataCorrente;
		}
		public void setQuotaCofinanziataCorrente(BigDecimal quotaCofinanziataCorrente) {
			this.quotaCofinanziataCorrente = quotaCofinanziataCorrente;
		}
		public BigDecimal getQuotaCofinanziataRimodulata() {
			return quotaCofinanziataRimodulata;
		}
		public void setQuotaCofinanziataRimodulata(BigDecimal quotaCofinanziataRimodulata) {
			this.quotaCofinanziataRimodulata = quotaCofinanziataRimodulata;
		}
		public BigDecimal getQuotaCofinanziataStanziata() {
			return quotaCofinanziataStanziata;
		}
		public void setQuotaCofinanziataStanziata(BigDecimal quotaCofinanziataStanziata) {
			this.quotaCofinanziataStanziata = quotaCofinanziataStanziata;
		}
		public BigDecimal getQuotaCofinanziataPagata() {
			return quotaCofinanziataPagata;
		}
		public void setQuotaCofinanziataPagata(BigDecimal quotaCofinanziataPagata) {
			this.quotaCofinanziataPagata = quotaCofinanziataPagata;
		}
	}

		/**
	 * RimodulaProgettoRicercaComponent constructor comment.
	 */
	public RimodulaProgettoRicercaComponent() {
			super();
	}
	
	public Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );

		Optional<String> optCdUo = Optional.ofNullable(bulk).filter(Progetto_rimodulazioneBulk.class::isInstance)
				.map(Progetto_rimodulazioneBulk.class::cast).flatMap(el->Optional.ofNullable(el.getProgetto()))
				.flatMap(el->Optional.ofNullable(el.getCd_unita_organizzativa()));

		Optional<String> optCdUo2 = Optional.empty();

		if (clauses!=null){
			Enumeration<SimpleFindClause> e = clauses.getClauses();
			while(e.hasMoreElements()){
				FindClause findClause = e.nextElement();
				if (findClause instanceof SimpleFindClause)
					if (((SimpleFindClause)findClause).getPropertyName().equals("progetto.unita_organizzativa.cd_unita_organizzativa"))
						optCdUo2 = Optional.of(((SimpleFindClause)findClause).getValue().toString());
			}
		}
		
	    Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		boolean isUoEnte = ((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa());
	    
	    if (optCdUo.isPresent() || optCdUo2.isPresent() || !isUoEnte) {
	 	    sql.addTableToHeader("V_PROGETTO_PADRE");
			sql.addSQLJoin("V_PROGETTO_PADRE.PG_PROGETTO", "PROGETTO_RIMODULAZIONE.PG_PROGETTO");
			sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.TIPO_FASE", SQLBuilder.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
			if (optCdUo.isPresent())
				sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, optCdUo.get());
			if (optCdUo2.isPresent())
				sql.addSQLClause(FindClause.AND, "V_PROGETTO_PADRE.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, optCdUo2.get());
			if (!isUoEnte) {
				try {
			    	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
					sql.addSQLExistsClause(FindClause.AND,progettohome.abilitazioniCommesse(userContext));
				} catch (Exception e) {
					throw handleException(e);
				}
			}
		}
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

			BulkList<Progetto_rimodulazione_variazioneBulk> listVariazioni = new BulkList<Progetto_rimodulazione_variazioneBulk>(
				testataHome.findVariazioniCompetenzaAssociate(testata).stream()
				.map(el->{
					Progetto_rimodulazione_variazioneBulk newVar = new Progetto_rimodulazione_variazioneBulk();
					newVar.setVariazioneCompetenza(el);
					return newVar;
				}).collect(Collectors.toList()));
			
			listVariazioni.addAll(new BulkList<Progetto_rimodulazione_variazioneBulk>(
					testataHome.findVariazioniResidueAssociate(testata).stream()
					.map(el->{
						Progetto_rimodulazione_variazioneBulk newVar = new Progetto_rimodulazione_variazioneBulk();
						newVar.setVariazioneResiduo(el);
						return newVar;
					}).collect(Collectors.toList())));
			testata.setVariazioniAssociate(listVariazioni);

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
			ProgettoHome prgHome = (ProgettoHome)getHome(usercontext, ProgettoBulk.class);
			BulkList<Progetto_rimodulazioneBulk> listRimodulazioni = new BulkList<Progetto_rimodulazioneBulk>(prgHome.findRimodulazioni(progettoRimodulazione.getPg_progetto()));
			listRimodulazioni.stream()
				.filter(el->!el.equalsByPrimaryKey(this))
				.filter(el->!el.isStatoRespinto())
				.filter(el->!el.isStatoApprovato())
				.findFirst().ifPresent(el->{
					throw new ApplicationRuntimeException("Attenzione! La rimodulazione n."+el.getPg_rimodulazione()+" del progetto "
							+ progettoRimodulazione.getProgetto().getCd_progetto() +" non risulta ancora in stato approvato."
							+ " Operazione non possibile.");
				});
			progettoRimodulazione.setPg_rimodulazione(listRimodulazioni.stream().mapToInt(Progetto_rimodulazioneBulk::getPg_rimodulazione).max().orElse(0)+1);
			progettoRimodulazione.setPg_gen_rimodulazione(
				new Integer(((Integer)prgRimodulazioneHome.findAndLockMax( new Progetto_rimodulazioneBulk(), "pg_gen_rimodulazione", new Integer(0) )).intValue()+1));

			progettoRimodulazione.setStato(StatoProgettoRimodulazione.STATO_PROVVISORIO.value());
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
				!progettoRimodulazione.isRimodulatoImportoFinanziato() && !progettoRimodulazione.isRimodulatoImportoCofinanziato() && 
			    (!progettoRimodulazione.getProgetto().isDatePianoEconomicoRequired() ||
			     (!progettoRimodulazione.isRimodulatoDtInizio() && !progettoRimodulazione.isRimodulatoDtFine() &&
			      !progettoRimodulazione.isRimodulatoDtProroga())))
				throw new ApplicationException("Salvataggio non consentito. Non risulta alcuna variazione sul piano economico.");
			return super.creaConBulk(usercontext, oggettobulk);
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch (BusyResourceException e) {
			throw new ComponentException(e);
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
							.filter(dett -> Optional.ofNullable(dett.getCd_unita_organizzativa()).orElse("")
									.equals(Optional.ofNullable(newDett.getCd_unita_organizzativa()).orElse("")))
							.filter(dett -> Optional.ofNullable(dett.getCd_voce_piano()).orElse("")
									.equals(Optional.ofNullable(newDett.getCd_voce_piano()).orElse("")))
							.filter(dett -> Optional.ofNullable(dett.getEsercizio_piano()).orElse(0)
									.equals(Optional.ofNullable(newDett.getEsercizio_piano()).orElse(0)))
							.findFirst().orElseGet(() -> {
								newDett.setToBeCreated();
								progettoRimodulazione.addToDettagliRimodulazione(newDett);
								return newDett;
							});
				detail.setImVarEntrata(BigDecimal.ZERO);
				detail.setImVarSpesaFinanziato(newDett.getImVarSpesaFinanziato());
				detail.setImVarSpesaCofinanziato(newDett.getImVarSpesaCofinanziato());
				if (progettoRimodulazione.isStatoProvvisorio()) {
					detail.setImStoassSpesaFinanziato(newDett.getImStoassSpesaFinanziato());
					detail.setImStoassSpesaCofinanziato(newDett.getImStoassSpesaCofinanziato());
				}
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
				!progettoRimodulazione.isRimodulatoImportoFinanziato() && !progettoRimodulazione.isRimodulatoImportoCofinanziato() && 
			    (!progettoRimodulazione.getProgetto().isDatePianoEconomicoRequired() ||
			     (!progettoRimodulazione.isRimodulatoDtInizio() && !progettoRimodulazione.isRimodulatoDtFine() &&
			      !progettoRimodulazione.isRimodulatoDtProroga())))
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
	
	public Progetto_rimodulazioneBulk approva(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione, OggettoBulk variazioneInApprovazione) throws ComponentException {
		try {
			Progetto_rimodulazioneHome home = (Progetto_rimodulazioneHome)getHome(userContext, Progetto_rimodulazioneBulk.class);
		    home.validaPassaggioStatoApprovato(userContext, rimodulazione, variazioneInApprovazione);
		    return approvaInt(userContext, rimodulazione);
		} catch (PersistencyException|IntrospectionException|RimodulazioneNonApprovataException e) {
			throw new ComponentException(e);
		}	    
	}

	private Progetto_rimodulazioneBulk approvaInt(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try{
			//Ricostruisco il progetto sulla base della nuova rimodulazione e aggiorno il dato
			//Nell'aggiornamento viene rifatta la validazione
			Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHome(userContext, Progetto_rimodulazioneBulk.class);
			ProgettoBulk progettoRimodulato = rimodHome.getProgettoRimodulato(rimodulazione);
			Utility.createProgettoRicercaComponentSession().modificaConBulk(userContext, progettoRimodulato, true);

			rimodulazione.setStato(StatoProgettoRimodulazione.STATO_APPROVATO.value());
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
		.orElseThrow(()->new ApplicationException("Operazione non possibile! Lo stato respinto può essere assegnato solo a rimodulazioni in stato definitivo!"));

		Optional.of(rimodulazione).flatMap(el->Optional.ofNullable(el.getMotivoRifiuto()))
				.orElseThrow(()->new ApplicationException("Operazione non possibile! Inserire la motivazione per cui la rimodulazione viene respinta!"));

		rimodulazione.setStato(StatoProgettoRimodulazione.STATO_RESPINTO.value());
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

			validaStatoDefinitivoRimodulazione(userContext, rimodulazione);

			rimodulazione.setStato(StatoProgettoRimodulazione.STATO_DEFINITIVO.value());
			rimodulazione.setDtStatoDefinitivo(EJBCommonServices.getServerTimestamp());
			rimodulazione.setToBeUpdated();
			rimodulazione = (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
			createReportRimodulazione(userContext, rimodulazione);

			//Se la rimodulazione rispetta uno dei seguenti casi:
			// 1) le condizioni di rapida approvazione nonchè consiste nella sola diversa distribuzione degli importi tra anni rispettando la ripartizione per categorie economiche;
			// oppure
			// 2) il tipo di finanziamento prevede la validazione automatica
			// allora provvedo direttamente alla validazione (che dovrebbe in automatico anche approvare)
			if ((!rimodulazione.isRimodulatoImportoFinanziato() && !rimodulazione.isRimodulatoImportoCofinanziato() &&
				 !rimodulazione.isRimodulatoDtProroga() && !rimodulazione.isRimodulatoDtInizio() && !rimodulazione.isRimodulatoDtFine() &&
				 rimodulazione.isRimodulazioneDiRapidaApprovazione()) || rimodulazione.getProgetto().getOtherField().getTipoFinanziamento().getFlValidazioneAutomatica()) {
				return this.valida(userContext, rimodulazione);
			}

			return rimodulazione;
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	public Progetto_rimodulazioneBulk valida(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try{
			//Prima salvo il progetto per aggiornare i dettagli della rimodulazione
			if (rimodulazione.isToBeCreated())
				rimodulazione = (Progetto_rimodulazioneBulk)this.creaConBulk(userContext, rimodulazione);
			else
				rimodulazione = (Progetto_rimodulazioneBulk)this.modificaConBulk(userContext, rimodulazione);
	
			Optional.of(rimodulazione).filter(Progetto_rimodulazioneBulk::isStatoDefinitivo)
			.orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! Lo stato validato può essere assegnato solo a rimodulazioni in stato definitivo!"));

		    List<OggettoBulk> listVariazioni = this.constructVariazioniBilancio(userContext, rimodulazione);

		    //Se la rimodulazione non prevede variazioni procedo direttamente con l'approvazione
		    if (Optional.ofNullable(listVariazioni).map(List::isEmpty).orElse(Boolean.TRUE))
		    	return approvaInt(userContext, rimodulazione);
		    else {
				Utility.createProgettoRicercaComponentSession().validaPianoEconomico(userContext, rimodulazione);
				
				rimodulazione.setStato(StatoProgettoRimodulazione.STATO_VALIDATO.value());
				rimodulazione.setToBeUpdated();
				return (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
		    }
		    
		} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch(Exception e) {
			throw handleException(e);
		}
	}

	private void createReportRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try {
			ModelPpeForPrint modelPrint = new ModelPpeForPrint();
			modelPrint.setCdProgetto(rimodulazione.getProgetto().getCd_progetto());
			modelPrint.setDsProgetto(rimodulazione.getProgetto().getDs_progetto());
			modelPrint.setPgRimodulazione(rimodulazione.getPg_rimodulazione().toString());
			modelPrint.setPgGenRimodulazione(rimodulazione.getPg_gen_rimodulazione().toString());
			modelPrint.setUoCoordinatrice(rimodulazione.getProgetto().getCd_unita_organizzativa()+" - "+rimodulazione.getProgetto().getUnita_organizzativa().getDs_unita_organizzativa());
			modelPrint.setTipoFinanziamento(rimodulazione.getProgetto().getOtherField().getTipoFinanziamento().getCodice()+" - "+rimodulazione.getProgetto().getOtherField().getTipoFinanziamento().getDescrizione());
			modelPrint.setDataInizio(new SimpleDateFormat("dd-MM-yyyy").format(rimodulazione.getProgetto().getOtherField().getDtInizio()));
			modelPrint.setDataFine(new SimpleDateFormat("dd-MM-yyyy").format(rimodulazione.getProgetto().getOtherField().getDtFine()));
			if (rimodulazione.getProgetto().getOtherField().getDtProroga()!=null)
				modelPrint.setDataProroga(new SimpleDateFormat("dd-MM-yyyy").format(rimodulazione.getProgetto().getOtherField().getDtProroga()));

			modelPrint.setImportoFinanziato(rimodulazione.getImFinanziatoRimodulato().subtract(rimodulazione.getImVarFinanziato()));
			modelPrint.setImportoCofinanziato(rimodulazione.getImCofinanziatoRimodulato().subtract(rimodulazione.getImVarCofinanziato()));
			modelPrint.setImportoFinanziatoRimodulato(rimodulazione.getImFinanziatoRimodulato());
			modelPrint.setImportoCofinanziatoRimodulato(rimodulazione.getImCofinanziatoRimodulato());

			if (rimodulazione.getDtProrogaRimodulato()!=null)
				modelPrint.setDataProrogaRimodulata(new SimpleDateFormat("dd-MM-yyyy").format(rimodulazione.getDtProrogaRimodulato()));

			modelPrint.setAnniPianoEconomico(
				rimodulazione.getAllDetailsProgettoPianoEconomico().stream().map(el->el.getEsercizio_piano()).distinct().map(currentAnno->{
					ModelPpeAnnoForPrint modelPpeAnno = new ModelPpeAnnoForPrint();
					modelPpeAnno.setAnno(currentAnno);
					modelPpeAnno.setVociPianoEconomico(
							rimodulazione.getAllDetailsProgettoPianoEconomico().stream().filter(el->el.getEsercizio_piano().equals(currentAnno))
							.map(detail->{
								ModelPpeVoceForPrint modelPpeVoce = new ModelPpeVoceForPrint();
								modelPpeVoce.setCdVoce(detail.getVoce_piano_economico().getCd_voce_piano());
								modelPpeVoce.setDsVoce(detail.getVoce_piano_economico().getDs_voce_piano());
								modelPpeVoce.setQuotaFinanziataCorrente(detail.getIm_spesa_finanziato());
								modelPpeVoce.setQuotaFinanziataRimodulata(detail.getImSpesaFinanziatoRimodulato());
								modelPpeVoce.setQuotaFinanziataStanziata(detail.getImAssestatoSpesaFinanziatoRimodulato());
								modelPpeVoce.setQuotaFinanziataPagata(detail.getSaldoSpesa().getManrisFin());
								modelPpeVoce.setQuotaCofinanziataCorrente(detail.getIm_spesa_cofinanziato());
								modelPpeVoce.setQuotaCofinanziataRimodulata(detail.getImSpesaCofinanziatoRimodulato());
								modelPpeVoce.setQuotaCofinanziataStanziata(detail.getImAssestatoSpesaCofinanziatoRimodulato());
								modelPpeVoce.setQuotaCofinanziataPagata(detail.getSaldoSpesa().getManrisCofin());
								return modelPpeVoce;
							}).collect(Collectors.toList()));
					return modelPpeAnno;
				}).collect(Collectors.toList()));
			
			Print_spoolerBulk print = new Print_spoolerBulk();
			print.setPgStampa(UUID.randomUUID().getLeastSignificantBits());
			print.setFlEmail(false);
			print.setReport("/progettiric/progettiric/rimodula_ppe.jasper");
			print.setNomeFile("Rimodulazione al piano economico n. " + rimodulazione.getPg_rimodulazione() + ".pdf");
			print.setUtcr(userContext.getUser());
			print.addParam("REPORT_DATA_SOURCE", new GsonBuilder().create().toJson(modelPrint),String.class);
			Report report = SpringUtil.getBean("printService", PrintService.class).executeReport(userContext, print);
			String cmisPath = rimodulazione.getStorePath();
			AllegatoProgettoRimodulazioneBulk allegato = new AllegatoProgettoRimodulazioneBulk();
			allegato.setObjectType(AllegatoProgettoRimodulazioneType.AUTOMATICO.value());
			allegato.setRimodulazione(rimodulazione);
			allegato.setNome(allegato.constructNomeFile());
			allegato.setToBeCreated();
			SpringUtil.getBean("storeService", StoreService.class).storeSimpleDocument(
					allegato,
			        report.getInputStream(),
			        report.getContentType(),
			        report.getName(),
			        cmisPath,
			        allegato.getObjectType(),
			        false);
		} catch (ComponentException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
	}

	public List<OggettoBulk> constructVariazioniBilancio(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try {
			Progetto_rimodulazioneHome rimodHome = (Progetto_rimodulazioneHome)getHome(userContext, Progetto_rimodulazioneBulk.class);
		    return rimodHome.constructVariazioniBilancio(userContext, rimodulazione);
		} catch(Exception e) {
			throw handleException(e);
		}			
	}
	
	/*
	 * Metodo che verifica se la rimodulazione può divenire definitiva.
	 * Per divenire tale: 
	 * 		1) on devono esserci variazioni sul progetto in stato definitivo o approvazione formale.
	 * 		2) la rimodulazione deve essere provvisoria.
	 * 		3) se rimodulato data proroga occorre file fi tipo proroga.
	 * 		4) se rimodulato altro occorre file di tipo rimodulazione.
	 * 		5) vengono lanciati controlli su progetto.
	 */
	private void validaStatoDefinitivoRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException{
    	try {
    		List<OggettoBulk> listVariazioni = this.constructVariazioniBilancio(userContext, rimodulazione);

		    //Se la rimodulazione prevede variazioni di competenza procedo a verificare che non ce ne siano sul progetto in stato definitivo
		    if (Optional.ofNullable(listVariazioni).map(List::stream).orElse(Stream.empty())
		    			.filter(Pdg_variazioneBulk.class::isInstance).findFirst().isPresent()) {
	       		Pdg_variazioneHome homeVarSpe = (Pdg_variazioneHome)getHome(userContext,Pdg_variazioneBulk.class);
	       		SQLBuilder sqlVarSpe = homeVarSpe.createSQLBuilder();
	
	       		sqlVarSpe.addTableToHeader("PDG_VARIAZIONE_RIGA_GEST");
	       		sqlVarSpe.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO", "PDG_VARIAZIONE.ESERCIZIO");
	       		sqlVarSpe.addSQLJoin("PDG_VARIAZIONE_RIGA_GEST.PG_VARIAZIONE_PDG", "PDG_VARIAZIONE.PG_VARIAZIONE_PDG");
	       		sqlVarSpe.addClause(FindClause.AND,"stato",SQLBuilder.EQUALS,Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
	       		
	       		sqlVarSpe.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
	       		sqlVarSpe.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "PDG_VARIAZIONE_RIGA_GEST.ESERCIZIO");
	       		sqlVarSpe.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "PDG_VARIAZIONE_RIGA_GEST.CD_CDR_ASSEGNATARIO");
	       		sqlVarSpe.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "PDG_VARIAZIONE_RIGA_GEST.CD_LINEA_ATTIVITA");
	       		sqlVarSpe.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());    		
	
	       		List<Pdg_variazioneBulk> resultVarSpe = homeVarSpe.fetchAll(sqlVarSpe);
	       		resultVarSpe.stream().findFirst().ifPresent(el->{
	    			throw new ApplicationRuntimeException("Operazione non possibile! Esiste la variazione di competenza "+el.getEsercizio()+
	    					"/"+el.getPg_variazione_pdg()+" con stato Definitivo che non consente di rendere definitiva la rimodulazione! "+
	    					" Approvare o annullare la variazione e rieffettuare l'operazione!");
	      		});
		    }
		    
		    //Se la rimodulazione prevede variazioni residue su un anno procedo a verificare se non ce ne siano sul progetto in stato definitivo
		    Optional.ofNullable(listVariazioni).map(List::stream).orElse(Stream.empty())
		    		.filter(Var_stanz_resBulk.class::isInstance).map(Var_stanz_resBulk.class::cast)
		    		.map(Var_stanz_resBulk::getEsercizio_residuo)
			   		.forEach(esercizioRes->{
			   			try{
				       		Var_stanz_resHome homeVarRes = (Var_stanz_resHome)getHome(userContext,Var_stanz_resBulk.class);
				       		SQLBuilder sqlVarRes = homeVarRes.createSQLBuilder();
				       		sqlVarRes.addClause(FindClause.AND,"esercizio_res",SQLBuilder.EQUALS,esercizioRes);
	
				       		sqlVarRes.addTableToHeader("VAR_STANZ_RES_RIGA");
				       		sqlVarRes.addSQLJoin("VAR_STANZ_RES_RIGA.ESERCIZIO", "VAR_STANZ_RES.ESERCIZIO");
				       		sqlVarRes.addSQLJoin("VAR_STANZ_RES_RIGA.PG_VARIAZIONE", "VAR_STANZ_RES.PG_VARIAZIONE");
				       		sqlVarRes.openParenthesis(FindClause.AND);
				       		sqlVarRes.addClause(FindClause.OR,"stato",SQLBuilder.EQUALS,Pdg_variazioneBulk.STATO_PROPOSTA_DEFINITIVA);
				       		sqlVarRes.addClause(FindClause.OR,"stato",SQLBuilder.EQUALS,Pdg_variazioneBulk.STATO_APPROVAZIONE_FORMALE);
				       		sqlVarRes.closeParenthesis();
				       		
				       		sqlVarRes.addTableToHeader("V_LINEA_ATTIVITA_VALIDA");
				       		sqlVarRes.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO", "VAR_STANZ_RES_RIGA.ESERCIZIO");
				       		sqlVarRes.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_CENTRO_RESPONSABILITA", "VAR_STANZ_RES_RIGA.CD_CDR");
				       		sqlVarRes.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA", "VAR_STANZ_RES_RIGA.CD_LINEA_ATTIVITA");
				       		sqlVarRes.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO",SQLBuilder.EQUALS,rimodulazione.getPg_progetto());    		
	
				       		List<Var_stanz_resBulk> resultVarRes = homeVarRes.fetchAll(sqlVarRes);
				       		resultVarRes.stream().findFirst().ifPresent(el->{
				    			throw new ApplicationRuntimeException("Operazione non possibile! Esiste la variazione residua "+el.getEsercizio()+
				    					"/"+el.getPg_variazione()+" con stato "+el.getStato()+" che non consente di rendere definitiva la rimodulazione! "+
				    					" Approvare o annullare la variazione e rieffettuare l'operazione!");
				      		});
			   			} catch (ComponentException|PersistencyException ex){
			   				throw new RuntimeException(ex);
			   			}
			   		});
       		
			if (((!rimodulazione.getDettagliRimodulazione().isEmpty() || !rimodulazione.getDettagliVoceRimodulazione().isEmpty())
					&& !rimodulazione.isRimodulazioneDiRapidaApprovazione()) ||
				rimodulazione.isRimodulatoImportoFinanziato() || rimodulazione.isRimodulatoImportoCofinanziato())
				rimodulazione.getArchivioAllegati().stream()
							 .filter(AllegatoProgettoRimodulazioneBulk.class::isInstance)
							 .map(AllegatoProgettoRimodulazioneBulk.class::cast)
							 .filter(AllegatoProgettoRimodulazioneBulk::isRimodulazione)
							 .findFirst()
							 .orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! E' necessario associare un allegato di tipo rimodulazione!"));

			if (rimodulazione.isRimodulatoDtProroga())
				rimodulazione.getArchivioAllegati().stream()
					.filter(AllegatoProgettoRimodulazioneBulk.class::isInstance)
					.map(AllegatoProgettoRimodulazioneBulk.class::cast)
					.filter(AllegatoProgettoRimodulazioneBulk::isProroga)
					.findFirst()
					.orElseThrow(()->new ApplicationRuntimeException("Operazione non possibile! E' necessario associare un allegato di tipo proroga!"));

			Utility.createProgettoRicercaComponentSession().validaPianoEconomico(userContext, rimodulazione);
    	} catch (ApplicationRuntimeException e) {
			throw new ApplicationException(e);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RuntimeException e) {
            throw handleException(e);
		} catch(Exception e) {
			throw handleException(e);
		}
    }
	
	public Progetto_rimodulazioneBulk rebuildRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		try {
			Progetto_rimodulazioneHome homeRimod = (Progetto_rimodulazioneHome)getHome(userContext,Progetto_rimodulazioneBulk.class);
			return homeRimod.rebuildRimodulazione(userContext, rimodulazione);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
	}

	public Progetto_rimodulazioneBulk riportaDefinitivo(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		Optional.of(rimodulazione).filter(el->el.isStatoRespinto()||el.isStatoValidato())
				.orElseThrow(()->new ApplicationException("Operazione non possibile! Lo stato definitivo può essere riassegnato solo a rimodulazioni in stato validato o respinto!"));

		Optional.of(rimodulazione).flatMap(el -> Optional.ofNullable(el.getVariazioniAssociate())).filter(el -> el.isEmpty())
				.orElseThrow(()->new ApplicationException("Operazione non possibile! Lo stato definitivo non può essere riassegnato a rimodulazioni già associate a variazioni di bilancio!"));

		validaStatoDefinitivoRimodulazione(userContext, rimodulazione);

		rimodulazione.setStato(StatoProgettoRimodulazione.STATO_DEFINITIVO.value());
		rimodulazione.setToBeUpdated();
		return (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);
	}

	public Progetto_rimodulazioneBulk riportaProvvisorio(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws ComponentException {
		Optional.of(rimodulazione).filter(el->el.isStatoDefinitivo())
				.orElseThrow(()->new ApplicationException("Operazione non possibile! Lo stato provvisorio può essere riassegnato solo a rimodulazioni in stato definitivo!"));

		rimodulazione.setStato(StatoProgettoRimodulazione.STATO_PROVVISORIO.value());
		rimodulazione.setDtStatoDefinitivo(null);
		rimodulazione.setToBeUpdated();
		rimodulazione = (Progetto_rimodulazioneBulk)super.modificaConBulk(userContext, rimodulazione);

		rimodulazione.getArchivioAllegati().stream()
				.filter(AllegatoProgettoRimodulazioneBulk.class::isInstance)
				.map(AllegatoProgettoRimodulazioneBulk.class::cast)
				.filter(AllegatoProgettoRimodulazioneBulk::isStampaAutomatica)
				.forEach(el->{
					SpringUtil.getBean("storeService", StoreService.class).delete(el.getStorageKey());
				});

		return rimodulazione;
	}
}
