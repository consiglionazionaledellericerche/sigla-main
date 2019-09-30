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
import java.util.Dictionary;
import java.util.Iterator;

import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.bulk.BulkList;

public class V_cdp_matricolaBulk extends V_cdp_matricolaBase {
	public static final String PROVENIENZA_INTERNA = "I";
	public static final String PROVENIENZA_CARICATO = "C";

	private BulkList costiScaricati;
	private BulkList costiScaricatiAltraUO;
	private java.math.BigDecimal giorni_lavorativi_a1;
	private java.math.BigDecimal giorni_lavorativi_a2;
	private java.math.BigDecimal giorni_lavorativi_a3;
	private java.math.BigDecimal totale_prc_la_a1;
	private java.math.BigDecimal totale_prc_la_a2;
	private java.math.BigDecimal totale_prc_la_a3;

	private Ass_cdp_uoBulk costoCaricato;
	public final static Dictionary tipo_naturaKeys = NaturaBulk.tipo_naturaKeys;
	
public V_cdp_matricolaBulk() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'stato_keys'
 *
 * @return Il valore della proprietà 'stato_keys'
 */
public final java.util.Dictionary getStato_keys() {
	return Ass_cdp_uoBulk.stato_keys;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_rapportoKeys'
 *
 * @return Il valore della proprietà 'ti_rapportoKeys'
 */
public java.util.Dictionary getTi_rapportoKeys() {
	return Costo_del_dipendenteBulk.ti_rapportoKeys;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'provenienzaCaricato'
 *
 * @return Il valore della proprietà 'provenienzaCaricato'
 */
public boolean isProvenienzaCaricato() {
	return PROVENIENZA_CARICATO.equalsIgnoreCase(getTi_provenienza());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'provenienzaInterna'
 *
 * @return Il valore della proprietà 'provenienzaInterna'
 */
public boolean isProvenienzaInterna() {
	return PROVENIENZA_INTERNA.equalsIgnoreCase(getTi_provenienza());
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costiScaricati'
 *
 * @return Il valore della proprietà 'costiScaricati'
 */
public BulkList getCostiScaricati() {
	return costiScaricati;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costiScaricatiAltraUO'
 *
 * @return Il valore della proprietà 'costiScaricatiAltraUO'
 */
public BulkList getCostiScaricatiAltraUO() {
	return costiScaricatiAltraUO;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'costiScaricati'
 *
 * @param newCostiScaricati	Il valore da assegnare a 'costiScaricati'
 */
public void setCostiScaricati(BulkList newCostiScaricati) {
	costiScaricati = newCostiScaricati;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'costiScaricatiAltraUO'
 *
 * @param newCostiScaricatiAltraUO	Il valore da assegnare a 'costiScaricatiAltraUO'
 */
public void setCostiScaricatiAltraUO(BulkList newCostiScaricatiAltraUO) {
	costiScaricatiAltraUO = newCostiScaricatiAltraUO;
}
/**
 * Rimuove una specifica di ripartizione CDP per linea di attività dalla lista dei costi scaricati
 * 
 * @param index indice dell'elemento da eliminare	
 * @return elemento eliminato
 */
public Ass_cdp_laBulk removeFromCostiScaricati(int index) {
	Ass_cdp_laBulk costi_scaricati = (Ass_cdp_laBulk)costiScaricati.remove(index);
	if (costi_scaricati != null)
		costi_scaricati.setToBeDeleted();
	return costi_scaricati;
}
/**
 * Rimuove una specifica di scarico CDP su altra UO dalla lista degli scarichi verso altra UO
 * 
 * @param index	indeice dell'elemento da rimuovere
 * @return elemento rimosso
 */
public Ass_cdp_uoBulk removeFromCostiScaricatiAltraUO(int index) {
	Ass_cdp_uoBulk ass_cdp_uo = (Ass_cdp_uoBulk)costiScaricatiAltraUO.remove(index);
	if (ass_cdp_uo != null)
		ass_cdp_uo.setToBeDeleted();
	return ass_cdp_uo;
}
/**
 * Aggiunge una specifica di ripartizione CDP per linea di attività alla lista dei costi scaricati
 *
 * @param ass_cdp_la specifica di ripartizione per data linea di attività
 * @return ritorna l'indice dell'elemento aggiunto nella lista dei costi scaricati
 */
public int addToCostiScaricati(Ass_cdp_laBulk ass_cdp_la) {
	costiScaricati.add(ass_cdp_la);
	ass_cdp_la.setMese(new Integer(getMese()));
	return costiScaricati.size()-1;
}
/**
 * Aggiunge una specifica di scarico CDP su altra UO alla lista degli scarichi verso altra UO
 *
 * @param ass_cdp_uo specifica di scarico CDP su altra UO
 * @return ritorna l'indice dell'elemento aggiunto nella lista degli scarichi verso altra UO
 */
public int addToCostiScaricatiAltraUO(Ass_cdp_uoBulk ass_cdp_uo) {
	costiScaricatiAltraUO.add(ass_cdp_uo);
	ass_cdp_uo.setMese(new Integer(getMese()));
	return costiScaricatiAltraUO.size()-1;
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costoCaricato'
 *
 * @return Il valore della proprietà 'costoCaricato'
 */
public Ass_cdp_uoBulk getCostoCaricato() {
	return costoCaricato;
}
/**
 * <!-- @TODO: da completare -->
 * Imposta il valore della proprietà 'costoCaricato'
 *
 * @param newCostoCaricato	Il valore da assegnare a 'costoCaricato'
 */
public void setCostoCaricato(Ass_cdp_uoBulk newCostoCaricato) {
	costoCaricato = newCostoCaricato;
}
public java.math.BigDecimal getGiorni_lavorativi(int i) {
	switch(i) {
		case 1 : return giorni_lavorativi_a1;
		case 2 : return giorni_lavorativi_a2;
		case 3 : return giorni_lavorativi_a3;
	}
	return null;
}
/**
 * Imposta i giorni lavorativi per l'anno i
 *
 * @param i	indice dell'anno (1,2,3)
 * @param giorni_lavorativi	giorni da impostare
 */
public void setGiorni_lavorativi(int i,java.math.BigDecimal giorni_lavorativi) {
	switch(i) {
		case 1 : giorni_lavorativi_a1 = giorni_lavorativi; break;
		case 2 : giorni_lavorativi_a2 = giorni_lavorativi; break;
		case 3 : giorni_lavorativi_a3 = giorni_lavorativi; break;
	}
}
public java.math.BigDecimal getGiorni_lavorativi_a1() {
	return giorni_lavorativi_a1;
}
public void setGiorni_lavorativi_a1(java.math.BigDecimal giorni_lavorativi_a1) {
	this.giorni_lavorativi_a1 = giorni_lavorativi_a1;
}
public java.math.BigDecimal getGiorni_lavorativi_a2() {
	return giorni_lavorativi_a2;
}
public void setGiorni_lavorativi_a2(java.math.BigDecimal giorni_lavorativi_a2) {
	this.giorni_lavorativi_a2 = giorni_lavorativi_a2;
}
public java.math.BigDecimal getGiorni_lavorativi_a3() {
	return giorni_lavorativi_a3;
}
public void setGiorni_lavorativi_a3(java.math.BigDecimal giorni_lavorativi_a3) {
	this.giorni_lavorativi_a3 = giorni_lavorativi_a3;
}
public java.math.BigDecimal getTotale_prc_la_a1() {
	return totale_prc_la_a1;
}
public void setTotale_prc_la_a1(java.math.BigDecimal totale_prc_la_a1) {
	this.totale_prc_la_a1 = totale_prc_la_a1;
}
public java.math.BigDecimal getTotale_prc_la_a2() {
	return totale_prc_la_a2;
}
public void setTotale_prc_la_a2(java.math.BigDecimal totale_prc_la_a2) {
	this.totale_prc_la_a2 = totale_prc_la_a2;
}
public java.math.BigDecimal getTotale_prc_la_a3() {
	return totale_prc_la_a3;
}
public void setTotale_prc_la_a3(java.math.BigDecimal totale_prc_la_a3) {
	this.totale_prc_la_a3 = totale_prc_la_a3;
}
/**
 * Imposta i giorni lavorativi l'anno i sulla base della percentuale
 *
 * @param i	indice dell'anno (1,2,3)
 * @param prc percentuale
*/
public void calcolaGiorni_lavorativi(int i,java.math.BigDecimal prc) {
	setGiorni_lavorativi(i,
		getGiorni_lavorativi(i).multiply(prc).divide(
		java.math.BigDecimal.valueOf(100),2,java.math.BigDecimal.ROUND_HALF_UP));
}
public java.math.BigDecimal getTotale_prc_ripartito_a1() {
	BigDecimal totale_prc = BigDecimal.ZERO;
	if (this.getCostiScaricati()!=null)
		for (Iterator iterator = this.getCostiScaricati().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_laBulk)iterator.next()).getPrc_la_a1()));
	if (this.getCostiScaricatiAltraUO()!=null)
		for (Iterator iterator = this.getCostiScaricatiAltraUO().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_uoBulk)iterator.next()).getPrc_uo_a1()));
	return totale_prc;
}
public java.math.BigDecimal getTotale_prc_ripartito_a2() {
	BigDecimal totale_prc = BigDecimal.ZERO;
	if (this.getCostiScaricati()!=null)
		for (Iterator iterator = this.getCostiScaricati().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_laBulk)iterator.next()).getPrc_la_a2()));
	if (this.getCostiScaricatiAltraUO()!=null)
		for (Iterator iterator = this.getCostiScaricatiAltraUO().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_uoBulk)iterator.next()).getPrc_uo_a2()));
	return totale_prc;
}
public java.math.BigDecimal getTotale_prc_ripartito_a3() {
	BigDecimal totale_prc = BigDecimal.ZERO;
	if (this.getCostiScaricati()!=null)
		for (Iterator iterator = this.getCostiScaricati().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_laBulk)iterator.next()).getPrc_la_a3()));
	if (this.getCostiScaricatiAltraUO()!=null)
		for (Iterator iterator = this.getCostiScaricatiAltraUO().iterator(); iterator.hasNext();)
			totale_prc = totale_prc.add(Utility.nvl(((Ass_cdp_uoBulk)iterator.next()).getPrc_uo_a3()));
	return totale_prc;
}
}
