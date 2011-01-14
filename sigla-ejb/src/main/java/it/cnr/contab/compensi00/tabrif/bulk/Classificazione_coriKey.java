package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_coriKey extends OggettoBulk implements KeyedPersistent {
	// CD_CLASSIFICAZIONE_CORI CHAR(2) NOT NULL (PK)
	private java.lang.String cd_classificazione_cori;

public Classificazione_coriKey() {
	super();
}
public Classificazione_coriKey(java.lang.String cd_classificazione_cori) {
	super();
	this.cd_classificazione_cori = cd_classificazione_cori;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Classificazione_coriKey)) return false;
	Classificazione_coriKey k = (Classificazione_coriKey)o;
	if(!compareKey(getCd_classificazione_cori(),k.getCd_classificazione_cori())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_classificazione_cori
 */
public java.lang.String getCd_classificazione_cori() {
	return cd_classificazione_cori;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_classificazione_cori());
}
/* 
 * Setter dell'attributo cd_classificazione_cori
 */
public void setCd_classificazione_cori(java.lang.String cd_classificazione_cori) {
	this.cd_classificazione_cori = cd_classificazione_cori;
}
}
