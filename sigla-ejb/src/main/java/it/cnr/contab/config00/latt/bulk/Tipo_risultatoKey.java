package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_risultatoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_RISULTATO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_risultato;

public Tipo_risultatoKey() {
	super();
}
public Tipo_risultatoKey(java.lang.String cd_tipo_risultato) {
	super();
	this.cd_tipo_risultato = cd_tipo_risultato;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_risultatoKey)) return false;
	Tipo_risultatoKey k = (Tipo_risultatoKey)o;
	if(!compareKey(getCd_tipo_risultato(),k.getCd_tipo_risultato())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_risultato
 */
public java.lang.String getCd_tipo_risultato() {
	return cd_tipo_risultato;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_risultato());
}
/* 
 * Setter dell'attributo cd_tipo_risultato
 */
public void setCd_tipo_risultato(java.lang.String cd_tipo_risultato) {
	this.cd_tipo_risultato = cd_tipo_risultato;
}
}
