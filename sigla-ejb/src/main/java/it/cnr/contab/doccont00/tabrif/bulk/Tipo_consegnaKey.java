package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_consegnaKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_CONSEGNA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_consegna;

public Tipo_consegnaKey() {
	super();
}
public Tipo_consegnaKey(java.lang.String cd_tipo_consegna) {
	super();
	this.cd_tipo_consegna = cd_tipo_consegna;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_consegnaKey)) return false;
	Tipo_consegnaKey k = (Tipo_consegnaKey)o;
	if(!compareKey(getCd_tipo_consegna(),k.getCd_tipo_consegna())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_consegna
 */
public java.lang.String getCd_tipo_consegna() {
	return cd_tipo_consegna;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_consegna());
}
/* 
 * Setter dell'attributo cd_tipo_consegna
 */
public void setCd_tipo_consegna(java.lang.String cd_tipo_consegna) {
	this.cd_tipo_consegna = cd_tipo_consegna;
}
}
