package it.cnr.contab.fondiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_fondoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_FONDO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_fondo;

public Tipo_fondoKey() {
	super();
}
public Tipo_fondoKey(java.lang.String cd_tipo_fondo) {
	super();
	this.cd_tipo_fondo = cd_tipo_fondo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_fondoKey)) return false;
	Tipo_fondoKey k = (Tipo_fondoKey)o;
	if(!compareKey(getCd_tipo_fondo(),k.getCd_tipo_fondo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_fondo
 */
public java.lang.String getCd_tipo_fondo() {
	return cd_tipo_fondo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_fondo());
}
/* 
 * Setter dell'attributo cd_tipo_fondo
 */
public void setCd_tipo_fondo(java.lang.String cd_tipo_fondo) {
	this.cd_tipo_fondo = cd_tipo_fondo;
}
}
