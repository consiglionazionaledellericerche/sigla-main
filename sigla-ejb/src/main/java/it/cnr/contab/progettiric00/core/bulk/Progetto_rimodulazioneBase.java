package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.persistency.Keyed;

public class Progetto_rimodulazioneBase extends Progetto_rimodulazioneKey implements Keyed {
	private String stato;

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
