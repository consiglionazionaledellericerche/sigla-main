package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class SezionaleBase extends SezionaleKey implements KeyedPersistent {
	// CORRENTE DECIMAL(10,0) NOT NULL
	protected java.lang.Long corrente;

	// PRIMO DECIMAL(10,0) NOT NULL
	protected java.lang.Long primo;

	// ULTIMO DECIMAL(10,0) NOT NULL
	protected java.lang.Long ultimo;

public SezionaleBase() {
	super();
}
public SezionaleBase(java.lang.String cd_cds,java.lang.String cd_tipo_sezionale,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.String ti_fattura) {
	super(cd_cds,cd_tipo_sezionale,cd_unita_organizzativa,esercizio,ti_fattura);
}
/* 
 * Getter dell'attributo corrente
 */
public java.lang.Long getCorrente() {
	return corrente;
}
/* 
 * Getter dell'attributo primo
 */
public java.lang.Long getPrimo() {
	return primo;
}
/* 
 * Getter dell'attributo ultimo
 */
public java.lang.Long getUltimo() {
	return ultimo;
}
/* 
 * Setter dell'attributo corrente
 */
public void setCorrente(java.lang.Long corrente) {
	this.corrente = corrente;
}
/* 
 * Setter dell'attributo primo
 */
public void setPrimo(java.lang.Long primo) {
	this.primo = primo;
}
/* 
 * Setter dell'attributo ultimo
 */
public void setUltimo(java.lang.Long ultimo) {
	this.ultimo = ultimo;
}
}
