package it.cnr.contab.doccont00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_contBase extends Numerazione_doc_contKey implements Keyed {
	// CORRENTE DECIMAL(10,0) NOT NULL
	private java.lang.Long corrente;

	// PRIMO DECIMAL(10,0) NOT NULL
	private java.lang.Long primo;

	// ULTIMO DECIMAL(10,0) NOT NULL
	private java.lang.Long ultimo;

public Numerazione_doc_contBase() {
	super();
}
public Numerazione_doc_contBase(java.lang.String cd_cds,java.lang.String cd_tipo_documento_cont,java.lang.Integer esercizio) {
	super(cd_cds,cd_tipo_documento_cont,esercizio);
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
