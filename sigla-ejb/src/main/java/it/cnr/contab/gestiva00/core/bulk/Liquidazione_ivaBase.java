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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Liquidazione_ivaBase extends Liquidazione_ivaKey implements Keyed {
	// ABI VARCHAR(5)
	private java.lang.String abi;

	// ACCONTO_IVA_VERS DECIMAL(15,2)
	private java.math.BigDecimal acconto_iva_vers;

	// ANNOTAZIONI VARCHAR(1000)
	private java.lang.String annotazioni;

	// CAB VARCHAR(5)
	private java.lang.String cab;

	// CD_CDS_DOC_AMM VARCHAR(30)
	private java.lang.String cd_cds_doc_amm;

	// CD_CDS_OBB_ACCENTR VARCHAR(30)
	private java.lang.String cd_cds_obb_accentr;

	// CD_TIPO_DOCUMENTO VARCHAR(10)
	private java.lang.String cd_tipo_documento;

	// CD_UO_DOC_AMM VARCHAR(30)
	private java.lang.String cd_uo_doc_amm;

	// COD_AZIENDA VARCHAR(4)
	private java.lang.String cod_azienda;

	// CRED_IVA_COMP_DETR DECIMAL(15,2)
	private java.math.BigDecimal cred_iva_comp_detr;

	// CRED_IVA_INFRANN_COMP DECIMAL(15,2)
	private java.math.BigDecimal cred_iva_infrann_comp;

	// CRED_IVA_INFRANN_RIMB DECIMAL(15,2)
	private java.math.BigDecimal cred_iva_infrann_rimb;

	// CRED_IVA_SPEC_DETR DECIMAL(15,2)
	private java.math.BigDecimal cred_iva_spec_detr;

	// DT_VERSAMENTO TIMESTAMP
	private java.sql.Timestamp dt_versamento;

	// ESERCIZIO_DOC_AMM DECIMAL(4,0)
	private java.lang.Integer esercizio_doc_amm;

	// ESERCIZIO_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_obb_accentr;

	// INT_DEB_LIQ_TRIM DECIMAL(15,2)
	private java.math.BigDecimal int_deb_liq_trim;

	// IVA_ACQUISTI DECIMAL(15,2)
	private java.math.BigDecimal iva_acquisti;

	// IVA_ACQUISTI_DIFF DECIMAL(15,2)
	private java.math.BigDecimal iva_acquisti_diff;

	// IVA_ACQ_DIFF_ESIG DECIMAL(15,2)
	private java.math.BigDecimal iva_acq_diff_esig;

	// IVA_ACQ_NON_DETR DECIMAL(15,2)
	private java.math.BigDecimal iva_acq_non_detr;

	// IVA_AUTOFATT DECIMAL(15,2)
	private java.math.BigDecimal iva_autofatt;

	// IVA_CREDITO DECIMAL(15,2)
	private java.math.BigDecimal iva_credito;

	// IVA_CREDITO_NO_PRORATA DECIMAL(15,2)
	private java.math.BigDecimal iva_credito_no_prorata;

	// IVA_DA_VERSARE DECIMAL(15,2)
	private java.math.BigDecimal iva_da_versare;

	// IVA_DEBITO DECIMAL(15,2)
	private java.math.BigDecimal iva_debito;

	// IVA_DEB_CRED DECIMAL(15,2)
	private java.math.BigDecimal iva_deb_cred;

	// IVA_DEB_CRED_PER_PREC DECIMAL(15,2)
	private java.math.BigDecimal iva_deb_cred_per_prec;

	// IVA_INTRAUE DECIMAL(15,2)
	private java.math.BigDecimal iva_intraue;

	// IVA_NON_VERS_PER_PREC DECIMAL(15,2)
	private java.math.BigDecimal iva_non_vers_per_prec;

	// IVA_VENDITE DECIMAL(15,2)
	private java.math.BigDecimal iva_vendite;

	// IVA_VENDITE_DIFF DECIMAL(15,2)
	private java.math.BigDecimal iva_vendite_diff;

	// IVA_VEND_DIFF_ESIG DECIMAL(15,2)
	private java.math.BigDecimal iva_vend_diff_esig;

	// IVA_VERSATA DECIMAL(15,2)
	private java.math.BigDecimal iva_versata;

	// PERC_PRORATA_DETRAIBILE DECIMAL(5,2)
	private java.math.BigDecimal perc_prorata_detraibile;

	// PG_DOC_AMM DECIMAL(10,0)
	private java.lang.Long pg_doc_amm;

	// ESERCIZIO_ORI_OBB_ACCENTR DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obb_accentr;

	// PG_OBB_ACCENTR DECIMAL(10,0)
	private java.lang.Long pg_obb_accentr;

	// STATO VARCHAR(1) NOT NULL
	private java.lang.String stato;

	// VAR_IMP_PER_PREC DECIMAL(15,2)
	private java.math.BigDecimal var_imp_per_prec;
	
	private java.math.BigDecimal iva_liq_esterna;
	
	private java.math.BigDecimal iva_vend_diff_es_prec_esig;

	private java.math.BigDecimal iva_acq_diff_es_prec_esig;
	
	private java.math.BigDecimal iva_ven_split_payment;
	
	private java.math.BigDecimal iva_acq_split_payment;

	private java.lang.String stato_coge;

	public Liquidazione_ivaBase() {
	super();
}
	public Liquidazione_ivaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id) {
		super(cd_cds,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,report_id);
	}
	public Liquidazione_ivaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.sql.Timestamp dt_fine,java.sql.Timestamp dt_inizio,java.lang.Integer esercizio,java.lang.Long report_id,java.lang.String tipo_liquidazione) {
		super(cd_cds,cd_unita_organizzativa,dt_fine,dt_inizio,esercizio,report_id,tipo_liquidazione);
	}
	/*
	 * Getter dell'attributo abi
	 */
	public java.lang.String getAbi() {
		return abi;
	}
	/*
	 * Getter dell'attributo acconto_iva_vers
	 */
	public java.math.BigDecimal getAcconto_iva_vers() {
		return acconto_iva_vers;
	}
	/*
	 * Getter dell'attributo annotazioni
	 */
	public java.lang.String getAnnotazioni() {
		return annotazioni;
	}
	/*
	 * Getter dell'attributo cab
	 */
	public java.lang.String getCab() {
		return cab;
	}
	/*
	 * Getter dell'attributo cd_cds_doc_amm
	 */
	public java.lang.String getCd_cds_doc_amm() {
		return cd_cds_doc_amm;
	}
	/*
	 * Getter dell'attributo cd_cds_obb_accentr
	 */
	public java.lang.String getCd_cds_obb_accentr() {
		return cd_cds_obb_accentr;
	}
	/*
	 * Getter dell'attributo cd_tipo_documento
	 */
	public java.lang.String getCd_tipo_documento() {
		return cd_tipo_documento;
	}
	/*
	 * Getter dell'attributo cd_uo_doc_amm
	 */
	public java.lang.String getCd_uo_doc_amm() {
		return cd_uo_doc_amm;
	}
	/*
	 * Getter dell'attributo cod_azienda
	 */
	public java.lang.String getCod_azienda() {
		return cod_azienda;
	}
	/*
	 * Getter dell'attributo cred_iva_comp_detr
	 */
	public java.math.BigDecimal getCred_iva_comp_detr() {
		return cred_iva_comp_detr;
	}
	/*
	 * Getter dell'attributo cred_iva_infrann_comp
	 */
	public java.math.BigDecimal getCred_iva_infrann_comp() {
		return cred_iva_infrann_comp;
	}
	/*
	 * Getter dell'attributo cred_iva_infrann_rimb
	 */
	public java.math.BigDecimal getCred_iva_infrann_rimb() {
		return cred_iva_infrann_rimb;
	}
	/*
	 * Getter dell'attributo cred_iva_spec_detr
	 */
	public java.math.BigDecimal getCred_iva_spec_detr() {
		return cred_iva_spec_detr;
	}
	/*
	 * Getter dell'attributo dt_versamento
	 */
	public java.sql.Timestamp getDt_versamento() {
		return dt_versamento;
	}
	/*
	 * Getter dell'attributo esercizio_doc_amm
	 */
	public java.lang.Integer getEsercizio_doc_amm() {
		return esercizio_doc_amm;
	}
	/*
	 * Getter dell'attributo esercizio_obb_accentr
	 */
	public java.lang.Integer getEsercizio_obb_accentr() {
		return esercizio_obb_accentr;
	}
	/*
	 * Getter dell'attributo int_deb_liq_trim
	 */
	public java.math.BigDecimal getInt_deb_liq_trim() {
		return int_deb_liq_trim;
	}
	/*
	 * Getter dell'attributo iva_acq_diff_esig
	 */
	public java.math.BigDecimal getIva_acq_diff_esig() {
		return iva_acq_diff_esig;
	}
	/*
	 * Getter dell'attributo iva_acq_non_detr
	 */
	public java.math.BigDecimal getIva_acq_non_detr() {
		return iva_acq_non_detr;
	}
	/*
	 * Getter dell'attributo iva_acquisti
	 */
	public java.math.BigDecimal getIva_acquisti() {
		return iva_acquisti;
	}
	/*
	 * Getter dell'attributo iva_acquisti_diff
	 */
	public java.math.BigDecimal getIva_acquisti_diff() {
		return iva_acquisti_diff;
	}
	/*
	 * Getter dell'attributo iva_autofatt
	 */
	public java.math.BigDecimal getIva_autofatt() {
		return iva_autofatt;
	}
	/*
	 * Getter dell'attributo iva_credito
	 */
	public java.math.BigDecimal getIva_credito() {
		return iva_credito;
	}
	/*
	 * Getter dell'attributo iva_credito_no_prorata
	 */
	public java.math.BigDecimal getIva_credito_no_prorata() {
		return iva_credito_no_prorata;
	}
	/*
	 * Getter dell'attributo iva_da_versare
	 */
	public java.math.BigDecimal getIva_da_versare() {
		return iva_da_versare;
	}
	/*
	 * Getter dell'attributo iva_deb_cred
	 */
	public java.math.BigDecimal getIva_deb_cred() {
		return iva_deb_cred;
	}
	/*
	 * Getter dell'attributo iva_deb_cred_per_prec
	 */
	public java.math.BigDecimal getIva_deb_cred_per_prec() {
		return iva_deb_cred_per_prec;
	}
	/*
	 * Getter dell'attributo iva_debito
	 */
	public java.math.BigDecimal getIva_debito() {
		return iva_debito;
	}
	/*
	 * Getter dell'attributo iva_intraue
	 */
	public java.math.BigDecimal getIva_intraue() {
		return iva_intraue;
	}
	/*
	 * Getter dell'attributo iva_non_vers_per_prec
	 */
	public java.math.BigDecimal getIva_non_vers_per_prec() {
		return iva_non_vers_per_prec;
	}
	/*
	 * Getter dell'attributo iva_vend_diff_esig
	 */
	public java.math.BigDecimal getIva_vend_diff_esig() {
		return iva_vend_diff_esig;
	}
	/*
	 * Getter dell'attributo iva_vendite
	 */
	public java.math.BigDecimal getIva_vendite() {
		return iva_vendite;
	}
	/*
	 * Getter dell'attributo iva_vendite_diff
	 */
	public java.math.BigDecimal getIva_vendite_diff() {
		return iva_vendite_diff;
	}
	/*
	 * Getter dell'attributo iva_versata
	 */
	public java.math.BigDecimal getIva_versata() {
		return iva_versata;
	}
	/*
	 * Getter dell'attributo perc_prorata_detraibile
	 */
	public java.math.BigDecimal getPerc_prorata_detraibile() {
		return perc_prorata_detraibile;
	}
	/*
	 * Getter dell'attributo pg_doc_amm
	 */
	public java.lang.Long getPg_doc_amm() {
		return pg_doc_amm;
	}
	/*
	 * Getter dell'attributo esercizio_ori_obb_accentr
	 */
	public java.lang.Integer getEsercizio_ori_obb_accentr() {
		return esercizio_ori_obb_accentr;
	}
	/*
	 * Getter dell'attributo pg_obb_accentr
	 */
	public java.lang.Long getPg_obb_accentr() {
		return pg_obb_accentr;
	}
	/*
	 * Getter dell'attributo stato
	 */
	public java.lang.String getStato() {
		return stato;
	}
	/*
	 * Getter dell'attributo var_imp_per_prec
	 */
	public java.math.BigDecimal getVar_imp_per_prec() {
		return var_imp_per_prec;
	}
	/*
	 * Setter dell'attributo abi
	 */
	public void setAbi(java.lang.String abi) {
		this.abi = abi;
	}
	/*
	 * Setter dell'attributo acconto_iva_vers
	 */
	public void setAcconto_iva_vers(java.math.BigDecimal acconto_iva_vers) {
		this.acconto_iva_vers = acconto_iva_vers;
	}
	/*
	 * Setter dell'attributo annotazioni
	 */
	public void setAnnotazioni(java.lang.String annotazioni) {
		this.annotazioni = annotazioni;
	}
	/*
	 * Setter dell'attributo cab
	 */
	public void setCab(java.lang.String cab) {
		this.cab = cab;
	}
	/*
	 * Setter dell'attributo cd_cds_doc_amm
	 */
	public void setCd_cds_doc_amm(java.lang.String cd_cds_doc_amm) {
		this.cd_cds_doc_amm = cd_cds_doc_amm;
	}
	/*
	 * Setter dell'attributo cd_cds_obb_accentr
	 */
	public void setCd_cds_obb_accentr(java.lang.String cd_cds_obb_accentr) {
		this.cd_cds_obb_accentr = cd_cds_obb_accentr;
	}
	/*
	 * Setter dell'attributo cd_tipo_documento
	 */
	public void setCd_tipo_documento(java.lang.String cd_tipo_documento) {
		this.cd_tipo_documento = cd_tipo_documento;
	}
	/*
	 * Setter dell'attributo cd_uo_doc_amm
	 */
	public void setCd_uo_doc_amm(java.lang.String cd_uo_doc_amm) {
		this.cd_uo_doc_amm = cd_uo_doc_amm;
	}
	/*
	 * Setter dell'attributo cod_azienda
	 */
	public void setCod_azienda(java.lang.String cod_azienda) {
		this.cod_azienda = cod_azienda;
	}
	/*
	 * Setter dell'attributo cred_iva_comp_detr
	 */
	public void setCred_iva_comp_detr(java.math.BigDecimal cred_iva_comp_detr) {
		this.cred_iva_comp_detr = cred_iva_comp_detr;
	}
	/*
	 * Setter dell'attributo cred_iva_infrann_comp
	 */
	public void setCred_iva_infrann_comp(java.math.BigDecimal cred_iva_infrann_comp) {
		this.cred_iva_infrann_comp = cred_iva_infrann_comp;
	}
	/*
	 * Setter dell'attributo cred_iva_infrann_rimb
	 */
	public void setCred_iva_infrann_rimb(java.math.BigDecimal cred_iva_infrann_rimb) {
		this.cred_iva_infrann_rimb = cred_iva_infrann_rimb;
	}
	/*
	 * Setter dell'attributo cred_iva_spec_detr
	 */
	public void setCred_iva_spec_detr(java.math.BigDecimal cred_iva_spec_detr) {
		this.cred_iva_spec_detr = cred_iva_spec_detr;
	}
	/*
	 * Setter dell'attributo dt_versamento
	 */
	public void setDt_versamento(java.sql.Timestamp dt_versamento) {
		this.dt_versamento = dt_versamento;
	}
	/*
	 * Setter dell'attributo esercizio_doc_amm
	 */
	public void setEsercizio_doc_amm(java.lang.Integer esercizio_doc_amm) {
		this.esercizio_doc_amm = esercizio_doc_amm;
	}
	/*
	 * Setter dell'attributo esercizio_obb_accentr
	 */
	public void setEsercizio_obb_accentr(java.lang.Integer esercizio_obb_accentr) {
		this.esercizio_obb_accentr = esercizio_obb_accentr;
	}
	/*
	 * Setter dell'attributo int_deb_liq_trim
	 */
	public void setInt_deb_liq_trim(java.math.BigDecimal int_deb_liq_trim) {
		this.int_deb_liq_trim = int_deb_liq_trim;
	}
	/*
	 * Setter dell'attributo iva_acq_diff_esig
	 */
	public void setIva_acq_diff_esig(java.math.BigDecimal iva_acq_diff_esig) {
		this.iva_acq_diff_esig = iva_acq_diff_esig;
	}
	/*
	 * Setter dell'attributo iva_acq_non_detr
	 */
	public void setIva_acq_non_detr(java.math.BigDecimal iva_acq_non_detr) {
		this.iva_acq_non_detr = iva_acq_non_detr;
	}
	/*
	 * Setter dell'attributo iva_acquisti
	 */
	public void setIva_acquisti(java.math.BigDecimal iva_acquisti) {
		this.iva_acquisti = iva_acquisti;
	}
	/*
	 * Setter dell'attributo iva_acquisti_diff
	 */
	public void setIva_acquisti_diff(java.math.BigDecimal iva_acquisti_diff) {
		this.iva_acquisti_diff = iva_acquisti_diff;
	}
	/*
	 * Setter dell'attributo iva_autofatt
	 */
	public void setIva_autofatt(java.math.BigDecimal iva_autofatt) {
		this.iva_autofatt = iva_autofatt;
	}
	/*
	 * Setter dell'attributo iva_credito
	 */
	public void setIva_credito(java.math.BigDecimal iva_credito) {
		this.iva_credito = iva_credito;
	}
	/*
	 * Setter dell'attributo iva_credito_no_prorata
	 */
	public void setIva_credito_no_prorata(java.math.BigDecimal iva_credito_no_prorata) {
		this.iva_credito_no_prorata = iva_credito_no_prorata;
	}
	/*
	 * Setter dell'attributo iva_da_versare
	 */
	public void setIva_da_versare(java.math.BigDecimal iva_da_versare) {
		this.iva_da_versare = iva_da_versare;
	}
	/*
	 * Setter dell'attributo iva_deb_cred
	 */
	public void setIva_deb_cred(java.math.BigDecimal iva_deb_cred) {
		this.iva_deb_cred = iva_deb_cred;
	}
	/*
	 * Setter dell'attributo iva_deb_cred_per_prec
	 */
	public void setIva_deb_cred_per_prec(java.math.BigDecimal iva_deb_cred_per_prec) {
		this.iva_deb_cred_per_prec = iva_deb_cred_per_prec;
	}
	/*
	 * Setter dell'attributo iva_debito
	 */
	public void setIva_debito(java.math.BigDecimal iva_debito) {
		this.iva_debito = iva_debito;
	}
	/*
	 * Setter dell'attributo iva_intraue
	 */
	public void setIva_intraue(java.math.BigDecimal iva_intraue) {
		this.iva_intraue = iva_intraue;
	}
	/*
	 * Setter dell'attributo iva_non_vers_per_prec
	 */
	public void setIva_non_vers_per_prec(java.math.BigDecimal iva_non_vers_per_prec) {
		this.iva_non_vers_per_prec = iva_non_vers_per_prec;
	}
	/*
	 * Setter dell'attributo iva_vend_diff_esig
	 */
	public void setIva_vend_diff_esig(java.math.BigDecimal iva_vend_diff_esig) {
		this.iva_vend_diff_esig = iva_vend_diff_esig;
	}
	/*
	 * Setter dell'attributo iva_vendite
	 */
	public void setIva_vendite(java.math.BigDecimal iva_vendite) {
		this.iva_vendite = iva_vendite;
	}
	/*
	 * Setter dell'attributo iva_vendite_diff
	 */
	public void setIva_vendite_diff(java.math.BigDecimal iva_vendite_diff) {
		this.iva_vendite_diff = iva_vendite_diff;
	}
	/*
	 * Setter dell'attributo iva_versata
	 */
	public void setIva_versata(java.math.BigDecimal iva_versata) {
		this.iva_versata = iva_versata;
	}
	/*
	 * Setter dell'attributo perc_prorata_detraibile
	 */
	public void setPerc_prorata_detraibile(java.math.BigDecimal perc_prorata_detraibile) {
		this.perc_prorata_detraibile = perc_prorata_detraibile;
	}
	/*
	 * Setter dell'attributo pg_doc_amm
	 */
	public void setPg_doc_amm(java.lang.Long pg_doc_amm) {
		this.pg_doc_amm = pg_doc_amm;
	}
	/*
	 * Setter dell'attributo esercizio_ori_obb_accentr
	 */
	public void setEsercizio_ori_obb_accentr(java.lang.Integer esercizio_ori_obb_accentr) {
		this.esercizio_ori_obb_accentr = esercizio_ori_obb_accentr;
	}
	/*
	 * Setter dell'attributo pg_obb_accentr
	 */
	public void setPg_obb_accentr(java.lang.Long pg_obb_accentr) {
		this.pg_obb_accentr = pg_obb_accentr;
	}
	/*
	 * Setter dell'attributo stato
	 */
	public void setStato(java.lang.String stato) {
		this.stato = stato;
	}
	/*
	 * Setter dell'attributo var_imp_per_prec
	 */
	public void setVar_imp_per_prec(java.math.BigDecimal var_imp_per_prec) {
		this.var_imp_per_prec = var_imp_per_prec;
	}
	public java.math.BigDecimal getIva_liq_esterna() {
		return iva_liq_esterna;
	}
	public void setIva_liq_esterna(java.math.BigDecimal iva_liq_esterna) {
		this.iva_liq_esterna = iva_liq_esterna;
	}

	public java.math.BigDecimal getIva_vend_diff_es_prec_esig() {
		return iva_vend_diff_es_prec_esig;
	}

	public void setIva_vend_diff_es_prec_esig(java.math.BigDecimal iva_vend_diff_es_prec_esig) {
		this.iva_vend_diff_es_prec_esig = iva_vend_diff_es_prec_esig;
	}

	public java.math.BigDecimal getIva_acq_diff_es_prec_esig() {
		return iva_acq_diff_es_prec_esig;
	}

	public void setIva_acq_diff_es_prec_esig(java.math.BigDecimal iva_acq_diff_es_prec_esig) {
		this.iva_acq_diff_es_prec_esig = iva_acq_diff_es_prec_esig;
	}

	public java.math.BigDecimal getIva_ven_split_payment() {
		return iva_ven_split_payment;
	}

	public void setIva_ven_split_payment(java.math.BigDecimal iva_ven_split_payment) {
		this.iva_ven_split_payment = iva_ven_split_payment;
	}

	public java.math.BigDecimal getIva_acq_split_payment() {
		return iva_acq_split_payment;
	}

	public void setIva_acq_split_payment(java.math.BigDecimal iva_acq_split_payment) {
		this.iva_acq_split_payment = iva_acq_split_payment;
	}

	public String getStato_coge() {
		return stato_coge;
	}

	public void setStato_coge(String stato_coge) {
		this.stato_coge = stato_coge;
	}

	public boolean isDefinitiva() {
		return Liquidazione_ivaVBulk.DEFINITIVO.equals(this.getStato());
	}
}
