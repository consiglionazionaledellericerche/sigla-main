package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Utente_unita_accessoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ACCESSO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_accesso;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// CD_UTENTE VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_utente;

public Utente_unita_accessoKey() {
	super();
}
public Utente_unita_accessoKey(java.lang.String cd_accesso,java.lang.String cd_unita_organizzativa,java.lang.String cd_utente) {
	super();
	this.cd_accesso = cd_accesso;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
	this.cd_utente = cd_utente;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Utente_unita_accessoKey)) return false;
	Utente_unita_accessoKey k = (Utente_unita_accessoKey)o;
	if(!compareKey(getCd_accesso(),k.getCd_accesso())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	if(!compareKey(getCd_utente(),k.getCd_utente())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_accesso
 */
public java.lang.String getCd_accesso() {
	return cd_accesso;
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
		calculateKeyHashCode(getCd_accesso())+
		calculateKeyHashCode(getCd_unita_organizzativa())+
		calculateKeyHashCode(getCd_utente());
}
/* 
 * Setter dell'attributo cd_accesso
 */
public void setCd_accesso(java.lang.String cd_accesso) {
	this.cd_accesso = cd_accesso;
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
