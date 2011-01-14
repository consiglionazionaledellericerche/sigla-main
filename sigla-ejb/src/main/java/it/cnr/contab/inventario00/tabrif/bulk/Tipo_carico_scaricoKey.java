package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_carico_scaricoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_CARICO_SCARICO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_carico_scarico;

public Tipo_carico_scaricoKey() {
	super();
}
public Tipo_carico_scaricoKey(java.lang.String cd_tipo_carico_scarico) {
	super();
	this.cd_tipo_carico_scarico = cd_tipo_carico_scarico;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_carico_scaricoKey)) return false;
	Tipo_carico_scaricoKey k = (Tipo_carico_scaricoKey)o;
	if(!compareKey(getCd_tipo_carico_scarico(),k.getCd_tipo_carico_scarico())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_carico_scaricoKey)) return false;
	Tipo_carico_scaricoKey k = (Tipo_carico_scaricoKey)o;
	if(!compareKey(getCd_tipo_carico_scarico(),k.getCd_tipo_carico_scarico())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_carico_scarico
 */
public java.lang.String getCd_tipo_carico_scarico() {
	return cd_tipo_carico_scarico;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_tipo_carico_scarico());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_carico_scarico());
}
/* 
 * Setter dell'attributo cd_tipo_carico_scarico
 */
public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico) {
	this.cd_tipo_carico_scarico = cd_tipo_carico_scarico;
}
}
