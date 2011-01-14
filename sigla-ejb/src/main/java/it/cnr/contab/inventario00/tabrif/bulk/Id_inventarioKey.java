package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Id_inventarioKey extends OggettoBulk implements KeyedPersistent {
	// PG_INVENTARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_inventario;

public Id_inventarioKey() {
	super();
}
public Id_inventarioKey(java.lang.Long pg_inventario) {
	super();
	this.pg_inventario = pg_inventario;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Id_inventarioKey)) return false;
	Id_inventarioKey k = (Id_inventarioKey)o;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Id_inventarioKey)) return false;
	Id_inventarioKey k = (Id_inventarioKey)o;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_inventario
 */
public java.lang.Long getPg_inventario() {
	return pg_inventario;
}
public int hashCode() {
	return
		calculateKeyHashCode(getPg_inventario());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_inventario());
}
/* 
 * Setter dell'attributo pg_inventario
 */
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.pg_inventario = pg_inventario;
}
}
