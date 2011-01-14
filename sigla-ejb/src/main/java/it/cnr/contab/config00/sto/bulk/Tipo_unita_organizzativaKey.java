package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_unita_organizzativaKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_UNITA VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_tipo_unita;

public Tipo_unita_organizzativaKey() {
	super();
}
public Tipo_unita_organizzativaKey(java.lang.String cd_tipo_unita) {
	super();
	this.cd_tipo_unita = cd_tipo_unita;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_unita_organizzativaKey)) return false;
	Tipo_unita_organizzativaKey k = (Tipo_unita_organizzativaKey)o;
	if(!compareKey(getCd_tipo_unita(),k.getCd_tipo_unita())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_unita
 */
public java.lang.String getCd_tipo_unita() {
	return cd_tipo_unita;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_unita());
}
/* 
 * Setter dell'attributo cd_tipo_unita
 */
public void setCd_tipo_unita(java.lang.String cd_tipo_unita) {
	this.cd_tipo_unita = cd_tipo_unita;
}
}
