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

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk;
import it.cnr.contab.doccont00.core.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (10/25/2001 11:52:43 AM)
 * @author: Roberto Peli
 */
@JsonInclude(value=Include.NON_NULL)
public class Nota_di_credito_rigaBulk extends Fattura_passiva_rigaBulk {
	@JsonIgnore
	private Nota_di_creditoBulk notaDiCredito;
	@JsonIgnore
	private Fattura_passiva_rigaIBulk riga_fattura_origine;
	private Fattura_passiva_rigaIBulk riga_fattura_associata;
	private Accertamento_scadenzarioBulk accertamento_scadenzario = null;

	public final static java.util.Dictionary STATO;
		
	static{
		STATO = new it.cnr.jada.util.OrderedHashtable();
		STATO.put(STATO_INIZIALE,"Iniziale");
		STATO.put(STATO_CONTABILIZZATO,"Stornato");
		STATO.put(STATO_ANNULLATO,"Annullato");
		STATO.put(STATO_PAGATO,"Pagato");
	}

/**
 * Nota_di_credito_rigaBulk constructor comment.
 */
public Nota_di_credito_rigaBulk() {
	super();
}
/**
 * Nota_di_credito_rigaBulk constructor comment.
 */
public Nota_di_credito_rigaBulk(Fattura_passiva_rigaIBulk dettaglio) {
	super();

	try {
		copyFrom(dettaglio);
	} catch (it.cnr.jada.bulk.FillException e) {
		
	}
}
public Nota_di_credito_rigaBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_passiva,java.lang.Long progressivo_riga) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva,progressivo_riga);
	setNotaDiCredito(new it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva));
}
public boolean checkIfRiportata() {

	return	!isPagata() && 
			((getObbligazione_scadenziario() != null && 
				!getObbligazione_scadenziario().getEsercizio().equals(getEsercizio())) || 
			 (getAccertamento_scadenzario() != null && 
				!getAccertamento_scadenzario().getEsercizio().equals(getEsercizio())));
}
public void copyFrom(Fattura_passiva_rigaIBulk dettaglio) 
	throws it.cnr.jada.bulk.FillException {

	setStato_cofi(STATO_INIZIALE);
	setTi_associato_manrev(NON_ASSOCIATO_A_MANDATO);

	setDt_da_competenza_coge(dettaglio.getDt_da_competenza_coge());
	setDt_a_competenza_coge(dettaglio.getDt_a_competenza_coge());
	setTi_istituz_commerc(dettaglio.getTi_istituz_commerc());
	setProgressivo_riga(new Long(getNotaDiCredito().getFattura_passiva_dettColl().size() + 1));

	setBene_servizio(dettaglio.getBene_servizio());
	setVoce_iva(dettaglio.getVoce_iva());

	boolean zeroNotValid = false;
	if (dettaglio.getIm_diponibile_nc().compareTo(dettaglio.getIm_imponibile().add(dettaglio.getIm_iva())) != 0) {
		setQuantita(new java.math.BigDecimal(1));
		setPrezzo_unitario(new java.math.BigDecimal(0));
		setFl_iva_forzata(Boolean.FALSE);
		zeroNotValid = true;
	} else {
		setQuantita(dettaglio.getQuantita());
		setPrezzo_unitario(dettaglio.getPrezzo_unitario());
		setFl_iva_forzata(dettaglio.getFl_iva_forzata());
		if (getFl_iva_forzata() == null)
			setFl_iva_forzata(Boolean.FALSE);
		if (getFl_iva_forzata().booleanValue())
			setIm_iva(dettaglio.getIm_iva());
	}
	setDs_riga_fattura(dettaglio.getDs_riga_fattura());
	setRiga_fattura_origine(dettaglio);

	setFornitore(dettaglio.getFornitore());
	//setTermini(dettaglio.getTermini());
	setTermini_pagamento(dettaglio.getTermini_pagamento());
	setModalita_pagamento(dettaglio.getModalita_pagamento());
	//setModalita(dettaglio.getModalita());
	setBanche(dettaglio.getBanche());
	setBanca(dettaglio.getBanca());
	setCessionario(dettaglio.getCessionario());
	
	calcolaCampiDiRiga();
	java.math.BigDecimal vecchioTotale = new java.math.BigDecimal(0).setScale(0, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal totaleDiRiga = getIm_imponibile().add(getIm_iva());
	java.math.BigDecimal nuovoImportoDisponibile = dettaglio.getIm_diponibile_nc().subtract(totaleDiRiga.subtract(vecchioTotale));
	if (nuovoImportoDisponibile.signum() < 0 || (nuovoImportoDisponibile.signum() == 0 && zeroNotValid)) {
		if (dettaglio.getBene_servizio() == null)
			throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile è di " + dettaglio.getIm_diponibile_nc() + " EUR!");
		else 
			throw new it.cnr.jada.bulk.FillException("Attenzione: l'importo di storno massimo ancora disponibile per \"" + dettaglio.getBene_servizio().getDs_bene_servizio() + "\" è di " + dettaglio.getIm_diponibile_nc() + " EUR!");
	}
	dettaglio.setIm_diponibile_nc(nuovoImportoDisponibile.setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
	
}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:01:15 PM)
 * @return it.cnr.contab.doccont00.core.bulk.AccertamentoBulk
 */
public it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk getAccertamento_scadenzario() {
	return accertamento_scadenzario;
}
public IDocumentoAmministrativoRigaBulk getAssociatedDetail() {

	return (IDocumentoAmministrativoRigaBulk)getRiga_fattura_associata();
}
public java.lang.String getCd_cds() {
	it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = this.getNotaDiCredito();
	if (notaDiCredito == null)
		return null;
	return notaDiCredito.getCd_cds();
}
public java.lang.String getCd_cds_accertamento() {
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario = this.getAccertamento_scadenzario();
	if (accertamento_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = accertamento_scadenzario.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getCd_cds();
}
public java.lang.String getCd_cds_assncna_eco() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_origine = this.getRiga_fattura_origine();
	if (riga_fattura_origine == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_origine.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getCd_cds();
}
public java.lang.String getCd_cds_assncna_fin() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
	if (riga_fattura_associata == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_associata.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getCd_cds();
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = this.getNotaDiCredito();
	if (notaDiCredito == null)
		return null;
	return notaDiCredito.getCd_unita_organizzativa();
}
public java.lang.String getCd_uo_assncna_eco() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_origine = this.getRiga_fattura_origine();
	if (riga_fattura_origine == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_origine.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getCd_unita_organizzativa();
}
public java.lang.String getCd_uo_assncna_fin() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
	if (riga_fattura_associata == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_associata.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getCd_unita_organizzativa();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = this.getNotaDiCredito();
	if (notaDiCredito == null)
		return null;
	return notaDiCredito.getEsercizio();
}
public java.lang.Integer getEsercizio_accertamento() {
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario = this.getAccertamento_scadenzario();
	if (accertamento_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = accertamento_scadenzario.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getEsercizio();
}
public java.lang.Integer getEsercizio_assncna_eco() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_origine = this.getRiga_fattura_origine();
	if (riga_fattura_origine == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_origine.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getEsercizio();
}
public java.lang.Integer getEsercizio_assncna_fin() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
	if (riga_fattura_associata == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_associata.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 12:03:34 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk
 */
public Fattura_passivaBulk getFattura_passiva() {
	
	return getNotaDiCredito();
}
public java.math.BigDecimal getImportoSignForDelete(java.math.BigDecimal importo) {

	return importo;
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 12:03:08 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk
 */
public Nota_di_creditoBulk getNotaDiCredito() {
	return notaDiCredito;
}
/**
 * Insert the method's description here.
 * Creation date: (2/13/2002 1:28:20 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk
 */
public IDocumentoAmministrativoRigaBulk getOriginalDetail() {
	return getRiga_fattura_origine();
}
public Integer getEsercizio_ori_accertamento() {
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario = this.getAccertamento_scadenzario();
	if (accertamento_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = accertamento_scadenzario.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getEsercizio_originale();
}
public java.lang.Long getPg_accertamento() {
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario = this.getAccertamento_scadenzario();
	if (accertamento_scadenzario == null)
		return null;
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = accertamento_scadenzario.getAccertamento();
	if (accertamento == null)
		return null;
	return accertamento.getPg_accertamento();
}
public java.lang.Long getPg_accertamento_scadenzario() {
	it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk accertamento_scadenzario = this.getAccertamento_scadenzario();
	if (accertamento_scadenzario == null)
		return null;
	return accertamento_scadenzario.getPg_accertamento_scadenzario();
}
public java.lang.Long getPg_fattura_assncna_eco() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_origine = this.getRiga_fattura_origine();
	if (riga_fattura_origine == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_origine.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getPg_fattura_passiva();
}
public java.lang.Long getPg_fattura_assncna_fin() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
	if (riga_fattura_associata == null)
		return null;
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk fattura_passivaI = riga_fattura_associata.getFattura_passivaI();
	if (fattura_passivaI == null)
		return null;
	return fattura_passivaI.getPg_fattura_passiva();
}
public java.lang.Long getPg_fattura_passiva() {
	it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk notaDiCredito = this.getNotaDiCredito();
	if (notaDiCredito == null)
		return null;
	return notaDiCredito.getPg_fattura_passiva();
}
public java.lang.Long getPg_riga_assncna_eco() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_origine = this.getRiga_fattura_origine();
	if (riga_fattura_origine == null)
		return null;
	return riga_fattura_origine.getProgressivo_riga();
}
public java.lang.Long getPg_riga_assncna_fin() {
	it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk riga_fattura_associata = this.getRiga_fattura_associata();
	if (riga_fattura_associata == null)
		return null;
	return riga_fattura_associata.getProgressivo_riga();
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 2:48:15 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
 */
public Fattura_passiva_rigaIBulk getRiga_fattura_associata() {
	return riga_fattura_associata;
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 2:48:15 PM)
 * @return it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
 */
public Fattura_passiva_rigaIBulk getRiga_fattura_origine() {
	return riga_fattura_origine;
}
public IScadenzaDocumentoContabileBulk getScadenzaDocumentoContabile() {

	if (getObbligazione_scadenziario() != null)
		return (IScadenzaDocumentoContabileBulk)getObbligazione_scadenziario();
	else if (getAccertamento_scadenzario() != null)
		return (IScadenzaDocumentoContabileBulk)getAccertamento_scadenzario();
	return null;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getStato_cofiKeys() {
		return STATO;
	}
public boolean isDirectlyLinkedToDC() {

	return getAccertamento_scadenzario() != null;
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di fattura.
	 *
	 * @return java.util.Dictionary
	 */

	public boolean isStornato() {
		
		return !STATO_INIZIALE.equals(getStato_cofi());
	}
/**
 * Insert the method's description here.
 * Creation date: (11/16/2001 3:01:15 PM)
 * @param newAccertamento it.cnr.contab.doccont00.core.bulk.AccertamentoBulk
 */
public void setAccertamento_scadenzario(it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk newAccertamento_scadenzario) {
	accertamento_scadenzario = newAccertamento_scadenzario;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getNotaDiCredito().setCd_cds(cd_cds);
}
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.getAccertamento_scadenzario().getAccertamento().setCd_cds(cd_cds_accertamento);
}
public void setCd_cds_assncna_eco(java.lang.String cd_cds_assncna_eco) {
	this.getRiga_fattura_origine().getFattura_passivaI().setCd_cds(cd_cds_assncna_eco);
}
public void setCd_cds_assncna_fin(java.lang.String cd_cds_assncna_fin) {
	this.getRiga_fattura_associata().getFattura_passivaI().setCd_cds(cd_cds_assncna_fin);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getNotaDiCredito().setCd_unita_organizzativa(cd_unita_organizzativa);
}
public void setCd_uo_assncna_eco(java.lang.String cd_uo_assncna_eco) {
	this.getRiga_fattura_origine().getFattura_passivaI().setCd_unita_organizzativa(cd_uo_assncna_eco);
}
public void setCd_uo_assncna_fin(java.lang.String cd_uo_assncna_fin) {
	this.getRiga_fattura_associata().getFattura_passivaI().setCd_unita_organizzativa(cd_uo_assncna_fin);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getNotaDiCredito().setEsercizio(esercizio);
}
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.getAccertamento_scadenzario().getAccertamento().setEsercizio(esercizio_accertamento);
}
public void setEsercizio_assncna_eco(java.lang.Integer esercizio_assncna_eco) {
	this.getRiga_fattura_origine().getFattura_passivaI().setEsercizio(esercizio_assncna_eco);
}
public void setEsercizio_assncna_fin(java.lang.Integer esercizio_assncna_fin) {
	this.getRiga_fattura_associata().getFattura_passivaI().setEsercizio(esercizio_assncna_fin);
}
/**
 * Insert the method's description here.
 * Creation date: (9/10/2001 5:51:50 PM)
 * @return it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk
 */
public void setFattura_passiva(Fattura_passivaBulk fattura_passiva) {

	setNotaDiCredito((Nota_di_creditoBulk)fattura_passiva);
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 12:03:08 PM)
 * @param newNotaDiCredito it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk
 */
public void setNotaDiCredito(Nota_di_creditoBulk newNotaDiCredito) {
	notaDiCredito = newNotaDiCredito;
}
public void setEsercizio_ori_accertamento(Integer esercizio_ori_accertamento) {
	this.getAccertamento_scadenzario().getAccertamento().setEsercizio_originale(esercizio_ori_accertamento);
}
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.getAccertamento_scadenzario().getAccertamento().setPg_accertamento(pg_accertamento);
}
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.getAccertamento_scadenzario().setPg_accertamento_scadenzario(pg_accertamento_scadenzario);
}
public void setPg_fattura_assncna_eco(java.lang.Long pg_fattura_assncna_eco) {
	this.getRiga_fattura_origine().getFattura_passivaI().setPg_fattura_passiva(pg_fattura_assncna_eco);
}
public void setPg_fattura_assncna_fin(java.lang.Long pg_fattura_assncna_fin) {
	this.getRiga_fattura_associata().getFattura_passivaI().setPg_fattura_passiva(pg_fattura_assncna_fin);
}
public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
	this.getNotaDiCredito().setPg_fattura_passiva(pg_fattura_passiva);
}
public void setPg_riga_assncna_eco(java.lang.Long pg_riga_assncna_eco) {
	this.getRiga_fattura_origine().setProgressivo_riga(pg_riga_assncna_eco);
}
public void setPg_riga_assncna_fin(java.lang.Long pg_riga_assncna_fin) {
	this.getRiga_fattura_associata().setProgressivo_riga(pg_riga_assncna_fin);
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 2:48:15 PM)
 * @param newRiga_fattura_associata it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
 */
public void setRiga_fattura_associata(Fattura_passiva_rigaIBulk newRiga_fattura_associata) {
	riga_fattura_associata = newRiga_fattura_associata;
}
/**
 * Insert the method's description here.
 * Creation date: (10/25/2001 2:48:15 PM)
 * @param newRiga_fattura_origine it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk
 */
public void setRiga_fattura_origine(Fattura_passiva_rigaIBulk newRiga_fattura_origine) {
	riga_fattura_origine = newRiga_fattura_origine;
}
}
