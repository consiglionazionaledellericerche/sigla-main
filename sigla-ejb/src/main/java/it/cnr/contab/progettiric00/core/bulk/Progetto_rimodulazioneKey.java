package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Progetto_rimodulazioneKey extends OggettoBulk implements KeyedPersistent {
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;
	// PG_RIMODULAZIONE NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_rimodulazione;

	public Progetto_rimodulazioneKey() {
		super();
	}
	
	public Progetto_rimodulazioneKey(java.lang.Integer pg_progetto, java.lang.Integer pg_rimodulazione) {
		super();
		this.pg_progetto = pg_progetto;
		this.pg_rimodulazione = pg_rimodulazione;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}
	
	public java.lang.Integer getPg_rimodulazione() {
		return pg_rimodulazione;
	}
	
	public void setPg_rimodulazione(java.lang.Integer pg_rimodulazione) {
		this.pg_rimodulazione = pg_rimodulazione;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Progetto_rimodulazioneKey)) return false;
		Progetto_rimodulazioneKey k = (Progetto_rimodulazioneKey)o;
		if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
		if(!compareKey(getPg_rimodulazione(),k.getPg_rimodulazione())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getPg_progetto());
		i = i + calculateKeyHashCode(getPg_rimodulazione());
		return i;
	}
}
