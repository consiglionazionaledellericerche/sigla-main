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
 * Created on Oct 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.bulk;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * @author 
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Stampa_situazione_analitica_x_GAEBulk extends OggettoBulk {

	private String cd_cds;
	private Integer esercizio;
	private it.cnr.contab.config00.sto.bulk.CdsBulk cdsForPrint;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrForPrint;
	private boolean cdsForPrintEnabled;
	private boolean uoForPrintEnabled;
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdrUtente;
	private int livello_Responsabilita;
	private boolean isCdrForPrintEnabled;
	private Unita_organizzativaBulk uoForPrint;
	
	// Livelli relativi al CdR utente
	public static final int LV_AC   = 0; // Amministazione centrale
	public static final int LV_CDRI = 1; // CDR I 00
	public static final int LV_RUO  = 2; // CDR II responsabile
	public static final int LV_NRUO = 3; // CDR II non responsabile

//	Tipo di stampa
	private String ti_etr_spe;
	
	public final static String TIPO_ENTRATA = "E";
	public final static String TIPO_SPESA = "S";

	public final static java.util.Dictionary ti_etr_speKeys;
	
	static {		
		ti_etr_speKeys = new it.cnr.jada.util.OrderedHashtable();
		ti_etr_speKeys.put(TIPO_ENTRATA,"Entrata");
		ti_etr_speKeys.put(TIPO_SPESA,"Spesa");
	};	
	
		public Stampa_situazione_analitica_x_GAEBulk() {
		super();
	}
	
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer newEsercizio) {
		esercizio = newEsercizio;
	}
		
	public it.cnr.contab.config00.sto.bulk.CdsBulk getCdsForPrint() {
		return cdsForPrint;
	}

	public void setCdsForPrint(it.cnr.contab.config00.sto.bulk.CdsBulk bulk) {
		cdsForPrint = bulk;
	}
	
	public String getCdCdsForPrint() {

		if (getCdsForPrint()==null)
			return "*";
		if (getCdsForPrint().getCd_unita_organizzativa()==null)
			return "*";

		return getCdsForPrint().getCd_unita_organizzativa().toString();
	}
	
	public void setCdsForPrintEnabled(boolean b) {
		cdsForPrintEnabled = b;
	}

	public boolean isCdsForPrintEnabled() {
		return !cdsForPrintEnabled;
	}
	
	public void setUoForPrintEnabled(boolean b) {
		uoForPrintEnabled = b;
	}

	public boolean isUoForPrintEnabled() {
		return uoForPrintEnabled;
	}
	
	public String getCdCdrForPrint() {

		// Se non è stata specificato un  CdR
		if ( (getCdrForPrint()==null) ||
				(getCdrForPrint().getCd_centro_responsabilita()==null) ){


			if (LV_NRUO==getLivello_Responsabilita()){
				// Restituisco il CdR dell'Utente loggato
				return getCdrUtente().getCd_centro_responsabilita().toString();
			} else{
				return "*";
			}
					
		}

		return getCdrForPrint().getCd_centro_responsabilita().toString();

	}
	
	public String getCdUoForPrint() {
		if (getUoForPrint()==null)
			return "*";
		return getUoForPrint().getCd_unita_organizzativa();
	}
	
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrForPrint() {
		return cdrForPrint;
	}
	
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdrUtente() {
		return cdrUtente;
	}
	
	public boolean isROCdrForPrint() {
		return getCdrForPrint()==null || getCdrForPrint().getCrudStatus()==NORMAL;
	}

	public boolean isROCdsForPrint() {
		return getCdsForPrint()==null || getCdsForPrint().getCrudStatus()==NORMAL;
	}
	
	public boolean isROUoForPrint() {
		return getUoForPrint()==null || getUoForPrint().getCrudStatus()==NORMAL;
	}
	
	public void setCdrForPrint(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrForPrint) {
		cdrForPrint = newCdrForPrint;
	}
	
	public void setCdrUtente(it.cnr.contab.config00.sto.bulk.CdrBulk newCdrUtente) {
		cdrUtente = newCdrUtente;
	}
	
	public int getLivello_Responsabilita() {
		return livello_Responsabilita;
	}
	
	public void setLivello_Responsabilita(int newLivello_Responsabilita) {
		livello_Responsabilita = newLivello_Responsabilita;
	}
	
	public boolean isCdrForPrintEnabled() {
		return isCdrForPrintEnabled;
	}
	
	public String getTi_etr_spe() {
		return ti_etr_spe;
	}

	public void setTi_etr_spe(String string) {
		ti_etr_spe = string;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;	
	}
	public void setCd_cds(java.lang.String newCd_cds) {
		cd_cds = newCd_cds;
	}


	public Unita_organizzativaBulk getUoForPrint() {
		return uoForPrint;
	}


	public void setUoForPrint(Unita_organizzativaBulk uoForPrint) {
		this.uoForPrint = uoForPrint;
	}
}
