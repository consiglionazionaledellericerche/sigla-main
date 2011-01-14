package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class CapKey extends OggettoBulk implements KeyedPersistent {
	// PG_COMUNE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_comune;

	// CD_CAP VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_cap;

public CapKey() {
	super();
}
public CapKey(java.lang.String cd_cap,java.lang.Long pg_comune) {
	this.cd_cap = cd_cap;
	this.pg_comune = pg_comune;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof CapKey)) return false;
	CapKey k = (CapKey)o;
	if(!compareKey(getCd_cap(),k.getCd_cap())) return false;
	if(!compareKey(getPg_comune(),k.getPg_comune())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cap
 */
public java.lang.String getCd_cap() {
	return cd_cap;
}
/* 
 * Getter dell'attributo pg_comune
 */
public java.lang.Long getPg_comune() {
	return pg_comune;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cap())+
		calculateKeyHashCode(getPg_comune());
}
/* 
 * Setter dell'attributo cd_cap
 */
public void setCd_cap(java.lang.String cd_cap) {
	this.cd_cap = cd_cap;
}
/* 
 * Setter dell'attributo pg_comune
 */
public void setPg_comune(java.lang.Long pg_comune) {
	this.pg_comune = pg_comune;
}
}
