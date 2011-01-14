package it.cnr.contab.messaggio00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Messaggio_notificatoBase extends Messaggio_notificatoKey implements Keyed {
	// PG_MESSAGGIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_messaggio;

public Messaggio_notificatoBase() {
	super();
}
public Messaggio_notificatoBase(java.lang.String cd_utente) {
	super(cd_utente);
}
/* 
 * Getter dell'attributo pg_messaggio
 */
public java.lang.Long getPg_messaggio() {
	return pg_messaggio;
}
/* 
 * Setter dell'attributo pg_messaggio
 */
public void setPg_messaggio(java.lang.Long pg_messaggio) {
	this.pg_messaggio = pg_messaggio;
}
}
