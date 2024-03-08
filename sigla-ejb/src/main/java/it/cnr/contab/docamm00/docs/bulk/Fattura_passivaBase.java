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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class Fattura_passivaBase extends Fattura_passivaKey implements Keyed {
    private static final long serialVersionUID = 1L;

    // CAMBIO DECIMAL(15,4) NOT NULL
    private java.math.BigDecimal cambio;

    // CD_CDS_FAT_CLGS VARCHAR(30)
    private java.lang.String cd_cds_fat_clgs;

    // CD_CDS_ORIGINE VARCHAR(30) NOT NULL
    private java.lang.String cd_cds_origine;

    // CD_DIVISA VARCHAR(10) NOT NULL
    private java.lang.String cd_divisa;

    // CD_MODALITA_PAG VARCHAR(10) NOT NULL
    private java.lang.String cd_modalita_pag;

    // CD_MODALITA_PAG_UO_CDS VARCHAR(10)
    private java.lang.String cd_modalita_pag_uo_cds;

    // CD_TERMINI_PAG VARCHAR(10)
    private java.lang.String cd_termini_pag;

    // CD_TERMINI_PAG_UO_CDS VARCHAR(10)
    private java.lang.String cd_termini_pag_uo_cds;

    // CD_TERZO DECIMAL(8,0) NOT NULL
    private java.lang.Integer cd_terzo;

    // CD_TERZO_CESSIONARIO DECIMAL(8,0)
    private java.lang.Integer cd_terzo_cessionario;

    // CD_TERZO_UO_CDS DECIMAL(8,0)
    private java.lang.Integer cd_terzo_uo_cds;

    // CD_TIPO_SEZIONALE VARCHAR(10) NOT NULL
    private java.lang.String cd_tipo_sezionale;

    // CD_UO_FAT_CLGS VARCHAR(30)
    private java.lang.String cd_uo_fat_clgs;

    // CD_UO_ORIGINE VARCHAR(30) NOT NULL
    private java.lang.String cd_uo_origine;

    // CODICE_FISCALE VARCHAR(20)
    private java.lang.String codice_fiscale;

    // COGNOME VARCHAR(50)
    private java.lang.String cognome;

    // DS_FATTURA_PASSIVA VARCHAR(1000)
    private java.lang.String ds_fattura_passiva;

    // DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
    private java.sql.Timestamp dt_a_competenza_coge;

    // DT_CANCELLAZIONE TIMESTAMP
    private java.sql.Timestamp dt_cancellazione;

    // DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
    private java.sql.Timestamp dt_da_competenza_coge;

    // DT_FATTURA_FORNITORE TIMESTAMP NOT NULL
    private java.sql.Timestamp dt_fattura_fornitore;

    // DT_PAGAMENTO_FONDO_ECO TIMESTAMP
    private java.sql.Timestamp dt_pagamento_fondo_eco;

    // DT_REGISTRAZIONE TIMESTAMP NOT NULL
    private java.sql.Timestamp dt_registrazione;

    // DT_SCADENZA TIMESTAMP
    private java.sql.Timestamp dt_scadenza;

    // ESERCIZIO_FATTURA_FORNITORE DECIMAL(4,0) NOT NULL
    private java.lang.Integer esercizio_fattura_fornitore;

    // ESERCIZIO_FAT_CLGS DECIMAL(4,0)
    private java.lang.Integer esercizio_fat_clgs;

    // ESERCIZIO_LETTERA DECIMAL(4,0)
    private java.lang.Integer esercizio_lettera;

    // FL_AUTOFATTURA CHAR(1) NOT NULL
    private java.lang.Boolean fl_autofattura;

    // FL_BOLLA_DOGANALE CHAR(1) NOT NULL
    private java.lang.Boolean fl_bolla_doganale;

    // FL_CONGELATA CHAR(1) NOT NULL
    private java.lang.Boolean fl_congelata;

    // FL_EXTRA_UE CHAR(1) NOT NULL
    private java.lang.Boolean fl_extra_ue;

    // FL_FATTURA_COMPENSO CHAR(1) NOT NULL
    private java.lang.Boolean fl_fattura_compenso;

    // FL_INTRA_UE CHAR(1) NOT NULL
    private java.lang.Boolean fl_intra_ue;

    // FL_SAN_MARINO_CON_IVA CHAR(1) NOT NULL
    private java.lang.Boolean fl_san_marino_con_iva;

    // FL_SAN_MARINO_SENZA_IVA CHAR(1) NOT NULL
    private java.lang.Boolean fl_san_marino_senza_iva;

    // FL_SPEDIZIONIERE CHAR(1) NOT NULL
    private java.lang.Boolean fl_spedizioniere;

    // FL_SPLIT_PAYMENT CHAR(1) NOT NULL
    private java.lang.Boolean fl_split_payment;

    // IM_TOTALE_FATTURA DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_totale_fattura;

    // IM_TOTALE_IMPONIBILE DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_totale_imponibile;

    // IM_TOTALE_IMPONIBILE_DIVISA DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_totale_imponibile_divisa;

    // IM_TOTALE_IVA DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_totale_iva;

    // IM_TOTALE_QUADRATURA DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_totale_quadratura;

    // NOME VARCHAR(50)
    private java.lang.String nome;

    // NOTE VARCHAR(300)
    private java.lang.String note;

    // NR_FATTURA_FORNITORE VARCHAR(20) NOT NULL
    private java.lang.String nr_fattura_fornitore;

    // NUMERO_PROTOCOLLO VARCHAR(20)
    private java.lang.String numero_protocollo;

    // PARTITA_IVA VARCHAR(20)
    private java.lang.String partita_iva;

    // PG_BANCA DECIMAL(10,0) NOT NULL
    private java.lang.Long pg_banca;

    // PG_BANCA_UO_CDS DECIMAL(10,0)
    private java.lang.Long pg_banca_uo_cds;

    // PG_FATTURA_PASSIVA_FAT_CLGS DECIMAL(10,0)
    private java.lang.Long pg_fattura_passiva_fat_clgs;

    // PG_LETTERA DECIMAL(10,0)
    private java.lang.Long pg_lettera;

    // PROTOCOLLO_IVA DECIMAL(10,0)
    private java.lang.Long protocollo_iva;

    // PROTOCOLLO_IVA_GENERALE DECIMAL(10,0)
    private java.lang.Long protocollo_iva_generale;

    // RAGIONE_SOCIALE VARCHAR(100)
    private java.lang.String ragione_sociale;

    // STATO_COAN CHAR(1) NOT NULL
    private java.lang.String stato_coan;

    // STATO_COFI CHAR(1) NOT NULL
    private java.lang.String stato_cofi;

    // STATO_COGE CHAR(1) NOT NULL
    private java.lang.String stato_coge;

    // STATO_PAGAMENTO_FONDO_ECO CHAR(1) NOT NULL
    private java.lang.String stato_pagamento_fondo_eco;

    // TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
    private java.lang.String ti_associato_manrev;

    // TI_BENE_SERVIZIO CHAR(1) NOT NULL
    private java.lang.String ti_bene_servizio;

    // TI_FATTURA CHAR(1) NOT NULL
    private java.lang.String ti_fattura;

    // TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
    private java.lang.String ti_istituz_commerc;

    // FL_MERCE_EXTRA_UE CHAR(1) NOT NULL
    private java.lang.Boolean fl_merce_extra_ue;

    // FL_LIQUIDAZIONE_DIFFERITA CHAR(1) NOT NULL
    private java.lang.Boolean fl_liquidazione_differita;

    // FL_MERCE_INTRA_UE CHAR(1) NOT NULL
    private java.lang.Boolean fl_merce_intra_ue;

    private java.lang.Long progr_univoco;

    private java.sql.Timestamp data_protocollo;

    private java.lang.String stato_liquidazione;

    private java.lang.String causale;

    // CD_CDS_COMPENSO VARCHAR(30)
    private java.lang.String cds_compenso;

    // CD_UO_COMPENSO VARCHAR(30)
    private java.lang.String uo_compenso;

    // ESERCIZIO_COMPENSO DECIMAL(4,0)
    private java.lang.Integer esercizio_compenso;

    // PG_COMPENSO DECIMAL(10,0)
    private java.lang.Long pg_compenso;
    // DT_PROTOCOLLO_LIQ TIMESTAMP
    private java.sql.Timestamp dt_protocollo_liq;
    // NR_PROTOCOLLO_LIQ VARCHAR2(20)
    private java.lang.String nr_protocollo_liq;


    private java.lang.String idPaese;
    private java.lang.String idCodice;
    private java.lang.Long identificativoSdi;
    private java.lang.Long progressivo;
    // FL_DA_ORDINI CHAR(1)
    private java.lang.Boolean flDaOrdini;

    public Fattura_passivaBase() {
        super();
    }

    public Fattura_passivaBase(java.lang.String cd_cds,
                               java.lang.String cd_unita_organizzativa,
                               java.lang.Integer esercizio, java.lang.Long pg_fattura_passiva) {
        super(cd_cds, cd_unita_organizzativa, esercizio, pg_fattura_passiva);
    }

    public java.lang.String getIdPaese() {
        return idPaese;
    }

    public void setIdPaese(java.lang.String idPaese) {
        this.idPaese = idPaese;
    }

    public java.lang.String getIdCodice() {
        return idCodice;
    }

    public void setIdCodice(java.lang.String idCodice) {
        this.idCodice = idCodice;
    }

    public java.lang.Long getIdentificativoSdi() {
        return identificativoSdi;
    }

    public void setIdentificativoSdi(java.lang.Long identificativoSdi) {
        this.identificativoSdi = identificativoSdi;
    }

    public java.lang.Long getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(java.lang.Long progressivo) {
        this.progressivo = progressivo;
    }

    /*
     * Getter dell'attributo cambio
     */
    public java.math.BigDecimal getCambio() {
        return cambio;
    }

    /*
     * Setter dell'attributo cambio
     */
    public void setCambio(java.math.BigDecimal cambio) {
        this.cambio = cambio;
    }

    /*
     * Getter dell'attributo cd_cds_fat_clgs
     */
    public java.lang.String getCd_cds_fat_clgs() {
        return cd_cds_fat_clgs;
    }

    /*
     * Setter dell'attributo cd_cds_fat_clgs
     */
    public void setCd_cds_fat_clgs(java.lang.String cd_cds_fat_clgs) {
        this.cd_cds_fat_clgs = cd_cds_fat_clgs;
    }

    /*
     * Getter dell'attributo cd_cds_origine
     */
    public java.lang.String getCd_cds_origine() {
        return cd_cds_origine;
    }

    /*
     * Setter dell'attributo cd_cds_origine
     */
    public void setCd_cds_origine(java.lang.String cd_cds_origine) {
        this.cd_cds_origine = cd_cds_origine;
    }

    /*
     * Getter dell'attributo cd_divisa
     */
    public java.lang.String getCd_divisa() {
        return cd_divisa;
    }

    /*
     * Setter dell'attributo cd_divisa
     */
    public void setCd_divisa(java.lang.String cd_divisa) {
        this.cd_divisa = cd_divisa;
    }

    /*
     * Getter dell'attributo cd_modalita_pag
     */
    public java.lang.String getCd_modalita_pag() {
        return cd_modalita_pag;
    }

    /*
     * Setter dell'attributo cd_modalita_pag
     */
    public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
        this.cd_modalita_pag = cd_modalita_pag;
    }

    /*
     * Getter dell'attributo cd_modalita_pag_uo_cds
     */
    public java.lang.String getCd_modalita_pag_uo_cds() {
        return cd_modalita_pag_uo_cds;
    }

    /*
     * Setter dell'attributo cd_modalita_pag_uo_cds
     */
    public void setCd_modalita_pag_uo_cds(java.lang.String cd_modalita_pag_uo_cds) {
        this.cd_modalita_pag_uo_cds = cd_modalita_pag_uo_cds;
    }

    /*
     * Getter dell'attributo cd_termini_pag
     */
    public java.lang.String getCd_termini_pag() {
        return cd_termini_pag;
    }

    /*
     * Setter dell'attributo cd_termini_pag
     */
    public void setCd_termini_pag(java.lang.String cd_termini_pag) {
        this.cd_termini_pag = cd_termini_pag;
    }

    /*
     * Getter dell'attributo cd_termini_pag_uo_cds
     */
    public java.lang.String getCd_termini_pag_uo_cds() {
        return cd_termini_pag_uo_cds;
    }

    /*
     * Setter dell'attributo cd_termini_pag_uo_cds
     */
    public void setCd_termini_pag_uo_cds(java.lang.String cd_termini_pag_uo_cds) {
        this.cd_termini_pag_uo_cds = cd_termini_pag_uo_cds;
    }

    /*
     * Getter dell'attributo cd_terzo
     */
    public java.lang.Integer getCd_terzo() {
        return cd_terzo;
    }

    /*
     * Setter dell'attributo cd_terzo
     */
    public void setCd_terzo(java.lang.Integer cd_terzo) {
        this.cd_terzo = cd_terzo;
    }

    /*
     * Getter dell'attributo cd_terzo_cessionario
     */
    public java.lang.Integer getCd_terzo_cessionario() {
        return cd_terzo_cessionario;
    }

    /*
     * Setter dell'attributo cd_terzo_cessionario
     */
    public void setCd_terzo_cessionario(java.lang.Integer cd_terzo_cessionario) {
        this.cd_terzo_cessionario = cd_terzo_cessionario;
    }

    /*
     * Getter dell'attributo cd_terzo_uo_cds
     */
    public java.lang.Integer getCd_terzo_uo_cds() {
        return cd_terzo_uo_cds;
    }

    /*
     * Setter dell'attributo cd_terzo_uo_cds
     */
    public void setCd_terzo_uo_cds(java.lang.Integer cd_terzo_uo_cds) {
        this.cd_terzo_uo_cds = cd_terzo_uo_cds;
    }

    /*
     * Getter dell'attributo cd_tipo_sezionale
     */
    public java.lang.String getCd_tipo_sezionale() {
        return cd_tipo_sezionale;
    }

    /*
     * Setter dell'attributo cd_tipo_sezionale
     */
    public void setCd_tipo_sezionale(java.lang.String cd_tipo_sezionale) {
        this.cd_tipo_sezionale = cd_tipo_sezionale;
    }

    /*
     * Getter dell'attributo cd_uo_fat_clgs
     */
    public java.lang.String getCd_uo_fat_clgs() {
        return cd_uo_fat_clgs;
    }

    /*
     * Setter dell'attributo cd_uo_fat_clgs
     */
    public void setCd_uo_fat_clgs(java.lang.String cd_uo_fat_clgs) {
        this.cd_uo_fat_clgs = cd_uo_fat_clgs;
    }

    /*
     * Getter dell'attributo cd_uo_origine
     */
    public java.lang.String getCd_uo_origine() {
        return cd_uo_origine;
    }

    /*
     * Setter dell'attributo cd_uo_origine
     */
    public void setCd_uo_origine(java.lang.String cd_uo_origine) {
        this.cd_uo_origine = cd_uo_origine;
    }

    /*
     * Getter dell'attributo codice_fiscale
     */
    public java.lang.String getCodice_fiscale() {
        return codice_fiscale;
    }

    /*
     * Setter dell'attributo codice_fiscale
     */
    public void setCodice_fiscale(java.lang.String codice_fiscale) {
        this.codice_fiscale = codice_fiscale;
    }

    /*
     * Getter dell'attributo cognome
     */
    public java.lang.String getCognome() {
        return cognome;
    }

    /*
     * Setter dell'attributo cognome
     */
    public void setCognome(java.lang.String cognome) {
        this.cognome = cognome;
    }

    /*
     * Getter dell'attributo ds_fattura_passiva
     */
    public java.lang.String getDs_fattura_passiva() {
        return ds_fattura_passiva;
    }

    /*
     * Setter dell'attributo ds_fattura_passiva
     */
    public void setDs_fattura_passiva(java.lang.String ds_fattura_passiva) {
        this.ds_fattura_passiva = ds_fattura_passiva;
    }

    /*
     * Getter dell'attributo dt_a_competenza_coge
     */
    public java.sql.Timestamp getDt_a_competenza_coge() {
        return dt_a_competenza_coge;
    }

    /*
     * Setter dell'attributo dt_a_competenza_coge
     */
    public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
        this.dt_a_competenza_coge = dt_a_competenza_coge;
    }

    /*
     * Getter dell'attributo dt_cancellazione
     */
    public java.sql.Timestamp getDt_cancellazione() {
        return dt_cancellazione;
    }

    /*
     * Setter dell'attributo dt_cancellazione
     */
    public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
        this.dt_cancellazione = dt_cancellazione;
    }

    /*
     * Getter dell'attributo dt_da_competenza_coge
     */
    public java.sql.Timestamp getDt_da_competenza_coge() {
        return dt_da_competenza_coge;
    }

    /*
     * Setter dell'attributo dt_da_competenza_coge
     */
    public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
        this.dt_da_competenza_coge = dt_da_competenza_coge;
    }

    /*
     * Getter dell'attributo dt_fattura_fornitore
     */
    public java.sql.Timestamp getDt_fattura_fornitore() {
        return dt_fattura_fornitore;
    }

    /*
     * Setter dell'attributo dt_fattura_fornitore
     */
    public void setDt_fattura_fornitore(java.sql.Timestamp dt_fattura_fornitore) {
        this.dt_fattura_fornitore = dt_fattura_fornitore;
    }

    /*
     * Getter dell'attributo dt_pagamento_fondo_eco
     */
    public java.sql.Timestamp getDt_pagamento_fondo_eco() {
        return dt_pagamento_fondo_eco;
    }

    /*
     * Setter dell'attributo dt_pagamento_fondo_eco
     */
    public void setDt_pagamento_fondo_eco(java.sql.Timestamp dt_pagamento_fondo_eco) {
        this.dt_pagamento_fondo_eco = dt_pagamento_fondo_eco;
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
     * Getter dell'attributo dt_scadenza
     */
    public java.sql.Timestamp getDt_scadenza() {
        return dt_scadenza;
    }

    /*
     * Setter dell'attributo dt_scadenza
     */
    public void setDt_scadenza(java.sql.Timestamp dt_scadenza) {
        this.dt_scadenza = dt_scadenza;
    }

    /*
     * Getter dell'attributo esercizio_fat_clgs
     */
    public java.lang.Integer getEsercizio_fat_clgs() {
        return esercizio_fat_clgs;
    }

    /*
     * Setter dell'attributo esercizio_fat_clgs
     */
    public void setEsercizio_fat_clgs(java.lang.Integer esercizio_fat_clgs) {
        this.esercizio_fat_clgs = esercizio_fat_clgs;
    }

    /*
     * Getter dell'attributo esercizio_fattura_fornitore
     */
    public java.lang.Integer getEsercizio_fattura_fornitore() {
        return esercizio_fattura_fornitore;
    }

    /*
     * Setter dell'attributo esercizio_fattura_fornitore
     */
    public void setEsercizio_fattura_fornitore(java.lang.Integer esercizio_fattura_fornitore) {
        this.esercizio_fattura_fornitore = esercizio_fattura_fornitore;
    }

    /*
     * Getter dell'attributo esercizio_lettera
     */
    public java.lang.Integer getEsercizio_lettera() {
        return esercizio_lettera;
    }

    /*
     * Setter dell'attributo esercizio_lettera
     */
    public void setEsercizio_lettera(java.lang.Integer esercizio_lettera) {
        this.esercizio_lettera = esercizio_lettera;
    }

    /*
     * Getter dell'attributo fl_autofattura
     */
    public java.lang.Boolean getFl_autofattura() {
        return fl_autofattura;
    }

    /*
     * Setter dell'attributo fl_autofattura
     */
    public void setFl_autofattura(java.lang.Boolean fl_autofattura) {
        this.fl_autofattura = fl_autofattura;
    }

    /*
     * Getter dell'attributo fl_bolla_doganale
     */
    public java.lang.Boolean getFl_bolla_doganale() {
        return fl_bolla_doganale;
    }

    /*
     * Setter dell'attributo fl_bolla_doganale
     */
    public void setFl_bolla_doganale(java.lang.Boolean fl_bolla_doganale) {
        this.fl_bolla_doganale = fl_bolla_doganale;
    }

    /*
     * Getter dell'attributo fl_congelata
     */
    public java.lang.Boolean getFl_congelata() {
        return fl_congelata;
    }

    /*
     * Setter dell'attributo fl_congelata
     */
    public void setFl_congelata(java.lang.Boolean fl_congelata) {
        this.fl_congelata = fl_congelata;
    }

    /*
     * Getter dell'attributo fl_extra_ue
     */
    public java.lang.Boolean getFl_extra_ue() {
        return fl_extra_ue;
    }

    /*
     * Setter dell'attributo fl_extra_ue
     */
    public void setFl_extra_ue(java.lang.Boolean fl_extra_ue) {
        this.fl_extra_ue = fl_extra_ue;
    }

    /*
     * Getter dell'attributo fl_fattura_compenso
     */
    public java.lang.Boolean getFl_fattura_compenso() {
        return fl_fattura_compenso;
    }

    /*
     * Setter dell'attributo fl_fattura_compenso
     */
    public void setFl_fattura_compenso(java.lang.Boolean fl_fattura_compenso) {
        this.fl_fattura_compenso = fl_fattura_compenso;
    }

    /*
     * Getter dell'attributo fl_intra_ue
     */
    public java.lang.Boolean getFl_intra_ue() {
        return fl_intra_ue;
    }

    /*
     * Setter dell'attributo fl_intra_ue
     */
    public void setFl_intra_ue(java.lang.Boolean fl_intra_ue) {
        this.fl_intra_ue = fl_intra_ue;
    }

    /*
     * Getter dell'attributo fl_san_marino_con_iva
     */
    public java.lang.Boolean getFl_san_marino_con_iva() {
        return fl_san_marino_con_iva;
    }

    /*
     * Setter dell'attributo fl_san_marino_con_iva
     */
    public void setFl_san_marino_con_iva(java.lang.Boolean fl_san_marino_con_iva) {
        this.fl_san_marino_con_iva = fl_san_marino_con_iva;
    }

    /*
     * Getter dell'attributo fl_san_marino_senza_iva
     */
    public java.lang.Boolean getFl_san_marino_senza_iva() {
        return fl_san_marino_senza_iva;
    }

    /*
     * Setter dell'attributo fl_san_marino_senza_iva
     */
    public void setFl_san_marino_senza_iva(java.lang.Boolean fl_san_marino_senza_iva) {
        this.fl_san_marino_senza_iva = fl_san_marino_senza_iva;
    }

    /*
     * Getter dell'attributo fl_spedizioniere
     */
    public java.lang.Boolean getFl_spedizioniere() {
        return fl_spedizioniere;
    }

    /*
     * Setter dell'attributo fl_spedizioniere
     */
    public void setFl_spedizioniere(java.lang.Boolean fl_spedizioniere) {
        this.fl_spedizioniere = fl_spedizioniere;
    }

    /*
     * Getter dell'attributo im_totale_fattura
     */
    public java.math.BigDecimal getIm_totale_fattura() {
        return im_totale_fattura;
    }

    /*
     * Setter dell'attributo im_totale_fattura
     */
    public void setIm_totale_fattura(java.math.BigDecimal im_totale_fattura) {
        this.im_totale_fattura = im_totale_fattura;
    }

    /*
     * Getter dell'attributo im_totale_imponibile
     */
    public java.math.BigDecimal getIm_totale_imponibile() {
        return im_totale_imponibile;
    }

    /*
     * Setter dell'attributo im_totale_imponibile
     */
    public void setIm_totale_imponibile(java.math.BigDecimal im_totale_imponibile) {
        this.im_totale_imponibile = im_totale_imponibile;
    }

    /*
     * Getter dell'attributo im_totale_imponibile_divisa
     */
    public java.math.BigDecimal getIm_totale_imponibile_divisa() {
        return im_totale_imponibile_divisa;
    }

    /*
     * Setter dell'attributo im_totale_imponibile_divisa
     */
    public void setIm_totale_imponibile_divisa(java.math.BigDecimal im_totale_imponibile_divisa) {
        this.im_totale_imponibile_divisa = im_totale_imponibile_divisa;
    }

    /*
     * Getter dell'attributo im_totale_iva
     */
    public java.math.BigDecimal getIm_totale_iva() {
        return im_totale_iva;
    }

    /*
     * Setter dell'attributo im_totale_iva
     */
    public void setIm_totale_iva(java.math.BigDecimal im_totale_iva) {
        this.im_totale_iva = im_totale_iva;
    }

    /*
     * Getter dell'attributo im_totale_quadratura
     */
    public java.math.BigDecimal getIm_totale_quadratura() {
        return im_totale_quadratura;
    }

    /*
     * Setter dell'attributo im_totale_quadratura
     */
    public void setIm_totale_quadratura(java.math.BigDecimal im_totale_quadratura) {
        this.im_totale_quadratura = im_totale_quadratura;
    }

    /*
     * Getter dell'attributo nome
     */
    public java.lang.String getNome() {
        return nome;
    }

    /*
     * Setter dell'attributo nome
     */
    public void setNome(java.lang.String nome) {
        this.nome = nome;
    }

    /*
     * Getter dell'attributo note
     */
    public java.lang.String getNote() {
        return note;
    }

    /*
     * Setter dell'attributo note
     */
    public void setNote(java.lang.String note) {
        this.note = note;
    }

    /*
     * Getter dell'attributo nr_fattura_fornitore
     */
    public java.lang.String getNr_fattura_fornitore() {
        return nr_fattura_fornitore;
    }

    /*
     * Setter dell'attributo nr_fattura_fornitore
     */
    public void setNr_fattura_fornitore(java.lang.String nr_fattura_fornitore) {
        this.nr_fattura_fornitore = nr_fattura_fornitore;
    }

    /*
     * Getter dell'attributo numero_protocollo
     */
    public java.lang.String getNumero_protocollo() {
        return numero_protocollo;
    }

    /*
     * Setter dell'attributo numero_protocollo
     */
    public void setNumero_protocollo(java.lang.String numero_protocollo) {
        this.numero_protocollo = numero_protocollo;
    }

    /*
     * Getter dell'attributo partita_iva
     */
    public java.lang.String getPartita_iva() {
        return partita_iva;
    }

    /*
     * Setter dell'attributo partita_iva
     */
    public void setPartita_iva(java.lang.String partita_iva) {
        this.partita_iva = partita_iva;
    }

    /*
     * Getter dell'attributo pg_banca
     */
    public java.lang.Long getPg_banca() {
        return pg_banca;
    }

    /*
     * Setter dell'attributo pg_banca
     */
    public void setPg_banca(java.lang.Long pg_banca) {
        this.pg_banca = pg_banca;
    }

    /*
     * Getter dell'attributo pg_banca_uo_cds
     */
    public java.lang.Long getPg_banca_uo_cds() {
        return pg_banca_uo_cds;
    }

    /*
     * Setter dell'attributo pg_banca_uo_cds
     */
    public void setPg_banca_uo_cds(java.lang.Long pg_banca_uo_cds) {
        this.pg_banca_uo_cds = pg_banca_uo_cds;
    }

    /*
     * Getter dell'attributo pg_fattura_passiva_fat_clgs
     */
    public java.lang.Long getPg_fattura_passiva_fat_clgs() {
        return pg_fattura_passiva_fat_clgs;
    }

    /*
     * Setter dell'attributo pg_fattura_passiva_fat_clgs
     */
    public void setPg_fattura_passiva_fat_clgs(java.lang.Long pg_fattura_passiva_fat_clgs) {
        this.pg_fattura_passiva_fat_clgs = pg_fattura_passiva_fat_clgs;
    }

    /*
     * Getter dell'attributo pg_lettera
     */
    public java.lang.Long getPg_lettera() {
        return pg_lettera;
    }

    /*
     * Setter dell'attributo pg_lettera
     */
    public void setPg_lettera(java.lang.Long pg_lettera) {
        this.pg_lettera = pg_lettera;
    }

    /*
     * Getter dell'attributo protocollo_iva
     */
    public java.lang.Long getProtocollo_iva() {
        return protocollo_iva;
    }

    /*
     * Setter dell'attributo protocollo_iva
     */
    public void setProtocollo_iva(java.lang.Long protocollo_iva) {
        this.protocollo_iva = protocollo_iva;
    }

    /*
     * Getter dell'attributo protocollo_iva_generale
     */
    public java.lang.Long getProtocollo_iva_generale() {
        return protocollo_iva_generale;
    }

    /*
     * Setter dell'attributo protocollo_iva_generale
     */
    public void setProtocollo_iva_generale(java.lang.Long protocollo_iva_generale) {
        this.protocollo_iva_generale = protocollo_iva_generale;
    }

    /*
     * Getter dell'attributo ragione_sociale
     */
    public java.lang.String getRagione_sociale() {
        return ragione_sociale;
    }

    /*
     * Setter dell'attributo ragione_sociale
     */
    public void setRagione_sociale(java.lang.String ragione_sociale) {
        this.ragione_sociale = ragione_sociale;
    }

    /*
     * Getter dell'attributo stato_coan
     */
    public java.lang.String getStato_coan() {
        return stato_coan;
    }

    /*
     * Setter dell'attributo stato_coan
     */
    public void setStato_coan(java.lang.String stato_coan) {
        this.stato_coan = stato_coan;
    }

    /*
     * Getter dell'attributo stato_cofi
     */
    public java.lang.String getStato_cofi() {
        return stato_cofi;
    }

    /*
     * Setter dell'attributo stato_cofi
     */
    public void setStato_cofi(java.lang.String stato_cofi) {
        this.stato_cofi = stato_cofi;
    }

    /*
     * Getter dell'attributo stato_coge
     */
    public java.lang.String getStato_coge() {
        return stato_coge;
    }

    /*
     * Setter dell'attributo stato_coge
     */
    public void setStato_coge(java.lang.String stato_coge) {
        this.stato_coge = stato_coge;
    }

    /*
     * Getter dell'attributo stato_pagamento_fondo_eco
     */
    public java.lang.String getStato_pagamento_fondo_eco() {
        return stato_pagamento_fondo_eco;
    }

    /*
     * Setter dell'attributo stato_pagamento_fondo_eco
     */
    public void setStato_pagamento_fondo_eco(java.lang.String stato_pagamento_fondo_eco) {
        this.stato_pagamento_fondo_eco = stato_pagamento_fondo_eco;
    }

    /*
     * Getter dell'attributo ti_associato_manrev
     */
    public java.lang.String getTi_associato_manrev() {
        return ti_associato_manrev;
    }

    /*
     * Setter dell'attributo ti_associato_manrev
     */
    public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
        this.ti_associato_manrev = ti_associato_manrev;
    }

    /*
     * Getter dell'attributo ti_bene_servizio
     */
    public java.lang.String getTi_bene_servizio() {
        return ti_bene_servizio;
    }

    /*
     * Setter dell'attributo ti_bene_servizio
     */
    public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
        this.ti_bene_servizio = ti_bene_servizio;
    }

    /*
     * Getter dell'attributo ti_fattura
     */
    public java.lang.String getTi_fattura() {
        return ti_fattura;
    }

    /*
     * Setter dell'attributo ti_fattura
     */
    public void setTi_fattura(java.lang.String ti_fattura) {
        this.ti_fattura = ti_fattura;
    }

    /*
     * Getter dell'attributo ti_istituz_commerc
     */
    public java.lang.String getTi_istituz_commerc() {
        return ti_istituz_commerc;
    }

    /*
     * Setter dell'attributo ti_istituz_commerc
     */
    public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
        this.ti_istituz_commerc = ti_istituz_commerc;
    }

    public java.lang.Boolean getFl_merce_extra_ue() {
        return fl_merce_extra_ue;
    }

    public void setFl_merce_extra_ue(java.lang.Boolean fl_merce_extra_ue) {
        this.fl_merce_extra_ue = fl_merce_extra_ue;
    }

    /*
     * Getter dell'attributo fl_liquidazione_differita
     */
    public java.lang.Boolean getFl_liquidazione_differita() {
        return fl_liquidazione_differita;
    }

    /*
     * Setter dell'attributo fl_liquidazione_differita
     */
    public void setFl_liquidazione_differita(java.lang.Boolean fl_liquidazione_differita) {
        this.fl_liquidazione_differita = fl_liquidazione_differita;
    }

    public java.lang.Boolean getFl_merce_intra_ue() {
        return fl_merce_intra_ue;
    }

    public void setFl_merce_intra_ue(java.lang.Boolean flMerceIntraUe) {
        fl_merce_intra_ue = flMerceIntraUe;
    }

    public java.lang.Long getProgr_univoco() {
        return progr_univoco;
    }

    public void setProgr_univoco(java.lang.Long progr_univoco) {
        this.progr_univoco = progr_univoco;
    }

    public java.sql.Timestamp getData_protocollo() {
        return data_protocollo;
    }

    public void setData_protocollo(java.sql.Timestamp data_protocollo) {
        this.data_protocollo = data_protocollo;
    }

    public java.lang.String getStato_liquidazione() {
        return stato_liquidazione;
    }

    public void setStato_liquidazione(java.lang.String stato_liquidazione) {
        this.stato_liquidazione = stato_liquidazione;
    }

    public java.lang.String getCausale() {
        return causale;
    }

    public void setCausale(java.lang.String causale) {
        this.causale = causale;
    }

    public java.lang.String getCds_compenso() {
        return cds_compenso;
    }

    public void setCds_compenso(java.lang.String cds_compenso) {
        this.cds_compenso = cds_compenso;
    }

    public java.lang.String getUo_compenso() {
        return uo_compenso;
    }

    public void setUo_compenso(java.lang.String uo_compenso) {
        this.uo_compenso = uo_compenso;
    }

    public java.lang.Integer getEsercizio_compenso() {
        return esercizio_compenso;
    }

    public void setEsercizio_compenso(java.lang.Integer esercizio_compenso) {
        this.esercizio_compenso = esercizio_compenso;
    }

    public java.lang.Long getPg_compenso() {
        return pg_compenso;
    }

    public void setPg_compenso(java.lang.Long pg_compenso) {
        this.pg_compenso = pg_compenso;
    }

    public java.lang.Boolean getFl_split_payment() {
        return fl_split_payment;
    }

    public void setFl_split_payment(java.lang.Boolean fl_split_payment) {
        this.fl_split_payment = fl_split_payment;
    }

    public Boolean getFlDaOrdini() {
        return flDaOrdini;
    }

    public void setFlDaOrdini(Boolean flDaOrdini) {
        this.flDaOrdini = flDaOrdini;
    }

    public Timestamp getDt_protocollo_liq() {
        return dt_protocollo_liq;
    }

    public void setDt_protocollo_liq(Timestamp dt_protocollo_liq) {
        this.dt_protocollo_liq = dt_protocollo_liq;
    }

    public String getNr_protocollo_liq() {
        return nr_protocollo_liq;
    }

    public void setNr_protocollo_liq(String nr_protocollo_liq) {
        this.nr_protocollo_liq = nr_protocollo_liq;
    }
}
