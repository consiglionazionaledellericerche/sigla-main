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

package it.cnr.contab.doccont00.ordine.bulk;

import it.cnr.jada.bulk.*;

public class OrdineBulk extends OrdineBase {

	private BulkList dettagli = new BulkList();
	private it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione;
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoce;
	private it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk tipoConsegna = new it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk();
	private it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo;
	public final static java.lang.String STAMPATO = "S";
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk terminiPagamento;
	private java.util.Collection termini;
	private it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalitaPagamento;
	private java.util.Collection modalita;
	private it.cnr.contab.anagraf00.core.bulk.BancaBulk banca;
public OrdineBulk() {
	super();
}
public OrdineBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.Long pg_ordine) {
	super(cd_cds,esercizio,pg_ordine);
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 17.51.05)
 * @return int
 * @param dett it.cnr.contab.doccont00.ordine.bulk.Ordine_dettBulk
 */
public int addToDettagli(Ordine_dettBulk dett) {

	getDettagli().add(dett);
	
	dett.setQuantita(new java.math.BigDecimal(0));
	dett.setIm_unitario(new java.math.BigDecimal(0));
	dett.setIm_iva(new java.math.BigDecimal(0));
	dett.setOrdine(this);

	return getDettagli().size()-1;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.46.53)
 * @return it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public it.cnr.contab.anagraf00.core.bulk.BancaBulk getBanca() {
	return banca;
}
public BulkCollection[] getBulkLists() {

	return new it.cnr.jada.bulk.BulkCollection[] { this.getDettagli() };
}
public java.lang.String getCd_cds() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = obbligazione.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoce = this.getElementoVoce();
	if (elementoVoce == null)
		return null;
	return elementoVoce.getCd_elemento_voce();
}
public java.lang.String getCd_modalita_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk modalitaPagamento = this.getModalitaPagamento();
	if (modalitaPagamento == null)
		return null;
	return modalitaPagamento.getCd_modalita_pag();
}
public java.lang.String getCd_termini_pag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk terminiPagamento = this.getTerminiPagamento();
	if (terminiPagamento == null)
		return null;
	return terminiPagamento.getCd_termini_pag();
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
public java.lang.String getCd_tipo_consegna() {
	it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk tipoConsegna = this.getTipoConsegna();
	if (tipoConsegna == null)
		return null;
	return tipoConsegna.getCd_tipo_consegna();
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2002 11.26.39)
 * @return it.cnr.jada.bulk.BulkList
 */
public it.cnr.jada.bulk.BulkList getDettagli() {
	return dettagli;
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 10.40.32)
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElementoVoce() {
	return elementoVoce;
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.47)
 * @return java.util.Collection
 */
public java.util.Collection getModalita() {
	return modalita;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.32)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk getModalitaPagamento() {
	return modalitaPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2002 14.30.40)
 * @return it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk
 */
public it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk getObbligazione() {
	return obbligazione;
}
public java.lang.Long getPg_banca() {
	it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
	if (banca == null)
		return null;
	return banca.getPg_banca();
}
public Integer getEsercizio_ori_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getEsercizio_originale();
}
public java.lang.Long getPg_obbligazione() {
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione = this.getObbligazione();
	if (obbligazione == null)
		return null;
	return obbligazione.getPg_obbligazione();
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.06)
 * @return java.util.Collection
 */
public java.util.Collection getTermini() {
	return termini;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.42.16)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk getTerminiPagamento() {
	return terminiPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 11.11.16)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoce = this.getElementoVoce();
	if (elementoVoce == null)
		return null;
	return elementoVoce.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoce = this.getElementoVoce();
	if (elementoVoce == null)
		return null;
	return elementoVoce.getTi_gestione();
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 10.41.21)
 * @return it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk
 */
public it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk getTipoConsegna() {
	return tipoConsegna;
}
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForSearch(bp,context);

	this.setObbligazione(new it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk());

	return this;	
}
public boolean isAbledToInsertBank() {
	return !(getTerzo() != null && 
		getTerzo().getCrudStatus() == OggettoBulk.NORMAL &&
		getModalitaPagamento() != null);
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 17.52.43)
 * @return it.cnr.contab.doccont00.ordine.bulk.Ordine_dettBulk
 * @param index int
 */
public boolean isROCdTipoConsegna(){

	return (getTipoConsegna() == null || getTipoConsegna().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (01/02/2002 16.59.48)
 * @return boolean
 */
public boolean isStampato() {
	return getStato()==this.STAMPATO;
}
/**
 * Insert the method's description here.
 * Creation date: (31/01/2002 17.52.43)
 * @return it.cnr.contab.doccont00.ordine.bulk.Ordine_dettBulk
 * @param index int
 */
public Ordine_dettBulk removeFromDettagli(int index) {

	Ordine_dettBulk dett = (Ordine_dettBulk)getDettagli().remove(index);
	return dett;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.46.53)
 * @param newBanca it.cnr.contab.anagraf00.core.bulk.BancaBulk
 */
public void setBanca(it.cnr.contab.anagraf00.core.bulk.BancaBulk newBanca) {
	banca = newBanca;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getObbligazione().getCds().setCd_unita_organizzativa(cd_cds);
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElementoVoce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.getModalitaPagamento().setCd_modalita_pag(cd_modalita_pag);
}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.getTerminiPagamento().setCd_termini_pag(cd_termini_pag);
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
}
public void setCd_tipo_consegna(java.lang.String cd_tipo_consegna) {
	this.getTipoConsegna().setCd_tipo_consegna(cd_tipo_consegna);
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2002 11.26.39)
 * @param newDettagli it.cnr.jada.bulk.BulkList
 */
public void setDettagli(it.cnr.jada.bulk.BulkList newDettagli) {
	dettagli = newDettagli;
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 10.40.32)
 * @param newElementoVoce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElementoVoce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElementoVoce) {
	elementoVoce = newElementoVoce;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getObbligazione().setEsercizio(esercizio);
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.47)
 * @param newModalita java.util.Collection
 */
public void setModalita(java.util.Collection newModalita) {
	modalita = newModalita;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.32)
 * @param newModalitaPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk
 */
public void setModalitaPagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk newModalitaPagamento) {
	modalitaPagamento = newModalitaPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (28/01/2002 14.30.40)
 * @param newObbligazione it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk
 */
public void setObbligazione(it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk newObbligazione) {
	obbligazione = newObbligazione;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.getBanca().setPg_banca(pg_banca);
}
public void setEsercizio_ori_obbligazione(Integer esercizio_ori_obbligazione) {
	this.getObbligazione().setEsercizio_originale(esercizio_ori_obbligazione);
}
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.getObbligazione().setPg_obbligazione(pg_obbligazione);
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.40.06)
 * @param newTermini java.util.Collection
 */
public void setTermini(java.util.Collection newTermini) {
	termini = newTermini;
}
/**
 * Insert the method's description here.
 * Creation date: (18/02/2002 11.42.16)
 * @param newTerminiPagamento it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk
 */
public void setTerminiPagamento(it.cnr.contab.anagraf00.tabrif.bulk.Rif_termini_pagamentoBulk newTerminiPagamento) {
	terminiPagamento = newTerminiPagamento;
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 11.11.16)
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElementoVoce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElementoVoce().setTi_gestione(ti_gestione);
}
/**
 * Insert the method's description here.
 * Creation date: (29/01/2002 10.41.21)
 * @param newTipoConsegna it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk
 */
public void setTipoConsegna(it.cnr.contab.doccont00.tabrif.bulk.Tipo_consegnaBulk newTipoConsegna) {
	tipoConsegna = newTipoConsegna;
}
public void validate() throws ValidationException {


	// controllo su DATA REGISTRAZIONE
	if (getDt_registrazione() == null )
		throw new ValidationException( "Inserire la data di registrazione!" );

	// controllo su TIPO CONSEGNA
	if (getTipoConsegna() == null ) 
		throw new ValidationException( "Selezionare un tipo consegna!" );

	// controllo su TERMINI di PAGAMENTO
	if (getTerminiPagamento() == null ) 
		throw new ValidationException( "Selezionare un termine di pagamento!" );

	// controllo su MODALITA di PAGAMENTO
	if (getModalitaPagamento() == null )
		throw new ValidationException( "Selezionare una modalita di pagamento!" );

	// controllo RIGHE ORDINE
	if (getDettagli().size()==0)
		throw new ValidationException( "Inserire almeno un dettaglio!" );
	
}		
}
