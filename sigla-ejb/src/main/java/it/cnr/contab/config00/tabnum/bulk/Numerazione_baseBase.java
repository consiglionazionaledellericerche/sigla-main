package it.cnr.contab.config00.tabnum.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_baseBase extends Numerazione_baseKey implements Keyed {
	// CD_CORRENTE VARCHAR(10)
	private java.lang.String cd_corrente;

	// CD_INIZIALE VARCHAR(10)
	private java.lang.String cd_iniziale;

	// CD_MASSIMO VARCHAR(10)
	private java.lang.String cd_massimo;

public Numerazione_baseBase() {
	super();
}
public Numerazione_baseBase(java.lang.String colonna,java.lang.Integer esercizio,java.lang.String tabella) {
	super(colonna,esercizio,tabella);
}
/* 
 * Getter dell'attributo cd_corrente
 */
public java.lang.String getCd_corrente() {
	return cd_corrente;
}
/* 
 * Getter dell'attributo cd_iniziale
 */
public java.lang.String getCd_iniziale() {
	return cd_iniziale;
}
/* 
 * Getter dell'attributo cd_massimo
 */
public java.lang.String getCd_massimo() {
	return cd_massimo;
}
/* 
 * Setter dell'attributo cd_corrente
 */
public void setCd_corrente(java.lang.String cd_corrente) {
	this.cd_corrente = cd_corrente;
}
/* 
 * Setter dell'attributo cd_iniziale
 */
public void setCd_iniziale(java.lang.String cd_iniziale) {
	this.cd_iniziale = cd_iniziale;
}
/* 
 * Setter dell'attributo cd_massimo
 */
public void setCd_massimo(java.lang.String cd_massimo) {
	this.cd_massimo = cd_massimo;
}
}
