package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Lunghezza_chiaviBase extends Lunghezza_chiaviKey implements Keyed {
	// LUNGHEZZA DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal lunghezza;

public Lunghezza_chiaviBase() {
	super();
}
public Lunghezza_chiaviBase(java.lang.String attributo,java.lang.Integer esercizio,java.lang.Integer livello,java.lang.String tabella) {
	super(attributo,esercizio,livello,tabella);
}
/* 
 * Getter dell'attributo lunghezza
 */
public java.math.BigDecimal getLunghezza() {
	return lunghezza;
}
/* 
 * Setter dell'attributo lunghezza
 */
public void setLunghezza(java.math.BigDecimal lunghezza) {
	this.lunghezza = lunghezza;
}
}
