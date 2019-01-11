package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.persistency.Keyed;

public class Fattura_passiva_rigaBase extends Fattura_passiva_rigaKey implements Keyed {
	// CD_BENE_SERVIZIO VARCHAR(10) NOT NULL
	private java.lang.String cd_bene_servizio;

	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// CD_CDS_ASSNCNA_ECO VARCHAR(30)
	private java.lang.String cd_cds_assncna_eco;

	// CD_CDS_ASSNCNA_FIN VARCHAR(30)
	private java.lang.String cd_cds_assncna_fin;

	// CD_CDS_OBBLIGAZIONE VARCHAR(30)
	private java.lang.String cd_cds_obbligazione;

	// CD_UO_ASSNCNA_ECO VARCHAR(30)
	private java.lang.String cd_uo_assncna_eco;

	// CD_UO_ASSNCNA_FIN VARCHAR(30)
	private java.lang.String cd_uo_assncna_fin;

	// CD_VOCE_IVA VARCHAR(10) NOT NULL
	private java.lang.String cd_voce_iva;

	// DS_RIGA_FATTURA VARCHAR(200)
	private java.lang.String ds_riga_fattura;

	// DT_A_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_a_competenza_coge;

	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

	// DT_DA_COMPETENZA_COGE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_da_competenza_coge;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_ASSNCNA_ECO DECIMAL(4,0)
	private java.lang.Integer esercizio_assncna_eco;

	// ESERCIZIO_ASSNCNA_FIN DECIMAL(4,0)
	private java.lang.Integer esercizio_assncna_fin;

	// ESERCIZIO_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_obbligazione;

	// FL_IVA_FORZATA CHAR(1) NOT NULL
	private java.lang.Boolean fl_iva_forzata;

	// IM_DIPONIBILE_NC DECIMAL(15,2)
	private java.math.BigDecimal im_diponibile_nc;

	// IM_IMPONIBILE DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_imponibile;

	// IM_IVA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_iva;

	// IM_TOTALE_DIVISA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_totale_divisa;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	// PG_ACCERTAMENTO_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_accertamento_scadenzario;

	// PG_FATTURA_ASSNCNA_ECO DECIMAL(10,0)
	private java.lang.Long pg_fattura_assncna_eco;

	// PG_FATTURA_ASSNCNA_FIN DECIMAL(10,0)
	private java.lang.Long pg_fattura_assncna_fin;

	// ESERCIZIO_ORI_OBBLIGAZIONE DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_obbligazione;

	// PG_OBBLIGAZIONE DECIMAL(10,0)
	private java.lang.Long pg_obbligazione;

	// PG_OBBLIGAZIONE_SCADENZARIO DECIMAL(10,0)
	private java.lang.Long pg_obbligazione_scadenzario;

	// PG_RIGA_ASSNCNA_ECO DECIMAL(10,0)
	private java.lang.Long pg_riga_assncna_eco;

	// PG_RIGA_ASSNCNA_FIN DECIMAL(10,0)
	private java.lang.Long pg_riga_assncna_fin;

	// PREZZO_UNITARIO DECIMAL(20,6) NOT NULL
	private java.math.BigDecimal prezzo_unitario;

	// QUANTITA DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal quantita;

	// STATO_COFI CHAR(1) NOT NULL
	private java.lang.String stato_cofi;

	// TI_ASSOCIATO_MANREV CHAR(1) NOT NULL
	private java.lang.String ti_associato_manrev;

	// TI_ISTITUZ_COMMERC CHAR(1) NOT NULL
	private java.lang.String ti_istituz_commerc;

	// CD_MODALITA_PAG VARCHAR(10) NOT NULL
	private java.lang.String cd_modalita_pag;

	// CD_TERMINI_PAG VARCHAR(10)
	private java.lang.String cd_termini_pag;

	// CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;

	// CD_TERZO_CESSIONARIO DECIMAL(8,0)
	private java.lang.Integer cd_terzo_cessionario;

	// PG_BANCA DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_banca;

	// DATA_ESIGIBILITA_IVA TIMESTAMP
	private java.sql.Timestamp data_esigibilita_iva;
	
	// PG_TROVATO DECIMAL(10,0)
	private java.lang.Long pg_trovato;

    private String motivo_assenza_cig;

public Fattura_passiva_rigaBase() {
	super();
}
public Fattura_passiva_rigaBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_passiva,java.lang.Long progressivo_riga) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva,progressivo_riga);
}
/* 
 * Getter dell'attributo cd_bene_servizio
 */
public java.lang.String getCd_bene_servizio() {
	return cd_bene_servizio;
}
/* 
 * Getter dell'attributo cd_cds_accertamento
 */
public java.lang.String getCd_cds_accertamento() {
	return cd_cds_accertamento;
}
/* 
 * Getter dell'attributo cd_cds_assncna_eco
 */
public java.lang.String getCd_cds_assncna_eco() {
	return cd_cds_assncna_eco;
}
/* 
 * Getter dell'attributo cd_cds_assncna_fin
 */
public java.lang.String getCd_cds_assncna_fin() {
	return cd_cds_assncna_fin;
}
/* 
 * Getter dell'attributo cd_cds_obbligazione
 */
public java.lang.String getCd_cds_obbligazione() {
	return cd_cds_obbligazione;
}
/* 
 * Getter dell'attributo cd_uo_assncna_eco
 */
public java.lang.String getCd_uo_assncna_eco() {
	return cd_uo_assncna_eco;
}
/* 
 * Getter dell'attributo cd_uo_assncna_fin
 */
public java.lang.String getCd_uo_assncna_fin() {
	return cd_uo_assncna_fin;
}
/* 
 * Getter dell'attributo cd_voce_iva
 */
public java.lang.String getCd_voce_iva() {
	return cd_voce_iva;
}
/* 
 * Getter dell'attributo ds_riga_fattura
 */
public java.lang.String getDs_riga_fattura() {
	return ds_riga_fattura;
}
/* 
 * Getter dell'attributo dt_a_competenza_coge
 */
public java.sql.Timestamp getDt_a_competenza_coge() {
	return dt_a_competenza_coge;
}
/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}
/* 
 * Getter dell'attributo dt_da_competenza_coge
 */
public java.sql.Timestamp getDt_da_competenza_coge() {
	return dt_da_competenza_coge;
}
/* 
 * Getter dell'attributo esercizio_accertamento
 */
public java.lang.Integer getEsercizio_accertamento() {
	return esercizio_accertamento;
}
/* 
 * Getter dell'attributo esercizio_assncna_eco
 */
public java.lang.Integer getEsercizio_assncna_eco() {
	return esercizio_assncna_eco;
}
/* 
 * Getter dell'attributo esercizio_assncna_fin
 */
public java.lang.Integer getEsercizio_assncna_fin() {
	return esercizio_assncna_fin;
}
/* 
 * Getter dell'attributo esercizio_obbligazione
 */
public java.lang.Integer getEsercizio_obbligazione() {
	return esercizio_obbligazione;
}
/* 
 * Getter dell'attributo fl_iva_forzata
 */
public java.lang.Boolean getFl_iva_forzata() {
	return fl_iva_forzata;
}
/* 
 * Getter dell'attributo im_diponibile_nc
 */
public java.math.BigDecimal getIm_diponibile_nc() {
	return im_diponibile_nc;
}
/* 
 * Getter dell'attributo im_imponibile
 */
public java.math.BigDecimal getIm_imponibile() {
	return im_imponibile;
}
/* 
 * Getter dell'attributo im_iva
 */
public java.math.BigDecimal getIm_iva() {
	return im_iva;
}
/* 
 * Getter dell'attributo im_totale_divisa
 */
public java.math.BigDecimal getIm_totale_divisa() {
	return im_totale_divisa;
}
/* 
 * Getter dell'attributo esercizio_ori_accertamento
 */
public java.lang.Integer getEsercizio_ori_accertamento() {
	return esercizio_ori_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento
 */
public java.lang.Long getPg_accertamento() {
	return pg_accertamento;
}
/* 
 * Getter dell'attributo pg_accertamento_scadenzario
 */
public java.lang.Long getPg_accertamento_scadenzario() {
	return pg_accertamento_scadenzario;
}
/* 
 * Getter dell'attributo pg_fattura_assncna_eco
 */
public java.lang.Long getPg_fattura_assncna_eco() {
	return pg_fattura_assncna_eco;
}
/* 
 * Getter dell'attributo pg_fattura_assncna_fin
 */
public java.lang.Long getPg_fattura_assncna_fin() {
	return pg_fattura_assncna_fin;
}
/* 
 * Getter dell'attributo esercizio_ori_obbligazione
 */
public java.lang.Integer getEsercizio_ori_obbligazione() {
	return esercizio_ori_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione
 */
public java.lang.Long getPg_obbligazione() {
	return pg_obbligazione;
}
/* 
 * Getter dell'attributo pg_obbligazione_scadenzario
 */
public java.lang.Long getPg_obbligazione_scadenzario() {
	return pg_obbligazione_scadenzario;
}
/* 
 * Getter dell'attributo pg_riga_assncna_eco
 */
public java.lang.Long getPg_riga_assncna_eco() {
	return pg_riga_assncna_eco;
}
/* 
 * Getter dell'attributo pg_riga_assncna_fin
 */
public java.lang.Long getPg_riga_assncna_fin() {
	return pg_riga_assncna_fin;
}
/* 
 * Getter dell'attributo prezzo_unitario
 */
public java.math.BigDecimal getPrezzo_unitario() {
	return prezzo_unitario;
}
/* 
 * Getter dell'attributo quantita
 */
public java.math.BigDecimal getQuantita() {
	return quantita;
}
/* 
 * Getter dell'attributo stato_cofi
 */
public java.lang.String getStato_cofi() {
	return stato_cofi;
}
/* 
 * Getter dell'attributo ti_associato_manrev
 */
public java.lang.String getTi_associato_manrev() {
	return ti_associato_manrev;
}
/* 
 * Getter dell'attributo ti_istituz_commerc
 */
public java.lang.String getTi_istituz_commerc() {
	return ti_istituz_commerc;
}
/* 
 * Setter dell'attributo cd_bene_servizio
 */
public void setCd_bene_servizio(java.lang.String cd_bene_servizio) {
	this.cd_bene_servizio = cd_bene_servizio;
}
/* 
 * Setter dell'attributo cd_cds_accertamento
 */
public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
	this.cd_cds_accertamento = cd_cds_accertamento;
}
/* 
 * Setter dell'attributo cd_cds_assncna_eco
 */
public void setCd_cds_assncna_eco(java.lang.String cd_cds_assncna_eco) {
	this.cd_cds_assncna_eco = cd_cds_assncna_eco;
}
/* 
 * Setter dell'attributo cd_cds_assncna_fin
 */
public void setCd_cds_assncna_fin(java.lang.String cd_cds_assncna_fin) {
	this.cd_cds_assncna_fin = cd_cds_assncna_fin;
}
/* 
 * Setter dell'attributo cd_cds_obbligazione
 */
public void setCd_cds_obbligazione(java.lang.String cd_cds_obbligazione) {
	this.cd_cds_obbligazione = cd_cds_obbligazione;
}
/* 
 * Setter dell'attributo cd_uo_assncna_eco
 */
public void setCd_uo_assncna_eco(java.lang.String cd_uo_assncna_eco) {
	this.cd_uo_assncna_eco = cd_uo_assncna_eco;
}
/* 
 * Setter dell'attributo cd_uo_assncna_fin
 */
public void setCd_uo_assncna_fin(java.lang.String cd_uo_assncna_fin) {
	this.cd_uo_assncna_fin = cd_uo_assncna_fin;
}
/* 
 * Setter dell'attributo cd_voce_iva
 */
public void setCd_voce_iva(java.lang.String cd_voce_iva) {
	this.cd_voce_iva = cd_voce_iva;
}
/* 
 * Setter dell'attributo ds_riga_fattura
 */
public void setDs_riga_fattura(java.lang.String ds_riga_fattura) {
	this.ds_riga_fattura = ds_riga_fattura;
}
/* 
 * Setter dell'attributo dt_a_competenza_coge
 */
public void setDt_a_competenza_coge(java.sql.Timestamp dt_a_competenza_coge) {
	this.dt_a_competenza_coge = dt_a_competenza_coge;
}
/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
/* 
 * Setter dell'attributo dt_da_competenza_coge
 */
public void setDt_da_competenza_coge(java.sql.Timestamp dt_da_competenza_coge) {
	this.dt_da_competenza_coge = dt_da_competenza_coge;
}
/* 
 * Setter dell'attributo esercizio_accertamento
 */
public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
	this.esercizio_accertamento = esercizio_accertamento;
}
/* 
 * Setter dell'attributo esercizio_assncna_eco
 */
public void setEsercizio_assncna_eco(java.lang.Integer esercizio_assncna_eco) {
	this.esercizio_assncna_eco = esercizio_assncna_eco;
}
/* 
 * Setter dell'attributo esercizio_assncna_fin
 */
public void setEsercizio_assncna_fin(java.lang.Integer esercizio_assncna_fin) {
	this.esercizio_assncna_fin = esercizio_assncna_fin;
}
/* 
 * Setter dell'attributo esercizio_obbligazione
 */
public void setEsercizio_obbligazione(java.lang.Integer esercizio_obbligazione) {
	this.esercizio_obbligazione = esercizio_obbligazione;
}
/* 
 * Setter dell'attributo fl_iva_forzata
 */
public void setFl_iva_forzata(java.lang.Boolean fl_iva_forzata) {
	this.fl_iva_forzata = fl_iva_forzata;
}
/* 
 * Setter dell'attributo im_diponibile_nc
 */
public void setIm_diponibile_nc(java.math.BigDecimal im_diponibile_nc) {
	this.im_diponibile_nc = im_diponibile_nc;
}
/* 
 * Setter dell'attributo im_imponibile
 */
public void setIm_imponibile(java.math.BigDecimal im_imponibile) {
	this.im_imponibile = im_imponibile;
}
/* 
 * Setter dell'attributo im_iva
 */
public void setIm_iva(java.math.BigDecimal im_iva) {
	this.im_iva = im_iva;
}
/* 
 * Setter dell'attributo im_totale_divisa
 */
public void setIm_totale_divisa(java.math.BigDecimal im_totale_divisa) {
	this.im_totale_divisa = im_totale_divisa;
}
/* 
 * Setter dell'attributo esercizio_ori_accertamento
 */
public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
	this.esercizio_ori_accertamento = esercizio_ori_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento
 */
public void setPg_accertamento(java.lang.Long pg_accertamento) {
	this.pg_accertamento = pg_accertamento;
}
/* 
 * Setter dell'attributo pg_accertamento_scadenzario
 */
public void setPg_accertamento_scadenzario(java.lang.Long pg_accertamento_scadenzario) {
	this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
}
/* 
 * Setter dell'attributo pg_fattura_assncna_eco
 */
public void setPg_fattura_assncna_eco(java.lang.Long pg_fattura_assncna_eco) {
	this.pg_fattura_assncna_eco = pg_fattura_assncna_eco;
}
/* 
 * Setter dell'attributo pg_fattura_assncna_fin
 */
public void setPg_fattura_assncna_fin(java.lang.Long pg_fattura_assncna_fin) {
	this.pg_fattura_assncna_fin = pg_fattura_assncna_fin;
}
/* 
 * Setter dell'attributo esercizio_ori_obbligazione
 */
public void setEsercizio_ori_obbligazione(java.lang.Integer esercizio_ori_obbligazione) {
	this.esercizio_ori_obbligazione = esercizio_ori_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione
 */
public void setPg_obbligazione(java.lang.Long pg_obbligazione) {
	this.pg_obbligazione = pg_obbligazione;
}
/* 
 * Setter dell'attributo pg_obbligazione_scadenzario
 */
public void setPg_obbligazione_scadenzario(java.lang.Long pg_obbligazione_scadenzario) {
	this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
}
/* 
 * Setter dell'attributo pg_riga_assncna_eco
 */
public void setPg_riga_assncna_eco(java.lang.Long pg_riga_assncna_eco) {
	this.pg_riga_assncna_eco = pg_riga_assncna_eco;
}
/* 
 * Setter dell'attributo pg_riga_assncna_fin
 */
public void setPg_riga_assncna_fin(java.lang.Long pg_riga_assncna_fin) {
	this.pg_riga_assncna_fin = pg_riga_assncna_fin;
}
/* 
 * Setter dell'attributo prezzo_unitario
 */
public void setPrezzo_unitario(java.math.BigDecimal prezzo_unitario) {
	this.prezzo_unitario = prezzo_unitario;
}
/* 
 * Setter dell'attributo quantita
 */
public void setQuantita(java.math.BigDecimal quantita) {
	this.quantita = quantita;
}
/* 
 * Setter dell'attributo stato_cofi
 */
public void setStato_cofi(java.lang.String stato_cofi) {
	this.stato_cofi = stato_cofi;
}
/* 
 * Setter dell'attributo ti_associato_manrev
 */
public void setTi_associato_manrev(java.lang.String ti_associato_manrev) {
	this.ti_associato_manrev = ti_associato_manrev;
}
/* 
 * Setter dell'attributo ti_istituz_commerc
 */
public void setTi_istituz_commerc(java.lang.String ti_istituz_commerc) {
	this.ti_istituz_commerc = ti_istituz_commerc;
}
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
public java.lang.Integer getCd_terzo_cessionario() {
	return cd_terzo_cessionario;
}
public void setCd_terzo_cessionario(java.lang.Integer cd_terzo_cessionario) {
	this.cd_terzo_cessionario = cd_terzo_cessionario;
}
public java.lang.Long getPg_banca() {
	return pg_banca;
}
public void setPg_banca(java.lang.Long pg_banca) {
	this.pg_banca = pg_banca;
}
public java.sql.Timestamp getData_esigibilita_iva() {
	return data_esigibilita_iva;
}
public void setData_esigibilita_iva(java.sql.Timestamp data_esigibilita_iva) {
	this.data_esigibilita_iva = data_esigibilita_iva;
}
public void setPg_trovato(java.lang.Long pg_trovato) {
	this.pg_trovato = pg_trovato;
}
public java.lang.Long getPg_trovato() {
	return pg_trovato;
}
public String getMotivo_assenza_cig() {
	return motivo_assenza_cig;
}
public void setMotivo_assenza_cig(String motivo_assenza_cig) {
	this.motivo_assenza_cig = motivo_assenza_cig;
}
}
