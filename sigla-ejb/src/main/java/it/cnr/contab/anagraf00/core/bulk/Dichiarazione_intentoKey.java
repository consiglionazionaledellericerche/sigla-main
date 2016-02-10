package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_ANAG DECIMAL(8,0) NOT NULL (PK)
	private java.lang.Integer cd_anag;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;
	
	private java.lang.Integer progr;

public Dichiarazione_intentoKey() {
	super();
}
public Dichiarazione_intentoKey(java.lang.Integer cd_anag,java.lang.Integer esercizio,java.lang.Integer progr) {
	super();
	this.cd_anag = cd_anag;
	this.esercizio = esercizio;
	this.progr = progr;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Dichiarazione_intentoKey)) return false;
	Dichiarazione_intentoKey k = (Dichiarazione_intentoKey)o;
	if(!compareKey(getCd_anag(),k.getCd_anag())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getProgr(),k.getProgr())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_anag
 */
public java.lang.Integer getCd_anag() {
	return cd_anag;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_anag())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getProgr());
}
/* 
 * Setter dell'attributo cd_anag
 */
public void setCd_anag(java.lang.Integer cd_anag) {
	this.cd_anag = cd_anag;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.Integer getProgr() {
	return progr;
}
public void setProgr(java.lang.Integer progr) {
	this.progr = progr;
}

}
