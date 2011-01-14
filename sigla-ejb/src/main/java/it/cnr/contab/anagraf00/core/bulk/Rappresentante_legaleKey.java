package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rappresentante_legaleKey extends OggettoBulk implements KeyedPersistent {
	// PG_RAPP_LEGALE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rapp_legale;

public Rappresentante_legaleKey() {
	super();
}
public Rappresentante_legaleKey(java.lang.Long pg_rapp_legale) {
	super();
	this.pg_rapp_legale = pg_rapp_legale;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rappresentante_legaleKey)) return false;
	Rappresentante_legaleKey k = (Rappresentante_legaleKey)o;
	if(!compareKey(getPg_rapp_legale(),k.getPg_rapp_legale())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_rapp_legale
 */
public java.lang.Long getPg_rapp_legale() {
	return pg_rapp_legale;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_rapp_legale());
}
/* 
 * Setter dell'attributo pg_rapp_legale
 */
public void setPg_rapp_legale(java.lang.Long pg_rapp_legale) {
	this.pg_rapp_legale = pg_rapp_legale;
}
}
