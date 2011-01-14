package it.cnr.contab.config00.tabnum.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_baseKey extends OggettoBulk implements KeyedPersistent {
	// COLONNA VARCHAR(35) NOT NULL (PK)
	private java.lang.String colonna;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TABELLA VARCHAR(35) NOT NULL (PK)
	private java.lang.String tabella;

public Numerazione_baseKey() {
	super();
}
public Numerazione_baseKey(java.lang.String colonna,java.lang.Integer esercizio,java.lang.String tabella) {
	super();
	this.colonna = colonna;
	this.esercizio = esercizio;
	this.tabella = tabella;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Numerazione_baseKey)) return false;
	Numerazione_baseKey k = (Numerazione_baseKey)o;
	if(!compareKey(getColonna(),k.getColonna())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getTabella(),k.getTabella())) return false;
	return true;
}
/* 
 * Getter dell'attributo colonna
 */
public java.lang.String getColonna() {
	return colonna;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo tabella
 */
public java.lang.String getTabella() {
	return tabella;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getColonna())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getTabella());
}
/* 
 * Setter dell'attributo colonna
 */
public void setColonna(java.lang.String colonna) {
	this.colonna = colonna;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo tabella
 */
public void setTabella(java.lang.String tabella) {
	this.tabella = tabella;
}
}
