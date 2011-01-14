/*
 * Created on Sep 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.bp;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.List;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import it.cnr.contab.config00.ejb.Parametri_livelliComponentSession;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
import it.cnr.contab.prevent01.bulk.Pdg_contrattazione_speseBulk;
import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_speseBulk;
import it.cnr.contab.prevent01.ejb.PdgContrSpeseComponentSession;
import it.cnr.contab.prevent01.ejb.PdgModuloCostiComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.BulkInfo;
import it.cnr.jada.bulk.ColumnFieldProperty;
import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.CRUDBP;
import it.cnr.jada.util.action.FormController;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;
import it.cnr.jada.util.jsp.JSPUtils;
import it.cnr.jada.util.jsp.TableCustomizer;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDDettagliModuloCostiBP extends SimpleCRUDBP {

	private Unita_organizzativaBulk uoSrivania;
	private CrudDettagliSpeseBP crudDettagliSpese = new CrudDettagliSpeseBP( "DettagliSpese", Pdg_modulo_speseBulk.class, "dettagliSpese", this);
	private Integer livelloContrattazione;
	private Boolean pdgApprovatoDefinitivo;

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
			setDescrizioneClassificazione(createParametriLivelliComponentSession().getDescrizioneLivello(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext()),Utility.TIPO_GESTIONE_SPESA));
			setDescrizioneClassificazioneContrSpese(createParametriLivelliComponentSession().getDescrizioneLivelloContrSpese(actioncontext.getUserContext(),CNRUserContext.getEsercizio(actioncontext.getUserContext())));
			setPdgApprovatoDefinitivo(Utility.createPdgContrSpeseComponentSession().isApprovatoDefinitivo(actioncontext.getUserContext()));
			setLivelloContrattazione(Utility.createPdgContrSpeseComponentSession().livelloContrattazioneSpese(actioncontext.getUserContext()));
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
		String label = "<SPAN style=\""+getBulkInfo().getFieldProperty("label_titolo").getLabelStyle()+"\">";
		label += getBulkInfo().getFieldProperty("label_titolo").getLabel() + "</SPAN>";
		openToolbar(jspwriter);
		JSPUtils.toolbar(jspwriter, abutton, this,label);
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
}
