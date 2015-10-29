package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ass_linea_attivita_esercizioBase extends Ass_linea_attivita_esercizioKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// PG_PROGETTO NUMBER(10)
	private java.lang.Integer pg_progetto;

	// ESERCIZIO_FINE NUMBER(4)
	private java.lang.Integer esercizio_fine;

	public Ass_linea_attivita_esercizioBase() {
		super();
	}

	public Ass_linea_attivita_esercizioBase(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita) {
		super(esercizio, cd_centro_responsabilita,cd_linea_attivita);
	}

	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}

	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}

	public java.lang.Integer getEsercizio_fine() {
		return esercizio_fine;
	}

	public void setEsercizio_fine(java.lang.Integer esercizio_fine) {
		this.esercizio_fine = esercizio_fine;
	}
}
