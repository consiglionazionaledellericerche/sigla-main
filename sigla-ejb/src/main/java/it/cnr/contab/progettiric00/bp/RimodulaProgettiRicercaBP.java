package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_ppeBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_voceBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class RimodulaProgettiRicercaBP extends SimpleCRUDBP {
	private boolean flPrgPianoEconomico = false;
	protected boolean isUoCdsCollegata = false;
	private Integer annoFromPianoEconomico;
	private Unita_organizzativaBulk uoScrivania;

	private SimpleDetailCRUDController crudPianoEconomicoTotale = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoTotale", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoTotale", this){
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			((Progetto_piano_economicoBulk)oggettobulk).setEsercizio_piano(Integer.valueOf(0));
			return super.addDetail(oggettobulk);
		};
	};

	protected SimpleDetailCRUDController crudPianoEconomicoAnnoCorrente = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoAnnoCorrente", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoAnnoCorrente", this){
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			((Progetto_piano_economicoBulk)oggettobulk).setEsercizio_piano(((Progetto_rimodulazioneBulk)this.getParentModel()).getProgetto().getEsercizio());
			((Progetto_piano_economicoBulk)oggettobulk).setIm_entrata(BigDecimal.ZERO);
			((Progetto_piano_economicoBulk)oggettobulk).setIm_spesa_finanziato(BigDecimal.ZERO);
			((Progetto_piano_economicoBulk)oggettobulk).setIm_spesa_cofinanziato(BigDecimal.ZERO);
			((Progetto_piano_economicoBulk)oggettobulk).setImSpesaFinanziatoRimodulato(BigDecimal.ZERO);
			((Progetto_piano_economicoBulk)oggettobulk).setImSpesaCofinanziatoRimodulato(BigDecimal.ZERO);
			return super.addDetail(oggettobulk);
		};
	};

	protected SimpleDetailCRUDController crudPianoEconomicoAltriAnni = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoAltriAnni", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoAltriAnni", this) {
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validate(actioncontext, oggettobulk);
			if (Optional.ofNullable(oggettobulk).filter(Progetto_piano_economicoBulk.class::isInstance)
					.map(Progetto_piano_economicoBulk.class::cast).flatMap(el->Optional.ofNullable(el.getEsercizio_piano()))
					.filter(el->el.equals(((Progetto_rimodulazioneBulk)this.getParentModel()).getProgetto().getEsercizio())).isPresent())
				throw new ValidationException("Operazione non possibile! Per caricare un dato relativo all'anno corrente utilizzare la sezione apposita.");
		};
	};

	private SimpleDetailCRUDController pianoEconomicoSummaryVoce = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoSummaryVoce", Progetto_piano_economicoBulk.class, "pianoEconomicoSummaryVoce", this);
	private SimpleDetailCRUDController pianoEconomicoSummaryAnno = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoSummaryAnno", Progetto_piano_economicoBulk.class, "pianoEconomicoSummaryAnno", this);

	protected SimpleDetailCRUDController crudPianoEconomicoVoceBilancioAnnoCorrente = new RimodulaProgettoPianoEconomicoVoceBilancioCRUDController( "PianoEconomicoVoceBilancioAnnoCorrente", Ass_progetto_piaeco_voceBulk.class, "vociBilancioAssociate", crudPianoEconomicoAnnoCorrente);
	protected SimpleDetailCRUDController crudPianoEconomicoVoceBilancioAltriAnni = new RimodulaProgettoPianoEconomicoVoceBilancioCRUDController( "PianoEconomicoVoceBilancioAltriAnni", Ass_progetto_piaeco_voceBulk.class, "vociBilancioAssociate", crudPianoEconomicoAltriAnni);

	/**
	 * RimodulaProgettiRicercaBP constructor comment.
	 */
	public RimodulaProgettiRicercaBP() {
		super();
	}
	/**
	 * RimodulaProgettiRicercaBP constructor comment.
	 * @param function java.lang.String
	 */
	public RimodulaProgettiRicercaBP(String function) {
		super(function);
	}

	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
			setFlPrgPianoEconomico(parEnte.getFl_prg_pianoeco().booleanValue());
			uoScrivania = (Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findUOByCodice(actioncontext.getUserContext(), CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()));
			isUoCdsCollegata = uoScrivania.getFl_uo_cds();

			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);
	   		if (Optional.ofNullable(annoFrom).isPresent())
	   			setAnnoFromPianoEconomico(annoFrom.intValue());
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
		super.init(config, actioncontext);
		resetForSearch(actioncontext);
	}

	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		progettoRimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_PROVVISORIO);
		progettoRimodulazione.setImVarFinanziato(BigDecimal.ZERO);
		progettoRimodulazione.setImVarCofinanziato(BigDecimal.ZERO);
		return progettoRimodulazione;
	}
	
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		initializeProgetto(actioncontext, progettoRimodulazione, progettoRimodulazione.getProgetto());
		return progettoRimodulazione;
	}
	
	public String[][] getTabs(HttpSession session) {
		String uo = CNRUserContext.getCd_unita_organizzativa(HttpActionContext.getUserContext(session));
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)this.getModel();
		ProgettoBulk progetto = progettoRimodulazione.getProgetto();
		
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTestata","Testata","/progettiric00/rimodula_progetto_ricerca_testata.jsp" });
		
		if (progetto!=null && !this.isSearching()) {
			if (isUoCdsCollegata || 
		    	  (progetto.getCd_unita_organizzativa()!=null && progetto.getCd_unita_organizzativa().equals(uo))) {
		    	if (this.isFlPrgPianoEconomico() && 
		    		((progetto.isPianoEconomicoRequired() && 
		    		  Optional.ofNullable(progetto.getOtherField()).flatMap(el->Optional.ofNullable(el.getDtInizio())).isPresent() &&
		    		  Optional.ofNullable(progetto.getOtherField()).flatMap(el->Optional.ofNullable(el.getDtFine())).isPresent()) ||
		    		 (progetto.isDettagliPianoEconomicoPresenti() && 
		    		  Optional.ofNullable(this.getAnnoFromPianoEconomico()).map(el->el.compareTo(CNRUserContext.getEsercizio(HttpActionContext.getUserContext(session)))<=0)
		    				 .orElse(Boolean.FALSE))))
		    		hash.put(i++, new String[]{"tabPianoEconomico","Piano Economico","/progettiric00/rimodula_progetto_piano_economico.jsp" });
		    } 
	    } 
		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}

	public String[][] getTabsPianoEconomico() {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{ "tabProgettoPianoEconomicoSummary","Totali","/progettiric00/rimodula_progetto_piano_economico_summary.jsp" });

		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)this.getModel();
		ProgettoBulk progetto = progettoRimodulazione.getProgetto();

		boolean existAnnoCorrente = false;
		if (progetto.getAnnoInizioOf() > progetto.getEsercizio() || progetto.getAnnoFineOf() < progetto.getEsercizio()) {
			//non sono nell'anno ma verifico se per caso non l'ho erronemanete caricato
			if (progetto.getDettagliPianoEconomicoAnnoCorrente().size()>0)
				existAnnoCorrente = true;
		} else 
			existAnnoCorrente = true;

		if (existAnnoCorrente)
			hash.put(i++, new String[]{ "tabProgettoPianoEconomicoAnnoCorrente","Anno "+progetto.getEsercizio(),"/progettiric00/rimodula_progetto_piano_economico_anno_corrente.jsp" });
			
		if (!progetto.getAnnoInizioOf().equals(progetto.getEsercizio()) || !progetto.getAnnoFineOf().equals(progetto.getEsercizio()))
			hash.put(i++, new String[]{ "tabProgettoPianoEconomicoAltriAnni","Altri Anni","/progettiric00/rimodula_progetto_piano_economico_altri_anni.jsp" });

		String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++) {
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		}
		return tabs;
	}
	
	protected void resetTabs(it.cnr.jada.action.ActionContext context) {
		setTab("tab","tabTestata");
		setTab("tabProgettoPianoEconomico","tabProgettoPianoEconomicoSummary");
	}
	
	public boolean isFlPrgPianoEconomico() {
		return flPrgPianoEconomico;
	}
	
	public void setFlPrgPianoEconomico(boolean flPrgPianoEconomico) {
		this.flPrgPianoEconomico = flPrgPianoEconomico;
	}	
	
	protected Integer getAnnoFromPianoEconomico() {
		return annoFromPianoEconomico;
	}
	
	public void setAnnoFromPianoEconomico(Integer annoFromPianoEconomico) {
		this.annoFromPianoEconomico = annoFromPianoEconomico;
	}
	
	public SimpleDetailCRUDController getCrudPianoEconomicoTotale() {
		return crudPianoEconomicoTotale;
	}
	
	public SimpleDetailCRUDController getCrudPianoEconomicoAnnoCorrente() {
		return crudPianoEconomicoAnnoCorrente;
	}
	
	public SimpleDetailCRUDController getCrudPianoEconomicoAltriAnni() {
		return crudPianoEconomicoAltriAnni;
	}

	public SimpleDetailCRUDController getPianoEconomicoSummaryVoce() {
		return pianoEconomicoSummaryVoce;
	}

	public SimpleDetailCRUDController getPianoEconomicoSummaryAnno() {
		return pianoEconomicoSummaryAnno;
	}

	public SimpleDetailCRUDController getCrudPianoEconomicoVoceBilancioAnnoCorrente() {
		return crudPianoEconomicoVoceBilancioAnnoCorrente;
	}
	
	public SimpleDetailCRUDController getCrudPianoEconomicoVoceBilancioAltriAnni() {
		return crudPianoEconomicoVoceBilancioAltriAnni;
	}
	
	public void initializeProgetto(ActionContext actioncontext, Progetto_rimodulazioneBulk rimodulazione, ProgettoBulk progetto) throws BusinessProcessException {
		try {
			progetto = Utility.createProgettoRicercaComponentSession().initializePianoEconomico(actioncontext.getUserContext(), progetto, true);

			rimodulazione.setProgetto(progetto);
			rimodulazione.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setImFinanziatoRimodulato(progetto.getImFinanziato().add(rimodulazione.getImVarFinanziato()));
			rimodulazione.setImCofinanziatoRimodulato(progetto.getImCofinanziato().add(rimodulazione.getImVarCofinanziato()));
			
			progetto.getDettagliPianoEconomicoTotale().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setProgetto(el.getProgetto());
					ppe.setVoce_piano_economico(el.getVoce_piano_economico());
					ppe.setEsercizio_piano(el.getEsercizio_piano());
					ppe.setIm_entrata(el.getIm_entrata());
					ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
					ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
					el.getVociBilancioAssociate().stream()
						.forEach(voce->{
							Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
							newVoce.setElemento_voce(voce.getElemento_voce());
							newVoce.setProgetto_piano_economico(ppe);
							newVoce.setSaldoSpesa(voce.getSaldoSpesa());
							newVoce.setSaldoEntrata(voce.getSaldoEntrata());
							ppe.addToVociBilancioAssociate(newVoce);
						});
					rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
				});
			
			progetto.getDettagliPianoEconomicoAnnoCorrente().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setProgetto(el.getProgetto());
					ppe.setVoce_piano_economico(el.getVoce_piano_economico());
					ppe.setEsercizio_piano(el.getEsercizio_piano());
					ppe.setIm_entrata(el.getIm_entrata());
					ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
					ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
					el.getVociBilancioAssociate().stream()
						.forEach(voce->{
							Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
							newVoce.setElemento_voce(voce.getElemento_voce());
							newVoce.setProgetto_piano_economico(ppe);
							newVoce.setSaldoSpesa(voce.getSaldoSpesa());
							newVoce.setSaldoEntrata(voce.getSaldoEntrata());
							ppe.addToVociBilancioAssociate(newVoce);
						});
					rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
				});
	
			progetto.getDettagliPianoEconomicoAltriAnni().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setProgetto(el.getProgetto());
					ppe.setVoce_piano_economico(el.getVoce_piano_economico());
					ppe.setEsercizio_piano(el.getEsercizio_piano());
					ppe.setIm_entrata(el.getIm_entrata());
					ppe.setIm_spesa_finanziato(el.getIm_spesa_finanziato());
					ppe.setIm_spesa_cofinanziato(el.getIm_spesa_cofinanziato());
					el.getVociBilancioAssociate().stream()
						.forEach(voce->{
							Ass_progetto_piaeco_voceBulk newVoce = new Ass_progetto_piaeco_voceBulk();
							newVoce.setElemento_voce(voce.getElemento_voce());
							newVoce.setProgetto_piano_economico(ppe);
							newVoce.setSaldoSpesa(voce.getSaldoSpesa());
							newVoce.setSaldoEntrata(voce.getSaldoEntrata());
							ppe.addToVociBilancioAssociate(newVoce);
						});
					rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);
				});
			
			//Aggiorno i dettagli presenti
			rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
				.forEach(el->{
					Progetto_rimodulazione_ppeBulk dett = 
							rimodulazione.getDettagliRimodulazione().stream()
								.filter(dettRim->dettRim.getPg_progetto().equals(el.getPg_progetto()))
								.filter(dettRim->dettRim.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
								.filter(dettRim->dettRim.getCd_voce_piano().equals(el.getCd_voce_piano()))
								.filter(dettRim->dettRim.getEsercizio_piano().equals(el.getEsercizio_piano()))
								.findAny().orElse(null);
					el.setImSpesaFinanziatoRimodulato(el.getIm_spesa_finanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaFinanziato).orElse(BigDecimal.ZERO)));
					el.setImSpesaCofinanziatoRimodulato(el.getIm_spesa_cofinanziato().add(Optional.ofNullable(dett).map(Progetto_rimodulazione_ppeBulk::getImVarSpesaCofinanziato).orElse(BigDecimal.ZERO)));
				});

			//Aggiungo i dettagli nuovi
			rimodulazione.getDettagliRimodulazione().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk dett = 
							rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
								.filter(dettPpe->dettPpe.getPg_progetto().equals(el.getPg_progetto()))
								.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(el.getCd_unita_organizzativa()))
								.filter(dettPpe->dettPpe.getCd_voce_piano().equals(el.getCd_voce_piano()))
								.filter(dettPpe->dettPpe.getEsercizio_piano().equals(el.getEsercizio_piano()))
								.findAny().orElse(null);
					if (!Optional.ofNullable(dett).isPresent()) {
						Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
						ppe.setProgetto(rimodulazione.getProgetto());
						ppe.setVoce_piano_economico(el.getVocePianoEconomico());
						ppe.setEsercizio_piano(el.getEsercizio_piano());
						ppe.setIm_entrata(el.getImVarEntrata());
						ppe.setIm_spesa_finanziato(BigDecimal.ZERO);
						ppe.setIm_spesa_cofinanziato(BigDecimal.ZERO);
						ppe.setImSpesaFinanziatoRimodulato(el.getImVarSpesaFinanziato());
						ppe.setImSpesaCofinanziatoRimodulato(el.getImVarSpesaCofinanziato());
						
						if (ppe.getEsercizio_piano().equals(0))
							rimodulazione.addToDettagliPianoEconomicoTotale(ppe);
						else if (ppe.getEsercizio_piano().equals(rimodulazione.getProgetto().getEsercizio()))
							rimodulazione.addToDettagliPianoEconomicoAnnoCorrente(ppe);
						else
							rimodulazione.addToDettagliPianoEconomicoAltriAnni(ppe);						
					}
				});

			//Aggiorno le voci di bilancio presenti
			rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
				.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
				.forEach(ppeVoce->{
					ppeVoce.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
					ppeVoce.setImVarCofinanziatoRimodulato(BigDecimal.ZERO);
					Progetto_rimodulazione_voceBulk rimVoce = 
							rimodulazione.getDettagliVoceRimodulazione().stream()
								.filter(dettVoceRim->dettVoceRim.getPg_progetto().equals(ppeVoce.getPg_progetto()))
								.filter(dettVoceRim->dettVoceRim.getCd_unita_organizzativa().equals(ppeVoce.getCd_unita_organizzativa()))
								.filter(dettVoceRim->dettVoceRim.getCd_voce_piano().equals(ppeVoce.getCd_voce_piano()))
								.filter(dettVoceRim->dettVoceRim.getEsercizio_piano().equals(ppeVoce.getEsercizio_piano()))
								.filter(dettVoceRim->dettVoceRim.getEsercizio_voce().equals(ppeVoce.getEsercizio_voce()))
								.filter(dettVoceRim->dettVoceRim.getTi_appartenenza().equals(ppeVoce.getTi_appartenenza()))
								.filter(dettVoceRim->dettVoceRim.getTi_gestione().equals(ppeVoce.getTi_gestione()))
								.filter(dettVoceRim->dettVoceRim.getCd_elemento_voce().equals(ppeVoce.getCd_elemento_voce()))
								.findAny().orElse(null);
					if (Optional.ofNullable(rimVoce).isPresent()) {
						ppeVoce.setDetailRimodulatoAggiunto(rimVoce.isTiOperazioneAggiunto());
						ppeVoce.setDetailRimodulatoEliminato(rimVoce.isTiOperazioneEliminato());
						ppeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
						ppeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());
					}
				});

			//Aggiungo i dettagli nuovi
			rimodulazione.getDettagliVoceRimodulazione().stream()
				.forEach(rimVoce->{
					Ass_progetto_piaeco_voceBulk ppeVoce = 
							rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
								.flatMap(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).map(List::stream).orElse(Stream.empty()))
								.filter(dettVocePpe->dettVocePpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
								.filter(dettVocePpe->dettVocePpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
								.filter(dettVocePpe->dettVocePpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
								.filter(dettVocePpe->dettVocePpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
								.filter(dettVocePpe->dettVocePpe.getEsercizio_voce().equals(rimVoce.getEsercizio_voce()))
								.filter(dettVocePpe->dettVocePpe.getTi_appartenenza().equals(rimVoce.getTi_appartenenza()))
								.filter(dettVocePpe->dettVocePpe.getTi_gestione().equals(rimVoce.getTi_gestione()))
								.filter(dettVocePpe->dettVocePpe.getCd_elemento_voce().equals(rimVoce.getCd_elemento_voce()))
								.findAny().orElse(null);
					if (!Optional.ofNullable(ppeVoce).isPresent()) {
						Progetto_piano_economicoBulk ppe = 
								rimodulazione.getAllDetailsProgettoPianoEconomico().stream()
									.filter(dettPpe->dettPpe.getPg_progetto().equals(rimVoce.getPg_progetto()))
									.filter(dettPpe->dettPpe.getCd_unita_organizzativa().equals(rimVoce.getCd_unita_organizzativa()))
									.filter(dettPpe->dettPpe.getCd_voce_piano().equals(rimVoce.getCd_voce_piano()))
									.filter(dettPpe->dettPpe.getEsercizio_piano().equals(rimVoce.getEsercizio_piano()))
									.findAny().orElse(null);
						if (Optional.of(ppe).isPresent()) {
							Ass_progetto_piaeco_voceBulk newPpeVoce = new Ass_progetto_piaeco_voceBulk();
							newPpeVoce.setElemento_voce(rimVoce.getElementoVoce());
							newPpeVoce.setDetailRimodulatoAggiunto(Boolean.TRUE);
							newPpeVoce.setImVarFinanziatoRimodulato(rimVoce.getImVarSpesaFinanziato());
							newPpeVoce.setImVarCofinanziatoRimodulato(rimVoce.getImVarSpesaCofinanziato());
							
							ppe.addToVociBilancioAssociate(newPpeVoce);
						}
					}
				});
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
}