package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Capoconto_finKey extends OggettoBulk implements KeyedPersistent {
	// CD_CAPOCONTO_FIN VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_capoconto_fin;

public Capoconto_finKey() {
	super();
}
public Capoconto_finKey(java.lang.String cd_capoconto_fin) {
	super();
	this.cd_capoconto_fin = cd_capoconto_fin;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Capoconto_finKey)) return false;
	Capoconto_finKey k = (Capoconto_finKey)o;
	if(!compareKey(getCd_capoconto_fin(),k.getCd_capoconto_fin())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_capoconto_fin
 */
public java.lang.String getCd_capoconto_fin() {
	return cd_capoconto_fin;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_capoconto_fin());
}
/* 
 * Setter dell'attributo cd_capoconto_fin
 */
public void setCd_capoconto_fin(java.lang.String cd_capoconto_fin) {
	this.cd_capoconto_fin = cd_capoconto_fin;
}
}
