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
 * Adatta e implementa: {@link Pdg_aggregato_etr_detBase } e  {@link Pdg_aggregato_det } 
 * 		perchè si ottengano e si settino gli oggetti complessi.
 * 
 * @author: Bisquadro Vincenzo
 */

public class Pdg_aggregato_etr_detBulk extends Pdg_aggregato_etr_detBase implements Pdg_aggregato_det {

	private CdrBulk cdr;
	private Elemento_voceBulk elemento_voce;
	private NaturaBulk natura;
	private java.util.Collection nature;
/**
 * Costruttore standard di Pdg_aggregato_etr_detBulk
 */
public Pdg_aggregato_etr_detBulk() {
	super();
}
/**
 * Costruttore di Pdg_aggregato_etr_detBulk cui vengono passati i seguenti parametri:
 * 		cd_centro_responsabilita,cd_elemento_voce,cd_natura,esercizio,ti_aggregato,ti_appartenenza,ti_gestione.
 *
 * @param cd_centro_responsabilita java.lang.String
 *
 * @param cd_elemento_voce java.lang.String
 *
 * @param cd_natura java.lang.String
 *
 * @param esercizio java.lang.Integer
 *
 * @param ti_aggregato java.lang.String
 *
 * @param ti_appartenenza java.lang.String
 *
 * @param ti_gestione java.lang.String
 */
public Pdg_aggregato_etr_detBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_aggregato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super();
	setCdr(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
	setNatura(new it.cnr.contab.config00.pdcfin.bulk.NaturaBulk(cd_natura));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
	setEsercizio(esercizio);
	setTi_aggregato(ti_aggregato);
}
/**
 * Copia gli importi iniziali delle Entrate dal relativo Bulk.
 *
 * @param etr_iniziale Pdg_aggregato_etr_detBulk
 */
public void copiaImportiDa(Pdg_aggregato_etr_detBulk etr_iniziale) {
	setIm_ra_rce(etr_iniziale.getIm_ra_rce());
	setIm_rb_rse(etr_iniziale.getIm_rb_rse());
	setIm_rc_esr(etr_iniziale.getIm_rc_esr());
	setIm_rd_a2_ricavi(etr_iniziale.getIm_rd_a2_ricavi());
	setIm_re_a2_entrate(etr_iniziale.getIm_re_a2_entrate());
	setIm_rf_a3_ricavi(etr_iniziale.getIm_rf_a3_ricavi());
	setIm_rg_a3_entrate(etr_iniziale.getIm_rg_a3_entrate());
}
/**
 * Restituisce il centro di responsabilità per le Entrate.
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
 * Restituisce l'elemento voce per le Entrate.
 * 
 * @return java.lang.String Cd_centro_responsabilita
 */
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
/**
 * Restituisce la natura delle Entrate.
 * 
 * @return java.lang.String natura
 */
public java.lang.String getCd_natura() {
	it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura = this.getNatura();
	if (natura == null)
		return null;
	return natura.getCd_natura();
}
/**
 * Restituisce il Bulk del cdr per le Entrate.
 * 
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk cdr
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
/**
 * Restituisce il Bulk dell'elemento voce per le Entrate.
 * 
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * Restituisce il Bulk della natura per le Entrate.
 * 
 * @return it.cnr.contab.config00.pdcfin.bulk.NaturaBulk natura
 */
public it.cnr.contab.config00.pdcfin.bulk.NaturaBulk getNatura() {
	return natura;
}
/**
 * Restituisce la collezione dei Bulk della natura per le Entrate.
 * 
 * @return java.util.Collection nature
 */
public java.util.Collection getNature() {
	return nature;
}
/**
 * Restituisce l'area di appartenenza per le Entrate.
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
 * Restituisce il tipo di gestione per le Entrate.
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
	setCdr(((CRUDEtrDetPdGAggregatoBP)bp).getCdr());
	setElemento_voce(new Elemento_voceBulk());
	// (08/11/2002 13:10:54) CNRADM
	// Tolti il seguente set perchè non è legato al searchtool ma a combo box
	// e provoca un setDirty()
    //setNatura(new NaturaBulk());
	return this;
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
 * Imposta il Bulk {@link it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk } dell'elemento voce.
 *
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk 
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
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
 * Imposta l'appartenenza delle entrate.
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
