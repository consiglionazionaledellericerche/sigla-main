package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ComuneKey extends OggettoBulk implements KeyedPersistent {
	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_comune;

public ComuneKey() {
	super();
}
public ComuneKey(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ComuneKey)) return false;
	ComuneKey k = (ComuneKey)o;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_comune());
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
}
