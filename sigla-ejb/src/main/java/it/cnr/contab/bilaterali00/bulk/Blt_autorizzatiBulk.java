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
 * Date 02/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
public class Blt_autorizzatiBulk extends Blt_autorizzatiBase {
	
	private BulkList bltAutorizzatiDettColl = new BulkList();

	/**
	 * [BLT_PROGETTI null]
	 **/
	private Blt_progettiBulk bltProgetti =  new Blt_progettiBulk();
	/**
	 * [TERZO Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
	 * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	private TerzoBulk terzo =  new TerzoBulk();
	/**
	 * [CDR Rappresentazione dei Centri di Responsabilità di primo e secondo livello in una struttura ad albero organizzata su più livelli]
	 **/
	private CdrBulk cdrTerzo =  new CdrBulk();

	private ComuneBulk  comuneEnteDiAppartenenza;

	private java.util.Collection caps_comune;
	
	private BltIstitutiBulk bltIstituto;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_AUTORIZZATI
	 **/
	public Blt_autorizzatiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_AUTORIZZATI
	 **/
	public Blt_autorizzatiBulk(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo) {
		super(cdAccordo, cdProgetto, cdTerzo);
		setBltProgetti( new Blt_progettiBulk(cdAccordo,cdProgetto) );
		setTerzo( new TerzoBulk(cdTerzo) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public Blt_progettiBulk getBltProgetti() {
		return bltProgetti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setBltProgetti(Blt_progettiBulk bltProgetti)  {
		this.bltProgetti=bltProgetti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
	 * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public TerzoBulk getTerzo() {
		return terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Tabella contenente le entità anagrafiche di secondo livello (terzi). Ogni entità anagrafica di secondo livello afferisce ad una di primo (ANAGRAFICO).
	 * Rappresenta le sedi, reali o per gestione, in cui si articola un soggetto anagrafico]
	 **/
	public void setTerzo(TerzoBulk terzo)  {
		this.terzo=terzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Rappresentazione dei Centri di Responsabilità di primo e secondo livello in una struttura ad albero organizzata su più livelli]
	 **/
	public CdrBulk getCdrTerzo() {
		return cdrTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Rappresentazione dei Centri di Responsabilità di primo e secondo livello in una struttura ad albero organizzata su più livelli]
	 **/
	public void setCdrTerzo(CdrBulk cdrTerzo)  {
		this.cdrTerzo=cdrTerzo;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_progettiBulk bltProgetti = this.getBltProgetti();
		if (bltProgetti == null)
			return null;
		return getBltProgetti().getCd_accordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltProgetti().setCd_accordo(cdAccordo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		Blt_progettiBulk bltProgetti = this.getBltProgetti();
		if (bltProgetti == null)
			return null;
		return getBltProgetti().getCd_progetto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.getBltProgetti().setCd_progetto(cdProgetto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		TerzoBulk terzo = this.getTerzo();
		if (terzo == null)
			return null;
		return getTerzo().getCd_terzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.getTerzo().setCd_terzo(cdTerzo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdrTerzo]
	 **/
	public java.lang.String getCdCdrTerzo() {
		CdrBulk cdrTerzo = this.getCdrTerzo();
		if (cdrTerzo == null)
			return null;
		return getCdrTerzo().getCd_centro_responsabilita();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdrTerzo]
	 **/
	public void setCdCdrTerzo(java.lang.String cdCdrTerzo)  {
		this.getCdrTerzo().setCd_centro_responsabilita(cdCdrTerzo);
	}
	
	public Boolean isItaliano(){
		return getTiItalianoEstero()!=null&&getTiItalianoEstero().equals(NazioneBulk.ITALIA);
	}
	public Boolean isStraniero(){
		return getTiItalianoEstero()!=null&&getTiItalianoEstero().equals(NazioneBulk.EXTRA_CEE);
	}
	public BulkList getBltAutorizzatiDettColl() {
		return bltAutorizzatiDettColl;
	}
	public void setBltAutorizzatiDettColl(BulkList bltAutorizzatiDettColl) {
		this.bltAutorizzatiDettColl = bltAutorizzatiDettColl;
	}
	public BulkCollection[] getBulkLists() {
		return new it.cnr.jada.bulk.BulkCollection[] { 
				getBltAutorizzatiDettColl() };
	}
	public int addToBltAutorizzatiDettColl(Blt_autorizzati_dettBulk dett) {
		dett.setBltAutorizzati(this);
		getBltAutorizzatiDettColl().add(dett);
		return getBltAutorizzatiDettColl().size()-1;
	}	
	public Blt_autorizzati_dettBulk removeFromBltAutorizzatiDettColl(int index) {
		Blt_autorizzati_dettBulk dett = (Blt_autorizzati_dettBulk)getBltAutorizzatiDettColl().remove(index);
		return dett;
	}
	public ComuneBulk getComuneEnteDiAppartenenza() {
		return comuneEnteDiAppartenenza;
	}
	public void setComuneEnteDiAppartenenza(ComuneBulk comuneEnteDiAppartenenza) {
		this.comuneEnteDiAppartenenza = comuneEnteDiAppartenenza;
	}
	public boolean isROdsComuneEnteDiAppartenenza() {
		return getComuneEnteDiAppartenenza() == null || getComuneEnteDiAppartenenza().getCrudStatus() == OggettoBulk.NORMAL;
	}
	@Override
	public Long getPgComuneEnteDiAppartenenza() {
		ComuneBulk comuneEnteDiAppartenenza = this.getComuneEnteDiAppartenenza();
		if (comuneEnteDiAppartenenza == null)
			return null;
		return getComuneEnteDiAppartenenza().getPg_comune();
	}
	@Override
	public void setPgComuneEnteDiAppartenenza(Long pgComuneEnteDiAppartenenza) {
		this.getComuneEnteDiAppartenenza().setPg_comune(pgComuneEnteDiAppartenenza);
	}
	public java.util.Collection getCaps_comune() {
		return caps_comune;
	}
	public void setCaps_comune(java.util.Collection caps_comune) {
		this.caps_comune = caps_comune;
	}
	public BltIstitutiBulk getBltIstituto() {
		return bltIstituto;
	}
	public void setBltIstituto(BltIstitutiBulk bltIstituto) {
		this.bltIstituto = bltIstituto;
	}
}