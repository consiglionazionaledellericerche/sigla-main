package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class TelefonoKey extends OggettoBulk implements KeyedPersistent {
	// PG_RIFERIMENTO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_riferimento;

	// CD_TERZO DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_terzo;

public TelefonoKey() {
	super();
}
public TelefonoKey(java.lang.Integer cd_terzo,java.lang.Long pg_riferimento) {
	super();
	this.cd_terzo = cd_terzo;
	this.pg_riferimento = pg_riferimento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof TelefonoKey)) return false;
	TelefonoKey k = (TelefonoKey)o;
	if(!compareKey(getCd_terzo(),k.getCd_terzo())) return false;
	if(!compareKey(getPg_riferimento(),k.getPg_riferimento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_terzo
 */
public java.lang.Integer getCd_terzo() {
	return cd_terzo;
}
/* 
 * Getter dell'attributo pg_riferimento
 */
public java.lang.Long getPg_riferimento() {
	return pg_riferimento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_terzo())+
		calculateKeyHashCode(getPg_riferimento());
}
/* 
 * Setter dell'attributo cd_terzo
 */
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.cd_terzo = cd_terzo;
}
/* 
 * Setter dell'attributo pg_riferimento
 */
public void setPg_riferimento(java.lang.Long pg_riferimento) {
	this.pg_riferimento = pg_riferimento;
}
}
