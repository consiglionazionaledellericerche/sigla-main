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

package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.EsercizioComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.progettiric00.core.bulk.AllegatoProgettoRimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazione_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.V_saldi_voce_progettoBulk;
import it.cnr.contab.progettiric00.ejb.RimodulaProgettoRicercaComponentSession;
import it.cnr.contab.progettiric00.enumeration.StatoProgettoRimodulazione;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

public class RimodulaProgettiRicercaBP extends AllegatiProgettoRimodulazioneCRUDBP<AllegatoProgettoRimodulazioneBulk, Progetto_rimodulazioneBulk> {
	private boolean flPrgPianoEconomico = false;
	protected boolean isUoCdsCollegata = false;
	private Integer annoFromPianoEconomico;
	private Integer lastEsercizioAperto;
	private Unita_organizzativaBulk uoScrivania;
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
				if (!assVoce.isDetailRimodulatoEliminato()) {
					Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk) getParentController().getParentController().getModel();
					rimodulazione.getDettagliPianoEconomicoAnnoCorrente().stream()
							.filter(ppe -> Optional.ofNullable(ppe.getVoce_piano_economico()).isPresent())
							.filter(ppe -> !ppe.getVoce_piano_economico().equalsByPrimaryKey(assVoce.getProgetto_piano_economico().getVoce_piano_economico()))
							.filter(ppe -> Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
							.flatMap(ppe -> ppe.getVociBilancioAssociate().stream())
							.filter(ppe -> !ppe.isDetailRimodulatoEliminato())
							.filter(ppe -> Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getTi_gestione()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
							.filter(ppe -> ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
							.filter(ppe -> ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
							.filter(ppe -> ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
							.filter(ppe -> ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
							.findFirst().ifPresent(ppe -> {
						throw new DetailedRuntimeException("Associazione non possibile! La voce di bilancio " + ppe.getCd_elemento_voce() +
								" risulta già essere stata associata alla voce economica " + ppe.getCd_voce_piano() + "!");
					});
				}
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
				if (!assVoce.isDetailRimodulatoEliminato()) {
					Progetto_rimodulazioneBulk rimodulazione = (Progetto_rimodulazioneBulk)getParentController().getParentController().getModel();
					rimodulazione.getDettagliPianoEconomicoAltriAnni().stream()
							.filter(ppe -> Optional.ofNullable(ppe.getVoce_piano_economico()).isPresent())
							.filter(ppe -> !ppe.getVoce_piano_economico().equalsByPrimaryKey(assVoce.getProgetto_piano_economico().getVoce_piano_economico()))
							.filter(ppe -> Optional.ofNullable(ppe.getVociBilancioAssociate()).isPresent())
							.flatMap(ppe -> ppe.getVociBilancioAssociate().stream())
							.filter(ppe -> !ppe.isDetailRimodulatoEliminato())
							.filter(ppe -> Optional.ofNullable(ppe.getEsercizio_voce()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getTi_appartenenza()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getTi_gestione()).isPresent())
							.filter(ppe -> Optional.ofNullable(ppe.getCd_elemento_voce()).isPresent())
							.filter(ppe -> ppe.getEsercizio_voce().equals(assVoce.getEsercizio_voce()))
							.filter(ppe -> ppe.getTi_appartenenza().equals(assVoce.getTi_appartenenza()))
							.filter(ppe -> ppe.getTi_gestione().equals(assVoce.getTi_gestione()))
							.filter(ppe -> ppe.getCd_elemento_voce().equals(assVoce.getCd_elemento_voce()))
							.findFirst().ifPresent(ppe -> {
						throw new DetailedRuntimeException("Associazione non possibile! La voce di bilancio " + ppe.getCd_elemento_voce() +
								" risulta già essere stata associata alla voce economica " + ppe.getCd_voce_piano() + "!");
					});
				}
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
			isUoCdsCollegata = uoScrivania.getFl_uo_cds();

			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession configSession = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
	   		BigDecimal annoFrom = configSession.getIm01(actioncontext.getUserContext(), new Integer(0), null, Configurazione_cnrBulk.PK_GESTIONE_PROGETTI, Configurazione_cnrBulk.SK_PROGETTO_PIANO_ECONOMICO);

	   		if (Optional.ofNullable(annoFrom).isPresent())
	   			setAnnoFromPianoEconomico(annoFrom.intValue());

			EsercizioComponentSession esercizioComponentSession = ((it.cnr.contab.config00.ejb.EsercizioComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_EsercizioComponentSession",	EsercizioComponentSession.class));
			EsercizioBulk lastEsercizio = esercizioComponentSession.getLastEsercizioOpen(actioncontext.getUserContext());
			setLastEsercizioAperto(Optional.ofNullable(lastEsercizio).map(EsercizioBulk::getEsercizio).orElse(CNRUserContext.getEsercizio(actioncontext.getUserContext())));
	   		
	   		if (Optional.ofNullable(this.getMainProgetto()).isPresent()) {
				List<Progetto_rimodulazioneBulk> listRimodulazioni = this.createComponentSession().find(actioncontext.getUserContext(), ProgettoBulk.class, "findRimodulazioni", this.getMainProgetto().getPg_progetto());
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
		Optional<Progetto_rimodulazioneBulk> optPrgRim =
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);
		//Se la rimodulazione è approvata/annullata non può fare nulla
		if (optPrgRim.map(el->el.isStatoApprovato()||el.isStatoRespinto())
				     .orElse(Boolean.FALSE) ||
			//Se la uo collegata non è la coordinatrice del progetto non può fare nulla
			!optPrgRim.flatMap(el->Optional.ofNullable(el.getProgetto()))
					  .flatMap(el->Optional.ofNullable(el.getCd_unita_organizzativa()))
					  .map(el->el.equals(uoScrivania.getCd_unita_organizzativa()))
					  .orElse(Boolean.FALSE))
			this.setStatus(VIEW);
	}

    protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 5];

		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.definitiveSave");
		newToolbar[i+1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.valida");
		newToolbar[i+2] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.respingi");
		newToolbar[i+3] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaDefinitivo");
		newToolbar[i+4] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()), "CRUDToolbar.riportaProvvisorio");
        return newToolbar;
    }
    
	@Override
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForInsert(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		progettoRimodulazione.setStato(StatoProgettoRimodulazione.STATO_PROVVISORIO.value());
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
	
	@Override
	public OggettoBulk initializeModelForSearch(ActionContext actioncontext, OggettoBulk oggettobulk) throws BusinessProcessException {
		oggettobulk = super.initializeModelForSearch(actioncontext, oggettobulk);
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)oggettobulk;
		ProgettoBulk progetto = new ProgettoBulk();
		progetto.setUnita_organizzativa(new Unita_organizzativaBulk());
		progettoRimodulazione.setProgetto(progetto);
		return progettoRimodulazione;
	}
	
	public String[][] getTabs(HttpSession session) {
		String uo = CNRUserContext.getCd_unita_organizzativa(HttpActionContext.getUserContext(session));
		Progetto_rimodulazioneBulk progettoRimodulazione = (Progetto_rimodulazioneBulk)this.getModel();
		Optional<ProgettoBulk> optProgetto = Optional.ofNullable(progettoRimodulazione).flatMap(el->Optional.ofNullable(el.getProgetto()));
		
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTestata","Testata","/progettiric00/rimodula_progetto_ricerca_testata.jsp" });
		
		if (!this.isSearching()) {
			if (optProgetto.isPresent() && !(progettoRimodulazione.isStatoApprovato() || progettoRimodulazione.isStatoRespinto())) {
				//Il piano economico non lo faccio vedere per le rimodulazioni approvate/respinte perchè ancora non implementata la procedura
				//di ricostruzione della rimodulazione
				if (isUoCdsCollegata ||
						(optProgetto.flatMap(el->Optional.ofNullable(el.getCd_unita_organizzativa())).filter(el->el.equals(uo))).isPresent()) {
					if (this.isFlPrgPianoEconomico() &&
							((optProgetto.get().isPianoEconomicoRequired() &&
									optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField())).flatMap(el -> Optional.ofNullable(el.getDtInizio())).isPresent() &&
									optProgetto.flatMap(el->Optional.ofNullable(el.getOtherField())).flatMap(el -> Optional.ofNullable(el.getDtFine())).isPresent()) ||
									(optProgetto.get().isDettagliPianoEconomicoPresenti() &&
											Optional.ofNullable(this.getAnnoFromPianoEconomico()).map(el -> el.compareTo(CNRUserContext.getEsercizio(HttpActionContext.getUserContext(session))) <= 0)
													.orElse(Boolean.FALSE))))
						hash.put(i++, new String[]{"tabPianoEconomico", "Piano Economico", "/progettiric00/rimodula_progetto_piano_economico.jsp"});
				}
			}

			//Se su una rimodulazione definitiva è prevista la creazione di variazioni visualizzo la tab corrispondente
			if (Optional.ofNullable(progettoRimodulazione).filter(el->el.isStatoValidato() || el.isStatoApprovato())
					.flatMap(el -> Optional.ofNullable(el.getVariazioniAssociate())).filter(el -> !el.isEmpty())
					.isPresent())
				hash.put(i++, new String[]{"tabVariazioniAss", "Variazioni Associate", "/progettiric00/tab_ass_progetto_rimod_variazioni.jsp"});

			hash.put(i++, new String[]{"tabAllegati","Allegati","/util00/tab_allegati.jsp" });

			if ((uoScrivania.isUoEnte() && !progettoRimodulazione.isStatoProvvisorio()) || progettoRimodulazione.isStatoRespinto())
				hash.put(i++, new String[]{"tabAnnotazioni","Annotazioni","/progettiric00/tab_annotazioni_progetto_rimod.jsp" });
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
			
		if (!annoInizio.equals(optProgetto.get().getEsercizio()) || !annoFine.equals(optProgetto.get().getEsercizio()) ||
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

	@Override
	public void setTab(String tabName, String pageName) {
		super.setTab(tabName, pageName);
		if (!isSearching() &&
				Optional.ofNullable(uoScrivania).map(Unita_organizzativaBulk::isUoEnte).orElse(Boolean.FALSE)) {
			if (pageName.equalsIgnoreCase("tabAnnotazioni") &&
					Optional.ofNullable(getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast)
						.filter(el->el.isStatoDefinitivo()||el.isStatoValidato())
						.isPresent())
				setStatus(EDIT);
			else
				setStatus(VIEW);
		}
	}

	@Override
	public boolean isSaveButtonEnabled() {
		return super.isSaveButtonEnabled() ||
				(Optional.ofNullable(uoScrivania).map(Unita_organizzativaBulk::isUoEnte).orElse(Boolean.FALSE) &&
						Optional.ofNullable(getModel())
								.filter(Progetto_rimodulazioneBulk.class::isInstance)
								.map(Progetto_rimodulazioneBulk.class::cast)
								.filter(el->el.isStatoDefinitivo()||el.isStatoValidato())
								.isPresent()
				);
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
		try {
	    	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
			return comp.rebuildRimodulazione(actioncontext.getUserContext(), rimodulazione);
		} catch (Throwable throwable) {
			throw new BusinessProcessException(throwable);
		}
	}
	
	public void initializeAssProgettoPiaecoVoce(ActionContext actioncontext, Ass_progetto_piaeco_voceBulk assProgettoPiaecoVoce) {
		ProgettoBulk progetto = ((Progetto_rimodulazioneBulk)this.getModel()).getProgetto();
		assProgettoPiaecoVoce.initializeSaldo(progetto.getVociBilancioMovimentate());
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
					dett.initializeSaldo(progetto.getVociBilancioMovimentate());
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
		return true;
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
	 * Restituisce il valore della proprietà 'riportaDefinitivoButtonHidden'
	 * Il bottone di RiportaDefinitivo è disponibile solo se:
	 * - la proposta è validata o respinta
	 * - la UO collegata è la UoEnte
	 *
	 * @return Il valore della proprietà 'riportaDefinitivoButtonHidden'
	 */
	public boolean isRiportaDefinitivoButtonHidden() {
		Optional<Progetto_rimodulazioneBulk> optModel =
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

		return optModel.filter(el->!el.isStatoValidato()&&!el.isStatoRespinto()).isPresent() ||
				optModel.flatMap(el->Optional.ofNullable(el.getVariazioniAssociate())).filter(el->!el.isEmpty()).isPresent() ||
				!uoScrivania.isUoEnte();
	}

	/**
	 * Restituisce il valore della proprietà 'riportaProvvisorioButtonHidden'
	 * Il bottone di RiportaProvvisorio è disponibile solo se:
	 * - la proposta è definitiva
	 * - la UO collegata è la UoEnte
	 *
	 * @return Il valore della proprietà 'riportaProvvisorioButtonHidden'
	 */
	public boolean isRiportaProvvisorioButtonHidden() {
		Optional<Progetto_rimodulazioneBulk> optModel =
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

		return optModel.filter(el->!el.isStatoDefinitivo()).isPresent() ||
				!uoScrivania.isUoEnte();
	}

    /**
     * Restituisce il valore della proprietà 'validaButtonHidden'
     * Il bottone di Valida è disponibile solo se:
     * - la proposta di rimodulazione è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'validaButtonHidden'
     */
    public boolean isValidaButtonHidden() {
		Optional<Progetto_rimodulazioneBulk> optModel = 
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

        return !optModel.filter(Progetto_rimodulazioneBulk::isStatoDefinitivo).isPresent() ||
               !uoScrivania.isUoEnte();
    }

    /**
     * Restituisce il valore della proprietà 'respingiButtonHidden'
     * Il bottone di Respingi è disponibile solo se:
     * - la proposta di variazione PDG è definitiva
     * - la UO che sta effettuando l'operazione è di tipo ENTE
     *
     * @return Il valore della proprietà 'respingiButtonHidden'
     */
    public boolean isRespingiButtonHidden() {
		Optional<Progetto_rimodulazioneBulk> optModel = 
				Optional.ofNullable(this.getModel())
						.filter(Progetto_rimodulazioneBulk.class::isInstance)
						.map(Progetto_rimodulazioneBulk.class::cast);

        return !optModel.filter(Progetto_rimodulazioneBulk::isStatoDefinitivo).isPresent() ||
               !uoScrivania.isUoEnte();
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
     * Gestione del salvataggio come validata di una rimodulazione
     *
     * @param context L'ActionContext della richiesta
     * @throws BusinessProcessException
     */
    public void valida(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
        try {
        	this.save(context);
        	RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
        	Progetto_rimodulazioneBulk bulk = comp.valida(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
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

	/**
	 * Gestione del riporto a definitivo di una rimodulazione respinta/validata
	 *
	 * @param context L'ActionContext della richiesta
	 * @throws BusinessProcessException
	 */
	public void riportaDefinitivo(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
		try {
			this.save(context);
			RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
			Progetto_rimodulazioneBulk bulk = comp.riportaDefinitivo(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
			edit(context, bulk);
		} catch (it.cnr.jada.comp.ComponentException ex) {
			throw handleException(ex);
		} catch (java.rmi.RemoteException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * Gestione del riporto a provvisorio di una rimodulazione defintiva
	 *
	 * @param context L'ActionContext della richiesta
	 * @throws BusinessProcessException
	 */
	public void riportaProvvisorio(ActionContext context) throws it.cnr.jada.action.BusinessProcessException, ValidationException {
		try {
			this.save(context);
			RimodulaProgettoRicercaComponentSession comp = (RimodulaProgettoRicercaComponentSession) createComponentSession();
			Progetto_rimodulazioneBulk bulk = comp.riportaProvvisorio(context.getUserContext(), (Progetto_rimodulazioneBulk) getModel());
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

		if (!Optional.ofNullable(optPpe.get().getImSpesaFinanziatoRimodulato()).isPresent())
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore nullo.");

		if (!Optional.ofNullable(optPpe.get().getImSpesaCofinanziatoRimodulato()).isPresent())
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore nullo.");

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore negativo.");

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0 && 
				optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0)
			throw new ValidationException("Operazione non possibile! I campi importo finanziato e cofinanziato non possono assumere entrambi valore 0.");

		//Calcolo il valore minimo al di sotto del quale non si può andare
		BigDecimal totaleUtilizzato = optPpe.get().getVociBilancioAssociate().stream()
			  		.filter(el->!isAddVoceBilancio||Optional.ofNullable(el.getElemento_voce()).isPresent())
			  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
			  		.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
			  		.map(el->Optional.ofNullable(el.getUtilizzatoAssestatoFinanziamento()).orElse(BigDecimal.ZERO))
			  		.reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO);
		
		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(totaleUtilizzato)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore inferiore"
					+ " all'importo già utilizzato ("
					+ new it.cnr.contab.util.EuroFormat().format(totaleUtilizzato)
					+ ") su voci di bilancio associate obbligatoriamente alla voce di piano economico corrispondente.");
    }
    
    @Override
	protected void completeAllegato(AllegatoProgettoRimodulazioneBulk allegato) throws ApplicationException {
    	super.completeAllegato(allegato);
		allegato.setDaNonEliminare(Boolean.FALSE);
    	if (Optional.ofNullable(this.getModel()).filter(Progetto_rimodulazioneBulk.class::isInstance).map(Progetto_rimodulazioneBulk.class::cast)
    			.filter(el->el.isStatoProvvisorio())
    			.isPresent()){
    		if (allegato.isProroga() || allegato.isStampaAutomatica())
    			allegato.setDaNonEliminare(Boolean.TRUE);
    	} 
	}
    
    public void validaImportoCofinanziatoRimodulato(ActionContext actioncontext, Optional<Progetto_piano_economicoBulk> optPpe) throws ValidationException {
        boolean isAddVoceBilancio = optPpe.flatMap(el->Optional.ofNullable(el.getVoce_piano_economico()))
										.map(Voce_piano_economico_prgBulk::getFl_add_vocibil)
										.orElse(Boolean.FALSE);

		if (!Optional.ofNullable(optPpe.get().getImSpesaFinanziatoRimodulato()).isPresent())
			throw new ValidationException("Operazione non possibile! Il campo importo finanziato non può assumere un valore nullo.");

		if (!Optional.ofNullable(optPpe.get().getImSpesaCofinanziatoRimodulato()).isPresent())
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore nullo.");

		if (optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore negativo.");

		if (optPpe.get().getImSpesaFinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0 && 
				optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(BigDecimal.ZERO)==0)
			throw new ValidationException("Operazione non possibile! I campi importo finanziato e cofinanziato non possono assumere entrambi valore 0.");

		//Calcolo il valore minimo al di sotto del quale non si può andare
		BigDecimal totaleUtilizzato = optPpe.get().getVociBilancioAssociate().stream()
			  		.filter(el->!isAddVoceBilancio||Optional.ofNullable(el.getElemento_voce()).isPresent())
			  		.filter(el->Elemento_voceHome.GESTIONE_SPESE.equals(el.getTi_gestione()))
			  		.filter(el->Optional.ofNullable(el.getSaldoSpesa()).isPresent())
			  		.map(Ass_progetto_piaeco_voceBulk::getSaldoSpesa)
			  		.map(el->Optional.ofNullable(el.getUtilizzatoAssestatoCofinanziamento()).orElse(BigDecimal.ZERO))
			  		.reduce((x, y)->x.add(y)).orElse(BigDecimal.ZERO);
  		
		if (optPpe.get().getImSpesaCofinanziatoRimodulato().compareTo(totaleUtilizzato)<0)
			throw new ValidationException("Operazione non possibile! Il campo importo cofinanziato non può assumere un valore inferiore"
					+ " all'importo già utilizzato ("
					+ new it.cnr.contab.util.EuroFormat().format(totaleUtilizzato)
					+ ") su voci di bilancio associate obbligatoriamente alla voce di piano economico corrispondente.");
	}
    
    @Override
    protected Boolean isPossibileCancellazione(AllegatoGenericoBulk allegato) {
    	return (Optional.ofNullable(this.getModel()).filter(Progetto_rimodulazioneBulk.class::isInstance)
    			.map(Progetto_rimodulazioneBulk.class::cast)
    			.map(el->!el.isROFieldRimodulazione())
    			.orElse(Boolean.TRUE) ||
				Optional.ofNullable(allegato).map(el->el.isToBeCreated()).orElse(Boolean.TRUE)) &&
				super.isPossibileCancellazione(allegato);
    }
    
    @Override
    protected Boolean isPossibileModifica(AllegatoGenericoBulk allegato) {
    	return (Optional.ofNullable(this.getModel()).filter(Progetto_rimodulazioneBulk.class::isInstance)
    			.map(Progetto_rimodulazioneBulk.class::cast)
    			.map(el->!el.isROFieldRimodulazione())
    			.orElse(Boolean.TRUE) ||
				Optional.ofNullable(allegato).map(el->el.isToBeCreated()).orElse(Boolean.TRUE)) &&
				super.isPossibileModifica(allegato);
    }
    
    @Override
    public RemoteIterator find(ActionContext actioncontext, CompoundFindClause compoundfindclause,
    		OggettoBulk oggettobulk) throws BusinessProcessException {
    	Optional.ofNullable(oggettobulk).filter(Progetto_rimodulazioneBulk.class::isInstance)
		.map(Progetto_rimodulazioneBulk.class::cast).ifPresent(el->el.setStato(null));
    	return super.find(actioncontext, compoundfindclause, oggettobulk);
    }

	public Unita_organizzativaBulk getUoScrivania() {
		return uoScrivania;
	}

	@Override
	public String getAllegatiFormName() {
    	String formName = super.getAllegatiFormName();
    	if ("default".equals(formName) && Optional.ofNullable(this.getArchivioAllegati().getModel()).map(el->el.isNew()).orElse(Boolean.TRUE))
    		return "insert";
    	return formName;
	}
}