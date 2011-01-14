package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AccessoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ACCESSO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_accesso;

public AccessoKey() {
	super();
}
public AccessoKey(java.lang.String cd_accesso) {
	super();
	this.cd_accesso = cd_accesso;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof AccessoKey)) return false;
	AccessoKey k = (AccessoKey)o;
	if(!compareKey(getCd_accesso(),k.getCd_accesso())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_accesso
 */
public java.lang.String getCd_accesso() {
	return cd_accesso;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_accesso());
}
/* 
 * Setter dell'attributo cd_accesso
 */
public void setCd_accesso(java.lang.String cd_accesso) {
	this.cd_accesso = cd_accesso;
}
}
