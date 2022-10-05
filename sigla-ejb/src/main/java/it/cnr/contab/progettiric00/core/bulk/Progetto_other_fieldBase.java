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

package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_other_fieldBase extends Progetto_other_fieldKey implements Keyed {
	private String cd_programma;

	private String cd_missione;

	private Long idTipoFinanziamento;

	private String stato;

	private java.sql.Timestamp dtInizio;

	private java.sql.Timestamp dtFine;

	private java.sql.Timestamp dtProroga;

	private java.math.BigDecimal imFinanziato;
	
	private java.math.BigDecimal imCofinanziato;
	
	private java.math.BigDecimal imFideiussione;

	private java.sql.Timestamp dtInizioFideiussione;

	private java.sql.Timestamp dtFineFideiussione;

	private java.sql.Timestamp dtRilascioFideiussione;

	private java.math.BigDecimal imSvincolatoFideiussione;

	private java.sql.Timestamp dtSvincoloFideiussione;

	private String idEsternoFideiussione;

	private Boolean flControlliDisabled;

	private Boolean flControlliDateDisabled;

	public Progetto_other_fieldBase() {
		super();
	}
	
	public Progetto_other_fieldBase(java.lang.Integer pg_progetto) {
		super(pg_progetto);
	}

	public String getCd_programma() {
		return cd_programma;
	}
	
	public void setCd_programma(String cd_programma) {
		this.cd_programma = cd_programma;
	}
	
	public String getCd_missione() {
		return cd_missione;
	}
	
	public void setCd_missione(String cd_missione) {
		this.cd_missione = cd_missione;
	}

	public Long getIdTipoFinanziamento() {
		return idTipoFinanziamento;
	}

	public void setIdTipoFinanziamento(Long idTipoFinanziamento) {
		this.idTipoFinanziamento = idTipoFinanziamento;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public java.sql.Timestamp getDtInizio() {
		return dtInizio;
	}

	public void setDtInizio(java.sql.Timestamp dtInizio) {
		this.dtInizio = dtInizio;
	}

	public java.sql.Timestamp getDtFine() {
		return dtFine;
	}

	public void setDtFine(java.sql.Timestamp dtFine) {
		this.dtFine = dtFine;
	}

	public java.sql.Timestamp getDtProroga() {
		return dtProroga;
	}

	public void setDtProroga(java.sql.Timestamp dtProroga) {
		this.dtProroga = dtProroga;
	}

	public java.math.BigDecimal getImFinanziato() {
		return imFinanziato;
	}

	public void setImFinanziato(java.math.BigDecimal imFinanziato) {
		this.imFinanziato = imFinanziato;
	}

	public java.math.BigDecimal getImCofinanziato() {
		return imCofinanziato;
	}

	public void setImCofinanziato(java.math.BigDecimal imCofinanziato) {
		this.imCofinanziato = imCofinanziato;
	}

	public java.math.BigDecimal getImFideiussione() {
		return imFideiussione;
	}

	public void setImFideiussione(java.math.BigDecimal imFideiussione) {
		this.imFideiussione = imFideiussione;
	}

	public java.sql.Timestamp getDtInizioFideiussione() {
		return dtInizioFideiussione;
	}

	public void setDtInizioFideiussione(java.sql.Timestamp dtInizioFideiussione) {
		this.dtInizioFideiussione = dtInizioFideiussione;
	}

	public java.sql.Timestamp getDtFineFideiussione() {
		return dtFineFideiussione;
	}

	public void setDtFineFideiussione(java.sql.Timestamp dtFineFideiussione) {
		this.dtFineFideiussione = dtFineFideiussione;
	}

	public java.sql.Timestamp getDtRilascioFideiussione() {
		return dtRilascioFideiussione;
	}

	public void setDtRilascioFideiussione(java.sql.Timestamp dtRilascioFideiussione) {
		this.dtRilascioFideiussione = dtRilascioFideiussione;
	}

	public java.math.BigDecimal getImSvincolatoFideiussione() {
		return imSvincolatoFideiussione;
	}

	public void setImSvincolatoFideiussione(java.math.BigDecimal imSvincolatoFideiussione) {
		this.imSvincolatoFideiussione = imSvincolatoFideiussione;
	}

	public java.sql.Timestamp getDtSvincoloFideiussione() {
		return dtSvincoloFideiussione;
	}

	public void setDtSvincoloFideiussione(java.sql.Timestamp dtSvincoloFideiussione) {
		this.dtSvincoloFideiussione = dtSvincoloFideiussione;
	}

	public String getIdEsternoFideiussione() {
		return idEsternoFideiussione;
	}

	public void setIdEsternoFideiussione(String idEsternoFideiussione) {
		this.idEsternoFideiussione = idEsternoFideiussione;
	}

	public Boolean getFlControlliDisabled() {
		return flControlliDisabled;
	}

	public void setFlControlliDisabled(Boolean flControlliDisabled) {
		this.flControlliDisabled = flControlliDisabled;
	}

	public Boolean getFlControlliDateDisabled() {
		return flControlliDateDisabled;
	}

	public void setFlControlliDateDisabled(Boolean flControlliDateDisabled) {
		this.flControlliDateDisabled = flControlliDateDisabled;
	}
}
