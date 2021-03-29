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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 25/02/2015
 */
package it.cnr.contab.docamm00.fatturapa.bulk;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.util.OrderedHashtable;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.RegimeFiscaleType;
public class DocumentoEleTrasmissioneBulk extends DocumentoEleTrasmissioneBase {
	private static final long serialVersionUID = 1L;
	public static final java.util.Dictionary<String, String> regimefiscaleKeys = new OrderedHashtable();
	static {
		regimefiscaleKeys.put(RegimeFiscaleType.RF_01.name(),RegimeFiscaleType.RF_01.value()+" - Regime ordinario");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_02.name(),RegimeFiscaleType.RF_02.value()+" - Regime dei contribuenti minimi (art. 1,c.96-117, L. 244/2007)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_04.name(),RegimeFiscaleType.RF_04.value()+" - Agricoltura e attività connesse e pesca (artt. 34 e 34-bis, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_05.name(),RegimeFiscaleType.RF_05.value()+" - Vendita sali e tabacchi (art. 74, c.1, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_06.name(),RegimeFiscaleType.RF_06.value()+" - Commercio dei fiammiferi (art. 74, c.1, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_07.name(),RegimeFiscaleType.RF_07.value()+" - Editoria (art. 74, c.1, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_08.name(),RegimeFiscaleType.RF_08.value()+" - Gestione di servizi di telefonia pubblica (art. 74, c.1, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_09.name(),RegimeFiscaleType.RF_09.value()+" - Rivendita di documenti di trasporto pubblico e di sosta (art. 74, c.1, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_10.name(),RegimeFiscaleType.RF_10.value()+" - Intrattenimenti, giochi e altre attività di cui alla tariffa allegata al D.P.R. 640/72 (art. 74, c.6, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_11.name(),RegimeFiscaleType.RF_11.value()+" - Agenzie di viaggi e turismo (art. 74-ter, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_12.name(),RegimeFiscaleType.RF_12.value()+" - Agriturismo (art. 5, c.2, L. 413/1991)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_13.name(),RegimeFiscaleType.RF_13.value()+" - Vendite a domicilio (art. 25-bis, c.6, D.P.R. 600/1973)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_14.name(),RegimeFiscaleType.RF_14.value()+" - Rivendita di beni usati, di oggetti d'arte, d'antiquariato o da collezione (art.	36, D.L. 41/1995)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_15.name(),RegimeFiscaleType.RF_15.value()+" - Agenzie di vendite all'asta di oggetti d'arte, antiquariato o da collezione (art. 40-bis, D.L. 41/1995)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_16.name(),RegimeFiscaleType.RF_16.value()+" - IVA per cassa P.A. (art. 6, c.5, D.P.R. 633/1972)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_17.name(),RegimeFiscaleType.RF_17.value()+" - IVA per cassa (art. 32-bis, D.L. 83/2012)");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_18.name(),RegimeFiscaleType.RF_18.value()+" - Altro");
		regimefiscaleKeys.put(RegimeFiscaleType.RF_19.name(),RegimeFiscaleType.RF_19.value()+" - Regime forfettario (art. 1 c. 54- 89 L. 190/2014)");	
	}
	private BulkList<DocumentoEleTestataBulk> docEleTestataColl = new BulkList<DocumentoEleTestataBulk>();
	
	/**
	 * [TERZO Tabella contenente le entitè  anagrafiche di secondo livello (terzi). Ogni entitè  anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

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
	 * Restituisce il valore di: [Tabella contenente le entitè  anagrafiche di secondo livello (terzi). Ogni entitè  anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

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
	 * Restituisce il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
	 **/
	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella contenente le entitè  anagrafiche di secondo livello (terzi). Ogni entitè  anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).

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
	 * Setta il valore di: [Rappresentazione dei Centri di Spesa e delle Unità Organizzative in una struttura ad albero organizzata su più livelli]
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
	 * Restituisce il valore di: [Unità organizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		Unita_organizzativaBulk unitaOrganizzativa = this.getUnitaOrganizzativa();
		if (unitaOrganizzativa == null)
			return null;
		return unitaOrganizzativa.getCd_unita_organizzativa();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Unità organizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.getUnitaOrganizzativa().setCd_unita_organizzativa(cdUnitaOrganizzativa);
	}

}