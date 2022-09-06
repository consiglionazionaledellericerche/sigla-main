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

import java.util.*;
import it.cnr.contab.config00.pdcep.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;

/**
 * Classe che eredita le caratteristiche della classe <code>Voce_epBulk</code>,
 * che contiene le variabili e i metodi comuni a tutte le sue sottoclassi.
 * In particolare si riferisce ad un'entità di tipo Conto.
 */
public class ContoBulk extends Voce_epBulk {
	private CapocontoBulk voce_ep_padre = new CapocontoBulk();
	private ContoBulk riapre_a_conto;
	static private java.util.Hashtable associazioni_natura_gruppo;
	private boolean fl_gruppoNaturaNonCongruiConfermati = false;

	private ContoBulk contoContropartita;

	/**
 * Costruttore della classe <code>ContoBulk</code>.
 */
public ContoBulk() 
{
	setTi_voce_ep(Voce_epHome.TIPO_CONTO); 
}
public ContoBulk(java.lang.String cd_voce_ep,java.lang.Integer esercizio) {
	super(cd_voce_ep,esercizio);
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>associazioni_natura_gruppo</code>
 * di tipo <code>Hashtable</code>.
 * In particolare, questo metodo carica in una Hashtable l'elenco dei possibili valori
 * che può assumere l'associazione delle nature con i gruppi.
 * @return java.util.Hashtable associazioni_natura_gruppo I valori dell'associazione delle
 * 														  nature con i gruppi.
 */
public java.util.Hashtable getAssociazioni_natura_gruppo() {
		Hashtable associazioni_natura_gruppo = new java.util.Hashtable();
		
		associazioni_natura_gruppo.put("NUA", "A");
		associazioni_natura_gruppo.put("EPC", "C");
		associazioni_natura_gruppo.put("EEC", "C");
		associazioni_natura_gruppo.put("CDC", "N");
		associazioni_natura_gruppo.put("CDO", "O");
		associazioni_natura_gruppo.put("NUP", "P");
		associazioni_natura_gruppo.put("EPR", "R");
		associazioni_natura_gruppo.put("EER", "R");
		
	return associazioni_natura_gruppo;
}
public java.lang.String getCd_voce_ep_padre() {
	it.cnr.contab.config00.pdcep.bulk.CapocontoBulk voce_ep_padre = this.getVoce_ep_padre();
	if (voce_ep_padre == null)
		return null;
	return voce_ep_padre.getCd_voce_ep();
}
/**
 * Metodo con cui si ottiene la descrizione del gruppo relativo al capoconto
 * selezionato.
 * @return String La descrizione del gruppo.
 */
public String getDs_gruppo()
{
//	if ( cd_voce_ep_padre != null && getGruppiKeys() != null )
//		return (String) getGruppiKeys().get( cd_voce_ep_padre.substring( 0, 1) );

	if ( voce_ep_padre != null  && voce_ep_padre.getCd_voce_ep() != null && !voce_ep_padre.getCd_voce_ep().equals(""))
		return voce_ep_padre.getCd_voce_ep().substring( 0, 1);
	return null;	
		
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>riapre_a_conto</code>
 * di tipo <code>ContoBulk</code>.
 * @return it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public ContoBulk getRiapre_a_conto() {
	return riapre_a_conto;
}
public java.lang.String getRiapre_a_conto_economico() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk riapre_a_conto = this.getRiapre_a_conto();
	if (riapre_a_conto == null)
		return null;
	return riapre_a_conto.getCd_voce_ep();
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>voce_ep_padre</code>
 * di tipo <code>CapocontoBulk</code>.
 * @return it.cnr.contab.config00.pdcep.bulk.CapocontoBulk
 */
public CapocontoBulk getVoce_ep_padre() {
	return voce_ep_padre;
}
/**
 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
 * @param bp  Business Process <code>CRUDBP</code> in uso.
 * @param context  <code>ActionContext</code> in uso.
 * @return OggettoBulk this Oggetto bulk in uso.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	riapre_a_conto = new ContoBulk();
	contoContropartita = new ContoBulk();
	setFl_a_pareggio(new Boolean (false));
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
 * Restituisce il valore della proprietà 'fl_gruppoNaturaNonCongruiConfermati'
 *
 * @return Il valore della proprietà 'fl_gruppoNaturaNonCongruiConfermati'
 */
public boolean isFl_gruppoNaturaNonCongruiConfermati() {
	return fl_gruppoNaturaNonCongruiConfermati;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo <code>riapre_a_conto</code>.
 * @return boolean true quando il campo deve essere disabilitato.
 */
public boolean isRORiapre_a_conto() {
	return riapre_a_conto == null || riapre_a_conto.getCrudStatus() == NORMAL;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo <code>voce_ep_padre</code>.
 * @return boolean true quando il campo deve essere disabilitato.
 */
public boolean isROVoce_ep_padre() {
	return voce_ep_padre == null || voce_ep_padre.getCrudStatus() == NORMAL;
}
public void setCd_voce_ep_padre(java.lang.String cd_voce_ep_padre) {
	this.getVoce_ep_padre().setCd_voce_ep(cd_voce_ep_padre);
}
/**
 * Imposta il valore della proprietà 'fl_gruppoNaturaNonCongruiConfermati'
 *
 * @param newFl_gruppoNaturaNonCongruiConfermati	Il valore da assegnare a 'fl_gruppoNaturaNonCongruiConfermati'
 */
public void setFl_gruppoNaturaNonCongruiConfermati(boolean newFl_gruppoNaturaNonCongruiConfermati) {
	fl_gruppoNaturaNonCongruiConfermati = newFl_gruppoNaturaNonCongruiConfermati;
}
/**
 * Metodo con cui si definisce il valore della variabile <code>riapre_a_conto</code>
 * di tipo <code>ContoBulk</code>.
 * @param newRiapre_a_conto it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public void setRiapre_a_conto(ContoBulk newRiapre_a_conto) {
	riapre_a_conto = newRiapre_a_conto;
}
public void setRiapre_a_conto_economico(java.lang.String riapre_a_conto_economico) {
	this.getRiapre_a_conto().setCd_voce_ep(riapre_a_conto_economico);
}
/**
 * Metodo con cui si definisce il valore della variabile <code>voce_ep_padre</code>
 * di tipo <code>CapocontoBulk</code>.
 * @param newVoce_ep_padre it.cnr.contab.config00.pdcep.bulk.CapocontoBulk
 */
public void setVoce_ep_padre(CapocontoBulk newVoce_ep_padre) {
	voce_ep_padre = newVoce_ep_padre;
}

	public ContoBulk getContoContropartita() {
		return contoContropartita;
	}

	public void setContoContropartita(ContoBulk contoContropartita) {
		this.contoContropartita = contoContropartita;
	}
	public java.lang.String getCd_voce_ep_contr() {
		return Optional.ofNullable(this.getContoContropartita())
				.map(Voce_epBulk::getCd_voce_ep)
				.orElse(null);
	}

	public void setCd_voce_ep_contr(java.lang.String cd_voce_ep) {
		Optional.ofNullable(this.getContoContropartita()).ifPresent(el->el.setCd_voce_ep(cd_voce_ep));
	}
}
