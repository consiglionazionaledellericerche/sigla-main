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

package it.cnr.contab.coepcoan00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_anag_voce_epBulk extends Ass_anag_voce_epBase {

	private it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = new it.cnr.contab.config00.pdcep.bulk.ContoBulk();
	private it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk classanag = new it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk();
	
	public static java.util.Dictionary ti_entita_giuridicaKeys;
	public static java.util.Dictionary ti_entitaKeys;
	public static java.util.Dictionary ti_terzoKeys;
	public static java.util.Dictionary ti_italiano_esteroKeys;
/**
 * Ass_anag_voce_epBulk constructor comment.
 */
public Ass_anag_voce_epBulk() {
	super();
}
/**
 * Ass_anag_voce_epBulk constructor comment.
 */
public Ass_anag_voce_epBulk(java.lang.String cd_classific_anag,java.lang.String ente_altro,java.lang.Integer esercizio,java.lang.String italiano_estero,java.lang.String ti_entita,java.lang.String ti_terzo) {
	super(cd_classific_anag,ente_altro,esercizio,italiano_estero,ti_entita,ti_terzo);
	setClassanag(new it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk(cd_classific_anag));
}
public java.lang.String getCd_classific_anag() {
	it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk classanag = this.getClassanag();
	if (classanag == null)
		return null;
	return classanag.getCd_classific_anag();
}
public java.lang.String getCd_voce_ep() {
	it.cnr.contab.config00.pdcep.bulk.ContoBulk conto = this.getConto();
	if (conto == null)
		return null;
	return conto.getCd_voce_ep();
}
/**
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk getClassanag() {
	return classanag;
}
/**
 * @return it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public it.cnr.contab.config00.pdcep.bulk.ContoBulk getConto() {
	return conto;
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_entita_giuridicaKeys() 
{
	if ( ti_entita_giuridicaKeys == null )
	{
		ti_entita_giuridicaKeys = (java.util.Dictionary) new it.cnr.jada.util.OrderedHashtable();
		for ( java.util.Enumeration e = it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ENTITA_GIURIDICA.keys(); e.hasMoreElements(); )
		{
			String key = (String) e.nextElement();
			ti_entita_giuridicaKeys.put( key, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ENTITA_GIURIDICA.get( key) );
		}
		ti_entita_giuridicaKeys.put("*", "*");
	}	
	return ti_entita_giuridicaKeys;
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_entitaKeys() 
{
	if ( ti_entitaKeys == null )
	{
		ti_entitaKeys = (java.util.Dictionary) new it.cnr.jada.util.OrderedHashtable();
		for ( java.util.Enumeration e = it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ti_entitaKeys.keys(); e.hasMoreElements(); )
		{
			String key = (String) e.nextElement();
			ti_entitaKeys.put( key, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ti_entitaKeys.get( key) );
		}
		ti_entitaKeys.put("*", "*");
	}	
	return ti_entitaKeys;
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_italiano_esteroKeys() 
{
	if ( ti_italiano_esteroKeys == null )
	{
		ti_italiano_esteroKeys = (java.util.Dictionary) new it.cnr.jada.util.OrderedHashtable();
		for ( java.util.Enumeration e = it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ITALIANO_ESTERO.keys(); e.hasMoreElements(); )
		{
			String key = (String) e.nextElement();
			ti_italiano_esteroKeys.put( key, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.ITALIANO_ESTERO.get( key) );
		}
		ti_italiano_esteroKeys.put("*", "*");
	}	
	return ti_italiano_esteroKeys;
}
/**
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_terzoKeys() 
{
	if ( ti_terzoKeys == null )
	{
		ti_terzoKeys = (java.util.Dictionary) new it.cnr.jada.util.OrderedHashtable();
		for ( java.util.Enumeration e = it.cnr.contab.anagraf00.core.bulk.TerzoBulk.DEBITORE_CREDITORE.keys(); e.hasMoreElements(); )
		{
			String key = (String) e.nextElement();
			ti_terzoKeys.put( key, it.cnr.contab.anagraf00.core.bulk.TerzoBulk.DEBITORE_CREDITORE.get( key) );
		}
		ti_terzoKeys.put("*", "*");
	}	
	return ti_terzoKeys;
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
 * Inizializza l'Oggetto Bulk per la ricerca.
 * @param bp Il Business Process in uso
 * @param context Il contesto dell'azione
 * @return OggettoBulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeForSearch(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	setEsercizio( it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context) );
	return this;
}
public boolean isROClassanag() {
	return classanag == null || classanag.getCrudStatus() == NORMAL;
}
public boolean isROConto() {
	return conto == null || conto.getCrudStatus() == NORMAL;
}
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.getClassanag().setCd_classific_anag(cd_classific_anag);
}
public void setCd_voce_ep(java.lang.String cd_voce_ep) {
	this.getConto().setCd_voce_ep(cd_voce_ep);
}
/**
 * @param newClassanag it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk
 */
public void setClassanag(it.cnr.contab.anagraf00.tabrif.bulk.Classificazione_anagBulk newClassanag) {
	classanag = newClassanag;
}
/**
 * @param newConto it.cnr.contab.config00.pdcep.bulk.ContoBulk
 */
public void setConto(it.cnr.contab.config00.pdcep.bulk.ContoBulk newConto) {
	conto = newConto;
}
/**
 * Metodo con cui si verifica la validità di alcuni campi, mediante un 
 * controllo sintattico o contestuale.
 */
public void validate() throws ValidationException {
	super.validate();

	// controllo che, in fase di inserimento, tutti i campi necessari siano validati
	if ( getTi_terzo() == null || getItaliano_estero() == null || getTi_entita() == null)
		throw new ValidationException ("Attenzione! Il campo TIPO TERZO o ITALIANO/ESTERO o TIPO ENTITA' non può essere nullo.");
		
	if ( !getTi_entita().equals( it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.GIURIDICA ) && this.getEnte_altro() == null)
		this.setEnte_altro("*");

	// controllo contestuale su campo TIPO ENTITA' in relazione al campo ENTE/ALTRO
	if ( !getTi_entita().equals( it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.GIURIDICA ) && !this.getEnte_altro().equals("*"))
		throw new ValidationException ("Attenzione! Il campo ENTE/ALTRO non è valido perchè non conforme al valore del campo TIPO ENTITA' indicato.");
		// this.setEnte_altro("*");
		
	// controllo su campo ENTE/ALTRO in relazione al campo TIPO ENTITA'
	if ( getTi_entita().equals( it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk.GIURIDICA ) && getEnte_altro() == null )
		throw new ValidationException ("Il campo ENTE/ALTRO non può essere nullo.");
	
	// controllo su campo CODICE CLASSIFICAZIONE ANAGRAFICA
	if ( getCd_classific_anag() == null )
		throw new ValidationException ("Il campo CODICE CLASSIFICAZIONE ANAGRAFICA non può essere nullo.");

	// controllo su campo CODICE CONTO
	if ( getCd_voce_ep() == null )
		throw new ValidationException ("Il campo CONTO non può essere nullo.");		
		
}
}
