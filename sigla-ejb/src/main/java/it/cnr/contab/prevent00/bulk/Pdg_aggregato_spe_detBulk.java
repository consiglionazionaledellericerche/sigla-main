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

package it.cnr.contab.prevent00.bulk;

import it.cnr.contab.prevent00.bp.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Adatta e implementa: {@link Pdg_aggregato_spe_detBase } e  {@link Pdg_aggregato_det } 
 * 		perchè si ottengano e si settino gli oggetti complessi.
 * 
 * @author: Bisquadro Vincenzo
 */
 
public class Pdg_aggregato_spe_detBulk extends Pdg_aggregato_spe_detBase implements Pdg_aggregato_det{

	private CdrBulk cdr;
	private Elemento_voceBulk elemento_voce;
	private FunzioneBulk funzione;
	private NaturaBulk natura;
	private java.util.Collection nature;
	private java.util.Collection funzioni;

	private it.cnr.contab.config00.sto.bulk.CdsBulk cds;
	private java.util.Collection cdss;
	private java.util.Collection elementi_voci;
/**
 * Costruttore standard di Pdg_aggregato_spe_detBulk
 */
public Pdg_aggregato_spe_detBulk() {
	super();
}
/**
 * Costruttore di Pdg_aggregato_spe_detBulk cui vengono passati in ingresso i
 *		parametri: cd_cds,cd_centro_responsabilita,cd_elemento_voce,cd_funzione,
 *		cd_natura,esercizio,ti_aggregato,ti_appartenenza,ti_gestione
 *
 * @param cd_cds java.lang.String
 * @param cd_centro_responsabilita java.lang.String
 * @param cd_elemento_voce java.lang.String
 * @param cd_funzione java.lang.String
 * @param cd_natura java.lang.String
 * @param esercizio java.lang.Integer
 * @param ti_aggregato java.lang.String
 * @param ti_appartenenza java.lang.String
 * @param ti_gestione java.lang.String
 */
public Pdg_aggregato_spe_detBulk(java.lang.String cd_cds,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_funzione,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super();
	setCds(new it.cnr.contab.config00.sto.bulk.CdsBulk(cd_cds));
	setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setNatura(new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
	setFunzione(new it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk(cd_funzione));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
	setEsercizio(esercizio);
	setTi_aggregato(ti_aggregato);
}
/**
 * Copia gli importi iniziali delle Spese dal relativo Bulk.
 *
 * @param spe_iniziale Pdg_aggregato_spe_detBulk 
 */
public void copiaImportiDa(Pdg_aggregato_spe_detBulk spe_iniziale) {
	setIm_rh_ccs_costi(spe_iniziale.getIm_rh_ccs_costi());
	setIm_ri_ccs_spese_odc(spe_iniziale.getIm_ri_ccs_spese_odc());
	setIm_rj_ccs_spese_odc_altra_uo(spe_iniziale.getIm_rj_ccs_spese_odc_altra_uo());
	setIm_rk_ccs_spese_ogc(spe_iniziale.getIm_rk_ccs_spese_ogc());
	setIm_rl_ccs_spese_ogc_altra_uo(spe_iniziale.getIm_rl_ccs_spese_ogc_altra_uo());
	setIm_rm_css_ammortamenti(spe_iniziale.getIm_rm_css_ammortamenti());
	setIm_rn_css_rimanenze(spe_iniziale.getIm_rn_css_rimanenze());
	setIm_ro_css_altri_costi(spe_iniziale.getIm_ro_css_altri_costi());
	setIm_rp_css_verso_altro_cdr(spe_iniziale.getIm_rp_css_verso_altro_cdr());
	setIm_rq_ssc_costi_odc(spe_iniziale.getIm_rq_ssc_costi_odc());
	setIm_rr_ssc_costi_odc_altra_uo(spe_iniziale.getIm_rr_ssc_costi_odc_altra_uo());
	setIm_rs_ssc_costi_ogc(spe_iniziale.getIm_rs_ssc_costi_ogc());
	setIm_rt_ssc_costi_ogc_altra_uo(spe_iniziale.getIm_rt_ssc_costi_ogc_altra_uo());
	setIm_ru_spese_costi_altrui(spe_iniziale.getIm_ru_spese_costi_altrui());
	setIm_rv_pagamenti(spe_iniziale.getIm_rv_pagamenti());
	setIm_raa_a2_costi_finali(spe_iniziale.getIm_raa_a2_costi_finali());
	setIm_rab_a2_costi_altro_cdr(spe_iniziale.getIm_rab_a2_costi_altro_cdr());
	setIm_rac_a2_spese_odc(spe_iniziale.getIm_rac_a2_spese_odc());
	setIm_rad_a2_spese_odc_altra_uo(spe_iniziale.getIm_rad_a2_spese_odc_altra_uo());
	setIm_rae_a2_spese_ogc(spe_iniziale.getIm_rae_a2_spese_ogc());
	setIm_raf_a2_spese_ogc_altra_uo(spe_iniziale.getIm_raf_a2_spese_ogc_altra_uo());
	setIm_rag_a2_spese_costi_altrui(spe_iniziale.getIm_rag_a2_spese_costi_altrui());
	setIm_rah_a3_costi_finali(spe_iniziale.getIm_rah_a3_costi_finali());
	setIm_rai_a3_costi_altro_cdr(spe_iniziale.getIm_rai_a3_costi_altro_cdr());
	setIm_ral_a3_spese_odc(spe_iniziale.getIm_ral_a3_spese_odc());
	setIm_ram_a3_spese_odc_altra_uo(spe_iniziale.getIm_ram_a3_spese_odc_altra_uo());
	setIm_ran_a3_spese_ogc(spe_iniziale.getIm_ran_a3_spese_ogc());
	setIm_rao_a3_spese_ogc_altra_uo(spe_iniziale.getIm_rao_a3_spese_ogc_altra_uo());
	setIm_rap_a3_spese_costi_altrui(spe_iniziale.getIm_rap_a3_spese_costi_altrui());
}
/**
 * Restituisce il cds per le Spese.
 * 
 * @return java.lang.String Cd_unita_organizzativa
 */
public java.lang.String getCd_cds() {
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = this.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
/**
 * Restituisce il centro di responsabilità per le Spese.
 * 
 * @return java.lang.String Cd_centro_responsabilita
 */
 public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
	if (cdr == null)
		return null;
	return cdr.getCd_centro_responsabilita();
}
/**
 * Restituisce l'elemento voce per le Spese.
 * 
 * @return java.lang.String Cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
/**
 * Restituisce la funzione per le Spese.
 * 
 * @return java.lang.String Cd_funzione
 */
public java.lang.String getCd_funzione() {
	it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione = this.getFunzione();
	if (funzione == null)
		return null;
	return funzione.getCd_funzione();
}
/**
 * Restituisce la natura per le Spese.
 * 
 * @return java.lang.String Cd_natura
 */
public java.lang.String getCd_natura() {
	it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura = this.getNatura();
	if (natura == null)
		return null;
	return natura.getCd_natura();
}
/**
 * Restituisce il Bulk del cdr per le Spese.
 * 
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk cdr
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * Restituisce il Bulk del cds per le Spese.
 * 
 * @return it.cnr.contab.config00.sto.bulk.CdsBulk cds
 */
public it.cnr.contab.config00.sto.bulk.CdsBulk getCds() {
	return cds;
}
/**
 * Restituisce la collezione dei cds per le Spese.
 * 
 * @return java.util.Collection cdss
 */
public java.util.Collection getCdss() {
	return cdss;
}
/**
 * Restituisce la collezione degli elementi_voci per le Spese.
 * 
 * @return java.util.Collection elementi_voci
 */
public java.util.Collection getElementi_voci() {
	return elementi_voci;
}
/**
 * Restituisce il Bulk dell'elemento voce per le Spese.
 * 
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * Restituisce il Bulk della funzione per le Spese.
 * 
 * @return it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk funzione
 */
public it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk getFunzione() {
	return funzione;
}
/**
 * Restituisce la collezione deglle funzioni per le Spese.
 * 
 * @return java.util.Collection funzioni
 */
public java.util.Collection getFunzioni() {
	return funzioni;
}
/**
 * Restituisce il Bulk della natura per le Spese.
 * 
 * @return it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * Restituisce la collezione dei Bulk della natura per le Spese.
 * 
 * @return java.util.Collection nature
 */
public java.util.Collection getNature() {
	return nature;
}
/**
 * Restituisce l'area di appartenenza per le Spese.
 * 
 * @return java.lang.String Ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
/**
 * Restituisce il tipo di gestione per le Spese.
 * 
 * @return java.lang.String Ti_gestione
 */
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
/**
 * Init del bp e del context.
 *
 * @param bp it.cnr.jada.util.action.CRUDBP
 *
 * @param context it.cnr.jada.action.ActionContext
 * 
 * @return OggettoBulk Pdg_aggregato_etr_detBulk
 */
protected OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	it.cnr.contab.utenze00.bp.CNRUserContext uc = (it.cnr.contab.utenze00.bp.CNRUserContext)context.getUserContext();
	setEsercizio(uc.getEsercizio());
	setCdr(((CRUDSpeDetPdGAggregatoBP)bp).getCdr());
	// (08/11/2002 13:10:54) CNRADM
	// Tolti i seguenti set perchè non sono legati al searchtool ma a combo box
	// e provocavano un setDirty()
	//setElemento_voce(new Elemento_voceBulk());
    //setFunzione(new FunzioneBulk());
    //setNatura(new NaturaBulk());
    //setCds(new CdsBulk());
    return this;
}
/**
 * Imposta il cds.
 *
 * @param cd_cds java.lang.String 
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.getCds().setCd_unita_organizzativa(cd_cds);
}
/**
 * Imposta il centro di responsabilità.
 *
 * @param cd_centro_responsabilita java.lang.String 
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
}
/**
 * Imposta l'elemento voce.
 *
 * @param cd_elemento_voce java.lang.String 
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
/**
 * Imposta la funzione.
 *
 * @param cd_funzione java.lang.String 
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.getFunzione().setCd_funzione(cd_funzione);
}
/**
 * Imposta la natura.
 *
 * @param cd_natura java.lang.String 
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.getNatura().setCd_natura(cd_natura);
}
/**
 * Imposta il centro di responsabilità.
 *
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
/**
 * Imposta il cds.
 *
 * @param newCds it.cnr.contab.config00.sto.bulk.CdsBulk
 */
public void setCds(it.cnr.contab.config00.sto.bulk.CdsBulk newCds) {
	cds = newCds;
}
/**
 * Imposta la collezione {@link java.util.Collection } dei cdss.
 *
 * @param newCdss java.util.Collection
 */
public void setCdss(java.util.Collection newCdss) {
	cdss = newCdss;
}
/**
 * Imposta la collezione {@link java.util.Collection } degli elementi voci.
 *
 * @param newElementi_voci java.util.Collection
 */
public void setElementi_voci(java.util.Collection newElementi_voci) {
	elementi_voci = newElementi_voci;
}
/**
 * Imposta il Bulk {@link it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk } dell'elemento voce.
 *
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk 
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
/**
 * Imposta il Bulk {@link it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk } della funzione.
 *
 * @param newFunzione it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk 
 */
public void setFunzione(it.cnr.contab.config00.pdcfin.bulk.FunzioneBulk newFunzione) {
	funzione = newFunzione;
}
/**
 * Imposta la collezione {@link java.util.Collection } delle funzioni.
 *
 * @param newFunzioni java.util.Collection
 */
public void setFunzioni(java.util.Collection newFunzioni) {
	funzioni = newFunzioni;
}
/**
 * Imposta il Bulk {@link it.cnr.contab.config00.pdcfin.bulk.NaturaBulk } della natura.
 *
 * @param newNatura it.cnr.contab.config00.pdcfin.bulk.NaturaBulk 
 */
public void setNatura(it.cnr.contab.config00.pdcfin.bulk.NaturaBulk newNatura) {
	natura = newNatura;
}
/**
 * Imposta la collezione {@link java.util.Collection } delle nature.
 *
 * @param newNature java.util.Collection
 */
public void setNature(java.util.Collection newNature) {
	nature = newNature;
}
/**
 * Imposta l'appartenenza delle spese.
 *
 * @param ti_appartenenza java.lang.String
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
/**
 * Imposta la gestione delle entrate.
 *
 * @param ti_gestione java.lang.String
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
}
