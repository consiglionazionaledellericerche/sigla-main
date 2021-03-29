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

package it.cnr.contab.anagraf00.tabter.bulk;

import java.util.Optional;
import java.util.StringTokenizer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

/**
 * Gestione dei dati relativi ai comuni esteri nella tabella Nazione
 */
@JsonInclude(value=Include.NON_NULL)
public class NazioneBulk extends NazioneBase {

	private final static Integer MARGINE = 0;
	
	public final static java.lang.String ITALIA = "I";
	public final static java.lang.String CEE = "C";
	public final static java.lang.String EXTRA_CEE = "E";
	public final static java.lang.String SAN_MARINO = "M";
	public final static java.lang.String INDIFFERENTE = "*";
	public final static OrderedHashtable TI_NAZIONE = new OrderedHashtable();

	static {
		TI_NAZIONE.put(ITALIA, "Italia");
		TI_NAZIONE.put(CEE, "CEE");
		TI_NAZIONE.put(EXTRA_CEE, "Extra CEE");
		TI_NAZIONE.put(SAN_MARINO, "San Marino");
		TI_NAZIONE.put(INDIFFERENTE, "Indifferente");
	}
	@JsonIgnore
	private it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa;
	@JsonIgnore
	private it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisaPerMissione;

	public final static java.lang.String IBAN_SEPARATORE = ";";
	public final static java.lang.String IBAN_TIPO_CARATTERE = "A";
	public final static java.lang.String IBAN_TIPO_NUMERICO = "N";
	public final static java.lang.String IBAN_TIPO_ALFANUMERICO = "C";

/**
 * 
 */
public NazioneBulk() {}
public NazioneBulk(java.lang.Long pg_nazione) {
	super(pg_nazione);
}
public java.lang.String getCd_divisa() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa = this.getDivisa();
	if (divisa == null)
		return null;
	return divisa.getCd_divisa();
}
public java.lang.String getCd_divisa_per_missione() {
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisaPerMissione = this.getDivisaPerMissione();
	if (divisaPerMissione == null)
		return null;
	return divisaPerMissione.getCd_divisa();
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 13.04.39)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisa() {
	return divisa;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 13.05.00)
 * @return it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getDivisaPerMissione() {
	return divisaPerMissione;
}
/**
 * Insert the method's description here.
 * Creation date: (27/11/2001 17.06.35)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTi_nazioneKeys() {
	return TI_NAZIONE;
}
public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {

	super.initializeForInsert(bp, context);
	setDivisa(new DivisaBulk());
	setDivisaPerMissione(new DivisaBulk());
	setTi_nazione(ITALIA);
	return this;
}
public OggettoBulk initializeForSearch(CRUDBP bp, ActionContext context) {

	super.initializeForSearch(bp, context);
	setDivisa(new DivisaBulk());
	setDivisaPerMissione(new DivisaBulk());
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
@JsonIgnore
public boolean isRODivisa() {
	return getDivisa()==null || getDivisa().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
@JsonIgnore
public boolean isRODivisaPerMissione() {
	return getDivisaPerMissione()==null || getDivisaPerMissione().getCrudStatus()==NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
@JsonIgnore
public boolean isROPgNazione() {
	return isTipoIndifferente();
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 11.52.30)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
 */
public boolean isTipoIndifferente() {
	return INDIFFERENTE.equals(getTi_nazione());
}
public void setCd_divisa(java.lang.String cd_divisa) {
	this.getDivisa().setCd_divisa(cd_divisa);
}
public void setCd_divisa_per_missione(java.lang.String cd_divisa_per_missione) {
	this.getDivisaPerMissione().setCd_divisa(cd_divisa_per_missione);
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 13.04.39)
 * @param newDivisa it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisa(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisa) {
	divisa = newDivisa;
}
/**
 * Insert the method's description here.
 * Creation date: (30/10/2002 13.05.00)
 * @param newDivisaPerMissione it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk
 */
public void setDivisaPerMissione(it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk newDivisaPerMissione) {
	divisaPerMissione = newDivisaPerMissione;
}
/**
 * Oltre alla normale validate da un avviso di errore se la nazione è nulla.
 *
 * @exception it.cnr.jada.bulk.ValidationException
*/
public void validate() throws ValidationException {

	super.validate();
	if (getDs_nazione()==null)
		throw new ValidationException("Il campo DESCRIZIONE non può essere vuoto");
	if (getTi_nazione()==null)
		throw new ValidationException("Selezionare un TIPO NAZIONE");
	if(getNazionalita() == null)
		throw new ValidationException("Il campo NAZIONALITA non può essere vuoto");
	if(getCd_nazione() == null)
		throw new ValidationException("Il campo CODICE NAZIONE non può essere vuoto");
}
@JsonIgnore
public String getStrutturaIbanLivello(int livello){
	int nrLivello = 0;
	String aLivello = "";

	StringTokenizer stLivelli= new StringTokenizer(Optional.ofNullable(getStruttura_iban()).orElse(""), IBAN_SEPARATORE);

	while (stLivelli.hasMoreTokens()) {
		nrLivello++;
		aLivello=stLivelli.nextToken();
		if (livello==nrLivello)
			break;
	}
	return aLivello;
}
	@JsonIgnore
public int getStrutturaIbanNrLivelli(){
	return new StringTokenizer(Optional.ofNullable(getStruttura_iban()).orElse(""),IBAN_SEPARATORE).countTokens();
}
	@JsonIgnore
public int getStruttura_iban_parte1MaxLength(){
	return getStrutturaIbanLivello(1).length();
}
	@JsonIgnore
public int getStruttura_iban_parte2MaxLength(){
	return getStrutturaIbanLivello(2).length();
}
	@JsonIgnore
public int getStruttura_iban_parte3MaxLength(){
	return getStrutturaIbanLivello(3).length();
}
	@JsonIgnore
public int getStruttura_iban_parte4MaxLength(){
	return getStrutturaIbanLivello(4).length();
}
	@JsonIgnore
public int getStruttura_iban_parte5MaxLength(){
	return getStrutturaIbanLivello(5).length();
}
	@JsonIgnore
public int getStruttura_iban_parte6MaxLength(){
	return getStrutturaIbanLivello(6).length();
}
	@JsonIgnore
public int getStruttura_iban_parte1InputSize(){
	return getStruttura_iban_parte1MaxLength()+MARGINE;
}
	@JsonIgnore
public int getStruttura_iban_parte2InputSize(){
	return getStruttura_iban_parte2MaxLength()+MARGINE;
}
	@JsonIgnore
public int getStruttura_iban_parte3InputSize(){
	return getStruttura_iban_parte3MaxLength()+MARGINE;
}
	@JsonIgnore
public int getStruttura_iban_parte4InputSize(){
	return getStruttura_iban_parte4MaxLength()+MARGINE;
}
	@JsonIgnore
public int getStruttura_iban_parte5InputSize(){
	return getStruttura_iban_parte5MaxLength()+MARGINE;
}
	@JsonIgnore
public int getStruttura_iban_parte6InputSize(){
	return getStruttura_iban_parte6MaxLength()+MARGINE;
}
	@JsonIgnore
public String getStrutturaPivaModello(int livello){
	int nrLivello = 0;
	String aLivello=null;

	StringTokenizer stLivelli=new StringTokenizer(getStruttura_piva(),IBAN_SEPARATORE);

	while (stLivelli.hasMoreTokens()) {
		nrLivello++;
		aLivello=stLivelli.nextToken();
		if (livello==nrLivello)
			break;
	}
	return aLivello;
}
	@JsonIgnore
public int getStrutturaPivaModelliPossibili(){
	return new StringTokenizer(Optional.ofNullable(getStruttura_piva()).orElse(""),IBAN_SEPARATORE).countTokens();
}

}
