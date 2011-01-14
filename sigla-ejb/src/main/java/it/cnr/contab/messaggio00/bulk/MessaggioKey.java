package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MessaggioKey extends OggettoBulk implements KeyedPersistent {
	// PG_MESSAGGIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_messaggio;

public MessaggioKey() {
	super();
}
public MessaggioKey(java.lang.Long pg_messaggio) {
	super();
	this.pg_messaggio = pg_messaggio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof MessaggioKey)) return false;
	MessaggioKey k = (MessaggioKey)o;
	if(!compareKey(getPg_messaggio(),k.getPg_messaggio())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_messaggio
 */
public java.lang.Long getPg_messaggio() {
	return pg_messaggio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_messaggio());
}
/* 
 * Setter dell'attributo pg_messaggio
 */
public void setPg_messaggio(java.lang.Long pg_messaggio) {
	this.pg_messaggio = pg_messaggio;
}
}
