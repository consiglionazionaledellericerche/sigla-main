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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.jada.util.OrderedHashtable;

/**
 * Insert the type's description here.
 * Creation date: (27/01/2004 11.09.05)
 * @author: Gennaro Borriello
 */
public class StampaCertificazioneVBulk extends it.cnr.jada.bulk.OggettoBulk {

	// ESERCIZIO 
	private java.lang.Integer esercizio;

	// ANAGRAFICO
	private AnagraficoBulk anagraficoForPrint;

	// NOTE
	private String note;
	private String ti_cert;
	private String denominazione;
	//private boolean editingTi_cert; 

	private static OrderedHashtable tipoKeys;
	// Tipi di stampa
	public static final String TI_ACCONTO = "RA";
	public static final String TI_PREVIDENZIALE = "PR";
	public static final String TI_IMPOSTA = "TI";
	public static final String TI_IMPOSTA_CC = "CC";
	public static final String TI_CONTRIBUTI = "RC";
	public static final String TI_IMPOSTA_PC = "PC";
	public static final String TI_ACCONTO_PPT = "RAPPT";

	private boolean stampaRit_prev;
	private boolean stampaRit_acconto;
	private boolean stampaTit_imposta;
	private boolean stampaTit_imposta_cc;	
	private boolean stampaRit_contrib;
	private boolean stampaTit_imposta_pc;
	private boolean stampaRit_acconto_ppt;

/**
 * StampaCertificazioneVBulk constructor comment.
 */
public StampaCertificazioneVBulk() {
	super();
}

public OrderedHashtable getTipoKeys() {
	if (tipoKeys == null)
	{
		tipoKeys = new OrderedHashtable();
		tipoKeys.put("RA", "a Ritenuta d'Acconto");
		tipoKeys.put("PR", "a Ritenuta Previdenziale");	
		tipoKeys.put("TI", "a Titolo d'Imposta");
		tipoKeys.put("CC", "a Titolo d'Imposta - Co.Co.Co.");
		tipoKeys.put("RC", "per Contributi corrisposti ad imprese");
		tipoKeys.put("PC", "a Titolo d'Imposta - Premi per concorsi");
		tipoKeys.put("RAPPT", "a Ritenuta d'Acconto su somme liquidate a seguito di pignoramenti presso terzi");
	}
	return tipoKeys;
}

public static void setTipoKeys(OrderedHashtable hashtable) {
	tipoKeys = hashtable;
}

/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoForPrint() {
	return anagraficoForPrint;
}
public String getCdAnagCRParameter() {

	if (getAnagraficoForPrint()==null)
		return "%";
	if (getAnagraficoForPrint().getCd_anag()==null)
		return "%";

	return getAnagraficoForPrint().getCd_anag().toString();
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @return java.lang.String
 */
public java.lang.String getNote() {
	return note;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @return java.lang.String
 */
public java.lang.String getNoteForPrint() {

	if (getNote() != null)
		return getNote();

	return " ";
}
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 14.11.01)
 * @return java.lang.String
 */
public Integer getTc() {
	return new Integer(0);
}
/**
 * Insert the method's description here.
 * Creation date: (13/02/2003 14.11.01)
 * @return java.lang.String
 */
public String getTi_cert() {
	return ti_cert;
/*
	if (isStampaRit_acconto())
		return TI_ACCONTO;
	else if (isStampaRit_prev())
		return TI_PREVIDENZIALE;
	else if (isStampaTit_imposta())
		return TI_IMPOSTA;		
	else if (isStampaTit_imposta_cc())
		return TI_IMPOSTA_CC;
    else
    	return TI_CONTRIBUTI;
    	*/
}
public String getDenominazione() {
	if (getAnagraficoForPrint()==null)
		return null;
	if (getAnagraficoForPrint().getCd_anag()==null)
		return null;
    if (getAnagraficoForPrint().getRagione_sociale()!=null)
    	return getAnagraficoForPrint().getRagione_sociale();
    
	return getAnagraficoForPrint().getCognome()+" "+ getAnagraficoForPrint().getNome();

}
public boolean isROAnagraficoForPrint(){
	return anagraficoForPrint == null || anagraficoForPrint.getCrudStatus() == NORMAL;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @return boolean
 */
public boolean isStampaRit_acconto() {
	return stampaRit_acconto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @return boolean
 */
public boolean isStampaRit_prev() {
	return stampaRit_prev;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @return boolean
 */
public boolean isStampaTit_imposta() {
	return stampaTit_imposta;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @return boolean
 */
public boolean isStampaRit_contrib() {
	return stampaRit_contrib;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @param newAnagraficoForPrint it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
 */
public void setAnagraficoForPrint(it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk newAnagraficoForPrint) {
	anagraficoForPrint = newAnagraficoForPrint;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 11.34.50)
 * @param newNote java.lang.String
 */
public void setNote(java.lang.String newNote) {
	note = newNote;
}

/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @param newStampaRit_acconto boolean
 */
public void setStampaRit_acconto(boolean newStampaRit_acconto) {
	stampaRit_acconto = newStampaRit_acconto;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @param newStampaRit_prev boolean
 */
public void setStampaRit_prev(boolean newStampaRit_prev) {
	stampaRit_prev = newStampaRit_prev;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @param newStampaRit_prev boolean
 */
public void setStampaRit_contrib(boolean newStampaRit_contrib) {
	stampaRit_contrib = newStampaRit_contrib;
}
/**
 * Insert the method's description here.
 * Creation date: (27/01/2004 12.14.31)
 * @param newStampaTit_imposta boolean
 */
public void setStampaTit_imposta(boolean newStampaTit_imposta) {
	stampaTit_imposta = newStampaTit_imposta;
}
	/**
	 * @return
	 */
	public boolean isStampaTit_imposta_cc() {
		return stampaTit_imposta_cc;
	}

	/**
	 * @param b
	 */
	public void setStampaTit_imposta_cc(boolean b) {
		stampaTit_imposta_cc = b;
	}
	/**
	 * @param ti_cert The ti_cert to set.
	 */
	public void setTi_cert(String ti_cert) {
		this.ti_cert = ti_cert;
	}
	public boolean isStampaTit_imposta_pc() {
		return stampaTit_imposta_pc;
	}

	public void setStampaTit_imposta_pc(boolean b) {
		this.stampaTit_imposta_pc = b;
	}
	public boolean isStampaRit_acconto_ppt() {
		return stampaRit_acconto_ppt;
	}

	public void setStampaRit_acconto_ppt(boolean stampaRit_acconto_ppt) {
		this.stampaRit_acconto_ppt = stampaRit_acconto_ppt;
	}
}
