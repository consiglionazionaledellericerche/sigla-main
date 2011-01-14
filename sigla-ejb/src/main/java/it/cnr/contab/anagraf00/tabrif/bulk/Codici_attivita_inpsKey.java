package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Codici_attivita_inpsKey extends OggettoBulk implements KeyedPersistent {
	// CD_ATTIVITA_INPS VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_attivita_inps;

public Codici_attivita_inpsKey() {
	super();
}
public Codici_attivita_inpsKey(java.lang.String cd_attivita_inps) {
	super();
	this.cd_attivita_inps = cd_attivita_inps;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Codici_attivita_inpsKey)) return false;
	Codici_attivita_inpsKey k = (Codici_attivita_inpsKey)o;
	if(!compareKey(getCd_attivita_inps(),k.getCd_attivita_inps())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_attivita_inps
 */
public java.lang.String getCd_attivita_inps() {
	return cd_attivita_inps;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_attivita_inps());
}
/* 
 * Setter dell'attributo cd_attivita_inps
 */
public void setCd_attivita_inps(java.lang.String cd_attivita_inps) {
	this.cd_attivita_inps = cd_attivita_inps;
}
}
