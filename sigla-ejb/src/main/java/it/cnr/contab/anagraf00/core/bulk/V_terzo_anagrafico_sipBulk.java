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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_terzo_anagrafico_sipBulk extends OggettoBulk implements Persistent {
	// CD_TERZO     NUMBER(8)
	private java.lang.Integer cd_terzo;

	// CD_ANAG     NUMBER(8)
	private java.lang.Integer cd_anag;

	// DENOMINAZIONE_SEDE VARCHAR(200)
	private java.lang.String denominazione_sede;

	// TI_ENTITA CHAR(1)
	private java.lang.String ti_entita;
	
	// CODICE_FISCALE_PARIVA VARCHAR(20)
	private java.lang.String codice_fiscale_pariva;

	// COGNOME VARCHAR(50)
	private java.lang.String cognome;

	// NOME VARCHAR(50)
	private java.lang.String nome;

	private java.lang.String via_fiscale;
	
	private java.lang.String num_civico_fiscale;
	
	private java.lang.String cap_comune_fiscale;
	
	private java.lang.Integer pg_nazione_fiscale;
	private java.lang.Integer pg_comune_fiscale;
	
		// DT_NASCITA TIMESTAMP
	private java.sql.Timestamp dt_nascita;
	private java.lang.String sesso;
	private java.lang.Integer pg_comune_nascita;
	
	private java.lang.String comune_fiscale;
	private java.lang.String comune_nascita;
	private java.lang.String ds_nazione;
	
	public V_terzo_anagrafico_sipBulk() {
		super();
	}

	public java.lang.Integer getCd_anag() {
		return cd_anag;
	}

	public void setCd_anag(java.lang.Integer cd_anag) {
		this.cd_anag = cd_anag;
	}

	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}

	public void setCd_terzo(java.lang.Integer cd_terzo) {
		this.cd_terzo = cd_terzo;
	}

	public java.lang.String getCodice_fiscale_pariva() {
		return codice_fiscale_pariva;
	}

	public void setCodice_fiscale_pariva(java.lang.String codice_fiscale_pariva) {
		this.codice_fiscale_pariva = codice_fiscale_pariva;
	}

	public java.lang.String getCognome() {
		return cognome;
	}

	public void setCognome(java.lang.String cognome) {
		this.cognome = cognome;
	}

	public java.lang.String getDenominazione_sede() {
		return denominazione_sede;
	}

	public void setDenominazione_sede(java.lang.String denominazione_sede) {
		this.denominazione_sede = denominazione_sede;
	}

	public java.lang.String getNome() {
		return nome;
	}

	public void setNome(java.lang.String nome) {
		this.nome = nome;
	}

	public java.lang.String getTi_entita() {
		return ti_entita;
	}

	public void setTi_entita(java.lang.String ti_entita) {
		this.ti_entita = ti_entita;
	}

	public java.lang.String getVia_fiscale() {
		return via_fiscale;
	}

	public void setVia_fiscale(java.lang.String via_fiscale) {
		this.via_fiscale = via_fiscale;
	}

	public java.lang.String getNum_civico_fiscale() {
		return num_civico_fiscale;
	}

	public void setNum_civico_fiscale(java.lang.String num_civico_fiscale) {
		this.num_civico_fiscale = num_civico_fiscale;
	}

	public java.lang.String getCap_comune_fiscale() {
		return cap_comune_fiscale;
	}

	public void setCap_comune_fiscale(java.lang.String cap_comune_fiscale) {
		this.cap_comune_fiscale = cap_comune_fiscale;
	}

	public java.lang.Integer getPg_nazione_fiscale() {
		return pg_nazione_fiscale;
	}

	public void setPg_nazione_fiscale(java.lang.Integer pg_nazione_fiscale) {
		this.pg_nazione_fiscale = pg_nazione_fiscale;
	}

	public java.lang.Integer getPg_comune_fiscale() {
		return pg_comune_fiscale;
	}

	public void setPg_comune_fiscale(java.lang.Integer pg_comune_fiscale) {
		this.pg_comune_fiscale = pg_comune_fiscale;
	}

	public java.sql.Timestamp getDt_nascita() {
		return dt_nascita;
	}

	public void setDt_nascita(java.sql.Timestamp dt_nascita) {
		this.dt_nascita = dt_nascita;
	}

	public java.lang.String getSesso() {
		return sesso;
	}

	public void setSesso(java.lang.String sesso) {
		this.sesso = sesso;
	}

	public java.lang.Integer getPg_comune_nascita() {
		return pg_comune_nascita;
	}

	public void setPg_comune_nascita(java.lang.Integer pg_comune_nascita) {
		this.pg_comune_nascita = pg_comune_nascita;
	}

	public java.lang.String getComune_fiscale() {
		return comune_fiscale;
	}

	public void setComune_fiscale(java.lang.String comune_fiscale) {
		this.comune_fiscale = comune_fiscale;
	}

	public java.lang.String getComune_nascita() {
		return comune_nascita;
	}

	public void setComune_nascita(java.lang.String comune_nascita) {
		this.comune_nascita = comune_nascita;
	}

	public java.lang.String getDs_nazione() {
		return ds_nazione;
	}

	public void setDs_nazione(java.lang.String ds_nazione) {
		this.ds_nazione = ds_nazione;
	}

}
