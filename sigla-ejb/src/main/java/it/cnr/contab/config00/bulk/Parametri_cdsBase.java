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

package it.cnr.contab.config00.bulk;

import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

/**
 * Creation date: (12/11/2004)
 * @author Aurelio D'Amico
 * @version 1.0
 */
public class Parametri_cdsBase extends Parametri_cdsKey implements Keyed {
	// FL_COMMESSA_OBBLIGATORIA CHAR(1) NOT NULL
	private Boolean fl_commessa_obbligatoria;
	private Boolean fl_progetto_numeratore;
	private Boolean fl_obbligo_protocollo_inf;
	private Boolean fl_contratto_cessato;
	private Boolean fl_approva_var_pdg;
	private Boolean fl_approva_var_stanz_res;
	private Integer progetto_numeratore_cifre;
	private Boolean fl_ribaltato;
	private Boolean fl_mod_obbl_res;
	private Boolean fl_riporta_avanti;
	private Boolean fl_riporta_indietro;
	private Boolean fl_linea_pgiro_e_cds;
	private String cd_cdr_linea_pgiro_e;
	private String cd_linea_pgiro_e;
	private Boolean fl_linea_pgiro_s_cds;
	private String cd_cdr_linea_pgiro_s;
	private String cd_linea_pgiro_s;
	private Boolean fl_blocco_iban;
	private Boolean fl_kit_firma_digitale;
	// IM_SOGLIA_CONTRATTO_S NUMBER(15,2) NULL
	private BigDecimal im_soglia_contratto_s;

	// IM_SOGLIA_CONTRATTO_E NUMBER(15,2) NULL
	private BigDecimal im_soglia_contratto_e;
	
    // BLOCCO_IMPEGNI_CDR_GAE CHAR(1) NOT NULL
	private String blocco_impegni_cdr_gae;
	
	// IM_SOGLIA_CONSUMO_RESIDUO NUMBER(15,2) NULL
	private BigDecimal im_soglia_consumo_residuo;

	private String cd_dipartimento;

	private Boolean fl_riaccertamento;

	private Boolean fl_riobbligazione;

	private java.lang.Boolean fl_blocco_impegni_natfin;

	// ABIL_PROGETTO_STRORG VARCHAR2(3)
	private java.lang.String abil_progetto_strorg;

	public Parametri_cdsBase() {
		super();
	}
	public Parametri_cdsBase(String cd_cds, Integer esercizio) {
		super(cd_cds, esercizio);
	}

	public Boolean getFl_commessa_obbligatoria() {
		return fl_commessa_obbligatoria;
	}

	public void setFl_commessa_obbligatoria(Boolean boolean1) {
		fl_commessa_obbligatoria = boolean1;
	}

	public Boolean getFl_progetto_numeratore() {
		return fl_progetto_numeratore;
	}

	public void setFl_progetto_numeratore(Boolean boolean1) {
		fl_progetto_numeratore = boolean1;
	}

	public Integer getProgetto_numeratore_cifre() {
		return progetto_numeratore_cifre;
	}

	public void setProgetto_numeratore_cifre(Integer integer) {
		progetto_numeratore_cifre = integer;
	}

	/**
	 * @return
	 */
	public BigDecimal getIm_soglia_contratto_e() {
		return im_soglia_contratto_e;
	}

	/**
	 * @return
	 */
	public BigDecimal getIm_soglia_contratto_s() {
		return im_soglia_contratto_s;
	}

	/**
	 * @param decimal
	 */
	public void setIm_soglia_contratto_e(BigDecimal decimal) {
		im_soglia_contratto_e = decimal;
	}

	/**
	 * @param decimal
	 */
	public void setIm_soglia_contratto_s(BigDecimal decimal) {
		im_soglia_contratto_s = decimal;
	}

	/**
	 * @return
	 */
	public Boolean getFl_obbligo_protocollo_inf() {
		return fl_obbligo_protocollo_inf;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_obbligo_protocollo_inf(Boolean boolean1) {
		fl_obbligo_protocollo_inf = boolean1;
	}

	/**
	 * @return
	 */
	public Boolean getFl_contratto_cessato() {
		return fl_contratto_cessato;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_contratto_cessato(Boolean boolean1) {
		fl_contratto_cessato = boolean1;
	}

	/**
	 * @return
	 */
	public String getBlocco_impegni_cdr_gae() {
		return blocco_impegni_cdr_gae;
	}

	/**
	 * @param string
	 */
	public void setBlocco_impegni_cdr_gae(String string) {
		blocco_impegni_cdr_gae = string;
	}

	/**
	 * @return
	 */
	public Boolean getFl_approva_var_pdg() {
		return fl_approva_var_pdg;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_approva_var_pdg(Boolean boolean1) {
		fl_approva_var_pdg = boolean1;
	}

	/**
	 * @return
	 */
	public Boolean getFl_ribaltato() {
		return fl_ribaltato;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_ribaltato(Boolean boolean1) {
		fl_ribaltato = boolean1;
	}

	public Boolean getFl_mod_obbl_res() {
		return fl_mod_obbl_res;
	}
	public void setFl_mod_obbl_res(Boolean fl_mod_obbl_res) {
		this.fl_mod_obbl_res = fl_mod_obbl_res;
	}

	/**
	 * @return Returns the im_soglia_consumo_residuo.
	 */
	public BigDecimal getIm_soglia_consumo_residuo() {
		return im_soglia_consumo_residuo;
	}
	/**
	 * @param im_soglia_consumo_residuo The im_soglia_consumo_residuo to set.
	 */
	public void setIm_soglia_consumo_residuo(
			BigDecimal im_soglia_consumo_residuo) {
		this.im_soglia_consumo_residuo = im_soglia_consumo_residuo;
	}
	/**
	 * @return
	 */
	public Boolean getFl_approva_var_stanz_res() {
		return fl_approva_var_stanz_res;
	}

	/**
	 * @param boolean1
	 */
	public void setFl_approva_var_stanz_res(Boolean boolean1) {
		fl_approva_var_stanz_res = boolean1;
	}
	
	
	
	public Boolean getFl_linea_pgiro_e_cds() {
		return fl_linea_pgiro_e_cds;
	}
	public void setFl_linea_pgiro_e_cds(Boolean b) {
		this.fl_linea_pgiro_e_cds = b;
	}
	public Boolean getFl_linea_pgiro_s_cds() {
		return fl_linea_pgiro_s_cds;
	}
	public void setFl_linea_pgiro_s_cds(Boolean b) {
		this.fl_linea_pgiro_s_cds = b;
	}
	
	public Boolean getFl_riporta_avanti() {
		return fl_riporta_avanti;
	}
	public void setFl_riporta_avanti(Boolean b) {
		this.fl_riporta_avanti = b;
	}
	public Boolean getFl_riporta_indietro() {
		return fl_riporta_indietro;
	}
	public void setFl_riporta_indietro(Boolean b) {
		this.fl_riporta_indietro = b;
	}
	
	public String getCd_cdr_linea_pgiro_e() {
		return cd_cdr_linea_pgiro_e;
	}
	public void setCd_cdr_linea_pgiro_e(String cd_cdr_linea_pgiro_e) {
		this.cd_cdr_linea_pgiro_e = cd_cdr_linea_pgiro_e;
	}
	public String getCd_cdr_linea_pgiro_s() {
		return cd_cdr_linea_pgiro_s;
	}
	public void setCd_cdr_linea_pgiro_s(String cd_cdr_linea_pgiro_s) {
		this.cd_cdr_linea_pgiro_s = cd_cdr_linea_pgiro_s;
	}
	public String getCd_linea_pgiro_e() {
		return cd_linea_pgiro_e;
	}
	public void setCd_linea_pgiro_e(String cd_linea_pgiro_e) {
		this.cd_linea_pgiro_e = cd_linea_pgiro_e;
	}
	public String getCd_linea_pgiro_s() {
		return cd_linea_pgiro_s;
	}
	public void setCd_linea_pgiro_s(String cd_linea_pgiro_s) {
		this.cd_linea_pgiro_s = cd_linea_pgiro_s;
	}
	public Boolean getFl_blocco_iban() {
		return fl_blocco_iban;
	}
	public void setFl_blocco_iban(Boolean fl_blocco_iban) {
		this.fl_blocco_iban = fl_blocco_iban;
	}
	public Boolean getFl_kit_firma_digitale() {
		return fl_kit_firma_digitale;
	}
	public void setFl_kit_firma_digitale(Boolean fl_kit_firma_digitale) {
		this.fl_kit_firma_digitale = fl_kit_firma_digitale;
	}
	public String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(String cd_dipartimento) {
		this.cd_dipartimento = cd_dipartimento;
	}
	public Boolean getFl_riaccertamento() {
		return fl_riaccertamento;
	}
	public void setFl_riaccertamento(Boolean fl_riaccertamento) {
		this.fl_riaccertamento = fl_riaccertamento;
	}
	public Boolean getFl_riobbligazione() {
		return fl_riobbligazione;
	}
	public void setFl_riobbligazione(Boolean fl_riobbligazione) {
		this.fl_riobbligazione = fl_riobbligazione;
	}

	public Boolean getFl_blocco_impegni_natfin() {
		return fl_blocco_impegni_natfin;
	}

	public void setFl_blocco_impegni_natfin(Boolean fl_blocco_impegni_natfin) {
		this.fl_blocco_impegni_natfin = fl_blocco_impegni_natfin;
	}

	public String getAbil_progetto_strorg() {
		return abil_progetto_strorg;
	}

	public void setAbil_progetto_strorg(String abil_progetto_strorg) {
		this.abil_progetto_strorg = abil_progetto_strorg;
	}
}
