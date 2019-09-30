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

package it.cnr.contab.client.docamm;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

public class FatturaAttivaRiga {
	private java.lang.Boolean fl_iva_forzata;
	private String cd_voce;
	private String cd_tariffario;
	private String cd_voce_iva;
	private java.math.BigDecimal prezzo_unitario;
	private java.math.BigDecimal im_iva;
	private java.math.BigDecimal quantita;
	private Integer esercizio_contratto;
	private Long pg_contratto;
	private String stato_contratto;
	private String ds_riga_fattura;
	private Long pg_fattura_assncna_fin;
	private Integer esercizio_assncna_fin;
	private Long pg_riga_assncna_fin;
	private Long progressivo_riga;
	private String ds_accertamento;
	private String ds_obbligazione;
	private java.util.ArrayList<FatturaAttivaScad> righescadvoc;
	private Long pg_obbligazione_scadenzario;
	private Long pg_obbligazione;
	private Long pg_accertamento_scadenzario;
	private Long pg_accertamento;
	private String motivazione;
	private Date dt_scadenza;
	private String cd_bene_servizio;
	public FatturaAttivaRiga() {
		super();
	}

	public java.lang.Boolean getFl_iva_forzata() {
		return fl_iva_forzata;
	}
	@XmlElement(required=true)
	public void setFl_iva_forzata(java.lang.Boolean fl_iva_forzata) {
		this.fl_iva_forzata = fl_iva_forzata;
	}
	public String getCd_voce() {
		return cd_voce;
	}
	@XmlElement(required=true)
	public void setCd_voce(String cd_voce) {
		this.cd_voce = cd_voce;
	}
	public String getCd_tariffario() {
		return cd_tariffario;
	}
	public void setCd_tariffario(String cd_tariffario) {
		this.cd_tariffario = cd_tariffario;
	}
	public String getCd_voce_iva() {
		return cd_voce_iva;
	}
	public void setCd_voce_iva(String cd_voce_iva) {
		this.cd_voce_iva = cd_voce_iva;
	}
	public java.math.BigDecimal getPrezzo_unitario() {
		return prezzo_unitario;
	}
	public void setPrezzo_unitario(java.math.BigDecimal prezzo_unitario) {
		this.prezzo_unitario = prezzo_unitario;
	}
	public java.math.BigDecimal getQuantita() {
		return quantita;
	}
	public void setQuantita(java.math.BigDecimal quantita) {
		this.quantita = quantita;
	}
	public Integer getEsercizio_contratto() {
		return esercizio_contratto;
	}
	public void setEsercizio_contratto(Integer esercizio_contratto) {
		this.esercizio_contratto = esercizio_contratto;
	}
	public Long getPg_contratto() {
		return pg_contratto;
	}
	public void setPg_contratto(Long pg_contratto) {
		this.pg_contratto = pg_contratto;
	}
	public String getStato_contratto() {
		return stato_contratto;
	}
	public void setStato_contratto(String stato_contratto) {
		this.stato_contratto = stato_contratto;
	}
	public String getDs_riga_fattura() {
		return ds_riga_fattura;
	}
	public void setDs_riga_fattura(String ds_riga_fattura) {
		this.ds_riga_fattura = ds_riga_fattura;
	}
	public Long getPg_fattura_assncna_fin() {
		return pg_fattura_assncna_fin;
	}
	public void setPg_fattura_assncna_fin(Long pg_fattura_assncna_fin) {
		this.pg_fattura_assncna_fin = pg_fattura_assncna_fin;
	}
	public Integer getEsercizio_assncna_fin() {
		return esercizio_assncna_fin;
	}
	public void setEsercizio_assncna_fin(Integer esercizio_assncna_fin) {
		this.esercizio_assncna_fin = esercizio_assncna_fin;
	}
	public Long getPg_riga_assncna_fin() {
		return pg_riga_assncna_fin;
	}
	public void setPg_riga_assncna_fin(Long pg_riga_assncna_fin) {
		this.pg_riga_assncna_fin = pg_riga_assncna_fin;
	}
	public Long getProgressivo_riga() {
		return progressivo_riga;
	}
	public void setProgressivo_riga(Long progressivo_riga) {
		this.progressivo_riga = progressivo_riga;
	}
	public String getDs_accertamento() {
		return ds_accertamento;
	}
	public void setDs_accertamento(String ds_accertamento) {
		this.ds_accertamento = ds_accertamento;
	}
	public java.util.ArrayList<FatturaAttivaScad> getRighescadvoc() {
		return righescadvoc;
	}
	@XmlElement(required=true)
	public void setRighescadvoc(java.util.ArrayList<FatturaAttivaScad> righescadvoc) {
		this.righescadvoc = righescadvoc;
	}
	public String getDs_obbligazione() {
		return ds_obbligazione;
	}
	public void setDs_obbligazione(String ds_obbligazione) {
		this.ds_obbligazione = ds_obbligazione;
	}
	public Long getPg_obbligazione_scadenzario() {
		return pg_obbligazione_scadenzario;
	}
	public void setPg_obbligazione_scadenzario(Long pg_obbligazione_scadenzario) {
		this.pg_obbligazione_scadenzario = pg_obbligazione_scadenzario;
	}
	public Long getPg_obbligazione() {
		return pg_obbligazione;
	}
	public void setPg_obbligazione(Long pg_obbligazione) {
		this.pg_obbligazione = pg_obbligazione;
	}
	public Long getPg_accertamento_scadenzario() {
		return pg_accertamento_scadenzario;
	}
	public void setPg_accertamento_scadenzario(Long pg_accertamento_scadenzario) {
		this.pg_accertamento_scadenzario = pg_accertamento_scadenzario;
	}
	public Long getPg_accertamento() {
		return pg_accertamento;
	}
	public void setPg_accertamento(Long pg_accertamento) {
		this.pg_accertamento = pg_accertamento;
	}
	public String getMotivazione() {
		return motivazione;
	}
	public void setMotivazione(String motivazione) {
		this.motivazione = motivazione;
	}
	public Date getDt_scadenza() {
		return dt_scadenza;
	}
	@XmlElement(required=true)
	public void setDt_scadenza(Date dt_scadenza) {
		this.dt_scadenza = dt_scadenza;
	}

	public String getCd_bene_servizio() {
		return cd_bene_servizio;
	}

	public void setCd_bene_servizio(String cd_bene_servizio) {
		this.cd_bene_servizio = cd_bene_servizio;
	}
	public java.math.BigDecimal getIm_iva() {
		return im_iva;
	}

	public void setIm_iva(java.math.BigDecimal im_iva) {
		this.im_iva = im_iva;
	}
}
