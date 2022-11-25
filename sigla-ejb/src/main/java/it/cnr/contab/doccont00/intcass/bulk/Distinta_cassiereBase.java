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

package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.persistency.*;
import it.cnr.si.spring.storage.annotation.StorageProperty;

import java.util.Optional;

public class Distinta_cassiereBase extends Distinta_cassiereKey implements Keyed {

	// DT_EMISSIONE TIMESTAMP
	private java.sql.Timestamp dt_emissione;

	// DT_INVIO TIMESTAMP
	private java.sql.Timestamp dt_invio;

	// IM_MAN_INI_ACC DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_man_ini_acc;

	// IM_MAN_INI_PAG DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_man_ini_pag;

	// IM_MAN_INI_SOS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_man_ini_sos;

	// IM_REV_INI_SOS DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rev_ini_sos;

	// IM_REV_INI_TRA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rev_ini_tra;

	// IM_REV_INI_RIT DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_rev_ini_rit;

	@StorageProperty(name="doccont:numDoc")
	// PG_DISTINTA_DEF DECIMAL(10,0)
	private java.lang.Long pg_distinta_def;

	private java.lang.Boolean fl_flusso;
	private java.lang.Boolean fl_sepa;
	private java.lang.Boolean fl_annulli;
	private java.sql.Timestamp dt_invio_pec;
	private java.lang.Boolean inviaPEC;

	private Integer progFlusso;
	private String identificativoFlussoBT;
	private String stato;

	private String cd_tesoreria;

	public Distinta_cassiereBase() {
		super();
	}
	public Distinta_cassiereBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_distinta) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_distinta);
	}
	/*
     * Getter dell'attributo dt_emissione
     */
	public java.sql.Timestamp getDt_emissione() {
		return dt_emissione;
	}
	/*
     * Getter dell'attributo dt_invio
     */
	public java.sql.Timestamp getDt_invio() {
		return dt_invio;
	}
	/*
     * Getter dell'attributo im_man_ini_acc
     */
	public java.math.BigDecimal getIm_man_ini_acc() {
		return im_man_ini_acc;
	}
	/*
     * Getter dell'attributo im_man_ini_pag
     */
	public java.math.BigDecimal getIm_man_ini_pag() {
		return im_man_ini_pag;
	}
	/*
     * Getter dell'attributo im_man_ini_sos
     */
	public java.math.BigDecimal getIm_man_ini_sos() {
		return im_man_ini_sos;
	}
	/*
     * Getter dell'attributo im_rev_ini_rit
     */
	public java.math.BigDecimal getIm_rev_ini_rit() {
		return im_rev_ini_rit;
	}
	/*
     * Getter dell'attributo im_rev_ini_sos
     */
	public java.math.BigDecimal getIm_rev_ini_sos() {
		return im_rev_ini_sos;
	}
	/*
     * Getter dell'attributo im_rev_ini_tra
     */
	public java.math.BigDecimal getIm_rev_ini_tra() {
		return im_rev_ini_tra;
	}
	/*
     * Getter dell'attributo pg_distinta_def
     */
	public java.lang.Long getPg_distinta_def() {
		return pg_distinta_def;
	}
	/*
     * Setter dell'attributo dt_emissione
     */
	public void setDt_emissione(java.sql.Timestamp dt_emissione) {
		this.dt_emissione = dt_emissione;
	}
	/*
     * Setter dell'attributo dt_invio
     */
	public void setDt_invio(java.sql.Timestamp dt_invio) {
		this.dt_invio = dt_invio;
	}
	/*
     * Setter dell'attributo im_man_ini_acc
     */
	public void setIm_man_ini_acc(java.math.BigDecimal im_man_ini_acc) {
		this.im_man_ini_acc = im_man_ini_acc;
	}
	/*
     * Setter dell'attributo im_man_ini_pag
     */
	public void setIm_man_ini_pag(java.math.BigDecimal im_man_ini_pag) {
		this.im_man_ini_pag = im_man_ini_pag;
	}
	/*
     * Setter dell'attributo im_man_ini_sos
     */
	public void setIm_man_ini_sos(java.math.BigDecimal im_man_ini_sos) {
		this.im_man_ini_sos = im_man_ini_sos;
	}
	/*
     * Setter dell'attributo im_rev_ini_rit
     */
	public void setIm_rev_ini_rit(java.math.BigDecimal newIm_rev_ini_rit) {
		im_rev_ini_rit = newIm_rev_ini_rit;
	}
	/*
     * Setter dell'attributo im_rev_ini_sos
     */
	public void setIm_rev_ini_sos(java.math.BigDecimal im_rev_ini_sos) {
		this.im_rev_ini_sos = im_rev_ini_sos;
	}
	/*
     * Setter dell'attributo im_rev_ini_tra
     */
	public void setIm_rev_ini_tra(java.math.BigDecimal im_rev_ini_tra) {
		this.im_rev_ini_tra = im_rev_ini_tra;
	}
	/*
     * Setter dell'attributo pg_distinta_def
     */
	public void setPg_distinta_def(java.lang.Long pg_distinta_def) {
		this.pg_distinta_def = pg_distinta_def;
	}
	public java.lang.Boolean getFl_flusso() {
		return fl_flusso;
	}
	public void setFl_flusso(java.lang.Boolean fl_flusso) {
		this.fl_flusso = fl_flusso;
	}
	public java.lang.Boolean getFl_sepa() {
		return fl_sepa;
	}
	public void setFl_sepa(java.lang.Boolean fl_sepa) {
		this.fl_sepa = fl_sepa;
	}
	public java.sql.Timestamp getDt_invio_pec() {
		return dt_invio_pec;
	}
	public void setDt_invio_pec(java.sql.Timestamp dt_invio_pec) {
		this.dt_invio_pec = dt_invio_pec;
	}
	public java.lang.Boolean getFl_annulli() {
		return fl_annulli;
	}
	public void setFl_annulli(java.lang.Boolean fl_annulli) {
		this.fl_annulli = fl_annulli;
	}

	public Integer getProgFlusso() {
		return progFlusso;
	}

	public void setProgFlusso(Integer progFlusso) {
		this.progFlusso = progFlusso;
	}

	public String getIdentificativoFlussoBT() {
		return identificativoFlussoBT;
	}

	public void setIdentificativoFlussoBT(String identificativoFlussoBT) {
		this.identificativoFlussoBT = identificativoFlussoBT;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}


	public Boolean getInviaPEC() {
		return inviaPEC;
	}

	public void setInviaPEC(Boolean inviaPEC) {
		this.inviaPEC = inviaPEC;
	}

	public String getCd_tesoreria() {
		return cd_tesoreria;
	}

	public void setCd_tesoreria(String cd_tesoreria) {
		this.cd_tesoreria = cd_tesoreria;
	}

}