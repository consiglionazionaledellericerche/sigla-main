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

package it.cnr.contab.pdg00.cdip.bulk;

import java.math.BigDecimal;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_cdp_laBulk extends Ass_cdp_laBase {
	public static final java.util.Dictionary stato_keys;
	public static final java.util.Dictionary stato_mese_keys;

	public static final String STATO_SCARICATO = "S";
	public static final String STATO_SCARICATO_PDGP = "SP";
	public static final String STATO_NON_SCARICATO = "I";

	//VARIABILI STATICHE VALIDE SOLO PER I DATI GESTIONALI (MESE!=0)
	public static final String STATO_SCARICATO_PROVVISORIO = "SPR";
	public static final String STATO_SCARICATO_DEFINITIVO = "SDE";

	static {
		stato_keys = new it.cnr.jada.util.OrderedHashtable();
		stato_keys.put("I","Non scaricato");
		stato_keys.put("S","Scaricato");
		stato_keys.put("SP","Scaricato in PdgP");
	}

	static {
		stato_mese_keys = new it.cnr.jada.util.OrderedHashtable();
		stato_mese_keys.put(STATO_NON_SCARICATO,"Non scaricato");
		stato_mese_keys.put(STATO_SCARICATO_PROVVISORIO,"Scaricato Provvisorio");
		stato_mese_keys.put(STATO_SCARICATO_DEFINITIVO,"Scaricato Definitivo");
	}

	private it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita;

	private java.math.BigDecimal giorni_la_a1;
	private java.math.BigDecimal giorni_la_a2;
	private java.math.BigDecimal giorni_la_a3;
	private boolean readonly;
public Ass_cdp_laBulk() {
	super();
}
public Ass_cdp_laBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese) {
	super(cd_centro_responsabilita,cd_linea_attivita,esercizio,id_matricola,mese);
	setLinea_attivita(new it.cnr.contab.config00.latt.bulk.WorkpackageBulk(cd_centro_responsabilita,cd_linea_attivita));
}
/**
 * Calcola i giorni lavorativi in funzione della percentuale di ripartizione su linea di attività
 *
 * @param i indice dell'anno (1,2,3)	
 * @param giorni_lavorativi	giorni lavorativi da frazionare
 */
public void calcolaGiorni_la(int i,java.math.BigDecimal giorni_lavorativi) {
	setGiorni_la(i,
		getPrc_la(i) == null ? 
			java.math.BigDecimal.valueOf(0) : 
			getPrc_la(i).multiply(
				giorni_lavorativi).divide(
				java.math.BigDecimal.valueOf(100),2,java.math.BigDecimal.ROUND_HALF_UP));
}
/**
 * Calcola i giorni lavorativi in funzione della percentuale di ripartizione su linea di attività
 *
 * @param giorni_lavorativi	giorni lavorativi da frazionare
 */
public void calcolaGiorni_la(java.math.BigDecimal giorni_lavorativi) {
	calcolaGiorni_la(1,giorni_lavorativi);
	calcolaGiorni_la(2,giorni_lavorativi);
	calcolaGiorni_la(3,giorni_lavorativi);
}
/**
 * Calcola la percentuale su linea di attività a partire dai giorni per i tre anni (i=1,2,3)
 * 
 * @param i indice dell'anno (1,2,3)	
 * @param giorni_lavorativi	giorni lavorativi
 */
public void calcolaPrc_la(int i,java.math.BigDecimal giorni_lavorativi) {
	if(giorni_lavorativi.compareTo(BigDecimal.ZERO)!=0)
		setPrc_la(i,
				getGiorni_la(i) == null ?
					java.math.BigDecimal.valueOf(0) :
			    getGiorni_la(i).multiply(
			    	java.math.BigDecimal.valueOf(100)).divide(
			    		giorni_lavorativi,2,java.math.BigDecimal.ROUND_HALF_UP));
	else
		setPrc_la(i,BigDecimal.ZERO);
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = linea_attivita.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
public java.lang.String getCd_linea_attivita() {
	it.cnr.contab.config00.latt.bulk.WorkpackageBulk linea_attivita = this.getLinea_attivita();
	if (linea_attivita == null)
		return null;
	return linea_attivita.getCd_linea_attivita();
}
/**
 * Ritorna i giorni relativi all'anno i
 *
 * @param i indice dell'anno (1,2,3)	
 * @return numero dei giorni 
 */
public java.math.BigDecimal getGiorni_la(int i) {
	switch(i) {
		case 1 : return getGiorni_la_a1();
		case 2 : return getGiorni_la_a2();
		case 3 : return getGiorni_la_a3();
	}
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_la_a1'
 *
 * @return Il valore della proprietà 'giorni_la_a1'
 */
public java.math.BigDecimal getGiorni_la_a1() {
	return giorni_la_a1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_la_a2'
 *
 * @return Il valore della proprietà 'giorni_la_a2'
 */
public java.math.BigDecimal getGiorni_la_a2() {
	return giorni_la_a2;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_la_a3'
 *
 * @return Il valore della proprietà 'giorni_la_a3'
 */
public java.math.BigDecimal getGiorni_la_a3() {
	return giorni_la_a3;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'linea_attivita'
 *
 * @return Il valore della proprietà 'linea_attivita'
 */
public it.cnr.contab.config00.latt.bulk.WorkpackageBulk getLinea_attivita() {
	return linea_attivita;
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param i	
 * @return 
 */
public java.math.BigDecimal getPrc_la(int i) {
	switch(i) {
		case 1 : return getPrc_la_a1();
		case 2 : return getPrc_la_a2();
		case 3 : return getPrc_la_a3();
	}
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'nonCancellabile'
 *
 * @return Il valore della proprietà 'nonCancellabile'
 */
public boolean isNonCancellabile() {
	return isReadonly() || STATO_SCARICATO.equalsIgnoreCase(getStato()) ||
  					       STATO_SCARICATO_PDGP.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'readonly'
 *
 * @return Il valore della proprietà 'readonly'
 */
public boolean isReadonly() {
	return readonly;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOGiorni'
 *
 * @return Il valore della proprietà 'rOGiorni'
 */
public boolean isROGiorni() {
	return isReadonly() || STATO_SCARICATO.equalsIgnoreCase(getStato()) ||
	                       STATO_SCARICATO_PDGP.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOPrc'
 *
 * @return Il valore della proprietà 'rOPrc'
 */
public boolean isROPrc() {
	return isReadonly() || STATO_SCARICATO.equalsIgnoreCase(getStato()) ||
	                       STATO_SCARICATO_PDGP.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'scaricato'
 *
 * @return Il valore della proprietà 'scaricato'
 */
public boolean isScaricato() {
	return STATO_SCARICATO.equalsIgnoreCase(getStato());
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getLinea_attivita().getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
	this.getLinea_attivita().setCd_linea_attivita(cd_linea_attivita);
}
/**
 * Imposta i giorni relativi all'anno i
 *
 * @param i indice dell'anno (1,2,3)	
 * @param giorni giorni da impostare
 */
public void setGiorni_la(int i,java.math.BigDecimal giorni) {
	switch(i) {
		case 1 : setGiorni_la_a1(giorni); break;
		case 2 : setGiorni_la_a2(giorni); break;
		case 3 : setGiorni_la_a3(giorni); break;
	}
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_la_a1'
 *
 * @param newGiorni_la_a1	Il valore da assegnare a 'giorni_la_a1'
 */
public void setGiorni_la_a1(java.math.BigDecimal newGiorni_la_a1) {
	giorni_la_a1 = newGiorni_la_a1;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_la_a2'
 *
 * @param newGiorni_la_a2	Il valore da assegnare a 'giorni_la_a2'
 */
public void setGiorni_la_a2(java.math.BigDecimal newGiorni_la_a2) {
	giorni_la_a2 = newGiorni_la_a2;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_la_a3'
 *
 * @param newGiorni_la_a3	Il valore da assegnare a 'giorni_la_a3'
 */
public void setGiorni_la_a3(java.math.BigDecimal newGiorni_la_a3) {
	giorni_la_a3 = newGiorni_la_a3;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'linea_attivita'
 *
 * @param newLinea_attivita	Il valore da assegnare a 'linea_attivita'
 */
public void setLinea_attivita(it.cnr.contab.config00.latt.bulk.WorkpackageBulk newLinea_attivita) {
	linea_attivita = newLinea_attivita;
}
/**
 * Imposta la percentuale relativa alla linea di attività e all'anno i a partire dai giorni
 *
 * @param i indice dell'anno (1,2,3)	
 * @param giorni giorni
 */
public void setPrc_la(int i,java.math.BigDecimal giorni) {
	switch(i) {
		case 1 : setPrc_la_a1(giorni); break;
		case 2 : setPrc_la_a2(giorni); break;
		case 3 : setPrc_la_a3(giorni); break;
	}
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'readonly'
 *
 * @param newReadonly	Il valore da assegnare a 'readonly'
 */
public void setReadonly(boolean newReadonly) {
	readonly = newReadonly;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'stato_mese_keys'
 *
 * @return Il valore della proprietà 'stato_mese_keys'
 */
public final java.util.Dictionary getStato_mese_keys() {
	return stato_mese_keys;
}
}
