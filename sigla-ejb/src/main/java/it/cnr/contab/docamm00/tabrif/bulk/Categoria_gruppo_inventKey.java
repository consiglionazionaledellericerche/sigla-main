package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_gruppo_inventKey extends OggettoBulk implements KeyedPersistent {
	// CD_CATEGORIA_GRUPPO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_categoria_gruppo;

public Categoria_gruppo_inventKey() {
	super();
}
public Categoria_gruppo_inventKey(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Categoria_gruppo_inventKey)) return false;
	Categoria_gruppo_inventKey k = (Categoria_gruppo_inventKey)o;
	if(!compareKey(getCd_categoria_gruppo(),k.getCd_categoria_gruppo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_categoria_gruppo
 */
public java.lang.String getCd_categoria_gruppo() {
	return cd_categoria_gruppo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_categoria_gruppo());
}
/* 
 * Setter dell'attributo cd_categoria_gruppo
 */
public void setCd_categoria_gruppo(java.lang.String cd_categoria_gruppo) {
	this.cd_categoria_gruppo = cd_categoria_gruppo;
}
}
