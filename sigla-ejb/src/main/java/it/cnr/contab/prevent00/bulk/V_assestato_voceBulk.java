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
public class V_assestato_voceBulk extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;
	
	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
	
	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

	// CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;

	// DS_VOCE VARCHAR(300) NOT NULL
	private java.lang.String ds_voce;
	
	// CD_ELEMENTO_VOCE VARCHAR2(30) NULL
	private String cd_elemento_voce;

	// IM_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_obbl_res_pro;

	// ASSESTATO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal assestato;

	// VAR_PIU_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_piu_obbl_res_pro;
    
	// VAR_MENO_OBBL_RES_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal var_meno_obbl_res_pro;

	// IM_MANDATI_REVERSALI_PRO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_mandati_reversali_pro;
	
	// IM_PAGAMENTI_INCASSI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_pagamenti_incassi;
	
	/**
	 * 
	 */
	public V_assestato_voceBulk() {
		super();
	}

	public V_assestato_voceBulk(java.lang.Integer esercizio, java.lang.Integer esercizio_res, java.lang.String cd_centro_responsabilita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce) {
		super();
		this.esercizio = esercizio;
		this.esercizio_res = esercizio_res;
		this.cd_centro_responsabilita = cd_centro_responsabilita;	
		this.ti_appartenenza = ti_appartenenza;
		this.ti_gestione = ti_gestione;
		this.cd_elemento_voce = cd_elemento_voce;
	}	
	
	public java.math.BigDecimal getAssestato() {
		return assestato;
	}

	public void setAssestato(java.math.BigDecimal assestato) {
		this.assestato = assestato;
	}

	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	public void setCd_centro_responsabilita(
			java.lang.String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	public String getCd_elemento_voce() {
		return cd_elemento_voce;
	}

	public void setCd_elemento_voce(String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}

	public java.lang.String getCd_voce() {
		return cd_voce;
	}

	public void setCd_voce(java.lang.String cd_voce) {
		this.cd_voce = cd_voce;
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}

	public void setEsercizio_res(java.lang.Integer esercizio_res) {
		this.esercizio_res = esercizio_res;
	}

	public java.math.BigDecimal getIm_mandati_reversali_pro() {
		return im_mandati_reversali_pro;
	}

	public void setIm_mandati_reversali_pro(
			java.math.BigDecimal im_mandati_reversali_pro) {
		this.im_mandati_reversali_pro = im_mandati_reversali_pro;
	}

	public java.math.BigDecimal getIm_obbl_res_pro() {
		return im_obbl_res_pro;
	}

	public void setIm_obbl_res_pro(java.math.BigDecimal im_obbl_res_pro) {
		this.im_obbl_res_pro = im_obbl_res_pro;
	}

	public java.math.BigDecimal getIm_pagamenti_incassi() {
		return im_pagamenti_incassi;
	}

	public void setIm_pagamenti_incassi(java.math.BigDecimal im_pagamenti_incassi) {
		this.im_pagamenti_incassi = im_pagamenti_incassi;
	}

	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}

	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}

	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}

	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}

	public java.math.BigDecimal getVar_meno_obbl_res_pro() {
		return var_meno_obbl_res_pro;
	}

	public void setVar_meno_obbl_res_pro(java.math.BigDecimal var_meno_obbl_res_pro) {
		this.var_meno_obbl_res_pro = var_meno_obbl_res_pro;
	}

	public java.math.BigDecimal getVar_piu_obbl_res_pro() {
		return var_piu_obbl_res_pro;
	}

	public void setVar_piu_obbl_res_pro(java.math.BigDecimal var_piu_obbl_res_pro) {
		this.var_piu_obbl_res_pro = var_piu_obbl_res_pro;
	}

	public java.lang.String getDs_voce() {
		return ds_voce;
	}

	public void setDs_voce(java.lang.String ds_voce) {
		this.ds_voce = ds_voce;
	}
}
