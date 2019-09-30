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
 * Created on Feb 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.persistency.Persistent;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class V_assestato_residuoBulk extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;
	
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
	
	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

	// CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;

	//	CD_ELEMENTO_VOCE VARCHAR2(30) NULL
	private String cd_elemento_voce;

	//	ASSESTATO_RES DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal assestato_res;

	//	TOT_IMP_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal tot_imp_res_imp;

	//	DISP_RES_IMP DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal disp_res_imp;
    
	//	VARIAZIONI_PROVVISORIE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_provvisorie;

	//	VARIAZIONI_DEFINITIVE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal variazioni_definitive;
	
	//	totale DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal totale_da_poter_assegnare;
	
	private java.math.BigDecimal imp_da_assegnare;
	/**
	 * 
	 */
	public V_assestato_residuoBulk() {
		super();
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	/**
	 * @return
	 */
	public java.lang.String getCd_voce() {
		return cd_voce;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}

	/**
	 * @return
	 */
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}

	/**
	 * @return
	 */
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	/**
	 * @param string
	 */
	public void setCd_centro_responsabilita(java.lang.String string) {
		cd_centro_responsabilita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_linea_attivita(java.lang.String string) {
		cd_linea_attivita = string;
	}

	/**
	 * @param string
	 */
	public void setCd_voce(java.lang.String string) {
		cd_voce = string;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio_res(java.lang.Integer integer) {
		esercizio_res = integer;
	}

	/**
	 * @param string
	 */
	public void setTi_appartenenza(java.lang.String string) {
		ti_appartenenza = string;
	}

	/**
	 * @param string
	 */
	public void setTi_gestione(java.lang.String string) {
		ti_gestione = string;
	}

	/**
	 * @return
	 */
	public String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	
	/**
	 * @param string
	 */
	public void setCd_elemento_voce(String string) {
		cd_elemento_voce = string;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getAssestato_res() {
		return assestato_res;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getDisp_res_imp() {
		return disp_res_imp;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getTot_imp_res_imp() {
		return tot_imp_res_imp;
	}

	/**
	 * @param decimal
	 */
	public void setAssestato_res(java.math.BigDecimal decimal) {
		assestato_res = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setDisp_res_imp(java.math.BigDecimal decimal) {
		disp_res_imp = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setTot_imp_res_imp(java.math.BigDecimal decimal) {
		tot_imp_res_imp = decimal;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getImp_da_assegnare() {
		return imp_da_assegnare;
	}

	/**
	 * @param decimal
	 */
	public void setImp_da_assegnare(java.math.BigDecimal decimal) {
		imp_da_assegnare = decimal;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getVariazioni_provvisorie() {
		return variazioni_provvisorie;
	}

	/**
	 * @param decimal
	 */
	public void setVariazioni_provvisorie(java.math.BigDecimal decimal) {
		variazioni_provvisorie = decimal;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getVariazioni_definitive() {
		return variazioni_definitive;
	}

	/**
	 * @param decimal
	 */
	public void setVariazioni_definitive(java.math.BigDecimal decimal) {
		variazioni_definitive = decimal;
	}

	/**
	 * @return
	 */
	public java.math.BigDecimal getTotale_da_poter_assegnare() {
		return totale_da_poter_assegnare;
	}

	public void setTotale_da_poter_assegnare(java.math.BigDecimal totale_da_poter_assegnare) {
		this.totale_da_poter_assegnare = totale_da_poter_assegnare;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof V_assestato_residuoBulk)) return false;
		V_assestato_residuoBulk k = (V_assestato_residuoBulk)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getEsercizio_res(),k.getEsercizio_res())) return false;			
		if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
		if(!compareKey(getCd_linea_attivita(),k.getCd_linea_attivita())) return false;
		if(!compareKey(getTi_appartenenza(),k.getTi_appartenenza())) return false;
		if(!compareKey(getTi_gestione(),k.getTi_gestione())) return false;
		if(!compareKey(getCd_voce(),k.getCd_voce())) return false;
		return true;		
	}
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getEsercizio_res())+
			calculateKeyHashCode(getCd_centro_responsabilita())+
			calculateKeyHashCode(getCd_linea_attivita())+
			calculateKeyHashCode(getTi_appartenenza())+
			calculateKeyHashCode(getTi_gestione()) +
		    calculateKeyHashCode(getCd_voce());
	}

}
