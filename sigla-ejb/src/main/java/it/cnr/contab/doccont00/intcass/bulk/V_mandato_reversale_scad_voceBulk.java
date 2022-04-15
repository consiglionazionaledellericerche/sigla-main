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

package it.cnr.contab.doccont00.intcass.bulk;

import java.math.BigDecimal;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_mandato_reversale_scad_voceBulk extends OggettoBulk implements Persistent {
	private String ti_documento;
	private String ti_competenza_residuo;
	private String cd_cds;
	private Integer esercizio;
	private Integer pg_documento;
	private String ti_appartenenza;
	private String ti_gestione;
	private String cd_voce;
	private String cd_centro_responsabilita;
	private String cd_linea_attivita;
	private Integer esercizio_originale;
	private String cd_tipo_documento_cont;
	private BigDecimal im_voce;

	public V_mandato_reversale_scad_voceBulk() {
		super();
	}

	public String getCd_cds() {
		return cd_cds;
	}

	public void setCd_cds(String cd_cds) {
		this.cd_cds = cd_cds;
	}

	public String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	public void setCd_centro_responsabilita(String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}

	public String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public void setCd_linea_attivita(String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	public String getCd_tipo_documento_cont() {
		return cd_tipo_documento_cont;
	}

	public void setCd_tipo_documento_cont(String cd_tipo_documento_cont) {
		this.cd_tipo_documento_cont = cd_tipo_documento_cont;
	}

	public String getCd_voce() {
		return cd_voce;
	}

	public void setCd_voce(String cd_voce) {
		this.cd_voce = cd_voce;
	}

	public Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(Integer esercizio) {
		this.esercizio = esercizio;
	}

	public Integer getEsercizio_originale() {
		return esercizio_originale;
	}

	public void setEsercizio_originale(Integer esercizio_originale) {
		this.esercizio_originale = esercizio_originale;
	}

	public BigDecimal getIm_voce() {
		return im_voce;
	}

	public void setIm_voce(BigDecimal im_voce) {
		this.im_voce = im_voce;
	}

	public Integer getPg_documento() {
		return pg_documento;
	}

	public void setPg_documento(Integer pg_documento) {
		this.pg_documento = pg_documento;
	}

	public String getTi_appartenenza() {
		return ti_appartenenza;
	}

	public void setTi_appartenenza(String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}

	public String getTi_competenza_residuo() {
		return ti_competenza_residuo;
	}

	public void setTi_competenza_residuo(String ti_competenza_residuo) {
		this.ti_competenza_residuo = ti_competenza_residuo;
	}

	public String getTi_documento() {
		return ti_documento;
	}

	public void setTi_documento(String ti_documento) {
		this.ti_documento = ti_documento;
	}

	public String getTi_gestione() {
		return ti_gestione;
	}

	public void setTi_gestione(String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}

}
