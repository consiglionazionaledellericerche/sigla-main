package it.cnr.contab.doccont00.intcass.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ext_cassiere00Key extends OggettoBulk implements KeyedPersistent {
	// NOME_FILE VARCHAR(20) NOT NULL (PK)
	private java.lang.String nome_file;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_REC DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_rec;

public Ext_cassiere00Key() {
	super();
}
public Ext_cassiere00Key(java.lang.Integer esercizio,java.lang.String nome_file,java.lang.Long pg_rec) {
	super();
	this.esercizio = esercizio;
	this.nome_file = nome_file;
	this.pg_rec = pg_rec;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ext_cassiere00Key)) return false;
	Ext_cassiere00Key k = (Ext_cassiere00Key)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getNome_file(),k.getNome_file())) return false;
	if(!compareKey(getPg_rec(),k.getPg_rec())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo nome_file
 */
public java.lang.String getNome_file() {
	return nome_file;
}
/* 
 * Getter dell'attributo pg_rec
 */
public java.lang.Long getPg_rec() {
	return pg_rec;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getNome_file())+
		calculateKeyHashCode(getPg_rec());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo nome_file
 */
public void setNome_file(java.lang.String nome_file) {
	this.nome_file = nome_file;
}
/* 
 * Setter dell'attributo pg_rec
 */
public void setPg_rec(java.lang.Long pg_rec) {
	this.pg_rec = pg_rec;
}
}
