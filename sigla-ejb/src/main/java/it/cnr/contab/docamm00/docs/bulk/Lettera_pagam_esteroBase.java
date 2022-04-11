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

import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.jada.persistency.Keyed;

public class Lettera_pagam_esteroBase extends Lettera_pagam_esteroKey implements Keyed {

	private static final long serialVersionUID = 1L;

	// CD_SOSPESO VARCHAR(20)
	private java.lang.String cd_sospeso;

	// DT_REGISTRAZIONE TIMESTAMP NOT NULL
	@StorageProperty(name="doccont:datDoc")
	private java.sql.Timestamp dt_registrazione;

	// IM_COMMISSIONI DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_commissioni;

	// IM_PAGAMENTO DECIMAL(15,2) NOT NULL
	@StorageProperty(name="doccont:importo")
	private java.math.BigDecimal im_pagamento;

	// TI_ENTRATA_SPESA CHAR(1)
	private java.lang.String ti_entrata_spesa;

	// TI_SOSPESO_RISCONTRO CHAR(1)
	private java.lang.String ti_sospeso_riscontro;

	// STATO_TRASMISSIONE CHAR(1)
	@StorageProperty(name="doccont:stato_trasmissione")
	private java.lang.String stato_trasmissione;
	
	// DT_FIRMA TIMESTAMP
	private java.sql.Timestamp dt_firma;
	
	//  BONIFICO_MEZZO      CHAR(1)
	private java.lang.String bonifico_mezzo;
	
	//  DIVISA              VARCHAR2(250),
	private java.lang.String divisa;
	
	//  BENEFICIARIO        VARCHAR2(250),
	private java.lang.String beneficiario;
	
	//  NUM_CONTO_BEN       VARCHAR2(250),
	private java.lang.String num_conto_ben;
	
	//  IBAN                VARCHAR2(250),
	private java.lang.String iban;
	
	//  INDIRIZZO           VARCHAR2(250),
	private java.lang.String indirizzo;
	
	//  INDIRIZZO_SWIFT     VARCHAR2(250),
	private java.lang.String indirizzo_swift;
	
	//  MOTIVO_PAG          VARCHAR2(250),
	private java.lang.String motivo_pag;
	
	//  AMMONTARE_DEBITO    CHAR(1),
	private java.lang.String ammontare_debito;
	
	//  CONTO_DEBITO        VARCHAR2(250),
	private java.lang.String conto_debito;
	
	//  COMMISSIONI_SPESE   CHAR(1),
	private java.lang.String commissioni_spese;
	
	//  COMMISSIONI_SPESE_ESTERE CHAR(1)
	private java.lang.String commissioni_spese_estere;

	// ESERCIZIO_DISTINTA DECIMAL(4,0)
	private java.lang.Integer esercizio_distinta;

	// PG_DISTINTA DECIMAL(10,0)
	private java.lang.Long pg_distinta;
	
	// FL_SECONDA_FIRMA_APPOSTA CHAR(1)
	private java.lang.Boolean fl_seconda_firma_apposta;
	
	// CD_SOSPESO VARCHAR(20)
    private java.lang.String cd_cds_sospeso;
	
    private java.sql.Timestamp dt_cancellazione;

	//  ISTRUZIONI_SPECIALI_1     VARCHAR2(250),
	private java.lang.String istruzioni_speciali_1;

	//  ISTRUZIONI_SPECIALI_2     VARCHAR2(250),
	private java.lang.String istruzioni_speciali_2;
	//  ISTRUZIONI_SPECIALI_3     VARCHAR2(250),
	private java.lang.String istruzioni_speciali_3;

	public Lettera_pagam_esteroBase() {
		super();
	}
	public Lettera_pagam_esteroBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_lettera) {
		super(cd_cds,cd_unita_organizzativa,esercizio,pg_lettera);
	}
	/* 
	 * Getter dell'attributo cd_sospeso
	 */
	public java.lang.String getCd_sospeso() {
		return cd_sospeso;
	}
	/* 
	 * Getter dell'attributo dt_registrazione
	 */
	public java.sql.Timestamp getDt_registrazione() {
		return dt_registrazione;
	}
	/* 
	 * Getter dell'attributo im_commissioni
	 */
	public java.math.BigDecimal getIm_commissioni() {
		return im_commissioni;
	}
	/* 
	 * Getter dell'attributo im_pagamento
	 */
	public java.math.BigDecimal getIm_pagamento() {
		return im_pagamento;
	}
	/* 
	 * Getter dell'attributo ti_entrata_spesa
	 */
	public java.lang.String getTi_entrata_spesa() {
		return ti_entrata_spesa;
	}
	/* 
	 * Getter dell'attributo ti_sospeso_riscontro
	 */
	public java.lang.String getTi_sospeso_riscontro() {
		return ti_sospeso_riscontro;
	}
	/* 
	 * Setter dell'attributo cd_sospeso
	 */
	public void setCd_sospeso(java.lang.String cd_sospeso) {
		this.cd_sospeso = cd_sospeso;
	}
	/* 
	 * Setter dell'attributo dt_registrazione
	 */
	public void setDt_registrazione(java.sql.Timestamp dt_registrazione) {
		this.dt_registrazione = dt_registrazione;
	}
	/* 
	 * Setter dell'attributo im_commissioni
	 */
	public void setIm_commissioni(java.math.BigDecimal im_commissioni) {
		this.im_commissioni = im_commissioni;
	}
	/* 
	 * Setter dell'attributo im_pagamento
	 */
	public void setIm_pagamento(java.math.BigDecimal im_pagamento) {
		this.im_pagamento = im_pagamento;
	}
	/* 
	 * Setter dell'attributo ti_entrata_spesa
	 */
	public void setTi_entrata_spesa(java.lang.String ti_entrata_spesa) {
		this.ti_entrata_spesa = ti_entrata_spesa;
	}
	/* 
	 * Setter dell'attributo ti_sospeso_riscontro
	 */
	public void setTi_sospeso_riscontro(java.lang.String ti_sospeso_riscontro) {
		this.ti_sospeso_riscontro = ti_sospeso_riscontro;
	}
	public java.lang.String getStato_trasmissione() {
		return stato_trasmissione;
	}
	public void setStato_trasmissione(java.lang.String stato_trasmissione) {
		this.stato_trasmissione = stato_trasmissione;
	}
	public java.sql.Timestamp getDt_firma() {
		return dt_firma;
	}
	public void setDt_firma(java.sql.Timestamp dt_firma) {
		this.dt_firma = dt_firma;
	}
	public java.lang.String getBonifico_mezzo() {
		return bonifico_mezzo;
	}
	public void setBonifico_mezzo(java.lang.String bonifico_mezzo) {
		this.bonifico_mezzo = bonifico_mezzo;
	}
	public java.lang.String getDivisa() {
		return divisa;
	}
	public void setDivisa(java.lang.String divisa) {
		this.divisa = divisa;
	}
	public java.lang.String getBeneficiario() {
		return beneficiario;
	}
	public void setBeneficiario(java.lang.String beneficiario) {
		this.beneficiario = beneficiario;
	}
	public java.lang.String getNum_conto_ben() {
		return num_conto_ben;
	}
	public void setNum_conto_ben(java.lang.String num_conto_ben) {
		this.num_conto_ben = num_conto_ben;
	}
	public java.lang.String getIban() {
		return iban;
	}
	public void setIban(java.lang.String iban) {
		this.iban = iban;
	}
	public java.lang.String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(java.lang.String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public java.lang.String getIndirizzo_swift() {
		return indirizzo_swift;
	}
	public void setIndirizzo_swift(java.lang.String indirizzo_swift) {
		this.indirizzo_swift = indirizzo_swift;
	}
	public java.lang.String getMotivo_pag() {
		return motivo_pag;
	}
	public void setMotivo_pag(java.lang.String motivo_pag) {
		this.motivo_pag = motivo_pag;
	}
	public java.lang.String getAmmontare_debito() {
		return ammontare_debito;
	}
	public void setAmmontare_debito(java.lang.String ammontare_debito) {
		this.ammontare_debito = ammontare_debito;
	}
	public java.lang.String getConto_debito() {
		return conto_debito;
	}
	public void setConto_debito(java.lang.String conto_debito) {
		this.conto_debito = conto_debito;
	}
	public java.lang.String getCommissioni_spese() {
		return commissioni_spese;
	}
	public void setCommissioni_spese(java.lang.String commissioni_spese) {
		this.commissioni_spese = commissioni_spese;
	}
	public java.lang.String getCommissioni_spese_estere() {
		return commissioni_spese_estere;
	}
	public void setCommissioni_spese_estere(
			java.lang.String commissioni_spese_estere) {
		this.commissioni_spese_estere = commissioni_spese_estere;
	}
	public java.lang.Integer getEsercizio_distinta() {
		return esercizio_distinta;
	}
	public void setEsercizio_distinta(java.lang.Integer esercizio_distinta) {
		this.esercizio_distinta = esercizio_distinta;
	}
	public java.lang.Long getPg_distinta() {
		return pg_distinta;
	}
	public void setPg_distinta(java.lang.Long pg_distinta) {
		this.pg_distinta = pg_distinta;
	}
	public java.lang.Boolean getFl_seconda_firma_apposta() {
		return fl_seconda_firma_apposta;
	}
	public void setFl_seconda_firma_apposta(
			java.lang.Boolean fl_seconda_firma_apposta) {
		this.fl_seconda_firma_apposta = fl_seconda_firma_apposta;
	}
	public java.lang.String getCd_cds_sospeso() {
		return cd_cds_sospeso;
	}
	public void setCd_cds_sospeso(java.lang.String cd_cds_sospeso) {
		this.cd_cds_sospeso = cd_cds_sospeso;
	}
	public java.sql.Timestamp getDt_cancellazione() {
		return dt_cancellazione;
	}
	public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
		this.dt_cancellazione = dt_cancellazione;
	}

	public String getIstruzioni_speciali_1() {
		return istruzioni_speciali_1;
	}

	public void setIstruzioni_speciali_1(String istruzioni_speciali_1) {
		this.istruzioni_speciali_1 = istruzioni_speciali_1;
	}

	public String getIstruzioni_speciali_2() {
		return istruzioni_speciali_2;
	}

	public void setIstruzioni_speciali_2(String istruzioni_speciali_2) {
		this.istruzioni_speciali_2 = istruzioni_speciali_2;
	}

	public String getIstruzioni_speciali_3() {
		return istruzioni_speciali_3;
	}

	public void setIstruzioni_speciali_3(String istruzioni_speciali_3) {
		this.istruzioni_speciali_3 = istruzioni_speciali_3;
	}
}
