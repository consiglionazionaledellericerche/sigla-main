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
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_gae_residui_entBulk extends OggettoBulk implements Persistent{
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
 
//    CD_CDS_ACC VARCHAR(30)
	private java.lang.String cd_cds_acc;
 
//    PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;
 
//    PG_ACCERTAMENTO_SCADENZARIO DECIMAL(22,0)
	private java.lang.Long pg_accertamento_scadenzario;
 
//    CD_TIPO_DOCUMENTO_CONT VARCHAR(10)
	private java.lang.String cd_tipo_documento_cont;
 
//    DS_SCADENZA VARCHAR(300)
	private java.lang.String ds_scadenza;
 
//    IM_OBBL_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal im_obbl_res_pro;
 
//    PG_VAR_RES_PRO DECIMAL(22,0)
	private java.lang.Long pg_var_res_pro;
 
//    DS_VAR_RES_PRO VARCHAR(300)
	private java.lang.String ds_var_res_pro;
 
//    VAR_PIU_ACC_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal var_piu_acc_res_pro;
 
//    VAR_MENO_ACC_RES_PRO DECIMAL(22,0)
	private java.math.BigDecimal var_meno_acc_res_pro;
 
//    CD_CDS_REV VARCHAR(30)
	private java.lang.String cd_cds_rev;
 
//    PG_REVERSALE DECIMAL(22,0)
	private java.lang.Long pg_reversale;
 
//    DS_REVERSALE VARCHAR(300)
	private java.lang.String ds_reversale;
 
//    IM_MANDATI_REVERSALI_PRO DECIMAL(22,0)
	private java.math.BigDecimal im_mandati_reversali_pro;

//  DA_RISCUOTERE DECIMAL(22,0)
	private java.math.BigDecimal da_riscuotere;
 

	
	public V_cons_gae_residui_entBulk() {
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
	public java.lang.String getCd_cds_acc() {
		return cd_cds_acc;
	}
	public void setCd_cds_acc(java.lang.String cd_cds_acc)  {
		this.cd_cds_acc=cd_cds_acc;
	}
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(java.lang.Long pg_accertamento)  {
		this.pg_accertamento=pg_accertamento;
	}
	public java.lang.Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
	}
	public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario)  {
		this.pg_accertamento_scadenzario=pg_accertamento_scadenzario;
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
	public java.math.BigDecimal getVar_piu_acc_res_pro() {
		return var_piu_acc_res_pro;
	}
	public void setVar_piu_acc_res_pro(java.math.BigDecimal var_piu_acc_res_pro)  {
		this.var_piu_acc_res_pro=var_piu_acc_res_pro;
	}
	public java.math.BigDecimal getVar_meno_acc_res_pro() {
		return var_meno_acc_res_pro;
	}
	public void setVar_meno_acc_res_pro(java.math.BigDecimal var_meno_acc_res_pro)  {
		this.var_meno_acc_res_pro=var_meno_acc_res_pro;
	}
	public java.lang.String getCd_cds_rev() {
		return cd_cds_rev;
	}
	public void setCd_cds_rev(java.lang.String cd_cds_rev)  {
		this.cd_cds_rev=cd_cds_rev;
	}
	public java.lang.Long getPg_reversale() {
		return pg_reversale;
	}
	public void setPg_reversale(java.lang.Long pg_reversale)  {
		this.pg_reversale=pg_reversale;
	}
	public java.lang.String getDs_reversale() {
		return ds_reversale;
	}
	public void setDs_reversale(java.lang.String ds_reversale)  {
		this.ds_reversale=ds_reversale;
	}
	public java.math.BigDecimal getIm_mandati_reversali_pro() {
		return im_mandati_reversali_pro;
	}
	public void setIm_mandati_reversali_pro(java.math.BigDecimal im_mandati_reversali_pro)  {
		this.im_mandati_reversali_pro=im_mandati_reversali_pro;
	}

	public java.math.BigDecimal getDa_riscuotere() {
		return da_riscuotere;
	}

	public void setDa_riscuotere(java.math.BigDecimal da_riscuotere) {
		this.da_riscuotere = da_riscuotere;
	}

	public java.lang.String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
}