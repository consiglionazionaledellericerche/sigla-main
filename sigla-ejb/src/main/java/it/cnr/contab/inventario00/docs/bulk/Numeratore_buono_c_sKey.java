package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numeratore_buono_c_sKey extends OggettoBulk implements KeyedPersistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// TI_CARICO_SCARICO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_carico_scarico;

	// PG_INVENTARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_inventario;

public Numeratore_buono_c_sKey() {
	super();
}
public Numeratore_buono_c_sKey(java.lang.Integer esercizio,java.lang.Long pg_inventario,java.lang.String ti_carico_scarico) {
	super();
	this.esercizio = esercizio;
	this.pg_inventario = pg_inventario;
	this.ti_carico_scarico = ti_carico_scarico;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Numeratore_buono_c_sKey)) return false;
	Numeratore_buono_c_sKey k = (Numeratore_buono_c_sKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	if(!compareKey(getTi_carico_scarico(),k.getTi_carico_scarico())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Numeratore_buono_c_sKey)) return false;
	Numeratore_buono_c_sKey k = (Numeratore_buono_c_sKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	if(!compareKey(getTi_carico_scarico(),k.getTi_carico_scarico())) return false;
	return true;
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
/* 
 * Getter dell'attributo ti_carico_scarico
 */
public java.lang.String getTi_carico_scarico() {
	return ti_carico_scarico;
}
public int hashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_inventario())+
		calculateKeyHashCode(getTi_carico_scarico());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getPg_inventario())+
		calculateKeyHashCode(getTi_carico_scarico());
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
/* 
 * Setter dell'attributo ti_carico_scarico
 */
public void setTi_carico_scarico(java.lang.String ti_carico_scarico) {
	this.ti_carico_scarico = ti_carico_scarico;
}
}
