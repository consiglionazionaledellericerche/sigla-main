/*
* Creted by Generator 1.0
* Date 09/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_norma_perlaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.ejb.EJBCommonServices;
@CMISType(name="F:sigla_contratti:appalti")
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
	
	
	private BulkList associazioneUO = new BulkList();
	private BulkList associazioneUODisponibili = new BulkList();
	private BulkList<AllegatoContrattoDocumentBulk> archivioAllegati = new BulkList();
	
	private java.math.BigDecimal tot_doc_cont_spe;
	private java.math.BigDecimal tot_doc_cont_etr;

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
	public boolean isRODati_cessazione(){
		return getStato()!=null && !isDefinitivo();
	}	
	public boolean isCessato(){
		return getStato().equals(ContrattoBulk.STATO_CESSSATO);
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
	
	@CMISProperty(name="sigla_contratti:natura_contabile")
	public String getDescrizioneNaturaContabile(){
		return (String) ti_natura_contabileKeys.get(getNatura_contabile());
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
		if (getStato().equals(STATO_CESSSATO))
		  return true;
		return false;   			
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
		return new it.cnr.jada.bulk.BulkCollection[] {getAssociazioneUO(),getAssociazioneUODisponibili(),getArchivioAllegati()};
	}
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
		return getOrgano().getCd_organo();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_organo_ann()
	 */
	public java.lang.String getCd_organo_ann() {
		return getOrgano_annullamento().getCd_organo();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_atto()
	 */
	public java.lang.String getCd_tipo_atto() {
		return getAtto().getCd_tipo_atto();
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.contab.config00.contratto.bulk.ContrattoBase#getCd_tipo_atto_ann()
	 */
	public java.lang.String getCd_tipo_atto_ann() {
		return getAtto_annullamento().getCd_tipo_atto();
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
		return getFirmatario().getCd_terzo();
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
		return getProcedura_amministrativa().getCd_proc_amm();
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
	@CMISProperty(name="cmis:name")
	public String getCMISFolderName(){
		return "Contratto ".concat(String.valueOf(getEsercizio())).concat(Utility.lpad(getPg_contratto(), 9, '0'));
	}
	
	public boolean isAllegatoContrattoPresent(){
		for (AllegatoContrattoDocumentBulk allegato : getArchivioAllegati()) {
			if (allegato.getType().equals(AllegatoContrattoDocumentBulk.CONTRATTO))
				return true;
		}
		return false;
	}
	
	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	public String getCd_cds(){
		return getUnita_organizzativa().getUnita_padre().getCd_unita_organizzativa();
	}

	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:descrizione"))
	public String getDs_cds(){
		return getUnita_organizzativa().getUnita_padre().getDs_unita_organizzativa();
	}
	
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	public String getCd_unita_organizzativa(){
		return getUnita_organizzativa().getCd_unita_organizzativa();
	}

	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:descrizione"))
	public String getDs_unita_organizzativa(){
		return getUnita_organizzativa().getDs_unita_organizzativa();
	}
	
	@CMISProperty(name="sigla_contratti:fig_giu_esterna_codice")
	public Integer getFig_giu_esterna_codice(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getCd_terzo();
	}

	@CMISProperty(name="sigla_contratti:fig_giu_esterna_denominazione")
	public String getFig_giu_esterna_denominazione(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getDenominazione_sede();
	}

	@CMISProperty(name="sigla_contratti:fig_giu_esterna_codfis")
	public String getFig_giu_esterna_codfis(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getAnagrafico().getCodice_fiscale();
	}

	@CMISProperty(name="sigla_contratti:fig_giu_esterna_pariva")
	public String getFig_giu_esterna_pariva(){
		if (getFigura_giuridica_esterna() == null)
			return null;
		return getFigura_giuridica_esterna().getAnagrafico().getPartita_iva();
	}

	@CMISProperty(name="sigla_contratti:tipo_norma")
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

	@CMISProperty(name="sigla_contratti:responsabile_codice")
	public Integer getCodiceResponsabile(){
		if (getDirettore() == null)
			return null;
		return getDirettore().getCd_terzo();
	}

	@CMISProperty(name="sigla_contratti:responsabile_denominazione")
	public String getDenominazioneResponsabile(){
		if (getDirettore() == null)
			return null;
		return getDirettore().getDenominazione_sede();
	}

	@CMISProperty(name="sigla_contratti:responsabile_procedimento_codice")
	public Integer getCodiceResponsabileProcedimento(){
		if (getResponsabile() == null)
			return null;
		return getResponsabile().getCd_terzo();
	}

	@CMISProperty(name="sigla_contratti:responsabile_procedimento_denominazione")
	public String getDenominazioneResponsabileProcedimento(){
		if (getResponsabile() == null)
			return null;
		return getResponsabile().getDenominazione_sede();
	}
	
	@CMISProperty(name="sigla_contratti:mod_individuazione_beneficiario")
	public String getModIndividuazioneBeneficiario(){
		if (getProcedura_amministrativa() == null)
			return null;
		return getProcedura_amministrativa().getDs_proc_amm();
	}
	
}