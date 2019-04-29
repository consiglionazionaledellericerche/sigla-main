package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_rimodulazioneBase extends Progetto_rimodulazioneKey implements Keyed {
	private String stato;

	private java.sql.Timestamp dtInizioOld;

	private java.sql.Timestamp dtFineOld;

	private java.sql.Timestamp dtInizio;

	private java.sql.Timestamp dtFine;

	private java.math.BigDecimal imVarFinanziato;
	
	private java.math.BigDecimal imVarCofinanziato;
	
	public Progetto_rimodulazioneBase() {
		super();
	}
	
	public Progetto_rimodulazioneBase(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione) {
		super(pg_progetto, pg_rimodulazione);
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
