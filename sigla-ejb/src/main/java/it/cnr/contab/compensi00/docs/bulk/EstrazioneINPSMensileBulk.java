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

/*import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;

/**
 * Insert the type's description here.
 * Creation date: (21/03/2005 15.00.00)
 * @author: Tilde
 */
public class EstrazioneINPSMensileBulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ID_REPORT
	private java.math.BigDecimal id_report;
	
	//	ANAGRAFICO
/*	private AnagraficoBulk anagrafico;*/

    private java.lang.Integer mese;

    private static final java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
	public static final int GENNAIO = 1;
	public static final int FEBBRAIO = 2;
	public static final int MARZO = 3;
	public static final int APRILE = 4;
	public static final int MAGGIO = 5;
	public static final int GIUGNO = 6;
	public static final int LUGLIO = 7;
	public static final int AGOSTO = 8;
	public static final int SETTEMBRE = 9;
	public static final int OTTOBRE = 10;
	public static final int NOVEMBRE = 11;
	public static final int DICEMBRE = 12;

	static {
		meseKeys.put(new Integer(GENNAIO),"Gennaio");
		meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
		meseKeys.put(new Integer(MARZO),"Marzo");
		meseKeys.put(new Integer(APRILE),"Aprile");
		meseKeys.put(new Integer(MAGGIO),"Maggio");
		meseKeys.put(new Integer(GIUGNO),"Giugno");
		meseKeys.put(new Integer(LUGLIO),"Luglio");
		meseKeys.put(new Integer(AGOSTO),"Agosto");
		meseKeys.put(new Integer(SETTEMBRE),"Settembre");
		meseKeys.put(new Integer(OTTOBRE),"Ottobre");
		meseKeys.put(new Integer(NOVEMBRE),"Novembre");
		meseKeys.put(new Integer(DICEMBRE),"Dicembre");
	}
	
	/**
	 * EstrazioneCUDVBulk constructor comment.
	 */     
public EstrazioneINPSMensileBulk() {
	super();
}
/**
 * Insert the method's description here.
 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
/*
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagrafico() {
	return anagrafico;
}*/
/**
 * Restituisce il cd_anagrafico selezionato per l'elaborazione.
 *	Se Anagrafico == null restituisce %, ad indicare tutte le anagrafiche.
 * 
 * @return String
 */
/*
public String getCdAnagParameter() {

	if (getAnagrafico()==null)
		return "%";
	if (getAnagrafico().getCd_anag()==null)
		return "%";

	return getAnagrafico().getCd_anag().toString();
}

public boolean isROAnagrafico(){
	return anagrafico == null || anagrafico.getCrudStatus() == NORMAL;
}*/
/**
 * Insert the method's description here.
 * Creation date: (02/02/2004 12.40.57)
 * @param newAnagrafico it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
/*
public void setAnagrafico(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagrafico) {
	anagrafico = newAnagrafico;
}*/

/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @return java.math.BigDecimal
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (16/03/2004 17.55.54)
 * @param newId_report java.math.BigDecimal
 */
public void setId_report(java.math.BigDecimal newId_report) {
	id_report = newId_report;
}
/**
 * @return
 */

public java.util.Dictionary getMeseKeys() {
	return meseKeys;
}

	/**
	 * @return
	 */
	public java.lang.Integer getMese() {
		return mese;
	}

	/**
	 * @param integer
	 */
	public void setMese(java.lang.Integer integer) {
		mese = integer;
	}

}
