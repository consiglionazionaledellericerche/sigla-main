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

public class Ass_cdp_uoBulk extends Ass_cdp_uoBase {
	public static final java.util.Dictionary stato_keys;

	public static final String STATO_INIZIALE = "X";
	public static final String STATO_ACCETTATO = "Y";
	public static final String STATO_NON_ACCETTATO = "N";

	static {
		stato_keys = new it.cnr.jada.util.OrderedHashtable();
		stato_keys.put(STATO_INIZIALE,"Iniziale");
		stato_keys.put(STATO_ACCETTATO,"Accettato");
		stato_keys.put(STATO_NON_ACCETTATO,"Non accettato");
	}

	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa;
	private java.math.BigDecimal giorni_uo_a1;
	private java.math.BigDecimal giorni_uo_a2;
	private java.math.BigDecimal giorni_uo_a3;
public Ass_cdp_uoBulk() {
	super();
}
public Ass_cdp_uoBulk(java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese) {
	super(cd_unita_organizzativa,esercizio,id_matricola,mese);
	setUnita_organizzativa(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_unita_organizzativa));
}
/**
 * Calcola i giorni relativi ad una UO per l'anno i frazionati per percentuale della linea di attività
 *
 * @param i	indice dell'anno (1,2,3)
 * @param giorni_lavorativi	giorni da frazionare
 */
public void calcolaGiorni_uo(int i,java.math.BigDecimal giorni_lavorativi) {
	setGiorni_uo(i,
		getPrc_uo(i) == null ? 
			java.math.BigDecimal.valueOf(0) : 
			getPrc_uo(i).multiply(
				giorni_lavorativi).divide(
				java.math.BigDecimal.valueOf(100),2,java.math.BigDecimal.ROUND_HALF_UP));
}
/**
 * Calcola i giorni relativi ad una UO per i tre anni (i=1,2,3)
 *
 * @param giorni_lavorativi	
 */
public void calcolaGiorni_uo(java.math.BigDecimal giorni_lavorativi) {
	calcolaGiorni_uo(1,giorni_lavorativi);
	calcolaGiorni_uo(2,giorni_lavorativi);
	calcolaGiorni_uo(3,giorni_lavorativi);
}
/**
/**
 * Imposta la percentuale della linea di attività relativa ad una UO per l'anno i a partire dai giorni
 *
 * @param i	indice dell'anno (1,2,3)
 * @param giorni_lavorativi	giorni lavorativi
 */
public void calcolaPrc_uo(int i,java.math.BigDecimal giorni_lavorativi) {
	setPrc_uo(i,
		getGiorni_uo(i) == null ?
			java.math.BigDecimal.valueOf(0) :
			getGiorni_uo(i).multiply(
				java.math.BigDecimal.valueOf(100)).divide(
				giorni_lavorativi,2,java.math.BigDecimal.ROUND_HALF_UP));
}
public java.lang.String getCd_unita_organizzativa() {
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk unita_organizzativa = this.getUnita_organizzativa();
	if (unita_organizzativa == null)
		return null;
	return unita_organizzativa.getCd_unita_organizzativa();
}
/**
 * Ritorna il numero di giorni relativi all'UO per l'anno i
 *
 * @param i	indice dell'anno (1,2,3)
 * @return giorni
 */
public java.math.BigDecimal getGiorni_uo(int i) {
	switch(i) {
		case 1 : return getGiorni_uo_a1();
		case 2 : return getGiorni_uo_a2();
		case 3 : return getGiorni_uo_a3();
	}
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_uo_a1'
 *
 * @return Il valore della proprietà 'giorni_uo_a1'
 */
public java.math.BigDecimal getGiorni_uo_a1() {
	return giorni_uo_a1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_uo_a2'
 *
 * @return Il valore della proprietà 'giorni_uo_a2'
 */
public java.math.BigDecimal getGiorni_uo_a2() {
	return giorni_uo_a2;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'giorni_uo_a3'
 *
 * @return Il valore della proprietà 'giorni_uo_a3'
 */
public java.math.BigDecimal getGiorni_uo_a3() {
	return giorni_uo_a3;
}
/**
 * Ritorna la percentuale relativi all'UO per l'anno i
 *
 * @param i	indice dell'anno (1,2,3)
 * @return la percentuale per UO
 */

public java.math.BigDecimal getPrc_uo(int i) {
	switch(i) {
		case 1 : return getPrc_uo_a1();
		case 2 : return getPrc_uo_a2();
		case 3 : return getPrc_uo_a3();
	}
	return null;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'stato_keys'
 *
 * @return Il valore della proprietà 'stato_keys'
 */
public final java.util.Dictionary getStato_keys() {
	return stato_keys;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'unita_organizzativa'
 *
 * @return Il valore della proprietà 'unita_organizzativa'
 */
public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUnita_organizzativa() {
	return unita_organizzativa;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'accettato'
 *
 * @return Il valore della proprietà 'accettato'
 */
public boolean isAccettato() {
	return STATO_ACCETTATO.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'non_accettato'
 *
 * @return Il valore della proprietà 'non_accettato'
 */
public boolean isNon_accettato() {
	return STATO_NON_ACCETTATO.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOGiorni'
 *
 * @return Il valore della proprietà 'rOGiorni'
 */
public boolean isROGiorni() {
    return !STATO_INIZIALE.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOPrc'
 *
 * @return Il valore della proprietà 'rOPrc'
 */
public boolean isROPrc() {
	return !STATO_INIZIALE.equalsIgnoreCase(getStato());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'rOStato'
 *
 * @return Il valore della proprietà 'rOStato'
 */
public boolean isROStato() {
	return false;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'stato_iniziale'
 *
 * @return Il valore della proprietà 'stato_iniziale'
 */
public boolean isStato_iniziale() {
	return STATO_INIZIALE.equalsIgnoreCase(getStato());
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getUnita_organizzativa().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Imposta i giorni per UO relativi all'anno i
 *
 * @param i	indice dell'anno (1,2,3)
 * @param giorni da impostare	
 */
public void setGiorni_uo(int i,java.math.BigDecimal giorni) {
	switch(i) {
		case 1 : setGiorni_uo_a1(giorni); break;
		case 2 : setGiorni_uo_a2(giorni); break;
		case 3 : setGiorni_uo_a3(giorni); break;
	}
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_uo_a1'
 *
 * @param newGiorni_uo_a1	Il valore da assegnare a 'giorni_uo_a1'
 */
public void setGiorni_uo_a1(java.math.BigDecimal newGiorni_uo_a1) {
	giorni_uo_a1 = newGiorni_uo_a1;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_uo_a2'
 *
 * @param newGiorni_uo_a2	Il valore da assegnare a 'giorni_uo_a2'
 */
public void setGiorni_uo_a2(java.math.BigDecimal newGiorni_uo_a2) {
	giorni_uo_a2 = newGiorni_uo_a2;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'giorni_uo_a3'
 *
 * @param newGiorni_uo_a3	Il valore da assegnare a 'giorni_uo_a3'
 */
public void setGiorni_uo_a3(java.math.BigDecimal newGiorni_uo_a3) {
	giorni_uo_a3 = newGiorni_uo_a3;
}
/**
 * Imposta la percentuale per linea di attività per UO relativi all'anno i in base ai giorni
 *
 * @param i	indice dell'anno (1,2,3)
 * @param giorni	
 */

public void setPrc_uo(int i,java.math.BigDecimal giorni) {
	switch(i) {
		case 1 : setPrc_uo_a1(giorni); break;
		case 2 : setPrc_uo_a2(giorni); break;
		case 3 : setPrc_uo_a3(giorni); break;
	}
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'unita_organizzativa'
 *
 * @param newUnita_organizzativa	Il valore da assegnare a 'unita_organizzativa'
 */
public void setUnita_organizzativa(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUnita_organizzativa) {
	unita_organizzativa = newUnita_organizzativa;
}
}
