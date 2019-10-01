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

package it.cnr.contab.prevent01.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_cons_pdgg_dipfo_speBulk extends V_cons_pdgg_dipfoBulk  {
	
	private java.lang.String  cd_cdr_assegnatario;
	
	private java.lang.String  ds_cdr_assegnatario;

	private java.math.BigDecimal im_dec_ist_int;
	
	private java.math.BigDecimal im_dec_ist_est;
	
	private java.math.BigDecimal im_dec_area_int;
	
	private java.math.BigDecimal im_dec_area_est;
	
	private java.math.BigDecimal imp_tot_dec_int;
	
	private java.math.BigDecimal imp_tot_dec_est;
	
	private java.math.BigDecimal imp_tot_decentrato;
	
	private java.math.BigDecimal tratt_econ_int;
	
	private java.math.BigDecimal tratt_econ_est;
	
	private java.math.BigDecimal im_acc_altre_sp_int;
	
	private java.math.BigDecimal imp_tot_comp_int;
	
	private java.math.BigDecimal imp_tot_comp_est;
	
	private java.math.BigDecimal im_pagamenti;
	
	public V_cons_pdgg_dipfo_speBulk() {
		super();
	}

	public java.math.BigDecimal getIm_dec_ist_int() {
		return im_dec_ist_int;
	}

	public void setIm_dec_ist_int(java.math.BigDecimal im_dec_ist_int) {
		this.im_dec_ist_int = im_dec_ist_int;
	}

	public java.math.BigDecimal getIm_dec_ist_est() {
		return im_dec_ist_est;
	}

	public void setIm_dec_ist_est(java.math.BigDecimal im_dec_ist_est) {
		this.im_dec_ist_est = im_dec_ist_est;
	}

	public java.math.BigDecimal getIm_dec_area_int() {
		return im_dec_area_int;
	}

	public void setIm_dec_area_int(java.math.BigDecimal im_dec_area_int) {
		this.im_dec_area_int = im_dec_area_int;
	}

	public java.math.BigDecimal getIm_dec_area_est() {
		return im_dec_area_est;
	}

	public void setIm_dec_area_est(java.math.BigDecimal im_dec_area_est) {
		this.im_dec_area_est = im_dec_area_est;
	}

	public java.math.BigDecimal getImp_tot_dec_int() {
		return imp_tot_dec_int;
	}

	public void setImp_tot_dec_int(java.math.BigDecimal imp_tot_dec_int) {
		this.imp_tot_dec_int = imp_tot_dec_int;
	}

	public java.math.BigDecimal getImp_tot_dec_est() {
		return imp_tot_dec_est;
	}

	public void setImp_tot_dec_est(java.math.BigDecimal imp_tot_dec_est) {
		this.imp_tot_dec_est = imp_tot_dec_est;
	}

	public java.math.BigDecimal getImp_tot_decentrato() {
		return imp_tot_decentrato;
	}

	public void setImp_tot_decentrato(java.math.BigDecimal imp_tot_decentrato) {
		this.imp_tot_decentrato = imp_tot_decentrato;
	}

	public java.math.BigDecimal getTratt_econ_int() {
		return tratt_econ_int;
	}

	public void setTratt_econ_int(java.math.BigDecimal tratt_econ_int) {
		this.tratt_econ_int = tratt_econ_int;
	}

	public java.math.BigDecimal getTratt_econ_est() {
		return tratt_econ_est;
	}

	public void setTratt_econ_est(java.math.BigDecimal tratt_econ_est) {
		this.tratt_econ_est = tratt_econ_est;
	}

	public java.math.BigDecimal getIm_acc_altre_sp_int() {
		return im_acc_altre_sp_int;
	}

	public void setIm_acc_altre_sp_int(java.math.BigDecimal im_acc_altre_sp_int) {
		this.im_acc_altre_sp_int = im_acc_altre_sp_int;
	}

	public java.math.BigDecimal getImp_tot_comp_int() {
		return imp_tot_comp_int;
	}

	public void setImp_tot_comp_int(java.math.BigDecimal imp_tot_comp_int) {
		this.imp_tot_comp_int = imp_tot_comp_int;
	}

	public java.math.BigDecimal getImp_tot_comp_est() {
		return imp_tot_comp_est;
	}

	public void setImp_tot_comp_est(java.math.BigDecimal imp_tot_comp_est) {
		this.imp_tot_comp_est = imp_tot_comp_est;
	}

	public java.math.BigDecimal getIm_pagamenti() {
		return im_pagamenti;
	}

	public void setIm_pagamenti(java.math.BigDecimal im_pagamenti) {
		this.im_pagamenti = im_pagamenti;
	}

	public java.lang.String getCd_cdr_assegnatario() {
		return cd_cdr_assegnatario;
	}

	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario) {
		this.cd_cdr_assegnatario = cd_cdr_assegnatario;
	}

	public java.lang.String getDs_cdr_assegnatario() {
		return ds_cdr_assegnatario;
	}

	public void setDs_cdr_assegnatario(java.lang.String ds_cdr_assegnatario) {
		this.ds_cdr_assegnatario = ds_cdr_assegnatario;
	}

}