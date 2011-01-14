package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AnagraficoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

public AnagraficoKey() {
	super();
}
public AnagraficoKey(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof AnagraficoKey)) return false;
	AnagraficoKey k = (AnagraficoKey)o;
	if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_anag());
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
}
