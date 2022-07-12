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

package it.cnr.contab.docamm00.docs.bulk;

import java.util.Calendar;
import java.util.Dictionary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
@JsonInclude(value=Include.NON_NULL)
public abstract class Fattura_attiva_rigaBulk extends Fattura_attiva_rigaBase implements IDocumentoAmministrativoRigaBulk, Voidable {

	private TariffarioBulk tariffario;
	private Voce_ivaBulk voce_iva;

	// TI_PROMISCUO CHAR(1) NOT NULL
	protected java.lang.String ti_promiscuo;

	public final static String STATO_INIZIALE = "I";
	public final static String STATO_CONTABILIZZATO = "C";
	public final static String STATO_LIQUIDATO = "L";
	public final static String STATO_PAGATO = "P";
	public final static String STATO_ANNULLATO = "A";

	public final static Dictionary STATO;
	public final static Dictionary STATO_MANDATO;
	public final static Dictionary STATI_RIPORTO;

	protected java.math.BigDecimal percentuale;
	@JsonIgnore
	private Fattura_attivaBulk fattura_attiva;
	private java.math.BigDecimal im_totale_inventario = null;
	public final static String NON_ASSOCIATO_A_MANDATO = "N";
	public final static String ASSOCIATO_A_MANDATO = "T";
	private Bene_servizioBulk bene_servizio;
	private Boolean collegatoCapitoloPerTrovato = false;
	static{

		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE,"Iniziale");
		STATO.put(STATO_CONTABILIZZATO,"Contabilizzato");
		STATO.put(STATO_LIQUIDATO,"Liquidato");
		STATO.put(STATO_PAGATO,"Pagato");
		STATO.put(STATO_ANNULLATO,"Annullato");

		STATO_MANDATO = new it.cnr.jada.util.OrderedHashtable();
		STATO_MANDATO.put(NON_ASSOCIATO_A_MANDATO,"Man/rev non associato");
		STATO_MANDATO.put(ASSOCIATO_A_MANDATO,"Man/rev associato");

		STATI_RIPORTO = new it.cnr.jada.util.OrderedHashtable();
		STATI_RIPORTO.put(NON_RIPORTATO,"Non riportata");
		STATI_RIPORTO.put(RIPORTATO,"Riportata");
	}

	private it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario;
	private boolean inventariato = false;
	private java.lang.String riportata = NON_RIPORTATO;
	private TrovatoBulk trovato = new TrovatoBulk(); // inizializzazione necessaria per i bulk non persistenti

	public Fattura_attiva_rigaBulk() {
		super();
	}
	public Fattura_attiva_rigaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_attiva,java.lang.Long progressivo_riga) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_attiva,progressivo_riga);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public boolean isRObeneservizio() {

		return getBene_servizio() == null ||
				getBene_servizio().getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public boolean isROBeneServizioSearchTool() {

		return 	!STATO_INIZIALE.equals(getStato_cofi());
	}

	public void calcolaCampiDiRiga() {

		if (getQuantita() == null) setQuantita(new java.math.BigDecimal(1));
		if (getPrezzo_unitario() == null) setPrezzo_unitario(new java.math.BigDecimal(0));
		if (getIm_iva() == null) setIm_iva(new java.math.BigDecimal(0));

		setIm_totale_divisa(getQuantita().multiply(getPrezzo_unitario()).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
		java.math.BigDecimal imp_divisa = new java.math.BigDecimal(0);
		java.math.BigDecimal change = getFattura_attiva().getCambio();
		imp_divisa = (getFattura_attiva().getChangeOperation() == Fattura_attivaBulk.MOLTIPLICA) ?
				getIm_totale_divisa().multiply(change) :
					getIm_totale_divisa().divide(change, java.math.BigDecimal.ROUND_HALF_UP);
				setIm_imponibile(imp_divisa.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
				if (getFl_iva_forzata() == null) setFl_iva_forzata(Boolean.FALSE);
				if (!getFl_iva_forzata().booleanValue()) {
					if (voce_iva != null && voce_iva.getPercentuale() != null)
						setIm_iva(imp_divisa.multiply(voce_iva.getPercentuale()).divide(new java.math.BigDecimal(100), 2, java.math.BigDecimal.ROUND_HALF_UP));
					else
						setIm_iva(new java.math.BigDecimal(0));
				}
				setIm_totale_divisa(getIm_totale_divisa().add(getIm_iva()));
	}

	public void calcolaTotaliDiRigaForzaIva() {

		calcolaCampiDiRiga();
		//if (getPrezzo_unitario() != null){
		//setIm_imponibile(
		//getQuantita().multiply(getPrezzo_unitario()).setScale(
		//2,
		//java.math.BigDecimal.ROUND_HALF_UP));
		//setIm_totale_divisa(
		//(getIm_imponibile().add(getIm_iva())).setScale(
		//2,
		//java.math.BigDecimal.ROUND_HALF_UP));
		//}
	}
	public boolean checkIfRiportata() {

		return	!isPagata() && 
				(getAccertamento_scadenzario() != null && 
				!getAccertamento_scadenzario().getEsercizio().equals(getEsercizio()));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 12:28:59 PM)
	 * @return it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk
	 */
	public it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk getAccertamento_scadenzario() {
		return accertamento_scadenzario;
	}
	public IDocumentoAmministrativoRigaBulk getAssociatedDetail() {

		return null;
	}
	public java.lang.String getCd_cds() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fattura_attiva = this.getFattura_attiva();
		if (fattura_attiva == null)
			return null;
		return fattura_attiva.getCd_cds();
	}
	public java.lang.String getCd_unita_organizzativa() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fattura_attiva = this.getFattura_attiva();
		if (fattura_attiva == null)
			return null;
		return fattura_attiva.getCd_unita_organizzativa();
	}
	public java.lang.String getCd_voce_iva() {
		it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk voce_iva = this.getVoce_iva();
		if (voce_iva == null)
			return null;

		return voce_iva.getCd_voce_iva();
	}
	public java.lang.Integer getEsercizio() {
		it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fattura_attiva = this.getFattura_attiva();
		if (fattura_attiva == null)
			return null;
		return fattura_attiva.getEsercizio();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/14/2001 2:40:23 PM)
	 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
	 */
	@JsonIgnore
	public IDocumentoAmministrativoBulk getFather() {
		return getFattura_attiva();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/10/2001 13.08.29)
	 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk
	 */
	public abstract Fattura_attivaBulk getFattura_attiva();
	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2001 12.05.32)
	 * @return java.math.BigDecimal
	 */
	public java.math.BigDecimal getPercentuale() {

		return percentuale;

	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/05/2003 16.17.42)
	 * @return java.lang.String
	 */
	public java.lang.String getRiportata() {
		return riportata;
	}
	public IScadenzaDocumentoContabileBulk getScadenzaDocumentoContabile() {

		return (IScadenzaDocumentoContabileBulk)getAccertamento_scadenzario();
	}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public Dictionary getStato_cofiKeys() {
		return STATO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/7/2001 3:39:39 PM)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk
	 */
	public TariffarioBulk getTariffario() {
		return tariffario;
	}
	/* 
	 * Getter dell'attributo ti_associato_manrev
	 */
	public Dictionary getTi_associato_manrevKeys() {
		return STATO_MANDATO;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public java.lang.String getTi_promiscuo() {
		return ti_promiscuo;

	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/2001 5:51:50 PM)
	 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
	 */
	public it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk getVoce_iva() {
		return voce_iva;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 * @return boolean
	 */
	public boolean isAnnullato() {
		return STATO_ANNULLATO.equalsIgnoreCase(getStato_cofi());
	}
	public boolean isDirectlyLinkedToDC() {

		return false;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/22/2002 4:59:07 PM)
	 * @return boolean
	 */
	public boolean isInventariato() {
		return inventariato;
	}
	public boolean isPagata() {

		return STATO_PAGATO.equals(getStato_cofi());
	}
	public boolean isRiportata() {

		return getRiportata().equals(RIPORTATO);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public boolean isROdetail() {

		return getVoce_iva() == null;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public boolean isROtariffario() {

		return getTariffario() == null ||
				getTariffario().getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @return java.lang.String
	 */
	public boolean isROvoce_iva() {

		return getVoce_iva() == null ||
				getVoce_iva().getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 */
	public boolean isVoidable() {
		return isRiportata() ||
				(STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
						ASSOCIATO_A_MANDATO.equals(getTi_associato_manrev())) ||
						(Fattura_attivaBulk.STATO_CONTABILIZZATO.equals(getStato_cofi()) &&
								Fattura_attivaBulk.PARZIALMENTE_ASSOCIATO_A_MANDATO.equals(getFattura_attiva().getTi_associato_manrev())) ||
								(!Fattura_attivaBulk.NON_REGISTRATO_IN_COGE.equalsIgnoreCase(getFattura_attiva().getStato_coge()) &&
										!Fattura_attivaBulk.NON_PROCESSARE_IN_COGE.equalsIgnoreCase(getFattura_attiva().getStato_coge())) ||
										(!Fattura_attivaBulk.NON_CONTABILIZZATO_IN_COAN.equalsIgnoreCase(getFattura_attiva().getStato_coan()) &&
												!Fattura_attivaBulk.NON_PROCESSARE_IN_COAN.equalsIgnoreCase(getFattura_attiva().getStato_coan()));
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (12/19/2001 12:28:59 PM)
	 * @param newAccertamento_scadenzario it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk
	 */
	public void setAccertamento_scadenzario(it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk newAccertamento_scadenzario) {
		accertamento_scadenzario = newAccertamento_scadenzario;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (13/05/2002 11.33.25)
	 * @return boolean
	 */
	public void setAnnullato(java.sql.Timestamp date) {

		setStato_cofi(STATO_ANNULLATO);
		setDt_cancellazione(date);
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.getFattura_attiva().setCd_cds(cd_cds);
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.getFattura_attiva().setCd_unita_organizzativa(cd_unita_organizzativa);
	}
	public void setCd_voce_iva(java.lang.String cd_voce_iva) {
		this.getVoce_iva().setCd_voce_iva(cd_voce_iva);
	}
	public void setEsercizio(java.lang.Integer esercizio) {
		this.getFattura_attiva().setEsercizio(esercizio);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/10/2001 13.08.29)
	 * @param newFattura_attiva it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk
	 */
	public void setFattura_attiva(Fattura_attivaBulk newFattura_attiva) {
		fattura_attiva = newFattura_attiva;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (1/22/2002 4:59:07 PM)
	 * @param newInventariato boolean
	 */
	public void setInventariato(boolean newInventariato) {
		inventariato = newInventariato;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2001 12.05.32)
	 * @param newPercentuale java.math.BigDecimal
	 */
	public void setPercentuale(java.math.BigDecimal newPercentuale) {	
		percentuale = newPercentuale;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (30/05/2003 16.17.42)
	 * @param newRiportata java.lang.String
	 */
	public void setRiportata(java.lang.String newRiportata) {
		riportata = newRiportata;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/7/2001 3:39:39 PM)
	 * @param newTariffario it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk
	 */
	public void setTariffario(TariffarioBulk newTariffario) {
		tariffario = newTariffario;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/13/2001 10:33:00 AM)
	 * @param newTi_promiscuo java.lang.String
	 */
	public void setTi_promiscuo(java.lang.String newTi_promiscuo) {
		ti_promiscuo = newTi_promiscuo;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/2001 5:51:50 PM)
	 * @param newVoce_iva it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
	 */
	public void setVoce_iva(it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk newVoce_iva) {
		voce_iva = newVoce_iva;
	}
	public void validaDateCompetenza()
			throws ValidationException {


		String dsRiga = (getDs_riga_fattura() != null) ?
				"per il dettaglio \"" + getDs_riga_fattura() + "\"":
					"per il/i dettagli selezionato/i";
		if (getDt_da_competenza_coge() == null) 
			throw new ValidationException("Inserire la data di \"competenza da\" " + dsRiga + ".");
		if (getDt_a_competenza_coge() == null)
			throw new ValidationException("Inserire la data di \"competenza a\" " + dsRiga + ".");

		Calendar competenzaDa = getFattura_attiva().getDateCalendar(getDt_da_competenza_coge());
		Calendar competenzaA = getFattura_attiva().getDateCalendar(getDt_a_competenza_coge());
		Calendar competenzaDaTestata = getFattura_attiva().getDateCalendar(getFattura_attiva().getDt_da_competenza_coge());
		Calendar competenzaATestata = getFattura_attiva().getDateCalendar(getFattura_attiva().getDt_a_competenza_coge());

		if (competenzaA.before(competenzaDa))
			throw new ValidationException("Inserire correttamente le date di competenza " + dsRiga + ".");

		if (competenzaDa.before(competenzaDaTestata))
			throw new ValidationException("La data di \"competenza Da\" deve essere successiva o uguale alla data di \"competenza da\" della testata " + dsRiga + ".");
		if (competenzaA.after(competenzaATestata))
			throw new ValidationException("La data di \"competenza A\" deve essere inferiore o uguale alla data di \"competenza a\" della testata " + dsRiga + ".");
	}
	public java.math.BigDecimal getIm_totale_inventario() {
		return getIm_imponibile();
	}
	public void setIm_totale_inventario(java.math.BigDecimal im_totale_inventario) {
		this.im_totale_inventario = im_totale_inventario;
	}
	public Bene_servizioBulk getBene_servizio() {
		return bene_servizio;
	}
	public void setBene_servizio(Bene_servizioBulk bene_servizio) {
		this.bene_servizio = bene_servizio;
	}
	public void setTrovato(TrovatoBulk trovato) {
		this.trovato = trovato;
	}
	public TrovatoBulk getTrovato() {
		return trovato;
	}
	public java.lang.Long getPg_trovato() {
		if (this.getTrovato() == null)
			return null;
		return this.getTrovato().getPg_trovato();
	}
	public void setPg_trovato(java.lang.Long pg_trovato) {
		if (this.getTrovato() != null)
			this.getTrovato().setPg_trovato(pg_trovato);
	}
	public Boolean getCollegatoCapitoloPerTrovato() {
		return collegatoCapitoloPerTrovato;
	}
	public void setCollegatoCapitoloPerTrovato(Boolean collegatoCapitoloPerTrovato) {
		this.collegatoCapitoloPerTrovato = collegatoCapitoloPerTrovato;
	}

	@Override
	public Integer getCd_terzo() {
		return this.getFattura_attiva().getCd_terzo();
	}
}