package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.util.enumeration.TipoDebitoSIOPE;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.annotation.StorageType;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.contab.util00.bulk.storage.AllegatoParentBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@StorageType(name="D:doccont:document")
public class V_mandato_reversaleBulk extends V_mandato_reversaleBase implements AllegatoParentBulk, StatoTrasmissione{
	TerzoBulk terzo = new TerzoBulk();
	Unita_organizzativaBulk uo = new Unita_organizzativaBulk();
	boolean stato_trasmissioneToBeUpdated = false;
	
	// PG_DISTINTA DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_distinta;
	private String documento;
	private BulkList<AllegatoGenericoBulk> archivioAllegati = new BulkList<AllegatoGenericoBulk>();
	protected BulkList<Mandato_rigaIBulk> mandato_rigaColl = new BulkList();

	public final static java.util.Dictionary cd_tipo_documento_contKeys;
	static 
	{
		cd_tipo_documento_contKeys = new java.util.Hashtable();
		cd_tipo_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN,	"Mandato");
		cd_tipo_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_REV,	"Reversale");				
	}
	
	public final static java.util.Dictionary ti_documento_contKeys;
	static 
	{
		ti_documento_contKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.TIPO_PAGAMENTO,				"Pagamento");
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.TIPO_ACCREDITAMENTO,			"Accreditamento");						
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_REGOLAM_SOSPESO,		"Regolamento Sospeso");		
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_REGOLARIZZAZIONE,	"Regolarizzazione");
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_TRASFERIMENTO, 		"Trasferimento");
		ti_documento_contKeys.put(it.cnr.contab.doccont00.core.bulk.ReversaleBulk.TIPO_INCASSO, 				"Incasso");				
	}
	
	public final static java.util.Dictionary statoKeys;
	static 
	{
		statoKeys = new it.cnr.jada.util.OrderedHashtable();
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_ANNULLATO,	"Annullato");
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_EMESSO,		"Emesso");
		statoKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_MANDATO_PAGATO,		"Pagato/Incassato");										
	}
	public final static java.util.Dictionary stato_trasmissioneKeys;
	static 
	{
		stato_trasmissioneKeys = new it.cnr.jada.util.OrderedHashtable();
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_NON_INSERITO,	"Non inserito in distinta");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO,	"Predisposto alla Firma");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_PRIMA_FIRMA,	"Prima Firma");				
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_INSERITO,		"Inserito in distinta");
		stato_trasmissioneKeys.put(it.cnr.contab.doccont00.core.bulk.MandatoBulk.STATO_TRASMISSIONE_TRASMESSO,		"Trasmesso");
		stato_trasmissioneKeys.put(StatoTrasmissione.ALL, "Tutti");

	}

	public final static java.util.Dictionary ti_cc_biKeys;
	static 
	{
		ti_cc_biKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_cc_biKeys.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_CC, "C/C");
		ti_cc_biKeys.put(it.cnr.contab.doccont00.core.bulk.SospesoBulk.TIPO_BANCA_ITALIA, "Banca d'Italia"); 
	}

	public final static Map<String,String> tipoDebitoSIOPEKeys = Arrays.asList(TipoDebitoSIOPE.values())
			.stream()
			.collect(Collectors.toMap(
					TipoDebitoSIOPE::value,
					TipoDebitoSIOPE::label,
					(oldValue, newValue) -> oldValue,
					Hashtable::new
			));

	public final static  Map<String,String> esito_OperazioneKeys = Arrays.asList(EsitoOperazione.values())
			.stream()
			.collect(Collectors.toMap(
					EsitoOperazione::value,
					EsitoOperazione::label,
					(oldValue, newValue) -> oldValue,
					Hashtable::new
			));

	public V_mandato_reversaleBulk() {
		super();
	}
	public V_mandato_reversaleBulk( Integer esercizio, String cd_tipo_documento_cont, String cd_cds, Long pg_documento_cont) {
		super( esercizio, cd_tipo_documento_cont, cd_cds, pg_documento_cont );
	}
	public Integer getCd_terzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:codice"))
	public java.lang.String getCd_unita_organizzativa() {
		Unita_organizzativaBulk uo = this.getUo();
		if (uo == null)
			return null;
		return uo.getCd_unita_organizzativa();
	}
	@StoragePolicy(name="P:strorg:uo", property=@StorageProperty(name="strorguo:descrizione"))
	public java.lang.String getDs_unita_organizzativa() {
		Unita_organizzativaBulk uo = this.getUo();
		if (uo == null)
			return null;
		return uo.getDs_unita_organizzativa();
	}

	/**
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
		return terzo;
	}
	/**
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUo() {
		return uo;
	}
	/**
	 * Inizializza l'Oggetto Bulk per la ricerca.
	 * @param bp Il Business Process in uso
	 * @param context Il contesto dell'azione
	 * @return OggettoBulk L'oggetto bulk inizializzato
	 */
	public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) 
	{
		setEsercizio( ((CNRUserContext)context.getUserContext()).getEsercizio() );
		
			try {
				if (!isUoDistintaTuttaSac(context))
				  setUo(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));
			} catch (ComponentException e) {
				throw new it.cnr.jada.DetailedRuntimeException(e);
			} catch (RemoteException e1) {
				throw new it.cnr.jada.DetailedRuntimeException(e1);
			}
	
		return this;
	}
	// l'oggetto è un Mandato
	public boolean isMandato() {
		return Numerazione_doc_contBulk.TIPO_MAN.equals( getCd_tipo_documento_cont());
	}
	// l'oggetto è un Mandato di Accreditamento
	public boolean isMandatoAccreditamento() {
		return Numerazione_doc_contBulk.TIPO_MAN.equals( getCd_tipo_documento_cont()) && 
		       MandatoBulk.TIPO_ACCREDITAMENTO.equals( getTi_documento_cont());
	}
	// l'oggetto è una Reversale
	public boolean isReversale() {
		return Numerazione_doc_contBulk.TIPO_REV.equals( getCd_tipo_documento_cont());
	}
	//l'oggetto è una Reversale di Trasferimento
	public boolean isReversaleTrasferimento() {
		return Numerazione_doc_contBulk.TIPO_REV.equals( getCd_tipo_documento_cont()) && 
			   ReversaleBulk.TIPO_TRASFERIMENTO.equals( getTi_documento_cont());
	}
	public boolean isROTerzo() 
	{
		return terzo == null || terzo.getCrudStatus() == NORMAL;
	}
	public boolean isStato_trasmissioneToBeUpdated() {
		return stato_trasmissioneToBeUpdated;
	}
	public void setCd_terzo(Integer cd_terzo) {
		this.getTerzo().setCd_terzo(cd_terzo);
	}
	public void setCd_unita_organizzativa(String cd_uo) {
		this.getUo().setCd_unita_organizzativa(cd_uo);
	}
	public void setStato_trasmissioneToBeUpdated(boolean newStato_trasmissioneToBeUpdated) {
		stato_trasmissioneToBeUpdated = newStato_trasmissioneToBeUpdated;
	}
	/**
	 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 */
	public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
		terzo = newTerzo;
	}
	/**
	 * @param newUo it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUo(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUo) {
		uo = newUo;
	}
	public boolean isUoDistintaTuttaSac(ActionContext context) throws ComponentException, RemoteException{
		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		if (sess.getVal01(context.getUserContext(), new Integer(0), null, "UO_SPECIALE", "UO_DISTINTA_TUTTA_SAC").equals(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context).getCd_unita_organizzativa()))
		{
			return true;
		}		
		return false;
	}
	/* 
	 * Getter dell'attributo pg_distinta
	 */
	public java.lang.Long getPg_distinta() {
		return pg_distinta;
	}
	/* 
	 * Setter dell'attributo pg_distinta
	 */
	public void setPg_distinta(java.lang.Long pg_distinta) {
		this.pg_distinta = pg_distinta;
	}

	public String getCMISFolderName() {
		String suffix = getCd_tipo_documento_cont().equalsIgnoreCase(it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk.TIPO_MAN) ? "Mandato n." : "Reversale n.";
		suffix = suffix.concat(String.valueOf(getPg_documento_cont()));
		return suffix;
	}
	
	@Override
	public String toString() {
		return getCMISFolderName();
	}

	public String getStorePath() {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				getCd_unita_organizzativa(),
				getCd_tipo_documento_cont().equals(Numerazione_doc_contBulk.TIPO_MAN)?"Mandati":"Reversali",
				Optional.ofNullable(getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				getCMISFolderName()
		).stream().collect(
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	public String getCMISName() {
		return (getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN) ? "Mandato n. " : "Reversale n. ")
				+ getPg_documento_cont() +
				(Optional.ofNullable(getStato())
						.filter(stato -> stato.equals(MandatoBulk.STATO_MANDATO_ANNULLATO))
						.map(s -> " A.pdf")
						.orElse(".pdf"));

	}

	public String getReportName() {
		return getCd_tipo_documento_cont().equalsIgnoreCase(Numerazione_doc_contBulk.TIPO_MAN) ? "/doccont/doccont/vpg_man_rev_ass.jasper" : "/doccont/doccont/vpg_reversale.jasper";
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				archivioAllegati };
	}	
	public AllegatoGenericoBulk removeFromArchivioAllegati(int index) {
		return getArchivioAllegati().remove(index);
	}
	public int addToArchivioAllegati(AllegatoGenericoBulk allegato) {
		archivioAllegati.add(allegato);
		return archivioAllegati.size()-1;		
	}
	public BulkList<AllegatoGenericoBulk> getArchivioAllegati() {
		return archivioAllegati;
	}
	public void setArchivioAllegati(
			BulkList<AllegatoGenericoBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
    public String getCssClassTi_cc_bi(){
        if(getTi_cc_bi().compareTo("B")==0)
            return "TableColumnRedBold";
        else
            return null;
    }

    /**
     * @return it.cnr.jada.bulk.BulkList
     */
    public it.cnr.jada.bulk.BulkList<Mandato_rigaIBulk> getMandato_rigaColl() {
        return mandato_rigaColl;
    }
    /**
     * @param newMandato_rigaColl it.cnr.jada.bulk.BulkList
     */
    public void setMandato_rigaColl(it.cnr.jada.bulk.BulkList<Mandato_rigaIBulk> newMandato_rigaColl) {
        mandato_rigaColl = newMandato_rigaColl;
    }
}
