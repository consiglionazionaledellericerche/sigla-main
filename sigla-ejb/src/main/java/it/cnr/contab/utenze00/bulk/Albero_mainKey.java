package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Albero_mainKey extends OggettoBulk implements KeyedPersistent {
	// CD_NODO VARCHAR(100) NOT NULL (PK)
	private java.lang.String cd_nodo;

public Albero_mainKey() {
	super();
}
public Albero_mainKey(java.lang.String cd_nodo) {
	super();
	this.cd_nodo = cd_nodo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Albero_mainKey)) return false;
	Albero_mainKey k = (Albero_mainKey)o;
	if(!compareKey(getCd_nodo(),k.getCd_nodo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_nodo
 */
public java.lang.String getCd_nodo() {
	return cd_nodo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_nodo());
}
/* 
 * Setter dell'attributo cd_nodo
 */
public void setCd_nodo(java.lang.String cd_nodo) {
	this.cd_nodo = cd_nodo;
}
}
