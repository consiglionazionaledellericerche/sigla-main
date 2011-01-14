package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ProgettoKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO NUMBER(4) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_PROGETTO NUMBER(10) NOT NULL (PK)
	private java.lang.Integer pg_progetto;

	// TIPO_FASE VARCHAR(1) NOT NULL (PK)
	private java.lang.String tipo_fase;
	
public ProgettoKey() {
	super();
}
public ProgettoKey(java.lang.Integer esercizio,java.lang.Integer pg_progetto,java.lang.String tipo_fase) {
	super();
	this.esercizio = esercizio;
	this.pg_progetto = pg_progetto;
	this.tipo_fase = tipo_fase;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof ProgettoKey)) return false;
	ProgettoKey k = (ProgettoKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_progetto(),k.getPg_progetto())) return false;
	if(!compareKey(getTipo_fase(),k.getTipo_fase())) return false;
	return true;
}
/* 
 * Getter dell'attributo pg_progetto
 */
public java.lang.Integer getPg_progetto() {
	return pg_progetto;
}
public int primaryKeyHashCode() {
	return	calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_progetto())+
			calculateKeyHashCode(getTipo_fase());
}
/* 
 * Setter dell'attributo pg_progetto
 */
public void setPg_progetto(java.lang.Integer pg_progetto) {
	this.pg_progetto = pg_progetto;
}
public void setPg_progetto(java.math.BigDecimal pg_progetto) {
	this.pg_progetto = new java.lang.Integer(pg_progetto.intValue());
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.String getTipo_fase() {
	return tipo_fase;
}
public void setTipo_fase(java.lang.String tipo_fase) {
	this.tipo_fase = tipo_fase;
}
}