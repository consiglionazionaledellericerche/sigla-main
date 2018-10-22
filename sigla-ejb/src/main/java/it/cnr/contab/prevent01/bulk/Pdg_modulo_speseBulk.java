/*
* Created by Generator 1.0
* Date 29/09/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import it.cnr.contab.config00.latt.bulk.CofogBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.pdg01.bulk.Pdg_modulo_spese_gestBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_modulo_speseBulk extends Pdg_modulo_speseBase {
	private Pdg_modulo_costiBulk pdg_modulo_costi;
	private V_classificazione_vociBulk classificazione;
	private CdsBulk area;
	private BigDecimal prev_ass_imp_int;
	private BigDecimal prev_ass_imp_est;
	private boolean speseScaricateDalPersonale;
	private SimpleBulkList dettagli_gestionali = new SimpleBulkList();
	private java.math.BigDecimal totale_spese_decentrate_interne_gest;
	private java.math.BigDecimal totale_spese_decentrate_esterne_gest;
	private java.math.BigDecimal totale_spese_accentrate_interne_gest;
	private java.math.BigDecimal totale_spese_accentrate_esterne_gest;
	private boolean limiteInt=false;
	private boolean limiteEst=false;
	private boolean prevAnnoSucObb=false;
	private CofogBulk cofog;
	private Pdg_missioneBulk pdgMissione;
	private Voce_piano_economico_prgBulk voce_piano_economico;

	public Pdg_modulo_speseBulk() {
		super();
	}
	public Pdg_modulo_speseBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita, java.lang.Integer pg_progetto, java.lang.Integer id_classificazione, java.lang.String cd_cds_area,Integer pg_dettaglio) {
		super(esercizio, cd_centro_responsabilita, pg_progetto, id_classificazione, cd_cds_area,pg_dettaglio);
		setPdg_modulo_costi(new Pdg_modulo_costiBulk(esercizio, cd_centro_responsabilita, pg_progetto));
		setClassificazione(new V_classificazione_vociBulk(id_classificazione));
		setArea(new CdsBulk(cd_cds_area));
	}
	/**
	 * @return
	 */
	public CdsBulk getArea() {
		return area;
	}

	/**
	 * @return
	 */
	public V_classificazione_vociBulk getClassificazione() {
		return classificazione;
	}

	/**
	 * @return
	 */
	public Pdg_modulo_costiBulk getPdg_modulo_costi() {
		return pdg_modulo_costi;
	}

	/**
	 * @param bulk
	 */
	public void setArea(CdsBulk bulk) {
		area = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setClassificazione(V_classificazione_vociBulk bulk) {
		classificazione = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setPdg_modulo_costi(Pdg_modulo_costiBulk bulk) {
		pdg_modulo_costi = bulk;
	}

	public void setEsercizio(java.lang.Integer esercizio)  {
		getPdg_modulo_costi().setEsercizio(esercizio);
	}
	public java.lang.Integer getEsercizio () {
		if(getPdg_modulo_costi() != null)
		  return getPdg_modulo_costi().getEsercizio();
		return null;  
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		getPdg_modulo_costi().setCd_centro_responsabilita(cd_centro_responsabilita);
	}
	public java.lang.String getCd_centro_responsabilita () {
		if(getPdg_modulo_costi() != null)
		  return getPdg_modulo_costi().getCd_centro_responsabilita();
		return null;  
	}
	public void setPg_progetto(java.lang.Integer pg_progetto)  {
		getPdg_modulo_costi().setPg_progetto(pg_progetto);
	}
	public java.lang.Integer getPg_progetto () {
		if(getPdg_modulo_costi() != null)
		  return getPdg_modulo_costi().getPg_progetto();
		return null;  
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		getClassificazione().setId_classificazione(id_classificazione);
	}
	public java.lang.Integer getId_classificazione () {
		if(getClassificazione() != null)
		  return getClassificazione().getId_classificazione();
		return null;  
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		if(getArea() != null)
		   getArea().setCd_proprio_unita(cd_cds_area);
		super.setCd_cds_area(cd_cds_area);   
	}
	public java.lang.String getCd_cds_area () {
		if(getArea() != null)
		  return getArea().getCd_proprio_unita();
		return null;  
	}
	public BigDecimal getTot_gest_decentrata() {
		return Utility.nvl(getIm_spese_gest_decentrata_est()).add(Utility.nvl(getIm_spese_gest_decentrata_int()));
	}
	public BigDecimal getTot_gest_accentrata() {
		return Utility.nvl(getIm_spese_gest_accentrata_est()).add(Utility.nvl(getIm_spese_gest_accentrata_int()));
	}
	public BigDecimal getTot_competenza_anno_in_corso() {
		return Utility.nvl(getTot_gest_accentrata()).add(Utility.nvl(getTot_gest_decentrata()));
	}
	public OggettoBulk initializeForInsert(CRUDBP crudbp,ActionContext actioncontext) {
		inizializzaImporti();		
		return super.initializeForInsert(crudbp, actioncontext);
	}	
	private void inizializzaImporti(){
		setIm_spese_a2(Utility.ZERO);
		setIm_spese_a3(Utility.ZERO);
		setIm_spese_gest_accentrata_est(Utility.ZERO);
		setIm_spese_gest_accentrata_int(Utility.ZERO);
		setIm_spese_gest_decentrata_est(Utility.ZERO);
		setIm_spese_gest_decentrata_int(Utility.ZERO);
	}
	public boolean isClassificazioneRO(){		
		return getCrudStatus() != 1;
	}
	public boolean isAreaRO(){		
		return getCrudStatus() != 1;
	}	
	/**
	 * @return
	 */
	public BigDecimal getPrev_ass_imp_est() {
		return prev_ass_imp_est;
	}

	/**
	 * @return
	 */
	public BigDecimal getPrev_ass_imp_int() {
		return prev_ass_imp_int;
	}

	/**
	 * @param decimal
	 */
	public void setPrev_ass_imp_est(BigDecimal decimal) {
		prev_ass_imp_est = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setPrev_ass_imp_int(BigDecimal decimal) {
		prev_ass_imp_int = decimal;
	}
	public BigDecimal getTot_prev_ass_imp() {
		return Utility.nvl(getPrev_ass_imp_est()).add(Utility.nvl(getPrev_ass_imp_int()));
	}
	public boolean isGestioneAccentrataDisable(){
		return (getClassificazione() != null &&
			    getClassificazione().getId_classificazione() != null &&
			   ((getClassificazione().getFl_accentrato().booleanValue() &&
			     getClassificazione().getCentro_responsabilita() != null &&
			     getClassificazione().getCentro_responsabilita().getCd_centro_responsabilita() != null &&
			     getClassificazione().getCentro_responsabilita().equalsByPrimaryKey(getPdg_modulo_costi().getPdg_modulo().getCdr()))
			     ||(!getClassificazione().getFl_accentrato().booleanValue())));

	}
    public boolean isROAccentrata(){    	
		if (isGestioneAccentrataDisable()){
			if (getCrudStatus() != OggettoBulk.NORMAL){
				setIm_spese_gest_accentrata_est(Utility.ZERO);
				setIm_spese_gest_accentrata_int(Utility.ZERO);									
			}
			return true;								
		}
    	return false;
    }
	public boolean isGestioneAccentrataRipartoDisable(){
		return (isGestioneAccentrataDisable() ||
				(getClassificazione() != null &&
				 getClassificazione().getId_classificazione() != null &&
				 getClassificazione().getFl_piano_riparto().booleanValue()));

	}
	public boolean isROAccentrataRiparto(){
		if (isROAccentrata() || isGestioneAccentrataRipartoDisable() 
				||isLimiteEst()
				){
			if (getCrudStatus() != OggettoBulk.NORMAL)
			  setIm_spese_gest_accentrata_est(Utility.ZERO);
			return true;								
		}		    	
		return false;
	}
	public boolean isGestioneAccentrataCdsAreaDisable(){
		return (isGestioneAccentrataDisable() ||
		        (getArea()!= null &&
			     getArea().getCd_proprio_unita()!= null &&
			     !getArea().equalsByPrimaryKey(getPdg_modulo_costi().getPdg_modulo().getCdr().getUnita_padre().getUnita_padre())));
	}
	public boolean isROAccentrataCdsArea(){
		if (isROAccentrata()||isGestioneAccentrataCdsAreaDisable()
				||isLimiteInt()
				){
	    	if (getCrudStatus() != OggettoBulk.NORMAL)
			  setIm_spese_gest_accentrata_int(Utility.ZERO);
			return true;								
		}		    	
		return false;
	}
	public boolean isGestioneDecentrataDisable(){
		return (getClassificazione() != null && 
		 	    getClassificazione().getId_classificazione() != null &&
			    getClassificazione().getFl_accentrato().booleanValue() && 
			    !getClassificazione().getFl_decentrato().booleanValue() &&
			    getClassificazione().getCentro_responsabilita() != null &&
			    getClassificazione().getCentro_responsabilita().getCd_centro_responsabilita() != null &&
			    !getClassificazione().getCentro_responsabilita().equalsByPrimaryKey(getPdg_modulo_costi().getPdg_modulo().getCdr())) ||
			    Optional.ofNullable(this.getPdg_modulo_costi()).flatMap(el->Optional.ofNullable(el.getPdg_modulo()))
					   .flatMap(el->Optional.ofNullable(el.getProgetto())).flatMap(el->Optional.ofNullable(el.getOtherField()))
					   .flatMap(progetto_other_fieldBulk -> Optional.ofNullable(progetto_other_fieldBulk.getTipoFinanziamento()))
					   .map(tipoFinanziamentoBulk -> !tipoFinanziamentoBulk.getFlPrevEntSpesa())
			   		   .orElse(Boolean.TRUE);
	}
	
	public boolean isRODecentrata(){
		if (isGestioneDecentrataDisable()){
			if (getCrudStatus() != OggettoBulk.NORMAL){
				setIm_spese_gest_decentrata_est(Utility.ZERO);
				setIm_spese_gest_decentrata_int(Utility.ZERO);		    						
			}
			return true;
	    }
		return false;
	}
    
	/**
	 * @return
	 */
	public boolean isSpeseScaricateDalPersonale() {
		return speseScaricateDalPersonale;
	}

	/**
	 * @param b
	 */
	public void setSpeseScaricateDalPersonale(boolean b) {
		speseScaricateDalPersonale = b;
	}

	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] 
		{getDettagli_gestionali()};
	}
	public SimpleBulkList getDettagli_gestionali() {
		return dettagli_gestionali;
	}
	public void setDettagli_gestionali(SimpleBulkList list) {
		dettagli_gestionali = list;
	}
	public Pdg_modulo_spese_gestBulk removeFromDettagli_gestionali(int indiceDiLinea) {
		Pdg_modulo_spese_gestBulk element = (Pdg_modulo_spese_gestBulk)dettagli_gestionali.get(indiceDiLinea);
		return (Pdg_modulo_spese_gestBulk)dettagli_gestionali.remove(indiceDiLinea);
	}
	public int addToDettagli_gestionali(Pdg_modulo_spese_gestBulk nuovo)
	{	
		nuovo.setPdg_modulo_spese(this);
		nuovo.setFl_sola_lettura(new Boolean(false));
		Timestamp today = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
		nuovo.setDt_registrazione(today);
		nuovo.setIm_spese_gest_accentrata_est(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_accentrata_int(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_decentrata_est(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_spese_gest_decentrata_int(it.cnr.contab.util.Utility.ZERO);
		nuovo.setIm_pagamenti(it.cnr.contab.util.Utility.ZERO);
		getDettagli_gestionali().add(nuovo);
		return getDettagli_gestionali().size()-1;
	}
	/**
	 * Ritorna la quota di previsione delle spese accentrate esterne 
	 * già assegnata
	 */
	public java.math.BigDecimal getTotale_spese_accentrate_esterne_gest() {
		return totale_spese_accentrate_esterne_gest;
	}

	/**
	 * Ritorna la quota di previsione delle spese accentrate interne 
	 * già assegnata
	 */
	public java.math.BigDecimal getTotale_spese_accentrate_interne_gest() {
		return totale_spese_accentrate_interne_gest;
	}

	/**
	 * Ritorna la quota di previsione delle spese decentrate esterne 
	 * già assegnata
	 */
	public java.math.BigDecimal getTotale_spese_decentrate_esterne_gest() {
		return totale_spese_decentrate_esterne_gest;
	}

	/**
	 * Ritorna la quota di previsione delle spese decentrate interne 
	 * già assegnata
	 */
	public java.math.BigDecimal getTotale_spese_decentrate_interne_gest() {
		return totale_spese_decentrate_interne_gest;
	}

	/**
	 * Imposta la quota di previsione delle spese accentrate esterne 
	 * già assegnata
	 */
	public void setTotale_spese_accentrate_esterne_gest(java.math.BigDecimal decimal) {
		totale_spese_accentrate_esterne_gest = decimal;
	}

	/**
	 * Imposta la quota di previsione delle spese accentrate interne 
	 * già assegnata
	 */
	public void setTotale_spese_accentrate_interne_gest(java.math.BigDecimal decimal) {
		totale_spese_accentrate_interne_gest = decimal;
	}

	/**
	 * Imposta la quota di previsione delle spese decentrate esterne 
	 * già assegnata
	 */
	public void setTotale_spese_decentrate_esterne_gest(java.math.BigDecimal decimal) {
		totale_spese_decentrate_esterne_gest = decimal;
	}

	/**
	 * Imposta la quota di previsione delle spese decentrate interne
	 * già assegnata
	 */
	public void setTotale_spese_decentrate_interne_gest(java.math.BigDecimal decimal) {
		totale_spese_decentrate_interne_gest = decimal;
	}
	/**
	 * Ritorna la quota di previsione delle spese accentrate interne 
	 * ancora da assegnare
	 */
	public java.math.BigDecimal getImporto_da_ripartire_acc_int_gest() {
		return Utility.nvl(getIm_spese_gest_accentrata_int()).subtract(Utility.nvl(getTotale_spese_accentrate_interne_gest()));
	}
	/**
	 * Ritorna la quota di previsione delle spese accentrate esterne 
	 * ancora da assegnare
	 */
	public java.math.BigDecimal getImporto_da_ripartire_acc_est_gest() {
		return Utility.nvl(getIm_spese_gest_accentrata_est()).subtract(Utility.nvl(getTotale_spese_accentrate_esterne_gest()));
	}
	/**
	 * Ritorna la quota di previsione delle spese decentrate interne 
	 * ancora da assegnare
	 */
	public java.math.BigDecimal getImporto_da_ripartire_dec_int_gest() {
		return Utility.nvl(getIm_spese_gest_decentrata_int()).subtract(Utility.nvl(getTotale_spese_decentrate_interne_gest()));
	}
	/**
	 * Ritorna la quota di previsione delle spese decentrate esterne 
	 * ancora da assegnare
	 */
	public java.math.BigDecimal getImporto_da_ripartire_dec_est_gest() {
		return Utility.nvl(getIm_spese_gest_decentrata_est()).subtract(Utility.nvl(getTotale_spese_decentrate_esterne_gest()));
	}
	public void setLimiteInt(boolean soggettaLimite) {
		limiteInt=soggettaLimite;
	}
	public void setLimiteEst(boolean soggettaLimite) {
		limiteEst=soggettaLimite;		
	}
	public boolean isLimiteInt() {
		return limiteInt;
	}
	public boolean isLimiteEst() {
		return limiteEst;
	}
	public CofogBulk getCofog() {
		return cofog;
	}
	public void setCofog(CofogBulk cofog) {
		this.cofog = cofog;
	}
	@Override
	public String getCd_cofog() {
		CofogBulk cofog = this.getCofog();
		if (cofog == null)
			return null;
		return cofog.getCd_cofog();
	}
	@Override
	public void setCd_cofog(String cd_cofog) {
		// TODO Auto-generated method stub
		getCofog().setCd_cofog(cd_cofog);
	}
	public Pdg_missioneBulk getPdgMissione() {
		return pdgMissione;
	}
	
	public void setPdgMissione(Pdg_missioneBulk pdgMissione) {
		this.pdgMissione = pdgMissione;
	}
	@Override
	public String getCd_missione() {
		Pdg_missioneBulk pdgMissione = this.getPdgMissione();
		if (pdgMissione == null)
			return null;
		return pdgMissione.getCd_missione();
	}
	
	@Override
	public void setCd_missione(String cd_missione) {
		this.getPdgMissione().setCd_missione(cd_missione);
	}
	public boolean isPrevAnnoSucObb() {
		return prevAnnoSucObb;
	}
	public void setPrevAnnoSucObb(boolean prevAnnoSucObb) {
		this.prevAnnoSucObb = prevAnnoSucObb;
	}

	public Voce_piano_economico_prgBulk getVoce_piano_economico() {
		return voce_piano_economico;
	}

	public void setVoce_piano_economico(Voce_piano_economico_prgBulk voce_piano_economico) {
		this.voce_piano_economico = voce_piano_economico;
	}	

	@Override
	public String getCd_unita_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_unita_organizzativa();
	}
	
	@Override
	public void setCd_unita_piano(String cd_unita_piano) {
		this.getVoce_piano_economico().setCd_unita_organizzativa(cd_unita_piano);
	}
	
	@Override
	public String getCd_voce_piano() {
		Voce_piano_economico_prgBulk vocePiano = this.getVoce_piano_economico();
		if (vocePiano == null)
			return null;
		return vocePiano.getCd_voce_piano();
	}
	
	@Override
	public void setCd_voce_piano(String cd_voce_piano) {
		this.getVoce_piano_economico().setCd_voce_piano(cd_voce_piano);
	}
	
	public boolean isROVocePianoEconomico(){
		return !Optional.ofNullable(this.getId_classificazione()).isPresent();
	}
}