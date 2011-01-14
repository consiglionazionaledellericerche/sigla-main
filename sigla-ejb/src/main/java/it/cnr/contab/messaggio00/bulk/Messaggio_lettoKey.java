package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_lettoKey extends OggettoBulk implements KeyedPersistent {
	// PG_MESSAGGIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_messaggio;

	// CD_UTENTE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_utente;

public Messaggio_lettoKey() {
	super();
}
public Messaggio_lettoKey(java.lang.String cd_utente,java.lang.Long pg_messaggio) {
	super();
	this.cd_utente = cd_utente;
	this.pg_messaggio = pg_messaggio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Messaggio_lettoKey)) return false;
	Messaggio_lettoKey k = (Messaggio_lettoKey)o;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	if(!compareKey(getPg_messaggio(),k.getPg_messaggio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_utente
 */
public java.lang.String getCd_utente() {
	return cd_utente;
}
/* 
 * Getter dell'attributo pg_messaggio
 */
public java.lang.Long getPg_messaggio() {
	return pg_messaggio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_utente())+
		calculateKeyHashCode(getPg_messaggio());
}
/* 
 * Setter dell'attributo cd_utente
 */
public void setCd_utente(java.lang.String cd_utente) {
	this.cd_utente = cd_utente;
}
/* 
 * Setter dell'attributo pg_messaggio
 */
public void setPg_messaggio(java.lang.Long pg_messaggio) {
	this.pg_messaggio = pg_messaggio;
}
}
