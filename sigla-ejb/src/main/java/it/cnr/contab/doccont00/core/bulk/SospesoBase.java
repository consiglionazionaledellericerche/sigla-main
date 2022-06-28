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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class SospesoBase extends SospesoKey implements Keyed {
    // CAUSALE VARCHAR(200)
    private String causale;

    // CD_CDS_ORIGINE VARCHAR(30)
    private String cd_cds_origine;

    // CD_UO_ORIGINE VARCHAR(30)
    private String cd_uo_origine;

    // DS_ANAGRAFICO VARCHAR(200)
    private String ds_anagrafico;

    // DT_REGISTRAZIONE TIMESTAMP NOT NULL
    private java.sql.Timestamp dt_registrazione;

    // FL_STORNATO CHAR(1) NOT NULL
    private Boolean fl_stornato;

    // IM_ASSOCIATO DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_associato;

    // IM_ASS_MOD_1210 DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_ass_mod_1210;

    // IM_SOSPESO DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_sospeso;

    // STATO_SOSPESO CHAR(1) NOT NULL
    private String stato_sospeso;

    // TI_CC_BI CHAR(1) NOT NULL
    private String ti_cc_bi;

    public String getCd_avviso_pagopa() {
        return cd_avviso_pagopa;
    }

    public void setCd_avviso_pagopa(String cd_avviso_pagopa) {
        this.cd_avviso_pagopa = cd_avviso_pagopa;
    }

    // CD_SOSPESO_PADRE(20)
    private String cd_sospeso_padre;

    // CD_PROPRIO_SOSPESO(20)
    private String cd_proprio_sospeso;

    // DT_STORNO TIMESTAMP NULL
    private java.sql.Timestamp dt_storno;

    private String destinazione;

    private String tipo_contabilita;
    private String cd_avviso_pagopa;

	private String cd_cds_man_riaccr;
	private Integer esercizio_man_riaccr;
	private Long pg_mandato_man_riaccr;

    public SospesoBase() {
        super();
    }

    public SospesoBase(String cd_cds, String cd_sospeso, Integer esercizio, String ti_entrata_spesa, String ti_sospeso_riscontro) {
        super(cd_cds, cd_sospeso, esercizio, ti_entrata_spesa, ti_sospeso_riscontro);
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getTipo_contabilita() {
        return tipo_contabilita;
    }

    public void setTipo_contabilita(String tipo_contabilita) {
        this.tipo_contabilita = tipo_contabilita;
    }

    /*
     * Getter dell'attributo causale
     */
    public String getCausale() {
        return causale;
    }

    /*
     * Setter dell'attributo causale
     */
    public void setCausale(String causale) {
        this.causale = causale;
    }

    /*
     * Getter dell'attributo cd_cds_origine
     */
    public String getCd_cds_origine() {
        return cd_cds_origine;
    }

    /*
     * Setter dell'attributo cd_cds_origine
     */
    public void setCd_cds_origine(String cd_cds_origine) {
        this.cd_cds_origine = cd_cds_origine;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/02/2003 12.36.35)
     *
     * @return java.lang.String
     */
    public String getCd_proprio_sospeso() {
        return cd_proprio_sospeso;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/02/2003 12.36.35)
     *
     * @param newCd_proprio_sospeso java.lang.String
     */
    public void setCd_proprio_sospeso(String newCd_proprio_sospeso) {
        cd_proprio_sospeso = newCd_proprio_sospeso;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/02/2003 12.36.35)
     *
     * @return java.lang.String
     */
    public String getCd_sospeso_padre() {
        return cd_sospeso_padre;
    }

    /**
     * Insert the method's description here.
     * Creation date: (06/02/2003 12.36.35)
     *
     * @param newCd_sospeso_padre java.lang.String
     */
    public void setCd_sospeso_padre(String newCd_sospeso_padre) {
        cd_sospeso_padre = newCd_sospeso_padre;
    }

    /*
     * Getter dell'attributo cd_uo_origine
     */
    public String getCd_uo_origine() {
        return cd_uo_origine;
    }

    /*
     * Setter dell'attributo cd_uo_origine
     */
    public void setCd_uo_origine(String cd_uo_origine) {
        this.cd_uo_origine = cd_uo_origine;
    }

    /*
     * Getter dell'attributo ds_anagrafico
     */
    public String getDs_anagrafico() {
        return ds_anagrafico;
    }

    /*
     * Setter dell'attributo ds_anagrafico
     */
    public void setDs_anagrafico(String ds_anagrafico) {
        this.ds_anagrafico = ds_anagrafico;
    }

    /*
     * Getter dell'attributo dt_registrazione
     */
    public java.sql.Timestamp getDt_registrazione() {
        return dt_registrazione;
    }

    /*
     * Setter dell'attributo dt_registrazione
     */
    public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
        this.dt_registrazione = dt_registrazione;
    }

    /*
     * Getter dell'attributo fl_stornato
     */
    public Boolean getFl_stornato() {
        return fl_stornato;
    }

    /*
     * Setter dell'attributo fl_stornato
     */
    public void setFl_stornato(Boolean fl_stornato) {
        this.fl_stornato = fl_stornato;
    }

    /*
     * Getter dell'attributo im_ass_mod_1210
     */
    public java.math.BigDecimal getIm_ass_mod_1210() {
        return im_ass_mod_1210;
    }

    /*
     * Setter dell'attributo im_ass_mod_1210
     */
    public void setIm_ass_mod_1210(java.math.BigDecimal im_ass_mod_1210) {
        this.im_ass_mod_1210 = im_ass_mod_1210;
    }

    /*
     * Getter dell'attributo im_associato
     */
    public java.math.BigDecimal getIm_associato() {
        return im_associato;
    }

    /*
     * Setter dell'attributo im_associato
     */
    public void setIm_associato(java.math.BigDecimal im_associato) {
        this.im_associato = im_associato;
    }

    /*
     * Getter dell'attributo im_sospeso
     */
    public java.math.BigDecimal getIm_sospeso() {
        return im_sospeso;
    }

    /*
     * Setter dell'attributo im_sospeso
     */
    public void setIm_sospeso(java.math.BigDecimal im_sospeso) {
        this.im_sospeso = im_sospeso;
    }

    /*
     * Getter dell'attributo stato_sospeso
     */
    public String getStato_sospeso() {
        return stato_sospeso;
    }

    /*
     * Setter dell'attributo stato_sospeso
     */
    public void setStato_sospeso(String stato_sospeso) {
        this.stato_sospeso = stato_sospeso;
    }

    /*
     * Getter dell'attributo ti_cc_bi
     */
    public String getTi_cc_bi() {
        return ti_cc_bi;
    }

    /*
     * Setter dell'attributo ti_cc_bi
     */
    public void setTi_cc_bi(String ti_cc_bi) {
        this.ti_cc_bi = ti_cc_bi;
    }

    public java.sql.Timestamp getDt_storno() {
        return dt_storno;
    }

    public void setDt_storno(java.sql.Timestamp dtStorno) {
        dt_storno = dtStorno;
    }

	public String getCd_cds_man_riaccr() {
		return cd_cds_man_riaccr;
	}

	public void setCd_cds_man_riaccr(String cd_cds_man_riaccr) {
		this.cd_cds_man_riaccr = cd_cds_man_riaccr;
	}

	public Integer getEsercizio_man_riaccr() {
		return esercizio_man_riaccr;
	}

	public void setEsercizio_man_riaccr(Integer esercizio_man_riaccr) {
		this.esercizio_man_riaccr = esercizio_man_riaccr;
	}

	public Long getPg_mandato_man_riaccr() {
		return pg_mandato_man_riaccr;
	}

	public void setPg_mandato_man_riaccr(Long pg_mandato_man_riaccr) {
		this.pg_mandato_man_riaccr = pg_mandato_man_riaccr;
	}
}
