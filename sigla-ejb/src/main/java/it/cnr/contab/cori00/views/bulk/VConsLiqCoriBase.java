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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_coriKey;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class VConsLiqCoriBase extends VConsLiqCoriKey implements Keyed {
	// CD_CDS_DOC VARCHAR(30)
	private String cd_cds_doc;

	private String ragione_sociale;

	// ESERCIZIO_ACC_COMPENS DECIMAL(4,0)
	private Integer esercizio_doc;

	// IM_LIQUIDATO DECIMAL(15,2)
	private java.math.BigDecimal im_liquidato;
	private java.math.BigDecimal ammontare;

	// PG_ACC_COMPENS DECIMAL(10,0)
	private Long pg_acc_compens;

	// PG_DOC DECIMAL(10,0)
	private Long numero_mandato;

	private String nome;
	private String cognome;
	private String codice_fiscale;
	private String ds_uo_liquidazione;
	private String ds_uo_compenso;
	private String ds_compenso;
	private String ds_contributo_ritenuta;
	private String ds_comune;
	private String ds_regione;
	private String ds_gruppo_cr;
	// DT_EMISSIONE TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_emissione;
	private java.sql.Timestamp dt_da;
	private java.sql.Timestamp dt_a;

public VConsLiqCoriBase() {
	super();
}
public VConsLiqCoriBase(String uo_compenso, String cd_contributo_ritenuta, String cd_gruppo_cr, String cd_regione, String cd_uo_liquidazione, Integer esercizio_compenso, Integer esercizio_liquidazione, Long pg_compenso, Long pg_comune, Integer pg_liquidazione, String ti_ente_percipiente) {
	super(uo_compenso, cd_contributo_ritenuta, cd_gruppo_cr, cd_regione, cd_uo_liquidazione, esercizio_compenso, esercizio_liquidazione, pg_compenso, pg_comune, pg_liquidazione, ti_ente_percipiente);
}

	public String getCd_cds_doc() {
		return cd_cds_doc;
	}

	public void setCd_cds_doc(String cd_cds_doc) {
		this.cd_cds_doc = cd_cds_doc;
	}

	public String getRagione_sociale() {
		return ragione_sociale;
	}

	public void setRagione_sociale(String ragione_sociale) {
		this.ragione_sociale = ragione_sociale;
	}

	public Integer getEsercizio_doc() {
		return esercizio_doc;
	}

	public void setEsercizio_doc(Integer esercizio_doc) {
		this.esercizio_doc = esercizio_doc;
	}

	public BigDecimal getIm_liquidato() {
		return im_liquidato;
	}

	public void setIm_liquidato(BigDecimal im_liquidato) {
		this.im_liquidato = im_liquidato;
	}

	public BigDecimal getAmmontare() {
		return ammontare;
	}

	public void setAmmontare(BigDecimal ammontare) {
		this.ammontare = ammontare;
	}

	public Long getPg_acc_compens() {
		return pg_acc_compens;
	}

	public void setPg_acc_compens(Long pg_acc_compens) {
		this.pg_acc_compens = pg_acc_compens;
	}

	public Long getNumero_mandato() {
		return numero_mandato;
	}

	public void setNumero_mandato(Long numero_mandato) {
		this.numero_mandato = numero_mandato;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodice_fiscale() {
		return codice_fiscale;
	}

	public void setCodice_fiscale(String codice_fiscale) {
		this.codice_fiscale = codice_fiscale;
	}

	public String getDs_uo_liquidazione() {
		return ds_uo_liquidazione;
	}

	public void setDs_uo_liquidazione(String ds_uo_liquidazione) {
		this.ds_uo_liquidazione = ds_uo_liquidazione;
	}

	public String getDs_uo_compenso() {
		return ds_uo_compenso;
	}

	public void setDs_uo_compenso(String ds_uo_compenso) {
		this.ds_uo_compenso = ds_uo_compenso;
	}

	public String getDs_compenso() {
		return ds_compenso;
	}

	public void setDs_compenso(String ds_compenso) {
		this.ds_compenso = ds_compenso;
	}

	public String getDs_contributo_ritenuta() {
		return ds_contributo_ritenuta;
	}

	public void setDs_contributo_ritenuta(String ds_contributo_ritenuta) {
		this.ds_contributo_ritenuta = ds_contributo_ritenuta;
	}

	public String getDs_comune() {
		return ds_comune;
	}

	public void setDs_comune(String ds_comune) {
		this.ds_comune = ds_comune;
	}

	public String getDs_regione() {
		return ds_regione;
	}

	public void setDs_regione(String ds_regione) {
		this.ds_regione = ds_regione;
	}

	public String getDs_gruppo_cr() {
		return ds_gruppo_cr;
	}

	public void setDs_gruppo_cr(String ds_gruppo_cr) {
		this.ds_gruppo_cr = ds_gruppo_cr;
	}

	public Timestamp getDt_emissione() {
		return dt_emissione;
	}

	public void setDt_emissione(Timestamp dt_emissione) {
		this.dt_emissione = dt_emissione;
	}

	public Timestamp getDt_da() {
		return dt_da;
	}

	public void setDt_da(Timestamp dt_da) {
		this.dt_da = dt_da;
	}

	public Timestamp getDt_a() {
		return dt_a;
	}

	public void setDt_a(Timestamp dt_a) {
		this.dt_a = dt_a;
	}
}
