package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProvinciaKey extends OggettoBulk implements KeyedPersistent {
	// CD_PROVINCIA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_provincia;

public ProvinciaKey() {
	super();
}
public ProvinciaKey(java.lang.String cd_provincia) {
	super();
	this.cd_provincia = cd_provincia;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ProvinciaKey)) return false;
	ProvinciaKey k = (ProvinciaKey)o;
	if(!compareKey(getCd_provincia(),k.getCd_provincia())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_provincia
 */
public java.lang.String getCd_provincia() {
	return cd_provincia;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_provincia());
}
/* 
 * Setter dell'attributo cd_provincia
 */
public void setCd_provincia(java.lang.String cd_provincia) {
	this.cd_provincia = cd_provincia;
}
}
