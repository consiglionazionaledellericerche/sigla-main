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

package it.cnr.contab.preventvar00.bulk;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Var_bilancio_detBulk extends Var_bilancio_detBase {

	private Var_bilancioBulk varBilancio = new Var_bilancioBulk();
	private V_assestato_voceBulk voceFSaldi = new V_assestato_voceBulk();

	private final static java.util.Dictionary TIPO_GESTIONE;
	public final static String ENTRATA = Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_ENTRATA;
	public final static String SPESA	 = Voce_f_saldi_cdr_lineaBulk.TIPO_GESTIONE_SPESA;
	public final static String ENTRATA_SPESA = "X";

	static{
		TIPO_GESTIONE = new it.cnr.jada.util.OrderedHashtable();
		TIPO_GESTIONE.put(ENTRATA, 		 "Entrata");
		TIPO_GESTIONE.put(SPESA,   		 "Spesa"  );
		TIPO_GESTIONE.put(ENTRATA_SPESA, "Entrambi");
	}

public Var_bilancio_detBulk() {
	super();
}
public Var_bilancio_detBulk(java.lang.String cd_cds,java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_cds,cd_voce,esercizio,pg_variazione,ti_appartenenza,ti_gestione);
}
/* 
 * Getter dell'attributo STANZIAMENTO ASSESTATO
 */
public java.math.BigDecimal getAssestato() {

	V_assestato_voceBulk aVSC = null;
	if ((voceFSaldi=getVoceFSaldi())!=null)
		return (voceFSaldi.getAssestato());
	return new java.math.BigDecimal(0);
}
public java.lang.String getCd_cds() {
	it.cnr.contab.preventvar00.bulk.Var_bilancioBulk varBilancio = this.getVarBilancio();
	if (varBilancio == null)
		return null;
	return varBilancio.getCd_cds();
}
public java.lang.String getCd_voce() {
	V_assestato_voceBulk voceFSaldi = this.getVoceFSaldi();
	if (voceFSaldi == null)
		return null;
	return voceFSaldi.getCd_voce();
}
public java.lang.Integer getEsercizio() {
	it.cnr.contab.preventvar00.bulk.Var_bilancioBulk varBilancio = this.getVarBilancio();
	if (varBilancio == null)
		return null;
	return varBilancio.getEsercizio();
}
/* 
 * Getter dell'attributo IMPORTO ENTRATA
 */
public java.math.BigDecimal getImportoEntrata() {

	if (ENTRATA.equals(getTipoGestione()) && getIm_variazione()!=null)
		return getIm_variazione();
	return new java.math.BigDecimal(0);
}
/* 
 * Getter dell'attributo IMPORTO SPESA
 */
public java.math.BigDecimal getImportoSpesa() {

	if (SPESA.equals(getTipoGestione()) && getIm_variazione()!=null)
		return getIm_variazione();
	return new java.math.BigDecimal(0);
}
public java.lang.Long getPg_variazione() {
	it.cnr.contab.preventvar00.bulk.Var_bilancioBulk varBilancio = this.getVarBilancio();
	if (varBilancio == null)
		return null;
	return varBilancio.getPg_variazione();
}
public java.lang.String getTi_appartenenza() {
	it.cnr.contab.preventvar00.bulk.Var_bilancioBulk varBilancio = this.getVarBilancio();
	if (varBilancio == null)
		return null;
	return varBilancio.getTi_appartenenza();
}
public java.lang.String getTi_gestione() {
	V_assestato_voceBulk voceFSaldi = this.getVoceFSaldi();
	if (voceFSaldi == null)
		return null;
	return voceFSaldi.getTi_gestione();
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2002 15.13.40)
 * @return java.lang.String
 */
public java.lang.String getTipoGestione() {

	if (getTi_gestione()==null)
		return ENTRATA_SPESA;
	
	return getTi_gestione();
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2002 15.10.41)
 * @return java.util.Dictionary
 */
public java.util.Dictionary getTipoGestioneKeys() {
	return TIPO_GESTIONE;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'varBilancio'
 *
 * @return Il valore della proprietà 'varBilancio'
 */
public Var_bilancioBulk getVarBilancio() {
	return varBilancio;
}

/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>
 * in stato <code>INSERT</code>.
 * Questo metodo viene invocato automaticamente da un 
 * <code>it.cnr.jada.util.action.CRUDBP</code> quando viene inizializzato
 * per l'inserimento di un OggettoBulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert(bp,context);
	resetImporti();
	return this;
	
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'voceRO'
 *
 * @return Il valore della proprietà 'voceRO'
 */
public boolean isCdVoceRO() {
	return (getVoceFSaldi() == null || getVoceFSaldi().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'voceRO'
 *
 * @return Il valore della proprietà 'voceRO'
 */
public boolean isTipoGestioneRO() {
	return ( Var_bilancioBulk.STORNO_E.equals(varBilancio.getTi_variazione()) ||
		     Var_bilancioBulk.STORNO_S.equals(varBilancio.getTi_variazione()) ) ||
		     isCdVoceRO() ||
		     (varBilancio.getCompetenzaResiduo() != null && 
			  varBilancio.getCompetenzaResiduo().equalsIgnoreCase("R"));
}
		
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'voceRO'
 *
 * @return Il valore della proprietà 'voceRO'
 */
public boolean isVoceRO() {
	return this.isNotNew();
}
/**
 * Insert the method's description here.
 * Creation date: (28/08/2002 15.26.54)
 */
private void resetImporti() {
	
	setIm_variazione(new java.math.BigDecimal(0));
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getVarBilancio().setCd_cds(cd_cds);
}
public void setCd_voce(java.lang.String cd_voce) {
	this.getVoceFSaldi().setCd_voce(cd_voce);
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getVarBilancio().setEsercizio(esercizio);
}
public void setPg_variazione(java.lang.Long pg_variazione) {
	this.getVarBilancio().setPg_variazione(pg_variazione);
}
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.getVarBilancio().setTi_appartenenza(ti_appartenenza);
}
public void setTi_gestione(java.lang.String ti_gestione) {
	this.getVoceFSaldi().setTi_gestione(ti_gestione);
}
/**
 * Insert the method's description here.
 * Creation date: (26/08/2002 15.13.40)
 * @param newTipoGestione java.lang.String
 */
public void setTipoGestione(java.lang.String newTipoGestione) {

	if(ENTRATA_SPESA.equals(newTipoGestione))
		setTi_gestione(null);
	else
		setTi_gestione(newTipoGestione);
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'varBilancio'
 *
 * @param newVarBilancio	Il valore da assegnare a 'varBilancio'
 */
public void setVarBilancio(Var_bilancioBulk newVarBilancio) {
	varBilancio = newVarBilancio;
}
/**
 * Insert the method's description here.
 * Creation date: (30/08/2002 14.14.32)
 */
public void validate() throws ValidationException{

	if (getCd_voce()==null)
		throw new ValidationException("Il campo VOCE non può essere vuoto");

	if (getIm_variazione()==null)
		throw new ValidationException("Il campo IMPORTO non può essere vuoto");
}
public V_assestato_voceBulk getVoceFSaldi() {
	return voceFSaldi;
}
public void setVoceFSaldi(V_assestato_voceBulk voceFSaldi) {
	this.voceFSaldi = voceFSaldi;
}
}
