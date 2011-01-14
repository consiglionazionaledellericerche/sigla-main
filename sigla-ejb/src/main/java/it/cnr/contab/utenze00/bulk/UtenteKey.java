package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class UtenteKey extends OggettoBulk implements KeyedPersistent {
	// CD_UTENTE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_utente;

public UtenteKey() {
	super();
}
public UtenteKey(java.lang.String cd_utente) {
	super();
	this.cd_utente = cd_utente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof UtenteKey)) return false;
	UtenteKey k = (UtenteKey)o;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_utente
 */
public java.lang.String getCd_utente() {
	return cd_utente;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_utente());
}
/* 
 * Setter dell'attributo cd_utente
 */
public void setCd_utente(java.lang.String cd_utente) {
	this.cd_utente = cd_utente;
}
}
