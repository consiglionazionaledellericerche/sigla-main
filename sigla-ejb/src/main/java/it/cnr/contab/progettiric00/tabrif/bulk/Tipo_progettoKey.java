package it.cnr.contab.progettiric00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_progettoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_PROGETTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_progetto;

public Tipo_progettoKey() {
	super();
}
public Tipo_progettoKey(java.lang.String cd_tipo_progetto) {
	super();
	this.cd_tipo_progetto = cd_tipo_progetto;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_progettoKey)) return false;
	Tipo_progettoKey k = (Tipo_progettoKey)o;
	if(!compareKey(getCd_tipo_progetto(),k.getCd_tipo_progetto())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_progetto
 */
public java.lang.String getCd_tipo_progetto() {
	return cd_tipo_progetto;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_progetto());
}
/* 
 * Setter dell'attributo cd_tipo_progetto
 */
public void setCd_tipo_progetto(java.lang.String cd_tipo_progetto) {
	this.cd_tipo_progetto = cd_tipo_progetto;
}
}
