/*
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.cmis.annotation.CMISPolicy;
import it.cnr.contab.cmis.annotation.CMISProperty;
import it.cnr.contab.cmis.annotation.CMISType;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.pdg00.bulk.cmis.AllegatoPdGVariazioneDocumentBulk;
import it.cnr.contab.pdg00.bulk.cmis.PdgVariazioneDocument;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
@CMISType(name="D:varpianogest:document")
public class ArchiviaStampaPdgVariazioneBulk extends OggettoBulk {

	private Pdg_variazioneBulk pdg_variazioneForPrint;
	private boolean pdg_variazioneForPrintEnabled;
	private PdgVariazioneDocument pdgVariazioneDocument; 
	private String tiSigned;
	private BulkList<AllegatoPdGVariazioneDocumentBulk> archivioAllegati = new BulkList<AllegatoPdGVariazioneDocumentBulk>();
	private static final java.util.Dictionary ti_signedKeys = new it.cnr.jada.util.OrderedHashtable();	
	private static final java.util.Dictionary<String,String> ti_signedTextKeys = new it.cnr.jada.util.OrderedHashtable();	
	
	final public static String VIEW_ALL = "ALL";
	final public static String VIEW_SIGNED = "SIGNED";
	final public static String VIEW_NOT_SIGNED = "NOT_SIGNED";

	static {
		ti_signedKeys.put(VIEW_ALL,"Tutte");
		ti_signedKeys.put(VIEW_SIGNED,"Firmate");
		ti_signedKeys.put(VIEW_NOT_SIGNED,"Da Firmare");

		ti_signedTextKeys.put(VIEW_SIGNED,"Variazione su cui è stata apposta la Firma Digitale");
		ti_signedTextKeys.put(VIEW_NOT_SIGNED,"In attesa di Firma Digitale");

	}

	public ArchiviaStampaPdgVariazioneBulk() {
		super();
	}
	
	public Pdg_variazioneBulk getPdg_variazioneForPrint() {
		return pdg_variazioneForPrint;
	}
	
	public PdgVariazioneDocument getPdgVariazioneDocument() {
		return pdgVariazioneDocument;
	}

	public void setPdgVariazioneDocument(PdgVariazioneDocument pdgVariazioneDocument) {
		this.pdgVariazioneDocument = pdgVariazioneDocument;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return boolean
	 */
	public boolean isPdg_variazioneForPrintEnabled() {
		return pdg_variazioneForPrintEnabled;
	}
	
	public void setPdg_variazioneForPrintEnabled(boolean b) {
		pdg_variazioneForPrintEnabled = b;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setPdg_variazioneForPrint(Pdg_variazioneBulk newPdg_variazioneForPrint) {
			pdg_variazioneForPrint = newPdg_variazioneForPrint;
	}
	public final java.util.Dictionary getTi_signedKeys() {
		return ti_signedKeys;
	}
	
	public String getTiSigned() {
		return tiSigned;
	}
	public void setTiSigned(String tiSigned) {
		this.tiSigned = tiSigned;
	}
	public String getTiSignedText() {
		if (getTiSigned() == null)
			return null;
		return ti_signedTextKeys.get(getTiSigned());
	}
	
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrintEnabled boolean
	 */
	@CMISProperty(name="varpianogest:esercizio")
	public Integer getEsercizio(){
		return getPdg_variazioneForPrint().getEsercizio();
	}
	
	@CMISPolicy(name="P:cm:titled", property=@CMISProperty(name="cm:description"))
	public java.lang.String getDs_variazione () {
		return pdg_variazioneForPrint.getDs_variazione();
	}
	
	public void setDs_variazione(java.lang.String ds_variazione)  {
		this.pdg_variazioneForPrint.setDs_variazione(ds_variazione);
	}
	
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pdg_variazioneForPrint.setPg_variazione_pdg(pg_variazione_pdg);
	}
	@CMISProperty(name="varpianogest:numeroVariazione", converterBeanName="cmis.converter.longToIntegerConverter")
	public java.lang.Long getPg_variazione_pdg () {
		return this.pdg_variazioneForPrint.getPg_variazione_pdg();
	}

	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:codice"))
	public String getCd_cds(){
		return pdg_variazioneForPrint.getCentro_responsabilita().getUnita_padre().getCd_cds();
	}

	@CMISPolicy(name="P:strorg:cds", property=@CMISProperty(name="strorgcds:descrizione"))
	public String getDs_cds(){
		return pdg_variazioneForPrint.getCentro_responsabilita().getUnita_padre().getUnita_padre().getDs_unita_organizzativa();
	}
	
	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:codice"))
	public String getCd_unita_organizzativa(){
		return pdg_variazioneForPrint.getCentro_responsabilita().getUnita_padre().getCd_unita_organizzativa();
	}

	@CMISPolicy(name="P:strorg:uo", property=@CMISProperty(name="strorguo:descrizione"))
	public String getDs_unita_organizzativa(){
		return pdg_variazioneForPrint.getCentro_responsabilita().getUnita_padre().getDs_unita_organizzativa();
	}

	@CMISPolicy(name="P:strorg:cdr", property=@CMISProperty(name="strorgcdr:codice"))
	public java.lang.String getCd_centro_responsabilita() {
		return pdg_variazioneForPrint.getCd_centro_responsabilita();
	}

	@CMISPolicy(name="P:strorg:cdr", property=@CMISProperty(name="strorgcdr:descrizione"))
	public java.lang.String getDs_centro_responsabilita() {
		return pdg_variazioneForPrint.getCentro_responsabilita().getDs_cdr();
	}
	
	public CdrBulk getCentro_responsabilita() {
		return pdg_variazioneForPrint.getCentro_responsabilita();
	}

	public void setCentro_responsabilita(CdrBulk bulk) {
		pdg_variazioneForPrint.setCentro_responsabilita(bulk);
	}
	
	
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.pdg_variazioneForPrint.setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	public BulkList<AllegatoPdGVariazioneDocumentBulk> getArchivioAllegati() {
		return archivioAllegati;
	}

	public void setArchivioAllegati(
			BulkList<AllegatoPdGVariazioneDocumentBulk> archivioAllegati) {
		this.archivioAllegati = archivioAllegati;
	}
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getArchivioAllegati()};
	};
	
	public int addToArchivioAllegati(AllegatoPdGVariazioneDocumentBulk dett) {
		getArchivioAllegati().add(dett);
		return getArchivioAllegati().size()-1;
	}	
	public AllegatoPdGVariazioneDocumentBulk removeFromArchivioAllegati(int index) {
		AllegatoPdGVariazioneDocumentBulk dett = (AllegatoPdGVariazioneDocumentBulk)getArchivioAllegati().remove(index);
		return dett;
	}
	
}
