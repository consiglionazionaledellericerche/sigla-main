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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 07/11/2007
 */
package it.cnr.contab.doccont00.consultazioni.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_cons_gae_residui_speBulk extends OggettoBulk implements Persistent {
		
//  ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    ESERCIZIO_RES DECIMAL(4,0)
	private java.lang.Integer esercizio_res;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    PG_PROGETTO DECIMAL(10,0)
	private java.lang.Long pg_progetto;
 
//    CD_PROGETTO VARCHAR(30)
	private java.lang.String cd_progetto;
 
//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    PG_COMMESSA DECIMAL(10,0)
	private java.lang.Long pg_commessa;
 
//    CD_COMMESSA VARCHAR(30)
	private java.lang.String cd_commessa;
 
//    DS_COMMESSA VARCHAR(433)
	private java.lang.String ds_commessa;
 
//    PG_MODULO DECIMAL(10,0)
	private java.lang.Long pg_modulo;
 
//    CD_MODULO VARCHAR(30)
	private java.lang.String cd_modulo;
 
//    DS_MODULO VARCHAR(433)
	private java.lang.String ds_modulo;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;
 
//  CD_CDS VARCHAR(15)
	private java.lang.String cd_cds;

//    CD_LINEA_ATTIVITA VARCHAR(10)
	private java.lang.String cd_linea_attivita;
 
//    DS_LINEA_ATTIVITA VARCHAR(300)
	private java.lang.String ds_linea_attivita;
 
//    TI_APPARTENENZA CHAR(1)
	private java.lang.String ti_appartenenza;
 
//    TI_GESTIONE CHAR(1)
	private java.lang.String ti_gestione;
 
//    CD_ELEMENTO_VOCE VARCHAR(20)
	private java.lang.String cd_elemento_voce;
 
//    DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;
 
//    IM_STANZ_RES_IMPROPRIO DECIMAL(22,0)
	private java.lang.Long im_stanz_res_improprio;
 
//    PG_VAR_ST_RES DECIMAL(22,0)
	private java.lang.Long pg_var_st_res;
 
//    DS_VAR_ST_RES VARCHAR(300)
	private java.lang.String ds_var_st_res;
 
//    VAR_PIU_STANZ_RES_IMP DECIMAL(22,0)
	private java.math.BigDecimal var_piu_stanz_res_imp;
 
//    VAR_MENO_STANZ_RES_IMP DECIMAL(22,0)
	private java.math.BigDecimal var_meno_stanz_res_imp;
 
//    CD_CDS_OBB VARCHAR(30)
	private java.lang.String cd_cds_obb;
 
//    PG_OBBLIGAZIONE DECIMAL(22,0)
	private java.lang.Long pg_obbligazione;
 
//    PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(22,0)
	private java.lang.Long pg_obbligazione_scadenzario;
 
//    CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private java.lang.String cd_tipo_documento_cont;
 
//    DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;
 
//    IM_OBBL_RES_IMP DECIMAL(22,0)
	private java.math.BigDecimal im_obbl_res_imp;
 
//    IM_OBBL_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal im_obbl_res_pro;
 
//    PG_VAR_RES_PRO DECIMAL(22,0)
	private java.lang.Long pg_var_res_pro;
 
//    DS_VAR_RES_PRO VARCHAR(300)
	private java.lang.String ds_var_res_pro;
 
//    VAR_PIU_OBBL_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal var_piu_obbl_res_pro;
 
//    VAR_MENO_OBBL_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal var_meno_obbl_res_pro;
 
//    CD_CDS_MAN VARCHAR(30)
	private java.lang.String cd_cds_man;
 
//    PG_MANDATO DECIMAL(22,0)
	private java.lang.Long pg_mandato;
 
//    DS_MANDATO VARCHAR(300)
	private java.lang.String ds_mandato;
 
//    IM_MANDATI_REVERSALI_PRO DECIMAL(22,0)
	private java.math.BigDecimal im_mandati_reversali_pro;
 
//    IM_MANDATI_REVERSALI_IMP DECIMAL(22,0)
	private java.math.BigDecimal im_mandati_reversali_imp;
 
//  IM_STANZ_RES_IMPROPRIO+VAR_PIU_STANZ_RES_IMP-VAR_MENO_STANZ_RES_IMP=Assestato residuo improprio DECIMAL(22,0)
	private java.math.BigDecimal ass_res_imp;

//  IM_OBBL_RES_PRO - VAR_PIU_OBBL_RES_PRO + VAR_MENO_OBBL_RES_PRO = Iniziale DECIMAL(22,0)
	private java.math.BigDecimal iniziale;

//  Assestato residuo improprio - IM_OBBL_RES_IMP + VAR_MENO_OBBL_RES_PRO - VAR_PIU_OBBL_RES_PRO = Disponibiltà residui DECIMAL(22,0)
	private java.math.BigDecimal disp_res;

//  IM_OBBL_RES_IMP + IM_OBBL_RES_PRO - IM_MANDATI_REVERSALI_IMP -IM_MANDATI_REVERSALI_PRO= Rimasti da Pagare  DECIMAL(22,0)
	private java.math.BigDecimal rimasti_da_pagare;

//  IM_OBBL_RES_IMP - IM_MANDATI_REVERSALI_IMP = Rimasti da Pagare (Impropri) DECIMAL(22,0)
	private java.math.BigDecimal rimasti_da_pagare_imp;

//  IM_OBBL_RES_PRO - IM_MANDATI_REVERSALI_PRO = Rimasti da Pagare (Propri) DECIMAL(22,0)
	private java.math.BigDecimal rimasti_da_pagare_pro;

//  IM_MANDATI_REVERSALI_PRO + IM_MANDATI_REVERSALI_IMP = Pagato Residuo Totale DECIMAL(22,0)
	private java.math.BigDecimal pagato_totale;

	
	public V_cons_gae_residui_speBulk() {
		super();
	}
	
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}
	public void setEsercizio_res(java.lang.Integer esercizio_res)  {
		this.esercizio_res=esercizio_res;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.Long getPg_progetto() {
		return pg_progetto;
	}
	public void setPg_progetto(java.lang.Long pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.String getCd_progetto() {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto() {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.Long getPg_commessa() {
		return pg_commessa;
	}
	public void setPg_commessa(java.lang.Long pg_commessa)  {
		this.pg_commessa=pg_commessa;
	}
	public java.lang.String getCd_commessa() {
		return cd_commessa;
	}
	public void setCd_commessa(java.lang.String cd_commessa)  {
		this.cd_commessa=cd_commessa;
	}
	public java.lang.String getDs_commessa() {
		return ds_commessa;
	}
	public void setDs_commessa(java.lang.String ds_commessa)  {
		this.ds_commessa=ds_commessa;
	}
	public java.lang.Long getPg_modulo() {
		return pg_modulo;
	}
	public void setPg_modulo(java.lang.Long pg_modulo)  {
		this.pg_modulo=pg_modulo;
	}
	public java.lang.String getCd_modulo() {
		return cd_modulo;
	}
	public void setCd_modulo(java.lang.String cd_modulo)  {
		this.cd_modulo=cd_modulo;
	}
	public java.lang.String getDs_modulo() {
		return ds_modulo;
	}
	public void setDs_modulo(java.lang.String ds_modulo)  {
		this.ds_modulo=ds_modulo;
	}
	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}
	public void setCd_linea_attivita(java.lang.String cd_linea_attivita)  {
		this.cd_linea_attivita=cd_linea_attivita;
	}
	public java.lang.String getDs_linea_attivita() {
		return ds_linea_attivita;
	}
	public void setDs_linea_attivita(java.lang.String ds_linea_attivita)  {
		this.ds_linea_attivita=ds_linea_attivita;
	}
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza)  {
		this.ti_appartenenza=ti_appartenenza;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.lang.String getDs_elemento_voce() {
		return ds_elemento_voce;
	}
	public void setDs_elemento_voce(java.lang.String ds_elemento_voce)  {
		this.ds_elemento_voce=ds_elemento_voce;
	}
	public java.lang.Long getIm_stanz_res_improprio() {
		return im_stanz_res_improprio;
	}
	public void setIm_stanz_res_improprio(java.lang.Long im_stanz_res_improprio)  {
		this.im_stanz_res_improprio=im_stanz_res_improprio;
	}
	public java.lang.Long getPg_var_st_res() {
		return pg_var_st_res;
	}
	public void setPg_var_st_res(java.lang.Long pg_var_st_res)  {
		this.pg_var_st_res=pg_var_st_res;
	}
	public java.lang.String getDs_var_st_res() {
		return ds_var_st_res;
	}
	public void setDs_var_st_res(java.lang.String ds_var_st_res)  {
		this.ds_var_st_res=ds_var_st_res;
	}
	public java.math.BigDecimal getVar_piu_stanz_res_imp() {
		return var_piu_stanz_res_imp;
	}
	public void setVar_piu_stanz_res_imp(java.math.BigDecimal var_piu_stanz_res_imp)  {
		this.var_piu_stanz_res_imp=var_piu_stanz_res_imp;
	}
	public java.math.BigDecimal getVar_meno_stanz_res_imp() {
		return var_meno_stanz_res_imp;
	}
	public void setVar_meno_stanz_res_imp(java.math.BigDecimal var_meno_stanz_res_imp)  {
		this.var_meno_stanz_res_imp=var_meno_stanz_res_imp;
	}
	public java.lang.String getCd_cds_obb() {
		return cd_cds_obb;
	}
	public void setCd_cds_obb(java.lang.String cd_cds_obb)  {
		this.cd_cds_obb=cd_cds_obb;
	}
	public java.lang.Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(java.lang.Long pg_obbligazione)  {
		this.pg_obbligazione=pg_obbligazione;
	}
	public java.lang.Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario)  {
		this.pg_obbligazione_scadenzario=pg_obbligazione_scadenzario;
	}
	public java.lang.String getCd_tipo_documento_cont() {
		return cd_tipo_documento_cont;
	}
	public void setCd_tipo_documento_cont(java.lang.String cd_tipo_documento_cont)  {
		this.cd_tipo_documento_cont=cd_tipo_documento_cont;
	}
	public java.lang.String getDs_scadenza() {
		return ds_scadenza;
	}
	public void setDs_scadenza(java.lang.String ds_scadenza)  {
		this.ds_scadenza=ds_scadenza;
	}
	public java.math.BigDecimal getIm_obbl_res_imp() {
		return im_obbl_res_imp;
	}
	public void setIm_obbl_res_imp(java.math.BigDecimal im_obbl_res_imp)  {
		this.im_obbl_res_imp=im_obbl_res_imp;
	}
	public java.math.BigDecimal getIm_obbl_res_pro() {
		return im_obbl_res_pro;
	}
	public void setIm_obbl_res_pro(java.math.BigDecimal im_obbl_res_pro)  {
		this.im_obbl_res_pro=im_obbl_res_pro;
	}
	public java.lang.Long getPg_var_res_pro() {
		return pg_var_res_pro;
	}
	public void setPg_var_res_pro(java.lang.Long pg_var_res_pro)  {
		this.pg_var_res_pro=pg_var_res_pro;
	}
	public java.lang.String getDs_var_res_pro() {
		return ds_var_res_pro;
	}
	public void setDs_var_res_pro(java.lang.String ds_var_res_pro)  {
		this.ds_var_res_pro=ds_var_res_pro;
	}
	public java.math.BigDecimal getVar_piu_obbl_res_pro() {
		return var_piu_obbl_res_pro;
	}
	public void setVar_piu_obbl_res_pro(java.math.BigDecimal var_piu_obbl_res_pro)  {
		this.var_piu_obbl_res_pro=var_piu_obbl_res_pro;
	}
	public java.math.BigDecimal getVar_meno_obbl_res_pro() {
		return var_meno_obbl_res_pro;
	}
	public void setVar_meno_obbl_res_pro(java.math.BigDecimal var_meno_obbl_res_pro)  {
		this.var_meno_obbl_res_pro=var_meno_obbl_res_pro;
	}
	public java.lang.String getCd_cds_man() {
		return cd_cds_man;
	}
	public void setCd_cds_man(java.lang.String cd_cds_man)  {
		this.cd_cds_man=cd_cds_man;
	}
	public java.lang.Long getPg_mandato() {
		return pg_mandato;
	}
	public void setPg_mandato(java.lang.Long pg_mandato)  {
		this.pg_mandato=pg_mandato;
	}
	public java.lang.String getDs_mandato() {
		return ds_mandato;
	}
	public void setDs_mandato(java.lang.String ds_mandato)  {
		this.ds_mandato=ds_mandato;
	}
	public java.math.BigDecimal getIm_mandati_reversali_pro() {
		return im_mandati_reversali_pro;
	}
	public void setIm_mandati_reversali_pro(java.math.BigDecimal im_mandati_reversali_pro)  {
		this.im_mandati_reversali_pro=im_mandati_reversali_pro;
	}
	public java.math.BigDecimal getIm_mandati_reversali_imp() {
		return im_mandati_reversali_imp;
	}
	public void setIm_mandati_reversali_imp(java.math.BigDecimal im_mandati_reversali_imp)  {
		this.im_mandati_reversali_imp=im_mandati_reversali_imp;
	}


	public java.math.BigDecimal getAss_res_imp() {
		return ass_res_imp;
	}


	public void setAss_res_imp(java.math.BigDecimal ass_res_imp) {
		this.ass_res_imp = ass_res_imp;
	}


	public java.math.BigDecimal getDisp_res() {
		return disp_res;
	}


	public void setDisp_res(java.math.BigDecimal disp_res) {
		this.disp_res = disp_res;
	}


	public java.math.BigDecimal getPagato_totale() {
		return pagato_totale;
	}


	public void setPagato_totale(java.math.BigDecimal pagato_totale) {
		this.pagato_totale = pagato_totale;
	}


	public java.math.BigDecimal getRimasti_da_pagare() {
		return rimasti_da_pagare;
	}


	public void setRimasti_da_pagare(java.math.BigDecimal rimasti_da_pagare) {
		this.rimasti_da_pagare = rimasti_da_pagare;
	}


	public java.math.BigDecimal getIniziale() {
		return iniziale;
	}


	public void setIniziale(java.math.BigDecimal iniziale) {
		this.iniziale = iniziale;
	}


	public java.math.BigDecimal getRimasti_da_pagare_imp() {
		return rimasti_da_pagare_imp;
	}


	public void setRimasti_da_pagare_imp(java.math.BigDecimal rimasti_da_pagare_imp) {
		this.rimasti_da_pagare_imp = rimasti_da_pagare_imp;
	}


	public java.math.BigDecimal getRimasti_da_pagare_pro() {
		return rimasti_da_pagare_pro;
	}


	public void setRimasti_da_pagare_pro(java.math.BigDecimal rimasti_da_pagare_pro) {
		this.rimasti_da_pagare_pro = rimasti_da_pagare_pro;
	}


	public java.lang.String getCd_cds() {
		return cd_cds;
	}


	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
}