/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_livelliComponentSession;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgContrSpeseComponentSession;
import it.cnr.contab.prevent01.ejb.PdgModuloCostiComponentSession;
import it.cnr.contab.progettiric00.bp.ProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDDettagliModuloCostiBP extends SimpleCRUDBP {

	private Parametri_enteBulk parametriEnte;
	private Unita_organizzativaBulk uoSrivania;
	private CrudDettagliSpeseBP crudDettagliSpese = new CrudDettagliSpeseBP( "DettagliSpese", Pdg_modulo_speseBulk.class, "dettagliSpese", this) {
	   protected void validate(ActionContext actioncontext, it.cnr.jada.bulk.OggettoBulk oggettobulk) throws it.cnr.jada.bulk.ValidationException {
		   if (getParametriEnte().getFl_prg_pianoeco() && ((Pdg_modulo_speseBulk)oggettobulk).getVoce_piano_economico()==null) {
				Progetto_sipBulk progetto = ((Pdg_modulo_speseBulk)oggettobulk).getPdg_modulo_costi().getPdg_modulo().getProgetto();
				if (progetto.isPianoEconomicoRequired())
					throw new it.cnr.jada.bulk.ValidationException("Il progetto selezionato richiede l'indicazione della Voce del Piano Economico.");
		   }
	   };
	};

	private Integer livelloContrattazione;
	private Boolean pdgApprovatoDefinitivo;
    private boolean cofogObb;
	private boolean flNuovoPdg = false;
	private boolean flPdgCodlast = false;

	private SimpleDetailCRUDController crudDettagliContrSpese = new SimpleDetailCRUDController( "DettagliContrSpese", Pdg_contrattazione_speseBulk.class, "dettagliContrSpese", this, false) {
		public boolean isFiltered()
		{
			return false;
		}
		public boolean isReadonly()
		{
			return super.isReadonly() && !isContrSpeseAggiornabile();
		}
		public boolean isGrowable()
		{
			return false;	
		}
		public boolean isShrinkable()
		{
			return false;	
		}
	};

	private SimpleDetailCRUDController pianoEconomicoAnnoCorrente = new SimpleDetailCRUDController( "PianoEconomicoAnnoCorrente", Progetto_piano_economicoBulk.class, "dettagliPianoEconomicoAnnoCorrente", this);
	private SimpleDetailCRUDController pianoEconomicoVoceBilancioAnnoCorrente = new ProgettoPianoEconomicoVoceBilancioCRUDController( "PianoEconomicoVoceBilancioAnnoCorrente", Ass_progetto_piaeco_voceBulk.class, "vociBilancioAssociate", pianoEconomicoAnnoCorrente);

	private String anno_corrente,anno_precedente,anno_successivo,anno_successivo_successivo;
	private Pdg_moduloBulk pdg_modulo;
	private Pdg_esercizioBulk pdg_esercizio;
	private String descrizioneClassificazione;
	private String descrizioneClassificazioneContrSpese;
	public CRUDDettagliModuloCostiBP() {
		super();
	}

	public CRUDDettagliModuloCostiBP(String s) {
		super(s);
	}
	public CRUDDettagliModuloCostiBP(String function, Pdg_moduloBulk pdg_modulo) {
		super(function);
		setPdg_modulo(pdg_modulo);
	}	
	protected void resetTabs(ActionContext actioncontext) {
		setTab("tab","tabTotali");
	}
	protected void initialize(ActionContext actioncontext) throws BusinessProcessException {
		
		anno_corrente = CNRUserContext.getEsercizio(actioncontext.getUserContext()).toString();
		anno_precedente = new Integer(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue() - 1).toString();
		anno_successivo = new Integer(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue() + 1).toString();
		anno_successivo_successivo = new Integer(CNRUserContext.getEsercizio(actioncontext.getUserContext()).intValue() + 2).toString();

		Pdg_modulo_costiBulk pdg_modulo = new Pdg_modulo_costiBulk(getPdg_modulo());
		PdgModuloCostiComponentSession session = (PdgModuloCostiComponentSession)createComponentSession();
		try {
			if (session.esisteBulk(actioncontext.getUserContext(),pdg_modulo)){
				setModel(actioncontext,initializeModelForEdit(actioncontext,pdg_modulo));
				setStatus(EDIT);				
			}
			else
				setModel(actioncontext,initializeModelForInsert(actioncontext,pdg_modulo));   
		}catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		}
		setUoSrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(actioncontext));
		setPdg_esercizio(cercaPdg_esercizio(actioncontext));
		
		if (!isEditable())
		  setStatus(VIEW);	
		resetTabs(actioncontext);
		try {
			Parametri_livelliComponentSession parametriLivelliSession = createParametriLivelliComponentSession();
			setDescrizioneClassificazione(parametriLivelliSession.getDescrizioneLivello(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext()),Utility.TIPO_GESTIONE_SPESA));
			setDescrizioneClassificazioneContrSpese(parametriLivelliSession.getDescrizioneLivelloContrSpese(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext())));

			PdgContrSpeseComponentSession pdgContrSpeseSession = Utility.createPdgContrSpeseComponentSession();
			setPdgApprovatoDefinitivo(pdgContrSpeseSession.isApprovatoDefinitivo(actioncontext.getUserContext()));
			setLivelloContrattazione(pdgContrSpeseSession.livelloContrattazioneSpese(actioncontext.getUserContext()));

			Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
			setCofogObb(parCnr.isCofogObbligatorio());
			setFlNuovoPdg(parCnr.getFl_nuovo_pdg().booleanValue());
			setFlPdgCodlast(parCnr.getFl_pdg_codlast().booleanValue());
			
			setParametriEnte(Utility.createParametriEnteComponentSession().getParametriEnte(actioncontext.getUserContext()));
			
		}catch (ComponentException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (PersistencyException e) {
			throw new BusinessProcessException(e);
		} 		
	}
	public OggettoBulk initializeModelForInsert(ActionContext actioncontext,OggettoBulk oggettobulk)throws BusinessProcessException {
		Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)oggettobulk;
		pdg_modulo_costi.setPdg_modulo(getPdg_modulo());
		return super.initializeModelForInsert(actioncontext, pdg_modulo_costi);
	}
	protected void writeToolbar(JspWriter jspwriter, Button[] abutton) throws IOException, ServletException {
		String field_titolo = this.isFlNuovoPdg()?"label_titolo_nuovo_pdg":"label_titolo";
		String label = "<SPAN style=\""+getBulkInfo().getFieldProperty(field_titolo).getLabelStyle()+"\">";
		label += getBulkInfo().getFieldProperty(field_titolo).getLabel() + "</SPAN>";
		openToolbar(jspwriter);
		JSPUtils.toolbar(jspwriter, abutton, this,label, this.getParentRoot().isBootstrap());
		closeToolbar(jspwriter);
	}

	public Parametri_livelliComponentSession createParametriLivelliComponentSession()throws BusinessProcessException {
		return (Parametri_livelliComponentSession)createComponentSession("CNRCONFIG00_EJB_Parametri_livelliComponentSession", Parametri_livelliComponentSession.class);		
	}
	public void delete(ActionContext actioncontext)throws BusinessProcessException {
		super.delete(actioncontext);
		Pdg_modulo_costiBulk pdg_modulo_costi = new Pdg_modulo_costiBulk(getPdg_modulo());
		setModel(actioncontext,initializeModelForInsert(actioncontext,pdg_modulo_costi));		
	}

	/**
	 * @return
	 */
	public CrudDettagliSpeseBP getCrudDettagliSpese() {
		return crudDettagliSpese;
	}

	/**
	 * @param controller
	 */
	public void setCrudDettagliSpese(CrudDettagliSpeseBP controller) {
		crudDettagliSpese = controller;
	}
	/**
	 * @return
	 */
	public String getDescrizioneClassificazione() {
		return descrizioneClassificazione;
	}

	/**
	 * @param string
	 */
	public void setDescrizioneClassificazione(String string) {
		descrizioneClassificazione = string;
	}
	public boolean isNewButtonHidden() {
		return true;
	}
	public boolean isSearchButtonHidden() {
		return true;
	}
	public boolean isFreeSearchButtonHidden() {
		return true;
	}

	/**
	 * @return
	 */
	public Pdg_moduloBulk getPdg_modulo() {
		return pdg_modulo;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_modulo(Pdg_moduloBulk bulk) {
		pdg_modulo = bulk;
	}
	public static CostiDipendenteComponentSession getCostiDipendenteComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (CostiDipendenteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPDG00_EJB_CostiDipendenteComponentSession",CostiDipendenteComponentSession.class);
	}
    public boolean isCostiDipendenteRipartiti(ActionContext actioncontext) throws BusinessProcessException{
    	Pdg_modulo_costiBulk pdg_modulo_costi = (Pdg_modulo_costiBulk)getModel();
    	try {
			return getCostiDipendenteComponentSession().isModuloUtilizzato(actioncontext.getUserContext(),pdg_modulo_costi.getPdg_modulo().getCdr(),pdg_modulo_costi.getPdg_modulo().getProgetto());
		}catch (PersistencyException e) {
			throw new BusinessProcessException(e);
		} catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
    }
	/**
	 * @return
	 */
	public SimpleDetailCRUDController getCrudDettagliContrSpese() {
		return crudDettagliContrSpese;
	}

	/**
	 * @param controller
	 */
	public void setCrudDettagliContrSpese(SimpleDetailCRUDController controller) {
		crudDettagliContrSpese = controller;
	}

	public boolean isContrSpeseAggiornabile() {
		CRUDBP bpp = ((CRUDBP)getParent());
		if (isUoEnte() && 
			bpp.getStatus()!=VIEW && 
			getPdg_esercizio().getStato().equals(Pdg_esercizioBulk.STATO_IN_ESAME_CDR) &&
			!isPdgApprovatoDefinitivo())
			return true;

		return false;
	}

	public boolean isSaveButtonEnabled() {
		if (isContrSpeseAggiornabile())
			return true;

		return super.isSaveButtonEnabled();
	}

	public Unita_organizzativaBulk getUoSrivania() {
		return uoSrivania;
	}

	public void setUoSrivania(Unita_organizzativaBulk bulk) {
		uoSrivania = bulk;
	}
	public Pdg_esercizioBulk cercaPdg_esercizio(ActionContext actioncontext) throws BusinessProcessException{
		try {
			PdgModuloCostiComponentSession session = (PdgModuloCostiComponentSession)createComponentSession();
			return (Pdg_esercizioBulk) session.cercaPdgEsercizio(actioncontext.getUserContext(),getPdg_modulo());
		}catch (RemoteException e) {
			throw new BusinessProcessException(e);
		} catch (ComponentException e) {
			throw new BusinessProcessException(e);
		}
	}

	public boolean isContrSpeseEnabled() {
		return isContrSpeseAggiornabile() && isSaveButtonEnabled();
	}

	public boolean isUoEnte(){
		return (getUoSrivania().getCd_tipo_unita().compareTo(it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0);
	}

	/**
	 * @return
	 */
	public Pdg_esercizioBulk getPdg_esercizio() {
		return pdg_esercizio;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_esercizio(Pdg_esercizioBulk bulk) {
		pdg_esercizio = bulk;
	}

	/**
	 * @return
	 */
	public String getDescrizioneClassificazioneContrSpese() {
		return descrizioneClassificazioneContrSpese;
	}

	/**
	 * @param string
	 */
	public void setDescrizioneClassificazioneContrSpese(String string) {
		descrizioneClassificazioneContrSpese = string;
	}
	
    public String getLabelTot_massa_spendibile_anno_prec(){
    	return "Massa Spendibile presunta "+ anno_precedente;
    }
	public String getLabelLabel_risorse_es_prec(){
		return "Risorse provenienti da<BR>esercizi precedenti al "+ anno_precedente;
	}
	public String getLabelLabel_risorse_presunte_es_prec(){
		return "Risorse presunte<BR>provenienti dal "+ anno_precedente;
	}
	public String getLabelTot_massa_spendibile_anno_in_corso(){
		return "Massa Spendibile presunta "+ anno_corrente;
	}
	public String getLabelValore_presunto_anno_in_corso(){
		return "Valore presunto delle attività "+ anno_corrente;
	}
	public String getLabelTot_entr_fonti_est_anno_in_corso(){
		return "Entrate da fonti esterne<BR>previste per il "+ anno_corrente;
	}
	public String getLabelTot_spese_coperte_fonti_esterne_anno_in_corso(){
		return "Spese coperte da fonti Esterne<BR>previste per il "+ anno_corrente;
	}
    
	/**
	 * @return
	 */
	public String getAnno_corrente() {
		return anno_corrente;
	}

	/**
	 * @return
	 */
	public String getAnno_precedente() {
		return anno_precedente;
	}

	/**
	 * @return
	 */
	public String getAnno_successivo() {
		return anno_successivo;
	}

	/**
	 * @return
	 */
	public String getAnno_successivo_successivo() {
		return anno_successivo_successivo;
	}
	public Boolean isPdgApprovatoDefinitivo() {
		return pdgApprovatoDefinitivo;
	}

	private void setPdgApprovatoDefinitivo(Boolean pdgApprovatoDefinitivo) {
		this.pdgApprovatoDefinitivo = pdgApprovatoDefinitivo;
	}

	public Integer getLivelloContrattazione() {
		return livelloContrattazione;
	}

	private void setLivelloContrattazione(Integer livelloContrattazione) {
		this.livelloContrattazione = livelloContrattazione;
	}

	public boolean isCofogObb() {
		return cofogObb;
	}

	public void setCofogObb(boolean cofogObb) {
		this.cofogObb = cofogObb;
	}

	public boolean isFlNuovoPdg() {
		return flNuovoPdg;
	}

	private void setFlNuovoPdg(boolean flNuovoPdg) {
		this.flNuovoPdg = flNuovoPdg;
	}
 
	public boolean isDeleteModuloButtonHidden() {
		return super.isDeleteButtonHidden() || this.isFlNuovoPdg();
	}

	public boolean isDeleteProgettoButtonHidden() {
		return super.isDeleteButtonHidden() || !this.isFlNuovoPdg();
	}
	
	public boolean isFlPdgCodlast() {
		return flPdgCodlast;
	}
	
	public void setFlPdgCodlast(boolean flPdgCodlast) {
		this.flPdgCodlast = flPdgCodlast;
	}
	
	public void setParametriEnte(Parametri_enteBulk parametriEnte) {
		this.parametriEnte = parametriEnte;
	}

	public Parametri_enteBulk getParametriEnte() {
		return parametriEnte;
	}
	
	public void inizializzaVoceEconomica(ActionContext actioncontext, Pdg_modulo_speseBulk pdg_modulo_spese, V_classificazione_vociBulk classificazione) throws BusinessProcessException {
		try {
	    	java.util.Collection<Progetto_piano_economicoBulk> result = createComponentSession().find(actioncontext.getUserContext(), Progetto_piano_economicoBulk.class, "findProgettoPianoEconomicoList", classificazione.getEsercizio(), pdg_modulo_spese.getPg_progetto(), classificazione.getId_classificazione());
	    	if (Optional.ofNullable(result).orElse(Collections.emptyList()).isEmpty())
	    		throw new ValidationException("Voce di bilancio non associata a nessuna voce del piano economico del progetto.");
	    	if (Optional.ofNullable(result).orElse(Collections.emptyList()).size()==1) {
	    		Voce_piano_economico_prgBulk vocePiaeco = result.stream().findAny().map(Progetto_piano_economicoBulk::getVoce_piano_economico).get();
	    		vocePiaeco = (Voce_piano_economico_prgBulk)createComponentSession().findByPrimaryKey(actioncontext.getUserContext(), vocePiaeco);
	    		pdg_modulo_spese.setVoce_piano_economico(vocePiaeco);
	    	}
		} catch (Exception e) {
			throw new BusinessProcessException(e);
		}
	}
	
	public SimpleDetailCRUDController getPianoEconomicoAnnoCorrente() {
		return pianoEconomicoAnnoCorrente;
	}
	
	public SimpleDetailCRUDController getPianoEconomicoVoceBilancioAnnoCorrente() {
		return pianoEconomicoVoceBilancioAnnoCorrente;
	}

	public String[][] getTabs(HttpSession session) {
		TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
		int i=0;

		hash.put(i++, new String[]{"tabTotali","<SPAN style='font : bold 13px;'>Totali</SPAN>","/prevent01/tab_modulo_totali.jsp" });
		hash.put(i++, new String[]{"tabSpese","<SPAN style='font : bold 13px;'>Previsione di Impegno</SPAN>","/prevent01/tab_modulo_spese.jsp" });
		hash.put(i++, new String[]{"tabRisorse","<SPAN style='font : bold 13px;'>Risorse provenienti da esercizi precedenti</SPAN>","/prevent01/tab_modulo_risorse.jsp" });
		hash.put(i++, new String[]{"tabCosti","<SPAN style='font : bold 13px;'>Costi Generali e Figurativi</SPAN>","/prevent01/tab_modulo_costi.jsp" });
		
	    if (this.isFlNuovoPdg() && this.getParametriEnte().getFl_prg_pianoeco())
    		hash.put(i++, new String[]{"tabPianoEconomico","<SPAN style='font : bold 13px;'>Piano Economico</SPAN>","/prevent01/tab_modulo_piaeco.jsp" });

	    String[][] tabs = new String[i][3];
		for (int j = 0; j < i; j++)
			tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
		return tabs;
	}
}
