package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_bolloKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_BOLLO VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_tipo_bollo;

public Tipo_bolloKey() {
	super();
}
public Tipo_bolloKey(java.lang.String cd_tipo_bollo) {
	super();
	this.cd_tipo_bollo = cd_tipo_bollo;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_bolloKey)) return false;
	Tipo_bolloKey k = (Tipo_bolloKey)o;
	if(!compareKey(getCd_tipo_bollo(),k.getCd_tipo_bollo())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_bolloKey)) return false;
	Tipo_bolloKey k = (Tipo_bolloKey)o;
	if(!compareKey(getCd_tipo_bollo(),k.getCd_tipo_bollo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_bollo
 */
public java.lang.String getCd_tipo_bollo() {
	return cd_tipo_bollo;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_tipo_bollo());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_bollo());
}
/* 
 * Setter dell'attributo cd_tipo_bollo
 */
public void setCd_tipo_bollo(java.lang.String cd_tipo_bollo) {
	this.cd_tipo_bollo = cd_tipo_bollo;
}
}
