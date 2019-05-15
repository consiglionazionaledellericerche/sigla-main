package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoRimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_ppeBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_voce_progettoBulk;
import it.cnr.contab.progettiric00.ejb.RimodulaProgettoRicercaComponentSession;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bp.AllegatiTypeCRUDBP;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

public class RimodulaProgettiRicercaBP extends AllegatiTypeCRUDBP<AllegatoProgettoRimodulazioneBulk, Progetto_rimodulazioneBulk> {
	private boolean flPrgPianoEconomico = false;
	protected boolean isUoCdsCollegata = false;
	private Integer annoFromPianoEconomico;
	private Integer lastEsercizioAperto;
	private Unita_organizzativaBulk uoScrivania;
    private CdrBulk cdrScrivania;
	private ProgettoBulk mainProgetto;

	private SimpleDetailCRUDController crudPianoEconomicoTotale = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoTotale", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoTotale", this){
		public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
			((Progetto_piano_economicoBulk)oggettobulk).setEsercizio_piano(Integer.valueOf(0));
			return super.addDetail(oggettobulk);
		};
	};

	protected SimpleDetailCRUDController crudPianoEconomicoAnnoCorrente = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoAnnoCorrente", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoAnnoCorrente", this);

	protected SimpleDetailCRUDController crudPianoEconomicoAltriAnni = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoAltriAnni", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoAltriAnni", this) {
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			super.validate(actioncontext, oggettobulk);
			if (Optional.ofNullable(oggettobulk).filter(Progetto_piano_economicoBulk.class::isInstance)
					.map(Progetto_piano_economicoBulk.class::cast).flatMap(el->Optional.ofNullable(el.getEsercizio_piano()))
					.filter(el->el.equals(((Progetto_rimodulazioneBulk)this.getParentModel()).getProgetto().getEsercizio())).isPresent())
				throw new ValidationException("Operazione non possibile! Per caricare un dato relativo all'anno corrente utilizzare la sezione apposita.");
		};
	};

	private SimpleDetailCRUDController pianoEconomicoSummaryVoce = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoSummaryVoce", Progetto_piano_economicoBulk.class, "pianoEconomicoSummaryVoce", this){
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException ,javax.servlet.ServletException {};

		@Override
		public String getRowStyle(Object obj) {
			Progetto_piano_economicoBulk ppe = (Progetto_piano_economicoBulk)obj;
			StringBuffer style = new StringBuffer();
			if (ppe.isDetailRimodulato() || (ppe.isToBeCreated() && !ppe.isDetailDerivato()))
				style.append("font-style:italic;font-weight:bold;");
			if (ppe.isDetailRimodulatoEliminato())
				style.append("text-decoration: line-through;");
			return Optional.of(style).filter(el->el.length()>0).map(StringBuffer::toString).orElse(null);
		};
	};
	private SimpleDetailCRUDController pianoEconomicoSummaryAnno = new RimodulaProgettoPianoEconomicoCRUDController( "PianoEconomicoSummaryAnno", Progetto_piano_economicoBulk.class, "pianoEconomicoSummaryAnno", this){
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException ,javax.servlet.ServletException {};
	};
	private SimpleDetailCRUDController pianoEconomicoVociBilancioDaAssociare = new SimpleDetailCRUDController( "VociMovimentateNonAssociate", V_saldi_voce_progettoBulk.class, "vociMovimentateNonAssociate", this); 

	protected SimpleDetailCRUDController crudPianoEconomicoVoceBilancioAnnoCorrente = new RimodulaProgettoPianoEconomicoVoceBilancioCRUDController( "PianoEconomicoVoceBilancioAnnoCorrente", Ass_progetto_piaeco_voceBulk.class, "vociBilancioAssociate", crudPianoEconomicoAnnoCorrente) {
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			try {
				Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
				Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)getParentController().getParentController().getModel();
				rimodulazione.getDettagliPianoEconomicoAnnoCorrente().stream()
					.filter(ppe->!ppe.getVoce_piano_economico().equalsByPrimaryKey(assVoce.getProgetto_piano_economico().getVoce_piano_economico()))
					.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
					.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
					.filter(ppe->!ppe.isDetailRimodulatoEliminato())
					.filter(ppe->Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getTi_gestione()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
					.filter(ppe->ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
					.filter(ppe->ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
					.filter(ppe->ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
					.filter(ppe->ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
					.findFirst().ifPresent(ppe->{
						throw new DetailedRuntimeException("Associazione non possibile! La voce di bilancio "+ppe.getCd_elemento_voce()+
								" risulta già essere stata associata alla voce economica "+ppe.getCd_voce_piano()+"!");
					});
	        } catch (DetailedRuntimeException _ex) {
	            throw new ValidationException(_ex.getMessage());
	        }
		};
		public void validateForUndoRemoveDetail(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			try {
				Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
				if (assVoce.isDetailRimodulatoEliminato()) {
					Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)getParentController().getParentController().getModel();
					rimodulazione.getDettagliPianoEconomicoAnnoCorrente().stream()
						.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
						.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
						.filter(ppe->!ppe.isDetailRimodulatoEliminato())
						.filter(ppe->Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getTi_gestione()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
						.filter(ppe->ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
						.filter(ppe->ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
						.filter(ppe->ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
						.filter(ppe->ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
						.findFirst().ifPresent(ppe->{
							throw new DetailedRuntimeException("Annullamento eliminazione non possibile! La voce di bilancio "+ppe.getCd_elemento_voce()+
								" risulta già essere stata associata alla voce economica "+ppe.getCd_voce_piano()+"!");
						});
				}
	        } catch (DetailedRuntimeException _ex) {
	            throw new ValidationException(_ex.getMessage());
	        }
		}
	};
	
	protected SimpleDetailCRUDController crudPianoEconomicoVoceBilancioAltriAnni = new RimodulaProgettoPianoEconomicoVoceBilancioCRUDController( "PianoEconomicoVoceBilancioAltriAnni", Ass_progetto_piaeco_voceBulk.class, "vociBilancioAssociate", crudPianoEconomicoAltriAnni) {
		protected void validate(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			try {
				Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
				Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)getParentController().getParentController().getModel();
				rimodulazione.getDettagliPianoEconomicoAltriAnni().stream()
					.filter(ppe->!ppe.getVoce_piano_economico().equalsByPrimaryKey(assVoce.getProgetto_piano_economico().getVoce_piano_economico()))
					.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
					.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
					.filter(ppe->!ppe.isDetailRimodulatoEliminato())
					.filter(ppe->Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getTi_gestione()).isPresent())
					.filter(ppe->Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
					.filter(ppe->ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
					.filter(ppe->ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
					.filter(ppe->ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
					.filter(ppe->ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
					.findFirst().ifPresent(ppe->{
						throw new DetailedRuntimeException("Associazione non possibile! La voce di bilancio "+ppe.getCd_elemento_voce()+
								" risulta già essere stata associata alla voce economica "+ppe.getCd_voce_piano()+"!");
					});
	        } catch (DetailedRuntimeException _ex) {
	            throw new ValidationException(_ex.getMessage());
	        }
		};
		
		public void validateForUndoRemoveDetail(ActionContext actioncontext, OggettoBulk oggettobulk) throws ValidationException {
			try {
				Ass_progetto_piaeco_voceBulk assVoce = (Ass_progetto_piaeco_voceBulk)oggettobulk;
				if (assVoce.isDetailRimodulatoEliminato()) {
					Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)getParentController().getParentController().getModel();
					rimodulazione.getDettagliPianoEconomicoAltriAnni().stream()
						.filter(ppe->Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
						.flatMap(ppe->ppe.getVociBilancioAssociate().stream())
						.filter(ppe->!ppe.isDetailRimodulatoEliminato())
						.filter(ppe->Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getTi_gestione()).isPresent())
						.filter(ppe->Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
						.filter(ppe->ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
						.filter(ppe->ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
						.filter(ppe->ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
						.filter(ppe->ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
						.findFirst().ifPresent(ppe->{
							throw new DetailedRuntimeException("Annullamento eliminazione non possibile! La voce di bilancio "+ppe.getCd_elemento_voce()+
								" risulta già essere stata associata alla voce economica "+ppe.getCd_voce_piano()+"!");
						});
				}
	        } catch (DetailedRuntimeException _ex) {
	            throw new ValidationException(_ex.getMessage());
	        }
		}
	};

	private final SimpleDetailCRUDController crudVariazioniAssociate = new SimpleDetailCRUDController("Variazioni associate", Progetto_rimodulazione_variazioneBulk.class,"variazioniAssociate",this){
		public void writeHTMLToolbar(javax.servlet.jsp.PageContext context, boolean reset, boolean find, boolean delete, boolean closedToolbar) throws java.io.IOException ,javax.servlet.ServletException {
			super.openButtonGROUPToolbar(context);

			{
				Button button = new Button();
	    		button.setImg("img/open16.gif");
	    		button.setDisabledImg("img/open16.gif");
	    		button.setTitle("Apri Variazione");
	    		button.setIconClass("fa fa-folder-open-o text-primary");
	    		button.setButtonClass("btn-sm btn-secondary btn-outline-secondary btn-title");
	            button.setHref("javascript:submitForm('doOpenVariazione(" + getInputPrefix() + ")')");
	            boolean isButtonEnable = Optional.ofNullable(this.getModel()).isPresent();
	            button.writeToolbarButton(context.getOut(), isButtonEnable, HttpActionContext.isFromBootstrap(context));
			}
	        {
				Button button = new Button();
	    		button.setImg("img/new16.gif");
	    		button.setDisabledImg("img/new16.gif");
	    		button.setTitle("Nuova Variazione Competenza");
	    		button.setIconClass("fa fa fa-plus text-primary");
	    		button.setButtonClass("btn-sm btn-secondary btn-outline-secondary btn-title");
	            button.setHref("javascript:submitForm('doNewVariazioneCompetenza()')");
	            button.writeToolbarButton(context.getOut(), Boolean.TRUE, HttpActionContext.isFromBootstrap(context));
            }
	        {
				Button button = new Button();
	    		button.setImg("img/new16.gif");
	    		button.setDisabledImg("img/new16.gif");
	    		button.setTitle("Nuova Variazione Residua");
	    		button.setIconClass("fa fa fa-plus text-primary");
	    		button.setButtonClass("btn-sm btn-secondary btn-outline-secondary btn-title");
	            button.setHref("javascript:submitForm('doNewVariazioneResidua()')");
	            button.writeToolbarButton(context.getOut(), Boolean.TRUE, HttpActionContext.isFromBootstrap(context));
            }
            
            super.closeButtonGROUPToolbar(context);			
		};
	};

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

	public RimodulaProgettiRicercaBP(String function, ProgettoBulk progetto) {
		super(function);
		setMainProgetto(progetto);
	}
	
	@Override
	protected void init(Config config, ActionContext actioncontext) throws BusinessProcessException {
		super.init(config, actioncontext);
		try {
			Parametri_enteBulk parEnte = Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext());
			setFlPrgPianoEconomico(parEnte.getFl_prg_pianoeco().booleanValue());
			uoScrivania = (Unita_organizzativaBulk)Utility.createUnita_organizzativaComponentSession().findUOByCodice(actioncontext.getUserContext(), CNRUserContext.getCd_unita_organizzativa(actioncontext.getUserContext()));
            cdrScrivania = Utility.createCdrComponentSession().cdrFromUserContext(actioncontext.getUserContext());
			isUoCdsCollegata = uoScrivania.getFl_uo_cds();

			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);

	   		if (Optional.ofNullable(annoFrom).isPresent())
	   			setAnnoFromPianoEconomico(annoFrom.intValue());

			EsercizioComponentSession esercizioComponentSession = ((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",	EsercizioComponentSession.class));
			EsercizioBulk lastEsercizio = esercizioComponentSession.getLastEsercizioOpen(actioncontext.getUserContext());
			setLastEsercizioAperto(Optional.ofNullable(lastEsercizio).map(EsercizioBulk::getEsercizio).orElse(CNRUserContext.getEsercizio(actioncontext.getUserContext())));
	   		
	   		if (Optional.ofNullable(this.getMainProgetto()).isPresent()) {
				List<Progetto_rimodulazioneBulk> listRimodulazioni = createComponentSession().find(actioncontext.getUserContext(), Progetto_rimodulazioneBulk.class, "findRimodulazioni", actioncontext.getUserContext(), this.getMainProgetto().getPg_progetto());
				Optional<Progetto_rimodulazioneBulk> lastRim = listRimodulazioni.stream()
									.filter(el->!el.isStatoApprovato()&&!el.isStatoRespinto())
									.sorted(Comparator.comparing(Progetto_rimodulazioneBulk::getPg_rimodulazione).reversed())
									.findFirst();
	
				if (lastRim.isPresent())
					edit(actioncontext, lastRim.get());
				else
					reset(actioncontext);
			} else
				resetForSearch(actioncontext);
		}catch(Throwable e) {
			throw new BusinessProcessException(e);
		}
	}

	@Override
	public void basicEdit(ActionContext actioncontext, OggettoBulk oggettobulk, boolean flag)
			throws BusinessProcessException {
		super.basicEdit(actioncontext, oggettobulk, flag);
		//Se la uo collegata non è la coordinatrice del progetto non può fare nulla
		if (!Optional.ofNullable(this.getModel())
			 		 .filter(Progetto_rimodulazioneBulk.class::isInstance)
					 .map(Progetto_rimodulazioneBulk.class::cast)
					 .flatMap(el->Optional.ofNullable(el.getProgetto()))
					 .flatMap(el->Optional.ofNullable(el.getCd_unita_organizzativa()))
					 .map(el->el.equals(uoScrivania.getCd_unita_organizzativa()))
					 .orElse(Boolean.FALSE))
			this.setStatus(VIEW);
	}

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 3];

		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.definitiveSave");
		newToolbar[i+1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.approva");
		newToolbar[i+2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.nonApprova");
        return newToolbar;
    }
    
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		progettoRimodulazione.setStato(Progetto_rimodulazioneBulk.STATO_PROVVISORIO);
		progettoRimodulazione.setImVarFinanziato(BigDecimal.ZERO);
		progettoRimodulazione.setImVarCofinanziato(BigDecimal.ZERO);
		if (Optional.ofNullable(this.getMainProgetto()).isPresent()) {
			progettoRimodulazione.setProgetto(this.getMainProgetto());
			progettoRimodulazione = rebuildRimodulazione(actioncontext, progettoRimodulazione);
		}
		progettoRimodulazione.setAnnoFromPianoEconomico(this.getAnnoFromPianoEconomico());
		progettoRimodulazione.setLastEsercizioAperto(this.getLastEsercizioAperto());
		return progettoRimodulazione;
	}
	
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		progettoRimodulazione = rebuildRimodulazione(actioncontext, progettoRimodulazione);
		progettoRimodulazione.setAnnoFromPianoEconomico(this.getAnnoFromPianoEconomico());
		progettoRimodulazione.setLastEsercizioAperto(this.getLastEsercizioAperto());
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

		//Se su una rimodulazione definitiva è prevista la creazioni di variazioni visualizzo la tab corrispondente
		if (Optional.ofNullable(progettoRimodulazione).filter(Progetto_rimodulazioneBulk::isStatoDefinitivo)
				.flatMap(el->Optional.ofNullable(el.getVariazioniModels())).filter(el->!el.isEmpty())
				.isPresent()) {
			hash.put(i++, new String[]{ "tabVariazioniAss", "Variazioni Associate", "/progettiric00/tab_ass_progetto_rimod_variazioni.jsp" });
		}

		hash.put(i++, new String[]{"tabAllegati","Allegati","/util00/tab_allegati.jsp" });
		
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

		Optional<Progetto_rimodulazioneBulk> optProgettoRimodulazione = Optional.ofNullable(this.getModel()).filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast);
		Optional<ProgettoBulk> optProgetto = optProgettoRimodulazione.flatMap(el->Optional.ofNullable(el.getProgetto()));

		Integer annoInizio = optProgettoRimodulazione.map(el->el.getAnnoInizioRimodulato())
				.orElse(optProgetto.map(ProgettoBulk::getAnnoInizioOf).orElse(0));
		Integer annoFine = optProgettoRimodulazione.map(el->el.getAnnoFineRimodulato())
				.orElse(optProgetto.map(ProgettoBulk::getAnnoFineOf).orElse(9999));

		boolean existAnnoCorrente = false;
		if (annoInizio > optProgetto.get().getEsercizio() || annoFine < optProgetto.get().getEsercizio()) {
			//non sono nell'anno ma verifico se per caso non l'ho erronemanete caricato
			if (optProgetto.get().getDettagliPianoEconomicoAnnoCorrente().size()>0)
				existAnnoCorrente = true;
		} else 
			existAnnoCorrente = true;

		if (existAnnoCorrente)
			hash.put(i++, new String[]{ "tabProgettoPianoEconomicoAnnoCorrente","Anno "+optProgetto.get().getEsercizio(),"/progettiric00/rimodula_progetto_piano_economico_anno_corrente.jsp" });
			
		if (!annoInizio.equals(optProgetto.get().getEsercizio()) || !optProgetto.get().getAnnoFineOf().equals(optProgetto.get().getEsercizio()) ||
				optProgettoRimodulazione.get().getDettagliPianoEconomicoAltriAnni().size()>0)
			hash.put(i++, new String[]{ "tabProgettoPianoEconomicoAltriAnni","Altri Anni","/progettiric00/rimodula_progetto_piano_economico_altri_anni.jsp" });

		if (!optProgettoRimodulazione.get().getVociMovimentateNonAssociate().isEmpty())
			hash.put(i++, new String[]{ "tabProgettoVociMovimentateNonAssociate","Voci Movimentate da Associare","/progettiric00/rimodula_progetto_piano_economico_voci_da_associare.jsp" });

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
	
	public Integer getLastEsercizioAperto() {
		return lastEsercizioAperto;
	}
	
	public void setLastEsercizioAperto(Integer lastEsercizioAperto) {
		this.lastEsercizioAperto = lastEsercizioAperto;
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
	
	public SimpleDetailCRUDController getCrudVariazioniAssociate() {
		return crudVariazioniAssociate;
	}
	
	public SimpleDetailCRUDController getPianoEconomicoVociBilancioDaAssociare() {
		return pianoEconomicoVociBilancioDaAssociare;
	}
	
	public Progetto_rimodulazioneBulk rebuildRimodulazione(ActionContext actioncontext, Progetto_rimodulazioneBulk rimodulazione) throws BusinessProcessException {
		rimodulazione = initializeRimodulazione(actioncontext, rimodulazione);

		if (rimodulazione.getProgetto()==null) 
			return rimodulazione;
		else if (rimodulazione.isStatoApprovato()||rimodulazione.isStatoRespinto())
			return rebuildRimodulazioneChiusa(actioncontext, rimodulazione);
		else
			return rebuildRimodulazioneInCorso(actioncontext, rimodulazione);
	}
	
	private Progetto_rimodulazioneBulk initializeRimodulazione(ActionContext actioncontext, Progetto_rimodulazioneBulk rimodulazione) throws BusinessProcessException {
		try {
			if (rimodulazione.getProgetto()==null) return rimodulazione;
	
			final ProgettoBulk progetto = Utility.createProgettoRicercaComponentSession().initializePianoEconomico(actioncontext.getUserContext(), rimodulazione.getProgetto(), true);
	
			rimodulazione.setProgetto(progetto);
			rimodulazione.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setImFinanziatoRimodulato(progetto.getImFinanziato());
			rimodulazione.setImCofinanziatoRimodulato(progetto.getImCofinanziato());
			rimodulazione.setDtInizioRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtInizio).orElse(null));
			rimodulazione.setDtFineRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtFine).orElse(null));
			rimodulazione.setDtProrogaRimodulato(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtProroga).orElse(null));
			
			progetto.getDettagliPianoEconomicoTotale().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setDetailDerivato(Boolean.TRUE);
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
					ppe.setDetailDerivato(Boolean.TRUE);
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
					ppe.setDetailDerivato(Boolean.TRUE);
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
			return rimodulazione;
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	private Progetto_rimodulazioneBulk rebuildRimodulazioneInCorso(ActionContext actioncontext, Progetto_rimodulazioneBulk rimodulazione) throws BusinessProcessException {
		final ProgettoBulk progetto = rimodulazione.getProgetto();
		
		Optional.ofNullable(rimodulazione.getImVarFinanziato()).ifPresent(el->rimodulazione.setImFinanziatoRimodulato(rimodulazione.getImFinanziatoRimodulato().add(el)));
		Optional.ofNullable(rimodulazione.getImVarCofinanziato()).ifPresent(el->rimodulazione.setImCofinanziatoRimodulato(rimodulazione.getImCofinanziatoRimodulato().add(el)));
		Optional.ofNullable(rimodulazione.getDtInizio()).ifPresent(el->rimodulazione.setDtInizioRimodulato(el));
		Optional.ofNullable(rimodulazione.getDtFine()).ifPresent(el->rimodulazione.setDtFineRimodulato(el));
		Optional.ofNullable(rimodulazione.getDtProroga()).ifPresent(el->rimodulazione.setDtProrogaRimodulato(el));

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
						initializeAssProgettoPiaecoVoce(actioncontext,newPpeVoce,progetto.getVociBilancioMovimentate());
						ppe.addToVociBilancioAssociate(newPpeVoce);
					}
				}
			});
		
		try {
			if (rimodulazione.isStatoDefinitivo()) {
	        	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
	        	rimodulazione.setVariazioniModels(new BulkList<>(comp.constructVariazioniBilancio(actioncontext.getUserContext(), rimodulazione)));
			}
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
		return rimodulazione;
	}

	private Progetto_rimodulazioneBulk rebuildRimodulazioneChiusa(ActionContext actioncontext, Progetto_rimodulazioneBulk rimodulazione) throws BusinessProcessException {
		try {
			ProgettoBulk progetto = rimodulazione.getProgetto();
			
			List<Progetto_rimodulazioneBulk> listRimodulazioni = createComponentSession().find(actioncontext.getUserContext(), Progetto_rimodulazioneBulk.class, "findRimodulazioni", actioncontext.getUserContext(), rimodulazione.getProgetto().getPg_progetto());
			listRimodulazioni.stream()
					.filter(el->el.isStatoApprovato())
					.filter(el->el.getPg_rimodulazione().compareTo(rimodulazione.getPg_rimodulazione())>=0)
					.sorted(Comparator.comparing(Progetto_rimodulazioneBulk::getPg_rimodulazione).reversed())
					.forEachOrdered(currRim->{
						Optional.ofNullable(currRim.getImVarFinanziato()).ifPresent(el->progetto.getOtherField().setImFinanziato(progetto.getOtherField().getImFinanziato().subtract(el)));
						Optional.ofNullable(currRim.getImVarCofinanziato()).ifPresent(el->progetto.getOtherField().setImCofinanziato(progetto.getOtherField().getImCofinanziato().subtract(el)));
						Optional.ofNullable(currRim.getDtInizioOld()).ifPresent(el->progetto.getOtherField().setDtInizio(el));
						Optional.ofNullable(currRim.getDtFineOld()).ifPresent(el->progetto.getOtherField().setDtFine(el));
					});

			rimodulazione.setProgetto(progetto);
			rimodulazione.setDettagliPianoEconomicoTotale(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAnnoCorrente(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setDettagliPianoEconomicoAltriAnni(new BulkList<Progetto_piano_economicoBulk>());
			rimodulazione.setImFinanziatoRimodulato(progetto.getImFinanziato().add(rimodulazione.getImVarFinanziato()));
			rimodulazione.setImCofinanziatoRimodulato(progetto.getImCofinanziato().add(rimodulazione.getImVarCofinanziato()));
			rimodulazione.setDtInizioRimodulato(Optional.ofNullable(rimodulazione.getDtInizio()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtInizio).orElse(null)));
			rimodulazione.setDtFineRimodulato(Optional.ofNullable(rimodulazione.getDtFine()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtFine).orElse(null)));
			rimodulazione.setDtProrogaRimodulato(Optional.ofNullable(rimodulazione.getDtProroga()).orElse(Optional.ofNullable(progetto.getOtherField()).map(Progetto_other_fieldBulk::getDtProroga).orElse(null)));
			
			progetto.getDettagliPianoEconomicoTotale().stream()
				.forEach(el->{
					Progetto_piano_economicoBulk ppe = new Progetto_piano_economicoBulk();
					ppe.setDetailDerivato(Boolean.TRUE);
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
					ppe.setDetailDerivato(Boolean.TRUE);
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
					ppe.setDetailDerivato(Boolean.TRUE);
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

							initializeAssProgettoPiaecoVoce(actioncontext,newPpeVoce,progetto.getVociBilancioMovimentate());
							ppe.addToVociBilancioAssociate(newPpeVoce);
						}
					}
				});
			return rimodulazione;
		} catch (ComponentException | RemoteException e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public void initializeAssProgettoPiaecoVoce(ActionContext actioncontext, Ass_progetto_piaeco_voceBulk assProgettoPiaecoVoce) {
		ProgettoBulk progetto = ((Progetto_rimodulazioneBulk)this.getModel()).getProgetto();
		initializeAssProgettoPiaecoVoce(actioncontext,assProgettoPiaecoVoce,progetto.getVociBilancioMovimentate());
	}

	private void initializeAssProgettoPiaecoVoce(ActionContext actioncontext, Ass_progetto_piaeco_voceBulk assProgettoPiaecoVoce, BulkList<V_saldi_voce_progettoBulk> vociBilancioMovimentate) {
		V_saldi_voce_progettoBulk saldo = 
				vociBilancioMovimentate.stream()
						.filter(el->el.getEsercizio_voce().equals(assProgettoPiaecoVoce.getEsercizio_voce()))
						.filter(el->el.getTi_appartenenza().equals(assProgettoPiaecoVoce.getTi_appartenenza()))
						.filter(el->el.getTi_gestione().equals(assProgettoPiaecoVoce.getTi_gestione()))
						.filter(el->el.getCd_elemento_voce().equals(assProgettoPiaecoVoce.getCd_elemento_voce()))
						.findFirst()
						.orElseGet(()->{
							V_saldi_voce_progettoBulk saldoNew = new V_saldi_voce_progettoBulk();
							saldoNew.setEsercizio_voce(assProgettoPiaecoVoce.getEsercizio_voce());
							saldoNew.setTi_appartenenza(assProgettoPiaecoVoce.getTi_appartenenza());
							saldoNew.setTi_gestione(assProgettoPiaecoVoce.getTi_gestione());
							saldoNew.setCd_elemento_voce(assProgettoPiaecoVoce.getCd_elemento_voce());

							saldoNew.setStanziamentoFin(BigDecimal.ZERO);
							saldoNew.setVariapiuFin(BigDecimal.ZERO);
							saldoNew.setVariamenoFin(BigDecimal.ZERO);
							saldoNew.setTrasfpiuFin(BigDecimal.ZERO);
							saldoNew.setTrasfmenoFin(BigDecimal.ZERO);

							saldoNew.setStanziamentoCofin(BigDecimal.ZERO);
							saldoNew.setVariapiuCofin(BigDecimal.ZERO);
							saldoNew.setVariamenoCofin(BigDecimal.ZERO);
							saldoNew.setTrasfpiuCofin(BigDecimal.ZERO);
							saldoNew.setTrasfmenoCofin(BigDecimal.ZERO);
							
							saldoNew.setImpaccFin(BigDecimal.ZERO);
							saldoNew.setImpaccCofin(BigDecimal.ZERO);
							saldoNew.setManrisFin(BigDecimal.ZERO);
							saldoNew.setManrisCofin(BigDecimal.ZERO);
							return saldoNew;
						});
		if (Elemento_voceHome.GESTIONE_ENTRATE.equals(assProgettoPiaecoVoce.getTi_gestione()))
			assProgettoPiaecoVoce.setSaldoEntrata(saldo);
		else
			assProgettoPiaecoVoce.setSaldoSpesa(saldo);
	}
	
	public void caricaVociPianoEconomicoAssociate(ActionContext context, Progetto_piano_economicoBulk progettoPiaeco) throws BusinessProcessException {
		try {
			if (Optional.ofNullable(progettoPiaeco.getVoce_piano_economico()).map(Voce_piano_economico_prgBulk::getFl_link_vocibil_associate).orElse(Boolean.FALSE)) {
				List<Elemento_voceBulk> listVoci = Utility.createProgettoRicercaComponentSession().find(context.getUserContext(), Elemento_voceBulk.class, "findElementoVociAssociate", progettoPiaeco);
				progettoPiaeco.setVociBilancioAssociate(new BulkList<>());
				ProgettoBulk progetto = ((Progetto_rimodulazioneBulk)this.getModel()).getProgetto();

				listVoci.stream().forEach(el->{
					Ass_progetto_piaeco_voceBulk dett = new Ass_progetto_piaeco_voceBulk();
					dett.setElemento_voce(el);
					dett.setDetailRimodulatoAggiunto(Boolean.TRUE);
					initializeAssProgettoPiaecoVoce(context,dett,progetto.getVociBilancioMovimentate());
					progettoPiaeco.addToVociBilancioAssociate(dett);
				});
			}
	    } catch (ComponentException | RemoteException e) {
	        throw handleException(e);
	    }
	}	
	
	public void setMainProgetto(ProgettoBulk mainProgetto) {
		this.mainProgetto = mainProgetto;
	}
	
	public ProgettoBulk getMainProgetto() {
		return mainProgetto;
	}
	
	@Override
	public boolean isSearchButtonHidden() {
		return super.isSearchButtonHidden() || Optional.ofNullable(this.getMainProgetto()).isPresent();
	}
	
	@Override
	public boolean isFreeSearchButtonHidden() {
		return super.isFreeSearchButtonHidden() || Optional.ofNullable(this.getMainProgetto()).isPresent();
	}

	@Override
	public boolean isNewButtonHidden() {
		return super.isNewButtonHidden() || Optional.ofNullable(this.getMainProgetto()).isPresent();
	}

	@Override
	public boolean isDeleteButtonEnabled() {
		return super.isDeleteButtonEnabled() &&
				Optional.ofNullable(this.getModel())
					.filter(Progetto_rimodulazioneBulk.class::isInstance)
					.map(Progetto_rimodulazioneBulk.class::cast)
					.map(Progetto_rimodulazioneBulk::isStatoProvvisorio)
					.orElse(Boolean.FALSE);
	}
	
	@Override
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled() &&
				Optional.ofNullable(this.getModel())
					.filter(Progetto_rimodulazioneBulk.class::isInstance)
					.map(Progetto_rimodulazioneBulk.class::cast)
					.map(el->el.isStatoProvvisorio() || 
							(el.isStatoDefinitivo() && !el.getVariazioniModels().isEmpty()))
					.orElse(Boolean.FALSE);
	}
	
    /**
     * Restituisce il valore della proprietà 'salvaDefinitivoButtonEnabled'
     * Il bottone di SalvaDefinitivo è disponibile solo se:
     * - la proposta è provvisoria
     * - il CDR è di 1° Livello
     *
     * @return Il valore della proprietà 'salvaDefinitivoButtonEnabled'
     */
    public boolean isSalvaDefinitivoButtonEnabled() {
        return this.isSaveButtonEnabled() &&
                ((Progetto_rimodulazioneBulk) getModel()).isStatoProvvisorio();
    }

    /**
     * Restituisce il valore della proprietà 'approvaButtonEnabled'
     * Il bottone di Approva è disponibile solo se:
     * - è attivo il bottone di salvataggio
     * - la proposta di variazione PDG è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'approvaButtonEnabled'
     */
    public boolean isApprovaButtonEnabled() {
		Optional<Progetto_rimodulazioneBulk> optModel = 
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

        return optModel.filter(Progetto_rimodulazioneBulk::isStatoDefinitivo).isPresent() &&
                uoScrivania.isUoEnte();
    }

    /**
     * Restituisce il valore della proprietà 'nonApprovaButtonEnabled'
     * Il bottone di NonApprova è disponibile solo se:
     * - è attivo il bottone di salvataggio
     * - la proposta di variazione PDG è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'nonApprovaButtonEnabled'
     */
    public boolean isNonApprovaButtonEnabled() {
		Optional<Progetto_rimodulazioneBulk> optModel = 
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

        return optModel.filter(Progetto_rimodulazioneBulk::isStatoDefinitivo).isPresent() &&
                uoScrivania.isUoEnte();
    }

    /**
     * Gestione del salvataggio come definitiva di una rimodulazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     * @throws ValidationException
     */
    public void salvaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        try {
        	this.save(context);
        	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
        	Progetto_rimodulazioneBulk bulk = comp.salvaDefinitivo(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
            edit(context, bulk);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Gestione del salvataggio come approvata di una variazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void approva(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        try {
        	this.save(context);
        	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
        	Progetto_rimodulazioneBulk bulk = comp.approva(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
            edit(context, bulk);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }

    /**
     * Gestione del salvataggio come respinta di una variazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void respingi(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        try {
        	this.save(context);
        	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
        	Progetto_rimodulazioneBulk bulk = comp.respingi(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
            edit(context, bulk);
        } catch (it.cnr.jada.comp.ComponentException ex) {
            throw handleException(ex);
        } catch (java.rmi.RemoteException ex) {
            throw handleException(ex);
        }
    }
    
    @Override
    public void delete(ActionContext actioncontext) throws BusinessProcessException {
        int crudStatus = getModel().getCrudStatus();
        try {
        	Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk) getModel();
            if (rimodulazione.isStatoProvvisorio()) {
            	rimodulazione.getDettagliVoceRimodulazione().stream().forEach(el->el.setToBeDeleted());
            	rimodulazione.getDettagliRimodulazione().stream().forEach(el->el.setToBeDeleted());
            	super.delete(actioncontext);
                setMessage("Cancellazione effettuata");
            } else 
                throw new BusinessProcessException("Lo stato della rimodulazione non ne consente la cancellazione.");
        } catch (Exception e) {
            getModel().setCrudStatus(crudStatus);
            throw handleException(e);
        }
    }

	@Override
	protected String getStorePath(Progetto_rimodulazioneBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
        return allegatoParentBulk.getStorePath();
	}

	@Override
	protected Class<AllegatoProgettoRimodulazioneBulk> getAllegatoClass() {
        return AllegatoProgettoRimodulazioneBulk.class;
	} 
	
    public void validaImportoFinanziatoRimodulato(ActionContext actioncontext, Optional<Progetto_piano_economicoBulk> optPpe) throws ValidationException {
        boolean isAddVoceBilancio = optPpe.flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
										.map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
										.orElse(Boolean.FALSE);

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore negativo.");

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0 && 
				optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0)
			throw new ValidationException("Operazione non possibile! I campi importo finanziato e cofinanziato non possono essere entrambi nulli.");

		//Calcolo il valore minimo al di sotto del quale non si può andare
		BigDecimal totaleUtilizzato = optPpe.get().getVociBilancioAssociate().stream()
			  		.filter(el->!isAddVoceBilancio||Optional.ofNullable(el.getElemento_voce()).isPresent())
			  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
			  		.map(V_saldi_voce_progettoBulk::getUtilizzatoAssestatoFinanziamento)
			  		.reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO);
		
		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(totaleUtilizzato)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore inferiore"
					+ " all'importo già utilizzato ("
					+ new it.cnr.contab.util.EuroFormat().format(totaleUtilizzato)
					+ ") su voci di bilancio associate obbligatoriamente alla voce di piano economico corrispondente.");
		
		//Se viene azzerato l'importo provvedo a proporre variazioni alle voci azzerative per riequilibrare il tutto
		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0) {
			optPpe.get().getVociBilancioAssociate().stream()
		  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
		  		.forEach(el->{
		  			el.setImVarFinanziatoRimodulato(el.getSaldoSpesa().getDispAssestatoFinanziamento().negate());
		  		});
		} else if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(optPpe.get().getIm_spesa_finanziato())==0) {
  			//Se l'importo non viene rimodulato rimetto a zero il valore delle variazioni stesse
			optPpe.get().getVociBilancioAssociate().stream()
		  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
		  		.forEach(el->{
		  				el.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
		  		});
    	} else {
			//se è una sola voce collegata propongo la variazione
			if (optPpe.get().getVociBilancioAssociate().stream()
					.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.count()==1)
				optPpe.get().getVociBilancioAssociate().stream()
			  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.findFirst()
			  		.ifPresent(el->{
			  			//Se con la rimodulazione l'importo viene diminuito propongo la variazione 
			  			//Se l'importo viene aumentato rimetto a zero il valore della variazione stessa se negativa
			  			if (optPpe.get().getDispResiduaFinanziamentoRimodulato().add(el.getImVarFinanziatoRimodulato()).compareTo(BigDecimal.ZERO)<0) {
			  				if (el.getSaldoSpesa().getDispAssestatoFinanziamento().compareTo(optPpe.get().getDispResiduaFinanziamentoRimodulato()
			  						.add(el.getImVarFinanziatoRimodulato()).negate())>=0)
			  					el.setImVarFinanziatoRimodulato(optPpe.get().getDispResiduaFinanziamentoRimodulato().add(el.getImVarFinanziatoRimodulato()));
			  				else
			  					el.setImVarFinanziatoRimodulato(el.getSaldoSpesa().getDispAssestatoFinanziamento().negate());
			  			} else if (el.getImVarFinanziatoRimodulato().compareTo(BigDecimal.ZERO)<0)
			  				el.setImVarFinanziatoRimodulato(BigDecimal.ZERO);
			  		});
		}
    }

    public void validaImportoCofinanziatoRimodulato(ActionContext actioncontext, Optional<Progetto_piano_economicoBulk> optPpe) throws ValidationException {
        boolean isAddVoceBilancio = optPpe.flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
										.map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
										.orElse(Boolean.FALSE);

		if (optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore negativo.");

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0 && 
				optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0)
			throw new ValidationException("Operazione non possibile! I campi importo finanziato e cofinanziato non possono essere entrambi nulli.");

		//Calcolo il valore minimo al di sotto del quale non si può andare
		BigDecimal totaleUtilizzato = optPpe.get().getVociBilancioAssociate().stream()
			  		.filter(el->!isAddVoceBilancio||Optional.ofNullable(el.getElemento_voce()).isPresent())
			  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
			  		.map(V_saldi_voce_progettoBulk::getUtilizzatoAssestatoCofinanziamento)
			  		.reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO);
		
		if (optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(totaleUtilizzato)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore inferiore"
					+ " all'importo già utilizzato ("
					+ new it.cnr.contab.util.EuroFormat().format(totaleUtilizzato)
					+ ") su voci di bilancio associate obbligatoriamente alla voce di piano economico corrispondente.");
	}
}