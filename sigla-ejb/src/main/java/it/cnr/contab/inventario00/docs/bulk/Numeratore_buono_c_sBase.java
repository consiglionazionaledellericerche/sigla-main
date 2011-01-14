package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numeratore_buono_c_sBase extends Numeratore_buono_c_sKey implements Keyed {
	// CORRENTE DECIMAL(10,0) NOT NULL
	private java.lang.Long corrente;

	// INIZIALE DECIMAL(10,0) NOT NULL
	private java.lang.Long iniziale;

public Numeratore_buono_c_sBase() {
	super();
}
public Numeratore_buono_c_sBase(java.lang.Integer esercizio,java.lang.Long pg_inventario,java.lang.String ti_carico_scarico) {
	super(esercizio,pg_inventario,ti_carico_scarico);
}
/* 
 * Getter dell'attributo corrente
 */
public java.lang.Long getCorrente() {
	return corrente;
}
/* 
 * Getter dell'attributo iniziale
 */
public java.lang.Long getIniziale() {
	return iniziale;
}
/* 
 * Setter dell'attributo corrente
 */
public void setCorrente(java.lang.Long corrente) {
	this.corrente = corrente;
}
/* 
 * Setter dell'attributo iniziale
 */
public void setIniziale(java.lang.Long iniziale) {
	this.iniziale = iniziale;
}
}
