package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Insieme_laKey extends OggettoBulk implements KeyedPersistent {
	// CD_INSIEME_LA VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_insieme_la;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_centro_responsabilita;

public Insieme_laKey() {
	super();
}
public Insieme_laKey(java.lang.String cd_centro_responsabilita,java.lang.String cd_insieme_la) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_insieme_la = cd_insieme_la;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Insieme_laKey)) return false;
	Insieme_laKey k = (Insieme_laKey)o;
	if(!compareKey(getCd_centro_responsabilita(),k.getCd_centro_responsabilita())) return false;
	if(!compareKey(getCd_insieme_la(),k.getCd_insieme_la())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/* 
 * Getter dell'attributo cd_insieme_la
 */
public java.lang.String getCd_insieme_la() {
	return cd_insieme_la;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_centro_responsabilita())+
		calculateKeyHashCode(getCd_insieme_la());
}
/* 
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
}
/* 
 * Setter dell'attributo cd_insieme_la
 */
public void setCd_insieme_la(java.lang.String cd_insieme_la) {
	this.cd_insieme_la = cd_insieme_la;
}
}
