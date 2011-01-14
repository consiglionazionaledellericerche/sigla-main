package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RegioneKey extends OggettoBulk implements KeyedPersistent {
	// CD_REGIONE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_regione;

public RegioneKey() {
	super();
}
public RegioneKey(java.lang.String cd_regione) {
	super();
	this.cd_regione = cd_regione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof RegioneKey)) return false;
	RegioneKey k = (RegioneKey)o;
	if(!compareKey(getCd_regione(),k.getCd_regione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_regione
 */
public java.lang.String getCd_regione() {
	return cd_regione;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_regione());
}
/* 
 * Setter dell'attributo cd_regione
 */
public void setCd_regione(java.lang.String cd_regione) {
	this.cd_regione = cd_regione;
}
}
