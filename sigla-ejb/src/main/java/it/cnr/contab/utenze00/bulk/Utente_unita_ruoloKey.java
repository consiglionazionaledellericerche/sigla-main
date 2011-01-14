package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_ruoloKey extends OggettoBulk implements KeyedPersistent {
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_RUOLO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_ruolo;

	// CD_UTENTE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_utente;

public Utente_unita_ruoloKey() {
	super();
}
public Utente_unita_ruoloKey(java.lang.String cd_ruolo,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super();
	this.cd_ruolo = cd_ruolo;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.cd_utente = cd_utente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Utente_unita_ruoloKey)) return false;
	Utente_unita_ruoloKey k = (Utente_unita_ruoloKey)o;
	if(!compareKey(getCd_ruolo(),k.getCd_ruolo())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_ruolo
 */
public java.lang.String getCd_ruolo() {
	return cd_ruolo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_utente
 */
public java.lang.String getCd_utente() {
	return cd_utente;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_ruolo())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getCd_utente());
}
/* 
 * Setter dell'attributo cd_ruolo
 */
public void setCd_ruolo(java.lang.String cd_ruolo) {
	this.cd_ruolo = cd_ruolo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_utente
 */
public void setCd_utente(java.lang.String cd_utente) {
	this.cd_utente = cd_utente;
}
}
