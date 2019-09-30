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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 11/01/2010
 */
package it.cnr.contab.segnalazioni00.bulk;
import it.cnr.jada.persistency.Keyed;
public class ConfrontoSiglaDwhBase extends ConfrontoSiglaDwhKey implements Keyed {
//    DT_ELABORAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_elaborazione;
 
//    PG_ELABORAZIONE DECIMAL(22,0)
	private java.lang.Long pg_elaborazione;
 
//    PG_RIGA DECIMAL(22,0)
	private java.lang.Long pg_riga;
 
//    ESERCIZIO DECIMAL(22,0)
	private java.lang.Long esercizio;
 
//    ESERCIZIO_RES DECIMAL(22,0)
	private java.lang.Long esercizio_res;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    IM_STANZ_INIZIALE_A1_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_iniziale_a1_sigla;
 
//    VARIAZIONI_PIU_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal variazioni_piu_sigla;
 
//    VARIAZIONI_MENO_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal variazioni_meno_sigla;
 
//    IM_OBBL_ACC_COMP_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_acc_comp_sigla;
 
//    IM_STANZ_RES_IMPROPRIO_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_res_improprio_sigla;
 
//    VAR_PIU_STANZ_RES_IMP_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal var_piu_stanz_res_imp_sigla;
 
//    VAR_MENO_STANZ_RES_IMP_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal var_meno_stanz_res_imp_sigla;
 
//    IM_OBBL_RES_IMP_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_res_imp_sigla;
 
//    IM_OBBL_RES_PRO_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_res_pro_sigla;
 
//    VAR_PIU_OBBL_RES_PRO_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal var_piu_obbl_res_pro_sigla;
 
//    VAR_MENO_OBBL_RES_PRO_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal var_meno_obbl_res_pro_sigla;
 
//    IM_MANDATI_REVERSALI_SIGLA DECIMAL(15,2)
	private java.math.BigDecimal im_mandati_reversali_sigla;
 
//    IM_STANZ_INIZIALE_A1_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_iniziale_a1_dwh;
 
//    VARIAZIONI_PIU_DWH DECIMAL(15,2)
	private java.math.BigDecimal variazioni_piu_dwh;
 
//    VARIAZIONI_MENO_DWH DECIMAL(15,2)
	private java.math.BigDecimal variazioni_meno_dwh;
 
//    IM_OBBL_ACC_COMP_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_acc_comp_dwh;
 
//    IM_STANZ_RES_IMPROPRIO_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_stanz_res_improprio_dwh;
 
//    VAR_PIU_STANZ_RES_IMP_DWH DECIMAL(15,2)
	private java.math.BigDecimal var_piu_stanz_res_imp_dwh;
 
//    VAR_MENO_STANZ_RES_IMP_DWH DECIMAL(15,2)
	private java.math.BigDecimal var_meno_stanz_res_imp_dwh;
 
//    IM_OBBL_RES_IMP_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_res_imp_dwh;
 
//    IM_OBBL_RES_PRO_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_obbl_res_pro_dwh;
 
//    VAR_PIU_OBBL_RES_PRO_DWH DECIMAL(15,2)
	private java.math.BigDecimal var_piu_obbl_res_pro_dwh;
 
//    VAR_MENO_OBBL_RES_PRO_DWH DECIMAL(15,2)
	private java.math.BigDecimal var_meno_obbl_res_pro_dwh;
 
//    IM_MANDATI_REVERSALI_DWH DECIMAL(15,2)
	private java.math.BigDecimal im_mandati_reversali_dwh;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: CONFRONTO_SIGLA_DWH
	 **/
	public ConfrontoSiglaDwhBase() {
		super();
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dt_elaborazione]
	 **/
	public java.sql.Timestamp getDt_elaborazione() {
		return dt_elaborazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dt_elaborazione]
	 **/
	public void setDt_elaborazione(java.sql.Timestamp dt_elaborazione)  {
		this.dt_elaborazione=dt_elaborazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pg_elaborazione]
	 **/
	public java.lang.Long getPg_elaborazione() {
		return pg_elaborazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pg_elaborazione]
	 **/
	public void setPg_elaborazione(java.lang.Long pg_elaborazione)  {
		this.pg_elaborazione=pg_elaborazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pg_riga]
	 **/
	public java.lang.Long getPg_riga() {
		return pg_riga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pg_riga]
	 **/
	public void setPg_riga(java.lang.Long pg_riga)  {
		this.pg_riga=pg_riga;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Long getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Long esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio_res]
	 **/
	public java.lang.Long getEsercizio_res() {
		return esercizio_res;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio_res]
	 **/
	public void setEsercizio_res(java.lang.Long esercizio_res)  {
		this.esercizio_res=esercizio_res;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_centro_responsabilita]
	 **/
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_centro_responsabilita]
	 **/
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_linea_attivita]
	 **/
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_linea_attivita]
	 **/
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [ti_gestione]
	 **/
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [ti_gestione]
	 **/
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cd_elemento_voce]
	 **/
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cd_elemento_voce]
	 **/
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_stanz_iniziale_a1_sigla]
	 **/
	public java.math.BigDecimal getIm_stanz_iniziale_a1_sigla() {
		return im_stanz_iniziale_a1_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_stanz_iniziale_a1_sigla]
	 **/
	public void setIm_stanz_iniziale_a1_sigla(java.math.BigDecimal im_stanz_iniziale_a1_sigla)  {
		this.im_stanz_iniziale_a1_sigla=im_stanz_iniziale_a1_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioni_piu_sigla]
	 **/
	public java.math.BigDecimal getVariazioni_piu_sigla() {
		return variazioni_piu_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioni_piu_sigla]
	 **/
	public void setVariazioni_piu_sigla(java.math.BigDecimal variazioni_piu_sigla)  {
		this.variazioni_piu_sigla=variazioni_piu_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioni_meno_sigla]
	 **/
	public java.math.BigDecimal getVariazioni_meno_sigla() {
		return variazioni_meno_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioni_meno_sigla]
	 **/
	public void setVariazioni_meno_sigla(java.math.BigDecimal variazioni_meno_sigla)  {
		this.variazioni_meno_sigla=variazioni_meno_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_acc_comp_sigla]
	 **/
	public java.math.BigDecimal getIm_obbl_acc_comp_sigla() {
		return im_obbl_acc_comp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_acc_comp_sigla]
	 **/
	public void setIm_obbl_acc_comp_sigla(java.math.BigDecimal im_obbl_acc_comp_sigla)  {
		this.im_obbl_acc_comp_sigla=im_obbl_acc_comp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_stanz_res_improprio_sigla]
	 **/
	public java.math.BigDecimal getIm_stanz_res_improprio_sigla() {
		return im_stanz_res_improprio_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_stanz_res_improprio_sigla]
	 **/
	public void setIm_stanz_res_improprio_sigla(java.math.BigDecimal im_stanz_res_improprio_sigla)  {
		this.im_stanz_res_improprio_sigla=im_stanz_res_improprio_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_piu_stanz_res_imp_sigla]
	 **/
	public java.math.BigDecimal getVar_piu_stanz_res_imp_sigla() {
		return var_piu_stanz_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_piu_stanz_res_imp_sigla]
	 **/
	public void setVar_piu_stanz_res_imp_sigla(java.math.BigDecimal var_piu_stanz_res_imp_sigla)  {
		this.var_piu_stanz_res_imp_sigla=var_piu_stanz_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_meno_stanz_res_imp_sigla]
	 **/
	public java.math.BigDecimal getVar_meno_stanz_res_imp_sigla() {
		return var_meno_stanz_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_meno_stanz_res_imp_sigla]
	 **/
	public void setVar_meno_stanz_res_imp_sigla(java.math.BigDecimal var_meno_stanz_res_imp_sigla)  {
		this.var_meno_stanz_res_imp_sigla=var_meno_stanz_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_res_imp_sigla]
	 **/
	public java.math.BigDecimal getIm_obbl_res_imp_sigla() {
		return im_obbl_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_res_imp_sigla]
	 **/
	public void setIm_obbl_res_imp_sigla(java.math.BigDecimal im_obbl_res_imp_sigla)  {
		this.im_obbl_res_imp_sigla=im_obbl_res_imp_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_res_pro_sigla]
	 **/
	public java.math.BigDecimal getIm_obbl_res_pro_sigla() {
		return im_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_res_pro_sigla]
	 **/
	public void setIm_obbl_res_pro_sigla(java.math.BigDecimal im_obbl_res_pro_sigla)  {
		this.im_obbl_res_pro_sigla=im_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_piu_obbl_res_pro_sigla]
	 **/
	public java.math.BigDecimal getVar_piu_obbl_res_pro_sigla() {
		return var_piu_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_piu_obbl_res_pro_sigla]
	 **/
	public void setVar_piu_obbl_res_pro_sigla(java.math.BigDecimal var_piu_obbl_res_pro_sigla)  {
		this.var_piu_obbl_res_pro_sigla=var_piu_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_meno_obbl_res_pro_sigla]
	 **/
	public java.math.BigDecimal getVar_meno_obbl_res_pro_sigla() {
		return var_meno_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_meno_obbl_res_pro_sigla]
	 **/
	public void setVar_meno_obbl_res_pro_sigla(java.math.BigDecimal var_meno_obbl_res_pro_sigla)  {
		this.var_meno_obbl_res_pro_sigla=var_meno_obbl_res_pro_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_mandati_reversali_sigla]
	 **/
	public java.math.BigDecimal getIm_mandati_reversali_sigla() {
		return im_mandati_reversali_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_mandati_reversali_sigla]
	 **/
	public void setIm_mandati_reversali_sigla(java.math.BigDecimal im_mandati_reversali_sigla)  {
		this.im_mandati_reversali_sigla=im_mandati_reversali_sigla;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_stanz_iniziale_a1_dwh]
	 **/
	public java.math.BigDecimal getIm_stanz_iniziale_a1_dwh() {
		return im_stanz_iniziale_a1_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_stanz_iniziale_a1_dwh]
	 **/
	public void setIm_stanz_iniziale_a1_dwh(java.math.BigDecimal im_stanz_iniziale_a1_dwh)  {
		this.im_stanz_iniziale_a1_dwh=im_stanz_iniziale_a1_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioni_piu_dwh]
	 **/
	public java.math.BigDecimal getVariazioni_piu_dwh() {
		return variazioni_piu_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioni_piu_dwh]
	 **/
	public void setVariazioni_piu_dwh(java.math.BigDecimal variazioni_piu_dwh)  {
		this.variazioni_piu_dwh=variazioni_piu_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [variazioni_meno_dwh]
	 **/
	public java.math.BigDecimal getVariazioni_meno_dwh() {
		return variazioni_meno_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [variazioni_meno_dwh]
	 **/
	public void setVariazioni_meno_dwh(java.math.BigDecimal variazioni_meno_dwh)  {
		this.variazioni_meno_dwh=variazioni_meno_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_acc_comp_dwh]
	 **/
	public java.math.BigDecimal getIm_obbl_acc_comp_dwh() {
		return im_obbl_acc_comp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_acc_comp_dwh]
	 **/
	public void setIm_obbl_acc_comp_dwh(java.math.BigDecimal im_obbl_acc_comp_dwh)  {
		this.im_obbl_acc_comp_dwh=im_obbl_acc_comp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_stanz_res_improprio_dwh]
	 **/
	public java.math.BigDecimal getIm_stanz_res_improprio_dwh() {
		return im_stanz_res_improprio_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_stanz_res_improprio_dwh]
	 **/
	public void setIm_stanz_res_improprio_dwh(java.math.BigDecimal im_stanz_res_improprio_dwh)  {
		this.im_stanz_res_improprio_dwh=im_stanz_res_improprio_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_piu_stanz_res_imp_dwh]
	 **/
	public java.math.BigDecimal getVar_piu_stanz_res_imp_dwh() {
		return var_piu_stanz_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_piu_stanz_res_imp_dwh]
	 **/
	public void setVar_piu_stanz_res_imp_dwh(java.math.BigDecimal var_piu_stanz_res_imp_dwh)  {
		this.var_piu_stanz_res_imp_dwh=var_piu_stanz_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_meno_stanz_res_imp_dwh]
	 **/
	public java.math.BigDecimal getVar_meno_stanz_res_imp_dwh() {
		return var_meno_stanz_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_meno_stanz_res_imp_dwh]
	 **/
	public void setVar_meno_stanz_res_imp_dwh(java.math.BigDecimal var_meno_stanz_res_imp_dwh)  {
		this.var_meno_stanz_res_imp_dwh=var_meno_stanz_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_res_imp_dwh]
	 **/
	public java.math.BigDecimal getIm_obbl_res_imp_dwh() {
		return im_obbl_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_res_imp_dwh]
	 **/
	public void setIm_obbl_res_imp_dwh(java.math.BigDecimal im_obbl_res_imp_dwh)  {
		this.im_obbl_res_imp_dwh=im_obbl_res_imp_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_obbl_res_pro_dwh]
	 **/
	public java.math.BigDecimal getIm_obbl_res_pro_dwh() {
		return im_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_obbl_res_pro_dwh]
	 **/
	public void setIm_obbl_res_pro_dwh(java.math.BigDecimal im_obbl_res_pro_dwh)  {
		this.im_obbl_res_pro_dwh=im_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_piu_obbl_res_pro_dwh]
	 **/
	public java.math.BigDecimal getVar_piu_obbl_res_pro_dwh() {
		return var_piu_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_piu_obbl_res_pro_dwh]
	 **/
	public void setVar_piu_obbl_res_pro_dwh(java.math.BigDecimal var_piu_obbl_res_pro_dwh)  {
		this.var_piu_obbl_res_pro_dwh=var_piu_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [var_meno_obbl_res_pro_dwh]
	 **/
	public java.math.BigDecimal getVar_meno_obbl_res_pro_dwh() {
		return var_meno_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [var_meno_obbl_res_pro_dwh]
	 **/
	public void setVar_meno_obbl_res_pro_dwh(java.math.BigDecimal var_meno_obbl_res_pro_dwh)  {
		this.var_meno_obbl_res_pro_dwh=var_meno_obbl_res_pro_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [im_mandati_reversali_dwh]
	 **/
	public java.math.BigDecimal getIm_mandati_reversali_dwh() {
		return im_mandati_reversali_dwh;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [im_mandati_reversali_dwh]
	 **/
	public void setIm_mandati_reversali_dwh(java.math.BigDecimal im_mandati_reversali_dwh)  {
		this.im_mandati_reversali_dwh=im_mandati_reversali_dwh;
	}
}