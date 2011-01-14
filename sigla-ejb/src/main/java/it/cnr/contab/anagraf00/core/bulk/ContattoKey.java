package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ContattoKey extends OggettoBulk implements KeyedPersistent {
	// PG_CONTATTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_contatto;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

public ContattoKey() {
	super();
}
public ContattoKey(java.lang.Integer cd_terzo,java.lang.Long pg_contatto) {
	super();
	this.cd_terzo = cd_terzo;
	this.pg_contatto = pg_contatto;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ContattoKey)) return false;
	ContattoKey k = (ContattoKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getPg_contatto(),k.getPg_contatto())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo pg_contatto
 */
public java.lang.Long getPg_contatto() {
	return pg_contatto;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getPg_contatto());
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo pg_contatto
 */
public void setPg_contatto(java.lang.Long pg_contatto) {
	this.pg_contatto = pg_contatto;
}
}
