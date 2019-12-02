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

package it.cnr.contab.config00.pdcep.bulk;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcep.bulk.Voce_epBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Questa classe eredita le caratteristiche della superclasse <code>OggettoBulk<code>.
 * Contiene tutte le variabili e i metodi che sono comuni alle sue sottoclassi.
 * In particolare, si tratta di un'associazione tra elemento voce e voce
 * economico patrimoniale.
 */
public class Ass_ev_voceepBulk extends Ass_ev_voceepBase {

	protected Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	protected ContoBulk voce_ep = new ContoBulk();
	protected ContoBulk voce_ep_contr = new ContoBulk();
public Ass_ev_voceepBulk() {
	super();
}
public Ass_ev_voceepBulk(java.lang.String cd_elemento_voce,java.lang.String cd_voce_ep,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,cd_voce_ep,esercizio,ti_appartenenza,ti_gestione);
	setVoce_ep(new it.cnr.contab.config00.pdcep.bulk.ContoBulk(cd_voce_ep,esercizio));
	setElemento_voce(new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione));
}
public java.lang.String getCd_elemento_voce() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getCd_elemento_voce();
}
public java.lang.String getCd_voce_ep() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk voce_ep = this.getVoce_ep();
	if (voce_ep == null)
		return null;
	return voce_ep.getCd_voce_ep();
}
/**
 * Metodo con cui si ottiene un'istanza di tipo <code>Elemento_voceBulk</code>.
 * @return elemento_voce L'istanza di Elemento_voceBulk.
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elemento_voce = this.getElemento_voce();
	if (elemento_voce == null)
		return null;
	return elemento_voce.getTi_gestione();
}
/**
 * Metodo con cui si ottiene un'istanza di tipo <code>ContoBulk</code>.
 * @return voce_ep L'istanza di ContoBulk.
 */
public ContoBulk getVoce_ep() {
	return voce_ep;
}
/**
 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
 * @param bp  Business Process <code>CRUDBP</code> in uso.
 * @param context  <code>ActionContext</code> in uso.
 * @return OggettoBulk this Oggetto bulk in uso.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}
/**
 * Metodo per inizializzare l'oggetto bulk in fase di ricerca.
 * @param bp  Business Process <code>CRUDBP</code> in uso.
 * @param context  <code>ActionContext</code> in uso.
 * @return OggettoBulk this Oggetto bulk in uso.
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo <code>elemento_voce</code>.
 * @return boolean true quando il campo deve essere disabilitato.
 */
public boolean isROElemento_voce() {
	return elemento_voce == null || elemento_voce.getCrudStatus() == NORMAL;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo <code>voce_ep</code>.
 * @return boolean true quando il campo deve essere disabilitato.
 */
public boolean isROVoce_ep() {
	return voce_ep == null || voce_ep.getCrudStatus() == NORMAL;
}
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.getElemento_voce().setCd_elemento_voce(cd_elemento_voce);
}
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.getVoce_ep().setCd_voce_ep(cd_voce_ep);
}
/**
 * Metodo con cui si definisce il valore della variabile <code>elemento_voce</code>
 * di tipo <code>Elemento_voceBulk</code>.
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_voce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getElemento_voce().setTi_appartenenza(ti_appartenenza);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getElemento_voce().setTi_gestione(ti_gestione);
}
/**
 * Metodo con cui si definisce il valore della variabile <code>voce_ep</code>
 * di tipo <code>ContoBulk</code>.
 * @param newVoce_ep it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public void setVoce_ep(ContoBulk newVoce_ep) {
	voce_ep = newVoce_ep;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException 
{
	if ( getEsercizio() == null  )
		throw new ValidationException( "Il campo ESERCIZIO è obbligatorio." );
	if ( getEsercizio().toString().length() != 4 )
		throw new ValidationException( "Il campo ESERCIZIO deve essere di quattro cifre. " );		
}
public ContoBulk getVoce_ep_contr() {
	return voce_ep_contr;
}
public void setVoce_ep_contr(ContoBulk voce_ep_contr) {
	this.voce_ep_contr = voce_ep_contr;
}
public java.lang.String getCd_voce_ep_contr() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk voce_ep_contr = this.getVoce_ep();
	if (voce_ep_contr == null)
		return null;
	return voce_ep_contr.getCd_voce_ep();
}
public void setCd_voce_ep_contr(java.lang.String cd_voce_ep) {
	this.getVoce_ep_contr().setCd_voce_ep(cd_voce_ep);
}
}
