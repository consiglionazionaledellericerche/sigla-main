package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_ap_chKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// DT_APERTURA TIMESTAMP NOT NULL (PK)
	private java.sql.Timestamp dt_apertura;

	// PG_INVENTARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_inventario;

public Inventario_ap_chKey() {
	super();
}
public Inventario_ap_chKey(java.sql.Timestamp dt_apertura,java.lang.Integer esercizio,java.lang.Long pg_inventario) {
	super();
	this.dt_apertura = dt_apertura;
	this.esercizio = esercizio;
	this.pg_inventario = pg_inventario;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Inventario_ap_chKey)) return false;
	Inventario_ap_chKey k = (Inventario_ap_chKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Inventario_ap_chKey)) return false;
	Inventario_ap_chKey k = (Inventario_ap_chKey)o;
	if(!compareKey(getDt_apertura(),k.getDt_apertura())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	return true;
}
/* 
 * Getter dell'attributo dt_apertura
 */
public java.sql.Timestamp getDt_apertura() {
	return dt_apertura;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo pg_inventario
 */
public java.lang.Long getPg_inventario() {
	return pg_inventario;
}
public int hashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_inventario());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getDt_apertura())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_inventario());
}
/* 
 * Setter dell'attributo dt_apertura
 */
public void setDt_apertura(java.sql.Timestamp dt_apertura) {
	this.dt_apertura = dt_apertura;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo pg_inventario
 */
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.pg_inventario = pg_inventario;
}
}
