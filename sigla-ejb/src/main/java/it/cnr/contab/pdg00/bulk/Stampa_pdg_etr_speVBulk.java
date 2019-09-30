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

/*
 * Created on Apr 22, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_pdg_etr_speVBulk extends OggettoBulk {
	// Esercizio di scrivania
	private Integer esercizio;

	//Tipo di stampa
	private String ti_etr_spe;
	
	public final static String TIPO_ENTRATA = "E";
	public final static String TIPO_SPESA = "S";

	public final static java.util.Dictionary ti_etr_speKeys;
	
	static {		
		ti_etr_speKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_etr_speKeys.put(TIPO_ENTRATA,"Entrata");
		ti_etr_speKeys.put(TIPO_SPESA,"Spesa");
	};	

	// Il Cds di scrivania
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint = new it.cnr.contab.config00.sto.bulk.CdsBulk();
	private boolean cdsForPrintEnabled;
		
	// Uo di stampa
	private it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uoForPrint = new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk();
	private boolean uoForPrintEnabled;

    //Elemento voce
    private it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk elementoVoceForPrint = new it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk();
	private boolean elementoVoceForPrintEnabled;
    
    // Check per i raggruppamenti
    private Boolean ragrr_istituto;
	private Boolean ragrr_uo;
	private Boolean ragrr_dipartimento;
	private Boolean ragrr_progetto;
	private Boolean ragrr_commessa;
	private Boolean ragrr_modulo;
	private Boolean ragrr_titolo;
	private Boolean ragrr_categoria;
	private Boolean ragrr_natura;
	private Boolean ragrr_elemento_voce; 
     
	/**
	 * 
	 */
	public Stampa_pdg_etr_speVBulk() {
		super();
	}
	public boolean isROCheckBox(){
		return true;
	}
	/**
	 * Inizializza gli attributi di ragruppamento
	 * @param bp business process corrente
	 * @param context contesto dell'Action che e' stata generata
	 * @return OggettoBulk Stampa_pdg_etr_speVBulk con i falg inizializzati
	 */
	public void inizializzaRagruppamenti() {
		setRagrr_istituto(new Boolean(false));
		setRagrr_uo(new Boolean(false));
		setRagrr_dipartimento(new Boolean(true));
		setRagrr_progetto(new Boolean(false));
		setRagrr_commessa(new Boolean(false));
		setRagrr_modulo(new Boolean(false));
		setRagrr_titolo(new Boolean(false));
		setRagrr_categoria(new Boolean(false));
		setRagrr_natura(new Boolean(false));
		setRagrr_elemento_voce(new Boolean(false));
	}
	public void selezionaRagruppamenti(){
		setRagrr_istituto(new Boolean(!getRagrr_istituto().booleanValue()));
		setRagrr_uo(new Boolean(!getRagrr_uo().booleanValue()));
		//setRagrr_dipartimento(new Boolean(!getRagrr_dipartimento().booleanValue()));
		setRagrr_progetto(new Boolean(!getRagrr_progetto().booleanValue()));
		setRagrr_commessa(new Boolean(!getRagrr_commessa().booleanValue()));
		setRagrr_modulo(new Boolean(!getRagrr_modulo().booleanValue()));
		setRagrr_titolo(new Boolean(!getRagrr_titolo().booleanValue()));
		setRagrr_categoria(new Boolean(!getRagrr_categoria().booleanValue()));
		setRagrr_natura(new Boolean(!getRagrr_natura().booleanValue()));
		setRagrr_elemento_voce(new Boolean(!getRagrr_elemento_voce().booleanValue()));	
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.lang.String
	 */
	public String getCdUOCRForPrint() {

		if (getUoForPrint()==null)
			return "*";
		if (getUoForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getUoForPrint().getCd_unita_organizzativa().toString();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/12/2002 10.47.40)
	 * @return boolean
	 */
	public boolean isROUoForPrint() {
		return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/12/2002 10.47.40)
	 * @return boolean
	 */
	public boolean isROCdsForPrint() {
		return getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @return boolean
	 */
	public boolean isUoForPrintEnabled() {
		return !uoForPrintEnabled;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newEsercizio java.lang.Integer
	 */
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrint it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk
	 */
	public void setUoForPrint(it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk newUoForPrint) {
		uoForPrint = newUoForPrint;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.30.55)
	 * @param newUoForPrintEnabled boolean
	 */
	public void setUoForPrintEnabled(boolean newUoForPrintEnabled) {
		uoForPrintEnabled = newUoForPrintEnabled;
	}
	/**
	 * @return
	 */
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}

	/**
	 * @param bulk
	 */
	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
		cdsForPrint = bulk;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (20/01/2003 16.50.12)
	 * @return java.lang.String
	 */
	public String getCdCDSCRForPrint() {

		if (getCdsForPrint()==null)
			return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getCdsForPrint().getCd_unita_organizzativa().toString();
	}
	/**
	 * @param b
	 */
	public void setCdsForPrintEnabled(boolean b) {
		cdsForPrintEnabled = b;
	}

	/**
	 * @return
	 */
	public boolean isCdsForPrintEnabled() {
		return !cdsForPrintEnabled;
	}		
	/**
	 * @return
	 */
	public String getTi_etr_spe() {
		return ti_etr_spe;
	}

	/**
	 * @param string
	 */
	public void setTi_etr_spe(String string) {
		ti_etr_spe = string;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (27/08/2004 17.31.30)
	 * @return java.util.Dictionary
	 */
	public final static java.util.Dictionary getTi_etr_speKeys() {
		return ti_etr_speKeys;
	}
	
	/**
	 * @return
	 */
	public it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk getElementoVoceForPrint() {
		return elementoVoceForPrint;
	}
	/**
	 * @return
	 */
	public String getCd_elementoVoceCRForPrint() {
		if (getElementoVoceForPrint()==null)
			return "*";
		if (getElementoVoceForPrint().getCd_elemento_voce()==null)
			return "*";
		return getElementoVoceForPrint().getCd_elemento_voce().toString();
	}	

	/**
	 * @return
	 */
	public boolean isElementoVoceForPrintEnabled() {
		return elementoVoceForPrintEnabled;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_categoria() {
		return ragrr_categoria;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_commessa() {
		return ragrr_commessa;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_dipartimento() {
		return ragrr_dipartimento;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_elemento_voce() {
		return ragrr_elemento_voce;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_istituto() {
		return ragrr_istituto;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_modulo() {
		return ragrr_modulo;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_natura() {
		return ragrr_natura;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_progetto() {
		return ragrr_progetto;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_titolo() {
		return ragrr_titolo;
	}

	/**
	 * @return
	 */
	public Boolean getRagrr_uo() {
		return ragrr_uo;
	}

	/**
	 * @param bulk
	 */
	public void setElementoVoceForPrint(
		it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk bulk) {
		elementoVoceForPrint = bulk;
	}

	/**
	 * @param b
	 */
	public void setElementoVoceForPrintEnabled(boolean b) {
		elementoVoceForPrintEnabled = b;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_categoria(Boolean boolean1) {
		ragrr_categoria = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_commessa(Boolean boolean1) {
		ragrr_commessa = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_dipartimento(Boolean boolean1) {
		ragrr_dipartimento = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_elemento_voce(Boolean boolean1) {
		ragrr_elemento_voce = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_istituto(Boolean boolean1) {
		ragrr_istituto = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_modulo(Boolean boolean1) {
		ragrr_modulo = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_natura(Boolean boolean1) {
		ragrr_natura = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_progetto(Boolean boolean1) {
		ragrr_progetto = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_titolo(Boolean boolean1) {
		ragrr_titolo = boolean1;
	}

	/**
	 * @param boolean1
	 */
	public void setRagrr_uo(Boolean boolean1) {
		ragrr_uo = boolean1;
	}

}
