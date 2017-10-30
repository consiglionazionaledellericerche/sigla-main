package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Progetto_other_fieldKey extends OggettoBulk implements KeyedPersistent {
	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	public Progetto_other_fieldKey() {
		super();
	}
	
	public Progetto_other_fieldKey(java.lang.Integer pg_progetto) {
		super();
		this.pg_progetto = pg_progetto;
	}
	
	public java.lang.Integer getPg_progetto() {
		return pg_progetto;
	}
	
	public void setPg_progetto(java.lang.Integer pg_progetto) {
		this.pg_progetto = pg_progetto;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Progetto_other_fieldKey)) return false;
		Progetto_other_fieldKey k = (Progetto_other_fieldKey)o;
		if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
		return true;
	}
	
	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getPg_progetto());
	}
}
