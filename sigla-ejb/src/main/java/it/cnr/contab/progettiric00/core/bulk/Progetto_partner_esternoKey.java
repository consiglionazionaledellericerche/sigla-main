package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Progetto_partner_esternoKey extends OggettoBulk implements KeyedPersistent {
	// CD_PARTNER_ESTERNO   NUMBER (8)    NOT NULL (PK)
	private java.lang.Integer cd_partner_esterno;

	// PG_PROGETTO NUMBER (10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

public Progetto_partner_esternoKey() {
	super();
}
public Progetto_partner_esternoKey(java.lang.Integer pg_progetto,java.lang.Integer cd_partner_esterno) {
	super();
	this.pg_progetto = pg_progetto;
	this.cd_partner_esterno = cd_partner_esterno;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Progetto_partner_esternoKey)) return false;
	Progetto_partner_esternoKey k = (Progetto_partner_esternoKey)o;
	if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
	if(!compareKey(getCd_partner_esterno(),k.getCd_partner_esterno())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
/* 
 * Getter dell'attributo cd_partner_esterno
 */
public java.lang.Integer getCd_partner_esterno() {
	return cd_partner_esterno;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_progetto())+
		calculateKeyHashCode(getCd_partner_esterno());
}
/* 
 * Setter dell'attributo cd_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}
/* 
 * Setter dell'attributo cd_partner_esterno
 */
public void setCd_partner_esterno(java.lang.Integer cd_partner_esterno) {
	this.cd_partner_esterno = cd_partner_esterno;
}
}