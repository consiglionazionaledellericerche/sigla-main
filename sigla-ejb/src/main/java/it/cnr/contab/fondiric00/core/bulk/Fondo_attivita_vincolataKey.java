package it.cnr.contab.fondiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fondo_attivita_vincolataKey extends OggettoBulk implements KeyedPersistent {
	// CD_FONDO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_fondo;

public Fondo_attivita_vincolataKey() {
	super();
}
public Fondo_attivita_vincolataKey(java.lang.String cd_fondo) {
	super();
	this.cd_fondo = cd_fondo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Fondo_attivita_vincolataKey)) return false;
	Fondo_attivita_vincolataKey k = (Fondo_attivita_vincolataKey)o;
	if(!compareKey(getCd_fondo(),k.getCd_fondo())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_fondo
 */
public java.lang.String getCd_fondo() {
	return cd_fondo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_fondo());
}
/* 
 * Setter dell'attributo cd_fondo
 */
public void setCd_fondo(java.lang.String cd_fondo) {
	this.cd_fondo = cd_fondo;
}
}
