package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Stipendi_cofiKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// MESE DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer mese;

public Stipendi_cofiKey() {
	super();
}
public Stipendi_cofiKey(java.lang.Integer esercizio,java.lang.Integer mese) {
	super();
	this.esercizio = esercizio;
	this.mese = mese;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Stipendi_cofiKey)) return false;
	Stipendi_cofiKey k = (Stipendi_cofiKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getMese(),k.getMese())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getMese());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
}
