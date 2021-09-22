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

/*
* Creted by Generator 1.0
* Date 09/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperMagBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.util.action.CRUDBP;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_norma_perlaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
@StorageType(name="F:sigla_contratti:appalti")
@JsonInclude(value=Include.NON_NULL)
public class ContrattoBulk extends ContrattoBase implements ICancellatoLogicamente{
	
	private static final java.util.Dictionary ti_statoKeys = new it.cnr.jada.util.OrderedHashtable();
	
	final public static String STATO_PROVVISORIO = "P";
	final public static String STATO_DEFINITIVO = "D";
	final public static String STATO_CESSSATO = "C";

	static {
		ti_statoKeys.put(STATO_PROVVISORIO,"Provvisorio");
		ti_statoKeys.put(STATO_DEFINITIVO,"Definitivo");
		ti_statoKeys.put(STATO_CESSSATO,"Cessato");
	}
	public static final java.util.Dictionary ti_natura_contabileKeys = new it.cnr.jada.util.OrderedHashtable();

	final public static String NATURA_CONTABILE_ATTIVO = "A";
	final public static String NATURA_CONTABILE_PASSIVO = "P";
	final public static String NATURA_CONTABILE_ATTIVO_E_PASSIVO = "E";
	final public static String NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI = "N";
	static {
		ti_natura_contabileKeys.put(NATURA_CONTABILE_ATTIVO,"Attivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_PASSIVO,"Passivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_ATTIVO_E_PASSIVO,"Attivo e Passivo");
		ti_natura_contabileKeys.put(NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI,"Senza flussi finanziari");
	}
	public static final java.util.Dictionary tipoDettaglioContrattoKeys = new it.cnr.jada.util.OrderedHashtable();


	final public static String DETTAGLIO_CONTRATTO_CATGRP = "CAT";
	final public static String DETTAGLIO_CONTRATTO_ARTICOLI = "ART";

	static {
		tipoDettaglioContrattoKeys.put(DETTAGLIO_CONTRATTO_CATGRP,"Categoria Gruppo");
		tipoDettaglioContrattoKeys.put(DETTAGLIO_CONTRATTO_ARTICOLI,"Articoli");
	}

	private ContrattoBulk contratto_padre;
	private TerzoBulk figura_giuridica_interna;
	private TerzoBulk figura_giuridica_esterna;
	private V_persona_fisicaBulk responsabile;
	private V_persona_fisicaBulk firmatario;
	private Tipo_contrattoBulk tipo_contratto;
	private Tipo_atto_amministrativoBulk atto;
	private OrganoBulk organo;
	private Tipo_atto_amministrativoBulk atto_annullamento;
	private OrganoBulk organo_annullamento;
	private Unita_organizzativaBulk unita_organizzativa;
	private Procedure_amministrativeBulk procedura_amministrativa;
	private Tipo_norma_perlaBulk tipoNormaPerla;
	private V_persona_fisicaBulk direttore;
	private CigBulk cig;
	private CupBulk cup;
	private ProgettoBulk progetto;
	
	private Boolean allegatoFlusso;
	private BulkList associazioneUO = new BulkList();
	private BulkList associazioneUODisponibili = new BulkList();
	private BulkList ditteInvitate = new BulkList();
	private BulkList<Dettaglio_contrattoBulk> dettaglio_contratto= new it.cnr.jada.bulk.BulkList<Dettaglio_contrattoBulk>();
		
	@Transient
	private BulkList<AllegatoContrattoDocumentBulk> archivioAllegati = new BulkList();
	@Transient
	private BulkList<AllegatoContrattoFlussoDocumentBulk> archivioAllegatiFlusso = new BulkList();
	
	private java.math.BigDecimal tot_ordini;

	private java.math.BigDecimal tot_doc_cont_spe;
	private java.math.BigDecimal tot_doc_cont_etr;

	private java.math.BigDecimal tot_docamm_cont_spe;
	private java.math.BigDecimal tot_docamm_cont_etr;
	
	private java.math.BigDecimal tot_doccont_cont_spe;
	private java.math.BigDecimal tot_doccont_cont_spe_netto;
	private java.math.BigDecimal tot_doccont_cont_etr;
	
	private java.math.BigDecimal tot_docamm_cont_spe_netto;

	private boolean checkDisponibilitaContrattoEseguito = false;

	public ContrattoBulk() {
		super();
	}
	public ContrattoBulk(java.lang.Integer esercizio, java.lang.String stato, java.lang.Long pg_contratto) {
		super(esercizio, stato, pg_contratto);
	}
	public boolean isProvvisorio(){
		return ContrattoBulk.STATO_PROVVISORIO.equals(getStato());
		
	}
	public boolean isDefinitivo(){
		return ContrattoBulk.STATO_DEFINITIVO.equals(getStato());
	}
	public boolean isRODefinitivo(){
		return getStato()!=null && isDefinitivo();
	}
	public boolean isROTipoContratto(){
		if (Optional.ofNullable(getDettaglio_contratto()).filter(p->( !p.isEmpty())).isPresent()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;

	}
	public boolean isRODati_cessazione(){
		return getStato()!=null && !isDefinitivo();
	}	
	public boolean isCessato(){
		return ContrattoBulk.STATO_CESSSATO.equals(getStato());
	}
	public boolean isDs_atto_non_definitoVisible(){
		return (getAtto() != null && getAtto().getFl_non_definito() != null && getAtto().getFl_non_definito().booleanValue());
	}
	public boolean isDs_atto_ann_non_definitoVisible(){
		return (getAtto_annullamento() != null && getAtto_annullamento().getFl_non_definito() != null && getAtto_annullamento().getFl_non_definito().booleanValue());
	}
	public boolean isDs_organo_non_definitoVisible(){
		return (getOrgano() != null && getOrgano().getFl_non_definito() != null && getOrgano().getFl_non_definito().booleanValue());
	}
	public boolean isDs_organo_ann_non_definitoVisible(){
		return (getOrgano_annullamento() != null && getOrgano_annullamento().getFl_non_definito() != null && getOrgano_annullamento().getFl_non_definito().booleanValue());
	}
	public boolean isAttivo(){
		return (getNatura_contabile() != null && getNatura_contabile().equals(NATURA_CONTABILE_ATTIVO));
	}
	
	public boolean isSenzaFlussiFinanziari(){
		return (getNatura_contabile() != null && getNatura_contabile().equals(NATURA_CONTABILE_SENZA_FLUSSI_FINANZIARI));
	}

	public boolean isPassivo(){
		return (getNatura_contabile() != null && getNatura_contabile().equals(NATURA_CONTABILE_PASSIVO));
	}
	public boolean isAttivo_e_Passivo(){
		return (getNatura_contabile() != null && getNatura_contabile().equals(NATURA_CONTABILE_ATTIVO_E_PASSIVO));
	}
	
	public boolean isCIGVisible(){
		if (isFromFlussoAcquisti()){
			return true;
		}
		if (getTipo_contratto() == null)
			return false;
		if (getTipo_contratto().getFl_cig() == null || !getTipo_contratto().getFl_cig())
			return false;
		return true;
	}

	/**
	 * Inizializza per l'inserimento i flag
	 */
	public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
		setFl_art82(new Boolean(false));
		setStato(ContrattoBulk.STATO_PROVVISORIO);
		setEsercizio(CNRUserContext.getEsercizio(context.getUserContext()));
		setDt_registrazione(EJBCommonServices.getServerTimestamp());
		return super.initializeForInsert(bp,context);
	}
	/**
	 * Restituisce il valore della proprietà 'ds_responsabile'
	 *
	 * @return Il valore della proprietà 'ds_responsabile'
	 */
	public java.lang.String getDs_responsabile() {
		if ( responsabile != null && responsabile.getAnagrafico() != null &&
		     responsabile.getAnagrafico().getCognome()!=null )
			return responsabile.getAnagrafico().getCognome() + " " + responsabile.getAnagrafico().getNome();
		return "";	

	}
	/**
	 * Restituisce il valore della proprietà 'ds_firmatario'
	 *
	 * @return Il valore della proprietà 'ds_responsabile'
	 */
	public java.lang.String getDs_firmatario() {
		if ( getFirmatario() != null && getFirmatario().getAnagrafico() != null &&
		     getFirmatario().getAnagrafico().getCognome()!=null )
			return getFirmatario().getAnagrafico().getCognome() + " " + getFirmatario().getAnagrafico().getNome();
		return "";	
	}	
	public boolean isROResponsabile() {
		return responsabile == null || responsabile.getCrudStatus() == NORMAL;
	}
	public boolean isROFirmatario() {
		return getFirmatario() == null || getFirmatario().getCrudStatus() == NORMAL;
	}	
		
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_statoKeys() {
		return ti_statoKeys;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (11/04/2005 12:34:48)
	 * @return java.util.Dictionary
	 */
	public final java.util.Dictionary getTi_natura_contabileKeys() {
		return ti_natura_contabileKeys;
	}

	public final java.util.Dictionary getTipoDettaglioContrattoKeys() {
		return tipoDettaglioContrattoKeys;
	}

	@StorageProperty(name="sigla_contratti:natura_contabile")
	public String getDescrizioneNaturaContabile(){
		return (String) Optional.ofNullable(getNatura_contabile()).map(x -> ti_natura_contabileKeys.get(x)).orElse(null);
	}
	/**
	 * @return
	 */
	public ContrattoBulk getContratto_padre() {
		return contratto_padre;
	}

	/**
	 * @return
	 */
	public TerzoBulk getFigura_giuridica_esterna() {
		return figura_giuridica_esterna;
	}

	/**
	 * @return
	 */
	public TerzoBulk getFigura_giuridica_interna() {
		return figura_giuridica_interna;
	}

	/**
	 * @return
	 */
	public V_persona_fisicaBulk getResponsabile() {
		return responsabile;
	}

	/**
	 * @return
	 */
	public Tipo_contrattoBulk getTipo_contratto() {
		return tipo_contratto;
	}

	/**
	 * @param bulk
	 */
	public void setContratto_padre(ContrattoBulk bulk) {
		contratto_padre = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setFigura_giuridica_esterna(TerzoBulk bulk) {
		figura_giuridica_esterna = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setFigura_giuridica_interna(TerzoBulk bulk) {
		figura_giuridica_interna = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setResponsabile(V_persona_fisicaBulk bulk) {
		responsabile = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setTipo_contratto(Tipo_contrattoBulk bulk) {
		tipo_contratto = bulk;
	}
	/* 
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getEsercizio_padre()
	 */
	public java.lang.Integer getEsercizio_padre () {
		if (getContratto_padre() != null)
		  return getContratto_padre().getEsercizio();
		return null;  		
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setEsercizio_padre(java.lang.Integer)
	 */
	public void setEsercizio_padre(java.lang.Integer esercizio_padre)  {
		getContratto_padre().setEsercizio(esercizio_padre);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getPg_contratto_padre()
	 */
	public java.lang.Long getPg_contratto_padre () {
		if (getContratto_padre() != null)
		  return getContratto_padre().getPg_contratto();
		return null;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setPg_contratto_padre(java.lang.Long)
	 */	
	public void setPg_contratto_padre(java.lang.Long pg_contratto_padre)  {
		getContratto_padre().setPg_contratto(pg_contratto_padre);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getFig_giur_int()
	 */	
	public java.lang.Integer getFig_giur_int () {
		if(getFigura_giuridica_interna() != null)
		  return getFigura_giuridica_interna().getCd_terzo();
		return null;  
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setFig_giur_int(java.lang.Integer)
	 */
	public void setFig_giur_int(java.lang.Integer fig_giur_int)  {
		this.getFigura_giuridica_interna().setCd_terzo(fig_giur_int);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getFig_giur_est()
	 */	
	public java.lang.Integer getFig_giur_est () {
		if(getFigura_giuridica_esterna() != null)
		  return getFigura_giuridica_esterna().getCd_terzo();
		return null;  
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setFig_giur_est(java.lang.Integer)
	 */
	public void setFig_giur_est(java.lang.Integer fig_giur_est)  {
		this.getFigura_giuridica_esterna().setCd_terzo(fig_giur_est);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_terzo_resp()
	 */
	public java.lang.Integer getCd_terzo_resp () {
		if (getResponsabile() != null)
		  return getResponsabile().getCd_terzo();
		return null;  
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_terzo_resp(java.lang.Integer)
	 */
	public void setCd_terzo_resp(java.lang.Integer cd_terzo_resp)  {
		this.getResponsabile().setCd_terzo(cd_terzo_resp);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_contratto()
	 */	
	public java.lang.String getCd_tipo_contratto () {
		if(getTipo_contratto() != null)
		  return getTipo_contratto().getCd_tipo_contratto();
		return null;  
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_tipo_contratto(java.lang.String)
	 */
	public void setCd_tipo_contratto(java.lang.String cd_tipo_contratto)  {
		this.getTipo_contratto().setCd_tipo_contratto(cd_tipo_contratto);
	}
	public boolean isCancellatoLogicamente(){
		return Optional.ofNullable(getStato()).map(x -> x.equals(STATO_CESSSATO)).orElse(false);
	}
	/**
	 * @return
	 */
	public BulkList getAssociazioneUO() {
		return associazioneUO;
	}

	/**
	 * @param list
	 */
	public void setAssociazioneUO(BulkList list) {
		associazioneUO = list;
	}
	public int addToAssociazioneUO(Ass_contratto_uoBulk dett) {
		dett.setContratto(this);
		dett.setEsercizio(getEsercizio());
		dett.setPg_contratto(getPg_contratto());
		getAssociazioneUO().add(dett);
		return getAssociazioneUO().size()-1;
	}
	public int addToAssociazioneUODisponibili(Unita_organizzativaBulk dett) {
		getAssociazioneUODisponibili().add(dett);
		return getAssociazioneUODisponibili().size()-1;
	}
	
	public it.cnr.jada.bulk.BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] {getAssociazioneUO(),getAssociazioneUODisponibili(),getArchivioAllegati(),
				getArchivioAllegatiFlusso(),getDitteInvitate(),getDettaglio_contratto()};
	}

//	public List getChildren() {
//		return getDettaglio_contratto();
//	}

	public Ass_contratto_uoBulk removeFromAssociazioneUO(int index) {
		Ass_contratto_uoBulk dett = (Ass_contratto_uoBulk)getAssociazioneUO().remove(index);
		return dett;
	}
	public Unita_organizzativaBulk removeFromAssociazioneUODisponubili(int index) {
		Unita_organizzativaBulk dett = (Unita_organizzativaBulk)getAssociazioneUODisponibili().remove(index);
		return dett;
	}
	

	/**
	 * @return
	 */
	public java.math.BigDecimal getTot_doc_cont_spe() {
		return tot_doc_cont_spe;
	}

	/**
	 * @param decimal
	 */
	public void setTot_doc_cont_spe(java.math.BigDecimal decimal) {
		tot_doc_cont_spe = decimal;
	}
	/* (non-Javadoc)
	 * @see it.cnr.contab.util.ICancellatoLogicamente#cancellaLogicamente()
	 */
	public void cancellaLogicamente() {
		setDt_annullamento(EJBCommonServices.getServerTimestamp());
		setStato(ContrattoBulk.STATO_CESSSATO);		
	}

	/**
	 * @return
	 */
	public OrganoBulk getOrgano() {
		return organo;
	}

	/**
	 * @return
	 */
	public OrganoBulk getOrgano_annullamento() {
		return organo_annullamento;
	}

	/**
	 * @return
	 */
	public Tipo_atto_amministrativoBulk getAtto() {
		return atto;
	}

	/**
	 * @return
	 */
	public Tipo_atto_amministrativoBulk getAtto_annullamento() {
		return atto_annullamento;
	}

	/**
	 * @param bulk
	 */
	public void setOrgano(OrganoBulk bulk) {
		organo = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setOrgano_annullamento(OrganoBulk bulk) {
		organo_annullamento = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setAtto(Tipo_atto_amministrativoBulk bulk) {
		atto = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setAtto_annullamento(Tipo_atto_amministrativoBulk bulk) {
		atto_annullamento = bulk;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_tipo_organo(java.lang.String)
	 */
	public void setCd_organo(java.lang.String cd_tipo_organo) {
		getOrgano().setCd_organo(cd_tipo_organo);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_tipo_organo_ann(java.lang.String)
	 */
	public void setCd_organo_ann(java.lang.String cd_tipo_organo_ann) {
		getOrgano_annullamento().setCd_organo(cd_tipo_organo_ann);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_tipo_atto(java.lang.String)
	 */
	public void setCd_tipo_atto(java.lang.String cd_tipo_atto) {
		getAtto().setCd_tipo_atto(cd_tipo_atto);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_tipo_atto_ann(java.lang.String)
	 */
	public void setCd_tipo_atto_ann(java.lang.String cd_tipo_atto_ann) {
		getAtto_annullamento().setCd_tipo_atto(cd_tipo_atto_ann);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_organo()
	 */
	public java.lang.String getCd_organo() {
		return Optional.ofNullable(getOrgano()).map(OrganoBulk::getCd_organo).orElse(null);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_organo_ann()
	 */
	public java.lang.String getCd_organo_ann() {
		return Optional.ofNullable(getOrgano_annullamento()).map(OrganoBulk::getCd_organo).orElse(null);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_atto()
	 */
	public java.lang.String getCd_tipo_atto() {
		return Optional.ofNullable(getAtto()).map(Tipo_atto_amministrativoBulk::getCd_tipo_atto).orElse(null);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_atto_ann()
	 */
	public java.lang.String getCd_tipo_atto_ann() {
		return Optional.ofNullable(getAtto_annullamento()).map(Tipo_atto_amministrativoBulk::getCd_tipo_atto).orElse(null);
	}
	/**
	 * @return
	 */
	public java.math.BigDecimal getTot_doc_cont_etr() {
		return tot_doc_cont_etr;
	}

	/**
	 * @param decimal
	 */
	public void setTot_doc_cont_etr(java.math.BigDecimal decimal) {
		tot_doc_cont_etr = decimal;
	}

	/**
	 * @return
	 */
	public V_persona_fisicaBulk getFirmatario() {
		return firmatario;
	}

	/**
	 * @return
	 */
	public Procedure_amministrativeBulk getProcedura_amministrativa() {
		return procedura_amministrativa;
	}

	/**
	 * @return
	 */
	public Unita_organizzativaBulk getUnita_organizzativa() {
		return unita_organizzativa;
	}

	/**
	 * @param bulk
	 */
	public void setFirmatario(V_persona_fisicaBulk bulk) {
		firmatario = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setProcedura_amministrativa(Procedure_amministrativeBulk bulk) {
		procedura_amministrativa = bulk;
	}

	/**
	 * @param bulk
	 */
	public void setUnita_organizzativa(Unita_organizzativaBulk bulk) {
		unita_organizzativa = bulk;
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_terzo_firmatario()
	 */
	public java.lang.Integer getCd_terzo_firmatario() {
		return Optional.ofNullable(getFirmatario()).map(V_persona_fisicaBulk::getCd_terzo).orElse(null);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_terzo_firmatario(java.lang.Integer)
	 */
	public void setCd_terzo_firmatario(java.lang.Integer cd_firmatario) {
		getFirmatario().setCd_terzo(cd_firmatario);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_unita_organizzativa()
	 */
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_unita_organizzativa(java.lang.String)
	 */
	public void setCd_unita_organizzativa(java.lang.String string) {
		getUnita_organizzativa().setCd_unita_organizzativa(string);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_proc_amm()
	 */
	public java.lang.String getCd_proc_amm () {
		return Optional.ofNullable(getProcedura_amministrativa()).map(Procedure_amministrativeBulk::getCd_proc_amm).orElse(null);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#setCd_proc_amm(java.lang.String)
	 */
	public void setCd_proc_amm(java.lang.String proc_amm)  {
		getProcedura_amministrativa().setCd_proc_amm(proc_amm);
	}

	/**
	 * @return
	 */
	public BulkList getAssociazioneUODisponibili() {
		return associazioneUODisponibili;
	}

	/**
	 * @param list
	 */
	public void setAssociazioneUODisponibili(BulkList list) {
		associazioneUODisponibili = list;
	}
	
	public Tipo_norma_perlaBulk getTipoNormaPerla() {
		return tipoNormaPerla;
	}
	public void setTipoNormaPerla(Tipo_norma_perlaBulk tipoNormaPerla) {
		this.tipoNormaPerla = tipoNormaPerla;
	}
	public V_persona_fisicaBulk getDirettore() {
		return direttore;
	}
	public void setDirettore(V_persona_fisicaBulk direttore) {
		this.direttore = direttore;
	}
	public CigBulk getCig() {
		return cig;
	}
	public void setCig(CigBulk cig) {
		this.cig = cig;
	}
	public java.math.BigDecimal getTot_docamm_cont_spe() {
		return tot_docamm_cont_spe;
	}
	public void setTot_docamm_cont_spe(java.math.BigDecimal tot_docamm_cont_spe) {
		this.tot_docamm_cont_spe = tot_docamm_cont_spe;
	}
	public java.math.BigDecimal getTot_docamm_cont_etr() {
		return tot_docamm_cont_etr;
	}
	public void setTot_docamm_cont_etr(java.math.BigDecimal tot_docamm_cont_etr) {
		this.tot_docamm_cont_etr = tot_docamm_cont_etr;
	}
	public java.math.BigDecimal getTot_doccont_cont_spe() {
		return tot_doccont_cont_spe;
	}
	public void setTot_doccont_cont_spe(java.math.BigDecimal tot_doccont_cont_spe) {
		this.tot_doccont_cont_spe = tot_doccont_cont_spe;
	}
	public java.math.BigDecimal getTot_doccont_cont_etr() {
		return tot_doccont_cont_etr;
	}
	public void setTot_doccont_cont_etr(java.math.BigDecimal tot_doccont_cont_etr) {
		this.tot_doccont_cont_etr = tot_doccont_cont_etr;
	}


	public BulkList<AllegatoContrattoDocumentBulk> getArchivioAllegati() {
		return archivioAllegati;
	}

	public void setArchivioAllegati(
			BulkList<AllegatoContrattoDocumentBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	
	public int addToArchivioAllegati(AllegatoContrattoDocumentBulk dett) {
		dett.setContrattoBulk(this);
		getArchivioAllegati().add(dett);
		return getArchivioAllegati().size()-1;
	}	
	public AllegatoContrattoDocumentBulk removeFromArchivioAllegati(int index) {
		AllegatoContrattoDocumentBulk dett = (AllegatoContrattoDocumentBulk)getArchivioAllegati().remove(index);
		return dett;
	}
	public BulkList<AllegatoContrattoFlussoDocumentBulk> getArchivioAllegatiFlusso() {
		return archivioAllegatiFlusso;
	}

	public void setArchivioAllegatiFlusso(
			BulkList<AllegatoContrattoFlussoDocumentBulk> archivioAllegatiFlusso) {
		this.archivioAllegatiFlusso = archivioAllegatiFlusso;
	}
	
	public int addToArchivioAllegatiFlusso(AllegatoContrattoFlussoDocumentBulk dett) {
		dett.setContrattoBulk(this);
		getArchivioAllegatiFlusso().add(dett);
		return getArchivioAllegatiFlusso().size()-1;
	}	
	public AllegatoContrattoFlussoDocumentBulk removeFromArchivioAllegatiFlusso(int index) {
		AllegatoContrattoFlussoDocumentBulk dett = (AllegatoContrattoFlussoDocumentBulk)getArchivioAllegatiFlusso().remove(index);
		return dett;
	}
	@StorageProperty(name="cmis:name")
	public String getCMISFolderName(){
		if (isFromFlussoAcquisti())
			return getCodiceFlussoAcquisti();
		
		return Arrays.asList(
				"Contratto ", 
				String.valueOf(getEsercizio()), 
				getStato(), 
				Utility.lpad(Optional.ofNullable(getPg_contratto()).map(x -> x.toString()).orElse(""), 9, '0')
		).stream().filter(x -> x != null).collect(Collectors.joining());
	}
	
	public boolean isAllegatoContrattoPresent(){
		for (AllegatoContrattoDocumentBulk allegato : getArchivioAllegati()) {
			if (allegato.getType().equals(AllegatoContrattoDocumentBulk.CONTRATTO))
				return true;
		}
		return false;
	}
	
	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:codice"))
	public String getCd_cds(){
		return Optional.ofNullable(Optional.ofNullable(getUnita_organizzativa()).
				map(Unita_organizzativaBulk::getUnita_padre).orElse(new CdsBulk())).map(CdsBulk::getCd_unita_organizzativa).orElse("");
	}

	@StoragePolicy(name="P:strorg:cds", property=@StorageProperty(name="strorgcds:descrizione"))
	public String getDs_cds(){
		return Optional.ofNullable(Optional.ofNullable(getUnita_organizzativa()).
			map(Unita_organizzativaBulk::getUnita_padre).orElse(new CdsBulk())).map(CdsBulk::getDs_unita_organizzativa).orElse("");
	}
	
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	public String getCd_unita_organizzativa(){
		return Optional.ofNullable(getUnita_organizzativa()).map(Unita_organizzativaBulk::getCd_unita_organizzativa).orElse(null);
	}

	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:descrizione"))
	public String getDs_unita_organizzativa(){
		return Optional.ofNullable(getUnita_organizzativa()).map(Unita_organizzativaBulk::getDs_unita_organizzativa).orElse("");
	}
	
	@StorageProperty(name="sigla_contratti:fig_giu_esterna_codice")
	public Integer getFig_giu_esterna_codice(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getCd_terzo();
	}

	@StorageProperty(name="sigla_contratti:fig_giu_esterna_denominazione")
	public String getFig_giu_esterna_denominazione(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getDenominazione_sede();
	}

	@StorageProperty(name="sigla_contratti:fig_giu_esterna_codfis")
	public String getFig_giu_esterna_codfis(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getAnagrafico().getCodice_fiscale();
	}

	@StorageProperty(name="sigla_contratti:fig_giu_esterna_pariva")
	public String getFig_giu_esterna_pariva(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getAnagrafico().getPartita_iva();
	}

	@StorageProperty(name="sigla_contratti:tipo_norma")
	public String getTipo_norma(){
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
		Tipo_norma_perlaBulk perla = getTipoNormaPerla();
		if (perla != null && perla.getDs_tipo_norma() != null)
			return perla.getDs_tipo_norma().concat(" ").concat(perla.getNumero_tipo_norma()).
				concat(" ").concat(formatter.format(perla.getDt_tipo_norma())).
				concat(" ").concat(perla.getArticolo_tipo_norma()).
				concat(" ").concat(perla.getComma_tipo_norma());
		return null;
	}

	@StorageProperty(name="sigla_contratti:responsabile_codice")
	public Integer getCodiceResponsabile(){
		if (getDirettore() == null)
			return null;
		return getDirettore().getCd_terzo();
	}

	@StorageProperty(name="sigla_contratti:responsabile_denominazione")
	public String getDenominazioneResponsabile(){
		if (getDirettore() == null)
			return null;
		return getDirettore().getDenominazione_sede();
	}

	@StorageProperty(name="sigla_contratti:responsabile_procedimento_codice")
	public Integer getCodiceResponsabileProcedimento(){
		if (getResponsabile() == null)
			return null;
		return getResponsabile().getCd_terzo();
	}

	@StorageProperty(name="sigla_contratti:responsabile_procedimento_denominazione")
	public String getDenominazioneResponsabileProcedimento(){
		if (getResponsabile() == null)
			return null;
		return getResponsabile().getDenominazione_sede();
	}
	
	@StorageProperty(name="sigla_contratti:mod_individuazione_beneficiario")
	public String getModIndividuazioneBeneficiario(){
		if (getProcedura_amministrativa() == null)
			return null;
		return getProcedura_amministrativa().getDs_proc_amm();
	}
	public CupBulk getCup() {
		return cup;
	}
	public void setCup(CupBulk cup) {
		this.cup = cup;
	}
	public BulkList getDitteInvitate() {
		return ditteInvitate;
	}
	public void setDitteInvitate(BulkList ditteInvitate) {
		this.ditteInvitate = ditteInvitate;
	}
	public int addToDitteInvitate(Ass_contratto_ditteBulk ass_contratto_ditte) throws ValidationException {
		ass_contratto_ditte.setContratto(this); 
		ass_contratto_ditte.setTipologia(Ass_contratto_ditteBulk.LISTA_INVITATE);
		this.getDitteInvitate().add(ass_contratto_ditte);
		return getDitteInvitate().size()-1;
	}		
	public java.math.BigDecimal getTot_ordini() {
		return tot_ordini;
	}
	public void setTot_ordini(java.math.BigDecimal tot_ordini) {
		this.tot_ordini = tot_ordini;
	}

	public Ass_contratto_ditteBulk removeFromDitteInvitate(int index) {
		return (Ass_contratto_ditteBulk)ditteInvitate.remove(index);
	}
	public java.math.BigDecimal getTot_doccont_cont_spe_netto() {
		return tot_doccont_cont_spe_netto;
	}
	public void setTot_doccont_cont_spe_netto(
			java.math.BigDecimal tot_doccont_cont_spe_netto) {
		this.tot_doccont_cont_spe_netto = tot_doccont_cont_spe_netto;
	}
	public java.math.BigDecimal getTot_docamm_cont_spe_netto() {
		return tot_docamm_cont_spe_netto;
	} 
	public void setTot_docamm_cont_spe_netto(
			java.math.BigDecimal tot_docamm_cont_spe_netto) {
		this.tot_docamm_cont_spe_netto = tot_docamm_cont_spe_netto;
	}
	public Boolean isFromFlussoAcquisti() {
		return !StringUtils.isEmpty(getCodiceFlussoAcquisti());
	}
	public Boolean getAllegatoFlusso() {
		return allegatoFlusso;
	}
	public void setAllegatoFlusso(Boolean allegatoFlusso) {
		this.allegatoFlusso = allegatoFlusso;
	}
	public ProgettoBulk getProgetto() {
		return progetto;
	}
	public void setProgetto(ProgettoBulk progetto) {
		this.progetto = progetto;
	}
    public java.lang.Integer getPg_progetto() {
		return Optional.ofNullable(this.getProgetto()).map(ProgettoBulk::getPg_progetto)
				.orElse(null);
    }
	public void setPg_progetto(java.lang.Integer progetto) {
		if (!Optional.ofNullable(this.getProgetto()).isPresent())
			this.setProgetto(new ProgettoBulk());
		this.getProgetto().setPg_progetto(progetto);
	}
	
	public boolean isROProgetto(){
		return Boolean.FALSE;
	}

	/**
	 * @return
	 */
	public boolean isCheckDisponibilitaContrattoEseguito() {
		return checkDisponibilitaContrattoEseguito;
	}

	/**
	 * @param b
	 */
	public void setCheckDisponibilitaContrattoEseguito(boolean b) {
		checkDisponibilitaContrattoEseguito = b;
	}

	@Override
	public OggettoBulk initializeForFreeSearch(CRUDBP crudbp, ActionContext actioncontext) {
		if (!Optional.ofNullable(getResponsabile()).isPresent())
			setResponsabile(new V_persona_fisicaBulk());
		return super.initializeForFreeSearch(crudbp, actioncontext);
	}

	public BulkList<Dettaglio_contrattoBulk> getDettaglio_contratto() {
		return dettaglio_contratto;
	}

	public void setDettaglio_contratto(BulkList<Dettaglio_contrattoBulk> dettaglio_contratto) {
		this.dettaglio_contratto = dettaglio_contratto;
	}
	public int addToDettaglio_contratto(Dettaglio_contrattoBulk dett) {
		//dett.setUtente(this);
		dett.setContratto(this);
		getDettaglio_contratto().add(dett);
		return getDettaglio_contratto().size()-1;
	}
	public Dettaglio_contrattoBulk removeFromDettaglio_contratto(int index) {
		Dettaglio_contrattoBulk dett = (Dettaglio_contrattoBulk)getDettaglio_contratto().remove(index);
		return dett;
	}
	public boolean isDettaglioContrattoPerArticoli()
	{
		return getTipo_dettaglio_contratto() != null && getTipo_dettaglio_contratto().equals(DETTAGLIO_CONTRATTO_ARTICOLI);
	}

	public boolean isDettaglioContrattoPerCategoriaGruppo()
	{
		return getTipo_dettaglio_contratto() != null && getTipo_dettaglio_contratto().equals(DETTAGLIO_CONTRATTO_CATGRP);
	}

}