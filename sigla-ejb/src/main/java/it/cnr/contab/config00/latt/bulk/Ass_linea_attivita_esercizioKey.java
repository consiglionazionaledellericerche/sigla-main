package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Ass_linea_attivita_esercizioKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO INTEGER NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_linea_attivita;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;
	
	public Ass_linea_attivita_esercizioKey() {
		super();
	}
	
	public Ass_linea_attivita_esercizioKey(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita) {
		super();
		this.esercizio=esercizio;
		this.cd_centro_responsabilita=cd_centro_responsabilita;
		this.cd_linea_attivita=cd_linea_attivita;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cd_centro_responsabilita == null) ? 0 : cd_centro_responsabilita.hashCode());
		result = prime * result + ((cd_linea_attivita == null) ? 0 : cd_linea_attivita.hashCode());
		result = prime * result + ((esercizio == null) ? 0 : esercizio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ass_linea_attivita_esercizioKey other = (Ass_linea_attivita_esercizioKey) obj;
		if (cd_centro_responsabilita == null) {
			if (other.cd_centro_responsabilita != null)
				return false;
		} else if (!cd_centro_responsabilita.equals(other.cd_centro_responsabilita))
			return false;
		if (cd_linea_attivita == null) {
			if (other.cd_linea_attivita != null)
				return false;
		} else if (!cd_linea_attivita.equals(other.cd_linea_attivita))
			return false;
		if (esercizio == null) {
			if (other.esercizio != null)
				return false;
		} else if (!esercizio.equals(other.esercizio))
			return false;
		return true;
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getCd_linea_attivita() {
		return cd_linea_attivita;
	}

	public void setCd_linea_attivita(java.lang.String cd_linea_attivita) {
		this.cd_linea_attivita = cd_linea_attivita;
	}

	public java.lang.String getCd_centro_responsabilita() {
		return cd_centro_responsabilita;
	}

	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.cd_centro_responsabilita = cd_centro_responsabilita;
	}
}