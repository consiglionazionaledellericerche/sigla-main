package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_movimento_inventarioKey extends OggettoBulk {
	// CD_TIPO_MOVIMENTO_INVENTARIO VARCHAR(5) NOT NULL (PK)
	private java.lang.String cd_tipo_movimento_inventario;

public Tipo_movimento_inventarioKey() {
	super();
}
public Tipo_movimento_inventarioKey(java.lang.String cd_tipo_movimento_inventario) {
	super();
	this.cd_tipo_movimento_inventario = cd_tipo_movimento_inventario;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_movimento_inventarioKey)) return false;
	Tipo_movimento_inventarioKey k = (Tipo_movimento_inventarioKey)o;
	if(!compareKey(getCd_tipo_movimento_inventario(),k.getCd_tipo_movimento_inventario())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_movimento_inventario
 */
public java.lang.String getCd_tipo_movimento_inventario() {
	return cd_tipo_movimento_inventario;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_tipo_movimento_inventario());
}
/* 
 * Setter dell'attributo cd_tipo_movimento_inventario
 */
public void setCd_tipo_movimento_inventario(java.lang.String cd_tipo_movimento_inventario) {
	this.cd_tipo_movimento_inventario = cd_tipo_movimento_inventario;
}
}
