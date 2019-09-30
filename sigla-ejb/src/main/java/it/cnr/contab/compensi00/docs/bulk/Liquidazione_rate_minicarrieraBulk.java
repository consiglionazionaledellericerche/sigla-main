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

package it.cnr.contab.compensi00.docs.bulk;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.latt.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (24/12/2002 11.51.07)
 * @author: Roberto Fantino
 */
public class Liquidazione_rate_minicarrieraBulk extends Minicarriera_rataBulk {
	private java.lang.Long nrMinicarrieraDa;
	private java.lang.Long nrMinicarrieraA;
	private java.sql.Timestamp dtRegistrazioneDa;
	private java.sql.Timestamp dtRegistrazioneA;
	private java.sql.Timestamp dtInizioMinicarrieraDa;
	private java.sql.Timestamp dtInizioMinicarrieraA;
	private java.sql.Timestamp dtScadenzaDa;
	private java.sql.Timestamp dtScadenzaA;
	private TerzoBulk terzo;
	private java.util.List rate;
	private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoce;
	private it.cnr.contab.config00.latt.bulk.WorkpackageBulk lineaAttivita;
	private it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk voceF;
	private Unita_organizzativaBulk uoScrivania;
	private EsercizioBulk esercizioScrivania;	
/**
 * Liquidazione_rate_minicarrieraBulk constructor comment.
 */
public Liquidazione_rate_minicarrieraBulk() {
	super();
}
/**
 * Liquidazione_rate_minicarrieraBulk constructor comment.
 * @param cd_cds java.lang.String
 * @param cd_unita_organizzativa java.lang.String
 * @param esercizio java.lang.Integer
 * @param pg_minicarriera java.lang.Long
 * @param pg_rata java.lang.Long
 */
public Liquidazione_rate_minicarrieraBulk(String cd_cds, String cd_unita_organizzativa, Integer esercizio, Long pg_minicarriera, Long pg_rata) {
	super(cd_cds, cd_unita_organizzativa, esercizio, pg_minicarriera, pg_rata);
}
/**
 * Insert the method's description here.
 * Creation date: (02/01/2003 14.43.13)
 * @return java.util.List
 */
public boolean addRata(Liquidazione_rate_minicarrieraBulk aRata) {

	if (getRate()==null)
		setRate(new java.util.LinkedList());
	return getRate().add(aRata);
}
/**
 * Insert the method's description here.
 * Creation date: (08/01/2003 16.12.30)
 * @return java.lang.String
 */
public Integer getCd_terzo() {
	if (getMinicarriera()==null)
		return null;
	return getMinicarriera().getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.16.58)
 * @return java.lang.String
 */
public String getCdElementoVoce() {
	if (getElementoVoce()==null)
		return null;
	return getElementoVoce().getCd_elemento_voce();
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.16.58)
 * @return java.lang.String
 */
public String getCdLineaAttivita() {
	if (getLineaAttivita()==null)
		return null;
	return getLineaAttivita().getCd_linea_attivita();
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.48)
 * @return java.lang.String
 */
public Integer getCdTerzo() {

	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.32)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtInizioMinicarrieraA() {
	return dtInizioMinicarrieraA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.21)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtInizioMinicarrieraDa() {
	return dtInizioMinicarrieraDa;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.00)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtRegistrazioneA() {
	return dtRegistrazioneA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.39.31)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtRegistrazioneDa() {
	return dtRegistrazioneDa;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.41.14)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtScadenzaA() {
	return dtScadenzaA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.41.02)
 * @return java.sql.Timestamp
 */
public java.sql.Timestamp getDtScadenzaDa() {
	return dtScadenzaDa;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 16.32.56)
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElementoVoce() {
	return elementoVoce;
}
/**
 * Insert the method's description here.
 * Creation date: (17/06/2003 16.56.12)
 * @return it.cnr.contab.config00.esercizio.bulk.EsercizioBulk
 */
public it.cnr.contab.config00.esercizio.bulk.EsercizioBulk getEsercizioScrivania() {
	return esercizioScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 16.34.04)
 * @return it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLineaAttivita() {
	return lineaAttivita;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.38.56)
 * @return java.lang.Long
 */
public java.lang.Long getNrMinicarrieraA() {
	return nrMinicarrieraA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.38.41)
 * @return java.lang.Long
 */
public java.lang.Long getNrMinicarrieraDa() {
	return nrMinicarrieraDa;
}
/**
 * Insert the method's description here.
 * Creation date: (02/01/2003 14.43.13)
 * @return java.util.List
 */
public java.util.List getRate() {
	return rate;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 15.30.36)
 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getTerzo() {
	return terzo;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.16.58)
 * @return java.lang.String
 */
public String getTipoAppartenenza() {
	if (getElementoVoce()==null)
		return null;
	if (Elemento_voceHome.APPARTENENZA_CDS.equalsIgnoreCase(getElementoVoce().getTi_appartenenza()))
		return "D - CDS";
	return "C - CNR";
	
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.16.58)
 * @return java.lang.String
 */
public String getTipoGestione() {
	if (getElementoVoce()==null)
		return null;
	else if (Elemento_voceHome.GESTIONE_SPESE.equalsIgnoreCase(getElementoVoce().getTi_gestione()))
		return "S - Spese";
	return "E - Entrate";
}
/**
 * Insert the method's description here.
 * Creation date: (08/01/2003 17.38.41)
 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoScrivania() {
	return uoScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 10.48.48)
 * @return it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk getVoceF() {
	return voceF;
}
/**
 * Inizializza il ricevente per la visualizzazione in un <code>FormController</code>.
 * Questo metodo viene invocato da {@link #initializeForEdit}, {@link #initializeForInsert},
 * {@link #initializeForSearch} e {@link #initializeForFreeSearch} e può contenere
 * inizializzazioni comuni ai 4 stati del <code>FormController</code>
 */
public OggettoBulk initialize(CRUDBP bp, ActionContext context) 
{
	setUoScrivania(it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context));

	setTerzo(new TerzoBulk());
	resetElementoVoce();

	// Inizializzazioni necessarie per verificare se l'esercizio è aperto
	setMinicarriera(new MinicarrieraBulk());
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = it.cnr.contab.utenze00.bulk.CNRUserInfo.getUnita_organizzativa(context);
	setCd_cds(unita_organizzativa.getCd_unita_padre());
	setEsercizioScrivania(new EsercizioBulk( getCd_cds(), it.cnr.contab.utenze00.bulk.CNRUserInfo.getEsercizio(context)));	
	
	return this;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 18.55.34)
 * @return boolean
 */
public boolean isROElementoVoce() {
	return (getElementoVoce() == null || getElementoVoce().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 18.55.34)
 * @return boolean
 */
public boolean isROLineaAttivita() {
	return (getLineaAttivita() == null || getLineaAttivita().getCrudStatus() == OggettoBulk.NORMAL);
}
//
// per interfaccia IDocumentoAmministrativoSpesaBulk
//
public Liquidazione_rate_minicarrieraBulk removeFromRate(int index) {

	if(getRate() == null)
		return null;
	return (Liquidazione_rate_minicarrieraBulk)getRate().remove(index);
}

public void resetElementoVoce() {
	setElementoVoce(new Elemento_voceBulk());
	getElementoVoce().setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	getElementoVoce().setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CDS);
	resetLineaAttivita();
}
public void resetLineaAttivita() {
	setLineaAttivita(new WorkpackageBulk());
	resetVoceF();
}
public void resetVoceF(){
	setVoceF(null);
}
/**
 * Insert the method's description here.
 * Creation date: (08/01/2003 16.12.30)
 * @return java.lang.String
 */
public void setCd_terzo(Integer newCdTerzo) {}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.48)
 * @param newCdTerzo java.lang.String
 */
public void setCdTerzo(Integer newCdTerzo) {
	this.getTerzo().setCd_terzo(newCdTerzo);
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.32)
 * @param newDtInizioMinicarrieraA java.sql.Timestamp
 */
public void setDtInizioMinicarrieraA(java.sql.Timestamp newDtInizioMinicarrieraA) {
	dtInizioMinicarrieraA = newDtInizioMinicarrieraA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.21)
 * @param newDtInizioMinicarrieraDa java.sql.Timestamp
 */
public void setDtInizioMinicarrieraDa(java.sql.Timestamp newDtInizioMinicarrieraDa) {
	dtInizioMinicarrieraDa = newDtInizioMinicarrieraDa;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.40.00)
 * @param newDtRegistrazioneA java.sql.Timestamp
 */
public void setDtRegistrazioneA(java.sql.Timestamp newDtRegistrazioneA) {
	dtRegistrazioneA = newDtRegistrazioneA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.39.31)
 * @param newDtRegistrazioneDa java.sql.Timestamp
 */
public void setDtRegistrazioneDa(java.sql.Timestamp newDtRegistrazioneDa) {
	dtRegistrazioneDa = newDtRegistrazioneDa;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.41.14)
 * @param newDtScadenzaA java.sql.Timestamp
 */
public void setDtScadenzaA(java.sql.Timestamp newDtScadenzaA) {
	dtScadenzaA = newDtScadenzaA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.41.02)
 * @param newDtScadenzaDa java.sql.Timestamp
 */
public void setDtScadenzaDa(java.sql.Timestamp newDtScadenzaDa) {
	dtScadenzaDa = newDtScadenzaDa;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 16.32.56)
 * @param newElementoVoce it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setElementoVoce(it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk newElementoVoce) {
	elementoVoce = newElementoVoce;
}
/**
 * Insert the method's description here.
 * Creation date: (17/06/2003 16.56.12)
 * @param newEsercizioScrivania it.cnr.contab.config00.esercizio.bulk.EsercizioBulk
 */
public void setEsercizioScrivania(it.cnr.contab.config00.esercizio.bulk.EsercizioBulk newEsercizioScrivania) {
	esercizioScrivania = newEsercizioScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 16.34.04)
 * @param newLineaAttivita it.cnr.contab.config00.latt.bulk.Linea_attivitaBulk
 */
public void setLineaAttivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLineaAttivita) {
	lineaAttivita = newLineaAttivita;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.38.56)
 * @param newNrMinicarrieraA java.lang.Long
 */
public void setNrMinicarrieraA(java.lang.Long newNrMinicarrieraA) {
	nrMinicarrieraA = newNrMinicarrieraA;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 12.38.41)
 * @param newNrMinicarrieraDa java.lang.Long
 */
public void setNrMinicarrieraDa(java.lang.Long newNrMinicarrieraDa) {
	nrMinicarrieraDa = newNrMinicarrieraDa;
}
/**
 * Insert the method's description here.
 * Creation date: (02/01/2003 14.43.13)
 * @param newRate java.util.List
 */
public void setRate(java.util.List newRate) {
	rate = newRate;
}
/**
 * Insert the method's description here.
 * Creation date: (24/12/2002 15.30.36)
 * @param newTerzo it.cnr.contab.anagraf00.core.bulk.TerzoBulk
 */
public void setTerzo(it.cnr.contab.anagraf00.core.bulk.TerzoBulk newTerzo) {
	terzo = newTerzo;
}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.14.27)
 * @param newTiGestione java.lang.String
 */
public void setTipoAppartenenza(String newTiAppartenenza) {}
/**
 * Insert the method's description here.
 * Creation date: (07/01/2003 17.14.27)
 * @param newTiGestione java.lang.String
 */
public void setTipoGestione(String newTiGestione) {}
/**
 * Insert the method's description here.
 * Creation date: (08/01/2003 17.38.41)
 * @param newUoScrivania it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
 */
public void setUoScrivania(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoScrivania) {
	uoScrivania = newUoScrivania;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2003 10.48.48)
 * @param newVoceF it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk
 */
public void setVoceF(it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk newVoceF) {
	voceF = newVoceF;
}
}
