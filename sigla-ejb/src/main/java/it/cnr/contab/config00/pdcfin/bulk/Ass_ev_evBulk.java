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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;

/**
 * Questa classe eredita le caratteristiche della superclasse <code>OggettoBulk<code>.
 * Contiene tutte le variabili e i metodi che sono comuni alle sue sottoclassi.
 * In particolare, si tratta di un'associazione tra elementi voce.
 */
public class Ass_ev_evBulk extends Ass_ev_evBase {
	private it.cnr.jada.util.OrderedHashtable naturaKeys;
	protected Elemento_voceBulk elemento_voce = new Elemento_voceBulk();
	protected Elemento_voceBulk elemento_voce_coll = new Elemento_voceBulk();	

public Ass_ev_evBulk() {
	super();
}
public Ass_ev_evBulk(java.lang.String cd_cds,java.lang.String cd_elemento_voce,java.lang.String cd_elemento_voce_coll,java.lang.String cd_natura,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_appartenenza_coll,java.lang.String ti_gestione,java.lang.String ti_gestione_coll) {
	super(cd_cds,cd_elemento_voce,cd_elemento_voce_coll,cd_natura,esercizio,ti_appartenenza,ti_appartenenza_coll,ti_gestione,ti_gestione_coll);
}
/**
 * Metodo con cui si ottiene un'istanza di tipo <code>Elemento_voceBulk</code>.
 * @return elemento_voce L'istanza di Elemento_voceBulk.
 */
public Elemento_voceBulk getElemento_voce() {
	return elemento_voce;
}
/**
 * Metodo con cui si ottiene un'istanza di tipo <code>Elemento_voceBulk</code>.
 * @return elemento_voce L'istanza di Elemento_voceBulk.
 */
public Elemento_voceBulk getElemento_voce_coll() {
	return elemento_voce_coll;
}
/**
 * Metodo con cui si ottiene il valore della variabile <code>naturaKeys</code>
 * di tipo <code>OrderedHashtable</code>.
 * @return it.cnr.jada.util.OrderedHashtable naturaKeys.
 */
public it.cnr.jada.util.OrderedHashtable getNaturaKeys() {
	return naturaKeys;
}
/**
 * Metodo per inizializzare l'oggetto bulk in fase di inserimento.
 * @param bp  Business Process <code>CRUDBP</code> in uso.
 * @param context  <code>ActionContext</code> in uso.
 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio(it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context));
	return this;
}
/**
 * Metodo per inizializzare l'oggetto bulk in fase di ricerca.
 * @param bp  Business Process <code>CRUDBP</code> in uso.
 * @param context  <code>ActionContext</code> in uso.
 * @return OggettoBulk this Oggetto bulk <code>EsercizioBulk</code>.
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
 * Determina quando abilitare o meno nell'interfaccia utente il campo <code>elemento_voce_coll</code>.
 * @return boolean true quando il campo deve essere disabilitato.
 */
public boolean isROElemento_voce_coll() {
	return elemento_voce_coll == null || elemento_voce_coll.getCrudStatus() == NORMAL;
}
/**
 * Metodo con cui si definisce il valore della variabile <code>elemento_voce</code>
 * di tipo <code>Elemento_voceBulk</code>.
 * @param newElemento_voce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_voce(Elemento_voceBulk newElemento_voce) {
	elemento_voce = newElemento_voce;
}
/**
 * Metodo con cui si definisce il valore della variabile <code>elemento_voce_coll</code>
 * di tipo <code>Elemento_voceBulk</code>.
 * @param newElemento_voce_coll it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElemento_voce_coll(Elemento_voceBulk newElemento_voce_coll) {
	elemento_voce_coll = newElemento_voce_coll;
}
/**
 * Metodo con cui si definisce il valore della variabile <code>naturaKeys</code>
 * di tipo <code>OrderedHashtable</code>.
 * @param newNaturaKeys it.cnr.jada.util.OrderedHashtable
 */
public void setNaturaKeys(it.cnr.jada.util.OrderedHashtable newNaturaKeys) {
	naturaKeys = newNaturaKeys;
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
}
