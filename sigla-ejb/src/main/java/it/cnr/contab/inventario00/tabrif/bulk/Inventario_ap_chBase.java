package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_ap_chBase extends Inventario_ap_chKey implements Keyed {
	// DT_CHIUSURA TIMESTAMP
	private java.sql.Timestamp dt_chiusura;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

public Inventario_ap_chBase() {
	super();
}
public Inventario_ap_chBase(java.sql.Timestamp dt_apertura,java.lang.Integer esercizio,java.lang.Long pg_inventario) {
	super(dt_apertura,esercizio,pg_inventario);
}
/* 
 * Getter dell'attributo dt_chiusura
 */
public java.sql.Timestamp getDt_chiusura() {
	return dt_chiusura;
}
/* 
 * Getter dell'attributo stato
 */
public java.lang.String getStato() {
	return stato;
}
/* 
 * Setter dell'attributo dt_chiusura
 */
public void setDt_chiusura(java.sql.Timestamp dt_chiusura) {
	this.dt_chiusura = dt_chiusura;
}
/* 
 * Setter dell'attributo stato
 */
public void setStato(java.lang.String stato) {
	this.stato = stato;
}
}
