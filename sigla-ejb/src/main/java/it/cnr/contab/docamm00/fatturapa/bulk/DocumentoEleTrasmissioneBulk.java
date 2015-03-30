/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
public class DocumentoEleTrasmissioneBulk extends DocumentoEleTrasmissioneBase {
	private static final long serialVersionUID = 1L;

	private BulkList<DocumentoEleTestataBulk> docEleTestataColl = new BulkList<DocumentoEleTestataBulk>();
	
	/**
	 * [TERZO Tabella contenente le entit√† anagrafiche di secondo livello (terzi). Ogni entit√† anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	private TerzoBulk prestatore =  new TerzoBulk();
	private AnagraficoBulk prestatoreAnag =  new AnagraficoBulk();

	private TerzoBulk rappresentante =  new TerzoBulk();
	private AnagraficoBulk rappresentanteAnag =  new AnagraficoBulk();

	private TerzoBulk committente =  new TerzoBulk();

	private TerzoBulk intermediario =  new TerzoBulk();
	private AnagraficoBulk intermediarioAnag =  new AnagraficoBulk();

	private Unita_organizzativaBulk unitaOrganizzativa =  new Unita_organizzativaBulk();
	private Unita_organizzativaBulk unitaCompetenza =  new Unita_organizzativaBulk();
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRASMISSIONE
	 **/
	public DocumentoEleTrasmissioneBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: DOCUMENTO_ELE_TRASMISSIONE
	 **/
	public DocumentoEleTrasmissioneBulk(java.lang.String idPaese, java.lang.String idCodice, java.lang.Long identificativoSdi) {
		super(idPaese, idCodice, identificativoSdi);
	}
	@Override
	public BulkCollection[] getBulkLists() {
		// Metti solo le liste di oggetti che devono essere resi persistenti
		return new it.cnr.jada.bulk.BulkCollection[] { 
				docEleTestataColl,
		};
	}
	
	public int addToDocEleTestataColl( DocumentoEleTestataBulk doc ) {
		docEleTestataColl.add(doc);
		doc.setDocumentoEleTrasmissione(this);		
		return docEleTestataColl.size()-1;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Tabella contenente le entit√† anagrafiche di secondo livello (terzi). Ogni entit√† anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public TerzoBulk getPrestatore() {
		return prestatore;
	}
	public TerzoBulk getRappresentante() {
		return rappresentante;
	}
	public TerzoBulk getCommittente() {
		return committente;
	}
	public TerzoBulk getIntermediario() {
		return intermediario;
	}
	public AnagraficoBulk getPrestatoreAnag() {
		return prestatoreAnag;
	}
	public AnagraficoBulk getRappresentanteAnag() {
		return rappresentanteAnag;
	}
	public AnagraficoBulk getIntermediarioAnag() {
		return intermediarioAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unit‡ Organizzative in una struttura ad albero organizzata su pi˘ livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella contenente le entit√† anagrafiche di secondo livello (terzi). Ogni entit√† anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public void setPrestatore(TerzoBulk terzo)  {
		this.prestatore=terzo;
	}
	public void setRappresentante(TerzoBulk terzo)  {
		this.rappresentante=terzo;
	}
	public void setCommittente(TerzoBulk terzo)  {
		this.committente=terzo;
	}
	public void setIntermediario(TerzoBulk terzo)  {
		this.intermediario=terzo;
	}
	public void setPrestatoreAnag(AnagraficoBulk prestatoreAnag) {
		this.prestatoreAnag = prestatoreAnag;
	}
	public void setRappresentanteAnag(AnagraficoBulk rappresentanteAnag) {
		this.rappresentanteAnag = rappresentanteAnag;
	}
	public void setIntermediarioAnag(AnagraficoBulk intermediarioAnag) {
		this.intermediarioAnag = intermediarioAnag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unit‡ Organizzative in una struttura ad albero organizzata su pi˘ livelli]
	 **/
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa)  {
		this.unitaOrganizzativa=unitaOrganizzativa;
	}	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCdTerzo]
	 **/
	public java.lang.Integer getPrestatoreCdTerzo() {
		TerzoBulk terzo = this.getPrestatore();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCdTerzo]
	 **/
	public void setPrestatoreCdTerzo(java.lang.Integer prestatoreCdTerzo)  {
		this.getPrestatore().setCd_terzo(prestatoreCdTerzo);
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [prestatoreCdTerzo]
	 **/
	public void setPrestatoreCdAnag(java.lang.Integer prestatoreCdAnag)  {
		this.getPrestatoreAnag().setCd_anag(prestatoreCdAnag);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [prestatoreCdTerzo]
	 **/
	public java.lang.Integer getPrestatoreCdAnag() {
		AnagraficoBulk anagrafico = this.getPrestatoreAnag();
		if (anagrafico == null)
			return null;
		return anagrafico.getCd_anag();
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCdTerzo]
	 **/
	public java.lang.Integer getRappresentanteCdTerzo() {
		TerzoBulk terzo = this.getRappresentante();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCdTerzo]
	 **/
	public void setRappresentanteCdTerzo(java.lang.Integer rappresentanteCdTerzo)  {
		this.getRappresentante().setCd_terzo(rappresentanteCdTerzo);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [rappresentanteCdTerzo]
	 **/
	public java.lang.Integer getRappresentanteCdAnag() {
		AnagraficoBulk anagrafico = this.getRappresentanteAnag();
		if (anagrafico == null)
			return null;
		return anagrafico.getCd_anag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [rappresentanteCdTerzo]
	 **/
	public void setRappresentanteCdAnag(java.lang.Integer rappresentanteCdAnag)  {
		this.getRappresentanteAnag().setCd_anag(rappresentanteCdAnag);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [committenteCdTerzo]
	 **/
	public java.lang.Integer getCommittenteCdTerzo() {
		TerzoBulk terzo = this.getCommittente();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [committenteCdTerzo]
	 **/
	public void setCommittenteCdTerzo(java.lang.Integer committenteCdTerzo)  {
		this.getCommittente().setCd_terzo(committenteCdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCdTerzo]
	 **/
	public java.lang.Integer getIntermediarioCdTerzo() {
		TerzoBulk terzo = this.getIntermediario();
		if (terzo == null)
			return null;
		return terzo.getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCdTerzo]
	 **/
	public void setIntermediarioCdTerzo(java.lang.Integer intermediarioCdTerzo)  {
		this.getIntermediario().setCd_terzo(intermediarioCdTerzo);
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [intermediarioCdTerzo]
	 **/
	public java.lang.Integer getIntermediarioCdAnag() {
		AnagraficoBulk anagrafico = this.getIntermediarioAnag();
		if (anagrafico == null)
			return null;
		return anagrafico.getCd_anag();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [intermediarioCdTerzo]
	 **/
	public void setIntermediarioCdAnag(java.lang.Integer intermediarioCdAnag)  {
		this.getIntermediarioAnag().setCd_anag(intermediarioCdAnag);
	}
	
	public BulkList<DocumentoEleTestataBulk> getDocEleTestataColl() {
		return docEleTestataColl;
	}
	public void setDocEleTestataColl(
			BulkList<DocumentoEleTestataBulk> docEleTestataColl) {
		this.docEleTestataColl = docEleTestataColl;
	}	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Unit‡ organizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return unitaOrganizzativa.getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Unit‡ organizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Unit‡ organizzativa]
	 **/
	public java.lang.String getCdUnitaCompetenza() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaCompetenza();
		if (unitaOrganizzativa == null)
			return null;
		return unitaOrganizzativa.getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Unit‡ organizzativa]
	 **/
	public void setCdUnitaCompetenza(java.lang.String cdUnitaOrganizzativa)  {
		this.getUnitaCompetenza().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}
	
	public Unita_organizzativaBulk getUnitaCompetenza() {
		return unitaCompetenza;
	}
	public void setUnitaCompetenza(Unita_organizzativaBulk unitaCompetenza) {
		this.unitaCompetenza = unitaCompetenza;
	}	
	
}