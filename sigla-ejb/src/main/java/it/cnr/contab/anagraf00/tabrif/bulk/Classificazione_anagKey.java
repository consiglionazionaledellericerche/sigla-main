package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_anagKey extends OggettoBulk implements KeyedPersistent {
	// CD_CLASSIFIC_ANAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_classific_anag;

public Classificazione_anagKey() {
	super();
}
public Classificazione_anagKey(java.lang.String cd_classific_anag) {
	super();
	this.cd_classific_anag = cd_classific_anag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Classificazione_anagKey)) return false;
	Classificazione_anagKey k = (Classificazione_anagKey)o;
	if(!compareKey(getCd_classific_anag(),k.getCd_classific_anag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_classific_anag
 */
public java.lang.String getCd_classific_anag() {
	return cd_classific_anag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_classific_anag());
}
/* 
 * Setter dell'attributo cd_classific_anag
 */
public void setCd_classific_anag(java.lang.String cd_classific_anag) {
	this.cd_classific_anag = cd_classific_anag;
}
}
