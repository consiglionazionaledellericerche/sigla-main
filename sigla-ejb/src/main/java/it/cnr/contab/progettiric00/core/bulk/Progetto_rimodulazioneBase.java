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

import java.sql.Timestamp;

public class Progetto_rimodulazioneBase extends Progetto_rimodulazioneKey implements Keyed {
	private java.lang.Integer pg_gen_rimodulazione;
	
	private String stato;

	private java.sql.Timestamp dtInizioOld;

	private java.sql.Timestamp dtFineOld;

	private java.sql.Timestamp dtProrogaOld;

	private java.sql.Timestamp dtInizio;

	private java.sql.Timestamp dtFine;

	private java.sql.Timestamp dtProroga;

	private java.sql.Timestamp dtStatoDefinitivo;

	private String motivoRifiuto;

	private String note;

	private java.math.BigDecimal imVarFinanziato;
	
	private java.math.BigDecimal imVarCofinanziato;
	
	public Progetto_rimodulazioneBase() {
		super();
	}
	
	public Progetto_rimodulazioneBase(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione) {
		super(pg_progetto, pg_rimodulazione);
	}

	public java.lang.Integer getPg_gen_rimodulazione() {
		return pg_gen_rimodulazione;
	}
	
	public void setPg_gen_rimodulazione(java.lang.Integer pg_gen_rimodulazione) {
		this.pg_gen_rimodulazione = pg_gen_rimodulazione;
	}
	
	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public java.sql.Timestamp getDtInizioOld() {
		return dtInizioOld;
	}

	public void setDtInizioOld(java.sql.Timestamp dtInizioOld) {
		this.dtInizioOld = dtInizioOld;
	}

	public java.sql.Timestamp getDtFineOld() {
		return dtFineOld;
	}

	public void setDtFineOld(java.sql.Timestamp dtFineOld) {
		this.dtFineOld = dtFineOld;
	}

	public java.sql.Timestamp getDtProrogaOld() {
		return dtProrogaOld;
	}
	
	public void setDtProrogaOld(java.sql.Timestamp dtProrogaOld) {
		this.dtProrogaOld = dtProrogaOld;
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

	public Timestamp getDtStatoDefinitivo() {
		return dtStatoDefinitivo;
	}

	public void setDtStatoDefinitivo(Timestamp dtStatoDefinitivo) {
		this.dtStatoDefinitivo = dtStatoDefinitivo;
	}

	public String getMotivoRifiuto() {
		return motivoRifiuto;
	}

	public void setMotivoRifiuto(String motivoRifiuto) {
		this.motivoRifiuto = motivoRifiuto;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public java.math.BigDecimal getImVarFinanziato() {
		return imVarFinanziato;
	}

	public void setImVarFinanziato(java.math.BigDecimal imVarFinanziato) {
		this.imVarFinanziato = imVarFinanziato;
	}

	public java.math.BigDecimal getImVarCofinanziato() {
		return imVarCofinanziato;
	}

	public void setImVarCofinanziato(java.math.BigDecimal imVarCofinanziato) {
		this.imVarCofinanziato = imVarCofinanziato;
	}
}
