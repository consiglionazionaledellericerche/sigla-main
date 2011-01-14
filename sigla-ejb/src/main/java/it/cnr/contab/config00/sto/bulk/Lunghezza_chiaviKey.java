package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lunghezza_chiaviKey extends OggettoBulk implements KeyedPersistent {
	// ATTRIBUTO VARCHAR(30) NOT NULL (PK)
	private java.lang.String attributo;

	// LIVELLO DECIMAL(2,0) NOT NULL (PK)
	private java.lang.Integer livello;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TABELLA VARCHAR(30) NOT NULL (PK)
	private java.lang.String tabella;

public Lunghezza_chiaviKey() {
	super();
}
public Lunghezza_chiaviKey(java.lang.String attributo,java.lang.Integer esercizio,java.lang.Integer livello,java.lang.String tabella) {
	super();
	this.attributo = attributo;
	this.esercizio = esercizio;
	this.livello = livello;
	this.tabella = tabella;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Lunghezza_chiaviKey)) return false;
	Lunghezza_chiaviKey k = (Lunghezza_chiaviKey)o;
	if(!compareKey(getAttributo(),k.getAttributo())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getLivello(),k.getLivello())) return false;
	if(!compareKey(getTabella(),k.getTabella())) return false;
	return true;
}
/* 
 * Getter dell'attributo attributo
 */
public java.lang.String getAttributo() {
	return attributo;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Getter dell'attributo tabella
 */
public java.lang.String getTabella() {
	return tabella;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getAttributo())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getLivello())+
		calculateKeyHashCode(getTabella());
}
/* 
 * Setter dell'attributo attributo
 */
public void setAttributo(java.lang.String attributo) {
	this.attributo = attributo;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo livello
 */
public void setLivello(java.lang.Integer livello) {
	this.livello = livello;
}
/* 
 * Setter dell'attributo tabella
 */
public void setTabella(java.lang.String tabella) {
	this.tabella = tabella;
}
}
