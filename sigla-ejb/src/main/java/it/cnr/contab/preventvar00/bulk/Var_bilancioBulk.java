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

package it.cnr.contab.preventvar00.bulk;

import it.cnr.contab.config00.esercizio.bulk.Esercizio_baseBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.preventvar00.tabrif.bulk.*;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.bulk.*;

public class Var_bilancioBulk extends Var_bilancioBase implements ICancellatoLogicamente{

	private it.cnr.jada.bulk.BulkList dettagli = new BulkList();

    protected final static java.util.Dictionary <String,String> TIPI_VARIAZIONE;
    public final static String STORNO_E = "STORNO_E";
    public final static String STORNO_S = "STORNO_S";
    public final static String VAR_QUADRATURA = "VAR_QUAD";
    public final static String VAR_LIBERA = "VAR_LIBERA";
    public final static String VAR_ECO = "VAR_ECO";
	public final static String VAR_MSP = "VAR_MSP";
	public final static String PREL_FON = "PREL_FON";
	public final static String VAR_REGOLARIZZAZIONE = "VAR_REG";

	static {
		TIPI_VARIAZIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPI_VARIAZIONE.put(STORNO_E,"Storno tra entrate");
		TIPI_VARIAZIONE.put(STORNO_S,"Storno tra spese");
		TIPI_VARIAZIONE.put(VAR_QUADRATURA,"Variazione a quadratura");
		TIPI_VARIAZIONE.put(VAR_ECO,"Economie");
		TIPI_VARIAZIONE.put(VAR_MSP,"Maggiori spese");		
		TIPI_VARIAZIONE.put(PREL_FON,"Prelievo da fondi");		
		TIPI_VARIAZIONE.put(VAR_REGOLARIZZAZIONE,"Regolarizzazione Contabile");		
	}
	
	private Causale_var_bilancioBulk causaleVariazione = new Causale_var_bilancioBulk();
	private Pdg_variazioneBulk pdg_variazione = new Pdg_variazioneBulk();
	private Var_stanz_resBulk var_stanz_res = new Var_stanz_resBulk();
	private MandatoIBulk mandato = new MandatoIBulk();
	private Esercizio_baseBulk esercizio_res;
	protected java.util.Collection esercizi_res;
	
	public final static java.lang.String PROVVISORIA = "P";
	public final static java.lang.String DEFINITIVA = "D";
	public final static java.lang.String ANNULLATA = "A";
	private it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk bilancio;
public Var_bilancioBulk() {
	super();
}
public Var_bilancioBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String ti_appartenenza) {
	super(cd_cds,esercizio,pg_variazione,ti_appartenenza);
	setBilancio(new it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk(cd_cds,esercizio,ti_appartenenza));
}
/**
 * Aggiunge il dettaglio di variazione specificato alla lista dei dettagli di variazione
 *
 * @param varBilancioDett dettaglio da aggiungere	
 * @return l'indice del dettaglio aggiunto
 */
public int addToDettagli(Var_bilancio_detBulk varBilancioDett) {

	varBilancioDett.setVarBilancio(this);
	getDettagli().add(varBilancioDett);
	varBilancioDett.setTipoGestione(getTipoGestione());

	return getDettagli().size()-1;
}
/**
 * Determina il tipo di gestione E/S in relazione al tipo di variazione
 * 
 * @param tipoVar tipo della variazione
 * @return il tipo di gestione E/S
 */
private String findTipoGestione(String tipoVar) {

	if (tipoVar !=null && tipoVar.equals(STORNO_E))
		return it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_gestione_entrata;

	if (tipoVar !=null && tipoVar.equals(STORNO_S))
		return it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_gestione_spesa;

	if (tipoVar !=null && tipoVar.equals(PREL_FON))
		return it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_gestione_spesa;

	if (tipoVar !=null && getCompetenzaResiduo() != null && getCompetenzaResiduo().equalsIgnoreCase("R"))
		return it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk.tipo_gestione_spesa;

	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'bilancio'
 *
 * @return Il valore della proprietà 'bilancio'
 */
public it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk getBilancio() {
	return bilancio;
}

public BulkCollection[] getBulkLists() {

	return new it.cnr.jada.bulk.BulkCollection[] { this.getDettagli() };
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'causaleVariazione'
 *
 * @return Il valore della proprietà 'causaleVariazione'
 */
public Causale_var_bilancioBulk getCausaleVariazione() {
	return causaleVariazione;
}
public java.lang.String getCd_causale_var_bilancio() {
	it.cnr.contab.preventvar00.tabrif.bulk.Causale_var_bilancioBulk causaleVariazione = this.getCausaleVariazione();
	if (causaleVariazione == null)
		return null;
	return causaleVariazione.getCd_causale_var_bilancio();
}
public java.lang.String getCd_cds() {
	it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk bilancio = this.getBilancio();
	if (bilancio == null)
		return null;
	return bilancio.getCd_cds();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'dettagli'
 *
 * @return Il valore della proprietà 'dettagli'
 */
public it.cnr.jada.bulk.BulkList getDettagli() {
	return dettagli;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk bilancio = this.getBilancio();
	if (bilancio == null)
		return null;
	return bilancio.getEsercizio();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipiVariazioneKeys'
 *
 * @return Il valore della proprietà 'tipiVariazioneKeys'
 */
public java.util.Dictionary getEsercizio_importiKeys() {
	it.cnr.jada.util.OrderedHashtable aD = new it.cnr.jada.util.OrderedHashtable();
	if(bilancio == null) {
	 return aD;
	}
	aD.put(bilancio.getEsercizio(),bilancio.getEsercizio());
    if(bilancio.getTi_appartenenza().equals(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome.APPARTENENZA_CNR)) {
 	 Integer aA2=new Integer((bilancio.getEsercizio().intValue()+1));
	 Integer aA3=new Integer((bilancio.getEsercizio().intValue()+2));
	 aD.put(aA2,aA2);
	 aD.put(aA3,aA3);
    }
	return aD;
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk bilancio = this.getBilancio();
	if (bilancio == null)
		return null;
	return bilancio.getTi_appartenenza();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipiVariazioneKeys'
 *
 * @return Il valore della proprietà 'tipiVariazioneKeys'
 */
public java.util.Dictionary getTipiVariazioneKeys(){
	return TIPI_VARIAZIONE;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipoGestione'
 *
 * @return Il valore della proprietà 'tipoGestione'
 */
public String getTipoGestione() {

	return findTipoGestione(getTi_variazione());
}
public java.math.BigDecimal getTotaleEntrate() {

	java.math.BigDecimal entrate = new java.math.BigDecimal(0);

	for (java.util.Iterator i=getDettagli().iterator();i.hasNext();){
		Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)i.next();
		entrate = entrate.add(varBilDett.getImportoEntrata());
	}

	return entrate;
}
public java.math.BigDecimal getTotaleSpese() {

	java.math.BigDecimal spese = new java.math.BigDecimal(0);

	for (java.util.Iterator i=getDettagli().iterator();i.hasNext();){
		Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)i.next();
		spese = spese.add(varBilDett.getImportoSpesa());
	}

	return spese;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipoVariazioneRO'
 *
 * @return Il valore della proprietà 'tipoVariazioneRO'
 */
public boolean hasVoceDuplicata(Var_bilancio_detBulk varDett, it.cnr.contab.prevent00.bulk.V_assestato_voceBulk voce) {

	boolean ok = false;
	for (java.util.Iterator i = getDettagli().iterator();i.hasNext();){
		Var_bilancio_detBulk det = (Var_bilancio_detBulk)i.next();
		if(!det.equals(varDett) && det.getCd_voce()!=null && det.getCd_voce().compareTo(voce.getCd_voce())==0)
				ok = true;
	}
	return ok;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'definitiva'
 *
 * @return Il valore della proprietà 'definitiva'
 */
public boolean isDefinitiva() {
	return (getStato()!=null && getStato().equals(DEFINITIVA));
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'esercizio_importiRO'
 *
 * @return Il valore della proprietà 'esercizio_importiRO'
 */
public boolean isEsercizio_importiRO() {
	return false;
//	return (getTi_variazione().equals(STORNO_E) || getTi_variazione().equals(STORNO_S)) && (!getDettagli().isEmpty());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'tipoVariazioneRO'
 *
 * @return Il valore della proprietà 'tipoVariazioneRO'
 */
public boolean isTipoVariazioneRO() {
	return isNotNew() &&
		   (getTi_variazione()!= null && 
		    getTi_variazione().equals(VAR_REGOLARIZZAZIONE));
//	return (getTi_variazione().equals(STORNO_E) || getTi_variazione().equals(STORNO_S)) && (!getDettagli().isEmpty());
}
/**
 * Rimuove il dettagli di variazione con indice specificato dalla lista dei dettagli della variazione
 *
 * @param index	indice del dettaglio da rimuovere
 * @return il dettaglio rimosso
 */
public Var_bilancio_detBulk removeFromDettagli(int index) {
	return (Var_bilancio_detBulk)getDettagli().remove(index);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'bilancio'
 *
 * @param newBilancio	Il valore da assegnare a 'bilancio'
 */
public void setBilancio(it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk newBilancio) {
	bilancio = newBilancio;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'causaleVariazione'
 *
 * @param newCausaleVariazione	Il valore da assegnare a 'causaleVariazione'
 */
public void setCausaleVariazione(Causale_var_bilancioBulk newCausaleVariazione) {
	causaleVariazione = newCausaleVariazione;
}
public void setCd_causale_var_bilancio(java.lang.String cd_causale_var_bilancio) {
	this.getCausaleVariazione().setCd_causale_var_bilancio(cd_causale_var_bilancio);
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getBilancio().setCd_cds(cd_cds);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'dettagli'
 *
 * @param newDettagli	Il valore da assegnare a 'dettagli'
 */
public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
	dettagli = newDettagli;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getBilancio().setEsercizio(esercizio);
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getBilancio().setTi_appartenenza(ti_appartenenza);
}
public boolean verificaQuadratura(){

	if (getTotaleEntrate().compareTo(getTotaleSpese())==0)
		return true;
	return false;
}
public boolean verificaTipoVariazione(){

	String tipoGestione = findTipoGestione(getTi_variazione());
	if (tipoGestione!= null){
		for (java.util.Iterator i = getDettagli().iterator();i.hasNext();){
			Var_bilancio_detBulk varBilDett = (Var_bilancio_detBulk)i.next();
			if (varBilDett.getTi_gestione() != null && !varBilDett.getTi_gestione().equals(tipoGestione))
				return false;
		}
	}

	return true;
}
/**
 * @param bulk
 */
public void setPdg_variazione(Pdg_variazioneBulk newPdg_variazione) {
	pdg_variazione = newPdg_variazione;
}
/*
 * Setter dell'attributo esercizio_pdg_variazione
 */
public void setEsercizio_pdg_variazione(java.lang.Integer esercizio_pdg_variazione) {
	this.getPdg_variazione().setEsercizio(esercizio_pdg_variazione);
}
/*
 * Setter dell'attributo pg_variazione_pdg
 */
public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg) {
	this.getPdg_variazione().setPg_variazione_pdg(pg_variazione_pdg);
}

/**
 * @return
 */
public Pdg_variazioneBulk getPdg_variazione() {
	return pdg_variazione;
}
public java.lang.Integer getEsercizio_pdg_variazione() {
	Pdg_variazioneBulk pdgVariazione = this.getPdg_variazione();
	if (pdgVariazione == null)
		return null;
	return pdgVariazione.getEsercizio();
}
public java.lang.Long getPg_variazione_pdg() {
	Pdg_variazioneBulk pdgVariazione = this.getPdg_variazione();
	if (pdgVariazione == null)
		return null;
	return pdgVariazione.getPg_variazione_pdg();
}
public Var_stanz_resBulk getVar_stanz_res() {
	return var_stanz_res;
}

public void setVar_stanz_res(Var_stanz_resBulk var_stanz_res) {
	this.var_stanz_res = var_stanz_res;
}
public void setEsercizio_var_stanz_res(Integer esercizio_var_stanz_res) {	
	getVar_stanz_res().setEsercizio(esercizio_var_stanz_res);
}
public Integer getEsercizio_var_stanz_res() {
	if (getVar_stanz_res() == null)
		return null;	
	return getVar_stanz_res().getEsercizio();
}
public void setPg_var_stanz_res(Long pg_var_stanz_res) {
	getVar_stanz_res().setPg_variazione(pg_var_stanz_res);
}
public Long getPg_var_stanz_res() {
	if (getVar_stanz_res() == null)
		return null;	
	return getVar_stanz_res().getPg_variazione();
}
public Esercizio_baseBulk getEsercizio_res() {
	return esercizio_res;
}

public void setEsercizio_res(Esercizio_baseBulk esercizio_res) {
	this.esercizio_res = esercizio_res;
}
public Integer getEsercizio_importi() {
	return getEsercizio_res().getEsercizio();
}
public void setEsercizio_importi(Integer esercizio_importi) {
	getEsercizio_res().setEsercizio(esercizio_importi);
}
public java.util.Collection getEsercizi_res() {
	return esercizi_res;
}

public void setEsercizi_res(java.util.Collection esercizi_res) {
	this.esercizi_res = esercizi_res;
}
protected String getCompetenzaResiduo(){
  return null;	
}
public boolean isCancellatoLogicamente() {
	return getStato().equalsIgnoreCase(ANNULLATA);
}
public void cancellaLogicamente() {
	setStato(ANNULLATA);
}
public MandatoIBulk getMandato() {
	return mandato;
}
public void setMandato(MandatoIBulk mandato) {
	this.mandato = mandato;
}
public String getCd_cds_mandato() {
	if (getMandato() == null)
		return null;	
	return getMandato().getCd_cds();
}
public void setCd_cds_mandato(String cd_cds_mandato) {
	getMandato().setCd_cds(cd_cds_mandato);
}
public Integer getEsercizio_mandato() {
	if (getMandato() == null)
		return null;	
	return getMandato().getEsercizio();
}
public void setEsercizio_mandato(Integer esercizio_mandato) {
	getMandato().setEsercizio(esercizio_mandato);
}
public Long getPg_mandato() {
	if (getMandato() == null)
		return null;	
	return getMandato().getPg_mandato();
}
public void setPg_mandato(Long pg_mandato) {
	getMandato().setPg_mandato(pg_mandato);
}
public boolean isVariazioneRegolarizzazione() {
   return this.getTi_variazione()!=null &&
          this.getTi_variazione().equals(VAR_REGOLARIZZAZIONE);		
}
}
