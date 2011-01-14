package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_crKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_GRUPPO_CR VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_gruppo_cr;

public Gruppo_crKey() {
	super();
}
public Gruppo_crKey(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr) {
	this.esercizio = esercizio;
	this.cd_gruppo_cr = cd_gruppo_cr;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Gruppo_crKey)) return false;
	Gruppo_crKey k = (Gruppo_crKey)o;
	if(!compareKey(getCd_gruppo_cr(),k.getCd_gruppo_cr())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_gruppo_cr
 */
public java.lang.String getCd_gruppo_cr() {
	return cd_gruppo_cr;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_gruppo_cr())+
		calculateKeyHashCode(getEsercizio());
}
/* 
 * Setter dell'attributo cd_gruppo_cr
 */
public void setCd_gruppo_cr(java.lang.String cd_gruppo_cr) {
	this.cd_gruppo_cr = cd_gruppo_cr;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
}
