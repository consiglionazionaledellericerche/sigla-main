package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class BancaBase extends BancaKey implements Keyed {
    // ABI CHAR(5)
    private java.lang.String abi;

    // CAB CHAR(5)
    private java.lang.String cab;

    // CD_TERZO_DELEGATO DECIMAL(8,0)
    private java.lang.Integer cd_terzo_delegato;

    // CIN VARCHAR(1)
    private java.lang.String cin;

    // CODICE_IBAN VARCHAR(34)
    private java.lang.String codice_iban;

    // CODICE_SWIFT VARCHAR(20)
    private java.lang.String codice_swift;

    // FL_CANCELLATO CHAR(1) NOT NULL
    private java.lang.Boolean fl_cancellato;

    // FL_CC_CDS CHAR(1) NOT NULL
    private java.lang.Boolean fl_cc_cds;

    // INTESTAZIONE VARCHAR(200)
    private java.lang.String intestazione;

    // NUMERO_CONTO VARCHAR(30)
    private java.lang.String numero_conto;

    // ORIGINE CHAR(1) NOT NULL
    private java.lang.String origine;

    // PG_BANCA_DELEGATO DECIMAL(10,0)
    private java.lang.Long pg_banca_delegato;

    // QUIETANZA VARCHAR(100)
    private java.lang.String quietanza;

    // TI_PAGAMENTO CHAR(1) NOT NULL
    private java.lang.String ti_pagamento;

    // TIPO_POSTALIZZAZIONE VARCHAR(250) NULL
    private java.lang.String tipo_postalizzazione;

    private java.lang.String tipo_pagamento_siope;

    public BancaBase() {
        super();
    }

    public BancaBase(java.lang.Integer cd_terzo, java.lang.Long pg_banca) {
        super(cd_terzo, pg_banca);
    }

    /*
     * Getter dell'attributo abi
     */
    public java.lang.String getAbi() {
        return abi;
    }

    /*
     * Setter dell'attributo abi
     */
    public void setAbi(java.lang.String abi) {
        this.abi = abi;
    }

    /*
     * Getter dell'attributo cab
     */
    public java.lang.String getCab() {
        return cab;
    }

    /*
     * Setter dell'attributo cab
     */
    public void setCab(java.lang.String cab) {
        this.cab = cab;
    }

    /*
     * Getter dell'attributo cd_terzo_delegato
     */
    public java.lang.Integer getCd_terzo_delegato() {
        return cd_terzo_delegato;
    }

    /*
     * Setter dell'attributo cd_terzo_delegato
     */
    public void setCd_terzo_delegato(java.lang.Integer cd_terzo_delegato) {
        this.cd_terzo_delegato = cd_terzo_delegato;
    }

    /*
     * Getter dell'attributo cin
     */
    public java.lang.String getCin() {
        return cin;
    }

    /*
     * Setter dell'attributo cin
     */
    public void setCin(java.lang.String cin) {
        this.cin = cin;
    }

    /*
     * Getter dell'attributo codice_iban
     */
    public java.lang.String getCodice_iban() {
        return codice_iban;
    }

    /*
     * Setter dell'attributo codice_iban
     */
    public void setCodice_iban(java.lang.String codice_iban) {
        this.codice_iban = codice_iban;
    }

    /*
     * Getter dell'attributo codice_swift
     */
    public java.lang.String getCodice_swift() {
        return codice_swift;
    }

    /*
     * Setter dell'attributo codice_swift
     */
    public void setCodice_swift(java.lang.String codice_swift) {
        this.codice_swift = codice_swift;
    }

    /*
     * Getter dell'attributo fl_cancellato
     */
    public java.lang.Boolean getFl_cancellato() {
        return fl_cancellato;
    }

    /*
     * Setter dell'attributo fl_cancellato
     */
    public void setFl_cancellato(java.lang.Boolean fl_cancellato) {
        this.fl_cancellato = fl_cancellato;
    }

    /*
     * Getter dell'attributo fl_cc_cds
     */
    public java.lang.Boolean getFl_cc_cds() {
        return fl_cc_cds;
    }

    /*
     * Setter dell'attributo fl_cc_cds
     */
    public void setFl_cc_cds(java.lang.Boolean fl_cc_cds) {
        this.fl_cc_cds = fl_cc_cds;
    }

    /*
     * Getter dell'attributo intestazione
     */
    public java.lang.String getIntestazione() {
        return intestazione;
    }

    /*
     * Setter dell'attributo intestazione
     */
    public void setIntestazione(java.lang.String intestazione) {
        this.intestazione = intestazione;
    }

    /*
     * Getter dell'attributo numero_conto
     */
    public java.lang.String getNumero_conto() {
        return numero_conto;
    }

    /*
     * Setter dell'attributo numero_conto
     */
    public void setNumero_conto(java.lang.String numero_conto) {
        this.numero_conto = numero_conto;
    }

    /*
     * Getter dell'attributo origine
     */
    public java.lang.String getOrigine() {
        return origine;
    }

    /*
     * Setter dell'attributo origine
     */
    public void setOrigine(java.lang.String origine) {
        this.origine = origine;
    }

    /*
     * Getter dell'attributo pg_banca_delegato
     */
    public java.lang.Long getPg_banca_delegato() {
        return pg_banca_delegato;
    }

    /*
     * Setter dell'attributo pg_banca_delegato
     */
    public void setPg_banca_delegato(java.lang.Long pg_banca_delegato) {
        this.pg_banca_delegato = pg_banca_delegato;
    }

    /*
     * Getter dell'attributo quietanza
     */
    public java.lang.String getQuietanza() {
        return quietanza;
    }

    /*
     * Setter dell'attributo quietanza
     */
    public void setQuietanza(java.lang.String quietanza) {
        this.quietanza = quietanza;
    }

    /*
     * Getter dell'attributo ti_pagamento
     */
    public java.lang.String getTi_pagamento() {
        return ti_pagamento;
    }

    /*
     * Setter dell'attributo ti_pagamento
     */
    public void setTi_pagamento(java.lang.String ti_pagamento) {
        this.ti_pagamento = ti_pagamento;
    }

    public String getTipo_postalizzazione() {
        return tipo_postalizzazione;
    }

    public void setTipo_postalizzazione(String tipo_postalizzazione) {
        this.tipo_postalizzazione = tipo_postalizzazione;
    }

    public String getTipo_pagamento_siope() {
        return tipo_pagamento_siope;
    }

    public void setTipo_pagamento_siope(String tipo_pagamento_siope) {
        this.tipo_pagamento_siope = tipo_pagamento_siope;
    }
}
