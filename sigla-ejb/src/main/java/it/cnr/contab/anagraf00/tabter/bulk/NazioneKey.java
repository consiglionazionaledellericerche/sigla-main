package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class NazioneKey extends OggettoBulk implements KeyedPersistent {
	// PG_NAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_nazione;

public NazioneKey() {
	super();
}
public NazioneKey(java.lang.Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof NazioneKey)) return false;
	NazioneKey k = (NazioneKey)o;
	if(!compareKey(getPg_nazione(),k.getPg_nazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_nazione
 */
public java.lang.Long getPg_nazione() {
	return pg_nazione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_nazione());
}
/* 
 * Setter dell'attributo pg_nazione
 */
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.pg_nazione = pg_nazione;
}
}
