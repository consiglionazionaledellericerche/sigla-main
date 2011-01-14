package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_uoKey extends OggettoBulk implements KeyedPersistent {
	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

	// PG_PROGETTO NUMBER(10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

public Progetto_uoKey() {
	super();
}
public Progetto_uoKey(java.lang.Integer pg_progetto,java.lang.String cd_unita_organizzativa) {
	super();
	this.pg_progetto = pg_progetto;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Progetto_uoKey)) return false;
	Progetto_uoKey k = (Progetto_uoKey)o;
	if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_progetto())+
		calculateKeyHashCode(getCd_unita_organizzativa());
}
/* 
 * Setter dell'attributo pg_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
}